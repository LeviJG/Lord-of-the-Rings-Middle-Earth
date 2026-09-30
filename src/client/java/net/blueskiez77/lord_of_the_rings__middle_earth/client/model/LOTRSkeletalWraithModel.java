package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelSkeleton: a zombie's pose on a skeleton's bones -- thin arms and
 * legs on the 64x32 skeleton sheet (only on the body model; the armour at
 * 1.0 and 0.5 keeps the biped's full limbs) -- the arms held out before it,
 * swinging down to strike.
 */
public class LOTRSkeletalWraithModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    public LOTRSkeletalWraithModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16)
                .addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2), PartPose.offset(-5.0f, 2.0f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror()
                .addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2), PartPose.offset(5.0f, 2.0f, 0.0f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2), PartPose.offset(-2.0f, 12.0f, 0.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror()
                .addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2), PartPose.offset(2.0f, 12.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    /** ModelZombie.setRotationAngles, after the biped's. */
    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        float onGround = state.attackTime;
        float f2 = state.ageInTicks;
        float f6 = Mth.sin(onGround * Mth.PI);
        float f7 = Mth.sin((1.0f - (1.0f - onGround) * (1.0f - onGround)) * Mth.PI);
        this.rightArm.zRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightArm.yRot = -(0.1f - f6 * 0.6f);
        this.leftArm.yRot = 0.1f - f6 * 0.6f;
        this.rightArm.xRot = -Mth.HALF_PI;
        this.leftArm.xRot = -Mth.HALF_PI;
        this.rightArm.xRot -= f6 * 1.2f - f7 * 0.4f;
        this.leftArm.xRot -= f6 * 1.2f - f7 * 0.4f;
        this.rightArm.zRot += Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot -= Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.rightArm.xRot += Mth.sin(f2 * 0.067f) * 0.05f;
        this.leftArm.xRot -= Mth.sin(f2 * 0.067f) * 0.05f;
    }
}
