package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.level.block.Block;

/**
 * Blockstates, block models, item models.
 *
 * Which generators emit the ITEM model too (learned the hard way):
 *   createTrivialCube            -> NO  (needs registerSimpleItemModel)
 *   createNonTemplateModelBlock  -> NO  (needs registerSimpleFlatItemModel)
 *   createCrossBlockWithDefaultItem -> YES (the "WithDefaultItem" suffix)
 *   createTrapdoor               -> YES
 *   createDoor                   -> YES (points at assets/<ns>/textures/item/<name>.png)
 * Adding a redundant item-model call throws
 * "IllegalStateException: Duplicate model definition".
 */
public class LOTRModelProvider extends FabricModelProvider {

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        // Solid cubes, planks, leaves
        trivialCubeWithItem(generators, LOTRBlocks.TIN_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SILVER_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.MITHRIL_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SALT_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SALTPETER_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SULFUR_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.AMBER_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.AMBER_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.AMETHYST_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.AMETHYST_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.ANGMAR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ANGMAR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ANGMAR_SNOW_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ARNOR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ARNOR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ARNOR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ARNOR_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BLACK_GONDOR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BLACK_GONDOR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BLACK_UMBAR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BLACK_URUK_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.BLUE_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BLUE_DWARF_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.BLUE_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.BLUE_ROCK_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.BONE_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.BRONZE_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.CHALK);
        trivialCubeWithItem(generators, LOTRBlocks.CHALK_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_BLACK);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_BLUE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_BROWN);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_CYAN);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_GRAY);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_GREEN);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_LIGHT_BLUE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_LIME);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_MAGENTA);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_ORANGE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_PINK);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_PURPLE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_RED);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_SILVER);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_WHITE);
        trivialCubeWithItem(generators, LOTRBlocks.CLAY_TILE_DYED_YELLOW);
        trivialCubeWithItem(generators, LOTRBlocks.CORAL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.DALE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DALE_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DALE_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DALE_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DIAMOND_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.DIAMOND_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.DOL_AMROTH_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DOL_GULDUR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DOL_GULDUR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DOL_GULDUR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DOL_GULDUR_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DORWINION_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DORWINION_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DORWINION_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DORWINION_FLOWERS_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DORWINION_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARF_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARVEN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARVEN_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARVEN_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARVEN_GLOWING_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.DWARVEN_OBSIDIAN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ELF_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.EMERALD_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.EMERALD_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_GOLD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALADHRIM_SILVER_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GALVORN_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.GILDED_IRON_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.GLOWSTONE_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_RUSTIC_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_RUSTIC_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GONDOR_RUSTIC_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.GORAN_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.GRAVEL);
        trivialCubeWithItem(generators, LOTRBlocks.GULDURIL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.GULDURIL_MORDOR_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.GULDURIL_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_GOLD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.HIGH_ELVEN_SILVER_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.MITHRIL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_DIRT);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_GRAVEL);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_MOSS_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.MORDOR_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.MOREDAIN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.MORGUL_IRON_MORDOR_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.MORGUL_IRON_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.MORGUL_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.MORWAITH_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NAURITE_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.NAURITE_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_LAPIS_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_RED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_RED_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.NEAR_HARAD_RED_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.OBSIDIAN_GRAVEL);
        trivialCubeWithItem(generators, LOTRBlocks.OPAL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.OPAL_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.ORC_PLATING_IRON);
        trivialCubeWithItem(generators, LOTRBlocks.ORC_PLATING_RUST);
        trivialCubeWithItem(generators, LOTRBlocks.ORC_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.PEARL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.QUAGMIRE);
        trivialCubeWithItem(generators, LOTRBlocks.QUENDITE_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.QUENDITE_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.RED_BRICK_CRACKED);
        trivialCubeWithItem(generators, LOTRBlocks.RED_BRICK_MOSSY);
        trivialCubeWithItem(generators, LOTRBlocks.RED_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RED_CLAY);
        trivialCubeWithItem(generators, LOTRBlocks.RED_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.RED_ROCK_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RED_SANDSTONE);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_FLOWERS_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_GOLD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_RED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.RHUN_RED_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ROHAN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ROHAN_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ROHAN_ROCK);
        trivialCubeWithItem(generators, LOTRBlocks.RUBY_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.RUBY_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SALT_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.SALTPETER_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.SAPPHIRE_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.SAPPHIRE_ORE);
        trivialCubeWithItem(generators, LOTRBlocks.SCORCHED_STONE);
        trivialCubeWithItem(generators, LOTRBlocks.SILVER_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.SULFUR_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.TAUREDAIN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.TAUREDAIN_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.TAUREDAIN_GOLD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.TAUREDAIN_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.TAUREDAIN_OBSIDIAN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.TIN_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.UMBAR_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UMBAR_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UMBAR_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.URUK_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.URUK_STEEL_BLOCK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_FIRE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_FIRE_TILE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_ICE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_ICE_GLOWING_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_ICE_TILE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_OBSIDIAN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_OBSIDIAN_FIRE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.UTUMNO_OBSIDIAN_TILE_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WHITE_SAND);
        trivialCubeWithItem(generators, LOTRBlocks.WHITE_SANDSTONE);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_CARVED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_CRACKED_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_GOLD_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_MOSSY_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.WOOD_ELVEN_SILVER_BRICK);
        trivialCubeWithItem(generators, LOTRBlocks.ALMOND_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.APPLE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.ASPEN_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.BANANA_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.BAOBAB_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.BEECH_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.CEDAR_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.CHARRED_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.CHERRY_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.CHESTNUT_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.CYPRESS_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.DATE_PALM_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.DRAGON_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.FIR_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.GREEN_OAK_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.HOLLY_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.KANUKA_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.LAIRELOSSE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.LARCH_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.LEBETHRON_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.LEMON_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.LIME_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MAHOGANY_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MALLORN_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MANGO_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MANGROVE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MAPLE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.MIRK_OAK_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.OLIVE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.ORANGE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.PALM_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.PEAR_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.PINE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.PLUM_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.POMEGRANATE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.REDWOOD_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.ROTTEN_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.SHIRE_PINE_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.WILLOW_PLANKS);
        trivialCubeWithItem(generators, LOTRBlocks.ACACIA_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.ALMOND_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.APPLE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.ASPEN_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.BANANA_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.BAOBAB_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.BEECH_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.BIRCH_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.CEDAR_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.CHERRY_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.CHESTNUT_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.CYPRESS_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.DARK_OAK_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.DATE_PALM_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.DRAGON_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.FIR_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.GREEN_OAK_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.HOLLY_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.JUNGLE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.KANUKA_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.LAIRELOSSE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.LARCH_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.LEBETHRON_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.LEMON_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.LIME_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MAHOGANY_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MALLORN_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MANGO_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MANGROVE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MAPLE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MIRK_OAK_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.MIRK_OAK_RED_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.OAK_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.OLIVE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.ORANGE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.PALM_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.PEAR_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.PINE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.PLUM_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.POMEGRANATE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.REDWOOD_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.SHIRE_PINE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.SPRUCE_LEAVES);
        trivialCubeWithItem(generators, LOTRBlocks.WILLOW_LEAVES);

        // Glass (transparent cubes; same cube_all model)
        trivialCubeWithItem(generators, LOTRBlocks.BLACK_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.BLUE_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.BROWN_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.CYAN_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.GRAY_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.GREEN_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.LIGHT_BLUE_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.LIME_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.MAGENTA_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.ORANGE_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.PINK_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.PURPLE_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.RED_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.SILVER_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.WHITE_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.YELLOW_STAINED_GLASS);
        trivialCubeWithItem(generators, LOTRBlocks.GLASS);

        // Saplings (cross model)
        crossBlockWithItem(generators, LOTRBlocks.ALMOND_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.APPLE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.ASPEN_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.BANANA_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.BAOBAB_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.BEECH_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.CEDAR_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.CHERRY_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.CHESTNUT_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.CYPRESS_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.DATE_PALM_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.DRAGON_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.FIR_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.GREEN_OAK_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.HOLLY_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.KANUKA_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.LAIRELOSSE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.LARCH_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.LEBETHRON_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.LEMON_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.LIME_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MAHOGANY_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MALLORN_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MANGO_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MANGROVE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MAPLE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MIRK_OAK_RED_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.MIRK_OAK_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.OLIVE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.ORANGE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.PALM_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.PEAR_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.PINE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.PLUM_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.POMEGRANATE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.REDWOOD_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.SHIRE_PINE_SAPLING);
        crossBlockWithItem(generators, LOTRBlocks.WILLOW_SAPLING);

        // Trapdoors
        generators.createTrapdoor(LOTRBlocks.ACACIA_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.ALMOND_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.APPLE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.ASPEN_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.BANANA_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.BAOBAB_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.BEECH_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.BIRCH_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.CEDAR_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.CHARRED_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.CHERRY_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.CHESTNUT_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.CYPRESS_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.DARK_OAK_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.DATE_PALM_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.DRAGON_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.FIR_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.GREEN_OAK_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.HOLLY_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.JUNGLE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.KANUKA_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.LAIRELOSSE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.LARCH_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.LEBETHRON_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.LEMON_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.LIME_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MAHOGANY_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MALLORN_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MANGO_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MANGROVE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MAPLE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.MIRK_OAK_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.OLIVE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.ORANGE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.PALM_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.PEAR_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.PINE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.PLUM_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.POMEGRANATE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.REDWOOD_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.ROTTEN_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.SHIRE_PINE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.SPRUCE_TRAPDOOR);
        generators.createTrapdoor(LOTRBlocks.WILLOW_TRAPDOOR);

        // Doors (createDoor emits blockstate, all block models, AND the item model,
        // which points at assets/lotr/textures/item/<name>.png)
        generators.createDoor(LOTRBlocks.ACACIA_DOOR);
        generators.createDoor(LOTRBlocks.ALMOND_DOOR);
        generators.createDoor(LOTRBlocks.APPLE_DOOR);
        generators.createDoor(LOTRBlocks.ASPEN_DOOR);
        generators.createDoor(LOTRBlocks.BANANA_DOOR);
        generators.createDoor(LOTRBlocks.BAOBAB_DOOR);
        generators.createDoor(LOTRBlocks.BEECH_DOOR);
        generators.createDoor(LOTRBlocks.BIRCH_DOOR);
        generators.createDoor(LOTRBlocks.CEDAR_DOOR);
        generators.createDoor(LOTRBlocks.CHARRED_DOOR);
        generators.createDoor(LOTRBlocks.CHERRY_DOOR);
        generators.createDoor(LOTRBlocks.CHESTNUT_DOOR);
        generators.createDoor(LOTRBlocks.CYPRESS_DOOR);
        generators.createDoor(LOTRBlocks.DARK_OAK_DOOR);
        generators.createDoor(LOTRBlocks.DATE_PALM_DOOR);
        generators.createDoor(LOTRBlocks.DRAGON_DOOR);
        generators.createDoor(LOTRBlocks.FIR_DOOR);
        generators.createDoor(LOTRBlocks.GREEN_OAK_DOOR);
        generators.createDoor(LOTRBlocks.HOLLY_DOOR);
        generators.createDoor(LOTRBlocks.JUNGLE_DOOR);
        generators.createDoor(LOTRBlocks.KANUKA_DOOR);
        generators.createDoor(LOTRBlocks.LAIRELOSSE_DOOR);
        generators.createDoor(LOTRBlocks.LARCH_DOOR);
        generators.createDoor(LOTRBlocks.LEBETHRON_DOOR);
        generators.createDoor(LOTRBlocks.LEMON_DOOR);
        generators.createDoor(LOTRBlocks.LIME_DOOR);
        generators.createDoor(LOTRBlocks.MAHOGANY_DOOR);
        generators.createDoor(LOTRBlocks.MALLORN_DOOR);
        generators.createDoor(LOTRBlocks.MANGO_DOOR);
        generators.createDoor(LOTRBlocks.MANGROVE_DOOR);
        generators.createDoor(LOTRBlocks.MAPLE_DOOR);
        generators.createDoor(LOTRBlocks.MIRK_OAK_DOOR);
        generators.createDoor(LOTRBlocks.OLIVE_DOOR);
        generators.createDoor(LOTRBlocks.ORANGE_DOOR);
        generators.createDoor(LOTRBlocks.PALM_DOOR);
        generators.createDoor(LOTRBlocks.PEAR_DOOR);
        generators.createDoor(LOTRBlocks.PINE_DOOR);
        generators.createDoor(LOTRBlocks.PLUM_DOOR);
        generators.createDoor(LOTRBlocks.POMEGRANATE_DOOR);
        generators.createDoor(LOTRBlocks.REDWOOD_DOOR);
        generators.createDoor(LOTRBlocks.ROTTEN_DOOR);
        generators.createDoor(LOTRBlocks.SHIRE_PINE_DOOR);
        generators.createDoor(LOTRBlocks.SPRUCE_DOOR);
        generators.createDoor(LOTRBlocks.WILLOW_DOOR);

        // Chandeliers (lantern model: hanging + standing)
        generators.createLantern(LOTRBlocks.BLUE_DWARVEN_CHANDELIER);
        generators.createLantern(LOTRBlocks.BRONZE_CHANDELIER);
        generators.createLantern(LOTRBlocks.DWARVEN_CHANDELIER);
        generators.createLantern(LOTRBlocks.GOLD_CHANDELIER);
        generators.createLantern(LOTRBlocks.HIGH_ELVEN_CHANDELIER);
        generators.createLantern(LOTRBlocks.IRON_CHANDELIER);
        generators.createLantern(LOTRBlocks.MALLORN_BLUE_CHANDELIER);
        generators.createLantern(LOTRBlocks.MALLORN_GOLD_CHANDELIER);
        generators.createLantern(LOTRBlocks.MALLORN_GREEN_CHANDELIER);
        generators.createLantern(LOTRBlocks.MALLORN_SILVER_CHANDELIER);
        generators.createLantern(LOTRBlocks.MITHRIL_CHANDELIER);
        generators.createLantern(LOTRBlocks.MORGUL_CHANDELIER);
        generators.createLantern(LOTRBlocks.ORC_CHANDELIER);
        generators.createLantern(LOTRBlocks.SILVER_CHANDELIER);
        generators.createLantern(LOTRBlocks.URUK_CHANDELIER);
        generators.createLantern(LOTRBlocks.WOOD_ELVEN_CHANDELIER);

        // Bars (pane model)
        generators.createBarsAndItem(LOTRBlocks.BLUE_DWARF_BARS);
        generators.createBarsAndItem(LOTRBlocks.BRONZE_BARS);
        generators.createBarsAndItem(LOTRBlocks.DWARF_BARS);
        generators.createBarsAndItem(LOTRBlocks.GALADHRIM_BARS);
        generators.createBarsAndItem(LOTRBlocks.GALADHRIM_WOOD_BARS);
        generators.createBarsAndItem(LOTRBlocks.GOLD_BARS);
        generators.createBarsAndItem(LOTRBlocks.HIGH_ELF_BARS);
        generators.createBarsAndItem(LOTRBlocks.HIGH_ELF_WOOD_BARS);
        generators.createBarsAndItem(LOTRBlocks.MITHRIL_BARS);
        generators.createBarsAndItem(LOTRBlocks.ORC_STEEL_BARS);
        generators.createBarsAndItem(LOTRBlocks.REED_BARS);
        generators.createBarsAndItem(LOTRBlocks.SILVER_BARS);
        generators.createBarsAndItem(LOTRBlocks.URUK_BARS);
        generators.createBarsAndItem(LOTRBlocks.WOOD_ELF_BARS);
        generators.createBarsAndItem(LOTRBlocks.WOOD_ELF_WOOD_BARS);
    }

    private static void trivialCubeWithItem(BlockModelGenerators generators, Block block) {
        generators.createTrivialCube(block);
        generators.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
    }

    private static void crossBlockWithItem(BlockModelGenerators generators, Block block) {
        generators.createCrossBlockWithDefaultItem(block, BlockModelGenerators.PlantType.NOT_TINTED);
    }


    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(LOTRItems.MITHRIL, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LOTRItems.ATHELAS, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LOTRItems.PIPEWEED, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public String getName() {
        return "LOTR Model Provider";
    }
}