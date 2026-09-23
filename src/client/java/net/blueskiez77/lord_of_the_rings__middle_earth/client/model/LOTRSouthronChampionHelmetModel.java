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
 * LOTRModelNearHaradWarlordHelmet, transcribed box for box.
 *
 * <p>The shell with a neck guard behind it, a broad flat fan standing up at the
 * back, and three plumed STICKS fanning out of the crown -- the centre one
 * upright, the outer two tilted twenty-eight degrees either way.
 */
public class LOTRSouthronChampionHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelNearHaradWarlordHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** stickRight.rotateAngleZ; stickLeft takes the positive. */
    private static final float STICK_ANGLE = -0.4886921905584123f;

    private final ModelPart head;

    public LOTRSouthronChampionHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, new CubeDeformation(INFLATE))
                        .texOffs(6, 24).addBox(-2.5f, -3.0f, 4.1f, 5, 3, 2)
                        .texOffs(0, 16).addBox(-9.0f, -16.0f, 5.5f, 18, 8, 0),
                PartPose.ZERO);

        String[] names = {"stick_right", "stick_centre", "stick_left"};
        float[] angles = {STICK_ANGLE, 0.0f, -STICK_ANGLE};
        for (int i = 0; i < names.length; i++) {
            head.addOrReplaceChild(names[i],
                    CubeListBuilder.create()
                            .texOffs(36, 0).addBox(-0.5f, -19.0f, 5.0f, 1, 18, 1)
                            .texOffs(0, 24).addBox(-1.5f, -24.0f, 5.5f, 3, 5, 0),
                    PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, angles[i]));
        }

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
