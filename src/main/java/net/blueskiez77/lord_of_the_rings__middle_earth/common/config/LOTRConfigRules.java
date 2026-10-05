package net.blueskiez77.lord_of_the_rings__middle_earth.common.config;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGuiMessageTypes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The vanilla features LOTREventHandler switched off by config: the
 * enchanting table (with vanilla enchanting off, as the server has it), the
 * brewing stand ("Enable Potion Brewing"), villager trading ("Enable Villager
 * trading"), and -- with "Enchanting: Auto-remove vanilla enchants" -- the
 * vanilla enchantments on anything a player carries, stripped every three
 * seconds (not in creative, and never from a fishing rod).
 *
 * <p>The original also told a player once why the enchanting table would not
 * open (LOTRGuiMessageTypes.ENCHANTING), a screen that comes with the GUIs.
 */
public final class LOTRConfigRules {

    private LOTRConfigRules() {
    }

    public static void init() {
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            BlockState state = level.getBlockState(hit.getBlockPos());
            if (state.is(Blocks.ENCHANTING_TABLE) && !LOTRConfig.isEnchantingEnabled(level)) {
                if (!level.isClientSide()) {
                    LOTRGuiMessageTypes.sendMessageIfNotReceived(player, LOTRGuiMessageTypes.ENCHANTING);
                }
                return InteractionResult.FAIL;
            }
            if (!level.isClientSide() && state.is(Blocks.BREWING_STAND) && !LOTRConfig.enablePotionBrewing) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if (!level.isClientSide() && entity instanceof AbstractVillager && !LOTRConfig.enableVillagerTrading) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!LOTRConfig.enchantingAutoRemoveVanilla) {
                return;
            }
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.tickCount % 60 == 0) {
                    for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                        dechant(player.getInventory().getItem(i), player);
                    }
                }
            }
        });
    }

    /** LOTREventHandler.dechant. */
    public static void dechant(ItemStack stack, Player player) {
        if (!player.isCreative() && stack.isEnchanted() && !(stack.getItem() instanceof FishingRodItem)) {
            stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        }
    }
}
