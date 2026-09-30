package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMountRenderState;

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
 * LOTRModelBoar: 1.7.10's ModelPig (a ModelQuadruped of leg height 6, with a
 * snout) plus a longer snout, ears and tusks, the tusks only on adults. A
 * piglet is drawn as ModelQuadruped drew a child: the head full size, moved
 * 4 down and 4 back, the rest at half size (the {@code "baby"} group).
 */
public class LOTRBoarModel extends EntityModel<LOTRMountRenderState> {

    private final ModelPart head;
    private final ModelPart tusks;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LOTRBoarModel(ModelPart root) {
        super(root);
        ModelPart rest = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.tusks = this.head.getChild("tusks");
        this.body = rest.getChild("body");
        this.leg1 = rest.getChild("leg1");
        this.leg2 = rest.getChild("leg2");
        this.leg3 = rest.getChild("leg3");
        this.leg4 = rest.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer(float inflate) {
        return create(inflate, false);
    }

    public static LayerDefinition createBabyLayer(float inflate) {
        return create(inflate, true);
    }

    private static LayerDefinition create(float inflate, boolean baby) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, f)
                .texOffs(16, 16).addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1, f)
                .texOffs(24, 0).addBox(-3.0f, 0.0f, -10.0f, 6, 4, 2, f)
                .texOffs(40, 0).addBox(-5.0f, -5.0f, -6.0f, 1, 2, 2, f)
                .mirror().addBox(4.0f, -5.0f, -6.0f, 1, 2, 2, f),
                baby ? PartPose.offset(0.0f, 16.0f, -2.0f) : PartPose.offset(0.0f, 12.0f, -6.0f));
        head.addOrReplaceChild("tusks", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0f, 2.0f, -11.0f, 1, 1, 2, f)
                .texOffs(1, 1).addBox(-4.0f, 1.0f, -11.5f, 1, 1, 1, f)
                .mirror()
                .texOffs(0, 0).addBox(3.0f, 2.0f, -11.0f, 1, 1, 2, f)
                .texOffs(1, 1).addBox(3.0f, 1.0f, -11.5f, 1, 1, 1, f), PartPose.ZERO);
        PartDefinition rest = baby
                ? root.addOrReplaceChild("baby", CubeListBuilder.create(), PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f))
                : root;
        rest.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8)
                .addBox(-5.0f, -10.0f, -7.0f, 10, 16, 8, f), PartPose.offsetAndRotation(0.0f, 11.0f, 2.0f, Mth.HALF_PI, 0.0f, 0.0f));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, f);
        rest.addOrReplaceChild("leg1", leg, PartPose.offset(-3.0f, 18.0f, 7.0f));
        rest.addOrReplaceChild("leg2", leg, PartPose.offset(3.0f, 18.0f, 7.0f));
        rest.addOrReplaceChild("leg3", leg, PartPose.offset(-3.0f, 18.0f, -5.0f));
        rest.addOrReplaceChild("leg4", leg, PartPose.offset(3.0f, 18.0f, -5.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    /** ModelQuadruped.setRotationAngles. */
    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        this.tusks.visible = !state.isBaby;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.body.xRot = Mth.HALF_PI;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
    }
}
