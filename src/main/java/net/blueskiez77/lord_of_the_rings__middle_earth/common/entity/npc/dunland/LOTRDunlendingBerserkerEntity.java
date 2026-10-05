package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
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
 * LOTREntityDunlendingBerserker: a warrior of 30 health hitting two harder,
 * with an iron or bronze battleaxe, in fur boots and leggings, a bone
 * chestplate and a Dunlending helmet, in the berserkers' own skins.
 *
 * <p>NOT ported yet: the berserker's cape (LOTRCapes.DUNLENDING_BERSERKER),
 * with NPC capes. (He carries no shield, as the original took it away.)
 */
public class LOTRDunlendingBerserkerEntity extends LOTRDunlendingWarriorEntity {

    public LOTRDunlendingBerserkerEntity(EntityType<? extends LOTRDunlendingBerserkerEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.DUNLENDING_BERSERKER;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRDunlendingEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_EXTRA, 2.0);
    }

    @Override
    protected Goal createDunlendingAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.7, false);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(2) == 0
                ? LOTRCombatItems.IRON_BATTLEAXE : LOTRCombatItems.BRONZE_BATTLEAXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.FUR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.FUR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.BONE_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DUNLENDING_HELMET));
        return data;
    }
}
