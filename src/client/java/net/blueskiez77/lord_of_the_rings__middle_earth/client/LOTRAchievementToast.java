package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRAchievementsScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiNotificationDisplay.NotificationAchievement: an achievement just earned, in the corner of the
 * screen for three seconds -- "Achievement get!", its title and icon, and the earned tick.
 */
public class LOTRAchievementToast implements Toast {

    private static final long DURATION_MS = 3000L;

    private final LOTRAchievement achievement;
    private Visibility wantedVisibility = Visibility.SHOW;

    public LOTRAchievementToast(LOTRAchievement achievement) {
        this.achievement = achievement;
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRPlayerAchievements.EarnedPayload.TYPE, (payload, context) -> {
            LOTRAchievement achievement = payload.achievement();
            if (achievement != null) {
                context.client().gui.toastManager().addToast(new LOTRAchievementToast(achievement));
            }
        });
    }

    @Override
    public int width() {
        return 190;
    }

    @Override
    public int height() {
        return 32;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        this.wantedVisibility = fullyVisibleForMs >= DURATION_MS ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRAchievementsScreen.ICONS, 0, 0, 0.0F, 200.0F, 190, 32, 256, 256);
        graphics.text(font, Component.translatable("lotr.achievement.get"), 30, 7, 0xFF000000 | 8019267, false);
        graphics.text(font, this.achievement.getTitle(Minecraft.getInstance().player), 30, 18, 0xFF000000 | 8019267, false);
        graphics.item(this.achievement.getIcon(), 8, 8);
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRAchievementsScreen.ICONS, 170, 9, 190.0F, 17.0F, 16, 16, 256, 256);
    }
}
