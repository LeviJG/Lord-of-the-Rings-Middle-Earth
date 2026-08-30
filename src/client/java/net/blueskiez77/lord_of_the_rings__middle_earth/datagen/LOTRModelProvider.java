package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderType;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRGateBorders;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPillarBlock;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

// Blockstates, block models, item models. Everything is driven off the family lists in LOTRBlocks, so a block only ever needs registering in one place. Which generators emit the ITEM model too (learned the hard way): createTrivialCube            -> NO  (needs registerSimpleItemModel) createNonTemplateModelBlock  -> NO  (needs registerSimpleFlatItemModel) createCrossBlockWithDefaultItem -> YES (the "WithDefaultItem" suffix) createTrapdoor               -> YES createDoor                   -> YES (points at assets/<ns>/textures/item/<n>.png) Adding a redundant item-model call throws "IllegalStateException: Duplicate model definition".
public class LOTRModelProvider extends FabricModelProvider {
    // Parented to vanilla dirt_path so the 15/16 height comes with it. CUBE_TOP
    // is a full 16px block, which is why the LOTR paths sat a pixel proud.
    private static final ModelTemplate DIRT_PATH = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("block/dirt_path")),
            Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);

    // The hand-written panel shape in assets/lotr/models/block/gate_panel.json:
    // a 4/16-thick slab, matching LOTRBlockGate.setBlockBoundsForItemRender.
    private static final ModelTemplate GATE_PANEL = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/gate_panel")),
            Optional.empty(),
            TextureSlot.ALL);

    private static final ModelTemplate CHANDELIER = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/chandelier")),
            Optional.empty(),
            TextureSlot.CROSS);

    // True for blocks textured as a column: <name>_side plus <name>_top, with no bare <name>.png. Anything cut from one of these has to map its faces by hand rather than using TextureMapping.cube().

    private static Block texOf(Block block) {
        return LOTRBlocks.TEXTURE_SOURCE.getOrDefault(block, block);
    }

    // Column families (logs, pillars, smooth stone) put <name>_top on a cut
    // end. The getIcon-swapping cubes (mordor rock, the steel blocks) only ever
    // had two sprites, so their end is the bare <name>.
    private static net.minecraft.client.resources.model.sprite.Material columnEnd(Block base, Block texBase) {
        return LOTRBlocks.CUBES_COLUMN_TEXTURED.contains(base)
                ? TextureMapping.getBlockTexture(texBase)
                : TextureMapping.getBlockTexture(texBase, "_top");
    }

    // Faces for anything cut from a base block. Three shapes, and getting this
    // wrong is silent: bottom/top bases (sandstone, hearth) keep three distinct
    // faces, column bases have no bare <name>.png at all, everything else is a
    // plain cube.
    // Bottom, top and side for a three-faced block. The side is the bare
    // <name>, matching the art as shipped.
    private static TextureMapping bottomTopMapping(Block block) {
        return new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block));
    }

    private static TextureMapping cutFaceMapping(Block base, Block texBase) {
        // Vanilla dirt path is a three-faced block and has no bare
        // minecraft:block/dirt_path, so anything cut from it must name its
        // faces. It is the only vanilla base in this port that behaves that way.
        if (base == Blocks.DIRT_PATH) {
            return new TextureMapping()
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.DIRT))
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DIRT_PATH, "_top"))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.DIRT_PATH, "_side"));
        }
        if (LOTRBlocks.ALL_BOTTOM_TOP.contains(base)) {
            return bottomTopMapping(texBase);
        }
        if (isColumnTextured(base)) {
            return new TextureMapping()
                    .put(TextureSlot.BOTTOM, columnEnd(base, texBase))
                    .put(TextureSlot.TOP, columnEnd(base, texBase))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(texBase, "_side"));
        }
        return TextureMapping.cube(texBase);
    }

    // <name>_top on the ends, and one of four side textures:
    //   alone         <name>_side          both edges
    //   pillar below  <name>_side_top      top edge only
    //   pillar above  <name>_side_bottom   bottom edge only
    //   both          <name>_side_middle   no edges
    // Names follow LOTRBlockPillarBase's _side / _sideTop / _sideBottom /
    // _sideMiddle, snake_cased.
    private static void pillarWithSegments(BlockModelGenerators generators, Block block) {
        Identifier alone = pillarSegment(generators, block, "_side", "");
        Identifier top = pillarSegment(generators, block, "_side_top", "_top_segment");
        Identifier bottom = pillarSegment(generators, block, "_side_bottom", "_bottom_segment");
        Identifier middle = pillarSegment(generators, block, "_side_middle", "_middle_segment");

        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(LOTRPillarBlock.UP, LOTRPillarBlock.DOWN)
                                .select(false, false, BlockModelGenerators.plainVariant(alone))
                                .select(false, true, BlockModelGenerators.plainVariant(top))
                                .select(true, false, BlockModelGenerators.plainVariant(bottom))
                                .select(true, true, BlockModelGenerators.plainVariant(middle))));

        // The inventory model is the standalone form, matching the original's
        // getIcon(int, int) which always returned the plain side icon.
        generators.registerSimpleItemModel(block, alone);
    }

    private static Identifier pillarSegment(BlockModelGenerators generators, Block block,
                                            String sideSuffix, String modelSuffix) {
        return ModelTemplates.CUBE_COLUMN.createWithSuffix(block, modelSuffix, new TextureMapping()
                        .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top"))
                        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, sideSuffix)),
                generators.modelOutput);
    }

    private static boolean isColumnTextured(Block base) {
        return LOTRBlocks.ALL_PILLARS.contains(base)
                || LOTRBlocks.ALL_LOGS.contains(base)
                || LOTRBlocks.ALL_BEAMS.contains(base)
                || LOTRBlocks.ALL_COLUMNS.contains(base)
                || LOTRBlocks.ALL_SOIL_COLUMNS.contains(base)
                || LOTRBlocks.CUBES_COLUMN_TEXTURED.contains(base);
    }

    public LOTRModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        // Forges: furnace-shaped. _front when idle, _active when lit, with
        // _side and _top around it.
        // Vanilla's own furnace generator: handles FACING rotation, the LIT
        // dispatch and the item model in one call. ORIENTABLE_ONLY_TOP wants
        // <name>_front / _side / _top, and createFurnace looks for
        // <name>_front_on when lit -- hence the _active -> _front_on rename.
        // NOTE FOR LEVI: if createFurnace or ORIENTABLE_ONLY_TOP does not
        // resolve, Ctrl-click BlockModelGenerators and search "furnace"; this
        // is one call and the texture names are already correct for it.
        LOTRBlocks.ALL_FORGES.forEach(b -> {
            generators.createFurnace(b, TexturedModel.ORIENTABLE_ONLY_TOP);
            // createFurnace emits the blockstate and models but no item model,
            // which is why the forges were a checkerboard in the inventory.
            // The model it creates sits at the block's default location.
            generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
        });

        // Dart traps: <name>_face on the facing side, the base brick on the
        // other five, rotated by FACING.
        LOTRBlocks.ALL_DART_TRAPS.forEach(b -> {
            Block base = LOTRBlocks.DART_TRAP_BASE.get(b);
            Identifier model = ModelTemplates.CUBE_ORIENTABLE.create(b, new TextureMapping()
                            .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(b, "_face"))
                            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(base))
                            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(base)),
                    generators.modelOutput);
            // ROTATION_HORIZONTAL_FACING is the same mutator createFurnace uses
            // for its FACING axis. Without it every state rendered the same
            // model, which is why the hole always pointed north.
            // CUBE_ORIENTABLE puts FRONT on the north face, which is the
            // orientation this mutator expects.
            generators.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(b, BlockModelGenerators.plainVariant(model))
                            .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
            generators.registerSimpleItemModel(b, model);
        });

        // Hobbit oven. Same shape as the forges: FACING + LIT, handled by
        // createFurnace, which expects the lit texture as <name>_front_on and
        // emits no item model of its own.
        generators.createFurnace(LOTRBlocks.HOBBIT_OVEN, TexturedModel.ORIENTABLE_ONLY_TOP);
        generators.registerSimpleItemModel(LOTRBlocks.HOBBIT_OVEN,
                ModelLocationUtils.getModelLocation(LOTRBlocks.HOBBIT_OVEN));

        // Beacon of Gondor. Its blockstate and block models are hand-written
        // under src/main/resources (custom pyre geometry, not a cube), but the
        // ITEM model is generated here like every other block's. Item models
        // live in assets/lotr/items/ in this version, not models/item/, and
        // hand-placing one there kept going wrong -- this is the same call the
        // forges use, pointed at the hand-written block model.
        generators.registerSimpleItemModel(LOTRBlocks.BEACON_OF_GONDOR,
                ModelLocationUtils.getModelLocation(LOTRBlocks.BEACON_OF_GONDOR));

        LOTRBlocks.ALL_CUBES.stream()
                .filter(b -> !LOTRConnectedBorderTypes.has(b))
                .filter(b -> !LOTRBlocks.CUBES_COLUMN_TEXTURED.contains(b))
                .forEach(b -> trivialCubeWithItem(generators, b));

        // Cubes whose four sides differ from top and bottom. In 1.7.10 these
        // were plain cubes overriding getIcon for side != 0/1; cube_column is
        // the modern equivalent, with <name> on the ends and <name>_side around.
        LOTRBlocks.CUBES_COLUMN_TEXTURED.forEach(b -> {
            Identifier model = ModelTemplates.CUBE_COLUMN.create(b, new TextureMapping()
                            .put(TextureSlot.END, TextureMapping.getBlockTexture(texOf(b)))
                            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(texOf(b), "_side")),
                    generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });
        LOTRBlocks.ALL_PLANKS.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_LEAVES.forEach(b -> trivialCubeWithItem(generators, b));
        LOTRBlocks.ALL_GLASS.forEach(b -> trivialCubeWithItem(generators, b));

        LOTRBlocks.ALL_SAPLINGS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));
        LOTRBlocks.ALL_FLOWERS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

        // "Duplicate model definition for lotr:item/<name>", it started emitting

        List.of(LOTRBlocks.ALL_LOGS, LOTRBlocks.ALL_BEAMS)
                .forEach(family -> family.forEach(b -> {
                    generators.createAxisAlignedPillarBlock(b, TexturedModel.COLUMN);
                    generators.registerSimpleItemModel(b, ModelLocationUtils.getModelLocation(b));
                }));

        // Pillars are segmented: the side texture loses its top or bottom edge
        // when another pillar is stacked against it, so a column reads as one
        // shaft. Four side variants, selected by the UP and DOWN properties.
        // The top and bottom faces never change -- they always use <name>_top.
        LOTRBlocks.ALL_PILLARS.forEach(b -> pillarWithSegments(generators, b));

        // already generated that above it would throw "Duplicate model

        LOTRBlocks.ALL_STAIRS.forEach(stairs -> {
            Block base = LOTRBlocks.STAIRS_BASE.get(stairs);
            Block texBase = texOf(base);
            TextureMapping tex = cutFaceMapping(base, texBase);
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
            TextureMapping tex = cutFaceMapping(base, texBase);
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
            // NOT TextureMapping.cubeBottomTop: that asks for <name>_side, but
            // these blocks keep the 1.7.10 convention where the bare <name> IS
            // the side (whiteSandstone.png was blockIcon, with _top/_bottom
            // beside it). Using the bare name avoids renaming shipped art.
            Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(
                    b, bottomTopMapping(b), generators.modelOutput);
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

        // Gates get no blockstate and no block model at all: LOTRConnectedBorderPlugin
        // registers a resolver for them, which replaces blockstate loading, and
        // LOTRGateModel builds the panel geometry in code so it can pick a
        // sprite per face from the block's neighbours. Only the item model is
        // generated -- the thin gate_panel shape wearing the fully framed
        // sprite, which is what LOTRBlockGate.getIcon(int, int) returned for
        // the inventory.
        //
        // The "_inventory" suffix matters: ModelTemplate.create() would name
        // this lotr:block/<gate>, which is the id the hand-written block models
        // used to occupy. Two source roots claiming one asset path is a
        // processResources duplicate, and the model here is an item icon
        // rather than the block, so it gets its own name either way.
        //
        // The dwarven doors are the exception: they fill their whole cube, so
        // setBlockBoundsForItemRender gave them a full block and getIcon(int,
        // int) gave them plain stone. Their icon is therefore an ordinary
        // cube_all in stone -- indistinguishable from a stone block in the
        // hotbar, which is the point of a hidden door.
        LOTRGateBorders.all().forEach(gate -> {
            ModelTemplate template = gate.isFullBlock() ? ModelTemplates.CUBE_ALL : GATE_PANEL;
            Identifier model = template.createWithSuffix(gate, "_inventory",
                    new TextureMapping().put(TextureSlot.ALL, new Material(LOTRGateBorders.itemTexture(gate))),
                    generators.modelOutput);
            generators.registerSimpleItemModel(gate, model);
        });

        LOTRBlocks.ALL_PATHS.forEach(b -> {
            var tex = TextureMapping.getBlockTexture(b);
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.TOP, tex)
                    .put(TextureSlot.SIDE, tex)
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(LOTRBlocks.MUD));
            Identifier model = DIRT_PATH.create(b, mapping, generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(b, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(b, model);
        });

        // Farmland swaps its top texture when watered, exactly as vanilla does
        // at moisture 7. Dispatching on MOISTURE rather than emitting one model
        // is why mud_farmland_moist.png was previously unreferenced.
        LOTRBlocks.ALL_FARMLAND.forEach(b -> {
            Identifier dry = ModelTemplates.FARMLAND.create(b, new TextureMapping()
                            .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(LOTRBlocks.MUD))
                            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(b)),
                    generators.modelOutput);
            Identifier moist = ModelTemplates.FARMLAND.createWithSuffix(b, "_moist", new TextureMapping()
                            .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(LOTRBlocks.MUD))
                            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(b, "_moist")),
                    generators.modelOutput);

            var dispatch = PropertyDispatch.initial(BlockStateProperties.MOISTURE);
            for (int moisture = 0; moisture <= 7; moisture++) {
                dispatch = dispatch.select(moisture,
                        BlockModelGenerators.plainVariant(moisture == 7 ? moist : dry));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(b).with(dispatch));
            // Farmland is obtainable again, so it needs an item model. The dry
            // variant is the inventory form.
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
        Block tex = texOf(block);
        if (tex != block) {
            // Borrows another block's texture (the gulduril bricks are a base
            // brick plus a glow, so they have no PNG of their own).
            // createTrivialCube always derives the texture from the block's own
            // name, so the model has to be built by hand here.
            Identifier model = ModelTemplates.CUBE_ALL.create(
                    block, TextureMapping.cube(tex), generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
            generators.registerSimpleItemModel(block, model);
            return;
        }
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