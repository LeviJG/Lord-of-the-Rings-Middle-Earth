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
 * LOTRModelDorwinionElfHelmet, transcribed box for box.
 *
 * <p>Three pieces: the shell, with a flat plume hanging down the back of it;
 * an outer HAT half a pixel wider than the shell, which is the one helmet in
 * the mod that uses bipedHeadwear; and a CREST fin standing along the crown,
 * tipped fifteen degrees back. The hat follows the head as bipedHeadwear did,
 * which LOTRArmorRenderers gives it by drawing inside the head's transform.
 */
public class LOTRDorwinionElvenHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelDorwinionElfHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX: fifteen degrees back. */
    private static final float CREST_ANGLE = -0.2617993877991494f;

    private final ModelPart head;
    private final ModelPart hat;

    public LOTRDorwinionElvenHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f))
                        .texOffs(20, 16).addBox(0.0f, -10.0f, 4.0f, 0, 10, 4),
                PartPose.ZERO);

        root.addOrReplaceChild("hat",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8,
                                new CubeDeformation(f + 0.5f)),
                PartPose.ZERO);

        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0f, -11.0f, -6.0f, 2, 5, 8),
                PartPose.offsetAndRotation(0.0f, -f, 0.0f, CREST_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
        this.hat.render(poseStack, consumer, light, overlay);
    }
}
