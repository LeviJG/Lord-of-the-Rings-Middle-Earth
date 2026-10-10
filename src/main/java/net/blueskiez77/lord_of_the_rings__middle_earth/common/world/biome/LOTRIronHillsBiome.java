package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDwarfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDwarfSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDwarvenTowerStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenIronHills.
 */
public class LOTRIronHillsBiome extends LOTRBiome {

    public LOTRIronHillsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.WILD_BOAR, 50, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 8, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DWARVES, 10);
        npcSpawnList.newFactionList(500).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2).setConquestOnly();
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer2[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0, 1.0f).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0, 1.0f).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 1);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0, 1.0f).add(arrspawnListContainer4);
        variantChance = 0.3f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.biomeGemFactor = 0.75f;
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("iron_ore"), 4), 20.0f, 0, 96);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("gold_ore"), 4), 2.0f, 0, 48);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSilver"), 4), 2.0f, 0, 48);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGlowstone"), 4), 8.0f, 0, 48);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.generateWater = false;
        decorator.generateLava = false;
        decorator.generateCobwebs = false;
        decorator.addTree(LOTRTreeType.SPRUCE, 500);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA, 200);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA_THIN, 50);
        decorator.addTree(LOTRTreeType.FIR, 400);
        decorator.addTree(LOTRTreeType.PINE, 400);
        registerMountainsFlowers();
        addFlower(LOTRLegacyBlocks.mod("dwarfHerb"), 0, 1);
        decorator.addRandomStructure(new LOTRDwarvenTowerStructure(false), 300);
        decorator.addRandomStructure(new LOTRDwarfSmithyStructure(false), 150);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 4; ++l) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = 70 + random.nextInt(60);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRDwarfHouseStructure(false).generate(world, random, i1, j1, k1);
        }
        for (l = 0; l < 8; ++l) {
            int k1;
            i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 <= 80) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_IRON_HILLS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DWARVEN.getSubregion("ironHills");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.IRON_HILLS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DWARVEN;
    }
}
