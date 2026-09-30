package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTravellingTrader: a trader who comes visiting a player with an escort,
 * stays a day, warns two minutes before leaving, and goes -- escort and all
 * (LOTRTravellingTraderInfo).
 */
public interface LOTRTravellingTrader extends LOTRTradeable {

    /** A fresh escort for the visit, not yet placed or spawned. */
    @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level);

    /** The speech bank for the warning that it will soon leave. */
    String getDepartureSpeech();
}
