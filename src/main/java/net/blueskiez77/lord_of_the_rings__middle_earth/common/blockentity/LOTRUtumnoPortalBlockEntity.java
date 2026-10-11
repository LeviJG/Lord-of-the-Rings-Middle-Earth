package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGuiMessageTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

/**
 * LOTRTileEntityUtumnoPortal: each block of the darkness at the bottom of the entrance. Every minute
 * after its last use the portal picks a new place somewhere within 50000 blocks of Utumno's middle to
 * send its comers; the whole portal sends them where its first block found (searching from the far
 * corner) aims. It warns whoever comes down the shaft, once, and now and then the depths groan.
 */
public class LOTRUtumnoPortalBlockEntity extends BlockEntity {
    public static final int TARGET_COORDINATE_RANGE = 50000;

    public int targetX;
    public int targetZ;
    public int targetResetTick;

    public LOTRUtumnoPortalBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.UTUMNO_PORTAL, pos, state);
    }

    /** findActingTargetingPortal: the first block of this portal within eight, searched from the +x, +z corner. */
    public LOTRUtumnoPortalBlockEntity findActingTargetingPortal() {
        int range = 8;
        for (int i = range; i >= -range; --i) {
            for (int k = range; k >= -range; --k) {
                BlockPos pos = this.worldPosition.offset(i, 0, k);
                if (this.level.getBlockState(pos).is(getBlockState().getBlock())
                        && this.level.getBlockEntity(pos) instanceof LOTRUtumnoPortalBlockEntity te) {
                    return te;
                }
            }
        }
        return this;
    }

    public void transferEntity(Entity entity) {
        entity.resetFallDistance();
        if (this.level instanceof ServerLevel serverLevel) {
            LOTRUtumnoPortalBlockEntity actingPortal = findActingTargetingPortal();
            ServerLevel utumno = serverLevel.getServer().getLevel(LOTRWorldGen.UTUMNO);
            if (utumno == null) {
                return;
            }
            if (entity instanceof ServerPlayer player) {
                player.teleport(LOTRUtumnoTeleporter.transition(utumno, player, actingPortal.targetX, actingPortal.targetZ));
            } else {
                LOTRPortals.transferEntity(entity, LOTRUtumnoTeleporter.transition(utumno, entity, actingPortal.targetX, actingPortal.targetZ));
            }
            actingPortal.targetResetTick = 1200;
            actingPortal.setChanged();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LOTRUtumnoPortalBlockEntity portal) {
        if (level.getBlockState(pos.below()).is(state.getBlock())) {
            level.removeBlock(pos, false);
        }
        if (!(level instanceof ServerLevel)) {
            return;
        }
        if (portal.targetResetTick > 0) {
            --portal.targetResetTick;
        } else {
            portal.targetX = LOTRWorldGenUtil.getRandomIntegerInRange(level.getRandom(), -TARGET_COORDINATE_RANGE, TARGET_COORDINATE_RANGE);
            portal.targetZ = LOTRWorldGenUtil.getRandomIntegerInRange(level.getRandom(), -TARGET_COORDINATE_RANGE, TARGET_COORDINATE_RANGE);
            portal.targetResetTick = 1200;
            portal.setChanged();
        }
        AABB box = new AABB(pos.getX() - 8, pos.getY(), pos.getZ() - 8, pos.getX() + 9, pos.getY() + 60, pos.getZ() + 9);
        for (Player player : level.getEntitiesOfClass(Player.class, box)) {
            LOTRGuiMessageTypes.sendMessageIfNotReceived(player, LOTRGuiMessageTypes.UTUMNO_WARN);
        }
        if (level.getRandom().nextInt(2000) == 0) {
            float volume = 6.0f;
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    level.getRandom().nextBoolean() ? LOTRSounds.WIGHT_AMBIENCE : SoundEvents.AMBIENT_CAVE.value(), SoundSource.AMBIENT, volume,
                    0.8f + level.getRandom().nextFloat() * 0.2f);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.targetX = input.getIntOr("TargetX", 0);
        this.targetZ = input.getIntOr("TargetZ", 0);
        this.targetResetTick = input.getIntOr("TargetReset", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("TargetX", this.targetX);
        output.putInt("TargetZ", this.targetZ);
        output.putInt("TargetReset", this.targetResetTick);
    }
}
