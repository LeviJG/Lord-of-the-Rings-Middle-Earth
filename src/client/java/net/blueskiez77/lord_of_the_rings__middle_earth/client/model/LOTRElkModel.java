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
 * LOTRModelElk, box for box: rears like a horse, and the antlers show only
 * above three-quarters size -- on an adult. The original grew a calf
 * smoothly (getHorseSize); 26.2 foals are simply half size, so a calf is the
 * model at half scale about the ground. The nose is its own part so the
 * renderer can paint it red at Christmas.
 */
public class LOTRElkModel extends EntityModel<LOTRMountRenderState> {

    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart head;
    private final ModelPart antlers;
    public final ModelPart nose;
    /** The copy that draws only the nose, in red, at Christmas. */
    private final boolean noseOnly;

    public LOTRElkModel(ModelPart root) {
        this(root, false);
    }

    public LOTRElkModel(ModelPart root, boolean noseOnly) {
        super(root);
        this.noseOnly = noseOnly;
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.head = root.getChild("head");
        this.antlers = this.head.getChild("antlers");
        this.nose = root.getChild("nose");
    }

    public static LayerDefinition createBabyLayer(float inflate) {
        return createBodyLayer(inflate).apply(net.minecraft.client.model.geom.builders.MeshTransformer.scaling(0.5f));
    }

