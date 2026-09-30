package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * LOTRModelSpider, box for box, on a 64x32 skin: a head, thorax and abdomen
 * that sways as it walks, and eight legs each with a hanging foot. The spider
 * itself is drawn inflated by 0.5, its glowing eyes by 0.55.
 */
public class LOTRSpiderModel extends EntityModel<LivingEntityRenderState> {

    private static final String[] LEGS = {"leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7", "leg8"};

    private final ModelPart head;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart[] legs = new ModelPart[8];

    public LOTRSpiderModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.thorax = root.getChild("thorax");
        this.abdomen = root.getChild("abdomen");
        for (int i = 0; i < 8; ++i) {
            this.legs[i] = root.getChild(LEGS[i]);
        }
    }

    public static LayerDefinition createBodyLayer(float f) {
        CubeDeformation d = new CubeDeformation(f);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4.0f, -4.0f, -8.0f, 8.0f, 8.0f, 8.0f, d), PartPose.offset(0.0f, 17.0f, -3.0f));
        root.addOrReplaceChild("thorax", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0f, -3.0f, -3.0f, 6.0f, 6.0f, 6.0f, d), PartPose.offset(0.0f, 17.0f, 0.0f));
        root.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 12)
                .addBox(-5.0f, -4.0f, -0.5f, 10.0f, 8.0f, 12.0f, d), PartPose.offset(0.0f, 17.0f, 3.0f));
        float[] legZ = {2.0f, 2.0f, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, -1.0f};
        for (int i = 0; i < 8; ++i) {
            boolean right = i % 2 == 0;
            CubeListBuilder leg = CubeListBuilder.create().texOffs(36, 16);
            if (right) {
                leg.addBox(-11.0f, -1.0f, -1.0f, 12.0f, 2.0f, 2.0f, d)
                        .texOffs(60, 20).addBox(-10.5f, 0.0f, -0.5f, 1.0f, 10.0f, 1.0f, d);
            } else {
                leg.mirror().addBox(-1.0f, -1.0f, -1.0f, 12.0f, 2.0f, 2.0f, d)
                        .texOffs(60, 20).addBox(9.5f, 0.0f, -0.5f, 1.0f, 10.0f, 1.0f, d);
            }
            root.addOrReplaceChild(LEGS[i], leg, PartPose.offset(right ? -4.0f : 4.0f, 17.0f, legZ[i]));
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    /** renderGlowingEyes: the head alone. */
    public void showOnlyHead() {
        this.thorax.visible = false;
        this.abdomen.visible = false;
        for (ModelPart leg : this.legs) {
            leg.visible = false;
        }
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.abdomen.yRot = Mth.cos(f * 0.6662f) * 0.5f * f1;
        float f6 = -0.51460177f;
        float[] zRot = {-f6, f6, -f6 * 0.74f, f6 * 0.74f, -f6 * 0.74f, f6 * 0.74f, -f6, f6};
        float f8 = 0.3926991f;
        float[] yRot = {f8 * 2.0f, -f8 * 2.0f, f8, -f8, -f8, f8, -f8 * 2.0f, f8 * 2.0f};
        for (int i = 0; i < 8; ++i) {
            this.legs[i].zRot = zRot[i];
            this.legs[i].yRot = yRot[i];
        }
        float f9 = -(Mth.cos(f * 0.6662f * 2.0f) * 0.4f) * f1;
        float f10 = -(Mth.cos(f * 0.6662f * 2.0f + Mth.PI) * 0.4f) * f1;
        float f11 = -(Mth.cos(f * 0.6662f * 2.0f + Mth.HALF_PI) * 0.4f) * f1;
        float f12 = -(Mth.cos(f * 0.6662f * 2.0f + Mth.PI * 1.5f) * 0.4f) * f1;
        float f13 = Math.abs(Mth.sin(f * 0.6662f) * 0.4f) * f1;
        float f14 = Math.abs(Mth.sin(f * 0.6662f + Mth.PI) * 0.4f) * f1;
        float f15 = Math.abs(Mth.sin(f * 0.6662f + Mth.HALF_PI) * 0.4f) * f1;
        float f16 = Math.abs(Mth.sin(f * 0.6662f + Mth.PI * 1.5f) * 0.4f) * f1;
        float[] swingY = {f9, f10, f11, f12};
        float[] swingZ = {f13, f14, f15, f16};
        for (int pair = 0; pair < 4; ++pair) {
            this.legs[pair * 2].yRot += swingY[pair];
            this.legs[pair * 2 + 1].yRot -= swingY[pair];
            this.legs[pair * 2].zRot += swingZ[pair];
            this.legs[pair * 2 + 1].zRot -= swingZ[pair];
        }
    }
}
