package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentBarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAlignmentBonusEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBonusMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderAlignmentBonus: the change in the alignment the player is
 * viewing (or, failing that, the one that matters most), with the name of
 * what earned it, floating where it was earned and fading in its last
 * quarter; a smaller line naming the faction if it is not the one viewed.
 *
 * <p>NOT ported yet: the conquest line's value, with conquest (D14); it is
 * shown whenever a conquest bonus arrives, which none yet does.
 */
public class LOTRAlignmentBonusRenderer extends EntityRenderer<LOTRAlignmentBonusEntity, LOTRAlignmentBonusRenderer.State> {

    private static final float SCALE = 0.025f;

    public static class State extends EntityRenderState {
        @Nullable List<Line> lines;
        float alpha;
    }

    /** One row: an optional icon (u, v) and bordered text in a colour, at a scale. */
    record Line(int iconU, int iconV, boolean icon, FormattedCharSequence text, int colour, int y, float scale) {
    }

    public LOTRAlignmentBonusRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRAlignmentBonusEntity bonus, State state, float partialTick) {
        super.extractRenderState(bonus, state, partialTick);
        state.lines = null;
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        LOTRFaction viewingFaction = LOTRViewingFaction.getViewingFaction(player);
        LOTRFaction mainFaction = bonus.mainFaction;
        LOTRAlignmentBonusMap factionBonusMap = bonus.factionBonusMap;
        LOTRFaction renderFaction = null;
        boolean showConquest = false;
        if (bonus.conquestBonus > 0.0f && LOTRPlayerAlignments.isPledgedTo(player, viewingFaction)) {
            renderFaction = viewingFaction;
            showConquest = true;
        } else if (bonus.conquestBonus < 0.0f && (viewingFaction == mainFaction
                || LOTRPlayerAlignments.isPledgedTo(player, viewingFaction))) {
            renderFaction = viewingFaction;
            showConquest = true;
        } else if (!factionBonusMap.isEmpty()) {
            if (factionBonusMap.containsKey(viewingFaction)) {
                renderFaction = viewingFaction;
            } else if (factionBonusMap.size() == 1 && mainFaction.isPlayableAlignmentFaction()) {
                renderFaction = mainFaction;
            } else if (mainFaction.isPlayableAlignmentFaction() && bonus.prevMainAlignment >= 0.0f
                    && factionBonusMap.getOrDefault(mainFaction, 0.0f) < 0.0f) {
                renderFaction = mainFaction;
            } else {
                for (Map.Entry<LOTRFaction, Float> entry : factionBonusMap.entrySet()) {
                    LOTRFaction faction = entry.getKey();
                    if (!faction.isPlayableAlignmentFaction() || entry.getValue() <= 0.0f) {
                        continue;
                    }
                    if (renderFaction != null && LOTRPlayerAlignments.getAlignment(player, faction)
                            <= LOTRPlayerAlignments.getAlignment(player, renderFaction)) {
                        continue;
                    }
                    renderFaction = faction;
                }
                if (renderFaction == null) {
                    if (mainFaction.isPlayableAlignmentFaction() && factionBonusMap.getOrDefault(mainFaction, 0.0f) < 0.0f) {
                        renderFaction = mainFaction;
                    } else {
                        for (Map.Entry<LOTRFaction, Float> entry : factionBonusMap.entrySet()) {
                            LOTRFaction faction = entry.getKey();
                            if (!faction.isPlayableAlignmentFaction() || entry.getValue() >= 0.0f) {
                                continue;
                            }
                            if (renderFaction != null && LOTRPlayerAlignments.getAlignment(player, faction)
                                    <= LOTRPlayerAlignments.getAlignment(player, renderFaction)) {
                                continue;
                            }
                            renderFaction = faction;
                        }
                    }
                }
            }
        }
        if (renderFaction == null) {
            return;
        }
        float alignBonus = factionBonusMap.getOrDefault(renderFaction, 0.0f);
        boolean showAlign = alignBonus != 0.0f;
        float conq = bonus.conquestBonus;
        if (!showAlign && !showConquest) {
            return;
        }
        boolean isViewingFaction = renderFaction == viewingFaction;
        boolean showTitle = showAlign || !bonus.isHiredKill;
        float particleHealth = (float) bonus.particleAge / bonus.particleMaxAge;
        state.alpha = particleHealth < 0.75f ? 1.0f : (1.0f - particleHealth) / 0.25f;
        Font fr = Minecraft.getInstance().font;
        String strAlign = LOTRAlignmentValues.formatAlignForDisplay(alignBonus);
        String strConq = LOTRAlignmentValues.formatConqForDisplay(conq, true);
        boolean negativeConq = conq < 0.0f;
        float scale = 1.0f;
        Component align = Component.literal(strAlign);
        if (!isViewingFaction) {
            scale = 0.5f;
            align = Component.literal(strAlign + " (").append(renderFaction.factionName()).append("...)");
        }
        List<Line> lines = new ArrayList<>();
        int y = -16;
        if (showAlign) {
            lines.add(new Line(0, 36, true, align.getVisualOrderText(), 16772620, y, scale));
            y += 14;
        }
        if (showTitle) {
            int colour = showAlign ? 16772620 : negativeConq ? 16773846 : 14833677;
            lines.add(new Line(0, 0, false, Component.literal(bonus.name).getVisualOrderText(), colour, y, scale));
            y += 16;
        }
        if (showConquest) {
            lines.add(new Line(negativeConq ? 16 : 0, 228, true, Component.literal(strConq).getVisualOrderText(),
                    negativeConq ? 16773846 : 14833677, y, scale));
        }
        state.lines = lines;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.lines == null) {
            return;
        }
        Font fr = Minecraft.getInstance().font;
        int alpha = Mth.clamp((int) (state.alpha * 255.0f), 4, 255) << 24;
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        poseStack.scale(SCALE, -SCALE, SCALE);
        for (Line line : state.lines) {
            poseStack.pushPose();
            poseStack.scale(line.scale(), line.scale(), 1.0f);
            int width = fr.width(line.text());
            int x = line.icon() ? -Mth.floor((width + 18) / 2.0) : -Mth.floor(width / 2.0);
            if (line.icon()) {
                int ix = x;
                int iy = line.y() - 5;
                int iu = line.iconU();
                int iv = line.iconV();
                int colour = alpha | 0xFFFFFF;
                collector.submitCustomGeometry(poseStack, RenderTypes.textSeeThrough(LOTRAlignmentBarRenderer.ALIGNMENT),
                        (pose, vc) -> {
                            float u0 = iu / 256.0f;
                            float v0 = iv / 256.0f;
                            float u1 = (iu + 16) / 256.0f;
                            float v1 = (iv + 16) / 256.0f;
                            vc.addVertex(pose, ix, iy + 16, 0.0f).setColor(colour).setUv(u0, v1).setLight(0xF000F0);
                            vc.addVertex(pose, ix + 16, iy + 16, 0.0f).setColor(colour).setUv(u1, v1).setLight(0xF000F0);
                            vc.addVertex(pose, ix + 16, iy, 0.0f).setColor(colour).setUv(u1, v0).setLight(0xF000F0);
                            vc.addVertex(pose, ix, iy, 0.0f).setColor(colour).setUv(u0, v0).setLight(0xF000F0);
                        });
                x += 18;
            }
            // drawBorderedText: black on every side, then the colour.
            int[][] offsets = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
            for (int[] o : offsets) {
                collector.submitText(poseStack, x + o[0], line.y() + o[1], line.text(), false,
                        Font.DisplayMode.SEE_THROUGH, 0xF000F0, alpha, 0, 0);
            }
            collector.submitText(poseStack, x, line.y(), line.text(), false, Font.DisplayMode.SEE_THROUGH, 0xF000F0,
                    line.colour() | alpha, 0, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
