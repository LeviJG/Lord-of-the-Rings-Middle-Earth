package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGreyWandererTracker;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRInvasionSpawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGandalfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREventSpawner: the rarer comings in Middle-earth -- the travelling traders, bandits about
 * players (each biome's kind, at its chance), Gollum about the High Pass, and the Grey Wanderer --
 * and their chances.
 */
public final class LOTREventSpawner {

    /** The kinds of travelling trader any biome has registered. */
    public static final Set<EntityType<?>> TRADER_TYPES = new LinkedHashSet<>();

    private static @Nullable MinecraftServer traderServer;
    private static final Map<EntityType<?>, LOTRTravellingTraderSpawner> TRAVELLING_TRADERS = new LinkedHashMap<>();

    private LOTREventSpawner() {
    }

    /** createTraderSpawner: a spawner for each kind of travelling trader, once. */
    public static void createTraderSpawner(EntityType<?> type) {
        TRADER_TYPES.add(type);
    }

    /** The traders' spawners for this server, made once its world -- and their saved waits -- are loaded. */
    private static Iterable<LOTRTravellingTraderSpawner> travellingTraders(MinecraftServer server) {
        if (traderServer != server) {
            traderServer = server;
            TRAVELLING_TRADERS.clear();
            for (EntityType<?> type : TRADER_TYPES) {
                TRAVELLING_TRADERS.put(type, new LOTRTravellingTraderSpawner(type));
            }
        }
        return TRAVELLING_TRADERS.values();
    }

    public static void performSpawning(ServerLevel world) {
        for (LOTRTravellingTraderSpawner trader : travellingTraders(world.getServer())) {
            trader.performSpawning(world);
        }
        if (world.getGameTime() % 20L == 0L) {
            Set<ChunkPos> eligibleSpawnChunks = new LinkedHashSet<>();
            LOTRSpawnerNPCs.getSpawnableChunksWithPlayerInRange(world, eligibleSpawnChunks, 32);
            List<ChunkPos> shuffled = LOTRSpawnerNPCs.shuffle(eligibleSpawnChunks);
            if (LOTRConfig.enableBandits && world.getDifficulty() != Difficulty.PEACEFUL) {
                spawnBandits(world, shuffled);
            }
            if (LOTRConfig.enableInvasions && world.getDifficulty() != Difficulty.PEACEFUL) {
                spawnInvasions(world, shuffled);
            }
        }
        performGollumSpawning(world);
        performGreyWandererSpawning(world);
    }

    private static boolean anyNonCreativePlayer(ServerLevel world, int i, int k, int range) {
        AABB box = new AABB(i - range, world.getMinY(), k - range, i + range, world.getMaxY(), k + range);
        return !world.getEntitiesOfClass(Player.class, box, p -> p.isAlive() && !p.getAbilities().instabuild).isEmpty();
    }

    private static boolean isOpenGround(ServerLevel world, LOTRBiome biome, int i1, int j1, int k1) {
        BlockState block = world.getBlockState(new BlockPos(i1, j1 - 1, k1));
        BlockPos pos = new BlockPos(i1, j1, k1);
        return j1 > 60 && (block.is(biome.topBlock.getBlock()) || block.is(biome.fillerBlock.getBlock()))
                && !world.getBlockState(pos).isRedstoneConductor(world, pos)
                && !world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above());
    }

    /** spawnBandits: in each chunk, by the biome's chance, one to four bandits of its kind within 32 blocks, a player near. */
    public static void spawnBandits(ServerLevel world, Iterable<ChunkPos> spawnChunks) {
        RandomSource rand = world.getRandom();
        for (ChunkPos chunkCoords : spawnChunks) {
            BlockPos chunkposition = LOTRSpawnerNPCs.getRandomSpawningPointInChunk(world, chunkCoords);
            if (chunkposition == null) {
                continue;
            }
            int i = chunkposition.getX();
            int k = chunkposition.getZ();
            LOTRBiome biome = LOTRBiomes.of(world.getBiome(chunkposition));
            if (biome == null) {
                continue;
            }
            EntityType<? extends LOTRBanditEntity> banditClass = biome.getBanditEntityClass();
            double chance = biome.getBanditChance().chancesPerSecondPerChunk[16];
            if (chance <= 0.0 || rand.nextDouble() >= chance || !anyNonCreativePlayer(world, i, k, 48)) {
                continue;
            }
            int banditsSpawned = 0;
            int maxBandits = LOTRWorldGenUtil.getRandomIntegerInRange(rand, 1, 4);
            for (int attempts = 0; attempts < 32; ++attempts) {
                int i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -32, 32);
                int k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -32, 32);
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                if (!isOpenGround(world, biome, i1, j1, k1)) {
                    continue;
                }
                LOTRBanditEntity bandit = banditClass.create(world, EntitySpawnReason.EVENT);
                if (bandit == null) {
                    continue;
                }
                bandit.snapTo(i1 + 0.5, j1, k1 + 0.5, rand.nextFloat() * 360.0f, 0.0f);
                if (!bandit.checkSpawnRules(world, EntitySpawnReason.EVENT) || !bandit.checkSpawnObstruction(world)) {
                    continue;
                }
                bandit.finalizeSpawn(world, world.getCurrentDifficultyAt(bandit.blockPosition()), EntitySpawnReason.EVENT, null);
                world.addFreshEntityWithPassengers(bandit);
                bandit.isNPCPersistent = false;
                if (++banditsSpawned >= maxBandits) {
                    break;
                }
            }
        }
    }

    /**
     * spawnInvasions: in each chunk, for each chance a biome has invasions at, one of them by that
     * chance (five times as likely by night in a lunar eclipse), launched within 32 blocks where an
     * enemy of its faction -- not in creative mode -- is within 48.
     */
    public static void spawnInvasions(ServerLevel world, Iterable<ChunkPos> spawnChunks) {
        RandomSource rand = world.getRandom();
        chunkLoop:
        for (ChunkPos chunkCoords : spawnChunks) {
            BlockPos chunkposition = LOTRSpawnerNPCs.getRandomSpawningPointInChunk(world, chunkCoords);
            if (chunkposition == null) {
                continue;
            }
            int i = chunkposition.getX();
            int k = chunkposition.getZ();
            LOTRBiome biome = LOTRBiomes.of(world.getBiome(chunkposition));
            if (biome == null) {
                continue;
            }
            for (EventChance invChance : EventChance.values()) {
                List<LOTRInvasions> invList = biome.invasionSpawns.getInvasionsForChance(invChance);
                if (invList.isEmpty()) {
                    continue;
                }
                LOTRInvasions invasionType = invList.get(rand.nextInt(invList.size()));
                double chance = invChance.chancesPerSecondPerChunk[16];
                if (!world.isBrightOutside() && net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldProvider.isLunarEclipse()) {
                    chance *= 5.0;
                }
                int range = 48;
                AABB box = new AABB(i - range, world.getMinY(), k - range, i + range, world.getMaxY(), k + range);
                if (rand.nextDouble() >= chance || world.getEntitiesOfClass(Player.class, box, p -> p.isAlive() && !p.getAbilities().instabuild
                        && LOTRPlayerAlignments.getAlignment(p, invasionType.invasionFaction) < 0.0f).isEmpty()) {
                    continue;
                }
                for (int attempts = 0; attempts < 16; ++attempts) {
                    int i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -32, 32);
                    int k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -32, 32);
                    int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                    if (!isOpenGround(world, biome, i1, j1, k1)) {
                        continue;
                    }
                    LOTRInvasionSpawnerEntity invasion = LOTREntities.INVASION_SPAWNER.create(world, EntitySpawnReason.EVENT);
                    if (invasion == null) {
                        continue;
                    }
                    invasion.setInvasionType(invasionType);
                    invasion.snapTo(i1 + 0.5, j1 + (3 + rand.nextInt(3)), k1 + 0.5, 0.0f, 0.0f);
                    if (!invasion.canInvasionSpawnHere()) {
                        continue;
                    }
                    world.addFreshEntity(invasion);
                    invasion.selectAppropriateBonusFactions();
                    invasion.startInvasion();
                    continue chunkLoop;
                }
            }
        }
    }

    /** LOTRGollumSpawner: while he is not abroad, once a second, a try at a cave spot within 128 of the High Pass, no player near. */
    public static void performGollumSpawning(ServerLevel world) {
        if (LOTRLevelData.gollumSpawned || world.getGameTime() % 20L != 0L) {
            return;
        }
        RandomSource rand = world.getRandom();
        LOTRWaypoint home = LOTRWaypoint.HIGH_PASS;
        int x = home.getXCoord();
        int z = home.getZCoord();
        int homeRange = 128;
        int checkRange = 16;
        int i = LOTRWorldGenUtil.getRandomIntegerInRange(rand, x - homeRange, x + homeRange);
        int j = LOTRWorldGenUtil.getRandomIntegerInRange(rand, 16, 32);
        int k = LOTRWorldGenUtil.getRandomIntegerInRange(rand, z - homeRange, z + homeRange);
        if (!world.hasChunksAt(i - checkRange, k - checkRange, i + checkRange, k + checkRange)) {
            return;
        }
        AABB aabb = new AABB(i, j, k, i + 1, j + 1, k + 1).inflate(checkRange);
        if (!world.getEntitiesOfClass(Player.class, aabb).isEmpty()) {
            return;
        }
        BlockPos pos = new BlockPos(i, j, k);
        if (world.getBlockState(pos.below()).isRedstoneConductor(world, pos.below()) && !world.getBlockState(pos).isRedstoneConductor(world, pos)
                && !world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above())) {
            LOTRGollumEntity gollum = LOTREntities.GOLLUM.create(world, EntitySpawnReason.EVENT);
            if (gollum == null) {
                return;
            }
            gollum.snapTo(i + 0.5, j, k + 0.5, 0.0f, 0.0f);
            if (gollum.checkSpawnRules(world, EntitySpawnReason.EVENT) && gollum.checkSpawnObstruction(world)) {
                gollum.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.EVENT, null);
                world.addFreshEntity(gollum);
                LOTRLevelData.setGollumSpawned(true);
            }
        }
    }

    /**
     * LOTRGreyWandererTracker.performSpawning: while none is abroad, every two minutes, the Grey
     * Wanderer arrives 4 to 16 blocks from a player who has no Grey Wanderer quest.
     */
    public static void performGreyWandererSpawning(ServerLevel world) {
        if (!LOTRGreyWandererTracker.activeGreyWanderers.isEmpty()) {
            return;
        }
        if (world.players().isEmpty() || --LOTRGreyWandererTracker.spawnCooldown > 0) {
            return;
        }
        LOTRGreyWandererTracker.spawnCooldown = 2400;
        List<ServerPlayer> players = new ArrayList<>(world.players());
        Collections.shuffle(players);
        RandomSource rand = world.getRandom();
        for (ServerPlayer entityplayer : players) {
            if (LOTRMiniQuests.forPlayer(entityplayer.getUUID()).hasAnyGWQuest()) {
                continue;
            }
            for (int attempts = 0; attempts < 32; ++attempts) {
                float angle = rand.nextFloat() * (float) Math.PI * 2.0f;
                int r = LOTRWorldGenUtil.getRandomIntegerInRange(rand, 4, 16);
                int i = Mth.floor(entityplayer.getX() + r * Mth.cos(angle));
                int k = Mth.floor(entityplayer.getZ() + r * Mth.sin(angle));
                int j = LOTRWorldGenUtil.getHeightValue(world, i, k);
                BlockPos pos = new BlockPos(i, j, k);
                if (j <= 62 || !world.getBlockState(pos.below()).isSolidRender() || world.getBlockState(pos).isRedstoneConductor(world, pos)
                        || world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above())) {
                    continue;
                }
                LOTRGandalfEntity wanderer = LOTREntities.GANDALF.create(world, EntitySpawnReason.EVENT);
                if (wanderer == null) {
                    return;
                }
                wanderer.snapTo(i + 0.5, j, k + 0.5, rand.nextFloat() * 360.0f, 0.0f);
                wanderer.liftSpawnRestrictions = true;
                wanderer.liftBannerRestrictions = true;
                if (!wanderer.checkSpawnRules(world, EntitySpawnReason.EVENT) || !wanderer.checkSpawnObstruction(world)) {
                    continue;
                }
                wanderer.liftSpawnRestrictions = false;
                wanderer.liftBannerRestrictions = false;
                wanderer.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.EVENT, null);
                world.addFreshEntity(wanderer);
                LOTRGreyWandererTracker.addNewWanderer(wanderer.getUUID());
                wanderer.arriveAt(entityplayer);
                return;
            }
        }
    }

    /** How likely an event is: its chance an hour, as a chance a second, and per second per chunk for 0-63 chunks. */
    public enum EventChance {
        NEVER(0.0f, 0), RARE(0.1f, 3600), UNCOMMON(0.3f, 3600), COMMON(0.9f, 3600),
        BANDIT_RARE(0.1f, 3600), BANDIT_UNCOMMON(0.3f, 3600), BANDIT_COMMON(0.8f, 3600);

        public final double chancePerSecond;
        public final double[] chancesPerSecondPerChunk;

        EventChance(float prob, int s) {
            this.chancePerSecond = getChance(prob, s);
            this.chancesPerSecondPerChunk = new double[64];
            for (int i = 0; i < this.chancesPerSecondPerChunk.length; ++i) {
                this.chancesPerSecondPerChunk[i] = getChance(this.chancePerSecond, i);
            }
        }

        public static double getChance(double prob, int trials) {
            if (prob == 0.0 || trials == 0) {
                return 0.0;
            }
            return 1.0 - Math.pow(1.0 - prob, 1.0 / trials);
        }
    }
}
