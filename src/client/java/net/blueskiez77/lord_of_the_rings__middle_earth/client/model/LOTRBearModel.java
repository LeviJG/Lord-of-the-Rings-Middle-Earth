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
 * LOTRModelBear, box for box. The bear rug lays one of these flat.
 *
 * <p>The cub: a full-size head moved 8 down and 4 back with the muzzle pulled
 * in 3, and the rest at half size about the ground (the {@code "baby"} group).
 */
public class LOTRBearModel extends EntityModel<LivingEntityRenderState> {

    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leg1;
    public final ModelPart leg2;
    public final ModelPart leg3;
    public final ModelPart leg4;

    public LOTRBearModel(ModelPart root) {
        super(root);
        ModelPart rest = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.body = rest.getChild("body");
        this.leg1 = rest.getChild("leg1");
        this.leg2 = rest.getChild("leg2");
        this.leg3 = rest.getChild("leg3");
        this.leg4 = rest.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        return create(false);
    }

    public static LayerDefinition createBabyLayer() {
        return create(true);
    }

    private static LayerDefinition create(boolean baby) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0f, -5.0f, -4.0f, 8, 9, 6)
                .texOffs(0, 0).addBox(-4.5f, -5.5f, -11.0f, 9, 10, 7),
                baby ? PartPose.offset(0.0f, 16.0f, -5.0f) : PartPose.offset(0.0f, 8.0f, -9.0f));
        // setRotationAngles: nose.rotationPointZ = isChild ? 3 : 0.
        head.addOrReplaceChild("nose", CubeListBuilder.create()
                .texOffs(0, 17).addBox(-2.5f, -2.0f, -17.0f, 5, 6, 6)
                .texOffs(0, 29).addBox(-1.5f, -2.5f, -17.5f, 3, 3, 7),
                PartPose.offset(0.0f, 0.0f, baby ? 3.0f : 0.0f));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(23, 17)
                .addBox(-4.0f, -8.0f, -6.0f, 3, 3, 1), PartPose.rotation(0.0f, 0.0f, -0.2617994f));
        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(23, 17).mirror()
                .addBox(1.0f, -8.0f, -6.0f, 3, 3, 1), PartPose.rotation(0.0f, 0.0f, 0.2617994f));
        PartDefinition rest = baby
                ? root.addOrReplaceChild("baby", CubeListBuilder.create(), PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f))
                : root;
        rest.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-6.0f, -8.0f, -9.0f, 12, 14, 28)
                .texOffs(92, 0).addBox(-2.5f, -6.0f, 19.0f, 5, 5, 2),
                PartPose.offset(0.0f, 10.0f, -2.0f));
        rest.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(56, 44).addBox(-6.0f, -2.0f, -3.5f, 6, 9, 9)
                .texOffs(86, 44).addBox(-5.5f, 7.0f, -1.5f, 5, 11, 5),
                PartPose.offset(-4.0f, 6.0f, 10.0f));
        rest.addOrReplaceChild("leg2", CubeListBuilder.create().mirror()
                .texOffs(56, 44).addBox(0.0f, -2.0f, -3.5f, 6, 9, 9)
                .texOffs(86, 44).addBox(0.5f, 7.0f, -1.5f, 5, 11, 5),
                PartPose.offset(4.0f, 6.0f, 10.0f));
        rest.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(0, 44).addBox(-6.0f, -2.0f, -3.0f, 6, 9, 8)
                .texOffs(28, 44).addBox(-5.5f, 7.0f, -1.5f, 5, 12, 5),
                PartPose.offset(-3.0f, 6.0f, -5.0f));
        rest.addOrReplaceChild("leg4", CubeListBuilder.create().mirror()
                .texOffs(0, 44).addBox(0.0f, -2.0f, -3.0f, 6, 9, 8)
                .texOffs(28, 44).addBox(0.5f, 7.0f, -1.5f, 5, 12, 5),
                PartPose.offset(3.0f, 6.0f, -5.0f));
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = 0.17453292f + state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.0f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.0f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.0f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.0f * f1;
    }
}
