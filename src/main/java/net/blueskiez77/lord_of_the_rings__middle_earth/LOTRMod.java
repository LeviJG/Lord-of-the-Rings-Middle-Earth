package net.blueskiez77.lord_of_the_rings__middle_earth;

import net.fabricmc.api.ModInitializer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRCreativeTabs;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockBehaviours;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRMillstoneRecipes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRPackets;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRRecipeTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.command.LOTRAlignmentCommand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.world.level.block.DispenserBlock;
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
        LOGGER.info("The Lord of the Rings mod (Fabric 26.2 port) initializing...");

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
        LOTRMillstoneRecipes.createRecipes();
        LOTRItems.init();

        // After items: the troll statue item names the entity type it places.
        LOTREntities.init();

        // LOTRDispenseCrossbowBolt. The bolt is an ArrowItem, so vanilla's own
        // projectile dispense behaviour already knows how to throw it -- the
        // dispenser just has to be told the item is one, which 1.7.10 did from
        // LOTRItemCrossbowBolt's constructor.
        DispenserBlock.registerProjectileBehavior(LOTRItems.CROSSBOW_BOLT);
        // LOTRDispensePebble, the same way.
        DispenserBlock.registerProjectileBehavior(LOTRItems.PEBBLE);
        DispenserBlock.registerProjectileBehavior(LOTRItems.TAURETHRIM_DART);
        DispenserBlock.registerProjectileBehavior(LOTRItems.POISONED_TAURETHRIM_DART);

        // Creative tab. Must come after blocks and items exist.
        LOTRCreativeTabs.init();

        // Payload types must be registered on both sides and before any
        // handler, so this runs here rather than in the client initializer.
        LOTRPackets.init();

        LOTRAlignmentCommand.register();

        LOGGER.info("LOTR factions initialized: {} factions loaded.", LOTRFaction.values().length);
    }
}