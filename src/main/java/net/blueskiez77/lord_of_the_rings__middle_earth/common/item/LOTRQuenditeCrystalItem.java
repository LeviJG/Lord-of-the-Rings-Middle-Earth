package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

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
import net.minecraft.world.level.block.Blocks;

/**
 * LOTRItemQuenditeCrystal, the Edhelvir. Used on grass by a friend of the
 * Galadhrim or the High Elves (+1 alignment with either), it turns the grass
 * into quendite grass and is spent; anyone else gets a puff of smoke and the
 * insufficient-alignment message.
 *
 * <p>NOT ported: the anvil name colour (LOTRItemWithAnvilNameColor, dark aqua).
 */
public class LOTRQuenditeCrystalItem extends Item {
    private static final float REQUIRED_ALIGNMENT = 1.0f;

    public LOTRQuenditeCrystalItem(Properties properties) {
        super(properties);
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
        if (!level.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
            return InteractionResult.PASS;
        }

        RandomSource random = level.getRandom();
        if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.LOTHLORIEN) >= REQUIRED_ALIGNMENT
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.HIGH_ELF) >= REQUIRED_ALIGNMENT) {
            level.setBlock(pos, LOTRBlocks.QUENDITE_GRASS.defaultBlockState(), Block.UPDATE_ALL);
            stack.shrink(1);
            for (int l = 0; l < 8; ++l) {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this),
                        pos.getX() + random.nextFloat(), pos.getY() + 1.5, pos.getZ() + random.nextFloat(),
                        0.0, 0.0, 0.0);
            }
        } else {
            for (int l = 0; l < 8; ++l) {
                level.addParticle(ParticleTypes.SMOKE,
                        pos.getX() + random.nextFloat(), pos.getY() + 1.0, pos.getZ() + random.nextFloat(),
                        0.0, 0.0, 0.0);
            }
            if (!level.isClientSide()) {
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, REQUIRED_ALIGNMENT,
                        LOTRFaction.LOTHLORIEN, LOTRFaction.HIGH_ELF);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
