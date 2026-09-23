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
 * LOTRModelEasterlingHelmet, transcribed box for box.
 *
 * <p>The golden Rhûnic helmet: a shell with a wide brim, a raised cap, a nose
 * fin, a forward-raked HORN on the crown, and a back plate whose CREST tips
 * thirty degrees out. The warlord's version -- {@code kineHorns} -- adds a pair
 * of three-segment kine horns sweeping out and up, and needs a 64x64 sheet for
 * them, which is why the original switched texture size on the same flag.
 */
public class LOTREasterlingHelmetModel {

    /** The f LOTRArmorModels passed: new LOTRModelEasterlingHelmet(1.0f, ...). */
    private static final float INFLATE = 1.0f;

    private static final float HORN_ANGLE = 0.3490658503988659f;
    private static final float CREST_ANGLE = 0.5235987755982988f;
    private static final float KINE_ANGLE = 0.6981317007977318f;
    private static final float KINE_BEND = -0.5235987755982988f;

    private final ModelPart head;

    public LOTREasterlingHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer(boolean kineHorns) {
        float f = INFLATE;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(f))
                        .texOffs(0, 16).addBox(-5.5f, -8.5f - f, -5.5f, 11, 2, 11)
                        .texOffs(32, 8).addBox(-3.5f, -9.5f - f, -3.5f, 7, 1, 7)
                        .texOffs(50, 16).addBox(0.0f, -10.5f - f, -4.5f - f, 0, 3, 4)
                        .texOffs(24, 0).addBox(-1.0f, -8.0f - f, 4.0f + f, 2, 4, 1)
                        .texOffs(32, 2).addBox(-6.0f, -12.0f - f, 5.0f + f, 12, 4, 0),
                PartPose.ZERO);

        head.addOrReplaceChild("horn",
                CubeListBuilder.create()
                        .texOffs(44, 16).addBox(-0.5f, -14.0f - f, -2.0f - f, 1, 8, 2),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, HORN_ANGLE, 0.0f, 0.0f));

        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-6.0f, -2.0f, 0.0f, 12, 2, 0),
                PartPose.offsetAndRotation(0.0f, -12.0f - f, 5.0f + f, CREST_ANGLE, 0.0f, 0.0f));

        if (kineHorns) {
            PartDefinition right = head.addOrReplaceChild("kine_horn_right",
                    CubeListBuilder.create().texOffs(0, 32).addBox(-7.0f, -1.5f, -1.5f, 7, 3, 3),
                    PartPose.offsetAndRotation(-1.0f - f, -8.0f - f, 0.0f, 0.0f, 0.0f, KINE_ANGLE));
            PartDefinition right1 = right.addOrReplaceChild("kine_horn_right_1",
                    CubeListBuilder.create().texOffs(0, 38).addBox(-5.0f, -1.0f, -1.0f, 6, 2, 2),
                    PartPose.offsetAndRotation(-7.0f, 0.0f, 0.0f, 0.0f, 0.0f, KINE_BEND));
            right1.addOrReplaceChild("kine_horn_right_2",
                    CubeListBuilder.create().texOffs(0, 42).addBox(-3.0f, -0.5f, -0.5f, 4, 1, 1),
                    PartPose.offsetAndRotation(-5.0f, 0.0f, 0.0f, 0.0f, 0.0f, KINE_BEND));

            PartDefinition left = head.addOrReplaceChild("kine_horn_left",
                    CubeListBuilder.create().texOffs(0, 32).mirror().addBox(0.0f, -1.5f, -1.5f, 7, 3, 3),
                    PartPose.offsetAndRotation(1.0f + f, -8.0f - f, 0.0f, 0.0f, 0.0f, -KINE_ANGLE));
            PartDefinition left1 = left.addOrReplaceChild("kine_horn_left_1",
                    CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-1.0f, -1.0f, -1.0f, 6, 2, 2),
                    PartPose.offsetAndRotation(7.0f, 0.0f, 0.0f, 0.0f, 0.0f, -KINE_BEND));
            left1.addOrReplaceChild("kine_horn_left_2",
                    CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-1.0f, -0.5f, -0.5f, 4, 1, 1),
                    PartPose.offsetAndRotation(5.0f, 0.0f, 0.0f, 0.0f, 0.0f, -KINE_BEND));
        }

        return LayerDefinition.create(mesh, 64, kineHorns ? 64 : 32);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
