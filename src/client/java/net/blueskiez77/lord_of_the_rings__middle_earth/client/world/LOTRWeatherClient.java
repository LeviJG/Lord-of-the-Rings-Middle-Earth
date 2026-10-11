package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDagorladBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradVolcanoBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMordorBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMorgulValeBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNurnBiome;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWeatherRenderer: in Middle-earth's rain and snow, with "New weather", the mod's own rain and
 * snow; and where it rains but the land does not take it, what falls instead -- ash over Mordor
 * (but not Nurn or the Morgul Vale), Dagorlad and the Far Harad volcano, and a sandstorm over dry
 * lands of sand or gravel -- save at Christmas, when it snows everywhere. Drawn as today's weather
 * draws its columns.
 */
public final class LOTRWeatherClient {

    public static final Identifier RAIN = Identifier.fromNamespaceAndPath("lotr", "weather/rain.png");
    public static final Identifier SNOW = Identifier.fromNamespaceAndPath("lotr", "weather/snow.png");
    private static final Identifier ASH = Identifier.fromNamespaceAndPath("lotr", "weather/ash.png");
    private static final Identifier SANDSTORM = Identifier.fromNamespaceAndPath("lotr", "weather/sandstorm.png");
    private static final float[] COLUMN_SIZE_X = new float[1024];
    private static final float[] COLUMN_SIZE_Z = new float[1024];

    static {
        for (int z = 0; z < 32; ++z) {
            for (int x = 0; x < 32; ++x) {
                float deltaX = x - 16;
                float deltaZ = z - 16;
                float distance = Mth.length(deltaX, deltaZ);
                COLUMN_SIZE_X[z * 32 + x] = -deltaZ / distance;
                COLUMN_SIZE_Z[z * 32 + x] = deltaX / distance;
            }
        }
    }

    private LOTRWeatherClient() {
    }

    /** Today's rain and snow texture, or the mod's in Middle-earth with "New weather". */
    public static Identifier texture(Identifier vanilla, boolean snow) {
        if (LOTRConfig.newWeather && LOTREnvironmentClient.isMiddleEarth(Minecraft.getInstance().level)) {
            return snow ? SNOW : RAIN;
        }
        return vanilla;
    }

    /** LOTRBiome.getEnableSnow at Christmas: snow, on the client, wherever it falls in Middle-earth. */
    public static boolean isChristmasSnow(ClientLevel level) {
        return LOTRMod.isChristmas() && LOTREnvironmentClient.isMiddleEarth(level);
    }

    /** isSandstormBiome: no rain, and sand or gravel underfoot (1.7.10's sand material). */
    public static boolean isSandstormBiome(LOTRBiome biome) {
        boolean canSpawnLightningBolt = !biome.enableSnow && biome.enableRain;
        return !canSpawnLightningBolt && (biome.topBlock.is(BlockTags.SAND) || biome.topBlock.is(Blocks.GRAVEL));
    }

    private static boolean isAshy(LOTRBiome biome) {
        return biome instanceof LOTRMordorBiome && !(biome instanceof LOTRNurnBiome) && !(biome instanceof LOTRMorgulValeBiome)
                || biome instanceof LOTRFarHaradVolcanoBiome || biome instanceof LOTRDagorladBiome;
    }

    /**
     * A column's ash or sandstorm, if its biome has one, added to the lists given; true if it did, so
     * that the game's own rain or snow is not drawn there too.
     */
    public static boolean addColumn(ClientLevel level, int x, int z, int cameraBlockY, int radius, float partialTicks,
                                    List<WeatherEffectRenderer.ColumnInstance> ashColumns, List<WeatherEffectRenderer.ColumnInstance> sandColumns) {
        if (!LOTREnvironmentClient.isMiddleEarth(level) || LOTRMod.isChristmas()) {
            return false;
        }
        LOTRBiome biome = LOTRBiomes.of(level.getBiome(new BlockPos(x, cameraBlockY, z)));
        if (biome == null) {
            return false;
        }
        boolean ashy = isAshy(biome);
        boolean sandy = !ashy && isSandstormBiome(biome);
        if (!ashy && !sandy) {
            return false;
        }
        int terrainHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        int y0 = Math.max(cameraBlockY - radius, terrainHeight);
        int y1 = Math.max(cameraBlockY + radius, terrainHeight);
        RandomSource random = RandomSource.create(x * x * 3121L + x * 45238971L ^ z * z * 418711L + z * 13761L);
        int lightSampleY = Math.max(cameraBlockY, terrainHeight);
        int lightCoords = LightCoordsUtil.getLightCoords(level, new BlockPos(x, lightSampleY, z));
        int brightened = LightCoordsUtil.pack((LightCoordsUtil.block(lightCoords) * 3 + 15) / 4, (LightCoordsUtil.sky(lightCoords) * 3 + 15) / 4);
        long ticks = level.getGameTime();
        float time = (ticks & 131071L) + partialTicks;
        float vOffset = -((float) (ticks & 511L) + partialTicks) / 512.0f;
        if (ashy) {
            float u = random.nextFloat() * 0.3f + time * 0.003f * (float) random.nextGaussian();
            float v = random.nextFloat() + time * (float) random.nextGaussian() * 0.001f;
            ashColumns.add(new WeatherEffectRenderer.ColumnInstance(x, z, y0, y1, u, vOffset + v, brightened));
        } else {
            float u = time * (0.07f + (float) random.nextGaussian() * 0.01f);
            float v = random.nextFloat() + time * (float) random.nextGaussian() * 0.001f;
            sandColumns.add(new WeatherEffectRenderer.ColumnInstance(x, z, y0, y1, u, vOffset + v, brightened));
        }
        return true;
    }

