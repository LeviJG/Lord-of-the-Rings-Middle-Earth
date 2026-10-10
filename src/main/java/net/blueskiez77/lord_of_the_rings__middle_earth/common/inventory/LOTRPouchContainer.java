package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRInventoryPouch: the goods of a pouch held by a player, saved back into the pouch as they
 * change -- on the server only, which the client's copy follows.
 */
public class LOTRPouchContainer extends SimpleContainer {

    private final Supplier<ItemStack> pouch;
    private final boolean save;

    public LOTRPouchContainer(Supplier<ItemStack> pouch, int size, boolean save) {
        super(size);
        this.pouch = pouch;
        this.save = save;
        if (save) {
            NonNullList<ItemStack> items = LOTRPouchItem.getContents(pouch.get());
            for (int i = 0; i < Math.min(items.size(), size); ++i) {
                setItem(i, items.get(i));
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.save) {
            ItemStack stack = this.pouch.get();
            if (LOTRPouchItem.isPouch(stack)) {
                LOTRPouchItem.setContents(stack, getItems());
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return !LOTRPouchItem.isPouch(stack);
    }
}
