package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRGollumRenderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelGollum: a big head with jug ears on a stooped body, the limbs
 * each an upper and a lower piece -- the upper following the lower, bent on
 * by 30 degrees at the shoulder and back 25 at the hip. The arms sway idly;
 * sitting, he rocks, arms and legs flapping; fleeing, his arms go up over his
 * head.
 */
public class LOTRGollumModel extends EntityModel<LOTRGollumRenderer.State> {

    public final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightShoulder;
    private final ModelPart rightArm;
    private final ModelPart leftShoulder;
    private final ModelPart leftArm;
    private final ModelPart rightThigh;
    private final ModelPart rightLeg;
    private final ModelPart leftThigh;
    private final ModelPart leftLeg;

    public LOTRGollumModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightShoulder = root.getChild("right_shoulder");
        this.rightArm = root.getChild("right_arm");
        this.leftShoulder = root.getChild("left_shoulder");
        this.leftArm = root.getChild("left_arm");
        this.rightThigh = root.getChild("right_thigh");
        this.rightLeg = root.getChild("right_leg");
        this.leftThigh = root.getChild("left_thigh");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.5f, -6.5f, -6.5f, 7.0f, 7.0f, 7.0f)
                .addBox(3.5f, -4.0f, -4.0f, 1.0f, 2.0f, 2.0f)
                .mirror()
                .addBox(-4.5f, -4.0f, -4.0f, 1.0f, 2.0f, 2.0f), PartPose.offset(0.0f, 5.0f, -5.5f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 17)
                .addBox(-5.0f, -12.0f, -2.0f, 10.0f, 12.0f, 3.0f)
                .texOffs(32, 0).addBox(-5.5f, -2.0f, -3.5f, 11.0f, 4.0f, 5.0f),
                PartPose.offsetAndRotation(0.0f, 11.0f, 5.0f, (float) Math.PI / 3.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("right_shoulder", CubeListBuilder.create().texOffs(0, 23)
                .addBox(-0.5f, -1.0f, -2.0f, 3.0f, 6.0f, 3.0f), PartPose.offset(5.0f, 6.0f, -4.5f));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(12, 22)
                .addBox(0.0f, 4.0f, 0.5f, 2.0f, 8.0f, 2.0f), PartPose.offset(5.0f, 6.0f, -4.5f));
        root.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(0, 23).mirror()
                .addBox(-1.5f, -1.0f, -2.0f, 3.0f, 6.0f, 3.0f), PartPose.offset(-5.0f, 6.0f, -4.5f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(12, 22).mirror()
                .addBox(-1.0f, 4.0f, 0.5f, 2.0f, 8.0f, 2.0f), PartPose.offset(-5.0f, 6.0f, -4.5f));
        root.addOrReplaceChild("right_thigh", CubeListBuilder.create().texOffs(0, 23)
                .addBox(-0.5f, -1.0f, -1.0f, 3.0f, 6.0f, 3.0f), PartPose.offset(2.0f, 12.0f, 4.0f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(12, 22)
                .addBox(0.0f, 4.0f, -2.5f, 2.0f, 8.0f, 2.0f), PartPose.offset(2.0f, 12.0f, 4.0f));
        root.addOrReplaceChild("left_thigh", CubeListBuilder.create().texOffs(0, 23)
                .addBox(-2.5f, -1.0f, -1.0f, 3.0f, 6.0f, 3.0f), PartPose.offset(-2.0f, 12.0f, 4.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 22)
                .addBox(-2.0f, 4.0f, -2.5f, 2.0f, 8.0f, 2.0f), PartPose.offset(-2.0f, 12.0f, 4.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRGollumRenderer.State state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.rightArm.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 2.0f * f1 * 0.5f;
        this.leftArm.xRot = Mth.cos(f * 0.6662f) * 2.0f * f1 * 0.5f;
        this.rightLeg.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leftLeg.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.rightArm.zRot = Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot = -(Mth.cos(f2 * 0.09f) * 0.05f + 0.05f);
        this.rightArm.xRot += Mth.sin(f2 * 0.067f) * 0.05f;
        this.leftArm.xRot -= Mth.sin(f2 * 0.067f) * 0.05f;
        if (state.sitting) {
            float f6 = f2 / 20.0f * Mth.TWO_PI;
            this.rightArm.xRot = Mth.sin(f6) * 3.0f;
            this.leftArm.xRot = Mth.sin(f6) * -3.0f;
            this.rightLeg.xRot = Mth.sin(f6) * 0.5f;
            this.leftLeg.xRot = Mth.sin(f6) * -0.5f;
        } else if (state.fleeing) {
            this.rightArm.xRot += Mth.PI;
            this.leftArm.xRot += Mth.PI;
        }
        this.body.zRot = Mth.cos(f * 0.6662f) * 0.25f * f1;
        sync(this.rightArm, this.rightShoulder, 30.0f);
        sync(this.leftArm, this.leftShoulder, 30.0f);
        sync(this.rightLeg, this.rightThigh, -25.0f);
        sync(this.leftLeg, this.leftThigh, -25.0f);
    }

    /** syncRotationAngles. */
    private static void sync(ModelPart source, ModelPart target, float additionalAngle) {
        target.x = source.x;
        target.y = source.y;
        target.z = source.z;
        target.xRot = source.xRot + Mth.DEG_TO_RAD * additionalAngle;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}
