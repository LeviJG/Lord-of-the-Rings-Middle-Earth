package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * The trunk of LOTRModelEnt, which is all the Mallorn Ent trophy draws.
 *
 * <p>LOTRRenderBossTrophy takes the whole ent model, hides both arms, turns on
 * {@code trophyBottomPanel} -- a flat cap that closes off the cut where the
 * trunk was severed -- and renders {@code trunk} alone. Everything parented to
 * the trunk comes with it, so the face and the branches are here; the arms and
 * hands are not, since they are switched off, and neither are the legs, which
 * were never the trunk's children.
 *
 * <p>Angles the original set with its {@code setRotation} helper are in degrees
 * there and radians here, converted at the call.
 */
public class LOTREntTrunkModel {

    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 128;

    private final ModelPart trunk;

    public LOTREntTrunkModel(ModelPart root) {
        this.trunk = root.getChild("trunk");
    }

    private static float rad(float degrees) {
        return (float) Math.toRadians(degrees);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition trunk = root.addOrReplaceChild("trunk",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0f, -48.0f, -6.0f, 16, 48, 12),
                PartPose.offset(0.0f, -10.0f, 0.0f));

        // The face: two brows tilted towards each other, two eyes set slightly
        // proud of the bark, a nose and a hanging beard.
        trunk.addOrReplaceChild("brow_right",
                CubeListBuilder.create().texOffs(56, 26)
                        .addBox(-6.5f, 0.0f, -8.0f, 5, 1, 2),
                PartPose.offsetAndRotation(0.0f, -39.0f, 0.0f, 0.0f, 0.0f, 0.17453292f));
        trunk.addOrReplaceChild("brow_left",
                CubeListBuilder.create().texOffs(56, 26).mirror()
                        .addBox(1.5f, 0.0f, -8.0f, 5, 1, 2),
                PartPose.offsetAndRotation(0.0f, -39.0f, 0.0f, 0.0f, 0.0f, -0.17453292f));

        // The eyes take an extra 0.2 of inflation, which is what lifts them off
        // the trunk instead of z-fighting with it.
        trunk.addOrReplaceChild("eye_right",
                CubeListBuilder.create().texOffs(56, 29)
                        .addBox(-1.5f, -2.0f, -7.0f, 3, 3, 1,
                                new net.minecraft.client.model.geom.builders.CubeDeformation(0.2f)),
                PartPose.offset(-3.5f, -36.0f, 0.0f));
        trunk.addOrReplaceChild("eye_left",
                CubeListBuilder.create().texOffs(56, 29).mirror()
                        .addBox(-1.5f, -2.0f, -7.0f, 3, 3, 1,
                                new net.minecraft.client.model.geom.builders.CubeDeformation(0.2f)),
                PartPose.offset(3.5f, -36.0f, 0.0f));

        trunk.addOrReplaceChild("nose",
                CubeListBuilder.create().mirror(false).texOffs(56, 33)
                        .addBox(-1.5f, -2.0f, -9.0f, 3, 6, 3),
                PartPose.offset(0.0f, -36.0f, 0.0f));
        trunk.addOrReplaceChild("beard",
                CubeListBuilder.create().texOffs(56, 0)
                        .addBox(-5.0f, 0.0f, -8.0f, 10, 24, 2),
                PartPose.offset(0.0f, -31.0f, 0.0f));

        // showModel = false on the living ent, true on the trophy: the flat cap
        // over the severed end. It is a zero-height quad, so it is only ever
        // seen from below.
        trunk.addOrReplaceChild("trophy_bottom_panel",
                CubeListBuilder.create().texOffs(72, 116)
                        .addBox(-8.0f, 0.0f, -6.0f, 16, 0, 12),
                PartPose.offset(0.0f, -24.0f, 0.0f));

        PartDefinition branches = trunk.addOrReplaceChild("branches",
                CubeListBuilder.create(), PartPose.offset(0.0f, -48.0f, 0.0f));

        // Five boughs and four twigs, each a limb with a blunt knot of leaves
        // at its end. Positions and angles straight from the original.
        branch(branches, "branch1", 80, 16,
                -1.5f, -28.0f, -1.5f, 3, 32, 3,
                -3.5f, -32.0f, -3.5f, 7, 7, 7,
                -1.0f, 0.0f, 0.0f, -7.0f, 17.0f, 0.0f);
        branch(branches, "branch1_twig1", 80, 16,
                -7.5f, -22.0f, -1.5f, 1, 12, 1,
                -8.5f, -23.0f, -2.5f, 3, 3, 3,
                1.0f, -5.0f, -7.0f, -50.0f, 25.0f, 15.0f);
        branch(branches, "branch1_twig2", 80, 16,
                -14.0f, -26.0f, -5.5f, 2, 12, 2,
                -15.5f, -28.0f, -7.0f, 5, 5, 5,
                -2.0f, 1.0f, 7.0f, 10.0f, 10.0f, 50.0f);
        branch(branches, "branch1_twig3", 80, 16,
                -7.5f, -24.0f, -3.5f, 1, 12, 1,
                -8.5f, -25.0f, -4.5f, 3, 3, 3,
                8.0f, -6.0f, 9.0f, 15.0f, -20.0f, -30.0f);
        branch(branches, "branch2", 80, 16,
                -0.5f, -10.0f, -0.5f, 1, 14, 1,
                -1.5f, -12.0f, -1.5f, 3, 3, 3,
                6.0f, 0.0f, 2.0f, -20.0f, 42.0f, 0.0f);
        branch(branches, "branch3", 80, 16,
                -1.0f, -16.0f, -1.0f, 2, 20, 2,
                -2.5f, -18.0f, -2.5f, 5, 5, 5,
                3.0f, 0.0f, -3.0f, 26.0f, -27.0f, 0.0f);
        branch(branches, "branch4", 80, 16,
                -1.0f, -18.0f, -1.0f, 2, 22, 2,
                -2.5f, -20.0f, -2.5f, 5, 5, 5,
                -5.0f, 0.0f, -4.0f, 17.0f, 60.0f, 0.0f);
        branch(branches, "branch4_twig1", 80, 16,
                8.5f, -21.0f, -7.5f, 1, 12, 1,
                7.0f, -22.0f, -9.0f, 4, 4, 4,
                -12.0f, -6.0f, 8.0f, 50.0f, 15.0f, -10.0f);
        branch(branches, "branch5", 80, 16,
                -1.0f, -24.0f, -1.0f, 2, 28, 2,
                -2.0f, -25.0f, -2.0f, 4, 4, 4,
                -5.0f, 0.0f, 3.0f, -20.0f, -36.0f, 0.0f);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** A limb box plus the knot at its tip, at texOffs (80, 0). */
    private static void branch(PartDefinition parent, String name, int u, int v,
            float lx, float ly, float lz, int lw, int lh, int ld,
            float kx, float ky, float kz, int kw, int kh, int kd,
            float px, float py, float pz, float rx, float ry, float rz) {
        parent.addOrReplaceChild(name,
                CubeListBuilder.create()
                        .texOffs(u, v).addBox(lx, ly, lz, lw, lh, ld)
                        .texOffs(80, 0).addBox(kx, ky, kz, kw, kh, kd),
                PartPose.offsetAndRotation(px, py, pz, rad(rx), rad(ry), rad(rz)));
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.trunk.render(poseStack, consumer, light, overlay);
    }
}
