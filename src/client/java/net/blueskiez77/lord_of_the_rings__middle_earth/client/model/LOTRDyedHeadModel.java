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
 * The three dyed head pieces, which the original tinted as a whole with
 * glColor3f: LOTRModelLeatherHat, LOTRModelPartyHat and LOTRModelHaradTurban.
 */
public class LOTRDyedHeadModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** turban shawl.rotateAngleX. */
    private static final float SHAWL_ANGLE = 0.22689280275926285f;

    private final ModelPart head;

    public LOTRDyedHeadModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    /** LOTRModelLeatherHat(): a 12x12 brim and an 8x8 crown, no inflation. */
    public static LayerDefinition createLeatherHat() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-6.0f, -9.0f, -6.0f, 12, 2, 12)
                        .texOffs(0, 14).addBox(-4.0f, -13.0f, -4.0f, 8, 4, 8),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** new LOTRModelPartyHat(0.6f): one cube, raised off the head. */
    public static LayerDefinition createPartyHat() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -14.0f, -4.0f, 8, 8, 8, new CubeDeformation(0.6f)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /**
     * LOTRModelHaradTurban(): the wrap and the shawl down the back. The gold
     * ornament on the brow is left out -- the port's turban has no ornament
     * state yet (see B6d).
     */
    public static LayerDefinition createTurban() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, -10.0f, -5.0f, 10, 5, 10),
                PartPose.ZERO);
        head.addOrReplaceChild("shawl",
                CubeListBuilder.create()
                        .texOffs(0, 15).addBox(-4.5f, -5.0f, 1.5f, 9, 6, 4, new CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, SHAWL_ANGLE, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
        this.head.render(poseStack, consumer, light, overlay, color);
    }
}
