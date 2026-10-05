package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** LOTRReplacedMethods.Fence.canConnectFenceTo: a fence joins up with a wall too. */
@Mixin(FenceBlock.class)
abstract class LOTRFenceBlockMixin {

    @ModifyReturnValue(method = "connectsTo", at = @At("RETURN"))
    private boolean lotr$connectsToWalls(boolean connects, BlockState state, boolean faceSolid, Direction direction) {
        return connects || state.is(BlockTags.WALLS);
    }
}
