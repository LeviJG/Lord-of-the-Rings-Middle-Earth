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

/** LOTRModelTermite, box for box: six legs with the spider's walk. Drawn at a quarter size by the renderer. */
public class LOTRTermiteModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart[] legs = new ModelPart[6];

    public LOTRTermiteModel(ModelPart root) {
        super(root);
        for (int i = 0; i < 6; ++i) {
            this.legs[i] = root.getChild("leg" + (i + 1));
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(10, 5)
                .addBox(0.0f, 0.0f, 0.0f, 6, 6, 21), PartPose.offset(-3.0f, 17.0f, -5.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(0.0f, 0.0f, 0.0f, 8, 8, 7), PartPose.offset(-4.0f, 14.0f, -10.0f));
        float[] legZ = {1.0f, 0.0f, -1.0f};
        for (int i = 0; i < 3; ++i) {
            root.addOrReplaceChild("leg" + (2 * i + 1), CubeListBuilder.create().texOffs(34, 0)
                    .addBox(-12.0f, -1.0f, -1.0f, 13, 2, 2), PartPose.offset(-2.0f, 19.0f, legZ[i]));
            root.addOrReplaceChild("leg" + (2 * i + 2), CubeListBuilder.create().texOffs(34, 0)
                    .addBox(-1.0f, -1.0f, -1.0f, 13, 2, 2), PartPose.offset(2.0f, 19.0f, legZ[i]));
        }
        CubeListBuilder feeler = CubeListBuilder.create().texOffs(50, 18).addBox(0.0f, 0.0f, -8.0f, 1, 1, 6);
        root.addOrReplaceChild("right_feeler", feeler, PartPose.offsetAndRotation(-3.0f, 15.0f, -8.0f, 0.0f, -0.1f, 0.0f));
        root.addOrReplaceChild("left_feeler", feeler, PartPose.offsetAndRotation(2.0f, 15.0f, -8.0f, 0.0f, -0.1f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f6 = -0.51460177f;
        float f8 = 0.3926991f;
        float[] zBase = {f6, -f6, f6 * 0.74f, -f6 * 0.74f, f6 * 0.74f, -f6 * 0.74f};
        float[] yBase = {f8 * 2.0f, -f8 * 2.0f, f8, -f8, -f8, f8};
        float[] phase = {0.0f, Mth.PI, Mth.HALF_PI};
        for (int i = 0; i < 6; ++i) {
            float sign = i % 2 == 0 ? 1.0f : -1.0f;
            float swingY = -(Mth.cos(f * 0.6662f * 2.0f + phase[i / 2]) * 0.4f) * f1;
            float swingZ = Math.abs(Mth.sin(f * 0.6662f + phase[i / 2]) * 0.4f) * f1;
            this.legs[i].yRot = yBase[i] + sign * swingY;
            this.legs[i].zRot = zBase[i] + sign * swingZ;
        }
    }
}
