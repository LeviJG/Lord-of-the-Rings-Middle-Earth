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

/** Blockstates, block models, item models. */
public class LOTRModelProvider extends FabricModelProvider {

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
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
    }

    /** cube_all block model + blockstate, plus a block-item model parenting it. */
    private static void trivialCubeWithItem(BlockModelGenerators generators, Block block) {
        generators.createTrivialCube(block);
        generators.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
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