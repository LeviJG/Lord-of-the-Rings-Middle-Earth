package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRFlamingoRenderState;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRFlamingoEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelFlamingo. The original held two whole sets of parts, adult and
 * chick, and drew one; here they are two layers of the same model. Only the
 * adult fishes: its head swings down under the water over the first second,
 * stays there, and comes back up over the last.
 */
public class LOTRFlamingoModel extends EntityModel<LOTRFlamingoRenderState> {

    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart legLeft;
    private final ModelPart legRight;

    public LOTRFlamingoModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.wingLeft = root.getChild("wing_left");
        this.wingRight = root.getChild("wing_right");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(8, 24).addBox(-2.0f, -17.0f, -2.0f, 4, 4, 4)
                .texOffs(24, 27).addBox(-1.5f, -16.0f, -5.0f, 3, 2, 3)
                .texOffs(36, 30).addBox(-1.0f, -14.0f, -5.0f, 2, 1, 1)
                .texOffs(0, 16).addBox(-1.0f, -15.0f, -1.0f, 2, 14, 2),
                PartPose.offset(0.0f, 5.0f, -2.0f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0f, 0.0f, -4.0f, 6, 7, 8), PartPose.offset(0.0f, 3.0f, 0.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(42, 23)
                .addBox(-2.5f, 0.0f, 0.0f, 5, 3, 6), PartPose.offset(0.0f, 4.0f, 3.0f));
        root.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(36, 0)
                .addBox(-1.0f, 0.0f, -3.0f, 1, 8, 6), PartPose.offset(-3.0f, 3.0f, 0.0f));
        root.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(50, 0)
                .addBox(0.0f, 0.0f, -3.0f, 1, 8, 6), PartPose.offset(3.0f, 3.0f, 0.0f));
        CubeListBuilder leg = CubeListBuilder.create()
                .texOffs(30, 0).addBox(-0.5f, 0.0f, -0.5f, 1, 16, 1)
                .texOffs(30, 17).addBox(-1.5f, 14.9f, -3.5f, 3, 1, 3);
        root.addOrReplaceChild("leg_left", leg, PartPose.offset(-2.0f, 8.0f, 0.0f));
        root.addOrReplaceChild("leg_right", leg, PartPose.offset(2.0f, 8.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 24).addBox(-2.0f, -4.0f, -4.0f, 4, 4, 4)
                .texOffs(16, 28).addBox(-1.0f, -2.0f, -6.0f, 2, 2, 2),
                PartPose.offset(0.0f, 15.0f, -3.0f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0f, 0.0f, -4.0f, 6, 5, 7), PartPose.offset(0.0f, 14.0f, 0.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 14)
                .addBox(-2.0f, 0.0f, 0.0f, 4, 2, 3), PartPose.offset(0.0f, 14.5f, 3.0f));
        root.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(40, 0)
                .addBox(-1.0f, 0.0f, -3.0f, 1, 4, 5), PartPose.offset(-3.0f, 14.0f, 0.0f));
        root.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(52, 0)
                .addBox(0.0f, 0.0f, -3.0f, 1, 4, 5), PartPose.offset(3.0f, 14.0f, 0.0f));
        CubeListBuilder leg = CubeListBuilder.create()
                .texOffs(27, 0).addBox(-0.5f, 0.0f, -0.5f, 1, 5, 1)
                .texOffs(27, 7).addBox(-1.5f, 3.9f, -3.5f, 3, 1, 3);
        root.addOrReplaceChild("leg_left", leg, PartPose.offset(-2.0f, 19.0f, 0.0f));
        root.addOrReplaceChild("leg_right", leg, PartPose.offset(2.0f, 19.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRFlamingoRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.legLeft.xRot = Mth.cos(f * 0.6662f) * 0.9f * f1;
        this.legRight.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 0.9f * f1;
        this.wingLeft.zRot = state.flap * 0.4f;
        this.wingRight.zRot = -state.flap * 0.4f;
        this.tail.xRot = -0.25f;
        if (!state.isBaby) {
            int cur = state.fishingCur;
            float fishing = state.fishing;
            int total = LOTRFlamingoEntity.FISHING_TIME_TOTAL;
            int neck = LOTRFlamingoEntity.NECK_TIME;
            if (cur > LOTRFlamingoEntity.FISHING_TIME + neck) {
                this.head.xRot = Mth.PI * (total - fishing) / neck;
            } else if (cur > neck) {
                this.head.xRot = Mth.PI;
            } else if (cur > 0) {
                this.head.xRot = Mth.PI * fishing / neck;
            }
        }
    }
}
