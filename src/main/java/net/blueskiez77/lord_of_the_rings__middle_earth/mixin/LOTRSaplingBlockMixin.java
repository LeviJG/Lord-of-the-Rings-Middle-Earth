package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRSaplingGrowth;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTREventHandler.onSaplingGrow: vanilla's six 1.7.10 saplings -- oak, spruce, birch, jungle,
 * acacia and dark oak -- grow as the mod grew them (LOTRVanillaSaplings), party trees and all.
 */
@Mixin(SaplingBlock.class)
public abstract class LOTRSaplingBlockMixin {

    @Inject(method = "advanceTree", at = @At("HEAD"), cancellable = true)
    private void lotr$growLikeTheMod(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, CallbackInfo ci) {
        if (state.getValue(SaplingBlock.STAGE) != 0 && LOTRSaplingGrowth.hasRules(state.getBlock())) {
            LOTRSaplingGrowth.growTree(level, pos, state, random);
            ci.cancel();
        }
    }
}
