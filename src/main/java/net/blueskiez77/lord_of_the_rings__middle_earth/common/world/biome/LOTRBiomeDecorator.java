package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGen;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBerryBush;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBiomeFlowers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBushes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenCaveCobwebs;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenCorn;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenFallenLeaves;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenLogs;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenReeds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStalactites;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStreams;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSurfaceGravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenTrollHoard;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRGrukHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRMarshHutStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTROrcDungeonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun.LOTRTicketBoothStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

/**
 * LOTRBiomeDecorator: what a biome strews over its chunks -- soils, ores and gems, sand and clay,
 * trees by kind and weight, flowers, grass, reeds, mushrooms, cane, cacti, melons, water and lava
 * springs, random structures and villages -- and how much of each.
 */
public class LOTRBiomeDecorator {

    public final LOTRBiome biome;
    public final Collection<OreGenerant> biomeSoils = new ArrayList<>();
    public final Collection<OreGenerant> biomeOres = new ArrayList<>();
    public final Collection<OreGenerant> biomeGems = new ArrayList<>();
    public float biomeOreFactor = 1.0f;
    public float biomeGemFactor = 0.5f;
    public int sandPerChunk = 4;
    public int clayPerChunk = 3;
    public int quagmirePerChunk;
    public int treesPerChunk;
    public int willowPerChunk;
    public int logsPerChunk;
    public int vinesPerChunk;
    public int flowersPerChunk = 2;
    public int doubleFlowersPerChunk;
    public int grassPerChunk = 1;
    public int doubleGrassPerChunk;
    public boolean enableFern;
    public boolean enableSpecialGrasses = true;
    public int deadBushPerChunk;
    public int waterlilyPerChunk;
    public int mushroomsPerChunk;
    public boolean enableRandomMushroom = true;
    public int canePerChunk;
    public int reedPerChunk = 1;
    public float dryReedChance = 0.1f;
    public int cornPerChunk;
    public int cactiPerChunk;
    public float melonPerChunk;
    public boolean generateWater = true;
    public boolean generateLava = true;
    public boolean generateCobwebs = true;
    public boolean generateAthelas;
    public boolean whiteSand;
    public int treeClusterSize;
    public int treeClusterChance = -1;
    public boolean generateOrcDungeon;
    public boolean generateTrollHoard;
    public final Collection<LOTRTreeType.WeightedTreeType> treeTypes = new ArrayList<>();
    public final Collection<RandomStructure> randomStructures = new ArrayList<>();
    public final Collection<LOTRVillageGen> villages = new ArrayList<>();

