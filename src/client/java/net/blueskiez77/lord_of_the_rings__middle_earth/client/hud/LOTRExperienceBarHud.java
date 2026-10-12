package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.PlayerRideableJumping;

/**
 * LOTRTickHandlerClient.onPreRenderGameOverlay: in Middle-earth or Utumno (a
 * LOTRWorldProvider) on a server with vanilla enchanting off, experience is of
 * no use, so its bar and level are not drawn (EXPERIENCE cancelled), and when
 * the player rides nothing the rows above it -- health, armour, food, air --
 * come down six pixels into its place (GuiIngameForge left_height and
 * right_height less 6).
 */
public final class LOTRExperienceBarHud {

    private static final float ROW_DROP = 6.0F;

    private LOTRExperienceBarHud() {
    }

    public static void init() {
        // The contextual bar is the experience bar unless a jumpable mount puts its jump bar there.
        HudElementRegistry.replaceElement(VanillaHudElements.INFO_BAR, bar -> (graphics, deltaTracker) -> {
            if (!enchantingDisabled() || ridingJumpable()) {
                bar.extractRenderState(graphics, deltaTracker);
            }
        });
        HudElementRegistry.replaceElement(VanillaHudElements.EXPERIENCE_LEVEL, level -> (graphics, deltaTracker) -> {
            if (!enchantingDisabled()) {
                level.extractRenderState(graphics, deltaTracker);
            }
        });
        dropRow(VanillaHudElements.HEALTH_BAR);
        dropRow(VanillaHudElements.ARMOR_BAR);
        dropRow(VanillaHudElements.FOOD_BAR);
        dropRow(VanillaHudElements.AIR_BAR);
    }

    private static void dropRow(Identifier id) {
        HudElementRegistry.replaceElement(id, row -> (graphics, deltaTracker) -> {
            Minecraft mc = Minecraft.getInstance();
            if (enchantingDisabled() && mc.player != null && mc.player.getVehicle() == null) {
                graphics.pose().pushMatrix();
                graphics.pose().translate(0.0F, ROW_DROP);
                row.extractRenderState(graphics, deltaTracker);
                graphics.pose().popMatrix();
            } else {
                row.extractRenderState(graphics, deltaTracker);
            }
        });
    }

    /** !LOTRLevelData.clientside_thisServer_enchanting && worldClient.provider instanceof LOTRWorldProvider */
    public static boolean enchantingDisabled() {
        Minecraft mc = Minecraft.getInstance();
        return !LOTRConfig.clientside_thisServer_enchanting && mc.level != null
                && (mc.level.dimension() == LOTRWorldGen.MIDDLE_EARTH || mc.level.dimension() == LOTRWorldGen.UTUMNO);
    }

    private static boolean ridingJumpable() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getVehicle() instanceof PlayerRideableJumping mount && mount.canJump();
    }
}
