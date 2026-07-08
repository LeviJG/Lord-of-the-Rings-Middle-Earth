package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.fabricmc.api.ClientModInitializer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

/**
 * Client entrypoint. Lives in the split client source set so server builds
 * get a compile-time guarantee against client class access.
 *
 * Will eventually own: entity renderers, block entity renderers, model
 * layers, HUD elements (alignment bar), the Middle-earth map screen,
 * particle factories, and keybindings.
 */
public class LOTRModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");
        LOTRAlignmentHud.register();
        LOTRKeyBindings.register();
    }
}