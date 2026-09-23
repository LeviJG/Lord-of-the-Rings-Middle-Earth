package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTREntDraughtItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmithsScrollItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTrollStatueItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRWarhornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// Creative inventory tabs. The 1.7.10 mod had ten tabs (LOTRCreativeTabs: blocks, util, decorations, food, materials, misc, tools, combat, story, spawning). This ports the first of them -- tabBlock, shown as "Middle-earth Blocks" -- faithfully: it holds building materials only, and nothing else. Membership is derived from the original by reading which block classes called setCreativeTab(LOTRCreativeTabs.tabBlock). Forty classes did, and they map onto the family lists in LOTRBlocks: rock and brick cubes, ores and storage blocks, planks, logs, beams, pillars, slabs, stairs, walls, glass, smooth stone, gravels/sands, soils and paths. Deliberately NOT here, because the original put them in other tabs: fences, leaves, saplings, flowers, vines, ladders, torches, chandeliers, panes and treasure piles (tabDeco, ported below); fence gates, doors, crafting tables, chests and beacons (tabUtil); buttons and pressure plates (tabMisc); kebabs, barrels and marzipan (tabFood). Those tabs are not ported yet, so the catch-all EVERYTHING tab below keeps that content reachable in creative in the meantime -- it is scaffolding, to be deleted once the other nine exist.
public final class LOTRCreativeTabs {
    public static final ResourceKey<CreativeModeTab> BLOCKS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "01_blocks"));

    public static final ResourceKey<CreativeModeTab> UTILITIES_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "02_utilities"));

    public static final ResourceKey<CreativeModeTab> DECORATIONS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "03_decorations"));

    public static final ResourceKey<CreativeModeTab> FOOD_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "04_food"));

    public static final ResourceKey<CreativeModeTab> MATERIALS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "05_materials"));

    public static final ResourceKey<CreativeModeTab> MISC_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "06_misc"));

    public static final ResourceKey<CreativeModeTab> TOOLS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "07_tools"));

    // Fabric sorts modded tabs by namespace, then by path -- registration order
    // is ignored -- so the path is the only lever on left-to-right order. The
    // 1.7.10 mod built its tabs in the order blocks, util, decorations, and the
    // numeric prefixes reproduce that; the remaining seven original tabs slot in
    // as 04_.. through 10_.. as they are ported. The prefixes are ids only: the
    // visible name comes from the translation key, which is unprefixed.
    // zz_everything keeps the scaffolding tab last.
    public static final ResourceKey<CreativeModeTab> COMBAT_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "08_combat"));

    public static final ResourceKey<CreativeModeTab> STORY_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "09_story"));

    public static final ResourceKey<CreativeModeTab> EVERYTHING_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "zz_everything"));

    // Blocks that sit in ALL_CUBES for registration convenience but belonged to a different tab in 1.7.10, so they must not appear in this one. The Utumno return portal pieces are a third case: the original never called setCreativeTab on them at all, so they were creative-unobtainable by design.
    // Blocks kept out of BOTH tabs. The bone block duplicates vanilla's, so it
    // stays registered (recipes and its stairs, slab and wall reference it) but
    // is not offered in creative -- take vanilla's instead.
    private static Set<Block> hiddenEverywhere() {
        return new LinkedHashSet<>(List.of(LOTRBlocks.BONE_BLOCK));
    }

    private static Set<Block> cubesFromOtherTabs() {
        return new LinkedHashSet<>(List.of(
                LOTRBlocks.KEBAB_BLOCK,
                LOTRBlocks.TREASURE_COPPER,
                LOTRBlocks.TREASURE_GOLD,
                LOTRBlocks.TREASURE_SILVER,
                LOTRBlocks.UTUMNO_RETURN_PORTAL_BASE,
                LOTRBlocks.UTUMNO_RETURN_LIGHT));
    }

    public static final CreativeModeTab BLOCKS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.HIGH_ELVEN_BRICK))
            .title(Component.translatable("creativeTab.lotr.blocks"))
            .displayItems((params, output) -> {
                Set<Block> skip = cubesFromOtherTabs();
                skip.addAll(hiddenEverywhere());

                LOTRBlocks.ALL_CUBES.forEach(b -> {
                    if (!skip.contains(b)) {
                        output.accept(b);
                    }
                });
                LOTRBlocks.ALL_COLUMNS.forEach(b -> {
                    if (!skip.contains(b)) {
                        output.accept(b);
                    }
                });
                LOTRBlocks.ALL_SOIL_COLUMNS.forEach(output::accept);
                LOTRBlocks.ALL_BOTTOM_TOP.forEach(output::accept);
                LOTRBlocks.ALL_PILLARS.forEach(output::accept);
                LOTRBlocks.ALL_PLANKS.forEach(output::accept);
                LOTRBlocks.ALL_LOGS.forEach(output::accept);
                LOTRBlocks.ALL_BEAMS.forEach(output::accept);
                LOTRBlocks.ALL_STAIRS.forEach(output::accept);
                LOTRBlocks.ALL_SLABS.forEach(output::accept);
                LOTRBlocks.ALL_WALLS.forEach(output::accept);
                LOTRBlocks.ALL_GLASS.forEach(output::accept);
                LOTRBlocks.ALL_PATHS.forEach(output::accept);
                LOTRBlocks.ALL_FARMLAND.forEach(output::accept);
            })
            .build();

    // tabUtil in the original: things you interact with rather than build from.
    // Membership taken from the classes that called
    // setCreativeTab(LOTRCreativeTabs.tabUtil) -- sixteen block classes and one
    // item class, LOTRItemBed. Ladders, torches and chandeliers are NOT here --
    // those were tabDeco; buttons and pressure plates were tabMisc.
    //
    // The ORDER, unlike the membership, is not the original's. 1.7.10 had no
    // ordering of its own: a creative tab simply listed its blocks in
    // registration order, so tabUtil came out interleaved by whatever order
    // LOTRMod happened to construct things in. The grouping below is ours,
    // arranged by what a block is FOR rather than by what class it came from,
    // so that scanning the tab is quicker.
    public static final CreativeModeTab UTILITIES = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.DWARVEN_FORGE))
            .title(Component.translatable("creativeTab.lotr.utilities"))
            .displayItems((params, output) -> {
                // --- Crafting -------------------------------------------
                // One table per culture, so this is the longest run in the
                // tab and the one most often reached for. It goes first.
                LOTRBlocks.ALL_CRAFTING_TABLES.forEach(output::accept);

                // --- Processing -----------------------------------------
                // Everything that consumes an input and gives back something
                // else, roughly hottest first: forges, then the two ovens,
                // then the grinder, then the spits.
                LOTRBlocks.ALL_FORGES.forEach(output::accept);
                // LOTRBlockHobbitOven called setCreativeTab(tabUtil) too.
                output.accept(LOTRBlocks.HOBBIT_OVEN);
                output.accept(LOTRBlocks.UNSMELTERY);
                // LOTRBlockMillstone also called setCreativeTab(tabUtil).
                output.accept(LOTRBlocks.MILLSTONE);
                LOTRBlocks.ALL_KEBAB_STANDS.forEach(output::accept);

                // --- Storage --------------------------------------------
                LOTRBlocks.ALL_CHESTS.forEach(output::accept);

                // --- Furnishing -----------------------------------------
                // The beds reach tabUtil through their item rather than their
                // block: LOTRItemBed's constructor is what sets the tab.
                LOTRBlocks.ALL_BEDS.forEach(output::accept);
                // LOTRBlockOrcChain, likewise tabUtil -- a fixture you hang
                // from a ceiling, and what chandeliers hang from.
                output.accept(LOTRBlocks.ORC_CHAIN);

                // --- Openings -------------------------------------------
                // Every way through a wall, smallest to largest: doors and
                // trapdoors, then fence gates, then the multi-block gates.
                LOTRBlocks.ALL_DOORS.forEach(output::accept);
                LOTRBlocks.ALL_TRAPDOORS.forEach(output::accept);
                LOTRBlocks.ALL_FENCE_GATES.forEach(output::accept);
                LOTRBlocks.ALL_GATES.forEach(output::accept);

                // --- Defence --------------------------------------------
                LOTRBlocks.ALL_DART_TRAPS.forEach(output::accept);

                // --- One of a kind --------------------------------------
                // The set pieces, last because you place one and never need
                // another. LOTRBlockBeacon, LOTRBlockEntJar,
                // LOTRBlockCommandTable and LOTRBlockTrollTotem all called
                // setCreativeTab(tabUtil); the totem listed its three metas in
                // getSubBlocks, which is ALL_TROLL_TOTEMS.
                output.accept(LOTRBlocks.BEACON_OF_GONDOR);
                output.accept(LOTRBlocks.TABLE_OF_COMMAND);
                output.accept(LOTRBlocks.ENT_JAR);
                LOTRBlocks.ALL_TROLL_TOTEMS.forEach(output::accept);
            })
            .build();

    // tabDeco in the original: the things you dress a build with rather than
    // build it out of. Membership is taken from the classes that called
    // setCreativeTab(LOTRCreativeTabs.tabDeco) -- LOTRBlockLeavesBase,
    // LOTRBlockSaplingBase, LOTRBlockFlower, LOTRBlockGrass, LOTRBlockMordorGrass,
    // LOTRBlockMordorMoss, LOTRBlockCorn, LOTRBlockGrapevine, LOTRBlockReed,
    // LOTRBlockFangornRiverweed, LOTRBlockCorruptMallorn, LOTRBlockBerryBush,
    // LOTRBlockVine, LOTRBlockLadder (and LOTRBlockRope, which extends it),
    // LOTRBlockFence, LOTRBlockPane, LOTRBlockGlassPane, LOTRBlockTorch,
    // LOTRBlockChandelier, LOTRBlockThatchFloor, LOTRBlockTreasurePile and
    // LOTRItemRugBase -- plus webUngoliant, which LOTRMod tabDeco'd at its
    // registration site rather than in a class.
    //
    // Deliberately absent because the port has not registered them yet:
    // LOTRBlockStalactite, and the tabDeco items LOTRItemArmorStand and the
    // four rugs (wargskin, bearskin, lionskin, giraffeskin), which place NPC-
    // style entities.
    //
    // As with UTILITIES the ORDER is ours, not the original's -- 1.7.10 simply
    // listed a tab's blocks in registration order. Grouped here by what a thing
    // is for: things that grow, then things that climb or screen, then light,
    // then floor coverings, then the treasure piles.
    public static final CreativeModeTab DECORATIONS = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabDeco.theIcon = simbelmyne.
            .icon(() -> new ItemStack(LOTRBlocks.SIMBELMYNE))
            .title(Component.translatable("creativeTab.lotr.decorations"))
            .displayItems((params, output) -> {
                // --- Foliage --------------------------------------------
                LOTRBlocks.ALL_LEAVES.forEach(output::accept);
                LOTRBlocks.ALL_SAPLINGS.forEach(output::accept);
                // registerFlower backs LOTRBlockFlower and the several other
                // 1.7.10 classes that were all "a cross-shaped plant": the tall
                // grasses, corn stalk, grapevine, reeds, Fangorn riverweed,
                // Mordor grass and corrupt mallorn. Every one was tabDeco.
                // All but the morgul-shroom, which LOTRBlockMorgulShroom put in tabFood.
                LOTRBlocks.ALL_FLOWERS.forEach(block -> {
                    if (block != LOTRBlocks.MORGUL_SHROOM) {
                        output.accept(block);
                    }
                });
                // LOTRBlockDoubleFlower, registered right after the fences.
                LOTRBlocks.ALL_DOUBLE_FLOWERS.forEach(output::accept);
                LOTRBlocks.ALL_BUSHES.forEach(output::accept);

                // --- Climbing and screening -----------------------------
                LOTRBlocks.ALL_VINES.forEach(output::accept);
                // ALL_LADDERS also holds the rope: LOTRBlockRope extends
                // LOTRBlockLadder, so it inherited tabDeco.
                LOTRBlocks.ALL_LADDERS.forEach(output::accept);
                LOTRBlocks.ALL_FENCES.forEach(output::accept);
                LOTRBlocks.ALL_BARS.forEach(output::accept);
                LOTRBlocks.ALL_GLASS_PANES.forEach(output::accept);
                output.accept(LOTRBlocks.WEB_UNGOLIANT);

                // --- Light ----------------------------------------------
                // Floor torches only. The wall variants are separate blocks in
                // 26.2 but were one block with a facing meta in 1.7.10, and
                // they have no item of their own.
                LOTRBlocks.ALL_TORCHES.forEach(output::accept);
                // The orc torch is two blocks tall, so it is its own family.
                LOTRBlocks.ALL_DOUBLE_TORCHES.forEach(output::accept);
                LOTRBlocks.ALL_CHANDELIERS.forEach(output::accept);

                // --- Floor coverings ------------------------------------
                // Mordor moss and the thatch floor, the port's two carpets.
                LOTRBlocks.ALL_CARPETS.forEach(output::accept);

                // --- Cages ----------------------------------------------
                // LOTRBlockBirdCage, LOTRBlockBirdCageWood and
                // LOTRBlockButterflyJar, plus the weapon rack -- all tabDeco.
                LOTRBlocks.ALL_ANIMAL_JARS.forEach(output::accept);
                output.accept(LOTRBlocks.WEAPON_RACK);

                // --- Banners ---------------------------------------------
                // The standing form only: its item places the wall form too.
                LOTRBlocks.ALL_BANNERS.forEach(output::accept);

                // --- Trophies --------------------------------------------
                // LOTRItemBossTrophy.getSubItems, in its trophyID order.
                output.accept(LOTRItems.MOUNTAIN_TROLL_CHIEFTAIN_TROPHY);
                output.accept(LOTRItems.MALLORN_ENT_TROPHY);

                // --- Statues ---------------------------------------------
                // LOTRItemTrollStatue.getSubItems: each of the three outfits
                // once plain and once two-headed, so six entries in all.
                for (int outfit = 0; outfit < LOTRTrollStatueItem.OUTFIT_COUNT; outfit++) {
                    output.accept(LOTRTrollStatueItem.stack(outfit, false));
                    output.accept(LOTRTrollStatueItem.stack(outfit, true));
                }

                // --- Treasure -------------------------------------------
                // getSubBlocks offered two forms of each hoard, metadata 0 and
                // metadata 7: the two-pixel carpet and the full block, both
                // under the same name.
                //
                // Both by item, never by block: accept(Block) goes through
                // Block.asItem(), which can only ever name one of the two.
                LOTRBlocks.ALL_TREASURE_PILES.forEach(pile -> {
                    output.accept(LOTRBlocks.TREASURE_PILE_CARPETS.get(pile));
                    output.accept(LOTRBlocks.TREASURE_PILE_BLOCKS.get(pile));
                });
            })
            .build();

    // The eighth of the original's ten tabs, tabCombat: weapons and armour.
    //
    // LOTRCreativeTabs.setupIcons gave it tabCombat.theIcon = helmetGondor.
    // tabFood, top to bottom as the original showed it: its four blocks first,
    // in block ID order (barrel, morgul-shroom, marchpane block, kebab block),
    // then the items in registerItem order -- the cakes and plates among them.
    // A brewed drink is listed at all five strengths, as getSubItems did; the
    // ent-draught at all seven kinds.
    public static final CreativeModeTab FOOD = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRItems.LEMBAS))
            .title(Component.translatable("creativeTab.lotr.food"))
            .displayItems((params, output) -> {
                output.accept(LOTRBlocks.BARREL);
                output.accept(LOTRBlocks.MORGUL_SHROOM);
                output.accept(LOTRBlocks.MARCHPANE_BLOCK);
                output.accept(LOTRBlocks.KEBAB_BLOCK);
                output.accept(LOTRItems.CLAY_MUG);
                output.accept(LOTRItems.MUG);
                output.accept(LOTRItems.WATER);
                output.accept(LOTRItems.MILK);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.ALE, strength));
                }
                output.accept(LOTRItems.CHOCOLATE);
                output.accept(LOTRBlocks.APPLE_CRUMBLE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.MIRUVOR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.ORC_DRAUGHT, strength));
                }
                output.accept(LOTRItems.LEMBAS);
                output.accept(LOTRItems.LETTUCE);
                output.accept(LOTRItems.GAMMON);
                output.accept(LOTRItems.CLAY_PLATE);
                output.accept(LOTRBlocks.FINE_PLATE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.MEAD, strength));
                }
                output.accept(LOTRItems.GREEN_APPLE);
                output.accept(LOTRItems.PEAR);
                output.accept(LOTRItems.CHERRIES);
                output.accept(LOTRBlocks.CHERRY_PIE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.RED_WINE, strength));
                }
                output.accept(LOTRItems.MALLORN_NUT);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.CIDER, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.PERRY, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.CHERRY_LIQUEUR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.RUM, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.ATHELAS_BREW, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.DWARVEN_TONIC, strength));
                }
                for (int draught = 0; draught < LOTREntDraughtItem.COUNT; draught++) {
                    output.accept(LOTREntDraughtItem.stack(LOTRItems.ENT_DRAUGHT, draught));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.DWARVEN_ALE, strength));
                }
                output.accept(LOTRItems.MAGGOTY_BREAD);
                output.accept(LOTRItems.RABBIT_STEW);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.VODKA, strength));
                }
                output.accept(LOTRItems.HOBBIT_PANCAKE);
                output.accept(LOTRItems.MANGO);
                output.accept(LOTRItems.MANGO_JUICE);
                output.accept(LOTRItems.BANANA);
                output.accept(LOTRItems.BANANA_BREAD);
                output.accept(LOTRBlocks.BANANA_CAKE);
                output.accept(LOTRItems.RAW_LION);
                output.accept(LOTRItems.COOKED_LION);
                output.accept(LOTRItems.RAW_ZEBRA);
                output.accept(LOTRItems.COOKED_ZEBRA);
                output.accept(LOTRItems.RAW_RHINO);
                output.accept(LOTRItems.COOKED_RHINO);
                output.accept(LOTRItems.MAPLE_SYRUP);
                output.accept(LOTRItems.HOBBIT_PANCAKE_WITH_MAPLE_SYRUP);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.MAPLE_BEER, strength));
                }
                output.accept(LOTRItems.DATE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.ARAK, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.CARROT_WINE, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.BANANA_BEER, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.MELON_LIQUEUR, strength));
                }
                output.accept(LOTRBlocks.DALISH_PASTRY);
                output.accept(LOTRItems.BLUEBERRIES);
                output.accept(LOTRItems.BLACKBERRIES);
                output.accept(LOTRItems.RASPBERRIES);
                output.accept(LOTRItems.CRANBERRIES);
                output.accept(LOTRItems.ELDERBERRIES);
                output.accept(LOTRItems.ROAST_CHESTNUT);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.CACTUS_LIQUEUR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.TOROG_DRAUGHT, strength));
                }
                output.accept(LOTRBlocks.BERRY_PIE);
                output.accept(LOTRItems.BLUEBERRY_JUICE);
                output.accept(LOTRItems.BLACKBERRY_JUICE);
                output.accept(LOTRItems.RASPBERRY_JUICE);
                output.accept(LOTRItems.CRANBERRY_JUICE);
                output.accept(LOTRItems.ELDERBERRY_JUICE);
                output.accept(LOTRItems.TOROG_STEW);
                output.accept(LOTRItems.CRAM);
                output.accept(LOTRItems.LEMON);
                output.accept(LOTRBlocks.LEMON_CAKE);
                output.accept(LOTRItems.ORANGE);
                output.accept(LOTRItems.ORANGE_JUICE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.LEMON_LIQUEUR, strength));
                }
                output.accept(LOTRItems.LEMONADE);
                output.accept(LOTRItems.LIME);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.LIME_LIQUEUR, strength));
                }
                output.accept(LOTRItems.RAW_MUTTON);
                output.accept(LOTRItems.COOKED_MUTTON);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.TAURETHRIM_COCOA, strength));
                }
                output.accept(LOTRItems.JUNGLE_REMEDY);
                output.accept(LOTRItems.CORN);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.CORN_LIQUOR, strength));
                }
                output.accept(LOTRItems.RAW_VENISON);
                output.accept(LOTRItems.COOKED_VENISON);
                output.accept(LOTRItems.KEBAB);
                output.accept(LOTRItems.COOKED_CORN);
                output.accept(LOTRItems.SHISH_KEBAB);
                output.accept(LOTRItems.LEEK);
                output.accept(LOTRItems.LEEK_SOUP);
                output.accept(LOTRItems.TURNIP);
                output.accept(LOTRItems.RAW_CAMEL);
                output.accept(LOTRItems.COOKED_CAMEL);
                output.accept(LOTRItems.OLIVES);
                output.accept(LOTRItems.APPLE_JUICE);
                output.accept(LOTRItems.OLIVE_BREAD);
                output.accept(LOTRItems.RED_GRAPES);
                output.accept(LOTRItems.GREEN_GRAPES);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.WHITE_WINE, strength));
                }
                output.accept(LOTRItems.RED_GRAPE_JUICE);
                output.accept(LOTRItems.GREEN_GRAPE_JUICE);
                output.accept(LOTRItems.ROAST_TURNIP);
                output.accept(LOTRItems.MELON_SOUP);
                output.accept(LOTRItems.CERAMIC_MUG);
                output.accept(LOTRItems.ALMOND);
                output.accept(LOTRItems.WILDBERRIES);
                output.accept(LOTRItems.PLUM);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.MORGUL_DRAUGHT, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.PLUM_KVASS, strength));
                }
                output.accept(LOTRItems.MARCHPANE);
                output.accept(LOTRItems.CHOCOLATE_MARCHPANE);
                output.accept(LOTRItems.GOLDEN_GOBLET);
                output.accept(LOTRItems.SILVER_GOBLET);
                output.accept(LOTRItems.COPPER_GOBLET);
                output.accept(LOTRItems.WOODEN_CUP);
                output.accept(LOTRItems.SKULL_CUP);
                output.accept(LOTRItems.WINE_GLASS);
                output.accept(LOTRItems.WATERSKIN);
                output.accept(LOTRItems.ALE_HORN);
                output.accept(LOTRItems.GOLDEN_ALE_HORN);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.TERMITE_TEQUILA, strength));
                }
                output.accept(LOTRItems.YAM);
                output.accept(LOTRItems.ROAST_YAM);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.SOURED_MILK, strength));
                }
                output.accept(LOTRItems.POMEGRANATE);
                output.accept(LOTRItems.POMEGRANATE_JUICE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRItems.POMEGRANATE_WINE, strength));
                }
                output.accept(LOTRItems.MAN_FLESH);
                output.accept(LOTRItems.SALT);
                output.accept(LOTRItems.SUSPICIOUS_MEAT);
                output.accept(LOTRItems.CORN_BREAD);
                output.accept(LOTRItems.RAISINS);
                output.accept(LOTRItems.MUSHROOM_PIE);
                output.accept(LOTRBlocks.WOODEN_PLATE);
                output.accept(LOTRBlocks.STONEWARE_PLATE);
            })
            .build();

    // tabMaterials: the items that named it, in registerItem order, with the
    // mithril ingot as its icon (setupIcons). NOT here: the copper ingot and iron
    // nugget, which vanilla's stand in for.
    public static final CreativeModeTab MATERIALS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRItems.MITHRIL))
            .title(Component.translatable("creativeTab.lotr.materials"))
            .displayItems((params, output) -> {
                output.accept(LOTRItems.TIN_INGOT);
                output.accept(LOTRItems.BRONZE_INGOT);
                output.accept(LOTRItems.SILVER_INGOT);
                output.accept(LOTRItems.MITHRIL);
                output.accept(LOTRItems.SILVER_NUGGET);
                output.accept(LOTRItems.MITHRIL_NUGGET);
                output.accept(LOTRItems.ORC_STEEL_INGOT);
                output.accept(LOTRItems.DURNOR);
                output.accept(LOTRItems.PIPEWEED_LEAF);
                output.accept(LOTRItems.PIPEWEED_SEEDS);
                output.accept(LOTRItems.YELLOW_DYE);
                output.accept(LOTRItems.WHITE_DYE);
                output.accept(LOTRItems.BLUEBELL_BLUE);
                output.accept(LOTRItems.CLOVER_GREEN);
                output.accept(LOTRItems.CHARCOAL_DUST);
                output.accept(LOTRItems.BROWN_DYE);
                output.accept(LOTRItems.MALLORN_STICK);
                output.accept(LOTRItems.FUR);
                output.accept(LOTRItems.EDHELVIR);
                output.accept(LOTRItems.WARG_BONE);
                output.accept(LOTRItems.DWARVEN_STEEL_INGOT);
                output.accept(LOTRItems.GALVORN_INGOT);
                output.accept(LOTRItems.ORC_BONE);
                output.accept(LOTRItems.ELF_BONE);
                output.accept(LOTRItems.DWARF_BONE);
                output.accept(LOTRItems.HOBBIT_BONE);
                output.accept(LOTRItems.URUK_STEEL_INGOT);
                output.accept(LOTRItems.TROLL_BONE);
                output.accept(LOTRItems.GULDURIL);
                output.accept(LOTRItems.MORGUL_STEEL_INGOT);
                output.accept(LOTRItems.SULFUR);
                output.accept(LOTRItems.NITER);
                output.accept(LOTRItems.LION_FUR);
                output.accept(LOTRItems.RHINO_HORN);
                output.accept(LOTRItems.GEMSBOK_HIDE);
                output.accept(LOTRItems.GEMSBOK_HORN);
                output.accept(LOTRItems.BLUE_DWARVEN_STEEL_INGOT);
                output.accept(LOTRItems.FLAX_SEEDS);
                output.accept(LOTRItems.FLAX);
                output.accept(LOTRItems.BLACK_URUK_STEEL_INGOT);
                output.accept(LOTRItems.ELVEN_STEEL_INGOT);
                output.accept(LOTRItems.SWAN_FEATHER);
                output.accept(LOTRItems.OBSIDIAN_SHARD);
                output.accept(LOTRItems.HITHLAIN);
                output.accept(LOTRItems.GATE_GEARS);
                output.accept(LOTRItems.RED_GRAPE_SEEDS);
                output.accept(LOTRItems.GREEN_GRAPE_SEEDS);
                output.accept(LOTRItems.KINE_OF_ARAW_HORN);
                output.accept(LOTRItems.BLACKROOT_STICK);
                output.accept(LOTRItems.HORN);
                output.accept(LOTRItems.ITHILDIN);
                output.accept(LOTRItems.GILDED_IRON_INGOT);
                output.accept(LOTRItems.FLAME_OF_UDUN);
                output.accept(LOTRItems.TOPAZ);
                output.accept(LOTRItems.AMETHYST);
                output.accept(LOTRItems.SAPPHIRE);
                output.accept(LOTRItems.RUBY);
                output.accept(LOTRItems.AMBER);
                output.accept(LOTRItems.DIAMOND);
                output.accept(LOTRItems.PEARL);
                output.accept(LOTRItems.CORAL);
                output.accept(LOTRItems.OPAL);
                output.accept(LOTRItems.EMERALD);
                output.accept(LOTRItems.CHILL_OF_DAEDELOS);
                output.accept(LOTRItems.HEADHUNTERS_TROPHY);
                // One scroll per modifier it could teach -- hasTemplateItem was
                // "weight above zero and beneficial".
                for (net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier modifier
                        : net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier.values()) {
                    if (modifier.weight() > 0 && modifier.isBeneficial()) {
                        output.accept(LOTRSmithsScrollItem.of(modifier));
                    }
                }
                output.accept(LOTRItems.MITHRIL_MAIL);
                output.accept(LOTRItems.BOOK_OF_TRUE_SILVER);
                output.accept(LOTRItems.RED_CLAY_BALL);
            })
            .build();

    // tabTools: the items that named it, in registerItem order, as tabCombat is.
    // NOT here yet: the branding iron, which brands NPCs -- the port has none.
    // tabMisc: the odds and ends -- rings, coins, keys, curios -- plus the
    // stone buttons and pressure plates, which were tabMisc blocks rather than
    // tabBlock ones. Item order is LOTRMod's registration order.
    public static final CreativeModeTab MISC = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabMisc.theIcon = hobbitPipe.
            .icon(() -> new ItemStack(LOTRItems.SMOKING_PIPE))
            .title(Component.translatable("creativeTab.lotr.misc"))
            .displayItems((params, output) -> {
                output.accept(LOTRItems.GOLD_RING);
                output.accept(LOTRItems.SILVER_RING);
                output.accept(LOTRItems.MITHRIL_RING);
                // getSubItems listed the pipe once per smoke colour.
                for (int smokeColour = 0; smokeColour < LOTRSmokingPipeItem.COLORS; smokeColour++) {
                    output.accept(LOTRSmokingPipeItem.of(smokeColour));
                }
                output.accept(LOTRItems.SILVER_COIN);
                output.accept(LOTRItems.SILVER_COIN_STACK);
                output.accept(LOTRItems.SILVER_COIN_PILE);
                output.accept(LOTRItems.HOBBIT_MARRIAGE_RING);
                output.accept(LOTRItems.ANCIENT_SWORD_TIP);
                output.accept(LOTRItems.ANCIENT_SWORD_BLADE);
                output.accept(LOTRItems.ANCIENT_SWORD_HILT);
                output.accept(LOTRItems.ANCIENT_ARMOR_PLATE);
                output.accept(LOTRItems.ANCIENT_SWORD);
                output.accept(LOTRItems.ANCIENT_DAGGER);
                output.accept(LOTRItems.ANCIENT_HELMET);
                output.accept(LOTRItems.ANCIENT_CHESTPLATE);
                output.accept(LOTRItems.ANCIENT_LEGGINGS);
                output.accept(LOTRItems.ANCIENT_BOOTS);
                output.accept(LOTRItems.DWARVEN_MARRIAGE_RING);
                output.accept(LOTRItems.RED_BOOK);
                output.accept(LOTRItems.KEY_OF_ICE);
                output.accept(LOTRItems.KEY_OF_OBSIDIAN);
                output.accept(LOTRItems.ICE_KEY_HANDLE);
                output.accept(LOTRItems.ICE_KEY_SHAFT);
                output.accept(LOTRItems.ICE_KEY_PIN);
                output.accept(LOTRItems.OBSIDIAN_KEY_HANDLE);
                output.accept(LOTRItems.OBSIDIAN_KEY_SHAFT);
                output.accept(LOTRItems.OBSIDIAN_KEY_PIN);
                output.accept(LOTRItems.TAURETHRIM_AMULET);
                // getSubItems: each colour sealed, then unsealed.
                for (net.minecraft.world.item.Item cracker : java.util.List.of(LOTRItems.RED_DALISH_CRACKER,
                        LOTRItems.BLUE_DALISH_CRACKER, LOTRItems.GREEN_DALISH_CRACKER,
                        LOTRItems.SILVER_DALISH_CRACKER, LOTRItems.GOLD_DALISH_CRACKER)) {
                    output.accept(cracker);
                    ItemStack unsealed = new ItemStack(cracker);
                    unsealed.set(net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents.CRACKER_EMPTY, true);
                    output.accept(unsealed);
                }
                output.accept(LOTRItems.MYSTERY_WEB);
                output.accept(LOTRItems.EXPLODING_TERMITE);
                output.accept(LOTRItems.CONKER);
                output.accept(LOTRItems.LEATHER_HAT);
                output.accept(LOTRItems.PARTY_HAT);
                output.accept(LOTRItems.HARAD_TURBAN);
                output.accept(LOTRItems.HARAD_ROBE);
                output.accept(LOTRItems.HARAD_ROBE_LEGGINGS);
                output.accept(LOTRItems.HARAD_ROBE_SHOES);
                output.accept(LOTRItems.KAFTAN);
                output.accept(LOTRItems.KAFTAN_LEGGINGS);
                output.accept(LOTRItems.BOTTLE_OF_POISON);
                output.accept(LOTRItems.MECHANISM);
                // LOTRMod tabMisc'd the pipeweed at its registration site.
                output.accept(LOTRItems.PIPEWEED);
                LOTRBlocks.ALL_BUTTONS.forEach(output::accept);
                LOTRBlocks.ALL_PRESSURE_PLATES.forEach(output::accept);
            })
            .build();

    public static final CreativeModeTab TOOLS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRItems.MORDOR_PICKAXE))
            .title(Component.translatable("creativeTab.lotr.tools"))
            .displayItems((params, output) -> {
                output.accept(LOTRItems.BRONZE_SHOVEL);
                output.accept(LOTRItems.BRONZE_PICKAXE);
                output.accept(LOTRItems.BRONZE_AXE);
                output.accept(LOTRItems.BRONZE_HOE);
                output.accept(LOTRItems.MITHRIL_SHOVEL);
                output.accept(LOTRItems.MITHRIL_PICKAXE);
                output.accept(LOTRItems.MITHRIL_AXE);
                output.accept(LOTRItems.MITHRIL_HOE);
                output.accept(LOTRItems.MALLORN_SHOVEL);
                output.accept(LOTRItems.MALLORN_PICKAXE);
                output.accept(LOTRItems.MALLORN_AXE);
                output.accept(LOTRItems.MALLORN_HOE);
                output.accept(LOTRItems.GALADHRIM_SHOVEL);
                output.accept(LOTRItems.GALADHRIM_PICKAXE);
                output.accept(LOTRItems.GALADHRIM_AXE);
                output.accept(LOTRItems.GALADHRIM_HOE);
                output.accept(LOTRItems.DWARVEN_SHOVEL);
                output.accept(LOTRItems.DWARVEN_PICKAXE);
                output.accept(LOTRItems.DWARVEN_AXE);
                output.accept(LOTRItems.DWARVEN_HOE);
                output.accept(LOTRItems.MORDOR_SHOVEL);
                output.accept(LOTRItems.MORDOR_PICKAXE);
                output.accept(LOTRItems.MORDOR_AXE);
                output.accept(LOTRItems.MORDOR_HOE);
                output.accept(LOTRItems.URUK_SHOVEL);
                output.accept(LOTRItems.URUK_PICKAXE);
                output.accept(LOTRItems.URUK_AXE);
                output.accept(LOTRItems.URUK_HOE);
                output.accept(LOTRItems.DWARVEN_MATTOCK);
                output.accept(LOTRItems.WOOD_ELVEN_SHOVEL);
                output.accept(LOTRItems.WOOD_ELVEN_PICKAXE);
                output.accept(LOTRItems.WOOD_ELVEN_AXE);
                output.accept(LOTRItems.WOOD_ELVEN_HOE);
                output.accept(LOTRItems.SULFUR_MATCH);
                output.accept(LOTRItems.ANGMAR_SHOVEL);
                output.accept(LOTRItems.ANGMAR_PICKAXE);
                output.accept(LOTRItems.ANGMAR_AXE);
                output.accept(LOTRItems.ANGMAR_HOE);
                output.accept(LOTRItems.LINDON_SHOVEL);
                output.accept(LOTRItems.LINDON_PICKAXE);
                output.accept(LOTRItems.LINDON_AXE);
                output.accept(LOTRItems.LINDON_HOE);
                output.accept(LOTRItems.BLUE_DWARVEN_SHOVEL);
                output.accept(LOTRItems.BLUE_DWARVEN_PICKAXE);
                output.accept(LOTRItems.BLUE_DWARVEN_AXE);
                output.accept(LOTRItems.BLUE_DWARVEN_HOE);
                output.accept(LOTRItems.BLUE_DWARVEN_MATTOCK);
                output.accept(LOTRItems.DOL_GULDUR_SHOVEL);
                output.accept(LOTRItems.DOL_GULDUR_AXE);
                output.accept(LOTRItems.DOL_GULDUR_PICKAXE);
                output.accept(LOTRItems.DOL_GULDUR_HOE);
                output.accept(LOTRItems.UTUMNO_PICKAXE);
                output.accept(LOTRItems.TAURETHRIM_SHOVEL);
                output.accept(LOTRItems.TAURETHRIM_PICKAXE);
                output.accept(LOTRItems.TAURETHRIM_AXE);
                output.accept(LOTRItems.TAURETHRIM_HOE);
                output.accept(LOTRItems.CHISEL);
                output.accept(LOTRItems.MOON_CHISEL);
                output.accept(LOTRItems.MITHRIL_MATTOCK);
                output.accept(LOTRItems.RIVENDELL_SHOVEL);
                output.accept(LOTRItems.RIVENDELL_PICKAXE);
                output.accept(LOTRItems.RIVENDELL_AXE);
                output.accept(LOTRItems.RIVENDELL_HOE);
            })
            .build();

    public static final CreativeModeTab COMBAT = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRItems.GONDOR_HELMET))
            .title(Component.translatable("creativeTab.lotr.combat"))
            .displayItems((params, output) -> {
                // LOTRCreativeTabs had no grouping of its own: a tab was the
                // items that named it, in registration order -- and blocks are
                // registered before items, which is why the bombs come first.
                // This is that order, minus the tools, which are not ported.
                LOTRBlocks.ALL_ORC_BOMBS.forEach(output::accept);
                // registerBlock(rhunFireJar) came after the bombs.
                output.accept(LOTRBlocks.KHAMULS_FIRE_JAR);

                output.accept(LOTRItems.BRONZE_SWORD);
                output.accept(LOTRItems.BRONZE_HELMET);
                output.accept(LOTRItems.BRONZE_CHESTPLATE);
                output.accept(LOTRItems.BRONZE_LEGGINGS);
                output.accept(LOTRItems.BRONZE_BOOTS);
                output.accept(LOTRItems.MORDOR_SCIMITAR);
                output.accept(LOTRItems.MORDOR_HELMET);
                output.accept(LOTRItems.MORDOR_CHESTPLATE);
                output.accept(LOTRItems.MORDOR_LEGGINGS);
                output.accept(LOTRItems.MORDOR_BOOTS);
                output.accept(LOTRItems.MORDOR_BATTLEAXE);
                output.accept(LOTRItems.MORDOR_DAGGER);
                output.accept(LOTRItems.POISONED_MORDOR_DAGGER);
                output.accept(LOTRItems.MITHRIL_SWORD);
                output.accept(LOTRItems.GONDOR_SWORD);
                output.accept(LOTRItems.GONDOR_HELMET);
                output.accept(LOTRItems.GONDOR_CHESTPLATE);
                output.accept(LOTRItems.GONDOR_LEGGINGS);
                output.accept(LOTRItems.GONDOR_BOOTS);
                output.accept(LOTRItems.MITHRIL_HELMET);
                output.accept(LOTRItems.MITHRIL_CHESTPLATE);
                output.accept(LOTRItems.MITHRIL_LEGGINGS);
                output.accept(LOTRItems.MITHRIL_BOOTS);
                output.accept(LOTRItems.GONDOR_SPEAR);
                output.accept(LOTRItems.MORDOR_SPEAR);
                output.accept(LOTRItems.BRONZE_SPEAR);
                output.accept(LOTRItems.IRON_SPEAR);
                output.accept(LOTRItems.MITHRIL_SPEAR);
                output.accept(LOTRItems.MALLORN_SWORD);
                output.accept(LOTRItems.GALADHRIM_SWORD);
                output.accept(LOTRItems.GALADHRIM_SPEAR);
                output.accept(LOTRItems.MALLORN_BOW);
                output.accept(LOTRItems.GALADHRIM_HELMET);
                output.accept(LOTRItems.GALADHRIM_CHESTPLATE);
                output.accept(LOTRItems.GALADHRIM_LEGGINGS);
                output.accept(LOTRItems.GALADHRIM_BOOTS);
                output.accept(LOTRItems.GALADHRIM_BOW);
                output.accept(LOTRItems.FUR_HAT);
                output.accept(LOTRItems.FUR_TUNIC);
                output.accept(LOTRItems.FUR_LEGGINGS);
                output.accept(LOTRItems.FUR_BOOTS);
                output.accept(LOTRItems.ORC_BOW);
                output.accept(LOTRItems.BLACKSMITH_HAMMER);
                output.accept(LOTRItems.GONDOR_DAGGER);
                output.accept(LOTRItems.GALADHRIM_DAGGER);
                output.accept(LOTRItems.DWARVEN_SWORD);
                output.accept(LOTRItems.DWARVEN_DAGGER);
                output.accept(LOTRItems.DWARVEN_BATTLEAXE);
                output.accept(LOTRItems.DWARVEN_WARHAMMER);
                output.accept(LOTRItems.MORDOR_WARHAMMER);
                output.accept(LOTRItems.DWARVEN_HELMET);
                output.accept(LOTRItems.DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.DWARVEN_LEGGINGS);
                output.accept(LOTRItems.DWARVEN_BOOTS);
                output.accept(LOTRItems.GALVORN_HELMET);
                output.accept(LOTRItems.GALVORN_CHESTPLATE);
                output.accept(LOTRItems.GALVORN_LEGGINGS);
                output.accept(LOTRItems.GALVORN_BOOTS);
                output.accept(LOTRItems.BRONZE_DAGGER);
                output.accept(LOTRItems.IRON_DAGGER);
                output.accept(LOTRItems.MITHRIL_DAGGER);
                output.accept(LOTRItems.MITHRIL_BATTLEAXE);
                output.accept(LOTRItems.MITHRIL_WARHAMMER);
                output.accept(LOTRItems.GONDOR_WARHAMMER);
                // getSubItems gave all four forms of the horn.
                for (LOTRCommandHornItem.Mode mode : LOTRCommandHornItem.Mode.values()) {
                    output.accept(LOTRCommandHornItem.stack(mode));
                }
                // throwingAxeDwarven, registered between the horn and the Uruks.
                output.accept(LOTRItems.DWARVEN_THROWING_AXE);
                output.accept(LOTRItems.URUK_CLEAVER);
                output.accept(LOTRItems.URUK_DAGGER);
                output.accept(LOTRItems.POISONED_URUK_DAGGER);
                output.accept(LOTRItems.URUK_BATTLEAXE);
                output.accept(LOTRItems.URUK_WARHAMMER);
                output.accept(LOTRItems.URUK_SPEAR);
                output.accept(LOTRItems.URUK_HELMET);
                output.accept(LOTRItems.URUK_CHESTPLATE);
                output.accept(LOTRItems.URUK_LEGGINGS);
                output.accept(LOTRItems.URUK_BOOTS);
                // The bolt and its three crossbows come straight after the Uruk
                // armour in LOTRMod's registerItem order.
                output.accept(LOTRItems.CROSSBOW_BOLT);
                output.accept(LOTRItems.URUK_CROSSBOW);
                output.accept(LOTRItems.IRON_CROSSBOW);
                output.accept(LOTRItems.MITHRIL_CROSSBOW);
                output.accept(LOTRItems.WOOD_ELVEN_SCOUT_HOOD);
                output.accept(LOTRItems.WOOD_ELVEN_SCOUT_TUNIC);
                output.accept(LOTRItems.WOOD_ELVEN_SCOUT_LEGGINGS);
                output.accept(LOTRItems.WOOD_ELVEN_SCOUT_BOOTS);
                output.accept(LOTRItems.MIRKWOOD_BOW);
                output.accept(LOTRItems.ROHIRRIC_SWORD);
                output.accept(LOTRItems.ROHIRRIC_DAGGER);
                output.accept(LOTRItems.ROHIRRIC_SPEAR);
                output.accept(LOTRItems.ROHIRRIC_COIF);
                output.accept(LOTRItems.ROHIRRIC_HAUBERK);
                output.accept(LOTRItems.ROHIRRIC_LEGGINGS);
                output.accept(LOTRItems.ROHIRRIC_BOOTS);
                output.accept(LOTRItems.GONDOR_WINGED_HELMET);
                output.accept(LOTRItems.PEBBLE);
                output.accept(LOTRItems.SLING);
                output.accept(LOTRItems.RANGER_HOOD);
                output.accept(LOTRItems.RANGER_TUNIC);
                output.accept(LOTRItems.RANGER_LEGGINGS);
                output.accept(LOTRItems.RANGER_BOOTS);
                output.accept(LOTRItems.DUNLENDING_HELMET);
                output.accept(LOTRItems.DUNLENDING_CHESTPLATE);
                output.accept(LOTRItems.DUNLENDING_LEGGINGS);
                output.accept(LOTRItems.DUNLENDING_BOOTS);
                output.accept(LOTRItems.DUNLENDING_CLUB);
                output.accept(LOTRItems.DUNLENDING_TRIDENT);
                output.accept(LOTRItems.MORGUL_BLADE);
                output.accept(LOTRItems.MORGUL_HELMET);
                output.accept(LOTRItems.MORGUL_CHESTPLATE);
                output.accept(LOTRItems.MORGUL_LEGGINGS);
                output.accept(LOTRItems.MORGUL_BOOTS);
                output.accept(LOTRItems.WOOD_ELVEN_SWORD);
                output.accept(LOTRItems.WOOD_ELVEN_DAGGER);
                output.accept(LOTRItems.WOOD_ELVEN_SPEAR);
                output.accept(LOTRItems.WOOD_ELVEN_HELMET);
                output.accept(LOTRItems.WOOD_ELVEN_CHESTPLATE);
                output.accept(LOTRItems.WOOD_ELVEN_LEGGINGS);
                output.accept(LOTRItems.WOOD_ELVEN_BOOTS);
                output.accept(LOTRItems.COMMAND_SWORD);
                output.accept(LOTRItems.POISONED_BRONZE_DAGGER);
                output.accept(LOTRItems.POISONED_IRON_DAGGER);
                output.accept(LOTRItems.POISONED_MITHRIL_DAGGER);
                output.accept(LOTRItems.POISONED_GONDOR_DAGGER);
                output.accept(LOTRItems.POISONED_GALADHRIM_DAGGER);
                output.accept(LOTRItems.POISONED_DWARVEN_DAGGER);
                output.accept(LOTRItems.POISONED_ROHIRRIC_DAGGER);
                output.accept(LOTRItems.POISONED_WOOD_ELVEN_DAGGER);
                output.accept(LOTRItems.ANGMAR_SWORD);
                output.accept(LOTRItems.ANGMAR_DAGGER);
                output.accept(LOTRItems.POISONED_ANGMAR_DAGGER);
                output.accept(LOTRItems.ANGMAR_BATTLEAXE);
                output.accept(LOTRItems.ANGMAR_WARHAMMER);
                output.accept(LOTRItems.ANGMAR_SPEAR);
                output.accept(LOTRItems.ANGMAR_HELMET);
                output.accept(LOTRItems.ANGMAR_CHESTPLATE);
                output.accept(LOTRItems.ANGMAR_LEGGINGS);
                output.accept(LOTRItems.ANGMAR_BOOTS);
                output.accept(LOTRItems.ROHIRRIC_BATTLEAXE);
                output.accept(LOTRItems.UMBARIC_SCIMITAR);
                output.accept(LOTRItems.COAST_SOUTHRON_HELMET);
                output.accept(LOTRItems.COAST_SOUTHRON_CHESTPLATE);
                output.accept(LOTRItems.COAST_SOUTHRON_LEGGINGS);
                output.accept(LOTRItems.COAST_SOUTHRON_BOOTS);
                output.accept(LOTRItems.GEMSBOK_HIDE_HELMET);
                output.accept(LOTRItems.GEMSBOK_HIDE_CHESTPLATE);
                output.accept(LOTRItems.GEMSBOK_HIDE_LEGGINGS);
                output.accept(LOTRItems.GEMSBOK_HIDE_BOOTS);
                output.accept(LOTRItems.LINDON_HELMET);
                output.accept(LOTRItems.LINDON_CHESTPLATE);
                output.accept(LOTRItems.LINDON_LEGGINGS);
                output.accept(LOTRItems.LINDON_BOOTS);
                output.accept(LOTRItems.LINDON_SWORD);
                output.accept(LOTRItems.LINDON_DAGGER);
                output.accept(LOTRItems.POISONED_LINDON_DAGGER);
                output.accept(LOTRItems.LINDON_SPEAR);
                output.accept(LOTRItems.UMBARIC_DAGGER);
                output.accept(LOTRItems.POISONED_UMBARIC_DAGGER);
                output.accept(LOTRItems.UMBARIC_SPEAR);
                output.accept(LOTRItems.HARAD_BOW);
                output.accept(LOTRItems.BLUE_DWARVEN_SWORD);
                output.accept(LOTRItems.BLUE_DWARVEN_DAGGER);
                output.accept(LOTRItems.POISONED_BLUE_DWARVEN_DAGGER);
                output.accept(LOTRItems.BLUE_DWARVEN_BATTLEAXE);
                output.accept(LOTRItems.BLUE_DWARVEN_WARHAMMER);
                output.accept(LOTRItems.BLUE_DWARVEN_THROWING_AXE);
                output.accept(LOTRItems.BLUE_DWARVEN_HELMET);
                output.accept(LOTRItems.BLUE_DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.BLUE_DWARVEN_LEGGINGS);
                output.accept(LOTRItems.BLUE_DWARVEN_BOOTS);
                output.accept(LOTRItems.DWARVEN_SPEAR);
                output.accept(LOTRItems.BLUE_DWARVEN_SPEAR);
                output.accept(LOTRItems.GONDOR_HORSE_ARMOR);
                output.accept(LOTRItems.ROHIRRIC_HORSE_ARMOR);
                output.accept(LOTRItems.ISENGARD_WARG_ARMOR);
                output.accept(LOTRItems.LINDON_HORSE_ARMOR);
                output.accept(LOTRItems.GALADHRIM_HORSE_ARMOR);
                output.accept(LOTRItems.MORGUL_HORSE_ARMOR);
                output.accept(LOTRItems.MITHRIL_HORSE_ARMOR);
                output.accept(LOTRItems.WOOD_ELVEN_ELK_ARMOR);
                output.accept(LOTRItems.MORDOR_WARG_ARMOR);
                output.accept(LOTRItems.ANGMAR_WARG_ARMOR);
                output.accept(LOTRItems.ORC_SKULL_STAFF);
                output.accept(LOTRItems.DOL_GULDUR_SWORD);
                output.accept(LOTRItems.DOL_GULDUR_DAGGER);
                output.accept(LOTRItems.POISONED_DOL_GULDUR_DAGGER);
                output.accept(LOTRItems.DOL_GULDUR_SPEAR);
                output.accept(LOTRItems.DOL_GULDUR_BATTLEAXE);
                output.accept(LOTRItems.DOL_GULDUR_WARHAMMER);
                output.accept(LOTRItems.DOL_GULDUR_HELMET);
                output.accept(LOTRItems.DOL_GULDUR_CHESTPLATE);
                output.accept(LOTRItems.DOL_GULDUR_LEGGINGS);
                output.accept(LOTRItems.DOL_GULDUR_BOOTS);
                output.accept(LOTRItems.UTUMNO_HELMET);
                output.accept(LOTRItems.UTUMNO_CHESTPLATE);
                output.accept(LOTRItems.UTUMNO_LEGGINGS);
                output.accept(LOTRItems.UTUMNO_BOOTS);
                output.accept(LOTRItems.BLACK_URUK_CLEAVER);
                output.accept(LOTRItems.BLACK_URUK_DAGGER);
                output.accept(LOTRItems.POISONED_BLACK_URUK_DAGGER);
                output.accept(LOTRItems.BLACK_URUK_SPEAR);
                output.accept(LOTRItems.BLACK_URUK_BATTLEAXE);
                output.accept(LOTRItems.BLACK_URUK_WARHAMMER);
                output.accept(LOTRItems.BLACK_URUK_HELMET);
                output.accept(LOTRItems.BLACK_URUK_CHESTPLATE);
                output.accept(LOTRItems.BLACK_URUK_LEGGINGS);
                output.accept(LOTRItems.BLACK_URUK_BOOTS);
                output.accept(LOTRItems.BLACK_URUK_BOW);
                output.accept(LOTRItems.SOUTHRON_CHAMPION_HELMET);
                output.accept(LOTRItems.UTUMNO_SWORD);
                output.accept(LOTRItems.UTUMNO_DAGGER);
                output.accept(LOTRItems.POISONED_UTUMNO_DAGGER);
                output.accept(LOTRItems.UTUMNO_SPEAR);
                output.accept(LOTRItems.UTUMNO_BATTLEAXE);
                output.accept(LOTRItems.UTUMNO_WARHAMMER);
                output.accept(LOTRItems.UTUMNO_BOW);
                output.accept(LOTRItems.ROHIRRIC_BOW);
                output.accept(LOTRItems.GONDOR_BOW);
                output.accept(LOTRItems.LINDON_BOW);
                output.accept(LOTRItems.BALROG_WHIP);
                output.accept(LOTRItems.IRON_BATTLEAXE);
                output.accept(LOTRItems.BRONZE_BATTLEAXE);
                output.accept(LOTRItems.BRONZE_CROSSBOW);
                // getSubItems gave one warhorn per invasion type, and there are
                // forty-five of those -- eight for Gondor's fiefs alone.
                for (LOTRInvasions invasion : LOTRInvasions.values()) {
                    output.accept(LOTRWarhornItem.stack(LOTRItems.WARHORN, invasion));
                }
                output.accept(LOTRItems.HALF_TROLL_HELMET);
                output.accept(LOTRItems.HALF_TROLL_CHESTPLATE);
                output.accept(LOTRItems.HALF_TROLL_LEGGINGS);
                output.accept(LOTRItems.HALF_TROLL_BOOTS);
                output.accept(LOTRItems.HALF_TROLL_BATTLEAXE);
                output.accept(LOTRItems.HALF_TROLL_WARHAMMER);
                output.accept(LOTRItems.HALF_TROLL_MACE);
                output.accept(LOTRItems.HALF_TROLL_SCIMITAR);
                output.accept(LOTRItems.HALF_TROLL_DAGGER);
                output.accept(LOTRItems.POISONED_HALF_TROLL_DAGGER);
                output.accept(LOTRItems.HALF_TROLL_RHINO_ARMOR);
                output.accept(LOTRItems.SILVER_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRItems.SILVER_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.SILVER_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRItems.SILVER_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRItems.GOLD_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRItems.GOLD_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.GOLD_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRItems.GOLD_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRItems.MITHRIL_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRItems.MITHRIL_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.MITHRIL_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRItems.MITHRIL_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRItems.DOL_AMROTH_SWORD);
                output.accept(LOTRItems.DOL_AMROTH_HELMET);
                output.accept(LOTRItems.DOL_AMROTH_CHESTPLATE);
                output.accept(LOTRItems.DOL_AMROTH_LEGGINGS);
                output.accept(LOTRItems.DOL_AMROTH_BOOTS);
                output.accept(LOTRItems.DOL_AMROTH_HORSE_ARMOR);
                output.accept(LOTRItems.MORWAITH_DAGGER);
                output.accept(LOTRItems.POISONED_MORWAITH_DAGGER);
                output.accept(LOTRItems.MORWAITH_BATTLEAXE);
                output.accept(LOTRItems.MORWAITH_SPEAR);
                output.accept(LOTRItems.MORWAITH_HELMET);
                output.accept(LOTRItems.MORWAITH_CHESTPLATE);
                output.accept(LOTRItems.MORWAITH_LEGGINGS);
                output.accept(LOTRItems.MORWAITH_BOOTS);
                output.accept(LOTRItems.MORWAITH_CHIEFTAIN_HELMET);
                output.accept(LOTRItems.MORWAITH_CHIEFTAIN_CHESTPLATE);
                output.accept(LOTRItems.MORWAITH_CHIEFTAIN_LEGGINGS);
                output.accept(LOTRItems.MORWAITH_CHIEFTAIN_BOOTS);
                output.accept(LOTRItems.BONE_HELMET);
                output.accept(LOTRItems.BONE_CHESTPLATE);
                output.accept(LOTRItems.BONE_LEGGINGS);
                output.accept(LOTRItems.BONE_BOOTS);
                // And the other two throwing axes, which LOTRMod registers a
                // long way after the Uruk set.
                output.accept(LOTRItems.BRONZE_THROWING_AXE);
                output.accept(LOTRItems.IRON_THROWING_AXE);
                output.accept(LOTRItems.GONDOLIN_SWORD);
                output.accept(LOTRItems.CHARRED_MALLORN_MACE);
                output.accept(LOTRItems.GONDOLIN_HELMET);
                output.accept(LOTRItems.GONDOLIN_CHESTPLATE);
                output.accept(LOTRItems.GONDOLIN_LEGGINGS);
                output.accept(LOTRItems.GONDOLIN_BOOTS);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_HELMET);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_CHESTPLATE);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_LEGGINGS);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_BOOTS);
                output.accept(LOTRItems.TAURETHRIM_DAGGER);
                output.accept(LOTRItems.POISONED_TAURETHRIM_DAGGER);
                output.accept(LOTRItems.TAURETHRIM_SPEAR);
                output.accept(LOTRItems.TAURETHRIM_SWORD);
                output.accept(LOTRItems.TAURETHRIM_HELMET);
                output.accept(LOTRItems.TAURETHRIM_CHESTPLATE);
                output.accept(LOTRItems.TAURETHRIM_LEGGINGS);
                output.accept(LOTRItems.TAURETHRIM_BOOTS);
                output.accept(LOTRItems.TAURETHRIM_CHIEFTAIN_HELMET);
                output.accept(LOTRItems.UMBARIC_POLEAXE);
                output.accept(LOTRItems.URUK_PIKE);
                output.accept(LOTRItems.COAST_SOUTHRON_HORSE_ARMOR);
                output.accept(LOTRItems.MORDOR_WARSCYTHE);
                output.accept(LOTRItems.DOL_AMROTH_LANCE);
                output.accept(LOTRItems.LINDON_BATTLESTAFF);
                output.accept(LOTRItems.GALADHRIM_BATTLESTAFF);
                output.accept(LOTRItems.WOOD_ELVEN_BATTLESTAFF);
                output.accept(LOTRItems.TAURETHRIM_BLOWGUN);
                output.accept(LOTRItems.TAURETHRIM_DART);
                output.accept(LOTRItems.POISONED_TAURETHRIM_DART);
                output.accept(LOTRItems.BARROW_BLADE);
                output.accept(LOTRItems.POISONED_BARROW_BLADE);
                output.accept(LOTRItems.UMBARIC_MACE);
                output.accept(LOTRItems.GALADHRIM_CLOAK_HOOD);
                output.accept(LOTRItems.GALADHRIM_CLOAK_TUNIC);
                output.accept(LOTRItems.GALADHRIM_CLOAK_LEGGINGS);
                output.accept(LOTRItems.GALADHRIM_CLOAK_BOOTS);
                output.accept(LOTRItems.DWARVEN_BOAR_ARMOR);
                output.accept(LOTRItems.BLUE_DWARVEN_BOAR_ARMOR);
                output.accept(LOTRItems.HALF_TROLL_PIKE);
                output.accept(LOTRItems.IRON_PIKE);
                output.accept(LOTRItems.GOLDEN_TAURETHRIM_HELMET);
                output.accept(LOTRItems.GOLDEN_TAURETHRIM_CHESTPLATE);
                output.accept(LOTRItems.GOLDEN_TAURETHRIM_LEGGINGS);
                output.accept(LOTRItems.GOLDEN_TAURETHRIM_BOOTS);
                output.accept(LOTRItems.DWARVEN_PIKE);
                output.accept(LOTRItems.BLUE_DWARVEN_PIKE);
                output.accept(LOTRItems.DOL_AMROTH_DAGGER);
                output.accept(LOTRItems.POISONED_DOL_AMROTH_DAGGER);
                output.accept(LOTRItems.GUNDABAD_URUK_HELMET);
                output.accept(LOTRItems.GUNDABAD_URUK_CHESTPLATE);
                output.accept(LOTRItems.GUNDABAD_URUK_LEGGINGS);
                output.accept(LOTRItems.GUNDABAD_URUK_BOOTS);
                output.accept(LOTRItems.GONDOR_LANCE);
                output.accept(LOTRItems.GUNDABAD_URUK_CLEAVER);
                output.accept(LOTRItems.GUNDABAD_URUK_WARAXE);
                output.accept(LOTRItems.GUNDABAD_URUK_BLUDGEON);
                output.accept(LOTRItems.URUK_BERSERKER_HELMET);
                output.accept(LOTRItems.URUK_BERSERKER_CLEAVER);
                output.accept(LOTRItems.ROHIRRIC_LANCE);
                output.accept(LOTRItems.GALADHRIM_LONGSPEAR);
                output.accept(LOTRItems.LINDON_LONGSPEAR);
                output.accept(LOTRItems.WOOD_ELVEN_LONGSPEAR);
                output.accept(LOTRItems.MITHRIL_HALBERD);
                output.accept(LOTRItems.DALE_SWORD);
                output.accept(LOTRItems.DALE_DAGGER);
                output.accept(LOTRItems.POISONED_DALE_DAGGER);
                output.accept(LOTRItems.DALE_SPEAR);
                output.accept(LOTRItems.DALE_BATTLEAXE);
                output.accept(LOTRItems.DALE_HELMET);
                output.accept(LOTRItems.DALE_CHESTPLATE);
                output.accept(LOTRItems.DALE_LEGGINGS);
                output.accept(LOTRItems.DALE_BOOTS);
                output.accept(LOTRItems.DORWINION_HELMET);
                output.accept(LOTRItems.DORWINION_CHESTPLATE);
                output.accept(LOTRItems.DORWINION_LEGGINGS);
                output.accept(LOTRItems.DORWINION_BOOTS);
                output.accept(LOTRItems.DORWINION_ELVEN_HELMET);
                output.accept(LOTRItems.DORWINION_ELVEN_CHESTPLATE);
                output.accept(LOTRItems.DORWINION_ELVEN_LEGGINGS);
                output.accept(LOTRItems.DORWINION_ELVEN_BOOTS);
                output.accept(LOTRItems.BLADORTHIN_SPEAR);
                output.accept(LOTRItems.DALE_BOW);
                output.accept(LOTRItems.DALE_HORSE_ARMOR);
                output.accept(LOTRItems.ITHILIEN_RANGER_HOOD);
                output.accept(LOTRItems.ITHILIEN_RANGER_TUNIC);
                output.accept(LOTRItems.ITHILIEN_RANGER_LEGGINGS);
                output.accept(LOTRItems.ITHILIEN_RANGER_BOOTS);
                output.accept(LOTRItems.GUNDABAD_URUK_DAGGER);
                output.accept(LOTRItems.POISONED_GUNDABAD_URUK_DAGGER);
                output.accept(LOTRItems.GUNDABAD_URUK_SPEAR);
                output.accept(LOTRItems.GUNDABAD_URUK_PIKE);
                output.accept(LOTRItems.GUNDABAD_URUK_BOW);
                output.accept(LOTRItems.TAURETHRIM_BLUDGEON);
                output.accept(LOTRItems.DORWINION_ELVEN_SWORD);
                output.accept(LOTRItems.DORWINION_ELVEN_DAGGER);
                output.accept(LOTRItems.POISONED_DORWINION_ELVEN_DAGGER);
                output.accept(LOTRItems.TAURETHRIM_BATTLEAXE);
                output.accept(LOTRItems.TAURETHRIM_PIKE);
                output.accept(LOTRItems.MORWAITH_CLUB);
                output.accept(LOTRItems.DALE_PITCHFORK);
                output.accept(LOTRItems.ROLLING_PIN);
                output.accept(LOTRItems.ANGMAR_POLEAXE);
                output.accept(LOTRItems.DOL_GULDUR_SPIKE);
                output.accept(LOTRItems.UMBARIC_PIKE);
                output.accept(LOTRItems.LOSSARNACH_HELMET);
                output.accept(LOTRItems.LOSSARNACH_CHESTPLATE);
                output.accept(LOTRItems.LOSSARNACH_LEGGINGS);
                output.accept(LOTRItems.LOSSARNACH_BOOTS);
                output.accept(LOTRItems.PELARGIR_HELMET);
                output.accept(LOTRItems.PELARGIR_CHESTPLATE);
                output.accept(LOTRItems.PELARGIR_LEGGINGS);
                output.accept(LOTRItems.PELARGIR_BOOTS);
                output.accept(LOTRItems.PINNATH_GELIN_HELMET);
                output.accept(LOTRItems.PINNATH_GELIN_CHESTPLATE);
                output.accept(LOTRItems.PINNATH_GELIN_LEGGINGS);
                output.accept(LOTRItems.PINNATH_GELIN_BOOTS);
                output.accept(LOTRItems.LOSSARNACH_BATTLEAXE);
                output.accept(LOTRItems.LOSSARNACH_THROWING_AXE);
                output.accept(LOTRItems.PELARGIR_EKET);
                output.accept(LOTRItems.PELARGIR_TRIDENT);
                output.accept(LOTRItems.BLACKROOT_VALE_HELMET);
                output.accept(LOTRItems.BLACKROOT_VALE_CHESTPLATE);
                output.accept(LOTRItems.BLACKROOT_VALE_LEGGINGS);
                output.accept(LOTRItems.BLACKROOT_VALE_BOOTS);
                output.accept(LOTRItems.BLACKROOT_BOW);
                output.accept(LOTRItems.GONDOR_PIKE);
                output.accept(LOTRItems.DOL_AMROTH_GAMBESON);
                output.accept(LOTRItems.DOL_AMROTH_CHAPS);
                output.accept(LOTRItems.DOL_AMROTH_LONGSPEAR);
                output.accept(LOTRItems.GONDOR_GAMBESON);
                output.accept(LOTRItems.LEBENNIN_GAMBESON);
                output.accept(LOTRItems.STONE_SPEAR);
                output.accept(LOTRItems.LAMEDON_HELMET);
                output.accept(LOTRItems.LAMEDON_CHESTPLATE);
                output.accept(LOTRItems.LAMEDON_LEGGINGS);
                output.accept(LOTRItems.LAMEDON_BOOTS);
                output.accept(LOTRItems.LAMEDON_HORSE_ARMOR);
                output.accept(LOTRItems.LAMEDON_JACKET);
                output.accept(LOTRItems.DALE_GAMBESON);
                output.accept(LOTRItems.ARNOR_HELMET);
                output.accept(LOTRItems.ARNOR_CHESTPLATE);
                output.accept(LOTRItems.ARNOR_LEGGINGS);
                output.accept(LOTRItems.ARNOR_BOOTS);
                output.accept(LOTRItems.RANGER_BOW);
                output.accept(LOTRItems.RHUNIC_SWORD);
                output.accept(LOTRItems.RHUNIC_DAGGER);
                output.accept(LOTRItems.POISONED_RHUNIC_DAGGER);
                output.accept(LOTRItems.RHUNIC_SPEAR);
                output.accept(LOTRItems.RHUNIC_BARDICHE);
                output.accept(LOTRItems.RHUNIC_PIKE);
                output.accept(LOTRItems.RHUNIC_HELMET);
                output.accept(LOTRItems.RHUNIC_CHESTPLATE);
                output.accept(LOTRItems.RHUNIC_LEGGINGS);
                output.accept(LOTRItems.RHUNIC_BOOTS);
                output.accept(LOTRItems.RHUNIC_BOW);
                output.accept(LOTRItems.RHUNIC_HORSE_ARMOR);
                output.accept(LOTRItems.RHUNIC_FIRE_POT);
                output.accept(LOTRItems.GOLDEN_RHUNIC_HELMET);
                output.accept(LOTRItems.GOLDEN_RHUNIC_CHESTPLATE);
                output.accept(LOTRItems.GOLDEN_RHUNIC_LEGGINGS);
                output.accept(LOTRItems.GOLDEN_RHUNIC_BOOTS);
                output.accept(LOTRItems.RHUNIC_WARLORD_HELMET);
                output.accept(LOTRItems.DORWINION_ELVEN_BOW);
                output.accept(LOTRItems.RHUNIC_BATTLEAXE);
                output.accept(LOTRItems.RIVENDELL_SWORD);
                output.accept(LOTRItems.RIVENDELL_DAGGER);
                output.accept(LOTRItems.POISONED_RIVENDELL_DAGGER);
                output.accept(LOTRItems.RIVENDELL_SPEAR);
                output.accept(LOTRItems.RIVENDELL_HELMET);
                output.accept(LOTRItems.RIVENDELL_CHESTPLATE);
                output.accept(LOTRItems.RIVENDELL_LEGGINGS);
                output.accept(LOTRItems.RIVENDELL_BOOTS);
                output.accept(LOTRItems.RIVENDELL_HORSE_ARMOR);
                output.accept(LOTRItems.RIVENDELL_BATTLESTAFF);
                output.accept(LOTRItems.RIVENDELL_LONGSPEAR);
                output.accept(LOTRItems.ARNOR_SWORD);
                output.accept(LOTRItems.ARNOR_DAGGER);
                output.accept(LOTRItems.POISONED_ARNOR_DAGGER);
                output.accept(LOTRItems.ARNOR_SPEAR);
                output.accept(LOTRItems.RIVENDELL_BOW);
                output.accept(LOTRItems.POISONED_ARROW);
                output.accept(LOTRItems.POISONED_CROSSBOW_BOLT);
                output.accept(LOTRItems.MORWAITH_SWORD);
                output.accept(LOTRItems.GULFEN_HELMET);
                output.accept(LOTRItems.GULFEN_CHESTPLATE);
                output.accept(LOTRItems.GULFEN_LEGGINGS);
                output.accept(LOTRItems.GULFEN_BOOTS);
                output.accept(LOTRItems.CORSAIR_HELMET);
                output.accept(LOTRItems.CORSAIR_CHESTPLATE);
                output.accept(LOTRItems.CORSAIR_LEGGINGS);
                output.accept(LOTRItems.CORSAIR_BOOTS);
                output.accept(LOTRItems.CORSAIR_EKET);
                output.accept(LOTRItems.CORSAIR_DAGGER);
                output.accept(LOTRItems.POISONED_CORSAIR_DAGGER);
                output.accept(LOTRItems.CORSAIR_HARPOON);
                output.accept(LOTRItems.CORSAIR_BATTLEAXE);
                output.accept(LOTRItems.UMBARIC_HELMET);
                output.accept(LOTRItems.UMBARIC_CHESTPLATE);
                output.accept(LOTRItems.UMBARIC_LEGGINGS);
                output.accept(LOTRItems.UMBARIC_BOOTS);
                output.accept(LOTRItems.HARNENNOR_HELMET);
                output.accept(LOTRItems.HARNENNOR_CHESTPLATE);
                output.accept(LOTRItems.HARNENNOR_LEGGINGS);
                output.accept(LOTRItems.HARNENNOR_BOOTS);
                output.accept(LOTRItems.HARADRIC_SWORD);
                output.accept(LOTRItems.HARADRIC_DAGGER);
                output.accept(LOTRItems.POISONED_HARADRIC_DAGGER);
                output.accept(LOTRItems.HARADRIC_SPEAR);
                output.accept(LOTRItems.HARADRIC_PIKE);
                output.accept(LOTRItems.GULFEN_KHOPESH);
                output.accept(LOTRItems.NOMAD_CAP);
                output.accept(LOTRItems.NOMAD_TUNIC);
                output.accept(LOTRItems.NOMAD_LEGGINGS);
                output.accept(LOTRItems.NOMAD_SHOES);
                output.accept(LOTRItems.UMBARIC_HORSE_ARMOR);
                output.accept(LOTRItems.OLD_HARADRIC_SACRIFICIAL_DAGGER);
                output.accept(LOTRItems.BLACK_NUMENOREAN_HELMET);
                output.accept(LOTRItems.BLACK_NUMENOREAN_CHESTPLATE);
                output.accept(LOTRItems.BLACK_NUMENOREAN_LEGGINGS);
                output.accept(LOTRItems.BLACK_NUMENOREAN_BOOTS);
                output.accept(LOTRItems.BLACK_NUMENOREAN_SWORD);
                output.accept(LOTRItems.BLACK_NUMENOREAN_DAGGER);
                output.accept(LOTRItems.POISONED_BLACK_NUMENOREAN_DAGGER);
                output.accept(LOTRItems.BLACK_NUMENOREAN_SPEAR);
                output.accept(LOTRItems.BLACK_NUMENOREAN_MACE);
            })
            .build();

    // tabStory: the seven named weapons of the tale, the only things
    // LOTRStoryItem marked. In LOTRMod's registration order, as everywhere else
    // here. NOT in the port: the elven blades' glow -- LOTRItemSword swapped in
    // a _glowing icon when orcs were near, and there are no orcs yet, so Sting,
    // Glamdring and Ringil stay unlit like every other elven blade here.
    public static final CreativeModeTab STORY = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabStory.theIcon = anduril.
            .icon(() -> new ItemStack(LOTRItems.ANDURIL))
            .title(Component.translatable("creativeTab.lotr.story"))
            .displayItems((params, output) -> {
                output.accept(LOTRItems.STING);
                output.accept(LOTRItems.SAURON_MACE);
                output.accept(LOTRItems.GANDALF_STAFF_WHITE);
                output.accept(LOTRItems.ANDURIL);
                output.accept(LOTRItems.RINGIL);
                output.accept(LOTRItems.GANDALF_STAFF_GREY);
                output.accept(LOTRItems.GLAMDRING);
            })
            .build();

    public static final CreativeModeTab EVERYTHING = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.MITHRIL_BLOCK))
            .title(Component.translatable("creativeTab.lotr.zz_everything"))
            .displayItems((params, output) -> {
                Set<Block> hidden = hiddenEverywhere();
                LOTRBlocks.ALL_BLOCKS.forEach(b -> {
                    if (!hidden.contains(b)) {
                        output.accept(b);
                    }
                });
                output.accept(LOTRItems.PIPEWEED);
            })
            .build();

    private LOTRCreativeTabs() {
    }

    // Called from mod init. Must run AFTER blocks and items are registered. */
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS_KEY, BLOCKS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UTILITIES_KEY, UTILITIES);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DECORATIONS_KEY, DECORATIONS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FOOD_KEY, FOOD);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MATERIALS_KEY, MATERIALS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MISC_KEY, MISC);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TOOLS_KEY, TOOLS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, COMBAT_KEY, COMBAT);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, STORY_KEY, STORY);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, EVERYTHING_KEY, EVERYTHING);
    }
}