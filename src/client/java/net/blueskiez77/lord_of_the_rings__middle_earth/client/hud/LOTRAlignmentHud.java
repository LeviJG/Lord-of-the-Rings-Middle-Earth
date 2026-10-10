package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMessageScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAlignmentBonusEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentHudPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * LOTRTickHandlerClient.renderAlignment: the alignment bar of the viewing
 * faction at the top of the screen, sliding up out of sight while a screen
 * (other than a message), the player list or the debug screen is up; the
 * drain icon beside it for ten seconds after a drain. Also the arrival of
 * the alignment popups (LOTRPacketAlignmentBonus).
 *
 * <p>Drawn only in Middle-earth unless "Always show alignment" is set.
 *
 * <p>With it, the on-screen compass in the top right (LOTRModelCompass), with
 * the player's coordinates beneath and the LOTR biome's name above if the
 * config asks.
 *
 * <p>The bar moves down out of the way of the boss bars, and of a watched invasion's bar.
 */
public final class LOTRAlignmentHud {

    /** alignDrainTickMax. */
    private static final int ALIGN_DRAIN_TICK_MAX = 200;

    private static int alignDrainTick;
    private static int alignDrainNum;
    private static int alignmentXBase;
    private static int alignmentYBase;
    private static int alignmentXCurrent;
    private static int alignmentYCurrent;
    private static int alignmentXPrev;
    private static int alignmentYPrev;
    private static boolean firstAlignmentRender = true;

    private LOTRAlignmentHud() {
    }

    public static void init() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment"), LOTRAlignmentHud::render);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !client.isPaused() && alignDrainTick > 0) {
                --alignDrainTick;
                if (alignDrainTick <= 0) {
                    alignDrainNum = 0;
                }
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> firstAlignmentRender = true);
        ClientPlayNetworking.registerGlobalReceiver(LOTRAlignmentHudPayloads.AlignDrain.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    alignDrainTick = ALIGN_DRAIN_TICK_MAX;
                    alignDrainNum = payload.numFactions();
                }));
        // spawnAlignmentBonus: the popup, made here alone.
        ClientPlayNetworking.registerGlobalReceiver(LOTRAlignmentHudPayloads.AlignmentBonus.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    var level = context.client().level;
                    if (level == null || payload.mainFaction() == null) {
                        return;
                    }
                    String name = payload.needsTranslation() ? Component.translatable(payload.name()).getString() : payload.name();
                    LOTRAlignmentBonusEntity entity = LOTREntities.ALIGNMENT_BONUS.create(level, EntitySpawnReason.TRIGGERED);
                    if (entity != null) {
                        entity.setup(payload.posX(), payload.posY(), payload.posZ(), name, payload.mainFaction(),
                                payload.prevMainAlignment(), payload.factionBonusMap(), payload.isKill(),
                                payload.isHiredKill(), payload.conquestBonus());
                        level.addEntity(entity);
                    }
                }));
    }

    /** onRenderTick's alignment half, a frame at a time as the original moved it. */
    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null
                || !(mc.level.dimension() == LOTRWorldGen.MIDDLE_EARTH || LOTRConfig.alwaysShowAlignment)) {
            return;
        }
        alignmentXPrev = alignmentXCurrent;
        alignmentYPrev = alignmentYCurrent;
        alignmentXCurrent = alignmentXBase;
        int yMove = (int) ((alignmentYBase + 20) / 10.0f);
        boolean alignmentOnscreen = (mc.gui.screen() == null || mc.gui.screen() instanceof LOTRMessageScreen)
                && !mc.options.keyPlayerList.isDown() && !mc.getDebugOverlay().showDebugScreen();
        if (alignmentOnscreen) {
            alignmentYCurrent = Math.min(alignmentYCurrent + yMove, alignmentYBase);
        } else {
            alignmentYCurrent = Math.max(alignmentYCurrent - yMove, -20);
        }
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        renderAlignment(graphics, mc, partialTick);
        if (LOTRConfig.enableOnscreenCompass && mc.gui.screen() == null && !mc.getDebugOverlay().showDebugScreen()) {
            renderCompass(graphics, mc, partialTick);
        }
    }

    private static void renderCompass(GuiGraphicsExtractor graphics, Minecraft mc, float partialTick) {
        int compassX = graphics.guiWidth() - 60;
        int compassY = 40;
        float rotation = 180.0f - mc.player.getViewYRot(partialTick);
        int half = 50;
        graphics.guiRenderState.addPicturesInPictureState(new LOTRCompassRenderer.State(rotation,
                compassX - half, compassY - half, compassX + half, compassY + half));
        if (LOTRConfig.compassExtraInfo) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(0.5f, 0.5f);
            int x = compassX * 2;
            int y = compassY * 2;
            String coords = Mth.floor(mc.player.getX()) + ", " + Mth.floor(mc.player.getBoundingBox().minY) + ", "
                    + Mth.floor(mc.player.getZ());
            graphics.text(mc.font, coords, x - mc.font.width(coords) / 2, y + 70, 0xFFFFFFFF, false);
            BlockPos pos = BlockPos.containing(mc.player.getX(), 0.0, mc.player.getZ());
            if (mc.level.hasChunkAt(pos)) {
                LOTRBiome biome = LOTRBiomes.of(mc.level.getBiome(pos));
                if (biome != null) {
                    Component biomeName = biome.getBiomeDisplayName();
                    graphics.text(mc.font, biomeName, x - mc.font.width(biomeName) / 2, y - 70, 0xFFFFFFFF, false);
                }
            }
            graphics.pose().popMatrix();
        }
    }

    private static void renderAlignment(GuiGraphicsExtractor graphics, Minecraft mc, float f) {
        LOTRFaction viewingFac = LOTRViewingFaction.getViewingFaction(mc.player);
        int width = graphics.guiWidth();
        alignmentXBase = width / 2 + LOTRConfig.alignmentXOffset;
        alignmentYBase = 4 + LOTRConfig.alignmentYOffset;
        // isBossActive: below a boss's bar. 1.7.10 had only the one; modern
        // vanilla stacks them nineteen apart, so the bar goes below them all.
        int bossBars = mc.gui.hud.getBossOverlay().events.size();
        if (bossBars > 0) {
            alignmentYBase += 20 + 19 * (bossBars - 1);
        }
        if (LOTRInvasionHud.isActive()) {
            alignmentYBase += 20;
        }
        if (firstAlignmentRender) {
            LOTRAlignmentTicker.updateAll(mc.player, true);
            alignmentXPrev = alignmentXCurrent = alignmentXBase;
            alignmentYPrev = alignmentYCurrent = -20;
            firstAlignmentRender = false;
        }
        float alignmentXF = alignmentXPrev + (alignmentXCurrent - alignmentXPrev) * f;
        float alignmentYF = alignmentYPrev + (alignmentYCurrent - alignmentYPrev) * f;
        boolean text = alignmentYCurrent == alignmentYBase;
        float alignment = LOTRAlignmentTicker.forFaction(viewingFac).getInterpolatedAlignment(f);
        LOTRAlignmentBarRenderer.renderAlignmentBar(graphics, alignment, false, viewingFac, alignmentXF, alignmentYF,
                text, text, text, false);
        if (alignDrainTick > 0 && text) {
            float alpha = alignDrainTick < 20 ? 0.0f : 1.0f;
            LOTRAlignmentBarRenderer.renderAlignmentDrain(graphics, mc.font, (int) alignmentXF - 155, (int) alignmentYF + 2,
                    alignDrainNum, alpha);
        }
    }
}
