package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBannerItem;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * LOTRTickHandlerClient's banner repossess hint (bannerRepossessDisplayTick):
 * a creative player who takes up a banner still carrying protection data is
 * told for three seconds that sneaking while placing keeps its previous owner.
 */
public final class LOTRBannerRepossessHud {

    private static int bannerRepossessDisplayTick;
    private static boolean wasShowingBannerRepossessMessage;

    private LOTRBannerRepossessHud() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.isPaused()) {
                return;
            }
            if (LOTRBannerItem.hasChoiceToKeepOriginalOwner(client.player)) {
                boolean show = LOTRBannerItem.isHoldingBannerWithExistingProtection(client.player);
                if (show && !wasShowingBannerRepossessMessage) {
                    bannerRepossessDisplayTick = 60;
                } else {
                    bannerRepossessDisplayTick--;
                }
                wasShowingBannerRepossessMessage = show;
            } else {
                bannerRepossessDisplayTick = 0;
                wasShowingBannerRepossessMessage = false;
            }
        });
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner_repossess"),
                LOTRBannerRepossessHud::render);
    }

    /** onPostRenderGameOverlay (TEXT): twelve pixels above the held item's name, fading out over the last ten ticks. */
    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (bannerRepossessDisplayTick <= 0 || mc.player == null) {
            return;
        }
        int fadeAtTick = 10;
        int opacity = Math.min((int) (bannerRepossessDisplayTick * 255.0F / fadeAtTick), 255);
        if (opacity <= 0) {
            return;
        }
        Component text = Component.translatable("item.lotr.banner.toggleRepossess", mc.options.keyShift.getTranslatedKeyMessage());
        int y = graphics.guiHeight() - 59;
        y -= 12;
        // playerController.shouldDrawHUD(): survival or adventure
        if (mc.player.isCreative() || mc.player.isSpectator()) {
            y += 14;
        }
        int x = (graphics.guiWidth() - mc.font.width(text)) / 2;
        graphics.text(mc.font, text, x, y, 0xFFFFFF | opacity << 24, false);
    }
}
