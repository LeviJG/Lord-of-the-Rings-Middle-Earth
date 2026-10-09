package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRPlayerShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * LOTRPacketSelectTitle and LOTRPacketSelectShield: the Titles and Shields screens' choices, which
 * the server takes only if the player may bear what they chose (or chose none).
 */
public final class LOTRTitleShieldNetworking {

    private LOTRTitleShieldNetworking() {
    }

    /** The title by name and its colour's number; no name is "Remove Title". */
    public record SelectTitle(Optional<String> title, int color) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SelectTitle> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "select_title"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SelectTitle> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), SelectTitle::title,
                ByteBufCodecs.VAR_INT, SelectTitle::color, SelectTitle::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** The shield by name; none is "Remove Shield". */
    public record SelectShield(Optional<String> shield) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SelectShield> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "select_shield"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SelectShield> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), SelectShield::shield, SelectShield::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        LOTRPlayerTitles.init();
        LOTRPlayerShields.init();
        PayloadTypeRegistry.serverboundPlay().register(SelectTitle.TYPE, SelectTitle.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SelectShield.TYPE, SelectShield.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SelectTitle.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (payload.title().isEmpty()) {
                LOTRPlayerTitles.setPlayerTitle(player, null);
                return;
            }
            LOTRTitle title = LOTRTitle.forName(payload.title().get());
            if (title != null && title.canPlayerUse(player)) {
                LOTRPlayerTitles.setPlayerTitle(player,
                        new LOTRTitle.PlayerTitle(title, LOTRTitle.PlayerTitle.colorForID(payload.color())));
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SelectShield.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRShields shield = payload.shield().map(LOTRShields::shieldForName).orElse(null);
            if (payload.shield().isPresent() && shield == null) {
                return;
            }
            if (shield == null || shield.canPlayerWear(player)) {
                LOTRPlayerShields.setShield(player, shield);
            } else {
                LOTRMod.LOGGER.error("Failed to update LOTR shield on server side: Player {} cannot wear shield {}",
                        player.getName().getString(), shield.name());
            }
        });
    }
}
