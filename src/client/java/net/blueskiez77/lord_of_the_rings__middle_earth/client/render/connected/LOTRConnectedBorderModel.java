package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.connected;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

/**
 * Draws a block with a neighbour-aware border, reproducing the 1.7.10
 * connected-texture effect for any family described by a
 * {@link LOTRConnectedBorderType}.
 *
 * Responsibilities are split so the fragile part stays small:
 *   - which pieces a face draws        -> LOTRConnectedBorder (pure, verified)
 *   - what counts as a neighbour       -> LOTRConnectedBorderType.Matcher
 *   - which world block is "top-left"  -> faceUp/faceRight below
 *   - turning pieces into quads        -> this class
 *
 * 26.2 notes: this is a BlockStateModel, not a BakedModel (which no longer
 * exists). FabricBlockStateModel is injected onto BlockStateModel via Mixin, so
 * emitQuads/createGeometryKey/materialFlags below are overrides even though
 * BlockStateModel itself does not declare them. Sprites are addressed as
 * Material.Baked, obtained at bake time from ModelBaker#materials and applied
 * with QuadEmitter#materialBake -- the old spriteBake/RenderMaterial/BlendMode
 * trio is gone.
 */
public class LOTRConnectedBorderModel implements BlockStateModel {

    /**
     * How far outside the face the border overlays sit.
     *
     * DEVIATION FROM THE ORIGINAL, deliberately: 1.7.10 composited the base and
     * its border pieces into a single image per neighbour combination
     * (LOTRConnectedTextures#createConnectedIcons) and drew one quad per face.
     * Doing that here would mean generating composites and stitching them onto
     * the atlas. Layering separate quads is far simpler, but coplanar quads
     * z-fight, so the overlays are nudged a fraction of a pixel outward.
     *
     * The pieces occupy almost disjoint pixels (an edge is one row, a corner a
     * few pixels), so one shared offset is enough.
     */
    private static final float OVERLAY_DEPTH = -0.001f;

    private final LOTRConnectedBorderType type;
    private final Material.Baked baseMaterial;
    private final Map<LOTRConnectedBorder.Piece, Material.Baked> pieceMaterials;
    private final @BakedQuad.MaterialFlags int staticMaterialFlags;

    public LOTRConnectedBorderModel(LOTRConnectedBorderType type,
                                    Material.Baked baseMaterial,
                                    Map<LOTRConnectedBorder.Piece, Material.Baked> pieceMaterials) {
        this.type = type;
        this.baseMaterial = baseMaterial;
        this.pieceMaterials = new EnumMap<>(pieceMaterials);

        @BakedQuad.MaterialFlags int flags = flagsOf(baseMaterial);

        for (Material.Baked material : pieceMaterials.values()) {
            flags |= flagsOf(material);
        }

        this.staticMaterialFlags = flags;
    }

    /**
     * Translucency/animation flags for one material, following the pattern in
     * Fabric's own PillarBlockStateModel. The overlays are cutouts rather than
     * translucent, but computing this properly costs nothing and keeps animated
     * textures working.
     */
    private static @BakedQuad.MaterialFlags int flagsOf(Material.Baked material) {
        int flags = 0;

        if (material.forceTranslucent()
                || material.sprite().contents().computeTransparency(0.0f, 0.0f, 1.0f, 1.0f).hasTranslucent()) {
            flags |= BakedQuad.FLAG_TRANSLUCENT;
        }

        if (material.sprite().contents().isAnimated()) {
            flags |= BakedQuad.FLAG_ANIMATED;
        }

        return flags;
    }

    // ------------------------------------------------------------------
    // Face-relative axes
    //
    // The eight neighbours sampled for a face must lie in that face's plane and
    // be labelled top/bottom/left/right the way the texture artist meant. That
    // labelling is face-relative, so each world face needs its own in-plane
    // axes: faceUp is the world direction the texture's top points toward,
    // faceRight the direction its right points toward.
    //
    // Derived from the per-side coordinate switch in the original's
    // getConnectedIconBlock rather than guessed. Note DOWN uses the same axes as
    // UP: cases 0 and 1 fall through together in the original, and mirroring
    // DOWN instead flips the whole bottom-face border.
    // ------------------------------------------------------------------

    private static Direction faceUp(Direction face) {
        return switch (face) {
            case UP, DOWN -> Direction.NORTH;
            default -> Direction.UP;
        };
    }

    private static Direction faceRight(Direction face) {
        return switch (face) {
            case UP, DOWN, SOUTH -> Direction.EAST;
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case EAST -> Direction.NORTH;
        };
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
        for (Direction face : Direction.values()) {
            emitter.square(face, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f)
                    .materialBake(baseMaterial, MutableQuadView.BAKE_LOCK_UV)
                    .emit();

            for (LOTRConnectedBorder.Piece piece : piecesFor(level, pos, state, face)) {
                emitter.square(face, 0.0f, 0.0f, 1.0f, 1.0f, OVERLAY_DEPTH)
                        .materialBake(pieceMaterials.get(piece), MutableQuadView.BAKE_LOCK_UV)
                        .emit();
            }
        }
    }

    /**
     * Lets the chunk builder cache geometry: two blocks whose six faces resolve
     * to the same piece sets can share a mesh.
     */
    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        record Key(Set<LOTRConnectedBorder.Piece> down, Set<LOTRConnectedBorder.Piece> up,
                   Set<LOTRConnectedBorder.Piece> north, Set<LOTRConnectedBorder.Piece> south,
                   Set<LOTRConnectedBorder.Piece> west, Set<LOTRConnectedBorder.Piece> east) {
        }

        return new Key(
                piecesFor(level, pos, state, Direction.DOWN),
                piecesFor(level, pos, state, Direction.UP),
                piecesFor(level, pos, state, Direction.NORTH),
                piecesFor(level, pos, state, Direction.SOUTH),
                piecesFor(level, pos, state, Direction.WEST),
                piecesFor(level, pos, state, Direction.EAST));
    }

    @Override
    public Material.Baked particleMaterial() {
        return baseMaterial;
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return staticMaterialFlags;
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                                      RandomSource random) {
        return staticMaterialFlags;
    }

    /** Sample the eight in-plane neighbours of one face and resolve the pieces. */
    private Set<LOTRConnectedBorder.Piece> piecesFor(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                                     Direction face) {
        Direction up = faceUp(face);
        Direction down = up.getOpposite();
        Direction right = faceRight(face);
        Direction left = right.getOpposite();

        return LOTRConnectedBorder.piecesFor(
                connects(level, pos, state, pos.relative(up).relative(left)),
                connects(level, pos, state, pos.relative(up)),
                connects(level, pos, state, pos.relative(up).relative(right)),
                connects(level, pos, state, pos.relative(left)),
                connects(level, pos, state, pos.relative(right)),
                connects(level, pos, state, pos.relative(down).relative(left)),
                connects(level, pos, state, pos.relative(down)),
                connects(level, pos, state, pos.relative(down).relative(right)));
    }

    private boolean connects(BlockAndTintGetter level, BlockPos selfPos, BlockState selfState, BlockPos otherPos) {
        return type.matcher().connects(level, selfPos, selfState, otherPos, level.getBlockState(otherPos));
    }

    /**
     * All geometry is emitted through emitQuads, so there are no static parts to
     * hand to the vanilla path.
     */
    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
    }
}