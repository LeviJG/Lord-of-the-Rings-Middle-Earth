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
 * LOTRModelUrukHelmet, transcribed box for box.
 *
 * <p>The shell, and two flat planes hung off it: the CREST, a broad fan standing
 * up and back over the crown, and the JAW, the pair of prongs that come down
 * over the face. Both are children of the head with their own rotation, so both
 * are separate parts here rather than more cubes on the head.
 *
 * <p>The crest is the piece that was missing. It is GEOMETRY, not texture -- its
 * pixels sit at (0,22) in uruk_helmet.png and the jaw's at (0,16), two regions
 * of the sheet the standard armour biped has never sampled, so with the vanilla
 * armour model the helmet rendered as a bare shell. Same story as the Gondorian
 * crest and the Galadhrim horn; see LOTRArmorRenderers.
 *
 * <p>Both planes have zero depth and take NO inflation -- the original passes
 * 0.0f for them and does not fold f into their coordinates either, unlike the
 * Gondorian and Galadhrim crests. The literals below are therefore the
 * original's exactly.
 */
public class LOTRUrukHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelUrukHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX: ten degrees back off vertical. */
    private static final float CREST_ANGLE = -0.17453292519943295f;

    /** jaw.rotateAngleX: sixty degrees, which swings it up in front of the face. */
    private static final float JAW_ANGLE = -1.0471975511965976f;

    private final ModelPart head;

    public LOTRUrukHelmetModel(ModelPart root) {
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

        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-10.0f, -16.0f, -1.0f, 20, 10, 0),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, CREST_ANGLE, 0.0f, 0.0f));

        head.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-6.0f, 2.0f, -4.0f, 12, 6, 0),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, JAW_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
