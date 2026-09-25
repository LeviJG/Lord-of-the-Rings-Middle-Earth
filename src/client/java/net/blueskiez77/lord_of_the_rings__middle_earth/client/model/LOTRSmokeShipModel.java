package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/** LOTRModelSmokeShip: the magic pipe's ship -- hull, deck, three masts and sails, bow and stern. */
public class LOTRSmokeShipModel {

    private final ModelPart root;

    public LOTRSmokeShipModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("ship", CubeListBuilder.create()
                .addBox(-3.5f, 1.0f, -8.0f, 7, 5, 16)
                .addBox(-5.0f, 0.0f, -8.0f, 10, 1, 16)
                .addBox(-1.0f, -9.0f, -6.0f, 2, 9, 2)
                .addBox(-6.0f, -8.0f, -5.5f, 12, 6, 1)
                .addBox(-1.0f, -12.0f, -1.0f, 2, 12, 2)
                .addBox(-8.0f, -11.0f, -0.5f, 16, 8, 1)
                .addBox(-1.0f, -9.0f, 4.0f, 2, 9, 2)
                .addBox(-6.0f, -8.0f, 4.5f, 12, 6, 1)
                .addBox(-3.5f, -1.0f, -12.0f, 7, 3, 4)
                .addBox(-3.5f, -1.0f, 8.0f, 7, 3, 4),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int colour) {
        this.root.render(poseStack, consumer, light, overlay, colour);
    }
}
