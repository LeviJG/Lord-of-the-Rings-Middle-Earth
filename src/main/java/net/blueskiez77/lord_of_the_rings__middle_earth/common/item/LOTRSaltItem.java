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
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRItemSalt: salting the earth. Used on the ground it spoils the soil in a
 * rough disc of radius 1 to 3 (half that vertically): grass, dirt and farmland
 * become coarse dirt, and jungle grass, jungle mud and jungle farmland become
 * barren jungle mud. The block clicked always turns; each other one has a two
 * in three chance. One salt is spent if anything changed.
 *
 * <p>"Dirt" here is plain dirt only -- the original tested Blocks.dirt at
 * metadata 0, so coarse dirt and podzol were left alone.
 */
public class LOTRSaltItem extends Item {

    public LOTRSaltItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide()) {
            BlockPos origin = context.getClickedPos();
            RandomSource random = level.getRandom();
            boolean changedAny = false;
            int range = 1 + random.nextInt(3);
            int yRange = range / 2;
            for (int i1 = -range; i1 <= range; ++i1) {
                for (int j1 = -yRange; j1 <= yRange; ++j1) {
                    for (int k1 = -range; k1 <= range; ++k1) {
                        if (i1 * i1 + k1 * k1 > range * range) {
                            continue;
                        }
                        BlockPos pos = origin.offset(i1, j1, k1);
                        Block salted = saltedForm(level.getBlockState(pos));
                        if (salted == null) {
                            continue;
                        }
                        if (i1 == 0 && j1 == 0 && k1 == 0 || random.nextInt(3) != 0) {
                            level.setBlock(pos, salted.defaultBlockState(), Block.UPDATE_ALL);
                        }
                        changedAny = true;
                    }
                }
            }
            if (changedAny) {
                context.getItemInHand().shrink(1);
            }
        }
        // The original returned true whether or not anything changed.
        return InteractionResult.SUCCESS;
    }

    private static Block saltedForm(BlockState state) {
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.FARMLAND)) {
            return Blocks.COARSE_DIRT;
        }
        if (state.is(LOTRBlocks.MUD_GRASS) || state.is(LOTRBlocks.MUD) || state.is(LOTRBlocks.MUD_FARMLAND)) {
            return LOTRBlocks.BARREN_JUNGLE_MUD;
        }
        return null;
    }
}
