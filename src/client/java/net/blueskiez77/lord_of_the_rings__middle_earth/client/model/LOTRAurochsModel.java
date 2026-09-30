package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAurochsRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelAurochs, box for box; the kine of Araw wears it too, scaled up by
 * its renderer. An enraged aurochs lowers its head by 15 degrees to charge.
 *
 * <p>The calf is drawn the way the original drew it: the head at full size,
 * moved 8 down and 6 back, with no horns, and the rest of the body at half
 * size about the ground. {@link #createBabyLayer} builds that as a full-size
 * head beside a half-scale {@code "baby"} group holding the rest.
 */
public class LOTRAurochsModel extends EntityModel<LOTRAurochsRenderState> {

    private final ModelPart head;
    private final ModelPart horns;
    private final ModelPart hornLeft1;
    private final ModelPart hornLeft2;
    private final ModelPart hornRight1;
    private final ModelPart hornRight2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LOTRAurochsModel(ModelPart root) {
        super(root);
        ModelPart body = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.horns = this.head.getChild("horns");
        this.hornLeft1 = this.horns.getChild("left_1");
        this.hornLeft2 = this.hornLeft1.getChild("left_2");
        this.hornRight1 = this.horns.getChild("right_1");
        this.hornRight2 = this.hornRight1.getChild("right_2");
        this.leg1 = body.getChild("leg1");
        this.leg2 = body.getChild("leg2");
        this.leg3 = body.getChild("leg3");
        this.leg4 = body.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        addHead(root, PartPose.offset(0.0f, -1.0f, -10.0f));
        addBody(root);
        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        addHead(root, PartPose.offset(0.0f, -1.0f + 8.0f, -10.0f + 6.0f));
        addBody(root.addOrReplaceChild("baby", CubeListBuilder.create(),
                PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f)));
        return LayerDefinition.create(mesh, 128, 64);
    }

    private static void addHead(PartDefinition root, PartPose pose) {
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(58, 0).addBox(-5.0f, -4.0f, -12.0f, 10, 10, 11)
                .texOffs(89, 0).addBox(-3.0f, 1.0f, -15.0f, 6, 4, 4)
                .texOffs(105, 0).addBox(-8.0f, -2.5f, -7.0f, 3, 2, 1)
                .mirror().addBox(5.0f, -2.5f, -7.0f, 3, 2, 1).mirror(false),
                pose);
        PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(98, 21)
                .addBox(-6.0f, -1.5f, -1.5f, 12, 3, 3), PartPose.offset(0.0f, -3.5f, -5.0f));
        PartDefinition left1 = horns.addOrReplaceChild("left_1", CubeListBuilder.create().texOffs(112, 27)
                .addBox(-5.0f, -1.0f, -1.0f, 6, 2, 2), PartPose.offset(-6.0f, 0.0f, 0.0f));
        left1.addOrReplaceChild("left_2", CubeListBuilder.create().texOffs(114, 31)
                .addBox(-5.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offset(-5.0f, 0.0f, 0.0f));
        PartDefinition right1 = horns.addOrReplaceChild("right_1", CubeListBuilder.create().texOffs(112, 27).mirror()
                .addBox(-1.0f, -1.0f, -1.0f, 6, 2, 2), PartPose.offset(6.0f, 0.0f, 0.0f));
        right1.addOrReplaceChild("right_2", CubeListBuilder.create().texOffs(114, 31).mirror()
                .addBox(-1.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offset(5.0f, 0.0f, 0.0f));
    }

    private static void addBody(PartDefinition parent) {
        parent.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0f, -6.0f, -11.0f, 16, 16, 26)
                .texOffs(28, 42).addBox(-8.0f, -8.0f, -8.0f, 16, 2, 10)
                .texOffs(84, 31).addBox(-3.0f, 10.0f, 4.0f, 6, 1, 6),
                PartPose.offset(0.0f, 2.0f, -1.0f));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 42).addBox(-2.5f, 0.0f, -2.5f, 5, 12, 5);
        CubeListBuilder legMirror = CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-2.5f, 0.0f, -2.5f, 5, 12, 5);
        parent.addOrReplaceChild("leg1", leg, PartPose.offset(-5.0f, 12.0f, 9.0f));
        parent.addOrReplaceChild("leg2", legMirror, PartPose.offset(5.0f, 12.0f, 9.0f));
        parent.addOrReplaceChild("leg3", leg, PartPose.offset(-5.0f, 12.0f, -7.0f));
        parent.addOrReplaceChild("leg4", legMirror, PartPose.offset(5.0f, 12.0f, -7.0f));
        parent.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 42)
                .addBox(-1.0f, -1.0f, 0.0f, 2, 12, 1), PartPose.offset(0.0f, 1.0f, 14.0f));
    }

    @Override
    public void setupAnim(LOTRAurochsRenderState state) {
        super.setupAnim(state);
        this.horns.visible = !state.isBaby;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.zRot = 0.0f;
        if (state.enraged) {
            this.head.xRot += 0.2617994f;
        }
        this.head.xRot += Mth.cos(f * 0.2f) * f1 * 0.4f;
        this.hornLeft1.zRot = 0.43633232f;
        this.hornLeft2.zRot = 0.2617994f;
        this.hornRight1.zRot = -this.hornLeft1.zRot;
        this.hornRight2.zRot = -this.hornLeft2.zRot;
        this.hornLeft1.yRot = -0.43633232f;
        this.hornRight1.yRot = -this.hornLeft1.yRot;
        this.hornLeft1.xRot = 0.61086524f;
        this.hornRight1.xRot = 0.61086524f;
        this.leg1.xRot = Mth.cos(f * 0.4f) * f1 * 0.8f;
        this.leg2.xRot = Mth.cos(f * 0.4f + Mth.PI) * f1 * 0.8f;
        this.leg3.xRot = Mth.cos(f * 0.4f + Mth.PI) * f1 * 0.8f;
        this.leg4.xRot = Mth.cos(f * 0.4f) * f1 * 0.8f;
    }
}
