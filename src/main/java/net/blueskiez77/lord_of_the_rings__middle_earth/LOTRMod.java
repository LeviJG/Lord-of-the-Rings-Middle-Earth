package net.blueskiez77.lord_of_the_rings__middle_earth;

import net.fabricmc.api.ModInitializer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRCreativeTabs;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockBehaviours;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRPackets;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRRecipeTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.command.LOTRAlignmentCommand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOTRMod implements ModInitializer {
    public static final String MOD_ID = "lord_of_the_rings_-_middle_earth";

    public static final String NAMESPACE = "lotr";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The Lord of the Rings mod (Fabric 26.1.2 port) initializing...");

        LOTRFaction.initAllProperties();

        LOTRPlayerAlignments.init();

        LOTRRecipeTypes.init();
        LOTRMenus.init();

        // Before blocks: LOTRGateBlock resolves its open/close sounds from
        // these the first time a gate is used, and they must exist by then.
        LOTRSounds.init();

        LOTRBlocks.init();
        LOTRBlockEntities.init();
        LOTRBlockBehaviours.init();
        LOTRItems.init();

        // Creative tab. Must come after blocks and items exist.
        LOTRCreativeTabs.init();

        // Payload types must be registered on both sides and before any
        // handler, so this runs here rather than in the client initializer.
        LOTRPackets.init();

        LOTRAlignmentCommand.register();

        LOGGER.info("LOTR factions initialized: {} factions loaded.", LOTRFaction.values().length);
    }
}