package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRWebOfUngoliantStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorSpiderPitStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenNanUngol.
 */
public class LOTRNanUngolBiome extends LOTRMordorBiome {

    public LOTRNanUngolBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 2);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_SPIDERS, 100);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        npcSpawnList.conquestGainRate = 0.5f;
        clearBiomeVariants();
        decorator.generateCobwebs = false;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRMordorSpiderPitStructure(false), 40);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 4; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRWebOfUngoliantStructure(false, 64).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NAN_UNGOL;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("nanUngol");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.NAN_UNGOL;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.75f;
    }
}
