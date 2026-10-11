package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRSnowTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityUtumnoSnowTroll: a snow troll of Utumno's ice, chilly. */
public class LOTRUtumnoSnowTrollEntity extends LOTRSnowTrollEntity {

    public LOTRUtumnoSnowTrollEntity(EntityType<? extends LOTRUtumnoSnowTrollEntity> type, Level level) {
        super(type, level);
        this.isChilly = true;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_UTUMNO_TROLL;
    }
}
