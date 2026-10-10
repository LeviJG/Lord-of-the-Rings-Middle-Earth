package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * The saplings' growTree (LOTRBlockSapling to LOTRBlockSapling9, LOTRBlockFruitSapling, and
 * LOTRVanillaSaplings for vanilla's own): which tree a sapling grows, the larger kinds needing
 * their saplings planted together -- a 5 x 5 of mallorns on quendite grass for the great mallorn,
 * a 3 x 3 for a party tree, five in a cross for mallorn boughs, 2 x 2, 3 x 3, 4 x 4 or 5 x 5 for
 * the giants. The saplings are cleared and the tree grown from the square's corner; if it will
 * not grow, the saplings are put back.
 */
public final class LOTRSaplingGrowth {

    /** Where the tree stands relative to the sapling, and the square of saplings it takes. */
    private record Match(int xOffset, int zOffset, int trunkNeg, int trunkPos, boolean cross) {
    }

    @FunctionalInterface
    private interface Shape {
        @Nullable Match find(ServerLevel level, BlockPos pos, Block sapling);
    }

    private record Rule(Shape shape, Function<RandomSource, LOTRTreeType> tree) {
    }

    private static final Map<Block, List<Rule>> RULES = new HashMap<>();

    private LOTRSaplingGrowth() {
    }

    private static boolean isSameSapling(ServerLevel level, int i, int j, int k, Block sapling) {
        return level.getBlockState(new BlockPos(i, j, k)).is(sapling);
    }

    /** findSaplingSquare: a square of {@code squareMin..squareMax} saplings about a point within {@code searchMin..searchMax}. */
    private static Shape square(int squareMin, int squareMax, int searchMin, int searchMax, int trunkNeg, int trunkPos) {
        return (level, pos, sapling) -> {
            for (int i1 = searchMin; i1 <= searchMax; ++i1) {
                for (int k1 = searchMin; k1 <= searchMax; ++k1) {
                    boolean canGenerate = true;
                    search:
                    for (int i2 = squareMin; i2 <= squareMax; ++i2) {
                        for (int k2 = squareMin; k2 <= squareMax; ++k2) {
                            if (!isSameSapling(level, pos.getX() + i1 + i2, pos.getY(), pos.getZ() + k1 + k2, sapling)) {
                                canGenerate = false;
                                break search;
                            }
                        }
                    }
                    if (canGenerate) {
                        return new Match(i1, k1, trunkNeg, trunkPos, false);
                    }
                }
            }
            return null;
        };
    }

    /** findPartyTree: a 3 x 3 square. */
    private static Shape party() {
        return square(-1, 1, -2, 2, 1, 1);
    }

    /** The 2 x 2 squares the saplings looked for themselves, from the sapling itself first. */
    private static Shape twoByTwo() {
        return (level, pos, sapling) -> {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            for (int i1 = 0; i1 >= -1; --i1) {
                for (int k1 = 0; k1 >= -1; --k1) {
                    if (isSameSapling(level, i + i1, j, k + k1, sapling) && isSameSapling(level, i + i1 + 1, j, k + k1, sapling)
                            && isSameSapling(level, i + i1, j, k + k1 + 1, sapling) && isSameSapling(level, i + i1 + 1, j, k + k1 + 1, sapling)) {
                        return new Match(i1, k1, 0, 1, false);
                    }
                }
            }
            return null;
        };
    }

