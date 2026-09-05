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
 * LOTRModelGondorHelmet, transcribed box for box.
 *
 * <p>This is the crest. It is GEOMETRY, not texture: the pixels for the fins
 * sit at (32,0) in gondor_helmet.png, a region no standard armour model has
 * ever sampled, and 1.7.10 drew them because LOTRArmorModels handed helmetGondor
 * a biped model that rebuilt bipedHead with six extra boxes on it. 26.2 draws
 * one fixed armour biped for every helmet in the game, so without this the crest
 * simply is not rendered.
 *
 * <p>The shell is the ordinary armour head box inflated by 1, which is the
 * {@code new LOTRModelGondorHelmet(1.0f)} LOTRArmorModels registered. The six
 * crest boxes take NO inflation of their own -- the original passes 0.0f and
 * folds f into their coordinates instead, which is why the literals below have
 * the 1 already worked in.
 *
 * <p>This is the first of twenty-two such helmets in the original. The registry
 * in LOTRArmorRenderers is meant to take the rest.
 */
public class LOTRGondorHelmetModel {

    /** LOTRModelBiped's sheet: the standard 64x32 armour layout. */
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelGondorHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    private final ModelPart head;

    public LOTRGondorHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        // The helmet shell itself.
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        // The ridge running front to back over the crown, and
                        // the thin comb standing on top of it.
                        .texOffs(0, 16).addBox(-1.5f, -9.0f, -3.5f, 3, 1, 7, f)
                        .texOffs(20, 16).addBox(-0.5f, -10.0f, -3.5f, 1, 1, 7, f)
                        // The front fin, with a stud above and below it.
                        .texOffs(24, 0).addBox(-1.5f, -10.5f - INFLATE, -4.5f - INFLATE, 3, 4, 1)
                        .texOffs(24, 5).addBox(-0.5f, -11.5f - INFLATE, -4.5f - INFLATE, 1, 1, 1)
                        .texOffs(28, 5).addBox(-0.5f, -6.5f - INFLATE, -4.5f - INFLATE, 1, 1, 1)
                        // And the back fin, likewise. These three are the boxes
                        // whose pixels live in the "unused" part of the sheet.
                        .texOffs(32, 0).addBox(-1.5f, -9.5f - INFLATE, 3.5f + INFLATE, 3, 3, 1)
                        .texOffs(32, 4).addBox(-0.5f, -10.5f - INFLATE, 3.5f + INFLATE, 1, 1, 1)
                        .texOffs(36, 4).addBox(-0.5f, -6.5f - INFLATE, 3.5f + INFLATE, 1, 1, 1),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