    /** The ash and sandstorm columns, as the game draws its snow. */
    public static void render(Vec3 cameraPos, int radius, float intensity, List<WeatherEffectRenderer.ColumnInstance> ashColumns,
                              List<WeatherEffectRenderer.ColumnInstance> sandColumns) {
        int columnCount = ashColumns.size() + sandColumns.size();
        if (columnCount == 0) {
            return;
        }
        RenderTarget target = OutputTarget.WEATHER_TARGET.getRenderTarget();
        RenderPipeline pipeline = Minecraft.getInstance().gameRenderer.gameRenderState().useShaderTransparency()
                ? RenderPipelines.WEATHER_DEPTH_WRITE : RenderPipelines.WEATHER_NO_DEPTH_WRITE;
        try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(columnCount * 4 * DefaultVertexFormat.PARTICLE.getVertexSize())) {
            BufferBuilder builder = new BufferBuilder(bytes, PrimitiveTopology.QUADS, DefaultVertexFormat.PARTICLE);
            addInstances(builder, ashColumns, cameraPos, radius, intensity);
            addInstances(builder, sandColumns, cameraPos, radius, intensity);
            try (MeshData mesh = builder.buildOrThrow();
                 GpuBuffer vertices = RenderSystem.getDevice().createBuffer(() -> "LOTR weather", GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer())) {
                AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
                GpuBuffer indexBuffer = indices.getBuffer(columnCount * 6);
                GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "LOTR weather",
                        target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
                    renderPass.setPipeline(pipeline);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                    renderPass.bindTexture("Sampler2", Minecraft.getInstance().gameRenderer.lightmap(),
                            RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                    renderPass.setIndexBuffer(indexBuffer, indices.type());
                    renderPass.setVertexBuffer(0, vertices.slice());
                    draw(renderPass, ASH, 0, ashColumns.size());
                    draw(renderPass, SANDSTORM, ashColumns.size(), sandColumns.size());
                }
            }
        }
    }

    private static void draw(RenderPass renderPass, Identifier texture, int startColumn, int columnCount) {
        if (columnCount == 0) {
            return;
        }
        AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(texture);
        renderPass.bindTexture("Sampler0", tex.getTextureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST));
        renderPass.drawIndexed(columnCount * 6, 1, startColumn * 6, 0, 0);
    }

    private static void addInstances(BufferBuilder builder, List<WeatherEffectRenderer.ColumnInstance> columns, Vec3 cameraPos, int radius,
                                     float intensity) {
        float radiusSq = radius * radius;
        for (WeatherEffectRenderer.ColumnInstance column : columns) {
            float relativeX = (float) (column.x() + 0.5 - cameraPos.x);
            float relativeZ = (float) (column.z() + 0.5 - cameraPos.z);
            float distanceSq = (float) Mth.lengthSquared(relativeX, relativeZ);
            float alpha = Mth.lerp(Math.min(distanceSq / radiusSq, 1.0f), 0.8f, 0.5f) * intensity;
            int color = ARGB.white(alpha);
            int index = (column.z() - Mth.floor(cameraPos.z) + 16) * 32 + column.x() - Mth.floor(cameraPos.x) + 16;
            index = Mth.clamp(index, 0, 1023);
            float halfSizeX = COLUMN_SIZE_X[index] / 2.0f;
            float halfSizeZ = COLUMN_SIZE_Z[index] / 2.0f;
            float x0 = relativeX - halfSizeX;
            float x1 = relativeX + halfSizeX;
            float y1 = (float) (column.topY() - cameraPos.y);
            float y0 = (float) (column.bottomY() - cameraPos.y);
            float z0 = relativeZ - halfSizeZ;
            float z1 = relativeZ + halfSizeZ;
            float u0 = column.uOffset();
            float u1 = column.uOffset() + 1.0f;
            float v0 = column.bottomY() * 0.25f + column.vOffset();
            float v1 = column.topY() * 0.25f + column.vOffset();
            builder.addVertex(x0, y1, z0).setUv(u0, v0).setColor(color).setLight(column.lightCoords());
            builder.addVertex(x1, y1, z1).setUv(u1, v0).setColor(color).setLight(column.lightCoords());
            builder.addVertex(x1, y0, z1).setUv(u1, v1).setColor(color).setLight(column.lightCoords());
            builder.addVertex(x0, y0, z0).setUv(u0, v1).setColor(color).setLight(column.lightCoords());
        }
    }

    public interface StateExtension {
        List<WeatherEffectRenderer.ColumnInstance> lotr$ashColumns();

        List<WeatherEffectRenderer.ColumnInstance> lotr$sandColumns();
    }

    public static @Nullable StateExtension extension(Object state) {
        return state instanceof StateExtension ext ? ext : null;
    }
}
