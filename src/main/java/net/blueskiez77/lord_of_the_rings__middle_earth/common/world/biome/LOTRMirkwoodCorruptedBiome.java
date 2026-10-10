package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRWebOfUngoliantStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenMirkwoodCorrupted.
 */
public class LOTRMirkwoodCorruptedBiome extends LOTRMirkwoodBiome {

    public LOTRMirkwoodCorruptedBiome(int i, boolean major) {
        super(i, major);
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.GORCROW, 6, 4, 4));
        variantChance = 0.2f;
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        decorator.treesPerChunk = 8;
        decorator.willowPerChunk = 1;
        decorator.vinesPerChunk = 20;
        decorator.logsPerChunk = 3;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 6;
        decorator.enableFern = true;
        decorator.mushroomsPerChunk = 4;
        decorator.generateCobwebs = false;
        decorator.addTree(LOTRTreeType.MIRK_OAK_LARGE, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.PINE, 400);
        biomeColors.setGrass(2841381);
        biomeColors.setFoliage(2503461);
        biomeColors.setFog(3302525);
        biomeColors.setFoggy(true);
        biomeColors.setWater(1708838);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.WOOD_ELF, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int l;
        super.decorate(world, random, i, k);
        if (decorator.treesPerChunk > 2) {
            for (l = 0; l < decorator.treesPerChunk / 2; ++l) {
                i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
                LOTRTreeType.MIRK_OAK.create(false, random).generate(world, random, i1, j1, k1);
            }
        }
        for (l = 0; l < 6; ++l) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRWebOfUngoliantStructure(false, 64).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MIRKWOOD.getSubregion("mirkwood");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }
}
