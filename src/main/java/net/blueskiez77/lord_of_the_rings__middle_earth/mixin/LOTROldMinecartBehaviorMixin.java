package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMechanisedRailBlock;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTRClassTransformer's EntityMinecart patch, checkForPoweredRail and
 * checkForDepoweredRail: the rail a cart is on is read as a powered rail when
 * it is a mechanised one, so a running mechanism boosts the cart and a stopped
 * one brakes it.
 */
@Mixin(OldMinecartBehavior.class)
abstract class LOTROldMinecartBehaviorMixin {
    @ModifyExpressionValue(method = "moveAlongTrack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
            ordinal = 0))
    private BlockState lotr$mechanisedRail(BlockState state) {
        return LOTRMechanisedRailBlock.asPoweredRail(state);
    }
}
