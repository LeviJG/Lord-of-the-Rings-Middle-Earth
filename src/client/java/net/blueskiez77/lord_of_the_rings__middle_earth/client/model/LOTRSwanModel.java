package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSwanRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** LOTRModelSwan, box for box: the neck dips to peck and hiss, the wings beat as it flaps. */
public class LOTRSwanModel extends EntityModel<LOTRSwanRenderState> {

    private final ModelPart tail;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart wingRight;
    private final ModelPart wingLeft;
    private final ModelPart legRight;
    private final ModelPart legLeft;

    public LOTRSwanModel(ModelPart root) {
        super(root);
        ModelPart body = root.getChild("body");
        this.tail = body.getChild("tail");
        this.neck = body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.wingRight = root.getChild("wing_right");
        this.wingLeft = root.getChild("wing_left");
        this.legRight = root.getChild("leg_right");
        this.legLeft = root.getChild("leg_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0f, -3.0f, -7.0f, 8, 6, 14), PartPose.offset(0.0f, 18.0f, 0.0f));
        body.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(24, 20).addBox(-3.0f, -1.5f, -1.0f, 6, 4, 4)
                .texOffs(24, 28).addBox(-2.0f, -1.0f, 3.0f, 4, 2, 3), PartPose.offset(0.0f, -2.0f, 7.0f));
        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(44, 11)
                .addBox(-1.0f, -11.0f, -3.0f, 2, 13, 2), PartPose.offset(0.0f, 0.0f, -5.5f));
        neck.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(44, 0).addBox(-1.5f, -2.0f, -4.0f, 3, 3, 4)
                .texOffs(44, 7).addBox(-1.0f, -0.5f, -7.0f, 2, 1, 3), PartPose.offset(0.0f, -10.0f, -2.0f));
        root.addOrReplaceChild("wing_right", CubeListBuilder.create()
                .texOffs(0, 20).addBox(-1.0f, -3.5f, -1.0f, 1, 7, 8)
                .texOffs(0, 35).addBox(-1.0f, -4.5f, 7.0f, 1, 6, 3)
                .texOffs(8, 35).addBox(-1.0f, -5.5f, 10.0f, 1, 5, 3), PartPose.offset(-4.0f, 18.0f, -5.0f));
        root.addOrReplaceChild("wing_left", CubeListBuilder.create().mirror()
                .texOffs(0, 20).addBox(0.0f, -3.5f, -1.0f, 1, 7, 8)
                .texOffs(0, 35).addBox(0.0f, -4.5f, 7.0f, 1, 6, 3)
                .texOffs(8, 35).addBox(0.0f, -5.5f, 10.0f, 1, 5, 3), PartPose.offset(4.0f, 18.0f, -5.0f));
        root.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(24, 33)
                .addBox(-1.5f, 0.0f, -3.0f, 3, 3, 3), PartPose.offset(-2.0f, 21.0f, 1.0f));
        root.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(24, 33).mirror()
                .addBox(-1.5f, 0.0f, -3.0f, 3, 3, 3), PartPose.offset(2.0f, 21.0f, 1.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LOTRSwanRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        float flapping = (Mth.sin(state.flapPhase) + 1.0f) * state.flapPower;
        this.neck.xRot = -0.20943952f + state.xRot * Mth.DEG_TO_RAD * 0.4f + state.peckAngle;
        this.neck.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = -this.neck.xRot;
        this.tail.xRot = 0.34906584f + Mth.cos(f * 0.4f) * f1 * 0.5f + Mth.cos(f2 * 0.1f) * 0.1f;
        float wingX = 0.17453292f;
        float wingY = (1.0f + Mth.cos(f * 0.4f + Mth.PI)) * f1 * 0.5f + (1.0f + Mth.cos(f2 * 0.15f)) * 0.1f;
        float wingZ = Mth.cos(f * 0.4f + Mth.PI) * f1 * 0.2f;
        wingY += flapping * 0.2f;
        wingZ += flapping * 0.5f;
        this.wingRight.xRot = wingX;
        this.wingLeft.xRot = wingX;
        this.wingRight.yRot = -wingY;
        this.wingLeft.yRot = wingY;
        this.wingRight.zRot = wingZ;
        this.wingLeft.zRot = -wingZ;
        this.legRight.xRot = Mth.cos(f * 0.7f + Mth.PI) * f1;
        this.legLeft.xRot = Mth.cos(f * 0.7f) * f1;
    }
}
