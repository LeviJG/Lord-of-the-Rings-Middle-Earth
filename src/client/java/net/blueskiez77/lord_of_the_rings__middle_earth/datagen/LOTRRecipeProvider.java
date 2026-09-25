package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFallenLeavesBlock;

// Crafting recipes for the blocks tab. Every recipe here is mechanical -- derived from the family lists in LOTRBlocks rather than transcribed one at a time -- because the original was mechanical too. LOTRRecipes.java has ~366 block recipes and they all reduce to a handful of shapes, every one identical to its vanilla equivalent: 4 planks   <- 1 log                       shapeless 3 beams    <- 3 logs                      X / X / X 2 smooth   <- 2 rock                      X / X 6 slabs    <- 3 base                      XXX 4 stairs   <- 6 base                      X.. / XX. / XXX 6 walls    <- 6 base                      XXX / XXX SCOPE -- deliberately not covered yet: - Pillars. They look like they should follow the vertical-three shape from their matching brick, but in 1.7.10 almost every pillar was crafted from plain vanilla stone regardless of appearance. Deriving angmar_pillar <- angmar_brick would invent a recipe the mod never had. - Brick recipes. Not derivable from names: most faction bricks come from plain vanilla stone, not a matching rock. These, and every other one-off recipe, are transcribed in LOTRTranscribedRecipes instead. - Faction gating. In 1.7.10 the faction brick stairs/slabs/walls lived in per-faction lists and could only be crafted at that faction's table. Every family member in LOTRTranscribedRecipes.FACTION_ONLY is skipped here and made at its table instead.
public class LOTRRecipeProvider extends FabricRecipeProvider {
    public LOTRRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new Recipes(registryLookup, exporter);
    }

    @Override
    public String getName() {
        return "LOTR Recipes";
    }

    /**
     * Every recipe in the lotr namespace. FabricRecipeProvider files a recipe
     * saved without an explicit id under the mod id
     * (lord_of_the_rings_-_middle_earth); the transcribed ones already name
     * lotr, and the mechanical ones here now follow.
     */
    @Override
    protected net.minecraft.resources.Identifier getRecipeIdentifier(net.minecraft.resources.Identifier identifier) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(
                LOTRMod.NAMESPACE, identifier.getPath());
    }

    private static class Recipes extends RecipeProvider {
        Recipes(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
            super(registryLookup, exporter);
        }

        @Override
        public void buildRecipes() {
            planksFromLogs();
            beamsFromLogs();
            smoothStone();
            cutFromBase();
            fallenLeaves();
            new LOTRTranscribedRecipes(registries, output).buildRecipes();
        }

        // LOTRRecipes: three of a LOTRBlockLeavesBase leaf in a row make six of
        // its fallen leaves, at the vanilla table. Only the mod's leaves -- the
        // loop skipped vanilla's.
        private void fallenLeaves() {
            LOTRBlocks.ALL_FALLEN_LEAVES.forEach(fallen -> {
                Block leaves = ((LOTRFallenLeavesBlock) fallen).leaves();
                if (!LOTRBlocks.ALL_LEAVES.contains(leaves)) {
                    return;
                }
                shaped(RecipeCategory.DECORATIONS, fallen, 6)
                        .pattern("XXX")
                        .define('X', leaves)
                        .unlockedBy(getHasName(leaves), has(leaves))
                        .save(output);
            });
        }

        private void planksFromLogs() {
            Map<String, Block> logsByStem = stems(LOTRBlocks.ALL_LOGS, "_log");
            LOTRBlocks.ALL_PLANKS.forEach(planks -> {
                Block log = logsByStem.get(stem(planks, "_planks"));
                if (log == null || factionOnly(planks)) {
                    return;
                }
                shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                        .requires(log)
                        .unlockedBy(getHasName(log), has(log))
                        .save(output);
            });
        }

        private void beamsFromLogs() {
            Map<String, Block> logsByStem = stems(LOTRBlocks.ALL_LOGS, "_log");
            Map<String, Block> vanillaLogs = Map.of(
                    "oak", Blocks.OAK_LOG,
                    "birch", Blocks.BIRCH_LOG,
                    "spruce", Blocks.SPRUCE_LOG,
                    "jungle", Blocks.JUNGLE_LOG,
                    "acacia", Blocks.ACACIA_LOG,
                    "dark_oak", Blocks.DARK_OAK_LOG);

            LOTRBlocks.ALL_BEAMS.forEach(beam -> {
                String stem = stem(beam, "_beam");
                Block log = logsByStem.getOrDefault(stem, vanillaLogs.get(stem));
                if (log != null && !factionOnly(beam)) {
                    verticalStack(beam, 3, log, 3);
                }
            });
        }

        private void smoothStone() {
            Map<String, Block> byId = allById();
            LOTRBlocks.ALL_COLUMNS.forEach(smooth -> {
                String id = id(smooth);
                if (!id.startsWith("smooth_")) {
                    return;
                }
                Block base = byId.get(id.substring("smooth_".length()));
                if (base != null && !factionOnly(smooth)) {
                    verticalStack(smooth, 2, base, 2);
                }
            });
        }

        private void cutFromBase() {
            LOTRBlocks.SLAB_BASE.forEach((slab, base) -> {
                if (factionOnly(slab)) {
                    return;
                }
                shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                        .pattern("XXX")
                        .define('X', base)
                        .unlockedBy(getHasName(base), has(base))
                        .save(output);
            });

            LOTRBlocks.STAIRS_BASE.forEach((stairs, base) -> {
                if (factionOnly(stairs)) {
                    return;
                }
                shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                        .pattern("X  ")
                        .pattern("XX ")
                        .pattern("XXX")
                        .define('X', base)
                        .unlockedBy(getHasName(base), has(base))
                        .save(output);
            });

            LOTRBlocks.WALL_BASE.forEach((wall, base) -> {
                if (factionOnly(wall)) {
                    return;
                }
                shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                        .pattern("XXX")
                        .pattern("XXX")
                        .define('X', base)
                        .unlockedBy(getHasName(base), has(base))
                        .save(output);
            });
        }

        // Crafted only at a faction table in 1.7.10; LOTRTranscribedRecipes emits those recipes instead.
        private static boolean factionOnly(Block block) {
            return LOTRTranscribedRecipes.FACTION_ONLY.contains(BuiltInRegistries.BLOCK.getKey(block));
        }

        private void verticalStack(Block result, int count, Block ingredient, int rows) {
            var builder = shaped(RecipeCategory.BUILDING_BLOCKS, result, count);
            for (int i = 0; i < rows; i++) {
                builder.pattern("X");
            }
            builder.define('X', ingredient)
                    .unlockedBy(getHasName(ingredient), has(ingredient))
                    .save(output);
        }

        private static String id(Block block) {
            return BuiltInRegistries.BLOCK.getKey(block).getPath();
        }

        private static String stem(Block block, String suffix) {
            String s = id(block);
            return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s;
        }

        private static Map<String, Block> stems(Iterable<Block> blocks, String suffix) {
            Map<String, Block> map = new HashMap<>();
            blocks.forEach(b -> map.put(stem(b, suffix), b));
            return map;
        }

        private static Map<String, Block> allById() {
            Map<String, Block> map = new HashMap<>();
            LOTRBlocks.ALL_BLOCKS.forEach(b -> map.put(id(b), b));
            return map;
        }
    }
}