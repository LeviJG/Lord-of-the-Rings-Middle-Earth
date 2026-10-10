package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A LOTR sapling.
 *
 * <p>The 1.7.10 mod's LOTRBlockSaplingBase extended BlockSapling, so the modern
 * port extends SaplingBlock rather than registering a plain Block: that is what
 * brings the STAGE property, the "must stand on dirt" survival check, the random
 * tick that advances growth, and BonemealableBlock. SaplingBlock's own
 * constructor is protected, which is the only reason this subclass exists.
 *
 * <p>Its tree is the original's growTree (LOTRSaplingGrowth), not a vanilla
 * TreeGrower: the larger trees need their saplings planted in a square or a
 * cross, which TreeGrower cannot ask. The grower each is handed is a
 * placeholder that is never used.
 */
public class LOTRSaplingBlock extends SaplingBlock {
    public LOTRSaplingBlock(TreeGrower grower, BlockBehaviour.Properties props) {
        super(grower, props);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), Block.UPDATE_INVISIBLE);
        } else {
            LOTRSaplingGrowth.growTree(level, pos, state, random);
        }
    }

    // TreeGrower keys itself by name in a static map used by its codec, so each
    // sapling needs a distinct one.
    public static TreeGrower placeholderGrower(String name) {
        return new TreeGrower(name, Optional.empty(), Optional.empty(), Optional.empty());
    }
}
