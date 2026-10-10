package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSpawnDamping;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRAnimalSpawnConditions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRSpawnerAnimals: 1.7.10's creature spawner as the mod ran it -- each kind (monsters, creatures,
 * ambient, water creatures and the mod's own ambient creatures) up to its cap about the players, from
 * the biome's lists, animals only every 400 ticks, a kind that keeps failing set aside a while; and the
 * animals placed as the world is made (worldGenSpawnAnimals).
 */
public final class LOTRSpawnerAnimals {
    private static final Map<ResourceKey<Level>, Integer> TICKS_SINCE_CYCLE = new HashMap<>();
    private static final Map<ResourceKey<Level>, Map<LOTRCreatureType, TypeInfo>> DIM_INFOS = new HashMap<>();

    private LOTRSpawnerAnimals() {
    }

    private static TypeInfo forDimAndType(ServerLevel level, LOTRCreatureType type) {
        return DIM_INFOS.computeIfAbsent(level.dimension(), d -> new EnumMap<>(LOTRCreatureType.class))
                .computeIfAbsent(type, t -> new TypeInfo());
    }

    /** spawnRandomCreature: one of the biome's entries of this kind here, by weight, or none. */
    private static @Nullable LOTRSpawnEntry spawnRandomCreature(ServerLevel level, LOTRCreatureType type, int i, int j, int k) {
        LOTRBiome biome = LOTRBiomes.of(level.getBiome(new BlockPos(i, j, k)));
        if (biome == null) {
            return null;
        }
        List<LOTRSpawnEntry> list = type.getSpawnableList(biome);
        return list.isEmpty() ? null : LOTRWeightedRandom.getRandomItem(level.getRandom(), list);
    }

