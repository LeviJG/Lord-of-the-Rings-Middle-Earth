package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.core.HolderLookup;

/**
 * Generates block loot tables. For simple blocks this is dropSelf(block),
 * so mining yields the block back. Ores that should drop a different item
 * (or multiple) get a matching helper as they land.
 */
public class LOTRBlockLootProvider extends FabricBlockLootSubProvider {

    public LOTRBlockLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(LOTRBlocks.MITHRIL_ORE);
    }
}