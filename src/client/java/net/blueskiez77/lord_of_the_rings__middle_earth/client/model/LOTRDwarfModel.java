package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

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
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;

/**
 * LOTRModelDwarf: a biped a quarter again as broad -- the body and legs
 * widened by 1.25, the arms set a pixel further out, the legs a quarter pixel
 * -- with a woman's chest and, on a 64x64 texture, a beard-and-hair hood
 * (the skin's lower half, 8x12x8 over the head). A child has a three-quarter
 * head on a half-size body, without the widening.
 *
 * <p>1.7.10 widened the body before placing and turning it; a ModelPart
 * scales after, so the body is widened along itself as it twists in a swing
 * rather than across the world -- the rest pose is exact, as is every leg
 * swing (a turn about the widened axis).
 */
public class LOTRDwarfModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    private static final float WIDEN = 1.25f;

    private final ModelPart chest;
    private final ModelPart[] limbs;
    /** The arms' placing before the widening, for the held items. */
    private final float[][] armPose = new float[2][3];

    public LOTRDwarfModel(ModelPart root) {
        super(root);
        this.chest = this.body.getChild("chest");
        this.limbs = new ModelPart[]{this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg};
    }

    /** LOTRModelDwarf(f, width, height): the beard hood only on a 64-high texture. */
    public static MeshDefinition createMesh(CubeDeformation f, boolean beard) {
        MeshDefinition mesh = HumanoidModel.createMesh(f, 0.0f);
        PartDefinition root = mesh.getRoot();
        if (beard) {
            // The headwear's own rotationPointY of 2 was overwritten by
            // setRotationAngles on the first frame, so it sits with the head.
            root.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 12.0f, 8.0f, f.extend(0.5f)), PartPose.ZERO);
        }
        root.getChild("body").addOrReplaceChild("chest", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-3.0f, 2.0f, -4.0f, 6.0f, 3.0f, 2.0f, f), PartPose.ZERO);
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE, true), 64, 64);
    }

    /** standardRenderPassModel, for the ring and the aprons: LOTRModelDwarf(0.5, 64, 64). */
    public static LayerDefinition createOutfitLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.5f), true), 64, 64);
    }

    /** LOTRRenderDwarfCommander's cloak: LOTRModelDwarf(1.5), a 64x32 texture. */
    public static LayerDefinition createCloakLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(1.5f), false), 64, 32);
    }

    /** func_82421_b: LOTRModelDwarf(1.0) outside, (0.5) for leggings, 64x32. */
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
        for (int i = 0; i < 2; ++i) {
            ModelPart arm = i == 0 ? this.rightArm : this.leftArm;
            this.armPose[i][0] = arm.x;
            this.armPose[i][1] = arm.y;
            this.armPose[i][2] = arm.z;
        }
        if (state.isBaby) {
            // render(): the head at 3/4 raised 16 pixels, the body and limbs
            // at half size raised 24 pixels.
            this.head.y = 0.75f * (16.0f + this.head.y);
            this.head.x *= 0.75f;
            this.head.z *= 0.75f;
            this.head.xScale = this.head.yScale = this.head.zScale = 0.75f;
            for (ModelPart limb : this.limbs) {
                limb.x *= 0.5f;
                limb.y = 0.5f * (24.0f + limb.y);
                limb.z *= 0.5f;
                limb.xScale = limb.yScale = limb.zScale = 0.5f;
            }
        } else {
            this.body.x *= WIDEN;
            this.body.xScale = WIDEN;
            this.rightArm.x -= 1.0f;
            this.leftArm.x += 1.0f;
            this.rightLeg.x = -0.25f + WIDEN * this.rightLeg.x;
            this.leftLeg.x = 0.25f + WIDEN * this.leftLeg.x;
            this.rightLeg.xScale = WIDEN;
            this.leftLeg.xScale = WIDEN;
        }
    }

    /**
     * RenderBiped.renderEquippedItems: the hand is found from the arm's own
     * placing -- the held item was drawn apart from render()'s shifting --
     * and held 0.0625 higher than a man's (getHeldItemYTranslation 0.125
     * against 0.1875). A child's hand is found as 1.7.10 did, at half size
     * 0.625 up and tipped 20 degrees.
     */
    @Override
    public void translateToHand(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack) {
        ModelPart part = getArm(arm);
        float x = part.x;
        float y = part.y;
        float z = part.z;
        float xs = part.xScale;
        float ys = part.yScale;
        float zs = part.zScale;
        float[] pose = this.armPose[arm == HumanoidArm.LEFT ? 1 : 0];
        if (state.isBaby) {
            poseStack.translate(0.0f, 0.625f, 0.0f);
            poseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }
        part.x = pose[0];
        part.y = pose[1];
        part.z = pose[2];
        part.xScale = part.yScale = part.zScale = 1.0f;
        this.root.translateAndRotate(poseStack);
        part.translateAndRotate(poseStack);
        part.x = x;
        part.y = y;
        part.z = z;
        part.xScale = xs;
        part.yScale = ys;
        part.zScale = zs;
        poseStack.translate(0.0f, -0.0625f, 0.0f);
    }
}
