package net.blueskiez77.lord_of_the_rings__middle_earth.common.config;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * The server's options a client must know, sent on login -- the config half
 * of LOTRLevelData.sendLoginPacket (LOTRPacketLogin's feastMode,
 * fellowshipCreation, fellowshipMaxSize, enchanting, enchantingLOTR,
 * strictFactionTitleRequirements and customWaypointMinY). The client keeps
 * them in LOTRConfig's {@code clientside_thisServer_} fields.
 */
public record LOTRConfigSync(boolean feastMode, boolean fellowshipCreation, int fellowshipMaxSize,
                             boolean enchanting, boolean enchantingLOTR, boolean strictFactionTitleRequirements,
                             int customWaypointMinY) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRConfigSync> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "config_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRConfigSync> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeBoolean(p.feastMode);
                buf.writeBoolean(p.fellowshipCreation);
                buf.writeInt(p.fellowshipMaxSize);
                buf.writeBoolean(p.enchanting);
                buf.writeBoolean(p.enchantingLOTR);
                buf.writeBoolean(p.strictFactionTitleRequirements);
                buf.writeInt(p.customWaypointMinY);
            }, buf -> new LOTRConfigSync(buf.readBoolean(), buf.readBoolean(), buf.readInt(), buf.readBoolean(),
                    buf.readBoolean(), buf.readBoolean(), buf.readInt()));

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(TYPE, STREAM_CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerPlayNetworking.send(handler.player, new LOTRConfigSync(LOTRConfig.canAlwaysEat,
                        LOTRConfig.enableFellowshipCreation, LOTRConfig.fellowshipMaxSize, LOTRConfig.enchantingVanilla,
                        LOTRConfig.enchantingLOTR, LOTRConfig.strictFactionTitleRequirements,
                        LOTRConfig.customWaypointMinY)));
    }

    /** On the client: the server's values, for LOTRConfig's side-aware accessors. */
    public void apply() {
        LOTRConfig.clientside_thisServer_feastMode = this.feastMode;
        LOTRConfig.clientside_thisServer_fellowshipCreation = this.fellowshipCreation;
        LOTRConfig.clientside_thisServer_fellowshipMaxSize = this.fellowshipMaxSize;
        LOTRConfig.clientside_thisServer_enchanting = this.enchanting;
        LOTRConfig.clientside_thisServer_enchantingLOTR = this.enchantingLOTR;
        LOTRConfig.clientside_thisServer_strictFactionTitleRequirements = this.strictFactionTitleRequirements;
        LOTRConfig.clientside_thisServer_customWaypointMinY = this.customWaypointMinY;
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
