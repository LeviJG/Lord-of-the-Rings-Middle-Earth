package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTREventHandler.onAnvilUpdate: with the vanilla enchanting system off, a
 * vanilla anvil makes nothing of an enchanted book in either slot.
 */
@Mixin(AnvilMenu.class)
abstract class LOTRAnvilMenuMixin extends ItemCombinerMenu {

    @Shadow
    @Final
    private DataSlot cost;

    private LOTRAnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory inventory,
                               ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slots) {
        super(type, containerId, inventory, access, slots);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void lotr$noBooks(CallbackInfo ci) {
        ItemStack input = this.inputSlots.getItem(0);
        ItemStack addition = this.inputSlots.getItem(1);
        if (!LOTRConfig.enchantingVanilla && (input.is(Items.ENCHANTED_BOOK) || addition.is(Items.ENCHANTED_BOOK))) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            ci.cancel();
        }
    }
}
