package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;

/**
 * LOTRBiomeGenSwanfleet.
 */
public class LOTRSwanfleetBiome extends LOTREriadorBiome {

    public LOTRSwanfleetBiome(int i, boolean major) {
        super(i, major);
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.SWAN, 20, 4, 8));
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 4;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 8;
        decorator.enableFern = true;
        decorator.waterlilyPerChunk = 4;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 3;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK_TALL, 500);
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.BIRCH, 200);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 100);
        registerSwampFlowers();
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 400);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_SWANFLEET;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ERIADOR.getSubregion("swanfleet");
    }
}
