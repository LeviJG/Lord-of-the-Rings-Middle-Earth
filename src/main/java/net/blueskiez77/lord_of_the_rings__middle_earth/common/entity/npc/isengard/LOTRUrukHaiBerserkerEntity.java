package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityUrukHaiBerserker: an Uruk a sixth again as large (its box and
 * its drawing, {@link #BERSERKER_SCALE}), 30 health, hitting two harder, in
 * furs and a berserker's helmet with a berserker's cleaver, and with a voice
 * lower still.
 */
public class LOTRUrukHaiBerserkerEntity extends LOTRUrukHaiEntity {

    public static final float BERSERKER_SCALE = 1.15f;

    public LOTRUrukHaiBerserkerEntity(EntityType<? extends LOTRUrukHaiBerserkerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRUrukHaiEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_EXTRA, 2.0);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.8f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.URUK_BERSERKER_CLEAVER));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.URUK_BERSERKER_HELMET));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.FUR_TUNIC));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.FUR_LEGGINGS));
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.FUR_BOOTS));
        return data;
    }
}
