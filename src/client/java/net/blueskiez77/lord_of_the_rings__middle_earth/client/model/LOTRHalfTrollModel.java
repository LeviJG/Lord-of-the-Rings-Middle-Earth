package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;

import org.jspecify.annotations.Nullable;

/**
 * LOTRModelHalfTroll: a great biped on a 64x64 texture -- a ten-pixel head
 * with jaw, nose, tusks and ears, raised eight pixels with the body, a broad
 * body, thick arms set far out and long legs, and no headwear -- with a
 * mohawk and horns (short, or full) shown as the half-troll has them. The
 * armour is the same model at 1.0 (0.5 for leggings), mohawk and horns and
 * all, as func_82421_b made it.
 */
public class LOTRHalfTrollModel<S extends LOTRHalfTrollModel.State> extends LOTRBipedModel<S> {

    /** The half-troll's mohawk and horns (getHalfTrollModelFlag). */
    public static class State extends LOTRNPCRenderState {
        public boolean mohawk;
        public boolean horns;
        public boolean fullHorns;
    }

    private final ModelPart mohawk;
    private final ModelPart hornRight1;
    private final ModelPart hornRight2;
    private final ModelPart hornLeft1;
    private final ModelPart hornLeft2;
    /** The armour piece this model draws, or null for the half-troll itself. */
    private final @Nullable EquipmentSlot armorSlot;

    public LOTRHalfTrollModel(ModelPart root) {
        this(root, null);
    }

    public LOTRHalfTrollModel(ModelPart root, @Nullable EquipmentSlot armorSlot) {
        super(root);
        this.mohawk = this.head.getChild("mohawk");
        this.hornRight1 = this.head.getChild("horn_right1");
        this.hornRight2 = this.head.getChild("horn_right2");
        this.hornLeft1 = this.head.getChild("horn_left1");
        this.hornLeft2 = this.head.getChild("horn_left2");
        this.armorSlot = armorSlot;
    }

