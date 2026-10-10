package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkGenerator;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Middle-earth's creatures come by the mod's own spawners (LOTRSpawnerAnimals and LOTRSpawnerNPCs,
 * from its biomes' lists), as the original's replaced vanilla's spawner there; vanilla's natural
 * spawning spawns nothing in it.
 */
@Mixin(ServerChunkCache.class)
abstract class LOTRServerChunkCacheMixin {
    @Shadow
    @Final
    ServerLevel level;

    @WrapOperation(method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;J)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/NaturalSpawner;getFilteredSpawningCategories(Lnet/minecraft/world/level/NaturalSpawner$SpawnState;ZZ)Ljava/util/List;"))
    private List<MobCategory> lotr$noVanillaSpawning(NaturalSpawner.SpawnState state, boolean spawnFriendlies, boolean spawnEnemies,
                                                     Operation<List<MobCategory>> original) {
        if (this.level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator) {
            return List.of();
        }
        return original.call(state, spawnFriendlies, spawnEnemies);
    }
}
