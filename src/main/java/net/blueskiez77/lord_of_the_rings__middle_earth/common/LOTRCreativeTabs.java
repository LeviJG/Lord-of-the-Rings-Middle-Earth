package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRWarhornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTrollStatueItem;

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
    // LOTRBlockAnimalJar, LOTRBlockDoubleFlower, LOTRBlockFallenLeaves,
    // LOTRBlockStalactite, LOTRBlockWeaponRack, and the tabDeco items
    // (banners, armour stands, boss trophies, troll statues, double torches).
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
                LOTRBlocks.ALL_FLOWERS.forEach(output::accept);
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
                output.accept(LOTRItems.MITHRIL_SPEAR);
                output.accept(LOTRItems.MALLORN_SWORD);
                output.accept(LOTRItems.GALADHRIM_SWORD);
                output.accept(LOTRItems.GALADHRIM_SPEAR);
                output.accept(LOTRItems.MALLORN_BOW);
                output.accept(LOTRItems.GALADHRIM_BOW);
                output.accept(LOTRItems.GALADHRIM_HELMET);
                output.accept(LOTRItems.GALADHRIM_CHESTPLATE);
                output.accept(LOTRItems.GALADHRIM_LEGGINGS);
                output.accept(LOTRItems.GALADHRIM_BOOTS);
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
                output.accept(LOTRItems.DWARVEN_SPEAR);
                output.accept(LOTRItems.BLUE_DWARVEN_SWORD);
                output.accept(LOTRItems.BLUE_DWARVEN_DAGGER);
                output.accept(LOTRItems.POISONED_BLUE_DWARVEN_DAGGER);
                output.accept(LOTRItems.BLUE_DWARVEN_BATTLEAXE);
                output.accept(LOTRItems.BLUE_DWARVEN_WARHAMMER);
                output.accept(LOTRItems.BLUE_DWARVEN_SPEAR);
                output.accept(LOTRItems.BLUE_DWARVEN_THROWING_AXE);
                output.accept(LOTRItems.BLUE_DWARVEN_HELMET);
                output.accept(LOTRItems.BLUE_DWARVEN_CHESTPLATE);
                output.accept(LOTRItems.BLUE_DWARVEN_LEGGINGS);
                output.accept(LOTRItems.BLUE_DWARVEN_BOOTS);
                output.accept(LOTRItems.GONDOR_HORSE_ARMOR);
                output.accept(LOTRItems.ROHIRRIC_HORSE_ARMOR);
                output.accept(LOTRItems.LINDON_HORSE_ARMOR);
                output.accept(LOTRItems.GALADHRIM_HORSE_ARMOR);
                output.accept(LOTRItems.MORGUL_HORSE_ARMOR);
                output.accept(LOTRItems.MITHRIL_HORSE_ARMOR);
                output.accept(LOTRItems.COAST_SOUTHRON_HORSE_ARMOR);
                output.accept(LOTRItems.UMBARIC_HORSE_ARMOR);
                output.accept(LOTRItems.ISENGARD_WARG_ARMOR);
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
                output.accept(LOTRItems.UTUMNO_SWORD);
                output.accept(LOTRItems.UTUMNO_DAGGER);
                output.accept(LOTRItems.POISONED_UTUMNO_DAGGER);
                output.accept(LOTRItems.UTUMNO_SPEAR);
                output.accept(LOTRItems.UTUMNO_BATTLEAXE);
                output.accept(LOTRItems.UTUMNO_WARHAMMER);
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
                output.accept(LOTRItems.ROHIRRIC_BOW);
                output.accept(LOTRItems.GONDOR_BOW);
                output.accept(LOTRItems.LINDON_BOW);
                output.accept(LOTRItems.BALROG_WHIP);
                output.accept(LOTRItems.IRON_BATTLEAXE);
                output.accept(LOTRItems.BRONZE_BATTLEAXE);
                output.accept(LOTRItems.BRONZE_CROSSBOW);
                // getSubItems gave one warhorn per invasion type, and there are
                // forty-one of those -- six for Gondor's fiefs alone.
                for (LOTRInvasions invasion : LOTRInvasions.values()) {
                    output.accept(LOTRWarhornItem.stack(LOTRItems.WARHORN, invasion));
                }
                output.accept(LOTRItems.HALF_TROLL_SCIMITAR);
                output.accept(LOTRItems.HALF_TROLL_DAGGER);
                output.accept(LOTRItems.POISONED_HALF_TROLL_DAGGER);
                output.accept(LOTRItems.HALF_TROLL_BATTLEAXE);
                output.accept(LOTRItems.HALF_TROLL_WARHAMMER);
                output.accept(LOTRItems.HALF_TROLL_MACE);
                output.accept(LOTRItems.HALF_TROLL_HELMET);
                output.accept(LOTRItems.HALF_TROLL_CHESTPLATE);
                output.accept(LOTRItems.HALF_TROLL_LEGGINGS);
                output.accept(LOTRItems.HALF_TROLL_BOOTS);
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
                output.accept(LOTRItems.GONDOLIN_SWORD);
                output.accept(LOTRItems.GONDOLIN_HELMET);
                output.accept(LOTRItems.GONDOLIN_CHESTPLATE);
                output.accept(LOTRItems.GONDOLIN_LEGGINGS);
                output.accept(LOTRItems.GONDOLIN_BOOTS);
                output.accept(LOTRItems.CHARRED_MALLORN_MACE);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_HELMET);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_CHESTPLATE);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_LEGGINGS);
                output.accept(LOTRItems.ROHIRRIC_MARSHAL_BOOTS);
                output.accept(LOTRItems.TAURETHRIM_SWORD);
                output.accept(LOTRItems.TAURETHRIM_DAGGER);
                output.accept(LOTRItems.POISONED_TAURETHRIM_DAGGER);
                output.accept(LOTRItems.TAURETHRIM_SPEAR);
                output.accept(LOTRItems.TAURETHRIM_BATTLEAXE);
                output.accept(LOTRItems.TAURETHRIM_BLUDGEON);
                output.accept(LOTRItems.TAURETHRIM_HELMET);
                output.accept(LOTRItems.TAURETHRIM_CHESTPLATE);
                output.accept(LOTRItems.TAURETHRIM_LEGGINGS);
                output.accept(LOTRItems.TAURETHRIM_BOOTS);
                output.accept(LOTRItems.TAURETHRIM_CHIEFTAIN_HELMET);
                output.accept(LOTRItems.UMBARIC_POLEAXE);
                output.accept(LOTRItems.MORDOR_WARSCYTHE);
                output.accept(LOTRItems.LINDON_BATTLESTAFF);
                output.accept(LOTRItems.GALADHRIM_BATTLESTAFF);
                output.accept(LOTRItems.WOOD_ELVEN_BATTLESTAFF);
                output.accept(LOTRItems.URUK_PIKE);
                output.accept(LOTRItems.DOL_AMROTH_LANCE);
                output.accept(LOTRItems.UMBARIC_MACE);
                output.accept(LOTRItems.BARROW_BLADE);
                output.accept(LOTRItems.POISONED_BARROW_BLADE);
                output.accept(LOTRItems.TAURETHRIM_BLOWGUN);
                output.accept(LOTRItems.TAURETHRIM_DART);
                output.accept(LOTRItems.POISONED_TAURETHRIM_DART);
                output.accept(LOTRItems.HALF_TROLL_RHINO_ARMOR);
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
                // And the other two throwing axes, which LOTRMod registers a
                // long way after the Uruk set.
                output.accept(LOTRItems.BRONZE_THROWING_AXE);
                output.accept(LOTRItems.IRON_THROWING_AXE);
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
                output.accept(LOTRItems.MITHRIL);
                output.accept(LOTRItems.PIPEWEED);
                output.accept(LOTRItems.KEBAB);
            })
            .build();

    private LOTRCreativeTabs() {
    }

    // Called from mod init. Must run AFTER blocks and items are registered. */
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS_KEY, BLOCKS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UTILITIES_KEY, UTILITIES);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DECORATIONS_KEY, DECORATIONS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, COMBAT_KEY, COMBAT);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, EVERYTHING_KEY, EVERYTHING);
    }
}