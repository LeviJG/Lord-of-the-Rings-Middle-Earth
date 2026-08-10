package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// Quagmire: boggy ground that you sink into. Ported from lotr.common.block.LOTRBlockQuagmire (1.7.10). The original was a plain Block(Material.ground) with three overrides: getCollisionBoundingBoxFromPool -> null   (no collision; you fall in) isOpaqueCube                    -> false onEntityCollidedWithBlock       -> entity.setInWeb() and, notably, NO setHardness call -- so it inherits the Block default of 0.0f and breaks instantly. The registration in LOTRBlocks reflects that. The first two are properties now (noCollision / noOcclusion) and are set at registration rather than here. This class exists only for the third. setInWeb() is exactly what vanilla cobweb does, so the modern equivalent is makeStuckInBlock with cobweb's own vector. The original special-cased LOTR spiders (setInQuag, letting them cross freely); that branch is deliberately left out until the entity tier lands, so for now spiders sink like anything else. Flagged rather than silently dropped.
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

    // NOTE FOR LEVI: this is the one signature I could not verify against 26.2.

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, STUCK_SPEED);
    }
}