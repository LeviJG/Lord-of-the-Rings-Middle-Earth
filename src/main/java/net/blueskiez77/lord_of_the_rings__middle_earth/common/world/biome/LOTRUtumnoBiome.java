package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSkullPile;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStalactites;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenUtumno: Utumno's one biome, black, rainless and foggy, peopled by its levels' creatures
 * (and, once in a thousand, by its guests). Its decoration is all its own: shafts and pits, bridges
 * and stairs over and between the corridors, pillars, the return portal's altars, barred windows
 * with fires behind, stalactites and skulls.
 */
public class LOTRUtumnoBiome extends LOTRBiome {
    public static final List<LOTRBiome> utumnoBiomes = new ArrayList<>();

    public final LOTRWorldGenStalactites stalactiteGen = new LOTRWorldGenStalactites(LOTRLegacyBlocks.mod("stalactite"));
    public final LOTRWorldGenStalactites stalactiteIceGen = new LOTRWorldGenStalactites(LOTRLegacyBlocks.mod("stalactiteIce"));
    public final LOTRWorldGenStalactites stalactiteObsidianGen = new LOTRWorldGenStalactites(LOTRLegacyBlocks.mod("stalactiteObsidian"));
    /** spawnableGuestList: Utumno's guests. */
    public final LOTRBiomeSpawnList spawnableGuestList = new LOTRBiomeSpawnList("UtumnoGuests");

    public LOTRUtumnoBiome(int i) {
        super(i, false, LOTRDimension.UTUMNO);
        utumnoBiomes.add(this);
        setDisableRain();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableLOTRAmbientList.clear();
        this.spawnableCaveCreatureList.clear();
        this.biomeColors.setGrass(0);
        this.biomeColors.setFoliage(0);
        this.biomeColors.setSky(0);
        this.biomeColors.setFoggy(true);
        this.biomeColors.setWater(0);
        this.spawnableGuestList.newFactionList(100).add(LOTRBiomeSpawnList.entry(
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList.UTUMNO_GUESTS, 10));
        LOTRUtumnoLevel.setupSpawnLists();
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.UTUMNO.getSubregion("utumno");
    }

    @Override
    public LOTRBiomeSpawnList getNPCSpawnList(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRBiomeVariant variant) {
        if (random.nextInt(1000) == 0) {
            return this.spawnableGuestList;
        }
        return LOTRUtumnoLevel.forY(j).getNPCSpawnList();
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        generateHoles(world, random, i, k);
        generatePits(world, random, i, k);
        generateBridges(world, random, i, k);
        generateStairs(world, random, i, k);
        generatePillars(world, random, i, k);
        generatePortalBases(world, random, i, k);
        generateBars(world, random, i, k);
        generateStalactites(world, random, i, k);
        generateSkulls(world, random, i, k);
    }

