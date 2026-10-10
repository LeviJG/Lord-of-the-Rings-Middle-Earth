package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenMarshLights;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenDeadMarshes.
 */
public class LOTRDeadMarshesBiome extends LOTRBiome {

    public LOTRDeadMarshesBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        npcSpawnList.clear();
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("remains"), 6, LOTRLegacyBlocks.vanilla("dirt")), 5.0f, 55, 65);
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariant(LOTRBiomeVariant.SWAMP_LOWLAND);
        decorator.sandPerChunk = 0;
        decorator.clayPerChunk = 0;
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 0;
        decorator.logsPerChunk = 2;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 8;
        decorator.flowersPerChunk = 0;
        decorator.enableFern = true;
        decorator.enableSpecialGrasses = false;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 2;
        decorator.dryReedChance = 1.0f;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 1000);
        flowers.clear();
        addFlower(LOTRLegacyBlocks.mod("deadPlant"), 0, 10);
        biomeColors.setGrass(8348751);
        biomeColors.setSky(5657394);
        biomeColors.setClouds(10525542);
        biomeColors.setFog(4210724);
        biomeColors.setWater(1316367);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int j1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 6; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = random.nextInt(128);
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("deadPlant")).generate(world, random, i1, j1, k1);
        }
        for (l = 0; l < 4; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            //noinspection StatementWithEmptyBody
            for (j1 = 128; j1 > 0 && world.isEmptyBlock(new BlockPos(i1, j1 - 1, k1)); --j1) {
            }
            new LOTRWorldGenMarshLights().generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DEAD_MARSHES;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DEAD_MARSHES.getSubregion("deadMarshes");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.NINDALF;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
