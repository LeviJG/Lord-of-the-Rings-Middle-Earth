package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelGulfChestplate, transcribed box for box.
 *
 * <p>The body and both arms of the standard chestplate, plus a gorget across
 * the chest with three small HORNS tilted out of it, and a pauldron on each
 * shoulder with two more horns apiece. Each piece is posed at zero here: the
 * wearer's own body and arm transforms are applied first by
 * LOTRArmorRenderers, exactly as ModelBiped rendered the parts at their
 * rotation points.
 */
public class LOTRGulfenChestplateModel implements LOTRBodyArmorModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelGulfChestplate(1.0f). */
    private static final float INFLATE = 1.0f;

    private static final float TILT = 0.4363323129985824f;
    private static final float SHOULDER_HORN = 0.17453292519943295f;

    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public LOTRGulfenChestplateModel(ModelPart root) {
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        CubeDeformation d = new CubeDeformation(f);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, d)
                        .texOffs(16, 0).addBox(-4.0f, 0.0f, -3.0f - f, 8, 3, 1),
                PartPose.ZERO);
        CubeListBuilder chestHorn = CubeListBuilder.create()
                .texOffs(0, 0).addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        body.addOrReplaceChild("chest_horn_1", chestHorn,
                PartPose.offsetAndRotation(-2.5f - f, 2.5f, -3.0f - f, -TILT, 0.0f, TILT));
        body.addOrReplaceChild("chest_horn_2", chestHorn,
                PartPose.offsetAndRotation(0.0f, 3.0f, -3.0f - f, -TILT, 0.0f, 0.0f));
        body.addOrReplaceChild("chest_horn_3", chestHorn,
                PartPose.offsetAndRotation(2.5f + f, 2.5f, -3.0f - f, -TILT, 0.0f, -TILT));

        PartDefinition rightArm = root.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, d)
                        .texOffs(40, 0).addBox(-4.0f, -2.0f - f, -2.5f, 5, 1, 5),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("right_horn_1",
                CubeListBuilder.create().texOffs(4, 0).addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-2.5f, -2.0f - f, 0.0f, 0.0f, 0.0f, -SHOULDER_HORN));
        rightArm.addOrReplaceChild("right_horn_2",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5f, -3.0f, -0.5f, 1, 3, 1),
                PartPose.offsetAndRotation(-0.5f, -2.0f - f, 0.0f, 0.0f, 0.0f, -SHOULDER_HORN));

        PartDefinition leftArm = root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().mirror()
                        .texOffs(40, 16).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, d)
                        .texOffs(40, 0).addBox(-1.0f, -2.0f - f, -2.5f, 5, 1, 5),
                PartPose.ZERO);
        leftArm.addOrReplaceChild("left_horn_1",
                CubeListBuilder.create().texOffs(4, 0).mirror().addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(2.5f, -2.0f - f, 0.0f, 0.0f, 0.0f, SHOULDER_HORN));
        leftArm.addOrReplaceChild("left_horn_2",
                CubeListBuilder.create().texOffs(8, 0).mirror().addBox(-0.5f, -3.0f, -0.5f, 1, 3, 1),
                PartPose.offsetAndRotation(0.5f, -2.0f - f, 0.0f, 0.0f, 0.0f, SHOULDER_HORN));

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
