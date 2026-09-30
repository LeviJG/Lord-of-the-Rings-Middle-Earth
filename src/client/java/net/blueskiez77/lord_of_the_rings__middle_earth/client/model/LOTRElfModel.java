package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * LOTRModelElf: a biped with pointed ears tilted out fifteen degrees, a
 * woman's chest, and -- on a 64x64 texture -- a tall hood of hair (the skin's
 * lower half, 8x16x8 over the head). The ears are never inflated, whatever
 * the model's size. A jazz elf in the middle of a solo holds its arms up and
 * in, round a saxophone.
 */
public class LOTRElfModel<S extends LOTRElfModel.ElfState> extends LOTRBipedModel<S> {

    /** What the elf model reads off its elf. */
    public static class ElfState extends LOTRNPCRenderState {
        public boolean jazzSolo;
    }

    private final ModelPart chest;

    public LOTRElfModel(ModelPart root) {
        super(root);
        this.chest = this.body.getChild("chest");
    }

    /** LOTRModelElf(f, width, height): a tall hood of hair only on a 64-high texture. */
    public static MeshDefinition createMesh(CubeDeformation f, boolean tallHair) {
        MeshDefinition mesh = HumanoidModel.createMesh(f, 0.0f);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0f, -6.5f, -1.0f, 1.0f, 4.0f, 2.0f), PartPose.rotation(0.0f, 0.0f, -0.2617994f));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 0).mirror()
                .addBox(3.0f, -6.5f, -1.0f, 1.0f, 4.0f, 2.0f), PartPose.rotation(0.0f, 0.0f, 0.2617994f));
        if (tallHair) {
            head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 16.0f, 8.0f, f.extend(0.5f)), PartPose.ZERO);
        }
        root.getChild("body").addOrReplaceChild("chest", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-3.0f, 2.0f, -4.0f, 6.0f, 3.0f, 2.0f, f), PartPose.ZERO);
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE, true), 64, 64);
    }

    /** The jazz outfit: LOTRModelElf(0.6, 64, 64). */
    public static LayerDefinition createOutfitLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.6f), true), 64, 64);
    }

    /** A trader's or smith's outfit: LOTRModelElf(0.5), a 64x32 texture with the ordinary hat. */
    public static LayerDefinition createTraderOutfitLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.5f), false), 64, 32);
    }

    /** func_82421_b: LOTRModelElf(1.0) outside, (0.5) for leggings, 64x32. */
    public static ArmorModelSet<LayerDefinition> createArmorLayers() {
        return new ArmorModelSet<>(
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.HEAD),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.CHEST),
                armorLayer(new CubeDeformation(0.5f), EquipmentSlot.LEGS),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.FEET));
    }

    private static LayerDefinition armorLayer(CubeDeformation f, EquipmentSlot slot) {
        MeshDefinition mesh = createMesh(f, false);
        Set<String> parts = ADULT_ARMOR_PARTS_PER_SLOT.get(slot);
        if (slot == EquipmentSlot.HEAD) {
            mesh.getRoot().retainPartsAndChildren(parts);
        } else {
            mesh.getRoot().retainExactParts(parts);
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        this.chest.visible = state.renderChest;
        if (state.jazzSolo) {
            this.rightArm.yRot = -0.7853981633974483f;
            this.leftArm.yRot = -this.rightArm.yRot;
            this.leftArm.xRot = this.rightArm.xRot = -0.8726646259971648f;
        }
    }
}
