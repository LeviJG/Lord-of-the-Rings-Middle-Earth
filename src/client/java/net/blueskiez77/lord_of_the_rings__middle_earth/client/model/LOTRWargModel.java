package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRWargRenderState;

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
 * LOTRModelWarg, box for box, on a 128x64 skin. Built inflated by 0.5 it is
 * the saddle's model, and by 0.05 the glowing eyes'. The warg rug lays one
 * flat.
 */
public class LOTRWargModel extends EntityModel<LOTRWargRenderState> {

    public final ModelPart body;
    public final ModelPart tail;
    public final ModelPart head;
    public final ModelPart leg1;
    public final ModelPart leg2;
    public final ModelPart leg3;
    public final ModelPart leg4;

    public LOTRWargModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.head = root.getChild("head");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer(float f) {
        CubeDeformation g = new CubeDeformation(f);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0f, -2.0f, -14.0f, 16, 14, 14, g)
                .texOffs(0, 28).addBox(-6.5f, 0.0f, 0.0f, 13, 11, 18, g),
                PartPose.offset(0.0f, 2.0f, 1.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(98, 55).addBox(-1.0f, -1.0f, 0.0f, 2, 1, 8, g),
                PartPose.offset(0.0f, 4.0f, 18.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(92, 0).addBox(-5.0f, -5.0f, -8.0f, 10, 10, 8, g)
                .texOffs(108, 18).addBox(-3.0f, -1.0f, -12.0f, 6, 5, 4, g)
                .texOffs(102, 18).addBox(-4.0f, -7.8f, -3.0f, 2, 3, 1, g)
                .texOffs(102, 18).addBox(2.0f, -7.8f, -3.0f, 2, 3, 1, g),
                PartPose.offset(0.0f, 8.0f, -13.0f));
        root.addOrReplaceChild("leg1", CubeListBuilder.create().mirror()
                .texOffs(62, 0).addBox(-6.0f, -1.0f, -2.5f, 6, 9, 8, g)
                .texOffs(66, 17).addBox(-5.5f, 8.0f, -1.0f, 5, 10, 5, g),
                PartPose.offset(-4.0f, 6.0f, 12.0f));
        root.addOrReplaceChild("leg2", CubeListBuilder.create()
                .texOffs(62, 0).addBox(0.0f, -1.0f, -2.5f, 6, 9, 8, g)
                .texOffs(66, 17).addBox(0.5f, 8.0f, -1.0f, 5, 10, 5, g),
                PartPose.offset(4.0f, 6.0f, 12.0f));
        root.addOrReplaceChild("leg3", CubeListBuilder.create().mirror()
                .texOffs(62, 0).addBox(-6.0f, -1.0f, -2.5f, 6, 9, 8, g)
                .texOffs(66, 17).addBox(-5.5f, 8.0f, -1.0f, 5, 11, 5, g),
                PartPose.offset(-6.0f, 5.0f, -8.0f));
        root.addOrReplaceChild("leg4", CubeListBuilder.create()
                .texOffs(62, 0).addBox(0.0f, -1.0f, -2.5f, 6, 9, 8, g)
                .texOffs(66, 17).addBox(0.5f, 8.0f, -1.0f, 5, 11, 5, g),
                PartPose.offset(6.0f, 5.0f, -8.0f));
        return LayerDefinition.create(mesh, 128, 64);
    }

    /** setRotationAngles. */
    @Override
    public void setupAnim(LOTRWargRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 0.9f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 0.9f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 0.9f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 0.9f * f1;
        this.tail.xRot = state.tailRotation;
    }

    /** renderGlowingEyes: the head alone. */
    public void showOnlyHead() {
        this.body.visible = false;
        this.tail.visible = false;
        this.leg1.visible = false;
        this.leg2.visible = false;
        this.leg3.visible = false;
        this.leg4.visible = false;
    }
}
