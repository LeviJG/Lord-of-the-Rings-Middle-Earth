package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

/**
 * LOTREntityGundabadWarg: a warg of Gundabad, which charges a little faster.
 */
public class LOTRGundabadWargEntity extends LOTRWargEntity {

    public LOTRGundabadWargEntity(EntityType<? extends LOTRGundabadWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.GUNDABAD;
    }

    @Override
    protected Goal getWargAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.75, false);
    }

    /** createWargRider: a Gundabad orc or archer. */
    @Override
    protected LOTRNPCEntity createWargRider(ServerLevel level) {
        return (level.getRandom().nextBoolean() ? LOTREntities.GUNDABAD_ORC_ARCHER : LOTREntities.GUNDABAD_ORC).create(level, EntitySpawnReason.JOCKEY);
    }
}
