package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRFishing;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTREntityFishHook: in Middle-earth, the catch is from LOTRFishing, by the rod's Luck of the Sea and
 * Lure; a ring fished up earns fishRing.
 */
@Mixin(FishingHook.class)
abstract class LOTRFishingHookMixin {

    @Shadow
    @Final
    private int luck;

    @Shadow
    @Final
    private int lureSpeed;

    @Shadow
    public abstract Player getPlayerOwner();

    @WrapOperation(method = "retrieve", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> lotr$middleEarthCatch(LootTable table, LootParams params, Operation<ObjectArrayList<ItemStack>> original) {
        FishingHook hook = (FishingHook) (Object) this;
        if (hook.level().dimension() != LOTRWorldGen.MIDDLE_EARTH) {
            return original.call(table, params);
        }
        // The rod's Lure is five seconds a level off the wait.
        ItemStack item = LOTRFishing.getFishResult(hook.getRandom(), hook.getRandom().nextFloat(), this.luck, this.lureSpeed / 100, true);
        Player owner = getPlayerOwner();
        if (owner != null && (item.is(LOTRMiscItems.SILVER_RING) || item.is(LOTRMiscItems.GOLD_RING) || item.is(LOTRMiscItems.MITHRIL_RING))) {
            LOTRPlayerAchievements.addAchievement(owner, LOTRAchievement.FISH_RING);
        }
        ObjectArrayList<ItemStack> list = new ObjectArrayList<>();
        list.add(item);
        return list;
    }
}
