package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRCorsairEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRCampBaseStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRCorsairCampStructure extends LOTRCampBaseStructure {
    public LOTRCorsairCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRStructureBase2 createTent(boolean flag, RandomSource random) {
        return new LOTRCorsairTentStructure(false);
    }

    @Override
    public void generateCentrepiece(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        loadStrScan("corsair_camp_centre");
        generateStrScan(world, random, 0, 0, 0);
    }

    @Override
    public boolean generateFarm() {
        return false;
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        if (super.generateWithSetRotation(world, random, i, j, k, rotation)) {
            int i1;
            int k1;
            float ang;
            int r;
            //noinspection StatementWithEmptyBody
            for (int att = 0; att < 16 && !generateSubstructureWithRestrictionFlag(new LOTRCorsairCampCageStructure(notifyChanges), world, random, i1 = (int) ((r = Mth.randomBetweenInclusive(random, 8, 20)) * Mth.cos(ang = random.nextFloat() * 3.1415927f * 2.0f)), getTopBlock(world, i1, k1 = (int) (r * Mth.sin(ang))), k1, random.nextInt(4), true); ++att) {
            }
            int chestPiles = 1 + random.nextInt(2);
            block1:
            for (int l = 0; l < chestPiles; ++l) {
                for (int att = 0; att < 16; ++att) {
                    int j12;
                    float ang2;
                    int k12;
                    int r2 = Mth.randomBetweenInclusive(random, 8, 20);
                    int i12 = (int) (r2 * Mth.cos(ang2 = random.nextFloat() * 3.1415927f * 2.0f));
                    if (!isOpaque(world, i12, (j12 = getTopBlock(world, i12, k12 = (int) (r2 * Mth.sin(ang2)))) - 1, k12) || !isAir(world, i12, j12, k12) || !isAir(world, i12, j12 + 1, k12)) {
                        continue;
                    }
                    setBlockAndMetadata(world, i12, j12, k12, LOTRLegacyBlocks.mod("wood8"), 3);
                    setGrassToDirt(world, i12, j12 - 1, k12);
                    placeChest(world, random, i12, j12 + 1, k12, LOTRLegacyBlocks.mod("chestBasket"), 2, LOTRChestContents.CORSAIR, 3 + random.nextInt(3));
                    tryPlaceSideChest(world, random, i12 - 1, j12, k12, 5);
                    tryPlaceSideChest(world, random, i12 + 1, j12, k12, 4);
                    tryPlaceSideChest(world, random, i12, j12, k12 - 1, 2);
                    tryPlaceSideChest(world, random, i12, j12, k12 + 1, 3);
                    continue block1;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public LOTRNPCEntity getCampCaptain(WorldGenLevel world, RandomSource random) {
        return null;
    }

    @Override
    public void placeNPCRespawner(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.CORSAIR);
        respawner.setCheckRanges(24, -12, 12, 10);
        respawner.setSpawnRanges(8, -4, 4, 16);
        placeNPCRespawner(respawner, world, i, j, k);
        int corsairs = 3 + random.nextInt(5);
        for (int l = 0; l < corsairs; ++l) {
            LOTRCorsairEntity corsair = create(LOTREntities.CORSAIR, world);
            if (l == 0) {
                corsair = random.nextBoolean() ? create(LOTREntities.CORSAIR_CAPTAIN, world) : create(LOTREntities.CORSAIR_SLAVER, world);
            }
            int r = 4;
            float ang = random.nextFloat() * 3.1415927f * 2.0f;
            int i1 = (int) (r * Mth.cos(ang));
            int k1 = (int) (r * Mth.sin(ang));
            int j1 = getTopBlock(world, i1, k1);
            spawnNPCAndSetHome(corsair, world, i1, j1, k1, 16);
        }
    }

    public void tryPlaceSideChest(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        if (isOpaque(world, i, j - 1, k) && isAir(world, i, j, k)) {
            if (random.nextBoolean()) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("chestBasket"), meta);
            } else {
                placeChest(world, random, i, j, k, LOTRLegacyBlocks.mod("chestBasket"), meta, LOTRChestContents.CORSAIR, 1);
            }
        }
    }
}
