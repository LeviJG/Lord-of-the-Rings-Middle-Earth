package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFarmGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRFarmhand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDorwinionVinehand: a Dorwinion man with an iron hoe, hired from a
 * vinekeeper, who tends grapes -- red or white, chosen when it first appears
 * ("SeedsID") -- and keeps a Dorwinman's own targeting.
 */
public class LOTRDorwinionVinehandEntity extends LOTRDorwinionManEntity implements LOTRFarmhand {

    /** What it plants unhired; wheat seeds unless set. */
    private @Nullable Item seedsItem;

    public LOTRDorwinionVinehandEntity(EntityType<? extends LOTRDorwinionVinehandEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new LOTRFarmGoal(this, 1.0, 1.0f));
        addTargetTasks(false);
    }

    /** getUnhiredSeeds: the grape it was given on spawning (none if it never was). */
    @Override
    public @Nullable Item getUnhiredSeeds() {
        return this.seedsItem;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (this.hiredNPCInfo.getHiringPlayer() == player) {
            return "dorwinion/vinehand/hired";
        }
        return super.getSpeechBank(player);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.seedsItem != null) {
            output.putString("SeedsID", BuiltInRegistries.ITEM.getKey(this.seedsItem).toString());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("SeedsID").map(Identifier::tryParse).flatMap(BuiltInRegistries.ITEM::getOptional)
                .ifPresent(item -> this.seedsItem = item);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(Items.IRON_HOE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.seedsItem = this.random.nextBoolean() ? LOTRMaterialItems.RED_GRAPE_SEEDS : LOTRMaterialItems.GREEN_GRAPE_SEEDS;
        return data;
    }
}
