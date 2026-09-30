package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityAngmarWargBombardier: a bomb-carrying warg of Angmar. */
public class LOTRAngmarWargBombardierEntity extends LOTRWargBombardierEntity {

    public LOTRAngmarWargBombardierEntity(EntityType<? extends LOTRAngmarWargBombardierEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ANGMAR;
    }
}
