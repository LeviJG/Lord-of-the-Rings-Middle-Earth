package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBirdRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** LOTRModelBird, box for box: wings beating in flight, folded when perched but for a flutter now and then. */
public class LOTRBirdModel extends EntityModel<LOTRBirdRenderState> {

    public final ModelPart body;
    public final ModelPart head;
    private final ModelPart wingRight;
    private final ModelPart wingLeft;
    private final ModelPart legRight;
    private final ModelPart legLeft;

    public LOTRBirdModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.wingRight = this.body.getChild("wing_right");
        this.wingLeft = this.body.getChild("wing_left");
        this.legRight = this.body.getChild("leg_right");
        this.legLeft = this.body.getChild("leg_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 7).addBox(-1.5f, -2.0f, -2.0f, 3, 3, 5)
                .texOffs(8, 0).addBox(-1.0f, -1.5f, 3.0f, 2, 1, 3)
                .texOffs(8, 4).addBox(-1.0f, -0.5f, 3.0f, 2, 1, 2), PartPose.offset(0.0f, 21.0f, 0.0f));
        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0f, -1.5f, -1.5f, 2, 2, 2)
                .texOffs(0, 4).addBox(-0.5f, -0.5f, -2.5f, 1, 1, 1)
                .texOffs(15, 0).addBox(-0.5f, -0.5f, -3.5f, 1, 1, 2), PartPose.offset(0.0f, -2.0f, -2.0f));
        body.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(16, 7)
                .addBox(0.0f, 0.0f, -2.0f, 0, 5, 4), PartPose.offset(-1.5f, -1.5f, 0.5f));
        body.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(16, 7).mirror()
                .addBox(0.0f, 0.0f, -2.0f, 0, 5, 4), PartPose.offset(1.5f, -1.5f, 0.5f));
        body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-1.0f, 0.0f, -1.5f, 1, 2, 2), PartPose.offset(-0.3f, 1.0f, 0.5f));
        body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(0, 16).mirror()
                .addBox(0.0f, 0.0f, -1.5f, 1, 2, 2), PartPose.offset(0.3f, 1.0f, 0.5f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRBirdRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.wingTime;
        if (state.still) {
            this.body.xRot = -0.17453292f;
            this.head.xRot = 0.34906584f;
            this.wingRight.zRot = state.flapping ? Mth.HALF_PI + Mth.cos(f2 * 1.5f) * 0.5235988f : 0.5235988f;
            this.wingLeft.zRot = -this.wingRight.zRot;
            this.legRight.xRot = -this.body.xRot + Mth.cos(f * 0.6662f) * f1;
            this.legLeft.xRot = -this.body.xRot + Mth.cos(f * 0.6662f + Mth.PI) * f1;
            this.legRight.y = 1.0f;
            this.legLeft.y = 1.0f;
        } else {
            this.body.xRot = 0.0f;
            this.head.xRot = 0.0f;
            this.wingRight.zRot = Mth.HALF_PI + Mth.cos(f2 * 1.5f) * 0.5235988f;
            this.wingLeft.zRot = -this.wingRight.zRot;
            this.legRight.xRot = 0.0f;
            this.legLeft.xRot = 0.0f;
            this.legRight.y = 0.0f;
            this.legLeft.y = 0.0f;
        }
    }
}
