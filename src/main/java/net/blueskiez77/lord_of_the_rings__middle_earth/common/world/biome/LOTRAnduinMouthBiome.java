package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;

/**
 * LOTRBiomeGenAnduinMouth.
 */
public class LOTRAnduinMouthBiome extends LOTRLebenninBiome {

    public LOTRAnduinMouthBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 2;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 5;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.waterlilyPerChunk = 5;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 4;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK, 200);
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.BIRCH, 100);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 100);
        registerSwampFlowers();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 500);
        decorator.clearVillages();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ANDUIN_MOUTH;
    }
}
