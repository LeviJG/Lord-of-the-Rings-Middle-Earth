package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.Locale;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRAlignmentValues.notifyAlignmentNotHighEnough: "You need at least +1.0
 * Mordor alignment to perform this action", with the figure in yellow. The
 * original had one overload per faction count and a translation key for each.
 */
public final class LOTRAlignmentMessages {
    private LOTRAlignmentMessages() {
    }

    /** formatAlignForDisplay: one decimal place, and a plus sign unless negative. */
    public static String formatAlignForDisplay(float alignment) {
        String s = String.format(Locale.ROOT, "%,.1f", alignment);
        return s.startsWith("-") ? s : "+" + s;
    }

    public static void notifyAlignmentNotHighEnough(Player player, float required, LOTRFaction... factions) {
        MutableComponent amount = Component.literal(formatAlignForDisplay(required)).withStyle(ChatFormatting.YELLOW);
        Object[] args = new Object[factions.length + 1];
        args[0] = amount;
        for (int i = 0; i < factions.length; ++i) {
            args[i + 1] = factions[i].factionName();
        }
        String key = switch (factions.length) {
            case 1 -> "chat.lotr.insufficientAlignment";
            case 2 -> "chat.lotr.insufficientAlignment2";
            default -> "chat.lotr.insufficientAlignment3";
        };
        player.sendSystemMessage(Component.translatable(key, args));
    }
}
