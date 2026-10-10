package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;

/**
 * LOTRBiomeGenLongMarshes.
 */
public class LOTRLongMarshesBiome extends LOTRWilderlandNorthBiome {

    public LOTRLongMarshesBiome(int i, boolean major) {
        super(i, major);
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.GORCROW, 8, 4, 4));
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 2;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.waterlilyPerChunk = 4;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 6;
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 1000);
        decorator.addTree(LOTRTreeType.GREEN_OAK, 200);
        registerSwampFlowers();
        biomeColors.setSky(13230818);
        biomeColors.setFog(12112325);
        biomeColors.setFoggy(true);
        biomeColors.setWater(8167049);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 400);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.clearInvasions();
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LONG_MARSHES;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHOVANION.getSubregion("longMarshes");
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
