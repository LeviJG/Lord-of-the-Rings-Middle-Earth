package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;

import org.spongepowered.asm.mixin.Mixin;

/** LOTREventHandler.onBonemeal: a baobab grown from its sapling by bone meal earns growBaobab. */
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
}
