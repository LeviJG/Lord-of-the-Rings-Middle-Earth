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
 * LOTRModelOrc: a biped with a snout and two pointed ears swept back, on a
 * 64x32 skin. The snout and ears grow with the model's size, so a helmet
 * covers them too.
 */
public class LOTROrcModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    public LOTROrcModel(ModelPart root) {
        super(root);
    }

    public static MeshDefinition createMesh(CubeDeformation f) {
        MeshDefinition mesh = HumanoidModel.createMesh(f, 0.0f);
        PartDefinition head = mesh.getRoot().getChild("head");
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(14, 17)
                .addBox(-0.5f, -4.0f, -4.8f, 1.0f, 2.0f, 1.0f, f), PartPose.ZERO);
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.5f, -5.5f, 2.0f, 1.0f, 2.0f, 3.0f, f),
                PartPose.rotation(0.2617994f, -0.5235988f, -0.22689281f));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 0)
                .addBox(2.5f, -5.5f, 2.0f, 1.0f, 2.0f, 3.0f, f),
                PartPose.rotation(0.2617994f, 0.5235988f, 0.22689281f));
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE), 64, 32);
    }

    /** The glowing eyes: LOTRModelOrc(0.05), of which only the head is drawn ({@link #showOnlyHead}). */
    public static LayerDefinition createEyesLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.05f)), 64, 32);
    }

    /** renderGlowingEyes: bipedHead alone, snout and ears with it, not the headwear. */
    public void showOnlyHead() {
        this.hat.visible = false;
        this.body.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;
    }

    /** func_82421_b: LOTRModelOrc(1.0) outside, (0.5) for leggings. */
    public static ArmorModelSet<LayerDefinition> createArmorLayers() {
        return new ArmorModelSet<>(
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.HEAD),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.CHEST),
                armorLayer(new CubeDeformation(0.5f), EquipmentSlot.LEGS),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.FEET));
    }

    private static LayerDefinition armorLayer(CubeDeformation f, EquipmentSlot slot) {
        MeshDefinition mesh = createMesh(f);
        Set<String> parts = ADULT_ARMOR_PARTS_PER_SLOT.get(slot);
        if (slot == EquipmentSlot.HEAD) {
            mesh.getRoot().retainPartsAndChildren(parts);
        } else {
            mesh.getRoot().retainExactParts(parts);
        }
        return LayerDefinition.create(mesh, 64, 32);
    }
}
