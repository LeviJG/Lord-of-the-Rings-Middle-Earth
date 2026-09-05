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
 * LOTRModelWingedHelmet, which in the original is LOTRModelGondorHelmet with six
 * more boxes on it -- three up each side, mirrored.
 *
 * <p>So this is {@link LOTRGondorHelmetModel}'s geometry repeated with the wings
 * added, rather than a subclass: 26.2 builds a model from a LayerDefinition
 * rather than by adding boxes to a superclass's part in a constructor, so there
 * is no inheritance to lean on. The crest boxes are therefore the same literals
 * as the plain helmet's and should be changed together.
 *
 * <p>The wings are the boxes at (32,8), (38,8) and (46,8): a stud at the temple,
 * a long feather sweeping up from it, and a tip above that. Each is one pixel
 * thick, sits half a pixel off the helmet's centre plane, and takes NO inflation
 * of its own -- f is folded into the x coordinate instead, which pushes them out
 * to clear the inflated shell.
 *
 * <p>1.7.10 set {@code bipedHead.mirror = true} before the second set, which
 * flips the UVs so the same three texture regions serve both sides. CubeListBuilder
 * has the same switch.
 */
public class LOTRGondorWingedHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelWingedHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    private final ModelPart head;

    public LOTRGondorWingedHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        // --- LOTRModelGondorHelmet, box for box. ---------------
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        .texOffs(0, 16).addBox(-1.5f, -9.0f, -3.5f, 3, 1, 7, f)
                        .texOffs(20, 16).addBox(-0.5f, -10.0f, -3.5f, 1, 1, 7, f)
                        .texOffs(24, 0).addBox(-1.5f, -10.5f - INFLATE, -4.5f - INFLATE, 3, 4, 1)
                        .texOffs(24, 5).addBox(-0.5f, -11.5f - INFLATE, -4.5f - INFLATE, 1, 1, 1)
                        .texOffs(28, 5).addBox(-0.5f, -6.5f - INFLATE, -4.5f - INFLATE, 1, 1, 1)
                        .texOffs(32, 0).addBox(-1.5f, -9.5f - INFLATE, 3.5f + INFLATE, 3, 3, 1)
                        .texOffs(32, 4).addBox(-0.5f, -10.5f - INFLATE, 3.5f + INFLATE, 1, 1, 1)
                        .texOffs(36, 4).addBox(-0.5f, -6.5f - INFLATE, 3.5f + INFLATE, 1, 1, 1)
                        // --- The left wing. -----------------------------------
                        .texOffs(32, 8).addBox(-6.0f - INFLATE, -4.0f, -0.5f, 2, 2, 1)
                        .texOffs(38, 8).addBox(-7.0f - INFLATE, -13.0f, -0.5f, 3, 9, 1)
                        .texOffs(46, 8).addBox(-5.5f - INFLATE, -17.0f, -0.5f, 2, 4, 1)
                        // --- And the right, off the same three regions. -------
                        .mirror()
                        .texOffs(32, 8).addBox(4.0f + INFLATE, -4.0f, -0.5f, 2, 2, 1)
                        .texOffs(38, 8).addBox(4.0f + INFLATE, -13.0f, -0.5f, 3, 9, 1)
                        .texOffs(46, 8).addBox(3.5f + INFLATE, -17.0f, -0.5f, 2, 4, 1)
                        .mirror(false),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
