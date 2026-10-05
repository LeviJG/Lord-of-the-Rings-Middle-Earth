package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.world.entity.LivingEntity;

/**
 * LOTREntityAIOrcSkirmish: now and then an orc picks a fight with another --
 * one chance in 20000 a tick, a tenth as long for every skirmishing orc within
 * 16 blocks (never better than one in 40). Neither may be a trader, hired, or
 * riding, and some kinds (the Black Uruks) never skirmish; nor does any, with
 * "Enable Orc Skirmishes" off.
 */
public class LOTROrcSkirmishGoal extends LOTRNearestAttackableTargetGoal {

    private final LOTROrcEntity orc;

    public LOTROrcSkirmishGoal(LOTROrcEntity orc, boolean checkSight) {
        super(orc, LOTROrcEntity.class, 0, checkSight, LOTROrcSkirmishGoal::canOrcSkirmish);
        this.orc = orc;
    }

    public static boolean canOrcSkirmish(LivingEntity entity) {
        return entity instanceof LOTROrcEntity orc && !orc.isTrader() && !orc.hiredNPCInfo.isActive
                && orc.getVehicle() == null && orc.canOrcSkirmish();
    }

    @Override
    public boolean canUse() {
        if (!LOTRConfig.enableOrcSkirmish || !canOrcSkirmish(this.orc)) {
            return false;
        }
        if (!this.orc.isOrcSkirmishing()) {
            int chance = 20000;
            for (LOTROrcEntity other : this.orc.level().getEntitiesOfClass(LOTROrcEntity.class,
                    this.orc.getBoundingBox().inflate(16.0, 8.0, 16.0))) {
                if (other.isOrcSkirmishing()) {
                    chance /= 10;
                }
            }
            chance = Math.max(chance, 40);
            if (this.orc.getRandom().nextInt(chance) != 0) {
                return false;
            }
        }
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.orc.setOrcSkirmishing();
    }
}
