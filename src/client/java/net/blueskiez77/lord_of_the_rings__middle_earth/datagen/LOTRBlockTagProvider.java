package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

/**
 * Mining + category tags, driven off the family lists in LOTRBlocks.
 *
 * The stone/iron split now lives on the registerCube call itself (the Tier
 * argument) rather than in a pair of hand-maintained arrays here, so a new cube
 * cannot silently end up untagged and instantly breakable by hand.
 */
public class LOTRBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public LOTRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
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

        LOTRBlocks.CUBES_STONE_TIER.forEach(b -> {
            pickaxe.add(b);
            stone.add(b);
        });
        LOTRBlocks.CUBES_IRON_TIER.forEach(b -> {
            pickaxe.add(b);
            iron.add(b);
        });
        LOTRBlocks.ALL_PLANKS.forEach(b -> {
            axe.add(b);
            planksTag.add(b);
        });
        LOTRBlocks.ALL_LEAVES.forEach(b -> {
            hoe.add(b);
            leavesTag.add(b);
        });
        LOTRBlocks.ALL_SAPLINGS.forEach(saplingsTag::add);
        LOTRBlocks.ALL_TRAPDOORS.forEach(b -> {
            axe.add(b);
            trapdoorsTag.add(b);
        });
        LOTRBlocks.ALL_DOORS.forEach(b -> {
            axe.add(b);
            doorsTag.add(b);
        });

        // Bars and chandeliers are metal: pickaxe. Glass needs no tool.
        LOTRBlocks.ALL_BARS.forEach(pickaxe::add);
        LOTRBlocks.ALL_CHANDELIERS.forEach(pickaxe::add);
    }
}