package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnerAnimals;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.rabbit.Rabbit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** LOTREntityRabbit.canDespawn: a rabbit the LOTR spawner made leaves when far from players. */
@Mixin(Animal.class)
public abstract class LOTRAnimalMixin {

    @Inject(method = "removeWhenFarAway", at = @At("HEAD"), cancellable = true)
    private void lotr$wildRabbitDespawns(double distSqr, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Rabbit rabbit && rabbit.getAttachedOrElse(LOTRSpawnerAnimals.WILD_RABBIT, false)) {
            cir.setReturnValue(true);
        }
    }
}
