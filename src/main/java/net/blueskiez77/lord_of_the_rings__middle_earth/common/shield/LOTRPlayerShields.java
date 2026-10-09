package net.blueskiez77.lord_of_the_rings__middle_earth.common.shield;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's Shield: the shield a player bears, which every player sees
 * (LOTRPacketShield, sent to all in the world as it is chosen and as players join).
 */
public final class LOTRPlayerShields {

    private static final Codec<LOTRShields> CODEC = Codec.STRING.comapFlatMap(name -> {
        LOTRShields shield = LOTRShields.shieldForName(name);
        return shield != null ? DataResult.success(shield) : DataResult.error(() -> "Unknown LOTR shield: " + name);
    }, LOTRShields::name);

    public static final AttachmentType<LOTRShields> SHIELD = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "shield"),
            builder -> builder.persistent(CODEC).copyOnDeath()
                    .syncWith(ByteBufCodecs.idMapper(i -> LOTRShields.values()[i], LOTRShields::ordinal),
                            AttachmentSyncPredicate.all()));

    private LOTRPlayerShields() {
    }

    public static @Nullable LOTRShields getShield(Player player) {
        return player.getAttached(SHIELD);
    }

    public static void setShield(Player player, @Nullable LOTRShields shield) {
        player.setAttached(SHIELD, shield);
    }

    public static void init() {
    }
}
