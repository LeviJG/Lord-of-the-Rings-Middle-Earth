package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import java.util.HashSet;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The small 1.7.10 vanilla generators the biome decorators used -- ore veins, flower and grass
 * patches, double plants, sugar cane, pumpkins and melons, lily pads, vines, cacti, dead bushes --
 * as they placed things, so a Middle-earth chunk is strewn as the original strewed it.
 */
public final class LOTRVanillaWorldGens {

    private LOTRVanillaWorldGens() {
    }

    private static boolean air(WorldGenLevel world, int i, int j, int k) {
        return world.isEmptyBlock(new BlockPos(i, j, k));
    }

    private static boolean canStay(BlockState state, WorldGenLevel world, int i, int j, int k) {
        return state.canSurvive(world, new BlockPos(i, j, k));
    }

    /**
     * WorldGenMinable: a vein of {@code number} ore blocks through the target block (stone), of any
     * of its old metadata, as Forge's isReplaceableOreGen compared the block alone.
     */
    public static final class Minable extends LOTRFeature {
        private final BlockState ore;
        private final int number;
        private final Set<Block> target = new HashSet<>();

        public Minable(LegacyBlock block, int number) {
            this(block, 0, number, LOTRLegacyBlocks.vanilla("stone"));
        }

        public Minable(LegacyBlock block, int number, LegacyBlock target) {
            this(block, 0, number, target);
        }

