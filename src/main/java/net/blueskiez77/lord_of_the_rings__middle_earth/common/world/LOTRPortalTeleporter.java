package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRTeleporterElvenPortal and LOTRTeleporterMorgulPortal: the way through a built portal, to the same
 * place in the other world. One comes out beside the nearest portal of the same kind within 128 blocks;
 * failing one, a portal is built at the nearest flat, clear spot within 16 (on a dirt or rock floor at
 * 70 if there is none) and one comes out beside that.
 *
 * <p>The search for a standing portal goes through its blocks' points of interest, as vanilla's own
 * portals' does, not block by block over the whole range; the original's cache of portals found by
 * chunk is left out with it.
 */
public abstract class LOTRPortalTeleporter {

    public static final LOTRPortalTeleporter ELVEN = new LOTRPortalTeleporter(2) {
        @Override
        protected Block portalBlock() {
            return net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks.ELVEN_PORTAL;
        }

        @Override
        protected PoiType poiType() {
            return LOTRPortals.ELVEN_PORTAL_POI;
        }

        @Override
        protected BlockState frameBlock(int i1, int j1, int k1) {
            if (j1 > 0) {
                return Blocks.AIR.defaultBlockState();
            }
            if (j1 < 0) {
                return Blocks.DIRT.defaultBlockState();
            }
            boolean isFrame = i1 == -2 || i1 == 2 || k1 == -2 || k1 == 2;
            return isFrame ? net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.QUENDITE_GRASS.defaultBlockState()
                    : portalBlock().defaultBlockState();
        }
    };

    public static final LOTRPortalTeleporter MORGUL = new LOTRPortalTeleporter(3) {
        @Override
        protected Block portalBlock() {
            return net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks.MORGUL_PORTAL;
        }

        @Override
        protected PoiType poiType() {
            return LOTRPortals.MORGUL_PORTAL_POI;
        }

        @Override
        protected BlockState frameBlock(int i1, int j1, int k1) {
            if (j1 > 0) {
                return Math.abs(i1) == 2 && Math.abs(k1) == 2
                        ? net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.GULDURIL_MORDOR_BRICK.defaultBlockState()
                        : Blocks.AIR.defaultBlockState();
            }
            if (j1 < 0) {
                return net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.MORDOR_ROCK.defaultBlockState();
            }
            boolean isFrame = i1 == -2 || i1 == 2 || k1 == -2 || k1 == 2;
            return isFrame ? net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.MORDOR_ROCK.defaultBlockState()
                    : portalBlock().defaultBlockState();
        }
    };

    /** How high above the portal the space is cleared: 2 for the elven, 3 for the Morgul (its pillars). */
    private final int clearHeight;

    private LOTRPortalTeleporter(int clearHeight) {
        this.clearHeight = clearHeight;
    }

    protected abstract Block portalBlock();

    protected abstract PoiType poiType();

    /** makePortal: the block at (i1, j1, k1) from the portal's centre -- frame, portal, cleared space, or floor below. */
    protected abstract BlockState frameBlock(int i1, int j1, int k1);

    /** placeInPortal: beside the nearest such portal in {@code target}, built first if there is none. */
    public TeleportTransition transition(ServerLevel target, Entity entity) {
        Vec3 pos = placeInExistingPortal(target, entity);
        if (pos == null) {
            makePortal(target, entity);
            pos = placeInExistingPortal(target, entity);
        }
        if (pos == null) {
            pos = entity.position();
        }
        return new TeleportTransition(target, pos, Vec3.ZERO, entity.getYRot(), entity.getXRot(), Set.of(),
                TeleportTransition.PLACE_PORTAL_TICKET);
    }

