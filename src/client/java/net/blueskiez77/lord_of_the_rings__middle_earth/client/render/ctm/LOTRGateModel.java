package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

/**
 * A gate panel, drawn the way LOTRBlockGate drew one.
 *
 * <p>Three things separate this from {@link LOTRConnectedBorderModel}, which
 * handles the plain connected-border cubes:
 *
 * <ul>
 *   <li>The geometry is not a cube. A gate is a quarter-block slab whose
 *       position and axis come from FACING, so the box is rebuilt per state
 *       out of {@link LOTRGateBlock#shapeFor}.</li>
 *   <li>An OPEN gate drops the base layer. LOTRBlockGate.getIcon passed
 *       {@code noBase = open} straight through to the connected-texture
 *       lookup: the frame pieces still draw, the middle does not, and the
 *       result is the see-through rim of an opened gate. Without this an open
 *       gate looks identical to a closed one and only betrays itself when you
 *       walk through it.</li>
 *   <li>A gate registered with {@code hasConnectedTextures = false} -- the
 *       iron bars, bronze bars and wooden cross -- draws its flat sprite
 *       untouched while closed, and only borrows the frame pieces once open.
 *       That is the two-branch getIcon from the original.</li>
 * </ul>
 */
public class LOTRGateModel implements BlockStateModel {
    private final LOTRGateBlock block;

    /** Composited over the base: a closed connected-texture gate. */
    private final Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withBase;

    /** Composited over nothing: any open gate. */
    private final Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withoutBase;

    /** The untouched sprite, for a closed gate that has no connected art. */
    private final Material.Baked flat;

    private final Material.Baked particle;

    private final @BakedQuad.MaterialFlags int staticMaterialFlags;

    /** shapeFor() is state-independent bar the facing, so resolve all six once. */
    private final Map<Direction, AABB> boxes = new EnumMap<>(Direction.class);

    public LOTRGateModel(LOTRGateBlock block,
                         Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withBase,
                         Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withoutBase,
                         Material.Baked flat) {
        this.block = block;
        this.withBase = Map.copyOf(withBase);
        this.withoutBase = Map.copyOf(withoutBase);
        this.flat = flat;

        Set<LOTRConnectedBorder.Piece> isolated =
                LOTRConnectedBorder.piecesFor(false, false, false, false, false, false, false, false);
        this.particle = block.hasConnectedTextures() ? this.withBase.get(isolated) : flat;

        @BakedQuad.MaterialFlags int flags = flagsOf(flat);

        for (Material.Baked material : this.withBase.values()) {
            flags |= flagsOf(material);
        }

        for (Material.Baked material : this.withoutBase.values()) {
            flags |= flagsOf(material);
        }

        this.staticMaterialFlags = flags;

        for (Direction facing : Direction.values()) {
            boxes.put(facing, block.shapeFor(facing).bounds());
        }
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

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
        AABB box = boxes.get(state.getValue(LOTRGateBlock.FACING));
        boolean open = state.getValue(LOTRGateBlock.OPEN);

        for (Direction face : Direction.values()) {
            Material.Baked material = materialFor(level, pos, state, face, open);

            if (material == null) {
                continue;
            }

            // No cull face: a quarter-block panel is inset from every side of
            // its own cube, so nothing it draws may be hidden by a neighbour.
            emitter.cullFace(null);
            emitter.nominalFace(face);
            corners(emitter, face, box);
            // BAKE_LOCK_UV derives the texture coordinates from the vertex
            // positions projected onto the nominal face, which is what gives
            // the four narrow edges their 4px strip of the sprite for free.
            emitter.materialBake(material, MutableQuadView.BAKE_LOCK_UV).emit();
        }
    }

    private Material.@Nullable Baked materialFor(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                                 Direction face, boolean open) {
        if (!open && !block.hasConnectedTextures()) {
            return flat;
        }

        Set<LOTRConnectedBorder.Piece> pieces = piecesFor(level, pos, state, face);
        Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> source = open ? withoutBase : withBase;
        return source.get(pieces);
    }

    /**
     * Vertices counter-clockwise as seen from outside the block, matching the
     * winding vanilla's own face baker produces.
     */
    private static void corners(QuadEmitter emitter, Direction face, AABB box) {
        float x0 = (float) box.minX;
        float y0 = (float) box.minY;
        float z0 = (float) box.minZ;
        float x1 = (float) box.maxX;
        float y1 = (float) box.maxY;
        float z1 = (float) box.maxZ;

        switch (face) {
            case DOWN -> {
                emitter.pos(0, x0, y0, z0);
                emitter.pos(1, x1, y0, z0);
                emitter.pos(2, x1, y0, z1);
                emitter.pos(3, x0, y0, z1);
            }
            case UP -> {
                emitter.pos(0, x0, y1, z1);
                emitter.pos(1, x1, y1, z1);
                emitter.pos(2, x1, y1, z0);
                emitter.pos(3, x0, y1, z0);
            }
            case NORTH -> {
                emitter.pos(0, x1, y1, z0);
                emitter.pos(1, x1, y0, z0);
                emitter.pos(2, x0, y0, z0);
                emitter.pos(3, x0, y1, z0);
            }
            case SOUTH -> {
                emitter.pos(0, x0, y1, z1);
                emitter.pos(1, x0, y0, z1);
                emitter.pos(2, x1, y0, z1);
                emitter.pos(3, x1, y1, z1);
            }
            case WEST -> {
                emitter.pos(0, x0, y1, z0);
                emitter.pos(1, x0, y0, z0);
                emitter.pos(2, x0, y0, z1);
                emitter.pos(3, x0, y1, z1);
            }
            case EAST -> {
                emitter.pos(0, x1, y1, z1);
                emitter.pos(1, x1, y0, z1);
                emitter.pos(2, x1, y0, z0);
                emitter.pos(3, x1, y1, z0);
            }
        }
    }

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        record Key(BlockState state,
                   Set<LOTRConnectedBorder.Piece> down, Set<LOTRConnectedBorder.Piece> up,
                   Set<LOTRConnectedBorder.Piece> north, Set<LOTRConnectedBorder.Piece> south,
                   Set<LOTRConnectedBorder.Piece> west, Set<LOTRConnectedBorder.Piece> east) {
        }

        return new Key(state,
                piecesFor(level, pos, state, Direction.DOWN),
                piecesFor(level, pos, state, Direction.UP),
                piecesFor(level, pos, state, Direction.NORTH),
                piecesFor(level, pos, state, Direction.SOUTH),
                piecesFor(level, pos, state, Direction.WEST),
                piecesFor(level, pos, state, Direction.EAST));
    }

    @Override
    public Material.Baked particleMaterial() {
        return particle;
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

    // The eight neighbours sampled for a face lie in that face's plane, using
    // the same up/right convention as LOTRConnectedBorderModel -- which is the
    // side -> (x, y) switch out of LOTRConnectedTextures.getConnectedIconBlock.
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
        return block.areBlocksConnected(level, selfPos, selfState, otherPos);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
    }
}