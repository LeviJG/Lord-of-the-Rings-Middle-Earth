package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityDarkHuorn: an oak of the Old Forest, black of heart, answering
 * to no one.
 *
 * <p>NOT ported yet: the killDarkHuorn achievement (D7).
 */
public class LOTRDarkHuornEntity extends LOTRHuornBaseEntity {

    public LOTRDarkHuornEntity(EntityType<? extends LOTRDarkHuornEntity> type, Level level) {
        super(type, level);
        setTreeType(0);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DARK_HUORN;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }
}
