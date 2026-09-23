package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

// LOTRRecipes.registerOres put every LOTR plank, log, wooden slab, wooden stair, leaf and sapling into the Forge ore dictionary (plankWood, logWood, slabWood, stairWood, treeLeaves, treeSapling), so they worked in every vanilla recipe that asked for "any planks" -- sticks, crafting tables, chests, bowls. The 26.2 equivalent is the item tag, and without these copies only the block tags existed: LOTR planks could not make a stick.
//
// The fence, fence gate, door and trapdoor tags come along so vanilla recipes and checks that ask for "any wooden fence" etc. see the LOTR ones too. Fuel does not depend on them: LOTRBlockBehaviours.fuel() registers burn times per block directly.
public class LOTRItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public LOTRItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture,
                               FabricTagsProvider.BlockTagsProvider blockTags) {
        super(output, registriesFuture, blockTags);
    }

    // BlockTags has no constant for these two in 26.2; ItemTags does.
    private static TagKey<Block> vanillaBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(path));
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(vanillaBlockTag("logs_that_burn"), ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(vanillaBlockTag("saplings"), ItemTags.SAPLINGS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
    }
}
