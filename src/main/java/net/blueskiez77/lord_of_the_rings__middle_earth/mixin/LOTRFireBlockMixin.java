package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTRClassTransformer.patchBlockFire, for "Disable fire spread": fire still
 * burns out and still burns the blocks around it, but never spreads -- the
 * tick stops after the six neighbours' burn checks, before the wider spread,
 * and a block burnt away leaves air rather than catching alight itself.
 */
@Mixin(FireBlock.class)
abstract class LOTRFireBlockMixin {

    @Inject(method = "tick", cancellable = true, at = @At(value = "INVOKE", ordinal = 5, shift = At.Shift.AFTER,
            target = "Lnet/minecraft/world/level/block/FireBlock;checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;I)V"))
    private void lotr$noSpread(CallbackInfo ci) {
        if (LOTRConfig.disableFireSpread) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "checkBurnOut", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean lotr$burntLeavesAir(Level level, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        if (LOTRConfig.disableFireSpread) {
            return level.removeBlock(pos, false);
        }
        return original.call(level, pos, state, flags);
    }
}
