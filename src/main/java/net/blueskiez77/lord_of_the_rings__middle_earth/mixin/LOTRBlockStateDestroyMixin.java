package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** LOTREventHandler.onBlockBreaking: Utumno's blocks that may not be mined there make no progress at all. */
@Mixin(BlockBehaviour.BlockStateBase.class)
abstract class LOTRBlockStateDestroyMixin {
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void lotr$utumnoBlocks(Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (!LOTRUtumno.canMine(player, (BlockState) (Object) this, pos)) {
            cir.setReturnValue(0.0f);
        }
    }
}
