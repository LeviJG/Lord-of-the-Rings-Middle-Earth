package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
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
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
     * Parented at lotr:block/chandelier rather than vanilla's block/cross: the
     * two are identical except vanilla adds rescale:true, which stretches the
     * planes to the full block diagonal and draws ~11% larger than 1.7.10's
     * renderCrossedSquares did. The fixture hangs because the texture's bottom
     * three rows are transparent, so the quads still span the full height.
     */
    private static final ModelTemplate CHANDELIER = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/chandelier")),
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

        // Saplings and flowers (cross model). NOT_TINTED because the LOTR art is
        // already coloured, unlike vanilla's greyscale foliage.
        LOTRBlocks.ALL_SAPLINGS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));
        LOTRBlocks.ALL_FLOWERS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        // Logs, beams and pillars are all axis-rotatable columns.
        // TexturedModel.COLUMN takes <name>_side for the shaft and <name>_top
        // for the cut end -- the same convention vanilla uses for quartz_pillar.
        //
        // createAxisAlignedPillarBlock emits the blockstate and block model but
        // NOT the item model, so register that too. (If datagen ever throws
        // "Duplicate model definition for lotr:item/<name>", it started emitting
        // one and this line should go.)
        List.of(LOTRBlocks.ALL_LOGS, LOTRBlocks.ALL_BEAMS, LOTRBlocks.ALL_PILLARS)
                .forEach(family -> family.forEach(b -> {
                    generators.createAxisAlignedPillarBlock(b, TexturedModel.COLUMN);
                    generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
                }));

        // Stairs. Each one borrows the texture of the block it was cut from,
        // which is recorded in LOTRBlocks.STAIRS_BASE.
        //
        // Built explicitly rather than via generators.family(base).stairs(...):
        // family() re-emits the base block's own model, and since we have
        // already generated that above it would throw "Duplicate model
        // definition".
        LOTRBlocks.ALL_STAIRS.forEach(stairs -> {
            Block base = LOTRBlocks.STAIRS_BASE.get(stairs);
            TextureMapping tex = TextureMapping.cube(base);
            Identifier inner = ModelTemplates.STAIRS_INNER.create(stairs, tex, generators.modelOutput);
            Identifier straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, tex, generators.modelOutput);
            Identifier outer = ModelTemplates.STAIRS_OUTER.create(stairs, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs,
                    BlockModelGenerators.plainVariant(inner),
                    BlockModelGenerators.plainVariant(straight),
                    BlockModelGenerators.plainVariant(outer)));
            generators.registerSimpleItemModel(stairs, straight);
        });

        // Slabs. The double-slab state reuses the base block's own model rather
        // than generating a duplicate full cube.
        LOTRBlocks.ALL_SLABS.forEach(slab -> {
            Block base = LOTRBlocks.SLAB_BASE.get(slab);
            TextureMapping tex = TextureMapping.cube(base);
            Identifier bottom = ModelTemplates.SLAB_BOTTOM.create(slab, tex, generators.modelOutput);
            Identifier top = ModelTemplates.SLAB_TOP.create(slab, tex, generators.modelOutput);
            Identifier full = ModelLocationUtils.getModelLocation(base);
            generators.blockStateOutput.accept(BlockModelGenerators.createSlab(slab,
                    BlockModelGenerators.plainVariant(bottom),
                    BlockModelGenerators.plainVariant(top),
                    BlockModelGenerators.plainVariant(full)));
            generators.registerSimpleItemModel(slab, bottom);
        });

        // Fences and walls. Both are multipart blockstates (post + one arm per
        // connected side) and, like stairs and slabs, borrow the base texture.
        LOTRBlocks.ALL_FENCES.forEach(fence -> {
            Block base = LOTRBlocks.FENCE_BASE.get(fence);
            TextureMapping tex = TextureMapping.defaultTexture(base);
            Identifier post = ModelTemplates.FENCE_POST.create(fence, tex, generators.modelOutput);
            Identifier side = ModelTemplates.FENCE_SIDE.create(fence, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createFence(fence,
                    BlockModelGenerators.plainVariant(post),
                    BlockModelGenerators.plainVariant(side)));
            Identifier inv = ModelTemplates.FENCE_INVENTORY.create(fence, tex, generators.modelOutput);
            generators.registerSimpleItemModel(fence, inv);
        });

        LOTRBlocks.ALL_WALLS.forEach(wall -> {
            Block base = LOTRBlocks.WALL_BASE.get(wall);
            // WALL_POST/LOW_SIDE/TALL_SIDE declare a slot named #wall, which
            // TextureMapping.defaultTexture (slot #texture) does not fill.
            // columnWithWall fills #wall (plus #texture/#side/#end, which these
            // templates simply ignore -- only missing slots are an error).
            TextureMapping tex = TextureMapping.columnWithWall(base);
            Identifier post = ModelTemplates.WALL_POST.create(wall, tex, generators.modelOutput);
            Identifier low = ModelTemplates.WALL_LOW_SIDE.create(wall, tex, generators.modelOutput);
            Identifier tall = ModelTemplates.WALL_TALL_SIDE.create(wall, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createWall(wall,
                    BlockModelGenerators.plainVariant(post),
                    BlockModelGenerators.plainVariant(low),
                    BlockModelGenerators.plainVariant(tall)));
            Identifier inv = ModelTemplates.WALL_INVENTORY.create(wall, tex, generators.modelOutput);
            generators.registerSimpleItemModel(wall, inv);
        });

        // Smooth stone: distinct top texture, no axis property. Same
        // <name>_side / <name>_top pair the columns use.
        // createTrivialBlock(block, provider) does not exist here, so both of
        // these build their model explicitly and hand it to createSimpleBlock.
        LOTRBlocks.ALL_COLUMNS.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_COLUMN.create(
                    b, TextureMapping.column(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        // Crafting tables: <name>_side on all four sides, <name>_top on the lid.
        LOTRBlocks.ALL_CRAFTING_TABLES.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_TOP.create(
                    b, TextureMapping.cubeTop(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        // Vines: multipart, one face per side. Ladders: a single flat model.
        LOTRBlocks.ALL_VINES.forEach(b -> generators.createMultiface(b));
        generators.createTrivialCube(LOTRBlocks.WEB_UNGOLIANT);
        generators.registerSimpleItemModel(LOTRBlocks.WEB_UNGOLIANT,
                ModelLocationUtils.getModelLocation(LOTRBlocks.WEB_UNGOLIANT));
        LOTRBlocks.ALL_LADDERS.forEach(b -> {
            Identifier model = ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(b.asItem()),
                    TextureMapping.layer0(b), generators.modelOutput);
            generators.registerSimpleItemModel(b, model);
        });

        // Gates: plain cubes for now (see LOTRBlocks#registerGate).
        // Fence gates: same texture as the matching fence.
        LOTRBlocks.ALL_FENCE_GATES.forEach(gate -> {
            TextureMapping tex = TextureMapping.defaultTexture(gate);
            Identifier open = ModelTemplates.FENCE_GATE_OPEN.create(gate, tex, generators.modelOutput);
            Identifier closed = ModelTemplates.FENCE_GATE_CLOSED.create(gate, tex, generators.modelOutput);
            Identifier wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(gate, tex, generators.modelOutput);
            Identifier wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(gate, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createFenceGate(gate,
                    BlockModelGenerators.plainVariant(open), BlockModelGenerators.plainVariant(closed),
                    BlockModelGenerators.plainVariant(wallOpen), BlockModelGenerators.plainVariant(wallClosed),
                    true));
            generators.registerSimpleItemModel(gate, closed);
        });

        // Buttons and pressure plates: reuse the base block's texture.
        LOTRBlocks.ALL_BUTTONS.forEach(generators::createButton);
        LOTRBlocks.ALL_PRESSURE_PLATES.forEach(generators::createPressurePlate);

        // Crops. CropBlock has eight ages but the original art has three or
        // four stages, so several ages share a texture.
        LOTRBlocks.ALL_CROPS.forEach(crop -> {
            int stages = LOTRBlocks.CROP_STAGES.get(crop);
            int[] ageToModel = new int[8];
            for (int age = 0; age < 8; age++) {
                ageToModel[age] = Math.min(age * stages / 8, stages - 1);
            }
            generators.createCropBlock(crop, BlockStateProperties.AGE_7, ageToModel);
        });

        LOTRBlocks.ALL_BUSHES.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        LOTRBlocks.ALL_GATES.forEach(b -> {
            generators.createTrivialCube(b);
            generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
        });

        LOTRBlocks.ALL_CARPETS.forEach(b -> {
            Identifier model = ModelTemplates.CARPET.create(
                    b, TextureMapping.wool(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        // Torches. createNormalTorch emits both the standing and wall models
        // plus the blockstates; the flat inventory icon comes from the torch's
        // own block texture.
        LOTRBlocks.ALL_TORCHES.forEach(torch ->
                generators.createNormalTorch(torch, LOTRBlocks.TORCH_WALL.get(torch)));

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

        // Flat 2D inventory icon, not the block model. Backing the item with the
        // crossed-squares model renders it at full block scale, which is far too
        // large in hand and on the ground; 1.7.10 drew render-type-1 blocks as a
        // flat icon too. layer0 points at the BLOCK texture (there is no
        // textures/item/<name>.png), exactly as vanilla's flower items do.
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(block.asItem()),
                TextureMapping.layer0(block),
                generators.modelOutput);
        generators.registerSimpleItemModel(block, itemModel);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(LOTRItems.MITHRIL, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(LOTRItems.PIPEWEED, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public String getName() {
        return "LOTR Model Provider";
    }
}