package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCRideableEntity;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerNPCMountInventory: a tame mount's saddle and barding, laid out
 * as the horse's screen is.
 */
public class LOTRNPCMountInventoryMenu extends AbstractContainerMenu {

    private static final Identifier SADDLE_SLOT = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier ARMOR_SLOT = Identifier.withDefaultNamespace("container/slot/horse_armor");

    public final @Nullable LOTRNPCRideableEntity theMount;
    private final Container theMountInv;

    public LOTRNPCMountInventoryMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory,
                inventory.player.level().getEntity(entityId) instanceof LOTRNPCRideableEntity mount ? mount : null);
    }

    public LOTRNPCMountInventoryMenu(int containerId, Inventory inventory, @Nullable LOTRNPCRideableEntity mount) {
        super(LOTRMenus.NPC_MOUNT_INVENTORY, containerId);
        this.theMount = mount;
        Container mountInv = mount == null ? null : mount.getMountInventory();
        this.theMountInv = mountInv != null ? mountInv : new SimpleContainer(2);
        this.theMountInv.startOpen(inventory.player);
        addSlot(new Slot(this.theMountInv, 0, 8, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.SADDLE) && !hasItem();
            }

            @Override
            public Identifier getNoItemIcon() {
                return SADDLE_SLOT;
            }
        });
        addSlot(new Slot(this.theMountInv, 1, 8, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return mount != null && mount.isMountArmorValid(stack);
            }

            @Override
            public Identifier getNoItemIcon() {
                return ARMOR_SLOT;
            }
        });
        int yOffset = (3 - 4) * 18;
        addStandardInventorySlots(inventory, 8, 102 + yOffset);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.theMount != null && this.theMountInv.stillValid(player) && this.theMount.isAlive()
                && this.theMount.distanceTo(player) < 8.0f;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.theMountInv.stopOpen(player);
    }

    /** transferStackInSlot: armour to its slot if free, a saddle to its own. */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        int mountSize = this.theMountInv.getContainerSize();
        if (index < mountSize) {
            if (!moveItemStackTo(stack, mountSize, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (getSlot(1).mayPlace(stack) && !getSlot(1).hasItem()) {
            if (!moveItemStackTo(stack, 1, 2, false)) {
                return ItemStack.EMPTY;
            }
        } else if (getSlot(0).mayPlace(stack)) {
            if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (mountSize <= 2 || !moveItemStackTo(stack, 2, mountSize, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }
}
