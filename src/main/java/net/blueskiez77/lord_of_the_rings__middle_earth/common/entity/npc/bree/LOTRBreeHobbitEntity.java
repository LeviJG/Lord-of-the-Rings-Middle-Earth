package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeHobbit: the Little Folk of Bree-land. A hobbit in all its
 * ways, but of Bree: its names are mostly Bree names, it eats and drinks as
 * Bree-land does, marries only its own kind, and leaves a Bree-land house's
 * odds and ends when slain.
 *
 * <p>NOT ported yet: pickpocketing (IPickpocketable), mini-quests (D14), the
 * pull of Bree-land on its wandering (with the biomes), and the
 * killBreeHobbit achievement (D7).
 */
public class LOTRBreeHobbitEntity extends LOTRHobbitEntity {

    public LOTRBreeHobbitEntity(EntityType<? extends LOTRBreeHobbitEntity> type, Level level) {
        super(type, level);
        this.familyInfo.marriageEntityClass = LOTRBreeHobbitEntity.class;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.BREE;
    }

    @Override
    public LOTRFoods getHobbitFoods() {
        return LOTRFoods.BREE;
    }

    @Override
    public LOTRFoods getHobbitDrinks() {
        return LOTRFoods.BREE_DRINK;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getBreeHobbitName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public void createNPCChildName(LOTRNPCEntity maleParent, LOTRNPCEntity femaleParent) {
        this.familyInfo.setName(LOTRNames.getBreeHobbitChildNameForParent(this.random, this.familyInfo.isMale(), maleParent));
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isDrunkard()) {
            return "bree/hobbit/drunkard/neutral";
        }
        if (isFriendlyAndAligned(player)) {
            return isBaby() ? "bree/hobbit/child/friendly" : "bree/hobbit/friendly";
        }
        return isBaby() ? "bree/hobbit/child/hostile" : "bree/hobbit/hostile";
    }

    @Override
    protected void dropHobbitItems(ServerLevel level, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.BREE_HOUSE, 1, 2 + looting);
        }
    }
}
