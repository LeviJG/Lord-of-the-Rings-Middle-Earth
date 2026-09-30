package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityMirkwoodSpider: a spider of Mirkwood, of Dol Guldur's faction,
 * sized 0 to 2; half have no venom, the rest slowness or poison. Slain by a
 * player, one in four leaves a mystery web.
 *
 * <p>NOT ported yet: the killMirkwoodSpider achievement (D7).
 */
public class LOTRMirkwoodSpiderEntity extends LOTRSpiderEntity {

    public LOTRMirkwoodSpiderEntity(EntityType<? extends LOTRMirkwoodSpiderEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected int getRandomSpiderScale() {
        return this.random.nextInt(3);
    }

    @Override
    protected int getRandomSpiderType() {
        return this.random.nextBoolean() ? VENOM_NONE : 1 + this.random.nextInt(2);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DOL_GULDUR;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        if (killedByPlayer && this.random.nextInt(4) == 0) {
            spawnAtLocation(level, LOTRMiscItems.MYSTERY_WEB);
        }
    }
}
