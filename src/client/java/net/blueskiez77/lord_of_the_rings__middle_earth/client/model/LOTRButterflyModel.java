package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRButterflyRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelButterfly: a body and two flat wings. In flight it hangs tilted
 * and beats its wings; settled it lies flat on the block, wings half open,
 * beating them only now and then.
 */
public class LOTRButterflyModel extends EntityModel<LOTRButterflyRenderState> {

    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public LOTRButterflyModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.0f, -6.0f, -1.0f, 2, 12, 2), PartPose.ZERO);
        body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(10, 0)
                .addBox(-12.0f, -10.5f, 0.0f, 12, 21, 0), PartPose.ZERO);
        body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(10, 0).mirror()
                .addBox(0.0f, -10.5f, 0.0f, 12, 21, 0), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRButterflyRenderState state) {
        super.setupAnim(state);
        float f2 = state.wingTime;
        if (state.still) {
            this.body.y = 24.0f;
            this.body.xRot = Mth.HALF_PI;
            this.rightWing.yRot = state.flapping ? Mth.cos(f2 * 1.3f) * Mth.PI * 0.25f : 0.31415927f;
        } else {
            this.body.y = 8.0f;
            this.body.xRot = 0.7853982f + Mth.cos(f2 * 0.1f) * 0.15f;
            this.rightWing.yRot = Mth.cos(f2 * 1.3f) * Mth.PI * 0.25f;
        }
        this.leftWing.yRot = -this.rightWing.yRot;
    }
}
