package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRWorldGenRammasEchor: the wall about the Pelennor, 500 blocks around Minas Tirith, Gondor brick to height 85 with a wooden walk along its top, torches and fenced gates where the roads pass. Built by the Pelennor as its chunks are decorated.
 */
public class LOTRRammasEchorStructure extends LOTRStructureBase2 {

    public static final LOTRRammasEchorStructure INSTANCE = new LOTRRammasEchorStructure(false);
    private final int centreX;
    private final int centreZ;
    private final int radius = 500;
    private final int radiusSq = radius * radius;
    private final double wallThick = 0.03;
    private final int wallTop = 85;
    private final int gateBottom = 77;
    private final int gateTop = 82;

    public LOTRRammasEchorStructure(boolean flag) {
        super(flag);
        this.centreX = LOTRWaypoint.MINAS_TIRITH.xCoord;
        this.centreZ = LOTRWaypoint.MINAS_TIRITH.zCoord;
    }

    /** Called for each chunk (i, k) of the biome as it is decorated; builds the wall's part in it. */
    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        if (isPosInWall(i + 8, k + 8) >= this.wallThick * 3.0) {
            return true;
        }
        for (int i1 = i; i1 <= i + 15; ++i1) {
            column:
            for (int k1 = k; k1 <= k + 15; ++k1) {
                double circleDist = isPosInWall(i1, k1);
                if (circleDist >= this.wallThick) {
                    continue;
                }
                float roadNear = LOTRRoads.isRoadNear(i1, k1, 9);
                boolean gate = roadNear >= 0.0f;
                boolean fences = false;
                boolean wallEdge = circleDist > 0.025;
                for (int j1 = this.wallTop; j1 > 0; --j1) {
                    if (fences) {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("fence"), 0);
                    } else {
                        if (j1 >= this.wallTop && wallEdge) {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick2"), 11);
                        } else if (j1 == this.wallTop && circleDist < 0.015) {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wooden_slab"), 0);
                        } else {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 1);
                        }
                        if (wallEdge && j1 == this.wallTop) {
                            setBlockAndMetadata(world, i1, j1 + 1, k1, LOTRLegacyBlocks.mod("brick2"), 11);
                            if (Mth.positiveModulo(i1 + k1, 2) == 1) {
                                setBlockAndMetadata(world, i1, j1 + 2, k1, LOTRLegacyBlocks.mod("slabSingle5"), 3);
                            } else if (isTorchAt(i1, k1)) {
                                setBlockAndMetadata(world, i1, j1 + 2, k1, LOTRLegacyBlocks.vanilla("fence"), 0);
                                setBlockAndMetadata(world, i1, j1 + 3, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
                            }
                        }
                    }
                    BlockState below = getBlockState(world, i1, j1 - 1, k1);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    if (below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.STONE)) {
                        continue column;
                    }
                    if (!gate) {
                        continue;
                    }
                    if (fences) {
                        if (j1 == this.gateBottom) {
                            continue column;
                        }
                        continue;
                    }
                    int lerpGateTop = this.gateBottom + Math.round((this.gateTop - this.gateBottom) * Mth.sqrt(1.0f - roadNear));
                    if (j1 != lerpGateTop) {
                        continue;
                    }
                    if (circleDist <= 0.025) {
                        continue column;
                    }
                    fences = true;
                }
            }
        }
        return true;
    }

    @Override
    public int getX(int x, int z) {
        return x;
    }

    @Override
    public int getY(int y) {
        return y;
    }

    @Override
    public int getZ(int x, int z) {
        return z;
    }

    public double isPosInWall(int i, int k) {
        int dx = i - this.centreX;
        int dz = k - this.centreZ;
        int distSq = dx * dx + dz * dz;
        return Math.abs((double) distSq / this.radiusSq - 1.0);
    }

    public boolean isTorchAt(int i, int k) {
        int torchRange = 12;
        return Mth.positiveModulo(Mth.positiveModulo(i, torchRange) + Mth.positiveModulo(k, torchRange), torchRange) == 0;
    }
}
