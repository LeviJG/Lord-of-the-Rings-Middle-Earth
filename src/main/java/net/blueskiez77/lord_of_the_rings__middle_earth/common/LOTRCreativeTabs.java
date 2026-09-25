package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTREntDraughtItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;

// Creative inventory tabs. The 1.7.10 mod had ten tabs (LOTRCreativeTabs: blocks, util, decorations, food, materials, misc, tools, combat, story, spawning). This ports the first of them -- tabBlock, shown as "Middle-earth Blocks" -- faithfully: it holds building materials only, and nothing else. Membership is derived from the original by reading which block classes called setCreativeTab(LOTRCreativeTabs.tabBlock). Forty classes did, and they map onto the family lists in LOTRBlocks: rock and brick cubes, ores and storage blocks, planks, logs, beams, pillars, slabs, stairs, walls, glass, smooth stone, gravels/sands, soils and paths. Deliberately NOT here, because the original put them in other tabs: fences, leaves, saplings, flowers, vines, ladders, torches, chandeliers, panes and treasure piles (tabDeco, ported below); fence gates, doors, crafting tables, chests and beacons (tabUtil); buttons and pressure plates (tabMisc); kebabs, barrels and marzipan (tabFood).
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
    public static final ResourceKey<CreativeModeTab> COMBAT_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "08_combat"));

    public static final ResourceKey<CreativeModeTab> STORY_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "09_story"));

    // Blocks that sit in ALL_CUBES for registration convenience but belonged to a different tab in 1.7.10, so they must not appear in this one. The Utumno return portal pieces are a third case: the original never called setCreativeTab on them at all, so they were creative-unobtainable by design.
    // Blocks kept out of BOTH tabs. The bone block duplicates vanilla's, so it
    // stays registered (recipes and its stairs, slab and wall reference it) but
    // is not offered in creative -- take vanilla's instead.
    private static Set<Block> hiddenEverywhere() {
        return new LinkedHashSet<>(List.of(LOTRBuildingBlocks.BONE_BLOCK));
    }

    private static Set<Block> cubesFromOtherTabs() {
        return new LinkedHashSet<>(List.of(
                LOTRFoodBlocks.KEBAB_BLOCK,
                LOTRDecorationBlocks.TREASURE_COPPER,
                LOTRDecorationBlocks.TREASURE_GOLD,
                LOTRDecorationBlocks.TREASURE_SILVER,
                LOTRUtilityBlocks.UTUMNO_RETURN_PORTAL_BASE,
                LOTRUtilityBlocks.UTUMNO_RETURN_LIGHT));
    }

    public static final CreativeModeTab BLOCKS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBuildingBlocks.HIGH_ELVEN_BRICK))
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
            .icon(() -> new ItemStack(LOTRUtilityBlocks.DWARVEN_FORGE))
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
                output.accept(LOTRUtilityBlocks.HOBBIT_OVEN);
                output.accept(LOTRUtilityBlocks.UNSMELTERY);
                // LOTRBlockMillstone also called setCreativeTab(tabUtil).
                output.accept(LOTRUtilityBlocks.MILLSTONE);
                // The LOTR anvil, which opens LOTRContainerAnvil.
                output.accept(LOTRUtilityBlocks.ANVIL);
                LOTRBlocks.ALL_KEBAB_STANDS.forEach(output::accept);

                // --- Storage --------------------------------------------
                LOTRBlocks.ALL_CHESTS.forEach(output::accept);

                // --- Furnishing -----------------------------------------
                // The beds reach tabUtil through their item rather than their
                // block: LOTRItemBed's constructor is what sets the tab.
                LOTRBlocks.ALL_BEDS.forEach(output::accept);
                // LOTRBlockOrcChain, likewise tabUtil -- a fixture you hang
                // from a ceiling, and what chandeliers hang from.
                output.accept(LOTRUtilityBlocks.ORC_CHAIN);

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
                output.accept(LOTRUtilityBlocks.BEACON_OF_GONDOR);
                output.accept(LOTRUtilityBlocks.TABLE_OF_COMMAND);
                output.accept(LOTRUtilityBlocks.ENT_JAR);
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
    // Not here: the four rugs (wargskin, bearskin, lionskin, giraffeskin),
    // which place entities the port does not have yet (D4), and
    // LOTRItemArmorStand, left out as a duplicate of vanilla's.
    //
    // As with UTILITIES the ORDER is ours, not the original's -- 1.7.10 simply
    // listed a tab's blocks in registration order. Grouped here by what a thing
    // is for: things that grow, then things that climb or screen, then light,
    // then floor coverings, then the treasure piles.
    public static final CreativeModeTab DECORATIONS = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabDeco.theIcon = simbelmyne.
            .icon(() -> new ItemStack(LOTRDecorationBlocks.SIMBELMYNE))
            .title(Component.translatable("creativeTab.lotr.decorations"))
            .displayItems((params, output) -> {
                // --- Foliage --------------------------------------------
                LOTRBlocks.ALL_LEAVES.forEach(output::accept);
                LOTRBlocks.ALL_SAPLINGS.forEach(output::accept);
                // LOTRBlockFallenLeaves.getSubBlocks: vanilla's leaves, then the mod's.
                LOTRBlocks.ALL_FALLEN_LEAVES.forEach(output::accept);
                // registerFlower backs LOTRBlockFlower and the several other
                // 1.7.10 classes that were all "a cross-shaped plant": the tall
                // grasses, corn stalk, grapevine, reeds, Fangorn riverweed,
                // Mordor grass and corrupt mallorn. Every one was tabDeco.
                // All but the morgul-shroom, which LOTRBlockMorgulShroom put in tabFood.
                LOTRBlocks.ALL_FLOWERS.forEach(block -> {
                    if (block != LOTRFoodBlocks.MORGUL_SHROOM) {
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
                output.accept(LOTRDecorationBlocks.WEB_UNGOLIANT);

                // --- Stalactites -----------------------------------------
                // LOTRBlockStalactite.getSubBlocks: stalactite, stalagmite.
                LOTRBlocks.ALL_STALACTITES.forEach(output::accept);

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
                output.accept(LOTRDecorationBlocks.WEAPON_RACK);

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
            .icon(() -> new ItemStack(LOTRFoodItems.LEMBAS))
            .title(Component.translatable("creativeTab.lotr.food"))
            .displayItems((params, output) -> {
                output.accept(LOTRFoodBlocks.BARREL);
                output.accept(LOTRFoodBlocks.MORGUL_SHROOM);
                output.accept(LOTRFoodBlocks.MARCHPANE_BLOCK);
                output.accept(LOTRFoodBlocks.KEBAB_BLOCK);
                output.accept(LOTRFoodItems.CLAY_MUG);
                output.accept(LOTRFoodItems.MUG);
                output.accept(LOTRFoodItems.WATER);
                output.accept(LOTRFoodItems.MILK);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.ALE, strength));
                }
                output.accept(LOTRFoodItems.CHOCOLATE);
                output.accept(LOTRFoodBlocks.APPLE_CRUMBLE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.MIRUVOR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.ORC_DRAUGHT, strength));
                }
                output.accept(LOTRFoodItems.LEMBAS);
                output.accept(LOTRFoodItems.LETTUCE);
                output.accept(LOTRFoodItems.GAMMON);
                output.accept(LOTRFoodItems.CLAY_PLATE);
                output.accept(LOTRFoodBlocks.FINE_PLATE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.MEAD, strength));
                }
                output.accept(LOTRFoodItems.GREEN_APPLE);
                output.accept(LOTRFoodItems.PEAR);
                output.accept(LOTRFoodItems.CHERRIES);
                output.accept(LOTRFoodBlocks.CHERRY_PIE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.RED_WINE, strength));
                }
                output.accept(LOTRFoodItems.MALLORN_NUT);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.CIDER, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.PERRY, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.CHERRY_LIQUEUR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.RUM, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.ATHELAS_BREW, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.DWARVEN_TONIC, strength));
                }
                for (int draught = 0; draught < LOTREntDraughtItem.COUNT; draught++) {
                    output.accept(LOTREntDraughtItem.stack(LOTRFoodItems.ENT_DRAUGHT, draught));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.DWARVEN_ALE, strength));
                }
                output.accept(LOTRFoodItems.MAGGOTY_BREAD);
                output.accept(LOTRFoodItems.RABBIT_STEW);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.VODKA, strength));
                }
                output.accept(LOTRFoodItems.HOBBIT_PANCAKE);
                output.accept(LOTRFoodItems.MANGO);
                output.accept(LOTRFoodItems.MANGO_JUICE);
                output.accept(LOTRFoodItems.BANANA);
                output.accept(LOTRFoodItems.BANANA_BREAD);
                output.accept(LOTRFoodBlocks.BANANA_CAKE);
                output.accept(LOTRFoodItems.RAW_LION);
                output.accept(LOTRFoodItems.COOKED_LION);
                output.accept(LOTRFoodItems.RAW_ZEBRA);
                output.accept(LOTRFoodItems.COOKED_ZEBRA);
                output.accept(LOTRFoodItems.RAW_RHINO);
                output.accept(LOTRFoodItems.COOKED_RHINO);
                output.accept(LOTRFoodItems.MAPLE_SYRUP);
                output.accept(LOTRFoodItems.HOBBIT_PANCAKE_WITH_MAPLE_SYRUP);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.MAPLE_BEER, strength));
                }
                output.accept(LOTRFoodItems.DATE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.ARAK, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.CARROT_WINE, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.BANANA_BEER, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.MELON_LIQUEUR, strength));
                }
                output.accept(LOTRFoodBlocks.DALISH_PASTRY);
                output.accept(LOTRFoodItems.BLUEBERRIES);
                output.accept(LOTRFoodItems.BLACKBERRIES);
                output.accept(LOTRFoodItems.RASPBERRIES);
                output.accept(LOTRFoodItems.CRANBERRIES);
                output.accept(LOTRFoodItems.ELDERBERRIES);
                output.accept(LOTRFoodItems.ROAST_CHESTNUT);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.CACTUS_LIQUEUR, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.TOROG_DRAUGHT, strength));
                }
                output.accept(LOTRFoodBlocks.BERRY_PIE);
                output.accept(LOTRFoodItems.BLUEBERRY_JUICE);
                output.accept(LOTRFoodItems.BLACKBERRY_JUICE);
                output.accept(LOTRFoodItems.RASPBERRY_JUICE);
                output.accept(LOTRFoodItems.CRANBERRY_JUICE);
                output.accept(LOTRFoodItems.ELDERBERRY_JUICE);
                output.accept(LOTRFoodItems.TOROG_STEW);
                output.accept(LOTRFoodItems.CRAM);
                output.accept(LOTRFoodItems.LEMON);
                output.accept(LOTRFoodBlocks.LEMON_CAKE);
                output.accept(LOTRFoodItems.ORANGE);
                output.accept(LOTRFoodItems.ORANGE_JUICE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.LEMON_LIQUEUR, strength));
                }
                output.accept(LOTRFoodItems.LEMONADE);
                output.accept(LOTRFoodItems.LIME);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.LIME_LIQUEUR, strength));
                }
                output.accept(LOTRFoodItems.RAW_MUTTON);
                output.accept(LOTRFoodItems.COOKED_MUTTON);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.TAURETHRIM_COCOA, strength));
                }
                output.accept(LOTRFoodItems.JUNGLE_REMEDY);
                output.accept(LOTRFoodItems.CORN);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.CORN_LIQUOR, strength));
                }
                output.accept(LOTRFoodItems.RAW_VENISON);
                output.accept(LOTRFoodItems.COOKED_VENISON);
                output.accept(LOTRItems.KEBAB);
                output.accept(LOTRFoodItems.COOKED_CORN);
                output.accept(LOTRFoodItems.SHISH_KEBAB);
                output.accept(LOTRFoodItems.LEEK);
                output.accept(LOTRFoodItems.LEEK_SOUP);
                output.accept(LOTRFoodItems.TURNIP);
                output.accept(LOTRFoodItems.RAW_CAMEL);
                output.accept(LOTRFoodItems.COOKED_CAMEL);
                output.accept(LOTRFoodItems.OLIVES);
                output.accept(LOTRFoodItems.APPLE_JUICE);
                output.accept(LOTRFoodItems.OLIVE_BREAD);
                output.accept(LOTRFoodItems.RED_GRAPES);
                output.accept(LOTRFoodItems.GREEN_GRAPES);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.WHITE_WINE, strength));
                }
                output.accept(LOTRFoodItems.RED_GRAPE_JUICE);
                output.accept(LOTRFoodItems.GREEN_GRAPE_JUICE);
                output.accept(LOTRFoodItems.ROAST_TURNIP);
                output.accept(LOTRFoodItems.MELON_SOUP);
                output.accept(LOTRFoodItems.CERAMIC_MUG);
                output.accept(LOTRFoodItems.ALMOND);
                output.accept(LOTRFoodItems.WILDBERRIES);
                output.accept(LOTRFoodItems.PLUM);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.MORGUL_DRAUGHT, strength));
                }
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.PLUM_KVASS, strength));
                }
                output.accept(LOTRFoodItems.MARCHPANE);
                output.accept(LOTRFoodItems.CHOCOLATE_MARCHPANE);
                output.accept(LOTRFoodItems.GOLDEN_GOBLET);
                output.accept(LOTRFoodItems.SILVER_GOBLET);
                output.accept(LOTRFoodItems.COPPER_GOBLET);
                output.accept(LOTRFoodItems.WOODEN_CUP);
                output.accept(LOTRFoodItems.SKULL_CUP);
                output.accept(LOTRFoodItems.WINE_GLASS);
                output.accept(LOTRFoodItems.WATERSKIN);
                output.accept(LOTRFoodItems.ALE_HORN);
                output.accept(LOTRFoodItems.GOLDEN_ALE_HORN);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.TERMITE_TEQUILA, strength));
                }
                output.accept(LOTRFoodItems.YAM);
                output.accept(LOTRFoodItems.ROAST_YAM);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.SOURED_MILK, strength));
                }
                output.accept(LOTRFoodItems.POMEGRANATE);
                output.accept(LOTRFoodItems.POMEGRANATE_JUICE);
                for (int strength = 0; strength < 5; strength++) {
                    output.accept(LOTRDrinkItem.stack(LOTRFoodItems.POMEGRANATE_WINE, strength));
                }
                output.accept(LOTRFoodItems.MAN_FLESH);
                output.accept(LOTRFoodItems.SALT);
                output.accept(LOTRFoodItems.SUSPICIOUS_MEAT);
                output.accept(LOTRFoodItems.CORN_BREAD);
                output.accept(LOTRFoodItems.RAISINS);
                output.accept(LOTRFoodItems.MUSHROOM_PIE);
                output.accept(LOTRFoodBlocks.WOODEN_PLATE);
                output.accept(LOTRFoodBlocks.STONEWARE_PLATE);
            })
            .build();

    // tabMaterials: the items that named it, in registerItem order, with the
    // mithril ingot as its icon (setupIcons). NOT here: the copper ingot and iron
    // nugget, which vanilla's stand in for.
    public static final CreativeModeTab MATERIALS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRItems.MITHRIL))
            .title(Component.translatable("creativeTab.lotr.materials"))
            .displayItems((params, output) -> {
                output.accept(LOTRMaterialItems.TIN_INGOT);
                output.accept(LOTRMaterialItems.BRONZE_INGOT);
                output.accept(LOTRMaterialItems.SILVER_INGOT);
                output.accept(LOTRItems.MITHRIL);
                output.accept(LOTRMaterialItems.SILVER_NUGGET);
                output.accept(LOTRMaterialItems.MITHRIL_NUGGET);
                output.accept(LOTRMaterialItems.ORC_STEEL_INGOT);
                output.accept(LOTRMaterialItems.DURNOR);
                output.accept(LOTRMaterialItems.PIPEWEED_LEAF);
                output.accept(LOTRMaterialItems.PIPEWEED_SEEDS);
                output.accept(LOTRMaterialItems.YELLOW_DYE);
                output.accept(LOTRMaterialItems.WHITE_DYE);
                output.accept(LOTRMaterialItems.BLUEBELL_BLUE);
                output.accept(LOTRMaterialItems.CLOVER_GREEN);
                output.accept(LOTRMaterialItems.CHARCOAL_DUST);
                output.accept(LOTRMaterialItems.BROWN_DYE);
                output.accept(LOTRMaterialItems.MALLORN_STICK);
                output.accept(LOTRMaterialItems.FUR);
                output.accept(LOTRMaterialItems.EDHELVIR);
                output.accept(LOTRMaterialItems.WARG_BONE);
                output.accept(LOTRMaterialItems.DWARVEN_STEEL_INGOT);
                output.accept(LOTRMaterialItems.GALVORN_INGOT);
                output.accept(LOTRMaterialItems.ORC_BONE);
                output.accept(LOTRMaterialItems.ELF_BONE);
                output.accept(LOTRMaterialItems.DWARF_BONE);
                output.accept(LOTRMaterialItems.HOBBIT_BONE);
                output.accept(LOTRMaterialItems.URUK_STEEL_INGOT);
                output.accept(LOTRMaterialItems.TROLL_BONE);
                output.accept(LOTRMaterialItems.GULDURIL);
                output.accept(LOTRMaterialItems.MORGUL_STEEL_INGOT);
                output.accept(LOTRMaterialItems.SULFUR);
                output.accept(LOTRMaterialItems.NITER);
                output.accept(LOTRMaterialItems.LION_FUR);
                output.accept(LOTRMaterialItems.RHINO_HORN);
                output.accept(LOTRMaterialItems.GEMSBOK_HIDE);
                output.accept(LOTRMaterialItems.GEMSBOK_HORN);
                output.accept(LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT);
                output.accept(LOTRMaterialItems.FLAX_SEEDS);
                output.accept(LOTRMaterialItems.FLAX);
                output.accept(LOTRMaterialItems.BLACK_URUK_STEEL_INGOT);
                output.accept(LOTRMaterialItems.ELVEN_STEEL_INGOT);
                output.accept(LOTRMaterialItems.SWAN_FEATHER);
                output.accept(LOTRMaterialItems.OBSIDIAN_SHARD);
                output.accept(LOTRMaterialItems.HITHLAIN);
                output.accept(LOTRMaterialItems.GATE_GEARS);
                output.accept(LOTRMaterialItems.RED_GRAPE_SEEDS);
                output.accept(LOTRMaterialItems.GREEN_GRAPE_SEEDS);
                output.accept(LOTRMaterialItems.KINE_OF_ARAW_HORN);
                output.accept(LOTRMaterialItems.BLACKROOT_STICK);
                output.accept(LOTRMaterialItems.HORN);
                output.accept(LOTRMaterialItems.ITHILDIN);
                output.accept(LOTRMaterialItems.GILDED_IRON_INGOT);
                output.accept(LOTRMaterialItems.FLAME_OF_UDUN);
                output.accept(LOTRMaterialItems.TOPAZ);
                output.accept(LOTRMaterialItems.AMETHYST);
                output.accept(LOTRMaterialItems.SAPPHIRE);
                output.accept(LOTRMaterialItems.RUBY);
                output.accept(LOTRMaterialItems.AMBER);
                output.accept(LOTRMaterialItems.DIAMOND);
                output.accept(LOTRMaterialItems.PEARL);
                output.accept(LOTRMaterialItems.CORAL);
                output.accept(LOTRMaterialItems.OPAL);
                output.accept(LOTRMaterialItems.EMERALD);
                output.accept(LOTRMaterialItems.CHILL_OF_DAEDELOS);
                output.accept(LOTRMaterialItems.HEADHUNTERS_TROPHY);
                // One scroll per modifier it could teach -- hasTemplateItem was
                // "weight above zero and beneficial".
                for (LOTRModifier modifier
                        : LOTRModifier.values()) {
                    if (modifier.weight() > 0 && modifier.isBeneficial()) {
                        output.accept(LOTRSmithsScrollItem.of(modifier));
                    }
                }
                output.accept(LOTRMaterialItems.MITHRIL_MAIL);
                output.accept(LOTRMaterialItems.BOOK_OF_TRUE_SILVER);
                output.accept(LOTRMaterialItems.RED_CLAY_BALL);
            })
            .build();

    // tabTools: the items that named it, in registerItem order, as tabCombat is.
    // NOT here yet: the branding iron, which brands NPCs -- the port has none.
    // tabMisc: the odds and ends -- rings, coins, keys, curios -- plus the
    // stone buttons and pressure plates, which were tabMisc blocks rather than
    // tabBlock ones. Item order is LOTRMod's registration order.
    public static final CreativeModeTab MISC = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabMisc.theIcon = hobbitPipe.
            .icon(() -> new ItemStack(LOTRMiscItems.SMOKING_PIPE))
            .title(Component.translatable("creativeTab.lotr.misc"))
            .displayItems((params, output) -> {
                output.accept(LOTRMiscItems.GOLD_RING);
                output.accept(LOTRMiscItems.SILVER_RING);
                output.accept(LOTRMiscItems.MITHRIL_RING);
                // getSubItems listed the pipe once per smoke colour.
                for (int smokeColour = 0; smokeColour < LOTRSmokingPipeItem.COLORS; smokeColour++) {
                    output.accept(LOTRSmokingPipeItem.of(smokeColour));
                }
                output.accept(LOTRMiscItems.SILVER_COIN);
                output.accept(LOTRMiscItems.SILVER_COIN_STACK);
                output.accept(LOTRMiscItems.SILVER_COIN_PILE);
                output.accept(LOTRMiscItems.HOBBIT_MARRIAGE_RING);
                output.accept(LOTRMiscItems.ANCIENT_SWORD_TIP);
                output.accept(LOTRMiscItems.ANCIENT_SWORD_BLADE);
                output.accept(LOTRMiscItems.ANCIENT_SWORD_HILT);
                output.accept(LOTRMiscItems.ANCIENT_ARMOR_PLATE);
                output.accept(LOTRMiscItems.ANCIENT_SWORD);
                output.accept(LOTRMiscItems.ANCIENT_DAGGER);
                output.accept(LOTRMiscItems.ANCIENT_HELMET);
                output.accept(LOTRMiscItems.ANCIENT_CHESTPLATE);
                output.accept(LOTRMiscItems.ANCIENT_LEGGINGS);
                output.accept(LOTRMiscItems.ANCIENT_BOOTS);
                output.accept(LOTRMiscItems.DWARVEN_MARRIAGE_RING);
                output.accept(LOTRMiscItems.RED_BOOK);
                output.accept(LOTRMiscItems.KEY_OF_ICE);
                output.accept(LOTRMiscItems.KEY_OF_OBSIDIAN);
                output.accept(LOTRMiscItems.ICE_KEY_HANDLE);
                output.accept(LOTRMiscItems.ICE_KEY_SHAFT);
                output.accept(LOTRMiscItems.ICE_KEY_PIN);
                output.accept(LOTRMiscItems.OBSIDIAN_KEY_HANDLE);
                output.accept(LOTRMiscItems.OBSIDIAN_KEY_SHAFT);
                output.accept(LOTRMiscItems.OBSIDIAN_KEY_PIN);
                output.accept(LOTRMiscItems.TAURETHRIM_AMULET);
                // getSubItems: each colour sealed, then unsealed.
                for (net.minecraft.world.item.Item cracker : java.util.List.of(LOTRMiscItems.RED_DALISH_CRACKER,
                        LOTRMiscItems.BLUE_DALISH_CRACKER, LOTRMiscItems.GREEN_DALISH_CRACKER,
                        LOTRMiscItems.SILVER_DALISH_CRACKER, LOTRMiscItems.GOLD_DALISH_CRACKER)) {
                    output.accept(cracker);
                    ItemStack unsealed = new ItemStack(cracker);
                    unsealed.set(LOTRDataComponents.CRACKER_EMPTY, true);
                    output.accept(unsealed);
                }
                output.accept(LOTRMiscItems.MYSTERY_WEB);
                output.accept(LOTRMiscItems.EXPLODING_TERMITE);
                output.accept(LOTRMiscItems.CONKER);
                output.accept(LOTRMiscItems.LEATHER_HAT);
                output.accept(LOTRMiscItems.PARTY_HAT);
                output.accept(LOTRMiscItems.HARAD_TURBAN);
                output.accept(LOTRMiscItems.HARAD_ROBE);
                output.accept(LOTRMiscItems.HARAD_ROBE_LEGGINGS);
                output.accept(LOTRMiscItems.HARAD_ROBE_SHOES);
                output.accept(LOTRMiscItems.KAFTAN);
                output.accept(LOTRMiscItems.KAFTAN_LEGGINGS);
                output.accept(LOTRMiscItems.BOTTLE_OF_POISON);
                output.accept(LOTRMiscItems.MECHANISM);
                // LOTRMod tabMisc'd the pipeweed at its registration site.
                output.accept(LOTRItems.PIPEWEED);
                LOTRBlocks.ALL_BUTTONS.forEach(output::accept);
                LOTRBlocks.ALL_PRESSURE_PLATES.forEach(output::accept);
            })
            .build();

    public static final CreativeModeTab TOOLS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRToolItems.MORDOR_PICKAXE))
            .title(Component.translatable("creativeTab.lotr.tools"))
            .displayItems((params, output) -> {
                output.accept(LOTRToolItems.BRONZE_SHOVEL);
                output.accept(LOTRToolItems.BRONZE_PICKAXE);
                output.accept(LOTRToolItems.BRONZE_AXE);
                output.accept(LOTRToolItems.BRONZE_HOE);
                output.accept(LOTRToolItems.MITHRIL_SHOVEL);
                output.accept(LOTRToolItems.MITHRIL_PICKAXE);
                output.accept(LOTRToolItems.MITHRIL_AXE);
                output.accept(LOTRToolItems.MITHRIL_HOE);
                output.accept(LOTRToolItems.MALLORN_SHOVEL);
                output.accept(LOTRToolItems.MALLORN_PICKAXE);
                output.accept(LOTRToolItems.MALLORN_AXE);
                output.accept(LOTRToolItems.MALLORN_HOE);
                output.accept(LOTRToolItems.GALADHRIM_SHOVEL);
                output.accept(LOTRToolItems.GALADHRIM_PICKAXE);
                output.accept(LOTRToolItems.GALADHRIM_AXE);
                output.accept(LOTRToolItems.GALADHRIM_HOE);
                output.accept(LOTRToolItems.DWARVEN_SHOVEL);
                output.accept(LOTRToolItems.DWARVEN_PICKAXE);
                output.accept(LOTRToolItems.DWARVEN_AXE);
                output.accept(LOTRToolItems.DWARVEN_HOE);
                output.accept(LOTRToolItems.MORDOR_SHOVEL);
                output.accept(LOTRToolItems.MORDOR_PICKAXE);
                output.accept(LOTRToolItems.MORDOR_AXE);
                output.accept(LOTRToolItems.MORDOR_HOE);
                output.accept(LOTRToolItems.URUK_SHOVEL);
                output.accept(LOTRToolItems.URUK_PICKAXE);
                output.accept(LOTRToolItems.URUK_AXE);
                output.accept(LOTRToolItems.URUK_HOE);
                output.accept(LOTRToolItems.DWARVEN_MATTOCK);
                output.accept(LOTRToolItems.WOOD_ELVEN_SHOVEL);
                output.accept(LOTRToolItems.WOOD_ELVEN_PICKAXE);
                output.accept(LOTRToolItems.WOOD_ELVEN_AXE);
                output.accept(LOTRToolItems.WOOD_ELVEN_HOE);
                output.accept(LOTRToolItems.SULFUR_MATCH);
                output.accept(LOTRToolItems.ANGMAR_SHOVEL);
                output.accept(LOTRToolItems.ANGMAR_PICKAXE);
                output.accept(LOTRToolItems.ANGMAR_AXE);
                output.accept(LOTRToolItems.ANGMAR_HOE);
                output.accept(LOTRToolItems.LINDON_SHOVEL);
                output.accept(LOTRToolItems.LINDON_PICKAXE);
                output.accept(LOTRToolItems.LINDON_AXE);
                output.accept(LOTRToolItems.LINDON_HOE);
                output.accept(LOTRToolItems.BLUE_DWARVEN_SHOVEL);
                output.accept(LOTRToolItems.BLUE_DWARVEN_PICKAXE);
                output.accept(LOTRToolItems.BLUE_DWARVEN_AXE);
                output.accept(LOTRToolItems.BLUE_DWARVEN_HOE);
                output.accept(LOTRToolItems.BLUE_DWARVEN_MATTOCK);
                output.accept(LOTRToolItems.DOL_GULDUR_SHOVEL);
                output.accept(LOTRToolItems.DOL_GULDUR_AXE);
                output.accept(LOTRToolItems.DOL_GULDUR_PICKAXE);
                output.accept(LOTRToolItems.DOL_GULDUR_HOE);
                output.accept(LOTRToolItems.UTUMNO_PICKAXE);
                output.accept(LOTRToolItems.TAURETHRIM_SHOVEL);
                output.accept(LOTRToolItems.TAURETHRIM_PICKAXE);
                output.accept(LOTRToolItems.TAURETHRIM_AXE);
                output.accept(LOTRToolItems.TAURETHRIM_HOE);
                output.accept(LOTRToolItems.CHISEL);
                output.accept(LOTRToolItems.MOON_CHISEL);
                output.accept(LOTRToolItems.MITHRIL_MATTOCK);
                output.accept(LOTRToolItems.RIVENDELL_SHOVEL);
                output.accept(LOTRToolItems.RIVENDELL_PICKAXE);
                output.accept(LOTRToolItems.RIVENDELL_AXE);
                output.accept(LOTRToolItems.RIVENDELL_HOE);
            })
            .build();

    public static final CreativeModeTab COMBAT = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRCombatItems.GONDOR_HELMET))
            .title(Component.translatable("creativeTab.lotr.combat"))
            .displayItems((params, output) -> {
                // LOTRCreativeTabs had no grouping of its own: a tab was the
                // items that named it, in registration order -- and blocks are
                // registered before items, which is why the bombs come first.
                // This is that order, minus the tools, which sit in the tools
                // tab (TOOLS, above).
                LOTRBlocks.ALL_ORC_BOMBS.forEach(output::accept);
                // registerBlock(rhunFireJar) came after the bombs.
                output.accept(LOTRCombatBlocks.KHAMULS_FIRE_JAR);

                output.accept(LOTRCombatItems.BRONZE_SWORD);
                output.accept(LOTRCombatItems.BRONZE_HELMET);
                output.accept(LOTRCombatItems.BRONZE_CHESTPLATE);
                output.accept(LOTRCombatItems.BRONZE_LEGGINGS);
                output.accept(LOTRCombatItems.BRONZE_BOOTS);
                output.accept(LOTRCombatItems.MORDOR_SCIMITAR);
                output.accept(LOTRCombatItems.MORDOR_HELMET);
                output.accept(LOTRCombatItems.MORDOR_CHESTPLATE);
                output.accept(LOTRCombatItems.MORDOR_LEGGINGS);
                output.accept(LOTRCombatItems.MORDOR_BOOTS);
                output.accept(LOTRCombatItems.MORDOR_BATTLEAXE);
                output.accept(LOTRCombatItems.MORDOR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_MORDOR_DAGGER);
                output.accept(LOTRCombatItems.MITHRIL_SWORD);
                output.accept(LOTRCombatItems.GONDOR_SWORD);
                output.accept(LOTRCombatItems.GONDOR_HELMET);
                output.accept(LOTRCombatItems.GONDOR_CHESTPLATE);
                output.accept(LOTRCombatItems.GONDOR_LEGGINGS);
                output.accept(LOTRCombatItems.GONDOR_BOOTS);
                output.accept(LOTRCombatItems.MITHRIL_HELMET);
                output.accept(LOTRCombatItems.MITHRIL_CHESTPLATE);
                output.accept(LOTRCombatItems.MITHRIL_LEGGINGS);
                output.accept(LOTRCombatItems.MITHRIL_BOOTS);
                output.accept(LOTRCombatItems.GONDOR_SPEAR);
                output.accept(LOTRCombatItems.MORDOR_SPEAR);
                output.accept(LOTRCombatItems.BRONZE_SPEAR);
                output.accept(LOTRCombatItems.IRON_SPEAR);
                output.accept(LOTRCombatItems.MITHRIL_SPEAR);
                output.accept(LOTRCombatItems.MALLORN_SWORD);
                output.accept(LOTRCombatItems.GALADHRIM_SWORD);
                output.accept(LOTRCombatItems.GALADHRIM_SPEAR);
                output.accept(LOTRCombatItems.MALLORN_BOW);
                output.accept(LOTRCombatItems.GALADHRIM_HELMET);
                output.accept(LOTRCombatItems.GALADHRIM_CHESTPLATE);
                output.accept(LOTRCombatItems.GALADHRIM_LEGGINGS);
                output.accept(LOTRCombatItems.GALADHRIM_BOOTS);
                output.accept(LOTRCombatItems.GALADHRIM_BOW);
                output.accept(LOTRCombatItems.FUR_HAT);
                output.accept(LOTRCombatItems.FUR_TUNIC);
                output.accept(LOTRCombatItems.FUR_LEGGINGS);
                output.accept(LOTRCombatItems.FUR_BOOTS);
                output.accept(LOTRCombatItems.ORC_BOW);
                output.accept(LOTRCombatItems.BLACKSMITH_HAMMER);
                output.accept(LOTRCombatItems.GONDOR_DAGGER);
                output.accept(LOTRCombatItems.GALADHRIM_DAGGER);
                output.accept(LOTRCombatItems.DWARVEN_SWORD);
                output.accept(LOTRCombatItems.DWARVEN_DAGGER);
                output.accept(LOTRCombatItems.DWARVEN_BATTLEAXE);
                output.accept(LOTRCombatItems.DWARVEN_WARHAMMER);
                output.accept(LOTRCombatItems.MORDOR_WARHAMMER);
                output.accept(LOTRCombatItems.DWARVEN_HELMET);
                output.accept(LOTRCombatItems.DWARVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.DWARVEN_LEGGINGS);
                output.accept(LOTRCombatItems.DWARVEN_BOOTS);
                output.accept(LOTRCombatItems.GALVORN_HELMET);
                output.accept(LOTRCombatItems.GALVORN_CHESTPLATE);
                output.accept(LOTRCombatItems.GALVORN_LEGGINGS);
                output.accept(LOTRCombatItems.GALVORN_BOOTS);
                output.accept(LOTRCombatItems.BRONZE_DAGGER);
                output.accept(LOTRCombatItems.IRON_DAGGER);
                output.accept(LOTRCombatItems.MITHRIL_DAGGER);
                output.accept(LOTRCombatItems.MITHRIL_BATTLEAXE);
                output.accept(LOTRCombatItems.MITHRIL_WARHAMMER);
                output.accept(LOTRCombatItems.GONDOR_WARHAMMER);
                // getSubItems gave all four forms of the horn.
                for (LOTRCommandHornItem.Mode mode : LOTRCommandHornItem.Mode.values()) {
                    output.accept(LOTRCommandHornItem.stack(mode));
                }
                // throwingAxeDwarven, registered between the horn and the Uruks.
                output.accept(LOTRCombatItems.DWARVEN_THROWING_AXE);
                output.accept(LOTRCombatItems.URUK_CLEAVER);
                output.accept(LOTRCombatItems.URUK_DAGGER);
                output.accept(LOTRCombatItems.POISONED_URUK_DAGGER);
                output.accept(LOTRCombatItems.URUK_BATTLEAXE);
                output.accept(LOTRCombatItems.URUK_WARHAMMER);
                output.accept(LOTRCombatItems.URUK_SPEAR);
                output.accept(LOTRCombatItems.URUK_HELMET);
                output.accept(LOTRCombatItems.URUK_CHESTPLATE);
                output.accept(LOTRCombatItems.URUK_LEGGINGS);
                output.accept(LOTRCombatItems.URUK_BOOTS);
                // The bolt and its three crossbows come straight after the Uruk
                // armour in LOTRMod's registerItem order.
                output.accept(LOTRCombatItems.CROSSBOW_BOLT);
                output.accept(LOTRCombatItems.URUK_CROSSBOW);
                output.accept(LOTRCombatItems.IRON_CROSSBOW);
                output.accept(LOTRCombatItems.MITHRIL_CROSSBOW);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SCOUT_HOOD);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SCOUT_TUNIC);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SCOUT_LEGGINGS);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SCOUT_BOOTS);
                output.accept(LOTRCombatItems.MIRKWOOD_BOW);
                output.accept(LOTRCombatItems.ROHIRRIC_SWORD);
                output.accept(LOTRCombatItems.ROHIRRIC_DAGGER);
                output.accept(LOTRCombatItems.ROHIRRIC_SPEAR);
                output.accept(LOTRCombatItems.ROHIRRIC_COIF);
                output.accept(LOTRCombatItems.ROHIRRIC_HAUBERK);
                output.accept(LOTRCombatItems.ROHIRRIC_LEGGINGS);
                output.accept(LOTRCombatItems.ROHIRRIC_BOOTS);
                output.accept(LOTRCombatItems.GONDOR_WINGED_HELMET);
                output.accept(LOTRCombatItems.PEBBLE);
                output.accept(LOTRCombatItems.SLING);
                output.accept(LOTRCombatItems.RANGER_HOOD);
                output.accept(LOTRCombatItems.RANGER_TUNIC);
                output.accept(LOTRCombatItems.RANGER_LEGGINGS);
                output.accept(LOTRCombatItems.RANGER_BOOTS);
                output.accept(LOTRCombatItems.DUNLENDING_HELMET);
                output.accept(LOTRCombatItems.DUNLENDING_CHESTPLATE);
                output.accept(LOTRCombatItems.DUNLENDING_LEGGINGS);
                output.accept(LOTRCombatItems.DUNLENDING_BOOTS);
                output.accept(LOTRCombatItems.DUNLENDING_CLUB);
                output.accept(LOTRCombatItems.DUNLENDING_TRIDENT);
                output.accept(LOTRCombatItems.MORGUL_BLADE);
                output.accept(LOTRCombatItems.MORGUL_HELMET);
                output.accept(LOTRCombatItems.MORGUL_CHESTPLATE);
                output.accept(LOTRCombatItems.MORGUL_LEGGINGS);
                output.accept(LOTRCombatItems.MORGUL_BOOTS);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SWORD);
                output.accept(LOTRCombatItems.WOOD_ELVEN_DAGGER);
                output.accept(LOTRCombatItems.WOOD_ELVEN_SPEAR);
                output.accept(LOTRCombatItems.WOOD_ELVEN_HELMET);
                output.accept(LOTRCombatItems.WOOD_ELVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.WOOD_ELVEN_LEGGINGS);
                output.accept(LOTRCombatItems.WOOD_ELVEN_BOOTS);
                output.accept(LOTRCombatItems.COMMAND_SWORD);
                output.accept(LOTRCombatItems.POISONED_BRONZE_DAGGER);
                output.accept(LOTRCombatItems.POISONED_IRON_DAGGER);
                output.accept(LOTRCombatItems.POISONED_MITHRIL_DAGGER);
                output.accept(LOTRCombatItems.POISONED_GONDOR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_GALADHRIM_DAGGER);
                output.accept(LOTRCombatItems.POISONED_DWARVEN_DAGGER);
                output.accept(LOTRCombatItems.POISONED_ROHIRRIC_DAGGER);
                output.accept(LOTRCombatItems.POISONED_WOOD_ELVEN_DAGGER);
                output.accept(LOTRCombatItems.ANGMAR_SWORD);
                output.accept(LOTRCombatItems.ANGMAR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_ANGMAR_DAGGER);
                output.accept(LOTRCombatItems.ANGMAR_BATTLEAXE);
                output.accept(LOTRCombatItems.ANGMAR_WARHAMMER);
                output.accept(LOTRCombatItems.ANGMAR_SPEAR);
                output.accept(LOTRCombatItems.ANGMAR_HELMET);
                output.accept(LOTRCombatItems.ANGMAR_CHESTPLATE);
                output.accept(LOTRCombatItems.ANGMAR_LEGGINGS);
                output.accept(LOTRCombatItems.ANGMAR_BOOTS);
                output.accept(LOTRCombatItems.ROHIRRIC_BATTLEAXE);
                output.accept(LOTRCombatItems.UMBARIC_SCIMITAR);
                output.accept(LOTRCombatItems.COAST_SOUTHRON_HELMET);
                output.accept(LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE);
                output.accept(LOTRCombatItems.COAST_SOUTHRON_LEGGINGS);
                output.accept(LOTRCombatItems.COAST_SOUTHRON_BOOTS);
                output.accept(LOTRCombatItems.GEMSBOK_HIDE_HELMET);
                output.accept(LOTRCombatItems.GEMSBOK_HIDE_CHESTPLATE);
                output.accept(LOTRCombatItems.GEMSBOK_HIDE_LEGGINGS);
                output.accept(LOTRCombatItems.GEMSBOK_HIDE_BOOTS);
                output.accept(LOTRCombatItems.LINDON_HELMET);
                output.accept(LOTRCombatItems.LINDON_CHESTPLATE);
                output.accept(LOTRCombatItems.LINDON_LEGGINGS);
                output.accept(LOTRCombatItems.LINDON_BOOTS);
                output.accept(LOTRCombatItems.LINDON_SWORD);
                output.accept(LOTRCombatItems.LINDON_DAGGER);
                output.accept(LOTRCombatItems.POISONED_LINDON_DAGGER);
                output.accept(LOTRCombatItems.LINDON_SPEAR);
                output.accept(LOTRCombatItems.UMBARIC_DAGGER);
                output.accept(LOTRCombatItems.POISONED_UMBARIC_DAGGER);
                output.accept(LOTRCombatItems.UMBARIC_SPEAR);
                output.accept(LOTRCombatItems.HARAD_BOW);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_SWORD);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_DAGGER);
                output.accept(LOTRCombatItems.POISONED_BLUE_DWARVEN_DAGGER);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_BATTLEAXE);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_WARHAMMER);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_THROWING_AXE);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_HELMET);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_LEGGINGS);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_BOOTS);
                output.accept(LOTRCombatItems.DWARVEN_SPEAR);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_SPEAR);
                output.accept(LOTRCombatItems.DIAMOND_HORSE_ARMOR);
                output.accept(LOTRCombatItems.GONDOR_HORSE_ARMOR);
                output.accept(LOTRCombatItems.ROHIRRIC_HORSE_ARMOR);
                output.accept(LOTRCombatItems.ISENGARD_WARG_ARMOR);
                output.accept(LOTRCombatItems.LINDON_HORSE_ARMOR);
                output.accept(LOTRCombatItems.GALADHRIM_HORSE_ARMOR);
                output.accept(LOTRCombatItems.MORGUL_HORSE_ARMOR);
                output.accept(LOTRCombatItems.MITHRIL_HORSE_ARMOR);
                output.accept(LOTRCombatItems.WOOD_ELVEN_ELK_ARMOR);
                output.accept(LOTRCombatItems.MORDOR_WARG_ARMOR);
                output.accept(LOTRCombatItems.ANGMAR_WARG_ARMOR);
                output.accept(LOTRCombatItems.ORC_SKULL_STAFF);
                output.accept(LOTRCombatItems.DOL_GULDUR_SWORD);
                output.accept(LOTRCombatItems.DOL_GULDUR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_DOL_GULDUR_DAGGER);
                output.accept(LOTRCombatItems.DOL_GULDUR_SPEAR);
                output.accept(LOTRCombatItems.DOL_GULDUR_BATTLEAXE);
                output.accept(LOTRCombatItems.DOL_GULDUR_WARHAMMER);
                output.accept(LOTRCombatItems.DOL_GULDUR_HELMET);
                output.accept(LOTRCombatItems.DOL_GULDUR_CHESTPLATE);
                output.accept(LOTRCombatItems.DOL_GULDUR_LEGGINGS);
                output.accept(LOTRCombatItems.DOL_GULDUR_BOOTS);
                output.accept(LOTRCombatItems.UTUMNO_HELMET);
                output.accept(LOTRCombatItems.UTUMNO_CHESTPLATE);
                output.accept(LOTRCombatItems.UTUMNO_LEGGINGS);
                output.accept(LOTRCombatItems.UTUMNO_BOOTS);
                output.accept(LOTRCombatItems.BLACK_URUK_CLEAVER);
                output.accept(LOTRCombatItems.BLACK_URUK_DAGGER);
                output.accept(LOTRCombatItems.POISONED_BLACK_URUK_DAGGER);
                output.accept(LOTRCombatItems.BLACK_URUK_SPEAR);
                output.accept(LOTRCombatItems.BLACK_URUK_BATTLEAXE);
                output.accept(LOTRCombatItems.BLACK_URUK_WARHAMMER);
                output.accept(LOTRCombatItems.BLACK_URUK_HELMET);
                output.accept(LOTRCombatItems.BLACK_URUK_CHESTPLATE);
                output.accept(LOTRCombatItems.BLACK_URUK_LEGGINGS);
                output.accept(LOTRCombatItems.BLACK_URUK_BOOTS);
                output.accept(LOTRCombatItems.BLACK_URUK_BOW);
                output.accept(LOTRCombatItems.SOUTHRON_CHAMPION_HELMET);
                output.accept(LOTRCombatItems.UTUMNO_SWORD);
                output.accept(LOTRCombatItems.UTUMNO_DAGGER);
                output.accept(LOTRCombatItems.POISONED_UTUMNO_DAGGER);
                output.accept(LOTRCombatItems.UTUMNO_SPEAR);
                output.accept(LOTRCombatItems.UTUMNO_BATTLEAXE);
                output.accept(LOTRCombatItems.UTUMNO_WARHAMMER);
                output.accept(LOTRCombatItems.UTUMNO_BOW);
                output.accept(LOTRCombatItems.ROHIRRIC_BOW);
                output.accept(LOTRCombatItems.GONDOR_BOW);
                output.accept(LOTRCombatItems.LINDON_BOW);
                output.accept(LOTRCombatItems.BALROG_WHIP);
                output.accept(LOTRCombatItems.IRON_BATTLEAXE);
                output.accept(LOTRCombatItems.BRONZE_BATTLEAXE);
                output.accept(LOTRCombatItems.BRONZE_CROSSBOW);
                // getSubItems gave one warhorn per invasion type, and there are
                // forty-five of those -- eight for Gondor's fiefs alone.
                for (LOTRInvasions invasion : LOTRInvasions.values()) {
                    output.accept(LOTRWarhornItem.stack(LOTRCombatItems.WARHORN, invasion));
                }
                output.accept(LOTRCombatItems.HALF_TROLL_HELMET);
                output.accept(LOTRCombatItems.HALF_TROLL_CHESTPLATE);
                output.accept(LOTRCombatItems.HALF_TROLL_LEGGINGS);
                output.accept(LOTRCombatItems.HALF_TROLL_BOOTS);
                output.accept(LOTRCombatItems.HALF_TROLL_BATTLEAXE);
                output.accept(LOTRCombatItems.HALF_TROLL_WARHAMMER);
                output.accept(LOTRCombatItems.HALF_TROLL_MACE);
                output.accept(LOTRCombatItems.HALF_TROLL_SCIMITAR);
                output.accept(LOTRCombatItems.HALF_TROLL_DAGGER);
                output.accept(LOTRCombatItems.POISONED_HALF_TROLL_DAGGER);
                output.accept(LOTRCombatItems.HALF_TROLL_RHINO_ARMOR);
                output.accept(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRCombatItems.MITHRIL_TRIMMED_DWARVEN_HELMET);
                output.accept(LOTRCombatItems.MITHRIL_TRIMMED_DWARVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.MITHRIL_TRIMMED_DWARVEN_LEGGINGS);
                output.accept(LOTRCombatItems.MITHRIL_TRIMMED_DWARVEN_BOOTS);
                output.accept(LOTRCombatItems.DOL_AMROTH_SWORD);
                output.accept(LOTRCombatItems.DOL_AMROTH_HELMET);
                output.accept(LOTRCombatItems.DOL_AMROTH_CHESTPLATE);
                output.accept(LOTRCombatItems.DOL_AMROTH_LEGGINGS);
                output.accept(LOTRCombatItems.DOL_AMROTH_BOOTS);
                output.accept(LOTRCombatItems.DOL_AMROTH_HORSE_ARMOR);
                output.accept(LOTRCombatItems.MORWAITH_DAGGER);
                output.accept(LOTRCombatItems.POISONED_MORWAITH_DAGGER);
                output.accept(LOTRCombatItems.MORWAITH_BATTLEAXE);
                output.accept(LOTRCombatItems.MORWAITH_SPEAR);
                output.accept(LOTRCombatItems.MORWAITH_HELMET);
                output.accept(LOTRCombatItems.MORWAITH_CHESTPLATE);
                output.accept(LOTRCombatItems.MORWAITH_LEGGINGS);
                output.accept(LOTRCombatItems.MORWAITH_BOOTS);
                output.accept(LOTRCombatItems.MORWAITH_CHIEFTAIN_HELMET);
                output.accept(LOTRCombatItems.MORWAITH_CHIEFTAIN_CHESTPLATE);
                output.accept(LOTRCombatItems.MORWAITH_CHIEFTAIN_LEGGINGS);
                output.accept(LOTRCombatItems.MORWAITH_CHIEFTAIN_BOOTS);
                output.accept(LOTRCombatItems.BONE_HELMET);
                output.accept(LOTRCombatItems.BONE_CHESTPLATE);
                output.accept(LOTRCombatItems.BONE_LEGGINGS);
                output.accept(LOTRCombatItems.BONE_BOOTS);
                // And the other two throwing axes, which LOTRMod registers a
                // long way after the Uruk set.
                output.accept(LOTRCombatItems.BRONZE_THROWING_AXE);
                output.accept(LOTRCombatItems.IRON_THROWING_AXE);
                output.accept(LOTRCombatItems.GONDOLIN_SWORD);
                output.accept(LOTRCombatItems.CHARRED_MALLORN_MACE);
                output.accept(LOTRCombatItems.GONDOLIN_HELMET);
                output.accept(LOTRCombatItems.GONDOLIN_CHESTPLATE);
                output.accept(LOTRCombatItems.GONDOLIN_LEGGINGS);
                output.accept(LOTRCombatItems.GONDOLIN_BOOTS);
                output.accept(LOTRCombatItems.ROHIRRIC_MARSHAL_HELMET);
                output.accept(LOTRCombatItems.ROHIRRIC_MARSHAL_CHESTPLATE);
                output.accept(LOTRCombatItems.ROHIRRIC_MARSHAL_LEGGINGS);
                output.accept(LOTRCombatItems.ROHIRRIC_MARSHAL_BOOTS);
                output.accept(LOTRCombatItems.TAURETHRIM_DAGGER);
                output.accept(LOTRCombatItems.POISONED_TAURETHRIM_DAGGER);
                output.accept(LOTRCombatItems.TAURETHRIM_SPEAR);
                output.accept(LOTRCombatItems.TAURETHRIM_SWORD);
                output.accept(LOTRCombatItems.TAURETHRIM_HELMET);
                output.accept(LOTRCombatItems.TAURETHRIM_CHESTPLATE);
                output.accept(LOTRCombatItems.TAURETHRIM_LEGGINGS);
                output.accept(LOTRCombatItems.TAURETHRIM_BOOTS);
                output.accept(LOTRCombatItems.TAURETHRIM_CHIEFTAIN_HELMET);
                output.accept(LOTRCombatItems.UMBARIC_POLEAXE);
                output.accept(LOTRCombatItems.URUK_PIKE);
                output.accept(LOTRCombatItems.COAST_SOUTHRON_HORSE_ARMOR);
                output.accept(LOTRCombatItems.MORDOR_WARSCYTHE);
                output.accept(LOTRCombatItems.DOL_AMROTH_LANCE);
                output.accept(LOTRCombatItems.LINDON_BATTLESTAFF);
                output.accept(LOTRCombatItems.GALADHRIM_BATTLESTAFF);
                output.accept(LOTRCombatItems.WOOD_ELVEN_BATTLESTAFF);
                output.accept(LOTRCombatItems.TAURETHRIM_BLOWGUN);
                output.accept(LOTRCombatItems.TAURETHRIM_DART);
                output.accept(LOTRCombatItems.POISONED_TAURETHRIM_DART);
                output.accept(LOTRCombatItems.BARROW_BLADE);
                output.accept(LOTRCombatItems.POISONED_BARROW_BLADE);
                output.accept(LOTRCombatItems.UMBARIC_MACE);
                output.accept(LOTRCombatItems.GALADHRIM_CLOAK_HOOD);
                output.accept(LOTRCombatItems.GALADHRIM_CLOAK_TUNIC);
                output.accept(LOTRCombatItems.GALADHRIM_CLOAK_LEGGINGS);
                output.accept(LOTRCombatItems.GALADHRIM_CLOAK_BOOTS);
                output.accept(LOTRCombatItems.DWARVEN_BOAR_ARMOR);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_BOAR_ARMOR);
                output.accept(LOTRCombatItems.HALF_TROLL_PIKE);
                output.accept(LOTRCombatItems.IRON_PIKE);
                output.accept(LOTRCombatItems.GOLDEN_TAURETHRIM_HELMET);
                output.accept(LOTRCombatItems.GOLDEN_TAURETHRIM_CHESTPLATE);
                output.accept(LOTRCombatItems.GOLDEN_TAURETHRIM_LEGGINGS);
                output.accept(LOTRCombatItems.GOLDEN_TAURETHRIM_BOOTS);
                output.accept(LOTRCombatItems.DWARVEN_PIKE);
                output.accept(LOTRCombatItems.BLUE_DWARVEN_PIKE);
                output.accept(LOTRCombatItems.DOL_AMROTH_DAGGER);
                output.accept(LOTRCombatItems.POISONED_DOL_AMROTH_DAGGER);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_HELMET);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_CHESTPLATE);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_LEGGINGS);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_BOOTS);
                output.accept(LOTRCombatItems.GONDOR_LANCE);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_CLEAVER);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_WARAXE);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_BLUDGEON);
                output.accept(LOTRCombatItems.URUK_BERSERKER_HELMET);
                output.accept(LOTRCombatItems.URUK_BERSERKER_CLEAVER);
                output.accept(LOTRCombatItems.ROHIRRIC_LANCE);
                output.accept(LOTRCombatItems.GALADHRIM_LONGSPEAR);
                output.accept(LOTRCombatItems.LINDON_LONGSPEAR);
                output.accept(LOTRCombatItems.WOOD_ELVEN_LONGSPEAR);
                output.accept(LOTRCombatItems.MITHRIL_HALBERD);
                output.accept(LOTRCombatItems.DALE_SWORD);
                output.accept(LOTRCombatItems.DALE_DAGGER);
                output.accept(LOTRCombatItems.POISONED_DALE_DAGGER);
                output.accept(LOTRCombatItems.DALE_SPEAR);
                output.accept(LOTRCombatItems.DALE_BATTLEAXE);
                output.accept(LOTRCombatItems.DALE_HELMET);
                output.accept(LOTRCombatItems.DALE_CHESTPLATE);
                output.accept(LOTRCombatItems.DALE_LEGGINGS);
                output.accept(LOTRCombatItems.DALE_BOOTS);
                output.accept(LOTRCombatItems.DORWINION_HELMET);
                output.accept(LOTRCombatItems.DORWINION_CHESTPLATE);
                output.accept(LOTRCombatItems.DORWINION_LEGGINGS);
                output.accept(LOTRCombatItems.DORWINION_BOOTS);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_HELMET);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_CHESTPLATE);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_LEGGINGS);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_BOOTS);
                output.accept(LOTRCombatItems.BLADORTHIN_SPEAR);
                output.accept(LOTRCombatItems.DALE_BOW);
                output.accept(LOTRCombatItems.DALE_HORSE_ARMOR);
                output.accept(LOTRCombatItems.ITHILIEN_RANGER_HOOD);
                output.accept(LOTRCombatItems.ITHILIEN_RANGER_TUNIC);
                output.accept(LOTRCombatItems.ITHILIEN_RANGER_LEGGINGS);
                output.accept(LOTRCombatItems.ITHILIEN_RANGER_BOOTS);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_DAGGER);
                output.accept(LOTRCombatItems.POISONED_GUNDABAD_URUK_DAGGER);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_SPEAR);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_PIKE);
                output.accept(LOTRCombatItems.GUNDABAD_URUK_BOW);
                output.accept(LOTRCombatItems.TAURETHRIM_BLUDGEON);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_SWORD);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_DAGGER);
                output.accept(LOTRCombatItems.POISONED_DORWINION_ELVEN_DAGGER);
                output.accept(LOTRCombatItems.TAURETHRIM_BATTLEAXE);
                output.accept(LOTRCombatItems.TAURETHRIM_PIKE);
                output.accept(LOTRCombatItems.MORWAITH_CLUB);
                output.accept(LOTRCombatItems.DALE_PITCHFORK);
                output.accept(LOTRCombatItems.ROLLING_PIN);
                output.accept(LOTRCombatItems.ANGMAR_POLEAXE);
                output.accept(LOTRCombatItems.DOL_GULDUR_SPIKE);
                output.accept(LOTRCombatItems.UMBARIC_PIKE);
                output.accept(LOTRCombatItems.LOSSARNACH_HELMET);
                output.accept(LOTRCombatItems.LOSSARNACH_CHESTPLATE);
                output.accept(LOTRCombatItems.LOSSARNACH_LEGGINGS);
                output.accept(LOTRCombatItems.LOSSARNACH_BOOTS);
                output.accept(LOTRCombatItems.PELARGIR_HELMET);
                output.accept(LOTRCombatItems.PELARGIR_CHESTPLATE);
                output.accept(LOTRCombatItems.PELARGIR_LEGGINGS);
                output.accept(LOTRCombatItems.PELARGIR_BOOTS);
                output.accept(LOTRCombatItems.PINNATH_GELIN_HELMET);
                output.accept(LOTRCombatItems.PINNATH_GELIN_CHESTPLATE);
                output.accept(LOTRCombatItems.PINNATH_GELIN_LEGGINGS);
                output.accept(LOTRCombatItems.PINNATH_GELIN_BOOTS);
                output.accept(LOTRCombatItems.LOSSARNACH_BATTLEAXE);
                output.accept(LOTRCombatItems.LOSSARNACH_THROWING_AXE);
                output.accept(LOTRCombatItems.PELARGIR_EKET);
                output.accept(LOTRCombatItems.PELARGIR_TRIDENT);
                output.accept(LOTRCombatItems.BLACKROOT_VALE_HELMET);
                output.accept(LOTRCombatItems.BLACKROOT_VALE_CHESTPLATE);
                output.accept(LOTRCombatItems.BLACKROOT_VALE_LEGGINGS);
                output.accept(LOTRCombatItems.BLACKROOT_VALE_BOOTS);
                output.accept(LOTRCombatItems.BLACKROOT_BOW);
                output.accept(LOTRCombatItems.GONDOR_PIKE);
                output.accept(LOTRCombatItems.DOL_AMROTH_GAMBESON);
                output.accept(LOTRCombatItems.DOL_AMROTH_CHAPS);
                output.accept(LOTRCombatItems.DOL_AMROTH_LONGSPEAR);
                output.accept(LOTRCombatItems.GONDOR_GAMBESON);
                output.accept(LOTRCombatItems.LEBENNIN_GAMBESON);
                output.accept(LOTRCombatItems.STONE_SPEAR);
                output.accept(LOTRCombatItems.LAMEDON_HELMET);
                output.accept(LOTRCombatItems.LAMEDON_CHESTPLATE);
                output.accept(LOTRCombatItems.LAMEDON_LEGGINGS);
                output.accept(LOTRCombatItems.LAMEDON_BOOTS);
                output.accept(LOTRCombatItems.LAMEDON_HORSE_ARMOR);
                output.accept(LOTRCombatItems.LAMEDON_JACKET);
                output.accept(LOTRCombatItems.DALE_GAMBESON);
                output.accept(LOTRCombatItems.ARNOR_HELMET);
                output.accept(LOTRCombatItems.ARNOR_CHESTPLATE);
                output.accept(LOTRCombatItems.ARNOR_LEGGINGS);
                output.accept(LOTRCombatItems.ARNOR_BOOTS);
                output.accept(LOTRCombatItems.RANGER_BOW);
                output.accept(LOTRCombatItems.RHUNIC_SWORD);
                output.accept(LOTRCombatItems.RHUNIC_DAGGER);
                output.accept(LOTRCombatItems.POISONED_RHUNIC_DAGGER);
                output.accept(LOTRCombatItems.RHUNIC_SPEAR);
                output.accept(LOTRCombatItems.RHUNIC_BARDICHE);
                output.accept(LOTRCombatItems.RHUNIC_PIKE);
                output.accept(LOTRCombatItems.RHUNIC_HELMET);
                output.accept(LOTRCombatItems.RHUNIC_CHESTPLATE);
                output.accept(LOTRCombatItems.RHUNIC_LEGGINGS);
                output.accept(LOTRCombatItems.RHUNIC_BOOTS);
                output.accept(LOTRCombatItems.RHUNIC_BOW);
                output.accept(LOTRCombatItems.RHUNIC_HORSE_ARMOR);
                output.accept(LOTRCombatItems.RHUNIC_FIRE_POT);
                output.accept(LOTRCombatItems.GOLDEN_RHUNIC_HELMET);
                output.accept(LOTRCombatItems.GOLDEN_RHUNIC_CHESTPLATE);
                output.accept(LOTRCombatItems.GOLDEN_RHUNIC_LEGGINGS);
                output.accept(LOTRCombatItems.GOLDEN_RHUNIC_BOOTS);
                output.accept(LOTRCombatItems.RHUNIC_WARLORD_HELMET);
                output.accept(LOTRCombatItems.DORWINION_ELVEN_BOW);
                output.accept(LOTRCombatItems.RHUNIC_BATTLEAXE);
                output.accept(LOTRCombatItems.RIVENDELL_SWORD);
                output.accept(LOTRCombatItems.RIVENDELL_DAGGER);
                output.accept(LOTRCombatItems.POISONED_RIVENDELL_DAGGER);
                output.accept(LOTRCombatItems.RIVENDELL_SPEAR);
                output.accept(LOTRCombatItems.RIVENDELL_HELMET);
                output.accept(LOTRCombatItems.RIVENDELL_CHESTPLATE);
                output.accept(LOTRCombatItems.RIVENDELL_LEGGINGS);
                output.accept(LOTRCombatItems.RIVENDELL_BOOTS);
                output.accept(LOTRCombatItems.RIVENDELL_HORSE_ARMOR);
                output.accept(LOTRCombatItems.RIVENDELL_BATTLESTAFF);
                output.accept(LOTRCombatItems.RIVENDELL_LONGSPEAR);
                output.accept(LOTRCombatItems.ARNOR_SWORD);
                output.accept(LOTRCombatItems.ARNOR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_ARNOR_DAGGER);
                output.accept(LOTRCombatItems.ARNOR_SPEAR);
                output.accept(LOTRCombatItems.RIVENDELL_BOW);
                output.accept(LOTRCombatItems.POISONED_ARROW);
                output.accept(LOTRCombatItems.POISONED_CROSSBOW_BOLT);
                output.accept(LOTRCombatItems.MORWAITH_SWORD);
                output.accept(LOTRCombatItems.GULFEN_HELMET);
                output.accept(LOTRCombatItems.GULFEN_CHESTPLATE);
                output.accept(LOTRCombatItems.GULFEN_LEGGINGS);
                output.accept(LOTRCombatItems.GULFEN_BOOTS);
                output.accept(LOTRCombatItems.CORSAIR_HELMET);
                output.accept(LOTRCombatItems.CORSAIR_CHESTPLATE);
                output.accept(LOTRCombatItems.CORSAIR_LEGGINGS);
                output.accept(LOTRCombatItems.CORSAIR_BOOTS);
                output.accept(LOTRCombatItems.CORSAIR_EKET);
                output.accept(LOTRCombatItems.CORSAIR_DAGGER);
                output.accept(LOTRCombatItems.POISONED_CORSAIR_DAGGER);
                output.accept(LOTRCombatItems.CORSAIR_HARPOON);
                output.accept(LOTRCombatItems.CORSAIR_BATTLEAXE);
                output.accept(LOTRCombatItems.UMBARIC_HELMET);
                output.accept(LOTRCombatItems.UMBARIC_CHESTPLATE);
                output.accept(LOTRCombatItems.UMBARIC_LEGGINGS);
                output.accept(LOTRCombatItems.UMBARIC_BOOTS);
                output.accept(LOTRCombatItems.HARNENNOR_HELMET);
                output.accept(LOTRCombatItems.HARNENNOR_CHESTPLATE);
                output.accept(LOTRCombatItems.HARNENNOR_LEGGINGS);
                output.accept(LOTRCombatItems.HARNENNOR_BOOTS);
                output.accept(LOTRCombatItems.HARADRIC_SWORD);
                output.accept(LOTRCombatItems.HARADRIC_DAGGER);
                output.accept(LOTRCombatItems.POISONED_HARADRIC_DAGGER);
                output.accept(LOTRCombatItems.HARADRIC_SPEAR);
                output.accept(LOTRCombatItems.HARADRIC_PIKE);
                output.accept(LOTRCombatItems.GULFEN_KHOPESH);
                output.accept(LOTRCombatItems.NOMAD_CAP);
                output.accept(LOTRCombatItems.NOMAD_TUNIC);
                output.accept(LOTRCombatItems.NOMAD_LEGGINGS);
                output.accept(LOTRCombatItems.NOMAD_SHOES);
                output.accept(LOTRCombatItems.UMBARIC_HORSE_ARMOR);
                output.accept(LOTRCombatItems.OLD_HARADRIC_SACRIFICIAL_DAGGER);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_HELMET);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_CHESTPLATE);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_LEGGINGS);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_BOOTS);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_SWORD);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_DAGGER);
                output.accept(LOTRCombatItems.POISONED_BLACK_NUMENOREAN_DAGGER);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_SPEAR);
                output.accept(LOTRCombatItems.BLACK_NUMENOREAN_MACE);
            })
            .build();

    // tabStory: the seven named weapons of the tale, the only things
    // LOTRStoryItem marked. In LOTRMod's registration order, as everywhere else
    // here. NOT in the port: the elven blades' glow -- LOTRItemSword swapped in
    // a _glowing icon when orcs were near, and there are no orcs yet, so Sting,
    // Glamdring and Ringil stay unlit like every other elven blade here.
    public static final CreativeModeTab STORY = FabricCreativeModeTab.builder()
            // LOTRCreativeTabs.setupIcons: tabStory.theIcon = anduril.
            .icon(() -> new ItemStack(LOTRStoryItems.ANDURIL))
            .title(Component.translatable("creativeTab.lotr.story"))
            .displayItems((params, output) -> {
                output.accept(LOTRStoryItems.STING);
                output.accept(LOTRStoryItems.SAURON_MACE);
                output.accept(LOTRStoryItems.GANDALF_STAFF_WHITE);
                output.accept(LOTRStoryItems.ANDURIL);
                output.accept(LOTRStoryItems.RINGIL);
                output.accept(LOTRStoryItems.GANDALF_STAFF_GREY);
                output.accept(LOTRStoryItems.GLAMDRING);
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
    }
}