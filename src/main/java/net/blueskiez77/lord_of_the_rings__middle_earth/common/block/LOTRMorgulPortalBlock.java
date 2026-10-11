package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPortalBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortalTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockMorgulPortal: the Enemy's portal -- a pool of lava framed in solid blocks with a gulduril
 * brick pillar three high at each corner, made a portal by a bone thrown in by a servant of Mordor,
 * Angmar or Dol Guldur.
 */
public class LOTRMorgulPortalBlock extends LOTRPortalBlock {
    public static final MapCodec<LOTRMorgulPortalBlock> CODEC = simpleCodec(LOTRMorgulPortalBlock::new);

    public LOTRMorgulPortalBlock(Properties properties) {
        super(properties, LOTRFaction.MORDOR, LOTRFaction.ANGMAR, LOTRFaction.DOL_GULDUR);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return LOTRPortalBlockEntity.morgul(pos, state);
    }

    @Override
    public LOTRPortalTeleporter getPortalTeleporter() {
        return LOTRPortalTeleporter.MORGUL;
    }

    @Override
    public boolean isValidPortalLocation(Level level, int i, int j, int k, boolean portalAlreadyMade) {
        for (int i1 = i - 2; i1 <= i + 2; ++i1) {
            for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                if (Math.abs(i1 - i) == 2 && Math.abs(k1 - k) == 2) {
                    for (int j1 = j + 1; j1 <= j + 3; ++j1) {
                        if (!LOTRBlocks.ALL_GULDURIL.contains(level.getBlockState(new BlockPos(i1, j1, k1)).getBlock())) {
                            return false;
                        }
                    }
                    continue;
                }
                if (Math.abs(i1 - i) == 2 || Math.abs(k1 - k) == 2) {
                    if (!LOTRFeature.isOpaqueCube(level.getBlockState(new BlockPos(i1, j, k1)))) {
                        return false;
                    }
                } else if (!level.getBlockState(new BlockPos(i1, j, k1)).is(portalAlreadyMade ? this : Blocks.LAVA)
                        || !LOTRFeature.isOpaqueCube(level.getBlockState(new BlockPos(i1, j - 1, k1)))) {
                    return false;
                }
            }
        }
        return true;
    }

    /** randomDisplayTick: a Morgul spark every tick, and none of the portal's sound. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        level.addParticle(LOTRParticles.MORGUL_PORTAL, pos.getX() + random.nextFloat(), pos.getY() + 0.8f, pos.getZ() + random.nextFloat(),
                -0.05 + random.nextFloat() * 0.1, 0.1 + random.nextFloat() * 0.1, -0.05 + random.nextFloat() * 0.1);
    }

    @Override
    public void setPlayerInPortal(Player player) {
        LOTRPortals.setInPortal(player, this);
        if (!player.level().isClientSide()) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.USE_MORGUL_PORTAL);
        }
    }
}
