package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelMug, LOTRModelGoblet, LOTRModelSkullCup, LOTRModelWineGlass,
 * LOTRModelGlassBottle and LOTRModelAleHorn, box for box. Every sheet is 64x32.
 */
public final class LOTRVesselModels {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 32;
    /** The horn's bend: -0.3490658503988659, twenty degrees a section. */
    private static final float BEND = -0.34906584f;

    private final ModelPart mug = createMug().bakeRoot();
    private final ModelPart goblet = createGoblet().bakeRoot();
    private final ModelPart skull = createSkullCup().bakeRoot();
    private final ModelPart glass = createWineGlass().bakeRoot();
    private final ModelPart bottle = createGlassBottle().bakeRoot();
    private final ModelPart horn = createAleHorn().bakeRoot();

    public ModelPart modelFor(LOTRVessel vessel) {
        return switch (vessel) {
            case GOBLET_GOLD, GOBLET_SILVER, GOBLET_COPPER, GOBLET_WOOD -> goblet;
            case SKULL -> skull;
            case GLASS -> glass;
            case BOTTLE -> bottle;
            case HORN, HORN_GOLD -> horn;
            default -> mug;
        };
    }

    /** LOTRModelAleHorn.prepareLiquid: down the horn to its mouth. */
    public void applyHornLiquidTransform(PoseStack poseStack) {
        ModelPart horn0 = horn.getChild("horn");
        horn0.translateAndRotate(poseStack);
        ModelPart horn1 = horn0.getChild("horn1");
        horn1.translateAndRotate(poseStack);
        ModelPart horn2 = horn1.getChild("horn2");
        horn2.translateAndRotate(poseStack);
        horn2.getChild("horn3").translateAndRotate(poseStack);
    }

