package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

/**
 * Generates block tags: mineable/pickaxe + the needs_*_tool tier that
 * reproduces the original harvest levels (copper/tin/salt/saltpeter/sulfur =
 * stone tier, silver/mithril = iron tier).
 *
 * 26.1 API notes (from the porting map + tags doc):
 *  - FabricTagProvider -> FabricTagsProvider (note the 's'), and the inner
 *    BlockTagProvider -> BlockTagsProvider.
 *  - The builder method is valueLookupBuilder(tag), added to via .add(block).
 */
public class LOTRBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public LOTRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        Block[] stoneTier = {
                LOTRBlocks.COPPER_ORE, LOTRBlocks.TIN_ORE,
                LOTRBlocks.SALT_ORE, LOTRBlocks.SALTPETER_ORE, LOTRBlocks.SULFUR_ORE
        };
        Block[] ironTier = {
                LOTRBlocks.SILVER_ORE, LOTRBlocks.MITHRIL_ORE
        };

        var pickaxe = valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
        var stone = valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL);
        var iron = valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL);

        for (Block b : stoneTier) {
            pickaxe.add(b);
            stone.add(b);
        }
        for (Block b : ironTier) {
            pickaxe.add(b);
            iron.add(b);
        }
    }
}