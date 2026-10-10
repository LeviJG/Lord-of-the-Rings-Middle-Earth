package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRElfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRGaladhrimForgeStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenLothlorien.
 */
public class LOTRLothlorienBiome extends LOTRBiome {

    public LOTRLothlorienBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 20, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 30, 4, 6));
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GALADHRIM, 10).setSpawnChance(50);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GALADHRIM_WARRIORS, 2).setSpawnChance(50);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GALADHRIM_WARDENS, 1).setSpawnChance(50);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer4[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.2f;
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(EntityTypes.RABBIT, 6, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.SWAN, 15, 4, 8));
        variantChance = 0.7f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.CLEARING, 0.5f);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreQuendite"), 6), 6.0f, 0, 48);
        enablePodzol = false;
        decorator.treesPerChunk = 3;
        decorator.willowPerChunk = 2;
        decorator.flowersPerChunk = 6;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 2;
        decorator.generateLava = false;
        decorator.generateCobwebs = false;
        decorator.whiteSand = true;
        decorator.addTree(LOTRTreeType.OAK, 300);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 50);
        decorator.addTree(LOTRTreeType.LARCH, 200);
        decorator.addTree(LOTRTreeType.BEECH, 100);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 20);
        decorator.addTree(LOTRTreeType.MALLORN, 300);
        decorator.addTree(LOTRTreeType.MALLORN_BOUGHS, 600);
        decorator.addTree(LOTRTreeType.MALLORN_PARTY, 100);
        decorator.addTree(LOTRTreeType.MALLORN_EXTREME, 30);
        decorator.addTree(LOTRTreeType.ASPEN, 100);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 20);
        decorator.addTree(LOTRTreeType.LAIRELOSSE, 50);
        registerForestFlowers();
        addFlower(LOTRLegacyBlocks.mod("elanor"), 0, 30);
        addFlower(LOTRLegacyBlocks.mod("niphredil"), 0, 20);
        biomeColors.setGrass(11527451);
        biomeColors.setFog(16770660);
        decorator.addRandomStructure(new LOTRGaladhrimForgeStructure(false), 120);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 120; ++l) {
            int j1;
            int k1;
            int i1 = i + random.nextInt(16) + 8;
            if (!world.isEmptyBlock(new BlockPos(i1, (j1 = 60 + random.nextInt(50)) - 1, k1 = k + random.nextInt(16) + 8)) || !world.isEmptyBlock(new BlockPos(i1, j1, k1)) || !world.isEmptyBlock(new BlockPos(i1, j1 + 1, k1))) {
                continue;
            }
            LOTRLegacyBlocks.LegacyBlock torchBlock = LOTRElfHouseStructure.getRandomTorch(random);
            if (isUprightMallorn(world, i1 - 1, j1, k1) && world.isEmptyBlock(new BlockPos(i1 - 1, j1, k1 - 1)) && world.isEmptyBlock(new BlockPos(i1 - 1, j1, k1 + 1))) {
                world.setBlock(new BlockPos(i1, j1, k1), torchBlock.state(1), 2);
                continue;
            }
            if (isUprightMallorn(world, i1 + 1, j1, k1) && world.isEmptyBlock(new BlockPos(i1 + 1, j1, k1 - 1)) && world.isEmptyBlock(new BlockPos(i1 + 1, j1, k1 + 1))) {
                world.setBlock(new BlockPos(i1, j1, k1), torchBlock.state(2), 2);
                continue;
            }
            if (isUprightMallorn(world, i1, j1, k1 - 1) && world.isEmptyBlock(new BlockPos(i1 - 1, j1, k1 - 1)) && world.isEmptyBlock(new BlockPos(i1 + 1, j1, k1 - 1))) {
                world.setBlock(new BlockPos(i1, j1, k1), torchBlock.state(3), 2);
                continue;
            }
            if (!isUprightMallorn(world, i1, j1, k1 + 1) || !world.isEmptyBlock(new BlockPos(i1 - 1, j1, k1 + 1)) || !world.isEmptyBlock(new BlockPos(i1 + 1, j1, k1 + 1))) {
                continue;
            }
            world.setBlock(new BlockPos(i1, j1, k1), torchBlock.state(4), 2);
        }
    }

    /** {@code world.getBlock(..) == LOTRMod.wood && meta == 1}: an upright mallorn log. */
    private static boolean isUprightMallorn(WorldGenLevel world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k)) == LOTRLegacyBlocks.mod("wood").state(1);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LOTHLORIEN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.LOTHLORIEN.getSubregion("lothlorien");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.LOTHLORIEN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.GALADHRIM;
    }

    @Override
    public boolean hasSeasonalGrass() {
        return false;
    }
}
