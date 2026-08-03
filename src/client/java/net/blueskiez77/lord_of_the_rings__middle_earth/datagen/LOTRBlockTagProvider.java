package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

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

/**
 * Mining + category tags, driven off the family lists in LOTRBlocks.
 *
 * The stone/iron split lives on the registerCube call itself (the Tier
 * argument) rather than in hand-maintained arrays here, so a new cube cannot
 * silently end up untagged and instantly breakable by hand.
 *
 * 26.2 notes:
 *   - valueLookupBuilder() was removed; the replacement is builder().
 *   - Tag contents are now ids rather than Block instances, so every element is
 *     added as a ResourceKey<Block>. LOTRBlocks.keyOf() hands back the key that
 *     register() already created, so there is no registry round-trip.
 */
public class LOTRBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public LOTRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Build a vanilla block tag key straight from its id.
     *
     * Used for the block+item category tags. 26.2 split tags that exist for
     * both a block and an item into a separate BlockItemTags class, so some
     * constants are no longer on BlockTags (saplings is the one this project
     * tripped over). Naming the tag by its id sidesteps the question entirely --
     * the ids themselves ("minecraft:saplings") are stable data. If any of the
     * BlockTags constants below also fail to resolve, swap them to this helper.
     */
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

        LOTRBlocks.CUBES_STONE_TIER.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            stone.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.CUBES_IRON_TIER.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            iron.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_PLANKS.forEach(b -> {
            axe.add(LOTRBlocks.keyOf(b));
            planksTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_LEAVES.forEach(b -> {
            hoe.add(LOTRBlocks.keyOf(b));
            leavesTag.add(LOTRBlocks.keyOf(b));
        });
        LOTRBlocks.ALL_SAPLINGS.forEach(b -> saplingsTag.add(LOTRBlocks.keyOf(b)));
        // Flowers take no tool (instabreak), so they join no mineable tag --
        // matching vanilla's own small flowers.
        LOTRBlocks.ALL_FLOWERS.forEach(b -> smallFlowersTag.add(LOTRBlocks.keyOf(b)));

        // Logs join the vanilla logs tags so they work in recipes and with
        // anything that looks for wood. Beams are decorative and deliberately
        // stay out of them -- they are not craftable into planks.
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

        // Stairs inherit their base block's tool and tier, so mirror whichever
        // tags the base sits in rather than assuming stone.
        LOTRBlocks.ALL_STAIRS.forEach(st -> {
            Block base = LOTRBlocks.STAIRS_BASE.get(st);
            stairsTag.add(LOTRBlocks.keyOf(st));
            if (LOTRBlocks.ALL_PLANKS.contains(base)) {
                axe.add(LOTRBlocks.keyOf(st));
                woodenStairsTag.add(LOTRBlocks.keyOf(st));
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

        // Smooth stone needs a pickaxe like the rock it is cut from; carpets
        // take no tool, matching the original (no harvest level was set).
        LOTRBlocks.ALL_COLUMNS.forEach(b -> {
            pickaxe.add(LOTRBlocks.keyOf(b));
            stone.add(LOTRBlocks.keyOf(b));
        });

        LOTRBlocks.ALL_CRAFTING_TABLES.forEach(b -> axe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_LADDERS.forEach(b -> axe.add(LOTRBlocks.keyOf(b)));
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

        // Bars and chandeliers are metal: pickaxe. Glass needs no tool.
        LOTRBlocks.ALL_BARS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
        LOTRBlocks.ALL_CHANDELIERS.forEach(b -> pickaxe.add(LOTRBlocks.keyOf(b)));
    }
}