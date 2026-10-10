package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRBeaconTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenWhiteMountains.
 */
public class LOTRWhiteMountainsBiome extends LOTRGondorBiome {

    public LOTRWhiteMountainsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DWARVES, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_HILLMEN, 5);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_SOLDIERS, 2);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LOSSARNACH_SOLDIERS, 2);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACKROOT_SOLDIERS, 1);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[6];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 20);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 1);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 4).setConquestThreshold(50.0f);
        arrspawnListContainer4[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer4[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer4[5] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        clearBiomeVariants();
        variantChance = 0.2f;
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.3f);
        decorator.biomeGemFactor = 1.0f;
        decorator.treesPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 2;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK, 100);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 50);
        decorator.addTree(LOTRTreeType.BIRCH, 20);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 5);
        decorator.addTree(LOTRTreeType.BEECH, 20);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 5);
        decorator.addTree(LOTRTreeType.SPRUCE, 300);
        decorator.addTree(LOTRTreeType.LARCH, 300);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        registerMountainsFlowers();
        decorator.clearVillages();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRBeaconTowerStructure(false), 100);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int stoneHeight = 100 - rockDepth;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.mod("rock").state(1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_WHITE_MOUNTAINS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.GONDOR.getSubregion("whiteMountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.WHITE_MOUNTAINS;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
