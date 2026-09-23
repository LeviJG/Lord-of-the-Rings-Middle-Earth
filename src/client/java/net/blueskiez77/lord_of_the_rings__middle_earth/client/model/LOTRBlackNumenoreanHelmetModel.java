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
 * LOTRModelBlackNumenoreanHelmet, transcribed box for box.
 *
 * <p>The shell and two tall flat WINGS hinged at the top corners of the head,
 * each swung twenty-five degrees back; the right wing is the left mirrored.
 */
public class LOTRBlackNumenoreanHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelBlackNumenoreanHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** wingLeft.rotateAngleY; wingRight takes the negative. */
    private static final float WING_ANGLE = 0.4363323129985824f;

    private final ModelPart head;

    public LOTRBlackNumenoreanHelmetModel(ModelPart root) {
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

        head.addOrReplaceChild("wing_left",
                CubeListBuilder.create().texOffs(33, 0).addBox(-6.0f, -6.0f, 0.0f, 6, 16, 0),
                PartPose.offsetAndRotation(-4.0f - f, -8.0f - f, 0.0f, 0.0f, WING_ANGLE, 0.0f));
        head.addOrReplaceChild("wing_right",
                CubeListBuilder.create().texOffs(33, 0).mirror().addBox(0.0f, -6.0f, 0.0f, 6, 16, 0),
                PartPose.offsetAndRotation(4.0f + f, -8.0f - f, 0.0f, 0.0f, -WING_ANGLE, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
