package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPortalBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortalTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockElvenPortal: the Elves' portal -- a pool of water ringed with quendite grass, made a
 * portal by an elanor or niphredil flower dropped in by a friend of Lothlórien or the High Elves.
 */
public class LOTRElvenPortalBlock extends LOTRPortalBlock {
    public static final MapCodec<LOTRElvenPortalBlock> CODEC = simpleCodec(LOTRElvenPortalBlock::new);

    public LOTRElvenPortalBlock(Properties properties) {
        super(properties, LOTRFaction.LOTHLORIEN, LOTRFaction.HIGH_ELF);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return LOTRPortalBlockEntity.elven(pos, state);
    }

    @Override
    public LOTRPortalTeleporter getPortalTeleporter() {
        return LOTRPortalTeleporter.ELVEN;
    }

    /** A 3x3 of water (or of the portal, once made) on solid ground, its sides quendite grass, its corners anything. */
    @Override
    public boolean isValidPortalLocation(Level level, int i, int j, int k, boolean portalAlreadyMade) {
        for (int i1 = i - 2; i1 <= i + 2; ++i1) {
            for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                if (Math.abs(i1 - i) == 2 && Math.abs(k1 - k) == 2) {
                    continue;
                }
                BlockState state = level.getBlockState(new BlockPos(i1, j, k1));
                if (Math.abs(i1 - i) == 2 || Math.abs(k1 - k) == 2) {
                    if (!state.is(LOTRBuildingBlocks.QUENDITE_GRASS)) {
                        return false;
                    }
                } else if (!state.is(portalAlreadyMade ? this : Blocks.WATER)
                        || !LOTRFeature.isOpaqueCube(level.getBlockState(new BlockPos(i1, j - 1, k1)))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.END_ROD, pos.getX() + random.nextFloat(), pos.getY() + 0.8, pos.getZ() + random.nextFloat(),
                    0.0, 0.3, 0.0);
        }
        super.animateTick(state, level, pos, random);
    }

    @Override
    public void setPlayerInPortal(Player player) {
        LOTRPortals.setInPortal(player, this);
        if (!player.level().isClientSide()) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.USE_ELVEN_PORTAL);
        }
    }
}
