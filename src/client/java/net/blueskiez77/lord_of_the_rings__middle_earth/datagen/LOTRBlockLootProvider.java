package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

/**
 * Block loot tables, driven off the family lists in LOTRBlocks.
 *
 * Doors use createDoorTable so a two-block door yields a single item. Glass
 * currently drops itself; swap ALL_GLASS over to a silk-touch-only table when
 * you want the vanilla "shatters unless silk touch" behaviour. Leaves and
 * saplings likewise drop themselves for now rather than running the vanilla
 * sapling/stick drop-chance tables.
 */
public class LOTRBlockLootProvider extends FabricBlockLootSubProvider {

    public LOTRBlockLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        // NOTE: crops have no loot table yet -- they should drop their seed
        // and produce item, neither of which exists until the item set lands.

        List<List<Block>> dropsSelf = List.of(
                LOTRBlocks.ALL_CUBES,
                LOTRBlocks.ALL_PLANKS,
                LOTRBlocks.ALL_LEAVES,
                LOTRBlocks.ALL_SAPLINGS,
                LOTRBlocks.ALL_TRAPDOORS,
                LOTRBlocks.ALL_BARS,
                LOTRBlocks.ALL_CHANDELIERS,
                LOTRBlocks.ALL_GLASS,
                LOTRBlocks.ALL_FLOWERS,
                LOTRBlocks.ALL_LOGS,
                LOTRBlocks.ALL_BEAMS,
                LOTRBlocks.ALL_PILLARS,
                LOTRBlocks.ALL_STAIRS,
                LOTRBlocks.ALL_FENCES,
                LOTRBlocks.ALL_WALLS,
                LOTRBlocks.ALL_COLUMNS,
                LOTRBlocks.ALL_CARPETS,
                LOTRBlocks.ALL_TORCHES,
                LOTRBlocks.ALL_CRAFTING_TABLES,
                LOTRBlocks.ALL_VINES,
                LOTRBlocks.ALL_LADDERS,
                LOTRBlocks.ALL_GATES,
                LOTRBlocks.ALL_BUSHES,
                LOTRBlocks.ALL_FENCE_GATES,
                LOTRBlocks.ALL_BUTTONS,
                LOTRBlocks.ALL_PRESSURE_PLATES);

        dropSelf(LOTRBlocks.WEB_UNGOLIANT);

        // A wall torch drops the standing torch, as vanilla does.
        LOTRBlocks.TORCH_WALL.forEach((torch, wall) -> dropOther(wall, torch));

        // A double slab must drop two items, so slabs get their own table
        // rather than dropSelf.
        LOTRBlocks.ALL_SLABS.forEach(b -> add(b, this::createSlabItemTable));

        dropsSelf.forEach(family -> family.forEach(this::dropSelf));

        LOTRBlocks.ALL_DOORS.forEach(door -> add(door, createDoorTable(door)));
    }
}