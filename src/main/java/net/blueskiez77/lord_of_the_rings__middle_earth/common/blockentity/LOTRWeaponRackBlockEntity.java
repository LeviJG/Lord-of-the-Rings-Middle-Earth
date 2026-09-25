package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRTileEntityWeaponRack: the one weapon a rack is displaying.
 *
 * <p>canAcceptItem asked LOTRWeaponStats whether the item was a melee or ranged
 * weapon, plus hoes and fishing rods by name. Those stats do not exist yet, so
 * the question is an item tag instead -- see {@link LOTRItemTags#WEAPON_RACK_HOLDABLE}.
 */
public class LOTRWeaponRackBlockEntity extends BlockEntity {

    private ItemStack weapon = ItemStack.EMPTY;

    public LOTRWeaponRackBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.WEAPON_RACK, pos, state);
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack stack) {
        this.weapon = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public static boolean canAccept(ItemStack stack) {
        return !stack.isEmpty() && stack.is(LOTRItemTags.WEAPON_RACK_HOLDABLE);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        weapon = input.read("Weapon", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!weapon.isEmpty()) {
            output.store("Weapon", ItemStack.CODEC, weapon);
        }
    }

    // The weapon has to reach clients: it is what the renderer draws.
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    /** breakBlock dropped whatever was on the rack alongside the rack itself. */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !getWeapon().isEmpty()) {
            Block.popResource(level, pos, getWeapon());
        }
    }
}
