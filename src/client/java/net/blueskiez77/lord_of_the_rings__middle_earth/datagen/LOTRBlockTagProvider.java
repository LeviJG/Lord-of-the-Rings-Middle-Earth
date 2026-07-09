package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

/** Mining + category tags. */
public class LOTRBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public LOTRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        Block[] stoneTier = {
                LOTRBlocks.TIN_ORE,
                LOTRBlocks.SALT_ORE,
                LOTRBlocks.SALTPETER_ORE,
                LOTRBlocks.SULFUR_ORE,
                LOTRBlocks.ANGMAR_BRICK,
                LOTRBlocks.ANGMAR_CRACKED_BRICK,
                LOTRBlocks.ANGMAR_SNOW_BRICK,
                LOTRBlocks.ARNOR_BRICK,
                LOTRBlocks.ARNOR_CARVED_BRICK,
                LOTRBlocks.ARNOR_CRACKED_BRICK,
                LOTRBlocks.ARNOR_MOSSY_BRICK,
                LOTRBlocks.BLACK_GONDOR_BRICK,
                LOTRBlocks.BLACK_GONDOR_CARVED_BRICK,
                LOTRBlocks.BLACK_UMBAR_CARVED_BRICK,
                LOTRBlocks.BLUE_CARVED_BRICK,
                LOTRBlocks.BLUE_DWARF_STEEL_BLOCK,
                LOTRBlocks.BLUE_ROCK,
                LOTRBlocks.BLUE_ROCK_BRICK,
                LOTRBlocks.BONE_BLOCK,
                LOTRBlocks.BRONZE_BLOCK,
                LOTRBlocks.CHALK,
                LOTRBlocks.CHALK_BRICK,
                LOTRBlocks.CLAY_TILE,
                LOTRBlocks.CLAY_TILE_DYED_BLACK,
                LOTRBlocks.CLAY_TILE_DYED_BLUE,
                LOTRBlocks.CLAY_TILE_DYED_BROWN,
                LOTRBlocks.CLAY_TILE_DYED_CYAN,
                LOTRBlocks.CLAY_TILE_DYED_GRAY,
                LOTRBlocks.CLAY_TILE_DYED_GREEN,
                LOTRBlocks.CLAY_TILE_DYED_LIGHT_BLUE,
                LOTRBlocks.CLAY_TILE_DYED_LIME,
                LOTRBlocks.CLAY_TILE_DYED_MAGENTA,
                LOTRBlocks.CLAY_TILE_DYED_ORANGE,
                LOTRBlocks.CLAY_TILE_DYED_PINK,
                LOTRBlocks.CLAY_TILE_DYED_PURPLE,
                LOTRBlocks.CLAY_TILE_DYED_RED,
                LOTRBlocks.CLAY_TILE_DYED_SILVER,
                LOTRBlocks.CLAY_TILE_DYED_WHITE,
                LOTRBlocks.CLAY_TILE_DYED_YELLOW,
                LOTRBlocks.DALE_BRICK,
                LOTRBlocks.DALE_CARVED_BRICK,
                LOTRBlocks.DALE_CRACKED_BRICK,
                LOTRBlocks.DALE_MOSSY_BRICK,
                LOTRBlocks.DOL_AMROTH_BRICK,
                LOTRBlocks.DOL_GULDUR_BRICK,
                LOTRBlocks.DOL_GULDUR_CARVED_BRICK,
                LOTRBlocks.DOL_GULDUR_CRACKED_BRICK,
                LOTRBlocks.DOL_GULDUR_MOSSY_BRICK,
                LOTRBlocks.DORWINION_BRICK,
                LOTRBlocks.DORWINION_CARVED_BRICK,
                LOTRBlocks.DORWINION_CRACKED_BRICK,
                LOTRBlocks.DORWINION_FLOWERS_BRICK,
                LOTRBlocks.DORWINION_MOSSY_BRICK,
                LOTRBlocks.DWARF_STEEL_BLOCK,
                LOTRBlocks.DWARVEN_BRICK,
                LOTRBlocks.DWARVEN_CARVED_BRICK,
                LOTRBlocks.DWARVEN_CRACKED_BRICK,
                LOTRBlocks.DWARVEN_GLOWING_BRICK,
                LOTRBlocks.DWARVEN_OBSIDIAN_BRICK,
                LOTRBlocks.GALADHRIM_BRICK,
                LOTRBlocks.GALADHRIM_CARVED_BRICK,
                LOTRBlocks.GALADHRIM_CRACKED_BRICK,
                LOTRBlocks.GALADHRIM_GOLD_BRICK,
                LOTRBlocks.GALADHRIM_MOSSY_BRICK,
                LOTRBlocks.GALADHRIM_SILVER_BRICK,
                LOTRBlocks.GILDED_IRON_BLOCK,
                LOTRBlocks.GLOWSTONE_ORE,
                LOTRBlocks.GONDOR_BRICK,
                LOTRBlocks.GONDOR_CARVED_BRICK,
                LOTRBlocks.GONDOR_CRACKED_BRICK,
                LOTRBlocks.GONDOR_MOSSY_BRICK,
                LOTRBlocks.GONDOR_ROCK,
                LOTRBlocks.GONDOR_RUSTIC_BRICK,
                LOTRBlocks.GONDOR_RUSTIC_CRACKED_BRICK,
                LOTRBlocks.GONDOR_RUSTIC_MOSSY_BRICK,
                LOTRBlocks.GORAN_ROCK,
                LOTRBlocks.GRAVEL,
                LOTRBlocks.HIGH_ELVEN_BRICK,
                LOTRBlocks.HIGH_ELVEN_CARVED_BRICK,
                LOTRBlocks.HIGH_ELVEN_CRACKED_BRICK,
                LOTRBlocks.HIGH_ELVEN_GOLD_BRICK,
                LOTRBlocks.HIGH_ELVEN_MOSSY_BRICK,
                LOTRBlocks.HIGH_ELVEN_SILVER_BRICK,
                LOTRBlocks.MORDOR_BRICK,
                LOTRBlocks.MORDOR_CARVED_BRICK,
                LOTRBlocks.MORDOR_CRACKED_BRICK,
                LOTRBlocks.MORDOR_DIRT,
                LOTRBlocks.MORDOR_GRAVEL,
                LOTRBlocks.MORDOR_MOSS_ROCK,
                LOTRBlocks.MORDOR_ROCK,
                LOTRBlocks.MOREDAIN_BRICK,
                LOTRBlocks.MORWAITH_CRACKED_BRICK,
                LOTRBlocks.NEAR_HARAD_BRICK,
                LOTRBlocks.NEAR_HARAD_CARVED_BRICK,
                LOTRBlocks.NEAR_HARAD_CRACKED_BRICK,
                LOTRBlocks.NEAR_HARAD_LAPIS_BRICK,
                LOTRBlocks.NEAR_HARAD_RED_BRICK,
                LOTRBlocks.NEAR_HARAD_RED_CARVED_BRICK,
                LOTRBlocks.NEAR_HARAD_RED_CRACKED_BRICK,
                LOTRBlocks.OBSIDIAN_GRAVEL,
                LOTRBlocks.ORC_PLATING_IRON,
                LOTRBlocks.ORC_PLATING_RUST,
                LOTRBlocks.ORC_STEEL_BLOCK,
                LOTRBlocks.QUAGMIRE,
                LOTRBlocks.RED_BRICK_CRACKED,
                LOTRBlocks.RED_BRICK_MOSSY,
                LOTRBlocks.RED_CARVED_BRICK,
                LOTRBlocks.RED_CLAY,
                LOTRBlocks.RED_ROCK,
                LOTRBlocks.RED_ROCK_BRICK,
                LOTRBlocks.RED_SANDSTONE,
                LOTRBlocks.RHUN_BRICK,
                LOTRBlocks.RHUN_CARVED_BRICK,
                LOTRBlocks.RHUN_CRACKED_BRICK,
                LOTRBlocks.RHUN_FLOWERS_BRICK,
                LOTRBlocks.RHUN_GOLD_BRICK,
                LOTRBlocks.RHUN_MOSSY_BRICK,
                LOTRBlocks.RHUN_RED_BRICK,
                LOTRBlocks.RHUN_RED_CARVED_BRICK,
                LOTRBlocks.ROHAN_BRICK,
                LOTRBlocks.ROHAN_CARVED_BRICK,
                LOTRBlocks.ROHAN_ROCK,
                LOTRBlocks.SALT_BLOCK,
                LOTRBlocks.SALTPETER_BLOCK,
                LOTRBlocks.SCORCHED_STONE,
                LOTRBlocks.SILVER_BLOCK,
                LOTRBlocks.SULFUR_BLOCK,
                LOTRBlocks.TAUREDAIN_BRICK,
                LOTRBlocks.TAUREDAIN_CRACKED_BRICK,
                LOTRBlocks.TAUREDAIN_GOLD_BRICK,
                LOTRBlocks.TAUREDAIN_MOSSY_BRICK,
                LOTRBlocks.TAUREDAIN_OBSIDIAN_BRICK,
                LOTRBlocks.TIN_BLOCK,
                LOTRBlocks.UMBAR_BRICK,
                LOTRBlocks.UMBAR_CARVED_BRICK,
                LOTRBlocks.UMBAR_CRACKED_BRICK,
                LOTRBlocks.URUK_BRICK,
                LOTRBlocks.URUK_STEEL_BLOCK,
                LOTRBlocks.UTUMNO_FIRE_BRICK,
                LOTRBlocks.UTUMNO_FIRE_TILE_BRICK,
                LOTRBlocks.UTUMNO_ICE_BRICK,
                LOTRBlocks.UTUMNO_ICE_GLOWING_BRICK,
                LOTRBlocks.UTUMNO_ICE_TILE_BRICK,
                LOTRBlocks.UTUMNO_OBSIDIAN_BRICK,
                LOTRBlocks.UTUMNO_OBSIDIAN_FIRE_BRICK,
                LOTRBlocks.UTUMNO_OBSIDIAN_TILE_BRICK,
                LOTRBlocks.WHITE_SAND,
                LOTRBlocks.WHITE_SANDSTONE,
                LOTRBlocks.WOOD_ELVEN_BRICK,
                LOTRBlocks.WOOD_ELVEN_CARVED_BRICK,
                LOTRBlocks.WOOD_ELVEN_CRACKED_BRICK,
                LOTRBlocks.WOOD_ELVEN_GOLD_BRICK,
                LOTRBlocks.WOOD_ELVEN_MOSSY_BRICK,
                LOTRBlocks.WOOD_ELVEN_SILVER_BRICK
        };
        Block[] ironTier = {
                LOTRBlocks.SILVER_ORE,
                LOTRBlocks.MITHRIL_ORE,
                LOTRBlocks.AMBER_BLOCK,
                LOTRBlocks.AMBER_ORE,
                LOTRBlocks.AMETHYST_BLOCK,
                LOTRBlocks.AMETHYST_ORE,
                LOTRBlocks.BLACK_URUK_STEEL_BLOCK,
                LOTRBlocks.CORAL_BLOCK,
                LOTRBlocks.DIAMOND_BLOCK,
                LOTRBlocks.DIAMOND_ORE,
                LOTRBlocks.ELF_STEEL_BLOCK,
                LOTRBlocks.EMERALD_BLOCK,
                LOTRBlocks.EMERALD_ORE,
                LOTRBlocks.GALVORN_BLOCK,
                LOTRBlocks.GULDURIL_BLOCK,
                LOTRBlocks.GULDURIL_MORDOR_ORE,
                LOTRBlocks.GULDURIL_ORE,
                LOTRBlocks.MITHRIL_BLOCK,
                LOTRBlocks.MORGUL_IRON_MORDOR_ORE,
                LOTRBlocks.MORGUL_IRON_ORE,
                LOTRBlocks.MORGUL_STEEL_BLOCK,
                LOTRBlocks.NAURITE_BLOCK,
                LOTRBlocks.NAURITE_ORE,
                LOTRBlocks.OPAL_BLOCK,
                LOTRBlocks.OPAL_ORE,
                LOTRBlocks.PEARL_BLOCK,
                LOTRBlocks.QUENDITE_BLOCK,
                LOTRBlocks.QUENDITE_ORE,
                LOTRBlocks.RUBY_BLOCK,
                LOTRBlocks.RUBY_ORE,
                LOTRBlocks.SAPPHIRE_BLOCK,
                LOTRBlocks.SAPPHIRE_ORE
        };
        Block[] planks = {
                LOTRBlocks.ALMOND_PLANKS,
                LOTRBlocks.APPLE_PLANKS,
                LOTRBlocks.ASPEN_PLANKS,
                LOTRBlocks.BANANA_PLANKS,
                LOTRBlocks.BAOBAB_PLANKS,
                LOTRBlocks.BEECH_PLANKS,
                LOTRBlocks.CEDAR_PLANKS,
                LOTRBlocks.CHARRED_PLANKS,
                LOTRBlocks.CHERRY_PLANKS,
                LOTRBlocks.CHESTNUT_PLANKS,
                LOTRBlocks.CYPRESS_PLANKS,
                LOTRBlocks.DATE_PALM_PLANKS,
                LOTRBlocks.DRAGON_PLANKS,
                LOTRBlocks.FIR_PLANKS,
                LOTRBlocks.GREEN_OAK_PLANKS,
                LOTRBlocks.HOLLY_PLANKS,
                LOTRBlocks.KANUKA_PLANKS,
                LOTRBlocks.LAIRELOSSE_PLANKS,
                LOTRBlocks.LARCH_PLANKS,
                LOTRBlocks.LEBETHRON_PLANKS,
                LOTRBlocks.LEMON_PLANKS,
                LOTRBlocks.LIME_PLANKS,
                LOTRBlocks.MAHOGANY_PLANKS,
                LOTRBlocks.MALLORN_PLANKS,
                LOTRBlocks.MANGO_PLANKS,
                LOTRBlocks.MANGROVE_PLANKS,
                LOTRBlocks.MAPLE_PLANKS,
                LOTRBlocks.MIRK_OAK_PLANKS,
                LOTRBlocks.OLIVE_PLANKS,
                LOTRBlocks.ORANGE_PLANKS,
                LOTRBlocks.PALM_PLANKS,
                LOTRBlocks.PEAR_PLANKS,
                LOTRBlocks.PINE_PLANKS,
                LOTRBlocks.PLUM_PLANKS,
                LOTRBlocks.POMEGRANATE_PLANKS,
                LOTRBlocks.REDWOOD_PLANKS,
                LOTRBlocks.ROTTEN_PLANKS,
                LOTRBlocks.SHIRE_PINE_PLANKS,
                LOTRBlocks.WILLOW_PLANKS
        };

        var pickaxe = valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
        var stone = valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL);
        var iron = valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL);
        var axe = valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE);
        var planksTag = valueLookupBuilder(BlockTags.PLANKS);
        var hoe = valueLookupBuilder(BlockTags.MINEABLE_WITH_HOE);
        var leavesTag = valueLookupBuilder(BlockTags.LEAVES);
        var saplingsTag = valueLookupBuilder(BlockTags.SAPLINGS);
        var trapdoorsTag = valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS);
        var doorsTag = valueLookupBuilder(BlockTags.WOODEN_DOORS);

        for (Block b : stoneTier) {
            pickaxe.add(b);
            stone.add(b);
        }
        for (Block b : ironTier) {
            pickaxe.add(b);
            iron.add(b);
        }
        for (Block b : planks) {
            axe.add(b);
            planksTag.add(b);
        }
        for (Block b : LOTRBlocks.ALL_LEAVES) {
            hoe.add(b);
            leavesTag.add(b);
        }
        for (Block b : LOTRBlocks.ALL_SAPLINGS) {
            saplingsTag.add(b);
        }
        for (Block b : LOTRBlocks.ALL_TRAPDOORS) {
            axe.add(b);
            trapdoorsTag.add(b);
        }
        for (Block b : LOTRBlocks.ALL_DOORS) {
            axe.add(b);
            doorsTag.add(b);
        }
        // Bars need a pickaxe (metal); glass needs no tool.
        for (Block b : LOTRBlocks.ALL_BARS) {
            pickaxe.add(b);
        }
        for (Block b : LOTRBlocks.ALL_CHANDELIERS) {
            pickaxe.add(b);
        }
    }
}