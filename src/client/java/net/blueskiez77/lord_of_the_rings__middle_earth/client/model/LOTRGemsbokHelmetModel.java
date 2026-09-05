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
 * LOTRModelGemsbokHelmet, transcribed box for box.
 *
 * <p>The shell, and the pair of HORNS that make it a gemsbok's: two long thin
 * bars sweeping back and up from the temples, thirteen pixels of them, tilted
 * twenty degrees. They are children of the head with their own rotation, and
 * their pixels sit at (32,0) where the standard armour biped never looks --
 * which is why without them the helmet was a bare cap.
 *
 * <p>The two share one texture region: the left horn is drawn mirrored, so the
 * same 1x1x13 strip serves both. Its x is the right horn's reflected, which the
 * original writes out rather than deriving.
 */
public class LOTRGemsbokHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelGemsbokHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** hornRight.rotateAngleX == hornLeft.rotateAngleX: twenty degrees back. */
    private static final float HORN_ANGLE = 0.3490658503988659f;

    private final ModelPart head;

    public LOTRGemsbokHelmetModel(ModelPart root) {
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

        // No inflation on either horn: the original passes none and works the
        // clearance into the tenth of a pixel on the x instead.
        head.addOrReplaceChild("horn_right",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-4.9f, -7.0f, 7.5f, 1, 1, 13),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, HORN_ANGLE, 0.0f, 0.0f));

        head.addOrReplaceChild("horn_left",
                CubeListBuilder.create()
                        .texOffs(32, 0).mirror().addBox(3.9f, -7.0f, 7.5f, 1, 1, 13),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, HORN_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
