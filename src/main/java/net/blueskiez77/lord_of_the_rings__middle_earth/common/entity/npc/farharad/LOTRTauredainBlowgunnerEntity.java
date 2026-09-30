package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierSpecials;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRDartEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTauredainBlowgunner: a Taurethrim warrior in Taurethrim armour
 * (no helmet) who shoots Taurethrim darts from a blowgun, quickly and from
 * 16 blocks, seeing 24 blocks about him; slain, he leaves darts.
 */
public class LOTRTauredainBlowgunnerEntity extends LOTRTauredainEntity {

    public LOTRTauredainBlowgunnerEntity(EntityType<? extends LOTRTauredainBlowgunnerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTauredainEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.5, 10, 30, 16.0f);
    }

    /**
     * A dart at 1.0 plus a hair per block of distance, times the blowgun's
     * launch speed, spread 1.0, carrying the blowgun's modifiers.
     */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack held = getMainHandItem();
        float str = 1.0f + distanceTo(target) / 16.0f * 0.015f;
        LOTRDartEntity dart = new LOTRDartEntity(LOTREntities.DART, this, level,
                new ItemStack(LOTRCombatItems.TAURETHRIM_DART), held.isEmpty() ? null : held);
        if (!held.isEmpty()) {
            str *= LOTRModifiers.rangedDamageFactor(held);
            LOTRModifierSpecials.onLaunch(held, dart);
        }
        double dx = target.getX() - getX();
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 3.0f - dart.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        dart.shoot(dx, dy + horizontal * 0.2, dz, str, 1.0f);
        playSound(LOTRSounds.ITEM_DART, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 1.2f) + 0.5f);
        level.addFreshEntity(dart);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, LOTRCombatItems.TAURETHRIM_DART, looting);
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
        if (isFriendlyAndAligned(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "tauredain/warrior/hired" : "tauredain/warrior/friendly";
        }
        return "tauredain/warrior/hostile";
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getRangedWeapon());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.TAURETHRIM_BLOWGUN));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.TAURETHRIM_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.TAURETHRIM_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.TAURETHRIM_CHESTPLATE));
        return data;
    }

    /** A blowgunner saved without a blowgun is given one. */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        if (this.npcItemsInv.getRangedWeapon().isEmpty()) {
            this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.TAURETHRIM_BLOWGUN));
            this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        }
    }
}
