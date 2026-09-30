package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMountRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelCamel, box for box (the tail never took the inflation). A calf is
 * the whole model at half size, without humps or chest.
 */
public class LOTRCamelModel extends EntityModel<LOTRMountRenderState> {

    private final ModelPart head;
    private final ModelPart humps;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart chest;

    public LOTRCamelModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.humps = root.getChild("humps");
        this.tail = root.getChild("tail");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.chest = root.getChild("chest");
    }

    public static LayerDefinition createBodyLayer(float inflate) {
        return LayerDefinition.create(createMesh(inflate), 64, 64);
    }

    public static LayerDefinition createBabyLayer(float inflate) {
        return LayerDefinition.create(createMesh(inflate), 64, 64).apply(MeshTransformer.scaling(0.5f));
    }

    private static MeshDefinition createMesh(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-4.5f, -5.0f, -10.0f, 9, 10, 22, f), PartPose.offset(0.0f, 10.0f, 0.0f));
        root.addOrReplaceChild("humps", CubeListBuilder.create().texOffs(34, 0)
                .addBox(-3.0f, -9.0f, -8.0f, 6, 4, 6, f)
                .addBox(-3.0f, -9.0f, 5.0f, 6, 4, 6, f), PartPose.offset(0.0f, 10.0f, 0.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(54, 52)
                .addBox(-1.0f, -1.0f, 0.0f, 2, 10, 2), PartPose.offset(0.0f, 7.0f, 12.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0f, -13.0f, -10.5f, 6, 5, 11, f)
                .addBox(-2.5f, -15.0f, -1.0f, 2, 2, 1, f)
                .mirror().addBox(0.5f, -15.0f, -1.0f, 2, 2, 1, f).mirror(false)
                .texOffs(0, 16).addBox(-2.5f, -9.0f, -5.0f, 5, 14, 5, f), PartPose.offset(0.0f, 6.0f, -10.0f));
        for (int i = 0; i < 4; ++i) {
            boolean right = i % 2 == 1;
            float x = right ? 4.5f : -4.5f;
            float z = i < 2 ? 8.0f : -5.0f;
            CubeListBuilder leg = CubeListBuilder.create().mirror(right)
                    .texOffs(0, 52).addBox(right ? 0.0f : -4.0f, -1.0f, -2.5f, 4, 7, 5, f)
                    .texOffs(18, 53).addBox(right ? 0.5f : -3.5f, 6.0f, -1.5f, 3, 8, 3, f)
                    .texOffs(30, 57).addBox(right ? 0.0f : -4.0f, 14.0f, -2.0f, 4, 3, 4, f);
            root.addOrReplaceChild("leg" + (i + 1), leg, PartPose.offset(x, 7.0f, z));
        }
        root.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(40, 22)
                .addBox(-7.5f, -4.5f, -2.5f, 3, 8, 8, f)
                .mirror().addBox(4.5f, -4.5f, -2.5f, 3, 8, 8, f), PartPose.offset(0.0f, 10.0f, 0.0f));
        return mesh;
    }

    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.humps.visible = !state.isBaby;
        this.chest.visible = !state.isBaby && state.chested;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD + Mth.cos(f * 0.3331f) * 0.1f * f1;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 0.8f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 0.8f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 0.8f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 0.8f * f1;
        this.tail.zRot = 0.1f * Mth.cos(f * 0.3331f + Mth.PI) * f1;
    }
}
