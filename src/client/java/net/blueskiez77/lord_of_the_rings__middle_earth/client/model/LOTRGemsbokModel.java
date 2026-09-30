package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * LOTRModelGemsbok, box for box; the white oryx wears it too. The head, ears
 * and horns are separate parts that follow the head's angles, as they were.
 *
 * <p>The calf is the whole model at half size about the ground, and without
 * horns: the original's child branch simply never rendered them.
 */
public class LOTRGemsbokModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart leftHorn;
    private final ModelPart rightHorn;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;

    public LOTRGemsbokModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.neck = root.getChild("neck");
        this.earLeft = root.getChild("ear_left");
        this.earRight = root.getChild("ear_right");
        this.leftHorn = root.getChild("left_horn");
        this.rightHorn = root.getChild("right_horn");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(true), 128, 64);
    }

    public static LayerDefinition createBabyLayer() {
        return LayerDefinition.create(createMesh(false), 128, 64).apply(MeshTransformer.scaling(0.5f));
    }

    private static MeshDefinition createMesh(boolean horns) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartPose headPose = PartPose.offset(0.0f, 4.0f, -9.0f);
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0)
                .addBox(-3.0f, -10.0f, -6.0f, 6, 7, 12), headPose);
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0)
                .addBox(0.0f, 0.0f, 0.0f, 2, 12, 2), PartPose.offset(-1.0f, 3.0f, 11.0f));
        root.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(28, 19)
                .addBox(-3.8f, -12.0f, 3.0f, 1, 3, 2), headPose);
        root.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(34, 19)
                .addBox(2.8f, -12.0f, 3.0f, 1, 3, 2), headPose);
        root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 14)
                .addBox(-2.5f, -6.0f, -5.0f, 5, 8, 9), headPose);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31)
                .addBox(-7.0f, -10.0f, -7.0f, 13, 10, 23), PartPose.offset(0.5f, 12.0f, -3.0f));
        CubeListBuilder hindLeg = CubeListBuilder.create().texOffs(0, 38).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        CubeListBuilder foreLeg = CubeListBuilder.create().texOffs(0, 38).addBox(-2.0f, 0.0f, -3.0f, 4, 12, 4);
        root.addOrReplaceChild("leg1", hindLeg, PartPose.offset(-4.0f, 12.0f, 10.0f));
        root.addOrReplaceChild("leg2", hindLeg, PartPose.offset(4.0f, 12.0f, 10.0f));
        root.addOrReplaceChild("leg3", foreLeg, PartPose.offset(-4.0f, 12.0f, -7.0f));
        root.addOrReplaceChild("leg4", foreLeg, PartPose.offset(4.0f, 12.0f, -7.0f));
        CubeListBuilder left = CubeListBuilder.create();
        CubeListBuilder right = CubeListBuilder.create();
        if (horns) {
            left.texOffs(0, 0).addBox(-2.8f, -9.5f, 5.8f, 1, 1, 13);
            right.texOffs(0, 0).addBox(1.8f, -9.5f, 5.8f, 1, 1, 13);
        }
        root.addOrReplaceChild("left_horn", left, headPose);
        root.addOrReplaceChild("right_horn", right, headPose);
        return mesh;
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD + 0.4014257f;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.neck.xRot = -1.0646508f;
        this.neck.yRot = this.head.yRot * 0.7f;
        for (ModelPart part : new ModelPart[] {this.rightHorn, this.leftHorn, this.earLeft, this.earRight}) {
            part.xRot = this.head.xRot;
            part.yRot = this.head.yRot;
        }
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.tail.xRot = 0.29670596f;
    }
}
