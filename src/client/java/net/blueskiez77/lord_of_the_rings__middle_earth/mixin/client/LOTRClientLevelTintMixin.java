package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRBiomeColoursClient;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Middle-earth's grass, leaves and water, each block's from its biome, variant and latitude (LOTRBiomeColoursClient). */
@Mixin(ClientLevel.class)
abstract class LOTRClientLevelTintMixin {

    @WrapOperation(method = "calculateBlockTint", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/ColorResolver;getColor(Lnet/minecraft/world/level/biome/Biome;DD)I"))
    private int lotr$middleEarthTint(ColorResolver resolver, Biome biome, double x, double z, Operation<Integer> original) {
        Integer color = LOTRBiomeColoursClient.resolve((ClientLevel) (Object) this, resolver, biome, x, z);
        return color != null ? color : original.call(resolver, biome, x, z);
    }
}
