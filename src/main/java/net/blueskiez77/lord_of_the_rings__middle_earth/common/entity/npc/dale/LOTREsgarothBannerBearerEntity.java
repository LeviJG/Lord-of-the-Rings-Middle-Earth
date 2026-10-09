package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityEsgarothBannerBearer: a soldier of Dale who carries Esgaroth's banner.
 *
 * <p>NOT ported yet: the Esgaroth shield (LOTRShields.ALIGNMENT_ESGAROTH, D7).
 */
public class LOTREsgarothBannerBearerEntity extends LOTRDaleSoldierEntity implements LOTRBannerBearer {

    public LOTREsgarothBannerBearerEntity(EntityType<? extends LOTREsgarothBannerBearerEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_ESGAROTH;
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.ESGAROTH;
    }
}
