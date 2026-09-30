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

/**
 * LOTRModelScorpion: LOTRModelSpider's head, thorax and eight legs (its
 * abdomen emptied), with two clawed arms and a jointed tail. The claws open
 * and the tail swings over as it strikes.
 */
public class LOTRScorpionModel extends EntityModel<LOTRStrikeRenderState> {

    private final ModelPart head;
    private final ModelPart[] legs = new ModelPart[8];
    private final ModelPart armRight;
    private final ModelPart armLeft;
    private final ModelPart tail;

    public LOTRScorpionModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        for (int i = 0; i < 8; ++i) {
            this.legs[i] = root.getChild("leg" + (i + 1));
        }
        this.armRight = root.getChild("arm_right");
        this.armLeft = root.getChild("arm_left");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8), PartPose.offset(0.0f, 17.0f, -3.0f));
        root.addOrReplaceChild("thorax", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6), PartPose.offset(0.0f, 17.0f, 0.0f));
        float[] legZ = {2.0f, 1.0f, 0.0f, -1.0f};
        for (int i = 0; i < 4; ++i) {
            root.addOrReplaceChild("leg" + (2 * i + 1), CubeListBuilder.create()
                    .texOffs(36, 16).addBox(-11.0f, -1.0f, -1.0f, 12, 2, 2)
                    .texOffs(60, 20).addBox(-10.5f, 0.0f, -0.5f, 1, 10, 1),
                    PartPose.offset(-4.0f, 17.0f, legZ[i]));
            root.addOrReplaceChild("leg" + (2 * i + 2), CubeListBuilder.create().mirror()
                    .texOffs(36, 16).addBox(-1.0f, -1.0f, -1.0f, 12, 2, 2)
                    .texOffs(60, 20).addBox(9.5f, 0.0f, -0.5f, 1, 10, 1),
                    PartPose.offset(4.0f, 17.0f, legZ[i]));
        }
        PartDefinition armRight = root.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(36, 16)
                .addBox(-16.0f, -1.0f, 0.0f, 16, 2, 2), PartPose.offset(-3.0f, 18.5f, -4.0f));
        armRight.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-13.0f, -2.0f, -16.0f, 4, 3, 5)
                .addBox(-13.0f, -1.0f, -20.0f, 1, 1, 4)
                .addBox(-10.0f, -1.0f, -20.0f, 1, 1, 4),
                PartPose.rotation(0.0f, 0.87266463f, 0.0f));
        PartDefinition armLeft = root.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(36, 16).mirror()
                .addBox(0.0f, -1.0f, 0.0f, 16, 2, 2), PartPose.offset(3.0f, 18.5f, -4.0f));
        armLeft.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(0, 12).mirror()
                .addBox(9.0f, -2.0f, -16.0f, 4, 3, 5)
                .addBox(12.0f, -1.0f, -20.0f, 1, 1, 4)
                .addBox(9.0f, -1.0f, -20.0f, 1, 1, 4),
                PartPose.rotation(0.0f, -0.87266463f, 0.0f));
        PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-2.5f, -3.0f, 0.0f, 5, 5, 11), PartPose.offset(0.0f, 19.5f, 3.0f));
        PartDefinition tail1 = tail.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-2.0f, -2.0f, 0.0f, 4, 4, 10), PartPose.offsetAndRotation(0.0f, -0.5f, 11.0f, 0.6981317f, 0.0f, 0.0f));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-1.5f, -2.0f, 0.0f, 3, 4, 10), PartPose.offsetAndRotation(0.0f, 0.0f, 11.0f, 0.6981317f, 0.0f, 0.0f));
        tail2.addOrReplaceChild("sting", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-1.0f, -0.5f, 0.0f, 2, 3, 5)
                .addBox(-0.5f, 0.0f, 5.0f, 1, 1, 3),
                PartPose.offsetAndRotation(0.0f, 0.0f, 9.0f, 1.5707964f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRStrikeRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        // LOTRModelSpider.setRotationAngles.
        float f6 = -0.51460177f;
        float[] zBase = {-f6, f6, -f6 * 0.74f, f6 * 0.74f, -f6 * 0.74f, f6 * 0.74f, -f6, f6};
        float f8 = 0.3926991f;
        float[] yBase = {f8 * 2.0f, -f8 * 2.0f, f8, -f8, -f8, f8, -f8 * 2.0f, f8 * 2.0f};
        float[] swingY = new float[4];
        float[] swingZ = new float[4];
        float[] phase = {0.0f, Mth.PI, Mth.HALF_PI, 4.712389f};
        for (int i = 0; i < 4; ++i) {
            swingY[i] = -(Mth.cos(f * 0.6662f * 2.0f + phase[i]) * 0.4f) * f1;
            swingZ[i] = Math.abs(Mth.sin(f * 0.6662f + phase[i]) * 0.4f) * f1;
        }
        for (int i = 0; i < 8; ++i) {
            float sign = i % 2 == 0 ? 1.0f : -1.0f;
            this.legs[i].zRot = zBase[i] + sign * swingZ[i / 2];
            this.legs[i].yRot = yBase[i] + sign * swingY[i / 2];
        }
        float strike = state.strike;
        this.armRight.yRot = -0.87266463f + Mth.cos(f * 0.4f) * f1 * 0.4f + strike * -0.6981317f;
        this.armLeft.yRot = -this.armRight.yRot;
        this.tail.xRot = 0.5235988f + Mth.cos(f * 0.4f) * f1 * 0.15f + strike * Mth.HALF_PI;
    }
}
