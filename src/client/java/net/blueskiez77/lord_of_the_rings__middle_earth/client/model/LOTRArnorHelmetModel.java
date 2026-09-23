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
 * LOTRModelArnorHelmet, transcribed box for box.
 *
 * <p>The shell and, on each side, a stepped wing of three thin posts -- eight,
 * seven and five pixels tall -- set just outside the inflated shell. The left
 * wing is the right one mirrored, as the original set {@code mirror = true}
 * before adding it.
 */
public class LOTRArnorHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelArnorHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    private final ModelPart head;

    public LOTRArnorHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f))
                        .texOffs(32, 0).addBox(-4.5f - f, -13.0f - f, -1.0f, 1, 8, 1)
                        .texOffs(36, 0).addBox(-4.5f - f, -12.0f - f, 0.0f, 1, 7, 1)
                        .texOffs(40, 0).addBox(-4.5f - f, -11.0f - f, 1.0f, 1, 5, 1)
                        .mirror()
                        .texOffs(32, 0).addBox(3.5f + f, -13.0f - f, -1.0f, 1, 8, 1)
                        .texOffs(36, 0).addBox(3.5f + f, -12.0f - f, 0.0f, 1, 7, 1)
                        .texOffs(40, 0).addBox(3.5f + f, -11.0f - f, 1.0f, 1, 5, 1),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
