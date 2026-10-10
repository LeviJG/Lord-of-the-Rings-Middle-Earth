package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievementEvents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LOTREventHandler.onCrafting: the crafting achievements, for whatever comes out of a crafting grid. */
@Mixin(ResultSlot.class)
public abstract class LOTRResultSlotMixin {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements", at = @At("HEAD"))
    private void lotr$craftingAchievements(ItemStack carried, CallbackInfo ci) {
        LOTRAchievementEvents.onCrafting(this.player, carried);
    }
}
