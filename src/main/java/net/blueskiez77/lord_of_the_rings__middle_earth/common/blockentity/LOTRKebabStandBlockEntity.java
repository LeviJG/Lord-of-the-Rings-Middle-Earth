package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.util.ProblemReporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityKebabStand. Eight skewers of meat over a fire, roasting into
 * Kebabs.
 *
 * <p>The stand has NO fuel slot of its own. It reaches DOWN into whatever
 * container is beneath it -- a furnace, a forge, a chest -- takes one piece of
 * fuel out, and burns it. That is the whole design: it is a rack you set on
 * top of something already hot.
 */
public class LOTRKebabStandBlockEntity extends BlockEntity {

    /** LOTRTileEntityKebabStand.MEAT_SLOTS. */
    public static final int MEAT_SLOTS = 8;

    /** Ticks to roast one piece, and the fuel a piece consumes. */
    public static final int COOK_TIME = 200;

    private final NonNullList<ItemStack> meats = NonNullList.withSize(MEAT_SLOTS, ItemStack.EMPTY);
    private final boolean[] cooked = new boolean[MEAT_SLOTS];

    private int cookTime;
    private int fuelTime;

    // NOTE: the original kept cookedClient / cookingClient / meatAmountClient
    // because its description packet carried only a three-field summary. This
    // one syncs the whole block entity, so both sides read the same arrays and
    // those mirror fields are gone.

    /** onBlockHarvested's meta bit 8: broken in creative, so the stand does not drop. */
    private boolean creativeBroken;

    /** The spit turns while it cooks, and coasts to a stop afterwards. */
    private float spin;
    private float prevSpin;

