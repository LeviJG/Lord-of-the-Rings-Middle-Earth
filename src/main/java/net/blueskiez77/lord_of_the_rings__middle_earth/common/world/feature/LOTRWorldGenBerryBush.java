package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenBerryBush: a patch of one kind of berry bush, in fruit; the poisonous wildberry half as often. */
public class LOTRWorldGenBerryBush extends LOTRFeature {

    /** LOTRBlockBerryBush.BushType: its metadata, and whether its berries are poisonous. */
    private static final int[] BUSH_METAS = {0, 1, 2, 3, 4, 5};
    private static final int WILDBERRY = 5;

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LegacyBlock bush = LOTRLegacyBlocks.mod("berryBush");
        int bushType = BUSH_METAS[random.nextInt(BUSH_METAS.length)];
        int bushMeta = bushType | 8;
        if (bushType == WILDBERRY && random.nextInt(2) != 0) {
            return false;
        }
        for (int l = 0; l < 12; ++l) {
            int i1 = i - random.nextInt(4) + random.nextInt(4);
            int j1 = j - random.nextInt(2) + random.nextInt(2);
            int k1 = k - random.nextInt(4) + random.nextInt(4);
            BlockState block = getBlock(world, i1, j1, k1);
            if (!canSustainPlant(world, i1, j1 - 1, k1) || isLiquid(block) || !isBlockReplaceable(block)) {
                continue;
            }
            setBlock(world, i1, j1, k1, bush, bushMeta, 2);
        }
        return true;
    }
}
