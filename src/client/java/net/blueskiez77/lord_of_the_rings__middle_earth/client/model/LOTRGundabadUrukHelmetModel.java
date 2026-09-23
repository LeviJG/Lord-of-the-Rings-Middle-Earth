package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelGundabadUrukHelmet, transcribed box for box.
 *
 * <p>The shell and two flat HORNS rising from either side of the crown, each
 * leaning six degrees outward. The left horn is the right one mirrored, texture
 * and all, as the original set {@code mirror = true} on it.
 */
public class LOTRGundabadUrukHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelGundabadUrukHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** hornRight.rotateAngleZ; the left horn takes the negative. */
    private static final float HORN_ANGLE = 0.10471975511965978f;

    private final ModelPart head;

    public LOTRGundabadUrukHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f)),
                PartPose.ZERO);

        head.addOrReplaceChild("horn_right",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-7.0f, -12.0f, 0.5f, 3, 8, 0),
                PartPose.offsetAndRotation(-f, -f, -f, 0.0f, 0.0f, HORN_ANGLE));

        head.addOrReplaceChild("horn_left",
                CubeListBuilder.create()
                        .texOffs(32, 0).mirror().addBox(4.0f, -12.0f, 0.5f, 3, 8, 0),
                PartPose.offsetAndRotation(f, -f, -f, 0.0f, 0.0f, -HORN_ANGLE));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