    public static LayerDefinition createLayer(CubeDeformation f) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0f, -10.0f, -5.0f, 10.0f, 10.0f, 10.0f, f)
                .texOffs(40, 5).addBox(-4.0f, -3.0f, -7.0f, 8.0f, 3.0f, 2.0f, f),
                PartPose.offset(0.0f, -8.0f, 0.0f));
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(30, 0)
                .addBox(-1.0f, -4.5f, -8.0f, 2.0f, 3.0f, 3.0f, f), PartPose.rotation(-0.34906584f, 0.0f, 0.0f));
        head.addOrReplaceChild("teeth", CubeListBuilder.create().texOffs(60, 7)
                .addBox(-3.5f, -7.5f, -5.0f, 1.0f, 2.0f, 1.0f, f)
                .mirror().addBox(2.5f, -7.5f, -5.0f, 1.0f, 2.0f, 1.0f, f), PartPose.rotation(0.5235988f, 0.0f, 0.0f));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-5.0f, -6.0f, -2.0f, 1.0f, 3.0f, 3.0f, f), PartPose.rotation(0.0f, -0.61086524f, 0.0f));
        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(0, 0).mirror()
                .addBox(4.0f, -6.0f, -2.0f, 1.0f, 3.0f, 3.0f, f), PartPose.rotation(0.0f, 0.61086524f, 0.0f));
        head.addOrReplaceChild("mohawk", CubeListBuilder.create().texOffs(40, 10)
                .addBox(-1.0f, -12.5f, -1.5f, 2.0f, 10.0f, 8.0f, f), PartPose.ZERO);
        head.addOrReplaceChild("horn_right1", CubeListBuilder.create().texOffs(40, 0)
                .addBox(-10.0f, -8.0f, 1.0f, 3.0f, 2.0f, 2.0f, f), PartPose.rotation(0.0f, 0.0f, 0.34906584f));
        head.addOrReplaceChild("horn_right2", CubeListBuilder.create().texOffs(50, 2)
                .addBox(-14.5f, -4.0f, 1.5f, 3.0f, 1.0f, 1.0f, f), PartPose.rotation(0.0f, 0.0f, 0.6981317f));
        head.addOrReplaceChild("horn_left1", CubeListBuilder.create().texOffs(40, 0).mirror()
                .addBox(7.0f, -8.0f, 1.0f, 3.0f, 2.0f, 2.0f, f), PartPose.rotation(0.0f, 0.0f, -0.34906584f));
        head.addOrReplaceChild("horn_left2", CubeListBuilder.create().texOffs(50, 2).mirror()
                .addBox(11.5f, -4.0f, 1.5f, 3.0f, 1.0f, 1.0f, f), PartPose.rotation(0.0f, 0.0f, -0.6981317f));
        // bipedHeadwear.isHidden: an empty hat.
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20)
                .addBox(-6.0f, 0.0f, -4.0f, 12.0f, 16.0f, 8.0f, f), PartPose.offset(0.0f, -8.0f, 0.0f));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(20, 50).addBox(-3.5f, -2.0f, -3.0f, 6.0f, 8.0f, 6.0f, f)
                .texOffs(0, 49).addBox(-3.0f, 6.0f, -2.5f, 5.0f, 10.0f, 5.0f, f),
                PartPose.offset(-8.5f, -6.0f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
                .texOffs(20, 50).addBox(-2.5f, -2.0f, -3.0f, 6.0f, 8.0f, 6.0f, f)
                .texOffs(0, 49).addBox(-2.0f, 6.0f, -2.5f, 5.0f, 10.0f, 5.0f, f),
                PartPose.offset(8.5f, -6.0f, 0.0f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 28)
                .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 16.0f, 6.0f, f), PartPose.offset(-3.2f, 8.0f, 0.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 28).mirror()
                .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 16.0f, 6.0f, f), PartPose.offset(3.2f, 8.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createBodyLayer() {
        return createLayer(CubeDeformation.NONE);
    }

    /** LOTRRenderHalfTrollScavenger's outfit: LOTRModelHalfTroll(0.5). */
    public static LayerDefinition createOutfitLayer() {
        return createLayer(new CubeDeformation(0.5f));
    }

    /** func_82421_b: LOTRModelHalfTroll(1.0) outside, (0.5) for leggings. */
    public static LayerDefinition createArmorLayer(EquipmentSlot slot) {
        return createLayer(new CubeDeformation(slot == EquipmentSlot.LEGS ? 0.5f : 1.0f));
    }

    private static final java.util.Map<EquipmentSlot, Set<String>> ARMOR_PARTS = java.util.Map.of(
            EquipmentSlot.HEAD, Set.of("head"),
            EquipmentSlot.CHEST, Set.of("body", "right_arm", "left_arm"),
            EquipmentSlot.LEGS, Set.of("body", "right_leg", "left_leg"),
            EquipmentSlot.FEET, Set.of("right_leg", "left_leg"));

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        this.hat.visible = false;
        this.mohawk.visible = state.mohawk;
        this.hornRight1.visible = this.hornLeft1.visible = state.horns;
        this.hornRight2.visible = this.hornLeft2.visible = state.fullHorns;
        if (this.armorSlot != null) {
            Set<String> parts = ARMOR_PARTS.get(this.armorSlot);
            this.head.visible = parts.contains("head");
            this.body.visible = parts.contains("body");
            this.rightArm.visible = parts.contains("right_arm");
            this.leftArm.visible = parts.contains("left_arm");
            this.rightLeg.visible = parts.contains("right_leg");
            this.leftLeg.visible = parts.contains("left_leg");
        }
    }

    /** Held 0.2625 lower than a man's (getHeldItemYTranslation 0.45 against 0.1875). */
    @Override
    public void translateToHand(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack) {
        super.translateToHand(state, arm, poseStack);
        poseStack.translate(0.0f, 0.2625f, 0.0f);
    }
}
