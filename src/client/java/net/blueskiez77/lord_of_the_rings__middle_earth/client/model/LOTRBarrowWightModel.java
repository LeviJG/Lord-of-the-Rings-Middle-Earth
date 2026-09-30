package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelBarrowWight: a tall robed shape with no legs -- a hooded head
 * raised eight pixels, a body 32 long falling to the ground, arms with long
 * forearms set high and wide -- on a 64x64 sheet. It does not walk, and its
 * arms move together.
 */
public class LOTRBarrowWightModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    public LOTRBarrowWightModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation f = CubeDeformation.NONE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f.extend(1.0f)), PartPose.offset(0.0f, -8.0f, 0.0f));
        // bipedHeadwear.cubeList.clear(): an empty hat.
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-4.0f, 0.0f, -3.0f, 8, 32, 6, f), PartPose.offset(0.0f, -8.0f, 0.0f));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(28, 16).addBox(-3.0f, -2.0f, -2.5f, 5, 9, 5, f)
                .texOffs(28, 30).addBox(-2.0f, 7.0f, -1.5f, 3, 10, 3, f), PartPose.offset(-6.0f, -5.0f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
                .texOffs(28, 16).addBox(-2.0f, -2.0f, -2.5f, 5, 9, 5, f)
                .texOffs(28, 30).addBox(-1.0f, 7.0f, -1.5f, 3, 10, 3, f), PartPose.offset(6.0f, -5.0f, 0.0f));
        // bipedRightLeg and bipedLeftLeg cubeList.clear(): no legs.
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12.0f, 0.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** setRotationAngles: the left arm as the right, mirrored. (The walk is stilled by the renderer.) */
    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        this.leftArm.xRot = this.rightArm.xRot;
        this.leftArm.yRot = -this.rightArm.yRot;
        this.leftArm.zRot = -this.rightArm.zRot;
    }
}
