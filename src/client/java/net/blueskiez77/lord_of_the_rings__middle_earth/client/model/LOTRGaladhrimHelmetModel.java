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
 * LOTRModelGaladhrimHelmet, transcribed box for box.
 *
 * <p>The shell plus a swept horn: three boxes that step up and back from the
 * crown, the whole assembly tilted 45 degrees about X. It is a CHILD of the head
 * in the original, with its own rotation, which is why it is a separate part
 * here rather than more cubes on the head itself.
 *
 * <p>Same story as the Gondorian crest -- the horn is geometry, and the pixels
 * for it sit at (32,0) in galadhrim_helmet.png, where no standard armour model
 * looks. See LOTRArmorRenderers.
 */
public class LOTRGaladhrimHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed for this helmet. */
    private static final float INFLATE = 1.0f;

    private final ModelPart head;

    public LOTRGaladhrimHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f),
                PartPose.ZERO);

        // The horn. No inflation of its own -- the original passes 0.0f and
        // works f into the coordinates, and the 45 degrees is its rotateAngleX.
        head.addOrReplaceChild("horn",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-0.5f, -9.0f - INFLATE, 2.0f - INFLATE, 1, 3, 3)
                        .texOffs(32, 6).addBox(-0.5f, -10.0f - INFLATE, 3.5f - INFLATE, 1, 1, 3)
                        .texOffs(32, 10).addBox(-0.5f, -11.0f - INFLATE, 5.5f - INFLATE, 1, 1, 4),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f,
                        (float) (Math.PI / 4.0), 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
