package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.gui.ModsScreen;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

/**
 * Mod Menu's part in the main menu: its Mods button, as the right half of the Realms button. Mod
 * Menu's classes are touched only from here, and only when it is installed.
 */
final class LOTRModMenuCompat {

    static final boolean LOADED = FabricLoader.getInstance().isModLoaded("modmenu");

    private LOTRModMenuCompat() {
    }

    /** A widget Mod Menu put on the title screen itself (its Mods button, in whichever of its styles). */
    static boolean isModMenuWidget(AbstractWidget widget) {
        return widget.getClass().getName().startsWith("com.terraformersmc.modmenu.");
    }

    static Component modsButtonText() {
        return ModMenuApi.createModsButtonText();
    }

    static Screen modsScreen(Screen parent) {
        return new ModsScreen(parent);
    }
}
