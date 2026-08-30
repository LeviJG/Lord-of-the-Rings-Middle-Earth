package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelTrollTotem, transcribed box for box.
 *
 * <p>Kept as an entity-style model rather than converted to a block model
 * JSON for two reasons: the texture is a 128x64 entity sheet rather than a
 * block sprite, and CubeListBuilder does the box UV unwrapping from the same
 * {@code texOffs} numbers the original used -- hand-computing forty-eight face
 * UVs into a JSON model would be a transcription error waiting to happen.
 *
 * <p>Rendered in three independent groups, because the three parts are three
 * separate blocks: {@link #renderBase}, {@link #renderBody}, {@link #renderHead}.
 */
public class LOTRTrollTotemModel {

    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 64;

    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart base;

    public LOTRTrollTotemModel(ModelPart root) {
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.base = root.getChild("base");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // head: the skull, the horn on its brow, and an ear either side.
        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-6.0f, -10.0f, -10.0f, 12, 10, 12)
                        .addBox(-1.0f, -5.0f, -12.0f, 2, 3, 2)
                        .texOffs(40, 0)
                        .addBox(-7.0f, -6.0f, -6.0f, 1, 4, 3)
                        .mirror()
                        .addBox(6.0f, -6.0f, -6.0f, 1, 4, 3)
                        .mirror(false),
                PartPose.offset(0.0f, 22.0f, 4.0f));

        root.addOrReplaceChild("jaw", CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(-6.0f, -2.0f, -6.0f, 12, 2, 12),
                PartPose.offset(0.0f, 24.0f, 0.0f));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-5.0f, 0.0f, -5.0f, 10, 16, 10),
                PartPose.offset(0.0f, 8.0f, 0.0f));

        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(40, 24)
                        .addBox(-3.0f, 0.0f, -3.0f, 3, 10, 6),
                PartPose.offset(-5.0f, 9.0f, 0.0f));

        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(40, 24)
                        .mirror()
                        .addBox(0.0f, 0.0f, -3.0f, 3, 10, 6),
                PartPose.offset(5.0f, 9.0f, 0.0f));

        // Each leg is a thigh with a narrower shin under it.
        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 50)
                        .addBox(-3.0f, 0.0f, -3.0f, 6, 7, 6)
                        .texOffs(24, 50)
                        .addBox(-2.5f, 7.0f, -2.5f, 5, 7, 5),
                PartPose.offset(-4.0f, 8.0f, 0.0f));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 50)
                        .mirror()
                        .addBox(-3.0f, 0.0f, -3.0f, 6, 7, 6)
                        .texOffs(24, 50)
                        .addBox(-2.5f, 7.0f, -2.5f, 5, 7, 5),
                PartPose.offset(4.0f, 8.0f, 0.0f));

        root.addOrReplaceChild("base", CubeListBuilder.create()
                        .texOffs(48, 46)
                        .addBox(-8.0f, 0.0f, -8.0f, 16, 2, 16),
                PartPose.offset(0.0f, 22.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** The plinth and the two legs standing on it. */
    public void renderBase(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        rightLeg.render(poseStack, consumer, light, overlay);
        leftLeg.render(poseStack, consumer, light, overlay);
        base.render(poseStack, consumer, light, overlay);
    }

    /** The torso and both arms. */
    public void renderBody(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        body.render(poseStack, consumer, light, overlay);
        rightArm.render(poseStack, consumer, light, overlay);
        leftArm.render(poseStack, consumer, light, overlay);
    }

    /**
     * The skull and its jaw. {@code jawDegrees} tips the SKULL back -- the
     * original rotated the head, not the jaw, so the mouth gapes upward.
     */
    public void renderHead(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
                           float jawDegrees) {
        head.xRot = jawDegrees * Mth.DEG_TO_RAD;
        head.render(poseStack, consumer, light, overlay);
        jaw.render(poseStack, consumer, light, overlay);
    }
}
