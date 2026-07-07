package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Minimal on-screen alignment readout (top-left corner). First real
 * client-side UI. Deliberately crude: shows a chosen "viewed faction" and
 * the player's current alignment + rank with it. A proper alignment bar /
 * faction picker comes later; this exists to make alignment visible without
 * running a command.
 * For now the viewed faction is hardcoded to GONDOR so there's something to
 * look at; once a "current faction" selection exists (menu slice) this reads
 * from that instead.
 * IMPORTANT (26.1 rendering notes):
 *  - Registered via HudElementRegistry (HudRenderCallback was REMOVED in 26.1).
 *  - Colors are ARGB (0xFF......); a bare RGB value renders invisible.
 *  - The HudElement lambda's graphics parameter type may be GuiGraphics or,
 *    in the newest split-phase docs, GuiGraphicsExtractor. If this fails to
 *    compile on the type, change the render() first parameter type to match
 *    what the HudElement functional interface declares (hover it in-IDE).
 */
public final class LOTRAlignmentHud {

    private static final Identifier ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_hud");

    // Temporary: the faction the HUD shows until a selection system exists.
    private static LOTRFaction viewedFaction = LOTRFaction.GONDOR;

    private LOTRAlignmentHud() {
    }

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ID, LOTRAlignmentHud::render);
    }

    public static void setViewedFaction(LOTRFaction faction) {
        if (faction != null) {
            viewedFaction = faction;
        }
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }
        LOTRFaction faction = viewedFaction;
        if (faction == null) {
            return;
        }

        float alignment = LOTRPlayerAlignments.getAlignment(player, faction);
        LOTRFactionRank rank = LOTRPlayerAlignments.getRank(player, faction);

        Component nameLine = Component.translatable(faction.untranslatedFactionName());
        Component valueLine = Component.literal(formatAlignment(alignment) + "  ")
                .append(rank.getDisplayName());

        int x = 4;
        int y = 4;
        int factionColor = 0xFF000000 | faction.getFactionColor();
        int white = 0xFFFFFFFF;

        graphics.text(mc.font, nameLine, x, y, factionColor, true);
        graphics.text(mc.font, valueLine, x, y + 10, white, true);
    }

    private static String formatAlignment(float alignment) {
        // Whole numbers show without a trailing .0; keep one decimal otherwise.
        if (alignment == Math.floor(alignment)) {
            return String.valueOf((int) alignment);
        }
        return String.format("%.1f", alignment);
    }
}
