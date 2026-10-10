package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;

import net.minecraft.util.RandomSource;

/**
 * LOTRBiomeGenGladdenFields.
 */
public class LOTRGladdenFieldsBiome extends LOTRAnduinBiome {

    public LOTRGladdenFieldsBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 10;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 8;
        decorator.waterlilyPerChunk = 4;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 3;
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 1000);
        registerSwampFlowers();
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 400);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_GLADDEN_FIELDS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHOVANION.getSubregion("gladden");
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(3) != 0) {
            LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
            doubleFlowerGen.setFlowerType(1);
            return doubleFlowerGen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
