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
 * LOTRModelKebabStand: a hearth plate with a back wall, two angled side panels
 * and an upright spit.
 *
 * <p>The meat is a SEPARATE model, and a deliberately crude one -- one box per
 * quantity, growing wider as more is loaded on, so eight skewers read as a
 * fat roast rather than eight separate objects. Index 0 draws nothing.
 *
 * <p>Note the two layers use DIFFERENT texture sizes: the stand is on a 64x64
 * sheet and the meat on a 32x32 one. The original swapped textureWidth mid
 * constructor to do that; here they are simply two layer definitions.
 */
public class LOTRKebabStandModel {

    public static final int STAND_TEXTURE_SIZE = 64;
    public static final int MEAT_TEXTURE_SIZE = 32;

    /** LOTRModelKebabStand.kebab has nine entries, 0 unused. */
    public static final int MEAT_MODELS = 9;

    private final ModelPart stand;
    private final ModelPart[] meat = new ModelPart[MEAT_MODELS];

    public LOTRKebabStandModel(ModelPart standRoot, ModelPart meatRoot) {
        this.stand = standRoot.getChild("stand");
        for (int i = 1; i < MEAT_MODELS; ++i) {
            this.meat[i] = meatRoot.getChild("meat_" + i);
        }
    }

    public static LayerDefinition createStandLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition stand = root.addOrReplaceChild("stand", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-7.0f, -1.0f, -7.0f, 14, 1, 14)      // hearth plate
                        .texOffs(0, 15)
                        .addBox(-4.0f, -16.0f, 6.0f, 8, 15, 1)       // back wall
                        .texOffs(0, 31)
                        .addBox(-4.0f, -16.0f, -2.0f, 8, 1, 8)       // canopy
                        .texOffs(0, 40)
                        .addBox(-0.5f, -15.0f, -0.5f, 1, 14, 1),     // the spit
                PartPose.offset(0.0f, 24.0f, 0.0f));

        // The two side panels splay out from the back wall at 45 degrees.
        stand.addOrReplaceChild("panel_right", CubeListBuilder.create()
                        .texOffs(18, 15)
                        .addBox(-4.0f, -16.0f, 0.0f, 4, 15, 1),
                PartPose.offsetAndRotation(-4.0f, 0.0f, 6.0f, 0.0f, -Mth.PI / 4.0f, 0.0f));
        stand.addOrReplaceChild("panel_left", CubeListBuilder.create()
                        .texOffs(18, 15)
                        .addBox(0.0f, -16.0f, 0.0f, 4, 15, 1),
                PartPose.offsetAndRotation(4.0f, 0.0f, 6.0f, 0.0f, Mth.PI / 4.0f, 0.0f));

        return LayerDefinition.create(mesh, STAND_TEXTURE_SIZE, STAND_TEXTURE_SIZE);
    }

    public static LayerDefinition createMeatLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // One box per meat count. Width grows with the load; height is fixed.
        for (int i = 1; i < MEAT_MODELS; ++i) {
            int width = i + 1;
            root.addOrReplaceChild("meat_" + i, CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-width / 2.0f, 0.0f, -width / 2.0f, width, 11, width),
                    PartPose.offset(0.0f, 10.0f, 0.0f));
        }
        return LayerDefinition.create(mesh, MEAT_TEXTURE_SIZE, MEAT_TEXTURE_SIZE);
    }

    public void renderStand(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        stand.render(poseStack, consumer, light, overlay);
    }

    /** {@code size} is the meat count; {@code spin} is in degrees. */
    public void renderMeat(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
                           int size, float spin) {
        if (size <= 0 || size >= MEAT_MODELS) {
            return;
        }
        ModelPart part = meat[size];
        part.yRot = spin * Mth.DEG_TO_RAD;
        part.render(poseStack, consumer, light, overlay);
    }
}
