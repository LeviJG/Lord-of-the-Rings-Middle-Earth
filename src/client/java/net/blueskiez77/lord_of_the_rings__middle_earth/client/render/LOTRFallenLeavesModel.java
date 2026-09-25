package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.util.TriState;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderBlocks.renderFallenLeaves, the fancy-graphics path: six to ten
 * small pieces of the leaf texture, each 2-6 pixels a side, cut from a random
 * spot of the sprite, turned to a random angle and laid on the ground at a
 * random place, a little higher each. The randomness is seeded from the block
 * position exactly as the original seeded its Random, so a given spot always
 * shows the same scatter.
 *
 * <p>Shaded to 0.7 and, for vanilla's leaves, tinted like them (tint index 0).
 * No ambient occlusion and no directional shading: the original drew these
 * with the block's mixed brightness and a flat colour.
 */
public class LOTRFallenLeavesModel implements BlockStateModel {

    private static final int MIN_LEAVES = 6;
    private static final int MAX_LEAVES = 10;
    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 6;
    /** The shade the original multiplied the colour by. */
    private static final int SHADE = 0xFFB2B2B2;

    private final Material.Baked leaf;
    private final boolean tinted;
    private final @BakedQuad.MaterialFlags int flags;

    public LOTRFallenLeavesModel(Material.Baked leaf, boolean tinted) {
        this.leaf = leaf;
        this.tinted = tinted;
        // As LOTRGateModel.flagsOf: what the sprite itself needs.
        int flags = 0;
        if (leaf.forceTranslucent()
                || leaf.sprite().contents().computeTransparency(0.0f, 0.0f, 1.0f, 1.0f).hasTranslucent()) {
            flags |= BakedQuad.FLAG_TRANSLUCENT;
        }
        if (leaf.sprite().contents().isAnimated()) {
            flags |= BakedQuad.FLAG_ANIMATED;
        }
        this.flags = flags;
    }

    /** MathHelper.getRandomIntegerInRange. */
    private static int range(Random random, int min, int max) {
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
        long seed = pos.getX() * 237690503L ^ pos.getZ() * 2689286L ^ pos.getY();
        seed = seed * seed * 1732965593L + seed * 673L;
        Random rand = new Random(seed);

        int leaves = range(rand, MIN_LEAVES, MAX_LEAVES);
        for (int l = 0; l < leaves; ++l) {
            float posX = rand.nextFloat();
            float posZ = rand.nextFloat();
            float posY = 0.01f + (float) l / leaves * 0.1f;
            float rotation = rand.nextFloat() * Mth.PI * 2.0f;
            int xSize = range(rand, MIN_SIZE, MAX_SIZE);
            int zSize = range(rand, MIN_SIZE, MAX_SIZE);
            int minU = range(rand, 0, 16 - xSize);
            int minV = range(rand, 0, 16 - zSize);
            float x2 = xSize / 16.0f / 2.0f;
            float z2 = zSize / 16.0f / 2.0f;

            // Vec3.rotateAroundY: x' = x cos + z sin, z' = z cos - x sin.
            float cos = Mth.cos(rotation);
            float sin = Mth.sin(rotation);
            float[][] corners = {{-x2, -z2}, {-x2, z2}, {x2, z2}, {x2, -z2}};
            float[][] uvs = {
                    {minU / 16.0f, minV / 16.0f},
                    {minU / 16.0f, (minV + zSize) / 16.0f},
                    {(minU + xSize) / 16.0f, (minV + zSize) / 16.0f},
                    {(minU + xSize) / 16.0f, minV / 16.0f}};
            for (int v = 0; v < 4; ++v) {
                float x = corners[v][0];
                float z = corners[v][1];
                emitter.pos(v, posX + x * cos + z * sin, posY, posZ + z * cos - x * sin);
                emitter.uv(v, uvs[v][0], uvs[v][1]);
                emitter.color(v, SHADE);
            }
            emitter.cullFace(null);
            emitter.nominalFace(Direction.UP);
            emitter.tintIndex(this.tinted ? 0 : -1);
            emitter.ambientOcclusion(TriState.FALSE);
            emitter.diffuseShade(false);
            emitter.materialBake(this.leaf, MutableQuadView.BAKE_NORMALIZED).emit();
        }
    }

    /** The scatter depends on the position, so each position is its own geometry. */
    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        record Key(BlockState state, BlockPos pos) {
        }
        return new Key(state, pos.immutable());
    }

    @Override
    public Material.Baked particleMaterial() {
        return this.leaf;
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return this.flags;
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                                      RandomSource random) {
        return this.flags;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
    }
}