    public LOTRWorldGenerator clayGen = new LOTRWorldGenSand(LOTRLegacyBlocks.vanilla("clay"), 5, 1);
    public LOTRWorldGenerator sandGen = new LOTRWorldGenSand(LOTRLegacyBlocks.vanilla("sand"), 7, 2);
    public LOTRWorldGenerator whiteSandGen = new LOTRWorldGenSand(LOTRLegacyBlocks.mod("whiteSand"), 7, 2);
    public LOTRWorldGenerator quagmireGen = new LOTRWorldGenSand(LOTRLegacyBlocks.mod("quagmire"), 7, 2);
    public LOTRWorldGenerator surfaceGravelGen = new LOTRWorldGenSurfaceGravel();
    public LOTRWorldGenerator flowerGen = new LOTRWorldGenBiomeFlowers();
    public LOTRWorldGenerator logGen = new LOTRWorldGenLogs();
    public LOTRWorldGenerator mushroomBrownGen = new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.vanilla("brown_mushroom"));
    public LOTRWorldGenerator mushroomRedGen = new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.vanilla("red_mushroom"));
    public LOTRWorldGenerator caneGen = new LOTRVanillaWorldGens.Reed();
    public LOTRWorldGenerator reedGen = new LOTRWorldGenReeds(LOTRLegacyBlocks.mod("reeds"));
    public LOTRWorldGenerator dryReedGen = new LOTRWorldGenReeds(LOTRLegacyBlocks.mod("driedReeds"));
    public LOTRWorldGenerator cornGen = new LOTRWorldGenCorn();
    public LOTRWorldGenerator pumpkinGen = new LOTRVanillaWorldGens.Gourd(true);
    public LOTRWorldGenerator waterlilyGen = new LOTRVanillaWorldGens.Waterlily();
    public LOTRWorldGenerator cobwebGen = new LOTRWorldGenCaveCobwebs();
    public LOTRWorldGenerator stalactiteGen = new LOTRWorldGenStalactites();
    public LOTRWorldGenerator vinesGen = new LOTRVanillaWorldGens.Vines();
    public LOTRWorldGenerator cactusGen = new LOTRVanillaWorldGens.Cactus();
    public LOTRWorldGenerator melonGen = new LOTRVanillaWorldGens.Gourd(false);
    public LOTRWorldGenerator orcDungeonGen = new LOTROrcDungeonStructure(false);
    public LOTRWorldGenerator trollHoardGen = new LOTRWorldGenTrollHoard();

    public LOTRBiomeDecorator(LOTRBiome biome) {
        this.biome = biome;
        addDefaultOres();
    }

    public void addDefaultOres() {
        addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("dirt"), 32), 40.0f, 0, 256);
        addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("Gravel"), 32), 20.0f, 0, 256);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("coal_ore"), 16), 40.0f, 0, 128);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreCopper"), 8), 16.0f, 0, 128);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreTin"), 8), 16.0f, 0, 128);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("iron_ore"), 8), 20.0f, 0, 64);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSulfur"), 8), 2.0f, 0, 64);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSaltpeter"), 8), 2.0f, 0, 64);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSalt"), 12), 2.0f, 0, 64);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("gold_ore"), 8), 2.0f, 0, 32);
        addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSilver"), 8), 3.0f, 0, 32);
        LOTRLegacyBlocks.LegacyBlock gem = LOTRLegacyBlocks.mod("oreGem");
        addGem(new LOTRVanillaWorldGens.Minable(gem, 1, 6, LOTRLegacyBlocks.vanilla("stone")), 2.0f, 0, 64);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 0, 6, LOTRLegacyBlocks.vanilla("stone")), 2.0f, 0, 64);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 4, 5, LOTRLegacyBlocks.vanilla("stone")), 1.5f, 0, 48);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 6, 5, LOTRLegacyBlocks.vanilla("stone")), 1.5f, 0, 48);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 2, 4, LOTRLegacyBlocks.vanilla("stone")), 1.0f, 0, 32);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 3, 4, LOTRLegacyBlocks.vanilla("stone")), 1.0f, 0, 32);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 7, 4, LOTRLegacyBlocks.vanilla("stone")), 0.75f, 0, 24);
        addGem(new LOTRVanillaWorldGens.Minable(gem, 5, 4, LOTRLegacyBlocks.vanilla("stone")), 0.5f, 0, 16);
    }

    public void addSoil(LOTRWorldGenerator gen, float f, int min, int max) {
        this.biomeSoils.add(new OreGenerant(gen, f, min, max));
    }

    public void addOre(LOTRWorldGenerator gen, float f, int min, int max) {
        this.biomeOres.add(new OreGenerant(gen, f, min, max));
    }

    public void addGem(LOTRWorldGenerator gen, float f, int min, int max) {
        this.biomeGems.add(new OreGenerant(gen, f, min, max));
    }

    public void clearOres() {
        this.biomeSoils.clear();
        this.biomeOres.clear();
        this.biomeGems.clear();
    }

    public void addTree(LOTRTreeType type, int weight) {
        this.treeTypes.add(new LOTRTreeType.WeightedTreeType(type, weight));
    }

    public void clearTrees() {
        this.treeTypes.clear();
    }

    public void addRandomStructure(LOTRWorldGenerator structure, int chunkChance) {
        this.randomStructures.add(new RandomStructure(structure, chunkChance));
    }

    public void clearRandomStructures() {
        this.randomStructures.clear();
    }

    public void addVillage(LOTRVillageGen village) {
        this.villages.add(village);
    }

    public void clearVillages() {
        this.villages.clear();
    }

    public boolean anyFixedVillagesAt(int i, int k) {
        for (LOTRVillageGen village : this.villages) {
            if (village.anyFixedVillagesAt(i, k)) {
                return true;
            }
        }
        return false;
    }

    /** checkForVillages: whether any of its villages reaches this spot, and whether a flat one does. */
    public VillageFlags checkForVillages(long worldSeed, LOTRWorldChunkManager chunkManager, int i, int k) {
        boolean isVillage = false;
        boolean isFlatVillage = false;
        for (LOTRVillageGen village : this.villages) {
            List<LOTRVillageGen.AbstractInstance<?>> instances = village.getNearbyVillagesAtPosition(worldSeed, chunkManager, i, k);
            if (instances.isEmpty()) {
                continue;
            }
            isVillage = true;
            for (LOTRVillageGen.AbstractInstance<?> inst : instances) {
                if (inst.isFlat()) {
                    isFlatVillage = true;
                }
            }
        }
        return new VillageFlags(isVillage, isFlatVillage);
    }

    public record VillageFlags(boolean isVillage, boolean isFlatVillage) {
    }

    /** genTree: one of the biome's trees for the variant here, grown. */
    public void genTree(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        this.biome.getTreeGen(world, random, i, j, k).generate(world, random, i, j, k);
    }

    public void setTreeCluster(int size, int chance) {
        this.treeClusterSize = size;
        this.treeClusterChance = chance;
    }

    public void resetTreeCluster() {
        setTreeCluster(0, -1);
    }

    public LOTRTreeType getRandomTree(RandomSource random) {
        if (this.treeTypes.isEmpty()) {
            return LOTRTreeType.OAK;
        }
        return LOTRWeightedRandom.getRandomItem(random, this.treeTypes).treeType();
    }

    public LOTRTreeType getRandomTreeForVariant(RandomSource random, LOTRBiomeVariant variant) {
        if (variant.treeTypes.isEmpty()) {
            return getRandomTree(random);
        }
        if (random.nextFloat() < variant.variantTreeChance) {
            return variant.getRandomTree(random);
        }
        return getRandomTree(random);
    }

    public int getVariantTreesPerChunk(LOTRBiomeVariant variant) {
        int trees = this.treesPerChunk;
        if (variant.treeFactor > 1.0f) {
            trees = Math.max(trees, 1);
        }
        return Math.round(trees * variant.treeFactor);
    }

    /**
     * decorate: everything the biome strews over the chunk whose corner is block i, k, offset by
     * eight as 1.7.10 populated -- in the original's order and from its random draws.
     */
    public void decorate(WorldGenLevel world, RandomSource rand, int chunkX, int chunkZ) {
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(world);
        LOTRBiomeVariant biomeVariant = chunkManager == null ? LOTRBiomeVariant.STANDARD
                : chunkManager.getBiomeVariantAt(chunkX + 8, chunkZ + 8);
        generateOres(world, rand, chunkX, chunkZ);
        biomeVariant.decorateVariant(world, rand, chunkX, chunkZ, this.biome);
        if (rand.nextBoolean() && this.generateCobwebs) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(60);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.cobwebGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < 3; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(60);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.stalactiteGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.quagmirePerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.quagmireGen.generate(world, rand, i, topSolid(world, i, k), k);
        }
        for (int l = 0; l < this.sandPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            (this.whiteSand ? this.whiteSandGen : this.sandGen).generate(world, rand, i, topSolid(world, i, k), k);
        }
        for (int l = 0; l < this.clayPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.clayGen.generate(world, rand, i, topSolid(world, i, k), k);
        }
        if (rand.nextInt(60) == 0) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.surfaceGravelGen.generate(world, rand, i, 0, k);
        }
        if (!biomeVariant.disableStructures && Math.abs(chunkX) > 32 && Math.abs(chunkZ) > 32) {
            long seed = chunkX * 1879267L ^ chunkZ * 67209689L;
            seed = seed * seed * 5829687L + seed * 2876L;
            RandomSource structureRand = new LegacyRandomSource(seed);
            boolean roadNear = LOTRRoads.isRoadNear(chunkX + 8, chunkZ + 8, 16) >= 0.0f;
            if (!roadNear) {
                for (RandomStructure randomstructure : this.randomStructures) {
                    if (structureRand.nextInt(randomstructure.chunkChance()) != 0) {
                        continue;
                    }
                    int i = chunkX + rand.nextInt(16) + 8;
                    int k = chunkZ + rand.nextInt(16) + 8;
                    place(randomstructure.structureGen(), world, rand, i, topSolid(world, i, k), k);
                }
            }
            for (LOTRVillageGen village : this.villages) {
                village.generateInChunk(world, chunkX, chunkZ);
            }
        }
        if (LOTRMarshHutStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRMarshHutStructure house = new LOTRMarshHutStructure();
            house.restrictions = false;
            house.generateAndFinish(world, rand, i, topSolid(world, i, k), k);
        }
        if (LOTRGrukHouseStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRGrukHouseStructure house = new LOTRGrukHouseStructure(false);
            house.restrictions = false;
            house.generateAndFinish(world, rand, i, topSolid(world, i, k), k, 2);
        }
        if (LOTRTicketBoothStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRTicketBoothStructure booth = new LOTRTicketBoothStructure(false);
            booth.restrictions = false;
            booth.generateAndFinish(world, rand, i, topSolid(world, i, k), k, 3);
        }
        int trees = getVariantTreesPerChunk(biomeVariant);
        if (rand.nextFloat() < this.biome.getTreeIncreaseChance() * biomeVariant.treeFactor) {
            ++trees;
        }
        int cluster = Math.round(this.treeClusterChance * (1.0f / Math.max(biomeVariant.treeFactor, 0.001f)));
        if (cluster > 0) {
            long seed = chunkX / this.treeClusterSize * 3129871L ^ chunkZ / this.treeClusterSize * 116129781L;
            seed = seed * seed * 42317861L + seed * 11L;
            RandomSource chunkRand = new LegacyRandomSource(seed);
            if (chunkRand.nextInt(cluster) == 0) {
                trees += 6 + rand.nextInt(5);
            }
        }
        for (int l = 0; l < trees; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            LOTRWorldGenerator treeGen = getRandomTreeForVariant(rand, biomeVariant).create(false, rand);
            treeGen.generate(world, rand, i, height(world, i, k), k);
        }
        for (int l = 0; l < this.willowPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            LOTRWorldGenerator treeGen = LOTRTreeType.WILLOW_WATER.create(false, rand);
            treeGen.generate(world, rand, i, height(world, i, k), k);
        }
        if (trees > 0) {
            // The original counted against the fraction, not the rounded count it drew.
            float fallenLeaves = trees / 2.0f;
            int fallenLeavesI = (int) fallenLeaves;
            if (rand.nextFloat() < fallenLeaves - fallenLeavesI) {
                ++fallenLeavesI;
            }
            for (int l = 0; l < fallenLeaves; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                new LOTRWorldGenFallenLeaves().generate(world, rand, i, topSolid(world, i, k), k);
            }
        }
        if (trees > 0) {
            float bushes = trees / 3.0f;
            int bushesI = (int) bushes;
            if (rand.nextFloat() < bushes - bushesI) {
                ++bushesI;
            }
            for (int l = 0; l < bushes; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                new LOTRWorldGenBushes().generate(world, rand, i, topSolid(world, i, k), k);
            }
        }
        for (int l = 0; l < this.logsPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.logGen.generate(world, rand, i, height(world, i, k), k);
        }
        for (int l = 0; l < this.vinesPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.vinesGen.generate(world, rand, i, 64, k);
        }
        int flowers = Math.round(this.flowersPerChunk * biomeVariant.flowerFactor);
        for (int l = 0; l < flowers; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.flowerGen.generate(world, rand, i, j, k);
        }
        int doubleFlowers = Math.round(this.doubleFlowersPerChunk * biomeVariant.flowerFactor);
        for (int l = 0; l < doubleFlowers; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.biome.getRandomWorldGenForDoubleFlower(rand).generate(world, rand, i, j, k);
        }
        int grasses = Math.round(this.biome.getGrassPerChunk(chunkX, chunkZ) * biomeVariant.grassFactor);
        for (int l = 0; l < grasses; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.biome.getRandomWorldGenForGrass(rand).generate(world, rand, i, j, k);
        }
        int doubleGrasses = Math.round(this.doubleGrassPerChunk * biomeVariant.grassFactor);
        for (int l = 0; l < doubleGrasses; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.biome.getRandomWorldGenForDoubleGrass(rand).generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.deadBushPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            new LOTRVanillaWorldGens.DeadBush().generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.waterlilyPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            while (j > 0 && world.isEmptyBlock(new BlockPos(i, j - 1, k))) {
                --j;
            }
            this.waterlilyGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.mushroomsPerChunk; ++l) {
            if (rand.nextInt(4) == 0) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                this.mushroomBrownGen.generate(world, rand, i, height(world, i, k), k);
            }
            if (rand.nextInt(8) != 0) {
                continue;
            }
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            this.mushroomRedGen.generate(world, rand, i, height(world, i, k), k);
        }
        if (this.enableRandomMushroom) {
            if (rand.nextInt(4) == 0) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(128);
                int k = chunkZ + rand.nextInt(16) + 8;
                this.mushroomBrownGen.generate(world, rand, i, j, k);
            }
            if (rand.nextInt(8) == 0) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(128);
                int k = chunkZ + rand.nextInt(16) + 8;
                this.mushroomRedGen.generate(world, rand, i, j, k);
            }
        }
        for (int l = 0; l < this.canePerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.caneGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < 10; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.caneGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.reedPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            while (j > 0 && world.isEmptyBlock(new BlockPos(i, j - 1, k))) {
                --j;
            }
            if (rand.nextFloat() < this.dryReedChance) {
                this.dryReedGen.generate(world, rand, i, j, k);
                continue;
            }
            this.reedGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.cornPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.cornGen.generate(world, rand, i, j, k);
        }
        for (int l = 0; l < this.cactiPerChunk; ++l) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.cactusGen.generate(world, rand, i, j, k);
        }
        if (this.melonPerChunk > 0.0f) {
            int melonInt = Mth.floor(this.melonPerChunk);
            float melonF = this.melonPerChunk - melonInt;
            for (int l = 0; l < melonInt; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                this.melonGen.generate(world, rand, i, height(world, i, k), k);
            }
            if (rand.nextFloat() < melonF) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                this.melonGen.generate(world, rand, i, height(world, i, k), k);
            }
        }
        if (this.flowersPerChunk > 0 && rand.nextInt(32) == 0) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            this.pumpkinGen.generate(world, rand, i, j, k);
        }
        if (this.flowersPerChunk > 0 && rand.nextInt(4) == 0) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            new LOTRWorldGenBerryBush().generate(world, rand, i, j, k);
        }
        if (this.generateAthelas && rand.nextInt(30) == 0) {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("athelas")).generate(world, rand, i, j, k);
        }
        if (this.generateWater) {
            LOTRWorldGenStreams waterGen = new LOTRWorldGenStreams(LOTRLegacyBlocks.vanilla("flowing_water"));
            for (int l = 0; l < 50; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(rand.nextInt(120) + 8);
                int k = chunkZ + rand.nextInt(16) + 8;
                waterGen.generate(world, rand, i, j, k);
            }
            if (this.biome.rootHeight > 1.0f) {
                for (int l = 0; l < 50; ++l) {
                    int i = chunkX + rand.nextInt(16) + 8;
                    int j = 100 + rand.nextInt(150);
                    int k = chunkZ + rand.nextInt(16) + 8;
                    waterGen.generate(world, rand, i, j, k);
                }
            }
        }
        if (this.generateLava) {
            LOTRWorldGenStreams lavaGen = new LOTRWorldGenStreams(LOTRLegacyBlocks.vanilla("flowing_lava"));
            int lava = this.biome instanceof LOTRMordorBiome ? 50 : 20;
            for (int l = 0; l < lava; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
                int k = chunkZ + rand.nextInt(16) + 8;
                lavaGen.generate(world, rand, i, j, k);
            }
        }
        if (this.generateOrcDungeon) {
            for (int l = 0; l < 6; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(128);
                int k = chunkZ + rand.nextInt(16) + 8;
                place(this.orcDungeonGen, world, rand, i, j, k);
            }
        }
        if (this.generateTrollHoard) {
            for (int l = 0; l < 2; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = LOTRWorldGenUtil.getRandomIntegerInRange(rand, 36, 90);
                int k = chunkZ + rand.nextInt(16) + 8;
                this.trollHoardGen.generate(world, rand, i, j, k);
            }
        }
        if (biomeVariant.boulderGen != null && rand.nextInt(biomeVariant.boulderChance) == 0) {
            int boulders = LOTRWorldGenUtil.getRandomIntegerInRange(rand, 1, biomeVariant.boulderMax);
            for (int l = 0; l < boulders; ++l) {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                biomeVariant.boulderGen.generate(world, rand, i, height(world, i, k), k);
            }
        }
    }

    /** generate -- a structure of the older kind with its shape pass, as the newer kind's generate has. */
    private static void place(LOTRWorldGenerator gen, WorldGenLevel world, RandomSource rand, int i, int j, int k) {
        if (gen instanceof LOTRStructureBase structure) {
            structure.generateAndFinish(world, rand, i, j, k);
        } else {
            gen.generate(world, rand, i, j, k);
        }
    }

    private static int topSolid(WorldGenLevel world, int i, int k) {
        return LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k);
    }

    private static int height(WorldGenLevel world, int i, int k) {
        return LOTRWorldGenUtil.getHeightValue(world, i, k);
    }

    public void generateOres(WorldGenLevel world, RandomSource rand, int chunkX, int chunkZ) {
        for (OreGenerant soil : this.biomeSoils) {
            genStandardOre(world, rand, chunkX, chunkZ, soil.oreChance(), soil.oreGen(), soil.minHeight(), soil.maxHeight());
        }
        for (OreGenerant ore : this.biomeOres) {
            genStandardOre(world, rand, chunkX, chunkZ, ore.oreChance() * this.biomeOreFactor, ore.oreGen(), ore.minHeight(), ore.maxHeight());
        }
        for (OreGenerant gem : this.biomeGems) {
            genStandardOre(world, rand, chunkX, chunkZ, gem.oreChance() * this.biomeGemFactor, gem.oreGen(), gem.minHeight(), gem.maxHeight());
        }
    }

    /** genStandardOre: the whole number of veins, and one more by the fraction left. */
    public void genStandardOre(WorldGenLevel world, RandomSource rand, int chunkX, int chunkZ, float ores,
                               LOTRWorldGenerator oreGen, int minHeight, int maxHeight) {
        int maxIterations = (int) ores;
        for (int iteration = 0; iteration < maxIterations; ++iteration) {
            int i = chunkX + rand.nextInt(16);
            int j = LOTRWorldGenUtil.getRandomIntegerInRange(rand, minHeight, maxHeight);
            int k = chunkZ + rand.nextInt(16);
            oreGen.generate(world, rand, i, j, k);
        }
        if (rand.nextFloat() < ores - maxIterations) {
            int i = chunkX + rand.nextInt(16);
            int j = LOTRWorldGenUtil.getRandomIntegerInRange(rand, minHeight, maxHeight);
            int k = chunkZ + rand.nextInt(16);
            oreGen.generate(world, rand, i, j, k);
        }
    }

    public record OreGenerant(LOTRWorldGenerator oreGen, float oreChance, int minHeight, int maxHeight) {
    }

    public record RandomStructure(LOTRWorldGenerator structureGen, int chunkChance) {
    }
}
