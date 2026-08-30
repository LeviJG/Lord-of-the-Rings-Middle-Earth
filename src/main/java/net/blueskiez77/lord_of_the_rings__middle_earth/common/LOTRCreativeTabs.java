package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

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

// Creative inventory tabs. The 1.7.10 mod had ten tabs (LOTRCreativeTabs: blocks, util, decorations, food, materials, misc, tools, combat, story, spawning). This ports the first of them -- tabBlock, shown as "Middle-earth Blocks" -- faithfully: it holds building materials only, and nothing else. Membership is derived from the original by reading which block classes called setCreativeTab(LOTRCreativeTabs.tabBlock). Forty classes did, and they map onto the family lists in LOTRBlocks: rock and brick cubes, ores and storage blocks, planks, logs, beams, pillars, slabs, stairs, walls, glass, smooth stone, gravels/sands, soils and paths. Deliberately NOT here, because the original put them in other tabs: fences, leaves, saplings, flowers, vines, ladders, torches, chandeliers, panes and treasure piles (tabDeco); fence gates, doors, crafting tables, chests and beacons (tabUtil); buttons and pressure plates (tabMisc); kebabs, barrels and marzipan (tabFood). Those tabs are not ported yet, so the catch-all EVERYTHING tab below keeps that content reachable in creative in the meantime -- it is scaffolding, to be deleted once the other nine exist.
public final class LOTRCreativeTabs {
    public static final ResourceKey<CreativeModeTab> BLOCKS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "blocks"));

    public static final ResourceKey<CreativeModeTab> UTILITIES_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "utilities"));

    // Tabs sort by id, not by registration order, so the scaffolding tab is
    // named zz_everything to keep it last: blocks, utilities, zz_everything.
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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, EVERYTHING_KEY, EVERYTHING);
    }
}