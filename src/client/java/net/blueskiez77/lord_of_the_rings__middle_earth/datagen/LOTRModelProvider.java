package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import java.util.ArrayList;
import net.minecraft.client.renderer.block.dispatch.Variant;
import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderType;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRGateBorders;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBerryBushBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMechanisedRailBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPillarBlock;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFallenLeavesBlock;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.FoliageColor;

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

    /** LOTRRenderBlocks.renderStalactite's two shapes, textured with the model block's. */
    private static final ModelTemplate STALACTITE = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/template_stalactite")),
            Optional.empty(),
            TextureSlot.ALL);
    private static final ModelTemplate STALAGMITE = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/template_stalagmite")),
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
        generators.createFurnace(LOTRUtilityBlocks.HOBBIT_OVEN, TexturedModel.ORIENTABLE_ONLY_TOP);
        generators.registerSimpleItemModel(LOTRUtilityBlocks.HOBBIT_OVEN,
                ModelLocationUtils.getModelLocation(LOTRUtilityBlocks.HOBBIT_OVEN));

        // Beacon of Gondor. Its blockstate and block models are hand-written
        // under src/main/resources (custom pyre geometry, not a cube), but the
        // ITEM model is generated here like every other block's. Item models
        // live in assets/lotr/items/ in this version, not models/item/, and
        // hand-placing one there kept going wrong -- this is the same call the
        // forges use, pointed at the hand-written block model.
        generators.registerSimpleItemModel(LOTRUtilityBlocks.BEACON_OF_GONDOR,
                ModelLocationUtils.getModelLocation(LOTRUtilityBlocks.BEACON_OF_GONDOR));

        LOTRBlocks.ALL_CUBES.stream()
                .filter(b -> !LOTRConnectedBorderTypes.has(b))
                .filter(b -> !LOTRBlocks.CUBES_COLUMN_TEXTURED.contains(b))
                .filter(b -> b != LOTRBuildingBlocks.WASTE_BLOCK)
                .forEach(b -> trivialCubeWithItem(generators, b));

        // LOTRBlockWaste.getIcon picked one of eight textures for EACH FACE from
        // a hash of the position and side. A blockstate can only pick a whole
        // model at random, so there are eight models, model k giving face f
        // texture (k + 3f) mod 8: every model shows a mix, and every texture
        // turns up on every face somewhere. waste_block is the original's _var0.
        List<TextureSlot> faces = List.of(TextureSlot.DOWN, TextureSlot.UP, TextureSlot.NORTH,
                TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.EAST);
        List<Variant> wasteModels = new ArrayList<>();
        for (int k = 0; k < 8; k++) {
            TextureMapping tex = new TextureMapping();
            for (int f = 0; f < faces.size(); f++) {
                int t = (k + 3 * f) % 8;
                tex.put(faces.get(f), TextureMapping.getBlockTexture(LOTRBuildingBlocks.WASTE_BLOCK, t == 0 ? "" : "_" + t));
            }
            tex.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(LOTRBuildingBlocks.WASTE_BLOCK));
            wasteModels.add(new Variant(ModelTemplates.CUBE.createWithSuffix(LOTRBuildingBlocks.WASTE_BLOCK,
                    k == 0 ? "" : "_" + k, tex, generators.modelOutput)));
        }
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(LOTRBuildingBlocks.WASTE_BLOCK,
                BlockModelGenerators.variants(wasteModels.toArray(new Variant[0]))));
        generators.registerSimpleItemModel(LOTRBuildingBlocks.WASTE_BLOCK,
                ModelLocationUtils.getModelLocation(LOTRBuildingBlocks.WASTE_BLOCK));

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
        // NOT trivialCubeWithItem: createGlassBlocks below emits the glass cube
        // AND its pane together, so doing the cube here as well would throw a
        // duplicate model definition.

        LOTRBlocks.ALL_SAPLINGS.forEach(b ->
                generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));
        // The six tall grasses are greyscale in the texture sheet and take the
        // biome grass colour at render time, exactly as LOTRBlockTallGrass's
        // colorMultiplier did -- so they need the tinted cross, not the plain
        // one, and an item model that carries a constant grass tint.
        //
        // Three of them (flowery grass, wheatgrass, thistle) also had a second,
        // untinted overlay icon in 1.7.10 for their flowers, seed heads and
        // bristles. A generated model cannot express two stacked crosses, so
        // those three are hand-written under src/main/resources and skipped
        // here; see lotr:block/tinted_cross_overlay.
        LOTRBlocks.ALL_FLOWERS.forEach(b -> {
            // Hand-written under src/main/resources: the three overlay grasses,
            // the reeds and corn (whose models depend on where they sit in
            // their column), the riverweed (a flat pad) and the grapevine post
            // (a 4x4 post) and the clovers (a stem plus stacked flat petals)
            // -- none of which are plain crosses.
            if (LOTRBlocks.GRASS_TINTED_WITH_OVERLAY.contains(b)
                    || LOTRBlocks.ALL_COLUMN_PLANTS.contains(b)
                    || b == LOTRDecorationBlocks.FANGORN_RIVERWEED
                    || b == LOTRDecorationBlocks.GRAPEVINE
                    || LOTRBlocks.ALL_CLOVERS.contains(b)) {
                return;
            }
            if (LOTRBlocks.GRASS_TINTED.contains(b)) {
                // Block model tinted, and the ITEM model tinted too -- the
                // default item model carries no tint, so the inventory icon
                // would stay grey. Vanilla's own short_grass item does exactly
                // this: a flat sprite with a constant grass colour at
                // temperature 0.5 / downfall 1.0.
                generators.createCrossBlock(b, BlockModelGenerators.PlantType.TINTED);
                generators.registerSimpleTintedItemModel(b,
                        generators.createFlatItemModelWithBlockTexture(b.asItem(), b),
                        new GrassColorSource(0.5F, 1.0F));
                return;
            }
            generators.createCrossBlockWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED);
        });

        // Vanilla's peony treatment: a cross per half, and the top half as the
        // inventory icon -- LOTRItemDoubleFlower.getIconFromDamage drew the top.
        LOTRBlocks.ALL_DOUBLE_FLOWERS.forEach(b ->
                generators.createDoublePlantWithDefaultItem(b, BlockModelGenerators.PlantType.NOT_TINTED));

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

        // createMultiface writes the multipart blockstate but NOT the model it
        // points at -- vanilla's block/vine is hand-written. All four LOTR vines
        // were referring to a model that did not exist, so they drew nothing.
        // The models are hand-written under src/main/resources to match; only
        // the blockstate is generated here.
        LOTRBlocks.ALL_VINES.forEach(b -> generators.createMultiface(b));
        // Ungoliant's web is vanilla's cobweb: a cross, not a cube, with a flat
        // sprite for the item. createTrivialCube was drawing it as a solid
        // block of web.
        generators.createCrossBlock(LOTRDecorationBlocks.WEB_UNGOLIANT,
                BlockModelGenerators.PlantType.NOT_TINTED);
        generators.registerSimpleFlatItemModel(LOTRDecorationBlocks.WEB_UNGOLIANT);
        LOTRBlocks.ALL_LADDERS.forEach(b -> {
            // The two ropes' item models are hand-written: LOTRBlockRope
            // overrode getItemIconName, and items/rope.png and
            // items/hithlainLadder.png are coiled ropes rather than the hanging
            // strand their block sprites draw. Their blockstates and block
            // models are hand-written too -- a rope is a cord with a knot, not
            // the flat panel a ladder is.
            if (b == LOTRDecorationBlocks.ROPE || b == LOTRDecorationBlocks.HITHLAIN_LADDER) {
                return;
            }
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
        // LOTRBlockLettuceCrop.getRenderType returned 1, a cross; every other
        // crop kept BlockCrops' '#' shape.
        LOTRBlocks.ALL_CROPS.forEach(crop -> {
            if (crop == LOTRUtilityBlocks.LETTUCE_CROP) {
                // Not createCrossBlock: unlike createCropBlock it makes a model per
                // AGE value, so two ages sharing a stage define the same model twice.
                java.util.Map<Integer, Identifier> stages = new java.util.HashMap<>();
                generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(crop)
                        .with(PropertyDispatch.initial(BlockStateProperties.AGE_7).generate(age -> {
                            int stage = ageToModel[age];
                            return BlockModelGenerators.plainVariant(stages.computeIfAbsent(stage, st ->
                                    BlockModelGenerators.PlantType.NOT_TINTED.getCross().createWithSuffix(
                                            crop, "_stage" + st,
                                            TextureMapping.cross(TextureMapping.getBlockTexture(crop, "_stage" + st)),
                                            generators.modelOutput)));
                        })));
            } else {
                generators.createCropBlock(crop, BlockStateProperties.AGE_7, ageToModel);
            }
        });

        // Berry bushes are full cubes drawn like leaves, not crosses -- 1.7.10
        // never overrode getRenderType on LOTRBlockBerryBush. Two textures per
        // bush, picked by HAS_BERRIES: <name> when ripe, <name>_bare when not.
        // The weapon rack has no block model at all -- it is RenderShape.INVISIBLE
        // and drawn entirely by its block entity renderer. Only the item needs
        // a model, and that is hand-written too.
        //
        // Bird cages and the butterfly jar are hand-written under src/main/resources: renderBirdCage
        // builds them out of thin walls with every face drawn, plus the post
        // and finial on the lid, none of which a cube template can express.

        LOTRBlocks.ALL_BUSHES.forEach(b -> {
            Identifier ripe = ModelTemplates.CUBE_ALL.create(b,
                    TextureMapping.cube(TextureMapping.getBlockTexture(b)), generators.modelOutput);
            Identifier bare = ModelTemplates.CUBE_ALL.createWithSuffix(b, "_bare",
                    TextureMapping.cube(TextureMapping.getBlockTexture(b, "_bare")),
                    generators.modelOutput);
            generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(b)
                    .with(PropertyDispatch.initial(LOTRBerryBushBlock.HAS_BERRIES)
                            .select(true, BlockModelGenerators.plainVariant(ripe))
                            .select(false, BlockModelGenerators.plainVariant(bare))));
            // A bush in the inventory is bare: LOTRBlockBerryBush.getDrops
            // always handed back setHasBerries(meta, false).
            generators.registerSimpleItemModel(b, bare);
        });

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
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(LOTRBuildingBlocks.MUD));
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
                            .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(LOTRBuildingBlocks.MUD))
                            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(b)),
                    generators.modelOutput);
            Identifier moist = ModelTemplates.FARMLAND.createWithSuffix(b, "_moist", new TextureMapping()
                            .put(TextureSlot.DIRT, TextureMapping.getBlockTexture(LOTRBuildingBlocks.MUD))
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

        mechanisedRails(generators);

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

        // Glass panes wear the matching glass block's texture on their faces and
        // glass_pane_top on the exposed edge, the way vanilla's do. The item is
        // a flat sprite of the glass, not a picture of the pane.
        LOTRBlocks.ALL_GLASS_PANES.forEach(pane -> {
            Block glass = LOTRBlocks.GLASS_PANE_BASE.get(pane);
            generators.createGlassBlocks(glass, pane);
        });

        LOTRBlocks.ALL_CHANDELIERS.forEach(b -> chandelier(generators, b));
        // Never drawn, never targeted or broken; the blockstate only has to
        // exist. Its particle is never seen, so any real texture will do.
        generators.createParticleOnlyBlock(LOTRDecorationBlocks.MARSH_LIGHTS, Blocks.LILY_PAD);
        trivialCubeWithItem(generators, LOTRDecorationBlocks.GORAN);
        trivialCubeWithItem(generators, LOTRDecorationBlocks.GORAN_ROCK);
        stalactites(generators, LOTRDecorationBlocks.STALACTITE, LOTRDecorationBlocks.STALAGMITE, Blocks.STONE);
        stalactites(generators, LOTRDecorationBlocks.ICE_STALACTITE, LOTRDecorationBlocks.ICE_STALAGMITE, Blocks.PACKED_ICE);
        stalactites(generators, LOTRDecorationBlocks.OBSIDIAN_STALACTITE, LOTRDecorationBlocks.OBSIDIAN_STALAGMITE, Blocks.OBSIDIAN);

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
    // getIcon returned the model block's own icon, so the item is drawn with
    // the block model, as renderInvStalactite did.
    private static void stalactites(BlockModelGenerators generators, Block hanging, Block standing, Block model) {
        TextureMapping texture = TextureMapping.cube(model);
        for (Block block : List.of(hanging, standing)) {
            Identifier id = (block == hanging ? STALACTITE : STALAGMITE).create(block, texture, generators.modelOutput);
            generators.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(id)));
            generators.registerSimpleItemModel(block, id);
        }
    }

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
        LOTRFlatItemModels.generate(generators);
        LOTRBlocks.ALL_FALLEN_LEAVES.forEach(block -> fallenLeavesItem(generators, block));
    }

    @Override
    public String getName() {
        return "LOTR Model Provider";
    }

    /**
     * A fallen-leaves item is the flat icon of its leaf, tinted as vanilla
     * tints that leaf's own item. The block has no blockstate file: its look
     * is LOTRFallenLeavesModel, which the client plugin hands it.
     */
    private static void fallenLeavesItem(ItemModelGenerators generators, Block block) {
        Block leaves = ((LOTRFallenLeavesBlock) block).leaves();
        Identifier leafId = BuiltInRegistries.BLOCK.getKey(leaves);
        Identifier model = ModelTemplates.FLAT_ITEM.create(block.asItem(), TextureMapping.layer0(leaves),
                generators.modelOutput);
        if (!leafId.getNamespace().equals("minecraft")) {
            generators.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
            return;
        }
        int tint = leaves == Blocks.SPRUCE_LEAVES ? FoliageColor.FOLIAGE_EVERGREEN
                : leaves == Blocks.BIRCH_LEAVES ? FoliageColor.FOLIAGE_BIRCH
                : FoliageColor.FOLIAGE_DEFAULT;
        generators.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.tintedModel(model, ItemModelUtils.constantTint(tint)));
    }

    /**
     * The two mechanised rails share one set of models: mechanised_rail and its
     * raised pair for a stopped rail, and the "_on" set -- the animated
     * texture -- for a running one. Unlike createActiveRail, the lit look
     * follows isPowerOn rather than POWERED, so the on rail lights up with no
     * signal and goes dark when one reaches it.
     */
    private static void mechanisedRails(BlockModelGenerators generators) {
        Block source = LOTRUtilityBlocks.MECHANISED_RAIL;
        MultiVariant flat = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        MultiVariant raisedNE = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
        MultiVariant raisedSW = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
        MultiVariant flatOn = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        MultiVariant raisedNEOn = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "_on", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
        MultiVariant raisedSWOn = BlockModelGenerators.plainVariant(generators.createSuffixedVariant(source, "_on", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
        generators.registerSimpleFlatItemModel(source);
        for (Block block : LOTRBlocks.ALL_RAILS) {
            LOTRMechanisedRailBlock rail = (LOTRMechanisedRailBlock) block;
            generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                    .with(PropertyDispatch.initial(BlockStateProperties.POWERED, BlockStateProperties.RAIL_SHAPE_STRAIGHT)
                            .generate((powered, shape) -> {
                                boolean on = rail.isPowerOn(rail.defaultBlockState().setValue(BlockStateProperties.POWERED, powered));
                                return switch (shape) {
                                    case NORTH_SOUTH -> on ? flatOn : flat;
                                    case EAST_WEST -> (on ? flatOn : flat).with(BlockModelGenerators.Y_ROT_90);
                                    case ASCENDING_EAST -> (on ? raisedNEOn : raisedNE).with(BlockModelGenerators.Y_ROT_90);
                                    case ASCENDING_WEST -> (on ? raisedSWOn : raisedSW).with(BlockModelGenerators.Y_ROT_90);
                                    case ASCENDING_NORTH -> on ? raisedNEOn : raisedNE;
                                    case ASCENDING_SOUTH -> on ? raisedSWOn : raisedSW;
                                    default -> throw new UnsupportedOperationException("Rail shape " + shape + " is not straight");
                                };
                            })));
        }
    }
}
