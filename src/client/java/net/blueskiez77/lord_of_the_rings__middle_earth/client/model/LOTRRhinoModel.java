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
 * LOTRModelRhino, box for box. A calf has no horns and no neck: its head is
 * drawn full size, moved 8 down and 12 back, and the rest at half size (the
 * {@code "baby"} group).
 */
public class LOTRRhinoModel extends EntityModel<LOTRMountRenderState> {

    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart horns;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LOTRRhinoModel(ModelPart root) {
        super(root);
        ModelPart rest = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.horns = this.head.getChild("horns");
        this.neck = root.getChild("neck");
        this.tail = rest.getChild("tail");
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
                .texOffs(0, 0).addBox(-5.0f, -2.0f, -22.0f, 10, 10, 16, f)
                .addBox(-4.0f, -4.0f, -10.0f, 1, 2, 2, f)
                .mirror().addBox(3.0f, -4.0f, -10.0f, 1, 2, 2, f),
                baby ? PartPose.offset(0.0f, 11.0f, 0.0f) : PartPose.offset(0.0f, 3.0f, -12.0f));
        PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create(), PartPose.ZERO);
        horns.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(36, 0)
                .addBox(-1.0f, -14.0f, -20.0f, 2, 8, 2, f), PartPose.rotation(0.2617994f, 0.0f, 0.0f));
        horns.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(44, 0)
                .addBox(-1.0f, -3.0f, -17.0f, 2, 4, 2, f), PartPose.rotation(-0.17453292f, 0.0f, 0.0f));
        CubeListBuilder neckCubes = CubeListBuilder.create();
        if (!baby) {
            neckCubes.texOffs(52, 0).addBox(-7.0f, -4.0f, -7.0f, 14, 13, 8, f);
        }
        root.addOrReplaceChild("neck", neckCubes, PartPose.offset(0.0f, 3.0f, -12.0f));
        PartDefinition rest = baby
                ? root.addOrReplaceChild("baby", CubeListBuilder.create(), PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f))
                : root;
        rest.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26)
                .addBox(-8.0f, -7.0f, -13.0f, 16, 16, 34, f), PartPose.offset(0.0f, 5.0f, 0.0f));
        rest.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(100, 63)
                .addBox(-1.5f, -1.0f, -1.0f, 3, 8, 2, f), PartPose.offset(0.0f, 7.0f, 21.0f));
        rest.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(30, 76).addBox(-8.0f, -3.0f, -5.0f, 8, 12, 10, f)
                .texOffs(0, 95).addBox(-7.0f, 9.0f, -3.0f, 6, 12, 6, f), PartPose.offset(-8.0f, 3.0f, 14.0f));
        rest.addOrReplaceChild("leg2", CubeListBuilder.create().mirror()
                .texOffs(30, 76).addBox(0.0f, -3.0f, -5.0f, 8, 12, 10, f)
                .texOffs(0, 95).addBox(1.0f, 9.0f, -3.0f, 6, 12, 6, f), PartPose.offset(8.0f, 3.0f, 14.0f));
        rest.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(0, 76).addBox(-7.0f, -3.0f, -4.0f, 7, 11, 8, f)
                .texOffs(0, 95).addBox(-6.5f, 8.0f, -3.0f, 6, 12, 6, f), PartPose.offset(-8.0f, 4.0f, -6.0f));
        rest.addOrReplaceChild("leg4", CubeListBuilder.create().mirror()
                .texOffs(0, 76).addBox(0.0f, -3.0f, -4.0f, 7, 11, 8, f)
                .texOffs(0, 95).addBox(0.5f, 8.0f, -3.0f, 6, 12, 6, f), PartPose.offset(8.0f, 4.0f, -6.0f));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        this.horns.visible = !state.isBaby;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.head.xRot = 0.20943952f + Mth.cos(f * 0.2f) * 0.3f * f1 + state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.neck.xRot = this.head.xRot;
        this.neck.yRot = this.head.yRot;
        this.neck.zRot = this.head.zRot;
        this.tail.xRot = 0.6981317f + Mth.cos(f * 0.3f) * 0.5f * f1;
        this.leg1.xRot = Mth.cos(f * 0.4f) * 1.0f * f1;
        this.leg2.xRot = Mth.cos(f * 0.4f + Mth.PI) * 1.0f * f1;
        this.leg3.xRot = Mth.cos(f * 0.4f + Mth.PI) * 1.0f * f1;
        this.leg4.xRot = Mth.cos(f * 0.4f) * 1.0f * f1;
    }
}
