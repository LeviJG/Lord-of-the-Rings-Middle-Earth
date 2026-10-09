package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRWoodElfTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWoodElf: an elf of the Woodland Realm, who trusts no one short of
 * its people's first rank (LOTRWoodElfTargetGoal) and fights only with its
 * Mirkwood bow -- in close as well. It drinks wine as the Wood-elves do,
 * bears a Sindarin name, and slain by a player may leave red wine, and one
 * time in six something from a Wood-elven house.
 *
 * <p>NOT ported yet: its natural spawn check (above y 62 on grass) and the pull
 * of the Woodland Realm on its wandering (with the biomes), and the killWoodElf
 * achievement (D7).
 */
public class LOTRWoodElfEntity extends LOTRElfEntity {

    public LOTRWoodElfEntity(EntityType<? extends LOTRWoodElfEntity> type, Level level) {
        super(type, level);
    }

    /** getWoodlandTrustLevel: the alignment of the Wood-elves' first rank. */
    public static float getWoodlandTrustLevel() {
        return LOTRFaction.WOOD_ELF.getFirstRank().alignment;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, rangedAttackAI());
        addTargetTasks(true, LOTRWoodElfTargetGoal::new);
    }

    /** createElfMeleeAttackAI: the bow, close in as well. */
    @Override
    protected Goal createElfMeleeAttackAI() {
        return createElfRangedAttackAI();
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 50, 16.0f);
    }

    @Override
    public LOTRFoods getElfDrinks() {
        return LOTRFoods.WOOD_ELF_DRINK;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.WOOD_ELF;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getSindarinName(this.random, this.familyInfo.isMale()));
    }

    /** Whether the player has the Wood-elves' trust. */
    protected boolean isTrusted(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= getWoodlandTrustLevel();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "woodElf/elf/hired";
            }
            return isTrusted(player) ? "woodElf/elf/friendly" : "woodElf/elf/neutral";
        }
        return "woodElf/elf/hostile";
    }

    /**
     * dropElfItems: to a player, now and then a light-to-strong red wine in
     * one of the elves' (not the Wood-elves') vessels; and one time in six,
     * whoever the killer, something from a Wood-elven house.
     */
    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropElfItems(level, killedByPlayer, looting);
        if (killedByPlayer && this.random.nextInt(Math.max(20 - looting * 4, 1)) == 0) {
            ItemStack elfDrink = LOTRDrinkItem.stack(LOTRFoodItems.RED_WINE, 1 + this.random.nextInt(3));
            LOTRVessel[] vessels = LOTRFoods.ELF_DRINK.getDrinkVessels();
            elfDrink.set(LOTRDataComponents.VESSEL, vessels[this.random.nextInt(vessels.length)]);
            spawnAtLocation(level, elfDrink, 0.0f);
        }
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.WOOD_ELF_HOUSE, 1, 1 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.MIRKWOOD_BOW));
        this.npcItemsInv.setMeleeWeapon(this.npcItemsInv.getRangedWeapon().copy());
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.WOOD_ELF.createQuest(this);
    }
}
