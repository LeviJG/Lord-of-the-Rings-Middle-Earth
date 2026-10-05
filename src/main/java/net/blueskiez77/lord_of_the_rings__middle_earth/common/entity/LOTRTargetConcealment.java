package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimWardenEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRHuornBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTREventHandler.onLivingSetAttackTarget: those no creature can take as its
 * target -- a Ranger in hiding, a Galadhrim warden sneaking, a Huorn at rest,
 * and a player in the full Galadhrim cloak (hithlain) who has not just struck
 * this creature and is at least 8 blocks from it. The target is dropped as it
 * is set.
 */
public final class LOTRTargetConcealment {

    private LOTRTargetConcealment() {
    }

    public static boolean isConcealedFrom(@Nullable LivingEntity target, Mob attacker) {
        if (target instanceof LOTRRangerEntity ranger && ranger.isRangerSneaking()) {
            return true;
        }
        if (target instanceof LOTRGaladhrimWardenEntity warden && warden.isElfSneaking()) {
            return true;
        }
        if (target instanceof LOTRHuornBaseEntity huorn && !huorn.isHuornActive()) {
            return true;
        }
        if (target instanceof Player player && isWearingFullHithlain(player)) {
            return player.getLastHurtMob() != attacker && player.distanceToSqr(attacker) >= 64.0;
        }
        return false;
    }

    /** isPlayerWearingFull(..) == LOTRMaterial.HITHLAIN: all four pieces of the Galadhrim cloak. */
    private static boolean isWearingFullHithlain(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(LOTRCombatItems.GALADHRIM_CLOAK_HOOD)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(LOTRCombatItems.GALADHRIM_CLOAK_TUNIC)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(LOTRCombatItems.GALADHRIM_CLOAK_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(LOTRCombatItems.GALADHRIM_CLOAK_BOOTS);
    }
}
