package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRRuinedDwarvenTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenGreyMountains.
 */
public class LOTRGreyMountainsBiome extends LOTRBiome {

    public LOTRGreyMountainsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 20);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 7);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SNOW_TROLLS, 1).setSpawnChance(5000);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 30).setConquestOnly();
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 20).setConquestOnly();
        arrspawnListContainer2[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SNOW_TROLLS, 6).setConquestOnly();
        npcSpawnList.newFactionList(20).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLUE_DWARVES, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DWARVES, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        decorator.biomeGemFactor = 1.0f;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 0;
        decorator.doubleGrassPerChunk = 0;
        decorator.addTree(LOTRTreeType.SPRUCE, 400);
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 400);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA, 50);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA_THIN, 10);
        decorator.addTree(LOTRTreeType.LARCH, 500);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        registerMountainsFlowers();
        biomeColors.setSky(10862798);
        decorator.generateOrcDungeon = true;
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.DWARVEN(1, 4), 1000);
        decorator.addRandomStructure(new LOTRRuinedDwarvenTowerStructure(false), 1000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.DWARF, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int count;
        super.decorate(world, random, i, k);
        for (count = 0; count < 3; ++count) {
            int k1;
            i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 >= 100) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        for (count = 0; count < 2; ++count) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            if (j1 >= 100) {
                continue;
            }
            getRandomWorldGenForGrass(random).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int snowHeight = 150 - rockDepth;
        int stoneHeight = snowHeight - 40;
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
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_GREY_MOUNTAINS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.GREY_MOUNTAINS.getSubregion("greyMountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.GREY_MOUNTAINS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.0f;
    }
}
