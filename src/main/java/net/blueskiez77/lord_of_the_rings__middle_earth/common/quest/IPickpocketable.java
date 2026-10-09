package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * IPickpocketable: an NPC whose pockets can be picked for a pickpocketing quest, and the item it gives
 * up. Stolen goods carry who they were taken from and who wanted them; no trader, coin exchange or
 * collecting quest will take them, and only the one who wanted them counts them.
 */
public interface IPickpocketable {

    boolean canPickpocket();

    ItemStack createPickpocketItem();

    /** The "LOTRPickpocket" tag's Owner, Wanter and WanterID. */
    record Stolen(String owner, String wanter, UUID wanterID) {
        public static final Codec<Stolen> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("Owner").forGetter(Stolen::owner),
                Codec.STRING.fieldOf("Wanter").forGetter(Stolen::wanter),
                UUIDUtil.STRING_CODEC.fieldOf("WanterID").forGetter(Stolen::wanterID)).apply(i, Stolen::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Stolen> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Stolen::owner, ByteBufCodecs.STRING_UTF8, Stolen::wanter,
                UUIDUtil.STREAM_CODEC, Stolen::wanterID, Stolen::new);
    }

    final class Helper {

        private Helper() {
        }

        public static @Nullable String getOwner(ItemStack stack) {
            Stolen stolen = stack.get(LOTRDataComponents.PICKPOCKET);
            return stolen == null ? null : stolen.owner();
        }

        public static @Nullable String getWanter(ItemStack stack) {
            Stolen stolen = stack.get(LOTRDataComponents.PICKPOCKET);
            return stolen == null ? null : stolen.wanter();
        }

        public static @Nullable UUID getWanterID(ItemStack stack) {
            Stolen stolen = stack.get(LOTRDataComponents.PICKPOCKET);
            return stolen == null ? null : stolen.wanterID();
        }

        public static boolean isPickpocketed(ItemStack stack) {
            return stack.has(LOTRDataComponents.PICKPOCKET);
        }

        public static void setPickpocketData(ItemStack stack, String ownerName, String wanterName, UUID wantedID) {
            stack.set(LOTRDataComponents.PICKPOCKET, new Stolen(ownerName, wanterName, wantedID));
        }
    }
}
