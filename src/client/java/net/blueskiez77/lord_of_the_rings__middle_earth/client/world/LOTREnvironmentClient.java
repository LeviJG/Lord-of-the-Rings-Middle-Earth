package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRScrapTraderMisbehaviour;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRBarrowWightEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBarrowDownsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMistyMountainsBiome;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTickHandlerClient and LOTRWorldProvider's touches to Middle-earth's air:
 * <ul>
 * <li>the fog and clouds tinted by the biome the viewer stands in (LOTRBiome.getFogColor,
 *     getCloudColor), and the sky black while the scrap trader misbehaves (getSkyColorByTemp);</li>
 * <li>the fog closing in -- in a foggy biome (modifyFogIntensity), in rain (with "New weather"), in the
 *     Misty Mountains' mist, and near a barrow-wight, which also greys the Barrow-downs' sky, clouds and
 *     fog -- and a Balrog's;</li>
 * <li>Utumno's fog, thick throughout, in its levels' colours, the fire level's brightening downwards;</li>
 * <li>the screen overlays: the sun's glare when one looks into it ("Sun flare"), the mountains' mist,
 *     and a wight's darkness on its quarry.</li>
 * </ul>
 * The original blended the tints over a range that, at any usual render distance, was only the
 * viewer's own column; so here. The base fog is today's; the original's thickenings act on the fog
 * as far as the render distance reaches.
 */
public final class LOTREnvironmentClient {

    private static final Identifier MIST_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/mist_overlay.png");
    private static final Identifier WIGHT_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/wight.png");

    private static int mistTick;
    private static int prevMistTick;
    private static float mistFactor;
    private static int wightLookTick;
    private static int prevWightLookTick;
    private static int wightNearTick;
    private static int prevWightNearTick;
    private static int balrogNearTick;
    private static int prevBalrogNearTick;
    private static float sunGlare;
    private static float prevSunGlare;
    private static float rainFactor;
    private static float prevRainFactor;
    /** anyWightsViewed: the game tick at which a wight hunting the viewer was last drawn. */
    private static long wightViewedTick = Long.MIN_VALUE;

