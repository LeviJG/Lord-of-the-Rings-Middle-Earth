package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargType;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityUtumnoFireWarg: a fire warg, unharmed by fire, flaming and smoking, whose bite sets alight. */
public class LOTRUtumnoFireWargEntity extends LOTRUtumnoWargEntity {

    public LOTRUtumnoFireWargEntity(EntityType<? extends LOTRUtumnoFireWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected LOTRWargType randomType() {
        return LOTRWargType.FIRE;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean flag = super.doHurtTarget(level, target);
        if (flag) {
            target.igniteForSeconds(4.0f);
        }
        return flag;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            level().addParticle(this.random.nextInt(3) > 0 ? ParticleTypes.FLAME : ParticleTypes.SMOKE,
                    getX() + (this.random.nextDouble() - 0.5) * getBbWidth(), getY() + this.random.nextDouble() * getBbHeight(),
                    getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
        }
    }
}
