package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

/**
 * LOTRSkyRenderer ("LOTR sky"): Middle-earth's own sun and moon -- the moon's eight phases counted by
 * the Shire calendar, reddened in a lunar eclipse -- its night sky, one of the starry skyboxes taken
 * at dusk, and Eärendil, the evening and morning star, bright a while after sunset and before
 * sunrise; none of them where a biome has no sky. The sky's colour, its disc and the sunrise's fan
 * are today's, the fan a touch greener as the original drew it.
 *
 * <p>Drawn the way today's sky draws its sun and moon, with quads made once and moved into place.
 */
public final class LOTRSkyRenderer {

    public static final int MOON_PHASES = 8;
    private static final Identifier SUN = Identifier.fromNamespaceAndPath("lotr", "sky/sun.png");
    private static final Identifier MOON = Identifier.fromNamespaceAndPath("lotr", "sky/moon.png");
    private static final Identifier EARENDIL = Identifier.fromNamespaceAndPath("lotr", "sky/earendil.png");
    private static final Random RAND = new Random();

    /** The sun, drawn over the sky as it is (1.7.10's SRC_ALPHA, ONE_MINUS_SRC_ALPHA); the rest add light (CELESTIAL). */
    private static final RenderPipeline CELESTIAL_TRANSLUCENT = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withLocation(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pipeline/celestial_translucent"))
            .withVertexShader("core/position_tex")
            .withFragmentShader("core/position_tex")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    private static @Nullable GpuBuffer quadBuffer;
    private static @Nullable GpuBuffer moonBuffer;
    private static @Nullable GpuBuffer skyboxBuffer;
    private static @Nullable Identifier currentSkyTexture;
    private static @Nullable List<Identifier> skyTextures;

    private LOTRSkyRenderer() {
    }

    /** LOTRWorldProvider.isLunarEclipse: the new moon of every fourth month of moons. */
    public static boolean isLunarEclipse() {
        int day = LOTRDate.ShireReckoning.currentDay;
        return Math.floorMod(day, MOON_PHASES) == 0 && Math.floorMod(day / MOON_PHASES, 4) == 3;
    }

    public static int getLOTRMoonPhase() {
        return Math.floorMod(LOTRDate.ShireReckoning.currentDay, MOON_PHASES);
    }

    /** Whether the sky over the viewer has its features (LOTRBiome.hasSky). */
    public static boolean renderSkyFeatures() {
        Minecraft mc = Minecraft.getInstance();
        Entity viewer = mc.getCameraEntity();
        if (mc.level == null || viewer == null) {
            return true;
        }
        LOTRBiome biome = LOTRBiomes.of(mc.level.getBiome(BlockPos.containing(viewer.position())));
        return biome == null || biome.hasSky();
    }

    /** The sunrise's fan, its green raised a fifth as the original's was. */
    public static int sunriseColor(int color) {
        return ARGB.color(ARGB.alpha(color), ARGB.red(color), Math.min(255, Math.round(ARGB.green(color) * 1.2f)), ARGB.blue(color));
    }

    // ---------------------------------------------------------------- the night sky's textures

    /** LOTRRandomSkins.loadSkinsList("lotr:sky/night"): the skyboxes there, numbered from 0. */
    private static List<Identifier> skyTextures() {
        if (skyTextures == null) {
            List<Identifier> list = new ArrayList<>();
            for (int i = 0; ; ++i) {
                Identifier id = Identifier.fromNamespaceAndPath("lotr", "sky/night/" + i + ".png");
                if (Minecraft.getInstance().getResourceManager().getResource(id).isEmpty()) {
                    break;
                }
                list.add(id);
            }
            skyTextures = list;
        }
        return skyTextures;
    }

    public static void onResourceReload() {
        skyTextures = null;
        currentSkyTexture = null;
    }

    // ---------------------------------------------------------------- buffers

    private static GpuBuffer build(String name, int quads, java.util.function.Consumer<BufferBuilder> builder) {
        try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(quads * 4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(bytes, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
            builder.accept(bufferBuilder);
            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> name, GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer());
            }
        }
    }

    /** A quad of side 2 at height 0, the whole of its texture across it, as the original's sun and Eärendil. */
    private static GpuBuffer quad() {
        if (quadBuffer == null) {
            quadBuffer = build("LOTR sky quad", 1, b -> {
                b.addVertex(-1.0f, 0.0f, -1.0f).setUv(0.0f, 0.0f);
                b.addVertex(1.0f, 0.0f, -1.0f).setUv(1.0f, 0.0f);
                b.addVertex(1.0f, 0.0f, 1.0f).setUv(1.0f, 1.0f);
                b.addVertex(-1.0f, 0.0f, 1.0f).setUv(0.0f, 1.0f);
            });
        }
        return quadBuffer;
    }

    /** The moon's eight phases, side by side in its texture, each as the original laid it under the world. */
    private static GpuBuffer moon() {
        if (moonBuffer == null) {
            moonBuffer = build("LOTR moon phases", MOON_PHASES, b -> {
                for (int phase = 0; phase < MOON_PHASES; ++phase) {
                    float u0 = (float) phase / MOON_PHASES;
                    float u1 = (float) (phase + 1) / MOON_PHASES;
                    b.addVertex(-1.0f, 0.0f, 1.0f).setUv(u1, 1.0f);
                    b.addVertex(1.0f, 0.0f, 1.0f).setUv(u0, 1.0f);
                    b.addVertex(1.0f, 0.0f, -1.0f).setUv(u0, 0.0f);
                    b.addVertex(-1.0f, 0.0f, -1.0f).setUv(u1, 0.0f);
                }
            });
        }
        return moonBuffer;
    }

