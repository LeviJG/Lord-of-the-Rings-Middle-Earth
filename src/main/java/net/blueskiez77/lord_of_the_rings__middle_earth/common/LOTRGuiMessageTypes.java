package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.HashSet;
import java.util.Set;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuPayloads;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRGuiMessageTypes: the one-time explanations a player is shown the first
 * time something happens, remembered once sent (LOTRPlayerData's
 * "SentMessageTypes").
 *
 * <p>NOT ported yet: UTUMNO_WARN's trigger, the Utumno portal (D10).
 */
public enum LOTRGuiMessageTypes {
    FRIENDLY_FIRE("friendlyFire"), UTUMNO_WARN("utumnoWarn"), ENCHANTING("enchanting"), ALIGN_DRAIN("alignDrain");

    public final String messageName;

    LOTRGuiMessageTypes(String name) {
        this.messageName = name;
    }

    public Component getMessage() {
        return Component.translatable("lotr.gui.message." + this.messageName);
    }

    /** The save names of the messages sent. */
    public static final AttachmentType<Set<String>> SENT_MESSAGES = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "sent_messages"),
            builder -> builder.initializer(Set::of).persistent(Codec.STRING.listOf().xmap(Set::copyOf, java.util.List::copyOf))
                    .copyOnDeath());

    /** LOTRPlayerData.sendMessageIfNotReceived. */
    public static void sendMessageIfNotReceived(Player player, LOTRGuiMessageTypes message) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        Set<String> sent = player.getAttachedOrCreate(SENT_MESSAGES);
        if (!sent.contains(message.messageName)) {
            Set<String> updated = new HashSet<>(sent);
            updated.add(message.messageName);
            player.setAttached(SENT_MESSAGES, Set.copyOf(updated));
            ServerPlayNetworking.send(serverPlayer, new LOTRMenuPayloads.Message(message.ordinal()));
        }
    }

    public static void init() {
    }
}
