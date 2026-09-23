package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCarvedSignBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderSignCarved and LOTRRenderSignCarvedIthildin: the lines of a carved
 * sign, laid on the face of the block behind it.
 *
 * <p>The matrix is the original's wall-sign chain: centre, turn to the face,
 * back to the block's surface, down to the text origin, and scale by 2/3 x
 * 1/60 with y flipped. Eight lines ten pixels apart, centred on the face.
 *
 * <p>Plain signs take the contrast of the carved block's own colour. Ithildin
 * signs are white, full bright, and as opaque as the glow is strong -- nothing
 * at all in daylight or with nobody near.
 */
public class LOTRCarvedSignRenderer implements BlockEntityRenderer<LOTRCarvedSignBlockEntity, LOTRCarvedSignRenderState> {
    private static final float SCALE = 0.6666667f;
    private static final float TEXT_SCALE = 0.016666668f * SCALE;

    private final Font font;

    public LOTRCarvedSignRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
    }

    /** getMaxRenderDistanceSquared: 1600, forty blocks. */
    @Override
    public int getViewDistance() {
        return 40;
    }

    @Override
    public LOTRCarvedSignRenderState createRenderState() {
        return new LOTRCarvedSignRenderState();
    }

    /** Direction.facingToDirection[side] * 90: south 0, west 90, north 180, east 270. */
    private static float rotationFor(Direction facing) {
        return switch (facing) {
            case WEST -> 90.0f;
            case NORTH -> 180.0f;
            case EAST -> 270.0f;
            default -> 0.0f;
        };
    }

    @Override
    public void extractRenderState(LOTRCarvedSignBlockEntity sign, LOTRCarvedSignRenderState state, float partialTick,
            Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(sign, state, crumbling);
        Direction facing = sign.getBlockState().hasProperty(LOTRCarvedSignBlock.FACING)
                ? sign.getBlockState().getValue(LOTRCarvedSignBlock.FACING) : Direction.NORTH;
        state.rotation = rotationFor(facing);
        state.lines = sign.copyText();
        if (sign.isIthildin()) {
            // getAlphaInt clamps to at least 4, and setupGlow's alpha test
            // discarded anything under 0.02 -- so a dark sign draws nothing.
            int alpha = Mth.clamp((int) (sign.getGlowBrightness(partialTick) * 255.0f), 4, 255);
            state.visible = alpha > 5;
            state.color = alpha << 24 | 0xFFFFFF;
            state.light = LightCoordsUtil.FULL_BRIGHT;
        } else {
            state.visible = sign.getLevel() != null;
            state.color = state.visible
                    ? LOTRSignColors.contrastColor(sign.getLevel().getBlockState(sign.getBlockPos().relative(facing.getOpposite())))
                    : 0;
            state.light = state.lightCoords;
        }
    }

    @Override
    public void submit(LOTRCarvedSignRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        if (!state.visible) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.75f * SCALE, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation));
        poseStack.translate(0.0f, -0.3125f, -0.4375f);
        poseStack.translate(0.0f, 0.5f * SCALE, -0.09f * SCALE);
        poseStack.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);
        int lineHeight = font.lineHeight + 1;
        int lines = state.lines.length;
        int lineBase = lines > 4 ? -((lines - 1) * lineHeight) / 2 : -lines * 5;
        for (int l = 0; l < lines; ++l) {
            FormattedCharSequence text = FormattedCharSequence.forward(state.lines[l], Style.EMPTY);
            float x = -font.width(text) / 2.0f;
            collector.submitText(poseStack, x, lineBase + l * lineHeight, text, false, Font.DisplayMode.POLYGON_OFFSET,
                    state.light, state.color, 0, 0);
        }
        poseStack.popPose();
    }
}
