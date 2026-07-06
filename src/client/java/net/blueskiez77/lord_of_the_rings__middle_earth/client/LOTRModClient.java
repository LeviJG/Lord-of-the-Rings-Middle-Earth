package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.fabricmc.api.ClientModInitializer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

public class LOTRModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");
    }
}