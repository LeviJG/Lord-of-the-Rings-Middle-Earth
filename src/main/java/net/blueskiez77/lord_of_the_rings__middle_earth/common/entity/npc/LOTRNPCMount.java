package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * LOTRNPCMount: a creature an NPC may ride -- the horse and every mount built
 * on it, and the rideable NPCs (wargs). One that belongs to an NPC refuses
 * players.
 *
 * <p>An NPC rider steers its mount (26.2 hands the rider the mount's
 * navigation), so the original's hired-horse goals -- keeping still, riding
 * at the rider's target, following the hiring player
 * (LOTREntityAIHiredHorseRemainStill, HorseMoveToRiderTarget,
 * HorseFollowHiringPlayer) -- are the rider's own goals acting through it.
 */
public interface LOTRNPCMount {

    boolean getBelongsToNPC();

    void setBelongsToNPC(boolean flag);

    boolean isMountSaddled();

    /** LOTRMountFunctions.setNavigatorRangeFromNPC: the mount looks as far as its rider. */
    static void setNavigatorRangeFromNPC(Mob mount, LOTRNPCEntity npc) {
        mount.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(npc.getAttributeValue(Attributes.FOLLOW_RANGE));
    }
}
