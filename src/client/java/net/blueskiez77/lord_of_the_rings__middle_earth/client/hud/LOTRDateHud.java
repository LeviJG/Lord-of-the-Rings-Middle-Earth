package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * LOTRPacketDate's client side and LOTRTickHandlerClient's newDate: the Shire Reckoning's day as the
 * server keeps it, and, when it changes, its long name fading in and out over ten seconds, half as
 * large again, two fifths of the way down the screen -- in Middle-earth, not over a screen.
 */
public final class LOTRDateHud {

    private static int newDate;

    private LOTRDateHud() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRLevelData.LOTRDatePayload.TYPE, (payload, context) -> {
            LOTRDate.ShireReckoning.currentDay = payload.shireDate();
            if (payload.update()) {
                newDate = 200;
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (newDate > 0 && !client.isPaused()) {
                --newDate;
            }
        });
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "new_date"), LOTRDateHud::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (newDate <= 0 || mc.player == null || mc.level == null || mc.level.dimension() != LOTRWorldGen.MIDDLE_EARTH
                || mc.gui.screen() != null) {
            return;
        }
        int halfMaxDate = 100;
        float alpha = newDate > halfMaxDate ? (float) (200 - newDate) / halfMaxDate : (float) newDate / halfMaxDate;
        String date = LOTRDate.ShireReckoning.getShireDate().getDateName(true);
        float scale = 1.5f;
        int w = (int) (graphics.guiWidth() / scale);
        int h = (int) (graphics.guiHeight() / scale);
        int x = (w - mc.font.width(date)) / 2;
        int y = (h - mc.font.lineHeight) * 2 / 5;
        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        graphics.text(mc.font, date, x, y, Mth.clamp((int) (alpha * 255.0f), 4, 255) << 24 | 0xFFFFFF, false);
        graphics.pose().popMatrix();
    }
}