    /** renderSkyboxSide: each of the six faces' third-by-half of the skybox texture, on a face 100 below. */
    private static GpuBuffer skybox() {
        if (skyboxBuffer == null) {
            skyboxBuffer = build("LOTR night sky", 6, b -> {
                for (int side = 0; side < 6; ++side) {
                    float u = side % 3 / 3.0f;
                    float v = side / 3 / 2.0f;
                    b.addVertex(-100.0f, -100.0f, -100.0f).setUv(u, v);
                    b.addVertex(-100.0f, -100.0f, 100.0f).setUv(u, v + 0.5f);
                    b.addVertex(100.0f, -100.0f, 100.0f).setUv(u + 0.33333334f, v + 0.5f);
                    b.addVertex(100.0f, -100.0f, -100.0f).setUv(u + 0.33333334f, v);
                }
            });
        }
        return skyboxBuffer;
    }

    // ---------------------------------------------------------------- drawing

    /**
     * In place of SkyRenderer.renderSunMoonAndStars: the night sky, the sun (and its sunrise glow), the
     * moon and Eärendil, turned with the sun.
     */
    public static void renderCelestials(PoseStack poseStack, float sunAngle, float rainBrightness, float starBrightness,
                                        int sunriseAndSunsetColor) {
        if (!renderSkyFeatures()) {
            return;
        }
        float celestialAngle = sunAngle / ((float) Math.PI * 2.0f);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        float stars = starBrightness * rainBrightness;
        if (stars > 0.0f) {
            List<Identifier> textures = skyTextures();
            if (currentSkyTexture == null && !textures.isEmpty()) {
                currentSkyTexture = textures.get(RAND.nextInt(textures.size()));
            }
            if (currentSkyTexture != null) {
                renderNightSky(poseStack, celestialAngle, stars);
            }
        } else {
            currentSkyTexture = null;
        }
        poseStack.mulPose(Axis.XP.rotationDegrees(celestialAngle * 360.0f));
        // The sun, then the sunrise's yellow over it.
        draw("LOTR sun", CELESTIAL_TRANSLUCENT, SUN, quad(), 0, poseStack, 100.0f, 10.0f, new Vector4f(1.0f, 1.0f, 1.0f, rainBrightness));
        float sunriseAlpha = ARGB.alphaFloat(sunriseAndSunsetColor);
        if (sunriseAlpha > 0.001f) {
            draw("LOTR sunrise sun", CELESTIAL_TRANSLUCENT, SUN, quad(), 0, poseStack, 100.0f, 10.0f,
                    new Vector4f(1.0f, 0.9f, 0.2f, sunriseAlpha * 0.5f * rainBrightness));
        }
        Vector4f moonColor = isLunarEclipse() ? new Vector4f(1.0f, 0.6f, 0.4f, 1.0f) : new Vector4f(1.0f, 1.0f, 1.0f, rainBrightness);
        draw("LOTR moon", RenderPipelines.CELESTIAL, MOON, moon(), getLOTRMoonPhase() * 4, poseStack, -100.0f, 10.0f, moonColor);
        // Eärendil: brightest midway between a quarter and three tenths of a day from noon, 18 degrees from the sun's path.
        float f0 = celestialAngle - 0.5f;
        float f1 = Math.abs(f0);
        float eMin = 0.15f;
        float eMax = 0.3f;
        if (f1 >= eMin && f1 <= eMax) {
            float eMid = (eMin + eMax) / 2.0f;
            float eHalfWidth = eMax - eMid;
            float eBright = Mth.cos((f1 - eMid) / eHalfWidth * (float) Math.PI / 2.0f);
            eBright *= eBright;
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(Math.signum(f0) * 18.0f));
            draw("LOTR Earendil", RenderPipelines.CELESTIAL, EARENDIL, quad(), 0, poseStack, 100.0f, 1.5f,
                    new Vector4f(1.0f, 1.0f, 1.0f, eBright * rainBrightness));
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /** The skybox's six faces, turned with the sun and set about the viewer as the original turned them. */
    private static void renderNightSky(PoseStack poseStack, float celestialAngle, float brightness) {
        Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, brightness);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(celestialAngle * 360.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0f));
        drawSide(poseStack, 4, color);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        drawSide(poseStack, 1, color);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        drawSide(poseStack, 0, color);
        poseStack.popPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        drawSide(poseStack, 5, color);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        drawSide(poseStack, 2, color);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        drawSide(poseStack, 3, color);
        poseStack.popPose();
    }

    private static void drawSide(PoseStack poseStack, int side, Vector4f color) {
        draw("LOTR night sky", RenderPipelines.CELESTIAL, currentSkyTexture, skybox(), side * 4, poseStack, 0.0f, 1.0f, color);
    }

    /** One quad of a buffer, raised and widened, in the pose given, textured and tinted. */
    private static void draw(String name, RenderPipeline pipeline, Identifier texture, GpuBuffer buffer, int baseVertex, PoseStack poseStack,
                             float height, float size, Vector4f color) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0.0f, height, 0.0f);
        modelViewStack.scale(size, 1.0f, size);
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f(modelViewStack), color);
        RenderTarget target = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indexBuffer = quadIndices.getBuffer(6);
        AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(texture);
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
                .createRenderPass(() -> name, target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(pipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.bindTexture("Sampler0", tex.getTextureView(), tex.getSampler());
            renderPass.setVertexBuffer(0, buffer.slice());
            renderPass.setIndexBuffer(indexBuffer, quadIndices.type());
            renderPass.drawIndexed(6, 1, 0, baseVertex, 0);
        }
        modelViewStack.popMatrix();
    }
}
