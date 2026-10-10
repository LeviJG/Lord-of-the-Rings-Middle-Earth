package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRInvasionSpawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRInvasionPayloads;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import org.jspecify.annotations.Nullable;

/**
 * LOTRInvasionStatus and its bar (LOTRTickHandlerClient): the invasion a player is watching -- the
 * one they were told of, or last struck an invader of -- drawn as a bar of its faction's colour
 * below the boss bars, its name above it, until it is gone or 30 seconds pass with no word of it.
 */
public final class LOTRInvasionHud {

    private static @Nullable LOTRInvasionSpawnerEntity watchedInvasion;
    private static int ticksSinceRelevance;

    private LOTRInvasionHud() {
    }

    public static boolean isActive() {
        return watchedInvasion != null;
    }

    public static void clear() {
        watchedInvasion = null;
        ticksSinceRelevance = 0;
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRInvasionPayloads.Watch.TYPE, (payload, context) -> {
            Entity e = context.client().level == null ? null : context.client().level.getEntity(payload.invasionEntityID());
            if ((payload.overrideAlreadyWatched() || !isActive()) && e instanceof LOTRInvasionSpawnerEntity invasion) {
                watchedInvasion = invasion;
                ticksSinceRelevance = 0;
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (watchedInvasion != null && !client.isPaused()) {
                if (watchedInvasion.isRemoved()) {
                    clear();
                } else if (++ticksSinceRelevance >= 600) {
                    clear();
                }
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> clear());
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "invasion"), LOTRInvasionHud::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LOTRInvasionSpawnerEntity invasion = watchedInvasion;
        if (invasion == null || mc.player == null) {
            return;
        }
        int width = graphics.guiWidth();
        int barWidth = 182;
        int remainingWidth = (int) (invasion.getInvasionHealthStatus() * (barWidth - 2));
        int barHeight = 5;
        int barX = width / 2 - barWidth / 2;
        int barY = 12;
        // isBossActive: below the boss bars, which modern vanilla stacks nineteen apart.
        int bossBars = mc.gui.hud.getBossOverlay().events.size();
        if (bossBars > 0) {
            barY += 20 + 19 * (bossBars - 1);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRAlignmentBarRenderer.ALIGNMENT, barX, barY, 64.0F, 64.0F, barWidth, barHeight, 256, 256);
        if (remainingWidth > 0) {
            float[] rgb = invasion.getInvasionType().invasionFaction.getFactionRGB_MinBrightness(0.45f);
            int tint = 0xFF000000 | (int) (rgb[0] * 255.0f) << 16 | (int) (rgb[1] * 255.0f) << 8 | (int) (rgb[2] * 255.0f);
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRAlignmentBarRenderer.ALIGNMENT, barX + 1, barY + 1, 65.0F, 70.0F,
                    remainingWidth, barHeight - 2, 256, 256, tint);
        }
        Component title = invasion.getInvasionType().invasionName();
        graphics.text(mc.font, title, width / 2 - mc.font.width(title) / 2, barY - 10, 0xFFFFFFFF, true);
    }
}
