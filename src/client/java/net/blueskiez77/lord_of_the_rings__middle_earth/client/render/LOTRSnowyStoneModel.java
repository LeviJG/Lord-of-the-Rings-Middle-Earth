package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRReplacedMethods.Stone.getIconWorld ("Snowy stone"): stone with snow on
 * top -- a layer or a block -- shows snow down its sides. Vanilla stone's own
 * model, with its four sides baked again in the snowy sprite when it is.
 */
public final class LOTRSnowyStoneModel implements BlockStateModel {

    private static final Identifier SNOWY_SIDE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/stone_snow");

    private final BlockStateModel stone;
    private final Material.Baked snowySide;

    private LOTRSnowyStoneModel(BlockStateModel stone, Material.Baked snowySide) {
        this.stone = stone;
        this.snowySide = snowySide;
    }

    public static void init() {
        ModelLoadingPlugin.register(context -> context.modifyBlockModelAfterBake().register((model, bake) ->
                bake.state().is(Blocks.STONE)
                        ? new LOTRSnowyStoneModel(model, bake.baker().materials().get(new Material(SNOWY_SIDE),
                                () -> "lotr snowy stone"))
                        : model));
    }

    private static boolean isSnowCapped(BlockAndTintGetter level, BlockPos pos) {
        if (!LOTRConfig.snowyStone) {
            return false;
        }
        BlockState above = level.getBlockState(pos.above());
        return above.is(Blocks.SNOW) || above.is(Blocks.SNOW_BLOCK);
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (!isSnowCapped(level, pos)) {
            this.stone.emitQuads(emitter, level, pos, state, random, cullTest);
            return;
        }
        emitter.pushTransform(quad -> {
            if (quad.nominalFace() != null && quad.nominalFace().getAxis().isHorizontal()) {
                quad.materialBake(this.snowySide, MutableQuadView.BAKE_LOCK_UV);
            }
            return true;
        });
        this.stone.emitQuads(emitter, level, pos, state, random, cullTest);
        emitter.popTransform();
    }

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        record Key(Object stone, boolean snowy) {
        }
        return new Key(this.stone.createGeometryKey(level, pos, state, random), isSnowCapped(level, pos));
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
        this.stone.collectParts(random, parts);
    }

    @Override
    public Material.Baked particleMaterial() {
        return this.stone.particleMaterial();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return this.stone.materialFlags();
    }
}
