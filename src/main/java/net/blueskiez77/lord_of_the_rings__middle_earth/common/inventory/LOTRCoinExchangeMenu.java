package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerCoinExchange: coins in the middle, and either side what they
 * change into -- ten of the next smaller kind each, or one of the next bigger
 * kind for every ten. A button (menu button 0 or 1, in place of
 * LOTRPacketCoinExchange) makes the exchange; then the chosen side can be
 * taken, and once it is all gone the exchange is offered again.
 *
 * <p>NOT ported yet: refusing pickpocketed coins (IPickpocketable).
 */
public class LOTRCoinExchangeMenu extends AbstractContainerMenu {

    /** InventoryBasic's stack limit. */
    private static final int STACK_LIMIT = 64;

    public final SimpleContainer coinInputInv = new ExchangeContainer(1);
    public final SimpleContainer exchangeInv = new ExchangeContainer(2);
    public final @Nullable LOTRNPCEntity theTraderNPC;
    /** exchanged, sent to the client as progress bar 0. */
    private final DataSlot exchanged = DataSlot.standalone();

    public LOTRCoinExchangeMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory,
                inventory.player.level().getEntity(entityId) instanceof LOTRNPCEntity npc ? npc : null);
    }

    public LOTRCoinExchangeMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity npc) {
        super(LOTRMenus.COIN_EXCHANGE, containerId);
        this.theTraderNPC = npc;
        addSlot(new Slot(coinInputInv, 0, 80, 46) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isValidCoin(stack);
            }
        });
        addSlot(new ResultSlot(exchangeInv, 0, 26, 46));
        addSlot(new ResultSlot(exchangeInv, 1, 134, 46));
        addStandardInventorySlots(inventory, 8, 106);
        addDataSlot(exchanged);
        slotsChanged(coinInputInv);
    }

    public static boolean isValidCoin(ItemStack stack) {
        return LOTRCoins.coinType(stack) >= 0;
    }

    public static boolean isExchangingWith(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRCoinExchangeMenu menu && menu.theTraderNPC == npc;
    }

    public boolean isExchanged() {
        return exchanged.get() == 1;
    }

    private void setExchanged(boolean flag) {
        exchanged.set(flag ? 1 : 0);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /** handleExchangePacket. */
    @Override
    public boolean clickMenuButton(Player player, int slot) {
        if (isExchanged() || coinInputInv.getItem(0).isEmpty() || slot < 0 || slot >= exchangeInv.getContainerSize()
                || exchangeInv.getItem(slot).isEmpty()) {
            return false;
        }
        setExchanged(true);
        int coins = exchangeInv.getItem(slot).getCount();
        int coinsTaken = slot == 0 ? coins / 10 : coins * 10;
        coinInputInv.removeItem(0, coinsTaken);
        for (int i = 0; i < exchangeInv.getContainerSize(); ++i) {
            if (i != slot) {
                exchangeInv.setItem(i, ItemStack.EMPTY);
            }
        }
        broadcastChanges();
        if (this.theTraderNPC != null) {
            this.theTraderNPC.playTradeSound();
        }
        return true;
    }

    /** onContainerClosed: the coins put in, and an exchange not yet taken, go back. */
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            clearContainer(player, coinInputInv);
            if (isExchanged()) {
                clearContainer(player, exchangeInv);
            }
        }
    }

    /** onCraftMatrixChanged. */
    @Override
    public void slotsChanged(Container inv) {
        if (inv == coinInputInv) {
            if (!isExchanged()) {
                ItemStack coin = coinInputInv.getItem(0);
                if (!coin.isEmpty() && isValidCoin(coin)) {
                    int coins = coin.getCount();
                    int coinType = LOTRCoins.coinType(coin);
                    if (coinType > 0) {
                        int coinsFloor = coins;
                        while (coinsFloor * 10 > STACK_LIMIT) {
                            --coinsFloor;
                        }
                        exchangeInv.setItem(0, new ItemStack(LOTRCoins.coin(coinType - 1), coinsFloor * 10));
                    } else {
                        exchangeInv.setItem(0, ItemStack.EMPTY);
                    }
                    if (coinType < LOTRCoins.TYPES - 1 && coins >= 10) {
                        exchangeInv.setItem(1, new ItemStack(LOTRCoins.coin(coinType + 1), coins / 10));
                    } else {
                        exchangeInv.setItem(1, ItemStack.EMPTY);
                    }
                } else {
                    exchangeInv.setItem(0, ItemStack.EMPTY);
                    exchangeInv.setItem(1, ItemStack.EMPTY);
                }
            }
        } else if (inv == exchangeInv && isExchanged() && exchangeInv.isEmpty()) {
            setExchanged(false);
            slotsChanged(coinInputInv);
        }
        super.slotsChanged(inv);
    }

    /** transferStackInSlot. */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < 3) {
            if (!moveItemStackTo(stack, 3, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean moved = slots.get(0).mayPlace(stack) && moveItemStackTo(stack, 0, 1, true);
            if (!moved && (index < 30 ? !moveItemStackTo(stack, 30, 39, false) : !moveItemStackTo(stack, 3, 30, false))) {
                return ItemStack.EMPTY;
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

    /** InventoryCoinExchangeSlot: every change re-reckons the exchange. */
    private class ExchangeContainer extends SimpleContainer {
        ExchangeContainer(int size) {
            super(size);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            slotsChanged(this);
        }
    }

    /** LOTRSlotCoinResult: nothing goes in; it can be taken once exchanged. */
    private class ResultSlot extends Slot {
        ResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return isExchanged();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
