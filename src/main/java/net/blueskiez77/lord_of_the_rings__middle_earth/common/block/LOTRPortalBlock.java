package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPortalBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortalTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockPortal: a built portal's surface, a sixteenth thick, walked through rather than on. Those
 * friendly to its peoples -- a player with at least +1 with one of them, or a creature not at odds with
 * them -- are taken between the overworld and Middle-earth (a player after the wait of a nether
 * portal, a creature at once); anyone else is burnt. It may stand only in those two worlds, and does
 * nothing while "Enable Middle-earth Portals" is off.
 */
public abstract class LOTRPortalBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    private final LOTRFaction[] portalFactions;

    protected LOTRPortalBlock(Properties properties, LOTRFaction... portalFactions) {
        super(properties);
        this.portalFactions = portalFactions;
    }

    public abstract boolean isValidPortalLocation(Level level, int i, int j, int k, boolean portalAlreadyMade);

    public abstract LOTRPortalTeleporter getPortalTeleporter();

    /** setPlayerInPortal: the player's wait begins, and the achievement for using it is earned. */
    public abstract void setPlayerInPortal(Player player);

    public boolean canUsePortal(Entity entity) {
        if (entity instanceof Player player) {
            if (player.getAbilities().instabuild) {
                return true;
            }
            for (LOTRFaction faction : this.portalFactions) {
                if (LOTRPlayerAlignments.getAlignment(player, faction) >= 1.0f) {
                    return true;
                }
            }
            return false;
        }
        for (LOTRFaction faction : this.portalFactions) {
            if (LOTRNearestAttackableTargetGoal.factionOf(entity).isBadRelation(faction) || entity.isPassenger() || entity.isVehicle()
                    || entity.isOnPortalCooldown()) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    /** onBlockAdded: nowhere but the overworld and Middle-earth. */
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.dimension() != Level.OVERWORLD && level.dimension() != LOTRWorldGen.MIDDLE_EARTH) {
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier,
                                boolean isPrecise) {
        if (!LOTRConfig.enablePortals) {
            return;
        }
        if (canUsePortal(entity)) {
            if (!entity.isPassenger() && !entity.isVehicle()) {
                if (entity instanceof Player player) {
                    setPlayerInPortal(player);
                } else if (level instanceof ServerLevel serverLevel) {
                    LOTRPortals.transferThroughPortal(serverLevel, entity, getPortalTeleporter());
                }
            }
        } else if (level instanceof ServerLevel serverLevel) {
            entity.igniteForSeconds(4.0f);
            entity.hurtServer(serverLevel, serverLevel.damageSources().inFire(), 2.0f);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f,
                    1.5f + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.5f);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS,
                    0.5f, random.nextFloat() * 0.4f + 0.8f, false);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRPortalBlockEntity>) LOTRPortalBlockEntity::serverTick;
    }
}
