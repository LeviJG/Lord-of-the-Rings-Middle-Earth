package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPortalEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRTeleporter: the way through the Ring portal. Into Middle-earth one comes out at (0, 0) -- the
 * Shire -- one above its true top block; into the overworld, where the overworld's Ring portal was
 * made. The first coming to Middle-earth marks its portal made and, through a Ring portal (rather
 * than by the world type's first sending), raises Middle-earth's own Ring portal over that spot,
 * which becomes its spawn point.
 */
public final class LOTRTeleporter {

    private LOTRTeleporter() {
    }

    /** placeInPortal: where in {@code target} the entity comes out, the Ring portal raised as it arrives. */
    public static TeleportTransition transition(ServerLevel target, Entity entity, boolean makeRingPortal) {
        int i;
        int j;
        int k;
        boolean middleEarth = target.dimension() == LOTRWorldGen.MIDDLE_EARTH;
        if (middleEarth) {
            i = 0;
            k = 0;
            target.getChunk(i >> 4, k >> 4);
            j = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(target, i, k);
        } else {
            i = LOTRLevelData.overworldPortalX;
            k = LOTRLevelData.overworldPortalZ;
            j = LOTRLevelData.overworldPortalY;
        }
        int fi = i;
        int fj = j;
        int fk = k;
        return new TeleportTransition(target, new Vec3(i + 0.5, j + 1.0, k + 0.5), Vec3.ZERO, entity.getYRot(), 0.0f, Set.of(),
                arrived -> {
                    arrived.resetFallDistance();
                    if (middleEarth) {
                        arriveInMiddleEarth(target, fi, fj, fk, makeRingPortal);
                    }
                });
    }

    /** The first coming to Middle-earth: the portal marked made, and with the Ring portal, raised. */
    public static void arriveInMiddleEarth(ServerLevel middleEarth, int i, int j, int k, boolean makeRingPortal) {
        if (LOTRLevelData.madeMiddleEarthPortal != 0) {
            return;
        }
        LOTRLevelData.setMadeMiddleEarthPortal(1);
        if (makeRingPortal) {
            LOTRLevelData.markMiddleEarthPortalLocation(i, j, k);
            LOTRPortalEntity portal = LOTREntities.PORTAL.create(middleEarth, EntitySpawnReason.TRIGGERED);
            if (portal != null) {
                portal.snapTo(i + 0.5, j + 3.5, k + 0.5, 0.0f, 0.0f);
                middleEarth.addFreshEntity(portal);
            }
        }
    }
}
