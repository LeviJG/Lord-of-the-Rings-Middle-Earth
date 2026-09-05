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
 * LOTRModelRohanMarshalHelmet, transcribed box for box.
 *
 * <p>The shell, a raised nasal and brow, and a horsehair PLUME: three identical
 * flat panes hung off the crown and fanned, the middle one tipped further back
 * than the two beside it.
 *
 * <p>The fan is baked in. The original works the three angles out in
 * setRotationAngles from the pane's index -- {@code (mid - |i - mid|) / mid} for
 * the pitch and {@code (i - mid) / mid} for the yaw, with mid at 1 -- which is
 * the same three numbers every frame, so they are constants here.
 *
 * <p>NOT ported: the sway. The original adds
 * {@code sin(limbSwing * 0.4) * limbSwingAmount * 0.2} to each pane's pitch, so
 * the plume bounces as the wearer walks. Fabric's ArmorRenderer hands over no
 * limb-swing state, so the plume is still; it wants the walk cycle plumbed
 * through the seam before it can move.
 */
public class LOTRRohirricMarshalHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private static final float INFLATE = 1.0f;

    /** The three panes' fixed fan, off the original's own arithmetic at mid = 1. */
    private static final float[] PLUME_PITCH = {0.0f, 0.22f, 0.0f};
    private static final float[] PLUME_YAW = {-0.17f, 0.0f, 0.17f};

    private final ModelPart head;

    public LOTRRohirricMarshalHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        .texOffs(0, 16).addBox(-1.0f, -11.5f - INFLATE, -4.5f - INFLATE, 2, 7, 6),
                PartPose.ZERO);

        for (int i = 0; i < PLUME_PITCH.length; i++) {
            head.addOrReplaceChild("plume_" + i,
                    CubeListBuilder.create()
                            .texOffs(32, 0).addBox(0.0f, -11.0f, -1.0f, 0, 14, 12),
                    PartPose.offsetAndRotation(0.0f, -INFLATE, INFLATE,
                            PLUME_PITCH[i], PLUME_YAW[i], 0.0f));
        }

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
