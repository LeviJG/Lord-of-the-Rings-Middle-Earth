package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
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
import net.minecraft.world.level.block.Blocks;

/**
 * LOTRItemQuenditeCrystal: edhelvir, which an elf-friend can press into grass
 * to make edhelvir grass.
 *
 * <p>onItemUse on a grass block: with at least +1 alignment to Lothlórien or
 * the High Elves the grass turns and one crystal is used, in a burst of
 * crystal shards; without it, a puff of smoke and the alignment message. The
 * outcome is decided on the server, which holds the alignment.
 *
 * <p>NOT ported: the anvil name colour (dark aqua) the item gave a renamed item.
 */
public class LOTRQuenditeCrystalItem extends Item {
    public LOTRQuenditeCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player == null || !player.mayUseItemAt(pos, context.getClickedFace(), stack)
                || !context.getLevel().getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
            return InteractionResult.PASS;
        }
        if (context.getLevel() instanceof ServerLevel level) {
            double x = pos.getX() + 0.5;
            double z = pos.getZ() + 0.5;
            if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.LOTHLORIEN) >= 1.0f
                    || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.HIGH_ELF) >= 1.0f) {
                level.setBlock(pos, LOTRBlocks.QUENDITE_GRASS.defaultBlockState(), Block.UPDATE_ALL);
                stack.shrink(1);
                level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, this),
                        x, pos.getY() + 1.5, z, 8, 0.25, 0.0, 0.25, 0.0);
            } else {
                level.sendParticles(ParticleTypes.SMOKE, x, pos.getY() + 1.0, z, 8, 0.25, 0.0, 0.25, 0.0);
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, 1.0f,
                        LOTRFaction.LOTHLORIEN, LOTRFaction.HIGH_ELF);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