        public Minable(LegacyBlock block, int meta, int number, LegacyBlock target) {
            this.ore = block.state(meta);
            this.number = number;
            for (int m = 0; m < 16; ++m) {
                this.target.add(target.state(m).getBlock());
            }
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            float f = rand.nextFloat() * (float) Math.PI;
            double d0 = x + 8 + Mth.sin(f) * this.number / 8.0f;
            double d1 = x + 8 - Mth.sin(f) * this.number / 8.0f;
            double d2 = z + 8 + Mth.cos(f) * this.number / 8.0f;
            double d3 = z + 8 - Mth.cos(f) * this.number / 8.0f;
            double d4 = y + rand.nextInt(3) - 2;
            double d5 = y + rand.nextInt(3) - 2;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for (int l = 0; l <= this.number; ++l) {
                double d6 = d0 + (d1 - d0) * l / this.number;
                double d7 = d4 + (d5 - d4) * l / this.number;
                double d8 = d2 + (d3 - d2) * l / this.number;
                double d9 = rand.nextDouble() * this.number / 16.0;
                double d10 = (Mth.sin(l * (float) Math.PI / this.number) + 1.0f) * d9 + 1.0;
                double d11 = (Mth.sin(l * (float) Math.PI / this.number) + 1.0f) * d9 + 1.0;
                int i1 = Mth.floor(d6 - d10 / 2.0);
                int j1 = Mth.floor(d7 - d11 / 2.0);
                int k1 = Mth.floor(d8 - d10 / 2.0);
                int l1 = Mth.floor(d6 + d10 / 2.0);
                int i2 = Mth.floor(d7 + d11 / 2.0);
                int j2 = Mth.floor(d8 + d10 / 2.0);
                for (int k2 = i1; k2 <= l1; ++k2) {
                    double d12 = (k2 + 0.5 - d6) / (d10 / 2.0);
                    if (d12 * d12 >= 1.0) {
                        continue;
                    }
                    for (int l2 = j1; l2 <= i2; ++l2) {
                        double d13 = (l2 + 0.5 - d7) / (d11 / 2.0);
                        if (d12 * d12 + d13 * d13 >= 1.0) {
                            continue;
                        }
                        for (int i3 = k1; i3 <= j2; ++i3) {
                            double d14 = (i3 + 0.5 - d8) / (d10 / 2.0);
                            if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0 && this.target.contains(world.getBlockState(pos.set(k2, l2, i3)).getBlock())) {
                                world.setBlock(pos, this.ore, Block.UPDATE_CLIENTS);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    /** WorldGenFlowers: a scatter of one plant -- a flower, a mushroom, athelas. */
    public static final class Flowers extends LOTRFeature {
        private final BlockState plant;

        public Flowers(LegacyBlock block) {
            this(block.state(0));
        }

        public Flowers(BlockState plant) {
            this.plant = plant;
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            for (int l = 0; l < 64; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && j1 < 255 && canStay(this.plant, world, i1, j1, k1)) {
                    world.setBlock(new BlockPos(i1, j1, k1), this.plant, Block.UPDATE_CLIENTS);
                }
            }
            return true;
        }
    }

    /** WorldGenTallGrass: down to the ground through air and leaves, then a patch of the grass about it. */
    public static final class TallGrass extends LOTRFeature {
        private final BlockState grass;

        public TallGrass(BlockState grass) {
            this.grass = grass;
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            BlockState state;
            while (((state = getBlock(world, x, y, z)).isAir() || isLeaves(state)) && y > 0) {
                --y;
            }
            for (int l = 0; l < 128; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && canStay(this.grass, world, i1, j1, k1)) {
                    world.setBlock(new BlockPos(i1, j1, k1), this.grass, Block.UPDATE_CLIENTS);
                }
            }
            return true;
        }
    }

    /** WorldGenDoublePlant: a patch of one two-block plant (its lower half given). */
    public static final class DoublePlant extends LOTRFeature {
        private BlockState lower;

        public DoublePlant(BlockState lower) {
            this.lower = lower;
        }

        /** {@code new WorldGenDoublePlant()}: sunflowers until func_150548_a says otherwise. */
        public DoublePlant() {
            this(LOTRLegacyBlocks.vanilla("double_plant").state(0));
        }

        /** func_150548_a: the kind, by the double plant's old metadata (0 sunflower ... 5 peony). */
        public void func_150548_a(int meta) {
            this.lower = LOTRLegacyBlocks.vanilla("double_plant").state(meta);
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            boolean flag = false;
            for (int l = 0; l < 64; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && j1 < 254 && placeDoublePlant(world, this.lower, i1, j1, k1, true)) {
                    flag = true;
                }
            }
            return flag;
        }
    }

    /** WorldGenReed: sugar cane, one to three high, beside water. */
    public static final class Reed extends LOTRFeature {
        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            BlockState cane = Blocks.SUGAR_CANE.defaultBlockState();
            for (int l = 0; l < 20; ++l) {
                int i1 = x + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(4) - rand.nextInt(4);
                if (!air(world, i1, y, k1)) {
                    continue;
                }
                if (isWater(getBlock(world, i1 - 1, y - 1, k1)) || isWater(getBlock(world, i1 + 1, y - 1, k1))
                        || isWater(getBlock(world, i1, y - 1, k1 - 1)) || isWater(getBlock(world, i1, y - 1, k1 + 1))) {
                    int height = 2 + rand.nextInt(rand.nextInt(3) + 1);
                    for (int j1 = 0; j1 < height; ++j1) {
                        if (canStay(cane, world, i1, y + j1, k1)) {
                            world.setBlock(new BlockPos(i1, y + j1, k1), cane, Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
            return true;
        }
    }

    /** WorldGenPumpkin and WorldGenMelon: a few on the grass. */
    public static final class Gourd extends LOTRFeature {
        private final boolean pumpkin;

        public Gourd(boolean pumpkin) {
            this.pumpkin = pumpkin;
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            for (int l = 0; l < 64; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && getBlock(world, i1, j1 - 1, k1).is(Blocks.GRASS_BLOCK)) {
                    // 1.7.10's wild pumpkins had a face, which today's carved pumpkin keeps; today's wild ones are plain.
                    if (this.pumpkin) {
                        rand.nextInt(4);
                    }
                    BlockState state = this.pumpkin ? Blocks.PUMPKIN.defaultBlockState() : Blocks.MELON.defaultBlockState();
                    world.setBlock(new BlockPos(i1, j1, k1), state, Block.UPDATE_CLIENTS);
                }
            }
            return true;
        }
    }

    /** WorldGenWaterlily: lily pads on still water. */
    public static final class Waterlily extends LOTRFeature {
        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            BlockState lily = Blocks.LILY_PAD.defaultBlockState();
            for (int l = 0; l < 10; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && canStay(lily, world, i1, j1, k1)) {
                    world.setBlock(new BlockPos(i1, j1, k1), lily, Block.UPDATE_CLIENTS);
                }
            }
            return true;
        }
    }

    /** WorldGenVines: up from y 64, vines on any face they will hang from, wandering where blocked. */
    public static final class Vines extends LOTRFeature {
        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            int x0 = x;
            int z0 = z;
            for (; y < 128; ++y) {
                if (air(world, x, y, z)) {
                    BlockPos pos = new BlockPos(x, y, z);
                    // 1.7.10 tried the sides 2-5 in turn, each against the block beyond it: south, north, east, west.
                    for (Direction dir : new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST}) {
                        BlockState vine = Blocks.VINE.defaultBlockState().setValue(VineBlock.getPropertyForFace(dir), true);
                        if (VineBlock.isAcceptableNeighbour(world, pos.relative(dir), dir.getOpposite())) {
                            world.setBlock(pos, vine, Block.UPDATE_CLIENTS);
                            break;
                        }
                    }
                } else {
                    x = x0 + rand.nextInt(4) - rand.nextInt(4);
                    z = z0 + rand.nextInt(4) - rand.nextInt(4);
                }
            }
            return true;
        }
    }

    /** WorldGenCactus: a few cacti, one to three high. */
    public static final class Cactus extends LOTRFeature {
        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            BlockState cactus = Blocks.CACTUS.defaultBlockState();
            for (int l = 0; l < 10; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (!air(world, i1, j1, k1)) {
                    continue;
                }
                int height = 1 + rand.nextInt(rand.nextInt(3) + 1);
                for (int l1 = 0; l1 < height; ++l1) {
                    if (canStay(cactus, world, i1, j1 + l1, k1)) {
                        world.setBlock(new BlockPos(i1, j1 + l1, k1), cactus, Block.UPDATE_CLIENTS);
                    }
                }
            }
            return true;
        }
    }

    /** WorldGenDeadBush: down to the ground through air and leaves, then a few dead bushes. */
    public static final class DeadBush extends LOTRFeature {
        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
            BlockState bush = Blocks.DEAD_BUSH.defaultBlockState();
            BlockState state;
            while (((state = getBlock(world, x, y, z)).isAir() || isLeaves(state)) && y > 0) {
                --y;
            }
            for (int l = 0; l < 4; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);
                if (air(world, i1, j1, k1) && canStay(bush, world, i1, j1, k1)) {
                    world.setBlock(new BlockPos(i1, j1, k1), bush, Block.UPDATE_CLIENTS);
                }
            }
            return true;
        }
    }
}
