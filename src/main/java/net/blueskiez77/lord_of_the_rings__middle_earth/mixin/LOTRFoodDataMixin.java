package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.world.food.FoodData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** LOTRReplacedMethods.Food.getExhaustionFactor: with "Hunger changes", all exhaustion counts for 0.3. */
@Mixin(FoodData.class)
abstract class LOTRFoodDataMixin {

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    private float lotr$exhaustionFactor(float amount) {
        return LOTRConfig.changedHunger ? amount * 0.3f : amount;
    }
}
