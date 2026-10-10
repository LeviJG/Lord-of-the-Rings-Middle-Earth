package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMallornExtremeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMirkOakStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTreeType: every tree the biomes, variants and saplings grow, each made fresh for each tree
 * (create; {@code flag} is whether it grows from a sapling, and so tells its neighbours).
 *
 * <p>The mod's own trees are its generators, ported. The 1.7.10 vanilla trees it borrowed -- the
 * swamp oak, the two spruces, the mega spruce and pine, the jungle trees, acacia and dark oak --
 * are today's vanilla trees, their descendants; the cloud forest's 30-high jungle giant is
 * vanilla's mega jungle tree given the original's height.
 */
public enum LOTRTreeType {
    OAK((flag, rand) -> {
            if (rand.nextInt(4) == 0) {
                return new LOTRWorldGenGnarledOak(flag);
            }
            return new LOTRWorldGenSimpleTrees(flag, 4, 6, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0);
        }),
    OAK_TALL((flag, rand) -> {
            if (rand.nextInt(4) == 0) {
                return new LOTRWorldGenGnarledOak(flag).setMinMaxHeight(6, 10);
            }
            return new LOTRWorldGenSimpleTrees(flag, 8, 12, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0);
        }),
    OAK_TALLER((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 12, 16, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_ITHILIEN_HIDEOUT((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 6, 6, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_FANGORN_DEAD((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0).setNoLeaves()),
    OAK_SWAMP((flag, rand) -> vanilla(TreeFeatures.SWAMP_OAK)),
    OAK_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.vanilla("log"), 0)),
    OAK_DESERT((flag, rand) -> new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    OAK_SHRUB((flag, rand) -> new LOTRWorldGenShrub(LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)),
    BIRCH((flag, rand) -> {
            if (rand.nextInt(3) != 0) {
                return new LOTRWorldGenAspen(flag).setBlocks(LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2).setMinMaxHeight(8, 16);
            }
            return new LOTRWorldGenSimpleTrees(flag, 5, 7, LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2);
        }),
    BIRCH_TALL((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 8, 11, LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2)),
    BIRCH_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2)),
    BIRCH_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2)),
    BIRCH_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.vanilla("log"), 2, LOTRLegacyBlocks.vanilla("leaves"), 2)),
    BIRCH_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.vanilla("log"), 2)),
    SPRUCE((flag, rand) -> vanilla(TreeFeatures.SPRUCE)),
    SPRUCE_THIN((flag, rand) -> vanilla(TreeFeatures.PINE)),
    SPRUCE_MEGA((flag, rand) -> vanilla(TreeFeatures.MEGA_SPRUCE)),
    SPRUCE_MEGA_THIN((flag, rand) -> vanilla(TreeFeatures.MEGA_PINE)),
    SPRUCE_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.vanilla("log"), 1)),
    JUNGLE((flag, rand) -> vanilla(TreeFeatures.JUNGLE_TREE)),
    JUNGLE_LARGE((flag, rand) -> vanilla(TreeFeatures.MEGA_JUNGLE_TREE)),
    JUNGLE_CLOUD((flag, rand) -> cloudJungle()),
    JUNGLE_SHRUB((flag, rand) -> new LOTRWorldGenShrub(LOTRLegacyBlocks.vanilla("log"), 3, LOTRLegacyBlocks.vanilla("leaves"), 3)),
    JUNGLE_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.vanilla("log"), 3, LOTRLegacyBlocks.vanilla("leaves"), 3).setHeightFactor(1.5f)),
    ACACIA((flag, rand) -> vanilla(TreeFeatures.ACACIA)),
    ACACIA_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.vanilla("log2"), 0)),
    DARK_OAK((flag, rand) -> vanilla(TreeFeatures.DARK_OAK)),
    DARK_OAK_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.vanilla("log2"), 1, LOTRLegacyBlocks.vanilla("leaves2"), 1)),
    SHIRE_PINE((flag, rand) -> new LOTRWorldGenShirePine(flag)),
    MALLORN((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 6, 9, LOTRLegacyBlocks.mod("wood"), 1, LOTRLegacyBlocks.mod("leaves"), 1)),
    MALLORN_BOUGHS((flag, rand) -> new LOTRWorldGenMallorn(flag)),
    MALLORN_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("wood"), 1, LOTRLegacyBlocks.mod("leaves"), 1)),
    MALLORN_EXTREME((flag, rand) -> structureTree(new LOTRMallornExtremeStructure(flag))),
    MALLORN_EXTREME_SAPLING((flag, rand) -> structureTree(new LOTRMallornExtremeStructure(flag).setSaplingGrowth())),
    MIRK_OAK((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 4, 7, 0, true))),
    MIRK_OAK_LARGE((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 12, 16, 1, true))),
    MIRK_OAK_DEAD((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 4, 7, 0, true).setDead())),
    RED_OAK((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 6, 9, 0, false).setRedOak())),
    RED_OAK_LARGE((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 12, 17, 1, false).setRedOak())),
    RED_OAK_WEIRWOOD((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 12, 20, 1, false).setBlocks(LOTRLegacyBlocks.mod("wood9"), 0, LOTRLegacyBlocks.mod("leaves"), 3))),
    CHARRED((flag, rand) -> new LOTRWorldGenCharredTrees()),
    CHARRED_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.mod("wood"), 3, LOTRLegacyBlocks.vanilla("air"), 0).setNoLeaves()),
    APPLE((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 4, 7, LOTRLegacyBlocks.mod("fruitWood"), 0, LOTRLegacyBlocks.mod("fruitLeaves"), 0)),
    PEAR((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 4, 5, LOTRLegacyBlocks.mod("fruitWood"), 1, LOTRLegacyBlocks.mod("fruitLeaves"), 1)),
    CHERRY((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 4, 8, LOTRLegacyBlocks.mod("fruitWood"), 2, LOTRLegacyBlocks.mod("fruitLeaves"), 2)),
    CHERRY_MORDOR((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("fruitWood"), 2, LOTRLegacyBlocks.mod("fruitLeaves"), 2).disableRestrictions()),
    MANGO((flag, rand) -> {
            if (rand.nextInt(3) == 0) {
                return new LOTRWorldGenOlive(flag).setBlocks(LOTRLegacyBlocks.mod("fruitWood"), 3, LOTRLegacyBlocks.mod("fruitLeaves"), 3);
            }
            return new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.mod("fruitWood"), 3, LOTRLegacyBlocks.mod("fruitLeaves"), 3);
        }),
    LEBETHRON((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 5, 9, LOTRLegacyBlocks.mod("wood2"), 0, LOTRLegacyBlocks.mod("leaves2"), 0)),
    LEBETHRON_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.mod("wood2"), 0, LOTRLegacyBlocks.mod("leaves2"), 0)),
    LEBETHRON_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("wood2"), 0, LOTRLegacyBlocks.mod("leaves2"), 0)),
    LEBETHRON_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.mod("wood2"), 0)),
    BEECH((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 5, 9, LOTRLegacyBlocks.mod("wood2"), 1, LOTRLegacyBlocks.mod("leaves2"), 1)),
    BEECH_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.mod("wood2"), 1, LOTRLegacyBlocks.mod("leaves2"), 1)),
    BEECH_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("wood2"), 1, LOTRLegacyBlocks.mod("leaves2"), 1)),
    BEECH_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.mod("wood2"), 1, LOTRLegacyBlocks.mod("leaves2"), 1)),
    BEECH_FANGORN_DEAD((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.mod("wood2"), 1, LOTRLegacyBlocks.mod("leaves2"), 1).setNoLeaves()),
    BEECH_DEAD((flag, rand) -> new LOTRWorldGenDeadTrees(LOTRLegacyBlocks.mod("wood2"), 1)),
    HOLLY((flag, rand) -> new LOTRWorldGenHolly(flag)),
    HOLLY_LARGE((flag, rand) -> new LOTRWorldGenHolly(flag).setLarge()),
    BANANA((flag, rand) -> new LOTRWorldGenBanana(flag)),
    MAPLE((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 4, 8, LOTRLegacyBlocks.mod("wood3"), 0, LOTRLegacyBlocks.mod("leaves3"), 0)),
    MAPLE_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.mod("wood3"), 0, LOTRLegacyBlocks.mod("leaves3"), 0)),
    MAPLE_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("wood3"), 0, LOTRLegacyBlocks.mod("leaves3"), 0)),
    LARCH((flag, rand) -> new LOTRWorldGenLarch(flag)),
    DATE_PALM((flag, rand) -> new LOTRWorldGenPalm(flag, LOTRLegacyBlocks.mod("wood3"), 2, LOTRLegacyBlocks.mod("leaves3"), 2).setMinMaxHeight(5, 8).setDates()),
    MANGROVE((flag, rand) -> new LOTRWorldGenMangrove(flag)),
    CHESTNUT((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 5, 7, LOTRLegacyBlocks.mod("wood4"), 0, LOTRLegacyBlocks.mod("leaves4"), 0)),
    CHESTNUT_LARGE((flag, rand) -> new LOTRWorldGenBigTrees(flag, LOTRLegacyBlocks.mod("wood4"), 0, LOTRLegacyBlocks.mod("leaves4"), 0)),
    CHESTNUT_PARTY((flag, rand) -> new LOTRWorldGenPartyTrees(LOTRLegacyBlocks.mod("wood4"), 0, LOTRLegacyBlocks.mod("leaves4"), 0)),
    BAOBAB((flag, rand) -> new LOTRWorldGenBaobab(flag)),
    CEDAR((flag, rand) -> new LOTRWorldGenCedar(flag)),
    CEDAR_LARGE((flag, rand) -> new LOTRWorldGenCedar(flag).setMinMaxHeight(15, 30)),
    FIR((flag, rand) -> new LOTRWorldGenFir(flag)),
    PINE((flag, rand) -> new LOTRWorldGenPine(flag)),
    PINE_SHRUB((flag, rand) -> new LOTRWorldGenShrub(LOTRLegacyBlocks.mod("wood5"), 0, LOTRLegacyBlocks.mod("leaves5"), 0)),
    LEMON((flag, rand) -> {
            if (rand.nextInt(3) == 0) {
                return new LOTRWorldGenOlive(flag).setBlocks(LOTRLegacyBlocks.mod("wood5"), 1, LOTRLegacyBlocks.mod("leaves5"), 1);
            }
            return new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.mod("wood5"), 1, LOTRLegacyBlocks.mod("leaves5"), 1);
        }),
    ORANGE((flag, rand) -> {
            if (rand.nextInt(3) == 0) {
                return new LOTRWorldGenOlive(flag).setBlocks(LOTRLegacyBlocks.mod("wood5"), 2, LOTRLegacyBlocks.mod("leaves5"), 2);
            }
            return new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.mod("wood5"), 2, LOTRLegacyBlocks.mod("leaves5"), 2);
        }),
    LIME((flag, rand) -> {
            if (rand.nextInt(3) == 0) {
                return new LOTRWorldGenOlive(flag).setBlocks(LOTRLegacyBlocks.mod("wood5"), 3, LOTRLegacyBlocks.mod("leaves5"), 3);
            }
            return new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.mod("wood5"), 3, LOTRLegacyBlocks.mod("leaves5"), 3);
        }),
    MAHOGANY((flag, rand) -> new LOTRWorldGenCedar(flag).setBlocks(LOTRLegacyBlocks.mod("wood6"), 0, LOTRLegacyBlocks.mod("leaves6"), 0).setHangingLeaves()),
    MAHOGANY_FANGORN((flag, rand) -> new LOTRWorldGenFangornTrees(flag, LOTRLegacyBlocks.mod("wood6"), 0, LOTRLegacyBlocks.mod("leaves6"), 0).setHeightFactor(1.5f)),
    WILLOW((flag, rand) -> new LOTRWorldGenWillow(flag)),
    WILLOW_WATER((flag, rand) -> new LOTRWorldGenWillow(flag).setNeedsWater()),
    CYPRESS((flag, rand) -> new LOTRWorldGenCypress(flag)),
    CYPRESS_LARGE((flag, rand) -> new LOTRWorldGenCypress(flag).setLarge()),
    OLIVE((flag, rand) -> new LOTRWorldGenOlive(flag)),
    OLIVE_LARGE((flag, rand) -> new LOTRWorldGenOlive(flag).setMinMaxHeight(5, 8).setExtraTrunkWidth(1)),
    ASPEN((flag, rand) -> new LOTRWorldGenAspen(flag)),
    ASPEN_LARGE((flag, rand) -> new LOTRWorldGenAspen(flag).setExtraTrunkWidth(1).setMinMaxHeight(14, 25)),
    GREEN_OAK((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 4, 7, 0, false).setGreenOak())),
    GREEN_OAK_LARGE((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 12, 16, 1, false).setGreenOak())),
    GREEN_OAK_EXTREME((flag, rand) -> structureTree(new LOTRMirkOakStructure(flag, 25, 45, 2, false).setGreenOak())),
    LAIRELOSSE((flag, rand) -> new LOTRWorldGenLairelosse(flag)),
    LAIRELOSSE_LARGE((flag, rand) -> new LOTRWorldGenLairelosse(flag).setExtraTrunkWidth(1).setMinMaxHeight(8, 12)),
    ALMOND((flag, rand) -> new LOTRWorldGenAlmond(flag)),
    PLUM((flag, rand) -> new LOTRWorldGenSimpleTrees(flag, 4, 6, LOTRLegacyBlocks.mod("wood8"), 0, LOTRLegacyBlocks.mod("leaves8"), 0)),
    REDWOOD((flag, rand) -> new LOTRWorldGenRedwood(flag)),
    REDWOOD_2((flag, rand) -> new LOTRWorldGenRedwood(flag).setExtraTrunkWidth(1)),
    REDWOOD_3((flag, rand) -> new LOTRWorldGenRedwood(flag).setTrunkWidth(1)),
    REDWOOD_4((flag, rand) -> new LOTRWorldGenRedwood(flag).setTrunkWidth(1).setExtraTrunkWidth(1)),
    REDWOOD_5((flag, rand) -> new LOTRWorldGenRedwood(flag).setTrunkWidth(2)),
    POMEGRANATE((flag, rand) -> {
            if (rand.nextInt(3) == 0) {
                return new LOTRWorldGenOlive(flag).setBlocks(LOTRLegacyBlocks.mod("wood8"), 2, LOTRLegacyBlocks.mod("leaves8"), 2);
            }
            return new LOTRWorldGenDesertTrees(flag, LOTRLegacyBlocks.mod("wood8"), 2, LOTRLegacyBlocks.mod("leaves8"), 2);
        }),
    PALM((flag, rand) -> new LOTRWorldGenPalm(flag, LOTRLegacyBlocks.mod("wood8"), 3, LOTRLegacyBlocks.mod("leaves8"), 3).setMinMaxHeight(6, 11)),
    DRAGONBLOOD((flag, rand) -> new LOTRWorldGenDragonblood(flag, 3, 7, 0)),
    DRAGONBLOOD_LARGE((flag, rand) -> new LOTRWorldGenDragonblood(flag, 6, 10, 1)),
    DRAGONBLOOD_HUGE((flag, rand) -> new LOTRWorldGenDragonblood(flag, 8, 16, 2)),
    KANUKA((flag, rand) -> new LOTRWorldGenKanuka(flag)),
    NULL(null);

    public interface TreeFactory {
        LOTRWorldGenerator createTree(boolean flag, RandomSource rand);
    }

    private final @Nullable TreeFactory treeFactory;

    LOTRTreeType(@Nullable TreeFactory factory) {
        this.treeFactory = factory;
    }

    /** The tree, or (NULL) one that places nothing. */
    public LOTRWorldGenerator create(boolean flag, RandomSource random) {
        if (this.treeFactory == null) {
            return (world, rand, i, j, k) -> false;
        }
        return this.treeFactory.createTree(flag, random);
    }

    /** One of vanilla's configured trees, planted at the position given. */
    private static LOTRWorldGenerator vanilla(ResourceKey<ConfiguredFeature<?, ?>> key) {
        return (world, random, i, j, k) -> world.registryAccess().lookup(Registries.CONFIGURED_FEATURE)
                .flatMap(registry -> registry.get(key))
                .map(feature -> feature.value().place(world, world.getLevel().getChunkSource().getGenerator(), random, new BlockPos(i, j, k)))
                .orElse(false);
    }

    /** WorldGenMegaJungle(flag, 30, 30, 3, 3): a jungle giant thirty high. */
    private static LOTRWorldGenerator cloudJungle() {
        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.JUNGLE_LOG),
                new MegaJungleTrunkPlacer(30, 0, 0),
                BlockStateProvider.simple(Blocks.JUNGLE_LEAVES),
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 1, 2),
                BlockStateProvider.simple(Blocks.DIRT))
                .decorators(List.of(TrunkVineDecorator.INSTANCE, new LeaveVineDecorator(0.25f))).build();
        return (world, random, i, j, k) -> Feature.TREE.place(config, world, world.getLevel().getChunkSource().getGenerator(),
                random, new BlockPos(i, j, k));
    }

    /** A tree built on the structure base (the extreme mallorn, the mirk-oaks): its leaves' distances set after. */
    private static LOTRWorldGenerator structureTree(LOTRWorldGenerator tree) {
        return new LOTRFeature.Wrapped(tree, 24, 64);
    }

    public record WeightedTreeType(LOTRTreeType treeType, int itemWeight) implements LOTRWeightedRandom.Item {
    }
}
