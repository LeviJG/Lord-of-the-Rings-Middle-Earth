package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenTrollHoard extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int i1;
        int k1;
        int j1;
        int l;
        int height = getTopSolidOrLiquidBlock(world, i, k);
        int maxCaveHeight = height - 4;
        int chests = 2 + random.nextInt(5);
        int chestsGenerated = 0;
        block0:
        for (l = 0; l < 64; ++l) {
            i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(random, -3, 3);
            j1 = j + LOTRWorldGenUtil.getRandomIntegerInRange(random, -3, 3);
            k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(random, -3, 3);
            if (j1 > maxCaveHeight || !isAirBlock(world, i1, j1, k1) || !getBlock(world, i1, j1 - 1, k1).is(net.minecraft.world.level.block.Blocks.STONE)) {
                continue;
            }
            LegacyBlock treasureBlock = random.nextInt(5) == 0 ? LOTRLegacyBlocks.mod("treasureCopper") : random.nextBoolean() ? LOTRLegacyBlocks.mod("treasureSilver") : LOTRLegacyBlocks.mod("treasureGold");
            int top = j1 + random.nextInt(3);
            for (int j2 = j1; j2 <= top; ++j2) {
                int treasureMeta = 7;
                if (j2 == top) {
                    treasureMeta = random.nextInt(7);
                }
                if (!isAirBlock(world, i1, j2, k1)) {
                    continue block0;
                }
                setBlockAndNotifyAdequately(world, i1, j2, k1, treasureBlock, treasureMeta);
            }
        }
        for (l = 0; l < 48; ++l) {
            i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(random, -8, 8);
            j1 = j + LOTRWorldGenUtil.getRandomIntegerInRange(random, -2, 2);
            k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(random, -8, 8);
            if (j1 > maxCaveHeight || !isAirBlock(world, i1, j1, k1) || !getBlock(world, i1, j1 - 1, k1).is(net.minecraft.world.level.block.Blocks.STONE)) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("chest"), 0);
            net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents.fillChest(world, random, new net.minecraft.core.BlockPos(i1, j1, k1), net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents.TROLL_HOARD, -1);
            if (getBiome(world, i1, k1) instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTREttenmoorsBiome && random.nextInt(5) == 0) {
                net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents.fillChest(world, random, new net.minecraft.core.BlockPos(i1, j1, k1), net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents.TROLL_HOARD_ETTENMOORS, -1);
            }
            chestsGenerated++;
            if (chestsGenerated >= chests) {
                break;
            }
        }
        return chestsGenerated > 0;
    }
}
