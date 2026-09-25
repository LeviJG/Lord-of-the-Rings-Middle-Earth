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
 * LOTRModelBlackUrukHelmet: an inflated head with a flat crest raised twenty
 * degrees back, painted at (32, 0) on the Black Uruk set's own sheet.
 */
public class LOTRBlackUrukHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** new LOTRModelBlackUrukHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX. */
    private static final float CREST_ANGLE = -0.3490658503988659f;

    private final ModelPart head;

    public LOTRBlackUrukHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(INFLATE)),
                PartPose.ZERO);
        head.addOrReplaceChild("crest",
                CubeListBuilder.create().texOffs(32, 0).addBox(-8.0f, -16.0f, -3.0f, 16, 10, 0),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, CREST_ANGLE, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
