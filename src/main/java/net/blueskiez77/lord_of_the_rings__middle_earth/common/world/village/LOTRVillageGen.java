package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMountains;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

import org.jspecify.annotations.Nullable;

/**
 * LOTRVillageGen: a people's settlement -- a layout of structures around a
 * centre, turned one of four ways, and the paths between them -- built a chunk
 * at a time from a seed fixed by the world and the centre, so every chunk of
 * it agrees.
 *
 * <p>A village is spawned whole by the structure spawner
 * ({@link #createAndSetupVillageInstance} then {@link #generateCompleteVillageInstance}),
 * or found in the world by its biome's decorator a chunk at a time
 * ({@link #generateInChunk}): at its fixed settlements by the waypoints, or
 * on a grid, by chance, clear of roads, mountains and fixed structures, where
 * the biomes and variants all about suit it.
 */
public abstract class LOTRVillageGen {
    /**
     * The biome the village belongs to, by the original's LOTRBiome field name. A biome's own villages
     * are made before it is named, so they are given null and named with it (LOTRBiome.setBiomeName).
     */
    public @Nullable String villageBiome;
    public int gridScale;
    public int gridRandomDisplace;
    public float spawnChance;
    public int villageChunkRadius;
    public int fixedVillageChunkRadius;
    /** The biomes it may spawn across: its own. */
    public final List<LOTRBiome> spawnBiomes = new ArrayList<>();
    public final Collection<LocationInfo> fixedLocations = new ArrayList<>();

    protected LOTRVillageGen(@Nullable String biome) {
        this.villageBiome = biome;
    }

    /** The biome a biome's own village belongs to, given as the biome is named. */
    public void setVillageBiome(LOTRBiome biome) {
        if (this.villageBiome == null) {
            this.villageBiome = biome.biomeName;
            this.spawnBiomes.add(biome);
        }
    }

    public static boolean hasFixedSettlements() {
        // D10k: not in Middle-earth Classic either.
        return LOTRConfig.generateFixedSettlements;
    }

    /** seedVillageRand: a random seeded by the world and a place. */
    public static RandomSource seedVillageRand(long worldSeed, int i, int k) {
        return new LegacyRandomSource(i * 6890360793007L + k * 456879569029062L + worldSeed + 274893855L);
    }

    public LocationInfo addFixedLocation(LOTRWaypoint wp, int addX, int addZ, int rotation, String name) {
        LocationInfo loc = new LocationInfo(wp.getXCoord() + addX, wp.getZCoord() + addZ, rotation, name).setFixedLocation(wp);
        this.fixedLocations.add(loc);
        return loc;
    }

    public LocationInfo addFixedLocation(LOTRWaypoint wp, int rotation, String name) {
        return addFixedLocation(wp, 0, 0, rotation, name);
    }

    public void addFixedLocationMapOffset(LOTRWaypoint wp, int addX, int addZ, int rotation, String name) {
        addFixedLocation(wp, addX * LOTRMapCoords.SCALE, addZ * LOTRMapCoords.SCALE, rotation, name);
    }

    public boolean anyFixedVillagesAt(int i, int k) {
        if (!hasFixedSettlements()) {
            return false;
        }
        int checkRange = (this.fixedVillageChunkRadius + 2) << 4;
        for (LocationInfo loc : this.fixedLocations) {
            if (Math.abs(loc.posX - i) <= checkRange && Math.abs(loc.posZ - k) <= checkRange) {
                return true;
            }
        }
        return false;
    }

    /** generateInChunk: each village near enough to reach this chunk builds its part of it. */
    public void generateInChunk(WorldGenLevel world, int i, int k) {
        for (AbstractInstance<?> instance : getNearbyVillagesAtPosition(world, i, k)) {
            instance.setupVillageStructures();
            generateInstanceInChunk(instance, world, i, k);
        }
    }

    /**
     * getNearbyVillages: every village whose reach takes in this chunk. The world is given by its
     * seed and chunk manager, which the terrain pass has before there is a level to give.
     */
    public List<AbstractInstance<?>> getNearbyVillages(long worldSeed, LOTRWorldChunkManager chunkManager, int chunkX, int chunkZ) {
        List<AbstractInstance<?>> villages = new ArrayList<>();
        int checkRange = Math.max(this.villageChunkRadius, this.fixedVillageChunkRadius);
        for (int i = chunkX - checkRange; i <= chunkX + checkRange; ++i) {
            for (int k = chunkZ - checkRange; k <= chunkZ + checkRange; ++k) {
                LocationInfo loc = isVillageCentre(worldSeed, chunkManager, i, k);
                if (!loc.isPresent()) {
                    continue;
                }
                int centreX;
                int centreZ;
                if (loc.isFixedLocation()) {
                    centreX = loc.posX;
                    centreZ = loc.posZ;
                } else {
                    centreX = (i << 4) + 8;
                    centreZ = (k << 4) + 8;
                }
                villages.add(createAndSetupVillageInstance(worldSeed, centreX, centreZ, seedVillageRand(worldSeed, centreX, centreZ), loc));
            }
        }
        return villages;
    }

