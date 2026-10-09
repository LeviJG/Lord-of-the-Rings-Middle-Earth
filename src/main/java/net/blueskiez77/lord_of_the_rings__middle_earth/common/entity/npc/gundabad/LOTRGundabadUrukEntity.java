package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGundabadUruk: a great orc of Gundabad, man-sized and no weak orc,
 * in Gundabad Uruk armour with a cleaver, waraxe, bludgeon, dagger (poisoned
 * or not) or pike, and a spear one time in six. It speaks lower and leaves
 * Uruk steel.
 *
 * <p>NOT ported yet: the killGundabadUruk achievement (D7).
 */
public class LOTRGundabadUrukEntity extends LOTRGundabadOrcEntity {

    public LOTRGundabadUrukEntity(EntityType<? extends LOTRGundabadUrukEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_GUNDABAD;
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTROrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected Item getOrcSteelDrop() {
        return LOTRMaterialItems.URUK_STEEL_INGOT;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] urukWeapons = {LOTRCombatItems.GUNDABAD_URUK_CLEAVER, LOTRCombatItems.GUNDABAD_URUK_WARAXE,
                LOTRCombatItems.GUNDABAD_URUK_BLUDGEON, LOTRCombatItems.GUNDABAD_URUK_DAGGER,
                LOTRCombatItems.POISONED_GUNDABAD_URUK_DAGGER, LOTRCombatItems.GUNDABAD_URUK_PIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(urukWeapons[this.random.nextInt(urukWeapons.length)]));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GUNDABAD_URUK_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GUNDABAD_URUK_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GUNDABAD_URUK_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GUNDABAD_URUK_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GUNDABAD_URUK_HELMET));
        return data;
    }
}