    private static BlockState get(BlockGetter world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k));
    }

    private static boolean isOpaque(BlockGetter world, int i, int j, int k) {
        return LOTRFeature.isOpaqueCube(get(world, i, j, k));
    }

    private static boolean isAir(BlockGetter world, int i, int j, int k) {
        return get(world, i, j, k).isAir();
    }

    private static void set(WorldGenLevel world, int i, int j, int k, BlockState state) {
        world.setBlock(new BlockPos(i, j, k), state, Block.UPDATE_CLIENTS);
    }

    public void generateBars(WorldGenLevel world, RandomSource random, int i, int k) {
        for (int l = 0; l < 200; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 4, 250);
            if (!isOpaque(world, i1, j1, k1)) {
                continue;
            }
            int barWidth = 1 + random.nextInt(3);
            int barHeight = 2 + random.nextInt(3);
            boolean fire = random.nextInt(3) == 0;
            int facingX = 0;
            int facingZ = 0;
            if (random.nextBoolean()) {
                facingX = random.nextBoolean() ? -1 : 1;
            } else {
                facingZ = random.nextBoolean() ? -1 : 1;
            }
            boolean generate = true;
            passes:
            for (int pass = 0; pass <= 1; ++pass) {
                for (int xz = 0; xz < barWidth; ++xz) {
                    for (int y = -1; y < barHeight + 1; ++y) {
                        int i2 = i1 + xz * facingZ;
                        int j2 = j1 + y;
                        int k2 = k1 + xz * facingX;
                        boolean flag = isOpaque(world, i2, j2, k2);
                        if (y >= 0 && y < barHeight && !isAir(world, i2 + facingX, j2, k2 + facingZ)) {
                            flag = false;
                        }
                        if (flag) {
                            continue;
                        }
                        if (pass == 0) {
                            generate = true;
                            int fx = facingX;
                            facingX = facingZ;
                            facingZ = fx;
                            continue passes;
                        }
                        generate = false;
                        break passes;
                    }
                }
            }
            if (!generate) {
                continue;
            }
            for (int xz = 0; xz < barWidth; ++xz) {
                for (int y = 0; y < barHeight; ++y) {
                    int i2 = i1 + xz * facingZ;
                    int j2 = j1 + y;
                    int k2 = k1 + xz * facingX;
                    set(world, i2, j2, k2, LOTRLegacyBlocks.mod("orcSteelBars").state(0));
                    int i3 = i2 - facingX;
                    int k3 = k2 - facingZ;
                    if (!fire || y != 0 || !isOpaque(world, i3, j2, k3)) {
                        continue;
                    }
                    set(world, i3, j2 - 1, k3, LOTRLegacyBlocks.mod("hearth").state(0));
                    set(world, i3, j2, k3, Blocks.FIRE.defaultBlockState());
                }
            }
        }
    }

    public void generateBridges(WorldGenLevel world, RandomSource random, int i, int k) {
        bridges:
        for (int l = 0; l < 20; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            LOTRUtumnoLevel utumnoLevel = LOTRUtumnoLevel.values()[random.nextInt(LOTRUtumnoLevel.values().length)];
            int j1 = utumnoLevel.corridorBaseLevels[random.nextInt(utumnoLevel.corridorBaseLevels.length)] - 1;
            int fuzz = 2;
            for (int j2 = j1 - fuzz; j2 <= j1 + fuzz; ++j2) {
                if (!isOpaque(world, i1, j2, k1) || !isAir(world, i1, j2 + 1, k1) || !isAir(world, i1 - 1, j2, k1)
                        && !isAir(world, i1 + 1, j2, k1) && !isAir(world, i1, j2, k1 - 1) && !isAir(world, i1, j2, k1 + 1)) {
                    continue;
                }
                int[] bridge = searchForBridge(world, i1, j2, k1, -1, 0);
                if (bridge == null && (bridge = searchForBridge(world, i1, j2, k1, 1, 0)) == null
                        && (bridge = searchForBridge(world, i1, j2, k1, 0, -1)) == null) {
                    bridge = searchForBridge(world, i1, j2, k1, 0, 1);
                }
                if (bridge == null) {
                    continue;
                }
                int xRange = bridge[0];
                int zRange = bridge[1];
                int startX = i1;
                int endX = i1;
                int startZ = k1;
                int endZ = k1;
                if (xRange >= 0) {
                    endX += xRange;
                } else {
                    startX -= -xRange;
                }
                if (zRange >= 0) {
                    endZ += zRange;
                } else {
                    startZ -= -zRange;
                }
                if (xRange == 0) {
                    int xWidth = random.nextInt(3);
                    startX -= xWidth;
                    endX += xWidth;
                }
                if (zRange == 0) {
                    int zWidth = random.nextInt(3);
                    startZ -= zWidth;
                    endZ += zWidth;
                }
                for (int x = startX; x <= endX; ++x) {
                    for (int z = startZ; z <= endZ; ++z) {
                        if (random.nextInt(8) == 0) {
                            continue;
                        }
                        set(world, x, j2, z, utumnoLevel.brick());
                    }
                }
                continue bridges;
            }
        }
    }

    public void generateHoles(WorldGenLevel world, RandomSource random, int i, int k) {
        for (int l = 0; l < 8; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 20, 240);
            if (!isAir(world, i1, j1, k1)) {
                continue;
            }
            LOTRUtumnoLevel level = LOTRUtumnoLevel.forY(j1);
            for (int j2 = j1; j2 >= level.corridorBaseLevels[0] - 1 && (!isAir(world, i1, j2, k1) || random.nextInt(10) != 0)
                    && LOTRUtumnoLevel.forY(j2 - 1) == level; --j2) {
                for (int i2 = i1 - 1; i2 <= i1; ++i2) {
                    for (int k2 = k1 - 1; k2 <= k1; ++k2) {
                        set(world, i2, j2, k2, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public void generatePillars(WorldGenLevel world, RandomSource random, int i, int k) {
        pillars:
        for (int l = 0; l < 40; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            LOTRUtumnoLevel utumnoLevel = LOTRUtumnoLevel.values()[random.nextInt(LOTRUtumnoLevel.values().length)];
            int j1 = utumnoLevel.corridorBaseLevels[random.nextInt(utumnoLevel.corridorBaseLevels.length)];
            int pillarHeight = LOTRWorldGenUtil.getRandomIntegerInRange(random, 1, utumnoLevel.corridorHeight);
            int fuzz = 2;
            for (int j2 = j1 - fuzz; j2 <= j1 + fuzz; ++j2) {
                if (isAir(world, i1, j2 - 1, k1)) {
                    continue;
                }
                boolean generated = false;
                for (int j3 = j2; j3 <= j2 + pillarHeight && isAir(world, i1, j3, k1); ++j3) {
                    set(world, i1, j3, k1, utumnoLevel.pillar());
                    generated = true;
                }
                if (generated) {
                    continue pillars;
                }
            }
        }
    }

    public void generatePits(WorldGenLevel world, RandomSource random, int i, int k) {
        if (random.nextInt(5) != 0) {
            return;
        }
        int i1 = i + 8 + random.nextInt(16);
        int k1 = k + 8 + random.nextInt(16);
        int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 20, 220);
        if (!isAir(world, i1, j1, k1)) {
            return;
        }
        int radius = 8 + random.nextInt(30);
        LOTRUtumnoLevel level = LOTRUtumnoLevel.forY(j1);
        int yMin = Math.max(j1 - radius, level.baseLevel + 5);
        int yMax = Math.min(j1 + radius, level.topLevel - 5);
        for (int i2 = i1 - radius; i2 <= i1 + radius; ++i2) {
            for (int j2 = yMin; j2 <= yMax; ++j2) {
                for (int k2 = k1 - radius; k2 <= k1 + radius; ++k2) {
                    int i3 = Math.abs(i2 - i1);
                    int j3 = Math.abs(j2 - j1);
                    int k3 = Math.abs(k2 - k1);
                    double dist = i3 * i3 + j3 * j3 + k3 * k3;
                    if (dist < (radius - 5) * (radius - 5)) {
                        set(world, i2, j2, k2, Blocks.AIR.defaultBlockState());
                        continue;
                    }
                    if (dist >= radius * radius || random.nextInt(6) != 0) {
                        continue;
                    }
                    set(world, i2, j2, k2, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    /** One altar a chunk, on a corridor floor: most often on the fire level, least on the ice. */
    public void generatePortalBases(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1 = i + 8 + random.nextInt(16);
        int k1 = k + 8 + random.nextInt(16);
        float f = random.nextFloat();
        LOTRUtumnoLevel utumnoLevel = f < 0.15f ? LOTRUtumnoLevel.ICE : f < 0.5f ? LOTRUtumnoLevel.OBSIDIAN : LOTRUtumnoLevel.FIRE;
        int j1 = utumnoLevel.corridorBaseLevels[random.nextInt(utumnoLevel.corridorBaseLevels.length)];
        int fuzz = 2;
        for (int j2 = j1 - fuzz; j2 <= j1 + fuzz; ++j2) {
            BlockPos below = new BlockPos(i1, j2 - 1, k1);
            if (!isAir(world, i1, j2, k1) || !world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)) {
                continue;
            }
            set(world, i1, j2, k1, net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks.UTUMNO_RETURN_PORTAL_BASE
                    .defaultBlockState());
            return;
        }
    }

    public void generateSkulls(WorldGenLevel world, RandomSource random, int i, int k) {
        for (int l = 0; l < 4; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 4, 250);
            new LOTRWorldGenSkullPile().generate(world, random, i1, j1, k1);
        }
    }

    public void generateStairs(WorldGenLevel world, RandomSource random, int i, int k) {
        stairs:
        for (int l = 0; l < 8; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            LOTRUtumnoLevel utumnoLevel = LOTRUtumnoLevel.values()[random.nextInt(LOTRUtumnoLevel.values().length)];
            int j1 = utumnoLevel.corridorBaseLevels[1 + random.nextInt(utumnoLevel.corridorBaseLevels.length - 1)] - 1;
            int fuzz = 2;
            for (int j2 = j1 - fuzz; j2 <= j1 + fuzz; ++j2) {
                if (!isOpaque(world, i1, j2, k1) || !isAir(world, i1, j2 + 1, k1)) {
                    continue;
                }
                int xDirection = 0;
                int zDirection = 0;
                int stairMeta;
                if (random.nextBoolean()) {
                    xDirection = random.nextBoolean() ? 1 : -1;
                    stairMeta = xDirection > 0 ? 1 : 0;
                } else {
                    zDirection = random.nextBoolean() ? 1 : -1;
                    stairMeta = zDirection > 0 ? 3 : 2;
                }
                int stairX = i1;
                int stairY = j2;
                int stairZ = k1;
                int minStairRange = 6;
                int maxStairRange = 20;
                int stairWidth = 1 + random.nextInt(3);
                int stairHeight = stairWidth + 2;
                int stair = 0;
                do {
                    for (int w = 0; w < stairWidth; ++w) {
                        int i2 = stairX + w * zDirection;
                        int k2 = stairZ + w * xDirection;
                        set(world, i2, stairY, k2, utumnoLevel.brickStair(stairMeta));
                        if (isAir(world, i2, stairY - 1, k2)) {
                            set(world, i2, stairY - 1, k2, utumnoLevel.brickStair(stairMeta ^ 1 | 4));
                        }
                        for (int j3 = stairY + 1; j3 <= stairY + stairHeight; ++j3) {
                            set(world, i2, j3, k2, Blocks.AIR.defaultBlockState());
                        }
                    }
                    if (++stair >= maxStairRange || stair >= minStairRange && random.nextInt(10) == 0) {
                        continue stairs;
                    }
                    stairX += xDirection;
                    stairZ += zDirection;
                } while (--stairY > utumnoLevel.corridorBaseLevels[0]);
                continue stairs;
            }
        }
    }

    public void generateStalactites(WorldGenLevel world, RandomSource random, int i, int k) {
        for (int l = 0; l < 2; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, LOTRUtumnoLevel.ICE.baseLevel, LOTRUtumnoLevel.ICE.topLevel);
            if (random.nextBoolean()) {
                this.stalactiteGen.generate(world, random, i1, j1, k1);
            } else {
                this.stalactiteIceGen.generate(world, random, i1, j1, k1);
            }
        }
        for (int l = 0; l < 2; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, LOTRUtumnoLevel.OBSIDIAN.baseLevel, LOTRUtumnoLevel.OBSIDIAN.topLevel);
            if (random.nextBoolean()) {
                this.stalactiteGen.generate(world, random, i1, j1, k1);
            } else {
                this.stalactiteObsidianGen.generate(world, random, i1, j1, k1);
            }
        }
        for (int l = 0; l < 2; ++l) {
            int i1 = i + 8 + random.nextInt(16);
            int k1 = k + 8 + random.nextInt(16);
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, LOTRUtumnoLevel.FIRE.baseLevel, LOTRUtumnoLevel.FIRE.topLevel);
            this.stalactiteObsidianGen.generate(world, random, i1, j1, k1);
        }
    }

    /** searchForBridge: a span of air from this ledge to another, long enough for the level's corridor. */
    public int[] searchForBridge(BlockGetter world, int i, int j, int k, int xDirection, int zDirection) {
        LOTRUtumnoLevel utumnoLevel = LOTRUtumnoLevel.forY(j);
        int maxBridgeLength = 16;
        int minBridgeLength = 2 + utumnoLevel.corridorWidth / 2;
        int foundAir = 0;
        int foundBrick = 0;
        int x = 0;
        int z = 0;
        while (Math.abs(x) < maxBridgeLength && Math.abs(z) < maxBridgeLength) {
            x += xDirection;
            z += zDirection;
            int i1 = i + x;
            int k1 = k + z;
            if (foundAir == 0 && isAir(world, i1, j, k1)) {
                if (xDirection == 0) {
                    foundAir = z;
                } else if (zDirection == 0) {
                    foundAir = x;
                }
            }
            if (foundAir == 0 || !isOpaque(world, i1, j, k1)) {
                continue;
            }
            foundBrick = xDirection == 0 ? z : x;
            break;
        }
        if (foundBrick != 0 && Math.abs(foundBrick - foundAir) >= minBridgeLength) {
            return new int[]{x, z};
        }
        return null;
    }
}
