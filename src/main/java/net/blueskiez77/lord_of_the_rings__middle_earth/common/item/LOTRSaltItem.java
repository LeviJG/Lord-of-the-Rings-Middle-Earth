package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * LOTRItemSalt: sown on the ground, it kills the soil.
 *
 * <p>A rough disc a block or three across: grass, dirt and farmland become
 * coarse dirt, and the centre always goes while the rest take two chances in
 * three. The original salted mud to a barren mud the port has no block for, so
 * mud grass and mud farmland fall back to plain mud here. One salt is spent if
 * anything changed.
 */
public class LOTRSaltItem extends Item {

    public LOTRSaltItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        RandomSource random = level.getRandom();
        BlockPos origin = context.getClickedPos();
        int range = 1 + random.nextInt(3);
        int yRange = range / 2;
        boolean changedAny = false;
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -yRange; dy <= yRange; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (dx * dx + dz * dz > range * range) {
                        continue;
                    }
                    BlockPos pos = origin.offset(dx, dy, dz);
                    Block block = level.getBlockState(pos).getBlock();
                    Block salted = null;
                    if (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.FARMLAND) {
                        salted = Blocks.COARSE_DIRT;
                    } else if (block == LOTRBlocks.MUD_GRASS || block == LOTRBlocks.MUD_FARMLAND) {
                        salted = LOTRBlocks.MUD;
                    }
                    if (salted == null) {
                        continue;
                    }
                    if ((dx == 0 && dy == 0 && dz == 0) || random.nextInt(3) != 0) {
                        level.setBlockAndUpdate(pos, salted.defaultBlockState());
                    }
                    changedAny = true;
                }
            }
        }
        if (changedAny) {
            context.getItemInHand().consume(1, context.getPlayer());
        }
        return InteractionResult.SUCCESS;
    }
}
