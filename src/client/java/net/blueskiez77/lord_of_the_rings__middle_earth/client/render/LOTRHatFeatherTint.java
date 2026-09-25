package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * The second render pass of LOTRItemLeatherHat.getColorFromItemStack: the
 * feather overlay, in the feather's colour. Item definitions name it as
 * {@code "type": "lotr:hat_feather"}.
 */
public record LOTRHatFeatherTint() implements ItemTintSource {

    public static final MapCodec<LOTRHatFeatherTint> CODEC = MapCodec.unit(new LOTRHatFeatherTint());

    public static void init() {
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hat_feather"), CODEC);
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        return ARGB.opaque(stack.getOrDefault(LOTRDataComponents.HAT_FEATHER, LOTRLeatherHatItem.FEATHER_WHITE));
    }

    @Override
    public MapCodec<LOTRHatFeatherTint> type() {
        return CODEC;
    }
}
