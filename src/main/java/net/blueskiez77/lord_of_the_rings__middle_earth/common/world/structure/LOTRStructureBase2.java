package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBarrelBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRForgeBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRHobbitOvenBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRIthildinDwarvenDoorBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRKebabStandBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWeaponRackBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRAnimalJarBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRDwarvenDoorBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRKebabStandBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMugBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRWeaponRackBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldGenStructureBase2: what every structure is built with. A structure
 * lays its blocks out in its own coordinates -- x across, z forward, y up
 * from its origin -- and this carries them into the world by the rotation it
 * was given, keeping only what falls inside the bounding box if it has one.
 *
 * <p>Blocks are given as the original gave them, a block and its metadata
 * ({@link LOTRLegacyBlocks}); the metadata is the orientation at rotation 0,
 * and the blocks the original turned with the structure -- stairs, logs and
 * beams, doors, trapdoors, gates, torches, ladders, signs, chests, furnaces
 * and the mod's ovens, forges, barrels, kebab stands, mugs and weapon racks,
 * beds, levers, buttons, tripwire hooks, anvils, pumpkins, skulls -- are
 * turned with it here too. Blocks are set with the original's flags
 * ({@link #placeFlags}): a spawned structure tells its neighbours, a
 * generated one does not. When the whole structure is placed, each block it
 * set has its shape brought into line with its neighbours, so fences, walls
 * and panes join as the original's did when drawn; stairs stay straight, as
 * the original's could not turn corners, and nothing is broken for want of
 * support, since 1.7.10 never looked again.
 *
 * <p>NOT ported yet: the timelapse (a debug option); the biome's own top and
 * filler blocks and LOTR biomes' flowers and grasses (D10), so until then a
 * structure's flowers and grass are vanilla's, as the original gave outside
 * its biomes; the spawner chests;
 * the mod's flower pot for its own plants (the port's pot is vanilla's).
 */
public abstract class LOTRStructureBase2 {

    public boolean restrictions = true;
    public boolean notifyChanges;
    public @Nullable Player usingPlayer;
    public boolean shouldFindSurface;
    public @Nullable VillageSurface villageInstance;
    public int originX;
    public int originY;
    public int originZ;
    public int rotationMode;
    public @Nullable BoundingBox sbb;
    public @Nullable LOTRStructureScan currentStrScan;
    public final Map<String, BlockAliasPool> scanAliases = new HashMap<>();
    public final Map<String, Float> scanAliasChances = new HashMap<>();

    /** Every block this structure (and its substructures) set, for the shape pass. */
    private List<BlockPos> placed = new ArrayList<>();

    /** A village's own idea of what ground a building may stand on. */
    public interface VillageSurface {
        boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k);
    }

    protected LOTRStructureBase2(boolean flag) {
        this.notifyChanges = flag;
    }

    // -------------------------------------------------------------- generation

    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        return generateAndFinish(world, random, i, j, k, random.nextInt(4));
    }

    /** generateWithSetRotation, then the shape pass over what it set. */
    public boolean generateAndFinish(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        this.placed = new ArrayList<>();
        boolean generated = generateWithSetRotation(world, random, i, j, k, rotation);
        finishPlacement(world);
        return generated;
    }

    public abstract boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation);

    /**
     * The shape pass: each block set takes the shape its neighbours now give
     * it, as vanilla's structure templates do after placing theirs -- all but
     * stairs, which 1.7.10 never bent into corners: they are set back straight.
     */
    private void finishPlacement(WorldGenLevel world) {
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
    }

    /**
     * setBlock's flags: with notifyChanges, 3 -- the neighbours told, as the
     * original's spawned structures did; without, 2, which in 1.7.10 told no
     * one, so nothing nearby reshapes or breaks while the structure goes up.
     */
    public int placeFlags() {
        return this.notifyChanges ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
    }

    public void generateSubstructure(LOTRStructureBase2 str, WorldGenLevel world, RandomSource random, int i, int j, int k, int r) {
        generateSubstructureWithRestrictionFlag(str, world, random, i, j, k, r, this.restrictions);
    }

    public boolean generateSubstructureWithRestrictionFlag(LOTRStructureBase2 str, WorldGenLevel world, RandomSource random,
                                                          int i, int j, int k, int r, boolean isRestrict) {
        int i1 = i;
        int k1 = k;
        i = getX(i1, k1);
        k = getZ(i1, k1);
        j = getY(j);
        r += this.rotationMode;
        str.restrictions = isRestrict;
        str.usingPlayer = this.usingPlayer;
        str.villageInstance = this.villageInstance;
        str.sbb = this.sbb;
        str.placed = this.placed;
        return str.generateWithSetRotation(world, random, i, j, k, r % 4);
    }

    public void setupRandomBlocks(RandomSource random) {
    }

    public int usingPlayerRotation() {
        return this.usingPlayer == null ? 0 : LOTRStructures.getRotationFromPlayer(this.usingPlayer);
    }

    public int getRotationMode() {
        return this.rotationMode;
    }

    public void setStructureBB(@Nullable BoundingBox box) {
        this.sbb = box;
    }

    public boolean hasSBB() {
        return this.sbb != null;
    }

    public boolean isInSBB(int i, int j, int k) {
        return this.sbb == null || this.sbb.isInside(i, j, k);
    }

    // ------------------------------------------------------------ coordinates

    public int getX(int x, int z) {
        return switch (this.rotationMode) {
            case 0 -> this.originX - x;
            case 1 -> this.originX - z;
            case 2 -> this.originX + x;
            case 3 -> this.originX + z;
            default -> this.originX;
        };
    }

    public int getY(int y) {
        return this.originY + y;
    }

    public int getZ(int x, int z) {
        return switch (this.rotationMode) {
            case 0 -> this.originZ + z;
            case 1 -> this.originZ - x;
            case 2 -> this.originZ - z;
            case 3 -> this.originZ + x;
            default -> this.originZ;
        };
    }

    private @Nullable BlockPos worldPos(int i, int j, int k) {
        int x = getX(i, k);
        int y = getY(j);
        int z = getZ(i, k);
        return isInSBB(x, y, z) ? new BlockPos(x, y, z) : null;
    }

    public void setOriginAndRotation(WorldGenLevel world, int i, int j, int k, int rotation, int shift) {
        setOriginAndRotation(world, i, j, k, rotation, shift, 0);
    }

    public void setOriginAndRotation(WorldGenLevel world, int i, int j, int k, int rotation, int shift, int shiftX) {
        --j;
        this.rotationMode = rotation;
        switch (this.rotationMode) {
            case 0 -> {
                k += shift;
                i += shiftX;
            }
            case 1 -> {
                i -= shift;
                k += shiftX;
            }
            case 2 -> {
                k -= shift;
                i -= shiftX;
            }
            case 3 -> {
                i += shift;
                k -= shiftX;
            }
            default -> {
            }
        }
        this.originX = i;
        this.originY = j;
        this.originZ = k;
        if (this.shouldFindSurface) {
            this.shouldFindSurface = false;
            findSurface(world, -shiftX, -shift);
        }
    }

    public void findSurface(WorldGenLevel world, int i, int k) {
        int j = 8;
        while (getY(j) >= world.getMinY()) {
            if (isSurface(world, i, j, k)) {
                this.originY = getY(j);
                break;
            }
            --j;
        }
    }

    // ------------------------------------------------------------ reading

    public BlockState getBlockState(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = worldPos(i, j, k);
        return pos == null ? Blocks.AIR.defaultBlockState() : world.getBlockState(pos);
    }

    public Block getBlock(WorldGenLevel world, int i, int j, int k) {
        return getBlockState(world, i, j, k).getBlock();
    }

    public @Nullable BlockEntity getTileEntity(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = worldPos(i, j, k);
        return pos == null ? null : world.getBlockEntity(pos);
    }

    public boolean isAir(WorldGenLevel world, int i, int j, int k) {
        return getBlockState(world, i, j, k).isAir();
    }

    /** isOpaqueCube. */
    public boolean isOpaque(WorldGenLevel world, int i, int j, int k) {
        return getBlockState(world, i, j, k).isSolidRender();
    }

    public boolean isReplaceable(WorldGenLevel world, int i, int j, int k) {
        return getBlockState(world, i, j, k).canBeReplaced();
    }

    public boolean isSideSolid(WorldGenLevel world, int i, int j, int k, Direction side) {
        BlockPos pos = new BlockPos(getX(i, k), getY(j), getZ(i, k));
        return getBlockState(world, i, j, k).isFaceSturdy(world, pos, side);
    }

    /** getTopSolidOrLiquidBlock, as a height above the origin. */
    public int getTopBlock(WorldGenLevel world, int i, int k) {
        int x = getX(i, k);
        int z = getZ(i, k);
        if (!isInSBB(x, 0, z)) {
            return 0;
        }
        return world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - this.originY;
    }

    public boolean isSurface(WorldGenLevel world, int i, int j, int k) {
        int x = getX(i, k);
        int y = getY(j);
        int z = getZ(i, k);
        if (isSurfaceStatic(world, x, y, z)) {
            return true;
        }
        return this.villageInstance != null && this.villageInstance.isVillageSpecificSurface(world, x, y, z);
    }

    /**
     * isSurfaceStatic: ground a structure may stand on -- grass, dirt, gravel,
     * paths, mud, sand, Mordor's dirt and gravel, looking through a half slab
     * to what is under it, and never with liquid above. The biome's own top
     * and filler blocks count too in the original; that waits for the biomes.
     */
    public static boolean isSurfaceStatic(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE) {
            return isSurfaceStatic(world, i, j - 1, k);
        }
        if (!world.getFluidState(pos.above()).isEmpty()) {
            return false;
        }
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.GRAVEL)
                || state.is(Blocks.DIRT_PATH) || state.is(Blocks.SAND)
                || state.is(LOTRLegacyBlocks.mod("dirtPath").state(1).getBlock())
                || state.is(LOTRLegacyBlocks.mod("mudGrass").state().getBlock())
                || state.is(LOTRLegacyBlocks.mod("mud").state().getBlock())
                || state.is(LOTRLegacyBlocks.mod("whiteSand").state().getBlock())
                || state.is(LOTRLegacyBlocks.mod("mordorDirt").state().getBlock())
                || state.is(LOTRLegacyBlocks.mod("mordorGravel").state().getBlock());
    }

    // ------------------------------------------------------------ writing

    public void setBlockAndMetadata(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta) {
        setBlockState(world, i, j, k, block.state(meta));
        // A chest, furnace, oven or forge at 0 was left to face the way it chose for itself.
        BlockPos pos = meta == 0 ? worldPos(i, j, k) : null;
        if (pos != null && takesDefaultDirection(world.getBlockState(pos).getBlock())) {
            world.setBlock(pos, withDefaultDirection(world, pos, world.getBlockState(pos)),
                    placeFlags());
        }
    }

    /** The blocks whose onBlockAdded set their own facing in 1.7.10 (setDefaultDirection). */
    public static boolean takesDefaultDirection(Block block) {
        return block instanceof ChestBlock || block instanceof AbstractFurnaceBlock || block instanceof LOTRHobbitOvenBlock;
    }

    /**
     * setDefaultDirection: facing away from a solid block on one side with
     * none opposite -- north, then south, west, east checked in turn, the last
     * that applies winning -- and south if none does.
     */
    public static BlockState withDefaultDirection(WorldGenLevel world, BlockPos pos, BlockState state) {
        if (!state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state;
        }
        boolean north = world.getBlockState(pos.north()).isSolidRender();
        boolean south = world.getBlockState(pos.south()).isSolidRender();
        boolean west = world.getBlockState(pos.west()).isSolidRender();
        boolean east = world.getBlockState(pos.east()).isSolidRender();
        Direction facing = Direction.SOUTH;
        if (south && !north) {
            facing = Direction.NORTH;
        }
        if (west && !east) {
            facing = Direction.EAST;
        }
        if (east && !west) {
            facing = Direction.WEST;
        }
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    /** setBlockAndMetadata, with the state already worked out at rotation 0. */
    public void setBlockState(WorldGenLevel world, int i, int j, int k, BlockState state) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        int flags = placeFlags();
        state = joinHalves(world, pos, rotateState(state), flags, this.sbb);
        world.setBlock(pos, state, flags);
        this.placed.add(pos);
    }

    /**
     * 1.7.10 split a two-block block's state between its halves: a door's
     * facing and whether it stood open were the bottom half's, its hinge the
     * top's; a double plant's kind was the bottom half's. Each half alone
     * reads as the rest defaulted, so as the second half goes in the two are
     * joined -- else the door turns to the top half's default facing, and a
     * plant's halves disagree and both break.
     */
    public static BlockState joinHalves(WorldGenLevel world, BlockPos pos, BlockState state, int flags,
                                        @Nullable BoundingBox sbb) {
        if (state.getBlock() instanceof DoorBlock) {
            boolean upper = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
            BlockPos otherPos = upper ? pos.below() : pos.above();
            BlockState other = world.getBlockState(otherPos);
            if (!other.is(state.getBlock()) || other.getValue(DoorBlock.HALF) == state.getValue(DoorBlock.HALF)
                    || sbb != null && !sbb.isInside(otherPos)) {
                return state;
            }
            BlockState lower = upper ? other : state;
            BlockState top = upper ? state : other;
            lower = lower.setValue(DoorBlock.HINGE, top.getValue(DoorBlock.HINGE))
                    .setValue(DoorBlock.POWERED, top.getValue(DoorBlock.POWERED));
            top = top.setValue(DoorBlock.FACING, lower.getValue(DoorBlock.FACING))
                    .setValue(DoorBlock.OPEN, lower.getValue(DoorBlock.OPEN));
            world.setBlock(otherPos, upper ? lower : top, flags);
            return upper ? top : lower;
        }
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = world.getBlockState(pos.below());
            if (below.getBlock() instanceof DoublePlantBlock && !below.is(state.getBlock())
                    && below.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                return below.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
            }
        }
        return state;
    }

    public void setAir(WorldGenLevel world, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.AIR.defaultBlockState());
    }

    /** rotateMeta: the kinds of block the original turned with the structure. */
    private BlockState rotateState(BlockState state) {
        if (this.rotationMode == 0 || !isRotated(state.getBlock())) {
            return state;
        }
        return state.rotate(switch (this.rotationMode) {
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            default -> Rotation.COUNTERCLOCKWISE_90;
        });
    }

    private static boolean isRotated(Block block) {
        return block instanceof RotatedPillarBlock || block instanceof StairBlock || block instanceof LOTRMugBlock
                || block instanceof TripWireHookBlock || block instanceof AnvilBlock
                || block instanceof LOTRWeaponRackBlock || block instanceof WallSignBlock
                || block instanceof LadderBlock || block instanceof AbstractFurnaceBlock || block instanceof ChestBlock
                || block instanceof LOTRBarrelBlock || block instanceof LOTRHobbitOvenBlock
                || block instanceof LOTRForgeBlock || block instanceof LOTRKebabStandBlock
                || block instanceof StandingSignBlock || block instanceof BedBlock || block instanceof TorchBlock
                || block instanceof WallTorchBlock || block instanceof DoorBlock || block instanceof TrapDoorBlock
                || block instanceof FenceGateBlock || block instanceof CarvedPumpkinBlock
                || block instanceof SkullBlock || block instanceof WallSkullBlock || block instanceof LOTRGateBlock
                || block instanceof LeverBlock || block instanceof ButtonBlock;
    }

    /** setGrassToDirt: onPlantGrow -- grass or mycelium under something set on it turns to dirt. */
    public void setGrassToDirt(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL)) {
            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
        }
    }

    /** setBiomeTop and setBiomeFiller: the biome's own blocks wait for D10; until then grass and dirt. */
    public void setBiomeTop(WorldGenLevel world, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.GRASS_BLOCK.defaultBlockState());
    }

    public void setBiomeFiller(WorldGenLevel world, int i, int j, int k) {
        setBlockState(world, i, j, k, Blocks.DIRT.defaultBlockState());
    }

    // ------------------------------------------------------------ scans

    public void loadStrScan(String name) {
        this.currentStrScan = LOTRStructureScan.getScanByName(name);
        if (this.currentStrScan == null) {
            LOTRMod.LOGGER.error("LOTR: Structure Scan for name {} does not exist!!!", name);
        }
        this.scanAliases.clear();
    }

    public void addBlockAliasOption(String alias, int weight, LegacyBlock block) {
        addBlockMetaAliasOption(alias, weight, block, -1);
    }

    /** addBlockMetaAliasOption: as the original, each option counts once whatever weight it was given. */
    public void addBlockMetaAliasOption(String alias, int weight, LegacyBlock block, int meta) {
        this.scanAliases.computeIfAbsent(alias, a -> new BlockAliasPool()).addEntry(1, block, meta);
    }

    public void associateBlockAlias(String alias, LegacyBlock block) {
        addBlockAliasOption(alias, 1, block);
    }

    public void associateBlockMetaAlias(String alias, LegacyBlock block, int meta) {
        addBlockMetaAliasOption(alias, 1, block, meta);
    }

    public void clearScanAlias(String alias) {
        this.scanAliases.remove(alias);
        this.scanAliasChances.remove(alias);
    }

    public void setBlockAliasChance(String alias, float chance) {
        this.scanAliasChances.put(alias, chance);
    }

    /**
     * generateStrScan: the loaded scan laid out from here, solid blocks and
     * air first, then everything that hangs on them; steps may fill down to
     * the ground or drop to it, and grass under a solid block turns to dirt.
     */
    public void generateStrScan(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRStructureScan scan = this.currentStrScan;
        if (scan == null) {
            return;
        }
        for (int pass = 0; pass <= 1; ++pass) {
            for (LOTRStructureScan.ScanStepBase step : scan.scanSteps) {
                int i1 = i - step.x;
                int j1 = j + step.y;
                int k1 = k + step.z;
                LegacyBlock aliasBlock = null;
                int aliasMeta = -1;
                String alias = step.getAlias();
                if (alias != null) {
                    BlockAliasPool pool = this.scanAliases.get(alias);
                    if (pool == null) {
                        throw new IllegalArgumentException("No block associated to alias " + alias + " !");
                    }
                    BlockAliasPool.BlockMetaEntry e = pool.getEntry(random);
                    aliasBlock = e.block();
                    aliasMeta = e.meta();
                    Float chance = this.scanAliasChances.get(alias);
                    if (chance != null && random.nextFloat() >= chance) {
                        continue;
                    }
                }
                LegacyBlock block = step.getBlock(aliasBlock);
                if (block == null) {
                    continue;
                }
                int meta = step.getMeta(aliasMeta);
                BlockState state = block.state(meta);
                if (isOpaqueMaterialOrAir(state) != (pass == 0)) {
                    continue;
                }
                if (step.findLowest) {
                    while (getY(j1) > world.getMinY() && !LOTRLegacyWorld.blocksMotion(getBlockState(world, i1, j1 - 1, k1))) {
                        --j1;
                    }
                }
                if (step instanceof LOTRStructureScan.ScanStepSkull) {
                    placeSkull(world, random, i1, j1, k1);
                    continue;
                }
                setBlockAndMetadata(world, i1, j1, k1, block, meta);
                if ((step.findLowest || j1 <= 1) && state.isSolidRender()) {
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if (!step.fillDown) {
                    continue;
                }
                int j2 = j1 - 1;
                while (!isOpaque(world, i1, j2, k1) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j2, k1, block, meta);
                    if (state.isSolidRender()) {
                        setGrassToDirt(world, i1, j2 - 1, k1);
                    }
                    --j2;
                }
            }
        }
        this.currentStrScan = null;
        this.scanAliases.clear();
    }

    /**
     * {@code block.getMaterial().isOpaque() || block == Blocks.air}: the scan's
     * first pass. 1.7.10's see-through materials -- glass, ice, leaves, plants,
     * torches and other fittings, carpets, snow, liquids -- went in the second,
     * after what they stand on.
     */
    private static boolean isOpaqueMaterialOrAir(BlockState state) {
        if (state.isAir()) {
            return true;
        }
        return state.canOcclude() && !state.canBeReplaced() && state.getFluidState().isEmpty()
                && !(state.getBlock() instanceof CarpetBlock) && !(state.getBlock() instanceof SnowLayerBlock);
    }

    // ------------------------------------------------------------ helpers

    public void fillChest(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRChestContents.Pool contents, int amount) {
        BlockPos pos = worldPos(i, j, k);
        if (pos != null) {
            LOTRChestContents.fillChest(world, random, pos, contents, amount);
        }
    }

    public void placeChest(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, LOTRChestContents.Pool contents) {
        placeChest(world, random, i, j, k, meta, contents, -1);
    }

    public void placeChest(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, LOTRChestContents.Pool contents, int amount) {
        placeChest(world, random, i, j, k, LOTRLegacyBlocks.vanilla("chest"), meta, contents, amount);
    }

    public void placeChest(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock chest, int meta, LOTRChestContents.Pool contents) {
        placeChest(world, random, i, j, k, chest, meta, contents, -1);
    }

    public void placeChest(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock chest, int meta,
                           LOTRChestContents.Pool contents, int amount) {
        setBlockAndMetadata(world, i, j, k, chest, meta);
        fillChest(world, random, i, j, k, contents, amount);
    }

    public void putInventoryInChest(WorldGenLevel world, int i, int j, int k, Container inv) {
        if (getTileEntity(world, i, j, k) instanceof Container blockInv) {
            for (int l = 0; l < blockInv.getContainerSize() && l < inv.getContainerSize(); ++l) {
                blockInv.setItem(l, inv.getItem(l));
            }
        }
    }

    /** placeBarrel: a barrel of a finished brew, light to strong, half full to full. */
    public void placeBarrel(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, ItemStack drink) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("barrel"), meta);
        if (getTileEntity(world, i, j, k) instanceof LOTRBarrelBlockEntity barrel) {
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

    /** placeMug: a vessel from the list, set down, full two times in three. */
    public void placeMug(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, ItemStack drink, LOTRVessel[] vesselTypes) {
        LOTRVessel vessel = vesselTypes[random.nextInt(vesselTypes.length)];
        setBlockState(world, i, j, k, vessel.block().defaultBlockState().setValue(LOTRMugBlock.FACING, Direction.from2DDataValue(meta & 3)));
        if (random.nextInt(3) != 0 && getTileEntity(world, i, j, k) instanceof LOTRMugBlockEntity mug) {
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
        placePlate_list(world, random, i, j, k, plateBlock, foodList, false);
    }

    public void placePlateWithCertainty(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock, LOTRFoods foodList) {
        placePlate_list(world, random, i, j, k, plateBlock, foodList, true);
    }

    public void placePlate_list(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock,
                                LOTRFoods foodList, boolean certain) {
        ItemStack food = foodList.getRandomFoodForPlate(random);
        if (random.nextInt(4) == 0) {
            food.grow(1 + random.nextInt(3));
        }
        placePlate_item(world, random, i, j, k, plateBlock, food, certain);
    }

    public void placePlate_item(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock plateBlock,
                                ItemStack foodItem, boolean certain) {
        if (!certain && random.nextBoolean()) {
            return;
        }
        setBlockAndMetadata(world, i, j, k, plateBlock, 0);
        if ((certain || random.nextBoolean()) && getTileEntity(world, i, j, k) instanceof LOTRPlateBlockEntity plate) {
            plate.setFoodItem(foodItem);
        }
    }

    public void placeWeaponRack(WorldGenLevel world, int i, int j, int k, int meta, @Nullable ItemStack weapon) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("weaponRack"), meta);
        if (weapon != null && getTileEntity(world, i, j, k) instanceof LOTRWeaponRackBlockEntity rack) {
            rack.setWeapon(weapon.copy());
        }
    }

    public void placeKebabStand(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock block, int meta) {
        setBlockAndMetadata(world, i, j, k, block, meta);
        if (getTileEntity(world, i, j, k) instanceof LOTRKebabStandBlockEntity stand) {
            stand.generateCookedKebab(Mth.randomBetweenInclusive(random, 1, 8));
        }
    }

    /** placeAnimalJar: a jar, with a creature in it if one is given. */
    public void placeAnimalJar(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta, @Nullable Mob creature) {
        setBlockAndMetadata(world, i, j, k, block, meta);
        if (creature != null && getTileEntity(world, i, j, k) instanceof LOTRAnimalJarBlockEntity jar) {
            creature.setPos(getX(i, k) + 0.5, getY(j), getZ(i, k) + 0.5);
            creature.finalizeSpawn(world, world.getCurrentDifficultyAt(creature.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            net.minecraft.world.level.storage.TagValueOutput output = net.minecraft.world.level.storage.TagValueOutput
                    .createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, world.registryAccess());
            if (creature.saveAsPassenger(output)) {
                jar.setEntityData(output.buildResult());
            }
        }
    }

    /** placeArmorStand: vanilla's armour stand (the mod's was a vanilla duplicate), facing as the rack would, in the armour given. */
    public void placeArmorStand(WorldGenLevel world, int i, int j, int k, int direction, ItemStack @Nullable [] armor) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        for (int l = 0; l < this.rotationMode; ++l) {
            direction = (direction + 1) & 3;
        }
        ArmorStand stand = new ArmorStand(world.getLevel(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
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

    public void placeSign(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta, String[] text) {
        setBlockAndMetadata(world, i, j, k, block, meta);
        if (getTileEntity(world, i, j, k) instanceof SignBlockEntity sign) {
            net.minecraft.world.level.block.entity.SignText signText = sign.getFrontText();
            for (int l = 0; l < text.length && l < 4; ++l) {
                signText = signText.setMessage(l, Component.literal(text[l]));
            }
            sign.setText(signText, true);
        }
    }

    /** {@code sign.signText[line] = text}: one line of a sign's front. */
    public static void setSignLine(SignBlockEntity sign, int line, String text) {
        sign.setText(sign.getFrontText().setMessage(line, Component.literal(text)), true);
    }

    public void placeSkull(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        placeSkull(world, i, j, k, random.nextInt(16));
    }

    public void placeSkull(WorldGenLevel world, int i, int j, int k, int dir) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        dir += this.rotationMode * 4;
        world.setBlock(pos, Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, dir % 16),
                placeFlags());
        this.placed.add(pos);
    }

    /** placeSpawnerChest: a chest with a creature waiting in it (LOTRSpawnerChests), filled if given contents. */
    public void placeSpawnerChest(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock block, int meta,
                                  EntityType<?> type, LOTRChestContents.@Nullable Pool contents) {
        placeSpawnerChest(world, random, i, j, k, block, meta, type, contents, -1);
    }

    public void placeSpawnerChest(WorldGenLevel world, RandomSource random, int i, int j, int k, LegacyBlock block, int meta,
                                  EntityType<?> type, LOTRChestContents.@Nullable Pool contents, int amount) {
        setBlockAndMetadata(world, i, j, k, block, meta);
        BlockEntity be = getTileEntity(world, i, j, k);
        if (be != null) {
            LOTRSpawnerChests.setMob(be, type);
        }
        if (contents != null) {
            fillChest(world, random, i, j, k, contents, amount);
        }
    }

    public void placeSpawnerChest(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta, EntityType<?> type) {
        placeSpawnerChest(world, world.getRandom(), i, j, k, block, meta, type, null);
    }

    /** placeMobSpawner: a spawner of this creature (the mod's spawner was a vanilla duplicate). */
    public void placeMobSpawner(WorldGenLevel world, int i, int j, int k, EntityType<?> type) {
        setBlockState(world, i, j, k, Blocks.SPAWNER.defaultBlockState());
        if (getTileEntity(world, i, j, k) instanceof SpawnerBlockEntity spawner) {
            spawner.setEntityId(type, world.getRandom());
        }
    }

    public void placeFlowerPot(WorldGenLevel world, int i, int j, int k, @Nullable ItemStack itemstack) {
        BlockState pot = Blocks.FLOWER_POT.defaultBlockState();
        if (itemstack != null && !itemstack.isEmpty()) {
            Block potted = pottedFor(Block.byItem(itemstack.getItem()));
            if (potted != null) {
                pot = potted.defaultBlockState();
            }
        }
        setBlockState(world, i, j, k, pot);
    }

    public void placeRandomFlowerPot(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        placeFlowerPot(world, i, j, k, getRandomFlower(world, random));
    }

    public static @Nullable Block pottedFor(Block plant) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof FlowerPotBlock pot && pot.getPotted() == plant && plant != Blocks.AIR) {
                return block;
            }
        }
        return null;
    }

    /** getRandomFlower: outside the mod's biomes, a dandelion or a poppy. */
    public ItemStack getRandomFlower(WorldGenLevel world, RandomSource random) {
        return new ItemStack(random.nextBoolean() ? Blocks.DANDELION : Blocks.POPPY);
    }

    /** getRandomTallGrass: outside the mod's biomes, grass. */
    public ItemStack getRandomTallGrass(WorldGenLevel world, RandomSource random) {
        return new ItemStack(Blocks.SHORT_GRASS);
    }

    public void plantFlower(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        setBlockState(world, i, j, k, Block.byItem(getRandomFlower(world, random).getItem()).defaultBlockState());
    }

    public void plantTallGrass(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        setBlockState(world, i, j, k, Block.byItem(getRandomTallGrass(world, random).getItem()).defaultBlockState());
    }

    public void placeOrcTorch(WorldGenLevel world, int i, int j, int k) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("orcTorch"), 0);
        setBlockAndMetadata(world, i, j + 1, k, LOTRLegacyBlocks.mod("orcTorch"), 1);
    }

    /** placeIthildinDoor: the door's blocks, each told its place in the door and where the door's base is. */
    public void placeIthildinDoor(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta,
                                  LOTRIthildinDwarvenDoorBlock.DoorSize doorSize) {
        BlockPos base = new BlockPos(getX(i, k), getY(j), getZ(i, k));
        int xzFactorX = meta == 2 ? -1 : meta == 3 ? 1 : 0;
        int xzFactorZ = meta == 4 ? 1 : meta == 5 ? -1 : 0;
        for (int y = 0; y < doorSize.height(); ++y) {
            for (int xz = 0; xz < doorSize.width(); ++xz) {
                int i2 = i + xz * xzFactorX;
                int j2 = j + y;
                int k2 = k + xz * xzFactorZ;
                setBlockAndMetadata(world, i2, j2, k2, block, meta);
                if (getTileEntity(world, i2, j2, k2) instanceof LOTRDwarvenDoorBlockEntity door) {
                    door.setDoorSizeAndPos(doorSize, xz, y);
                    door.setDoorBasePos(base);
                }
            }
        }
    }

    /**
     * placeBanner: a standing banner of this people, turned with the
     * structure, perhaps protecting it -- as the structure's own, which no one
     * may take down or edit, over its own range if it has one.
     */
    public void placeBanner(WorldGenLevel world, int i, int j, int k, String bannerType, int direction) {
        placeBanner(world, i, j, k, bannerType, direction, false, 0);
    }

    public void placeBanner(WorldGenLevel world, int i, int j, int k, String bannerType, int direction,
                            boolean protection, int r) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        for (int l = 0; l < this.rotationMode; ++l) {
            direction = ROTATE_RIGHT[direction];
        }
        if (r > 64) {
            throw new IllegalArgumentException("WARNING: Banner protection range " + r + " is too large!");
        }
        placeBannerBlock(world, pos, bannerType, direction, protection, r);
    }

    /** placeWallBanner: a banner hung on the face of this block, facing the given way, turned with the structure. */
    public void placeWallBanner(WorldGenLevel world, int i, int j, int k, String bannerType, int direction) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        for (int l = 0; l < this.rotationMode; ++l) {
            direction = ROTATE_RIGHT[direction];
        }
        placeWallBannerBlock(world, pos, bannerType, direction);
    }

    /**
     * A standing banner here, its rotation the original entity's yaw of
     * {@code direction * 90}: a quarter turn is four of vanilla's sixteenths.
     */
    static void placeBannerBlock(WorldGenLevel world, BlockPos pos, String bannerType, int direction,
                                 boolean protection, int r) {
        LOTRBannerType type = LOTRBannerType.forLegacyName(bannerType);
        if (type == null) {
            LOTRMod.LOGGER.warn("LOTR: no banner {} for a structure", bannerType);
            return;
        }
        Block banner = LOTRBlocks.standingBanner(type);
        world.setBlock(pos, banner.defaultBlockState().setValue(BannerBlock.ROTATION, (direction & 3) * 4), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
        if (world.getBlockEntity(pos) instanceof LOTRBannerBlockEntity be) {
            if (protection) {
                be.setStructureProtection(true);
                be.setSelfProtection(false);
            }
            if (r > 0) {
                be.setCustomRange(r);
            }
        }
    }

    /**
     * A wall banner on the face of the wall block given, facing that way. The
     * original's hanging banner was two blocks tall, from the wall block's
     * level up; a wall banner block hangs from the top of its block to below
     * it, so it goes one block up.
     */
    static void placeWallBannerBlock(WorldGenLevel world, BlockPos wallPos, String bannerType, int direction) {
        LOTRBannerType type = LOTRBannerType.forLegacyName(bannerType);
        if (type == null) {
            LOTRMod.LOGGER.warn("LOTR: no banner {} for a structure", bannerType);
            return;
        }
        Direction facing = Direction.from2DDataValue(direction & 3);
        Block wall = LOTRBlocks.BANNER_WALL_FORM.get(LOTRBlocks.standingBanner(type));
        world.setBlock(wallPos.relative(facing).above(), wall.defaultBlockState().setValue(WallBannerBlock.FACING, facing), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
    }

    /**
     * {@code getBiome(world, i, k) instanceof LOTRBiomeGen<biome>}, in the
     * structure's own coordinates: the biomes come with D10, so none matches yet.
     */
    public boolean isBiome(WorldGenLevel world, int i, int k, String biome) {
        return false;
    }

    /** {@code block.getMaterial() == Material.plants}: flowers, grass, ferns, saplings, crops, bushes. */
    public static boolean isPlant(BlockState state) {
        return state.getBlock() instanceof net.minecraft.world.level.block.VegetationBlock;
    }

    /**
     * {@code LOTRBiome.<biome>.func_150567_a(random).generate(..)}: one of the
     * biome's own trees, grown here. The biomes' trees come with D10; until then
     * nothing grows.
     */
    public void placeBiomeTree(WorldGenLevel world, RandomSource random, String biome, int i, int j, int k) {
    }

    /**
     * {@code LOTRTreeType.<type>.create(..).generate(world, random, x, y, z)}:
     * whether the tree grew. The mod's trees come with D10; until then none do.
     */
    public static boolean placeTree(WorldGenLevel world, RandomSource random, String treeType, int x, int y, int z) {
        return false;
    }

    public void placeNPCRespawner(LOTRNPCRespawnerEntity entity, WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        entity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
        world.addFreshEntity(entity);
    }

    public void placeRug(LOTRRugEntity rug, WorldGenLevel world, int i, int j, int k, float rotation) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        float f = rotation + switch (this.rotationMode) {
            case 1 -> 270.0f;
            case 2 -> 180.0f;
            case 3 -> 90.0f;
            default -> 0.0f;
        };
        rug.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, f % 360.0f, 0.0f);
        world.addFreshEntity(rug);
    }

    public void spawnItemFrame(WorldGenLevel world, int i, int j, int k, int direction, ItemStack itemstack) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        for (int l = 0; l < this.rotationMode; ++l) {
            direction = (direction + 1) & 3;
        }
        // 1.7.10's hanging entities took the block they hang ON; 26.2's take the
        // space they occupy, in front of that block's face.
        Direction facing = Direction.from2DDataValue(direction);
        ItemFrame frame = new ItemFrame(world.getLevel(), pos.relative(facing), facing);
        frame.setItem(itemstack, false);
        world.addFreshEntity(frame);
    }

    public void leashEntityTo(PathfinderMob entity, WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        LeashFenceKnotEntity leash = LeashFenceKnotEntity.getOrCreateKnot(world.getLevel(), pos);
        entity.setLeashedTo(leash, true);
    }

    /** spawnNPCAndSetHome: one of the structure's people, kept for good (an NPC), homed here. */
    public void spawnNPCAndSetHome(PathfinderMob entity, WorldGenLevel world, int i, int j, int k, int homeDistance) {
        BlockPos pos = worldPos(i, j, k);
        if (pos == null) {
            return;
        }
        entity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
        entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
        if (entity instanceof LOTRNPCEntity npc) {
            npc.isNPCPersistent = true;
        }
        world.addFreshEntityWithPassengers(entity);
        entity.setHomeTo(pos, homeDistance);
    }

    /** 1.7.10's Direction tables: 0 south, 1 west, 2 north, 3 east; facings 0-5 down, up, north, south, west, east. */
    public static final int[] ROTATE_RIGHT = {1, 2, 3, 0};
    public static final int[] DIRECTION_TO_FACING = {3, 4, 2, 5};
    public static final int[] FACING_TO_DIRECTION = {-1, -1, 2, 0, 1, 3};

    /** Direction.getMovementDirection: the way (0-3) an offset mostly points. */
    public static int getMovementDirection(double x, double z) {
        if (Math.abs(x) > Math.abs(z)) {
            return x > 0.0 ? 1 : 3;
        }
        return z > 0.0 ? 2 : 0;
    }

    /** 1.7.10's equipment slot index: 0 held, 1 boots, 2 leggings, 3 chestplate, 4 helmet. */
    public static net.minecraft.world.entity.EquipmentSlot slotOf(int index) {
        return switch (index) {
            case 1 -> net.minecraft.world.entity.EquipmentSlot.FEET;
            case 2 -> net.minecraft.world.entity.EquipmentSlot.LEGS;
            case 3 -> net.minecraft.world.entity.EquipmentSlot.CHEST;
            case 4 -> net.minecraft.world.entity.EquipmentSlot.HEAD;
            default -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
        };
    }

    /** An entity of this kind for the structure's level, to be placed by one of the above. */
    public static <T extends Entity> T create(EntityType<T> type, WorldGenLevel world) {
        T entity = type.create(world.getLevel(), EntitySpawnReason.STRUCTURE);
        if (entity == null) {
            throw new IllegalStateException("Could not create " + type);
        }
        return entity;
    }

    /** BlockAliasPool: the blocks a scan's alias may stand for, drawn by weight. */
    public static final class BlockAliasPool {
        private final List<BlockMetaEntry> entries = new ArrayList<>();
        private int totalWeight;

        public void addEntry(int weight, LegacyBlock block, int meta) {
            this.entries.add(new BlockMetaEntry(weight, block, meta));
            this.totalWeight += weight;
        }

        public BlockMetaEntry getEntry(RandomSource random) {
            int roll = random.nextInt(this.totalWeight);
            for (BlockMetaEntry entry : this.entries) {
                roll -= entry.weight();
                if (roll < 0) {
                    return entry;
                }
            }
            return this.entries.getLast();
        }

        public record BlockMetaEntry(int weight, LegacyBlock block, int meta) {
        }
    }
}
