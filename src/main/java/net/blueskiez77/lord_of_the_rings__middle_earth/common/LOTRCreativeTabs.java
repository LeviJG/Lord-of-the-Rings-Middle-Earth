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

    public static final ResourceKey<CreativeModeTab> EVERYTHING_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "everything"));

    // Blocks that sit in ALL_CUBES for registration convenience but belonged to a different tab in 1.7.10, so they must not appear in this one. The Utumno return portal pieces are a third case: the original never called setCreativeTab on them at all, so they were creative-unobtainable by design.
    private static Set<Block> cubesFromOtherTabs() {
        return new LinkedHashSet<>(List.of(
                LOTRBlocks.KEBAB_BLOCK,
                LOTRBlocks.MARZIPAN_CHOCOLATE,
                LOTRBlocks.TREASURE_COPPER,
                LOTRBlocks.TREASURE_GOLD,
                LOTRBlocks.TREASURE_SILVER,
                LOTRBlocks.UTUMNO_RETURN_PORTAL_BASE,
                LOTRBlocks.UTUMNO_RETURN_LIGHT));
    }

    public static final CreativeModeTab BLOCKS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.MALLORN_PLANKS))
            .title(Component.translatable("creativeTab.lotr.blocks"))
            .displayItems((params, output) -> {
                Set<Block> skip = cubesFromOtherTabs();

                LOTRBlocks.ALL_CUBES.forEach(b -> {
                    if (!skip.contains(b)) {
                        output.accept(b);
                    }
                });
                LOTRBlocks.ALL_COLUMNS.forEach(output::accept);
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
            })
            .build();

    public static final CreativeModeTab EVERYTHING = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.MITHRIL_BLOCK))
            .title(Component.translatable("creativeTab.lotr.everything"))
            .displayItems((params, output) -> {
                LOTRBlocks.ALL_BLOCKS.forEach(output::accept);
                output.accept(LOTRItems.MITHRIL);
                output.accept(LOTRItems.PIPEWEED);
            })
            .build();

    private LOTRCreativeTabs() {
    }

    // Called from mod init. Must run AFTER blocks and items are registered. */
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS_KEY, BLOCKS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, EVERYTHING_KEY, EVERYTHING);
    }
}