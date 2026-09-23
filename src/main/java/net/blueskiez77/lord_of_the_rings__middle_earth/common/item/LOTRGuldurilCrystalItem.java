package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemGuldurilCrystal. Two uses:
 *
 * <p>On one of the ten bricks LOTRBlockGuldurilBrick.guldurilMetaForBlock
 * knows, by a friend of Mordor, Angmar or Dol Guldur (+1 with any), it sets a
 * gulduril crystal into the brick and is spent. Anyone else gets smoke and the
 * insufficient-alignment message.
 *
 * <p>On a mallorn sapling, by an ENEMY of Fangorn (alignment below zero), it
 * corrupts the sapling and is spent.
 *
 * <p>NOT ported: the anvil name colour (LOTRItemWithAnvilNameColor, dark green).
 */
public class LOTRGuldurilCrystalItem extends Item {
    private static final float REQUIRED_ALIGNMENT = 1.0f;

    public LOTRGuldurilCrystalItem(Properties properties) {
        super(properties);
    }

    // guldurilMetaForBlock, in its own order: gulduril meta 0..9.
    private static Map<Block, Block> guldurilForms() {
        return Map.of(
                LOTRBlocks.MORDOR_BRICK, LOTRBlocks.GULDURIL_MORDOR_BRICK,
                LOTRBlocks.CRACKED_MORDOR_BRICK, LOTRBlocks.GULDURIL_CRACKED_MORDOR_BRICK,
                LOTRBlocks.ANGMAR_BRICK, LOTRBlocks.GULDURIL_ANGMAR_BRICK,
                LOTRBlocks.CRACKED_ANGMAR_BRICK, LOTRBlocks.GULDURIL_CRACKED_ANGMAR_BRICK,
                LOTRBlocks.DOL_GULDUR_BRICK, LOTRBlocks.GULDURIL_DOL_GULDUR_BRICK,
                LOTRBlocks.CRACKED_DOL_GULDUR_BRICK, LOTRBlocks.GULDURIL_CRACKED_DOL_GULDUR_BRICK,
                LOTRBlocks.GONDOR_BRICK, LOTRBlocks.GULDURIL_GONDOR_BRICK,
                LOTRBlocks.MOSSY_GONDOR_BRICK, LOTRBlocks.GULDURIL_MOSSY_GONDOR_BRICK,
                LOTRBlocks.CRACKED_GONDOR_BRICK, LOTRBlocks.GULDURIL_CRACKED_GONDOR_BRICK,
                LOTRBlocks.NUMENOREAN_BRICK, LOTRBlocks.GULDURIL_NUMENOREAN_BRICK);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player == null || !player.mayUseItemAt(pos, context.getClickedFace(), stack)) {
            return InteractionResult.FAIL;
        }

        Block target = level.getBlockState(pos).getBlock();
        Block gulduril = guldurilForms().get(target);
        if (gulduril != null) {
            boolean hasAlignment = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR) >= REQUIRED_ALIGNMENT
                    || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.ANGMAR) >= REQUIRED_ALIGNMENT
                    || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.DOL_GULDUR) >= REQUIRED_ALIGNMENT;
            if (hasAlignment) {
                level.setBlock(pos, gulduril.defaultBlockState(), Block.UPDATE_ALL);
                stack.shrink(1);
                spawnCrystalParticles(level, pos);
            } else {
                RandomSource random = level.getRandom();
                for (int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.SMOKE,
                            pos.getX() - 0.25 + random.nextFloat() * 1.5,
                            pos.getY() - 0.25 + random.nextFloat() * 1.5,
                            pos.getZ() - 0.25 + random.nextFloat() * 1.5,
                            0.0, 0.0, 0.0);
                }
                if (!level.isClientSide()) {
                    LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, REQUIRED_ALIGNMENT,
                            LOTRFaction.MORDOR, LOTRFaction.ANGMAR, LOTRFaction.DOL_GULDUR);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (target == LOTRBlocks.MALLORN_SAPLING
                && LOTRPlayerAlignments.getAlignment(player, LOTRFaction.FANGORN) < 0.0f) {
            level.setBlock(pos, LOTRBlocks.CORRUPT_MALLORN.defaultBlockState(), Block.UPDATE_ALL);
            stack.shrink(1);
            spawnCrystalParticles(level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void spawnCrystalParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int l = 0; l < 16; ++l) {
            level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this),
                    pos.getX() - 0.25 + random.nextFloat() * 1.5,
                    pos.getY() - 0.25 + random.nextFloat() * 1.5,
                    pos.getZ() - 0.25 + random.nextFloat() * 1.5,
                    0.0, 0.0, 0.0);
        }
    }
}
