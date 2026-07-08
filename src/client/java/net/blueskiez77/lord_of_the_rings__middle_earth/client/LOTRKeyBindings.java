package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

/**
 * Registers the keybinding that opens the factions screen (default: L),
 * rebindable in Options > Controls under the mod's category.
 *
 * 26.1 API notes:
 *  - Fabric renamed the keybinding API: keybinding.v1.KeyBindingHelper ->
 *    keymapping.v1.KeyMappingHelper (registerKeyBinding -> registerKeyMapping).
 *  - On 1.21.9+ the KeyMapping category is a KeyMapping.Category object
 *    (registered by Identifier), not a translation-key string.
 */
public final class LOTRKeyBindings {

    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "general"));

    public static KeyMapping openFactions;

    private LOTRKeyBindings() {
    }

    public static void register() {
        openFactions = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.lotr.factions",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openFactions.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new LOTRScreenFactions());
                }
            }
        });
    }
}