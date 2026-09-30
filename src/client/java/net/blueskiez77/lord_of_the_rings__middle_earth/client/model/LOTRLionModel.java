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
 * LOTRModelLion, box for box: lions and lionesses alike, mane and all. The
 * lion rug lays one of these flat (LOTRRugRenderer).
 *
 * <p>The cub is drawn as the original drew it: a full-size head moved 8 down
 * and 4 back, no mane, and the rest at half size about the ground -- the
 * {@code "baby"} group of {@link #createBabyLayer}.
 */
public class LOTRLionModel extends EntityModel<LivingEntityRenderState> {

    public final ModelPart head;
    public final ModelPart mane;
    public final ModelPart body;
    public final ModelPart leg1;
    public final ModelPart leg2;
    public final ModelPart leg3;
    public final ModelPart leg4;
    public final ModelPart tail;

    public LOTRLionModel(ModelPart root) {
        super(root);
        ModelPart rest = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.mane = root.getChild("mane");
        this.body = rest.getChild("body");
        this.leg1 = rest.getChild("leg1");
        this.leg2 = rest.getChild("leg2");
        this.leg3 = rest.getChild("leg3");
        this.leg4 = rest.getChild("leg4");
        this.tail = rest.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        addHead(root, PartPose.offset(0.0f, 3.0f, -10.0f), true);
        addBody(root);
        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        addHead(root, PartPose.offset(0.0f, 3.0f + 8.0f, -10.0f + 4.0f), false);
        addBody(root.addOrReplaceChild("baby", CubeListBuilder.create(),
                PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f)));
        return LayerDefinition.create(mesh, 128, 64);
    }

    private static void addHead(PartDefinition root, PartPose pose, boolean mane) {
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-5.0f, -6.0f, -10.0f, 10, 10, 10)
                .texOffs(78, 0).addBox(-3.0f, -1.0f, -14.0f, 6, 5, 4)
                .texOffs(98, 0).addBox(-1.0f, -2.0f, -14.2f, 2, 2, 5)
                .texOffs(0, 0).addBox(-4.0f, -9.0f, -7.5f, 3, 3, 1)
                .mirror().addBox(1.0f, -9.0f, -7.5f, 3, 3, 1),
                pose);
        CubeListBuilder maneCubes = CubeListBuilder.create();
        if (mane) {
            maneCubes.texOffs(0, 0).addBox(-8.0f, -10.0f, -6.0f, 16, 16, 8);
        }
        root.addOrReplaceChild("mane", maneCubes, pose);
    }

    private static void addBody(PartDefinition parent) {
        parent.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24)
                .addBox(-7.0f, -6.5f, -11.0f, 14, 14, 24), PartPose.offset(0.0f, 6.0f, 1.0f));
        parent.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(52, 24).addBox(-6.0f, -2.0f, -3.5f, 6, 10, 8)
                .texOffs(106, 24).addBox(-5.5f, 8.0f, -2.5f, 5, 12, 5),
                PartPose.offset(-4.0f, 4.0f, 11.0f));
        parent.addOrReplaceChild("leg2", CubeListBuilder.create().mirror()
                .texOffs(52, 24).addBox(0.0f, -2.0f, -3.5f, 6, 10, 8)
                .texOffs(106, 24).addBox(0.5f, 8.0f, -2.5f, 5, 12, 5),
                PartPose.offset(4.0f, 4.0f, 11.0f));
        parent.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(80, 24).addBox(-6.0f, -2.0f, -3.5f, 6, 9, 7)
                .texOffs(106, 24).addBox(-5.5f, 7.0f, -2.5f, 5, 12, 5),
                PartPose.offset(-4.0f, 5.0f, -5.0f));
        parent.addOrReplaceChild("leg4", CubeListBuilder.create().mirror()
                .texOffs(80, 24).addBox(0.0f, -2.0f, -3.5f, 6, 9, 7)
                .texOffs(106, 24).addBox(0.5f, 7.0f, -2.5f, 5, 12, 5),
                PartPose.offset(4.0f, 5.0f, -5.0f));
        parent.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(100, 50).addBox(-1.0f, -1.0f, 0.0f, 2, 2, 12)
                .texOffs(86, 57).addBox(-1.5f, -1.5f, 12.0f, 3, 3, 4),
                PartPose.offset(0.0f, 4.0f, 13.0f));
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.mane.xRot = this.head.xRot;
        this.mane.yRot = this.head.yRot;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.0f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.0f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.0f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.0f * f1;
        this.tail.xRot = -1.0471976f + Mth.cos(f * 0.3f) * 0.5f * f1;
    }
}
