package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRhudaurCastleStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickCrackedBlock;
    public int brickCrackedMeta;
    public LegacyBlock brickCrackedSlabBlock;
    public int brickCrackedSlabMeta;

    public LOTRRhudaurCastleStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int chestY;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        int width = Mth.randomBetweenInclusive(random, 6, 15);
        int height = Mth.randomBetweenInclusive(random, 3, 8);
        for (int i1 = -width; i1 <= width; ++i1) {
            for (int k1 = -width; k1 <= width; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 == width || k2 == width) {
                    int j12;
                    float f = Mth.randomBetween(random, 0.7f, 1.0f);
                    int h = Math.round(height * f);
                    int b = 1;
                    if (k1 == -width && i2 <= 1) {
                        b = 4;
                    }
                    if (Math.floorMod(i2 + k2, 2) == Math.floorMod(width, 2)) {
                        ++h;
                    }
                    int top = getTopBlock(world, i1, k1) - 1;
                    boolean foundSurface = false;
                    for (j12 = top; j12 >= top - 16; --j12) {
                        if (!isSurface(world, i1, j12, k1)) {
                            continue;
                        }
                        foundSurface = true;
                        break;
                    }
                    if (!foundSurface) {
                        continue;
                    }
                    for (int j2 = b; j2 <= h; ++j2) {
                        boolean low;
                        boolean cracked;
                        int j3 = j12 + j2;
                        low = j2 < (int) (height * 0.5f) && j2 < h;
                        if (low && random.nextInt(40) == 0) {
                            continue;
                        }
                        boolean slab = low && random.nextInt(20) == 0 || j2 == h && random.nextInt(5) == 0;
                        cracked = random.nextInt(4) == 0;
                        if (cracked) {
                            if (slab) {
                                setBlockAndMetadata(world, i1, j3, k1, brickCrackedSlabBlock, brickCrackedSlabMeta);
                            } else {
                                setBlockAndMetadata(world, i1, j3, k1, brickCrackedBlock, brickCrackedMeta);
                            }
                        } else if (slab) {
                            setBlockAndMetadata(world, i1, j3, k1, brickSlabBlock, brickSlabMeta);
                        } else {
                            setBlockAndMetadata(world, i1, j3, k1, brickBlock, brickMeta);
                        }
                        if (j2 != 1) {
                            continue;
                        }
                        setGrassToDirt(world, i1, j3 - 1, k1);
                    }
                    continue;
                }
                if (random.nextInt(16) == 0 && isSurface(world, i1, j1 = getTopBlock(world, i1, k1) - 1, k1)) {
                    if (random.nextInt(3) == 0) {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("gravel"), 0);
                    } else {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                    }
                }
                if (random.nextInt(50) != 0 || !isSurface(world, i1, j1 = getTopBlock(world, i1, k1) - 1, k1)) {
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    setBlockAndMetadata(world, i1, j1 + 1, k1, brickCrackedSlabBlock, brickCrackedSlabMeta);
                } else {
                    setBlockAndMetadata(world, i1, j1 + 1, k1, brickSlabBlock, brickSlabMeta);
                }
                setGrassToDirt(world, i1, j1, k1);
            }
        }
        int chestX = width - 1;
        int chestZ = width - 1;
        if (random.nextBoolean()) {
            chestX *= -1;
        }
        if (random.nextBoolean()) {
            chestZ *= -1;
        }
        if (isSurface(world, chestX, (chestY = getTopBlock(world, chestX, chestZ)) - 1, chestZ)) {
            int chestMeta = LOTRStructureBase2.DIRECTION_TO_FACING[random.nextInt(4)];
            setBlockAndMetadata(world, chestX, chestY, chestZ, LOTRLegacyBlocks.mod("chestStone"), chestMeta);
            fillChest(world, random, chestX, chestY, chestZ, LOTRChestContents.RUINED_HOUSE, 5);
            fillChest(world, random, chestX, chestY, chestZ, LOTRChestContents.ORC_DUNGEON, 4);
            fillChest(world, random, chestX, chestY, chestZ, LOTRChestContents.DUNEDAIN_TOWER, 4);
        }
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        if (random.nextBoolean()) {
            brickBlock = LOTRLegacyBlocks.vanilla("stonebrick");
            brickMeta = 0;
            brickSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
            brickSlabMeta = 5;
            brickCrackedBlock = LOTRLegacyBlocks.vanilla("stonebrick");
            brickCrackedMeta = 2;
            brickCrackedSlabBlock = LOTRLegacyBlocks.mod("slabSingleV");
            brickCrackedSlabMeta = 1;
        } else {
            brickBlock = LOTRLegacyBlocks.mod("brick2");
            brickMeta = 0;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle3");
            brickSlabMeta = 3;
            brickCrackedBlock = LOTRLegacyBlocks.mod("brick2");
            brickCrackedMeta = 1;
            brickCrackedSlabBlock = LOTRLegacyBlocks.mod("slabSingle3");
            brickCrackedSlabMeta = 4;
        }
    }
}
