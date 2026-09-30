package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRInventoryNPC (LOTREntityInventory): a small named inventory an NPC
 * carries -- a hired farmer's seeds and harvest -- saved under its name and
 * dropped (as the NPC's own drops) when it is let go or dies.
 */
public class LOTRInventoryNPC extends SimpleContainer {

    private final LOTRNPCEntity theNPC;
    private final String nbtName;

    public LOTRInventoryNPC(String name, LOTRNPCEntity npc, int size) {
        super(size);
        this.theNPC = npc;
        this.nbtName = name;
    }

    /** addItemToInventory: into matching stacks, then empty slots; true if any went in. */
    public boolean addItemToInventory(ItemStack stack) {
        int origCount = stack.getCount();
        for (int i = 0; i < getContainerSize() && stack.getCount() > 0; ++i) {
            ItemStack inSlot = getItem(i);
            if (inSlot.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(Math.min(copy.getCount(), getMaxStackSize()));
                setItem(i, copy);
                stack.shrink(copy.getCount());
                continue;
            }
            if (inSlot.getCount() >= inSlot.getMaxStackSize() || !ItemStack.isSameItemSameComponents(inSlot, stack)) {
                continue;
            }
            int max = Math.min(inSlot.getMaxStackSize(), getMaxStackSize());
            int difference = Math.min(max - inSlot.getCount(), stack.getCount());
            stack.shrink(difference);
            inSlot.grow(difference);
            setItem(i, inSlot);
        }
        return stack.getCount() < origCount;
    }

    public void dropAllItems() {
        if (!(this.theNPC.level() instanceof ServerLevel level)) {
            return;
        }
        for (int i = 0; i < getContainerSize(); ++i) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                this.theNPC.spawnAtLocation(level, stack);
                setItem(i, ItemStack.EMPTY);
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void save(ValueOutput output) {
        ValueOutput.TypedOutputList<net.minecraft.world.ItemStackWithSlot> list =
                output.list(this.nbtName, net.minecraft.world.ItemStackWithSlot.CODEC);
        for (int i = 0; i < getContainerSize(); ++i) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                list.add(new net.minecraft.world.ItemStackWithSlot(i, stack));
            }
        }
    }

    public void load(ValueInput input) {
        for (net.minecraft.world.ItemStackWithSlot item : input.listOrEmpty(this.nbtName, net.minecraft.world.ItemStackWithSlot.CODEC)) {
            if (item.isValidInContainer(getContainerSize())) {
                setItem(item.slot(), item.stack());
            }
        }
    }
}
