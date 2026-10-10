package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;

/**
 * LOTRBiomeGenEntwashMouth.
 */
public class LOTREntwashMouthBiome extends LOTRGondorBiome {

    public LOTREntwashMouthBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 2;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 2;
        decorator.flowersPerChunk = 3;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.waterlilyPerChunk = 2;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 4;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK_TALL, 100);
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 600);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 400);
        registerSwampFlowers();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 500);
        decorator.clearVillages();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ENTWASH_MOUTH;
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return null;
    }
}
