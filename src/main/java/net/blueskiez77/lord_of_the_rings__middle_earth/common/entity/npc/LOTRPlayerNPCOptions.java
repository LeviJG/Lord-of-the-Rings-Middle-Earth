package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRPlayerData's two NPC options (LOTROptions FRIENDLY_FIRE and
 * HIRED_DEATH_MESSAGES), under the original's keys: whether the player may
 * attack allied NPCs (off by default), and whether they are told when a unit
 * of theirs dies (on by default). Kept through death.
 *
 * <p>NOT ported yet: the Options screen that toggles them (D16).
 */
public final class LOTRPlayerNPCOptions {

    public record Options(boolean friendlyFire, boolean hiredDeathMessages) {
        public static final Codec<Options> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.optionalFieldOf("FriendlyFire", false).forGetter(Options::friendlyFire),
                Codec.BOOL.optionalFieldOf("HiredDeathMessages", true).forGetter(Options::hiredDeathMessages)
        ).apply(i, Options::new));
    }

    public static final AttachmentType<Options> OPTIONS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "npc_options"),
            builder -> builder.initializer(() -> new Options(false, true)).persistent(Options.CODEC).copyOnDeath());

    private LOTRPlayerNPCOptions() {
    }

    public static boolean getFriendlyFire(Player player) {
        return player.getAttachedOrCreate(OPTIONS).friendlyFire();
    }

    public static void setFriendlyFire(Player player, boolean flag) {
        player.setAttached(OPTIONS, new Options(flag, player.getAttachedOrCreate(OPTIONS).hiredDeathMessages()));
    }

    public static boolean getEnableHiredDeathMessages(Player player) {
        return player.getAttachedOrCreate(OPTIONS).hiredDeathMessages();
    }

    public static void setEnableHiredDeathMessages(Player player, boolean flag) {
        player.setAttached(OPTIONS, new Options(player.getAttachedOrCreate(OPTIONS).friendlyFire(), flag));
    }

    public static void init() {
    }
}
