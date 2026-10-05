package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRInventoryNPCItems: the items an NPC keeps to hand and swaps into its
 * hand as it needs them -- what it carries idle, its melee and ranged weapons
 * (on foot and mounted), a spare spear for after it throws one, what its hand
 * held while it eats, and the hired unit's replaced items. Saved as the
 * "NPCItemsInv" slot list, as LOTREntityInventory wrote it. The getters hand
 * back copies, as the original's did.
 */
public class LOTRInventoryNPCItems {

    public static final int IDLE_ITEM = 0;
    public static final int WEAPON_MELEE = 1;
    public static final int WEAPON_RANGED = 2;
    public static final int SPEAR_BACKUP = 3;
    public static final int EATING_BACKUP = 4;
    public static final int IDLE_ITEM_MOUNTED = 5;
    public static final int WEAPON_MELEE_MOUNTED = 6;
    public static final int REPLACED_IDLE = 7;
    public static final int REPLACED_MELEE_MOUNTED = 8;
    public static final int REPLACED_IDLE_MOUNTED = 9;
    public static final int BOMBING_ITEM = 10;
    public static final int BOMB = 11;

    private final LOTRNPCEntity npc;
    private final SimpleContainer items = new SimpleContainer(12);

    LOTRInventoryNPCItems(LOTRNPCEntity npc) {
        this.npc = npc;
    }

    private ItemStack get(int slot) {
        return this.items.getItem(slot).copy();
    }

    private void set(int slot, ItemStack stack) {
        this.items.setItem(slot, stack == null ? ItemStack.EMPTY : stack);
    }

    public ItemStack getIdleItem() { return get(IDLE_ITEM); }
    public void setIdleItem(ItemStack stack) { set(IDLE_ITEM, stack); }
    public ItemStack getMeleeWeapon() { return get(WEAPON_MELEE); }
    public void setMeleeWeapon(ItemStack stack) { set(WEAPON_MELEE, stack); }
    public ItemStack getRangedWeapon() { return get(WEAPON_RANGED); }
    public void setRangedWeapon(ItemStack stack) { set(WEAPON_RANGED, stack); }
    public ItemStack getSpearBackup() { return get(SPEAR_BACKUP); }
    public void setSpearBackup(ItemStack stack) { set(SPEAR_BACKUP, stack); }
    public ItemStack getEatingBackup() { return get(EATING_BACKUP); }
    public void setEatingBackup(ItemStack stack) { set(EATING_BACKUP, stack); }
    public ItemStack getIdleItemMounted() { return get(IDLE_ITEM_MOUNTED); }
    public void setIdleItemMounted(ItemStack stack) { set(IDLE_ITEM_MOUNTED, stack); }
    public ItemStack getMeleeWeaponMounted() { return get(WEAPON_MELEE_MOUNTED); }
    public void setMeleeWeaponMounted(ItemStack stack) { set(WEAPON_MELEE_MOUNTED, stack); }
    public ItemStack getReplacedIdleItem() { return get(REPLACED_IDLE); }
    public void setReplacedIdleItem(ItemStack stack) { set(REPLACED_IDLE, stack); }
    public ItemStack getReplacedMeleeWeaponMounted() { return get(REPLACED_MELEE_MOUNTED); }
    public void setReplacedMeleeWeaponMounted(ItemStack stack) { set(REPLACED_MELEE_MOUNTED, stack); }
    public ItemStack getReplacedIdleItemMounted() { return get(REPLACED_IDLE_MOUNTED); }
    public void setReplacedIdleItemMounted(ItemStack stack) { set(REPLACED_IDLE_MOUNTED, stack); }
    public ItemStack getBombingItem() { return get(BOMBING_ITEM); }
    public void setBombingItem(ItemStack stack) { set(BOMBING_ITEM, stack); }
    public ItemStack getBomb() { return get(BOMB); }
    public void setBomb(ItemStack stack) { set(BOMB, stack); }

    /**
     * A spear in hand with a backup to fall back on (updateCombat's
     * carryingSpearWithBackup). Every spear, the mod's and vanilla's, is a
     * kinetic weapon; only vanilla's are in #minecraft:spears.
     */
    public boolean hasSpearBackup(ItemStack held) {
        return held.has(DataComponents.KINETIC_WEAPON) && !this.items.getItem(SPEAR_BACKUP).isEmpty();
    }

    public boolean getIsEating() {
        return this.npc.isEating();
    }

    public void setIsEating(boolean eating) {
        this.npc.setEating(eating);
    }

    void save(ValueOutput output) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> list = output.list("NPCItemsInv", ItemStackWithSlot.CODEC);
        for (int i = 0; i < this.items.getContainerSize(); ++i) {
            ItemStack stack = this.items.getItem(i);
            if (!stack.isEmpty()) {
                list.add(new ItemStackWithSlot(i, stack));
            }
        }
        output.putBoolean("NPCEating", getIsEating());
    }

    /** readFromNBT: an NPC saved mid-meal gets its hand back. */
    void load(ValueInput input) {
        this.items.clearContent();
        for (ItemStackWithSlot item : input.listOrEmpty("NPCItemsInv", ItemStackWithSlot.CODEC)) {
            if (item.isValidInContainer(this.items.getContainerSize())) {
                this.items.setItem(item.slot(), item.stack());
            }
        }
        if (input.getBooleanOr("NPCEating", false)) {
            this.npc.setItemSlot(EquipmentSlot.MAINHAND, getEatingBackup());
            setEatingBackup(ItemStack.EMPTY);
            setIsEating(false);
        }
    }
}
