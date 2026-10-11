package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBalrogRenderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelBalrog: the Balrog -- a great horned head on a long neck, a broad ribbed body, long arms,
 * crooked legs, a long tail and (when it burns, if "Balrog Wings" is on) two vast wings -- swaying,
 * swinging its arms, its wings beating slowly. The fire model is the same without its wings.
 */
public class LOTRBalrogModel extends EntityModel<LOTRBalrogRenderer.State> {

    private final boolean isFireModel;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart tail;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public LOTRBalrogModel(ModelPart root, boolean isFireModel) {
        super(root);
        this.isFireModel = isFireModel;
        this.body = root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.tail = this.body.getChild("tail");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }

    public static LayerDefinition createLayer(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 38).addBox(-8.0f, -15.0f, -6.0f, 16, 18, 12, f)
                .texOffs(0, 207).addBox(-9.0f, -6.5f, -7.0f, 7, 1, 14, f)
                .addBox(-9.0f, -9.5f, -7.0f, 7, 1, 14, f)
                .addBox(-9.0f, -12.5f, -7.0f, 7, 1, 14, f)
                .mirror().addBox(2.0f, -6.5f, -7.0f, 7, 1, 14, f)
                .addBox(2.0f, -9.5f, -7.0f, 7, 1, 14, f)
                .addBox(2.0f, -12.5f, -7.0f, 7, 1, 14, f)
                .mirror(false)
                .texOffs(0, 0).addBox(-9.0f, -29.0f, -7.0f, 18, 14, 15, f)
                .texOffs(81, 163).addBox(-2.0f, -21.0f, 5.5f, 4, 25, 2, f),
                PartPose.offset(0.0f, 7.0f, 3.0f));
        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
                .texOffs(76, 0).addBox(-6.0f, -5.0f, -10.0f, 12, 12, 14, f), PartPose.offset(0.0f, -25.0f, -3.0f));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(92, 48).addBox(-4.0f, -6.0f, -6.0f, 8, 10, 7, f)
                .texOffs(57, 58).addBox(-6.0f, -7.0f, -4.0f, 12, 4, 4, f),
                PartPose.offsetAndRotation(0.0f, 0.0f, -10.0f, 0.17453292f, 0.0f, 0.0f));
        PartDefinition rightHorn1 = head.addOrReplaceChild("right_horn_1", CubeListBuilder.create()
                .texOffs(57, 47).addBox(-7.0f, -1.5f, -1.5f, 8, 3, 3, f),
                PartPose.offsetAndRotation(-6.0f, -5.0f, -2.0f, 0.0f, -0.61086524f, 0.0f));
        rightHorn1.addOrReplaceChild("right_horn_2", CubeListBuilder.create()
                .texOffs(57, 35).addBox(-1.0f, -1.0f, -6.0f, 2, 2, 6, f),
                PartPose.offsetAndRotation(-7.0f, 0.0f, 0.0f, 0.0f, 0.7853982f, 0.0f));
        PartDefinition leftHorn1 = head.addOrReplaceChild("left_horn_1", CubeListBuilder.create()
                .texOffs(57, 47).mirror().addBox(-1.0f, -1.5f, -1.5f, 8, 3, 3, f),
                PartPose.offsetAndRotation(6.0f, -5.0f, -2.0f, 0.0f, 0.61086524f, 0.0f));
        leftHorn1.addOrReplaceChild("left_horn_2", CubeListBuilder.create()
                .texOffs(57, 35).mirror().addBox(-1.0f, -1.0f, -6.0f, 2, 2, 6, f),
                PartPose.offsetAndRotation(7.0f, 0.0f, 0.0f, 0.0f, -0.7853982f, 0.0f));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(59, 136).addBox(-7.0f, -2.0f, -4.0f, 7, 10, 8, f)
                .texOffs(93, 136).addBox(-6.5f, 8.0f, -3.0f, 6, 16, 6, f), PartPose.offset(-9.0f, -25.0f, 0.0f));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
                .texOffs(59, 136).addBox(0.0f, -2.0f, -4.0f, 7, 10, 8, f)
                .texOffs(93, 136).addBox(0.5f, 8.0f, -3.0f, 6, 16, 6, f), PartPose.offset(9.0f, -25.0f, 0.0f));
        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(46, 230).addBox(-7.0f, -2.0f, -4.0f, 7, 9, 8, f)
                .texOffs(46, 208).addBox(-6.5f, 2.0f, 4.0f, 6, 13, 5, f), PartPose.offset(-6.0f, 6.0f, 3.0f));
        rightLeg.addOrReplaceChild("right_foot", CubeListBuilder.create()
                .texOffs(0, 243).addBox(-7.0f, 15.0f, -6.0f, 7, 3, 9, f), PartPose.rotation(0.43633232f, 0.0f, 0.0f));
        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
                .texOffs(46, 230).addBox(0.0f, -2.0f, -4.0f, 7, 9, 8, f)
                .texOffs(46, 208).addBox(0.5f, 2.0f, 4.0f, 6, 13, 5, f), PartPose.offset(6.0f, 6.0f, 3.0f));
        leftLeg.addOrReplaceChild("left_foot", CubeListBuilder.create().mirror()
                .texOffs(0, 243).addBox(0.0f, 15.0f, -6.0f, 7, 3, 9, f), PartPose.rotation(0.43633232f, 0.0f, 0.0f));
        body.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(79, 200).addBox(-3.5f, -3.0f, 2.0f, 7, 7, 10, f)
                .texOffs(80, 225).addBox(-2.5f, -2.5f, 11.0f, 5, 5, 14, f)
                .texOffs(96, 175).addBox(-1.5f, -2.0f, 24.0f, 3, 3, 12, f), PartPose.offset(0.0f, -3.0f, 3.0f));
        body.addOrReplaceChild("right_wing", wing(false, f), PartPose.offset(-6.0f, -27.0f, 4.0f));
        body.addOrReplaceChild("left_wing", wing(true, f), PartPose.offset(6.0f, -27.0f, 4.0f));
        return LayerDefinition.create(mesh, 128, 256);
    }

    private static CubeListBuilder wing(boolean mirror, CubeDeformation f) {
        return CubeListBuilder.create().mirror(mirror)
                .texOffs(0, 137).addBox(-1.5f, -1.5f, 0.0f, 3, 3, 25, f)
                .texOffs(0, 167).addBox(-1.0f, -2.0f, 25.0f, 2, 24, 2, f)
                .texOffs(0, 30).addBox(-0.5f, -7.0f, 25.5f, 1, 5, 1, f)
                .texOffs(0, 69).addBox(0.0f, 0.0f, 0.0f, 0, 35, 25, f);
    }

    /** The body, then the right arm: where the Balrog holds its weapon. */
    public void translateToRightArm(PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.rightArm.translateAndRotate(poseStack);
    }

    @Override
    public void setupAnim(LOTRBalrogRenderer.State state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        this.rightWing.visible = !this.isFireModel && state.showWings;
        this.leftWing.visible = this.rightWing.visible;
        this.neck.xRot = -0.17453292f + state.xRot * Mth.DEG_TO_RAD;
        this.neck.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.body.xRot = 0.17453292f + Mth.cos(f2 * 0.03f) * 0.15f;
        this.rightArm.xRot = Mth.cos(f * 0.4f + (float) Math.PI) * 0.8f * f1;
        this.leftArm.xRot = Mth.cos(f * 0.4f) * 0.8f * f1;
        this.rightArm.zRot = Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot = -(Mth.cos(f2 * 0.09f) * 0.05f + 0.05f);
        float onGround = state.attackTime;
        float f6 = 1.0f - onGround;
        this.rightArm.yRot += this.body.yRot;
        this.leftArm.yRot += this.body.yRot;
        this.leftArm.xRot += this.body.yRot;
        f6 *= f6;
        f6 *= f6;
        f6 = 1.0f - f6;
        float f7 = Mth.sin(f6 * (float) Math.PI);
        float f8 = Mth.sin(onGround * (float) Math.PI) * -(this.head.xRot - 0.7f) * 0.75f;
        this.rightArm.xRot = (float) (this.rightArm.xRot - (f7 * 1.2 + f8));
        this.rightArm.yRot += this.body.yRot * 2.0f;
        this.rightArm.zRot = Mth.sin(onGround * (float) Math.PI) * -0.4f;
        if (state.heldItemRight != 0) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.31415927f * state.heldItemRight;
        }
        this.rightLeg.xRot = -0.43633232f + Mth.sin(f * 0.4f) * 1.2f * f1;
        this.leftLeg.xRot = -0.43633232f + Mth.sin(f * 0.4f + (float) Math.PI) * 1.2f * f1;
        this.rightWing.xRot = 0.6981317f;
        this.leftWing.xRot = 0.6981317f;
        this.rightWing.yRot = -0.6981317f + Mth.cos(f2 * 0.04f) * 0.5f;
        this.leftWing.yRot = 0.6981317f - Mth.cos(f2 * 0.04f) * 0.5f;
        this.tail.xRot = -0.6981317f;
        this.tail.yRot = Mth.cos(f2 * 0.05f) * 0.15f + Mth.sin(f * 0.1f) * 0.6f * f1;
    }
}
