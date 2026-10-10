package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAngmarHillmanWarrior: a hillman man-at-arms with an Angmar blade,
 * axe, warhammer, dagger, poleaxe or spear, in Angmar, bone or fur armour (a
 * bone or fur helmet four times in five). He leaves no house goods.
 */
public class LOTRAngmarHillmanWarriorEntity extends LOTRAngmarHillmanEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.ANGMAR_SWORD, LOTRCombatItems.ANGMAR_BATTLEAXE,
            LOTRCombatItems.ANGMAR_WARHAMMER, LOTRCombatItems.ANGMAR_DAGGER, LOTRCombatItems.ANGMAR_POLEAXE,
            LOTRCombatItems.ANGMAR_SPEAR};
    private static final Item[] HELMETS = {LOTRCombatItems.BONE_HELMET, LOTRCombatItems.FUR_HAT};
    private static final Item[] BODIES = {LOTRCombatItems.ANGMAR_CHESTPLATE, LOTRCombatItems.ANGMAR_CHESTPLATE,
            LOTRCombatItems.BONE_CHESTPLATE, LOTRCombatItems.FUR_TUNIC};
    private static final Item[] LEGS = {LOTRCombatItems.ANGMAR_LEGGINGS, LOTRCombatItems.ANGMAR_LEGGINGS,
            LOTRCombatItems.BONE_LEGGINGS, LOTRCombatItems.FUR_LEGGINGS};
    private static final Item[] BOOTS = {LOTRCombatItems.ANGMAR_BOOTS, LOTRCombatItems.ANGMAR_BOOTS,
            LOTRCombatItems.BONE_BOOTS, LOTRCombatItems.FUR_BOOTS};

    public LOTRAngmarHillmanWarriorEntity(EntityType<? extends LOTRAngmarHillmanWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_ANGMAR;
    }

    @Override
    protected Goal createHillmanAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
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
    protected void dropHillmanItems(ServerLevel level, int looting) {
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(BOOTS[this.random.nextInt(BOOTS.length)]));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LEGS[this.random.nextInt(LEGS.length)]));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(BODIES[this.random.nextInt(BODIES.length)]));
        if (this.random.nextInt(5) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(HELMETS[this.random.nextInt(HELMETS.length)]));
        }
        return data;
    }
}
