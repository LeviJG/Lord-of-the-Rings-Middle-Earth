package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTREntRenderState;

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
 * LOTRModelEnt, box for box: a trunk with brows (raised in pain), eyelids
 * (shut as it blinks), a nose and a beard, long arms with hands, legs with
 * great feet, and a crown of branches -- and, for some Ents, two to five
 * more crowns set about the head. It walks with a slow swing, strikes with
 * both arms, and leans over a sapling it heals, waving its arms.
 *
 * <p>The extra crowns were drawn by rendering the branches again, for each,
 * after the trunk's transform: up 2.7 blocks, turned about, tipped out by 60
 * degrees and back down 2.6. Here they are five copies hung from the trunk
 * with that transform, turned and shown as the Ent has them.
 *
 * <p>The glowing eyes (LOTRGlowingEyes.Model) drew the trunk alone, at 0.05:
 * {@link Piece#EYES}.
 */
public class LOTREntModel extends EntityModel<LOTREntRenderState> {

    public enum Piece {
        ALL, EYES
    }

    private static final int MAX_EXTRA_BRANCHES = 5;

    private final Piece piece;
    private final ModelPart trunk;
    private final ModelPart browRight;
    private final ModelPart browLeft;
    private final ModelPart eyeRight;
    private final ModelPart eyeLeft;
    private final ModelPart rightArm;
    private final ModelPart rightHand;
    private final ModelPart leftArm;
    private final ModelPart leftHand;
    private final ModelPart rightLeg;
    private final ModelPart rightFoot;
    private final ModelPart leftLeg;
    private final ModelPart leftFoot;
    private final ModelPart[] extraBranches = new ModelPart[MAX_EXTRA_BRANCHES];

    public LOTREntModel(ModelPart root, Piece piece) {
        super(root);
        this.piece = piece;
        this.trunk = root.getChild("trunk");
        this.browRight = this.trunk.getChild("brow_right");
        this.browLeft = this.trunk.getChild("brow_left");
        this.eyeRight = this.trunk.getChild("eye_right");
        this.eyeLeft = this.trunk.getChild("eye_left");
        this.rightArm = this.trunk.getChild("right_arm");
        this.rightHand = this.rightArm.getChild("right_hand");
        this.leftArm = this.trunk.getChild("left_arm");
        this.leftHand = this.leftArm.getChild("left_hand");
        this.rightLeg = root.getChild("right_leg");
        this.rightFoot = this.rightLeg.getChild("right_foot");
        this.leftLeg = root.getChild("left_leg");
        this.leftFoot = this.leftLeg.getChild("left_foot");
        for (int i = 0; i < MAX_EXTRA_BRANCHES; ++i) {
            this.extraBranches[i] = this.trunk.getChild("extra_branches_" + i);
        }
    }

    private static float rad(float degrees) {
        return degrees * Mth.DEG_TO_RAD;
    }

    private static PartPose pose(float x, float y, float z, float xDeg, float yDeg, float zDeg) {
        return PartPose.offsetAndRotation(x, y, z, rad(xDeg), rad(yDeg), rad(zDeg));
    }

    /** The crown: five branches and four twigs, each ending in a knot. */
    private static void addBranches(PartDefinition branches, CubeDeformation f) {
        branches.addOrReplaceChild("branch1", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-1.5f, -28.0f, -1.5f, 3, 32, 3, f)
                .texOffs(80, 0).addBox(-3.5f, -32.0f, -3.5f, 7, 7, 7, f), pose(-1.0f, 0.0f, 0.0f, -7.0f, 17.0f, 0.0f));
        branches.addOrReplaceChild("branch1twig1", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-7.5f, -22.0f, -1.5f, 1, 12, 1, f)
                .texOffs(80, 0).addBox(-8.5f, -23.0f, -2.5f, 3, 3, 3, f), pose(1.0f, -5.0f, -7.0f, -50.0f, 25.0f, 15.0f));
        branches.addOrReplaceChild("branch1twig2", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-14.0f, -26.0f, -5.5f, 2, 12, 2, f)
                .texOffs(80, 0).addBox(-15.5f, -28.0f, -7.0f, 5, 5, 5, f), pose(-2.0f, 1.0f, 7.0f, 10.0f, 10.0f, 50.0f));
        branches.addOrReplaceChild("branch1twig3", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-7.5f, -24.0f, -3.5f, 1, 12, 1, f)
                .texOffs(80, 0).addBox(-8.5f, -25.0f, -4.5f, 3, 3, 3, f), pose(8.0f, -6.0f, 9.0f, 15.0f, -20.0f, -30.0f));
        branches.addOrReplaceChild("branch2", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-0.5f, -10.0f, -0.5f, 1, 14, 1, f)
                .texOffs(80, 0).addBox(-1.5f, -12.0f, -1.5f, 3, 3, 3, f), pose(6.0f, 0.0f, 2.0f, -20.0f, 42.0f, 0.0f));
        branches.addOrReplaceChild("branch3", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-1.0f, -16.0f, -1.0f, 2, 20, 2, f)
                .texOffs(80, 0).addBox(-2.5f, -18.0f, -2.5f, 5, 5, 5, f), pose(3.0f, 0.0f, -3.0f, 26.0f, -27.0f, 0.0f));
        branches.addOrReplaceChild("branch4", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-1.0f, -18.0f, -1.0f, 2, 22, 2, f)
                .texOffs(80, 0).addBox(-2.5f, -20.0f, -2.5f, 5, 5, 5, f), pose(-5.0f, 0.0f, -4.0f, 17.0f, 60.0f, 0.0f));
        branches.addOrReplaceChild("branch4twig1", CubeListBuilder.create()
                .texOffs(80, 16).addBox(8.5f, -21.0f, -7.5f, 1, 12, 1, f)
                .texOffs(80, 0).addBox(7.0f, -22.0f, -9.0f, 4, 4, 4, f), pose(-12.0f, -6.0f, 8.0f, 50.0f, 15.0f, -10.0f));
        branches.addOrReplaceChild("branch5", CubeListBuilder.create()
                .texOffs(80, 16).addBox(-1.0f, -24.0f, -1.0f, 2, 28, 2, f)
                .texOffs(80, 0).addBox(-2.0f, -25.0f, -2.0f, 4, 4, 4, f), pose(-5.0f, 0.0f, 3.0f, -20.0f, -36.0f, 0.0f));
    }

    public static LayerDefinition createLayer(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition trunk = root.addOrReplaceChild("trunk", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0f, -48.0f, -6.0f, 16, 48, 12, f), PartPose.offset(0.0f, -10.0f, 0.0f));
        trunk.addOrReplaceChild("brow_right", CubeListBuilder.create()
                .texOffs(56, 26).addBox(-6.5f, 0.0f, -8.0f, 5, 1, 2, f), pose(0.0f, -39.0f, 0.0f, 0.0f, 0.0f, 10.0f));
        trunk.addOrReplaceChild("brow_left", CubeListBuilder.create()
                .texOffs(56, 26).mirror().addBox(1.5f, 0.0f, -8.0f, 5, 1, 2, f), pose(0.0f, -39.0f, 0.0f, 0.0f, 0.0f, -10.0f));
        trunk.addOrReplaceChild("eye_right", CubeListBuilder.create()
                .texOffs(56, 29).addBox(-1.5f, -2.0f, -7.0f, 3, 3, 1, f.extend(0.2f)), PartPose.offset(-3.5f, -36.0f, 0.0f));
        trunk.addOrReplaceChild("eye_left", CubeListBuilder.create()
                .texOffs(56, 29).mirror().addBox(-1.5f, -2.0f, -7.0f, 3, 3, 1, f.extend(0.2f)), PartPose.offset(3.5f, -36.0f, 0.0f));
        trunk.addOrReplaceChild("nose", CubeListBuilder.create()
                .texOffs(56, 33).addBox(-1.5f, -2.0f, -9.0f, 3, 6, 3, f), PartPose.offset(0.0f, -36.0f, 0.0f));
        trunk.addOrReplaceChild("beard", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-5.0f, 0.0f, -8.0f, 10, 24, 2, f), PartPose.offset(0.0f, -31.0f, 0.0f));
        PartDefinition rightArm = trunk.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(96, 28).addBox(-8.0f, 0.0f, -4.0f, 8, 12, 8, f)
                .texOffs(112, 48).addBox(-7.0f, 12.0f, -2.0f, 4, 16, 4, f), PartPose.offset(-8.0f, -38.0f, 0.0f));
        rightArm.addOrReplaceChild("right_hand", CubeListBuilder.create()
                .texOffs(102, 68).addBox(-2.5f, 0.0f, -4.0f, 5, 16, 8, f)
                .texOffs(102, 92).addBox(-2.0f, 16.0f, -4.0f, 3, 10, 2, f)
                .texOffs(112, 92).addBox(-2.0f, 16.0f, -1.0f, 2, 8, 2, f)
                .texOffs(120, 92).addBox(-2.0f, 16.0f, 2.0f, 2, 6, 2, f), PartPose.offset(-5.0f, 28.0f, 0.0f));
        PartDefinition leftArm = trunk.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
                .texOffs(96, 28).addBox(0.0f, 0.0f, -4.0f, 8, 12, 8, f)
                .texOffs(112, 48).addBox(3.0f, 12.0f, -2.0f, 4, 16, 4, f), PartPose.offset(8.0f, -38.0f, 0.0f));
        leftArm.addOrReplaceChild("left_hand", CubeListBuilder.create().mirror()
                .texOffs(102, 68).addBox(-2.5f, 0.0f, -4.0f, 5, 16, 8, f)
                .texOffs(102, 92).addBox(-1.0f, 16.0f, -4.0f, 3, 10, 2, f)
                .texOffs(112, 92).addBox(0.0f, 16.0f, -1.0f, 2, 8, 2, f)
                .texOffs(120, 92).addBox(0.0f, 16.0f, 2.0f, 2, 6, 2, f), PartPose.offset(5.0f, 28.0f, 0.0f));
        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 60).addBox(-7.0f, -4.0f, -4.0f, 6, 22, 8, f), PartPose.offset(-4.0f, -12.0f, 0.0f));
        rightLeg.addOrReplaceChild("right_foot", CubeListBuilder.create()
                .texOffs(28, 60).addBox(-4.0f, 0.0f, -5.0f, 8, 12, 10, f)
                .texOffs(0, 90).addBox(-5.0f, 12.0f, -7.0f, 10, 6, 15, f)
                .texOffs(0, 111).addBox(2.0f, 13.0f, -16.0f, 3, 5, 9, f)
                .texOffs(24, 113).addBox(-2.0f, 14.0f, -15.0f, 3, 4, 8, f)
                .texOffs(46, 115).addBox(-5.0f, 15.0f, -14.0f, 2, 3, 7, f), PartPose.offset(-4.0f, 18.0f, 0.0f));
        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
                .texOffs(0, 60).addBox(1.0f, -4.0f, -4.0f, 6, 22, 8, f), PartPose.offset(4.0f, -12.0f, 0.0f));
        leftLeg.addOrReplaceChild("left_foot", CubeListBuilder.create().mirror()
                .texOffs(28, 60).addBox(-4.0f, 0.0f, -5.0f, 8, 12, 10, f)
                .texOffs(0, 90).addBox(-5.0f, 12.0f, -7.0f, 10, 6, 15, f)
                .texOffs(0, 111).addBox(-5.0f, 13.0f, -16.0f, 3, 5, 9, f)
                .texOffs(24, 113).addBox(-1.0f, 14.0f, -15.0f, 3, 4, 8, f)
                .texOffs(46, 115).addBox(3.0f, 15.0f, -14.0f, 2, 3, 7, f), PartPose.offset(4.0f, 18.0f, 0.0f));
        addBranches(trunk.addOrReplaceChild("branches", CubeListBuilder.create(), PartPose.offset(0.0f, -48.0f, 0.0f)), f);
        // getExtraHeadBranches: after trunk.postRender, translate(0, -2.7, 0),
        // rotate(angle, Y), rotate(-60, X), translate(0, 2.6, 0), then the
        // branches at their own (0, -48, 0).
        for (int i = 0; i < MAX_EXTRA_BRANCHES; ++i) {
            PartDefinition turn = trunk.addOrReplaceChild("extra_branches_" + i, CubeListBuilder.create(),
                    PartPose.offsetAndRotation(0.0f, -2.7f * 16.0f, 0.0f, rad(-60.0f), 0.0f, 0.0f));
            addBranches(turn.addOrReplaceChild("branches", CubeListBuilder.create(),
                    PartPose.offset(0.0f, 2.6f * 16.0f - 48.0f, 0.0f)), f);
        }
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LOTREntRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        float onGround = state.attackTime;
        this.trunk.xRot = 0.0f;
        if (state.healing) {
            this.trunk.xRot = 0.3f + Mth.sin(f2 * 0.08f) * 0.1f;
        }
        this.eyeRight.visible = state.eyesClosed;
        this.eyeLeft.visible = state.eyesClosed;
        if (state.hurt) {
            this.browRight.zRot = 0.5235988f;
            this.browRight.y = -40.0f;
            this.browLeft.y = -40.0f;
        } else {
            this.browRight.zRot = 0.17453292f;
            this.browRight.y = -39.0f;
            this.browLeft.y = -39.0f;
        }
        this.browLeft.zRot = -this.browRight.zRot;
        this.rightArm.xRot = 0.0f;
        this.rightHand.xRot = 0.0f;
        this.leftArm.xRot = 0.0f;
        this.leftHand.xRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        // onGround > -9990 always held.
        float f6 = 1.0f - onGround;
        f6 *= f6;
        f6 *= f6;
        f6 = 1.0f - f6;
        float f8 = Mth.sin(f6 * Mth.PI);
        float f9 = Mth.sin(onGround * Mth.PI) * -(this.trunk.xRot - 0.7f) * 0.75f;
        this.rightArm.xRot -= f8 * 1.2f + f9;
        this.leftArm.xRot -= f8 * 1.2f + f9;
        this.rightArm.zRot += Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot -= Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.rightArm.xRot += Mth.cos(f * 0.3f + Mth.PI) * 0.8f * f1;
        this.leftArm.xRot += Mth.cos(f * 0.3f) * 0.8f * f1;
        if (state.healing) {
            float armHealing = -0.5f + Mth.sin(f2 * 0.2f) * 0.4f;
            this.rightArm.xRot += armHealing;
            this.leftArm.xRot += armHealing;
        }
        if (this.rightArm.xRot < 0.0f) {
            this.rightHand.xRot = this.rightArm.xRot / Mth.PI * 2.5f;
        }
        if (this.leftArm.xRot < 0.0f) {
            this.leftHand.xRot = this.leftArm.xRot / Mth.PI * 2.5f;
        }
        this.rightLeg.xRot = Mth.cos(f * 0.3f + Mth.PI) * 1.2f * f1;
        this.leftLeg.xRot = Mth.cos(f * 0.3f) * 1.2f * f1;
        this.rightFoot.xRot = 0.0f;
        this.leftFoot.xRot = 0.0f;
        if (this.rightLeg.xRot < 0.0f) {
            this.rightFoot.xRot = -(this.rightLeg.xRot / Mth.PI) * 2.5f;
        }
        if (this.leftLeg.xRot < 0.0f) {
            this.leftFoot.xRot = -(this.leftLeg.xRot / Mth.PI) * 2.5f;
        }
        boolean eyes = this.piece == Piece.EYES;
        this.rightLeg.visible = !eyes;
        this.leftLeg.visible = !eyes;
        int num = eyes ? 0 : state.extraBranches;
        for (int l = 0; l < MAX_EXTRA_BRANCHES; ++l) {
            this.extraBranches[l].visible = l < num;
            if (l < num) {
                this.extraBranches[l].yRot = rad(90.0f + (float) l / num * 360.0f);
            }
        }
    }
}
