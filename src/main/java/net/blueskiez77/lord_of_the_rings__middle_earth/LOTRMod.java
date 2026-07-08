package net.blueskiez77.lord_of_the_rings__middle_earth;

import net.fabricmc.api.ModInitializer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.command.LOTRAlignmentCommand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for the LOTR mod port (Fabric, Minecraft 26.1.2).
 */
public class LOTRMod implements ModInitializer {

    // Fabric mod id (from fabric.mod.json) — used for logging/identity.
    public static final String MOD_ID = "lord_of_the_rings_-_middle_earth";

    // Namespace for all Identifiers (attachments, registries, assets).
    // Kept hyphen-free and snake_case to satisfy Identifier namespace
    // rules and avoid the '-' liability; this is the namespace your datagen/asset
    // folders should use (assets/lotr/..., datagen/lotr/...).
    public static final String NAMESPACE = "lotr";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The Lord of the Rings mod (Fabric 26.1.2 port) initializing...");

        // Populate faction ranks, control zones, and the relation graph.
        LOTRFaction.initAllProperties();

        // Register the per-player alignment Data Attachment.
        LOTRPlayerAlignments.init();

        // Register content.
        LOTRBlocks.init();
        LOTRItems.init();

        // Register the /alignment test/dev command.
        LOTRAlignmentCommand.register();

        LOGGER.info("LOTR factions initialized: {} factions loaded.", LOTRFaction.values().length);
    }
}