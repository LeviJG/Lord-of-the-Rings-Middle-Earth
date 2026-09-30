package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
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
 * LOTREntityDorwinionGuard (the Vintner Guard): Dorwinion's watch, always
 * men, seeking out their people's enemies and chasing even what they cannot
 * see, with an iron sword, battleaxe or pike -- one in eight with an iron
 * spear besides -- in Dorwinion armour, three in four helmeted. Its alert
 * over stolen grapes ("GrapeAlert", up to 3) cools by one every thirty
 * seconds.
 *
 * <p>NOT ported yet: the guards' defence of the vineyards
 * (defendGrapevines -- warnings, then attacks, and guards called in, for a
 * player picking grapes in a Dorwinion vineyard -- with the biome variants,
 * D10), the stealDorwinionGrapes achievement for killing an alerted guard
 * (D7), the Dorwinion shield (LOTRShields.ALIGNMENT_DORWINION, D7), and
 * throwing the spear (spears keep vanilla's mechanics, user).
 */
public class LOTRDorwinionGuardEntity extends LOTRDorwinionManEntity {

    public static final int MAX_GRAPE_ALERT = 3;
    public int grapeAlert;

    public LOTRDorwinionGuardEntity(EntityType<? extends LOTRDorwinionGuardEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createDorwinionAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, true);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "dorwinion/guard/hired" : "dorwinion/guard/friendly";
        }
        return "dorwinion/guard/hostile";
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && this.grapeAlert > 0 && this.tickCount % 600 == 0) {
            --this.grapeAlert;
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("GrapeAlert", this.grapeAlert);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.grapeAlert = input.getIntOr("GrapeAlert", 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {Items.IRON_SWORD, LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.IRON_PIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        if (this.random.nextInt(8) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.IRON_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DORWINION_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DORWINION_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DORWINION_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(4) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.DORWINION_HELMET));
        return data;
    }
}
