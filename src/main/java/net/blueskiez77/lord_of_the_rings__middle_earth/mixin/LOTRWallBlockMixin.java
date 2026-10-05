package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** LOTRReplacedMethods.Wall.canConnectWallTo: a wall joins up with a fence too. */
@Mixin(WallBlock.class)
abstract class LOTRWallBlockMixin {

    @ModifyReturnValue(method = "connectsTo", at = @At("RETURN"))
    private boolean lotr$connectsToFences(boolean connects, BlockState state, boolean faceSolid, Direction direction) {
        return connects || state.is(BlockTags.FENCES);
    }
}
