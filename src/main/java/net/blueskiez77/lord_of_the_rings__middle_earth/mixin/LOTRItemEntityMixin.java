package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievementEvents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTREventHandler.onItemPickup: a picked-up item goes first into the pouches in the player's
 * inventory that already hold the like, then into the inventory as usual. All of it in pouches
 * counts as picked up. Picking up athelas, a four-leaf clover or the Kine of Araw's horn earns its
 * achievement.
 */
@Mixin(ItemEntity.class)
public abstract class LOTRItemEntityMixin {

    @WrapOperation(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean lotr$fillPouches(Inventory inventory, ItemStack stack, Operation<Boolean> original) {
        LOTRAchievementEvents.onItemPickup(inventory.player, stack);
        boolean any = false;
        for (int i = 0; i < inventory.getContainerSize() && !stack.isEmpty(); ++i) {
            ItemStack inSlot = inventory.getItem(i);
            if (LOTRPouchItem.isPouch(inSlot)) {
                int before = stack.getCount();
                LOTRPouchItem.tryAddItemToPouch(inSlot, stack, true);
                any |= stack.getCount() != before;
            }
        }
        if (any) {
            inventory.setChanged();
        }
        if (stack.isEmpty()) {
            return true;
        }
        return original.call(inventory, stack);
    }
}
