package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/** LOTRModelDikDik, box for box. The dik-dik is never a baby, so there is no fawn model. */
public class LOTRDikDikModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart head;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LOTRDikDikModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(42, 23).addBox(-2.0f, -9.0f, -3.0f, 4, 4, 5)
                .texOffs(18, 28).addBox(-1.0f, -7.3f, -5.0f, 2, 2, 2)
                .texOffs(0, 27).addBox(-2.8f, -11.0f, 0.5f, 1, 3, 2)
                .texOffs(8, 27).addBox(1.8f, -11.0f, 0.5f, 1, 3, 2)
                .texOffs(0, 21).addBox(-1.5f, -11.0f, 0.0f, 1, 2, 1)
                .texOffs(0, 21).addBox(0.5f, -11.0f, 0.0f, 1, 2, 1)
                .texOffs(28, 22).addBox(-1.5f, -8.0f, -2.0f, 3, 7, 3),
                PartPose.offset(0.0f, 11.0f, -4.5f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0f, 0.0f, 0.0f, 6, 6, 14), PartPose.offset(0.0f, 9.0f, -7.0f));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(56, 0).addBox(-1.0f, 0.0f, -1.0f, 2, 10, 2);
        root.addOrReplaceChild("leg1", leg, PartPose.offset(-1.7f, 14.0f, 5.0f));
        root.addOrReplaceChild("leg2", leg, PartPose.offset(1.7f, 14.0f, 5.0f));
        root.addOrReplaceChild("leg3", leg, PartPose.offset(-1.7f, 14.0f, -5.0f));
        root.addOrReplaceChild("leg4", leg, PartPose.offset(1.7f, 14.0f, -5.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
    }
}
