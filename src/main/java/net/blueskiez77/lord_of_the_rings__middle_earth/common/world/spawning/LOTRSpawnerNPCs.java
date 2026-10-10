package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSpawnDamping;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import org.jspecify.annotations.Nullable;

/**
 * LOTRSpawnerNPCs: the NPCs of Middle-earth's biomes, spawned about each player -- three groups a
 * spawning point, from the biome's lists at that spot, out of sight of players and away from the
 * spawn point, up to the NPC cap (damped as more play together).
 */
public final class LOTRSpawnerNPCs {
    public static final int EXPECTED_CHUNKS = 196;
    private static final Map<ResourceKey<Level>, Integer> TICKS_SINCE_CYCLE = new HashMap<>();

    private LOTRSpawnerNPCs() {
    }

    /** The spawn point spawning keeps clear of: Middle-earth's is the ring portal's spot. */
    public static BlockPos getSpawnPoint(ServerLevel level) {
        return new BlockPos(LOTRLevelData.middleEarthPortalX, LOTRLevelData.middleEarthPortalY, LOTRLevelData.middleEarthPortalZ);
    }

    /** canNPCSpawnAtLocation: on a solid top a monster could spawn on, not bedrock, with room and no liquid. */
    public static boolean canNPCSpawnAtLocation(ServerLevel level, int i, int j, int k) {
        BlockPos below = new BlockPos(i, j - 1, k);
        BlockState belowState = level.getBlockState(below);
        if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
            return false;
        }
        BlockPos pos = new BlockPos(i, j, k);
        BlockPos above = pos.above();
        return !belowState.is(Blocks.BEDROCK) && !level.getBlockState(pos).isRedstoneConductor(level, pos)
                && level.getFluidState(pos).isEmpty() && !level.getBlockState(above).isRedstoneConductor(level, above);
    }

    public static int countNPCs(ServerLevel level) {
        int count = 0;
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof LOTRNPCEntity npc) {
                count += npc.getSpawnCountValue();
            }
        }
        return count;
    }

    /** getRandomSpawningPointInChunk: a random column of a loaded chunk, at a random height up to its top section. */
    public static @Nullable BlockPos getRandomSpawningPointInChunk(ServerLevel level, ChunkPos chunkCoords) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(chunkCoords.x(), chunkCoords.z());
        if (chunk == null) {
            return null;
        }
        RandomSource rand = level.getRandom();
        int i1 = chunkCoords.x() * 16 + rand.nextInt(16);
        int k1 = chunkCoords.z() * 16 + rand.nextInt(16);
        int j = rand.nextInt(Math.max(chunk.getHighestSectionPosition() + 16 - 1, 1));
        return new BlockPos(i1, j, k1);
    }

    public static LOTRSpawnEntry.@Nullable Instance getRandomSpawnListEntry(ServerLevel level, int i, int j, int k) {
        LOTRBiome biome = LOTRBiomes.of(level.getBiome(new BlockPos(i, j, k)));
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(level);
        if (biome == null || chunkManager == null) {
            return null;
        }
        LOTRBiomeVariant variant = chunkManager.getBiomeVariantAt(i, k);
        LOTRBiomeSpawnList spawnlist = biome.getNPCSpawnList(level, level.getRandom(), i, j, k, variant);
        return spawnlist == null ? null : spawnlist.getRandomSpawnEntry(level.getRandom(), level, i, j, k);
    }

    /** getSpawnableChunks: every chunk within seven of a player. */
    public static void getSpawnableChunks(ServerLevel level, Collection<ChunkPos> set) {
        set.clear();
        for (ServerPlayer player : level.players()) {
            int i = Mth.floor(player.getX() / 16.0);
            int k = Mth.floor(player.getZ() / 16.0);
            for (int i1 = -7; i1 <= 7; ++i1) {
                for (int k1 = -7; k1 <= 7; ++k1) {
                    set.add(new ChunkPos(i + i1, k + k1));
                }
            }
        }
    }

    /** getSpawnableChunksWithPlayerInRange: those with a player not in creative mode within range of their centre. */
    public static void getSpawnableChunksWithPlayerInRange(ServerLevel level, Collection<ChunkPos> set, int range) {
        getSpawnableChunks(level, set);
        List<ServerPlayer> validPlayers = new ArrayList<>();
        for (ServerPlayer player : level.players()) {
            if (!player.getAbilities().instabuild) {
                validPlayers.add(player);
            }
        }
        set.removeIf(chunkCoords -> {
            int i = chunkCoords.getMiddleBlockX();
            int k = chunkCoords.getMiddleBlockZ();
            for (ServerPlayer player : validPlayers) {
                if (player.getX() >= i - range && player.getX() <= i + range && player.getZ() >= k - range && player.getZ() <= k + range) {
                    return false;
                }
            }
            return true;
        });
    }

    public static void performSpawning(ServerLevel level) {
        int interval = LOTRConfig.mobSpawnInterval;
        if (interval > 0) {
            int ticks = TICKS_SINCE_CYCLE.getOrDefault(level.dimension(), 0) - 1;
            TICKS_SINCE_CYCLE.put(level.dimension(), ticks);
            if (ticks > 0) {
                return;
            }
            TICKS_SINCE_CYCLE.put(level.dimension(), interval);
        }
        Set<ChunkPos> eligibleSpawnChunks = new LinkedHashSet<>();
        getSpawnableChunks(level, eligibleSpawnChunks);
        BlockPos spawnPoint = getSpawnPoint(level);
        RandomSource rand = level.getRandom();
        int totalSpawnCount = countNPCs(level);
        int maxSpawnCount = LOTRSpawnDamping.getNPCSpawnCap(level) * eligibleSpawnChunks.size() / EXPECTED_CHUNKS;
        if (totalSpawnCount > maxSpawnCount) {
            return;
        }
        int cycles = Math.max(1, interval);
        for (int c = 0; c < cycles; ++c) {
            for (ChunkPos chunkCoords : shuffle(eligibleSpawnChunks)) {
                BlockPos chunkposition = getRandomSpawningPointInChunk(level, chunkCoords);
                if (chunkposition == null || level.getBlockState(chunkposition).isRedstoneConductor(level, chunkposition)
                        || !level.getBlockState(chunkposition).isAir()) {
                    continue;
                }
                int i = chunkposition.getX();
                int j = chunkposition.getY();
                int k = chunkposition.getZ();
                for (int l = 0; l < 3; ++l) {
                    int i1 = i;
                    int j1 = j;
                    int k1 = k;
                    int rangeP1 = 6;
                    int yRangeP1 = 1;
                    LOTRSpawnEntry.Instance spawnEntryInstance = getRandomSpawnListEntry(level, i1, j1, k1);
                    if (spawnEntryInstance == null) {
                        continue;
                    }
                    LOTRSpawnEntry spawnEntry = spawnEntryInstance.spawnEntry();
                    boolean isConquestSpawn = spawnEntryInstance.isConquestSpawn();
                    int spawnCount = LOTRWorldGenUtil.getRandomIntegerInRange(rand, spawnEntry.minGroupCount(), spawnEntry.maxGroupCount());
                    int chance = spawnEntryInstance.spawnChance();
                    if (chance != 0 && rand.nextInt(chance) != 0) {
                        continue;
                    }
                    SpawnGroupData entityData = null;
                    int spawned = 0;
                    int attempts = spawnCount * 8;
                    for (int a = 0; a < attempts; ++a) {
                        i1 += rand.nextInt(rangeP1) - rand.nextInt(rangeP1);
                        j1 += rand.nextInt(yRangeP1) - rand.nextInt(yRangeP1);
                        k1 += rand.nextInt(rangeP1) - rand.nextInt(rangeP1);
                        BlockPos pos = new BlockPos(i1, j1, k1);
                        float f = i1 + 0.5f;
                        float f1 = j1;
                        float f2 = k1 + 0.5f;
                        if (!level.hasChunkAt(pos) || !canNPCSpawnAtLocation(level, i1, j1, k1)
                                || level.getNearestPlayer(f, f1, f2, 24.0, false) != null
                                || spawnPoint.distToCenterSqr(f, f1, f2) < 576.0) {
                            continue;
                        }
                        Entity created = spawnEntry.type().create(level, EntitySpawnReason.NATURAL);
                        if (!(created instanceof Mob entity)) {
                            continue;
                        }
                        entity.snapTo(f, f1, f2, rand.nextFloat() * 360.0f, 0.0f);
                        if (entity instanceof LOTRNPCEntity npc && isConquestSpawn) {
                            npc.setConquestSpawning(true);
                        }
                        if (!entity.checkSpawnRules(level, EntitySpawnReason.NATURAL) || !entity.checkSpawnObstruction(level)) {
                            continue;
                        }
                        entityData = entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.NATURAL, entityData);
                        level.addFreshEntityWithPassengers(entity);
                        if (entity instanceof LOTRNPCEntity npc) {
                            npc.isNPCPersistent = false;
                            npc.setShouldTraderRespawn(false);
                            npc.setConquestSpawning(false);
                        }
                        if (c > 0 && (totalSpawnCount += entity instanceof LOTRNPCEntity npc ? npc.getSpawnCountValue() : 1) > maxSpawnCount) {
                            return;
                        }
                        if (++spawned >= spawnCount) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public static List<ChunkPos> shuffle(Set<ChunkPos> set) {
        List<ChunkPos> list = new ArrayList<>(set);
        Collections.shuffle(list);
        return list;
    }
}
