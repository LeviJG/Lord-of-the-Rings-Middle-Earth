package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.IdentityHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * The randomDisplayTick effects of 1.7.10 blocks that are plain blocks in the
 * port and so have no animateTick of their own. LOTRBlockMixin calls
 * {@link #animate} from Block.animateTick; a block class that overrides
 * animateTick must call super for its entry here to run.
 */
public final class LOTRBlockParticles {

    @FunctionalInterface
    private interface Effect {
        void play(Level level, BlockPos pos, RandomSource random);
    }

    private static final Map<Block, Effect> EFFECTS = new IdentityHashMap<>();

    static {
        // LOTRBlockRock, metadata 0: Mordor rock smoulders.
        EFFECTS.put(LOTRBuildingBlocks.MORDOR_ROCK, (level, pos, random) -> {
            if (random.nextInt(10) == 0) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + random.nextFloat(), pos.getY() + 1.1,
                        pos.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
            }
        });
        // LOTRBlockHearth: a fire on top sends large smoke up to five blocks,
        // stopping at the first solid one.
        EFFECTS.put(LOTRBuildingBlocks.HEARTH, (level, pos, random) -> {
            if (!level.getBlockState(pos.above()).is(BlockTags.FIRE)) {
                return;
            }
            for (int dy = 1; dy <= 5 && level.getBlockState(pos.above(dy)).getCollisionShape(level, pos.above(dy)).isEmpty(); dy++) {
                for (int l = 0; l < 3; l++) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + random.nextFloat(),
                            pos.getY() + dy + random.nextFloat(), pos.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
                }
            }
        });
        // LOTRBlockMorgulTable: two flames over the top every tick.
        EFFECTS.put(LOTRUtilityBlocks.MORGUL_CRAFTING_TABLE, (level, pos, random) -> {
            for (int l = 0; l < 2; l++) {
                level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25 + random.nextFloat() * 0.5, pos.getY() + 1.0,
                        pos.getZ() + 0.25 + random.nextFloat() * 0.5, 0.0, 0.0, 0.0);
            }
        });
        // LOTRBlockDolGuldurTable: now and then, a burst of sixteen Morgul sparks.
        EFFECTS.put(LOTRUtilityBlocks.DOL_GULDUR_CRAFTING_TABLE, (level, pos, random) -> {
            if (random.nextInt(20) == 0) {
                for (int l = 0; l < 16; l++) {
                    morgulSpark(level, pos.getX(), pos.getY() + 1.0, pos.getZ(), random);
                }
            }
        });
        // LOTRBlockCorruptMallorn: two Morgul sparks every tick.
        EFFECTS.put(LOTRDecorationBlocks.CORRUPT_MALLORN, (level, pos, random) -> {
            for (int l = 0; l < 2; l++) {
                morgulSpark(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), random);
            }
        });
        // LOTRBlockMorgulFlower: one tick in four, Morgul water or white smoke.
        EFFECTS.put(LOTRDecorationBlocks.MORGUL_FLOWER, (level, pos, random) -> {
            if (random.nextInt(4) == 0) {
                double x = pos.getX() + 0.1 + random.nextFloat() * 0.8;
                double y = pos.getY() + 0.5 + random.nextFloat() * 0.25;
                double z = pos.getZ() + 0.1 + random.nextFloat() * 0.8;
                level.addParticle(random.nextBoolean() ? LOTRParticles.MORGUL_WATER : LOTRParticles.WHITE_SMOKE,
                        x, y, z, 0.0, 0.0, 0.0);
            }
        });
        // LOTRBlockQuenditeGrass: blue smoke off the top, one tick in eight.
        EFFECTS.put(LOTRBuildingBlocks.QUENDITE_GRASS, (level, pos, random) -> {
            if (random.nextInt(8) == 0) {
                level.addParticle(LOTRParticles.QUENDITE_SMOKE, pos.getX() + random.nextFloat(), pos.getY() + 1.0,
                        pos.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
            }
        });
    }

    private LOTRBlockParticles() {
    }

    /** A "morgulPortal" particle over the middle of a block, drifting up and out. */
    private static void morgulSpark(Level level, double x, double y, double z, RandomSource random) {
        level.addParticle(LOTRParticles.MORGUL_PORTAL,
                x + 0.25 + random.nextFloat() * 0.5, y, z + 0.25 + random.nextFloat() * 0.5,
                -0.05 + random.nextFloat() * 0.1, 0.1 + random.nextFloat() * 0.1, -0.05 + random.nextFloat() * 0.1);
    }

    public static void animate(Block block, Level level, BlockPos pos, RandomSource random) {
        Effect effect = EFFECTS.get(block);
        if (effect != null) {
            effect.play(level, pos, random);
        }
    }
}
