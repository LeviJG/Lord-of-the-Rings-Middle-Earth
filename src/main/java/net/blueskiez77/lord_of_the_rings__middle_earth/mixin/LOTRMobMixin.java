package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTargetConcealment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** LOTREventHandler.onLivingSetAttackTarget: a creature cannot take a concealed target (LOTRTargetConcealment). */
@Mixin(Mob.class)
public abstract class LOTRMobMixin {

    @ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
    private @Nullable LivingEntity lotr$dropConcealedTarget(@Nullable LivingEntity target) {
        return LOTRTargetConcealment.isConcealedFrom(target, (Mob) (Object) this) ? null : target;
    }
}
