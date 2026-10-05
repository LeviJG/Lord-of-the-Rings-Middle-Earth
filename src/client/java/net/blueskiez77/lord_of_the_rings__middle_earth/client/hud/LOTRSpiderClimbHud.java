package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRSpiderEntity;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * LOTRTickHandlerClient's spider climbing meter: riding a spider that is
 * climbing, the player sees how much of its climb is left in the jump bar's
 * place -- full at the foot of the wall, empty when it must come down.
 */
public final class LOTRSpiderClimbHud {

    private static final Identifier BACKGROUND = Identifier.withDefaultNamespace("hud/jump_bar_background");
    private static final Identifier PROGRESS = Identifier.withDefaultNamespace("hud/jump_bar_progress");

    private LOTRSpiderClimbHud() {
    }

    public static void init() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.INFO_BAR,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "spider_climb"), LOTRSpiderClimbHud::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(minecraft.player.getVehicle() instanceof LOTRSpiderEntity spider)
                || !spider.shouldRenderClimbingMeter()) {
            return;
        }
        int x = graphics.guiWidth() / 2 - 91;
        int top = graphics.guiHeight() - 32 + 3;
        int filled = (int) (spider.getClimbFractionRemaining() * 183.0f);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, top, 182, 5);
        if (filled > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS, 182, 5, 0, 0, x, top, Math.min(filled, 182), 5);
        }
    }
}
