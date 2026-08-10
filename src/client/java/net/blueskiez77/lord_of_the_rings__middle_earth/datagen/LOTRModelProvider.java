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

// Blockstates, block models, item models. Everything is driven off the family lists in LOTRBlocks, so a block only ever needs registering in one place. Which generators emit the ITEM model too (learned the hard way): createTrivialCube            -> NO  (needs registerSimpleItemModel) createNonTemplateModelBlock  -> NO  (needs registerSimpleFlatItemModel) createCrossBlockWithDefaultItem -> YES (the "WithDefaultItem" suffix) createTrapdoor               -> YES createDoor                   -> YES (points at assets/<ns>/textures/item/<n>.png) Adding a redundant item-model call throws "IllegalStateException: Duplicate model definition".
public class LOTRModelProvider extends FabricModelProvider {
    private static final ModelTemplate CHANDELIER = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/chandelier")),
            Optional.empty(),
            TextureSlot.CROSS);

    // True for blocks textured as a column: <name>_side plus <name>_top, with no bare <name>.png. Anything cut from one of these has to map its faces by hand rather than using TextureMapping.cube().

    private static Block texOf(Block block) {
        return LOTRBlocks.TEXTURE_SOURCE.getOrDefault(block, block);
    }

    private static boolean isColumnTextured(Block base) {
        return LOTRBlocks.ALL_PILLARS.contains(base)
                || LOTRBlocks.ALL_LOGS.contains(base)
                || LOTRBlocks.ALL_BEAMS.contains(base)
                || LOTRBlocks.ALL_COLUMNS.contains(base)
                || LOTRBlocks.ALL_SOIL_COLUMNS.contains(base);
    }

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        LOTRBlocks.ALL_CUBES.stream()
                .filter(b -> !LOTRConnectedBorderTypes.has(b))
                .forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_PLANKS.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_LEAVES.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_GLASS.forEach(b -> trivialCubeWithItem(generators, b));

        LOTRBlocks.ALL_SAPLINGS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));
        LOTRBlocks.ALL_FLOWERS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        // "Duplicate model definition for lotr:item/<name>", it started emitting

        List.of(LOTRBlocks.ALL_LOGS, LOTRBlocks.ALL_BEAMS, LOTRBlocks.ALL_PILLARS)
                .forEach(family -> family.forEach(b -> {
                    generators.createAxisAlignedPillarBlock(b, TexturedModel.COLUMN);
                    generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
                }));

        // already generated that above it would throw "Duplicate model

        LOTRBlocks.ALL_STAIRS.forEach(stairs -> {
            Block base = LOTRBlocks.STAIRS_BASE.get(stairs);
            Block texBase = texOf(base);
            TextureMapping tex = isColumnTextured(base)
                    ? new TextureMapping()
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(texBase, "_top"))
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(texBase, "_top"))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(texBase, "_side"))
                    : TextureMapping.cube(texBase);
            Identifier inner = ModelTemplates.STAIRS_INNER.create(stairs, tex, generators.modelOutput);
            Identifier straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, tex, generators.modelOutput);
            Identifier outer = ModelTemplates.STAIRS_OUTER.create(stairs, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs,
                    BlockModelGenerators.plainVariant(inner),
                    BlockModelGenerators.plainVariant(straight),
                    BlockModelGenerators.plainVariant(outer)));
            generators.registerSimpleItemModel(stairs, straight);
        });

        // than generating a duplicate full cube.
        LOTRBlocks.ALL_SLABS.forEach(slab -> {
            Block base = LOTRBlocks.SLAB_BASE.get(slab);

            // <name>_top -- there is no bare <name>.png -- so a slab cut from

            // .cube() would point at a file that does not exist.
            Block texBase = texOf(base);
            TextureMapping tex = isColumnTextured(base)
                    ? new TextureMapping()
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(texBase, "_top"))
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(texBase, "_top"))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(texBase, "_side"))
                    : TextureMapping.cube(texBase);
            Identifier bottom = ModelTemplates.SLAB_BOTTOM.create(slab, tex, generators.modelOutput);
            Identifier top = ModelTemplates.SLAB_TOP.create(slab, tex, generators.modelOutput);
            Identifier full = ModelLocationUtils.getModelLocation(base);
            generators.blockStateOutput.accept(BlockModelGenerators.createSlab(slab,
                    BlockModelGenerators.plainVariant(bottom),
                    BlockModelGenerators.plainVariant(top),
                    BlockModelGenerators.plainVariant(full)));
            generators.registerSimpleItemModel(slab, bottom);
        });

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

            // Column-textured bases (bone) have no bare texture, so #wall has

            // NOTE FOR LEVI: TextureSlot.WALL is the one name here I could not

            TextureMapping tex = isColumnTextured(base)
                    ? new TextureMapping().put(TextureSlot.WALL,
                    TextureMapping.getBlockTexture(texOf(base), "_side"))
                    : TextureMapping.columnWithWall(texOf(base));
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

        // createTrivialBlock(block, provider) does not exist here, so both of

        LOTRBlocks.ALL_COLUMNS.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_COLUMN.create(
                    b, TextureMapping.column(texOf(b)), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        LOTRBlocks.ALL_BOTTOM_TOP.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(
                    b, TextureMapping.cubeBottomTop(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        LOTRBlocks.ALL_SOIL_COLUMNS.forEach(b -> {
            Block texSource = LOTRBlocks.SOIL_COLUMN_TEXTURE.getOrDefault(b, b);
            Identifier model = ModelTemplates.CUBE_COLUMN.create(
                    b, TextureMapping.column(texSource), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        LOTRBlocks.ALL_CRAFTING_TABLES.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_TOP.create(
                    b, TextureMapping.cubeTop(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

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

        LOTRBlocks.ALL_FENCE_GATES.forEach(gate -> {
            // which does not exist -- gates borrow the plank texture like

            Block base = LOTRBlocks.FENCE_GATE_BASE.get(gate);
            TextureMapping tex = TextureMapping.defaultTexture(base);
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

        LOTRBlocks.ALL_BUTTONS.forEach(button -> {
            Block base = LOTRBlocks.BUTTON_BASE.get(button);
            TextureMapping tex = TextureMapping.defaultTexture(base);
            Identifier unpressed = ModelTemplates.BUTTON.create(button, tex, generators.modelOutput);
            Identifier pressed = ModelTemplates.BUTTON_PRESSED.create(button, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createButton(button,
                    BlockModelGenerators.plainVariant(unpressed),
                    BlockModelGenerators.plainVariant(pressed)));
            Identifier inv = ModelTemplates.BUTTON_INVENTORY.create(button, tex, generators.modelOutput);
            generators.registerSimpleItemModel(button, inv);
        });

        LOTRBlocks.ALL_PRESSURE_PLATES.forEach(plate -> {
            Block base = LOTRBlocks.PRESSURE_PLATE_BASE.get(plate);
            TextureMapping tex = TextureMapping.defaultTexture(base);
            Identifier up = ModelTemplates.PRESSURE_PLATE_UP.create(plate, tex, generators.modelOutput);
            Identifier down = ModelTemplates.PRESSURE_PLATE_DOWN.create(plate, tex, generators.modelOutput);
            generators.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(plate,
                    BlockModelGenerators.plainVariant(up),
                    BlockModelGenerators.plainVariant(down)));
            generators.registerSimpleItemModel(plate, up);
        });

        final int[] ageToModel = {0, 0, 1, 1, 2, 2, 2, 3};
        LOTRBlocks.ALL_CROPS.forEach(crop ->
                generators.createCropBlock(crop, BlockStateProperties.AGE_7, ageToModel));

        LOTRBlocks.ALL_BUSHES.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        LOTRBlocks.ALL_GATES.forEach(b -> {
            generators.createTrivialCube(b);
            generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
        });

        LOTRBlocks.ALL_PATHS.forEach(b -> {
            var tex = TextureMapping.getBlockTexture(b);
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.TOP, tex)
                    .put(TextureSlot.SIDE, tex);
            Identifier model = ModelTemplates.CUBE_TOP.create(b, mapping, generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        LOTRBlocks.ALL_FARMLAND.forEach(b -> {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(LOTRBlocks.MUD))
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(b));
            Identifier dry = ModelTemplates.FARMLAND.create(b, mapping, generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(dry)));
            generators.registerSimpleItemModel(b, dry);
        });

        LOTRBlocks.ALL_RAILS.forEach(b -> {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.RAIL, TextureMapping.getBlockTexture(b));
            Identifier flat = ModelTemplates.RAIL_FLAT.create(b, mapping, generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(flat)));

            // which does not exist -- hence the blank hotbar icon.
            Identifier itemModel = ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(b.asItem()),
                    TextureMapping.layer0(b),
                    generators.modelOutput);
            generators.registerSimpleItemModel(b, itemModel);
        });

        LOTRBlocks.ALL_CARPETS.forEach(b -> {
            Identifier model = ModelTemplates.CARPET.create(
                    b, TextureMapping.wool(b), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        LOTRBlocks.ALL_TORCHES.forEach(torch ->
                generators.createNormalTorch(torch, LOTRBlocks.TORCH_WALL.get(torch)));

        LOTRBlocks.ALL_TRAPDOORS.forEach(generators::createTrapdoor);

        LOTRBlocks.ALL_DOORS.forEach(generators::createDoor);

        LOTRBlocks.ALL_BARS.forEach(generators::createBarsAndItem);

        LOTRBlocks.ALL_CHANDELIERS.forEach(b -> chandelier(generators, b));

        LOTRConnectedBorderTypes.all().forEach((block, type) -> connectedBorder(generators, block, type));
    }

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

    // Chandeliers are static (no blockstate properties), so a single variant pointing at the cross model is all the blockstate needs. The item is backed by that same block model (as the cubes are), NOT a flat item model: the textures live in textures/block/, and a flat item model would look for textures/item/<name>.png, which does not exist -> blank icon. Pointing the item at the block model renders the 3D cross in the inventory, the way vanilla torches and lanterns show.
    private static void chandelier(BlockModelGenerators generators, Block block) {
        Identifier model = CHANDELIER.create(block, TextureMapping.cross(block), generators.modelOutput);
        generators.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));

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