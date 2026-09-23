package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/** LOTRTileEntityPlate: the food on a plate, synced so it can be drawn. */
public class LOTRPlateBlockEntity extends BlockEntity {
    private ItemStack foodItem = ItemStack.EMPTY;

    public LOTRPlateBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.PLATE, pos, state);
    }

    /**
     * isValidFoodItem: food, but not a stew or anything else that hands back a
     * container when eaten.
     */
    public static boolean isValidFoodItem(ItemStack stack) {
        if (stack.isEmpty() || !stack.has(DataComponents.FOOD)) {
            return false;
        }
        return !stack.has(DataComponents.USE_REMAINDER) && stack.getItem().getCraftingRemainder() == null;
    }

    public ItemStack getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(ItemStack stack) {
        foodItem = stack.isEmpty() ? ItemStack.EMPTY : stack;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /** breakBlock: the food falls off. */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null && !foodItem.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), foodItem);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        foodItem = input.read("FoodItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("PlateEmpty", foodItem.isEmpty());
        if (!foodItem.isEmpty()) {
            output.store("FoodItem", ItemStack.CODEC, foodItem);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
