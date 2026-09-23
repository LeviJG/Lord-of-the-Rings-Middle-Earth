package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.minecraft.util.Mth;

public final class LOTRAlignmentValues {
    public static final float MAX_ALIGNMENT = 10000.0f;

    public static final AlignmentBonus MARRIAGE_BONUS = new AlignmentBonus(5.0f, "lotr.alignment.marriage");
    public static final AlignmentBonus FANGORN_TREE_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.cutFangornTree");
    public static final AlignmentBonus ROHAN_HORSE_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.killRohanHorse");
    public static final AlignmentBonus VINEYARD_STEAL_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.vineyardSteal");
    public static final AlignmentBonus PICKPOCKET_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.pickpocket");

    private LOTRAlignmentValues() {
    }

    /**
     * formatAlignForDisplay: one decimal place, thousands grouped, and a '+' on
     * anything not negative. The original read its separator characters from
     * two lang keys; the port has neither key, so it keeps the defaults those
     * keys fell back to, ',' and '.'.
     */
    public static String formatAlignForDisplay(float alignment) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        String s = new DecimalFormat(",##0.0", symbols).format(alignment);
        if (s.isEmpty() || s.charAt(0) != '-') {
            s = "+" + s;
        }
        return s;
    }

    /**
     * notifyAlignmentNotHighEnough, all three overloads in one: "You need at
     * least +1.0 X (or Y, or Z) alignment to perform this action", with the
     * number in yellow.
     */
    public static void notifyAlignmentNotHighEnough(Player player, float alignmentRequired, LOTRFaction... factions) {
        Component required = Component.literal(formatAlignForDisplay(alignmentRequired)).withStyle(ChatFormatting.YELLOW);
        Object[] args = new Object[factions.length + 1];
        args[0] = required;
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

    public static AlignmentBonus createPledgePenalty(float alignPenalty) {
        AlignmentBonus penalty = new AlignmentBonus(-Math.abs(alignPenalty), "lotr.alignment.pledgeBroken");
        return penalty;
    }

    public static class AlignmentBonus {
        public float bonus;
        public String name;
        public boolean needsTranslation = true;
        public boolean isKill;
        public boolean killByHiredUnit;
        public boolean isCivilianKill;

        public AlignmentBonus(float f, String s) {
            bonus = f;
            name = s;
        }

        public AlignmentBonus setKill() {
            isKill = true;
            return this;
        }

        public AlignmentBonus setCivilianKill() {
            isKill = true;
            isCivilianKill = true;
            return this;
        }

        public AlignmentBonus setKillByHiredUnit() {
            killByHiredUnit = true;
            return this;
        }

        public static float scalePenalty(float penalty, float alignment) {
            if (alignment > 0.0f && penalty < 0.0f) {
                float factor = alignment / 50.0f;
                factor = Mth.clamp(factor, 1.0f, 20.0f);
                penalty *= factor;
            }
            return penalty;
        }
    }
}