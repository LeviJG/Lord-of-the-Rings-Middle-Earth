package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRStrikeRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** LOTRModelCrocodile, box for box. The upper jaw lifts as it snaps; the head does not follow its gaze. */
public class LOTRCrocodileModel extends EntityModel<LOTRStrikeRenderState> {

    private final ModelPart head;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart legFrontLeft;
    private final ModelPart legBackLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackRight;

    public LOTRCrocodileModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.legFrontLeft = root.getChild("leg_front_left");
        this.legBackLeft = root.getChild("leg_back_left");
        this.legFrontRight = root.getChild("leg_front_right");
        this.legBackRight = root.getChild("leg_back_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 83)
                .addBox(-8.0f, -5.0f, 0.0f, 16, 9, 36), PartPose.offset(0.0f, 17.0f, -16.0f));
        PartPose tailPose = PartPose.offset(0.0f, 13.0f, 18.0f);
        root.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 28)
                .addBox(-7.0f, 0.0f, 0.0f, 14, 7, 19), tailPose);
        root.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 55)
                .addBox(-6.0f, 1.5f, 17.0f, 12, 5, 16), tailPose);
        root.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 77)
                .addBox(-5.0f, 3.0f, 31.0f, 10, 3, 14), tailPose);
        root.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(58, 18)
                .addBox(-6.5f, 0.3f, -19.0f, 13, 4, 19), PartPose.offset(0.0f, 17.0f, -16.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-7.5f, -6.0f, -21.0f, 15, 6, 21), PartPose.offset(0.0f, 18.5f, -16.0f));
        CubeListBuilder legLeft = CubeListBuilder.create().texOffs(2, 104).addBox(0.0f, 0.0f, -3.0f, 16, 3, 6);
        CubeListBuilder legRight = CubeListBuilder.create().texOffs(2, 104).mirror().addBox(-16.0f, 0.0f, -3.0f, 16, 3, 6);
        root.addOrReplaceChild("leg_front_left", legLeft, PartPose.offsetAndRotation(6.0f, 15.0f, -11.0f, 0.0f, 0.0f, 0.43633232f));
        root.addOrReplaceChild("leg_back_left", legLeft, PartPose.offsetAndRotation(6.0f, 15.0f, 15.0f, 0.0f, 0.0f, 0.43633232f));
        root.addOrReplaceChild("leg_front_right", legRight, PartPose.offsetAndRotation(-6.0f, 15.0f, -11.0f, 0.0f, 0.0f, -0.43633232f));
        root.addOrReplaceChild("leg_back_right", legRight, PartPose.offsetAndRotation(-6.0f, 15.0f, 15.0f, 0.0f, 0.0f, -0.43633232f));
        root.addOrReplaceChild("spines", CubeListBuilder.create().texOffs(46, 45)
                .addBox(-5.0f, 0.0f, 0.0f, 10, 4, 32), PartPose.offsetAndRotation(0.0f, 9.5f, -14.0f, -0.034906585f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LOTRStrikeRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = state.strike * Mth.PI * -0.3f;
        float swing = Mth.cos(f * 0.6662f) * f1;
        this.legBackRight.yRot = swing;
        this.legBackLeft.yRot = swing;
        this.legFrontRight.yRot = swing;
        this.legFrontLeft.yRot = swing;
        this.tail1.yRot = swing * 0.5f;
        this.tail2.yRot = swing * 0.5625f;
        this.tail3.yRot = swing * 0.59375f;
    }
}
