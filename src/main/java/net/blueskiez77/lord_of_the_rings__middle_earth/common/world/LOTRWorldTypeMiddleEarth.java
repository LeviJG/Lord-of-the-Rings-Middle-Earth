package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldTypeMiddleEarth: the "Middle-earth" world type, chosen when the
 * world is made ({@code data/lotr/worldgen/world_preset/middle_earth.json},
 * shown with the normal types). The overworld is the usual one; a new player
 * is made in Middle-earth, by its spawn point (the original made them in the
 * overworld and sent them on as they logged in, once only, "TeleportedME").
 *
 * <p>A player coming to Middle-earth this way marks its Ring portal as made
 * (LOTRTeleporter, without the portal), so none is raised over the spawn point.
 */
public final class LOTRWorldTypeMiddleEarth {

    public static final AttachmentType<Boolean> TELEPORTED_ME = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "teleported_me"),
            builder -> builder.initializer(() -> false).persistent(Codec.BOOL).copyOnDeath());

    /** Players who joined to be sent on, waiting for their client to load the overworld. */
    private static final Set<UUID> AWAITING_LOAD = new HashSet<>();

    private LOTRWorldTypeMiddleEarth() {
    }

    public static void init() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "middle_earth_world_type"),
                LOTRWorldTypeBiomeSource.CODEC);
        // LOTREventHandler.onPlayerLogin -- but only once the client has loaded the overworld it
        // joined: moved during the login itself, the overworld's chunks and creatures still on their
        // way landed in the client's Middle-earth, as blocks and mobs only it could see.
        // A new player is made in Middle-earth already (newPlayerLevel); one who first joined in the
        // overworld (before the world type, or saved there) goes on once.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            if (!isMiddleEarthWorldType(server) || player.getAttachedOrCreate(TELEPORTED_ME)) {
                return;
            }
            if (player.level().dimension() == LOTRWorldGen.MIDDLE_EARTH) {
                player.setAttached(TELEPORTED_ME, true);
                LOTRTeleporter.arriveInMiddleEarth(player.level(), 0, 0, 0, false);
            } else if (player.level() == server.overworld()) {
                AWAITING_LOAD.add(player.getUUID());
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (AWAITING_LOAD.isEmpty()) {
                return;
            }
            Iterator<UUID> it = AWAITING_LOAD.iterator();
            while (it.hasNext()) {
                ServerPlayer player = server.getPlayerList().getPlayer(it.next());
                if (player == null) {
                    it.remove();
                } else if (player.connection.hasClientLoaded()) {
                    it.remove();
                    ServerLevel middleEarth = server.getLevel(LOTRWorldGen.MIDDLE_EARTH);
                    if (middleEarth != null && player.level() == server.overworld() && !player.getAttachedOrCreate(TELEPORTED_ME)) {
                        sendToMiddleEarth(player, middleEarth);
                        player.setAttached(TELEPORTED_ME, true);
                    }
                }
            }
        });
    }

    /** PrepareSpawnTask: the level a new player is made in -- Middle-earth, in a world of this type. */
    public static @Nullable ServerLevel newPlayerLevel(MinecraftServer server, boolean newPlayer) {
        return newPlayer && isMiddleEarthWorldType(server) ? server.getLevel(LOTRWorldGen.MIDDLE_EARTH) : null;
    }

    /** ...and where: a safe place by Middle-earth's spawn point, the Ring portal's place (0, 0 until it is made). */
    public static CompletableFuture<Vec3> newPlayerSpawn(ServerLevel middleEarth) {
        return PlayerSpawnFinder.findSpawn(middleEarth, new BlockPos(LOTRLevelData.middleEarthPortalX,
                LOTRLevelData.middleEarthPortalY, LOTRLevelData.middleEarthPortalZ));
    }

    /** Whether the world was made as of the Middle-earth type: its overworld carries the marker. */
    public static boolean isMiddleEarthWorldType(MinecraftServer server) {
        return server.overworld().getChunkSource().getGenerator().getBiomeSource() instanceof LOTRWorldTypeBiomeSource;
    }

    /** new LOTRTeleporter(middleEarth, false): to (0, 0), the Ring portal marked made but not raised. */
    private static void sendToMiddleEarth(ServerPlayer player, ServerLevel middleEarth) {
        player.teleport(LOTRTeleporter.transition(middleEarth, player, false));
    }
}
