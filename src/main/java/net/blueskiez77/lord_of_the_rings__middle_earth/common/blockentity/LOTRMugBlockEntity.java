package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityMug: what is in a set-down vessel, and which vessel it is.
 *
 * <p>The vessel is remembered separately from the drink because a drink can be
 * poured in from a different cup; with none stored, the block says which it is.
 */
public class LOTRMugBlockEntity extends BlockEntity {
    private ItemStack mugItem = ItemStack.EMPTY;
    private @Nullable LOTRVessel mugVessel;
    private boolean creativeBroken;

    public LOTRMugBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.MUG, pos, state);
    }

    public LOTRVessel getVessel() {
        if (mugVessel != null) {
            return mugVessel;
        }
        return getBlockState().getBlock() instanceof LOTRMugBlock block ? block.vessel() : LOTRVessel.MUG;
    }

    public void setVessel(LOTRVessel vessel) {
        mugVessel = vessel;
        sync();
    }

    /** getMugItem: the drink, or the empty vessel. */
    public ItemStack getMugItem() {
        if (mugItem.isEmpty()) {
            return getVessel().emptyStack();
        }
        return mugItem.copy();
    }

    public void setMugItem(ItemStack stack) {
        mugItem = stack.isEmpty() ? ItemStack.EMPTY : stack;
        sync();
    }

    public void setEmpty() {
        setMugItem(ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        return !LOTRVessel.isFullDrink(getMugItem());
    }

    /** getMugItemForRender. */
    public ItemStack getMugItemForRender() {
        return LOTRVessel.equivalentDrink(getMugItem());
    }

    public void markCreativeBroken() {
        creativeBroken = true;
    }

    /** updateEntity: an empty vessel under open, rainy sky slowly fills with water. */
    public static void serverTick(Level level, BlockPos pos, BlockState state, LOTRMugBlockEntity mug) {
        if (mug.isEmpty() && level.isRainingAt(pos) && level.getRandom().nextInt(6000) == 0) {
            ItemStack water = new ItemStack(LOTRFoodItems.WATER);
            water.set(LOTRDataComponents.VESSEL, mug.getVessel());
            mug.setMugItem(water);
        }
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /** getDrops: the vessel and its drink, unless a creative player broke it. */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null && !creativeBroken) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getMugItem());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        mugItem = input.read("MugItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        mugVessel = input.read("Vessel", LOTRVessel.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("HasMugItem", !mugItem.isEmpty());
        if (!mugItem.isEmpty()) {
            output.store("MugItem", ItemStack.CODEC, mugItem);
        }
        if (mugVessel != null) {
            output.store("Vessel", LOTRVessel.CODEC, mugVessel);
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
