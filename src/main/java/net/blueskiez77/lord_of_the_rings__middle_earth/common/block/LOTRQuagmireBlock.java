package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRBlockQuagmire: boggy ground you sink into. No collision and no
 * occlusion (set at registration), and whatever is inside it is caught as in a
 * cobweb -- the original called entity.setInWeb(), which is makeStuckInBlock
 * with cobweb's own vector.
 *
 * <p>The original let LOTR spiders cross freely (setInQuag); that waits for
 * the spiders themselves (Track D).
 */
public class LOTRQuagmireBlock extends Block {
    public static final MapCodec<LOTRQuagmireBlock> CODEC = simpleCodec(LOTRQuagmireBlock::new);

    private static final Vec3 STUCK_SPEED = new Vec3(0.25, 0.05, 0.25);

    public LOTRQuagmireBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends LOTRQuagmireBlock> codec() {
        return CODEC;
    }

    // WebBlock's signature: entityInside gained a trailing boolean in 26.2.
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        // setInWeb() had no modifiers, so WebBlock's Weaving branch is not copied.
        entity.makeStuckInBlock(state, STUCK_SPEED);
    }
}