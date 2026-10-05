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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerUnitTrade: the hire screen's container, which holds nothing
 * but the player's inventory. A unit is hired with LOTRPacketBuyUnit.
 *
 * <p>NOT ported yet: the alignment reward slot, from which a captain sells
 * his warhorn at +1500 (LOTRSlotAlignmentReward, getWarhorn), with
 * invasions (D12).
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

    /** transferStackInSlot: between the main inventory and the hotbar. */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < 27 ? !moveItemStackTo(stack, 27, 36, false) : !moveItemStackTo(stack, 0, 27, false)) {
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
