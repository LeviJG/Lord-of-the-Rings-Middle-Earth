package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import com.mojang.authlib.GameProfile;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapPlayerData;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;

/**
 * The client's side of the map: the other players it shows (LOTRGuiMap.playerLocations, replaced
 * whole by each LOTRPacketUpdatePlayerLocations), their skins, and the server's
 * commemorateEmpressShamiir.
 */
public final class LOTRClientMap {

    public record PlayerLocationInfo(GameProfile profile, double posX, double posZ) {
    }

    /** LOTRGuiMap.showWP, showCWP, showHiddenSWP: the map's toggles, read from the player's map options once a connection. */
    public static boolean showWP = true;
    public static boolean showCWP = true;
    public static boolean showHiddenSWP;
    private static boolean togglesLoaded;
    /** LOTRGuiMap.isPlayerOp: the server's last answer to the map's question. */
    private static boolean isPlayerOp;
    private static long lastOpRequestTick = -1L;

    private static final Map<UUID, PlayerLocationInfo> PLAYER_LOCATIONS = new HashMap<>();
    private static final Map<UUID, Supplier<PlayerSkin>> SKINS = new HashMap<>();

    private LOTRClientMap() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRMapPlayerData.LocationsPayload.TYPE, (payload, context) -> {
            PLAYER_LOCATIONS.clear();
            for (LOTRMapPlayerData.PlayerLocation location : payload.locations()) {
                PLAYER_LOCATIONS.put(location.profile().id(), new PlayerLocationInfo(location.profile(), location.posX(), location.posZ()));
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRMapPlayerData.IsOpResponsePayload.TYPE,
                (payload, context) -> isPlayerOp = payload.isOp());
        ClientPlayNetworking.registerGlobalReceiver(net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen.ClassicPayload.TYPE,
                (payload, context) -> net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen.classic = payload.classic());
        ClientPlayNetworking.registerGlobalReceiver(LOTRLevelData.ShamiirPayload.TYPE,
                (payload, context) -> LOTRLevelData.clientside_thisServer_commemorateEmpressShamiir = payload.commemorate());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PLAYER_LOCATIONS.clear();
            SKINS.clear();
            LOTRLevelData.clientside_thisServer_commemorateEmpressShamiir = true;
            togglesLoaded = false;
            isPlayerOp = false;
            lastOpRequestTick = -1L;
        });
    }

    /** setWaypointModes: the toggles as the server kept them, taken when the map first opens. */
    public static void loadToggles(net.minecraft.world.entity.player.@org.jspecify.annotations.Nullable Player player) {
        if (togglesLoaded || player == null) {
            return;
        }
        LOTRMapPlayerData.Options options = LOTRMapPlayerData.get(player);
        showWP = options.showWP();
        showCWP = options.showCWP();
        showHiddenSWP = options.showHiddenSWP();
        togglesLoaded = true;
    }

    /**
     * requestIsOp, then isPlayerOp: asks the server whether the player is an operator and gives its last
     * answer. The original asked every frame it drew the teleport line; here at most once a tick.
     */
    public static boolean requestIsOp() {
        Minecraft mc = Minecraft.getInstance();
        long tick = mc.level == null ? -1L : mc.level.getGameTime();
        if (tick != lastOpRequestTick && ClientPlayNetworking.canSend(LOTRMapPlayerData.IsOpRequestPayload.TYPE)) {
            lastOpRequestTick = tick;
            ClientPlayNetworking.send(new LOTRMapPlayerData.IsOpRequestPayload());
        }
        return isPlayerOp;
    }

    public static Map<UUID, PlayerLocationInfo> getPlayerLocations() {
        return PLAYER_LOCATIONS;
    }

    /** A player's skin: the tab list's, else looked up once and kept. */
    public static PlayerSkin getSkin(GameProfile profile) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null) {
            PlayerInfo info = mc.getConnection().getPlayerInfo(profile.id());
            if (info != null) {
                return info.getSkin();
            }
        }
        return SKINS.computeIfAbsent(profile.id(), id -> mc.getSkinManager().createLookup(profile, false)).get();
    }
}
