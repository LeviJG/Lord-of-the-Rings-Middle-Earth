package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRShieldRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiShields' figure: the player's skin on a bare model, tilted towards the viewer and turned as
 * dragged, with the shield being looked at on its left arm.
 */
public class LOTRShieldPreviewRenderer extends PictureInPictureRenderer<LOTRShieldPreviewRenderer.State> {

    /**
     * The figure, its origin (the model's) {@code originBelowTop} GUI pixels down from the top of the
     * area it is drawn in, {@code scale} GUI pixels to the model's unit.
     */
    public record State(Model.Simple model, Identifier skin, LOTRShields shield, float rotation, float originBelowTop,
                        int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea,
                        @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
    }

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    @Override
    protected void renderToTexture(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.PLAYER_SKIN);
        int light = 15728880;
        // Up from the area's foot to the model's origin, then the original's (-55, 55, 55) scale --
        // the picture's own is (55, 55, -55), a half turn short -- its tilt and its turn.
        poseStack.translate(0.0f, -(state.y1() - state.y0() - state.originBelowTop()) / state.scale(), 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation()));
        collector.submitModel(state.model(), Unit.INSTANCE, poseStack, state.skin(), light, OverlayTexture.NO_OVERLAY, 0, null);
        LOTRShieldRenderer.submit(state.shield(), poseStack, collector, light, state.model().root().getChild("body"),
                state.model().root().getChild("left_arm"), false, false, 255);
    }

    @Override
    protected String getTextureLabel() {
        return "lotr shield preview";
    }
}
