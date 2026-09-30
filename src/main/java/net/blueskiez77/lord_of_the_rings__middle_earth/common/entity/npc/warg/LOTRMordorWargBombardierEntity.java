package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityMordorWargBombardier: a bomb-carrying warg of Mordor, always black. */
public class LOTRMordorWargBombardierEntity extends LOTRWargBombardierEntity {

    public LOTRMordorWargBombardierEntity(EntityType<? extends LOTRMordorWargBombardierEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORDOR;
    }

    @Override
    protected LOTRWargType randomType() {
        return LOTRWargType.BLACK;
    }
}
