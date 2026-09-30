package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRMarshWraithRenderState;

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
 * LOTRModelMarshWraith: a head and a hood, a long body falling away below
 * with no legs, arms hanging still, and a cape behind -- only the head turns.
 */
public class LOTRMarshWraithModel extends EntityModel<LOTRMarshWraithRenderState> {

    private final ModelPart head;
    private final ModelPart headwear;

    public LOTRMarshWraithModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8), PartPose.ZERO);
        root.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(0.5f)), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-4.0f, 0.0f, -2.0f, 8, 24, 4), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(46, 16)
                .addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4), PartPose.offset(-5.0f, 2.0f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(46, 16).mirror()
                .addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4), PartPose.offset(5.0f, 2.0f, 0.0f));
        root.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(24, 16)
                .addBox(-5.0f, 1.0f, 3.0f, 10, 16, 1), PartPose.rotation(0.1f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LOTRMarshWraithRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.headwear.yRot = this.head.yRot;
        this.headwear.xRot = this.head.xRot;
    }
}
