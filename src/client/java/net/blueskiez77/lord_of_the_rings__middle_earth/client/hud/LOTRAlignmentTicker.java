package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import net.minecraft.world.entity.player.Player;

/**
 * LOTRAlignmentTicker: per faction, the alignment the bar shows sliding to a
 * new value over a second, the ring flashing, and the number shown for ten
 * seconds after a change.
 */
public class LOTRAlignmentTicker {

    private static final Map<LOTRFaction, LOTRAlignmentTicker> ALL_FACTION_TICKERS = new EnumMap<>(LOTRFaction.class);

    public final LOTRFaction theFac;
    public float oldAlign;
    public float newAlign;
    public int moveTick;
    public int prevMoveTick;
    public int flashTick;
    public int numericalTick;

    private LOTRAlignmentTicker(LOTRFaction faction) {
        this.theFac = faction;
    }

    public static LOTRAlignmentTicker forFaction(LOTRFaction fac) {
        return ALL_FACTION_TICKERS.computeIfAbsent(fac, LOTRAlignmentTicker::new);
    }

    public static void updateAll(Player player, boolean forceInstant) {
        for (LOTRDimension dim : LOTRDimension.values()) {
            for (LOTRFaction fac : dim.factionList) {
                forFaction(fac).update(player, forceInstant);
            }
        }
    }

    /** Every client tick; at once on joining a world. */
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !client.isPaused()) {
                updateAll(client.player, false);
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> client.execute(() -> {
            if (client.player != null) {
                updateAll(client.player, true);
            }
        }));
    }

    public float getInterpolatedAlignment(float f) {
        if (this.moveTick == 0) {
            return this.oldAlign;
        }
        float tickF = this.prevMoveTick + (this.moveTick - this.prevMoveTick) * f;
        tickF /= 20.0f;
        tickF = 1.0f - tickF;
        return this.oldAlign + (this.newAlign - this.oldAlign) * tickF;
    }

    public void update(Player player, boolean forceInstant) {
        float curAlign = LOTRPlayerAlignments.getAlignment(player, this.theFac);
        if (forceInstant) {
            this.oldAlign = this.newAlign = curAlign;
            this.moveTick = 0;
            this.prevMoveTick = 0;
            this.flashTick = 0;
            this.numericalTick = 0;
            return;
        }
        if (this.newAlign != curAlign) {
            this.oldAlign = this.newAlign;
            this.newAlign = curAlign;
            this.moveTick = 20;
            this.flashTick = 30;
            this.numericalTick = 200;
        }
        this.prevMoveTick = this.moveTick;
        if (this.moveTick > 0) {
            --this.moveTick;
            if (this.moveTick <= 0) {
                this.oldAlign = this.newAlign;
            }
        }
        if (this.flashTick > 0) {
            --this.flashTick;
        }
        if (this.numericalTick > 0) {
            --this.numericalTick;
        }
    }
}
