package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomeBoneMeal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTREventHandler.onUseBonemeal: a baobab grown from its sapling by bone meal earns growBaobab, and
 * grass in the mod's biomes grows the biome's own plants ({@link LOTRBiomeBoneMeal}).
 */
@Mixin(BoneMealItem.class)
public abstract class LOTRBoneMealItemMixin {

    @WrapMethod(method = "useOn")
    private InteractionResult lotr$growBaobab(UseOnContext context, Operation<InteractionResult> original) {
        boolean baobabSapling = context.getLevel().getBlockState(context.getClickedPos()).is(LOTRDecorationBlocks.BAOBAB_SAPLING);
        InteractionResult result = original.call(context);
        if (baobabSapling && !context.getLevel().isClientSide() && context.getPlayer() != null
                && context.getLevel().getBlockState(context.getClickedPos()).is(LOTRBuildingBlocks.BAOBAB_LOG)) {
            LOTRPlayerAchievements.addAchievement(context.getPlayer(), LOTRAchievement.GROW_BAOBAB);
        }
        return result;
    }

    // growCrop, so a dispenser's bone meal does it too, as the original's BonemealEvent did.
    @WrapOperation(method = "growCrop", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private static void lotr$biomeGrass(BonemealableBlock block, ServerLevel level, RandomSource random, BlockPos pos,
                                        BlockState state, Operation<Void> original) {
        if (LOTRBiomeBoneMeal.applies(level, pos, state)) {
            LOTRBiomeBoneMeal.grow(level, random, pos);
        } else {
            original.call(block, level, random, pos, state);
        }
    }
}
