package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRTrollRenderState;

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
 * LOTRModelTroll for a living troll, box for box as {@link LOTRTrollModel}
 * (the statue's), with what only the living use: the hurt head (the same
 * boxes on another part of the sheet), the four weapons, and the animation.
 *
 * <p>A two-headed troll's heads were drawn by rotating the whole model 15
 * degrees one way and 10 the other before drawing the head, twice; here each
 * copy hangs from a pivot at the model's origin turned by just that, which is
 * the same transform.
 *
 * <p>Which parts a copy draws is its {@link Piece}: the troll in its skin, a
 * shirt or trousers in the outfit (LOTRModelTroll(f, i)), or one weapon, in
 * the weapon sheet, held in the right hand.
 */
public class LOTRTrollLivingModel extends EntityModel<LOTRTrollRenderState> {

    public enum Piece {
        ALL, SHIRT, TROUSERS, HELMET, CHESTPLATE, WOODEN_CLUB, WOODEN_CLUB_SPIKED, WARHAMMER, BATTLEAXE
    }

    private final Piece piece;
    private final ModelPart head;
    private final ModelPart headHurt;
    private final ModelPart headsLeft;
    private final ModelPart headsRight;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart woodenClub;
    private final ModelPart woodenClubSpikes;
    private final ModelPart warhammer;
    private final ModelPart battleaxe;

    public LOTRTrollLivingModel(ModelPart root, Piece piece) {
        super(root);
        this.piece = piece;
        this.head = root.getChild("head");
        this.headHurt = root.getChild("head_hurt");
        this.headsLeft = root.getChild("heads_left");
        this.headsRight = root.getChild("heads_right");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.woodenClub = root.getChild("wooden_club");
        this.woodenClubSpikes = root.getChild("wooden_club_spikes");
        this.warhammer = root.getChild("warhammer");
        this.battleaxe = root.getChild("battleaxe");
    }

    private static CubeListBuilder headBoxes(int u, int v, CubeDeformation f) {
        return CubeListBuilder.create()
                .texOffs(u, v).addBox(-6.0f, -6.0f, -12.0f, 12, 12, 12, f)
                .texOffs(40, 0).addBox(6.0f, -2.0f, -8.0f, 1, 4, 3, f)
                .texOffs(40, 0).mirror().addBox(-7.0f, -2.0f, -8.0f, 1, 4, 3, f)
                .mirror(false)
                .texOffs(0, 0).addBox(-1.0f, -1.0f, -14.0f, 2, 3, 2, f);
    }

