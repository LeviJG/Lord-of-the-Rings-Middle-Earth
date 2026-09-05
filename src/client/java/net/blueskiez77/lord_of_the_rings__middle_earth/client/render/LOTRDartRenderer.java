package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRDartEntity;

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
 * LOTRRenderDart: the dart's own item sprite, pointed the way it is going.
 *
 * <p>The original drew it off the ITEM atlas rather than an entity texture --
 * getEntityTexture returned TextureMap.locationItemsTexture -- so the sprite is
 * the right answer here too. Same matrix as the thrown trident's, and the same
 * roll: a sprite's point is at its top right, so the diagonal has to come round
 * to the line of flight head first.
 *
 * <p>Half the size of a thrown trident, because a dart is a small thing.
 */
public class LOTRDartRenderer
        extends EntityRenderer<LOTRDartEntity, LOTRThrowingAxeRenderState> {

    private static final float SCALE = 1.0f;
    /** See LOTRThrownTridentRenderer: forward is +X, a sprite's point is at +45. */
    private static final float SPRITE_ROLL = -45.0f;

    private final ItemModelResolver items;

    public LOTRDartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public LOTRThrowingAxeRenderState createRenderState() {
        return new LOTRThrowingAxeRenderState();
    }

    @Override
    public void extractRenderState(LOTRDartEntity dart, LOTRThrowingAxeRenderState state,
            float partialTick) {
        super.extractRenderState(dart, state, partialTick);
        state.yaw = dart.getYRot(partialTick);
        state.pitch = dart.getXRot(partialTick);
        state.stuck = true;
        state.spin = 0.0f;
        this.items.updateForNonLiving(state.axe, dart.getPickupItemStackOrigin(),
                ItemDisplayContext.GROUND, dart);
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
