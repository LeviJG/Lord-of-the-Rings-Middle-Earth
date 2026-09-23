package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMechanisedRailBlock;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * A cart standing still on a running mechanised rail is pushed away from a
 * solid block at one end, as on a powered rail -- 1.7.10 did that for any
 * rail the patch called powered.
 */
@Mixin(AbstractMinecart.class)
abstract class LOTRAbstractMinecartMixin {
    @ModifyExpressionValue(method = "getRedstoneDirection", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState lotr$mechanisedRail(BlockState state) {
        return LOTRMechanisedRailBlock.asPoweredRail(state);
    }
}
