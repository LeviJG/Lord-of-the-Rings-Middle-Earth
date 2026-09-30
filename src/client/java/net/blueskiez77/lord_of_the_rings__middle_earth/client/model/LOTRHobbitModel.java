package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

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
 * LOTRModelHobbit: a biped with its head four pixels and everything else 4.8
 * pixels lower, a woman's chest, bare feet turned out by ten degrees, and a
 * tall hood of hair (the 64x64 skin's lower half). Its body and limbs are
 * drawn at five sixths height; a child has a three-quarter head on a
 * half-size body.
 *
 * <p>1.7.10 scaled those parts before placing and turning them; a ModelPart
 * scales after, so a swinging limb is shortened along itself rather than
 * vertically -- the rest pose is exact, the swing very nearly so.
 */
public class LOTRHobbitModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    private static final float LIMB_Y_SCALE = 0.8333333f;

    private final ModelPart chest;
    private final ModelPart[] limbs;
    /** The arms' placing before the scaling, for the held items. */
    private final float[][] armPose = new float[2][3];

    public LOTRHobbitModel(ModelPart root) {
        super(root);
        this.chest = this.body.getChild("chest");
        this.limbs = new ModelPart[]{this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg};
    }

    /** LOTRModelHobbit(f): the body (64x64 skin); with {@code armor}, the 64x32 armour shape. */
    public static MeshDefinition createMesh(CubeDeformation f, boolean armor) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, f), PartPose.offset(0.0f, 4.0f, 0.0f));
        // The headwear's own rotationPointY of 2 + 4 was overwritten by
        // setRotationAngles on the first frame, so it sits with the head.
        head.addOrReplaceChild("hat", armor
                ? CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, f.extend(0.5f))
                : CubeListBuilder.create().texOffs(0, 32).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 12.0f, 8.0f, f.extend(0.5f)),
                PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16)
                .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, f), PartPose.offset(0.0f, 4.8f, 0.0f));
        body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-3.0f, 2.0f, -4.0f, 6.0f, 3.0f, 2.0f, f), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16)
                .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, f), PartPose.offset(-5.0f, 6.8f, 0.0f));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror()
                .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, f), PartPose.offset(5.0f, 6.8f, 0.0f));
        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, f), PartPose.offset(-1.9f, 16.8f, 0.0f));
        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror()
                .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, f), PartPose.offset(1.9f, 16.8f, 0.0f));
        if (!armor) {
            rightLeg.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(40, 32)
                    .addBox(-2.0f, 10.0f, -5.0f, 4.0f, 2.0f, 3.0f, f), PartPose.rotation(0.0f, 0.17453292f, 0.0f));
            leftLeg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(40, 32)
                    .addBox(-2.0f, 10.0f, -5.0f, 4.0f, 2.0f, 3.0f, f), PartPose.rotation(0.0f, -0.17453292f, 0.0f));
        }
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE, false), 64, 64);
    }

    /** The outfit pass (the marriage ring): LOTRModelHobbit(0.5, 64, 64). */
    public static LayerDefinition createOutfitLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.5f), false), 64, 64);
    }

    /** func_82421_b: LOTRModelHobbit(1.0) outside, (0.5) for leggings. */
    public static ArmorModelSet<LayerDefinition> createArmorLayers() {
        return new ArmorModelSet<>(
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.HEAD),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.CHEST),
                armorLayer(new CubeDeformation(0.5f), EquipmentSlot.LEGS),
                armorLayer(new CubeDeformation(1.0f), EquipmentSlot.FEET));
    }

    private static LayerDefinition armorLayer(CubeDeformation f, EquipmentSlot slot) {
        MeshDefinition mesh = createMesh(f, true);
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
        // render(): the head at 3/4 raised 15 pixels for a child; the body and
        // limbs at 5/6 height, and for a child at half size raised 24 pixels.
        float headScale = state.isBaby ? 0.75f : 1.0f;
        this.head.y = state.isBaby ? 0.75f * (15.0f + this.head.y) : this.head.y;
        this.head.x *= headScale;
        this.head.z *= headScale;
        this.head.xScale = this.head.yScale = this.head.zScale = headScale;
        float bodyScale = state.isBaby ? 0.5f : 1.0f;
        for (int i = 0; i < this.limbs.length; ++i) {
            ModelPart limb = this.limbs[i];
            float y = limb.y * LIMB_Y_SCALE;
            limb.x *= bodyScale;
            limb.y = state.isBaby ? 12.0f + y * bodyScale : y;
            limb.z *= bodyScale;
            limb.xScale = bodyScale;
            limb.yScale = LIMB_Y_SCALE * bodyScale;
            limb.zScale = bodyScale;
        }
    }

    /**
     * RenderBiped.renderEquippedItems: the hand is found from the arm's own
     * placing, unscaled -- the held item was drawn apart from the model's
     * scaling -- and held 0.1125 higher than a man's (getHeldItemYTranslation
     * 0.075 against 0.1875). A child's hand is found as 1.7.10 did, at half
     * size 0.625 up and tipped 20 degrees.
     */
    @Override
    public void translateToHand(HumanoidRenderState state, HumanoidArm arm,
                                PoseStack poseStack) {
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
        poseStack.translate(0.0f, -0.1125f, 0.0f);
    }
}