    public LOTRKebabStandBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.KEBAB_STAND, pos, state);
    }

    // ------------------------------------------------------------------ meat

    /**
     * isMeat. The original took any ItemFood flagged as a wolf's favourite --
     * that is, raw meat -- that also had a furnace recipe. The modern spelling
     * of "raw meat a wolf would eat" is the {@code c:foods/raw_meat} tag, and
     * the smelting check is unchanged.
     */
    public boolean isMeat(ItemStack stack) {
        if (stack.isEmpty() || !(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        if (!stack.is(ItemTags.WOLF_FOOD)) {
            return false;
        }
        return serverLevel.recipeAccess()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), serverLevel)
                .isPresent();
    }

    public int getMeatCount() {
        int count = 0;
        for (ItemStack stack : meats) {
            if (!stack.isEmpty()) {
                ++count;
            }
        }
        return count;
    }

    public boolean hasEmptySlot() {
        return meats.stream().anyMatch(ItemStack::isEmpty);
    }

    /** True once ANY skewer is done, which is what the renderer colours by. */
    public boolean isCooked() {
        for (int i = 0; i < MEAT_SLOTS; ++i) {
            if (cooked[i] && !meats.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCooking() {
        return fuelTime > 0;
    }

    private boolean isFullyCooked() {
        for (int i = 0; i < MEAT_SLOTS; ++i) {
            if (!meats.get(i).isEmpty() && !cooked[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean canCook() {
        return !isFullyCooked() && getMeatCount() > 0;
    }

    /** addMeat: one piece onto the first free skewer. */
    public boolean addMeat(ItemStack stack) {
        for (int i = 0; i < MEAT_SLOTS; ++i) {
            if (!meats.get(i).isEmpty()) {
                continue;
            }
            meats.set(i, stack.copyWithCount(1));
            cooked[i] = false;
            sync();
            return true;
        }
        return false;
    }

    /**
     * removeFirstMeat. Cooked pieces come off first -- you get your kebabs back
     * before your raw meat -- and taking the last piece puts the fire out.
     */
    public ItemStack removeFirstMeat() {
        ItemStack taken = ItemStack.EMPTY;

        for (int i = MEAT_SLOTS - 1; i >= 0 && taken.isEmpty(); --i) {
            if (!meats.get(i).isEmpty() && cooked[i]) {
                taken = meats.get(i);
                meats.set(i, ItemStack.EMPTY);
                cooked[i] = false;
            }
        }
        for (int i = MEAT_SLOTS - 1; i >= 0 && taken.isEmpty(); --i) {
            if (!meats.get(i).isEmpty()) {
                taken = meats.get(i);
                meats.set(i, ItemStack.EMPTY);
            }
        }
        if (isCooking() && getMeatCount() == 0) {
            stopCooking();
        }
        sync();
        return taken;
    }

    /** Everything on the spit, for dropping when the stand is broken. */
    public NonNullList<ItemStack> getMeats() {
        return meats;
    }

    public void markCreativeBroken() {
        creativeBroken = true;
    }

    // ------------------------------------------------------------- the item

    /**
     * getKebabStandDrop: the stand, with what is on its spit when there is
     * anything (shouldSaveBlockData).
     */
    public ItemStack getStandDrop() {
        ItemStack stack = new ItemStack(getBlockState().getBlock());
        if (getMeatCount() > 0 && level != null) {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            writeStand(output);
            stack.set(LOTRDataComponents.KEBAB_DATA, CustomData.of(output.buildResult()));
        }
        return stack;
    }

    /** loadKebabData then onReplaced: a stand set down again starts with its fire out. */
    public void loadFromItem(CompoundTag tag, HolderLookup.Provider registries) {
        readStand(TagValueInput.create(ProblemReporter.DISCARDING, registries, tag));
        stopCooking();
        sync();
    }

    // ---------------------------------------------------------------- cooking

    private void stopCooking() {
        cookTime = 0;
        fuelTime = 0;
    }

    /** cookFirstMeat: the last uncooked skewer becomes a Kebab. */
    private void cookFirstMeat() {
        cookTime = 0;
        fuelTime -= COOK_TIME;
        for (int i = MEAT_SLOTS - 1; i >= 0; --i) {
            if (meats.get(i).isEmpty() || cooked[i]) {
                continue;
            }
            meats.set(i, new ItemStack(LOTRItems.KEBAB));
            cooked[i] = true;
            break;
        }
    }

    /**
     * takeFuelFromBelow. One piece of fuel out of whatever container is
     * directly underneath. This is the stand's only fuel source.
     */
    private int takeFuelFromBelow(ServerLevel level) {
        if (!(level.getBlockEntity(worldPosition.below()) instanceof Container below)) {
            return 0;
        }
        for (int i = 0; i < below.getContainerSize(); ++i) {
            ItemStack stack = below.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            int burn = level.fuelValues().burnDuration(stack);
            if (burn <= 0) {
                continue;
            }
            stack.shrink(1);
            below.setChanged();
            return burn;
        }
        return 0;
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state,
                                  LOTRKebabStandBlockEntity stand) {
        boolean wasCooking = stand.isCooking();
        boolean wasCooked = stand.isCooked();

        if (stand.isCooking()) {
            if (stand.canCook()) {
                ++stand.cookTime;
                if (stand.cookTime > stand.fuelTime) {
                    int fuel = stand.takeFuelFromBelow(level);
                    if (fuel > 0) {
                        stand.fuelTime += fuel;
                    } else {
                        stand.stopCooking();
                    }
                } else if (stand.cookTime >= COOK_TIME) {
                    stand.cookFirstMeat();
                }
            } else {
                stand.stopCooking();
            }
        } else if (stand.canCook()) {
            int fuel = stand.takeFuelFromBelow(level);
            if (fuel > 0) {
                stand.cookTime = 0;
                stand.fuelTime = fuel;
            }
        }

        if (stand.isCooking() != wasCooking || stand.isCooked() != wasCooked) {
            stand.sync();
        } else if (stand.isCooking()) {
            // Only the save, not the client: the original pushed an update on
            // these two changes alone. But 1.7.10 re-saved loaded chunks on a
            // timer, and a modern one is saved only once marked.
            stand.setChanged();
        }
    }

    /**
     * The spit spins at 4 degrees a tick while cooking. When the fire goes out
     * it speeds up to 20 and stops at the next whole turn, so it always parks
     * square rather than at some arbitrary angle.
     */
    public static void clientTick(Level level, BlockPos pos, BlockState state,
                                  LOTRKebabStandBlockEntity stand) {
        if (stand.isCooking()) {
            stand.prevSpin = stand.spin;
            stand.spin += 4.0f;

            if (level.getRandom().nextInt(4) == 0) {
                double x = pos.getX() + level.getRandom().nextFloat();
                double y = pos.getY() + level.getRandom().nextFloat() * 0.2f;
                double z = pos.getZ() + level.getRandom().nextFloat();
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
            }
        } else if (stand.spin > 0.0f) {
            stand.prevSpin = stand.spin;
            stand.spin += 20.0f;
            if (Math.ceil(stand.spin / 360.0f) > Math.ceil(stand.prevSpin / 360.0f)) {
                float delta = stand.spin - stand.prevSpin;
                stand.spin = 0.0f;
                stand.prevSpin = -delta;
            }
        } else {
            stand.spin = 0.0f;
            stand.prevSpin = 0.0f;
        }
    }

    public float getSpin(float partialTick) {
        return prevSpin + (spin - prevSpin) * partialTick;
    }

    // ------------------------------------------------------------------ sync

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        readStand(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        writeStand(output);
    }

    /** readKebabStandFromNBT. */
    public void readStand(ValueInput input) {
        meats.clear();
        ContainerHelper.loadAllItems(input, meats);
        cookTime = input.getIntOr("CookTime", 0);
        fuelTime = input.getIntOr("FuelTime", 0);

        // The cooked flags ride as a bit mask rather than the original's
        // per-slot booleans -- eight bits is one byte either way, and it cannot
        // fall out of step with the slot list.
        int mask = input.getIntOr("Cooked", 0);
        for (int i = 0; i < MEAT_SLOTS; ++i) {
            cooked[i] = (mask & (1 << i)) != 0;
        }
    }

    /** writeKebabStandToNBT. */
    public void writeStand(ValueOutput output) {
        ContainerHelper.saveAllItems(output, meats);
        output.putInt("CookTime", cookTime);
        output.putInt("FuelTime", fuelTime);

        int mask = 0;
        for (int i = 0; i < MEAT_SLOTS; ++i) {
            if (cooked[i]) {
                mask |= 1 << i;
            }
        }
        output.putInt("Cooked", mask);
    }

    /**
     * getDrops: the stand drops itself with its meat inside it, unless a
     * creative player broke it. It has no loot table; this is the last hook with
     * the block entity still present.
     */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !creativeBroken) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getStandDrop());
        }
    }
}
