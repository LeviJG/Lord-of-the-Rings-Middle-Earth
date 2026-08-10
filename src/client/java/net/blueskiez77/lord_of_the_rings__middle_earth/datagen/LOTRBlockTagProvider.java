package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LOTRBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public LOTRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static TagKey<Block> vanillaBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(path));
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        var pickaxe = builder(BlockTags.MINEABLE_WITH_PICKAXE);
        var stone = builder(BlockTags.NEEDS_STONE_TOOL);
        var iron = builder(BlockTags.NEEDS_IRON_TOOL);
        var axe = builder(BlockTags.MINEABLE_WITH_AXE);
        var hoe = builder(BlockTags.MINEABLE_WITH_HOE);
        var shovel = builder(BlockTags.MINEABLE_WITH_SHOVEL);

        var planksTag = builder(vanillaBlockTag("planks"));
        var leavesTag = builder(vanillaBlockTag("leaves"));
        var saplingsTag = builder(vanillaBlockTag("saplings"));
        var trapdoorsTag = builder(vanillaBlockTag("wooden_trapdoors"));
        var doorsTag = builder(vanillaBlockTag("wooden_doors"));
        var smallFlowersTag = builder(vanillaBlockTag("small_flowers"));
        var logsTag = builder(vanillaBlockTag("logs"));
        var logsBurnTag = builder(vanillaBlockTag("logs_that_burn"));
        var stairsTag = builder(vanillaBlockTag("stairs"));
        var woodenStairsTag = builder(vanillaBlockTag("wooden_stairs"));
        var slabsTag = builder(vanillaBlockTag("slabs"));
        var woodenSlabsTag = builder(vanillaBlockTag("wooden_slabs"));
        var fencesTag = builder(vanillaBlockTag("fences"));
        var woodenFencesTag = builder(vanillaBlockTag("wooden_fences"));
        var wallsTag = builder(vanillaBlockTag("walls"));
        var fenceGatesTag = builder(vanillaBlockTag("fence_gates"));

        // Pickaxe, but deliberately no needs_*_tool entry: a wooden pickaxe

        // harvest level of 0.
        // Rails: pickaxe, no needs_*_tool tag -- vanilla rails behave the same
        // way, and the original never set a harvest level on the mechanised rail.

        List.of(LOTRBlocks.THATCH_REED, LOTRBlocks.THATCH_THATCH,
                        LOTRBlocks.THATCH_REED_STAIRS, LOTRBlocks.THATCH_THATCH_STAIRS,
                        LOTRBlocks.THATCH_THATCH_SLAB, LOTRBlocks.THATCH_FLOOR)
                .forEach(b -> hoe.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.ALL_RAILS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.CUBES_NO_TIER.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.CUBES_STONE_TIER.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            stone.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.CUBES_IRON_TIER.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            iron.add(LOTRBlocks.keyOf(b));
        });
        // Soils, gravels, sands and paths: shovel, and no needs_*_tool entry.

        LOTRBlocks.SHOVEL_MINEABLE.forEach(b -> shovel.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.ALL_PLANKS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            planksTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_LEAVES.forEach(b -> {
            hoe.add(LOTRBlocks.keyOf(b));
            leavesTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_SAPLINGS.forEach(b -> saplingsTag.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.ALL_FLOWERS.forEach(b -> smallFlowersTag.add(LOTRBlocks.keyOf(b)));

        // anything that looks for wood. Beams are decorative and deliberately

        LOTRBlocks.ALL_LOGS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            logsTag.add(LOTRBlocks.keyOf(b));
            logsBurnTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_BEAMS.forEach(b -> axe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_PILLARS.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            stone.add(LOTRBlocks.keyOf(b));
        });

        LOTRBlocks.ALL_STAIRS.forEach(st -> {
            Block base = LOTRBlocks.STAIRS_BASE.get(st);
            stairsTag.add(LOTRBlocks.keyOf(st));
            if (LOTRBlocks.ALL_PLANKS.contains(base)) {
                axe.add(LOTRBlocks.keyOf(st));
                woodenStairsTag.add(LOTRBlocks.keyOf(st));
            } else if (LOTRBlocks.SHOVEL_MINEABLE.contains(base)) {
                shovel.add(LOTRBlocks.keyOf(st));
            } else {
                pickaxe.add(LOTRBlocks.keyOf(st));
                if (LOTRBlocks.CUBES_IRON_TIER.contains(base)) {
                    iron.add(LOTRBlocks.keyOf(st));
                } else if (LOTRBlocks.CUBES_STONE_TIER.contains(base)) {
                    stone.add(LOTRBlocks.keyOf(st));
                }
            }
        });

        LOTRBlocks.ALL_SLABS.forEach(sl -> {
            Block base = LOTRBlocks.SLAB_BASE.get(sl);
            slabsTag.add(LOTRBlocks.keyOf(sl));
            if (LOTRBlocks.ALL_PLANKS.contains(base)) {
                axe.add(LOTRBlocks.keyOf(sl));
                woodenSlabsTag.add(LOTRBlocks.keyOf(sl));
            } else if (LOTRBlocks.SHOVEL_MINEABLE.contains(base)) {
                shovel.add(LOTRBlocks.keyOf(sl));
            } else {
                pickaxe.add(LOTRBlocks.keyOf(sl));
                if (LOTRBlocks.CUBES_IRON_TIER.contains(base)) {
                    iron.add(LOTRBlocks.keyOf(sl));
                } else if (LOTRBlocks.CUBES_STONE_TIER.contains(base)) {
                    stone.add(LOTRBlocks.keyOf(sl));
                }
            }
        });

        LOTRBlocks.ALL_FENCES.forEach(f -> {
            Block base = LOTRBlocks.FENCE_BASE.get(f);
            fencesTag.add(LOTRBlocks.keyOf(f));
            if (LOTRBlocks.ALL_PLANKS.contains(base)) {
                axe.add(LOTRBlocks.keyOf(f));
                woodenFencesTag.add(LOTRBlocks.keyOf(f));
            } else {
                pickaxe.add(LOTRBlocks.keyOf(f));
            }
        });

        LOTRBlocks.ALL_WALLS.forEach(w -> {
            Block base = LOTRBlocks.WALL_BASE.get(w);
            wallsTag.add(LOTRBlocks.keyOf(w));
            pickaxe.add(LOTRBlocks.keyOf(w));
            if (LOTRBlocks.CUBES_IRON_TIER.contains(base)) {
                iron.add(LOTRBlocks.keyOf(w));
            } else if (LOTRBlocks.CUBES_STONE_TIER.contains(base)) {
                stone.add(LOTRBlocks.keyOf(w));
            }
        });

        // take no tool, matching the original (no harvest level was set).
        // Smooth stone and the bone block: pickaxe, no needs_*_tool tag. The
        // originals never set a harvest level, and vanilla smooth stone and

        LOTRBlocks.ALL_COLUMNS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));

        LOTRBlocks.ALL_CRAFTING_TABLES.forEach(b -> axe.add(LOTRBlocks.keyOf(b)));

        var climbable = builder(vanillaBlockTag("climbable"));
        LOTRBlocks.ALL_LADDERS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            climbable.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_VINES.forEach(b -> climbable.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_GATES.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_FENCE_GATES.forEach(b -> axe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_BUTTONS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_PRESSURE_PLATES.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_TRAPDOORS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            trapdoorsTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_DOORS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            doorsTag.add(LOTRBlocks.keyOf(b));
        });

        LOTRBlocks.ALL_BARS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_CHANDELIERS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
    }
}