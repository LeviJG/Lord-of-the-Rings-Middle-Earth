package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * LOTRReplacedMethods.Piston.canPushBlock: a piston cannot move a block that a
 * protecting banner stands on or beside -- the original searched a box from
 * the block to four above it for banner entities three blocks tall, which
 * catches a banner standing from two below the block to three above it.
 */
@Mixin(PistonBaseBlock.class)
abstract class LOTRPistonBaseBlockMixin {

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private static void lotr$bannerHoldsBlock(BlockState state, Level level, BlockPos pos, Direction direction,
                                              boolean allowDestroyable, Direction connectionDirection,
                                              CallbackInfoReturnable<Boolean> cir) {
        for (LOTRBannerBlockEntity banner : LOTRBannerBlockEntity.loadedIn(level)) {
            BlockPos at = banner.getBlockPos();
            if (at.getX() == pos.getX() && at.getZ() == pos.getZ() && at.getY() >= pos.getY() - 2
                    && at.getY() <= pos.getY() + 3 && !banner.isRemoved() && banner.isProtectingTerritory()) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
