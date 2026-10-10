package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievementEvents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTREventHandler.onSmelting: the smelting achievements, for whatever is taken out of a furnace,
 * an alloy forge or a hobbit oven.
 */
@Mixin(FurnaceResultSlot.class)
public abstract class LOTRFurnaceResultSlotMixin {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements", at = @At("HEAD"))
    private void lotr$smeltingAchievements(ItemStack carried, CallbackInfo ci) {
        LOTRAchievementEvents.onSmelting(this.player, carried);
    }
}
