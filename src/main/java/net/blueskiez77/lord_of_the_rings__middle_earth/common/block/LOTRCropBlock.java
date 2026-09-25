package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

// Vanilla CropBlock.mayPlaceOn tests state.is(Blocks.FARMLAND) -- the specific
// vanilla block, not a tag. So on mud farmland every LOTR crop would pop off the
// instant it was planted. This accepts any FarmlandBlock instead.
public class LOTRCropBlock extends CropBlock {

    // No CODEC or codec() override: CropBlock declares codec() with an exact
    // type this cannot narrow, and the inherited one is only used for datapack
    // block definitions, which this mod does not use.
    public LOTRCropBlock(Properties properties) {
        super(properties);
    }

    /**
     * The item each crop is planted from and drops -- func_149866_i and
     * func_149865_P on the original's crop blocks. Items register after blocks,
     * so LOTRItems fills this in; a crop with no entry keeps vanilla's wheat seeds.
     */
    private static final Map<Block, ItemLike> SEEDS = new HashMap<>();

    public static void setSeed(Block crop, ItemLike seed) {
        SEEDS.put(crop, seed);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        ItemLike seed = SEEDS.get(this);
        return seed != null ? seed : super.getBaseSeedId();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }

    // LOTRBlockPipeweedCrop.randomDisplayTick: a ripe crop smokes like the wild plant.
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (this == LOTRUtilityBlocks.PIPEWEED_CROP && isMaxAge(state)) {
            LOTRPlantBlock.pipeweedSmoke(level, pos, random);
        }
    }
}
