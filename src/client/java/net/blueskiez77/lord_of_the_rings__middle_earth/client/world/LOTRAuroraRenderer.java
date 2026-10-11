package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import java.awt.Color;
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

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;

import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;

/**
 * LOTRRenderNorthernLights ("Aurora"): on some nights -- every night at Yule -- the northern lights hang
 * in the northern sky over the far north of Middle-earth, and on the rarest further south, never at
 * Christmas: three wavering curtains, their colours green or, now and then, red and violet, changing
 * every minute or two, red and orange near the gate of Utumno; dimmed by day, by rain and by being
 * further south.
 *
 * <p>Drawn at the end of the sky, behind everything in the world as the original's distance put it,
 * brought in a twentieth of the way towards the viewer to stay within the sky's reach -- it looks the
 * same.
 */
public final class LOTRAuroraRenderer {

    private static final float SKY_SCALE = 0.05f;
    private static final RenderPipeline AURORA_PIPELINE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withLocation(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pipeline/aurora"))
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    private static int nlTick;
    private static int currentNightNum = Integer.MIN_VALUE;
    private static float brightnessTonight;
    private static float maxNorthTonight;
    private static float minNorthTonight;
    private static int rainingTick;
    private static int rainingTickPrev;
    private static boolean atUtumno;
    private static int utumnoChange;
    private static final Random RAND = new Random();
    private static final Random DATE_RAND = new Random();
    private static float[] colorTopCurrent;
    private static float[] colorMidCurrent;
    private static float[] colorBottomCurrent;
    private static float[] colorTopNext;
    private static float[] colorMidNext;
    private static float[] colorBottomNext;
    private static int colorChangeTime;
    private static int colorChangeTick;
    private static int timeUntilColorChange;
    private static int utumnoCheckTime;
    private static final AuroraCycle WAVE0 = new AuroraCycle(4.0f, 0.01f, 0.9f);
    private static final List<AuroraCycle> WAVE_OSCILLATIONS = new ArrayList<>();
    private static final List<AuroraCycle> GLOW_OSCILLATIONS = new ArrayList<>();
    private static final AuroraCycle GLOW0 = new AuroraCycle(20.0f, 0.02f, 0.6f);

    private LOTRAuroraRenderer() {
    }

    public static boolean isEnabled() {
        return LOTRConfig.aurora && LOTREnvironmentClient.isMiddleEarth(Minecraft.getInstance().level);
    }

    private static boolean fancy() {
        return Minecraft.getInstance().options.graphicsPreset().get() != GraphicsPreset.FAST;
    }

    private static float randomFloatClamp(Random rand, float min, float max) {
        return min >= max ? min : rand.nextFloat() * (max - min) + min;
    }

    private static int randomIntegerInRange(Random rand, int min, int max) {
        return min >= max ? min : rand.nextInt(max - min + 1) + min;
    }

    private static float[] components(int rgb) {
        return new Color(rgb).getColorComponents(null);
    }

    /** generateColorSet: mostly greens, sometimes a red or violet crown, rarely a purple-blue band. */
    private static int[] generateColorSet() {
        float h1 = randomFloatClamp(RAND, 0.22f, 0.48f);
        float h2 = randomFloatClamp(RAND, 0.22f, 0.48f);
        float h3 = randomFloatClamp(RAND, 0.22f, 0.48f);
        if (RAND.nextInt(3) == 0) {
            h1 = randomFloatClamp(RAND, 0.78f, 1.08f);
        }
        if (RAND.nextInt(5) == 0) {
            h1 = randomFloatClamp(RAND, 0.78f, 1.08f);
            h2 = randomFloatClamp(RAND, 0.85f, 1.08f);
        }
        if (RAND.nextInt(50) == 0) {
            h1 = randomFloatClamp(RAND, 0.7f, 1.08f);
            h2 = randomFloatClamp(RAND, 0.54f, 0.77f);
            h3 = randomFloatClamp(RAND, 0.48f, 0.7f);
        }
        return new int[]{Color.HSBtoRGB(h1, 1.0f, 1.0f), Color.HSBtoRGB(h2, 1.0f, 1.0f), Color.HSBtoRGB(h3, 1.0f, 1.0f)};
    }

    private static float getNorthernness(Entity entity) {
        float northernness = ((float) entity.getZ() - minNorthTonight) / (maxNorthTonight - minNorthTonight);
        return Mth.clamp(northernness, 0.0f, 1.0f);
    }

    private static float glowEquation(float t, float tick) {
        float f = GLOW0.calc(t, tick);
        if (fancy()) {
            for (AuroraCycle c : GLOW_OSCILLATIONS) {
                f += c.calc(t, tick);
            }
        }
        return f;
    }

    private static float waveEquation(float t, float tick) {
        float f = WAVE0.calc(t, tick);
        for (AuroraCycle c : WAVE_OSCILLATIONS) {
            f += c.calc(t, tick);
        }
        return f;
    }

    /** isRainLayerAt: rain (not snow) falling where the viewer stands. */
    private static boolean isRainLayerAt(ClientLevel level, Entity entity) {
        if (!level.isRaining()) {
            return false;
        }
        LOTRBiome biome = LOTRBiomes.of(level.getBiome(BlockPos.containing(entity.position())));
        if (biome == null || biome.enableSnow) {
            return false;
        }
        return biome.temperature >= 0.15f && biome.enableRain;
    }

    /** update: each tick, tonight's lights, the rain's dimming, the colours' changes, the waves and glows coming and going. */
    public static void update(ClientLevel level, Entity viewer) {
        ++nlTick;
        int effectiveDay = LOTRDate.ShireReckoning.currentDay;
        // As the original's: the time of day in ticks below a quarter, so only at a day's very first tick.
        float daytime = level.getDefaultClockTime() % LOTRDate.TICKS_IN_DAY;
        if (daytime < 0.25f) {
            --effectiveDay;
        }
        if (effectiveDay != currentNightNum) {
            currentNightNum = effectiveDay;
            DATE_RAND.setSeed(currentNightNum * 35920558925051L + currentNightNum + 83025820626792L);
            LOTRDate.ShireReckoning.Month month = LOTRDate.ShireReckoning.getShireDate(currentNightNum).month;
            boolean isYule = month == LOTRDate.ShireReckoning.Month.YULE_1 || month == LOTRDate.ShireReckoning.Month.YULE_2;
            maxNorthTonight = -35000.0f;
            minNorthTonight = randomFloatClamp(DATE_RAND, -20000.0f, -15000.0f);
            float goSouth = DATE_RAND.nextFloat();
            if (isYule || goSouth < 0.01f) {
                minNorthTonight += 15000.0f;
            } else if (goSouth < 0.1f) {
                minNorthTonight += 10000.0f;
            } else if (goSouth < 0.5f) {
                minNorthTonight += 5000.0f;
            }
            if (LOTRMod.isChristmas()) {
                minNorthTonight = 1000000.0f;
            }
            brightnessTonight = isYule || DATE_RAND.nextFloat() < 0.5f ? randomFloatClamp(DATE_RAND, 0.4f, 1.0f) : 0.0f;
        }
        rainingTickPrev = rainingTick;
        if (isRainLayerAt(level, viewer)) {
            if (rainingTick < 80) {
                ++rainingTick;
            }
        } else if (rainingTick > 0) {
            --rainingTick;
        }
        if (colorTopCurrent == null) {
            int[] cs = generateColorSet();
            colorTopCurrent = components(cs[0]);
            colorMidCurrent = components(cs[1]);
            colorBottomCurrent = components(cs[2]);
        }
        if (timeUntilColorChange > 0) {
            --timeUntilColorChange;
        } else if (RAND.nextInt(1200) == 0) {
            int[] cs = generateColorSet();
            colorTopNext = components(cs[0]);
            colorMidNext = components(cs[1]);
            colorBottomNext = components(cs[2]);
            colorChangeTick = colorChangeTime = randomIntegerInRange(RAND, 100, 200);
            utumnoCheckTime = 0;
        }
        if (colorChangeTick > 0 && --colorChangeTick <= 0) {
            colorChangeTime = 0;
            colorTopCurrent = colorTopNext;
            colorMidCurrent = colorMidNext;
            colorBottomCurrent = colorBottomNext;
            colorTopNext = null;
            colorMidNext = null;
            colorBottomNext = null;
            timeUntilColorChange = randomIntegerInRange(RAND, 1200, 2400);
        }
        if (utumnoCheckTime > 0) {
            --utumnoCheckTime;
        } else {
            double range = 256.0;
            if (LOTRFixedStructures.UTUMNO_ENTRANCE.distanceSqTo(viewer) <= range * range) {
                atUtumno = true;
                timeUntilColorChange = 0;
                colorTopNext = new float[]{1.0f, 0.4f, 0.0f};
                colorMidNext = new float[]{1.0f, 0.0f, 0.0f};
                colorBottomNext = new float[]{1.0f, 0.0f, 0.3f};
                colorChangeTick = colorChangeTime = randomIntegerInRange(RAND, 100, 200);
            } else {
                atUtumno = false;
            }
            utumnoCheckTime = 200;
        }
        if (atUtumno) {
            if (utumnoChange < 200) {
                ++utumnoChange;
            }
        } else if (utumnoChange > 0) {
            --utumnoChange;
        }
        if (RAND.nextInt(50) == 0) {
            float freq = randomFloatClamp(RAND, 8.0f, 100.0f);
            AuroraCycle cycle = new AuroraCycle(freq, freq * 5.0E-4f, randomFloatClamp(RAND, 0.05f, 0.3f));
            cycle.age = cycle.maxAge = randomIntegerInRange(RAND, 100, 400);
            WAVE_OSCILLATIONS.add(cycle);
        }
        WAVE_OSCILLATIONS.removeIf(c -> {
            c.update();
            return c.age <= 0;
        });
        if (RAND.nextInt(120) == 0) {
            float freq = randomFloatClamp(RAND, 30.0f, 150.0f);
            AuroraCycle cycle = new AuroraCycle(freq, freq * 0.002f, randomFloatClamp(RAND, 0.05f, 0.5f));
            cycle.age = cycle.maxAge = randomIntegerInRange(RAND, 100, 400);
            GLOW_OSCILLATIONS.add(cycle);
        }
        if (RAND.nextInt(300) == 0) {
            float freq = randomFloatClamp(RAND, 400.0f, 500.0f);
            AuroraCycle cycle = new AuroraCycle(freq, freq * 0.004f, randomFloatClamp(RAND, 0.1f, 0.2f));
            cycle.age = cycle.maxAge = randomIntegerInRange(RAND, 100, 200);
            GLOW_OSCILLATIONS.add(cycle);
        }
        GLOW_OSCILLATIONS.removeIf(c -> {
            c.update();
            return c.age <= 0;
        });
    }

    /** render: the three curtains, if tonight has them, it is night enough, and the viewer is north enough. */
    public static void render(float sunBrightness, float rainStrength, float tick) {
        Minecraft mc = Minecraft.getInstance();
        Entity viewer = mc.getCameraEntity();
        if (viewer == null || colorTopCurrent == null || mc.options.renderDistance().get() < 6) {
            return;
        }
        float minSun = 0.2f;
        float daylight = (sunBrightness - minSun) / (1.0f - minSun);
        float maxDaylight = 0.3f;
        float nlBrightness = (1.0f - daylight - (1.0f - maxDaylight)) / maxDaylight;
        if (nlBrightness <= 0.0f) {
            return;
        }
        float tonight = brightnessTonight;
        float utumno = utumnoChange / 200.0f;
        tonight += (1.0f - tonight) * utumno;
        if (tonight <= 0.0f) {
            return;
        }
        nlBrightness *= tonight;
        float northernness = getNorthernness(viewer);
        if (northernness <= 0.0f) {
            return;
        }
        nlBrightness *= northernness;
        float raininess = (rainingTickPrev + (rainingTick - rainingTickPrev) * tick) / 80.0f;
        if (raininess >= 1.0f) {
            return;
        }
        nlBrightness *= 1.0f - raininess;
        nlBrightness *= 0.3f + (1.0f - rainStrength) * 0.7f;
        float nlScale = 2000.0f;
        float nlDistance = (1.0f - northernness) * nlScale * 2.0f;
        float nlHeight = nlScale * 0.5f;
        int strips = fancy() ? 500 : 80;
        try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(3 * strips * 3 * 4 * DefaultVertexFormat.POSITION_COLOR.getVertexSize())) {
            BufferBuilder builder = new BufferBuilder(bytes, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);
            renderSheet(builder, nlScale * -0.5f, nlBrightness * 0.8f, nlScale, nlScale * 0.25f, 0.25502f, tick, strips);
            renderSheet(builder, 0.0f, nlBrightness, nlScale * 1.5f, nlScale * 0.3f, 0.15696f, tick, strips);
            renderSheet(builder, nlScale * 0.5f, nlBrightness * 0.8f, nlScale, nlScale * 0.25f, 0.67596f, tick, strips);
            try (MeshData mesh = builder.buildOrThrow();
                 GpuBuffer vertices = RenderSystem.getDevice().createBuffer(() -> "LOTR aurora", GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer())) {
                Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
                modelViewStack.pushMatrix();
                modelViewStack.scale(SKY_SCALE);
                modelViewStack.translate(0.0f, nlHeight, -nlDistance);
                GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f(modelViewStack),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
                modelViewStack.popMatrix();
                int indexCount = mesh.drawState().indexCount();
                AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
                GpuBuffer indexBuffer = indices.getBuffer(indexCount);
                RenderTarget target = mc.gameRenderer.mainRenderTarget();
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "LOTR aurora",
                        target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
                    renderPass.setPipeline(AURORA_PIPELINE);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                    renderPass.setVertexBuffer(0, vertices.slice());
                    renderPass.setIndexBuffer(indexBuffer, indices.type());
                    renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
                }
            }
        }
    }

    /** renderSheet: one curtain, in strips, rippling along its length, its colours graded from the foot up and its glow wavering. */
    private static void renderSheet(BufferBuilder tess, float nlDistance, float nlBrightness, double halfWidth, double halfHeight,
                                    float tickExtra, float tick, int strips) {
        float r1 = colorTopCurrent[0];
        float g1 = colorTopCurrent[1];
        float b1 = colorTopCurrent[2];
        float r2 = colorMidCurrent[0];
        float g2 = colorMidCurrent[1];
        float b2 = colorMidCurrent[2];
        float r3 = colorBottomCurrent[0];
        float g3 = colorBottomCurrent[1];
        float b3 = colorBottomCurrent[2];
        if (colorChangeTime > 0 && colorTopNext != null) {
            float t = 1.0f - (float) colorChangeTick / colorChangeTime;
            r1 += (colorTopNext[0] - colorTopCurrent[0]) * t;
            g1 += (colorTopNext[1] - colorTopCurrent[1]) * t;
            b1 += (colorTopNext[2] - colorTopCurrent[2]) * t;
            r2 += (colorMidNext[0] - colorMidCurrent[0]) * t;
            g2 += (colorMidNext[1] - colorMidCurrent[1]) * t;
            b2 += (colorMidNext[2] - colorMidCurrent[2]) * t;
            r3 += (colorBottomNext[0] - colorBottomCurrent[0]) * t;
            g3 += (colorBottomNext[1] - colorBottomCurrent[1]) * t;
            b3 += (colorBottomNext[2] - colorBottomCurrent[2]) * t;
        }
        float a1 = 0.0f * nlBrightness;
        float a2 = 0.4f * nlBrightness;
        float a3 = 0.8f * nlBrightness;
        float fullTick = nlTick + tick + tickExtra;
        for (int l = 0; l < strips; ++l) {
            float t = (float) l / strips;
            float t1 = (float) (l + 1) / strips;
            float a1Here = a1;
            float a2Here = a2;
            float a3Here = a3;
            float fadeEdge = 0.3f;
            float fadePos = Math.min(t, 1.0f - t);
            if (fadePos < fadeEdge) {
                float fade = fadePos / fadeEdge;
                a1Here *= fade;
                a2Here *= fade;
                a3Here *= fade;
            }
            float randomFade = 0.5f + glowEquation(t, fullTick) * 0.5f;
            float x0 = (float) (-halfWidth + halfWidth * 2.0 * t);
            float x1 = (float) (x0 + halfWidth * 2.0 / strips);
            float yMin = (float) -halfHeight;
            float yMid = (float) (-halfHeight * 0.4);
            float extra = (float) (halfHeight * 0.15);
            float z0 = (float) (nlDistance + waveEquation(t, fullTick) * (halfWidth * 0.15));
            float z1 = (float) (nlDistance + waveEquation(t1, fullTick) * (halfWidth * 0.15));
            a3Here *= randomFade;
            a2Here *= randomFade;
            tess.addVertex(x0, yMin - extra, z0).setColor(r3, g3, b3, 0.0f);
            tess.addVertex(x1, yMin - extra, z1).setColor(r3, g3, b3, 0.0f);
            tess.addVertex(x1, yMin, z1).setColor(r3, g3, b3, a3Here);
            tess.addVertex(x0, yMin, z0).setColor(r3, g3, b3, a3Here);
            tess.addVertex(x0, yMin, z0).setColor(r3, g3, b3, a3Here);
            tess.addVertex(x1, yMin, z1).setColor(r3, g3, b3, a3Here);
            tess.addVertex(x1, yMid, z1).setColor(r2, g2, b2, a2Here);
            tess.addVertex(x0, yMid, z0).setColor(r2, g2, b2, a2Here);
            tess.addVertex(x0, yMid, z0).setColor(r2, g2, b2, a2Here);
            tess.addVertex(x1, yMid, z1).setColor(r2, g2, b2, a2Here);
            tess.addVertex(x1, (float) halfHeight, z1).setColor(r1, g1, b1, a1Here * randomFade);
            tess.addVertex(x0, (float) halfHeight, z0).setColor(r1, g1, b1, a1Here * randomFade);
        }
    }

    /** AuroraCycle: a ripple of a frequency and speed whose strength rises and falls over its life. */
    private static final class AuroraCycle {
        final float freq;
        final float tickMultiplier;
        final float amp;
        int age;
        int maxAge = -1;
        float ampModifier = 1.0f;

        AuroraCycle(float freq, float tickMultiplier, float amp) {
            this.freq = freq;
            this.tickMultiplier = tickMultiplier;
            this.amp = amp;
        }

        float calc(float t, float tick) {
            return Mth.cos(t * this.freq + tick * this.tickMultiplier) * this.amp * this.ampModifier;
        }

        void update() {
            if (this.age >= 0) {
                --this.age;
                float a = (float) (this.maxAge - this.age) / this.maxAge;
                this.ampModifier = Math.min(a, 1.0f - a);
            }
        }
    }
}
