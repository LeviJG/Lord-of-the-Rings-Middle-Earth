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
 * LOTRModelGondolinHelmet, transcribed box for box.
 *
 * <p>The shell, three spines of falling height standing front to back along the
 * crown, and a flat fin between them.
 *
 * <p>The fin's texOffs is {@code (32, -7)} in the original -- a NEGATIVE v,
 * which looks like a mistake and is not. The box is zero wide, so the only
 * faces drawn are its two x-faces, and their UVs are worked out as
 * {@code v + depth}: -7 + 7 = 0. Both land at rows 0 to 6, in range and on the
 * art. The literal is kept as the original wrote it because the arithmetic that
 * rescues it is identical in 26.2.
 */
public class LOTRGondolinHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private static final float INFLATE = 1.0f;

    private final ModelPart head;

    public LOTRGondolinHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        // Three spines, tallest at the brow.
                        .texOffs(46, 0).addBox(-0.5f, -14.0f - INFLATE, -4.5f, 1, 6, 1)
                        .texOffs(50, 0).addBox(-0.5f, -12.0f - INFLATE, -0.5f, 1, 4, 1)
                        .texOffs(54, 0).addBox(-0.5f, -10.0f - INFLATE, 3.5f, 1, 2, 1)
                        // And the fin between them. See the note above on (32,-7).
                        .texOffs(32, -7).addBox(0.0f, -13.5f - INFLATE, -3.5f, 0, 6, 7),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
