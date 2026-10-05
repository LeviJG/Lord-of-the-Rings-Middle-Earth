package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFarmGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRFarmhand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

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
 * LOTREntityHarnedorFarmhand: a Harnennor farmhand with a bronze hoe, who
 * farms for whoever hires him (wheat seeds unhired) and seeks no one out.
 */
public class LOTRHarnedorFarmhandEntity extends LOTRHarnedhrimEntity implements LOTRFarmhand {

    public @Nullable Item seedsItem;

    public LOTRHarnedorFarmhandEntity(EntityType<? extends LOTRHarnedorFarmhandEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new LOTRFarmGoal(this, 1.0, 1.0f));
        addTargetTasks(false);
    }

    @Override
    public Item getUnhiredSeeds() {
        return this.seedsItem == null ? Items.WHEAT_SEEDS : this.seedsItem;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (this.hiredNPCInfo.getHiringPlayer() == player) {
            return "nearHarad/harnennor/farmhand/hired";
        }
        return super.getSpeechBank(player);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRToolItems.BRONZE_HOE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
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
}
