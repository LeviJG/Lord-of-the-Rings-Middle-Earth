package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBarrelBoatEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * LOTRItemBarrel: a barrel that remembers what it held.
 *
 * <p>The original kept the block entity's NBT under "LOTRBarrelData"; that is
 * the {@code lotr:barrel_data} component here. Placing the barrel loads it back
 * in, and the tooltip shows the same subtitle as the barrel's screen.
 *
 * <p>Aimed at still water it sets the barrel afloat to be ridden
 * (onItemRightClick, LOTRBarrelBoatEntity).
 */
public class LOTRBarrelItem extends BlockItem implements LOTRTooltipItem {
    public LOTRBarrelItem(Block block, Properties properties) {
        super(block, properties);
    }

    /**
     * onItemRightClick: aimed at still water, the barrel goes on it to be
     * ridden, square to the player's facing and carrying what it held.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        BlockPos pos = hit.getBlockPos();
        if (!level.getFluidState(pos).is(FluidTags.WATER) || !level.getFluidState(pos).isSource()) {
            return InteractionResult.PASS;
        }
        LOTRBarrelBoatEntity barrel = new LOTRBarrelBoatEntity(LOTREntities.BARREL, level);
        barrel.setInitialPos(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        barrel.setYRot(((Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 3) - 1) * 90.0f);
        if (!level.noCollision(barrel, barrel.getBoundingBox().deflate(0.1))) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            barrel.setBarrelItem(stack);
            level.addFreshEntity(barrel);
        }
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    /** placeBlockAt -> loadBarrelDataToTE. */
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack,
            BlockState state) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        CustomData data = stack.get(LOTRDataComponents.BARREL_DATA);
        if (data != null && !level.isClientSide() && level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel) {
            barrel.loadFromTag(data.copyTag(), level.registryAccess());
            barrel.setChanged();
            return true;
        }
        return updated;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        CustomData data = stack.get(LOTRDataComponents.BARREL_DATA);
        if (data == null || context.registries() == null) {
            return;
        }
        LOTRBarrelBlockEntity barrel = new LOTRBarrelBlockEntity(BlockPos.ZERO, getBlock().defaultBlockState());
        barrel.loadFromTag(data.copyTag(), context.registries());
        builder.accept(barrel.subtitle());
    }
}
