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
 * LOTRModelSwanHelmet: Dol Amroth's, with a swan's wing up each side.
 *
 * <p>The shell, a low comb over the crown, and two WINGS -- each a thin spar
 * with a flat vane hanging off it, swept out and back. The original sets their
 * rotation in setRotationAngles rather than at build time, but it sets the same
 * two angles every frame and never varies them, so they are baked into the part
 * poses here.
 *
 * <p>The vanes are zero-thickness. Their back faces sample a blank stretch of
 * the sheet and are discarded by the cutout, and the armour layer does not cull,
 * so each reads correctly from both sides.
 */
public class LOTRSwanHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private static final float INFLATE = 1.0f;

    /** The wings' fixed splay: 25 degrees out, 20 degrees up. */
    private static final float WING_YAW = -0.4363323129985824f;
    private static final float WING_PITCH = 0.3490658503988659f;

    private final ModelPart head;

    public LOTRSwanHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        .texOffs(32, 0).addBox(-0.5f, -9.0f, -3.5f, 1, 1, 7, f),
                PartPose.ZERO);

        head.addOrReplaceChild("wing_right",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-4.0f - INFLATE, -6.0f, 1.0f + INFLATE, 1, 1, 9)
                        .texOffs(20, 16).addBox(-3.5f - INFLATE, -5.0f, 1.9f + INFLATE, 0, 6, 8),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, WING_PITCH, WING_YAW, 0.0f));

        head.addOrReplaceChild("wing_left",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 16).addBox(3.0f + INFLATE, -6.0f, 1.0f + INFLATE, 1, 1, 9)
                        .texOffs(20, 16).addBox(3.5f + INFLATE, -5.0f, 1.9f + INFLATE, 0, 6, 8),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, WING_PITCH, -WING_YAW, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
