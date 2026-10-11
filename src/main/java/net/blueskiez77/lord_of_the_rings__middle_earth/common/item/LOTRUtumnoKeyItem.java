package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRItemUtumnoKey's two whole keys: the Key of Ice, used on the floor below the ice level's lowest
 * corridor, blasts open a winding stair down into the obsidian level; the Key of Obsidian, likewise
 * from the obsidian level into the fire. Anywhere else in Utumno it only smokes.
 */
public class LOTRUtumnoKeyItem extends Item {
    private final LOTRUtumnoLevel keyUsageLevel;

    public LOTRUtumnoKeyItem(LOTRUtumnoLevel keyUsageLevel, Properties properties) {
        super(properties);
        this.keyUsageLevel = keyUsageLevel;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!LOTRUtumno.isUtumno(world) || context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockState block = world.getBlockState(pos);
        LOTRUtumnoLevel utumnoLevel = LOTRUtumnoLevel.forY(j);
        if (utumnoLevel == this.keyUsageLevel && j < utumnoLevel.corridorBaseLevels[0] && LOTRFeature.isOpaqueCube(block)) {
            if (world instanceof ServerLevel serverLevel) {
                world.playSound(null, i + 0.5, j + 0.5, k + 0.5, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 2.0f,
                        0.2f + world.getRandom().nextFloat() * 0.2f);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block), i + 0.5, j + 1.5, k + 0.5, 60, 1.0, 0.0,
                        1.0, 0.0);
                cutStair(world, i, j, k);
            }
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        for (int l = 0; l < 8; ++l) {
            world.addParticle(ParticleTypes.SMOKE, i + world.getRandom().nextFloat(), j + 1.0, k + world.getRandom().nextFloat(), 0.0, 0.0, 0.0);
        }
        return InteractionResult.PASS;
    }

    /** The stair: round and round a glowing core, down until it opens on a floor of the level below. */
    private void cutStair(Level world, int i, int j, int k) {
        LOTRUtumnoLevel targetLevel = LOTRUtumnoLevel.values()[this.keyUsageLevel.ordinal() + 1];
        int stair = 0;
        int stairX = i - 1;
        int stairZ = k - 1;
        int stairY = j;
        while (stairY >= targetLevel.corridorBaseLevels[0] && (LOTRUtumnoLevel.forY(stairY) != targetLevel
                || !world.isEmptyBlock(new BlockPos(stairX, stairY + 1, stairZ))
                || !world.getBlockState(new BlockPos(stairX, stairY, stairZ)).isFaceSturdy(world, new BlockPos(stairX, stairY, stairZ), Direction.UP))) {
            BlockPos stairPos = new BlockPos(stairX, stairY, stairZ);
            switch (stair) {
                case 0, 2, 4, 6 -> world.setBlock(stairPos, this.keyUsageLevel.brick(), Block.UPDATE_ALL);
                case 1 -> world.setBlock(stairPos, this.keyUsageLevel.brickStair(1), Block.UPDATE_ALL);
                case 3 -> world.setBlock(stairPos, this.keyUsageLevel.brickStair(3), Block.UPDATE_ALL);
                case 5 -> world.setBlock(stairPos, this.keyUsageLevel.brickStair(0), Block.UPDATE_ALL);
                case 7 -> world.setBlock(stairPos, this.keyUsageLevel.brickStair(2), Block.UPDATE_ALL);
                default -> {
                }
            }
            for (int j1 = 1; j1 <= 3; ++j1) {
                world.setBlock(new BlockPos(stairX, stairY + j1, stairZ), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
            world.setBlock(new BlockPos(i, stairY, k), this.keyUsageLevel.brickGlow(), Block.UPDATE_ALL);
            if (stair <= 1) {
                ++stairX;
            } else if (stair <= 3) {
                ++stairZ;
            } else if (stair <= 5) {
                --stairX;
            } else if (stair <= 7) {
                --stairZ;
            }
            if (stair % 2 == 1) {
                --stairY;
            }
            stair = (stair + 1) % 8;
        }
    }
}
