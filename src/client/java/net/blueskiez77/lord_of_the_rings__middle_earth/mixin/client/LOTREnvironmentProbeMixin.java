package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTREnvironmentClient;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** The camera's sky, fog and cloud colours in Middle-earth, tinted as the original's biomes and wights did (LOTREnvironmentClient). */
@Mixin(EnvironmentAttributeProbe.class)
abstract class LOTREnvironmentProbeMixin {

    @Shadow
    private @Nullable Level level;

    @Shadow
    private @Nullable Vec3 position;

    @ModifyReturnValue(method = "getValue", at = @At("RETURN"))
    private <V> V lotr$middleEarthColours(V value, EnvironmentAttribute<V> attribute, float partialTicks) {
        if (this.level instanceof ClientLevel && this.position != null && (LOTREnvironmentClient.isMiddleEarth(this.level)
                || net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno.isUtumno(this.level))) {
            return LOTREnvironmentClient.modifyAttribute(attribute, value, this.level, this.position, partialTicks);
        }
        return value;
    }
}
