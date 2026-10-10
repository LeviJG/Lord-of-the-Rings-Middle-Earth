package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMugBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldGenStructureBase: the older way the original built a structure --
 * straight in world coordinates, with no rotation or bounding box of its own;
 * the structure turns itself where it needs to. A few structures still use
 * it. As with the newer base, blocks are set without neighbour updates and
 * given their shapes when the whole structure is placed.
 */
public abstract class LOTRStructureBase implements net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator {

    public boolean restrictions = true;
    public @Nullable Player usingPlayer;
    public boolean notifyChanges;
    private final List<BlockPos> placed = new ArrayList<>();
    /** The block each position was last given, so setBlockMetadata can change only its metadata. */
    private final java.util.Map<BlockPos, LegacyBlock> placedBlocks = new java.util.HashMap<>();

    protected LOTRStructureBase(boolean flag) {
        this.notifyChanges = flag;
    }

    public abstract boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k);

    /** setBlock's flags: 3 with notifyChanges, else 2, which in 1.7.10 told no neighbour (LOTRStructureBase2.placeFlags). */
    public int placeFlags() {
        return this.notifyChanges ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
    }

    /** generate, then the shape pass over what it set (stairs kept straight, as 1.7.10's were). */
    public synchronized boolean generateAndFinish(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        this.placed.clear();
        boolean generated = generate(world, random, i, j, k);
        for (BlockPos pos : this.placed) {
            BlockState state = world.getBlockState(pos);
            BlockState updated = state.getBlock() instanceof StairBlock
                    ? state.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT)
                    : Block.updateFromNeighbourShapes(state, world, pos);
            // 1.7.10 never checked a block again once it was set, so the pass
            // only joins and turns blocks; it does not break what cannot stay.
            if (updated != state && !(updated.isAir() && !state.isAir())) {
                world.setBlock(pos, updated, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }
        this.placed.clear();
        return generated;
    }

    /** {@code world.getBiomeGenForCoords(i, k) instanceof <biome>}, the biome by its original name. */
    public boolean isBiome(WorldGenLevel world, int i, int k, String biome) {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.isBiomeOfClass(
                world.getBiome(new BlockPos(i, world.getSeaLevel(), k)), biome);
    }

    /** placeSpawnerChest: a chest with a creature waiting in it (LOTRSpawnerChests). */
    public void placeSpawnerChest(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta, net.minecraft.world.entity.EntityType<?> type) {
        setBlockAndNotifyAdequately(world, i, j, k, block, meta);
        net.minecraft.world.level.block.entity.BlockEntity be = world.getBlockEntity(new BlockPos(i, j, k));
        if (be != null) {
            LOTRSpawnerChests.setMob(be, type);
        }
    }

    /** {@code LOTRTreeType.<type>.create(false, random).generate(world, random, x, y, z)}. */
    public static void placeTree(WorldGenLevel world, RandomSource random, String treeType, int x, int y, int z) {
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType.valueOf(treeType)
                .create(false, random).generate(world, random, x, y, z);
    }

    /**
     * WorldGenAbstractTree.isReplaceable: what a tree may grow through -- air,
     * leaves, logs, saplings, vines, grass and plants.
     */
    public boolean isReplaceable(WorldGenLevel world, int i, int j, int k) {
        BlockState state = world.getBlockState(new BlockPos(i, j, k));
        return state.isAir() || state.is(net.minecraft.tags.BlockTags.LEAVES) || state.is(net.minecraft.tags.BlockTags.LOGS)
                || state.getBlock() instanceof net.minecraft.world.level.block.SaplingBlock || state.is(Blocks.VINE) || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT) || LOTRStructureBase2.isPlant(state);
    }

    /** LOTRMod.isOpaque: whether the block here is an opaque cube, in world coordinates. */
    public static boolean isOpaqueAt(WorldGenLevel world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k)).isSolidRender();
    }

    /**
     * setBlockAndNotifyAdequately. A chest, furnace, oven or forge then faces
     * the way its onBlockAdded chose, whatever metadata it was given (this
     * base never set it again); a structure that wanted otherwise turned it
     * afterwards with setBlockMetadata.
     */
    public void setBlockAndNotifyAdequately(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta) {
        BlockState state = block.state(meta);
        if (LOTRStructureBase2.takesDefaultDirection(state.getBlock())) {
            state = LOTRStructureBase2.withDefaultDirection(world, new BlockPos(i, j, k), state);
        }
        setBlockState(world, i, j, k, state);
        this.placedBlocks.put(new BlockPos(i, j, k), block);
    }

    /** setBlockMetadataWithNotify: the block set here, with other metadata (a chest or furnace turned). */
    public void setBlockMetadata(WorldGenLevel world, int i, int j, int k, int meta) {
        LegacyBlock block = this.placedBlocks.get(new BlockPos(i, j, k));
        if (block != null) {
            setBlockState(world, i, j, k, block.state(meta));
        }
    }

    public void placeFlowerPot(WorldGenLevel world, int i, int j, int k, @Nullable ItemStack itemstack) {
        BlockState pot = Blocks.FLOWER_POT.defaultBlockState();
        if (itemstack != null && !itemstack.isEmpty()) {
            Block potted = LOTRStructureBase2.pottedFor(Block.byItem(itemstack.getItem()));
            if (potted != null) {
                pot = potted.defaultBlockState();
            }
        }
        setBlockState(world, i, j, k, pot);
    }

    /** placeArmorStand: vanilla's armour stand (the mod's was a vanilla duplicate), in the armour given. */
    public void placeArmorStand(WorldGenLevel world, int i, int j, int k, int direction, ItemStack @Nullable [] armor) {
        net.minecraft.world.entity.decoration.ArmorStand stand = new net.minecraft.world.entity.decoration.ArmorStand(
                world.getLevel(), i + 0.5, j, k + 0.5);
        // LOTRRenderArmorStand drew metadata 0 facing north, back at whoever set it down: yaw 180 + 90 * direction.
        stand.setYRot(Direction.from2DDataValue(direction).getOpposite().toYRot());
        if (armor != null) {
            net.minecraft.world.entity.EquipmentSlot[] slots = {net.minecraft.world.entity.EquipmentSlot.HEAD,
                    net.minecraft.world.entity.EquipmentSlot.CHEST, net.minecraft.world.entity.EquipmentSlot.LEGS,
                    net.minecraft.world.entity.EquipmentSlot.FEET};
            for (int l = 0; l < armor.length && l < slots.length; ++l) {
                if (armor[l] != null) {
                    stand.setItemSlot(slots[l], armor[l].copy());
                }
            }
        }
        world.addFreshEntity(stand);
    }

    /** WorldGenAbstractTree.func_150515_a: a block, metadata 0. */
    public void func_150515_a(WorldGenLevel world, int i, int j, int k, LegacyBlock block) {
        setBlockAndNotifyAdequately(world, i, j, k, block, 0);
    }

    /** WorldGenAbstractTree.func_150516_a: a block and its metadata. */
    public void func_150516_a(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta) {
        setBlockAndNotifyAdequately(world, i, j, k, block, meta);
    }

    public void setBlockState(WorldGenLevel world, int i, int j, int k, BlockState state) {
        BlockPos pos = new BlockPos(i, j, k);
        int flags = placeFlags();
        world.setBlock(pos, LOTRStructureBase2.joinHalves(world, pos, state, flags, null), flags);
        this.placed.add(pos);
    }

    public void setAir(WorldGenLevel world, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.AIR.defaultBlockState());
    }

    public void setGrassToDirt(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL)) {
            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
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
        // 1.7.10's hanging entities took the block they hang ON; 26.2's take the
        // space they occupy, in front of that block's face.
        Direction facing = Direction.from2DDataValue(direction);
        ItemFrame frame = new ItemFrame(world.getLevel(), new BlockPos(i, j, k).relative(facing), facing);
        frame.setItem(itemstack, false);
        world.addFreshEntity(frame);
    }

    /** placeBanner: a standing banner here, facing the given way, protecting nothing. */
    public void placeBanner(WorldGenLevel world, int i, int j, int k, int direction, String bannerType) {
        LOTRStructureBase2.placeBannerBlock(world, new BlockPos(i, j, k), bannerType, direction, false, 0);
    }

    /** placeWallBanner: a banner hung on the face of this block, facing the given way. */
    public void placeWallBanner(WorldGenLevel world, int i, int j, int k, int direction, String bannerType) {
        LOTRStructureBase2.placeWallBannerBlock(world, new BlockPos(i, j, k), bannerType, direction);
    }
}
