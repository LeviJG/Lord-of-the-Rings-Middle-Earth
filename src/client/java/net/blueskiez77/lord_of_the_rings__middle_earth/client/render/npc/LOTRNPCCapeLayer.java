package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * LOTRRenderBiped.renderNPCCape: an NPC's cape -- ModelBiped's cloak, a
 * 10 x 16 x 1 slab on a 64 x 32 sheet -- hanging two pixels behind its
 * shoulders and tilted ten degrees out from its back. It does not swing.
 */
public class LOTRNPCCapeLayer<S extends LOTRNPCRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

    private final ModelPart cape;

    public LOTRNPCCapeLayer(RenderLayerParent<S, M> parent) {
        super(parent);
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cape",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, 0.0f, -1.0f, 10.0f, 16.0f, 1.0f), PartPose.ZERO);
        this.cape = LayerDefinition.create(mesh, 64, 32).bakeRoot().getChild("cape");
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, S state, float yRot, float xRot) {
        if (state.cape == null || state.isInvisible) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 0.125f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-10.0f));
        collector.submitModelPart(this.cape, poseStack, RenderTypes.entityCutout(state.cape), light,
                OverlayTexture.NO_OVERLAY, null);
        poseStack.popPose();
    }
}
