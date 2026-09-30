package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCorsair: a corsair of Umbar, who seeks out Near Harad's enemies
 * with an eket, dagger (poisoned or not), harpoon or battleaxe up close (one
 * in five a harpoon with the other held back) and a Harad bow at range,
 * changing as the fight needs. He is never mounted, wears Corsair armour
 * (bare-headed half the time), eats and drinks as the Corsairs do, loots
 * coins from what he kills, and leaves arrows and, one time in three,
 * something from a Corsair chest.
 *
 * <p>NOT ported yet: the Corsair shield (LOTRShields.ALIGNMENT_CORSAIR, D7),
 * and mini-quests (D14).
 */
public class LOTRCorsairEntity extends LOTRUmbarianEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.CORSAIR_EKET, LOTRCombatItems.CORSAIR_EKET,
            LOTRCombatItems.CORSAIR_DAGGER, LOTRCombatItems.POISONED_CORSAIR_DAGGER, LOTRCombatItems.CORSAIR_HARPOON,
            LOTRCombatItems.CORSAIR_HARPOON, LOTRCombatItems.CORSAIR_BATTLEAXE, LOTRCombatItems.CORSAIR_BATTLEAXE};

    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;

    public LOTRCorsairEntity(EntityType<? extends LOTRCorsairEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRNearHaradrimBaseEntity.createAttributes()
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return meleeAttackAI();
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.6, true);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.5, 30, 40, 16.0f);
        }
        return this.rangedAttackAI;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        Goal ranged = rangedAttackAI();
        this.goalSelector.removeGoal(melee);
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(2, melee);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else {
            this.goalSelector.addGoal(2, ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    /** onKillEntity: half the time, shaking coins out of what it kills. */
    @Override
    public boolean lootsExtraCoins() {
        return true;
    }

    @Override
    public LOTRFoods getHaradrimFoods() {
        return LOTRFoods.CORSAIR;
    }

    @Override
    public LOTRFoods getHaradrimDrinks() {
        return LOTRFoods.CORSAIR_DRINK;
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
            return this.hiredNPCInfo.getHiringPlayer() == player
                    ? "nearHarad/umbar/corsair/hired" : "nearHarad/umbar/corsair/friendly";
        }
        return "nearHarad/umbar/corsair/hostile";
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, Items.ARROW, looting);
    }

    @Override
    protected void dropHaradrimItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(3) == 0) {
            dropChestContents(level, LOTRChestContents.CORSAIR, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.CORSAIR_HARPOON));
        }
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.HARAD_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.CORSAIR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.CORSAIR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.CORSAIR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(2) == 0 ? ItemStack.EMPTY
                : new ItemStack(LOTRCombatItems.CORSAIR_HELMET));
        return data;
    }
}