    /** SpawnerAnimals.canCreatureTypeSpawnAtLocation: in water for the water kinds; else on solid ground with room. */
    public static boolean canCreatureTypeSpawnAtLocation(LOTRCreatureType type, ServerLevel level, int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockPos above = pos.above();
        if (type.water) {
            return level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos.below()).is(FluidTags.WATER)
                    && !level.getBlockState(above).isRedstoneConductor(level, above);
        }
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
            return false;
        }
        return !belowState.is(Blocks.BEDROCK) && !level.getBlockState(pos).isRedstoneConductor(level, pos)
                && level.getFluidState(pos).isEmpty() && !level.getBlockState(above).isRedstoneConductor(level, above);
    }

    private static int countEntities(ServerLevel level, LOTRCreatureType type) {
        int count = 0;
        for (Entity entity : level.getAllEntities()) {
            if (type.counts(entity)) {
                ++count;
            }
        }
        return count;
    }

    public static int performSpawning(ServerLevel level, boolean hostiles, boolean peacefuls, boolean rareTick) {
        int interval = rareTick ? 0 : LOTRConfig.mobSpawnInterval;
        if (interval > 0) {
            int ticks = TICKS_SINCE_CYCLE.getOrDefault(level.dimension(), 0) - 1;
            TICKS_SINCE_CYCLE.put(level.dimension(), ticks);
            if (ticks > 0) {
                return 0;
            }
            TICKS_SINCE_CYCLE.put(level.dimension(), interval);
        }
        if (!hostiles && !peacefuls) {
            return 0;
        }
        int totalSpawned = 0;
        Set<ChunkPos> eligibleSpawnChunks = new LinkedHashSet<>();
        LOTRSpawnerNPCs.getSpawnableChunks(level, eligibleSpawnChunks);
        BlockPos spawnPoint = LOTRSpawnerNPCs.getSpawnPoint(level);
        RandomSource rand = level.getRandom();
        typeLoop:
        for (LOTRCreatureType creatureType : LOTRCreatureType.values()) {
            TypeInfo typeInfo = forDimAndType(level, creatureType);
            boolean canSpawnType = creatureType.peacefulCreature ? peacefuls : hostiles;
            if (creatureType.animal) {
                canSpawnType = rareTick;
            }
            if (!canSpawnType) {
                continue;
            }
            int count = countEntities(level, creatureType);
            int maxCount = LOTRSpawnDamping.getCreatureSpawnCap(creatureType, level) * eligibleSpawnChunks.size() / LOTRSpawnerNPCs.EXPECTED_CHUNKS;
            if (count > maxCount) {
                continue;
            }
            int cycles = Math.max(1, interval);
            for (int c = 0; c < cycles; ++c) {
                if (typeInfo.blockedCycles > 0) {
                    --typeInfo.blockedCycles;
                    continue;
                }
                int newlySpawned = 0;
                chunkLoop:
                for (ChunkPos chunkCoords : LOTRSpawnerNPCs.shuffle(eligibleSpawnChunks)) {
                    BlockPos chunkposition = LOTRSpawnerNPCs.getRandomSpawningPointInChunk(level, chunkCoords);
                    if (chunkposition == null) {
                        continue;
                    }
                    int i = chunkposition.getX();
                    int j = chunkposition.getY();
                    int k = chunkposition.getZ();
                    BlockState here = level.getBlockState(chunkposition);
                    boolean materialMatches = creatureType.water ? level.getFluidState(chunkposition).is(FluidTags.WATER) : here.isAir();
                    if (spawnRandomCreature(level, creatureType, i, j, k) == null
                            || here.isRedstoneConductor(level, chunkposition) || !materialMatches) {
                        continue;
                    }
                    for (int groupsSpawned = 0; groupsSpawned < 3; ++groupsSpawned) {
                        int i1 = i;
                        int j1 = j;
                        int k1 = k;
                        int range = 6;
                        LOTRSpawnEntry spawnEntry = null;
                        SpawnGroupData entityData = null;
                        for (int attempts = 0; attempts < 4; ++attempts) {
                            i1 += rand.nextInt(range) - rand.nextInt(range);
                            k1 += rand.nextInt(range) - rand.nextInt(range);
                            BlockPos pos = new BlockPos(i1, j1, k1);
                            float f = i1 + 0.5f;
                            float f1 = j1;
                            float f2 = k1 + 0.5f;
                            if (!level.hasChunkAt(pos) || !canCreatureTypeSpawnAtLocation(creatureType, level, i1, j1, k1)
                                    || level.getNearestPlayer(f, f1, f2, 24.0, false) != null
                                    || spawnPoint.distToCenterSqr(f, f1, f2) < 576.0) {
                                continue;
                            }
                            if (spawnEntry == null && (spawnEntry = spawnRandomCreature(level, creatureType, i1, j1, k1)) == null) {
                                continue chunkLoop;
                            }
                            Entity created = spawnEntry.type().create(level, EntitySpawnReason.NATURAL);
                            if (!(created instanceof Mob entity)) {
                                continue;
                            }
                            entity.snapTo(f, f1, f2, rand.nextFloat() * 360.0f, 0.0f);
                            if (!entity.checkSpawnRules(level, EntitySpawnReason.NATURAL) || !entity.checkSpawnObstruction(level)) {
                                continue;
                            }
                            ++totalSpawned;
                            entityData = entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.NATURAL, entityData);
                            level.addFreshEntityWithPassengers(entity);
                            ++newlySpawned;
                            if (c > 0 && ++count > maxCount) {
                                continue typeLoop;
                            }
                            if (groupsSpawned >= entity.getMaxSpawnClusterSize()) {
                                continue chunkLoop;
                            }
                        }
                    }
                }
                if (newlySpawned == 0) {
                    if (++typeInfo.failedCycles >= 10) {
                        typeInfo.failedCycles = 0;
                        typeInfo.blockedCycles = 100;
                    }
                } else if (typeInfo.failedCycles > 0) {
                    --typeInfo.failedCycles;
                }
            }
        }
        return totalSpawned;
    }

    /**
     * worldGenSpawnAnimals: as the chunk is populated, packs of the biome's creatures about it, one
     * pack in ten (vanilla's spawning chance), each animal where it would stand and where it allows.
     */
    public static void worldGenSpawnAnimals(ServerLevel level, LOTRBiome biome, LOTRBiomeVariant variant, int i, int k, RandomSource rand) {
        int spawnRange = 16;
        int spawnFuzz = 5;
        List<LOTRSpawnEntry> spawnList = biome.spawnableCreatureList;
        if (spawnList.isEmpty()) {
            return;
        }
        while (rand.nextFloat() < 0.1f) {
            LOTRSpawnEntry spawnEntry = LOTRWeightedRandom.getRandomItem(level.getRandom(), spawnList);
            int count = LOTRWorldGenUtil.getRandomIntegerInRange(rand, spawnEntry.minGroupCount(), spawnEntry.maxGroupCount());
            SpawnGroupData entityData = null;
            int packX = i + rand.nextInt(spawnRange);
            int packZ = k + rand.nextInt(spawnRange);
            int i1 = packX;
            int k1 = packZ;
            for (int l = 0; l < count; ++l) {
                boolean spawned = false;
                for (int a = 0; !spawned && a < 4; ++a) {
                    int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(level, i1, k1);
                    if (canCreatureTypeSpawnAtLocation(LOTRCreatureType.CREATURE, level, i1, j1, k1)) {
                        Entity created = spawnEntry.type().create(level, EntitySpawnReason.CHUNK_GENERATION);
                        if (created instanceof Mob entity && (!(entity instanceof LOTRAnimalSpawnConditions conditions)
                                || conditions.canWorldGenSpawnAt(i1, j1, k1, biome, variant))) {
                            entity.snapTo(i1 + 0.5f, j1, k1 + 0.5f, rand.nextFloat() * 360.0f, 0.0f);
                            entityData = entity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()),
                                    EntitySpawnReason.CHUNK_GENERATION, entityData);
                            level.addFreshEntityWithPassengers(entity);
                            spawned = true;
                        }
                    }
                    i1 += rand.nextInt(spawnFuzz) - rand.nextInt(spawnFuzz);
                    k1 += rand.nextInt(spawnFuzz) - rand.nextInt(spawnFuzz);
                    while (i1 < i || i1 >= i + spawnRange || k1 < k || k1 >= k + spawnRange) {
                        i1 = packX + rand.nextInt(spawnFuzz) - rand.nextInt(spawnFuzz);
                        k1 = packZ + rand.nextInt(spawnFuzz) - rand.nextInt(spawnFuzz);
                    }
                }
            }
        }
    }

    private static final class TypeInfo {
        int failedCycles;
        int blockedCycles;
    }
}
