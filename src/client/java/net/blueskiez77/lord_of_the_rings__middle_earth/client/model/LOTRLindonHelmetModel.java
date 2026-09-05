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
 * LOTRModelHighElvenHelmet, transcribed box for box.
 *
 * <p>The shell, two small spikes -- one over the brow and one at the back of
 * the crown -- and the CREST: a long low blade running the whole length of the
 * helmet from front to back, tipped up sixteen degrees, with a raised nub at
 * its front end. The crest is a child of the head with that rotation of its
 * own, so it is a separate part here; the two spikes are cubes on the head.
 *
 * <p>The crest's pixels live at (32,0) and (32,12), which the standard armour
 * biped never samples, so without this the Lindon helmet rendered as a bare cap.
 *
 * <p>The brow spike takes NO texture offset of its own -- the original calls
 * addBox straight after setting the head's, so it continues from (0,0), where
 * the shell's own faces are. The second spike is explicitly at (0,4).
 */
public class LOTRLindonHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelHighElvenHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX: sixteen degrees, nose up. */
    private static final float CREST_ANGLE = -0.2792526803190927f;

    private final ModelPart head;

    public LOTRLindonHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        // The two spikes, neither of them inflated.
                        .texOffs(0, 0).addBox(-0.5f, -11.0f, -2.0f, 1, 3, 1)
                        .texOffs(0, 4).addBox(-0.5f, -10.0f, 2.0f, 1, 2, 1),
                PartPose.ZERO);

        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-1.0f, -11.0f, -8.0f, 2, 1, 11)
                        .texOffs(32, 12).addBox(-1.0f, -10.0f, -8.0f, 2, 1, 1),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, CREST_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
