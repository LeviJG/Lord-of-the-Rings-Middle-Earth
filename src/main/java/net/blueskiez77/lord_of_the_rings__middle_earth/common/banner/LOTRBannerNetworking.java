package net.blueskiez77.lord_of_the_rings__middle_earth.common.banner;

import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerDataPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerEditPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerNamePayloads;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * The server's side of the banner screen: LOTRPacketEditBanner's handler (the
 * owner's changes, taken only from someone who may edit the banner) and
 * LOTRPacketBannerRequestInvalidName's (is this a player the server knows?).
 * A fellowship name is never valid until fellowships are ported (D14).
 */
public final class LOTRBannerNetworking {

    private LOTRBannerNetworking() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(LOTRBannerDataPayload.TYPE, LOTRBannerDataPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRBannerNamePayloads.Validate.TYPE,
                LOTRBannerNamePayloads.Validate.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRBannerEditPayload.TYPE, LOTRBannerEditPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRBannerNamePayloads.Request.TYPE,
                LOTRBannerNamePayloads.Request.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LOTRBannerEditPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRBannerBlockEntity banner = bannerAt(player, payload.pos());
            if (banner == null || !banner.canPlayerEditBanner(player)) {
                return;
            }
            banner.setPlayerSpecificProtection(payload.playerSpecificProtection());
            banner.setSelfProtection(payload.selfProtection());
            banner.setAlignmentProtection(payload.alignmentProtection());
            banner.resizeWhitelist(payload.whitelistLength());
            // Each slot as sent, blank ones cleared; a name the server cannot find leaves the slot as it was.
            payload.whitelist().ifPresent(slots -> {
                for (LOTRBannerDataPayload.Slot slot : slots) {
                    if (slot.index() == 0) {
                        continue;
                    }
                    if (slot.name().isBlank()) {
                        banner.whitelistPlayer(slot.index(), null);
                        continue;
                    }
                    if (LOTRBannerWhitelistEntry.hasFellowshipCode(slot.name())) {
                        continue;
                    }
                    Optional<NameAndId> profile = player.level().getServer().services().nameToIdCache().get(slot.name());
                    profile.ifPresent(p -> banner.whitelistPlayer(slot.index(), LOTRBannerWhitelistEntry.player(p),
                            LOTRBannerWhitelistEntry.decodePermBitFlags(slot.perms())));
                }
            });
            banner.setDefaultPermissions(LOTRBannerWhitelistEntry.decodePermBitFlags(payload.defaultPerms()));
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRBannerNamePayloads.Request.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (bannerAt(player, payload.pos()) == null) {
                return;
            }
            String username = payload.username();
            boolean valid = !LOTRBannerWhitelistEntry.hasFellowshipCode(username)
                    && player.level().getServer().services().nameToIdCache().get(username).isPresent();
            ServerPlayNetworking.send(player, new LOTRBannerNamePayloads.Validate(payload.pos(), payload.slot(), username, valid));
        });
    }

    /** The banner at this spot, if the player is near enough to be using it. */
    private static @Nullable LOTRBannerBlockEntity bannerAt(ServerPlayer player, BlockPos pos) {
        if (player.distanceToSqr(Vec3.atCenterOf(pos)) > 64.0 * 64.0) {
            return null;
        }
        return player.level().getBlockEntity(pos) instanceof LOTRBannerBlockEntity banner ? banner : null;
    }
}
