package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import org.joml.Vector4f;

/**
 * LOTRCloudRenderer ("Cloud range"): Middle-earth's clouds, two wide sheets of the clouds texture, the
 * upper fifty blocks higher, twice the size and fainter, drifting with a wind whose direction and
 * speed, like the clouds' thickness, each day settles towards that day's own (CloudProperty); out to
 * the configured range, where they thin away as the original's fog took them.
 */
public final class LOTRCloudRenderer {

    private static final Identifier CLOUDS = Identifier.fromNamespaceAndPath("lotr", "sky/clouds.png");
    private static final Random CLOUD_RAND = new Random();
    private static final CloudProperty CLOUD_OPACITY = new CloudProperty(233591206262L, 0.1f, 1.0f, 0.001f);
    private static final CloudProperty CLOUD_SPEED = new CloudProperty(6283905602629L, 0.0f, 0.5f, 0.001f);
    private static final CloudProperty CLOUD_ANGLE = new CloudProperty(360360635650636L, 0.0f, 6.2831855f, 0.01f);
    /** The squares of each sheet, across and along, so that it can thin towards its edge. */
    private static final int GRID = 16;

    private static final RenderPipeline CLOUD_PIPELINE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withLocation(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pipeline/clouds"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    private static double cloudPosXPre;
    private static double cloudPosX;
    private static double cloudPosZPre;
    private static double cloudPosZ;

    private LOTRCloudRenderer() {
    }

    public static boolean isEnabled() {
        return LOTRConfig.cloudRange > 0 && LOTREnvironmentClient.isMiddleEarth(Minecraft.getInstance().level);
    }

    public static void resetClouds() {
        CLOUD_OPACITY.reset();
        CLOUD_SPEED.reset();
        CLOUD_ANGLE.reset();
    }

    /** updateClouds: each tick, the day's wind carries the clouds on. */
    public static void updateClouds() {
        CLOUD_OPACITY.update();
        CLOUD_SPEED.update();
        CLOUD_ANGLE.update();
        float angle = CLOUD_ANGLE.getValue(1.0f);
        float speed = CLOUD_SPEED.getValue(1.0f);
        cloudPosXPre = cloudPosX;
        cloudPosX += Mth.cos(angle) * speed;
        cloudPosZPre = cloudPosZ;
        cloudPosZ += Mth.sin(angle) * speed;
    }

    /** In place of today's clouds: the two sheets, in the clouds' colour, at the clouds' height. */
    public static void render(int color, float cloudHeight, Vec3 cameraPosition, float partialTicks) {
        int cloudRange = LOTRConfig.cloudRange;
        float r = ARGB.redFloat(color);
        float g = ARGB.greenFloat(color);
        float b = ARGB.blueFloat(color);
        RenderTarget main = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        RenderTarget cloudTarget = Minecraft.getInstance().levelRenderer.cloudsTarget();
        RenderTarget target = cloudTarget != null ? cloudTarget : main;
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(CLOUDS);
        for (int pass = 0; pass < 2; ++pass) {
            int scale = 4096 << pass;
            double invScale = 1.0 / scale;
            double cloudPosXAdd = (cloudPosXPre + (cloudPosX - cloudPosXPre) * partialTicks) / (pass + 1);
            double cloudPosZAdd = (cloudPosZPre + (cloudPosZ - cloudPosZPre) * partialTicks) / (pass + 1);
            double posX = cameraPosition.x + cloudPosXAdd;
            double posZ = cameraPosition.z + cloudPosZAdd;
            double cloudX = posX - Mth.floor(posX / scale) * (double) scale;
            double cloudZ = posZ - Mth.floor(posZ / scale) * (double) scale;
            float cloudY = (float) (cloudHeight - cameraPosition.y + 0.33 + pass * 50.0);
            float alpha = (0.8f - pass * 0.5f) * CLOUD_OPACITY.getValue(partialTicks);
            float cell = cloudRange * 2.0f / GRID;
            try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(GRID * GRID * 4 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize())) {
                BufferBuilder builder = new BufferBuilder(bytes, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                for (int i = 0; i < GRID; ++i) {
                    for (int k = 0; k < GRID; ++k) {
                        float x0 = -cloudRange + i * cell;
                        float z0 = -cloudRange + k * cell;
                        float x1 = x0 + cell;
                        float z1 = z0 + cell;
                        vertex(builder, x0, cloudY, z1, cloudX, cloudZ, invScale, r, g, b, alpha, cloudRange);
                        vertex(builder, x1, cloudY, z1, cloudX, cloudZ, invScale, r, g, b, alpha, cloudRange);
                        vertex(builder, x1, cloudY, z0, cloudX, cloudZ, invScale, r, g, b, alpha, cloudRange);
                        vertex(builder, x0, cloudY, z0, cloudX, cloudZ, invScale, r, g, b, alpha, cloudRange);
                    }
                }
                try (MeshData mesh = builder.buildOrThrow();
                     GpuBuffer vertices = RenderSystem.getDevice().createBuffer(() -> "LOTR clouds", GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer())) {
                    GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                            .writeTransform(RenderSystem.getModelViewMatrixCopy(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
                    AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
                    int indexCount = GRID * GRID * 6;
                    GpuBuffer indexBuffer = indices.getBuffer(indexCount);
                    try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "LOTR clouds",
                            target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
                        renderPass.setPipeline(CLOUD_PIPELINE);
                        RenderSystem.bindDefaultUniforms(renderPass);
                        renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                        renderPass.bindTexture("Sampler0", texture.getTextureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST));
                        renderPass.setVertexBuffer(0, vertices.slice());
                        renderPass.setIndexBuffer(indexBuffer, indices.type());
                        renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
                    }
                }
            }
        }
    }

    /** A corner of the sheet, its texture as far along as the drift has carried it, thinning over the last tenth of the range. */
    private static void vertex(BufferBuilder builder, float x, float y, float z, double cloudX, double cloudZ, double invScale,
                               float r, float g, float b, float alpha, int cloudRange) {
        float dist = Mth.sqrt(x * x + z * z);
        float fade = Mth.clamp((cloudRange - dist) / (cloudRange * 0.1f), 0.0f, 1.0f);
        builder.addVertex(x, y, z).setUv((float) ((x + cloudX) * invScale), (float) ((z + cloudZ) * invScale)).setColor(r, g, b, alpha * fade);
    }

    /** CloudProperty: a value that each day drifts, a step a tick, towards that day's own, seeded by the day. */
    private static final class CloudProperty {
        private final long baseSeed;
        private float value = -1.0f;
        private float prevValue;
        private final float minValue;
        private final float maxValue;
        private final float interval;

        CloudProperty(long seed, float min, float max, float interval) {
            this.baseSeed = seed;
            this.minValue = min;
            this.maxValue = max;
            this.interval = interval;
        }

        float getCurrentDayValue() {
            int day = LOTRDate.ShireReckoning.currentDay;
            long seed = day * this.baseSeed + day + 83025820626792L;
            CLOUD_RAND.setSeed(seed);
            return CLOUD_RAND.nextFloat() * (this.maxValue - this.minValue) + this.minValue;
        }

        float getValue(float f) {
            return this.prevValue + (this.value - this.prevValue) * f;
        }

        void reset() {
            this.value = -1.0f;
        }

        void update() {
            float currentDayValue = getCurrentDayValue();
            if (this.value == -1.0f) {
                this.prevValue = this.value = currentDayValue;
            } else {
                this.prevValue = this.value;
                if (this.value > currentDayValue) {
                    this.value = Math.max(this.value - this.interval, currentDayValue);
                } else if (this.value < currentDayValue) {
                    this.value = Math.min(this.value + this.interval, currentDayValue);
                }
            }
        }
    }
}
