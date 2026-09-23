package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * The item model conditions LOTRRenderLargeItem needed and 26.2 lacks.
 *
 * <p>The original posed a pike differently by what its holder was doing: braced
 * low while sneaking, carried at a slant otherwise, and neither mid-swing so
 * the swing itself reads normally. An items/*.json can only ask about the
 * holder through a registered condition, and vanilla has none for sneaking or
 * swinging, so these two add them as {@code lotr:sneaking} and
 * {@code lotr:swinging}. ConditionalItemModelProperties.ID_MAPPER is widened to
 * public by Fabric's transitive access wideners.
 */
public final class LOTRItemModelProperties {

    private LOTRItemModelProperties() {
    }

    /** Must run before the first resource reload, which is when models parse. */
    public static void init() {
        ConditionalItemModelProperties.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "sneaking"), Sneaking.MAP_CODEC);
        ConditionalItemModelProperties.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "swinging"), Swinging.MAP_CODEC);
    }

    /** entityliving.isSneaking(). */
    public record Sneaking() implements ConditionalItemModelProperty {
        public static final MapCodec<Sneaking> MAP_CODEC = MapCodec.unit(new Sneaking());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner,
                int seed, ItemDisplayContext displayContext) {
            return owner != null && owner.isShiftKeyDown();
        }

        @Override
        public MapCodec<Sneaking> type() {
            return MAP_CODEC;
        }
    }

    /** The inverse of {@code entityliving.swingProgress <= 0.0f}. */
    public record Swinging() implements ConditionalItemModelProperty {
        public static final MapCodec<Swinging> MAP_CODEC = MapCodec.unit(new Swinging());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner,
                int seed, ItemDisplayContext displayContext) {
            return owner != null && owner.swinging;
        }

        @Override
        public MapCodec<Swinging> type() {
            return MAP_CODEC;
        }
    }
}
