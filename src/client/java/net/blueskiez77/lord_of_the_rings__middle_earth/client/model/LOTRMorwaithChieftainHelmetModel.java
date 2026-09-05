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
 * LOTRModelMoredainLionHelmet: a lion's head worn as a helm.
 *
 * <p>The shell, then the beast on top of it -- a brow band, the muzzle running
 * twelve pixels forward, a snout ridge on that, and two fangs, the right one
 * mirrored from the left. Around it hangs the MANE: four flat panels, one each
 * side, one behind and one standing up over the crown, each tipped a few
 * degrees out so the whole thing sits proud of the head rather than flat on it.
 *
 * <p>All four panels are zero-thickness and take no inflation; the boxes on the
 * head itself take the shell's.
 */
public class LOTRMorwaithChieftainHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private static final float INFLATE = 1.0f;

    /** Four degrees, which is all the panels are splayed by. */
    private static final float PANEL_SPLAY = 0.06981317007977318f;

    /** The crown panel leans back ten. */
    private static final float TOP_SPLAY = -0.17453292519943295f;

    private final ModelPart head;

    public LOTRMorwaithChieftainHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        // The brow band, the muzzle and the ridge along it.
                        .texOffs(34, 16).addBox(-4.5f, -9.0f, -2.5f, 9, 2, 5, f)
                        .texOffs(0, 17).addBox(-2.5f, -10.0f, -7.0f, 5, 3, 12, f)
                        .texOffs(34, 23).addBox(-1.0f, -10.4f, -7.2f, 2, 2, 7, f)
                        // And the two fangs, the second mirrored off the first.
                        .texOffs(0, 0).addBox(-2.0f, -8.0f, -6.8f - INFLATE, 1, 3, 1)
                        .mirror().texOffs(0, 0).addBox(1.0f, -8.0f, -6.8f - INFLATE, 1, 3, 1)
                        .mirror(false),
                PartPose.ZERO);

        head.addOrReplaceChild("mane_right",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-5.0f - INFLATE, -8.0f, -3.0f, 0, 8, 8),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, PANEL_SPLAY));

        head.addOrReplaceChild("mane_left",
                CubeListBuilder.create().mirror()
                        .texOffs(32, 0).addBox(5.0f + INFLATE, -8.0f, -3.0f, 0, 8, 8),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -PANEL_SPLAY));

        head.addOrReplaceChild("mane_back",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-4.0f, -8.0f, 4.8f + INFLATE, 8, 10, 0),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, PANEL_SPLAY, 0.0f, 0.0f));

        head.addOrReplaceChild("mane_top",
                CubeListBuilder.create()
                        .texOffs(52, 25).addBox(-2.5f, -16.0f - INFLATE, -2.0f, 5, 7, 0),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, TOP_SPLAY, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
