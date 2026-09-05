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
 * LOTRModelTauredainChieftainHelmet, transcribed box for box.
 *
 * <p>The shell, a broad band around the back of the head, and an enormous flat
 * CREST -- sixteen wide by fourteen tall, tipped ten degrees back. It is a
 * child of the head with that rotation, and it is what makes the helmet a
 * chieftain's; without it the piece rendered as a bare cap.
 */
public class LOTRTaurethrimChieftainHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private static final float INFLATE = 1.0f;

    /** crest.rotateAngleX: ten degrees back. */
    private static final float CREST_ANGLE = -0.17453292519943295f;

    private final ModelPart head;

    public LOTRTaurethrimChieftainHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f)
                        .texOffs(32, 0).addBox(-5.0f, -9.0f, 0.0f, 10, 6, 3, f),
                PartPose.ZERO);

        // The crest's own rotation point is dropped by f, which is the one
        // place the inflation reaches it -- the box itself takes none.
        head.addOrReplaceChild("crest",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-8.0f, -23.0f, 0.0f, 16, 14, 0),
                PartPose.offsetAndRotation(0.0f, -INFLATE, 0.0f, CREST_ANGLE, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
