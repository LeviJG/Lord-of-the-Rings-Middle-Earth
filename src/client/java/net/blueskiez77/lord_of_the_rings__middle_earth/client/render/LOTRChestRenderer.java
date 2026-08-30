package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRChestBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRChestBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;

/**
 * Vanilla's ChestRenderer, line for line, with one change: the sprite comes
 * from the block's variant instead of ChestRenderer's private getChestMaterial,
 * which only knows the vanilla chest kinds.
 *
 * <p>Only the SINGLE layer is needed -- {@link LOTRChestBlock} pins ChestType
 * to SINGLE -- and it is taken from ChestRenderer.LAYERS so it is the very
 * model vanilla bakes, not a second copy of the definition.
 */
public class LOTRChestRenderer
        implements BlockEntityRenderer<LOTRChestBlockEntity, LOTRChestRenderState> {

    private final ChestModel model;
    private final SpriteGetter sprites;

    public LOTRChestRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ChestModel(context.bakeLayer(ChestRenderer.LAYERS.select(ChestType.SINGLE)));
        this.sprites = context.sprites();
    }

    @Override
    public LOTRChestRenderState createRenderState() {
        return new LOTRChestRenderState();
    }

    @Override
    public void extractRenderState(LOTRChestBlockEntity chest, LOTRChestRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(chest, state, crumbling);

        BlockState blockState = chest.getBlockState();
        if (!(blockState.getBlock() instanceof LOTRChestBlock block)) {
            return;
        }
        state.facing = blockState.getValue(ChestBlock.FACING);
        state.open = chest.getOpenNess(partialTick);
        // CHEST_MAPPER prefixes entity/chest/, giving the sprite
        // lotr:entity/chest/<variant> on the chest sheet -- the same call the
        // item's minecraft:chest special renderer makes.
        state.sprite = Sheets.CHEST_MAPPER.apply(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, block.variant()));
    }

    @Override
    public void submit(LOTRChestRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (state.sprite == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));

        // The lid does NOT move linearly. Vanilla eases it:
        //     f = 1 - open;  f = 1 - f*f*f
        // ChestModel.setupAnim then does lid.xRot = f * PI/2. Passing raw
        // openness makes the lid swing at a constant rate and reach the wrong
        // angle for most of the animation.
        float open = 1.0F - state.open;
        open = 1.0F - open * open * open;

        collector.submitModel(model, open, poseStack,
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1,
                state.sprite, sprites, 0, state.breakProgress);

        poseStack.popPose();
    }
}
