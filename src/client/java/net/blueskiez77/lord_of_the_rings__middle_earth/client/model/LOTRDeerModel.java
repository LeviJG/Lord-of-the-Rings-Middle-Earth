package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRDeerRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelDeer, transcribed box for box. The antlers show on adult males only.
 *
 * <p>The original drew a fawn by scaling the whole model by half about the
 * ground (glScalef(0.5) then glTranslatef(0, 24 * f5, 0)); that is
 * {@link MeshTransformer#scaling}, baked into {@link #createBabyLayer}.
 */
public class LOTRDeerModel extends EntityModel<LOTRDeerRenderState> {

    private final ModelPart head;
    private final ModelPart antlers;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg1Foot;
    private final ModelPart leg2Foot;
    private final ModelPart leg3Foot;
    private final ModelPart leg4Foot;
    private final ModelPart tail;

    public LOTRDeerModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.antlers = this.head.getChild("antlers");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.leg1Foot = this.leg1.getChild("foot");
        this.leg2Foot = this.leg2.getChild("foot");
        this.leg3Foot = this.leg3.getChild("foot");
        this.leg4Foot = this.leg4.getChild("foot");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(), 64, 64);
    }

    public static LayerDefinition createBabyLayer() {
        return LayerDefinition.create(createMesh(), 64, 64).apply(MeshTransformer.scaling(0.5f));
    }

    private static MeshDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.5f, -4.0f, -7.0f, 7, 7, 15), PartPose.offset(0.0f, 14.0f, 0.0f));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(12, 46)
                .addBox(-1.0f, -2.0f, -2.0f, 3, 6, 4), PartPose.offset(-4.0f, 14.0f, 5.0f));
        leg1.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(12, 56)
                .addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2), PartPose.offset(0.5f, 4.0f, 0.0f));
        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(12, 46).mirror()
                .addBox(-2.0f, -2.0f, -2.0f, 3, 6, 4), PartPose.offset(4.0f, 14.0f, 5.0f));
        leg2.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(12, 56).mirror()
                .addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2), PartPose.offset(-0.5f, 4.0f, 0.0f));
        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 47)
                .addBox(-1.5f, -2.0f, -1.5f, 3, 6, 3), PartPose.offset(-3.0f, 14.0f, -4.0f));
        leg3.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(0, 56)
                .addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2), PartPose.offset(0.0f, 4.0f, 0.0f));
        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 47).mirror()
                .addBox(-1.5f, -2.0f, -1.5f, 3, 6, 3), PartPose.offset(3.0f, 14.0f, -4.0f));
        leg4.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(0, 56).mirror()
                .addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2), PartPose.offset(0.0f, 4.0f, 0.0f));

        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 58)
                .addBox(-1.5f, -8.0f, 3.0f, 3, 2, 4), PartPose.offset(0.0f, 14.0f, 0.0f));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 22).addBox(-2.5f, -8.0f, -6.0f, 5, 4, 7)
                .texOffs(24, 22).addBox(-2.0f, -4.0f, -4.0f, 4, 7, 4),
                PartPose.offset(0.0f, 11.0f, -5.0f));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(0, 22)
                .addBox(-1.0f, -2.0f, -0.5f, 2, 3, 1),
                PartPose.offsetAndRotation(-2.0f, -8.0f, 0.0f, 0.0f, 0.5235988f, -0.87266463f));
        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(0, 22).mirror()
                .addBox(-1.0f, -2.0f, -0.5f, 2, 3, 1),
                PartPose.offsetAndRotation(2.0f, -8.0f, 0.0f, 0.0f, -0.5235988f, 0.87266463f));

        PartDefinition antlers = head.addOrReplaceChild("antlers", CubeListBuilder.create(), PartPose.ZERO);
        antlers.addOrReplaceChild("right_1", CubeListBuilder.create().texOffs(0, 33)
                .addBox(-0.5f, -8.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(-2.0f, -7.0f, 1.0f, -0.6981317f, 0.0f, -0.61086524f));
        antlers.addOrReplaceChild("right_2", CubeListBuilder.create().texOffs(4, 33)
                .addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1),
                PartPose.offsetAndRotation(-2.0f, -6.0f, 0.0f, -1.0471976f, -0.87266463f, -0.34906584f));
        antlers.addOrReplaceChild("left_1", CubeListBuilder.create().texOffs(0, 33).mirror()
                .addBox(-0.5f, -8.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(2.0f, -7.0f, 1.0f, -0.6981317f, 0.0f, 0.61086524f));
        antlers.addOrReplaceChild("left_2", CubeListBuilder.create().texOffs(4, 33).mirror()
                .addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1),
                PartPose.offsetAndRotation(2.0f, -6.0f, 0.0f, -1.0471976f, 0.87266463f, 0.34906584f));
        return mesh;
    }

    @Override
    public void setupAnim(LOTRDeerRenderState state) {
        super.setupAnim(state);
        this.antlers.visible = state.male && !state.isBaby;

        this.head.xRot = 0.5235988f + state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.8f) * f1 * 1.4f;
        this.leg2.xRot = Mth.cos(f * 0.8f + Mth.PI) * f1 * 1.4f;
        this.leg3.xRot = Mth.cos(f * 0.8f + Mth.PI) * f1 * 1.4f;
        this.leg4.xRot = Mth.cos(f * 0.8f) * f1 * 1.4f;
        this.leg1Foot.xRot = this.leg1.xRot * -0.6f;
        this.leg2Foot.xRot = this.leg2.xRot * -0.6f;
        this.leg3Foot.xRot = this.leg3.xRot * -0.6f;
        this.leg4Foot.xRot = this.leg4.xRot * -0.6f;
        this.tail.xRot = -0.87266463f;
    }
}