    public List<AbstractInstance<?>> getNearbyVillagesAtPosition(WorldGenLevel world, int i, int k) {
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(world);
        return chunkManager == null ? List.of() : getNearbyVillagesAtPosition(world.getSeed(), chunkManager, i, k);
    }

    public List<AbstractInstance<?>> getNearbyVillagesAtPosition(long worldSeed, LOTRWorldChunkManager chunkManager, int i, int k) {
        return getNearbyVillages(worldSeed, chunkManager, i >> 4, k >> 4);
    }

    /**
     * isVillageCentre: whether the village has its centre in this chunk -- a fixed settlement's
     * chunk (and none other near one), else one on its grid, displaced at random, by its spawn
     * chance, clear of roads, mountains and fixed structures, where the biomes and variants all
     * about suit it.
     */
    public LocationInfo isVillageCentre(long worldSeed, LOTRWorldChunkManager worldChunkMgr, int chunkX, int chunkZ) {
        LOTRVillagePositionCache cache = worldChunkMgr.getVillageCache(this);
        LocationInfo cacheLocation = cache.getLocationAt(chunkX, chunkZ);
        if (cacheLocation != null) {
            return cacheLocation;
        }
        if (hasFixedSettlements()) {
            for (LocationInfo loc : this.fixedLocations) {
                int locChunkX = loc.posX >> 4;
                int locChunkZ = loc.posZ >> 4;
                if (chunkX == locChunkX && chunkZ == locChunkZ) {
                    return cache.markResult(chunkX, chunkZ, loc);
                }
                int locCheckSize = Math.max(this.villageChunkRadius, this.fixedVillageChunkRadius);
                if (Math.abs(chunkX - locChunkX) > locCheckSize || Math.abs(chunkZ - locChunkZ) > locCheckSize) {
                    continue;
                }
                return cache.markResult(chunkX, chunkZ, LocationInfo.NONE_HERE);
            }
        }
        int i2 = Mth.floor((double) chunkX / this.gridScale);
        int k2 = Mth.floor((double) chunkZ / this.gridScale);
        RandomSource villageRand = seedVillageRand(worldSeed, i2, k2);
        i2 *= this.gridScale;
        k2 *= this.gridScale;
        i2 += LOTRWorldGenUtil.getRandomIntegerInRange(villageRand, -this.gridRandomDisplace, this.gridRandomDisplace);
        if (chunkX == i2 && chunkZ == k2 + LOTRWorldGenUtil.getRandomIntegerInRange(villageRand, -this.gridRandomDisplace, this.gridRandomDisplace)) {
            int i1 = chunkX * 16 + 8;
            int k1 = chunkZ * 16 + 8;
            int villageRange = this.villageChunkRadius * 16;
            if (villageRand.nextFloat() < this.spawnChance) {
                int diagRange = (int) Math.round((villageRange + 8) * SQRT2);
                boolean anythingNear = LOTRRoads.isRoadNear(i1, k1, diagRange) >= 0.0f;
                if (!anythingNear && !(anythingNear = LOTRMountains.mountainNear(i1, k1, diagRange))) {
                    anythingNear = LOTRFixedStructures.structureNear(i1, k1, diagRange);
                }
                if (!anythingNear) {
                    LocationInfo loc = LocationInfo.RANDOM_GEN_HERE;
                    AbstractInstance<?> instance = createAndSetupVillageInstance(worldSeed, i1, k1, seedVillageRand(worldSeed, i1, k1), loc);
                    boolean flat = instance.isFlat();
                    if (worldChunkMgr.areBiomesViable(i1, k1, villageRange, this.spawnBiomes)
                            && worldChunkMgr.areVariantsSuitableVillage(i1, k1, villageRange, flat)) {
                        return cache.markResult(chunkX, chunkZ, loc);
                    }
                }
            }
        }
        return cache.markResult(chunkX, chunkZ, LocationInfo.NONE_HERE);
    }

    private static final double SQRT2 = 1.4142135623730951;

    /**
     * LOTRBiome.getRoadBlock for the biomes a village asks it of -- only the
     * Gondor villages do: Dor-en-Ernil's own road, else Gondor's mixed one.
     */
    public static LOTRRoadType biomeRoadBlock(String biome) {
        return "dorEnErnil".equals(biome) ? LOTRRoadType.DOL_AMROTH : LOTRRoadType.GONDOR_MIX;
    }

