package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * getMaxFireProtectionLevel: fire protection also cuts how long you burn, by
 * 15% a level of the best piece worn, as vanilla's own did in 1.7.10. Vanilla's
 * fire protection now does its share through the burning-time attribute, which
 * applies after this.
 */
@Mixin(LivingEntity.class)
abstract class LOTRLivingEntityMixin {
    @ModifyVariable(method = "igniteForTicks", at = @At("HEAD"), argsOnly = true)
    private int lotr$fireProtection(int ticks) {
        int level = LOTRModifiers.maxFireProtectionLevel((LivingEntity) (Object) this);
        return level > 0 ? ticks - Mth.floor(ticks * level * 0.15f) : ticks;
    }
}
