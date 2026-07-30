package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderType;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * Blockstates, block models, item models.
 *
 * Everything is driven off the family lists in LOTRBlocks, so a block only ever
 * needs registering in one place.
 *
 * Which generators emit the ITEM model too (learned the hard way):
 *   createTrivialCube            -> NO  (needs registerSimpleItemModel)
 *   createNonTemplateModelBlock  -> NO  (needs registerSimpleFlatItemModel)
 *   createCrossBlockWithDefaultItem -> YES (the "WithDefaultItem" suffix)
 *   createTrapdoor               -> YES
 *   createDoor                   -> YES (points at assets/<ns>/textures/item/<n>.png)
 * Adding a redundant item-model call throws
 * "IllegalStateException: Duplicate model definition".
 */
public class LOTRModelProvider extends FabricModelProvider {

    /**
     * Chandelier model: crossed quads with the block's own texture.
     *
     * Parented to vanilla minecraft:block/cross, which defines both crossed
     * planes correctly and carries the item display transforms -- so each
     * generated model renders both beams in-world AND produces a proper flat
     * inventory icon. The template fills the "cross" texture slot per block.
     *
     * (The 1.7.10 fixture hung 3/16 up from the floor; vanilla cross sits on
     * the floor. The offset is barely visible and not worth a bespoke model
     * that has to re-derive the crossed geometry by hand.)
     */
    private static final ModelTemplate CHANDELIER = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("block/cross")),
            Optional.empty(),
            TextureSlot.CROSS);

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        // Solid cubes, planks, leaves and glass all use the same cube_all model.
        //
        // Connected-border blocks are skipped here and handled below: their
        // world model is installed in code and their textures live in
        // block/ctm/, so the default block/<name> lookup would miss.
        LOTRBlocks.ALL_CUBES.stream()
                .filter(b -> !LOTRConnectedBorderTypes.has(b))
                .forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_PLANKS.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_LEAVES.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_GLASS.forEach(b -> trivialCubeWithItem(generators, b));

        // Saplings (cross model)
        LOTRBlocks.ALL_SAPLINGS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        LOTRBlocks.ALL_TRAPDOORS.forEach(generators::createTrapdoor);

        // createDoor emits blockstate, all block models, AND the item model,
        // which points at assets/lotr/textures/item/<n>.png
        LOTRBlocks.ALL_DOORS.forEach(generators::createDoor);

        LOTRBlocks.ALL_BARS.forEach(generators::createBarsAndItem);

        LOTRBlocks.ALL_CHANDELIERS.forEach(b -> chandelier(generators, b));

        LOTRConnectedBorderTypes.all().forEach((block, type) -> connectedBorder(generators, block, type));
    }

    /**
     * Item model for a connected-border block.
     *
     * The world model is built in code (LOTRConnectedBorderModel), so nothing
     * here needs to describe the border. What is needed is an item model, and
     * LOTRConnectedBorderType#itemTexture supplies the sprite: the composited
     * no-neighbours combination, i.e. the base with its full frame drawn on.
     * That is one of the 47 sprites the sprite source generates, NOT a shipped
     * file -- there is no <base>_item.png to keep in sync.
     *
     * That matches the original: LOTRBlockOreStorage served its inventory icon
     * from the same IIcon set as the world block (the noBase flag in
     * getConnectedIconBlock existed for exactly this), so there was never a
     * second copy of the texture to keep in sync.
     */
    private static void connectedBorder(BlockModelGenerators generators, Block block,
                                        LOTRConnectedBorderType type) {
        Identifier model = ModelTemplates.CUBE_ALL.create(
                block, TextureMapping.cube(new Material(type.itemTexture())), generators.modelOutput);
        generators.registerSimpleItemModel(block, model);
    }

    private static void trivialCubeWithItem(BlockModelGenerators generators, Block block) {
        generators.createTrivialCube(block);
        generators.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
    }

    /**
     * Chandeliers are static (no blockstate properties), so a single variant
     * pointing at the cross model is all the blockstate needs.
     *
     * The item is backed by that same block model (as the cubes are), NOT a
     * flat item model: the textures live in textures/block/, and a flat item
     * model would look for textures/item/<name>.png, which does not exist ->
     * blank icon. Pointing the item at the block model renders the 3D cross in
     * the inventory, the way vanilla torches and lanterns show.
     */
    private static void chandelier(BlockModelGenerators generators, Block block) {
        Identifier model = CHANDELIER.create(block, TextureMapping.cross(block), generators.modelOutput);
        generators.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        generators.registerSimpleItemModel(block, model);
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