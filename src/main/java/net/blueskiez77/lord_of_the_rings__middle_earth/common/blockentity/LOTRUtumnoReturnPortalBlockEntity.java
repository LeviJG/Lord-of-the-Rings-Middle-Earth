package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRTileEntityUtumnoReturnPortal: the shaft of light the altar became. It keeps a column of light
 * up to the top of Utumno, and lifts whoever steps into it -- no servant of Utumno, and no one
 * flying -- straight up the middle, faster and faster, out into Middle-earth by the entrance.
 */
public class LOTRUtumnoReturnPortalBlockEntity extends BlockEntity {
    public static final int PORTAL_TOP = 250;

    public int beamCheck;
    public int ticksExisted;

    public LOTRUtumnoReturnPortalBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.UTUMNO_RETURN_PORTAL, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LOTRUtumnoReturnPortalBlockEntity portal) {
        ++portal.ticksExisted;
        if (level instanceof ServerLevel) {
            if (portal.beamCheck % 20 == 0) {
                for (int j = pos.getY() + 1; j <= PORTAL_TOP; ++j) {
                    level.setBlock(new BlockPos(pos.getX(), j, pos.getZ()), LOTRUtilityBlocks.UTUMNO_RETURN_LIGHT.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
            ++portal.beamCheck;
        }
        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, PORTAL_TOP, pos.getZ() + 1);
        for (Entity entity : level.getEntitiesOfClass(Entity.class, box)) {
            if (LOTRNearestAttackableTargetGoal.factionOf(entity) == LOTRFaction.UTUMNO) {
                continue;
            }
            if (entity instanceof Player player && player.getAbilities().flying) {
                continue;
            }
            if (level instanceof ServerLevel) {
                float accel = 0.2f;
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, Math.max(motion.y + accel, 0.0), motion.z);
                entity.setPos(pos.getX() + 0.5, entity.getBoundingBox().minY, pos.getZ() + 0.5);
                entity.resetFallDistance();
            }
            if (entity instanceof Player player) {
                LOTRUtumno.clientInReturnPortal.accept(player);
                if (player instanceof ServerPlayer serverPlayer) {
                    ServerPlayNetworking.send(serverPlayer, new LOTRUtumno.ReturnPayload(player.getX(), player.getZ()));
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
            }
            if (!(level instanceof ServerLevel serverLevel) || entity.getY() < PORTAL_TOP - 5.0) {
                continue;
            }
            ServerLevel middleEarth = serverLevel.getServer().getLevel(LOTRWorldGen.MIDDLE_EARTH);
            if (middleEarth == null) {
                continue;
            }
            if (entity instanceof ServerPlayer player) {
                player.teleport(LOTRUtumnoTeleporter.transition(middleEarth, player, 0, 0));
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.LEAVE_UTUMNO);
            } else {
                LOTRPortals.transferEntity(entity, LOTRUtumnoTeleporter.transition(middleEarth, entity, 0, 0));
            }
        }
    }
}
