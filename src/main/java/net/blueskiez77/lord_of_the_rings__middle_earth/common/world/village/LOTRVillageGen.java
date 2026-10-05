package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import java.util.ArrayList;
import java.util.Collection;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import org.jspecify.annotations.Nullable;

/**
 * LOTRVillageGen: a people's settlement -- a layout of structures around a
 * centre, turned one of four ways, and the paths between them -- built a chunk
 * at a time from a seed fixed by the world and the centre, so every chunk of
 * it agrees.
 *
 * <p>Ported: everything a village needs to be built where it is spawned (the
 * structure spawner, {@link #createAndSetupVillageInstance} then
 * {@link #generateCompleteVillageInstance}). The grid and spawn chance each
 * generator sets are kept for natural generation; finding village centres in
 * the world (isVillageCentre, its position cache, the road, mountain and
 * fixed-structure checks), the fixed settlements at waypoints, and the biome
 * each spawns in come with the world (D10).
 */
public abstract class LOTRVillageGen {
    /** The biome the village belongs to, by the original's LOTRBiome field name (D10). */
    public final String villageBiome;
    public int gridScale;
    public int gridRandomDisplace;
    public float spawnChance;
    public int villageChunkRadius;
    public int fixedVillageChunkRadius;

    protected LOTRVillageGen(String biome) {
        this.villageBiome = biome;
    }

    /**
     * LOTRBiome.getRoadBlock for the biomes a village asks it of -- only the
     * Gondor villages do: Dor-en-Ernil's own road, else Gondor's mixed one.
     * The biomes themselves come with D10.
     */
    public static LOTRRoadType biomeRoadBlock(String biome) {
        return "dorEnErnil".equals(biome) ? LOTRRoadType.DOL_AMROTH : LOTRRoadType.GONDOR_MIX;
    }

    public AbstractInstance<?> createAndSetupVillageInstance(WorldGenLevel world, int i, int k, RandomSource random,
                                                             LocationInfo location) {
        AbstractInstance<?> instance = createVillageInstance(world, i, k, random, location);
        instance.setupBaseAndVillageProperties();
        return instance;
    }

    public abstract AbstractInstance<?> createVillageInstance(WorldGenLevel world, int i, int k, RandomSource random,
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
                    world.setBlock(new BlockPos(i1, path.j, k1), roadblock.state(), 2);
                    world.setBlock(new BlockPos(i1, path.j - 1, k1), roadblockSolid.state(), 2);
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
        public final String instanceVillageBiome;
        public final WorldGenLevel theWorld;
        public final RandomSource instanceRand;
        public final long instanceRandSeed;
        public final int centreX;
        public final int centreZ;
        public int rotationMode;
        public final Collection<StructureInfo> structures = new ArrayList<>();
        public final LocationInfo locationInfo;

        protected AbstractInstance(V village, WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
            this.instanceVillageBiome = village.villageBiome;
            this.theWorld = world;
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
            long seed = this.centreX * 580682095692076767L + this.centreZ * 12789948968296726L + this.theWorld.getSeed()
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
            long seed = coords[0] * seed1 + coords[1] * seed2 ^ this.theWorld.getSeed();
            this.instanceRand.setSeed(seed);
        }
    }

    public record StructureInfo(LOTRStructureBase2 structure, int posX, int posZ, int rotation) {
    }
}
