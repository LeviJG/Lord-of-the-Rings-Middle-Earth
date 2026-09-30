package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * LOTRModelMidge: one midge, a body and two buzzing wings. The renderer draws
 * it once per midge in the swarm; the copy the renderer draws as the swarm
 * itself is kept empty ({@code hidden}).
 */
public class LOTRMidgeModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final boolean hidden;

    public LOTRMidgeModel(ModelPart root, boolean hidden) {
        super(root);
        this.body = root.getChild("body");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
        this.hidden = hidden;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -1.5f, -0.5f, 1, 5, 1), PartPose.offset(0.0f, 8.0f, 0.0f));
        body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 6)
                .addBox(-5.0f, -2.5f, 0.0f, 5, 5, 1), PartPose.ZERO);
        body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 6).mirror()
                .addBox(0.0f, -2.5f, 0.0f, 5, 5, 1), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.body.visible = !this.hidden;
        float f2 = state.ageInTicks;
        this.body.xRot = 0.7853982f + Mth.cos(f2 * 0.1f) * 0.15f;
        this.rightWing.yRot = Mth.cos(f2 * 4.0f) * Mth.PI * 0.25f;
        this.leftWing.yRot = -this.rightWing.yRot;
    }
}
