package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ColorResolver;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** The level's tint caches, so the water's can be dropped when its colour changes (LOTRBiomeColoursClient). */
@Mixin(ClientLevel.class)
public interface LOTRClientLevelAccessor {

    @Accessor("tintCaches")
    Object2ObjectArrayMap<ColorResolver, BlockTintCache> lotr$tintCaches();
}
