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
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

// Crafting recipes for the blocks tab. Every recipe here is mechanical -- derived from the family lists in LOTRBlocks rather than transcribed one at a time -- because the original was mechanical too. LOTRRecipes.java has ~366 block recipes and they all reduce to a handful of shapes, every one identical to its vanilla equivalent: 4 planks   <- 1 log                       shapeless 3 beams    <- 3 logs                      X / X / X 2 smooth   <- 2 rock                      X / X 6 slabs    <- 3 base                      XXX 4 stairs   <- 6 base                      X.. / XX. / XXX 6 walls    <- 6 base                      XXX / XXX SCOPE -- deliberately not covered yet: - Pillars. They look like they should follow the vertical-three shape from their matching brick, but in 1.7.10 almost every pillar was crafted from plain vanilla stone regardless of appearance. Deriving angmar_pillar <- angmar_brick would invent a recipe the mod never had. - Brick recipes (4 bricks <- 4 rock). Not derivable from names: dwarven brick comes from blue rock, not any "dwarven rock", and several bricks have no rock source. Wants a BRICK_BASE map in LOTRBlocks. - Faction gating. In 1.7.10 the faction brick stairs/slabs/walls lived in per-faction lists and could only be crafted at that faction's table. Everything here goes on the vanilla table. That is a design decision still to be settled, not an oversight.
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

    private static class Recipes extends RecipeProvider {
        Recipes(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
            super(registryLookup, exporter);
        }

        @Override
        public void buildRecipes() {
            planksFromLogs();
            beamsFromLogs();
            smoothStone();
            charcoal();
            cutFromBase();
        }

        private void planksFromLogs() {
            Map<String, Block> logsByStem = stems(LOTRBlocks.ALL_LOGS, "_log");
            LOTRBlocks.ALL_PLANKS.forEach(planks -> {
                Block log = logsByStem.get(stem(planks, "_planks"));
                if (log == null) {
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
                if (log != null) {
                    verticalStack(beam, 3, log, 3);
                }
            });
        }

        // Vanilla smelts any log to charcoal: 0.15 xp, 200 ticks, MISC.
        private void charcoal() {
            LOTRBlocks.ALL_LOGS.forEach(log ->
                    SimpleCookingRecipeBuilder.smelting(Ingredient.of(log), RecipeCategory.MISC,
                                    CookingBookCategory.MISC, Items.CHARCOAL, 0.15F, 200)
                            .unlockedBy(getHasName(log), has(log))
                            .save(output, id(log) + "_to_charcoal"));
        }

        private void smoothStone() {
            Map<String, Block> byId = allById();
            LOTRBlocks.ALL_COLUMNS.forEach(smooth -> {
                String id = id(smooth);
                if (!id.startsWith("smooth_")) {
                    return;
                }
                Block base = byId.get(id.substring("smooth_".length()));
                if (base != null) {
                    verticalStack(smooth, 2, base, 2);
                }
            });
        }

        private void cutFromBase() {
            LOTRBlocks.SLAB_BASE.forEach((slab, base) ->
                    shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                            .pattern("XXX")
                            .define('X', base)
                            .unlockedBy(getHasName(base), has(base))
                            .save(output));

            LOTRBlocks.STAIRS_BASE.forEach((stairs, base) ->
                    shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                            .pattern("X  ")
                            .pattern("XX ")
                            .pattern("XXX")
                            .define('X', base)
                            .unlockedBy(getHasName(base), has(base))
                            .save(output));

            LOTRBlocks.WALL_BASE.forEach((wall, base) ->
                    shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                            .pattern("XXX")
                            .pattern("XXX")
                            .define('X', base)
                            .unlockedBy(getHasName(base), has(base))
                            .save(output));
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