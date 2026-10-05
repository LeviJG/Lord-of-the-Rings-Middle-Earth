package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemBanner: a faction banner, standing or hung. Stood on a block of
 * bronze, silver or gold it would protect land, so -- out of creative, with
 * protection allowed -- it asks at least +1 alignment with its faction, and
 * may not go where its land would reach into land another banner already
 * protects against the player. It carries a banner's protection
 * ({@code LOTRBannerData}) from where it was broken to where it is placed,
 * and says so; placed by anyone but a creative player sneaking, it becomes
 * that player's (LOTRBannerBlock.setPlacedBy).
 */
public class LOTRBannerItem extends StandingAndWallBlockItem implements LOTRTooltipItem {

    public LOTRBannerItem(Block standing, Block wall, Properties properties) {
        super(standing, wall, Direction.DOWN, properties);
    }

    public static boolean hasProtectionData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getCompound(LOTRBannerBlockEntity.ITEM_DATA_KEY).isPresent();
    }

    /** shouldKeepOriginalOwnerOnPlacement: a creative player sneaking keeps the banner's owner. */
    public static boolean shouldKeepOriginalOwnerOnPlacement(@Nullable Player player) {
        return player != null && player.isCreative() && player.isShiftKeyDown();
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.getPlayer();
        BlockPlaceContext placeContext = updatePlacementContext(context);
        BlockState state = placeContext == null ? null : getPlacementState(placeContext);
        if (player != null && state != null && state.getBlock() instanceof LOTRBannerBlock standing
                && LOTRConfig.allowBannerProtection && !player.isCreative()) {
            Level level = context.getLevel();
            BlockPos pos = placeContext.getClickedPos();
            int protectRange = LOTRBannerProtection.getProtectionRange(level.getBlockState(pos.below()).getBlock());
            if (protectRange > 0) {
                LOTRFaction faction = standing.getBannerType().faction;
                if (LOTRPlayerAlignments.getAlignment(player, faction) < 1.0f) {
                    if (!level.isClientSide()) {
                        LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, 1.0f, faction);
                    }
                    return InteractionResult.FAIL;
                }
                if (!level.isClientSide() && LOTRBannerProtection.isProtected(level, pos,
                        LOTRBannerProtection.forPlayer(player, LOTRBannerProtection.Permission.FULL), false, protectRange)) {
                    player.sendSystemMessage(Component.translatable("chat.lotr.alreadyProtected"));
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.place(context);
    }

    @Override
    public void addTooltip(ItemStack stack, TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        if (hasProtectionData(stack)) {
            builder.accept(Component.translatable("item.lotr.banner.protect"));
        }
    }
}