    /** placeInExistingPortal: the centre of a whole 3x3 portal nearest the entity, and one of its four sides at random. */
    private Vec3 placeInExistingPortal(ServerLevel world, Entity entity) {
        int range = 128;
        BlockPos centre = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        PoiManager poi = world.getPoiManager();
        poi.ensureLoadedAndValid(world, centre, range);
        Block portal = portalBlock();
        Optional<BlockPos> found = poi.getInSquare(holder -> holder.value() == poiType(), centre, range, PoiManager.Occupancy.ANY)
                .map(record -> record.getPos())
                .filter(p -> {
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            if (!world.getBlockState(p.offset(i2, 0, k2)).is(portal)) {
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .min(Comparator.comparingDouble(p -> {
                    double xDistance = p.getX() + 0.5 - entity.getX();
                    double yDistance = p.getY() + 0.5 - entity.getY();
                    double zDistance = p.getZ() + 0.5 - entity.getZ();
                    return xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;
                }));
        if (found.isEmpty()) {
            return null;
        }
        int i = found.get().getX();
        int j = found.get().getY();
        int k = found.get().getZ();
        RandomSource rand = world.getRandom();
        return switch (rand.nextInt(4)) {
            case 0 -> new Vec3(i - 2 + 0.5, j + 1, k - 1 + 0.25 + rand.nextFloat() * 2.0f);
            case 1 -> new Vec3(i + 2 + 0.5, j + 1, k - 1 + 0.25 + rand.nextFloat() * 2.0f);
            case 2 -> new Vec3(i - 1 + 0.25 + rand.nextFloat() * 2.0f, j + 1, k - 2 + 0.25);
            default -> new Vec3(i - 1 + 0.25 + rand.nextFloat() * 2.0f, j + 1, k + 2 + 0.25);
        };
    }

    /** makePortal: on the nearest spot within 16 with a solid 5x5 floor and the space above it clear. */
    private void makePortal(ServerLevel world, Entity entity) {
        int range = 16;
        double distanceToPortal = -1.0;
        int i = Mth.floor(entity.getX());
        int j = Mth.floor(entity.getY());
        int k = Mth.floor(entity.getZ());
        int posX = i;
        int posY = j;
        int posZ = k;
        int top = world.getMaxY();
        for (int i1 = i - range; i1 <= i + range; ++i1) {
            double xDistance = i1 + 0.5 - entity.getX();
            for (int k1 = k - range; k1 <= k + range; ++k1) {
                double zDistance = k1 + 0.5 - entity.getZ();
                search:
                for (int j1 = top; j1 >= world.getMinY(); --j1) {
                    if (!world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                        continue;
                    }
                    while (j1 > world.getMinY() && world.isEmptyBlock(new BlockPos(i1, j1 - 1, k1))) {
                        --j1;
                    }
                    for (int i2 = i1 - 2; i2 <= i1 + 2; ++i2) {
                        for (int k2 = k1 - 2; k2 <= k1 + 2; ++k2) {
                            for (int j2 = -1; j2 <= this.clearHeight; ++j2) {
                                BlockPos p = new BlockPos(i2, j1 + j2, k2);
                                if (j2 < 0 && !LOTRFeature.isOpaqueCube(world.getBlockState(p)) || j2 >= 0 && !world.isEmptyBlock(p)) {
                                    continue search;
                                }
                            }
                        }
                    }
                    double yDistance = j1 + 0.5 - entity.getY();
                    double distanceSq = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;
                    if (distanceToPortal >= 0.0 && distanceSq >= distanceToPortal) {
                        continue;
                    }
                    distanceToPortal = distanceSq;
                    posX = i1;
                    posY = j1;
                    posZ = k1;
                }
            }
        }
        boolean floorBelow = false;
        if (distanceToPortal < 0.0) {
            posY = Mth.clamp(posY, 70, top + 1 - 10);
            floorBelow = true;
        }
        for (int i1 = -2; i1 <= 2; ++i1) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                for (int j1 = floorBelow ? -1 : 0; j1 <= this.clearHeight; ++j1) {
                    world.setBlock(new BlockPos(posX + i1, posY + j1, posZ + k1), frameBlock(i1, j1, k1), Block.UPDATE_CLIENTS);
                }
            }
        }
        for (int i1 = -3; i1 <= 3; ++i1) {
            for (int k1 = -3; k1 <= 3; ++k1) {
                for (int j1 = floorBelow ? -2 : -1; j1 <= this.clearHeight + 1; ++j1) {
                    BlockPos p = new BlockPos(posX + i1, posY + j1, posZ + k1);
                    world.updateNeighborsAt(p, world.getBlockState(p).getBlock());
                }
            }
        }
    }
}