    public static LayerDefinition createBodyLayer(float inflate) {
        CubeDeformation f = new CubeDeformation(inflate);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-6.0f, -4.0f, -21.0f, 12, 11, 26, f), PartPose.offset(0.0f, 4.0f, 9.0f));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 54)
                .addBox(-1.0f, -5.0f, 2.0f, 2, 2, 8, f), PartPose.rotation(-1.0471976f, 0.0f, 0.0f));
        root.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(42, 37).addBox(-5.5f, 0.0f, -3.0f, 7, 11, 8, f)
                .texOffs(26, 37).addBox(-4.0f, 11.0f, -1.0f, 4, 10, 4, f), PartPose.offset(-4.0f, 3.0f, 8.0f));
        root.addOrReplaceChild("leg2", CubeListBuilder.create().mirror()
                .texOffs(42, 37).addBox(-1.5f, 0.0f, -3.0f, 7, 11, 8, f)
                .texOffs(26, 37).addBox(0.0f, 11.0f, -1.0f, 4, 10, 4, f), PartPose.offset(4.0f, 3.0f, 8.0f));
        root.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(0, 37).addBox(-4.5f, 0.0f, -3.0f, 6, 10, 7, f)
                .texOffs(26, 37).addBox(-3.5f, 10.0f, -2.0f, 4, 10, 4, f), PartPose.offset(-4.0f, 4.0f, -6.0f));
        root.addOrReplaceChild("leg4", CubeListBuilder.create().mirror()
                .texOffs(0, 37).addBox(-1.5f, 0.0f, -3.0f, 6, 10, 7, f)
                .texOffs(26, 37).addBox(-0.5f, 10.0f, -2.0f, 4, 10, 4, f), PartPose.offset(4.0f, 4.0f, -6.0f));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(50, 0).addBox(-2.0f, -10.0f, -4.0f, 4, 12, 8, f)
                .texOffs(74, 0).addBox(-3.0f, -16.0f, -8.0f, 6, 6, 13, f)
                .texOffs(50, 20).addBox(-2.0f, -18.0f, 3.0f, 1, 2, 1, f)
                .mirror().addBox(1.0f, -18.0f, 3.0f, 1, 2, 1, f), PartPose.offset(0.0f, 4.0f, -10.0f));
        PartDefinition antlers = head.addOrReplaceChild("antlers", CubeListBuilder.create(), PartPose.ZERO);
        antlers.addOrReplaceChild("right_1", CubeListBuilder.create().texOffs(0, 0).addBox(10.0f, -19.0f, 2.5f, 1, 12, 1, f), PartPose.rotation(0, 0, -1.134464f));
        antlers.addOrReplaceChild("right_2", CubeListBuilder.create().texOffs(4, 0).addBox(-3.0f, -23.6f, 2.5f, 1, 8, 1, f), PartPose.rotation(0, 0, -0.2617994f));
        antlers.addOrReplaceChild("right_3", CubeListBuilder.create().texOffs(8, 0).addBox(-8.0f, -36.0f, 2.5f, 1, 16, 1, f), PartPose.rotation(0, 0, -0.2617994f));
        antlers.addOrReplaceChild("right_4", CubeListBuilder.create().texOffs(12, 0).addBox(7.5f, -35.0f, 2.5f, 1, 10, 1, f), PartPose.rotation(0, 0, -0.87266463f));
        antlers.addOrReplaceChild("left_1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-11.0f, -19.0f, 2.5f, 1, 12, 1, f), PartPose.rotation(0, 0, 1.134464f));
        antlers.addOrReplaceChild("left_2", CubeListBuilder.create().texOffs(4, 0).mirror().addBox(2.0f, -23.6f, 2.5f, 1, 8, 1, f), PartPose.rotation(0, 0, 0.2617994f));
        antlers.addOrReplaceChild("left_3", CubeListBuilder.create().texOffs(8, 0).mirror().addBox(7.0f, -36.0f, 2.5f, 1, 16, 1, f), PartPose.rotation(0, 0, 0.2617994f));
        antlers.addOrReplaceChild("left_4", CubeListBuilder.create().texOffs(12, 0).mirror().addBox(-8.5f, -35.0f, 2.5f, 1, 10, 1, f), PartPose.rotation(0, 0, 0.87266463f));
        root.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(56, 20)
                .addBox(-1.0f, -14.5f, -9.0f, 2, 2, 1, f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(LOTRMountRenderState state) {
        super.setupAnim(state);
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float rear = state.standAnimation;
        float antiRear = 1.0f - rear;
        this.antlers.visible = !state.isBaby;

        this.head.y = rear * -6.0f + antiRear * 4.0f;
        this.head.z = rear * -1.0f + antiRear * -10.0f;
        this.head.xRot = antiRear * (0.34906584f + state.xRot * Mth.DEG_TO_RAD);
        this.head.yRot = antiRear * (state.yRot * Mth.DEG_TO_RAD);
        if (f1 > 0.2f) {
            this.head.xRot += Mth.cos(f * 0.3f) * 0.1f * f1;
        }
        this.nose.x = this.head.x;
        this.nose.y = this.head.y;
        this.nose.z = this.head.z;
        this.nose.xRot = this.head.xRot;
        this.nose.yRot = this.head.yRot;
        this.nose.zRot = this.head.zRot;
        this.body.xRot = rear * -0.7853982f;
        float legRotation = Mth.cos(f * 0.4f + Mth.PI) * f1;
        float f17 = -1.0471976f;
        float f18 = 0.2617994f * rear;
        float f19 = Mth.cos(state.ageInTicks * 0.4f + Mth.PI);
        this.leg4.y = -2.0f * rear + 4.0f * antiRear;
        this.leg4.z = -2.0f * rear + -6.0f * antiRear;
        this.leg3.y = this.leg4.y;
        this.leg3.z = this.leg4.z;
        this.leg1.xRot = f18 + legRotation * antiRear;
        this.leg2.xRot = f18 + -legRotation * antiRear;
        this.leg3.xRot = (f17 - f19) * rear + -legRotation * 0.8f * antiRear;
        this.leg4.xRot = (f17 + f19) * rear + legRotation * 0.8f * antiRear;

        // At Christmas the original drew the nose with glColor3f(1, 0, 0).
        this.nose.visible = this.noseOnly || !state.christmas;
        if (this.noseOnly) {
            for (ModelPart part : new ModelPart[] {this.body, this.leg1, this.leg2, this.leg3, this.leg4, this.head}) {
                part.visible = false;
            }
        }
    }
}
