package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRCrossbowBoltEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderCrossbowBolt: vanilla's arrow, drawn off the bolt's own sheet.
 *
 * <p>The original drew the bolt by hand -- a cross of quads with the UVs
 * {@code (0, 0)-(16, 5)} for the shaft and {@code (0, 5)-(5, 10)} for the head,
 * off a 32x32 sheet -- which is exactly the layout and geometry ArrowModel has.
 *
 * <p>The sheet holds two bolts, the poisoned one ten pixels under the plain
 * one, and yOffset picked between them. The poisoned bolt uses a copy of
 * ArrowModel with its texture offsets moved those ten pixels down, so both are
 * read from the one original sheet.
 */
public class LOTRCrossbowBoltRenderer
        extends EntityRenderer<LOTRCrossbowBoltEntity, LOTRCrossbowBoltRenderer.BoltRenderState> {

    /** lotr:item/crossbowBolt.png. */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/projectiles/crossbow_bolt.png");

    /** yOffset * 10: where the poisoned bolt starts on the sheet. */
    private static final int POISONED_V = 10;

    public static class BoltRenderState extends ArrowRenderState {
        public boolean poisoned;
    }

    private final ArrowModel model;
    private final ArrowModel poisonedModel;

    public LOTRCrossbowBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
        this.poisonedModel = new ArrowModel(createBodyLayer(POISONED_V).bakeRoot());
    }

    /** ArrowModel.createBodyLayer, with the texture offsets moved {@code v} down. */
    private static LayerDefinition createBodyLayer(int v) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("back",
                CubeListBuilder.create().texOffs(0, v).addBox(0.0f, -2.5f, -2.5f, 0.0f, 5.0f, 5.0f),
                PartPose.offsetAndRotation(-11.0f, 0.0f, 0.0f, (float) (Math.PI / 4), 0.0f, 0.0f).withScale(0.8f));
        CubeListBuilder cross = CubeListBuilder.create().texOffs(0, v)
                .addBox(-12.0f, -2.0f, 0.0f, 16.0f, 4.0f, 0.0f, CubeDeformation.NONE, 1.0f, 0.8f);
        root.addOrReplaceChild("cross_1", cross, PartPose.rotation((float) (Math.PI / 4), 0.0f, 0.0f));
        root.addOrReplaceChild("cross_2", cross, PartPose.rotation((float) (Math.PI * 3 / 4), 0.0f, 0.0f));
        return LayerDefinition.create(mesh.transformed(pose -> pose.scaled(0.9f)), 32, 32);
    }

    @Override
    public BoltRenderState createRenderState() {
        return new BoltRenderState();
    }

    @Override
    public void extractRenderState(LOTRCrossbowBoltEntity bolt, BoltRenderState state, float partialTick) {
        super.extractRenderState(bolt, state, partialTick);
        state.xRot = bolt.getXRot(partialTick);
        state.yRot = bolt.getYRot(partialTick);
        state.shake = bolt.shakeTime - partialTick;
        state.poisoned = bolt.isPoisoned();
    }

    /** ArrowRenderer.submit, choosing the model by the bolt. */
    @Override
    public void submit(BoltRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        collector.submitModel(state.poisoned ? this.poisonedModel : this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
