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
 * LOTRModelTauredainGoldHelmet, transcribed box for box.
 *
 * <p>The shell, a flat CREST fanned up over the crown, and four TUSKS: two
 * zero-width planes at each front corner of the head, splayed out at different
 * angles. None of it is on the standard armour biped, so with that model the
 * helmet rendered as a bare shell with the crest and tusk pixels unused -- the
 * texture samples them from (32,0) and (0,16).
 *
 * <p>As in the original, the crest and tusks take no inflation of their own;
 * f only reaches them through their rotation points.
 */
public class LOTRGoldenTaurethrimHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelTauredainGoldHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX: four degrees back. */
    private static final float CREST_ANGLE = -0.06981317007977318f;

    /** Every tusk's rotateAngleX: twenty degrees. */
    private static final float TUSK_PITCH = 0.3490658503988659f;

    /** The outer tusks splay thirty degrees, the inner ones twenty. */
    private static final float TUSK_YAW_OUTER = 0.5235987755982988f;
    private static final float TUSK_YAW_INNER = 0.3490658503988659f;

    private final ModelPart head;

    public LOTRGoldenTaurethrimHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f)),
                PartPose.ZERO);

        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-7.0f, -20.0f, 0.0f, 14, 12, 0),
                PartPose.offsetAndRotation(0.0f, -f, 0.0f, CREST_ANGLE, 0.0f, 0.0f));

        CubeListBuilder tusk = CubeListBuilder.create()
                .texOffs(0, 16).addBox(0.0f, -6.0f, -5.0f, 0, 6, 6);
        float rightX = -3.5f - f;
        float leftX = 3.5f + f;
        float y = 0.0f + f;
        float z = -4.0f - f;
        head.addOrReplaceChild("tusks1", tusk,
                PartPose.offsetAndRotation(rightX, y, z, TUSK_PITCH, TUSK_YAW_OUTER, 0.0f));
        head.addOrReplaceChild("tusks2", tusk,
                PartPose.offsetAndRotation(rightX, y, z, TUSK_PITCH, -TUSK_YAW_INNER, 0.0f));
        head.addOrReplaceChild("tusks3", tusk,
                PartPose.offsetAndRotation(leftX, y, z, TUSK_PITCH, TUSK_YAW_INNER, 0.0f));
        head.addOrReplaceChild("tusks4", tusk,
                PartPose.offsetAndRotation(leftX, y, z, TUSK_PITCH, -TUSK_YAW_OUTER, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
