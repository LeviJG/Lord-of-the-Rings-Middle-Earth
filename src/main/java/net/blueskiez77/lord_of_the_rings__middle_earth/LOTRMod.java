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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.world.level.block.DispenserBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCauldronWashing;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDispenserBehaviours;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMechanisedRailBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierSpecials;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRAlcoholTolerance;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRArmourSets;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVanillaVessels;

public class LOTRMod implements ModInitializer {
    public static final String MOD_ID = "lord_of_the_rings - middle_earth";

    public static final String NAMESPACE = "lotr";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The Lord of the Rings mod (Fabric 26.2 port) initializing...");

        LOTRFaction.initAllProperties();

        LOTRPlayerAlignments.init();
        LOTRLevelData.init();
        LOTRAlcoholTolerance.init();

        LOTRRecipeTypes.init();
        LOTRMenus.init();
        LOTRSounds.init();
        LOTRParticles.init();
        LOTRBlocks.init();
        LOTRBlockEntities.init();
        LOTRBlockBehaviours.init();
        LOTRDataComponents.init();
        LOTREffects.init();
        LOTRItems.init();
        // After the items, as the original built its recipes after registration.
        LOTRMillstoneRecipes.createRecipes();

        LOTREntities.init();

        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.CROSSBOW_BOLT);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.PEBBLE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.TAURETHRIM_DART);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.POISONED_TAURETHRIM_DART);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.POISONED_ARROW);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.POISONED_CROSSBOW_BOLT);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.RHUNIC_FIRE_POT);
        DispenserBlock.registerProjectileBehavior(LOTRMiscItems.CONKER);
        DispenserBlock.registerProjectileBehavior(LOTRMiscItems.MYSTERY_WEB);
        DispenserBlock.registerProjectileBehavior(LOTRMiscItems.EXPLODING_TERMITE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.IRON_THROWING_AXE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.BRONZE_THROWING_AXE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.DWARVEN_THROWING_AXE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.BLUE_DWARVEN_THROWING_AXE);
        DispenserBlock.registerProjectileBehavior(LOTRCombatItems.LOSSARNACH_THROWING_AXE);

        LOTRDispenserBehaviours.init();
        LOTRCauldronWashing.init();
        LOTRBlocks.ALL_PLATES.forEach(plate -> DispenserBlock.registerProjectileBehavior(plate.asItem()));
        LOTRVanillaVessels.init();
        LOTRMechanisedRailBlock.init();
        LOTRModifiers.init();
        LOTRModifierSpecials.init();

        LOTRArmourSets.init();

        LOTRCreativeTabs.init();

        // Payload types must be registered on both sides and before any handler, so this runs here rather than in the client initializer.
        LOTRPackets.init();

        LOTRAlignmentCommand.register();

        LOGGER.info("LOTR factions initialized: {} factions loaded.", LOTRFaction.values().length);
    }

    /** isAprilFools: the first of April, when faction and drink names turn silly. */
    public static boolean isAprilFools() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return today.getMonth() == java.time.Month.APRIL && today.getDayOfMonth() == 1;
    }
}
