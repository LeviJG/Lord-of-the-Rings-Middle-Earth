package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LOTRBlockLootProvider extends FabricBlockLootSubProvider {
    public LOTRBlockLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        LOTRBlocks.ALL_GLASS.forEach(this::dropWhenSilkTouch);

        // Farmland has no item of its own. Vanilla farmland drops dirt; the LOTR
        // one is tilled mud, so it drops mud.
        // Vanilla dirt path and farmland both drop dirt, with no silk-touch
        // special case. The LOTR pair are worked mud, so they drop mud.
        LOTRBlocks.ALL_FARMLAND.forEach(b -> dropOther(b, LOTRBlocks.MUD));
        dropOther(LOTRBlocks.DIRT_PATH_MUD, LOTRBlocks.MUD);
        LOTRBlocks.ALL_RAILS.forEach(this::dropSelf);

        // NOTE: crops deliberately have no loot table. They should drop their

        List<List<Block>> dropsSelf = List.of(
                LOTRBlocks.ALL_CUBES,
                LOTRBlocks.ALL_PLANKS,
                LOTRBlocks.ALL_LEAVES,
                LOTRBlocks.ALL_SAPLINGS,
                LOTRBlocks.ALL_TRAPDOORS,
                LOTRBlocks.ALL_BARS,
                LOTRBlocks.ALL_CHANDELIERS,
                LOTRBlocks.ALL_FLOWERS,
                LOTRBlocks.ALL_LOGS,
                LOTRBlocks.ALL_BEAMS,
                LOTRBlocks.ALL_PILLARS,
                LOTRBlocks.ALL_STAIRS,
                LOTRBlocks.ALL_FENCES,
                LOTRBlocks.ALL_WALLS,
                LOTRBlocks.ALL_COLUMNS,

                LOTRBlocks.ALL_BOTTOM_TOP,
                LOTRBlocks.ALL_SOIL_COLUMNS,
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

        // In no family list, because its models are hand-written rather than
        // generated -- so it needs its loot table naming explicitly.
        dropSelf(LOTRBlocks.BEACON_OF_GONDOR);
        dropSelf(LOTRBlocks.HOBBIT_OVEN);
        dropSelf(LOTRBlocks.ENT_JAR);
        dropSelf(LOTRBlocks.TABLE_OF_COMMAND);
        dropSelf(LOTRBlocks.UNSMELTERY);
        dropSelf(LOTRBlocks.MILLSTONE);
        dropSelf(LOTRBlocks.ORC_CHAIN);
        // getItemDropped returned null for the head half, so a bed drops one
        // item, not two -- vanilla's own beds use exactly this condition.
        LOTRBlocks.ALL_BEDS.forEach(bed -> add(bed,
                createSinglePropConditionTable(bed, BedBlock.PART, BedPart.FOOT)));
        LOTRBlocks.ALL_KEBAB_STANDS.forEach(this::dropSelf);
        LOTRBlocks.ALL_CHESTS.forEach(this::dropSelf);
        // damageDropped(i) = i & 3: each part dropped its own kind.
        LOTRBlocks.ALL_TROLL_TOTEMS.forEach(this::dropSelf);

        LOTRBlocks.TORCH_WALL.forEach((torch, wall) -> dropOther(wall, torch));

        // A double slab must drop two items, so slabs get their own table

        LOTRBlocks.ALL_SLABS.forEach(b -> add(b, this::createSlabItemTable));

        dropsSelf.forEach(family -> family.forEach(this::dropSelf));

        LOTRBlocks.ALL_DOORS.forEach(door -> add(door, createDoorTable(door)));
    }
}