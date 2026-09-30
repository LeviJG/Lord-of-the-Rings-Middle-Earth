package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMountRenderState;

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
 * Minecraft 1.7.10's ModelHorse, which LOTRRenderHorse drew every LOTR
 * horse, pony and zebra with. 26.2's horse model has a different shape and
 * texture layout, and the LOTR coats (the zebra) and barding are painted for
 * this one, so it is transcribed box for box, with its animation.
 *
 * <p>The saddle, bridle and reins are part of the model, textured from the
 * horse's own sheet, and show only on a saddled adult (the reins only while
 * ridden). A foal is drawn as the original drew it, in three groups -- legs,
 * body, head -- each scaled and lifted on its own
 * ({@link #setupBabyGroups}), with a foal's size fixed at vanilla's 0.5.
 *
 * <p>Never drawn for the LOTR equines, as they were never donkeys or mules
 * on the client: the mule ears and the chest bags. They are left out.
 */
public class LOTRLegacyHorseModel extends EntityModel<LOTRMountRenderState> {

    private static final float BABY_SIZE = 0.5f;

    private final ModelPart legs;
    private final ModelPart bodyGroup;
    private final ModelPart headGroup;

    private final ModelPart head;
    private final ModelPart mouthTop;
    private final ModelPart mouthBottom;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart neck;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart tailBase;
    private final ModelPart tailMiddle;
    private final ModelPart tailTip;
    private final ModelPart backLeftLeg;
    private final ModelPart backLeftShin;
    private final ModelPart backLeftHoof;
    private final ModelPart backRightLeg;
    private final ModelPart backRightShin;
    private final ModelPart backRightHoof;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontLeftShin;
    private final ModelPart frontLeftHoof;
    private final ModelPart frontRightLeg;
    private final ModelPart frontRightShin;
    private final ModelPart frontRightHoof;
    private final ModelPart faceRopes;
    private final ModelPart saddleBottom;
    private final ModelPart saddleFront;
    private final ModelPart saddleBack;
    private final ModelPart leftSaddleRope;
    private final ModelPart leftSaddleMetal;
    private final ModelPart rightSaddleRope;
    private final ModelPart rightSaddleMetal;
    private final ModelPart leftFaceMetal;
    private final ModelPart rightFaceMetal;
    private final ModelPart leftRein;
    private final ModelPart rightRein;

    public LOTRLegacyHorseModel(ModelPart root) {
        super(root);
        this.legs = root.getChild("legs");
        this.bodyGroup = root.getChild("body_group");
        this.headGroup = root.getChild("head_group");
        this.head = this.headGroup.getChild("head");
        this.mouthTop = this.head.getChild("mouth_top");
        this.mouthBottom = this.head.getChild("mouth_bottom");
        this.earLeft = this.headGroup.getChild("ear_left");
        this.earRight = this.headGroup.getChild("ear_right");
        this.body = this.bodyGroup.getChild("body");
        this.tailBase = this.bodyGroup.getChild("tail_base");
        this.tailMiddle = this.bodyGroup.getChild("tail_middle");
        this.tailTip = this.bodyGroup.getChild("tail_tip");
        this.neck = this.bodyGroup.getChild("neck");
        this.mane = this.bodyGroup.getChild("mane");
        this.backLeftLeg = this.legs.getChild("back_left_leg");
        this.backLeftShin = this.legs.getChild("back_left_shin");
        this.backLeftHoof = this.legs.getChild("back_left_hoof");
        this.backRightLeg = this.legs.getChild("back_right_leg");
        this.backRightShin = this.legs.getChild("back_right_shin");
        this.backRightHoof = this.legs.getChild("back_right_hoof");
        this.frontLeftLeg = this.legs.getChild("front_left_leg");
        this.frontLeftShin = this.legs.getChild("front_left_shin");
        this.frontLeftHoof = this.legs.getChild("front_left_hoof");
        this.frontRightLeg = this.legs.getChild("front_right_leg");
        this.frontRightShin = this.legs.getChild("front_right_shin");
        this.frontRightHoof = this.legs.getChild("front_right_hoof");
        this.faceRopes = root.getChild("face_ropes");
        this.saddleBottom = root.getChild("saddle_bottom");
        this.saddleFront = root.getChild("saddle_front");
        this.saddleBack = root.getChild("saddle_back");
        this.leftSaddleRope = root.getChild("left_saddle_rope");
        this.leftSaddleMetal = root.getChild("left_saddle_metal");
        this.rightSaddleRope = root.getChild("right_saddle_rope");
        this.rightSaddleMetal = root.getChild("right_saddle_metal");
        this.leftFaceMetal = root.getChild("left_face_metal");
        this.rightFaceMetal = root.getChild("right_face_metal");
        this.leftRein = root.getChild("left_rein");
        this.rightRein = root.getChild("right_rein");
    }

    private static PartPose pose(float x, float y, float z, float xr, float yr, float zr) {
        return PartPose.offsetAndRotation(x, y, z, xr, yr, zr);
    }

    private static CubeListBuilder box(int u, int v, float x, float y, float z, int w, int h, int d) {
        return CubeListBuilder.create().texOffs(u, v).addBox(x, y, z, w, h, d);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition bodyGroup = root.addOrReplaceChild("body_group", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition headGroup = root.addOrReplaceChild("head_group", CubeListBuilder.create(), PartPose.ZERO);

        bodyGroup.addOrReplaceChild("body", box(0, 34, -5.0f, -8.0f, -19.0f, 10, 10, 24), pose(0.0f, 11.0f, 9.0f, 0, 0, 0));
        bodyGroup.addOrReplaceChild("tail_base", box(44, 0, -1.0f, -1.0f, 0.0f, 2, 2, 3), pose(0.0f, 3.0f, 14.0f, -1.134464f, 0, 0));
        bodyGroup.addOrReplaceChild("tail_middle", box(38, 7, -1.5f, -2.0f, 3.0f, 3, 4, 7), pose(0.0f, 3.0f, 14.0f, -1.134464f, 0, 0));
        bodyGroup.addOrReplaceChild("tail_tip", box(24, 3, -1.5f, -4.5f, 9.0f, 3, 4, 7), pose(0.0f, 3.0f, 14.0f, -1.40215f, 0, 0));

        legs.addOrReplaceChild("back_left_leg", box(78, 29, -2.5f, -2.0f, -2.5f, 4, 9, 5), pose(4.0f, 9.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("back_left_shin", box(78, 43, -2.0f, 0.0f, -1.5f, 3, 5, 3), pose(4.0f, 16.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("back_left_hoof", box(78, 51, -2.5f, 5.1f, -2.0f, 4, 3, 4), pose(4.0f, 16.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("back_right_leg", box(96, 29, -1.5f, -2.0f, -2.5f, 4, 9, 5), pose(-4.0f, 9.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("back_right_shin", box(96, 43, -1.0f, 0.0f, -1.5f, 3, 5, 3), pose(-4.0f, 16.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("back_right_hoof", box(96, 51, -1.5f, 5.1f, -2.0f, 4, 3, 4), pose(-4.0f, 16.0f, 11.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_left_leg", box(44, 29, -1.9f, -1.0f, -2.1f, 3, 8, 4), pose(4.0f, 9.0f, -8.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_left_shin", box(44, 41, -1.9f, 0.0f, -1.6f, 3, 5, 3), pose(4.0f, 16.0f, -8.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_left_hoof", box(44, 51, -2.4f, 5.1f, -2.1f, 4, 3, 4), pose(4.0f, 16.0f, -8.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_right_leg", box(60, 29, -1.1f, -1.0f, -2.1f, 3, 8, 4), pose(-4.0f, 9.0f, -8.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_right_shin", box(60, 41, -1.1f, 0.0f, -1.6f, 3, 5, 3), pose(-4.0f, 16.0f, -8.0f, 0, 0, 0));
        legs.addOrReplaceChild("front_right_hoof", box(60, 51, -1.6f, 5.1f, -2.1f, 4, 3, 4), pose(-4.0f, 16.0f, -8.0f, 0, 0, 0));

        PartDefinition head = headGroup.addOrReplaceChild("head", box(0, 0, -2.5f, -10.0f, -1.5f, 5, 5, 7), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        head.addOrReplaceChild("mouth_top", box(24, 18, -2.0f, -10.0f, -7.0f, 4, 3, 6), pose(0.0f, 3.95f, -10.0f, 0.5235988f, 0, 0));
        head.addOrReplaceChild("mouth_bottom", box(24, 27, -2.0f, -7.0f, -6.5f, 4, 2, 5), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        headGroup.addOrReplaceChild("ear_left", box(0, 0, 0.45f, -12.0f, 4.0f, 2, 3, 1), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        headGroup.addOrReplaceChild("ear_right", box(0, 0, -2.45f, -12.0f, 4.0f, 2, 3, 1), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        bodyGroup.addOrReplaceChild("neck", box(0, 12, -2.05f, -9.8f, -2.0f, 4, 14, 8), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        bodyGroup.addOrReplaceChild("mane", box(58, 0, -1.0f, -11.5f, 5.0f, 2, 16, 4), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));

        root.addOrReplaceChild("saddle_bottom", box(80, 0, -5.0f, 0.0f, -3.0f, 10, 1, 8), pose(0.0f, 2.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("saddle_front", box(106, 9, -1.5f, -1.0f, -3.0f, 3, 1, 2), pose(0.0f, 2.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("saddle_back", box(80, 9, -4.0f, -1.0f, 3.0f, 8, 1, 2), pose(0.0f, 2.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("left_saddle_metal", box(74, 0, -0.5f, 6.0f, -1.0f, 1, 2, 2), pose(5.0f, 3.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("left_saddle_rope", box(70, 0, -0.5f, 0.0f, -0.5f, 1, 6, 1), pose(5.0f, 3.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("right_saddle_metal", box(74, 4, -0.5f, 6.0f, -1.0f, 1, 2, 2), pose(-5.0f, 3.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("right_saddle_rope", box(80, 0, -0.5f, 0.0f, -0.5f, 1, 6, 1), pose(-5.0f, 3.0f, 2.0f, 0, 0, 0));
        root.addOrReplaceChild("left_face_metal", box(74, 13, 1.5f, -8.0f, -4.0f, 1, 2, 2), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        root.addOrReplaceChild("right_face_metal", box(74, 13, -2.5f, -8.0f, -4.0f, 1, 2, 2), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        root.addOrReplaceChild("left_rein", box(44, 10, 2.6f, -6.0f, -6.0f, 0, 3, 16), pose(0.0f, 4.0f, -10.0f, 0, 0, 0));
        root.addOrReplaceChild("right_rein", box(44, 5, -2.6f, -6.0f, -6.0f, 0, 3, 16), pose(0.0f, 4.0f, -10.0f, 0, 0, 0));
        root.addOrReplaceChild("face_ropes", CubeListBuilder.create().texOffs(80, 12)
                .addBox(-2.5f, -10.1f, -7.0f, 5, 5, 12, new CubeDeformation(0.2f)), pose(0.0f, 4.0f, -10.0f, 0.5235988f, 0, 0));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float yawDiff = Mth.clamp(state.yRot, -20.0f, 20.0f);
        float pitch = state.xRot * Mth.DEG_TO_RAD;
        if (f1 > 0.2f) {
            pitch += Mth.cos(f * 0.4f) * 0.15f * f1;
        }
        float eat = state.eatAnimation;
        float rear = state.standAnimation;
        float antiRear = 1.0f - rear;
        float mouth = state.feedingAnimation;
        boolean wagTail = state.animateTail;
        boolean saddled = !state.saddle.isEmpty();
        boolean ridden = state.isRidden;
        float ticks = state.ageInTicks;
        float legSwing = Mth.cos(f * 0.6662f + Mth.PI);
        float legAmount = legSwing * 0.8f * f1;

        this.head.y = 4.0f;
        this.head.z = -10.0f;
        this.tailBase.y = 3.0f;
        this.tailMiddle.z = 14.0f;
        this.body.xRot = 0.0f;
        this.head.xRot = 0.5235988f + pitch;
        this.head.yRot = yawDiff * Mth.DEG_TO_RAD;
        float notRearOrEat = 1.0f - Math.max(rear, eat);
        this.head.xRot = rear * (0.2617994f + pitch) + eat * 2.18166f + notRearOrEat * this.head.xRot;
        this.head.yRot = rear * (yawDiff * Mth.DEG_TO_RAD) + notRearOrEat * this.head.yRot;
        this.head.y = rear * -6.0f + eat * 11.0f + notRearOrEat * this.head.y;
        this.head.z = rear * -1.0f + eat * -10.0f + notRearOrEat * this.head.z;
        this.tailBase.y = rear * 9.0f + antiRear * this.tailBase.y;
        this.tailMiddle.z = rear * 18.0f + antiRear * this.tailMiddle.z;
        this.body.xRot = rear * -0.7853981f + antiRear * this.body.xRot;

        for (ModelPart part : new ModelPart[] {this.earLeft, this.earRight, this.neck, this.mane}) {
            part.y = this.head.y;
            part.z = this.head.z;
            part.xRot = this.head.xRot;
            part.yRot = this.head.yRot;
        }
        this.mouthTop.y = 0.02f;
        this.mouthBottom.y = 0.0f;
        this.mouthTop.z = 0.02f - mouth;
        this.mouthBottom.z = mouth;
        this.mouthTop.xRot = -0.09424778f * mouth;
        this.mouthBottom.xRot = 0.15707964f * mouth;
        this.mouthTop.yRot = 0.0f;
        this.mouthBottom.yRot = 0.0f;

        float rearLift = 0.2617994f * rear;
        float kick = Mth.cos(ticks * 0.6f + Mth.PI);
        this.frontLeftLeg.y = -2.0f * rear + 9.0f * antiRear;
        this.frontLeftLeg.z = -2.0f * rear + -8.0f * antiRear;
        this.frontRightLeg.y = this.frontLeftLeg.y;
        this.frontRightLeg.z = this.frontLeftLeg.z;
        this.backLeftShin.y = this.backLeftLeg.y + Mth.sin(Mth.HALF_PI + rearLift + antiRear * -legSwing * 0.5f * f1) * 7.0f;
        this.backLeftShin.z = this.backLeftLeg.z + Mth.cos(4.712389f + rearLift + antiRear * -legSwing * 0.5f * f1) * 7.0f;
        this.backRightShin.y = this.backRightLeg.y + Mth.sin(Mth.HALF_PI + rearLift + antiRear * legSwing * 0.5f * f1) * 7.0f;
        this.backRightShin.z = this.backRightLeg.z + Mth.cos(4.712389f + rearLift + antiRear * legSwing * 0.5f * f1) * 7.0f;
        float frontLeft = (-1.0471976f + kick) * rear + legAmount * antiRear;
        float frontRight = (-1.0471976f + -kick) * rear + -legAmount * antiRear;
        this.frontLeftShin.y = this.frontLeftLeg.y + Mth.sin(Mth.HALF_PI + frontLeft) * 7.0f;
        this.frontLeftShin.z = this.frontLeftLeg.z + Mth.cos(4.712389f + frontLeft) * 7.0f;
        this.frontRightShin.y = this.frontRightLeg.y + Mth.sin(Mth.HALF_PI + frontRight) * 7.0f;
        this.frontRightShin.z = this.frontRightLeg.z + Mth.cos(4.712389f + frontRight) * 7.0f;
        this.backLeftLeg.xRot = rearLift + -legSwing * 0.5f * f1 * antiRear;
        this.backLeftShin.xRot = -0.08726646f * rear + (-legSwing * 0.5f * f1 - Math.max(0.0f, legSwing * 0.5f * f1)) * antiRear;
        this.backLeftHoof.xRot = this.backLeftShin.xRot;
        this.backRightLeg.xRot = rearLift + legSwing * 0.5f * f1 * antiRear;
        this.backRightShin.xRot = -0.08726646f * rear + (legSwing * 0.5f * f1 - Math.max(0.0f, -legSwing * 0.5f * f1)) * antiRear;
        this.backRightHoof.xRot = this.backRightShin.xRot;
        this.frontLeftLeg.xRot = frontLeft;
        this.frontLeftShin.xRot = (this.frontLeftLeg.xRot + Mth.PI * Math.max(0.0f, 0.2f + kick * 0.2f)) * rear
                + (legAmount + Math.max(0.0f, legSwing * 0.5f * f1)) * antiRear;
        this.frontLeftHoof.xRot = this.frontLeftShin.xRot;
        this.frontRightLeg.xRot = frontRight;
        this.frontRightShin.xRot = (this.frontRightLeg.xRot + Mth.PI * Math.max(0.0f, 0.2f - kick * 0.2f)) * rear
                + (-legAmount + Math.max(0.0f, -legSwing * 0.5f * f1)) * antiRear;
        this.frontRightHoof.xRot = this.frontRightShin.xRot;
        this.backLeftHoof.y = this.backLeftShin.y;
        this.backLeftHoof.z = this.backLeftShin.z;
        this.backRightHoof.y = this.backRightShin.y;
        this.backRightHoof.z = this.backRightShin.z;
        this.frontLeftHoof.y = this.frontLeftShin.y;
        this.frontLeftHoof.z = this.frontLeftShin.z;
        this.frontRightHoof.y = this.frontRightShin.y;
        this.frontRightHoof.z = this.frontRightShin.z;

        boolean adult = !state.isBaby;
        boolean showSaddle = adult && saddled;
        for (ModelPart part : new ModelPart[] {this.faceRopes, this.saddleBottom, this.saddleFront, this.saddleBack,
                this.leftSaddleRope, this.leftSaddleMetal, this.rightSaddleRope, this.rightSaddleMetal,
                this.leftFaceMetal, this.rightFaceMetal}) {
            part.visible = showSaddle;
        }
        this.leftRein.visible = showSaddle && ridden;
        this.rightRein.visible = showSaddle && ridden;
        if (saddled) {
            this.saddleBottom.y = rear * 0.5f + antiRear * 2.0f;
            this.saddleBottom.z = rear * 11.0f + antiRear * 2.0f;
            for (ModelPart part : new ModelPart[] {this.saddleFront, this.saddleBack, this.leftSaddleRope,
                    this.rightSaddleRope, this.leftSaddleMetal, this.rightSaddleMetal}) {
                part.y = this.saddleBottom.y;
                part.z = this.saddleBottom.z;
            }
            this.saddleBottom.xRot = this.body.xRot;
            this.saddleFront.xRot = this.body.xRot;
            this.saddleBack.xRot = this.body.xRot;
            for (ModelPart part : new ModelPart[] {this.leftRein, this.rightRein, this.faceRopes,
                    this.leftFaceMetal, this.rightFaceMetal}) {
                part.y = this.head.y;
                part.z = this.head.z;
            }
            this.leftRein.xRot = pitch;
            this.rightRein.xRot = pitch;
            this.faceRopes.xRot = this.head.xRot;
            this.leftFaceMetal.xRot = this.head.xRot;
            this.rightFaceMetal.xRot = this.head.xRot;
            for (ModelPart part : new ModelPart[] {this.faceRopes, this.leftFaceMetal, this.leftRein,
                    this.rightFaceMetal, this.rightRein}) {
                part.yRot = this.head.yRot;
            }
            if (ridden) {
                for (ModelPart part : new ModelPart[] {this.leftSaddleRope, this.leftSaddleMetal,
                        this.rightSaddleRope, this.rightSaddleMetal}) {
                    part.xRot = -1.0471976f;
                    part.zRot = 0.0f;
                }
            } else {
                this.leftSaddleRope.xRot = legAmount / 3.0f;
                this.leftSaddleMetal.xRot = legAmount / 3.0f;
                this.rightSaddleRope.xRot = legAmount / 3.0f;
                this.rightSaddleMetal.xRot = legAmount / 3.0f;
                this.leftSaddleRope.zRot = legAmount / 5.0f;
                this.leftSaddleMetal.zRot = legAmount / 5.0f;
                this.rightSaddleRope.zRot = -legAmount / 5.0f;
                this.rightSaddleMetal.zRot = -legAmount / 5.0f;
            }
        }

        float tailDrop = Math.min(-1.3089f + f1 * 1.5f, 0.0f);
        if (wagTail) {
            this.tailBase.yRot = Mth.cos(ticks * 0.7f);
            tailDrop = 0.0f;
        } else {
            this.tailBase.yRot = 0.0f;
        }
        this.tailMiddle.yRot = this.tailBase.yRot;
        this.tailTip.yRot = this.tailBase.yRot;
        this.tailMiddle.y = this.tailBase.y;
        this.tailTip.y = this.tailBase.y;
        this.tailMiddle.z = this.tailBase.z;
        this.tailTip.z = this.tailBase.z;
        this.tailBase.xRot = tailDrop;
        this.tailMiddle.xRot = tailDrop;
        this.tailTip.xRot = -0.2618f + tailDrop;

        setupBabyGroups(state.isBaby, eat);
    }

    /**
     * render's foal transforms. Each group was scaled and then lifted in the
     * scaled space; a part pose's offset is applied before its scale, so the
     * lift is multiplied through here.
     */
    private void setupBabyGroups(boolean baby, float eat) {
        if (!baby) {
            return;
        }
        float s = BABY_SIZE;
        float legY = 0.5f + s * 0.5f;
        this.legs.xScale = s;
        this.legs.yScale = legY;
        this.legs.zScale = s;
        this.legs.y = legY * 16.0f * 0.95f * (1.0f - s);
        this.bodyGroup.xScale = s;
        this.bodyGroup.yScale = s;
        this.bodyGroup.zScale = s;
        this.bodyGroup.y = s * 16.0f * 1.35f * (1.0f - s);
        float h = 0.5f + s * s * 0.5f;
        this.headGroup.xScale = h;
        this.headGroup.yScale = h;
        this.headGroup.zScale = h;
        if (eat <= 0.0f) {
            this.headGroup.y = h * 16.0f * 1.35f * (1.0f - s);
            this.headGroup.z = 0.0f;
        } else {
            this.headGroup.y = h * 16.0f * (0.9f * (1.0f - s) * eat + 1.35f * (1.0f - s) * (1.0f - eat));
            this.headGroup.z = h * 16.0f * 0.15f * (1.0f - s) * eat;
        }
    }
}
