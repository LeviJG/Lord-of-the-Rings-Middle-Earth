package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;

import net.minecraft.world.entity.EntityTypes;

/**
 * LOTRBiomeGenChetwood.
 */
public class LOTRChetwoodBiome extends LOTRBreelandBiome {

    public LOTRChetwoodBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 4, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 20, 4, 6));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10).setSpawnChance(500);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 4;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 2;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        registerForestFlowers();
        decorator.clearVillages();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_CHETWOOD;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.BREE.getSubregion("chetwood");
    }
}
