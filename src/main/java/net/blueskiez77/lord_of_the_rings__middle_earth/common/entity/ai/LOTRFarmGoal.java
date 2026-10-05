package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGrapevineBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRFarmhand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRGrapeSeedsItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIFarm: a farmhand's work. Now and then it looks about for
 * something to do, in this order: take its crops to a chest, hoe grass or
 * dirt near water, plant on wet farmland, harvest ripe crops, bone-meal them,
 * and fetch seeds and bone meal from a chest. Chests it will use are those
 * with a hoe in an item frame on them (or on the other half).
 *
 * <p>An unhired farmhand only hoes and plants, its own seeds for ever. A
 * hired one does it all while guarding -- following its player, it does
 * nothing -- out of its four slots: seeds, two for the harvest, bone meal;
 * it plants the seeds it holds and keeps one seed back.
 *
 * <p>The crops it knows: those planted by a seed or stem seed (Forge's
 * EnumPlantType.Crop), and grapes on a post.
 */
public class LOTRFarmGoal extends Goal {

    private static final int DEPOSIT_THRESHOLD = 16;
    private static final int COLLECT_THRESHOLD = 16;
    private static final int MIN_CHEST_RANGE = 24;

    private enum Action {
        HOEING, PLANTING, HARVESTING, DEPOSITING, BONEMEALING, COLLECTING
    }

    private record TargetPair(BlockPos actionTarget, BlockPos pathTarget) {
    }

    private final LOTRNPCEntity theEntity;
    private final LOTRFarmhand theEntityFarmer;
    private final double moveSpeed;
    private final float farmingEfficiency;
    private @Nullable Action action;
    private @Nullable BlockPos actionTarget;
    private @Nullable BlockPos pathTarget;
    private int pathingTick;
    private int rePathDelay;
    private boolean harvestingSolidBlock;

