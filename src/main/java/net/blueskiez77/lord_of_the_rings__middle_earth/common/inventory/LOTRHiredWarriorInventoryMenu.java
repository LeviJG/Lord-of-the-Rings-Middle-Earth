package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTROrcBombBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryHiredReplacedItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerHiredWarriorInventory: the unit's armour and melee weapon (and
 * a bombardier's bomb), which its player may replace with their own. What
 * the unit had is kept ({@link LOTRInventoryHiredReplacedItems}) and goes
 * back on when the player's piece is taken off.
 */
public class LOTRHiredWarriorInventoryMenu extends AbstractContainerMenu {

    private static final Identifier MELEE_SLOT = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "container/slot/melee");
    private static final Identifier BOMB_SLOT = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "container/slot/bomb");
    private static final Identifier[] ARMOR_SLOTS = {InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS};

    public final @Nullable LOTRNPCEntity theNPC;
    /** proxyInv: the slots' own copy; a change is passed on to the unit. */
    private final SimpleContainer proxyInv = new SimpleContainer(LOTRInventoryHiredReplacedItems.SIZE);
    private int npcActiveSlotCount;

    public LOTRHiredWarriorInventoryMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory, inventory.player.level().getEntity(entityId) instanceof LOTRNPCEntity npc ? npc : null);
    }

    public LOTRHiredWarriorInventoryMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity npc) {
        super(LOTRMenus.HIRED_WARRIOR_INVENTORY, containerId);
        this.theNPC = npc;
        if (npc != null) {
            for (int i = 0; i < 4; ++i) {
                EquipmentSlot armorSlot = LOTRInventoryHiredReplacedItems.getNPCArmorSlot(i);
                Identifier icon = ARMOR_SLOTS[i];
                addSlot(new ReplaceSlot(i, 80, 21 + i * 18, icon) {
                    @Override
                    boolean parentValid(ItemStack stack) {
                        var equippable = stack.get(DataComponents.EQUIPPABLE);
                        return equippable != null && equippable.slot() == armorSlot;
                    }
                });
            }
            addSlot(new ReplaceSlot(LOTRInventoryHiredReplacedItems.MELEE, 50, 48, MELEE_SLOT) {
                @Override
                boolean parentValid(ItemStack stack) {
                    return isMeleeWeapon(stack);
                }
            });
            if (isBombardier()) {
                addSlot(new ReplaceSlot(LOTRInventoryHiredReplacedItems.BOMB, 110, 48, BOMB_SLOT) {
                    @Override
                    boolean parentValid(ItemStack stack) {
                        return stack.getItem() instanceof BlockItem block && block.getBlock() instanceof LOTROrcBombBlock;
                    }
                });
            }
        }
        this.npcActiveSlotCount = this.slots.size();
        addStandardInventorySlots(inventory, 8, 107);
    }

    public boolean isBombardier() {
        return this.theNPC instanceof LOTROrcEntity orc && orc.isOrcBombardier();
    }

    /** LOTRWeaponStats.isMeleeWeapon: the item carries the weapon-damage modifier. */
    public static boolean isMeleeWeapon(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        for (var entry : modifiers.modifiers()) {
            if (entry.modifier().id().equals(Item.BASE_ATTACK_DAMAGE_ID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOpenOn(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRHiredWarriorInventoryMenu menu && menu.theNPC == npc;
    }

    @Override
    public boolean stillValid(Player player) {
        LOTRNPCEntity npc = this.theNPC;
        return npc != null && npc.isAlive() && npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player
                && npc.hiredNPCInfo.getTask() == LOTRHiredTask.WARRIOR && player.distanceToSqr(npc) <= 144.0;
    }

    /** onContainerClosed: back to the unit's screen. */
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide() && this.theNPC != null) {
            this.theNPC.hiredNPCInfo.sendClientPacket(true);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (slot.container == proxyInv) {
            if (!moveItemStackTo(stack, this.npcActiveSlotCount, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            for (int j = 0; j < this.npcActiveSlotCount; ++j) {
                Slot npcSlot = slots.get(j);
                if (npcSlot.mayPlace(stack) && !moveItemStackTo(stack, j, j + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
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

    /**
     * LOTRSlotHiredReplaceItem: shows the player's piece if the unit wears
     * one; takes only what the slot and the unit allow, one at a time.
     */
    private abstract class ReplaceSlot extends Slot {
        private final Identifier icon;

        ReplaceSlot(int index, int x, int y, Identifier icon) {
            super(proxyInv, index, x, y);
            this.icon = icon;
            LOTRNPCEntity npc = LOTRHiredWarriorInventoryMenu.this.theNPC;
            if (!npc.level().isClientSide() && npc.hiredReplacedInv.hasReplacedEquipment(index)) {
                proxyInv.setItem(index, npc.hiredReplacedInv.getEquippedReplacement(index));
            }
        }

        abstract boolean parentValid(ItemStack stack);

        @Override
        public Identifier getNoItemIcon() {
            return this.icon;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return parentValid(stack) && LOTRHiredWarriorInventoryMenu.this.theNPC.canReEquipHired(getContainerSlot(), stack);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            LOTRNPCEntity npc = LOTRHiredWarriorInventoryMenu.this.theNPC;
            if (!npc.level().isClientSide()) {
                npc.hiredReplacedInv.onEquipmentChanged(getContainerSlot(), getItem());
            }
        }
    }
}
