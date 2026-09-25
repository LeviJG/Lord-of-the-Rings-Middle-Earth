package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * LOTRBlockGoran, "Goran" and "Cargoran": a joke block with no creative tab.
 * Used by a server operator, it fills every loaded air block within 32 in each
 * direction with water. Anyone else gets nothing.
 */
public class LOTRGoranBlock extends Block {

    public static final MapCodec<LOTRGoranBlock> CODEC = simpleCodec(LOTRGoranBlock::new);

    private static final int RANGE = 32;

    public LOTRGoranBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (level instanceof ServerLevel server && player instanceof ServerPlayer serverPlayer) {
            if (!server.getServer().getPlayerList().isOp(serverPlayer.nameAndId())) {
                return InteractionResult.PASS;
            }
            BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos();
            for (int x = -RANGE; x <= RANGE; ++x) {
                for (int y = -RANGE; y <= RANGE; ++y) {
                    for (int z = -RANGE; z <= RANGE; ++z) {
                        target.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (server.isLoaded(target) && server.isEmptyBlock(target)) {
                            server.setBlockAndUpdate(target, Blocks.WATER.defaultBlockState());
                        }
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
