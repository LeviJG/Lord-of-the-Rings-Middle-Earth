package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * LOTRItemVessel: an EMPTY mug, goblet, horn or skin.
 *
 * <p>onItemRightClick: aimed at a still water source it fills -- one vessel from
 * the stack becomes a vessel of water, with the mug-fill splash.
 *
 * <p>onItemUse: used on a block, the empty vessel is set down there.
 */
public class LOTRVesselItem extends Item {

    private final LOTRVessel vessel;

    public LOTRVesselItem(LOTRVessel vessel, Properties properties) {
        super(properties);
        this.vessel = vessel;
    }

    public LOTRVessel vessel() {
        return this.vessel;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock.tryPlaceMug(
                context, context.getItemInHand());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        BlockPos pos = hit.getBlockPos();
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hit.getDirection(), stack)) {
            return InteractionResult.PASS;
        }
        FluidState fluid = level.getFluidState(pos);
        if (!fluid.is(FluidTags.WATER) || !fluid.isSource()) {
            return InteractionResult.PASS;
        }
        ItemStack water = new ItemStack(LOTRItems.WATER);
        water.set(LOTRDataComponents.VESSEL, this.vessel);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), LOTRSounds.ITEM_MUG_FILL,
                SoundSource.PLAYERS, 0.5f, 0.8f + level.getRandom().nextFloat() * 0.4f);
        return InteractionResult.SUCCESS.heldItemTransformedTo(ItemUtils.createFilledResult(stack, player, water));
    }
}
