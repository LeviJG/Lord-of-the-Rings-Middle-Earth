package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * LOTRRenderThrowingAxe: the axe's own sprite, tumbling end over end.
 *
 * <p>The original drew the item's icon as a flat quad through
 * ItemRenderer.renderItemIn2D at 0.0625 a pixel -- a 16x16 sprite exactly one
 * block across -- and turned it about Z by axeRotation, a full revolution every
 * ten ticks. renderItemIn2D is gone, so the sprite comes from the item's own
 * model instead; GROUND is the display context that centres a model on the
 * origin, and it halves the size, so the scale below puts the axe back at the
 * one block the original drew it at.
 *
 * <p>The rest of the matrix is the original's: yaw to face the way it is
 * travelling, then either the pitch it stuck at or the tumble, then the seven
 * ticks of wobble after it lands, then a fixed roll because an item
 * sprite runs corner to corner with its handle at the bottom left.
 */
public class LOTRThrowingAxeRenderer
        extends EntityRenderer<LOTRThrowingAxeEntity, LOTRThrowingAxeRenderState> {

    /** GROUND halves the model; two puts it back at renderItemIn2D's one block. */
    private static final float SCALE = 2.0f;

    /**
     * The angle the sprite's own diagonal has to be rolled through to lie along
     * the line of flight, head first. See LOTRThrownTridentRenderer for where
     * the figure comes from -- ArrowModel's own geometry -- and for the two
     * wrong answers it took to get there.
     */
    private static final float SPRITE_ROLL = -45.0f;

    private final ItemModelResolver items;

    public LOTRThrowingAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public LOTRThrowingAxeRenderState createRenderState() {
        return new LOTRThrowingAxeRenderState();
    }

    @Override
    public void extractRenderState(LOTRThrowingAxeEntity axe, LOTRThrowingAxeRenderState state,
            float partialTick) {
        super.extractRenderState(axe, state, partialTick);
        state.yaw = axe.getYRot(partialTick);
        state.pitch = axe.getXRot(partialTick);
        state.shake = axe.shakeTime - partialTick;
        state.stuck = axe.isStuck();
        // axeRotation / 9 * 360 in the original, which is a turn every ten ticks
        // with the partial tick smoothing it out.
        state.spin = (axe.getSpin() + partialTick)
                / LOTRThrowingAxeEntity.SPIN_PERIOD * 360.0f;
        this.items.updateForNonLiving(state.axe, axe.getPickupItemStackOrigin(),
                ItemDisplayContext.GROUND, axe);
    }

    @Override
    public void submit(LOTRThrowingAxeRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw - 90.0f));
        if (state.stuck) {
            // Left where it landed, lying along the line it came in on.
            poseStack.mulPose(Axis.ZP.rotationDegrees(state.pitch));
        } else {
            poseStack.mulPose(Axis.ZN.rotationDegrees(state.spin));
        }

        // The shudder a stuck axe gives for its first seven ticks.
        if (state.shake > 0.0f) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(
                    -Mth.sin(state.shake * 3.0f) * state.shake));
        }

        poseStack.mulPose(Axis.ZP.rotationDegrees(SPRITE_ROLL));
        poseStack.scale(SCALE, SCALE, SCALE);

        state.axe.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
                state.outlineColor);

        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }
}