    public static LayerDefinition createLayer(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartPose headPose = PartPose.offset(0.0f, -27.0f, -6.0f);
        root.addOrReplaceChild("head", headBoxes(0, 0, f), headPose);
        root.addOrReplaceChild("head_hurt", headBoxes(48, 44, f), headPose);
        PartDefinition left = root.addOrReplaceChild("heads_left", CubeListBuilder.create(),
                PartPose.rotation(0.0f, 10.0f * Mth.DEG_TO_RAD, -15.0f * Mth.DEG_TO_RAD));
        left.addOrReplaceChild("head", headBoxes(0, 0, f), headPose);
        left.addOrReplaceChild("head_hurt", headBoxes(48, 44, f), headPose);
        PartDefinition right = root.addOrReplaceChild("heads_right", CubeListBuilder.create(),
                PartPose.rotation(0.0f, -10.0f * Mth.DEG_TO_RAD, 15.0f * Mth.DEG_TO_RAD));
        right.addOrReplaceChild("head", headBoxes(0, 0, f), headPose);
        right.addOrReplaceChild("head_hurt", headBoxes(48, 44, f), headPose);
        root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-12.0f, -28.0f, -8.0f, 24, 28, 16, f), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(0, 24).mirror().addBox(-12.0f, -3.0f, -6.0f, 12, 12, 12, f)
                .texOffs(0, 48).mirror().addBox(-11.0f, 9.0f, -5.0f, 10, 20, 10, f),
                PartPose.offset(-12.0f, -23.0f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(0, 24).addBox(0.0f, -3.0f, -6.0f, 12, 12, 12, f)
                .texOffs(0, 48).addBox(1.0f, 9.0f, -5.0f, 10, 20, 10, f),
                PartPose.offset(12.0f, -23.0f, 0.0f));
        // The feet took no deformation, so the trousers stop at the ankle.
        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 78).mirror().addBox(-6.0f, 0.0f, -6.0f, 11, 12, 12, f)
                .texOffs(0, 102).mirror().addBox(-5.5f, 12.0f, -5.0f, 10, 12, 10),
                PartPose.offset(-6.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 78).addBox(-5.0f, 0.0f, -6.0f, 11, 12, 12, f)
                .texOffs(0, 102).addBox(-4.5f, 12.0f, -5.0f, 10, 12, 10),
                PartPose.offset(6.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("wooden_club", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-9.0f, 5.0f, 21.0f, 6, 24, 6, f), PartPose.offset(-12.0f, -23.0f, 0.0f));
        root.addOrReplaceChild("wooden_club_spikes", CubeListBuilder.create()
                .texOffs(24, 0).addBox(-12.0f, 25.0f, 23.5f, 12, 1, 1, f)
                .texOffs(24, 0).addBox(-12.0f, 20.0f, 23.5f, 12, 1, 1, f)
                .texOffs(24, 0).addBox(-12.0f, 15.0f, 23.5f, 12, 1, 1, f)
                .texOffs(24, 2).addBox(-6.5f, 25.0f, 18.0f, 1, 1, 12, f)
                .texOffs(24, 2).addBox(-6.5f, 20.0f, 18.0f, 1, 1, 12, f)
                .texOffs(24, 2).addBox(-6.5f, 15.0f, 18.0f, 1, 1, 12, f), PartPose.offset(-12.0f, -23.0f, 0.0f));
        root.addOrReplaceChild("warhammer", CubeListBuilder.create()
                .texOffs(52, 29).addBox(-7.5f, 5.0f, 22.5f, 3, 20, 3, f)
                .texOffs(0, 32).addBox(-12.0f, 25.0f, 14.0f, 12, 12, 20, f), PartPose.offset(-12.0f, -23.0f, 0.0f));
        root.addOrReplaceChild("battleaxe", CubeListBuilder.create()
                .texOffs(64, 0).addBox(-7.0f, -40.0f, 22.5f, 2, 80, 2, f)
                .texOffs(72, 0).addBox(-6.0f, 20.0f, 24.0f, 0, 24, 16, f), PartPose.offset(-12.0f, -23.0f, 0.0f));
        return LayerDefinition.create(mesh, LOTRTrollModel.TEXTURE_WIDTH, LOTRTrollModel.TEXTURE_HEIGHT);
    }

    /** setRotationAngles, for a living troll. */
    @Override
    public void setupAnim(LOTRTrollRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        float onGround = state.attackTime;
        this.head.x = 0.0f;
        this.head.y = -27.0f;
        this.head.zRot = 0.0f;
        this.body.x = 0.0f;
        this.body.y = 0.0f;
        this.body.zRot = 0.0f;
        this.rightArm.x = -12.0f;
        this.rightArm.y = -23.0f;
        this.leftArm.x = 12.0f;
        this.leftArm.y = -23.0f;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        if (state.sniff > 0.0f) {
            this.head.yRot = Mth.sin(state.sniff / 8.0f * Mth.TWO_PI) * 0.5f;
        }
        this.rightArm.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 2.0f * f1 * 0.5f;
        this.leftArm.xRot = Mth.cos(f * 0.6662f) * 2.0f * f1 * 0.5f;
        this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.31415927f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        // onGround > -9990 always held.
        float f6 = onGround;
        this.body.yRot = Mth.sin(Mth.sqrt(f6) * Mth.PI * 2.0f) * 0.2f;
        this.rightArm.z = Mth.sin(this.body.yRot) * 5.0f;
        this.rightArm.x = -Mth.cos(this.body.yRot) * 12.0f;
        this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0f;
        this.leftArm.x = Mth.cos(this.body.yRot) * 12.0f;
        this.leftArm.xRot += this.body.yRot;
        f6 = 1.0f - onGround;
        f6 *= f6;
        f6 *= f6;
        f6 = 1.0f - f6;
        float f7 = Mth.sin(f6 * Mth.PI);
        float f8 = Mth.sin(onGround * Mth.PI) * -(this.head.xRot - 0.7f) * 0.75f;
        this.rightArm.xRot = (float) (this.rightArm.xRot - (f7 * 1.2 + f8));
        this.rightArm.zRot = Mth.sin(onGround * Mth.PI) * -0.4f;
        this.rightLeg.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leftLeg.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.rightLeg.yRot = 0.0f;
        this.leftLeg.yRot = 0.0f;
        this.rightArm.yRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        this.rightArm.zRot += Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot -= Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.rightArm.xRot += Mth.sin(f2 * 0.067f) * 0.05f;
        this.leftArm.xRot -= Mth.sin(f2 * 0.067f) * 0.05f;
        if (state.throwing) {
            this.rightArm.xRot -= 0.5f;
            this.rightArm.zRot -= 0.4f;
            this.leftArm.xRot = this.rightArm.xRot;
            this.leftArm.yRot = -this.rightArm.yRot;
            this.leftArm.zRot = -this.rightArm.zRot;
        }
        // The lumbering sway of a walking troll.
        float f62 = Mth.sin(f * 0.2f) * 0.3f * f1;
        this.head.x += Mth.sin(f62) * 27.0f;
        this.head.y += 27.0f - Mth.cos(f62) * 27.0f;
        this.head.zRot += f62;
        this.body.zRot += f62;
        float armRotationOffsetX = Mth.sin(f62) * 23.0f + Mth.cos(f62) * 12.0f - 12.0f;
        float armRotationOffsetY = Mth.cos(f62) * -23.0f + Mth.sin(f62) * 12.0f + 23.0f;
        this.rightArm.x += armRotationOffsetX;
        this.rightArm.y -= armRotationOffsetY;
        this.rightArm.zRot += f62;
        this.leftArm.x += armRotationOffsetX;
        this.leftArm.y += armRotationOffsetY;
        this.leftArm.zRot += f62;

        this.headHurt.loadPose(this.head.storePose());
        for (ModelPart pair : new ModelPart[]{this.headsLeft, this.headsRight}) {
            pair.getChild("head").loadPose(this.head.storePose());
            pair.getChild("head_hurt").loadPose(this.head.storePose());
        }
        for (ModelPart weapon : new ModelPart[]{this.woodenClub, this.woodenClubSpikes, this.warhammer, this.battleaxe}) {
            weapon.x = this.rightArm.x;
            weapon.y = this.rightArm.y;
            weapon.z = this.rightArm.z;
            weapon.xRot = this.rightArm.xRot - Mth.HALF_PI;
            weapon.yRot = this.rightArm.yRot;
            weapon.zRot = this.rightArm.zRot;
        }
        showPieces(state);
    }

    /** rightArm.postRender: to the right hand, where a mountain troll holds its rock. */
    public void translateToRightArm(com.mojang.blaze3d.vertex.PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.rightArm.translateAndRotate(poseStack);
    }

    /** render(): the heads (hurt, and two, as the troll is), then the rest; or one weapon. */
    private void showPieces(LOTRTrollRenderState state) {
        boolean weapon = this.piece.ordinal() >= Piece.WOODEN_CLUB.ordinal();
        boolean showHead = this.piece == Piece.ALL || this.piece == Piece.SHIRT || this.piece == Piece.HELMET;
        boolean showBody = this.piece == Piece.ALL || this.piece == Piece.SHIRT || this.piece == Piece.CHESTPLATE;
        boolean showLegs = this.piece == Piece.ALL || this.piece == Piece.TROUSERS;
        boolean hurt = this.piece == Piece.ALL && state.headHurt;
        this.head.visible = showHead && !state.twoHeads && !hurt;
        this.headHurt.visible = showHead && !state.twoHeads && hurt;
        this.headsLeft.visible = this.headsRight.visible = showHead && state.twoHeads;
        for (ModelPart pair : new ModelPart[]{this.headsLeft, this.headsRight}) {
            pair.getChild("head").visible = !hurt;
            pair.getChild("head_hurt").visible = hurt;
        }
        this.body.visible = this.rightArm.visible = this.leftArm.visible = showBody;
        this.rightLeg.visible = this.leftLeg.visible = showLegs;
        this.woodenClub.visible = weapon && (this.piece == Piece.WOODEN_CLUB || this.piece == Piece.WOODEN_CLUB_SPIKED);
        this.woodenClubSpikes.visible = this.piece == Piece.WOODEN_CLUB_SPIKED;
        this.warhammer.visible = this.piece == Piece.WARHAMMER;
        this.battleaxe.visible = this.piece == Piece.BATTLEAXE;
    }
}
