package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * The trading packets. The sell button and the coin exchange's two buttons
 * travel as vanilla's container-button packet instead of LOTRPacketSell and
 * LOTRPacketCoinExchange.
 */
public final class LOTRTradePayloads {

    private LOTRTradePayloads() {
    }

    /** LOTRPacketTraderInfo: the trader's trades, for the trade screen. */
    public record TraderInfo(int entityId, CompoundTag data) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TraderInfo> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "trader_info"));

        public static final StreamCodec<RegistryFriendlyByteBuf, TraderInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, TraderInfo::entityId, ByteBufCodecs.COMPOUND_TAG, TraderInfo::data,
                TraderInfo::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /**
     * openGui 19, 20, 24 and 58: the server opens a talk-or-trade choice.
     * Those guis had no container, so the client is told which to show.
     */
    public record OpenInteract(int entityId, int kind) implements CustomPacketPayload {
        /** 19: Talk, Trade, Exchange Coins (and Smith). */
        public static final int TRADE = 0;
        /** 20: Talk, Hire. */
        public static final int UNIT_TRADE = 1;
        /** 24: the trader's buttons and Hire beneath. */
        public static final int TRADE_UNIT_TRADE = 2;
        /** 58: a mercenary's Talk, Hire. */
        public static final int MERCENARY = 3;
        /** 21: a hired unit's Talk, Command, Dismiss. */
        public static final int HIRED = 4;

        public static final CustomPacketPayload.Type<OpenInteract> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "trader_open_interact"));

        public static final StreamCodec<RegistryFriendlyByteBuf, OpenInteract> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OpenInteract::entityId, ByteBufCodecs.VAR_INT, OpenInteract::kind,
                OpenInteract::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /**
     * LOTRPacketUnitTraderInteract and LOTRPacketMercenaryInteract: 0 talk,
     * 1 hire (the server tells a hirer from a mercenary).
     */
    public record UnitInteract(int entityId, int action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<UnitInteract> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "unit_trader_interact"));

        public static final StreamCodec<RegistryFriendlyByteBuf, UnitInteract> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, UnitInteract::entityId, ByteBufCodecs.BYTE.map(Byte::intValue, Integer::byteValue),
                UnitInteract::action, UnitInteract::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketBuyUnit: which unit, and the company it is to join (empty for none). */
    public record BuyUnit(int tradeIndex, String squadron) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<BuyUnit> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "buy_unit"));

        public static final StreamCodec<RegistryFriendlyByteBuf, BuyUnit> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, BuyUnit::tradeIndex, ByteBufCodecs.STRING_UTF8, BuyUnit::squadron,
                BuyUnit::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketTraderInteract: 0 talk, 1 trade, 2 exchange coins, 3 smith. */
    public record Interact(int entityId, int action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Interact> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "trader_interact"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Interact> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Interact::entityId, ByteBufCodecs.BYTE.map(Byte::intValue, Integer::byteValue),
                Interact::action, Interact::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
