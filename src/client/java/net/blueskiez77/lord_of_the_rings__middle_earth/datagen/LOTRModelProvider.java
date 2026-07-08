package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

/**
 * Generates block models, blockstates, and item models as JSON.
 *
 * For the bulk of simple content this is a one-liner per entry:
 *  - cube-all blocks: blockStateModelGenerator.createTrivialCube(block)
 *  - flat items: itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM)
 *
 * As the ~1,500 simple entries land in LOTRBlocks/LOTRItems, they get a line
 * here; complex blocks (stairs, slabs, pillars, plants) use the matching
 * BlockModelGenerators helper. Textures still have to exist under
 * assets/lotr/textures/... — datagen writes the model JSON that points at them,
 * not the PNGs themselves.
 *
 * 26.1 note: FabricModelProvider's methods take BlockModelGenerators and
 * ItemModelGenerators (the generator classes moved to net.minecraft.client.data.models).
 */
public class LOTRModelProvider extends FabricModelProvider {

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createTrivialCube(LOTRBlocks.COPPER_ORE);
        generators.createTrivialCube(LOTRBlocks.TIN_ORE);
        generators.createTrivialCube(LOTRBlocks.SILVER_ORE);
        generators.createTrivialCube(LOTRBlocks.MITHRIL_ORE);
        generators.createTrivialCube(LOTRBlocks.SALT_ORE);
        generators.createTrivialCube(LOTRBlocks.SALTPETER_ORE);
        generators.createTrivialCube(LOTRBlocks.SULFUR_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(LOTRItems.MITHRIL, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LOTRItems.ATHELAS, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LOTRItems.PIPEWEED, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public String getName() {
        return "LOTR Model Provider";
    }
}