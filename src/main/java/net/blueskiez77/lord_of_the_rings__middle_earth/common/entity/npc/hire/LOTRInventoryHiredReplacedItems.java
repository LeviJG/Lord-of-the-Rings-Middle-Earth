package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRInventoryHiredReplacedItems: what a hired unit wore and wielded before
 * its player gave it something else. Taking the player's piece back puts the
 * unit's own on again; when the unit dies, the player's pieces drop and its
 * own go back on first, so it is those that fall as its equipment.
 */
public class LOTRInventoryHiredReplacedItems extends LOTRInventoryNPC {

    public static final int HELMET = 0;
    public static final int BODY = 1;
    public static final int LEGS = 2;
    public static final int BOOTS = 3;
    public static final int MELEE = 4;
    public static final int BOMB = 5;
    public static final int RANGED = 6;
    public static final int SIZE = 7;

    private final LOTRNPCEntity theNPC;
    private final boolean[] hasReplacedEquipment = new boolean[SIZE];
    private boolean replacedMeleeWeapons;

    public LOTRInventoryHiredReplacedItems(LOTRNPCEntity npc) {
        super("HiredReplacedItems", npc, SIZE);
        this.theNPC = npc;
    }

    public void dropAllReplacedItems() {
        if (!(this.theNPC.level() instanceof ServerLevel level)) {
            return;
        }
        for (int i = 0; i < SIZE; ++i) {
            ItemStack stack;
            if (!hasReplacedEquipment(i) || (stack = getEquippedReplacement(i)).isEmpty()) {
                continue;
            }
            this.theNPC.dropUnpouched(level, stack);
            equipReplacement(i, getReplacedEquipment(i));
            setReplacedEquipment(i, ItemStack.EMPTY, false);
        }
    }

    public void equipReplacement(int i, ItemStack stack) {
        var items = this.theNPC.npcItemsInv;
        switch (i) {
            case MELEE -> {
                boolean idleMelee = ItemStack.matches(items.getMeleeWeapon(), items.getIdleItem());
                items.setMeleeWeapon(stack);
                if (!this.replacedMeleeWeapons) {
                    items.setReplacedIdleItem(items.getIdleItem());
                    items.setReplacedMeleeWeaponMounted(items.getMeleeWeaponMounted());
                    items.setReplacedIdleItemMounted(items.getIdleItemMounted());
                    this.replacedMeleeWeapons = true;
                }
                items.setMeleeWeaponMounted(stack);
                if (idleMelee) {
                    items.setIdleItem(stack);
                    items.setIdleItemMounted(stack);
                }
                updateHeldItem();
            }
            case RANGED -> {
                items.setRangedWeapon(stack);
                updateHeldItem();
            }
            case BOMB -> {
                items.setBomb(stack);
                updateHeldItem();
            }
            default -> this.theNPC.setItemSlot(getNPCArmorSlot(i), stack);
        }
    }

    public ItemStack getEquippedReplacement(int i) {
        return switch (i) {
            case MELEE -> this.theNPC.npcItemsInv.getMeleeWeapon();
            case RANGED -> this.theNPC.npcItemsInv.getRangedWeapon();
            case BOMB -> this.theNPC.npcItemsInv.getBomb();
            default -> this.theNPC.getItemBySlot(getNPCArmorSlot(i));
        };
    }

    /** getNPCArmorSlot: 1.7.10's 4 - i, helmet first. */
    public static EquipmentSlot getNPCArmorSlot(int i) {
        return switch (i) {
            case HELMET -> EquipmentSlot.HEAD;
            case BODY -> EquipmentSlot.CHEST;
            case LEGS -> EquipmentSlot.LEGS;
            default -> EquipmentSlot.FEET;
        };
    }

    public ItemStack getReplacedEquipment(int i) {
        return getItem(i).copy();
    }

    public boolean hasReplacedEquipment(int i) {
        return this.hasReplacedEquipment[i];
    }

    public void onEquipmentChanged(int i, ItemStack newItem) {
        if (newItem.isEmpty()) {
            if (hasReplacedEquipment(i)) {
                equipReplacement(i, getReplacedEquipment(i));
                setReplacedEquipment(i, ItemStack.EMPTY, false);
            }
        } else {
            if (!hasReplacedEquipment(i)) {
                setReplacedEquipment(i, getEquippedReplacement(i), true);
            }
            equipReplacement(i, newItem.copy());
        }
    }

    public void setReplacedEquipment(int i, ItemStack stack, boolean flag) {
        setItem(i, stack);
        this.hasReplacedEquipment[i] = flag;
        if (!flag && i == MELEE) {
            if (this.replacedMeleeWeapons) {
                var items = this.theNPC.npcItemsInv;
                items.setIdleItem(items.getReplacedIdleItem());
                items.setMeleeWeaponMounted(items.getReplacedMeleeWeaponMounted());
                items.setIdleItemMounted(items.getReplacedIdleItemMounted());
                items.setReplacedMeleeWeaponMounted(ItemStack.EMPTY);
                items.setReplacedIdleItem(ItemStack.EMPTY);
                items.setReplacedIdleItemMounted(ItemStack.EMPTY);
                this.replacedMeleeWeapons = false;
            }
            updateHeldItem();
        }
    }

    private void updateHeldItem() {
        if (!this.theNPC.npcItemsInv.getIsEating()) {
            this.theNPC.refreshCurrentAttackMode();
        }
    }

    @Override
    public void save(ValueOutput output) {
        super.save(output);
        for (int i = 0; i < SIZE; ++i) {
            output.putBoolean("ReplacedFlag_" + i, this.hasReplacedEquipment[i]);
        }
        output.putBoolean("ReplacedMelee", this.replacedMeleeWeapons);
    }

    @Override
    public void load(ValueInput input) {
        super.load(input);
        for (int i = 0; i < SIZE; ++i) {
            this.hasReplacedEquipment[i] = input.getBooleanOr("ReplacedFlag_" + i, false);
        }
        this.replacedMeleeWeapons = input.getBooleanOr("ReplacedMelee", false);
    }
}
