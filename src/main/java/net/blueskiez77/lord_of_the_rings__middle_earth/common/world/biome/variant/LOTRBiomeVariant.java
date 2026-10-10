package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import java.util.ArrayList;
import java.util.Collection;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBiomeVariant: a variation laid over a biome here and there -- forests, hills, steppe,
 * orchards, lakes, rivers -- raising or flattening its ground, thickening or thinning its trees,
 * grass and flowers, warming or wetting its colours, or changing its surface. Each has an id the
 * chunk stores (LOTRBiomeVariantStorage) and a scale it may appear at (VariantScale).
 */
public class LOTRBiomeVariant {

    private static final LOTRBiomeVariant[] ALL_VARIANTS = new LOTRBiomeVariant[256];

    public static final LOTRBiomeVariant STANDARD = new LOTRBiomeVariant(0, "standard", VariantScale.ALL);
    public static final LOTRBiomeVariant FLOWERS = new LOTRBiomeVariant(1, "flowers", VariantScale.SMALL).setFlowers(10.0f);
    public static final LOTRBiomeVariant FOREST = new LOTRForestBiomeVariant(2, "forest");
    public static final LOTRBiomeVariant FOREST_LIGHT = new LOTRBiomeVariant(3, "forest_light", VariantScale.ALL).setTemperatureRainfall(0.0f, 0.2f).setTrees(3.0f).setGrass(2.0f);
    public static final LOTRBiomeVariant STEPPE = new LOTRBiomeVariant(4, "steppe", VariantScale.LARGE).setTemperatureRainfall(0.0f, -0.1f).setHeight(0.0f, 0.1f).setTrees(0.01f).setGrass(3.0f).setFlowers(0.8f);
    public static final LOTRBiomeVariant STEPPE_BARREN = new LOTRBiomeVariant(5, "steppe_barren", VariantScale.LARGE).setTemperatureRainfall(0.1f, -0.2f).setHeight(0.0f, 0.1f).setTrees(0.01f).setGrass(0.2f).setFlowers(0.4f);
    public static final LOTRBiomeVariant HILLS = new LOTRBiomeVariant(6, "hills", VariantScale.ALL).setTemperatureRainfall(-0.1f, -0.1f).setHeight(0.5f, 1.5f).setGrass(0.5f);
    public static final LOTRBiomeVariant HILLS_FOREST = new LOTRBiomeVariant(7, "hills_forest", VariantScale.ALL).setTemperatureRainfall(-0.1f, 0.0f).setHeight(0.5f, 1.5f).setTrees(3.0f);
    public static final LOTRBiomeVariant MOUNTAIN = new LOTRBiomeVariant(8, "mountain", VariantScale.ALL).setTemperatureRainfall(-0.1f, -0.2f).setHeight(1.2f, 3.0f).setFlowers(0.8f);
    public static final LOTRBiomeVariant CLEARING = new LOTRBiomeVariant(9, "clearing", VariantScale.SMALL).setHeight(0.0f, 0.5f).setTrees(0.0f).setGrass(2.0f).setFlowers(3.0f);
    public static final LOTRBiomeVariant DENSEFOREST_OAK = new LOTRDenseForestBiomeVariant(10, "denseForest_oak").addTreeTypes(0.5f, LOTRTreeType.OAK_LARGE, 600, LOTRTreeType.OAK_PARTY, 100);
    public static final LOTRBiomeVariant DENSEFOREST_SPRUCE = new LOTRDenseForestBiomeVariant(11, "denseForest_spruce").addTreeTypes(0.5f, LOTRTreeType.SPRUCE_MEGA, 100);
    public static final LOTRBiomeVariant DENSEFOREST_OAK_SPRUCE = new LOTRDenseForestBiomeVariant(12, "denseForest_oak_spruce").addTreeTypes(0.5f, LOTRTreeType.OAK_LARGE, 600, LOTRTreeType.OAK_PARTY, 200, LOTRTreeType.SPRUCE_MEGA, 200);
    public static final LOTRBiomeVariant DEADFOREST_OAK = new LOTRDeadForestBiomeVariant(13, "deadForest_oak").addTreeTypes(0.5f, LOTRTreeType.OAK_DEAD, 100);
    public static final LOTRBiomeVariant DEADFOREST_SPRUCE = new LOTRDeadForestBiomeVariant(14, "deadForest_spruce").addTreeTypes(0.5f, LOTRTreeType.SPRUCE_DEAD, 100);
    public static final LOTRBiomeVariant DEADFOREST_OAK_SPRUCE = new LOTRDeadForestBiomeVariant(15, "deadForest_oak_spruce").addTreeTypes(0.5f, LOTRTreeType.OAK_DEAD, 100, LOTRTreeType.SPRUCE_DEAD, 100);
    public static final LOTRBiomeVariant SHRUBLAND_OAK = new LOTRBiomeVariant(16, "shrubland_oak", VariantScale.ALL).setTemperatureRainfall(0.0f, 0.3f).setTrees(6.0f).addTreeTypes(0.7f, LOTRTreeType.OAK_SHRUB, 100);
    public static final LOTRBiomeVariant DENSEFOREST_BIRCH = new LOTRDenseForestBiomeVariant(17, "denseForest_birch").addTreeTypes(0.5f, LOTRTreeType.BIRCH_LARGE, 600, LOTRTreeType.BIRCH_PARTY, 100);
    public static final LOTRBiomeVariant SWAMP_LOWLAND = new LOTRBiomeVariant(18, "swampLowland", VariantScale.SMALL).setHeight(-0.12f, 0.2f).setTrees(0.5f).setGrass(5.0f).setMarsh();
    public static final LOTRBiomeVariant SWAMP_UPLAND = new LOTRBiomeVariant(19, "swampUpland", VariantScale.SMALL).setHeight(0.12f, 1.0f).setTrees(6.0f).setGrass(5.0f);
    public static final LOTRBiomeVariant SAVANNAH_BAOBAB = new LOTRBiomeVariant(20, "savannahBaobab", VariantScale.LARGE).setHeight(0.0f, 0.5f).setTemperatureRainfall(0.0f, 0.2f).setTrees(1.5f).setGrass(0.5f).addTreeTypes(0.6f, LOTRTreeType.BAOBAB, 100);
    public static final LOTRBiomeVariant LAKE = new LOTRBiomeVariant(21, "lake", VariantScale.NONE).setAbsoluteHeight(-0.5f, 0.05f);
    public static final LOTRBiomeVariant DENSEFOREST_LEBETHRON = new LOTRDenseForestBiomeVariant(22, "denseForest_lebethron").addTreeTypes(0.5f, LOTRTreeType.LEBETHRON_LARGE, 600, LOTRTreeType.LEBETHRON_PARTY, 100);
    public static final LOTRBiomeVariant BOULDERS_RED = new LOTRBiomeVariant(23, "boulders_red", VariantScale.LARGE).setBoulders(new LOTRWorldGenBoulder(Blocks.RED_SANDSTONE.defaultBlockState(), 1, 3), 2, 4);
    public static final LOTRBiomeVariant BOULDERS_ROHAN = new LOTRBiomeVariant(24, "boulders_rohan", VariantScale.LARGE).setBoulders(new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock").state(2), 1, 3), 2, 4);
    public static final LOTRBiomeVariant JUNGLE_DENSE = new LOTRBiomeVariant(25, "jungle_dense", VariantScale.LARGE).setTemperatureRainfall(0.1f, 0.1f).setTrees(2.0f).addTreeTypes(0.6f, LOTRTreeType.JUNGLE_FANGORN, 1000, LOTRTreeType.MAHOGANY_FANGORN, 500);
    public static final LOTRBiomeVariant VINEYARD = new LOTRBiomeVariant(26, "vineyard", VariantScale.SMALL).setHeight(0.0f, 0.5f).setTrees(0.0f).setGrass(0.5f).setFlowers(0.0f).disableStructuresVillages();
    public static final LOTRBiomeVariant FOREST_ASPEN = new LOTRForestBiomeVariant(27, "forest_aspen").addTreeTypes(0.8f, LOTRTreeType.ASPEN, 1000, LOTRTreeType.ASPEN_LARGE, 50);
    public static final LOTRBiomeVariant FOREST_BIRCH = new LOTRForestBiomeVariant(28, "forest_birch").addTreeTypes(0.8f, LOTRTreeType.BIRCH, 1000, LOTRTreeType.BIRCH_LARGE, 150);
    public static final LOTRBiomeVariant FOREST_BEECH = new LOTRForestBiomeVariant(29, "forest_beech").addTreeTypes(0.8f, LOTRTreeType.BEECH, 1000, LOTRTreeType.BEECH_LARGE, 150);
    public static final LOTRBiomeVariant FOREST_MAPLE = new LOTRForestBiomeVariant(30, "forest_maple").addTreeTypes(0.8f, LOTRTreeType.MAPLE, 1000, LOTRTreeType.MAPLE_LARGE, 150);
    public static final LOTRBiomeVariant FOREST_LARCH = new LOTRForestBiomeVariant(31, "forest_larch").addTreeTypes(0.8f, LOTRTreeType.LARCH, 1000);
    public static final LOTRBiomeVariant FOREST_PINE = new LOTRForestBiomeVariant(32, "forest_pine").addTreeTypes(0.8f, LOTRTreeType.PINE, 1000);
    public static final LOTRBiomeVariant ORCHARD_SHIRE = new LOTROrchardBiomeVariant(33, "orchard_shire").addTreeTypes(1.0f, LOTRTreeType.APPLE, 100, LOTRTreeType.PEAR, 100, LOTRTreeType.CHERRY, 10);
    public static final LOTRBiomeVariant ORCHARD_APPLE_PEAR = new LOTROrchardBiomeVariant(34, "orchard_apple_pear").addTreeTypes(1.0f, LOTRTreeType.APPLE, 100, LOTRTreeType.PEAR, 100);
    public static final LOTRBiomeVariant ORCHARD_ORANGE = new LOTROrchardBiomeVariant(35, "orchard_orange").addTreeTypes(1.0f, LOTRTreeType.ORANGE, 100);
    public static final LOTRBiomeVariant ORCHARD_LEMON = new LOTROrchardBiomeVariant(36, "orchard_lemon").addTreeTypes(1.0f, LOTRTreeType.LEMON, 100);
    public static final LOTRBiomeVariant ORCHARD_LIME = new LOTROrchardBiomeVariant(37, "orchard_lime").addTreeTypes(1.0f, LOTRTreeType.LIME, 100);
    public static final LOTRBiomeVariant ORCHARD_ALMOND = new LOTROrchardBiomeVariant(38, "orchard_almond").addTreeTypes(1.0f, LOTRTreeType.ALMOND, 100);
    public static final LOTRBiomeVariant ORCHARD_OLIVE = new LOTROrchardBiomeVariant(39, "orchard_olive").addTreeTypes(1.0f, LOTRTreeType.OLIVE, 100);
    public static final LOTRBiomeVariant ORCHARD_PLUM = new LOTROrchardBiomeVariant(40, "orchard_plum").addTreeTypes(1.0f, LOTRTreeType.PLUM, 100);
    public static final LOTRBiomeVariant RIVER = new LOTRBiomeVariant(41, "river", VariantScale.NONE).setAbsoluteHeight(-0.5f, 0.05f).setTemperatureRainfall(0.0f, 0.3f);
    public static final LOTRBiomeVariant SCRUBLAND = new LOTRScrublandBiomeVariant(42, "scrubland", Blocks.STONE.defaultBlockState()).setHeight(0.0f, 0.8f);
    public static final LOTRBiomeVariant HILLS_SCRUBLAND = new LOTRScrublandBiomeVariant(43, "hills_scrubland", Blocks.STONE.defaultBlockState()).setHeight(0.5f, 2.0f);
    public static final LOTRBiomeVariant WASTELAND = new LOTRWastelandBiomeVariant(44, "wasteland", Blocks.STONE.defaultBlockState()).setHeight(0.0f, 0.5f);
    public static final LOTRBiomeVariant ORCHARD_DATE = new LOTROrchardBiomeVariant(45, "orchard_date").addTreeTypes(1.0f, LOTRTreeType.DATE_PALM, 100);
    public static final LOTRBiomeVariant DENSEFOREST_DARK_OAK = new LOTRDenseForestBiomeVariant(46, "denseForest_darkOak").addTreeTypes(0.5f, LOTRTreeType.DARK_OAK, 600, LOTRTreeType.DARK_OAK_PARTY, 100);
    public static final LOTRBiomeVariant ORCHARD_POMEGRANATE = new LOTROrchardBiomeVariant(47, "orchard_pomegranate").addTreeTypes(1.0f, LOTRTreeType.POMEGRANATE, 100);
    public static final LOTRBiomeVariant DUNES = new LOTRDunesBiomeVariant(48, "dunes");
    public static final LOTRBiomeVariant SCRUBLAND_SAND = new LOTRScrublandBiomeVariant(49, "scrubland_sand", Blocks.SANDSTONE.defaultBlockState()).setHeight(0.0f, 0.8f);
    public static final LOTRBiomeVariant HILLS_SCRUBLAND_SAND = new LOTRScrublandBiomeVariant(50, "hills_scrubland_sand", Blocks.SANDSTONE.defaultBlockState()).setHeight(0.5f, 2.0f);
    public static final LOTRBiomeVariant WASTELAND_SAND = new LOTRWastelandBiomeVariant(51, "wasteland_sand", Blocks.SANDSTONE.defaultBlockState()).setHeight(0.0f, 0.5f);

    public static final LOTRBiomeVariant[] SET_NORMAL = {FLOWERS, FOREST, FOREST_LIGHT, STEPPE, STEPPE_BARREN, HILLS, HILLS_FOREST};
    public static final LOTRBiomeVariant[] SET_NORMAL_OAK = concat(SET_NORMAL, DENSEFOREST_OAK, DEADFOREST_OAK, SHRUBLAND_OAK);
    public static final LOTRBiomeVariant[] SET_NORMAL_OAK_NOSTEPPE = without(SET_NORMAL_OAK, STEPPE, STEPPE_BARREN);
    public static final LOTRBiomeVariant[] SET_NORMAL_SPRUCE = concat(SET_NORMAL, DENSEFOREST_SPRUCE, DEADFOREST_SPRUCE);
    public static final LOTRBiomeVariant[] SET_NORMAL_OAK_SPRUCE = concat(SET_NORMAL, DENSEFOREST_OAK, DEADFOREST_OAK, SHRUBLAND_OAK,
            DENSEFOREST_SPRUCE, DEADFOREST_SPRUCE, DENSEFOREST_OAK_SPRUCE, DEADFOREST_OAK_SPRUCE);
    public static final LOTRBiomeVariant[] SET_NORMAL_NOSTEPPE = without(SET_NORMAL, STEPPE, STEPPE_BARREN);
    public static final LOTRBiomeVariant[] SET_FOREST = {FLOWERS, HILLS, CLEARING};
    public static final LOTRBiomeVariant[] SET_MOUNTAINS = {FOREST, FOREST_LIGHT};
    public static final LOTRBiomeVariant[] SET_SWAMP = {SWAMP_LOWLAND, SWAMP_LOWLAND, SWAMP_LOWLAND, SWAMP_UPLAND};

    public static final LOTRNoiseGeneratorPerlin MARSH_NOISE = new LOTRNoiseGeneratorPerlin(444L, 1);
    public static final LOTRNoiseGeneratorPerlin PODZOL_NOISE = new LOTRNoiseGeneratorPerlin(58052L, 1);

    public final int variantID;
    public final String variantName;
    public final VariantScale variantScale;
    public float tempBoost;
    public float rainBoost;
    public boolean absoluteHeight;
    public float absoluteHeightLevel;
    public float heightBoost;
    public float hillFactor = 1.0f;
    public float treeFactor = 1.0f;
    public float grassFactor = 1.0f;
    public float flowerFactor = 1.0f;
    public boolean hasMarsh;
    public boolean disableStructures;
    public boolean disableVillages;
    public final Collection<LOTRTreeType.WeightedTreeType> treeTypes = new ArrayList<>();
    public float variantTreeChance;
    public @Nullable LOTRWorldGenerator boulderGen;
    public int boulderChance;
    public int boulderMax = 1;

    public LOTRBiomeVariant(int i, String s, VariantScale scale) {
        if (ALL_VARIANTS[i] != null) {
            throw new IllegalArgumentException("LOTR Biome variant already exists at index " + i);
        }
        this.variantID = i;
        ALL_VARIANTS[i] = this;
        this.variantName = s;
        this.variantScale = scale;
    }

    public static LOTRBiomeVariant getVariantForID(int i) {
        LOTRBiomeVariant variant = ALL_VARIANTS[i & 0xFF];
        return variant == null ? STANDARD : variant;
    }

    private static LOTRBiomeVariant[] concat(LOTRBiomeVariant[] set, LOTRBiomeVariant... more) {
        LOTRBiomeVariant[] all = java.util.Arrays.copyOf(set, set.length + more.length);
        System.arraycopy(more, 0, all, set.length, more.length);
        return all;
    }

    private static LOTRBiomeVariant[] without(LOTRBiomeVariant[] set, LOTRBiomeVariant... removed) {
        java.util.List<LOTRBiomeVariant> list = new ArrayList<>(java.util.Arrays.asList(set));
        list.removeAll(java.util.Arrays.asList(removed));
        return list.toArray(new LOTRBiomeVariant[0]);
    }

    public LOTRBiomeVariant addTreeTypes(float f, Object... trees) {
        this.variantTreeChance = f;
        for (int i = 0; i < trees.length / 2; ++i) {
            this.treeTypes.add(new LOTRTreeType.WeightedTreeType((LOTRTreeType) trees[i * 2], (Integer) trees[i * 2 + 1]));
        }
        return this;
    }

    public void decorateVariant(WorldGenLevel world, RandomSource random, int i, int k, LOTRBiome biome) {
    }

    public LOTRBiomeVariant disableStructuresVillages() {
        this.disableStructures = true;
        this.disableVillages = true;
        return this;
    }

    public LOTRBiomeVariant disableVillages() {
        this.disableVillages = true;
        return this;
    }

    public void generateVariantTerrain(LOTRChunkTerrain terrain, RandomSource random, int i, int k, int height, LOTRBiome biome) {
    }

    public float getHeightBoostAt(int i, int k) {
        return this.heightBoost;
    }

    public LOTRTreeType getRandomTree(RandomSource random) {
        return LOTRWeightedRandom.getRandomItem(random, this.treeTypes).treeType();
    }

    public LOTRBiomeVariant setAbsoluteHeight(float height, float hills) {
        this.absoluteHeight = true;
        this.absoluteHeightLevel = height;
        this.heightBoost = height - 2.0f + 0.2f;
        this.hillFactor = hills;
        return this;
    }

    public LOTRBiomeVariant setBoulders(LOTRWorldGenerator boulder, int chance, int num) {
        if (num < 1) {
            throw new IllegalArgumentException("n must be > 1");
        }
        this.boulderGen = boulder;
        this.boulderChance = chance;
        this.boulderMax = num;
        return this;
    }

    public LOTRBiomeVariant setFlowers(float f) {
        this.flowerFactor = f;
        return this;
    }

    public LOTRBiomeVariant setGrass(float f) {
        this.grassFactor = f;
        return this;
    }

    public LOTRBiomeVariant setHeight(float height, float hills) {
        this.heightBoost = height;
        this.hillFactor = hills;
        return this;
    }

    public LOTRBiomeVariant setMarsh() {
        this.hasMarsh = true;
        return this;
    }

    public LOTRBiomeVariant setTemperatureRainfall(float temp, float rain) {
        this.tempBoost = temp;
        this.rainBoost = rain;
        return this;
    }

    public LOTRBiomeVariant setTrees(float f) {
        this.treeFactor = f;
        return this;
    }

    public enum VariantScale {
        LARGE, SMALL, ALL, NONE
    }
}
