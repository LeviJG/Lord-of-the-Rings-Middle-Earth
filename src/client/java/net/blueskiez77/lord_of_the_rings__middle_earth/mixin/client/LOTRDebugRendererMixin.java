package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.AABB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTRRenderBanner's debug view: with the debug screen open, each protecting
 * banner outlines the land it protects in green, and -- with "Show permitted
 * banner silhouettes" -- one the player may do as they like under (or any, in
 * creative) shows as a solid green shape through blocks. The original drew
 * the banner model itself in green; the gizmo is the banner's space.
 */
@Mixin(DebugRenderer.class)
abstract class LOTRDebugRendererMixin {

    private static final int PROTECT_COLOUR = 0xFF00FF00;

    @Inject(method = "emitGizmos", at = @At("TAIL"))
    private void lotr$bannerProtection(Frustum frustum, double camX, double camY, double camZ, float partialTicks,
                                       CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || !mc.getDebugOverlay().showDebugScreen()) {
            return;
        }
        for (LOTRBannerBlockEntity banner : LOTRBannerBlockEntity.loadedIn(mc.level)) {
            if (banner.isRemoved() || !banner.isProtectingTerritory()) {
                continue;
            }
            Gizmos.cuboid(banner.createProtectionCube(), GizmoStyle.stroke(PROTECT_COLOUR));
            if (LOTRConfig.showPermittedBannerSilhouettes
                    && (mc.player.isCreative() || banner.clientsidePlayerHasPermissionInSurvival())) {
                AABB space = new AABB(banner.getBlockPos()).expandTowards(0.0, 1.0, 0.0).deflate(0.3, 0.0, 0.3);
                if (frustum.isVisible(space)) {
                    Gizmos.cuboid(space, GizmoStyle.fill(PROTECT_COLOUR)).setAlwaysOnTop();
                }
            }
        }
    }
}
