package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRAchievementsScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentBarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestGrid;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * LOTRGuiNotificationDisplay.NotificationConquest: the conquest of the zone the player stands in
 * passing a fifty, for six seconds -- the strength (pale when an enemy's is cleansed), "conquest in
 * this area", and the faction.
 */
public class LOTRConquestToast implements Toast {

    private static final long DURATION_MS = 6000L;
    /** LOTRClientProxy.alignmentTexture. */
    private static final Identifier ALIGNMENT = Identifier.fromNamespaceAndPath("lotr", "gui/alignment.png");

    private final LOTRFaction conqFac;
    private final float conqValue;
    private final boolean isCleansing;
    private Visibility wantedVisibility = Visibility.SHOW;

    public LOTRConquestToast(LOTRFaction fac, float conq, boolean clean) {
        this.conqFac = fac;
        this.conqValue = conq;
        this.isCleansing = clean;
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRConquestGrid.NotificationPayload.TYPE, (payload, context) -> {
            if (payload.faction() != null) {
                context.client().gui.toastManager().addToast(new LOTRConquestToast(payload.faction(), payload.conquest(), payload.cleansing()));
            }
        });
    }

    /** drawConquestText: bordered, pale for a cleansing, red for a conquest. */
    public static void drawConquestText(GuiGraphicsExtractor graphics, Font font, int x, int y, Component s, boolean cleanse, float alpha) {
        LOTRAlignmentBarRenderer.drawBorderedText(graphics, font, x, y, s, cleanse ? 16773846 : 14833677, alpha);
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
        String conqS = LOTRAlignmentValues.formatConqForDisplay(this.conqValue, false);
        drawConquestText(graphics, font, 31, 7, Component.literal(conqS), this.isCleansing, 1.0f);
        graphics.text(font, Component.translatable("lotr.gui.map.conquest.notif"), 30 + font.width(conqS + " ") + 2, 7,
                0xFF000000 | 8019267, false);
        graphics.text(font, this.conqFac.factionName().copy().withStyle(ChatFormatting.ITALIC), 30, 18, 0xFF000000 | 9666921, false);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, 8, 8, this.isCleansing ? 16.0F : 0.0F, 228.0F, 16, 16, 256, 256);
    }
}
