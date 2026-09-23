package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentMessages;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRItemGuldurilCrystal: gulduril, the Necromancer's crystal.
 *
 * <p>onItemUse, transcribed:
 * <ul>
 *   <li>On one of the ten bricks LOTRBlockGuldurilBrick.guldurilMetaForBlock
 *       names, a servant of Mordor, Angmar or Dol Guldur (+1 alignment) sets
 *       the crystal into it, making the glowing gulduril brick; anyone else
 *       gets smoke and the alignment message.</li>
 *   <li>On a mallorn sapling, anyone the Ents count as an enemy (Fangorn
 *       alignment below zero) corrupts it into a corrupt mallorn.</li>
 * </ul>
 *
 * <p>NOT ported: the anvil name colour (dark green) the item gave a renamed item.
 */
public class LOTRGuldurilCrystalItem extends Item {
    private static Map<Block, Block> guldurilBricks;

    public LOTRGuldurilCrystalItem(Properties properties) {
        super(properties);
    }

    /** guldurilMetaForBlock, as source brick to gulduril brick. */
    private static Map<Block, Block> guldurilBricks() {
        if (guldurilBricks == null) {
            guldurilBricks = Map.of(
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
        return guldurilBricks;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player == null || !player.mayUseItemAt(pos, context.getClickedFace(), stack)) {
            return InteractionResult.PASS;
        }
        BlockState state = context.getLevel().getBlockState(pos);
        Block gulduril = guldurilBricks().get(state.getBlock());
        if (gulduril != null) {
            if (context.getLevel() instanceof ServerLevel level) {
                boolean hasAlignment = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR) >= 1.0f
                        || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.ANGMAR) >= 1.0f
                        || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.DOL_GULDUR) >= 1.0f;
                if (hasAlignment) {
                    level.setBlock(pos, gulduril.defaultBlockState(), Block.UPDATE_ALL);
                    stack.shrink(1);
                    spawnCrystalParticles(level, pos);
                } else {
                    level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            8, 0.375, 0.375, 0.375, 0.0);
                    LOTRAlignmentMessages.notifyAlignmentNotHighEnough(player, 1.0f,
                            LOTRFaction.MORDOR, LOTRFaction.ANGMAR, LOTRFaction.DOL_GULDUR);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (state.is(LOTRBlocks.MALLORN_SAPLING)
                && LOTRPlayerAlignments.getAlignment(player, LOTRFaction.FANGORN) < 0.0f) {
            if (context.getLevel() instanceof ServerLevel level) {
                level.setBlock(pos, LOTRBlocks.CORRUPT_MALLORN.defaultBlockState(), Block.UPDATE_ALL);
                stack.shrink(1);
                spawnCrystalParticles(level, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /** spawnCrystalParticles: sixteen shards over a block and a half. */
    private void spawnCrystalParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, this),
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 16, 0.375, 0.375, 0.375, 0.0);
    }
}
