package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityHuorn: a huorn of Fangorn.
 */
public class LOTRHuornEntity extends LOTRHuornBaseEntity {

    public LOTRHuornEntity(EntityType<? extends LOTRHuornEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.FANGORN;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_HUORN;
    }
}