    /** findCrossShape: five saplings in a cross. */
    private static Shape cross() {
        return (level, pos, sapling) -> {
            for (int i1 = -2; i1 <= 2; ++i1) {
                for (int k1 = -2; k1 <= 2; ++k1) {
                    if (i1 != 0 && k1 != 0) {
                        continue;
                    }
                    boolean canGenerate = true;
                    search:
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            if (i2 != 0 && k2 != 0) {
                                continue;
                            }
                            if (!isSameSapling(level, pos.getX() + i1 + i2, pos.getY(), pos.getZ() + k1 + k2, sapling)) {
                                canGenerate = false;
                                break search;
                            }
                        }
                    }
                    if (canGenerate) {
                        return new Match(i1, k1, 1, 1, true);
                    }
                }
            }
            return null;
        };
    }

    /** The great mallorn: a 5 x 5 of mallorn saplings, every one on quendite grass. */
    private static Shape mallornExtreme() {
        return (level, pos, sapling) -> {
            Block quendite = LOTRBuildingBlocks.QUENDITE_GRASS;
            if (!level.getBlockState(pos.below()).is(quendite)) {
                return null;
            }
            for (int i1 = -4; i1 <= 4; ++i1) {
                for (int k1 = -4; k1 <= 4; ++k1) {
                    boolean canGenerate = true;
                    search:
                    for (int i2 = -2; i2 <= 2; ++i2) {
                        for (int k2 = -2; k2 <= 2; ++k2) {
                            int i3 = pos.getX() + i1 + i2;
                            int k3 = pos.getZ() + k1 + k2;
                            if (!isSameSapling(level, i3, pos.getY(), k3, sapling)
                                    || !level.getBlockState(new BlockPos(i3, pos.getY() - 1, k3)).is(quendite)) {
                                canGenerate = false;
                                break search;
                            }
                        }
                    }
                    if (canGenerate) {
                        return new Match(i1, k1, 2, 2, false);
                    }
                }
            }
            return null;
        };
    }

    private static Shape single() {
        return (level, pos, sapling) -> new Match(0, 0, 0, 0, false);
    }

    private static Function<RandomSource, LOTRTreeType> tree(LOTRTreeType type) {
        return random -> type;
    }

    /** One in ten the large kind, else the ordinary. */
    private static Function<RandomSource, LOTRTreeType> sometimesLarge(LOTRTreeType large, LOTRTreeType normal) {
        return random -> random.nextInt(10) == 0 ? large : normal;
    }

    private static void rules(Block sapling, Rule... rules) {
        RULES.put(sapling, new ArrayList<>(List.of(rules)));
    }

    private static Rule rule(Shape shape, LOTRTreeType tree) {
        return new Rule(shape, tree(tree));
    }

    private static Rule rule(Shape shape, Function<RandomSource, LOTRTreeType> tree) {
        return new Rule(shape, tree);
    }

    public static void init() {
        // LOTRBlockSapling: Shire pine, mallorn, mirk-oak, red oak.
        rules(LOTRDecorationBlocks.SHIRE_PINE_SAPLING, rule(single(), LOTRTreeType.SHIRE_PINE));
        rules(LOTRDecorationBlocks.MALLORN_SAPLING, rule(mallornExtreme(), LOTRTreeType.MALLORN_EXTREME_SAPLING),
                rule(party(), LOTRTreeType.MALLORN_PARTY), rule(cross(), LOTRTreeType.MALLORN_BOUGHS), rule(single(), LOTRTreeType.MALLORN));
        rules(LOTRDecorationBlocks.MIRK_OAK_SAPLING, rule(party(), LOTRTreeType.MIRK_OAK_LARGE), rule(single(), LOTRTreeType.MIRK_OAK));
        rules(LOTRDecorationBlocks.MIRK_OAK_RED_SAPLING, rule(party(), LOTRTreeType.RED_OAK_LARGE), rule(single(), LOTRTreeType.RED_OAK));
        // LOTRBlockSapling2: lebethron, beech, holly, banana.
        rules(LOTRDecorationBlocks.LEBETHRON_SAPLING, rule(party(), LOTRTreeType.LEBETHRON_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.LEBETHRON_LARGE, LOTRTreeType.LEBETHRON)));
        rules(LOTRDecorationBlocks.BEECH_SAPLING, rule(party(), LOTRTreeType.BEECH_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.BEECH_LARGE, LOTRTreeType.BEECH)));
        rules(LOTRDecorationBlocks.HOLLY_SAPLING, rule(twoByTwo(), LOTRTreeType.HOLLY_LARGE), rule(single(), LOTRTreeType.HOLLY));
        rules(LOTRDecorationBlocks.BANANA_SAPLING, rule(single(), LOTRTreeType.BANANA));
        // LOTRBlockSapling3: maple, larch, date palm, mangrove.
        rules(LOTRDecorationBlocks.MAPLE_SAPLING, rule(party(), LOTRTreeType.MAPLE_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.MAPLE_LARGE, LOTRTreeType.MAPLE)));
        rules(LOTRDecorationBlocks.LARCH_SAPLING, rule(single(), LOTRTreeType.LARCH));
        rules(LOTRDecorationBlocks.DATE_PALM_SAPLING, rule(single(), LOTRTreeType.DATE_PALM));
        rules(LOTRDecorationBlocks.MANGROVE_SAPLING, rule(single(), LOTRTreeType.MANGROVE));
        // LOTRBlockSapling4: chestnut, baobab, cedar, fir.
        rules(LOTRDecorationBlocks.CHESTNUT_SAPLING, rule(party(), LOTRTreeType.CHESTNUT_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.CHESTNUT_LARGE, LOTRTreeType.CHESTNUT)));
        rules(LOTRDecorationBlocks.BAOBAB_SAPLING, rule(single(), LOTRTreeType.BAOBAB));
        rules(LOTRDecorationBlocks.CEDAR_SAPLING, rule(single(), LOTRTreeType.CEDAR));
        rules(LOTRDecorationBlocks.FIR_SAPLING, rule(single(), LOTRTreeType.FIR));
        // LOTRBlockSapling5: pine, lemon, orange, lime.
        rules(LOTRDecorationBlocks.PINE_SAPLING, rule(single(), LOTRTreeType.PINE));
        rules(LOTRDecorationBlocks.LEMON_SAPLING, rule(single(), LOTRTreeType.LEMON));
        rules(LOTRDecorationBlocks.ORANGE_SAPLING, rule(single(), LOTRTreeType.ORANGE));
        rules(LOTRDecorationBlocks.LIME_SAPLING, rule(single(), LOTRTreeType.LIME));
        // LOTRBlockSapling6: mahogany, willow, cypress, olive.
        rules(LOTRDecorationBlocks.MAHOGANY_SAPLING, rule(single(), LOTRTreeType.MAHOGANY));
        rules(LOTRDecorationBlocks.WILLOW_SAPLING, rule(single(), LOTRTreeType.WILLOW));
        rules(LOTRDecorationBlocks.CYPRESS_SAPLING, rule(twoByTwo(), LOTRTreeType.CYPRESS_LARGE), rule(single(), LOTRTreeType.CYPRESS));
        rules(LOTRDecorationBlocks.OLIVE_SAPLING, rule(twoByTwo(), LOTRTreeType.OLIVE_LARGE), rule(single(), LOTRTreeType.OLIVE));
        // LOTRBlockSapling7: aspen, green oak, lairelossë, almond.
        rules(LOTRDecorationBlocks.ASPEN_SAPLING, rule(twoByTwo(), LOTRTreeType.ASPEN_LARGE), rule(single(), LOTRTreeType.ASPEN));
        rules(LOTRDecorationBlocks.GREEN_OAK_SAPLING, rule(party(), LOTRTreeType.GREEN_OAK_LARGE), rule(single(), LOTRTreeType.GREEN_OAK));
        rules(LOTRDecorationBlocks.LAIRELOSSE_SAPLING, rule(twoByTwo(), LOTRTreeType.LAIRELOSSE_LARGE), rule(single(), LOTRTreeType.LAIRELOSSE));
        rules(LOTRDecorationBlocks.ALMOND_SAPLING, rule(single(), LOTRTreeType.ALMOND));
        // LOTRBlockSapling8: plum, redwood, pomegranate, palm.
        rules(LOTRDecorationBlocks.PLUM_SAPLING, rule(single(), LOTRTreeType.PLUM));
        rules(LOTRDecorationBlocks.REDWOOD_SAPLING, rule(square(-2, 2, -4, 4, 2, 2), LOTRTreeType.REDWOOD_5),
                rule(square(-1, 2, -2, 1, 1, 2), LOTRTreeType.REDWOOD_4), rule(party(), LOTRTreeType.REDWOOD_3),
                rule(square(0, 1, -1, 0, 0, 1), LOTRTreeType.REDWOOD_2), rule(single(), LOTRTreeType.REDWOOD));
        rules(LOTRDecorationBlocks.POMEGRANATE_SAPLING, rule(single(), LOTRTreeType.POMEGRANATE));
        rules(LOTRDecorationBlocks.PALM_SAPLING, rule(single(), LOTRTreeType.PALM));
        // LOTRBlockSapling9: dragonblood, kanuka.
        rules(LOTRDecorationBlocks.DRAGON_SAPLING, rule(square(-2, 2, -4, 4, 2, 2), LOTRTreeType.DRAGONBLOOD_HUGE),
                rule(party(), LOTRTreeType.DRAGONBLOOD_LARGE), rule(single(), LOTRTreeType.DRAGONBLOOD));
        rules(LOTRDecorationBlocks.KANUKA_SAPLING, rule(single(), LOTRTreeType.KANUKA));
        // LOTRBlockFruitSapling: apple, pear, cherry, mango.
        rules(LOTRDecorationBlocks.APPLE_SAPLING, rule(single(), LOTRTreeType.APPLE));
        rules(LOTRDecorationBlocks.PEAR_SAPLING, rule(single(), LOTRTreeType.PEAR));
        rules(LOTRDecorationBlocks.CHERRY_SAPLING, rule(single(), LOTRTreeType.CHERRY));
        rules(LOTRDecorationBlocks.MANGO_SAPLING, rule(single(), LOTRTreeType.MANGO));
        // LOTRVanillaSaplings: vanilla's own six, as the original grew them (onSaplingGrow).
        rules(Blocks.OAK_SAPLING, rule(party(), LOTRTreeType.OAK_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.OAK_LARGE, LOTRTreeType.OAK)));
        rules(Blocks.SPRUCE_SAPLING, rule(twoByTwo(), random -> random.nextBoolean() ? LOTRTreeType.SPRUCE_MEGA : LOTRTreeType.SPRUCE_MEGA_THIN),
                rule(single(), LOTRTreeType.SPRUCE));
        rules(Blocks.BIRCH_SAPLING, rule(party(), LOTRTreeType.BIRCH_PARTY),
                rule(single(), sometimesLarge(LOTRTreeType.BIRCH_LARGE, LOTRTreeType.BIRCH)));
        rules(Blocks.JUNGLE_SAPLING, rule(twoByTwo(), LOTRTreeType.JUNGLE_LARGE), rule(single(), LOTRTreeType.JUNGLE));
        rules(Blocks.ACACIA_SAPLING, rule(single(), LOTRTreeType.ACACIA));
        rules(Blocks.DARK_OAK_SAPLING, rule(party(), LOTRTreeType.DARK_OAK_PARTY), rule(twoByTwo(), LOTRTreeType.DARK_OAK));
    }

    public static boolean hasRules(Block sapling) {
        return RULES.containsKey(sapling);
    }

    /** growTree: grows the sapling at pos, if one of its trees will grow; true if it did. */
    public static boolean growTree(ServerLevel level, BlockPos pos, BlockState saplingState, RandomSource random) {
        List<Rule> rules = RULES.get(saplingState.getBlock());
        if (rules == null) {
            return false;
        }
        Block sapling = saplingState.getBlock();
        for (Rule rule : rules) {
            Match match = rule.shape().find(level, pos, sapling);
            if (match == null) {
                continue;
            }
            LOTRWorldGenerator tree = rule.tree().apply(random).create(true, random);
            setSquare(level, pos, match, Blocks.AIR.defaultBlockState());
            if (tree.generate(level, random, pos.getX() + match.xOffset(), pos.getY(), pos.getZ() + match.zOffset())) {
                return true;
            }
            setSquare(level, pos, match, saplingState.hasProperty(net.minecraft.world.level.block.SaplingBlock.STAGE)
                    ? saplingState.setValue(net.minecraft.world.level.block.SaplingBlock.STAGE, 0) : saplingState);
            return false;
        }
        return false;
    }

    private static void setSquare(ServerLevel level, BlockPos pos, Match match, BlockState state) {
        for (int i1 = -match.trunkNeg(); i1 <= match.trunkPos(); ++i1) {
            for (int k1 = -match.trunkNeg(); k1 <= match.trunkPos(); ++k1) {
                if (match.cross() && i1 != 0 && k1 != 0) {
                    continue;
                }
                level.setBlock(pos.offset(match.xOffset() + i1, 0, match.zOffset() + k1), state, Block.UPDATE_INVISIBLE);
            }
        }
    }
}
