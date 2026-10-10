package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WoolCarpetBlock;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCamel: the Harad pack animal. It carries a chest
 * (LOTRChestedHorseEntity), and wears a carpet of any colour where a
 * horse wears barding (getCamelCarpetColor, for the renderer). It was a
 * donkey on the server, so it brays like one. Bred on wheat, jumps half as
 * well; drops leather and camel meat ({@code lotr:entities/camel}).
 *
 * <p>Riding a saddled one earns rideCamel.
 */
public class LOTRCamelEntity extends LOTRChestedHorseEntity implements net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNearHaradBiome.ImmuneToHeat {

    public LOTRCamelEntity(EntityType<? extends LOTRCamelEntity> type, Level level) {
        super(type, level);
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

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel && getFirstPassenger() instanceof Player player && isMountSaddled()) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.RIDE_CAMEL);
        }
    }
}
