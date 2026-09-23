package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelHarnedorChestplate, transcribed box for box.
 *
 * <p>The standard body and arms, with a flat pauldron on each shoulder and two
 * barbed plates hanging off its front and back edges at thirty degrees. Posed at
 * zero, like LOTRGulfenChestplateModel, for LOTRArmorRenderers to place.
 */
public class LOTRHarnennorChestplateModel implements LOTRBodyArmorModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelHarnedorChestplate(1.0f). */
    private static final float INFLATE = 1.0f;

    private static final float BARB_ANGLE = 0.5235987755982988f;

    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public LOTRHarnennorChestplateModel(ModelPart root) {
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        CubeDeformation d = new CubeDeformation(f);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, d),
                PartPose.ZERO);

        PartDefinition rightArm = root.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, d)
                        .texOffs(46, 0).addBox(-4.0f - f, -3.0f - f, -2.0f, 5, 1, 4),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("right_barbs_1",
                CubeListBuilder.create().texOffs(29, 0).addBox(-2.5f, 0.0f, -2.0f, 5, 0, 2),
                PartPose.offsetAndRotation(-1.5f, -2.5f - f, -2.0f, BARB_ANGLE, 0.0f, 0.0f));
        rightArm.addOrReplaceChild("right_barbs_2",
                CubeListBuilder.create().texOffs(29, 3).addBox(-2.5f, 0.0f, 0.0f, 5, 0, 2),
                PartPose.offsetAndRotation(-1.5f, -2.5f - f, 2.0f, -BARB_ANGLE, 0.0f, 0.0f));

        PartDefinition leftArm = root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().mirror()
                        .texOffs(40, 16).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, d)
                        .texOffs(46, 0).addBox(-1.0f + f, -3.0f - f, -2.0f, 5, 1, 4),
                PartPose.ZERO);
        leftArm.addOrReplaceChild("left_barbs_1",
                CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-2.5f, 0.0f, -2.0f, 5, 0, 2),
                PartPose.offsetAndRotation(1.5f, -2.5f - f, -2.0f, BARB_ANGLE, 0.0f, 0.0f));
        leftArm.addOrReplaceChild("left_barbs_2",
                CubeListBuilder.create().texOffs(29, 3).mirror().addBox(-2.5f, 0.0f, 0.0f, 5, 0, 2),
                PartPose.offsetAndRotation(1.5f, -2.5f - f, 2.0f, -BARB_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
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
