package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMenuScreen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

/**
 * LOTRKeyHandler.keyBindingMenu: L (key 38 in 1.7.10) opens the LOTR menu,
 * or the menu screen last opened from it.
 *
 * <p>NOT ported yet: the original's other keys (the alignment cycling keys,
 * the map key and the dismount key), with their systems.
 */
public final class LOTRKeyBindings {

    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "general"));

    public static KeyMapping menu;

    private LOTRKeyBindings() {
    }

    public static void register() {
        menu = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.lotr.menu", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_L, CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menu.consumeClick()) {
                if (client.gui.screen() == null && client.player != null) {
                    client.setScreenAndShow(LOTRMenuScreen.openMenu());
                }
            }
        });
    }
}
