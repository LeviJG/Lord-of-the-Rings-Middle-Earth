package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRTermiteEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockTermite, meta 0: a termite mound with its termites still in it.
 * Half the time, broken by a player it lets out one to three of them, and
 * blown up it lets out one. (Only with silk touch does it drop, as the
 * cleared mound.)
 */
public class LOTRInfestedTermiteMoundBlock extends Block {

    public LOTRInfestedTermiteMoundBlock(Properties properties) {
        super(properties);
    }

    /** onBlockDestroyedByPlayer. */
    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof ServerLevel server && server.getRandom().nextBoolean()) {
            int termites = 1 + server.getRandom().nextInt(3);
            for (int l = 0; l < termites; ++l) {
                spawnTermite(server, pos);
            }
        }
    }

    /** onBlockExploded. */
    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        if (level.getRandom().nextBoolean()) {
            spawnTermite(level, pos);
        }
    }

    private static void spawnTermite(ServerLevel level, BlockPos pos) {
        LOTRTermiteEntity termite = LOTREntities.TERMITE.create(level, EntitySpawnReason.TRIGGERED);
        if (termite != null) {
            termite.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.getRandom().nextFloat() * 360.0f, 0.0f);
            level.addFreshEntity(termite);
        }
    }
}
