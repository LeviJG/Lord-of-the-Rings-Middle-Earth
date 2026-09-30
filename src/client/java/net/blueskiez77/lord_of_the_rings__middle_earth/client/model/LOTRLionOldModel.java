package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * LOTRModelLionOld: the mod's first lion, kept for a lion named "ticket lion"
 * (LOTRRenderLion.isTicket), with its own 64x96 texture. Cubs as the modern
 * lion: full-size head and headwear moved 8 down and 4 back, no mane, the rest
 * at half size.
 */
public class LOTRLionOldModel extends EntityModel<LivingEntityRenderState> {

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LOTRLionOldModel(ModelPart root) {
        super(root);
        ModelPart rest = root.hasChild("baby") ? root.getChild("baby") : root;
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.mane = root.getChild("mane");
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
        PartPose headPose = baby ? PartPose.offset(0.0f, 12.0f, -5.0f) : PartPose.offset(0.0f, 4.0f, -9.0f);
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0f, -4.0f, -7.0f, 8, 8, 8)
                .texOffs(52, 34).addBox(-2.0f, 0.0f, -9.0f, 4, 4, 2), headPose);
        root.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4.0f, -4.0f, -7.0f, 8, 8, 8, new CubeDeformation(0.5f)), headPose);
        CubeListBuilder maneCubes = CubeListBuilder.create();
        if (!baby) {
            maneCubes.texOffs(0, 36).addBox(-7.0f, -7.0f, -5.0f, 14, 14, 9);
        }
        root.addOrReplaceChild("mane", maneCubes, headPose);
        PartDefinition rest = baby
                ? root.addOrReplaceChild("baby", CubeListBuilder.create(), PartPose.offset(0.0f, 12.0f, 0.0f).withScale(0.5f))
                : root;
        rest.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 68)
                .addBox(-6.0f, -10.0f, -7.0f, 12, 18, 10), PartPose.offset(0.0f, 5.0f, 2.0f));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 19).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        CubeListBuilder legMirror = CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        rest.addOrReplaceChild("leg1", leg, PartPose.offset(-4.0f, 12.0f, 7.0f));
        rest.addOrReplaceChild("leg2", legMirror, PartPose.offset(4.0f, 12.0f, 7.0f));
        rest.addOrReplaceChild("leg3", leg, PartPose.offset(-4.0f, 12.0f, -5.0f));
        rest.addOrReplaceChild("leg4", legMirror, PartPose.offset(4.0f, 12.0f, -5.0f));
        return LayerDefinition.create(mesh, 64, 96);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.headwear.xRot = this.head.xRot;
        this.headwear.yRot = this.head.yRot;
        this.mane.xRot = this.head.xRot;
        this.mane.yRot = this.head.yRot;
        this.body.xRot = Mth.HALF_PI;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg3.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg4.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
    }
}
