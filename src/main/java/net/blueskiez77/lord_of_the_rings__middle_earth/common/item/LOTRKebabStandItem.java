package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRKebabStandBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemKebabStand: a stand picked up with meat on its spit keeps it, and
 * says how much (and whether any is cooked) in its tooltip.
 */
public class LOTRKebabStandItem extends BlockItem implements LOTRTooltipItem {
    public LOTRKebabStandItem(Block block, Properties properties) {
        super(block, properties);
    }

    /** onBlockPlacedBy -> loadKebabData and onReplaced. */
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack,
            BlockState state) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        CustomData data = stack.get(LOTRDataComponents.KEBAB_DATA);
        if (data != null && !level.isClientSide() && level.getBlockEntity(pos) instanceof LOTRKebabStandBlockEntity stand) {
            stand.loadFromItem(data.copyTag(), level.registryAccess());
            return true;
        }
        return updated;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        CustomData data = stack.get(LOTRDataComponents.KEBAB_DATA);
        if (data == null || context.registries() == null) {
            return;
        }
        LOTRKebabStandBlockEntity stand = new LOTRKebabStandBlockEntity(BlockPos.ZERO, getBlock().defaultBlockState());
        stand.readStand(TagValueInput.create(ProblemReporter.DISCARDING, context.registries(), data.copyTag()));
        builder.accept(Component.translatable("block.lotr.kebab_stand.meats", stand.getMeatCount()));
        if (stand.isCooked()) {
            builder.accept(Component.translatable("block.lotr.kebab_stand.cooked"));
        }
    }
}
