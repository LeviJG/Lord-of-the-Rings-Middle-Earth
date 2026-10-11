package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPortalEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTickHandlerClient's side of the portals: the player's time in the Ring portal or a built one,
 * drawn as that portal's overlay thickening over the screen, and the portal's trigger sound when the
 * time is up; and the tick count the Morgul portal's glow keeps time by.
 */
public final class LOTRPortalsClient {

    private static final Identifier PORTAL_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/portal_overlay.png");
    private static final Identifier ELVEN_PORTAL_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/elvenportal_overlay.png");
    private static final Identifier MORGUL_PORTAL_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/morgulportal_overlay.png");

    /** LOTRTickHandlerClient.clientTick. */
    public static int clientTick;

    /** The local player's time in each kind of portal; -1 when not in one. */
    private static int inPortal = -1;
    private static int inElvenPortal = -1;
    private static int inMorgulPortal = -1;

    private LOTRPortalsClient() {
    }

    public static void init() {
        LOTRPortals.clientInPortal = LOTRPortalsClient::setInPortal;
        ClientTickEvents.END_CLIENT_TICK.register(LOTRPortalsClient::tick);
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "portal_overlays"), LOTRPortalsClient::render);
    }

    private static void setInPortal(Player player, @Nullable Block portal) {
        if (player != Minecraft.getInstance().player) {
            return;
        }
        if (portal == null) {
            inPortal = Math.max(inPortal, 0);
        } else if (portal == LOTRUtilityBlocks.ELVEN_PORTAL) {
            inElvenPortal = Math.max(inElvenPortal, 0);
        } else if (portal == LOTRUtilityBlocks.MORGUL_PORTAL) {
            inMorgulPortal = Math.max(inMorgulPortal, 0);
        }
    }

    /** getMaxInPortalTime, as the game rules stand by default (the client is not told them). */
    private static int maxInPortalTime(Player player) {
        return player.getAbilities().invulnerable ? 0 : 80;
    }

    private static void tick(Minecraft mc) {
        if (mc.isPaused()) {
            return;
        }
        ++clientTick;
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            inPortal = inElvenPortal = inMorgulPortal = -1;
            return;
        }
        boolean portalWorld = mc.level.dimension() == Level.OVERWORLD || mc.level.dimension() == LOTRWorldGen.MIDDLE_EARTH;
        if (portalWorld && inPortal >= 0) {
            boolean in = false;
            for (LOTRPortalEntity portal : mc.level.getEntitiesOfClass(LOTRPortalEntity.class, player.getBoundingBox().inflate(8.0))) {
                if (portal.getBoundingBox().intersects(player.getBoundingBox())) {
                    in = true;
                    break;
                }
            }
            if (in && ++inPortal >= 100) {
                playTrigger(mc);
                inPortal = -1;
            } else if (!in) {
                inPortal = -1;
            }
        }
        if (portalWorld) {
            inElvenPortal = updatePlayerInPortal(mc, player, inElvenPortal, LOTRUtilityBlocks.ELVEN_PORTAL);
            inMorgulPortal = updatePlayerInPortal(mc, player, inMorgulPortal, LOTRUtilityBlocks.MORGUL_PORTAL);
        }
    }

    private static int updatePlayerInPortal(Minecraft mc, LocalPlayer player, int time, Block portalBlock) {
        if (time < 0) {
            return time;
        }
        if (!mc.level.getBlockState(BlockPos.containing(player.getX(), player.getBoundingBox().minY, player.getZ())).is(portalBlock)) {
            return -1;
        }
        if (++time >= maxInPortalTime(player)) {
            playTrigger(mc);
            return -1;
        }
        return time;
    }

    private static void playTrigger(Minecraft mc) {
        mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRIGGER,
                mc.level.getRandom().nextFloat() * 0.4f + 0.8f, 0.25f));
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (inPortal > 0) {
            overlay(graphics, PORTAL_OVERLAY, 0.1f + inPortal / 100.0f * 0.6f);
        }
        int max = Math.max(1, maxInPortalTime(player));
        if (inElvenPortal > 0) {
            overlay(graphics, ELVEN_PORTAL_OVERLAY, 0.1f + (float) inElvenPortal / max * 0.6f);
        }
        if (inMorgulPortal > 0) {
            overlay(graphics, MORGUL_PORTAL_OVERLAY, 0.1f + (float) inMorgulPortal / max * 0.6f);
        }
    }

    /** renderOverlay: the texture stretched over the screen. */
    private static void overlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha) {
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0.0f, 0.0f, width, height, width, height, ARGB.white(alpha));
    }
}
