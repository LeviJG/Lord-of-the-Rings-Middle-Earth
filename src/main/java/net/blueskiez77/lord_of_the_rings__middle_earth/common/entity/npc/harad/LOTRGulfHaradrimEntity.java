package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGulfHaradrim (the Gulfing): a man or woman of the Gulf of Harad,
 * with a Haradric dagger, a Gulf name, the Gulf's food and drink, and now and
 * then something from a Gulf house. Like the Harnedhrim, every one seeks out
 * Near Harad's enemies.
 */
public class LOTRGulfHaradrimEntity extends LOTRNearHaradrimBaseEntity {

    public LOTRGulfHaradrimEntity(EntityType<? extends LOTRGulfHaradrimEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    public LOTRFoods getHaradrimFoods() {
        return LOTRFoods.GULF_HARAD;
    }

    @Override
    public LOTRFoods getHaradrimDrinks() {
        return LOTRFoods.GULF_HARAD_DRINK;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getGulfHaradName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "nearHarad/gulf/haradrim/friendly" : "nearHarad/gulf/haradrim/hostile";
    }

    @Override
    protected void dropHaradrimItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.GULF_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.HARADRIC_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.GULF_HARAD.createQuest(this);
    }
}
