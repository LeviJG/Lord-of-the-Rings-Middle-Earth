package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Registers the per-player alignment Data Attachment and provides the
 * accessor API the rest of the mod calls. This replaces the alignment
 * get/set surface of the 1.7.10 LOTRPlayerData god-object.
 * The attachment is:
 *  - persistent (survives server restart) via LOTRAlignmentData.CODEC
 *  - synced to the owning client via LOTRAlignmentData.STREAM_CODEC
 *    (targetOnly: a player only needs their own alignment for the HUD)
 *  - copyOnDeath (alignment shouldn't reset when you die and respawn)
 * Because LOTRAlignmentData is immutable, setters read-modify-write:
 * fetch current, produce a new record, store it. Storing re-triggers sync.
 * The complex addAlignment(...) behavior (control-zone multipliers, penalty
 * scaling, conquest bonuses) is deliberately NOT here yet — this slice is
 * storage + simple get/set. That logic ports on top of this in a later slice.
 */
public final class LOTRPlayerAlignments {

    public static final AttachmentType<LOTRAlignmentData> ALIGNMENT_DATA =
            AttachmentRegistry.create(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_data"),
                    builder -> builder
                            .initializer(() -> LOTRAlignmentData.EMPTY)
                            .persistent(LOTRAlignmentData.CODEC)
                            .syncWith(LOTRAlignmentData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                            .copyOnDeath());

    private LOTRPlayerAlignments() {
    }

    /** Force class-load so the static attachment registers. Called from mod init. */
    public static void init() {
    }

    public static LOTRAlignmentData get(Player player) {
        return player.getAttachedOrCreate(ALIGNMENT_DATA);
    }

    public static float getAlignment(Player player, LOTRFaction faction) {
        if (faction.hasFixedAlignment) {
            return faction.fixedAlignment;
        }
        return get(player).getAlignment(faction);
    }

    public static void setAlignment(Player player, LOTRFaction faction, float value) {
        if (faction.hasFixedAlignment) {
            return;
        }
        LOTRAlignmentData current = get(player);
        player.setAttached(ALIGNMENT_DATA, current.withAlignment(faction, value));
    }

    public static void addAlignment(Player player, LOTRFaction faction, float delta) {
        setAlignment(player, faction, getAlignment(player, faction) + delta);
    }

    public static LOTRFactionRank getRank(Player player, LOTRFaction faction) {
        return faction.getRank(getAlignment(player, faction));
    }

    public static boolean isPledgedTo(Player player, LOTRFaction faction) {
        return get(player).isPledgedTo(faction);
    }

    public static void setPledgeFaction(Player player, LOTRFaction faction) {
        player.setAttached(ALIGNMENT_DATA, get(player).withPledgeFaction(faction));
    }

    public static boolean getHideAlignment(Player player) {
        return get(player).hideAlignment();
    }

    public static void setHideAlignment(Player player, boolean hide) {
        player.setAttached(ALIGNMENT_DATA, get(player).withHideAlignment(hide));
    }
}

