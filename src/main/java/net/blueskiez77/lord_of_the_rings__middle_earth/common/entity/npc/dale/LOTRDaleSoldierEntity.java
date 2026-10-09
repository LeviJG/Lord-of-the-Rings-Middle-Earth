package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDaleSoldier: a soldier of Dale in Dale's armour (bare-headed one
 * in ten), with a Dale sword (three in five), battleaxe or pitchfork -- one
 * in six with a Dale spear to throw first; one in eight rides, on a horse in
 * Dale's barding.
 *
 * <p>NOT ported yet: the Dale shield (LOTRShields.ALIGNMENT_DALE, D7).
 */
public class LOTRDaleSoldierEntity extends LOTRDaleLevymanEntity {

    public LOTRDaleSoldierEntity(EntityType<? extends LOTRDaleSoldierEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_DALE;
        this.spawnRidingHorse = this.random.nextInt(8) == 0;
    }

    @Override
    protected Goal createDaleAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, true);
    }

    /** createMountToRide: a horse in Dale's barding. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.DALE_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(5)) {
            case 3 -> LOTRCombatItems.DALE_BATTLEAXE;
            case 4 -> LOTRCombatItems.DALE_PITCHFORK;
            default -> LOTRCombatItems.DALE_SWORD;
        }));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DALE_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DALE_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DALE_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DALE_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(10) == 0 ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.DALE_HELMET));
        return data;
    }
}