    public <T extends LOTRNPCEntity & LOTRFarmhand> LOTRFarmGoal(T npc, double speed, float efficiency) {
        this.theEntity = npc;
        this.theEntityFarmer = npc;
        this.moveSpeed = speed;
        this.farmingEfficiency = efficiency;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean hired() {
        return this.theEntity.hiredNPCInfo.isActive;
    }

    private @Nullable LOTRInventoryNPC inventory() {
        return this.theEntity.hiredNPCInfo.getHiredInventory();
    }

    private ServerLevel level() {
        return (ServerLevel) this.theEntity.level();
    }

    // --- What it can do ------------------------------------------------------

    private boolean canDoBonemealing() {
        return hired() && getInventoryBonemeal() != null;
    }

    private boolean canDoCollecting() {
        if (!hired()) {
            return false;
        }
        ItemStack seeds = getInventorySeeds();
        if (seeds != null && seeds.getCount() <= COLLECT_THRESHOLD) {
            return true;
        }
        ItemStack bonemeal = getInventoryBonemeal();
        return bonemeal == null || bonemeal.getCount() <= COLLECT_THRESHOLD;
    }

    private boolean canDoDepositing() {
        if (hired() && inventory() != null) {
            for (int l = 1; l <= 2; l++) {
                if (inventory().getItem(l).getCount() >= DEPOSIT_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDoHarvesting() {
        return hired() && getInventorySeeds() != null && hasSpaceForCrops() && getCropForSeed(getSeedsToPlant()) != null;
    }

    private boolean canDoHoeing() {
        return true;
    }

    private boolean canDoPlanting() {
        if (hired()) {
            ItemStack seeds = getInventorySeeds();
            return seeds != null && seeds.getCount() > 1;
        }
        return true;
    }

    // --- The seeds and their crop --------------------------------------------

    /** The plant a seed item puts down: a crop, a stem, or grapes -- Forge's Crop plant type. */
    public static @Nullable Block getPlant(Item seed) {
        if (seed instanceof LOTRGrapeSeedsItem grapes) {
            return grapes.getBlock();
        }
        if (seed instanceof BlockItem blockItem
                && (blockItem.getBlock() instanceof CropBlock || blockItem.getBlock() instanceof StemBlock)) {
            return blockItem.getBlock();
        }
        return null;
    }

    private @Nullable ItemStack getInventoryBonemeal() {
        if (inventory() == null) {
            return null;
        }
        ItemStack stack = inventory().getItem(3);
        return stack.is(Items.BONE_MEAL) ? stack : null;
    }

    private @Nullable ItemStack getInventorySeeds() {
        if (inventory() == null) {
            return null;
        }
        ItemStack stack = inventory().getItem(0);
        return !stack.isEmpty() && getPlant(stack.getItem()) != null ? stack : null;
    }

    private Item getSeedsToPlant() {
        if (hired()) {
            ItemStack seeds = getInventorySeeds();
            if (seeds != null) {
                return seeds.getItem();
            }
        }
        return this.theEntityFarmer.getUnhiredSeeds();
    }

    /**
     * getCropForSeed: what the plant is grown for. The original read it off
     * the crop (or the stem's fruit); here it is the ripe plant's drops that
     * are not the seed itself, or failing that the seed.
     */
    private @Nullable ItemStack getCropForSeed(Item seed) {
        Block plant = getPlant(seed);
        if (plant instanceof CropBlock crop) {
            return firstDropOtherThan(crop.getStateForAge(crop.getMaxAge()), seed);
        }
        if (plant instanceof StemBlock stem) {
            Optional<Block> fruit = BuiltInRegistries.BLOCK.getOptional(stem.fruit);
            return fruit.map(b -> firstDropOtherThan(b.defaultBlockState(), Items.AIR)).orElse(null);
        }
        if (plant instanceof LOTRGrapevineBlock) {
            return firstDropOtherThan(plant.defaultBlockState().setValue(LOTRGrapevineBlock.AGE, LOTRGrapevineBlock.MAX_AGE), seed);
        }
        return null;
    }

    private @Nullable ItemStack firstDropOtherThan(BlockState state, Item seed) {
        ItemStack fallback = null;
        for (ItemStack drop : Block.getDrops(state, level(), this.theEntity.blockPosition(), null)) {
            if (!drop.is(seed)) {
                return new ItemStack(drop.getItem());
            }
            fallback = new ItemStack(drop.getItem());
        }
        return fallback;
    }

    private boolean isFarmingGrapes() {
        return getPlant(getSeedsToPlant()) instanceof LOTRGrapevineBlock;
    }

    private boolean hasSpaceForCrops() {
        if (inventory() == null) {
            return false;
        }
        ItemStack crop = getCropForSeed(getSeedsToPlant());
        for (int l = 1; l <= 2; l++) {
            ItemStack stack = inventory().getItem(l);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize() && crop != null && stack.is(crop.getItem())) {
                return true;
            }
        }
        return false;
    }

    // --- Where ---------------------------------------------------------------

    private boolean isReplaceable(BlockPos pos) {
        BlockState state = level().getBlockState(pos);
        return state.getFluidState().isEmpty() && state.canBeReplaced();
    }

    /** isSolidOpenWalkTarget: a spot with a solid (or farmland) floor, two clear blocks, and a way there. */
    private boolean isSolidOpenWalkTarget(BlockPos pos) {
        BlockState below = level().getBlockState(pos.below());
        if (below.isSolidRender() || below.getBlock() instanceof FarmlandBlock) {
            for (int j1 = 0; j1 <= 1; j1++) {
                BlockPos p = pos.above(j1);
                if (!level().getBlockState(p).getCollisionShape(level(), p).isEmpty()) {
                    return false;
                }
            }
            return this.theEntity.getNavigation().createPath(pos, 0) != null;
        }
        return false;
    }

    private BlockPos getAdjacentSolidOpenWalkTarget(BlockPos pos) {
        for (int i1 = -1; i1 <= 1; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                for (int j1 = 1; j1 >= -1; j1--) {
                    BlockPos p = pos.offset(i1, j1, k1);
                    if (isSolidOpenWalkTarget(p)) {
                        return p;
                    }
                }
            }
        }
        return pos;
    }

    private BlockPos getPathTarget(BlockPos pos, Action targetAction) {
        if (targetAction == Action.HOEING) {
            return isReplaceable(pos.above()) ? pos.above() : getAdjacentSolidOpenWalkTarget(pos.above());
        }
        if (targetAction == Action.PLANTING || targetAction == Action.HARVESTING || targetAction == Action.BONEMEALING) {
            if (this.harvestingSolidBlock) {
                return pos.above();
            }
            if (isFarmingGrapes()) {
                int groundY = pos.getY();
                for (int j1 = 1; j1 <= 2; j1++) {
                    if (!(level().getBlockState(pos.below(j1 + 1)).getBlock() instanceof LOTRGrapevineBlock)) {
                        groundY = pos.getY() - j1 - 1;
                        break;
                    }
                }
                return getAdjacentSolidOpenWalkTarget(new BlockPos(pos.getX(), groundY + 1, pos.getZ()));
            }
            return pos;
        }
        if (targetAction == Action.DEPOSITING || targetAction == Action.COLLECTING) {
            return getAdjacentSolidOpenWalkTarget(pos);
        }
        return pos;
    }

    /** isFarmhandMarked: an item frame holding a hoe hangs on this chest. */
    private boolean isFarmhandMarked(BlockPos chestPos) {
        for (ItemFrame frame : level().getEntitiesOfClass(ItemFrame.class, new AABB(chestPos).inflate(2.0))) {
            BlockPos support = frame.getPos().relative(frame.getDirection().getOpposite());
            if (support.equals(chestPos) && frame.getItem().getItem() instanceof HoeItem) {
                return true;
            }
        }
        return false;
    }

    /** getSuitableChest: a chest marked for the farmhand, itself or through its other half. */
    private @Nullable ChestBlockEntity getSuitableChest(BlockPos pos) {
        if (!(level().getBlockEntity(pos) instanceof ChestBlockEntity chest)) {
            return null;
        }
        if (isFarmhandMarked(pos)) {
            return chest;
        }
        BlockState state = level().getBlockState(pos);
        if (state.getBlock() instanceof ChestBlock && ChestBlock.getBlockType(state) != net.minecraft.world.level.block.DoubleBlockCombiner.BlockType.SINGLE) {
            BlockPos other = pos.relative(ChestBlock.getConnectedDirection(state));
            if (isFarmhandMarked(other)) {
                return chest;
            }
        }
        return null;
    }

    private List<BlockPos> gatherNearbyChests() {
        int x = Mth.floor(this.theEntity.getX());
        int z = Mth.floor(this.theEntity.getZ());
        int searchRange = this.theEntity.getHomeRadius();
        int chunkRange = (searchRange >> 4) + 1;
        List<BlockPos> nearbyChests = new ArrayList<>();
        for (int i = -chunkRange; i <= chunkRange; i++) {
            for (int k = -chunkRange; k <= chunkRange; k++) {
                LevelChunk chunk = level().getChunkSource().getChunkNow((x >> 4) + i, (z >> 4) + k);
                if (chunk == null) {
                    continue;
                }
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (be instanceof ChestBlockEntity && !be.isRemoved() && this.theEntity.isWithinHome(be.getBlockPos())) {
                        nearbyChests.add(be.getBlockPos());
                    }
                }
            }
        }
        return nearbyChests;
    }

    // --- Suitability ---------------------------------------------------------

    private boolean isSuitableForBonemealing(BlockPos pos) {
        this.harvestingSolidBlock = false;
        Block plant = getPlant(getSeedsToPlant());
        BlockState state = level().getBlockState(pos);
        if (plant instanceof BonemealableBlock growable && state.is(plant) && growable.isValidBonemealTarget(level(), pos, state)) {
            this.harvestingSolidBlock = state.isSolidRender();
            return true;
        }
        return false;
    }

    /**
     * What a collecting slot takes: the seeds it holds, or -- the bone-meal
     * slot even when empty -- bone meal; nothing for an empty seed slot.
     */
    private @Nullable ItemStack collectMatch(int slot) {
        ItemStack stack = inventory().getItem(slot);
        if (stack.isEmpty()) {
            return slot == 3 ? new ItemStack(Items.BONE_MEAL) : null;
        }
        return stack;
    }

    private boolean isSuitableForCollecting(BlockPos pos) {
        this.harvestingSolidBlock = false;
        ChestBlockEntity chest = getSuitableChest(pos);
        if (chest == null) {
            return false;
        }
        for (int l : new int[]{0, 3}) {
            ItemStack match = collectMatch(l);
            if (match == null || inventory().getItem(l).getCount() > COLLECT_THRESHOLD) {
                continue;
            }
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack chestItem = chest.getItem(slot);
                if (!chestItem.isEmpty() && ItemStack.isSameItemSameComponents(chestItem, match)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSuitableForDepositing(BlockPos pos) {
        this.harvestingSolidBlock = false;
        ChestBlockEntity chest = getSuitableChest(pos);
        if (chest == null) {
            return false;
        }
        for (int l = 1; l <= 2; l++) {
            ItemStack depositItem = inventory().getItem(l);
            if (depositItem.isEmpty()) {
                continue;
            }
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack chestItem = chest.getItem(slot);
                if (chestItem.isEmpty() || ItemStack.isSameItemSameComponents(chestItem, depositItem)
                        && chestItem.getCount() < chestItem.getMaxStackSize()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSuitableForHarvesting(BlockPos pos) {
        this.harvestingSolidBlock = false;
        Block plant = getPlant(getSeedsToPlant());
        BlockState state = level().getBlockState(pos);
        if (plant instanceof CropBlock crop) {
            return state.is(crop) && crop.isMaxAge(state);
        }
        if (plant instanceof StemBlock stem) {
            this.harvestingSolidBlock = true;
            return BuiltInRegistries.BLOCK.getOptional(stem.fruit).map(state::is).orElse(false);
        }
        if (plant instanceof LOTRGrapevineBlock) {
            return state.is(plant) && state.getValue(LOTRGrapevineBlock.AGE) >= LOTRGrapevineBlock.MAX_AGE;
        }
        return false;
    }

    /**
     * isSuitableForHoeing: grass or dirt -- not already farmland, not on sand
     * -- under open air or a grapevine post, with water within four blocks
     * level with it.
     */
    private boolean isSuitableForHoeing(BlockPos pos) {
        this.harvestingSolidBlock = false;
        BlockState state = level().getBlockState(pos);
        boolean isGrassDirt = state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT);
        if (!isGrassDirt || !(isReplaceable(pos.above()) || level().getBlockState(pos.above()).is(LOTRDecorationBlocks.GRAPEVINE))) {
            return false;
        }
        if (level().getBlockState(pos.below()).is(Blocks.SAND)) {
            return false;
        }
        int range = 4;
        for (int i1 = -range; i1 <= range; i1++) {
            for (int k1 = -range; k1 <= range; k1++) {
                FluidState fluid = level().getFluidState(pos.offset(i1, 0, k1));
                if (fluid.is(Fluids.WATER) || fluid.is(Fluids.FLOWING_WATER)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** isSuitableForPlanting: open space on wet farmland -- or, for grapes, a post they can climb. */
    private boolean isSuitableForPlanting(BlockPos pos) {
        this.harvestingSolidBlock = false;
        if (isFarmingGrapes()) {
            return level().getBlockState(pos).is(LOTRDecorationBlocks.GRAPEVINE) && LOTRGrapevineBlock.canPlantGrapesAt(level(), pos);
        }
        BlockState below = level().getBlockState(pos.below());
        return below.getBlock() instanceof FarmlandBlock && below.getValue(FarmlandBlock.MOISTURE) > 0 && isReplaceable(pos);
    }

    private boolean isSuitable(Action targetAction, BlockPos pos) {
        return switch (targetAction) {
            case HOEING -> isSuitableForHoeing(pos);
            case PLANTING -> isSuitableForPlanting(pos);
            case HARVESTING -> isSuitableForHarvesting(pos);
            case DEPOSITING -> isSuitableForDepositing(pos);
            case BONEMEALING -> isSuitableForBonemealing(pos);
            case COLLECTING -> isSuitableForCollecting(pos);
        };
    }

    private boolean canDo(Action targetAction) {
        return switch (targetAction) {
            case HOEING -> canDoHoeing();
            case PLANTING -> canDoPlanting();
            case HARVESTING -> canDoHarvesting();
            case DEPOSITING -> canDoDepositing();
            case BONEMEALING -> canDoBonemealing();
            case COLLECTING -> canDoCollecting();
        };
    }

    // --- Choosing ------------------------------------------------------------

    /** setAppropriateHomeRange: a hired farmhand keeps to its guard range -- at least 24 to reach a chest. */
    private void setAppropriateHomeRange(@Nullable Action targetAction) {
        if (hired()) {
            int range = this.theEntity.hiredNPCInfo.getGuardRange();
            if ((targetAction == Action.DEPOSITING || targetAction == Action.COLLECTING) && range < MIN_CHEST_RANGE) {
                range = MIN_CHEST_RANGE;
            }
            this.theEntity.setHomeTo(this.theEntity.getHomePosition(), range);
        }
    }

    /** findTarget: 32 tries at a spot within 8 (and 4 up or down), or at a chest, with a way there. */
    private @Nullable TargetPair findTarget(Action targetAction) {
        setAppropriateHomeRange(targetAction);
        RandomSource rand = this.theEntity.getRandom();
        boolean isChestAction = targetAction == Action.DEPOSITING || targetAction == Action.COLLECTING;
        List<BlockPos> chests = isChestAction ? gatherNearbyChests() : List.of();
        for (int l = 0; l < 32; l++) {
            BlockPos pos;
            if (isChestAction) {
                if (chests.isEmpty()) {
                    continue;
                }
                pos = chests.get(rand.nextInt(chests.size()));
            } else {
                pos = new BlockPos(Mth.floor(this.theEntity.getX()) + Mth.nextInt(rand, -8, 8),
                        Mth.floor(this.theEntity.getBoundingBox().minY) + Mth.nextInt(rand, -4, 4),
                        Mth.floor(this.theEntity.getZ()) + Mth.nextInt(rand, -8, 8));
            }
            if (isSuitable(targetAction, pos) && this.theEntity.isWithinHome(pos)) {
                BlockPos path = getPathTarget(pos, targetAction);
                if (this.theEntity.getNavigation().createPath(path, 0) != null) {
                    return new TargetPair(pos, path);
                }
            }
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (hired() && !this.theEntity.hiredNPCInfo.isGuardMode()) {
            return false;
        }
        setAppropriateHomeRange(null);
        if (this.theEntity.hasHome() && !this.theEntity.isWithinHome()) {
            return false;
        }
        if (this.theEntity.getRandom().nextFloat() < this.farmingEfficiency * 0.1f) {
            for (Action candidate : new Action[]{Action.DEPOSITING, Action.HOEING, Action.PLANTING, Action.HARVESTING,
                    Action.BONEMEALING, Action.COLLECTING}) {
                if (canDo(candidate)) {
                    TargetPair target = findTarget(candidate);
                    if (target != null) {
                        this.actionTarget = target.actionTarget();
                        this.pathTarget = target.pathTarget();
                        this.action = candidate;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (hired() && !this.theEntity.hiredNPCInfo.isGuardMode() || this.theEntity.getNavigation().isDone()) {
            return false;
        }
        return this.pathingTick < 200 && canDo(this.action) && isSuitable(this.action, this.actionTarget);
    }

    @Override
    public void start() {
        setAppropriateHomeRange(this.action);
    }

    @Override
    public void stop() {
        this.action = null;
        setAppropriateHomeRange(null);
        this.actionTarget = null;
        this.pathTarget = null;
        this.pathingTick = 0;
        this.rePathDelay = 0;
        this.harvestingSolidBlock = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    // --- Doing ---------------------------------------------------------------

    @Override
    public void tick() {
        boolean canDoAction;
        if (this.action == Action.HOEING || this.action == Action.PLANTING) {
            canDoAction = BlockPos.containing(this.theEntity.getX(), this.theEntity.getBoundingBox().minY, this.theEntity.getZ())
                    .equals(this.pathTarget);
        } else {
            canDoAction = this.theEntity.distanceToSqr(this.pathTarget.getX() + 0.5, this.pathTarget.getY(),
                    this.pathTarget.getZ() + 0.5) < 9.0;
        }
        if (!canDoAction) {
            this.theEntity.getLookControl().setLookAt(this.actionTarget.getX() + 0.5, this.actionTarget.getY() + 0.5,
                    this.actionTarget.getZ() + 0.5, 10.0f, this.theEntity.getMaxHeadXRot());
            if (--this.rePathDelay <= 0) {
                this.rePathDelay = 10;
                this.theEntity.getNavigation().moveTo(this.pathTarget.getX() + 0.5, this.pathTarget.getY(),
                        this.pathTarget.getZ() + 0.5, this.moveSpeed);
            }
            ++this.pathingTick;
            return;
        }
        switch (this.action) {
            case HOEING -> hoe();
            case PLANTING -> plant();
            case HARVESTING -> harvest();
            case DEPOSITING -> deposit();
            case BONEMEALING -> bonemeal();
            case COLLECTING -> collect();
        }
    }

    /** Tills the target and the four around it that are fit to be tilled, clearing grass above them. */
    private void hoe() {
        if (!isSuitableForHoeing(this.actionTarget)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        for (int i1 = -1; i1 <= 1; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                if (Math.abs(i1) + Math.abs(k1) > 1) {
                    continue;
                }
                BlockPos pos = this.actionTarget.offset(i1, 0, k1);
                boolean alreadyChecked = i1 == 0 && k1 == 0;
                if (alreadyChecked || isSuitableForHoeing(pos)) {
                    if (isReplaceable(pos.above())) {
                        level().removeBlock(pos.above(), false);
                    }
                    // ItemHoe.onItemUse on the top face, as an iron hoe in a stand-in's hand.
                    if (level().getBlockState(pos.above()).isAir()) {
                        SoundType sound = Blocks.FARMLAND.defaultBlockState().getSoundType();
                        level().playSound(null, pos, sound.getStepSound(), SoundSource.BLOCKS,
                                (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
                        level().setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
                    }
                }
            }
        }
    }

    private void plant() {
        if (!isSuitableForPlanting(this.actionTarget)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        Block plant = getPlant(getSeedsToPlant());
        level().setBlock(this.actionTarget, plant.defaultBlockState(), Block.UPDATE_ALL);
        if (hired()) {
            inventory().removeItem(0, 1);
        }
    }

    private void harvest() {
        if (!isSuitableForHarvesting(this.actionTarget)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        BlockState state = level().getBlockState(this.actionTarget);
        List<ItemStack> drops = new ArrayList<>(Block.getDrops(state, level(), this.actionTarget, null));
        if (state.getBlock() instanceof LOTRGrapevineBlock) {
            // removedByPlayer: the grapes come off and the post stays.
            level().setBlock(this.actionTarget, LOTRDecorationBlocks.GRAPEVINE.defaultBlockState(), Block.UPDATE_ALL);
        } else {
            level().removeBlock(this.actionTarget, false);
        }
        SoundType sound = state.getSoundType();
        level().playSound(null, this.actionTarget, sound.getBreakSound(), SoundSource.BLOCKS,
                (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
        ItemStack seedItem = inventory().getItem(0);
        ItemStack cropItem = getCropForSeed(getSeedsToPlant());
        boolean addedOneCropSeed = false;
        for (ItemStack drop : drops) {
            if (cropItem != null && drop.is(cropItem.getItem())) {
                // A crop that is its own seed: one goes back with the seeds, the rest to the harvest.
                if (drop.is(seedItem.getItem()) && !addedOneCropSeed) {
                    addedOneCropSeed = true;
                    if (seedItem.getCount() + drop.getCount() <= seedItem.getMaxStackSize()) {
                        seedItem.grow(1);
                        inventory().setItem(0, seedItem);
                        continue;
                    }
                }
                for (int l = 1; l <= 2; l++) {
                    ItemStack stack = inventory().getItem(l);
                    if (stack.isEmpty()) {
                        inventory().setItem(l, drop);
                        break;
                    }
                    if (stack.getCount() + drop.getCount() <= stack.getMaxStackSize() && stack.is(cropItem.getItem())) {
                        stack.grow(1);
                        inventory().setItem(l, stack);
                        break;
                    }
                }
                continue;
            }
            if (drop.is(seedItem.getItem()) && seedItem.getCount() + drop.getCount() <= seedItem.getMaxStackSize()) {
                seedItem.grow(1);
                inventory().setItem(0, seedItem);
            }
        }
    }

    /** Empties the two harvest slots into the chest, stack by stack. */
    private void deposit() {
        if (!isSuitableForDepositing(this.actionTarget) || !(level().getBlockEntity(this.actionTarget) instanceof ChestBlockEntity chest)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        for (int l = 1; l <= 2; l++) {
            ItemStack stack = inventory().getItem(l);
            if (stack.isEmpty()) {
                continue;
            }
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack chestItem = chest.getItem(slot);
                if (chestItem.isEmpty() || ItemStack.isSameItemSameComponents(chestItem, stack)
                        && chestItem.getCount() < chestItem.getMaxStackSize()) {
                    if (chestItem.isEmpty()) {
                        chestItem = stack.copyWithCount(0);
                    }
                    int moved = Math.min(stack.getCount(), chestItem.getMaxStackSize() - chestItem.getCount());
                    chestItem = chestItem.copyWithCount(chestItem.getCount() + moved);
                    stack.shrink(moved);
                    chest.setItem(slot, chestItem);
                    if (stack.isEmpty()) {
                        inventory().setItem(l, ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }
        level().playSound(null, this.actionTarget, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5f,
                level().getRandom().nextFloat() * 0.1f + 0.9f);
    }

    private void bonemeal() {
        if (!isSuitableForBonemealing(this.actionTarget)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        ItemStack bonemeal = getInventoryBonemeal();
        if (BoneMealItem.growCrop(bonemeal, level(), this.actionTarget)) {
            level().levelEvent(1505, this.actionTarget, 15);
        }
        inventory().setItem(3, bonemeal.isEmpty() ? ItemStack.EMPTY : bonemeal);
    }

    /** Fills the seed slot and the bone-meal slot from the chest, up to a stack each. */
    private void collect() {
        if (!isSuitableForCollecting(this.actionTarget) || !(level().getBlockEntity(this.actionTarget) instanceof ChestBlockEntity chest)) {
            return;
        }
        this.theEntity.swing(InteractionHand.MAIN_HAND);
        for (int l : new int[]{0, 3}) {
            ItemStack match = collectMatch(l);
            if (match == null) {
                continue;
            }
            int count = inventory().getItem(l).getCount();
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack chestItem = chest.getItem(slot);
                if (!chestItem.isEmpty() && ItemStack.isSameItemSameComponents(chestItem, match)) {
                    int moved = Math.min(chestItem.getCount(), match.getMaxStackSize() - count);
                    count += moved;
                    chestItem.shrink(moved);
                    inventory().setItem(l, count > 0 ? match.copyWithCount(count) : ItemStack.EMPTY);
                    chest.setItem(slot, chestItem.isEmpty() ? ItemStack.EMPTY : chestItem);
                    if (count >= match.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }
        level().playSound(null, this.actionTarget, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5f,
                level().getRandom().nextFloat() * 0.1f + 0.9f);
    }
}
