package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/** LOTRBiomeVariantOrchard: rows of fruit trees, two to a chunk, between rows of fence, away from the roads. */
public class LOTROrchardBiomeVariant extends LOTRBiomeVariant {

    public LOTROrchardBiomeVariant(int i, String s) {
        super(i, s, VariantScale.SMALL);
        setTemperatureRainfall(0.1f, 0.2f);
        setHeight(0.0f, 0.4f);
        setTrees(0.0f);
        setGrass(0.5f);
        disableStructuresVillages();
    }

    @Override
    public void decorateVariant(WorldGenLevel world, RandomSource random, int i, int k, LOTRBiome biome) {
        // The original's k + 8, between the fence rows: the port's populate corner is 8 blocks back
        // (each chunk decorates its own square), so the row is k + 16 here.
        for (int i1 : new int[]{i + 3, i + 11}) {
            int k1 = k + 16;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
            getRandomTree(random).create(false, random).generate(world, random, i1, j1, k1);
        }
    }

    /**
     * The fence rows laid with the terrain, joined up: 1.7.10 drew a fence joined to what stood
     * beside it, where 26.2 keeps that in the block, set once the chunks about it are there. A row
     * runs along each chunk's first column of z (k mod 16 == 0); x0, z0 is the chunk's corner.
     */
    public static void connectFences(WorldGenLevel world, LOTRWorldChunkManager chunkManager, int x0, int z0) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = x0; x < x0 + 16; ++x) {
            if (!(chunkManager.getBiomeVariantAt(x, z0) instanceof LOTROrchardBiomeVariant)) {
                continue;
            }
            for (int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z0) - 1; y > world.getMinY(); --y) {
                BlockState state = world.getBlockState(pos.set(x, y, z0));
                if (state.is(Blocks.OAK_FENCE)) {
                    world.setBlock(pos, Block.updateFromNeighbourShapes(state, world, pos), Block.UPDATE_CLIENTS);
                    break;
                }
                if (LOTRChunkTerrain.isOpaque(state)) {
                    break;
                }
            }
        }
    }

    @Override
    public void generateVariantTerrain(LOTRChunkTerrain terrain, RandomSource random, int i, int k, int height, LOTRBiome biome) {
        int xzIndex = (i & 0xF) * 16 + (k & 0xF);
        if (LOTRRoads.isRoadAt(i, k)) {
            return;
        }
        for (int j = 128; j >= 0; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState above = terrain.blocks[index + 1];
            BlockState block = terrain.blocks[index];
            if (!LOTRChunkTerrain.isOpaque(block) || !above.isAir()) {
                continue;
            }
            int i1 = Mth.positiveModulo(i, 32);
            int k1 = Mth.positiveModulo(k, 16);
            if (i1 == 6 || i1 == 7 || i1 == 8 || k1 != 0) {
                break;
            }
            terrain.blocks[index + 1] = Blocks.OAK_FENCE.defaultBlockState();
            break;
        }
    }
}
