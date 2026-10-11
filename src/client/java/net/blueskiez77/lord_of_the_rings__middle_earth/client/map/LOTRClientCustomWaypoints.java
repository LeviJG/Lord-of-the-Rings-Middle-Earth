package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRCustomWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRCustomWaypoints;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * The client's custom waypoints, as the server last sent them: the player's own, those shared with
 * them, the server's lowest height for one (clientside_thisServer_customWaypointMinY), and why the
 * land last refused one, for the map to say (LOTRPacketCWPProtectionMessage).
 */
public final class LOTRClientCustomWaypoints {

    private static List<LOTRCustomWaypoint> own = List.of();
    private static List<LOTRCustomWaypoint> shared = List.of();
    private static int minY = -1;
    private static @Nullable Component protectionMessage;

    private LOTRClientCustomWaypoints() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRCustomWaypoints.SyncPayload.TYPE, (payload, context) -> {
            own = List.copyOf(payload.own());
            shared = List.copyOf(payload.shared());
            minY = payload.minY();
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRCustomWaypoints.ProtectionPayload.TYPE,
                (payload, context) -> protectionMessage = payload.message());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            own = List.of();
            shared = List.of();
            minY = -1;
            protectionMessage = null;
        });
    }

    public static List<LOTRCustomWaypoint> getCustomWaypoints() {
        return own;
    }

    public static List<LOTRCustomWaypoint> getSharedCustomWaypoints() {
        return shared;
    }

    public static @Nullable LOTRCustomWaypoint getCustomWaypointByID(int id) {
        for (LOTRCustomWaypoint cwp : own) {
            if (cwp.getID() == id) {
                return cwp;
            }
        }
        return null;
    }

    public static @Nullable LOTRCustomWaypoint getSharedCustomWaypointByID(UUID sharer, int id) {
        for (LOTRCustomWaypoint cwp : shared) {
            if (sharer.equals(cwp.getSharingPlayerID()) && cwp.getID() == id) {
                return cwp;
            }
        }
        return null;
    }

    public static int getCustomWaypointMinY() {
        return minY;
    }

    /** The land's last refusal, taken once. */
    public static @Nullable Component takeProtectionMessage() {
        Component message = protectionMessage;
        protectionMessage = null;
        return message;
    }
}
