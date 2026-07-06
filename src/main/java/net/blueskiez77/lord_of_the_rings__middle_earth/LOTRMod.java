package net.blueskiez77.lord_of_the_rings__middle_earth;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOTRMod implements ModInitializer {

    public static final String MOD_ID = "lotr";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The Lord of the Rings mod (Fabric 26.1.2 port) initializing...");
        // Nothing wired yet. LOTRFaction.initAllProperties() gets called here
        // once player-alignment storage exists (next slice).
    }
}