package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRReplacedMethods.BlockRendering.renderStandardBlock (config "Natural blocks"): the ground's
 * blocks drawn with each listed face's texture turned a random quarter, by the block's position,
 * so the ground does not tile. Each face turns on its own, within its own texture, as the
 * original's uvRotate did; the option is read as the chunk is drawn, so turning it off (and the
 * chunks redrawn) draws them plain.
 *
 * <p>The table is the original's setupNaturalBlockTable, by the blocks it named or the classes
 * whose blocks it took in, as the port has them. Only blocks drawn as standard blocks were
 * turned, so stairs and walls never were; the slabs were. Where the original's block is now
 * vanilla's in its modern form (the dirt path), vanilla's look stands.
 */
public final class LOTRNaturalBlockModel implements BlockStateModel {

    private static final int ALL_SIDES = 0b111111;
    /** Bit by Direction.get3DDataValue: down 0, up 1. */
    private static final int TOP_AND_BOTTOM = 0b000011;

    private static final Map<Identifier, Integer> TABLE = new HashMap<>();

    static {
        // BlockGrass, BlockMycelium, LOTRBlockMudGrass: top and bottom.
        top("minecraft", "grass_block", "mycelium");
        top(LOTRMod.NAMESPACE, "mud_grass");
        // Blocks.dirt 0 and 1 (dirt, coarse dirt) all round; 2 (podzol) top and bottom.
        all("minecraft", "dirt", "coarse_dirt");
        top("minecraft", "podzol");
        // LOTRBlockSlabDirt, LOTRBlockMud, LOTRBlockMordorDirt, LOTRBlockDirtPath (its mud path),
        // LOTRBlockMordorMoss.
        all(LOTRMod.NAMESPACE, "dirt_slab", "dirt_path_slab", "mud_slab", "mordor_dirt_slab", "dirt_path_mud_slab",
                "mud", "barren_jungle_mud", "mordor_dirt", "dirt_path_mud", "mordor_moss");
        // BlockSand, LOTRBlockSand, LOTRBlockSlabSand.
        all("minecraft", "sand", "red_sand");
        all(LOTRMod.NAMESPACE, "white_sand", "sand_slab", "red_sand_slab", "white_sand_slab");
        // Sandstone 0, its slab (stone_slab 1, single and double); LOTRBlockSandstone (red and white
        // sandstone) and their slabs (slabSingle7 5, slabSingle10 6); the Mordor rock (rock 0).
        top("minecraft", "sandstone", "sandstone_slab", "red_sandstone", "red_sandstone_slab");
        top(LOTRMod.NAMESPACE, "white_sandstone", "white_sandstone_slab", "mordor_rock");
        // BlockGravel, LOTRBlockSlabGravel (the mod's gravel blocks themselves were plain Blocks).
        all("minecraft", "gravel");
        all(LOTRMod.NAMESPACE, "gravel_slab", "mordor_gravel_slab", "obsidian_gravel_slab");
        // BlockClay, BlockSnow, BlockSnowBlock.
        all("minecraft", "clay", "snow", "snow_block");
    }

    private static void all(String namespace, String... paths) {
        for (String path : paths) {
            TABLE.put(Identifier.fromNamespaceAndPath(namespace, path), ALL_SIDES);
        }
    }

    private static void top(String namespace, String... paths) {
        for (String path : paths) {
            TABLE.put(Identifier.fromNamespaceAndPath(namespace, path), TOP_AND_BOTTOM);
        }
    }

    private static final ThreadLocal<Random> BLOCK_RAND = ThreadLocal.withInitial(Random::new);

    private final BlockStateModel model;
    private final int sides;
    private final List<TextureAtlasSprite> sprites;

    private LOTRNaturalBlockModel(BlockStateModel model, int sides, List<TextureAtlasSprite> sprites) {
        this.model = model;
        this.sides = sides;
        this.sprites = sprites;
    }

