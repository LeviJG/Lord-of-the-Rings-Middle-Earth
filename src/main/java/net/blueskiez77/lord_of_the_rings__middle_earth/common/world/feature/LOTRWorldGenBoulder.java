package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRWorldGenBoulder: a boulder of a few rough spheres of one block, on level ground fit for
 * building (no more than {@code heightCheck} blocks between its highest and lowest point); grass
 * under it turns to dirt.
 */
public class LOTRWorldGenBoulder implements LOTRWorldGenerator {

    private final BlockState block;
    private final int minWidth;
    private final int maxWidth;
    private int heightCheck = 3;

    public LOTRWorldGenBoulder(BlockState block, int minWidth, int maxWidth) {
        this.block = block;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }

    public LOTRWorldGenBoulder(net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock block,
                               int meta, int minWidth, int maxWidth) {
        this(block.state(meta), minWidth, maxWidth);
    }

    public LOTRWorldGenBoulder setHeightCheck(int i) {
        this.heightCheck = i;
        return this;
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (!LOTRStructureBase2.isSurfaceStatic(world, i, j - 1, k)) {
            return false;
        }
        int boulderWidth = LOTRWorldGenUtil.getRandomIntegerInRange(random, this.minWidth, this.maxWidth);
        int highestHeight = j;
        int lowestHeight = j;
        for (int i1 = i - boulderWidth; i1 <= i + boulderWidth; ++i1) {
            for (int k1 = k - boulderWidth; k1 <= k + boulderWidth; ++k1) {
                int heightValue = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                if (!LOTRStructureBase2.isSurfaceStatic(world, i1, heightValue - 1, k1)) {
                    return false;
                }
                highestHeight = Math.max(highestHeight, heightValue);
                lowestHeight = Math.min(lowestHeight, heightValue);
            }
        }
        if (highestHeight - lowestHeight > this.heightCheck) {
            return false;
        }
        int spheres = 1 + random.nextInt(boulderWidth + 1);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int l = 0; l < spheres; ++l) {
            int posX = i + LOTRWorldGenUtil.getRandomIntegerInRange(random, -boulderWidth, boulderWidth);
            int posZ = k + LOTRWorldGenUtil.getRandomIntegerInRange(random, -boulderWidth, boulderWidth);
            int posY = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, posX, posZ);
            int sphereWidth = LOTRWorldGenUtil.getRandomIntegerInRange(random, this.minWidth, this.maxWidth);
            for (int i1 = posX - sphereWidth; i1 <= posX + sphereWidth; ++i1) {
                for (int j1 = posY - sphereWidth; j1 <= posY + sphereWidth; ++j1) {
                    for (int k1 = posZ - sphereWidth; k1 <= posZ + sphereWidth; ++k1) {
                        int i2 = i1 - posX;
                        int j2 = j1 - posY;
                        int k2 = k1 - posZ;
                        int dist = i2 * i2 + j2 * j2 + k2 * k2;
                        if (dist >= sphereWidth * sphereWidth
                                && (dist >= (sphereWidth + 1) * (sphereWidth + 1) || random.nextInt(3) != 0)) {
                            continue;
                        }
                        int j3 = j1;
                        while (j3 >= 0 && !world.getBlockState(pos.set(i1, j3 - 1, k1)).isSolidRender()) {
                            --j3;
                        }
                        world.setBlock(pos.set(i1, j3, k1), this.block, Block.UPDATE_CLIENTS);
                        // onPlantGrow: grass under the boulder turns to dirt.
                        if (world.getBlockState(pos.set(i1, j3 - 1, k1)).is(Blocks.GRASS_BLOCK)) {
                            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
        return true;
    }
}
