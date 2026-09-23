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
 * LOTRModelUmbarHelmet, transcribed box for box.
 *
 * <p>The shell, an outer HAT half a pixel wider (bipedHeadwear, drawn inside the
 * head's transform as the Dorwinion elven helmet's is), two short posts on the
 * crown and a tall flat fin running front to back between them.
 */
public class LOTRUmbaricHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelUmbarHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    private final ModelPart head;
    private final ModelPart hat;

    public LOTRUmbaricHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f))
                        .texOffs(0, 0).addBox(-0.5f, -11.0f - f, -3.0f, 1, 3, 1)
                        .texOffs(0, 0).addBox(-0.5f, -10.0f - f, 2.0f, 1, 2, 1)
                        .texOffs(0, 16).addBox(0.0f, -13.0f - f, -6.0f, 0, 4, 12),
                PartPose.ZERO);

        root.addOrReplaceChild("hat",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8,
                                new CubeDeformation(f + 0.5f)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
        this.hat.render(poseStack, consumer, light, overlay);
    }
}
