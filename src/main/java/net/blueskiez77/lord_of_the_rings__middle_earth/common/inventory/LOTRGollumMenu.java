package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/** LOTRContainerGollum: Gollum's pack of three rows, open only to his master. */
public class LOTRGollumMenu extends AbstractContainerMenu {

    private static final int SIZE = LOTRGollumEntity.INV_ROWS * 9;

    public final @Nullable LOTRGollumEntity theGollum;

    public LOTRGollumMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory, inventory.player.level().getEntity(entityId) instanceof LOTRGollumEntity g ? g : null);
    }

    public LOTRGollumMenu(int containerId, Inventory inventory, @Nullable LOTRGollumEntity gollum) {
        super(LOTRMenus.GOLLUM, containerId);
        this.theGollum = gollum;
        Container pack = gollum != null ? gollum.inventory : new SimpleContainer(SIZE);
        for (int i = 0; i < LOTRGollumEntity.INV_ROWS; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(pack, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        addStandardInventorySlots(inventory, 8, 86);
    }

    @Override
    public boolean stillValid(Player player) {
        LOTRGollumEntity gollum = this.theGollum;
        return gollum != null && gollum.getGollumOwner() == player && player.distanceToSqr(gollum) <= 144.0 && gollum.isAlive();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < SIZE ? !moveItemStackTo(stack, SIZE, this.slots.size(), true) : !moveItemStackTo(stack, 0, SIZE, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }
}
