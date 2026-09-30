package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCamel: the Harad pack animal. It carries a chest, which the
 * horse, pony and zebra cannot, and wears a carpet of any colour where a
 * horse wears barding (getCamelCarpetColor, for the renderer). It was a
 * donkey on the server, so it brays like one. Bred on wheat, jumps half as
 * well; drops leather and camel meat ({@code lotr:entities/camel}).
 *
 * <p>The chest is AbstractChestedHorse's, which a Horse subclass cannot
 * inherit, so its few pieces are repeated here.
 *
 * <p>NOT ported yet: the rideCamel achievement (D7) and ImmuneToHeat (D10).
 */
public class LOTRCamelEntity extends LOTRHorseEntity {

    private static final EntityDataAccessor<Boolean> DATA_CHEST =
            SynchedEntityData.defineId(LOTRCamelEntity.class, EntityDataSerializers.BOOLEAN);

    public LOTRCamelEntity(EntityType<? extends LOTRCamelEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHEST, false);
    }

    @Override
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 12.0, 36.0);
    }

    @Override
    public double clampChildJump(double jump) {
        return Mth.clamp(jump, 0.1, 0.6);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.1, 0.35);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH) * 0.5);
    }

    // --- The carpet -----------------------------------------------------------

    /** isMountArmorValid: a carpet goes where barding would. */
    @Override
    public boolean isEquippableInSlot(ItemStack stack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.BODY && isCarpet(stack)) {
            return canUseSlot(EquipmentSlot.BODY);
        }
        return super.isEquippableInSlot(stack, slot);
    }

    private static boolean isCarpet(ItemStack stack) {
        return stack.getItem() instanceof BlockItem block && block.getBlock() instanceof WoolCarpetBlock;
    }

    public boolean isCamelWearingCarpet() {
        return isCarpet(getBodyArmorItem());
    }

    /** getCamelCarpetColor: the carpet's dye, or null. */
    public @Nullable DyeColor getCamelCarpetColor() {
        ItemStack stack = getBodyArmorItem();
        if (stack.getItem() instanceof BlockItem block && block.getBlock() instanceof WoolCarpetBlock carpet) {
            return carpet.getColor();
        }
        return null;
    }

    /** setNomadChestAndCarpet: a nomad's camel, chested and in a carpet of a random colour. */
    public void setNomadChestAndCarpet() {
        setChest(true);
        createInventory();
        setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.CARPET.pick(DyeColor.byId(this.random.nextInt(16)))));
    }

    // --- The chest (AbstractChestedHorse) --------------------------------------

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

    // --- A donkey's voice ------------------------------------------------------

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DONKEY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DONKEY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DONKEY_DEATH;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.DONKEY_ANGRY;
    }
}
