package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeInvasionSpawns;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGen;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRWickedDwarfEntity;
import net.minecraft.world.level.Level;

/**
 * LOTRBiome: one of Middle-earth's biomes -- its id, its colour on the map, its height and
 * hilliness, temperature and rain, the blocks on its surface and under it, the variants that may
 * vary it, its decorator, and the colours it gives sky, fog, clouds, grass and leaves.
 *
 * <p>The vanilla biome registered under {@link #key} carries what the game itself reads of a biome
 * (its temperature, rain and colours, {@code data/lotr/worldgen/biome}); the rest is here and
 * Middle-earth's chunk generator does the work.
 */
public abstract class LOTRBiome {

    public static final LOTRNoiseGeneratorPerlin BIOME_TERRAIN_NOISE = new LOTRNoiseGeneratorPerlin(1955L, 1);
    private static final BlockState GRASS = Blocks.GRASS_BLOCK.defaultBlockState();
    private static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();

    public final LOTRDimension biomeDimension;
    public final int biomeID;
    public String biomeName = "";
    public @Nullable ResourceKey<Biome> key;
    public int color;
    public float temperature = 0.5f;
    public float rainfall = 0.5f;
    public boolean enableRain = true;
    public boolean enableSnow;
    public float rootHeight = 0.1f;
    public float heightVariation = 0.2f;
    public float heightBaseParameter;
    public BlockState topBlock = GRASS;
    public BlockState fillerBlock = Blocks.DIRT.defaultBlockState();
    public boolean enablePodzol = true;
    public boolean enableRocky = true;
    public final LOTRBiomeVariantList biomeVariantsSmall = new LOTRBiomeVariantList();
    public final LOTRBiomeVariantList biomeVariantsLarge = new LOTRBiomeVariantList();
    public float variantChance = 0.4f;
    public final LOTRBiomeDecorator decorator;
    public final BiomeColors biomeColors = new BiomeColors();
    public final BiomeTerrain biomeTerrain = new BiomeTerrain();
    public final LOTRBiomeSpawnList npcSpawnList = new LOTRBiomeSpawnList(getClass().getName());
    private volatile boolean initDwarven;
    private volatile boolean isDwarven;
    public final List<LOTRSpawnEntry> spawnableCreatureList = new ArrayList<>();
    public final List<LOTRSpawnEntry> spawnableMonsterList = new ArrayList<>();
    public final List<LOTRSpawnEntry> spawnableWaterCreatureList = new ArrayList<>();
    public final List<LOTRSpawnEntry> spawnableCaveCreatureList = new ArrayList<>();
    public final List<LOTRSpawnEntry> spawnableLOTRAmbientList = new ArrayList<>();
    public final List<EntityType<?>> spawnableTraders = new ArrayList<>();
    public LOTREventSpawner.EventChance banditChance = LOTREventSpawner.EventChance.NEVER;
    public @Nullable EntityType<? extends LOTRBanditEntity> banditEntityClass;
    public final LOTRBiomeInvasionSpawns invasionSpawns = new LOTRBiomeInvasionSpawns(getClass().getName());

    protected LOTRBiome(int i, boolean major) {
        this(i, major, LOTRDimension.MIDDLE_EARTH);
    }