    public AbstractInstance<?> createAndSetupVillageInstance(long worldSeed, int i, int k, RandomSource random,
                                                             LocationInfo location) {
        AbstractInstance<?> instance = createVillageInstance(worldSeed, i, k, random, location);
        instance.setupBaseAndVillageProperties();
        return instance;
    }

    public abstract AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random,
                                                              LocationInfo loc);

    public void generateCompleteVillageInstance(AbstractInstance<?> instance, WorldGenLevel world, int i, int k) {
        instance.setupVillageStructures();
        int checkRange = Math.max(this.villageChunkRadius, this.fixedVillageChunkRadius);
        for (int i1 = -checkRange; i1 <= checkRange; ++i1) {
            for (int k1 = -checkRange; k1 <= checkRange; ++k1) {
                int i2 = i - 8 + i1 * 16;
                int k2 = k - 8 + k1 * 16;
                generateInstanceInChunk(instance, world, i2, k2);
            }
        }
    }

    public void generateInstanceInChunk(AbstractInstance<?> instance, WorldGenLevel world, int i, int k) {
        for (int i1 = i; i1 <= i + 15; ++i1) {
            for (int k1 = k; k1 <= k + 15; ++k1) {
                PathAt path = getHeightAndPath(instance, world, i1, k1);
                if (path != null) {
                    instance.setupWorldPositionSeed(i1, k1);
                    LOTRRoadType.RoadBlock roadblock = path.road.getBlock(instance.instanceRand, true, path.isSlab);
                    LOTRRoadType.RoadBlock roadblockSolid = path.road.getBlock(instance.instanceRand, false, false);
                    world.setBlock(new BlockPos(i1, path.j, k1), roadblock.state(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                    world.setBlock(new BlockPos(i1, path.j - 1, k1), roadblockSolid.state(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                    BlockPos abovePos = new BlockPos(i1, path.j + 1, k1);
                    if (!world.getBlockState(abovePos).canSurvive(world, abovePos)) {
                        world.setBlock(abovePos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                instance.setupWorldPositionSeed(i1, k1);
                for (StructureInfo struct : instance.structures) {
                    int[] coords = instance.getWorldCoords(struct.posX(), struct.posZ());
                    if (i1 != coords[0] || k1 != coords[1]) {
                        continue;
                    }
                    int j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                    // The original's floor: 62, or just under a flatter world's own minimum.
                    int minHeight = Math.min(62, world.getSeaLevel() - 1);
                    if (j1 <= minHeight) {
                        continue;
                    }
                    struct.structure().generateAndFinish(world, instance.instanceRand, i1, j1, k1,
                            instance.getStructureRotation(struct.rotation()));
                }
            }
        }
    }

    private record PathAt(int j, LOTRRoadType road, boolean isSlab) {
    }

    /**
     * getHeight_getPath_isSlab: where a path runs here, at what height, and
     * whether it steps down to a slab because ground beside it is lower --
     * unless something solid stands right above.
     */
    private @Nullable PathAt getHeightAndPath(AbstractInstance<?> instance, WorldGenLevel world, int i, int k) {
        instance.setupWorldPositionSeed(i, k);
        int[] coords = instance.getRelativeCoords(i, k);
        LOTRRoadType road = instance.getPath(instance.instanceRand, coords[0], coords[1]);
        int j1 = getTopTerrainBlock(world, i, k, true);
        if (road == null || j1 <= 0 || !LOTRStructureBase2.isSurfaceStatic(world, i, j1, k)) {
            return null;
        }
        int[][] slabArray = new int[3][3];
        for (int i2 = -1; i2 <= 1; ++i2) {
            for (int k2 = -1; k2 <= 1; ++k2) {
                slabArray[i2 + 1][k2 + 1] = j1;
                if (i2 == 0 && k2 == 0) {
                    continue;
                }
                int j2 = getTopTerrainBlock(world, i + i2, k + k2, true);
                if (j2 > 0 && j2 < j1) {
                    slabArray[i2 + 1][k2 + 1] = j2;
                }
            }
        }
        boolean isSlab = slabArray[0][1] < j1 || slabArray[2][1] < j1 || slabArray[1][0] < j1 || slabArray[1][2] < j1
                || slabArray[0][0] < j1 || slabArray[2][0] < j1 || slabArray[0][2] < j1 || slabArray[2][2] < j1;
        BlockPos above = new BlockPos(i, j1 + 1, k);
        if (isSlab && world.getBlockState(above).isSolidRender()) {
            isSlab = false;
        }
        return new PathAt(j1, road, isSlab);
    }

    /** getTopTerrainBlock: the highest solid block (or slab on solid), above sea level and under no liquid, or -1. */
    public int getTopTerrainBlock(WorldGenLevel world, int i, int k, boolean acceptSlab) {
        int j = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k) - 1;
        while (world.getFluidState(new BlockPos(i, j + 1, k)).isEmpty()) {
            BlockState block = world.getBlockState(new BlockPos(i, j, k));
            BlockState below = world.getBlockState(new BlockPos(i, j - 1, k));
            if (block.isSolidRender() || acceptSlab && block.getBlock() instanceof SlabBlock && below.isSolidRender()) {
                return j;
            }
            j--;
            if (j <= 62) {
                break;
            }
        }
        return -1;
    }

    /**
     * {@code world.getBlock(..) == LOTRMod.<field> && meta == m}: each of the
     * mod's old metadata variants is its own block now, so the block decides.
     */
    protected static boolean is(BlockState state, String field, int meta) {
        return state.is(LOTRLegacyBlocks.mod(field).state(meta).getBlock());
    }

    /** A village laid out around a centre, with its own seeded random and the structures and paths it chose. */
    public abstract static class AbstractInstance<V extends LOTRVillageGen> implements LOTRStructureBase2.VillageSurface {
        public final @Nullable String instanceVillageBiome;
        /** The world's seed, which the village's own randoms are seeded from. */
        public final long worldSeed;
        public final RandomSource instanceRand;
        public final long instanceRandSeed;
        public final int centreX;
        public final int centreZ;
        public int rotationMode;
        public final Collection<StructureInfo> structures = new ArrayList<>();
        public final LocationInfo locationInfo;

        protected AbstractInstance(V village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            this.instanceVillageBiome = village.villageBiome;
            this.worldSeed = worldSeed;
            this.instanceRand = RandomSource.create();
            this.instanceRandSeed = random.nextLong();
            this.centreX = i;
            this.centreZ = k;
            this.locationInfo = loc;
        }

        public void addStructure(LOTRStructureBase2 structure, int x, int z, int r) {
            addStructure(structure, x, z, r, false);
        }

        public void addStructure(LOTRStructureBase2 structure, int x, int z, int r, boolean force) {
            structure.villageInstance = this;
            structure.restrictions = !force;
            if (force) {
                structure.shouldFindSurface = true;
            }
            this.structures.add(new StructureInfo(structure, x, z, r));
        }

        public abstract void addVillageStructures(RandomSource random);

        public abstract @Nullable LOTRRoadType getPath(RandomSource random, int i, int k);

        public int[] getRelativeCoords(int xWorld, int zWorld) {
            return switch (this.rotationMode) {
                case 0 -> new int[]{this.centreX - xWorld, this.centreZ - zWorld};
                case 1 -> new int[]{this.centreZ - zWorld, xWorld - this.centreX};
                case 2 -> new int[]{xWorld - this.centreX, zWorld - this.centreZ};
                case 3 -> new int[]{zWorld - this.centreZ, this.centreX - xWorld};
                default -> new int[]{0, 0};
            };
        }

        public int getStructureRotation(int r) {
            return (r + this.rotationMode + 2) % 4;
        }

        public int[] getWorldCoords(int xRel, int zRel) {
            return switch (this.rotationMode) {
                case 0 -> new int[]{this.centreX - xRel, this.centreZ - zRel};
                case 1 -> new int[]{this.centreX + zRel, this.centreZ - xRel};
                case 2 -> new int[]{this.centreX + xRel, this.centreZ + zRel};
                case 3 -> new int[]{this.centreX - zRel, this.centreZ + xRel};
                default -> new int[]{this.centreX, this.centreZ};
            };
        }

        public abstract boolean isFlat();

        @Override
        public abstract boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k);

        public void setRotation(int i) {
            this.rotationMode = i;
        }

        public void setupBaseAndVillageProperties() {
            setupVillageSeed();
            this.rotationMode = this.locationInfo.isFixedLocation() ? (this.locationInfo.rotation + 2) % 4
                    : this.instanceRand.nextInt(4);
            setupVillageProperties(this.instanceRand);
        }

        public abstract void setupVillageProperties(RandomSource random);

        public void setupVillageSeed() {
            long seed = this.centreX * 580682095692076767L + this.centreZ * 12789948968296726L + this.worldSeed
                    + 49920968939865L;
            this.instanceRand.setSeed(seed + this.instanceRandSeed);
        }

        public void setupVillageStructures() {
            setupVillageSeed();
            this.structures.clear();
            addVillageStructures(this.instanceRand);
        }

        public void setupWorldPositionSeed(int i, int k) {
            setupVillageSeed();
            int[] coords = getRelativeCoords(i, k);
            long seed1 = this.instanceRand.nextLong();
            long seed2 = this.instanceRand.nextLong();
            long seed = coords[0] * seed1 + coords[1] * seed2 ^ this.worldSeed;
            this.instanceRand.setSeed(seed);
        }
    }

    public record StructureInfo(LOTRStructureBase2 structure, int posX, int posZ, int rotation) {
    }
}
