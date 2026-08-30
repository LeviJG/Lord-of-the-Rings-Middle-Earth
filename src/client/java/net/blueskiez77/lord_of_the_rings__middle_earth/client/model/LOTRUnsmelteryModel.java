package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * LOTRModelUnsmeltery, transcribed box for box: a wide cauldron slung between
 * two uprights, standing on a plinth.
 *
 * <p>The cauldron TIPS as it works -- {@code body.rotateAngleX} is driven by
 * the rocking amount, up to twenty degrees either way, which is what makes the
 * machine look like it is pouring.
 */
public class LOTRUnsmelteryModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 64;

    /** {@code f * 0.3490658503988659f} -- twenty degrees, in radians. */
    private static final float MAX_ROCK_RADIANS = 20.0f * Mth.DEG_TO_RAD;

    private final ModelPart base;
    private final ModelPart body;
    private final ModelPart standRight;
    private final ModelPart standLeft;

    public LOTRUnsmelteryModel(ModelPart root) {
        this.base = root.getChild("base");
        this.body = root.getChild("body");
        this.standRight = root.getChild("stand_right");
        this.standLeft = root.getChild("stand_left");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-7.0f, 0.0f, -7.0f, 14, 3, 14),
                PartPose.offset(0.0f, 21.0f, 0.0f));

        // The cauldron, plus the four strips of its rim.
        root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-7.0f, -2.0f, -7.0f, 14, 10, 14)
                        .texOffs(0, 41)
                        .addBox(-7.0f, -4.0f, -7.0f, 14, 2, 1)
                        .addBox(-7.0f, -4.0f, 6.0f, 14, 2, 1)
                        .texOffs(0, 44)
                        .addBox(-7.0f, -4.0f, -6.0f, 1, 2, 12)
                        .addBox(6.0f, -4.0f, -6.0f, 1, 2, 12),
                PartPose.offset(0.0f, 12.0f, 0.0f));

        // Each upright carries a small panel tilted 45 degrees at its top.
        PartDefinition standRight = root.addOrReplaceChild("stand_right", CubeListBuilder.create()
                        .texOffs(56, 6)
                        .addBox(-0.9f, -12.0f, -1.0f, 1, 12, 2),
                PartPose.offset(-7.0f, 23.0f, 0.0f));
        standRight.addOrReplaceChild("panel", CubeListBuilder.create()
                        .texOffs(56, 0)
                        .addBox(-1.0f, -2.0f, -1.0f, 1, 3, 3),
                PartPose.offsetAndRotation(0.0f, -11.0f, 0.0f, Mth.PI / 4.0f, 0.0f, 0.0f));

        PartDefinition standLeft = root.addOrReplaceChild("stand_left", CubeListBuilder.create()
                        .texOffs(56, 6)
                        .mirror()
                        .addBox(-0.1f, -12.0f, -1.0f, 1, 12, 2),
                PartPose.offset(7.0f, 23.0f, 0.0f));
        standLeft.addOrReplaceChild("panel", CubeListBuilder.create()
                        .texOffs(56, 0)
                        .mirror()
                        .addBox(0.0f, -2.0f, -1.0f, 1, 3, 3),
                PartPose.offsetAndRotation(0.0f, -11.0f, 0.0f, Mth.PI / 4.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** {@code rocking} runs -1..1; the cauldron tips that far either way. */
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
                       float rocking) {
        body.xRot = rocking * MAX_ROCK_RADIANS;
        base.render(poseStack, consumer, light, overlay);
        body.render(poseStack, consumer, light, overlay);
        standRight.render(poseStack, consumer, light, overlay);
        standLeft.render(poseStack, consumer, light, overlay);
    }
}
