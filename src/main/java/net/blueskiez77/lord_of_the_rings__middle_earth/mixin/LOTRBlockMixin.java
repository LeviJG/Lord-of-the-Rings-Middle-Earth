package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockParticles;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * getSilkTouchModifier, for block drops: loot tables test the tool for silk
 * touch itself, so a Silken tool is handed to them as a copy that has it.
 */
@Mixin(Block.class)
abstract class LOTRBlockMixin {
    @ModifyVariable(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemInstance;)Ljava/util/List;",
            at = @At("HEAD"), argsOnly = true)
    private static ItemInstance lotr$silkTouch(ItemInstance instance, @Local(argsOnly = true) ServerLevel level) {
        if (!(instance instanceof ItemStack tool) || tool.isEmpty() || !LOTRModifiers.isSilkTouch(tool)) {
            return instance;
        }
        var silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, tool) > 0) {
            return tool;
        }
        ItemStack silken = tool.copy();
        silken.enchant(silkTouch, 1);
        return silken;
    }

    /** LOTRBlockParticles: randomDisplayTick effects of blocks with no class of their own. */
    @Inject(method = "animateTick", at = @At("HEAD"))
    private void lotr$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        LOTRBlockParticles.animate((Block) (Object) this, level, pos, random);
    }
}
