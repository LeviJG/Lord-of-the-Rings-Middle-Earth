package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRelations;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentZonesPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBrokenPledgePayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRFactionRelationsPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * The faction state the server sends: the broken-pledge cooldown
 * (LOTRPacketBrokenPledge), control zones on or off, and overridden relations,
 * the last two into the same statics the server keeps them in.
 */
public final class LOTRClientFactionState {

    private static LOTRBrokenPledgePayload state = new LOTRBrokenPledgePayload(0, 0, null);

    private LOTRClientFactionState() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRBrokenPledgePayload.TYPE,
                (payload, context) -> context.client().execute(() -> state = payload));
        ClientPlayNetworking.registerGlobalReceiver(LOTRAlignmentZonesPayload.TYPE,
                (payload, context) -> context.client().execute(() -> LOTRLevelData.enableAlignmentZones = payload.enabled()));
        ClientPlayNetworking.registerGlobalReceiver(LOTRFactionRelationsPayload.TYPE,
                (payload, context) -> context.client().execute(() -> {
                    LOTRFactionRelations.overrideMap.clear();
                    LOTRFactionRelations.overrideMap.putAll(payload.overrides());
                }));
    }

    public static LOTRBrokenPledgePayload get() {
        return state;
    }

    public static boolean canMakeNewPledge() {
        return state.cooldown() <= 0;
    }
}
