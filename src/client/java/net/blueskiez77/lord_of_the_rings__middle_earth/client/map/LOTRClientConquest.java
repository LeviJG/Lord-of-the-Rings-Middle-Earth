package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMapScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestGrid;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestZone;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * The client's side of the conquest map: the Table of Command opening it (gui 60), and each
 * faction's grid (LOTRClientProxy.receiveConquestGrid) handed to the map screen open.
 */
public final class LOTRClientConquest {

    private LOTRClientConquest() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRConquestGrid.OpenMapPayload.TYPE, (payload, context) ->
                context.client().setScreenAndShow(new LOTRMapScreen().setConquestGrid()));
        ClientPlayNetworking.registerGlobalReceiver(LOTRConquestGrid.GridPayload.TYPE, (payload, context) -> {
            if (payload.faction() == null || !(context.client().gui.screen() instanceof LOTRMapScreen map)) {
                return;
            }
            List<LOTRConquestZone> zones = new ArrayList<>();
            for (LOTRConquestGrid.GridZone zone : payload.zones()) {
                zones.add(zone.toClientZone(payload.faction()));
            }
            map.receiveConquestGrid(payload.faction(), zones);
        });
    }
}
