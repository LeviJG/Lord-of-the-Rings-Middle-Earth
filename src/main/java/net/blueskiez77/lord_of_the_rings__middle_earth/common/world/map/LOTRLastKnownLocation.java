package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.Optional;
import java.util.UUID;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;

import org.jspecify.annotations.Nullable;

/**
 * Where a player was last known to be, for the NPCs who help with a bounty on them: the Middle-earth
 * biome they were last in (LOTRPlayerData.lastBiome, "LastBiome", looked at every five seconds there)
 * and the waypoint they last travelled to (LOTRPlayerWaypoints' "LastWP"). A bounty's target is often
 * away, so for an absent player both are read from their player file.
 */
public final class LOTRLastKnownLocation {

    /** The biome id, saved as the original's short. */
    public static final AttachmentType<Short> LAST_BIOME = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "last_biome"),
            builder -> builder.persistent(Codec.SHORT.fieldOf("LastBiome").codec()).copyOnDeath());

    private LOTRLastKnownLocation() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerLevel level = server.getLevel(LOTRWorldGen.MIDDLE_EARTH);
            if (level == null || server.getTickCount() % 100 != 0) {
                return;
            }
            for (ServerPlayer player : level.players()) {
                LOTRBiome biome = LOTRNPCEntity.biomeAt(level, player.blockPosition());
                if (biome != null && biome.biomeDimension == LOTRDimension.MIDDLE_EARTH) {
                    player.setAttached(LAST_BIOME, (short) biome.biomeID);
                }
            }
        });
    }

    /** getLastKnownBiome, of an online or absent player. */
    public static @Nullable LOTRBiome getLastKnownBiome(MinecraftServer server, UUID playerID) {
        ServerPlayer online = server.getPlayerList().getPlayer(playerID);
        Short id = online != null ? online.getAttached(LAST_BIOME) : saved(server, playerID, LAST_BIOME, Codec.SHORT.fieldOf("LastBiome").codec());
        LOTRBiome[] biomeList = LOTRDimension.MIDDLE_EARTH.biomeList;
        return id != null && id >= 0 && id < biomeList.length ? biomeList[id] : null;
    }

    /** getLastKnownWaypoint, of an online or absent player. */
    public static @Nullable LOTRWaypoint getLastKnownWaypoint(MinecraftServer server, UUID playerID) {
        ServerPlayer online = server.getPlayerList().getPlayer(playerID);
        if (online != null) {
            return LOTRPlayerWaypoints.getLastKnownWaypoint(online);
        }
        LOTRPlayerWaypoints saved = saved(server, playerID, LOTRPlayerWaypoints.WAYPOINTS, LOTRPlayerWaypoints.CODEC);
        return saved == null ? null : saved.lastWaypoint().orElse(null);
    }

    /** An attachment as saved in an absent player's file. */
    private static <T> @Nullable T saved(MinecraftServer server, UUID playerID, AttachmentType<T> type, Codec<T> codec) {
        return server.playerDataStorage.load(new NameAndId(playerID, ""))
                .flatMap(tag -> tag.getCompound(AttachmentTarget.NBT_ATTACHMENT_KEY))
                .flatMap(attachments -> Optional.ofNullable(attachments.get(type.identifier().toString())))
                .flatMap(tag -> codec.parse(RegistryOps.create(NbtOps.INSTANCE, server.registryAccess()), tag).result())
                .orElse(null);
    }
}
