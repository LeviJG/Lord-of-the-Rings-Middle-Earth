package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDaleCrackerItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRContainerDaleCracker: three slots to put a gift in, and a Seal button.
 * Sealing moves the gift into the held cracker; closing the window without
 * sealing hands the gift back.
 *
 * <p>The seal button is menu button 0, which vanilla's container-button packet
 * carries -- the job LOTRPacketSealCracker did.
 */
public class LOTRDaleCrackerMenu extends AbstractContainerMenu {
    public static final int SEAL_BUTTON = 0;
    private static final int CAPACITY = LOTRDaleCrackerItem.CAPACITY;

    private final SimpleContainer contents = new SimpleContainer(CAPACITY);
    private final InteractionHand hand;
    private final int lockedSlot;

    public LOTRDaleCrackerMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, InteractionHand.MAIN_HAND);
    }

    public LOTRDaleCrackerMenu(int containerId, Inventory inventory, InteractionHand hand) {
        super(LOTRMenus.DALE_CRACKER, containerId);
        this.hand = hand;
        for (int i = 0; i < CAPACITY; i++) {
            addSlot(new Slot(contents, i, 62 + i * 18, 24));
        }
        addStandardInventorySlots(inventory, 8, 84);
        // slotClick refused the player's current hotbar slot, so the cracker
        // itself cannot be moved -- or sealed inside itself.
        this.lockedSlot = hand == InteractionHand.MAIN_HAND
                ? CAPACITY + 27 + inventory.getSelectedSlot() : -1;
    }

    /** isCrackerInvEmpty. */
    public boolean isContentsEmpty() {
        return contents.isEmpty();
    }

    /** canInteractWith: the same unsealed cracker is still in hand. */
    @Override
    public boolean stillValid(Player player) {
        ItemStack held = player.getItemInHand(hand);
        return held.getItem() instanceof LOTRDaleCrackerItem && LOTRDaleCrackerItem.isEmpty(held);
    }

    /** receiveSealingPacket. */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != SEAL_BUTTON || contents.isEmpty() || !stillValid(player)) {
            return false;
        }
        List<ItemStack> gift = new ArrayList<>();
        for (int i = 0; i < CAPACITY; i++) {
            gift.add(contents.removeItemNoUpdate(i));
        }
        LOTRDaleCrackerItem.seal(player.getItemInHand(hand), gift, player);
        return true;
    }

    @Override
    public void clicked(int slotIndex, int button, ContainerInput input, Player player) {
        // checkHotbarKeys returned false: no number key may swap the cracker out either.
        if (slotIndex == lockedSlot
                || input == ContainerInput.SWAP && lockedSlot >= 0 && CAPACITY + 27 + button == lockedSlot) {
            return;
        }
        super.clicked(slotIndex, button, input, player);
    }

    /** onContainerClosed: an unsealed gift goes back to the player. */
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            clearContainer(player, contents);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (index == lockedSlot || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < CAPACITY) {
            if (!moveItemStackTo(stack, CAPACITY, CAPACITY + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, CAPACITY, false)) {
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