    private static LayerDefinition createMug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("mug", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0f, -8.0f, -2.0f, 1, 8, 4)
                .texOffs(10, 3).addBox(-3.0f, -8.0f, -3.0f, 6, 8, 1)
                .texOffs(24, 0).addBox(2.0f, -8.0f, -2.0f, 1, 8, 4)
                .texOffs(34, 3).addBox(-3.0f, -8.0f, 2.0f, 6, 8, 1)
                .texOffs(0, 12).addBox(-2.0f, -1.0f, -2.0f, 4, 1, 4), PartPose.ZERO);
        root.addOrReplaceChild("handle", CubeListBuilder.create()
                .texOffs(0, 17).addBox(3.0f, -7.0f, -0.5f, 2, 1, 1)
                .texOffs(0, 19).addBox(4.0f, -6.0f, -0.5f, 1, 4, 1)
                .texOffs(0, 24).addBox(3.0f, -2.0f, -0.5f, 2, 1, 1), PartPose.ZERO);
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private static LayerDefinition createGoblet() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5f, 0.0f, -2.5f, 5, 1, 5)
                .texOffs(0, 6).addBox(-0.5f, -3.0f, -0.5f, 1, 3, 1), PartPose.offset(0.0f, -1.0f, 0.0f));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-2.5f, 0.0f, -2.5f, 5, 1, 5)
                .texOffs(0, 18).addBox(-2.5f, -4.0f, -2.5f, 1, 4, 5)
                .texOffs(12, 22).addBox(-1.5f, -4.0f, -2.5f, 3, 4, 1)
                .texOffs(20, 18).addBox(1.5f, -4.0f, -2.5f, 1, 4, 5)
                .texOffs(32, 22).addBox(-1.5f, -4.0f, 1.5f, 3, 4, 1), PartPose.offset(0.0f, -5.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private static LayerDefinition createSkullCup() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0f, 0.0f, -3.0f, 6, 1, 6)
                .texOffs(0, 7).addBox(-1.0f, -3.0f, -1.0f, 2, 3, 2), PartPose.offset(0.0f, -1.0f, 0.0f));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0f, 0.0f, -4.0f, 8, 1, 8)
                .texOffs(0, 16).addBox(-4.0f, -5.0f, -4.0f, 1, 5, 8)
                .texOffs(18, 23).addBox(-3.0f, -5.0f, -4.0f, 6, 5, 1)
                .texOffs(32, 16).addBox(3.0f, -5.0f, -4.0f, 1, 5, 8)
                .texOffs(50, 23).addBox(-3.0f, -5.0f, 3.0f, 6, 5, 1), PartPose.offset(0.0f, -5.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private static LayerDefinition createWineGlass() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4)
                .texOffs(0, 5).addBox(-0.5f, -4.0f, -0.5f, 1, 4, 1), PartPose.offset(0.0f, -1.0f, 0.0f));
        root.addOrReplaceChild("cup", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.5f, 0.0f, -1.5f, 3, 1, 3)
                .texOffs(0, 20).addBox(-2.5f, -4.0f, -1.5f, 1, 4, 3)
                .texOffs(8, 22).addBox(-1.5f, -4.0f, -2.5f, 3, 4, 1)
                .texOffs(16, 20).addBox(1.5f, -4.0f, -1.5f, 1, 4, 3)
                .texOffs(24, 22).addBox(-1.5f, -4.0f, 1.5f, 3, 4, 1), PartPose.offset(0.0f, -6.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private static LayerDefinition createGlassBottle() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("bottle", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4)
                .texOffs(0, 6).addBox(-3.0f, -4.0f, -2.0f, 1, 4, 4)
                .texOffs(10, 9).addBox(-2.0f, -4.0f, -3.0f, 4, 4, 1)
                .texOffs(20, 6).addBox(2.0f, -4.0f, -2.0f, 1, 4, 4)
                .texOffs(30, 9).addBox(-2.0f, -4.0f, 2.0f, 4, 4, 1)
                .texOffs(16, 0).addBox(-2.0f, -5.0f, -2.0f, 4, 1, 4)
                .texOffs(0, 16).addBox(-1.0f, -6.0f, -1.0f, 2, 1, 2)
                .texOffs(0, 19).addBox(-1.5f, -7.0f, -1.5f, 3, 1, 3)
                .texOffs(12, 19).addBox(-1.0f, -8.5f, -1.0f, 2, 2, 2), PartPose.offset(0.0f, -1.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private static LayerDefinition createAleHorn() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition horn0 = root.addOrReplaceChild("horn", CubeListBuilder.create()
                        .texOffs(28, 16).addBox(-1.0f, -1.0f, -1.0f, 2, 6, 2),
                PartPose.offsetAndRotation(-4.0f, -5.0f, 0.0f, 0.0f, 0.0f, (float) (Math.PI / 2.0)));
        PartDefinition horn1 = horn0.addOrReplaceChild("horn1", CubeListBuilder.create()
                        .texOffs(16, 16).addBox(-1.5f, -6.0f, -1.5f, 3, 6, 3),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, BEND));
        PartDefinition horn2 = horn1.addOrReplaceChild("horn2", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-2.0f, -6.0f, -2.0f, 4, 6, 4),
                PartPose.offsetAndRotation(0.0f, -5.0f, 0.0f, 0.0f, 0.0f, BEND));
        horn2.addOrReplaceChild("horn3", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5f, -1.0f, -2.5f, 5, 1, 5)
                        .texOffs(0, 6).addBox(-2.5f, -6.0f, -1.5f, 1, 5, 3)
                        .texOffs(8, 8).addBox(-2.5f, -6.0f, -2.5f, 5, 5, 1)
                        .texOffs(20, 6).addBox(1.5f, -6.0f, -1.5f, 1, 5, 3)
                        .texOffs(28, 8).addBox(-2.5f, -6.0f, 1.5f, 5, 5, 1),
                PartPose.offsetAndRotation(0.0f, -5.0f, 0.0f, 0.0f, 0.0f, BEND));
        root.addOrReplaceChild("stand", CubeListBuilder.create()
                .texOffs(40, 16).addBox(1.5f, -8.0f, -2.5f, 1, 9, 1)
                .addBox(1.5f, -8.0f, 1.5f, 1, 9, 1)
                .texOffs(44, 16).addBox(-2.5f, -6.0f, -0.5f, 1, 7, 1), PartPose.offset(0.0f, -1.0f, 0.0f));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
