package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCoinExchangeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiHandler's inventory coin counts ("Inventory coin counts"): beside each inventory a
 * container screen shows, level with its top row, a silver coin and what its coins are worth (stolen
 * ones too), on a shadow fading to the right. Not for crafting results or the coin exchange's own
 * slots, nor the creative screen's tabs other than the inventory.
 */
public final class LOTRCoinCountDisplay {

    /** coinCountLeftSide: false in the original, so the counts stand to the right. */
    private static final boolean COIN_COUNT_LEFT_SIDE = false;
    /** Built on first use: an item's components are bound only once the registries are loaded. */
    private static ItemStack silverCoin;

    private LOTRCoinCountDisplay() {
    }

    public static void init() {
        ScreenEvents.AFTER_INIT.register((minecraft, screen, width, height) -> {
            if (screen instanceof AbstractContainerScreen<?> containerScreen) {
                ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, partialTick) ->
                        render(containerScreen, graphics));
            }
        });
    }

    private static boolean isExcluded(AbstractContainerMenu menu, Container inv) {
        return inv instanceof ResultContainer
                || menu instanceof LOTRCoinExchangeMenu exchange && (inv == exchange.coinInputInv || inv == exchange.exchangeInv);
    }

    private static void render(AbstractContainerScreen<?> screen, GuiGraphicsExtractor graphics) {
        if (!LOTRConfig.displayCoinCounts || screen instanceof CreativeModeInventoryScreen creative && !creative.isInventoryOpen()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        AbstractContainerMenu menu = screen.getMenu();
        List<Container> differentInvs = new ArrayList<>();
        Map<Container, Integer> invHighestY = new HashMap<>();
        for (Slot slot : menu.slots) {
            Container inv = slot.container;
            if (inv == null || isExcluded(menu, inv)) {
                continue;
            }
            if (!differentInvs.contains(inv)) {
                differentInvs.add(inv);
            }
            invHighestY.merge(inv, slot.y, Math::min);
        }
        for (Container inv : differentInvs) {
            int coins = LOTRCoins.getContainerValue(inv, true);
            if (coins <= 0) {
                continue;
            }
            String sCoins = String.valueOf(coins);
            int border = 2;
            int rectWidth = 18 + mc.font.width(sCoins) + 1;
            int guiGap = 8;
            int x = screen.leftPos + screen.imageWidth + guiGap;
            if (COIN_COUNT_LEFT_SIDE) {
                x = screen.leftPos - guiGap - rectWidth;
            }
            int y = invHighestY.get(inv) + screen.topPos;
            int rectX0 = x - border;
            int rectX1 = x + rectWidth + border;
            // The shadow, black fading from whole on the left to a tenth on the right.
            float a0 = 1.0f;
            float a1 = 0.1f;
            for (int col = rectX0; col < rectX1; ++col) {
                float f = (col - rectX0 + 0.5f) / (rectX1 - rectX0);
                graphics.fill(col, y - border, col + 1, y + 16 + border, ARGB.colorFromFloat(Mth.lerp(f, a0, a1), 0.0f, 0.0f, 0.0f));
            }
            if (silverCoin == null) {
                silverCoin = new ItemStack(LOTRMiscItems.SILVER_COIN);
            }
            graphics.item(silverCoin, x, y);
            graphics.text(mc.font, sCoins, x + 16 + 2, y + (16 - mc.font.lineHeight + 2) / 2, 0xFFFFFFFF, false);
        }
    }
}
