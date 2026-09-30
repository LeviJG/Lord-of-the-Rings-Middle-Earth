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
 * LOTRModelGiraffe, box for box. Ridden by a player it lowers its neck flat
 * ahead, out of the rider's view (setRiddenHeadNeckRotation) -- the giraffe
 * rug lays it that way too. A calf is the whole model at half size.
 */
public class LOTRGiraffeModel extends EntityModel<LOTRMountRenderState> {

    public final ModelPart body;
    public final ModelPart neck;
    public final ModelPart head;
    public final ModelPart tail;
    public final ModelPart leg1;
    public final ModelPart leg2;
    public final ModelPart leg3;
    public final ModelPart leg4;

    public LOTRGiraffeModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer(float inflate) {
        return LayerDefinition.create(createMesh(inflate), 128, 64);
    }

    public static LayerDefinition createBabyLayer(float inflate) {
        return createBodyLayer(inflate).apply(MeshTransformer.scaling(0.5f));
    }

    private static MeshDefinition createMesh(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-6.0f, -8.0f, -13.0f, 12, 16, 26, f), PartPose.offset(0.0f, -11.0f, 0.0f));
        root.addOrReplaceChild("neck", CubeListBuilder.create()
                .texOffs(0, 44).addBox(-4.5f, -13.0f, -4.5f, 9, 11, 9, f)
                .texOffs(78, 0).addBox(-3.0f, -37.0f, -3.0f, 6, 40, 6, f), PartPose.offset(0.0f, -14.0f, -7.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(96, 48).addBox(-3.0f, -43.0f, -6.0f, 6, 6, 10, f)
                .texOffs(10, 0).addBox(-4.0f, -45.0f, 1.5f, 1, 3, 2, f)
                .texOffs(17, 0).addBox(3.0f, -45.0f, 1.5f, 1, 3, 2, f)
                .texOffs(0, 0).addBox(-2.5f, -47.0f, 0.0f, 1, 4, 1, f)
                .texOffs(5, 0).addBox(1.5f, -47.0f, 0.0f, 1, 4, 1, f)
                .texOffs(76, 56).addBox(-2.0f, -41.0f, -11.0f, 4, 3, 5, f), PartPose.offset(0.0f, -14.0f, -7.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(104, 0)
                .addBox(-0.5f, 0.0f, 0.0f, 1, 24, 1, f), PartPose.offset(0.0f, -12.0f, 13.0f));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(112, 0).addBox(-2.0f, 0.0f, -2.0f, 4, 27, 4, f);
        CubeListBuilder legMirror = CubeListBuilder.create().texOffs(112, 0).mirror().addBox(-2.0f, 0.0f, -2.0f, 4, 27, 4, f);
        root.addOrReplaceChild("leg1", leg, PartPose.offset(-3.9f, -3.0f, 8.0f));
        root.addOrReplaceChild("leg2", legMirror, PartPose.offset(3.9f, -3.0f, 8.0f));
        root.addOrReplaceChild("leg3", leg, PartPose.offset(-3.9f, -3.0f, -7.0f));
        root.addOrReplaceChild("leg4", legMirror, PartPose.offset(3.9f, -3.0f, -7.0f));
        return mesh;
    }

    /** setRiddenHeadNeckRotation. */
    public void setRiddenHeadNeckRotation() {
        this.head.y = 25.0f;
        this.head.z = -48.0f;
        this.neck.xRot = 1.5707964f;
        this.neck.yRot = 0.0f;
        this.head.xRot = 0.0f;
        this.head.yRot = 0.0f;
    }

    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        this.leg1.xRot = 0.5f * Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leg2.xRot = 0.5f * Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg3.xRot = 0.5f * Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        this.leg4.xRot = 0.5f * Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.tail.zRot = 0.2f * Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        if (state.riddenByPlayer) {
            setRiddenHeadNeckRotation();
        } else {
            this.neck.xRot = 0.17453294f + state.xRot * Mth.DEG_TO_RAD;
            this.head.xRot = this.neck.xRot;
            this.neck.yRot = state.yRot * Mth.DEG_TO_RAD;
            this.head.yRot = this.neck.yRot;
        }
    }
}
