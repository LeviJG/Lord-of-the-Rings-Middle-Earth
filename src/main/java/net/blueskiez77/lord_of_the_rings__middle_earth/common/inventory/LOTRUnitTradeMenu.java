package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHireableBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenary;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenaryTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerUnitTrade: the hire screen's container -- the player's
 * inventory, and for a hirer with a warhorn, the alignment reward slot that
 * sells it at +1500. A unit is hired with LOTRPacketBuyUnit.
 */
public class LOTRUnitTradeMenu extends AbstractContainerMenu {

    /**
     * The opening data: the hirer, and each unit and its mount as the server
     * made them for show (see {@link LOTRUnitTradeEntry#createDisplayData}).
     */
    public record OpeningData(int entityId, List<Optional<CompoundTag>> units, List<Optional<CompoundTag>> mounts) {
        public static final StreamCodec<RegistryFriendlyByteBuf, OpeningData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OpeningData::entityId,
                ByteBufCodecs.optional(ByteBufCodecs.COMPOUND_TAG).apply(ByteBufCodecs.list()), OpeningData::units,
                ByteBufCodecs.optional(ByteBufCodecs.COMPOUND_TAG).apply(ByteBufCodecs.list()), OpeningData::mounts,
                OpeningData::new);

        public static OpeningData create(LOTRNPCEntity trader, ServerLevel level) {
            List<Optional<CompoundTag>> units = new ArrayList<>();
            List<Optional<CompoundTag>> mounts = new ArrayList<>();
            for (LOTRUnitTradeEntry trade : tradesFor(trader)) {
                units.add(Optional.ofNullable(trade.createDisplayData(level, false)));
                mounts.add(Optional.ofNullable(trade.hasMount() ? trade.createDisplayData(level, true) : null));
            }
            return new OpeningData(trader.getId(), units, mounts);
        }
    }

    public final @Nullable LOTRNPCEntity theLivingTrader;
    public final List<LOTRUnitTradeEntry> trades;
    /** The client's: what the screen shows of each unit. */
    public final @Nullable OpeningData displayData;
    public final SimpleContainer alignmentRewardInv;
    public final int alignmentRewardSlots;

    public LOTRUnitTradeMenu(int containerId, Inventory inventory, OpeningData data) {
        this(containerId, inventory,
                inventory.player.level().getEntity(data.entityId()) instanceof LOTRNPCEntity npc ? npc : null, data);
    }

    public LOTRUnitTradeMenu(int containerId, Inventory inventory, LOTRNPCEntity trader) {
        this(containerId, inventory, trader, null);
    }

    private LOTRUnitTradeMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity trader,
            @Nullable OpeningData data) {
        super(LOTRMenus.UNIT_TRADE, containerId);
        this.theLivingTrader = trader;
        this.trades = trader == null ? List.of() : tradesFor(trader);
        this.displayData = data;
        ItemStack reward = ItemStack.EMPTY;
        if (trader instanceof LOTRUnitTradeable unitTrader) {
            LOTRInvasions conquestType = unitTrader.getWarhorn();
            reward = conquestType == null ? ItemStack.EMPTY : conquestType.createConquestHorn();
        }
        boolean hasReward = !reward.isEmpty();
        this.alignmentRewardSlots = hasReward ? 1 : 0;
        this.alignmentRewardInv = new SimpleContainer(this.alignmentRewardSlots);
        if (hasReward) {
            addSlot(new AlignmentRewardSlot(this.alignmentRewardInv, 0, 174, 78, trader, reward.copy()));
            if (!inventory.player.level().isClientSide()
                    && LOTRPlayerAlignments.getAlignment(inventory.player, trader.getFaction()) >= 1500.0f) {
                this.alignmentRewardInv.setItem(0, reward.copy());
            }
        }
        addStandardInventorySlots(inventory, 30, 174);
    }

    /** The units on offer: a hirer's list, or the mercenary himself (LOTRGuiMercenaryHire). */
    public static List<LOTRUnitTradeEntry> tradesFor(LOTRNPCEntity trader) {
        if (trader instanceof LOTRUnitTradeable unitTrader) {
            return unitTrader.getUnits().tradeEntries;
        }
        if (trader instanceof LOTRMercenary merc) {
            return List.of(LOTRMercenaryTradeEntry.createFor(merc));
        }
        return List.of();
    }

    public static boolean isHiringFrom(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRUnitTradeMenu menu && menu.theLivingTrader == npc;
    }

    public @Nullable LOTRHireableBase unitTrader() {
        return this.theLivingTrader instanceof LOTRHireableBase hirer ? hirer : null;
    }

    @Override
    public boolean stillValid(Player player) {
        LOTRNPCEntity npc = this.theLivingTrader;
        return npc != null && player.distanceTo(npc) <= 12.0 && npc.isAlive() && npc.getTarget() == null
                && ((LOTRHireableBase) npc).canTradeWith(player);
    }

    /**
     * transferStackInSlot: the reward into the inventory, and between the
     * main inventory and the hotbar. The original's shift-click was retried
     * only while the slot could still be taken from, so as many horns are
     * bought as can be paid for; vanilla's retry does not ask, so this does.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        int r = this.alignmentRewardSlots;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < r ? !moveItemStackTo(stack, r, 36 + r, true)
                : index < 27 + r ? !moveItemStackTo(stack, 27 + r, 36 + r, false)
                : !moveItemStackTo(stack, r, 27 + r, false)) {
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

    /**
     * LOTRSlotAlignmentReward: the hirer's warhorn, for those of +1500 with
     * its faction who can pay 2000 coins; taken, it is there again.
     */
    public static class AlignmentRewardSlot extends Slot {
        public static final int REWARD_COST = 2000;
        private final LOTRNPCEntity theLivingTrader;
        private final ItemStack alignmentReward;

        public AlignmentRewardSlot(Container inv, int i, int x, int y, LOTRNPCEntity trader, ItemStack item) {
            super(inv, i, x, y);
            this.theLivingTrader = trader;
            this.alignmentReward = item.copy();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            if (LOTRPlayerAlignments.getAlignment(player, this.theLivingTrader.getFaction()) < 1500.0f) {
                return false;
            }
            if (LOTRCoins.getInventoryValue(player) < REWARD_COST) {
                return false;
            }
            return super.mayPickup(player);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            if (!player.level().isClientSide()) {
                LOTRCoins.takeCoins(REWARD_COST, player);
                LOTRFactionData.takeConquestHorn(player, this.theLivingTrader.getFaction());
                this.theLivingTrader.playTradeSound();
            }
            super.onTake(player, stack);
            if (!player.level().isClientSide()) {
                set(this.alignmentReward.copy());
            }
        }
    }
}
