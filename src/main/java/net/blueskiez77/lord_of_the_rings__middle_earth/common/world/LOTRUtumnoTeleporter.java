package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRTeleporterUtumno: into Utumno, onto the top corridor floor of the ice level within 32 blocks
 * of the portal's target (a new one every minute); out of it, 40 to 80 blocks from the entrance, on
 * the surface. Whoever comes through falls no further, and so does their mount.
 */
public final class LOTRUtumnoTeleporter {

    private LOTRUtumnoTeleporter() {
    }

    public static TeleportTransition transition(ServerLevel target, Entity entity, int targetX, int targetZ) {
        return new TeleportTransition(target, placeInPortal(target, targetX, targetZ), Vec3.ZERO, entity.getYRot(), 0.0f, Set.of(),
                arrived -> {
                    arrived.resetFallDistance();
                    if (arrived.getVehicle() != null) {
                        arrived.getVehicle().resetFallDistance();
                    }
                });
    }

    /** placeInPortal: where in {@code world} one comes out. */
    public static Vec3 placeInPortal(ServerLevel world, int targetX, int targetZ) {
        int i = 0;
        int j = 256;
        int k = 0;
        RandomSource rand = world.getRandom();
        if (world.dimension() == LOTRWorldGen.UTUMNO) {
            search:
            for (int l = 0; l < 10000; ++l) {
                int y = LOTRUtumnoLevel.ICE.corridorBaseLevels[LOTRUtumnoLevel.ICE.corridorBaseLevels.length - 1];
                int targetFuzz = 32;
                int x1 = LOTRWorldGenUtil.getRandomIntegerInRange(rand, targetX - targetFuzz, targetX + targetFuzz);
                int z1 = LOTRWorldGenUtil.getRandomIntegerInRange(rand, targetZ - targetFuzz, targetZ + targetFuzz);
                int yFuzz = 3;
                for (int j1 = -yFuzz; j1 <= yFuzz; ++j1) {
                    int y1 = y + j1;
                    if (!LOTRFeature.isOpaqueCube(world.getBlockState(new BlockPos(x1, y1 - 1, z1))) || !world.isEmptyBlock(new BlockPos(x1, y1, z1))) {
                        continue;
                    }
                    i = x1;
                    j = y1;
                    k = z1;
                    break search;
                }
            }
        } else {
            double randomDistance = 40.0 + rand.nextDouble() * 40.0;
            float angle = rand.nextFloat() * (float) Math.PI * 2.0f;
            i = LOTRFixedStructures.UTUMNO_ENTRANCE.xCoord + (int) (randomDistance * Mth.sin(angle));
            k = LOTRFixedStructures.UTUMNO_ENTRANCE.zCoord + (int) (randomDistance * Mth.cos(angle));
            world.getChunk(i >> 4, k >> 4);
            j = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k);
        }
        return new Vec3(i + 0.5, j + 1.0, k + 0.5);
    }
}
