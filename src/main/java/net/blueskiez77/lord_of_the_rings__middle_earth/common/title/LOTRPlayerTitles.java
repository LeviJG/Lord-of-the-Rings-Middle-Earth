package net.blueskiez77.lord_of_the_rings__middle_earth.common.title;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's PlayerTitle and PlayerTitleColor: the title a player bears, sent to them for
 * their Titles screen. It goes before their name in chat (LOTREventHandler.onServerChat, with
 * "Enable Titles") and beside it in their fellows' fellowships screen; one they can no longer
 * bear is taken from them, with a message.
 */
public final class LOTRPlayerTitles {

    private static final Codec<LOTRTitle> TITLE_CODEC = Codec.STRING.comapFlatMap(name -> {
        LOTRTitle title = LOTRTitle.forName(name);
        return title != null ? DataResult.success(title) : DataResult.error(() -> "Unknown LOTR title: " + name);
    }, LOTRTitle::getTitleName);

    public static final Codec<LOTRTitle.PlayerTitle> CODEC = RecordCodecBuilder.create(i -> i.group(
            TITLE_CODEC.fieldOf("PlayerTitle").forGetter(LOTRTitle.PlayerTitle::title),
            Codec.INT.optionalFieldOf("PlayerTitleColor", 15).forGetter(LOTRTitle.PlayerTitle::colorID)
    ).apply(i, (title, color) -> new LOTRTitle.PlayerTitle(title, LOTRTitle.PlayerTitle.colorForID(color))));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRTitle.PlayerTitle> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(LOTRTitle::forName, LOTRTitle::getTitleName), LOTRTitle.PlayerTitle::title,
            ByteBufCodecs.VAR_INT, LOTRTitle.PlayerTitle::colorID,
            (title, color) -> new LOTRTitle.PlayerTitle(title, LOTRTitle.PlayerTitle.colorForID(color)));

    public static final AttachmentType<LOTRTitle.PlayerTitle> PLAYER_TITLE = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "player_title"),
            builder -> builder.persistent(CODEC).copyOnDeath()
                    .syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    private LOTRPlayerTitles() {
    }

    public static LOTRTitle.@Nullable PlayerTitle getPlayerTitle(Player player) {
        return player.getAttached(PLAYER_TITLE);
    }

    /** setPlayerTitle: the player's fellows see the new title too. */
    public static void setPlayerTitle(Player player, LOTRTitle.@Nullable PlayerTitle title) {
        player.setAttached(PLAYER_TITLE, title);
        if (player instanceof ServerPlayer serverPlayer) {
            net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships.onTitleChanged(serverPlayer);
        }
    }

    /** useFeminineRanks' second half: bearing the feminine form of a rank's title. */
    public static boolean hasFeminineRankTitle(Player player) {
        LOTRTitle.PlayerTitle title = getPlayerTitle(player);
        return title != null && title.title().isFeminineRank();
    }

    /** onServerChat: "[Title] " before the speaker's name. */
    public static ChatType.Bound withTitle(ChatType.Bound bound, ServerPlayer player) {
        LOTRTitle.PlayerTitle title = getPlayerTitle(player);
        if (!LOTRConfig.enableTitles || title == null) {
            return bound;
        }
        Component name = Component.empty().append(title.getFullTitleComponent(player)).append(bound.name());
        return new ChatType.Bound(bound.chatType(), name, bound.targetName());
    }

    /** onUpdate's handlePlayerTitle: a title the player no longer meets the terms of is taken from them. */
    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                LOTRTitle.PlayerTitle title = getPlayerTitle(player);
                if (title != null && !title.title().canPlayerUse(player)) {
                    player.sendSystemMessage(Component.translatable("chat.lotr.loseTitle",
                            title.getFullTitleComponent(player)));
                    setPlayerTitle(player, null);
                }
            }
        });
    }
}
