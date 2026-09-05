package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrownTridentEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * A thrown LOTR trident: its own item sprite, pointed the way it is going.
 *
 * <p>Vanilla's ThrownTridentRenderer draws a TridentModel with the vanilla
 * trident's texture baked in, which is exactly the problem this exists to
 * solve. There is no LOTR trident entity texture to draw instead -- the
 * original's trident was a polearm that never left your hand -- so the sprite
 * is the answer: the same flat item model the hand shows, turned to lie along
 * the flight path.
 *
 * <p>The matrix is LOTRRenderThrowingAxe's stuck-in-the-ground case, which is
 * the one that points a sprite along its heading: yaw to face the way it flew,
 * pitch to tip it, then the fixed roll that takes a sprite's corner-to-corner
 * diagonal onto that line.
 */
public class LOTRThrownTridentRenderer
        extends EntityRenderer<LOTRThrownTridentEntity, LOTRThrowingAxeRenderState> {

    /** GROUND halves the model; two puts it back at a full block. */
    private static final float SCALE = 2.0f;

    /**
     * The roll that lays the sprite along its line of flight, point first.
     *
     * <p>Derived rather than guessed, because guessing at it cost three goes.
     * ArrowRenderer sets the frame with exactly the two rotations used below --
     * {@code YP(yRot - 90)} then {@code ZP(xRot)} -- and ArrowModel's shaft is
     * {@code addBox(-12, -2, 0, 16, 4, 0)} with the fletching parented at
     * {@code x = -11}, so the feathers sit at -X and the head at +X: in that
     * frame FORWARD IS +X.
     *
     * <p>An item/generated sprite lies in the XY plane with image column 0 at
     * model x 0 and image ROW 0 at model y 1, so the trident's head -- top right
     * of the sprite -- sits at +X+Y, forty-five degrees round from forward.
     * Bringing it to +X is therefore a roll of MINUS forty-five.
     *
     * <p>-135 pointed it at the ground and 135 pointed it tail first. The second
     * of those was my own doing: -45 was already correct, and the report that
     * followed it ("thrown, it is held in the hand backwards -- it flies in the
     * right direction though") was about the HAND, not the projectile. The hand
     * is fixed in the item model instead; this stays where the arithmetic puts it.
     */
    private static final float SPRITE_ROLL = -45.0f;

    private final ItemModelResolver items;

    public LOTRThrownTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public LOTRThrowingAxeRenderState createRenderState() {
        return new LOTRThrowingAxeRenderState();
    }

    @Override
    public void extractRenderState(LOTRThrownTridentEntity trident,
            LOTRThrowingAxeRenderState state, float partialTick) {
        super.extractRenderState(trident, state, partialTick);
        state.yaw = trident.getYRot(partialTick);
        state.pitch = trident.getXRot(partialTick);
        state.shake = trident.shakeTime - partialTick;
        state.stuck = true;
        state.spin = 0.0f;
        this.items.updateForNonLiving(state.axe, trident.getPickupItemStackOrigin(),
                ItemDisplayContext.GROUND, trident);
    }

    @Override
    public void submit(LOTRThrowingAxeRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(SPRITE_ROLL));
        poseStack.scale(SCALE, SCALE, SCALE);
        state.axe.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
                state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
