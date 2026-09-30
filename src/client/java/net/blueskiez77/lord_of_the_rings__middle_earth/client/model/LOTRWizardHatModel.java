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
 * LOTRModelWizardHat(1.0): nothing but a pointed hat on the head -- a brim
 * 14 wide, and three tiers above it, the upper two bending back further the
 * faster its wearer walks.
 */
public class LOTRWizardHatModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    private final ModelPart hat2;
    private final ModelPart hat3;

    public LOTRWizardHatModel(ModelPart root) {
        super(root);
        ModelPart hat1 = this.head.getChild("hat_brim").getChild("hat1");
        this.hat2 = hat1.getChild("hat2");
        this.hat3 = this.hat2.getChild("hat3");
    }

    public static LayerDefinition createLayer() {
        float f = 1.0f;
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        for (String part : new String[]{"body", "right_arm", "left_arm", "right_leg", "left_leg"}) {
            root.addOrReplaceChild(part, CubeListBuilder.create(), PartPose.ZERO);
        }
        PartDefinition brim = head.addOrReplaceChild("hat_brim", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-7.0f, -8.0f - f, -7.0f, 14.0f, 1.0f, 14.0f), PartPose.ZERO);
        PartDefinition hat1 = brim.addOrReplaceChild("hat1", CubeListBuilder.create().texOffs(32, 3)
                .addBox(-4.0f, -5.0f, -4.0f, 8.0f, 5.0f, 8.0f), PartPose.offset(0.0f, -8.0f - f, 0.0f));
        PartDefinition hat2 = hat1.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(11, 7)
                .addBox(-2.5f, -4.0f, -2.5f, 5.0f, 4.0f, 5.0f), PartPose.offset(0.0f, -4.0f, 0.0f));
        hat2.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(0, 22)
                .addBox(-1.5f, -3.0f, -1.0f, 3.0f, 3.0f, 3.0f), PartPose.offset(0.0f, -3.5f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        float bend = -(10.0f + state.walkAnimationSpeed * 10.0f) * Mth.DEG_TO_RAD;
        this.hat2.xRot = bend;
        this.hat3.xRot = bend;
    }
}
