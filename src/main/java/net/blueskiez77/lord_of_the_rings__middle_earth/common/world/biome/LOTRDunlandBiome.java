package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenDunland.
 */
public class LOTRDunlandBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRDunlandBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 8, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 4, 1, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.CREBAIN, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 3);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 1);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10);
        npcSpawnList.newFactionList(2).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ISENGARD_SNAGA, 5);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 4);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        npcSpawnList.conquestGainRate = 0.5f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.4f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.4f);
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.SPRUCE, 500);
        decorator.addTree(LOTRTreeType.OAK_TALL, 200);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 20);
        decorator.addTree(LOTRTreeType.CHESTNUT, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        registerForestFlowers();
        decorator.addRandomStructure(new LOTRDunlendingHouseStructure(false), 25);
        decorator.addRandomStructure(new LOTRDunlendingTavernStructure(false), 100);
        decorator.addRandomStructure(new LOTRDunlendingCampfireStructure(false), 40);
        decorator.addRandomStructure(new LOTRDunlandHillFortStructure(false), 150);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 3), 500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 300);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.RANGER_NORTH, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.ROHAN, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        super.decorate(world, random, i, k);
        for (int count = 0; count < 6; ++count) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 <= 85) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        if (random.nextInt(8) == 0) {
            for (int l = 0; l < 4; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DUNLAND;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DUNLAND.getSubregion("dunland");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.DUNLAND;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.75f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
