package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRBlueMountainsHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRBlueMountainsSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRBlueMountainsStrongholdStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenBlueMountains.
 */
public class LOTRBlueMountainsBiome extends LOTRBiome {

    public LOTRBlueMountainsBiome(int i, boolean major) {
        super(i, major);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLUE_DWARVES, 10);
        npcSpawnList.newFactionList(600).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 4);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 1);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(1, 2.0f).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 4);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 1);
        npcSpawnList.newFactionList(0, 2.0f).add(arrspawnListContainer3);
        variantChance = 0.2f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        decorator.biomeGemFactor = 1.0f;
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("rock"), 3, 60, LOTRLegacyBlocks.vanilla("stone")), 6.0f, 0, 96);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("coal_ore"), 8), 10.0f, 0, 128);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("iron_ore"), 4), 10.0f, 0, 96);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGlowstone"), 4), 8.0f, 0, 48);
        decorator.treesPerChunk = 1;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.generateWater = false;
        decorator.generateLava = false;
        decorator.generateCobwebs = false;
        decorator.addTree(LOTRTreeType.OAK, 300);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.SPRUCE, 500);
        decorator.addTree(LOTRTreeType.BIRCH, 400);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        registerMountainsFlowers();
        addFlower(LOTRLegacyBlocks.mod("dwarfHerb"), 0, 1);
        biomeColors.setSky(7506425);
        decorator.addRandomStructure(new LOTRBlueMountainsStrongholdStructure(false), 400);
        decorator.addRandomStructure(new LOTRBlueMountainsSmithyStructure(false), 150);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 4; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = 70 + random.nextInt(80);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRBlueMountainsHouseStructure(false).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int snowHeight = 110 - rockDepth;
        int stoneHeight = snowHeight - 20;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (j >= snowHeight && block.is(topBlock.getBlock())) {
                blocks[index] = LOTRLegacyBlocks.vanilla("snow").state(0);
                continue;
            }
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.mod("rock").state(3);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_BLUE_MOUNTAINS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DWARVEN.getSubregion("blueMountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.BLUE_MOUNTAINS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.2f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DWARVEN;
    }
}
