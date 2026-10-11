package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTREnvironmentClient;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryBiome;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** onRenderDebugText: under the biome, the Middle-earth biome and its variant (LOTRBiome.addBiomeF3Info). */
@Mixin(DebugEntryBiome.class)
abstract class LOTRDebugEntryBiomeMixin {

    @Unique
    private static final Identifier LOTR_GROUP = Identifier.fromNamespaceAndPath("lotr", "biome");

    @Inject(method = "display", at = @At("TAIL"))
    private void lotr$middleEarthBiome(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk,
                                       @Nullable LevelChunk serverChunk, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Entity viewer = mc.player;
        if (viewer == null || mc.level == null || !LOTREnvironmentClient.isMiddleEarth(mc.level)) {
            return;
        }
        BlockPos pos = BlockPos.containing(viewer.getX(), viewer.getBoundingBox().minY, viewer.getZ());
        LOTRBiome biome = LOTRBiomes.of(mc.level.getBiome(pos));
        if (biome == null) {
            return;
        }
        Level level = serverOrClientLevel != null && LOTREnvironmentClient.isMiddleEarth(serverOrClientLevel) ? serverOrClientLevel : mc.level;
        List<String> info = new ArrayList<>();
        biome.addBiomeF3Info(info, mc.level, LOTRBiomeVariantStorage.getBiomeVariantAt(level, pos.getX(), pos.getZ()), pos.getX(), pos.getY(), pos.getZ());
        displayer.addToGroup(LOTR_GROUP, info);
    }
}
