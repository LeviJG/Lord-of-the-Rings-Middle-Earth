package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.Optional;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A LOTR sapling.
 *
 * <p>The 1.7.10 mod's LOTRBlockSaplingBase extended BlockSapling, so the modern
 * port extends SaplingBlock rather than registering a plain Block: that is what
 * brings the STAGE property, the "must stand on dirt" survival check, the random
 * tick that advances growth, and BonemealableBlock. SaplingBlock's own
 * constructor is protected, which is the only reason this subclass exists.
 *
 * <p>Growing is NOT wired up yet. Each sapling is handed its own TreeGrower with
 * no configured features, which is safe: TreeGrower.growTree resolves its feature
 * with Optional.orElse(null) and returns false when there is none, so the sapling
 * simply stays put on a random tick or when bone-mealed. When the tree features
 * are ported, replace {@link #placeholderGrower} with the real grower for each
 * species -- nothing else here needs to change.
 */
public class LOTRSaplingBlock extends SaplingBlock {
    public LOTRSaplingBlock(TreeGrower grower, BlockBehaviour.Properties props) {
        super(grower, props);
    }

    // TreeGrower keys itself by name in a static map used by its codec, so each
    // sapling needs a distinct one.
    public static TreeGrower placeholderGrower(String name) {
        return new TreeGrower(name, Optional.empty(), Optional.empty(), Optional.empty());
    }
}
