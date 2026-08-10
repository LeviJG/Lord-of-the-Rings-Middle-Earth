package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCraftingScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderPlugin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.gui.screens.MenuScreens;

public class LOTRModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");

        LOTRConnectedBorderPlugin.init();

        for (LOTRCraftingTable table : LOTRCraftingTable.values()) {
            MenuScreens.register(LOTRMenus.forTable(table), LOTRCraftingScreen::new);
        }
    }
}