    public static void init() {
        ModelLoadingPlugin.register(context -> context.modifyBlockModelAfterBake().register((model, bake) -> {
            Block block = bake.state().getBlock();
            Integer sides = TABLE.get(BuiltInRegistries.BLOCK.getKey(block));
            return sides == null ? model : new LOTRNaturalBlockModel(model, sides, spritesOf(model));
        }));
    }

    /** The textures the model's faces are drawn from, to turn a face within its own. */
    private static List<TextureAtlasSprite> spritesOf(BlockStateModel model) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(RandomSource.create(0L), parts);
        List<TextureAtlasSprite> sprites = new ArrayList<>();
        for (BlockStateModelPart part : parts) {
            for (Direction direction : Direction.values()) {
                for (BakedQuad quad : part.getQuads(direction)) {
                    if (!sprites.contains(quad.materialInfo().sprite())) {
                        sprites.add(quad.materialInfo().sprite());
                    }
                }
            }
        }
        return sprites;
    }

    /** The quarter turns of the six faces, by Direction.get3DDataValue, packed two bits a face. */
    private static int randomSides(BlockPos pos) {
        Random blockRand = BLOCK_RAND.get();
        int hash = pos.getX() * 234890405 ^ pos.getZ() * 37383934 ^ pos.getY();
        int packed = 0;
        for (int l = 0; l < 6; ++l) {
            blockRand.setSeed(hash + l * 285502L);
            blockRand.setSeed(blockRand.nextLong());
            packed |= blockRand.nextInt(4) << (l * 2);
        }
        return packed;
    }

    private @Nullable TextureAtlasSprite spriteAt(float u, float v) {
        for (TextureAtlasSprite sprite : this.sprites) {
            if (u >= sprite.getU0() && u <= sprite.getU1() && v >= sprite.getV0() && v <= sprite.getV1()) {
                return sprite;
            }
        }
        return null;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (!LOTRConfig.naturalBlocks) {
            this.model.emitQuads(emitter, level, pos, state, random, cullTest);
            return;
        }
        int randomSides = randomSides(pos);
        emitter.pushTransform(quad -> {
            Direction face = quad.nominalFace();
            if (face == null || (this.sides & (1 << face.get3DDataValue())) == 0) {
                return true;
            }
            int turns = (randomSides >> (face.get3DDataValue() * 2)) & 3;
            if (turns == 0) {
                return true;
            }
            float centreU = (quad.u(0) + quad.u(1) + quad.u(2) + quad.u(3)) / 4.0f;
            float centreV = (quad.v(0) + quad.v(1) + quad.v(2) + quad.v(3)) / 4.0f;
            TextureAtlasSprite sprite = spriteAt(centreU, centreV);
            if (sprite == null) {
                return true;
            }
            float u0 = sprite.getU0();
            float v0 = sprite.getV0();
            float du = sprite.getU1() - u0;
            float dv = sprite.getV1() - v0;
            for (int vertex = 0; vertex < 4; ++vertex) {
                // Within the texture, a quarter turn about its centre at a time.
                float s = (quad.u(vertex) - u0) / du;
                float t = (quad.v(vertex) - v0) / dv;
                for (int k = 0; k < turns; ++k) {
                    float turned = 1.0f - t;
                    t = s;
                    s = turned;
                }
                quad.uv(vertex, u0 + s * du, v0 + t * dv);
            }
            return true;
        });
        this.model.emitQuads(emitter, level, pos, state, random, cullTest);
        emitter.popTransform();
    }

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        record Key(Object model, int randomSides) {
        }
        return new Key(this.model.createGeometryKey(level, pos, state, random),
                LOTRConfig.naturalBlocks ? randomSides(pos) : 0);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
        this.model.collectParts(random, parts);
    }

    @Override
    public Material.Baked particleMaterial() {
        return this.model.particleMaterial();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return this.model.materialFlags();
    }
}
