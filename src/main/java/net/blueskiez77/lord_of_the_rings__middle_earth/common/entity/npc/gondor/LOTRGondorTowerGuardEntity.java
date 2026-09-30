package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGondorTowerGuard: a Guard of the Citadel, on foot, quicker and
 * hardier than a soldier, with a Gondorian spear and no spare, under the
 * winged helmet.
 *
 * <p>NOT ported yet: the Tower Guard cape (LOTRCapes.TOWER_GUARD).
 */
public class LOTRGondorTowerGuardEntity extends LOTRGondorSoldierEntity {

    public LOTRGondorTowerGuardEntity(EntityType<? extends LOTRGondorTowerGuardEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGondorSoldierEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.24);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GONDOR_SPEAR));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setSpearBackup(ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GONDOR_WINGED_HELMET));
        return data;
    }
}
