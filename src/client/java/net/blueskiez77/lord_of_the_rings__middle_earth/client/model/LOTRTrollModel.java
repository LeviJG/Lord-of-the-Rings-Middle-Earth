package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelTroll, transcribed box for box.
 *
 * <p>Kept as an entity-style model built from {@code texOffs} numbers rather
 * than converted to JSON, for the same reason as the weapon rack and the troll
 * totem: the sheet is a 128x128 entity texture and hand-computing those faces
 * would be a transcription error waiting to happen.
 *
 * <p>Only what the stone troll needs is here. The original's {@code headHurt}
 * part and its four weapons -- club, spiked club, warhammer, battleaxe --
 * belong to LOTREntityTroll and its kin, which the port has no NPCs for yet;
 * they are left out rather than carried as dead geometry, and this note is
 * where to start when the trolls themselves are ported.
 *
 * <p>The original built three of these for one troll and the renderer drew all
 * three: the body at f = 0, then a shirt inflated by 1.0 and trousers by 0.75,
 * both in the outfit's texture. {@link Piece} is that second constructor
 * argument -- the switch that decided which parts a given copy shows.
 */
public class LOTRTrollModel {

    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 128;

    /** LOTRModelTroll(float, int): which parts an outfit layer shows. */
    public enum Piece {
        /** The whole troll: the base model, drawn in its own skin. */
        ALL,
        /** i = 0: head, body and arms -- the shirt. */
        SHIRT,
        /** i = 1: the legs only -- the trousers. */
        TROUSERS;

        boolean showsHead() {
            return this != TROUSERS;
        }

        boolean showsBody() {
            return this != TROUSERS;
        }

        boolean showsLegs() {
            return this != SHIRT;
        }
    }

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final Piece piece;

    public LOTRTrollModel(ModelPart root, Piece piece) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.piece = piece;
    }

    public static LayerDefinition createLayer(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-6.0f, -6.0f, -12.0f, 12, 12, 12, f)
                        // The ears, mirrored either side, then the nose.
                        .texOffs(40, 0).addBox(6.0f, -2.0f, -8.0f, 1, 4, 3, f)
                        .texOffs(40, 0).mirror().addBox(-7.0f, -2.0f, -8.0f, 1, 4, 3, f)
                        .mirror(false)
                        .texOffs(0, 0).addBox(-1.0f, -1.0f, -14.0f, 2, 3, 2, f),
                PartPose.offset(0.0f, -27.0f, -6.0f));

        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(48, 0).addBox(-12.0f, -28.0f, -8.0f, 24, 28, 16, f),
                PartPose.ZERO);

        // Each arm is a shoulder plus a longer forearm hanging off it.
        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(0, 24).mirror().addBox(-12.0f, -3.0f, -6.0f, 12, 12, 12, f)
                        .texOffs(0, 48).mirror().addBox(-11.0f, 9.0f, -5.0f, 10, 20, 10, f),
                PartPose.offset(-12.0f, -23.0f, 0.0f));

        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(0, 24).addBox(0.0f, -3.0f, -6.0f, 12, 12, 12, f)
                        .texOffs(0, 48).addBox(1.0f, 9.0f, -5.0f, 10, 20, 10, f),
                PartPose.offset(12.0f, -23.0f, 0.0f));

        // The feet took no deformation in the original -- addBox without the
        // trailing f -- so the trouser layer stops at the ankle.
        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 78).mirror().addBox(-6.0f, 0.0f, -6.0f, 11, 12, 12, f)
                        .texOffs(0, 102).mirror().addBox(-5.5f, 12.0f, -5.0f, 10, 12, 10),
                PartPose.offset(-6.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 78).addBox(-5.0f, 0.0f, -6.0f, 11, 12, 12, f)
                        .texOffs(0, 102).addBox(-4.5f, 12.0f, -5.0f, 10, 12, 10),
                PartPose.offset(6.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /**
     * The statue's pose, which is setRotationAngles with every animation input
     * at zero -- as LOTRRenderStoneTroll passed them.
     *
     * <p>Worth spelling out, because it is not simply the rest pose. With
     * limbSwing, limbSwingAmount, ageInTicks, yaw and pitch all zero, every
     * walk and look term drops out, and ModelBase.onGround is 0 so its swing
     * block contributes nothing either -- except for the last two lines, which
     * are unconditional: {@code rightArm.rotateAngleZ += cos(0) * 0.05 + 0.05}
     * and its mirror on the left. That leaves the arms a tenth of a radian out
     * from the body, which is exactly how a stone troll stands.
     */
    public void setStatuePose() {
        this.head.setPos(0.0f, -27.0f, -6.0f);
        this.head.xRot = 0.0f;
        this.head.yRot = 0.0f;
        this.head.zRot = 0.0f;

        this.body.setPos(0.0f, 0.0f, 0.0f);
        this.body.xRot = 0.0f;
        this.body.yRot = 0.0f;
        this.body.zRot = 0.0f;

        this.rightArm.setPos(-12.0f, -23.0f, 0.0f);
        this.rightArm.xRot = 0.0f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.1f;

        this.leftArm.setPos(12.0f, -23.0f, 0.0f);
        this.leftArm.xRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = -0.1f;

        this.rightLeg.xRot = 0.0f;
        this.rightLeg.yRot = 0.0f;
        this.leftLeg.xRot = 0.0f;
        this.leftLeg.yRot = 0.0f;
    }

    /**
     * The Hill-troll Chieftain trophy is the troll's head alone, and
     * LOTRRenderBossTrophy moves its rotation point to (0, -6, 6) before
     * drawing it -- the living troll's (0, -27, -6) would put a severed head
     * far above where it was set down.
     */
    public void setTrophyPose() {
        this.head.setPos(0.0f, -6.0f, 6.0f);
        this.head.xRot = 0.0f;
        this.head.yRot = 0.0f;
        this.head.zRot = 0.0f;
    }

    /** One head, for the trophy: no body, arms or legs. */
    public void renderHead(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }

    /**
     * render(): the head first -- twice, tilted apart, if the troll has two of
     * them -- then the body, arms and legs, each skipped when this layer does
     * not show it.
     */
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
            boolean twoHeads) {
        if (this.piece.showsHead()) {
            if (twoHeads) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.ZP.rotationDegrees(-15.0f));
                poseStack.mulPose(Axis.YP.rotationDegrees(10.0f));
                this.head.render(poseStack, consumer, light, overlay);
                poseStack.popPose();

                poseStack.pushPose();
                poseStack.mulPose(Axis.ZP.rotationDegrees(15.0f));
                poseStack.mulPose(Axis.YP.rotationDegrees(-10.0f));
                this.head.render(poseStack, consumer, light, overlay);
                poseStack.popPose();
            } else {
                this.head.render(poseStack, consumer, light, overlay);
            }
        }
        if (this.piece.showsBody()) {
            this.body.render(poseStack, consumer, light, overlay);
            this.rightArm.render(poseStack, consumer, light, overlay);
            this.leftArm.render(poseStack, consumer, light, overlay);
        }
        if (this.piece.showsLegs()) {
            this.rightLeg.render(poseStack, consumer, light, overlay);
            this.leftLeg.render(poseStack, consumer, light, overlay);
        }
    }
}