    private LOTREnvironmentClient() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTREnvironmentClient::tick);
        HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "environment_fx"), LOTREnvironmentClient::renderOverlays);
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "wight_overlay"), LOTREnvironmentClient::renderWightOverlay);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> LOTRCloudRenderer.resetClouds());
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "sky"),
                (ResourceManagerReloadListener) manager -> LOTRSkyRenderer.onResourceReload());
    }

    public static boolean isMiddleEarth(@Nullable Level level) {
        return level != null && level.dimension() == LOTRWorldGen.MIDDLE_EARTH;
    }

    /** anyWightsViewed: a wight hunting the viewer was drawn this tick or the last. */
    public static boolean anyWightsViewed() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null && mc.level.getGameTime() - wightViewedTick <= 1L;
    }

    /** LOTRRenderBarrowWight: a wight hunting the viewer was drawn this frame. */
    public static void markWightViewed(LOTRBarrowWightEntity wight) {
        Minecraft mc = Minecraft.getInstance();
        Entity viewer = mc.getCameraEntity();
        if (viewer != null && mc.level != null && wight.getTargetEntityID() == viewer.getId()) {
            wightViewedTick = mc.level.getGameTime();
        }
    }

    private static @Nullable LOTRBiome viewerBiome(Level level, Vec3 pos) {
        return LOTRBiomes.of(level.getBiome(BlockPos.containing(pos)));
    }

    private static float partial(int prev, int now, float partialTick) {
        return prev + (now - prev) * partialTick;
    }

    // ---------------------------------------------------------------- the tick

    private static void tick(Minecraft mc) {
        ClientLevel level = mc.level;
        Entity viewer = mc.getCameraEntity();
        if (level == null || viewer == null || mc.isPaused()) {
            return;
        }
        LOTRCloudRenderer.updateClouds();
        if (LOTRConfig.aurora) {
            LOTRAuroraRenderer.update(level, viewer);
        }
        int i = Mth.floor(viewer.getX());
        int j = Mth.floor(viewer.getBoundingBox().minY);
        int k = Mth.floor(viewer.getZ());
        BlockPos pos = new BlockPos(i, j, k);
        LOTRBiome biome = isMiddleEarth(level) ? LOTRBiomes.of(level.getBiome(pos)) : null;
        prevMistTick = mistTick;
        if (viewer.getY() >= 72.0 && biome instanceof LOTRMistyMountainsBiome && biome != LOTRBiomes.MISTY_MOUNTAINS_FOOTHILLS
                && level.canSeeSky(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 7) {
            if (mistTick < 80) {
                ++mistTick;
            }
        } else if (mistTick > 0) {
            --mistTick;
        }
        prevWightLookTick = wightLookTick;
        if (level.getGameTime() - wightViewedTick <= 1L) {
            if (wightLookTick < 100) {
                ++wightLookTick;
            }
        } else if (wightLookTick > 0) {
            --wightLookTick;
        }
        prevWightNearTick = wightNearTick;
        double wightRange = 32.0;
        if (!level.getEntitiesOfClass(LOTRBarrowWightEntity.class, viewer.getBoundingBox().inflate(wightRange)).isEmpty()) {
            if (wightNearTick < 100) {
                ++wightNearTick;
            }
        } else if (wightNearTick > 0) {
            --wightNearTick;
        }
        prevBalrogNearTick = balrogNearTick;
        double balrogRange = 24.0;
        if (!level.getEntitiesOfClass(net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno.LOTRBalrogEntity.class,
                viewer.getBoundingBox().inflate(balrogRange)).isEmpty()) {
            if (balrogNearTick < 100) {
                ++balrogNearTick;
            }
        } else if (balrogNearTick > 0) {
            --balrogNearTick;
        }
        tickSunGlare(mc, level, viewer, biome);
        if (LOTRConfig.newWeather) {
            prevRainFactor = rainFactor;
            if (level.isRaining()) {
                if (rainFactor < 1.0f) {
                    rainFactor = Math.min(rainFactor + 0.008333334f, 1.0f);
                }
            } else if (rainFactor > 0.0f) {
                rainFactor = Math.max(rainFactor - 0.0016666667f, 0.0f);
            }
        } else {
            prevRainFactor = rainFactor = 0.0f;
        }
    }

    /** The sun's glare: rising while one looks within a few degrees of the sun, under open sky, by day. */
    private static void tickSunGlare(Minecraft mc, ClientLevel level, Entity viewer, @Nullable LOTRBiome biome) {
        if (!LOTRConfig.enableSunFlare || !isMiddleEarth(level)) {
            prevSunGlare = sunGlare = 0.0f;
            return;
        }
        prevSunGlare = sunGlare;
        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        boolean lookingAtSky = viewer.pick(10000.0, partialTick, false).getType() == HitResult.Type.MISS;
        boolean biomeHasSun = biome == null || biome.hasSky();
        float celestialAngle = sunAngle(mc, partialTick) / 360.0f;
        float sunPitch = celestialAngle * 360.0f - 90.0f;
        float sunYaw = 90.0f;
        float yc = Mth.cos((float) Math.toRadians(-sunYaw - 180.0f));
        float ys = Mth.sin((float) Math.toRadians(-sunYaw - 180.0f));
        float pc = -Mth.cos((float) Math.toRadians(-sunPitch));
        float ps = Mth.sin((float) Math.toRadians(-sunPitch));
        Vec3 sunVec = new Vec3(ys * pc, ps, yc * pc);
        Vec3 lookVec = viewer.getViewVector(partialTick);
        double cos = lookVec.dot(sunVec) / lookVec.length() * sunVec.length();
        float cosThreshold = 0.95f;
        float cQ = Math.max(((float) cos - cosThreshold) / (1.0f - cosThreshold), 0.0f);
        float brightness = sunBrightness(level, celestialAngle, partialTick);
        float brightnessThreshold = 0.7f;
        float bQ = Math.max((brightness - brightnessThreshold) / (1.0f - brightnessThreshold), 0.0f);
        float maxGlare = cQ * bQ;
        if (maxGlare > 0.0f && lookingAtSky && !level.isRaining() && biomeHasSun) {
            if (sunGlare < maxGlare) {
                sunGlare = Math.min(sunGlare + 0.1f * maxGlare, maxGlare);
            } else if (sunGlare > maxGlare) {
                sunGlare = Math.max(sunGlare - 0.02f, maxGlare);
            }
        } else {
            if (sunGlare > 0.0f) {
                sunGlare -= 0.02f;
            }
            sunGlare = Math.max(sunGlare, 0.0f);
        }
    }

    public static float sunAngle(Minecraft mc, float partialTick) {
        return mc.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, partialTick);
    }

    /** World.getSunBrightness, as 1.7.10 worked it out from the sun's angle, the rain and the thunder. */
    public static float sunBrightness(ClientLevel level, float celestialAngle, float partialTick) {
        float f1 = 1.0f - (Mth.cos(celestialAngle * (float) Math.PI * 2.0f) * 2.0f + 0.2f);
        f1 = Mth.clamp(f1, 0.0f, 1.0f);
        f1 = 1.0f - f1;
        f1 *= 1.0f - level.getRainLevel(partialTick) * 5.0f / 16.0f;
        f1 *= 1.0f - level.getThunderLevel(partialTick) * 5.0f / 16.0f;
        return f1 * 0.8f + 0.2f;
    }

    // ---------------------------------------------------------------- colours

    /** The camera's sky, fog and cloud colours in Middle-earth, as the original's biomes and wights tinted them. */
    @SuppressWarnings("unchecked")
    public static <V> V modifyAttribute(EnvironmentAttribute<V> attribute, V value, Level level, Vec3 pos, float partialTick) {
        if (attribute != EnvironmentAttributes.SKY_COLOR && attribute != EnvironmentAttributes.FOG_COLOR
                && attribute != EnvironmentAttributes.CLOUD_COLOR) {
            return value;
        }
        if (net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno.isUtumno(level)) {
            return attribute == EnvironmentAttributes.FOG_COLOR ? (V) (Integer) utumnoFogColor((Integer) value, pos.y) : value;
        }
        LOTRBiome biome = viewerBiome(level, pos);
        if (biome == null) {
            return value;
        }
        int color = (Integer) value;
        float wightFactor = partial(prevWightNearTick, wightNearTick, partialTick) / 100.0f;
        boolean barrowWight = biome instanceof LOTRBarrowDownsBiome && wightFactor > 0.0f;
        if (attribute == EnvironmentAttributes.SKY_COLOR) {
            if (LOTRScrapTraderMisbehaviour.isMisbehaving()) {
                return (V) (Integer) ARGB.color(ARGB.alpha(color), 0, 0, 0);
            }
            if (barrowWight) {
                color = ARGB.color(ARGB.alpha(color), lerpColor(color, 9674385, wightFactor));
            }
            return (V) (Integer) color;
        }
        Integer tint = attribute == EnvironmentAttributes.FOG_COLOR ? biome.biomeColors.fog : biome.biomeColors.clouds;
        if (barrowWight) {
            tint = attribute == EnvironmentAttributes.FOG_COLOR ? lerpColor(16777215, 10197915, wightFactor)
                    : lerpColor(16777215, 11842740, wightFactor);
        }
        if (tint != null) {
            color = ARGB.color(ARGB.alpha(color), ARGB.red(color) * ARGB.red(tint) / 255, ARGB.green(color) * ARGB.green(tint) / 255,
                    ARGB.blue(color) * ARGB.blue(tint) / 255);
        }
        return (V) (Integer) color;
    }

    /**
     * LOTRBiomeGenUtumno.updateFogColor and LOTRWorldProviderUtumno.handleFinalFogColors: the fog in the
     * level's own colour; on the fire level ever brighter towards its bottom corridors.
     */
    private static int utumnoFogColor(int color, double y) {
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel level =
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel.forY((int) y);
        int tint = level.fogColor;
        color = ARGB.color(ARGB.alpha(color), ARGB.red(color) * ARGB.red(tint) / 255, ARGB.green(color) * ARGB.green(tint) / 255,
                ARGB.blue(color) * ARGB.blue(tint) / 255);
        if (level == net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel.FIRE) {
            int min = level.getLowestCorridorFloor();
            int max = level.getHighestCorridorRoof();
            max = (int) (max - (max - min) * 0.3);
            double yFactor = 1.0 - Mth.clamp((y - min) / (max - min), 0.0, 1.0);
            float[] hsb = java.awt.Color.RGBtoHSB(ARGB.red(color), ARGB.green(color), ARGB.blue(color), null);
            float br = hsb[2];
            if (br > 0.0f) {
                hsb[2] = (float) (br + (1.0f - br) * yFactor);
            }
            color = ARGB.color(ARGB.alpha(color), java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0xFFFFFF);
        }
        return color;
    }

    /** LOTRColorUtil.lerpColors_I. */
    private static int lerpColor(int color0, int color1, float f) {
        int r = Math.round(ARGB.red(color0) + (ARGB.red(color1) - ARGB.red(color0)) * f);
        int g = Math.round(ARGB.green(color0) + (ARGB.green(color1) - ARGB.green(color0)) * f);
        int b = Math.round(ARGB.blue(color0) + (ARGB.blue(color1) - ARGB.blue(color0)) * f);
        return ARGB.color(0, r, g, b) & 0xFFFFFF;
    }

    // ---------------------------------------------------------------- fog

    /** onRenderFog: the fog closing in, after today's has been worked out. */
    public static void modifyFog(FogData fog, ClientLevel level, Entity viewer, float renderDistance, float partialTick) {
        if (!isMiddleEarth(level) && !net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno.isUtumno(level)) {
            return;
        }
        LOTRBiome biome = viewerBiome(level, viewer.position());
        boolean thicker = false;
        float fogStart = fog.environmentalStart;
        float fogEnd = fog.environmentalEnd;
        if (biome != null && biome.hasFog()) {
            fogStart = renderDistance * 0.05f;
            fogEnd = Math.min(renderDistance, 192.0f) * 0.5f;
            thicker = true;
        }
        float rain;
        if (LOTRConfig.newWeather && biome != null && (biome.enableRain || biome.enableSnow)
                && (rain = prevRainFactor + (rainFactor - prevRainFactor) * partialTick) > 0.0f) {
            fogStart = Math.min(fogStart, fog.renderDistanceStart);
            fogEnd = Math.min(fogEnd, renderDistance);
            fogStart -= fogStart * (rain * 0.95f);
            fogEnd -= fogEnd * (rain * 0.2f);
            thicker = true;
        }
        if (mistFactor > 0.0f) {
            fogStart = Math.min(fogStart, fog.renderDistanceStart);
            fogEnd = Math.min(fogEnd, renderDistance);
            fogStart -= fogStart * (mistFactor * 0.95f);
            fogEnd -= fogEnd * (mistFactor * 0.7f);
            thicker = true;
        }
        float wightFactor = partial(prevWightNearTick, wightNearTick, partialTick) / 100.0f;
        if (wightFactor > 0.0f) {
            fogStart = Math.min(fogStart, fog.renderDistanceStart);
            fogEnd = Math.min(fogEnd, renderDistance);
            fogStart -= fogStart * (wightFactor * 0.97f);
            fogEnd -= fogEnd * (wightFactor * 0.75f);
            thicker = true;
        }
        float balrogFactor = partial(prevBalrogNearTick, balrogNearTick, partialTick) / 100.0f;
        if (balrogFactor > 0.0f) {
            fogStart = Math.min(fogStart, fog.renderDistanceStart);
            fogEnd = Math.min(fogEnd, renderDistance);
            fogStart -= fogStart * (balrogFactor * 0.98f);
            fogEnd -= fogEnd * (balrogFactor * 0.75f);
            thicker = true;
        }
        if (thicker) {
            fog.environmentalStart = fogStart;
            fog.environmentalEnd = fogEnd;
            fog.skyEnd = Math.min(fog.skyEnd, fogEnd);
            fog.cloudEnd = Math.min(fog.cloudEnd, fogEnd);
        }
    }

    // ---------------------------------------------------------------- overlays

    /** The sun's glare (first person only), then the mountains' mist (with "Misty Mountains mist"). */
    private static void renderOverlays(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        if (sunGlare > 0.0f && mc.options.getCameraType().isFirstPerson()) {
            float brightness = prevSunGlare + (sunGlare - prevSunGlare) * partialTicks;
            graphics.fill(0, 0, width, height, ARGB.white(brightness));
        }
        if (LOTRConfig.enableMistyMountainsMist) {
            float mistTickF = partial(prevMistTick, mistTick, partialTicks) / 80.0f;
            float mistFactorY = (float) player.getY() / 256.0f;
            mistFactor = mistTickF * mistFactorY;
            if (mistFactor > 0.0f) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, MIST_OVERLAY, 0, 0, 0.0f, 0.0f, width, height, width, height, ARGB.white(mistFactor * 0.75f));
            }
        } else {
            mistFactor = 0.0f;
        }
    }

    /** A wight's darkness on the one it hunts. */
    private static void renderWightOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (wightLookTick > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIGHT_OVERLAY, 0, 0, 0.0f, 0.0f, graphics.guiWidth(), graphics.guiHeight(),
                    graphics.guiWidth(), graphics.guiHeight(), ARGB.white(wightLookTick / 100.0f * 0.95f));
        }
    }
}
