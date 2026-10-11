package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Where a player who died in Middle-earth comes back: in Middle-earth, as its
 * world provider let one respawn there -- at their bed in Middle-earth if they
 * have one, else at its spawn point (the Ring portal's place) -- and then
 * LOTREventHandler.onPlayerRespawn's Middle-earth respawning: if that is
 * further from where they fell than the config's threshold (5000 blocks from
 * a bed, 2000 from the spawn point), somewhere 500 to 1500 blocks from it
 * instead, on the surface. Who dies in Utumno comes back in Middle-earth, by
 * its entrance (LOTRWorldProviderUtumno.getRespawnDimension, and
 * LOTRTeleporterUtumno).
 */
public final class LOTRMiddleEarthRespawn {

    private LOTRMiddleEarthRespawn() {
    }

    /** ServerPlayer.findRespawnPositionAndUseSpawnBlock's answer, for a player who died in Middle-earth. */
    public static TeleportTransition adjust(ServerPlayer player, TeleportTransition respawn) {
        if (player.level() instanceof ServerLevel utumno && utumno.dimension() == LOTRWorldGen.UTUMNO && player.isDeadOrDying()) {
            ServerLevel middleEarth = utumno.getServer().getLevel(LOTRWorldGen.MIDDLE_EARTH);
            if (middleEarth != null) {
                return new TeleportTransition(middleEarth, LOTRUtumnoTeleporter.placeInPortal(middleEarth, 0, 0), Vec3.ZERO,
                        respawn.yRot(), respawn.xRot(), respawn.missingRespawnBlock(), false, Set.of(), respawn.postTeleportTransition());
            }
        }
        if (!(player.level() instanceof ServerLevel deathLevel) || deathLevel.dimension() != LOTRWorldGen.MIDDLE_EARTH
                || !player.isDeadOrDying()) {
            return respawn;
        }
        // getBedLocation(Middle-earth): a bed elsewhere does not count.
        boolean hasBed = respawn.newLevel() == deathLevel;
        if (!hasBed) {
            int i = LOTRLevelData.middleEarthPortalX;
            int k = LOTRLevelData.middleEarthPortalZ;
            respawn = new TeleportTransition(deathLevel, new Vec3(i + 0.5, surface(deathLevel, i, k), k + 0.5), Vec3.ZERO,
                    respawn.yRot(), respawn.xRot(), respawn.missingRespawnBlock(), false, Set.of(), respawn.postTeleportTransition());
        }
        if (LOTRConfig.middleEarthRespawning) {
            Vec3 deathPoint = player.position();
            double respawnThreshold = hasBed ? LOTRConfig.MERBedRespawnThreshold : LOTRConfig.MERWorldRespawnThreshold;
            if (deathPoint.distanceToSqr(respawn.position()) > respawnThreshold * respawnThreshold) {
                RandomSource rand = deathLevel.getRandom();
                double randomDistance = LOTRWorldGenUtil.getRandomIntegerInRange(rand, LOTRConfig.MERMinRespawn, LOTRConfig.MERMaxRespawn);
                float angle = rand.nextFloat() * Mth.PI * 2.0f;
                int i = Mth.floor(deathPoint.x) + (int) (randomDistance * Mth.sin(angle));
                int k = Mth.floor(deathPoint.z) + (int) (randomDistance * Mth.cos(angle));
                respawn = respawn.withPosition(new Vec3(i + 0.5, surface(deathLevel, i, k), k + 0.5));
            }
        }
        return respawn;
    }

    /** LOTRMod.getTrueTopBlock: one above the highest block that stops movement, leaves aside. */
    private static int surface(ServerLevel level, int i, int k) {
        level.getChunk(i >> 4, k >> 4);
        return LOTRWorldGenUtil.getTopSolidOrLiquidBlock(level, i, k);
    }
}
