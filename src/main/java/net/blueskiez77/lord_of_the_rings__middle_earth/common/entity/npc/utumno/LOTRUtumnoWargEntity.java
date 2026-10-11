package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

/**
 * LOTREntityUtumnoWarg: a warg of Utumno, twice as strong as another and a little quicker, charging
 * at 1.7, never to be ridden by a player; its riders are Utumno's orcs and archers.
 */
public class LOTRUtumnoWargEntity extends LOTRWargEntity {

    public LOTRUtumnoWargEntity(EntityType<? extends LOTRUtumnoWargEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRWargEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean canWargBeRidden() {
        return false;
    }

    @Override
    protected LOTRNPCEntity createWargRider(ServerLevel level) {
        return (level.getRandom().nextBoolean() ? LOTREntities.UTUMNO_ORC_ARCHER : LOTREntities.UTUMNO_ORC).create(level, EntitySpawnReason.JOCKEY);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_UTUMNO_WARG;
    }

    @Override
    protected Goal getWargAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.7, true);
    }
}
