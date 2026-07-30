package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Chandelier: a light fixture that hangs from the ceiling.
 *
 * Ported from lotr.common.block.LOTRBlockChandelier (1.7.10). The original was
 * a single block with 16 metadata variants; here each variant is its own block
 * (the modern idiom), so the only per-variant state left is which particles it
 * emits.
 *
 * Behaviour carried over from the original:
 *   - setBlockBounds(0.0625, 0.1875, 0.0625, 0.9375, 1.0, 0.9375)
 *       -> SHAPE below, i.e. box(1, 3, 1, 15, 16, 15)
 *   - getCollisionBoundingBoxFromPool() returned null -> no collision at all,
 *     you walk straight through it. Set via .noCollission() on the properties
 *     AND getCollisionShape() here, so the outline still highlights correctly.
 *   - canBlockStay() checked the block ABOVE for a down-facing solid surface
 *     (or a fence / wall / bottom slab / bottom stairs / orc chain)
 *   - onNeighborBlockChange() dropped the block when that support vanished
 *   - getRenderType() == 1 -> crossed squares, NOT a 3D lantern model. The
 *     matching model is assets/lotr/models/block/chandelier.json.
 *   - randomDisplayTick() spawned particles at four points around the fixture
 */
public class LOTRChandelierBlock extends Block {

    public static final MapCodec<LOTRChandelierBlock> CODEC = simpleCodec(
            props -> new LOTRChandelierBlock(ParticleStyle.FLAME, props));

    /** 1/16..15/16 on X and Z, 3/16..16/16 on Y: hangs from the ceiling. */
    private static final VoxelShape SHAPE = Block.box(1.0, 3.0, 1.0, 15.0, 16.0, 15.0);

    /**
     * Which particles the fixture gives off, matching the original's metadata
     * switch in spawnChandelierParticles().
     *
     * FLAME is the original's default branch (smoke + flame) and covers the
     * plain metal variants. The four MALLORN_*, WOOD_ELVEN, HIGH_ELVEN and
     * MORGUL entries stood for custom particles the 1.7.10 mod registered
     * itself ("elvenGlow", "morgulPortal", "leafRed_*", LOTRBlockTorch's torch
     * particles). Those do not exist yet in this port, so each maps to the
     * closest vanilla stand-in for now -- swap them out once the mod's own
     * particle types are registered.
     */
    public enum ParticleStyle {
        /** Original default: smoke + flame. */
        FLAME(ParticleTypes.SMOKE, ParticleTypes.FLAME),
        /** Original meta 5, mallornTorchSilver. */
        MALLORN_SILVER(ParticleTypes.END_ROD, null),
        /** Original meta 13, mallornTorchBlue. */
        MALLORN_BLUE(ParticleTypes.SOUL_FIRE_FLAME, null),
        /** Original meta 14, mallornTorchGold. */
        MALLORN_GOLD(ParticleTypes.ELECTRIC_SPARK, null),
        /** Original meta 15, mallornTorchGreen. */
        MALLORN_GREEN(ParticleTypes.HAPPY_VILLAGER, null),
        /** Original meta 6: falling red leaves. */
        WOOD_ELVEN(ParticleTypes.CHERRY_LEAVES, null),
        /** Original meta 10: "elvenGlow". */
        HIGH_ELVEN(ParticleTypes.END_ROD, null),
        /** Original meta 12: "morgulPortal". */
        MORGUL(ParticleTypes.PORTAL, null);

        private final ParticleOptions primary;
        private final ParticleOptions secondary;

        ParticleStyle(ParticleOptions primary, ParticleOptions secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }
    }

    private final ParticleStyle particleStyle;

    public LOTRChandelierBlock(ParticleStyle particleStyle, Properties properties) {
        super(properties);
        this.particleStyle = particleStyle;
    }

    @Override
    protected MapCodec<? extends LOTRChandelierBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    /**
     * The original walked a list of special cases (fence, wall, bottom slab,
     * bottom stairs, orc chain) before falling back to isSideSolid(..., DOWN).
     * canSupportCenter covers all of those in one call: a bottom slab and a
     * bottom-half stair both present a solid down face at their top, a fence
     * and a wall both have a solid post centre, and anything else solid passes
     * for the same reason it did before.
     */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.above(), Direction.DOWN);
    }

    /**
     * Instead of overriding neighborChanged (whose signature has churned across
     * versions), we schedule a tick whenever a neighbouring block updates and
     * do the support check in tick(). updateShape is the modern hook Fabric
     * blocks use for this and its return value lets us keep the block; we only
     * use it to enqueue the check.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            tickAccess.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            // destroyBlock(pos, true) already drops the block's resources -- an
            // extra dropResources() call here would yield two chandeliers.
            level.destroyBlock(pos, true);
        }
    }

    /**
     * Four emission points around the fixture, as in the original:
     * x/z at 0.13 and 0.87, y at 0.6875.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double near = 0.13;
        double far = 1.0 - near;
        double height = 0.6875;

        spawn(level, pos.getX() + near, pos.getY() + height, pos.getZ() + near);
        spawn(level, pos.getX() + far, pos.getY() + height, pos.getZ() + far);
        spawn(level, pos.getX() + near, pos.getY() + height, pos.getZ() + far);
        spawn(level, pos.getX() + far, pos.getY() + height, pos.getZ() + near);
    }

    private void spawn(Level level, double x, double y, double z) {
        level.addParticle(particleStyle.primary, x, y, z, 0.0, 0.0, 0.0);
        if (particleStyle.secondary != null) {
            level.addParticle(particleStyle.secondary, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}