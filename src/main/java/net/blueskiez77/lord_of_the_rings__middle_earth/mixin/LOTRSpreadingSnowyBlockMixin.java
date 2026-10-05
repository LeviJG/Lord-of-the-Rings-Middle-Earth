package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMudGrassBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTRReplacedMethods.Grass.updateTick_optimised: the grass block's spread
 * also takes the mod's mud, turning it to mud grass, on the same tries it
 * spends on dirt. The spread loop's look at each spot it tries (the first
 * getBlockState in randomTick) turns mud there to mud grass and reports that,
 * so vanilla's own dirt test passes it by.
 */
@Mixin(SpreadingSnowyBlock.class)
abstract class LOTRSpreadingSnowyBlockMixin {

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", ordinal = 0,
            target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState lotr$spreadOntoMud(ServerLevel level, BlockPos testPos, Operation<BlockState> original) {
        BlockState state = original.call(level, testPos);
        if ((Object) this == Blocks.GRASS_BLOCK && state.is(LOTRBuildingBlocks.MUD)
                && LOTRMudGrassBlock.canPropagate(LOTRBuildingBlocks.MUD_GRASS.defaultBlockState(), level, testPos)) {
            BlockState mudGrass = LOTRBuildingBlocks.MUD_GRASS.defaultBlockState();
            level.setBlockAndUpdate(testPos, mudGrass);
            return mudGrass;
        }
        return state;
    }
}
