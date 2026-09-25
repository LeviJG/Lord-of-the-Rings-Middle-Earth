package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDamageTypes;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A cross-shaped LOTR plant: its 1.7.10 outline box, and the ground it will
 * stand on.
 *
 * <p>VegetationBlock is the modern BlockBush, which every plant in
 * LOTRBlockFlower's family extended. It brings the survival check -- a plant
 * whose support is broken pops off as an item -- and, through
 * {@link #mayPlaceOn}, the rule about what counts as support. The 1.7.10
 * classes did not agree on that rule, so it is a parameter here: see
 * {@link Ground}.
 *
 * <p>{@link Shape} holds the {@code setBlockBounds} / {@code setFlowerBounds}
 * call each plant made, converted from 0..1 fractions to the 0..16 pixels
 * {@link Block#box} wants. Without it every plant kept Block's default full
 * cube, so the selection box floated a metre above a flower.
 */
public class LOTRPlantBlock extends VegetationBlock {
    public static final MapCodec<LOTRPlantBlock> CODEC =
            simpleCodec(props -> new LOTRPlantBlock(Shape.FLOWER, Ground.SOIL, props));

    /** Outline boxes, named for the 1.7.10 class or call site they come from. */
    public enum Shape {
        /** BlockBush's default, which LOTRBlockFlower inherited: 0.3..0.7, 0.6 high. */
        FLOWER(4.8, 9.6, 11.2),
        /** setFlowerBounds(0.1, 0, 0.1, 0.9, 0.7, 0.9) -- shire heather, athelas, lavender. */
        BROAD(1.6, 11.2, 14.4),
        /** LOTRBlockGrass: setBlockBounds(0.1, 0, 0.1, 0.9, 0.8, 0.9). */
        GRASS(1.6, 12.8, 14.4),
        /** setFlowerBounds(0.2, 0, 0.2, 0.8, 0.8, 0.8) -- flax and the Fangorn plants. */
        MEDIUM(3.2, 12.8, 12.8),
        /** f = 0.125 -- blackroot and the morgul-flowers. */
        WIDE(2.0, 12.8, 14.0),
        /** LOTRBlockMorgulShroom: f = 0.2, so only 0.4 high. */
        SHROOM(4.8, 6.4, 11.2),
        /** f = 0.375 and full height -- corn stalks and reeds. */
        STALK(2.0, 16.0, 14.0),
        /**
         * LOTRBlockGrapevine.setBlockBoundsForItemRender: f = 0.125, so a
         * 4x4 post running the full height of the block. The bare post is not
         * a cross at all -- 1.7.10 drew it with renderStandardBlock.
         */
        POST(6.0, 16.0, 10.0),
        /** LOTRBlockClover: setBlockBounds(0.2, 0, 0.2, 0.8, 0.4, 0.8). */
        CLOVER(3.2, 6.4, 12.8);

        private final VoxelShape shape;

        Shape(double min, double height, double max) {
            this.shape = Block.box(min, 0.0, min, max, height, max);
        }
    }

    /**
     * What a plant will stand on, ported from the 1.7.10 canBlockStay /
     * canPlaceBlockAt overrides.
     */
    public enum Ground {
        /**
         * BlockBush's own rule, which most LOTR flowers inherited. In 1.7.10
         * that meant grass, dirt or farmland; #supports_vegetation is the
         * modern spelling of the same set, and LOTRBlockGrass's slightly wider
         * canSustainPlant check collapses onto it too.
         */
        SOIL,
        /** LOTRBlockGrass.setSandy() -- arid grass, which also takes sand. */
        SOIL_OR_SAND,
        /**
         * LOTRBiomeGenMordor.isSurfaceMordorBlock: Mordor rock, dirt or gravel,
         * and nothing else. LOTRBlockMordorPlant and LOTRBlockMordorGrass.
         */
        MORDOR,
        /** LOTRBlockMorgulFlower: super.canBlockStay() OR a Mordor surface. */
        SOIL_OR_MORDOR,
        /**
         * LOTRBlockReed: a water source directly below, or another reed. The
         * reeds stand IN the water rather than beside it.
         */
        WATER,
        /**
         * LOTRBlockCorn: soil, or the Beach plant type (sand), or another corn
         * block -- corn is a column.
         */
        SOIL_OR_SAND_OR_SELF,
        /**
         * LOTRBlockGrapevine: any sturdy top face, soil, or another grapevine.
         */
        STURDY_OR_SELF
    }

    /**
     * What a plant does to something standing in it, from the 1.7.10
     * onEntityCollidedWithBlock overrides.
     */
    public enum Sting {
        /** Most plants. */
        NONE(0.0F),
        /**
         * LOTRBlockTallGrass: the thistle (meta 3) pricked anything SPRINTING
         * through it and the nettles (meta 4) pricked players, both for a
         * quarter heart, and both were ignored if you wore boots AND leggings.
         */
        THISTLE(0.25F),
        NETTLE(0.25F),
        /**
         * LOTRBlockMordorThorn: 2.0 damage, a full heart, to anything not of
         * the Mordor faction, with no armour exemption at all.
         */
        THORN(2.0F);

        private final float damage;

        Sting(float damage) {
            this.damage = damage;
        }
    }

    private final Shape plantShape;
    private final Ground ground;
    private final Sting sting;

    public LOTRPlantBlock(Shape plantShape, Ground ground, Properties properties) {
        this(plantShape, ground, Sting.NONE, properties);
    }

    public LOTRPlantBlock(Shape plantShape, Ground ground, Sting sting, Properties properties) {
        super(properties);
        this.plantShape = plantShape;
        this.ground = ground;
        this.sting = sting;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
            InsideBlockEffectApplier effects, boolean flag) {
        // hurtServer, not hurt or hurtOrSimulate: both of those are deprecated
        // now, and the client-side guard below already leaves only a ServerLevel.
        if (sting == Sting.NONE || !(level instanceof ServerLevel server)) {
            return;
        }
        switch (sting) {
            case THISTLE -> {
                if (entity.isSprinting() && !wearsLegArmour(entity)) {
                    entity.hurtServer(server, LOTRDamageTypes.plantHurt(level), Sting.THISTLE.damage);
                }
            }
            case NETTLE -> {
                if (entity instanceof Player && !wearsLegArmour(entity)) {
                    entity.hurtServer(server, LOTRDamageTypes.plantHurt(level), Sting.NETTLE.damage);
                }
            }
            // LOTRBlockMordorThorn spared only entities whose getNPCFaction is
            // MORDOR, which means Mordor NPCs. The port has none yet, and
            // everything else (players included) was UNALIGNED, so it pricks
            // everything -- exactly as the original did for these entities.
            case THORN -> entity.hurtServer(server, LOTRDamageTypes.plantHurt(level), Sting.THORN.damage);
            default -> {
            }
        }
    }

    private static boolean wearsLegArmour(Entity entity) {
        return entity instanceof LivingEntity living
                && !living.getItemBySlot(EquipmentSlot.FEET).isEmpty()
                && !living.getItemBySlot(EquipmentSlot.LEGS).isEmpty();
    }

    @Override
    public MapCodec<? extends LOTRPlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // The box has to follow the random per-position offset the plant is
        // drawn with, or the outline sits beside the plant instead of on it.
        return plantShape.shape.move(state.getOffset(pos));
    }

    @Override
    protected boolean mayPlaceOn(BlockState below, BlockGetter level, BlockPos belowPos) {
        return switch (ground) {
            case SOIL -> soil(below);
            case SOIL_OR_SAND -> soil(below) || below.is(BlockTags.SAND);
            case MORDOR -> below.is(LOTRBlockTags.MORDOR_SURFACE);
            case SOIL_OR_MORDOR -> soil(below) || below.is(LOTRBlockTags.MORDOR_SURFACE);
            case WATER -> below.is(this)
                    || (below.getFluidState().is(Fluids.WATER) && below.getFluidState().isSource());
            case SOIL_OR_SAND_OR_SELF -> below.is(this) || soil(below) || below.is(BlockTags.SAND);
            // LOTRBlockGrapevine.canPlaceBlockAt: any grapevine, post or vine.
            case STURDY_OR_SELF -> below.is(this) || below.getBlock() instanceof LOTRGrapevineBlock
                    || soil(below) || below.isFaceSturdy(level, belowPos, Direction.UP);
        };
    }

    private static boolean soil(BlockState below) {
        return below.is(BlockTags.SUPPORTS_VEGETATION);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (this == LOTRBlocks.PIPEWEED_PLANT) {
            pipeweedSmoke(level, pos, random);
        }
    }

    /**
     * LOTRBlockPipeweedPlant.randomDisplayTick: a wisp of smoke one tick in
     * four. The ripe pipeweed crop borrowed it too.
     */
    public static void pipeweedSmoke(Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            double x = pos.getX() + 0.1 + random.nextFloat() * 0.8;
            double y = pos.getY() + 0.5 + random.nextFloat() * 0.25;
            double z = pos.getZ() + 0.1 + random.nextFloat() * 0.8;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
