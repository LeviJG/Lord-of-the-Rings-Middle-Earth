package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMugBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldGenStructureBase: the older way the original built a structure --
 * straight in world coordinates, with no rotation or bounding box of its own;
 * the structure turns itself where it needs to. A few structures still use
 * it. As with the newer base, blocks are set without neighbour updates and
 * given their shapes when the whole structure is placed.
 *
 * <p>NOT ported yet: banners (with the banner entities, D14); the mod's flower
 * pot; the spawner chests; the biome checks natural generation makes (D10).
 */
public abstract class LOTRStructureBase {

    public boolean restrictions = true;
    public @Nullable Player usingPlayer;
    public boolean notifyChanges;
    private final List<BlockPos> placed = new ArrayList<>();

    protected LOTRStructureBase(boolean flag) {
        this.notifyChanges = flag;
    }

    public abstract boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k);

    /** generate, then the shape pass over what it set. */
    public boolean generateAndFinish(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        this.placed.clear();
        boolean generated = generate(world, random, i, j, k);
        for (BlockPos pos : this.placed) {
            BlockState state = world.getBlockState(pos);
            BlockState updated = Block.updateFromNeighbourShapes(state, world, pos);
            if (updated != state) {
                world.setBlock(pos, updated, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }
        this.placed.clear();
        return generated;
    }

    /** {@code world.getBiomeGenForCoords(i, k) instanceof <biome>}: the biomes come with D10, so none matches yet. */
    public boolean isBiome(WorldGenLevel world, int i, int k, String biome) {
        return false;
    }

    public void setBlockAndNotifyAdequately(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta) {
        setBlockState(world, i, j, k, block.state(meta));
    }

    public void setBlockState(WorldGenLevel world, int i, int j, int k, BlockState state) {
        BlockPos pos = new BlockPos(i, j, k);
        world.setBlock(pos, state, this.notifyChanges ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS);
        this.placed.add(pos);
    }

    public void setAir(WorldGenLevel world, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.AIR.defaultBlockState());
    }

    public void setGrassToDirt(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL)) {
            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    /** An entity of this kind for the structure's level. */
    public static <T extends net.minecraft.world.entity.Entity> T create(net.minecraft.world.entity.EntityType<T> type, WorldGenLevel world) {
        return LOTRStructureBase2.create(type, world);
    }

    public int usingPlayerRotation() {
        return this.usingPlayer == null ? 0 : LOTRStructures.getRotationFromPlayer(this.usingPlayer);
    }

    public void placeBarrel(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, ItemStack drink) {
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("barrel"), meta);
        if (world.getBlockEntity(new BlockPos(i, j, k)) instanceof LOTRBarrelBlockEntity barrel) {
            barrel.setBarrelMode(2);
            drink = drink.copy();
            if (drink.getItem() instanceof LOTRDrinkItem) {
                drink.set(LOTRDataComponents.DRINK_STRENGTH, Mth.randomBetweenInclusive(random, 1, 3));
            }
            drink.set(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
            drink.setCount(Mth.randomBetweenInclusive(random, LOTRBrewingRecipes.BARREL_CAPACITY / 2, LOTRBrewingRecipes.BARREL_CAPACITY));
            barrel.setItem(9, drink);
        }
    }

    public void placeBarrel(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, LOTRFoods foodList) {
        placeBarrel(world, random, i, j, k, meta, foodList.getRandomBrewableDrink(random));
    }

    public void placeMug(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, ItemStack drink, LOTRVessel[] vesselTypes) {
        LOTRVessel vessel = vesselTypes[random.nextInt(vesselTypes.length)];
        setBlockState(world, i, j, k, vessel.block().defaultBlockState().setValue(LOTRMugBlock.FACING, Direction.from2DDataValue(meta & 3)));
        if (random.nextInt(3) != 0 && world.getBlockEntity(new BlockPos(i, j, k)) instanceof LOTRMugBlockEntity mug) {
            drink = drink.copyWithCount(1);
            if (drink.getItem() instanceof LOTRDrinkItem drinkItem && drinkItem.isBrewable()) {
                drink.set(LOTRDataComponents.DRINK_STRENGTH, Mth.randomBetweenInclusive(random, 1, 3));
            }
            drink.set(LOTRDataComponents.VESSEL, vessel);
            mug.setVessel(vessel);
            mug.setMugItem(drink);
        }
    }

    public void placeMug(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, ItemStack drink, LOTRFoods foodList) {
        placeMug(world, random, i, j, k, meta, drink, foodList.getPlaceableDrinkVessels());
    }

    public void placeMug(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, LOTRFoods foodList) {
        placeMug(world, random, i, j, k, meta, foodList.getRandomPlaceableDrink(random), foodList);
    }

    public void placePlate(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock, LOTRFoods foodList) {
        placePlate_do(world, random, i, j, k, plateBlock, foodList, false);
    }

    public void placePlateWithCertainty(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock, LOTRFoods foodList) {
        placePlate_do(world, random, i, j, k, plateBlock, foodList, true);
    }

    public void placePlate_do(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock,
                              LOTRFoods foodList, boolean certain) {
        if (!certain && random.nextBoolean()) {
            return;
        }
        setBlockAndNotifyAdequately(world, i, j, k, plateBlock, 0);
        if ((certain || random.nextBoolean()) && world.getBlockEntity(new BlockPos(i, j, k)) instanceof LOTRPlateBlockEntity plate) {
            ItemStack food = foodList.getRandomFoodForPlate(random);
            if (random.nextInt(4) == 0) {
                food.grow(1 + random.nextInt(3));
            }
            plate.setFoodItem(food);
        }
    }

    public void placeSkull(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, random.nextInt(16)));
    }

    public void placeNPCRespawner(LOTRNPCRespawnerEntity entity, WorldGenLevel world, int i, int j, int k) {
        entity.snapTo(i + 0.5, j, k + 0.5, 0.0f, 0.0f);
        world.addFreshEntity(entity);
    }

    public void placeOrcTorch(WorldGenLevel world, int i, int j, int k) {
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("orcTorch"), 0);
        setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRLegacyBlocks.mod("orcTorch"), 1);
    }

    public void spawnItemFrame(WorldGenLevel world, int i, int j, int k, int direction, ItemStack itemstack) {
        ItemFrame frame = new ItemFrame(world.getLevel(), new BlockPos(i, j, k), Direction.from2DDataValue(direction));
        frame.setItem(itemstack, false);
        world.addFreshEntity(frame);
    }

    /** placeBanner and placeWallBanner: with the banner entities (D14); nothing yet. */
    public void placeBanner(WorldGenLevel world, int i, int j, int k, int direction, String bannerType) {
    }

    public void placeWallBanner(WorldGenLevel world, int i, int j, int k, int direction, String bannerType) {
    }
}
