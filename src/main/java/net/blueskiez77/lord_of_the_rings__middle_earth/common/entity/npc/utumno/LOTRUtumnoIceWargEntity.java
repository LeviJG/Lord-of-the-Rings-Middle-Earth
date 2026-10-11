package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentHudPayloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/** LOTREntityUtumnoIceWarg: an ice warg, chilly, whose bite frosts a player's sight and slows. */
public class LOTRUtumnoIceWargEntity extends LOTRUtumnoWargEntity {

    public LOTRUtumnoIceWargEntity(EntityType<? extends LOTRUtumnoIceWargEntity> type, Level level) {
        super(type, level);
        this.isChilly = true;
    }

    @Override
    protected LOTRWargType randomType() {
        return LOTRWargType.ICE;
    }

    /** attackEntityAsMob: LOTRDamage.doFrostDamage, and Slowness for (difficulty x (difficulty + 5) / 2) seconds. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            if (target instanceof ServerPlayer player) {
                ServerPlayNetworking.send(player, new LOTRAlignmentHudPayloads.EnvironmentOverlay(LOTRAlignmentHudPayloads.EnvironmentOverlay.FROST));
            }
            int difficulty = level.getDifficulty().getId();
            int duration = difficulty * (difficulty + 5) / 2;
            if (target instanceof LivingEntity living && duration > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration * 20, 0));
            }
            return true;
        }
        return false;
    }
}
