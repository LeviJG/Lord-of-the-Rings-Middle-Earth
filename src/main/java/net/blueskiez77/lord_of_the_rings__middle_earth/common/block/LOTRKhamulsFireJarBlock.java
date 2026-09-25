package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockRhunFireJar, the block the original called "Khamûl's Fire": a sealed
 * jar of Rhûnic fire that scatters flames when it breaks.
 *
 * <p>It is a falling block, and almost anything sets it off -- flint and steel,
 * a redstone signal, another explosion, being walked into hard enough, or fire
 * appearing anywhere near it. The explosion itself is small (power 2, no fire of
 * its own); what makes it dangerous is what follows: sixty-four attempts to drop
 * a patch of {@link LOTRKhamulsFireBlock} somewhere in the five-block cube
 * around it.
 */
public class LOTRKhamulsFireJarBlock extends FallingBlock {
    public static final MapCodec<LOTRKhamulsFireJarBlock> CODEC =
            simpleCodec(LOTRKhamulsFireJarBlock::new);

    /** setBlockBounds(0.125, 0, 0.125, 0.875, 1, 0.875). */
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    /** createExplosion(null, i, j, k, 2.0f, false). */
    private static final float EXPLOSION_POWER = 2.0f;

    /** Sixty-four tries to seed fire, each up to two blocks away on every axis. */
    private static final int FIRE_ATTEMPTS = 64;
    private static final int FIRE_RANGE = 2;

    /** How far a going-off jar reaches to set its neighbours off. */
    private static final int CHAIN_RANGE = 3;

    public LOTRKhamulsFireJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<LOTRKhamulsFireJarBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
            BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**
     * explode: a small blast, the scattered fire, and every other jar it
     * touches going up with it.
     *
     * <p>Three things worth spelling out, two of which differ from the original.
     *
     * <p>The blast does NOT break blocks. 1.7.10's createExplosion(entity, x, y,
     * z, 2.0f, false) passes isFlaming false and leaves isSmoking true, so the
     * original's did break them; leaving terrain alone is a deliberate change.
     * Entities are still hurt -- ExplosionInteraction.NONE only spares the
     * blocks.
     *
     * <p>Which means the chain has to be explicit. The original got it for free:
     * its blast destroyed neighbouring jars, each of which then ran
     * onBlockExploded and went off in turn. With nothing breaking, that path is
     * gone, so the jars in range are gathered and worked through here instead --
     * iteratively, with a set of what has already gone, so a wall of them
     * cascades without recursing.
     */
    public void explode(Level level, BlockPos origin) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }

        Deque<BlockPos> pending = new ArrayDeque<>();
        Set<BlockPos> done = new HashSet<>();
        pending.add(origin.immutable());

        while (!pending.isEmpty()) {
            BlockPos pos = pending.poll();
            if (!done.add(pos) || !server.getBlockState(pos).is(this)) {
                continue;
            }

            server.removeBlock(pos, false);
            server.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    EXPLOSION_POWER, Level.ExplosionInteraction.NONE);
            scatterFire(server, pos);

            // Anything else of ours the blast reached goes too.
            for (BlockPos other : BlockPos.betweenClosed(
                    pos.offset(-CHAIN_RANGE, -CHAIN_RANGE, -CHAIN_RANGE),
                    pos.offset(CHAIN_RANGE, CHAIN_RANGE, CHAIN_RANGE))) {
                if (!done.contains(other) && server.getBlockState(other).is(this)) {
                    pending.add(other.immutable());
                }
            }
        }
    }

    /** The 64 attempts to seed fire in the five-block cube around a broken jar. */
    private void scatterFire(ServerLevel server, BlockPos pos) {
        RandomSource random = server.getRandom();
        for (int attempt = 0; attempt < FIRE_ATTEMPTS; attempt++) {
            BlockPos target = pos.offset(
                    Mth.randomBetweenInclusive(random, -FIRE_RANGE, FIRE_RANGE),
                    Mth.randomBetweenInclusive(random, -FIRE_RANGE, FIRE_RANGE),
                    Mth.randomBetweenInclusive(random, -FIRE_RANGE, FIRE_RANGE));
            BlockState there = server.getBlockState(target);
            // Air or anything replaceable, and never in a liquid.
            if ((!there.isAir() && !there.canBeReplaced()) || !there.getFluidState().isEmpty()) {
                continue;
            }
            server.setBlock(target, LOTRDecorationBlocks.KHAMULS_FIRE.defaultBlockState(), 3);
        }
    }

    /**
     * func_149828_a, which is BlockFalling's "it has landed": a jar that falls
     * and hits the ground breaks on impact.
     *
     * <p>This is half the point of the jar being a falling block at all: knock
     * the support out from under one and it goes off where it lands, rather than
     * settling quietly.
     */
    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaced,
            net.minecraft.world.entity.item.FallingBlockEntity falling) {
        super.onLand(level, pos, state, replaced, falling);
        explode(level, pos);
    }

    /** onBlockActivated: flint and steel, and only that. */
    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (!held.is(Items.FLINT_AND_STEEL)) {
            return InteractionResult.PASS;
        }
        explode(level, pos);
        return InteractionResult.SUCCESS;
    }

    /** explodeOnAdded: cleared while a dispenser sets a jar down. */
    public static boolean explodeOnPlace = true;

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            if (!explodeOnPlace) {
                return;
            }
            explode(level, pos);
            return;
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbourBlock,
            Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighbourBlock, orientation, movedByPiston);
        if (level.getBlockState(pos).is(this) && level.hasNeighborSignal(pos)) {
            explode(level, pos);
        }
    }

    /** onBlockExploded: a jar caught in a blast adds its own. */
    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion,
            java.util.function.BiConsumer<ItemStack, BlockPos> dropConsumer) {
        explode(level, pos);
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    /**
     * onEntityCollidedWithBlock: walking into it gently is fine; running or
     * falling into it is not. The threshold is a fresh roll between 0.3 and 0.8
     * each time, so it is a gamble rather than a fixed speed.
     */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
            InsideBlockEffectApplier effects, boolean flag) {
        double speed = entity.getDeltaMovement().length();
        if (speed >= Mth.randomBetween(level.getRandom(), 0.3f, 0.8f)) {
            explode(level, pos);
        }
    }

    /**
     * updateTick: fire anywhere within a few blocks sets it off. Twelve looks,
     * each one to four blocks out.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (int look = 0; look < 12; look++) {
            int range = 1 + random.nextInt(4);
            BlockPos target = pos.offset(
                    Mth.randomBetweenInclusive(random, -range, range),
                    Mth.randomBetweenInclusive(random, -range, range),
                    Mth.randomBetweenInclusive(random, -range, range));
            BlockState there = level.getBlockState(target);
            if (there.is(net.minecraft.tags.BlockTags.FIRE)
                    || there.is(LOTRDecorationBlocks.KHAMULS_FIRE)
                    || there.getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                explode(level, pos);
                return;
            }
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    public int getDustColor(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return state.getMapColor(level, pos).col;
    }
}
