package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentHudPayloads;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * LOTRTickHandlerClient's FROST and BURN overlays: four seconds of frost at
 * the screen's top and bottom edges, fading, after a chill (or, later, frost
 * damage); two of burning after the desert's heat.
 *
 * <p>NOT ported yet, with their sources: frost damage itself
 * (LOTRDamage.frost) and Utumno's ice wargs and spiders (D10), and the
 * desert heat that burns (D10) -- until then only the chilling modifier
 * frosts the screen and nothing burns it.
 */
public final class LOTREnvironmentOverlayHud {

    private static final Identifier FROST_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/frost_overlay.png");
    private static final Identifier BURN_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "misc/burn_overlay.png");
    private static final float[] FROST_RGB_MIDDLE = {0.4f, 0.46f, 0.74f};
    private static final float[] FROST_RGB_EDGE = {1.0f, 1.0f, 1.0f};

    private static int frostTick;
    private static int burnTick;

    private LOTREnvironmentOverlayHud() {
    }

    public static void init() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "environment_overlays"), LOTREnvironmentOverlayHud::render);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !client.isPaused()) {
                if (frostTick > 0) {
                    --frostTick;
                }
                if (burnTick > 0) {
                    --burnTick;
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRAlignmentHudPayloads.EnvironmentOverlay.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (payload.overlay() == LOTRAlignmentHudPayloads.EnvironmentOverlay.FROST) {
                        frostTick = 80;
                    } else if (payload.overlay() == LOTRAlignmentHudPayloads.EnvironmentOverlay.BURN) {
                        burnTick = 40;
                    }
                }));
    }

    private static int argb(float[] rgb, float alpha) {
        return Mth.clamp((int) (alpha * 255.0f), 0, 255) << 24 | (int) (rgb[0] * 255.0f) << 16
                | (int) (rgb[1] * 255.0f) << 8 | (int) (rgb[2] * 255.0f);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        if (frostTick > 0) {
            float frostAlpha = frostTick / 80.0f * 0.9f;
            float frostAlphaEdge = (float) Math.sqrt(frostAlpha);
            // renderOverlayWithVerticalGradients: edge to middle down the top third, middle across, middle to edge below.
            int edge = argb(FROST_RGB_EDGE, frostAlphaEdge);
            int centre = argb(FROST_RGB_MIDDLE, frostAlpha);
            int third = height / 3;
            int twoThirds = height * 2 / 3;
            graphics.fillGradient(0, 0, width, third, edge, centre);
            graphics.fillGradient(0, third, width, twoThirds, centre, centre);
            graphics.fillGradient(0, twoThirds, width, height, centre, edge);
            renderOverlay(graphics, FROST_OVERLAY, frostAlpha * 0.6f, width, height);
        }
        if (burnTick > 0) {
            renderOverlay(graphics, BURN_OVERLAY, burnTick / 40.0f * 0.6f, width, height);
        }
    }

    /** renderOverlay: the texture stretched over the whole screen. */
    private static void renderOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0.0F, 0.0F, width, height, 256, 256, 256, 256,
                argb(new float[]{1.0f, 1.0f, 1.0f}, alpha));
    }
}
