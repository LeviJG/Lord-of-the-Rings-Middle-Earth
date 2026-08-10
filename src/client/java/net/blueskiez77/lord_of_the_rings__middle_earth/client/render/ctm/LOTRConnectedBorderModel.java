package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

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

public class LOTRConnectedBorderModel implements BlockStateModel {
    private final LOTRConnectedBorderType type;
    private final Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> composited;
    private final Material.Baked isolatedMaterial;
    private final @BakedQuad.MaterialFlags int staticMaterialFlags;

    public LOTRConnectedBorderModel(LOTRConnectedBorderType type,
                                    Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> composited) {
        this.type = type;
        this.composited = Map.copyOf(composited);

        this.isolatedMaterial = this.composited.get(
                LOTRConnectedBorder.piecesFor(false, false, false, false, false, false, false, false));

        @BakedQuad.MaterialFlags int flags = 0;

        for (Material.Baked material : this.composited.values()) {
            flags |= flagsOf(material);
        }

        this.staticMaterialFlags = flags;
    }

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

    // The eight neighbours sampled for a face must lie in that face's plane and

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
            Material.Baked material = composited.getOrDefault(
                    piecesFor(level, pos, state, face), isolatedMaterial);

            emitter.square(face, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f)
                    .materialBake(material, MutableQuadView.BAKE_LOCK_UV)
                    .emit();
        }
    }

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
        return isolatedMaterial;
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

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
    }
}