package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * LOTRModelBiped: the NPC biped's own posing. Much as 1.7.10's ModelBiped,
 * plus legs turned out by five degrees, a drunkard's lolling head (and a
 * raised drinking arm), and every rest position taken from the model itself,
 * so a biped with shifted parts (the hobbit's) keeps them.
 * An elf bowing (LOTREntityElf.getBowingAmount) bends it all forward.
 */
public class LOTRBipedModel<S extends LOTRNPCRenderState> extends HumanoidModel<S> {

    private final float baseBodyRotateX;
    private final float baseArmX;
    private final float baseArmY;
    private final float baseArmZ;
    private final float baseLegY;
    private final float baseLegZ;
    private final float baseHeadY;
    private final float baseHeadZ;
    private final float baseBodyY;
    private final float baseBodyZ;

    public LOTRBipedModel(ModelPart root) {
        super(root);
        // setupModelBiped
        this.baseBodyRotateX = this.body.xRot;
        this.baseArmX = Math.abs(this.rightArm.x);
        this.baseArmY = this.rightArm.y;
        this.baseArmZ = this.rightArm.z;
        this.baseLegY = this.rightLeg.y;
        this.baseLegZ = this.rightLeg.z;
        this.baseHeadY = this.head.y;
        this.baseHeadZ = this.head.z;
        this.baseBodyY = this.body.y;
        this.baseBodyZ = this.body.z;
    }

    /** setRotationAngles. */
    @Override
    public void setupAnim(S state) {
        resetPose();
        this.hat.visible = state.renderHair;
        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;
        float f2 = state.ageInTicks;
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.rightArm.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 2.0f * f1 * 0.5f;
        this.leftArm.xRot = Mth.cos(f * 0.6662f) * 2.0f * f1 * 0.5f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos(f * 0.6662f) * 1.4f * f1;
        this.leftLeg.xRot = Mth.cos(f * 0.6662f + Mth.PI) * 1.4f * f1;
        if (state.isNPC) {
            this.rightLeg.yRot = 0.08726646f;
            this.leftLeg.yRot = -0.08726646f;
        }
        if (state.isPassenger) {
            this.rightArm.xRot -= 0.62831855f;
            this.leftArm.xRot -= 0.62831855f;
            this.rightLeg.xRot = -1.2566371f;
            this.leftLeg.xRot = -1.2566371f;
            this.rightLeg.yRot = 0.31415927f;
            this.leftLeg.yRot = -0.31415927f;
        }
        if (state.heldItemLeft != 0) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.31415927f * state.heldItemLeft;
        }
        if (state.heldItemRight != 0) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.31415927f * state.heldItemRight;
        }
        this.rightArm.yRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        // onGround > -9990 always held for a living model: the swing pose runs every frame.
        {
            float swing = state.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(swing) * Mth.TWO_PI) * 0.2f;
            this.rightArm.z = Mth.sin(this.body.yRot) * this.baseArmX;
            this.rightArm.x = -Mth.cos(this.body.yRot) * this.baseArmX;
            this.leftArm.z = -Mth.sin(this.body.yRot) * this.baseArmX;
            this.leftArm.x = Mth.cos(this.body.yRot) * this.baseArmX;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            float f6 = 1.0f - swing;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0f - f6;
            float f7 = Mth.sin(f6 * Mth.PI);
            float f8 = Mth.sin(swing * Mth.PI) * -(this.head.xRot - 0.7f) * 0.75f;
            this.rightArm.xRot = (float) (this.rightArm.xRot - (f7 * 1.2 + f8));
            this.rightArm.yRot += this.body.yRot * 2.0f;
            this.rightArm.zRot = Mth.sin(swing * Mth.PI) * -0.4f;
        }
        if (state.isCrouching) {
            this.body.xRot = this.baseBodyRotateX + 0.5f;
            this.rightArm.xRot += 0.4f;
            this.leftArm.xRot += 0.4f;
            this.rightLeg.z = this.baseLegZ + 4.0f;
            this.leftLeg.z = this.baseLegZ + 4.0f;
            this.rightLeg.y = this.baseLegY - 3.0f;
            this.leftLeg.y = this.baseLegY - 3.0f;
            this.head.y = this.baseHeadY + 1.0f;
        } else {
            this.body.xRot = this.baseBodyRotateX;
            this.rightLeg.z = this.baseLegZ + 0.1f;
            this.leftLeg.z = this.baseLegZ + 0.1f;
            this.rightLeg.y = this.baseLegY;
            this.leftLeg.y = this.baseLegY;
            this.head.y = this.baseHeadY;
        }
        this.rightArm.zRot += Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.leftArm.zRot -= Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
        this.rightArm.xRot += Mth.sin(f2 * 0.067f) * 0.05f;
        this.leftArm.xRot -= Mth.sin(f2 * 0.067f) * 0.05f;
        if (state.aimedBow) {
            this.rightArm.zRot = 0.0f;
            this.leftArm.zRot = 0.0f;
            this.rightArm.yRot = -0.1f + this.head.yRot;
            this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
            this.rightArm.xRot = -Mth.HALF_PI + this.head.xRot;
            this.leftArm.xRot = -Mth.HALF_PI + this.head.xRot;
            this.rightArm.zRot += Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
            this.leftArm.zRot -= Mth.cos(f2 * 0.09f) * 0.05f + 0.05f;
            this.rightArm.xRot += Mth.sin(f2 * 0.067f) * 0.05f;
            this.leftArm.xRot -= Mth.sin(f2 * 0.067f) * 0.05f;
        }
        if (state.drunkard) {
            float f62 = f2 / 80.0f;
            float f72 = (f2 + 40.0f) / 80.0f;
            this.head.xRot += Mth.sin(f62 * Mth.TWO_PI) * 0.5f;
            this.head.yRot += Mth.sin(f72 * Mth.TWO_PI) * 0.5f;
            if (state.holdingItem) {
                this.rightArm.xRot = -1.0471976f;
            }
        }
        if (state.bowAmount != 0.0f) {
            // An elf bowing: head, body and arms pitch forward up to 30 degrees,
            // lowered and drawn forward about the hips. The original took the
            // head's forward shift and the arms' from their Y rests, not Z.
            float bowAmountRad = (float) Math.toRadians(state.bowAmount * 30.0f);
            float bowCos = Mth.cos(bowAmountRad);
            float bowSin = Mth.sin(bowAmountRad);
            this.head.y = this.baseHeadY + 12.0f * (1.0f - bowCos);
            this.head.z = this.baseHeadY - 12.0f * bowSin;
            this.body.y = this.baseBodyY + 12.0f * (1.0f - bowCos);
            this.body.z = this.baseBodyZ - 12.0f * bowSin;
            this.rightArm.y = this.baseArmY + 10.0f * (1.0f - bowCos);
            this.rightArm.z = this.baseArmY - 12.0f * bowSin;
            this.leftArm.y = this.rightArm.y;
            this.leftArm.z = this.rightArm.z;
            this.head.xRot = bowAmountRad;
            this.body.xRot = bowAmountRad;
            this.rightArm.xRot = bowAmountRad;
            this.leftArm.xRot = bowAmountRad;
            return;
        }
        if (!state.isCrouching) {
            this.head.y = this.baseHeadY;
            this.head.z = this.baseHeadZ;
        }
        this.body.y = this.baseBodyY;
        this.body.z = this.baseBodyZ;
        this.rightArm.y = this.baseArmY;
        this.rightArm.z = this.baseArmZ;
        this.leftArm.y = this.baseArmY;
        this.leftArm.z = this.baseArmZ;
    }
}