    protected LOTRBiome(int i, boolean major, LOTRDimension dim) {
        this.biomeID = i;
        this.biomeDimension = dim;
        if (dim.biomeList[i] != null) {
            throw new IllegalArgumentException("LOTR biome already exists at index " + i + " for dimension " + dim.dimensionName + "!");
        }
        dim.biomeList[i] = this;
        if (major) {
            dim.majorBiomes.add(this);
        }
        this.decorator = new LOTRBiomeDecorator(this);
        addDefaultFlowers();
        if (hasDomesticAnimals()) {
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.SHEEP, 12, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.PIG, 10, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.CHICKEN, 10, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.COW, 8, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 5, 4, 4));
        } else {
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.SHEEP, 12, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.WILD_BOAR, 10, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.CHICKEN, 8, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 10, 4, 4));
            this.spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.AUROCHS, 6, 4, 4));
        }
        this.spawnableWaterCreatureList.add(new LOTRSpawnEntry(EntityTypes.COD, 10, 4, 4));
        this.spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 8, 4, 4));
        this.spawnableLOTRAmbientList.add(new LOTRSpawnEntry(EntityTypes.RABBIT, 8, 4, 4));
        this.spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        this.spawnableCaveCreatureList.add(new LOTRSpawnEntry(EntityTypes.BAT, 10, 8, 8));
    }

    // --- Set up as initBiomes sets each one up -------------------------------

    /** setBiomeName: the original's camel-case name ("mistyMountains"); the registry id is it in snake case. */
    public LOTRBiome setBiomeName(String name) {
        this.biomeName = name;
        for (LOTRVillageGen village : this.decorator.villages) {
            village.setVillageBiome(this);
        }
        String path = name.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(java.util.Locale.ROOT);
        this.key = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("lotr", path));
        return this;
    }

    public LOTRBiome setColor(int rgb) {
        int argb = rgb | 0xFF000000;
        Integer existing = this.biomeDimension.colorsToBiomeIDs.get(argb);
        if (existing != null) {
            throw new IllegalStateException("LOTR biome (ID " + this.biomeID + ") is duplicating the color of another LOTR biome (ID " + existing + ")");
        }
        this.biomeDimension.colorsToBiomeIDs.put(argb, this.biomeID);
        this.color = rgb;
        return this;
    }

    public LOTRBiome setMinMaxHeight(float f, float f1) {
        this.heightBaseParameter = f;
        this.rootHeight = f - 2.0f + 0.2f;
        this.heightVariation = f1 / 2.0f;
        return this;
    }

    public LOTRBiome setTemperatureRainfall(float temp, float rain) {
        if (temp > 0.1f && temp < 0.2f) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        this.temperature = temp;
        this.rainfall = rain;
        return this;
    }

    public LOTRBiome setEnableSnow() {
        this.enableSnow = true;
        return this;
    }

    public LOTRBiome setDisableRain() {
        this.enableRain = false;
        return this;
    }

    public String getRegistryPath() {
        return this.key.identifier().getPath();
    }

    public Component getBiomeDisplayName() {
        return Component.translatable("biome.lotr." + getRegistryPath());
    }

    // --- Variants --------------------------------------------------------------

    public void addBiomeVariant(LOTRBiomeVariant v) {
        addBiomeVariant(v, 1.0f);
    }

    public void addBiomeVariant(LOTRBiomeVariant v, float f) {
        switch (v.variantScale) {
            case ALL -> {
                this.biomeVariantsLarge.add(v, f);
                this.biomeVariantsSmall.add(v, f);
            }
            case LARGE -> this.biomeVariantsLarge.add(v, f);
            case SMALL -> this.biomeVariantsSmall.add(v, f);
            case NONE -> {
            }
        }
    }

    public void addBiomeVariantSet(LOTRBiomeVariant[] set) {
        for (LOTRBiomeVariant v : set) {
            addBiomeVariant(v);
        }
    }

    public void clearBiomeVariants() {
        this.biomeVariantsLarge.clear();
        this.biomeVariantsSmall.clear();
        this.variantChance = 0.4f;
    }

    public LOTRBiomeVariantList getBiomeVariantsLarge() {
        return this.biomeVariantsLarge;
    }

    public LOTRBiomeVariantList getBiomeVariantsSmall() {
        return this.biomeVariantsSmall;
    }

    // --- The surface -----------------------------------------------------------

    /**
     * generateBiomeTerrain: the column's surface -- the top block and the filler under it (stone
     * where the filler runs out, sand turning to sandstone below), bedrock at the bottom, bare
     * rock on high ground, podzol and coarse dirt in the forests, standing water in the marshes,
     * the local stone under Gondor, Rohan and Dorwinion -- then the mountains' and the variant's
     * own.
     */
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    /**
     * The surface pass with the top and filler blocks given: the original's biomes swapped their own
     * top and filler fields about a call to it, which chunks generated side by side cannot share.
     */
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant,
                                     BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int xzIndex = (i & 0xF) * 16 + (k & 0xF);
        int ySize = LOTRChunkTerrain.HEIGHT;
        int seaLevel = 63;
        double stoneNoiseFiller = modifyStoneNoiseForFiller(stoneNoise);
        int fillerDepthBase = (int) (stoneNoiseFiller / 4.0 + 5.0 + random.nextDouble() * 0.25);
        int fillerDepth = -1;
        BlockState top = topBlock;
        BlockState filler = fillerBlock;
        if (this.enableRocky && height >= 90) {
            float hFactor = (height - 90) / 10.0f;
            float thresh = Math.max(1.2f - hFactor * 0.2f, 0.0f);
            double d12 = BIOME_TERRAIN_NOISE.getValue(i * 0.03, k * 0.03);
            if (d12 + BIOME_TERRAIN_NOISE.getValue(i * 0.3, k * 0.3) > thresh) {
                top = random.nextInt(5) == 0 ? Blocks.GRAVEL.defaultBlockState() : LOTRChunkTerrain.STONE;
                filler = LOTRChunkTerrain.STONE;
                int prevHeight = height;
                height++;
                random.nextInt(20);
                for (int j = height; j >= prevHeight; --j) {
                    blocks[LOTRChunkTerrain.index(xzIndex, j)] = LOTRChunkTerrain.STONE;
                }
            }
        }
        if (isPodzolEnabled(height) && topBlock == GRASS) {
            float trees = Math.max(this.decorator.treesPerChunk + getTreeIncreaseChance(), variant.treeFactor * 0.5f);
            if (trees >= 1.0f) {
                float thresh = Math.max(0.8f - trees * 0.15f, 0.0f);
                if (BIOME_TERRAIN_NOISE.getValue(i * 0.06, k * 0.06) > thresh) {
                    RandomSource terrainRand = new LegacyRandomSource(worldSeed);
                    terrainRand.setSeed(terrainRand.nextLong() + i * 4668095025L + k * 1387590552L ^ worldSeed);
                    float pdzRand = terrainRand.nextFloat();
                    if (pdzRand < 0.35f) {
                        top = Blocks.PODZOL.defaultBlockState();
                    } else if (pdzRand < 0.5f) {
                        top = Blocks.COARSE_DIRT.defaultBlockState();
                    } else if (pdzRand < 0.51f) {
                        top = Blocks.GRAVEL.defaultBlockState();
                    }
                }
            }
        }
        if (variant.hasMarsh && LOTRBiomeVariant.MARSH_NOISE.getValue(i * 0.1, k * 0.1) > -0.1) {
            for (int j = ySize - 1; j >= 0; --j) {
                int index = LOTRChunkTerrain.index(xzIndex, j);
                if (blocks[index].isAir()) {
                    continue;
                }
                if (j == seaLevel - 1 && blocks[index] != LOTRChunkTerrain.WATER) {
                    blocks[index] = LOTRChunkTerrain.WATER;
                }
                break;
            }
        }
        for (int j = ySize - 1; j >= 0; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            if (j <= random.nextInt(5)) {
                blocks[index] = BEDROCK;
                continue;
            }
            BlockState block = blocks[index];
            if (block.isAir()) {
                fillerDepth = -1;
                continue;
            }
            if (block != LOTRChunkTerrain.STONE) {
                continue;
            }
            if (fillerDepth == -1) {
                if (fillerDepthBase <= 0) {
                    top = LOTRChunkTerrain.AIR;
                    filler = LOTRChunkTerrain.STONE;
                } else if (j >= seaLevel - 4 && j <= seaLevel + 1) {
                    top = topBlock;
                    filler = fillerBlock;
                }
                if (j < seaLevel && top.isAir()) {
                    top = LOTRChunkTerrain.WATER;
                }
                fillerDepth = fillerDepthBase;
                blocks[index] = j >= seaLevel - 1 ? top : filler;
                continue;
            }
            if (fillerDepth <= 0) {
                continue;
            }
            blocks[index] = filler;
            if (--fillerDepth == 0) {
                boolean sand = false;
                if (filler.is(Blocks.RED_SAND)) {
                    filler = Blocks.RED_SANDSTONE.defaultBlockState();
                    sand = true;
                } else if (filler.is(Blocks.SAND)) {
                    filler = Blocks.SANDSTONE.defaultBlockState();
                    sand = true;
                }
                if (filler == LOTRLegacyBlocks.mod("whiteSand").state()) {
                    filler = LOTRLegacyBlocks.mod("whiteSandstone").state();
                    sand = true;
                }
                if (sand) {
                    fillerDepth = 10 + random.nextInt(4);
                }
            }
            if ((this instanceof LOTRGondorBiome || this instanceof LOTRIthilienBiome || this instanceof LOTRDorEnErnilBiome)
                    && fillerDepth == 0 && filler == fillerBlock) {
                fillerDepth = 8 + random.nextInt(3);
                filler = LOTRLegacyBlocks.mod("rock").state(1);
                continue;
            }
            if ((this instanceof LOTRRohanBiome || this instanceof LOTRAdornlandBiome) && fillerDepth == 0 && filler == fillerBlock) {
                fillerDepth = 8 + random.nextInt(3);
                filler = LOTRLegacyBlocks.mod("rock").state(2);
                continue;
            }
            if (this instanceof LOTRDorwinionBiome && fillerDepth == 0 && !LOTRLegacyBlocks.mod("rock").matches(fillerBlock)
                    && filler == fillerBlock) {
                fillerDepth = 6 + random.nextInt(3);
                filler = LOTRLegacyBlocks.mod("rock").state(5);
            }
        }
        int rockDepth = (int) (stoneNoise * 6.0 + 2.0 + random.nextDouble() * 0.25);
        generateMountainTerrain(random, terrain, i, k, xzIndex, ySize, height, rockDepth, variant, topBlock, fillerBlock);
        variant.generateVariantTerrain(terrain, random, i, k, height, this);
    }

    /** generateMountainTerrain: a biome's own pass over the column after the surface, with the top and filler it was laid with. */
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
    }

    /** enablePodzol, for a column of this height (Kanuka's has it only on high ground). */
    public boolean isPodzolEnabled(int height) {
        return this.enablePodzol;
    }

    /** decorator.grassPerChunk, for the chunk at block i, k (Near Harad's grows more where its arid grass noise is high). */
    public int getGrassPerChunk(int i, int k) {
        return this.decorator.grassPerChunk;
    }

    public double modifyStoneNoiseForFiller(double stoneNoise) {
        return stoneNoise;
    }

    // --- Roads, bridges and flowers -------------------------------------------

    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.PATH;
    }

    public LOTRRoadType.BridgeType getBridgeBlock() {
        return LOTRRoadType.BridgeType.DEFAULT;
    }

    /** A flower and its weight among the biome's flowers (1.7.10's BiomeGenBase.FlowerEntry). */
    public record FlowerEntry(BlockState state, int itemWeight) implements LOTRWeightedRandom.Item {
    }

    public final List<FlowerEntry> flowers = new ArrayList<>();

    public void addFlower(LOTRLegacyBlocks.LegacyBlock block, int meta, int weight) {
        this.flowers.add(new FlowerEntry(block.state(meta), weight));
    }

    /** BiomeGenBase.addDefaultFlowers: dandelions, and half as many poppies. */
    public void addDefaultFlowers() {
        addFlower(LOTRLegacyBlocks.vanilla("yellow_flower"), 0, 20);
        addFlower(LOTRLegacyBlocks.vanilla("red_flower"), 0, 10);
    }

    /** getRandomFlower: one of its flowers, for a spot of the given variant. */
    public @Nullable FlowerEntry getRandomFlower(LOTRBiomeVariant variant, RandomSource random) {
        return this.flowers.isEmpty() ? null : LOTRWeightedRandom.getRandomItem(random, this.flowers);
    }

    /** getRandomFlower: one of its flowers, for this spot. */
    public @Nullable FlowerEntry getRandomFlower(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(world);
        return getRandomFlower(chunkManager == null ? LOTRBiomeVariant.STANDARD : chunkManager.getBiomeVariantAt(i, k), random);
    }

    public void registerForestFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
        addFlower(LOTRLegacyBlocks.mod("bluebell"), 0, 5);
        addFlower(LOTRLegacyBlocks.mod("marigold"), 0, 10);
    }

    public void registerHaradFlowers() {
        this.flowers.clear();
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 0, 10);
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 1, 10);
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 2, 5);
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 3, 5);
    }

    public void registerJungleFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 2, 20);
        addFlower(LOTRLegacyBlocks.mod("haradFlower"), 3, 20);
    }

    public void registerMountainsFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
        addFlower(LOTRLegacyBlocks.vanilla("red_flower"), 1, 10);
        addFlower(LOTRLegacyBlocks.mod("bluebell"), 0, 5);
    }

    public void registerPlainsFlowers() {
        this.flowers.clear();
        LOTRLegacyBlocks.LegacyBlock red = LOTRLegacyBlocks.vanilla("red_flower");
        addFlower(red, 4, 3);
        addFlower(red, 5, 3);
        addFlower(red, 6, 3);
        addFlower(red, 7, 3);
        addFlower(red, 0, 20);
        addFlower(red, 3, 20);
        addFlower(red, 8, 20);
        addFlower(LOTRLegacyBlocks.vanilla("yellow_flower"), 0, 30);
        addFlower(LOTRLegacyBlocks.mod("bluebell"), 0, 5);
        addFlower(LOTRLegacyBlocks.mod("marigold"), 0, 10);
        addFlower(LOTRLegacyBlocks.mod("lavender"), 0, 5);
    }

    public void registerRhunForestFlowers() {
        registerForestFlowers();
        addRhunFlowers();
    }

    public void registerRhunPlainsFlowers() {
        registerPlainsFlowers();
        addRhunFlowers();
    }

    private void addRhunFlowers() {
        addFlower(LOTRLegacyBlocks.mod("marigold"), 0, 10);
        for (int meta = 0; meta <= 4; ++meta) {
            addFlower(LOTRLegacyBlocks.mod("rhunFlower"), meta, 10);
        }
    }

    public void registerSavannaFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
    }

    public void registerSwampFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
    }

    public void registerTaigaFlowers() {
        this.flowers.clear();
        addDefaultFlowers();
        addFlower(LOTRLegacyBlocks.vanilla("red_flower"), 1, 10);
        addFlower(LOTRLegacyBlocks.mod("bluebell"), 0, 5);
    }

    // --- The rest of a biome's character --------------------------------------

    public boolean getEnableRiver() {
        return true;
    }

    public boolean isRiver() {
        return false;
    }

    public boolean isWateryBiome() {
        return this.heightBaseParameter < 0.0f;
    }

    /** getTreeGen: one of its trees, for the variant at this spot. */
    public LOTRWorldGenerator getTreeGen(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(world);
        LOTRBiomeVariant variant = chunkManager == null ? LOTRBiomeVariant.STANDARD : chunkManager.getBiomeVariantAt(i, k);
        return this.decorator.getRandomTreeForVariant(random, variant).create(false, random);
    }

    /** decorate: the decorator's pass over the chunk at block i, k. */
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        this.decorator.decorate(world, random, i, k);
    }

    /** A grass, fern or other low plant, as 1.7.10 gave block and metadata. */
    public record GrassBlockAndMeta(LOTRLegacyBlocks.LegacyBlock block, int meta) {
    }

    /** getRandomGrass: ferns where the decorator has them, now and then flax, else the mod's grasses, clover and vanilla's. */
    public GrassBlockAndMeta getRandomGrass(RandomSource random) {
        boolean fern = this.decorator.enableFern;
        boolean special = this.decorator.enableSpecialGrasses;
        if (fern && random.nextInt(3) == 0) {
            return new GrassBlockAndMeta(LOTRLegacyBlocks.vanilla("tallgrass"), 2);
        }
        if (special && random.nextInt(500) == 0) {
            return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("flaxPlant"), 0);
        }
        if (random.nextInt(4) > 0) {
            if (special) {
                if (random.nextInt(200) == 0) {
                    return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 3);
                }
                if (random.nextInt(16) == 0) {
                    return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 1);
                }
                if (random.nextInt(10) == 0) {
                    return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 2);
                }
            }
            if (random.nextInt(80) == 0) {
                return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 4);
            }
            return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 0);
        }
        if (random.nextInt(3) == 0) {
            return new GrassBlockAndMeta(LOTRLegacyBlocks.mod("clover"), 0);
        }
        return new GrassBlockAndMeta(LOTRLegacyBlocks.vanilla("tallgrass"), 1);
    }

    /** getRandomWorldGenForDoubleFlower: lilacs, rose bushes or peonies. */
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        int meta = switch (random.nextInt(3)) {
            case 0 -> 1;
            case 1 -> 4;
            default -> 5;
        };
        return new LOTRVanillaWorldGens.DoublePlant(LOTRLegacyBlocks.vanilla("double_plant").state(meta));
    }

    /** getRandomWorldGenForDoubleGrass: tall grass, or large ferns where the decorator has ferns. */
    public LOTRWorldGenerator getRandomWorldGenForDoubleGrass(RandomSource random) {
        int meta = this.decorator.enableFern && random.nextInt(4) == 0 ? 3 : 2;
        return new LOTRVanillaWorldGens.DoublePlant(LOTRLegacyBlocks.vanilla("double_plant").state(meta));
    }

    public LOTRWorldGenerator getRandomWorldGenForGrass(RandomSource random) {
        GrassBlockAndMeta obj = getRandomGrass(random);
        return new LOTRVanillaWorldGens.TallGrass(obj.block().state(obj.meta()));
    }

    public float getTreeIncreaseChance() {
        return 0.1f;
    }

    public float getChanceToSpawnAnimals() {
        return 1.0f;
    }

    public boolean hasDomesticAnimals() {
        return false;
    }

    public boolean canSpawnHostilesInDay() {
        return false;
    }

    /** isDwarvenBiome: whether dwarves, but no wicked dwarves, spawn here by default; worked out once. */
    public boolean isDwarvenBiome(Level level) {
        if (this.initDwarven) {
            return this.isDwarven;
        }
        this.isDwarven = this.npcSpawnList.containsEntityClassByDefault(LOTRDwarfEntity.class, level)
                && !this.npcSpawnList.containsEntityClassByDefault(LOTRWickedDwarfEntity.class, level);
        this.initDwarven = true;
        return this.isDwarven;
    }

    public boolean isHiddenBiome() {
        return false;
    }

    // --- Who lives, comes and passes through here ----------------------------

    /** registerTravellingTrader: a kind of travelling trader that comes here. */
    public void registerTravellingTrader(EntityType<?> type) {
        this.spawnableTraders.add(type);
        LOTREventSpawner.createTraderSpawner(type);
    }

    public void clearTravellingTraders() {
        this.spawnableTraders.clear();
    }

    public LOTREventSpawner.EventChance getBanditChance() {
        return this.banditChance;
    }

    public void setBanditChance(LOTREventSpawner.EventChance c) {
        this.banditChance = c;
    }

    public EntityType<? extends LOTRBanditEntity> getBanditEntityClass() {
        return this.banditEntityClass == null ? LOTREntities.BANDIT : this.banditEntityClass;
    }

    public void setBanditEntityClass(EntityType<? extends LOTRBanditEntity> type) {
        this.banditEntityClass = type;
    }

    /** getNPCSpawnList: the NPC lists for a spot (some biomes give another list in some variants). */
    public LOTRBiomeSpawnList getNPCSpawnList(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRBiomeVariant variant) {
        return this.npcSpawnList;
    }

    /** getBiomeAchievement: the achievement for entering it, if any. */
    public @Nullable LOTRAchievement getBiomeAchievement() {
        return null;
    }

    /** getBiomeMusic: the music region it plays. */
    public abstract LOTRMusicRegion.Sub getBiomeMusic();

    /** getBiomeWaypoints: the waypoint region it unlocks, if any. */
    public LOTRWaypoint.@Nullable Region getBiomeWaypoints() {
        return null;
    }

    public int getSnowHeight() {
        return 0;
    }

    public boolean hasFog() {
        return this.biomeColors.foggy;
    }

    public boolean hasSky() {
        return true;
    }

    public boolean hasSeasonalGrass() {
        return this.temperature > 0.3f && this.temperature < 1.0f;
    }

    public int spawnCountMultiplier() {
        return 1;
    }

    /** BiomeColors: the colours a biome sets for itself; any left null are the game's own. */
    public static class BiomeColors {
        public static final int DEFAULT_WATER = 7186907;
        public @Nullable Integer grass;
        public @Nullable Integer foliage;
        public @Nullable Integer sky;
        public @Nullable Integer clouds;
        public @Nullable Integer fog;
        public boolean foggy;
        public int water = DEFAULT_WATER;
        public boolean hasCustomWater;

        public void setGrass(int rgb) {
            this.grass = rgb;
        }

        public void setFoliage(int rgb) {
            this.foliage = rgb;
        }

        public void setSky(int rgb) {
            this.sky = rgb;
        }

        public void setClouds(int rgb) {
            this.clouds = rgb;
        }

        public void setFog(int rgb) {
            this.fog = rgb;
        }

        public void setFoggy(boolean flag) {
            this.foggy = flag;
        }

        public void setWater(int rgb) {
            this.water = rgb;
            if (rgb != DEFAULT_WATER) {
                this.hasCustomWater = true;
            }
        }

        public void resetGrass() {
            this.grass = null;
        }

        public void resetFoliage() {
            this.foliage = null;
        }

        public void resetSky() {
            this.sky = null;
        }

        public void resetClouds() {
            this.clouds = null;
        }

        public void resetFog() {
            this.fog = null;
        }

        public void resetWater() {
            setWater(DEFAULT_WATER);
            this.hasCustomWater = false;
        }
    }

    /** BiomeTerrain: a biome's own noise scale and height stretch, when it has them (-1 when not). */
    public static class BiomeTerrain {
        public double xzScale = -1.0;
        public double heightStretchFactor = -1.0;

        public boolean hasXZScale() {
            return this.xzScale != -1.0;
        }

        public boolean hasHeightStretchFactor() {
            return this.heightStretchFactor != -1.0;
        }

        public void setXZScale(double d) {
            this.xzScale = d;
        }

        public void setHeightStretchFactor(double d) {
            this.heightStretchFactor = d;
        }

        public void resetXZScale() {
            this.xzScale = -1.0;
        }

        public void resetHeightStretchFactor() {
            this.heightStretchFactor = -1.0;
        }
    }
}
