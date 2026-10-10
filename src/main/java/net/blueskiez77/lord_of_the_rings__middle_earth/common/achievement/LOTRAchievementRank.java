package net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRAchievementRank: reaching a faction rank that makes an achievement -- listed in the faction's
 * land, numbered from 1001 in the order the ranks are made, a gold ring its icon, named for the rank
 * (in its feminine form where the player uses those). One must be a friend of the faction to earn it,
 * and pledged to it for the ranks above its pledge rank.
 */
public class LOTRAchievementRank extends LOTRAchievement {

    public final LOTRFactionRank theRank;
    public final LOTRFaction theFac;

    public LOTRAchievementRank(LOTRFactionRank rank, Category category) {
        super(category, category.getNextRankAchID(),
                () -> new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gold_ring"))),
                "alignment_" + rank.fac.codeName() + "_" + rank.alignment);
        this.theRank = rank;
        this.theFac = rank.fac;
        setRequiresAlly(this.theFac);
        setSpecial();
    }

    @Override
    public boolean canPlayerEarn(Player player) {
        if (LOTRPlayerAlignments.getAlignment(player, this.theFac) < 0.0f) {
            return false;
        }
        return !requiresPledge() || LOTRPlayerAlignments.isPledgedTo(player, this.theFac);
    }

    @Override
    public Component getDescription(Player player) {
        String suffix = requiresPledge() ? "achieveRankPledge" : "achieveRank";
        return Component.translatable("lotr.faction." + this.theFac.codeName() + "." + suffix,
                LOTRAlignmentValues.formatAlignForDisplay(this.theRank.alignment));
    }

    @Override
    public String getUntranslatedTitle(Player player) {
        return this.theRank.getCodeFullNameWithGender(LOTRRankOptions.useFeminineRanks(player));
    }

    public boolean isPlayerRequiredRank(Player player) {
        if (requiresPledge() && !LOTRPlayerAlignments.isPledgedTo(player, this.theFac)) {
            return false;
        }
        return LOTRPlayerAlignments.getAlignment(player, this.theFac) >= this.theRank.alignment;
    }

    public boolean requiresPledge() {
        return this.theRank.isAbovePledgeRank();
    }
}
