package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelSwanChestplate, the Dol Amroth chestplate: a body and arms with two
 * swan wings down the back, each a chain of twelve feathers that curls and
 * stirs as the wearer idles and walks.
 *
 * <p>The original also drove the wings off the MOUNT's stride while riding
 * (1.5 and 2.0 times the mount's limb swing); there are no mounts yet, so the
 * wearer's own walk is used throughout.
 */
public class LOTRSwanChestplateModel implements LOTRBodyArmorModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** new LOTRModelSwanChestplate(1.0f). */
    private static final float INFLATE = 1.0f;

    private static final int FEATHERS = 12;

    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart[] wingsRight = new ModelPart[FEATHERS];
    private final ModelPart[] wingsLeft = new ModelPart[FEATHERS];

    public LOTRSwanChestplateModel(ModelPart root) {
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        ModelPart right = this.body;
        ModelPart left = this.body;
        for (int i = 0; i < FEATHERS; i++) {
            right = right.getChild("wing_right_" + i);
            left = left.getChild("wing_left_" + i);
            this.wingsRight[i] = right;
            this.wingsLeft[i] = left;
        }
    }

    public static LayerDefinition createLayer() {
        CubeDeformation d = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, d),
                PartPose.ZERO);

        PartDefinition right = body;
        PartDefinition left = body;
        for (int i = 0; i < FEATHERS; i++) {
            right = right.addOrReplaceChild("wing_right_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, 16).addBox(-2.0f, 0.0f, 0.0f, 2, 1, 1)
                            .texOffs(6, 16).addBox(-2.0f, 1.0f, 0.5f, 2, 10, 0),
                    i == 0 ? PartPose.offset(-2.0f, 1.0f, 1.0f) : PartPose.offset(-2.0f, 0.0f, 0.0f));
            left = left.addOrReplaceChild("wing_left_" + i,
                    CubeListBuilder.create().mirror()
                            .texOffs(0, 16).addBox(0.0f, 0.0f, 0.0f, 2, 1, 1)
                            .texOffs(6, 16).addBox(0.0f, 1.0f, 0.5f, 2, 10, 0),
                    i == 0 ? PartPose.offset(2.0f, 1.0f, 1.0f) : PartPose.offset(2.0f, 0.0f, 0.0f));
        }

        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(24, 0).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, d),
                PartPose.ZERO);
        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().mirror().texOffs(24, 0).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, d),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /**
     * setRotationAngles' wing half: f2 is age in ticks, f the limb swing and f1
     * its amount.
     */
    public void setupWings(float ageInTicks, float walkPos, float walkSpeed) {
        float wingAngleBase = 0.17453292519943295f;
        wingAngleBase += Mth.sin(ageInTicks * 0.02f) * 0.01f;
        wingAngleBase += Mth.sin(walkPos * 0.2f) * 0.03f * walkSpeed;
        float wingYaw = 0.8726646259971648f;
        wingYaw += Mth.sin(ageInTicks * 0.03f) * 0.05f;
        wingYaw += Mth.sin(walkPos * 0.25f) * 0.12f * walkSpeed;
        for (int i = 0; i < FEATHERS; i++) {
            float wingAngle = wingAngleBase / ((i + 1) / 3.4f);
            this.wingsRight[i].zRot = wingAngle;
            this.wingsLeft[i].zRot = -wingAngle;
        }
        this.wingsRight[0].yRot = Mth.sin(wingYaw);
        this.wingsRight[0].xRot = Mth.cos(wingYaw);
        this.wingsLeft[0].yRot = Mth.sin(-wingYaw);
        this.wingsLeft[0].xRot = Mth.cos(-wingYaw);
    }

    @Override
    public ModelPart body() {
        return this.body;
    }

    @Override
    public ModelPart rightArm() {
        return this.rightArm;
    }

    @Override
    public ModelPart leftArm() {
        return this.leftArm;
    }
}
