package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRFastTravelScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRAbstractWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFastTravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.world.entity.player.Player;

/**
 * The client's fast travel: its clock as the server last sent it (LOTRPacketFTTimer), the waypoints'
 * waits (LOTRPacketFTCooldown), the answer that lets the server move it (LOTRPacketFTBounceClient), and
 * the journey's screen once it has (LOTRPacketFTScreen).
 */
public final class LOTRClientFastTravel {

    private static int timeSinceFT;
    private static int cooldownMax = 1800;
    private static int cooldownMin = 180;

    private LOTRClientFastTravel() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRFastTravel.TimerPayload.TYPE, (payload, context) -> timeSinceFT = payload.timer());
        ClientPlayNetworking.registerGlobalReceiver(LOTRFastTravel.CooldownPayload.TYPE, (payload, context) -> {
            cooldownMax = payload.max();
            cooldownMin = payload.min();
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRFastTravel.BounceClientPayload.TYPE,
                (payload, context) -> ClientPlayNetworking.send(new LOTRFastTravel.BounceServerPayload()));
        ClientPlayNetworking.registerGlobalReceiver(LOTRFastTravel.ScreenPayload.TYPE, (payload, context) -> {
            LOTRAbstractWaypoint waypoint = null;
            int wpID = payload.wpID();
            if (payload.isCustom()) {
                waypoint = payload.sharingPlayer().isPresent()
                        ? LOTRClientCustomWaypoints.getSharedCustomWaypointByID(payload.sharingPlayer().get(), wpID)
                        : LOTRClientCustomWaypoints.getCustomWaypointByID(wpID);
            } else if (wpID >= 0 && wpID < LOTRWaypoint.values().length) {
                waypoint = LOTRWaypoint.values()[wpID];
            }
            if (waypoint != null) {
                context.client().setScreenAndShow(new LOTRFastTravelScreen(waypoint, payload.startX(), payload.startZ()));
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> timeSinceFT = 0);
    }

    public static int getTimeSinceFT() {
        return timeSinceFT;
    }

    /** getWaypointFTTime, with the waits the server sent. */
    public static int getWaypointFTTime(LOTRAbstractWaypoint wp, Player player) {
        return LOTRFastTravel.getWaypointFTTime(wp, player, cooldownMax, cooldownMin);
    }
}
