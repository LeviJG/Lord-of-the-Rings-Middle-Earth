package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRPlayerData's FemRankOverride: gendered ranks always shown in their
 * feminine form. Sent to the player for their Options screen.
 *
 * <p>NOT ported yet: the other way to feminine ranks, a feminine title
 * (useFeminineRanks' playerTitle, with titles, D7).
 */
public final class LOTRRankOptions {

    public static final AttachmentType<Boolean> FEM_RANK_OVERRIDE = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fem_rank_override"),
            builder -> builder.initializer(() -> false).persistent(Codec.BOOL).copyOnDeath()
                    .syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.targetOnly()));

    private LOTRRankOptions() {
    }

    public static boolean getFemRankOverride(Player player) {
        return player.getAttachedOrCreate(FEM_RANK_OVERRIDE);
    }

    public static void setFemRankOverride(Player player, boolean flag) {
        player.setAttached(FEM_RANK_OVERRIDE, flag);
    }

    /** useFeminineRanks. */
    public static boolean useFeminineRanks(Player player) {
        return getFemRankOverride(player);
    }

    public static void init() {
    }
}
