package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBlastedLand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland.LOTRDunlandHillFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland.LOTRDunlendingCampfireStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland.LOTRDunlendingHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland.LOTRDunlendingTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukWargPitStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanBarrowStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRuinedRohanWatchtowerStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenAdornland.
 */
public class LOTRAdornlandBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGenStone = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRWorldGenerator boulderGenRohan = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 2, 1, 4);

    public LOTRAdornlandBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 10, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 8, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 4, 1, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.CREBAIN, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 40);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 10);
        npcSpawnList.newFactionList(50).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10);
        npcSpawnList.newFactionList(2).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 3);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2);
        npcSpawnList.newFactionList(5).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
        addBiomeVariant(LOTRBiomeVariant.BOULDERS_ROHAN);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.5f);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("rock"), 2, 60, LOTRLegacyBlocks.vanilla("stone")), 2.0f, 0, 64);
        decorator.setTreeCluster(12, 30);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 3;
        decorator.addTree(LOTRTreeType.OAK, 300);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.SPRUCE, 300);
        decorator.addTree(LOTRTreeType.BIRCH, 20);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
        decorator.addTree(LOTRTreeType.BEECH, 20);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 10);
        decorator.addTree(LOTRTreeType.CHESTNUT, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
        decorator.addTree(LOTRTreeType.FIR, 300);
        decorator.addTree(LOTRTreeType.PINE, 300);
        decorator.addTree(LOTRTreeType.APPLE, 2);
        decorator.addTree(LOTRTreeType.PEAR, 2);
        registerPlainsFlowers();
        addFlower(LOTRLegacyBlocks.mod("simbelmyne"), 0, 1);
        decorator.addRandomStructure(new LOTRRohanBarrowStructure(false), 4000);
        decorator.addRandomStructure(new LOTRDunlendingHouseStructure(false), 150);
        decorator.addRandomStructure(new LOTRDunlendingTavernStructure(false), 250);
        decorator.addRandomStructure(new LOTRDunlendingCampfireStructure(false), 200);
        decorator.addRandomStructure(new LOTRDunlandHillFortStructure(false), 700);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 3), 600);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 300);
        decorator.addRandomStructure(new LOTRUrukCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRUrukWargPitStructure(false), 3000);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 500);
        decorator.addRandomStructure(new LOTRRuinedRohanWatchtowerStructure(false), 500);
        decorator.addRandomStructure(new LOTRWorldGenBlastedLand(true), 400);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.DUNLAND, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.URUK_HAI, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.ROHAN, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int l;
        super.decorate(world, random, i, k);
        if (random.nextInt(60) == 0) {
            for (l = 0; l < 3; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGenStone.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(60) == 0) {
            for (l = 0; l < 3; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGenRohan.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ADORNLAND;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DUNLAND.getSubregion("adorn");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.ROHAN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.75f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.ROHAN_MIX.setRepair(0.8f);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.15f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
