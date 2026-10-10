package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * The mounts that were donkeys on the server (horse type 1) and so carried a
 * chest: the camel and the Shire pony. The chest is AbstractChestedHorse's,
 * which a Horse subclass cannot inherit, so its few pieces are repeated here.
 */
public abstract class LOTRChestedHorseEntity extends LOTRHorseEntity {

    private static final EntityDataAccessor<Boolean> DATA_CHEST =
            SynchedEntityData.defineId(LOTRChestedHorseEntity.class, EntityDataSerializers.BOOLEAN);

    protected LOTRChestedHorseEntity(EntityType<? extends LOTRChestedHorseEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHEST, false);
    }

    public boolean hasChest() {
        return this.entityData.get(DATA_CHEST);
    }

    public void setChest(boolean chest) {
        this.entityData.set(DATA_CHEST, chest);
    }

    @Override
    public int getInventoryColumns() {
        return hasChest() ? 5 : 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getMountable() && !isMountEnraged() && !getBelongsToNPC() && !isVehicle() && isTamed()
                && !isBaby() && !player.isSecondaryUseActive() && !hasChest() && stack.is(Items.CHEST)) {
            setChest(true);
            playSound(SoundEvents.DONKEY_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            stack.consume(1, player);
            createInventory();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public @Nullable SlotAccess getSlot(int slot) {
        if (slot == 499) {
            return new SlotAccess() {
                @Override
                public ItemStack get() {
                    return hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
                }

                @Override
                public boolean set(ItemStack stack) {
                    if (stack.isEmpty()) {
                        if (hasChest()) {
                            setChest(false);
                            createInventory();
                        }
                        return true;
                    }
                    if (stack.is(Items.CHEST)) {
                        if (!hasChest()) {
                            setChest(true);
                            createInventory();
                        }
                        return true;
                    }
                    return false;
                }
            };
        }
        return super.getSlot(slot);
    }

    @Override
    protected void dropEquipment(ServerLevel level) {
        super.dropEquipment(level);
        if (hasChest() && !getBelongsToNPC()) {
            spawnAtLocation(level, Blocks.CHEST);
            setChest(false);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("ChestedHorse", hasChest());
        if (hasChest()) {
            ValueOutput.TypedOutputList<ItemStackWithSlot> items = output.list("Items", ItemStackWithSlot.CODEC);
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (!stack.isEmpty()) {
                    items.add(new ItemStackWithSlot(i, stack));
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setChest(input.getBooleanOr("ChestedHorse", false));
        createInventory();
        if (hasChest()) {
            for (ItemStackWithSlot item : input.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
                if (item.isValidInContainer(this.inventory.getContainerSize())) {
                    this.inventory.setItem(item.slot(), item.stack());
                }
            }
        }
    }
}
