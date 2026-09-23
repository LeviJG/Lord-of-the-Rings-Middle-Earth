package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRPlayerData's alcoholTolerance: every bout of drunkenness adds to it, and it
 * makes the next drink a little less likely to take. Saved with the player and
 * kept through death, as the original's player data was.
 */
public final class LOTRAlcoholTolerance {

    public static final AttachmentType<Integer> TOLERANCE = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alcohol_tolerance"),
            builder -> builder.initializer(() -> 0).persistent(Codec.INT).copyOnDeath());

    private LOTRAlcoholTolerance() {
    }

    public static int get(Player player) {
        return player.getAttachedOrCreate(TOLERANCE);
    }

    public static void set(Player player, int tolerance) {
        player.setAttached(TOLERANCE, tolerance);
    }

    public static void init() {
    }
}
