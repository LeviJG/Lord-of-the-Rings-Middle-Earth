package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.IPickpocketable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRItemCoin's sums: the silver coin is worth 1, the stack 10 and the pile
 * 100. A player's worth counts the main inventory and the stack on the
 * cursor; paying takes the biggest coins first and gives change. Stolen
 * coins are worth nothing to pay with (allowStolen false).
 */
public final class LOTRCoins {

    private static final int[] VALUES = {1, 10, 100};

    private LOTRCoins() {
    }

    /** LOTRItemCoin.values.length: the silver coin, the stack and the pile. */
    public static final int TYPES = VALUES.length;

    /** The coin of this kind: 0 the coin, 1 the stack, 2 the pile (the original's damage values). */
    public static Item coin(int i) {
        return switch (i) {
            case 0 -> LOTRMiscItems.SILVER_COIN;
            case 1 -> LOTRMiscItems.SILVER_COIN_STACK;
            default -> LOTRMiscItems.SILVER_COIN_PILE;
        };
    }

    /** The coin's kind, as its damage value was; -1 for anything else. */
    public static int coinType(ItemStack stack) {
        for (int i = 0; i < VALUES.length; ++i) {
            if (stack.is(coin(i))) {
                return i;
            }
        }
        return -1;
    }

    public static int getSingleItemValue(ItemStack stack) {
        return getSingleItemValue(stack, false);
    }

    public static int getSingleItemValue(ItemStack stack, boolean allowStolen) {
        if (!allowStolen && IPickpocketable.Helper.isPickpocketed(stack)) {
            return 0;
        }
        for (int i = 0; i < VALUES.length; ++i) {
            if (stack.is(coin(i))) {
                return VALUES[i];
            }
        }
        return 0;
    }

    public static int getStackValue(ItemStack stack) {
        return getStackValue(stack, false);
    }

    public static int getStackValue(ItemStack stack, boolean allowStolen) {
        return stack.isEmpty() ? 0 : getSingleItemValue(stack, allowStolen) * stack.getCount();
    }

    public static int getInventoryValue(Player player) {
        int coins = 0;
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            coins += getStackValue(stack);
        }
        return coins + getStackValue(player.containerMenu.getCarried());
    }

    /** giveCoins: the biggest coins that fit, and anything that does not dropped at the player's feet. */
    public static void giveCoins(int coins, Player player) {
        if (coins <= 0) {
            LOTRMod.LOGGER.warn("Attempted to give a non-positive value of coins {} to player {}", coins,
                    player.getName().getString());
        }
        Inventory inv = player.getInventory();
        for (int i = VALUES.length - 1; i >= 0; --i) {
            while (coins >= VALUES[i] && inv.add(new ItemStack(coin(i)))) {
                coins -= VALUES[i];
            }
        }
        for (int i = VALUES.length - 1; i >= 0; --i) {
            while (coins >= VALUES[i]) {
                player.drop(new ItemStack(coin(i)), false);
                coins -= VALUES[i];
            }
        }
    }

    /**
     * takeCoins: first the biggest coins no bigger than the sum, one at a
     * time from the cursor and then the inventory; then, if that fell short,
     * break the smallest bigger coin and give back the change.
     */
    public static void takeCoins(int coins, Player player) {
        int invValue = getInventoryValue(player);
        if (invValue < coins) {
            LOTRMod.LOGGER.warn("Attempted to take {} coins from player {} who has only {}", coins,
                    player.getName().getString(), invValue);
        }
        int initCoins = coins;
        outer:
        for (int i = VALUES.length - 1; i >= 0; --i) {
            int value = VALUES[i];
            if (value > initCoins) {
                continue;
            }
            for (int slot = -1; slot < player.getInventory().getNonEquipmentItems().size(); ++slot) {
                while (takeOne(player, slot, coin(i))) {
                    coins -= value;
                    if (coins < value) {
                        continue outer;
                    }
                }
            }
        }
        if (coins > 0) {
            for (int i = 1; i < VALUES.length; ++i) {
                int value = VALUES[i];
                inner:
                for (int slot = -1; slot < player.getInventory().getNonEquipmentItems().size(); ++slot) {
                    while (takeOne(player, slot, coin(i))) {
                        coins -= value;
                        if (coins < 0) {
                            break inner;
                        }
                    }
                }
                if (coins < 0) {
                    break;
                }
            }
        }
        if (coins < 0) {
            giveCoins(-coins, player);
        }
    }

    /** One coin of this kind from the cursor (slot -1) or an inventory slot, if there is one. */
    private static boolean takeOne(Player player, int slot, Item coin) {
        ItemStack stack = slot == -1 ? player.containerMenu.getCarried()
                : player.getInventory().getNonEquipmentItems().get(slot);
        if (stack.isEmpty() || !stack.is(coin)) {
            return false;
        }
        stack.shrink(1);
        return true;
    }
}
