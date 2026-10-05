package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentBarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBipedRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRGollumRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHuornRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCSpeechRendering;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRSpeechClient;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRTrollRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRNPCRendering.renderHiredIcon / renderNPCHealthBar / renderHealthBar and
 * LOTRRenderPlayer's alignment: over one's own hired unit, its held item (or,
 * sneaking, its level) and its company's name, and its health bar and its
 * mount's; over Gollum, his master's health bar for him; over another player,
 * their alignment with the faction one is viewing. All seen through walls,
 * within 64 blocks, and not with the HUD hidden. The original drew these
 * from its biped, warg, spider, troll and Huorn renderers (Gollum's health
 * only), each at its own height; so here.
 *
 * <p>NOT ported yet, with fellowships (D14): a fellow's alignment shown even
 * when they hide it, and fellow players' health bars. Every world counts as
 * Middle-earth for the alignment until D10.
 */
public final class LOTROverheadRendering {

    private static final double NAME_TAG_RANGE = LOTRNPCSpeechRendering.NAME_TAG_RANGE;
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final int[] HIRED_COLOURS = {5888860, 12006707};
    private static final int[] HIRED_MOUNT_COLOURS = {6079225, 12006707};

    /** What one entity shows overhead; any part may be absent. */
    public record Overhead(float height, float yOffset, @Nullable ItemStackRenderState icon, int displayLevel,
                           @Nullable String squadron, float health, float mountHealth, boolean healthBar,
                           @Nullable String alignment) {
    }

    private LOTROverheadRendering() {
    }

    public static @Nullable Overhead extract(EntityRenderer<?, ?> renderer, Entity entity, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player viewer = mc.player;
        if (viewer == null || mc.gui.hud.isHidden() || entity.distanceToSqr(viewer) > NAME_TAG_RANGE * NAME_TAG_RANGE) {
            return null;
        }
        if (renderer instanceof AvatarRenderer && entity instanceof Player other) {
            return extractPlayer(other, viewer);
        }
        if (!(entity instanceof LivingEntity living)) {
            return null;
        }
        float offset;
        boolean iconAllowed = true;
        if (renderer instanceof LOTRGollumRenderer) {
            if (!(entity instanceof LOTRGollumEntity gollum) || gollum.getGollumOwner() != viewer) {
                return null;
            }
            offset = 0.5f;
            iconAllowed = false;
        } else if (renderer instanceof LOTRBipedRenderer || renderer instanceof LOTRWargRenderer
                || renderer instanceof LOTRSpiderRenderer) {
            offset = 0.5f;
        } else if (renderer instanceof LOTRTrollRenderer) {
            offset = 1.0f;
        } else if (renderer instanceof LOTRHuornRenderer) {
            offset = 3.5f;
        } else {
            return null;
        }
        if (iconAllowed && !(entity instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.getHiringPlayer() == viewer)) {
            return null;
        }
        // Neither for a mount carrying an NPC.
        if (entity.getFirstPassenger() instanceof LOTRNPCEntity) {
            return null;
        }
        if (entity instanceof LOTRNPCEntity npc && LOTRSpeechClient.getSpeechFor(npc) != null) {
            offset += calcSpeechDisplacement(npc, mc.font);
        }
        ItemStackRenderState icon = null;
        int displayLevel = -1;
        String squadron = null;
        if (iconAllowed && LOTRConfig.hiredUnitIcons) {
            LOTRNPCEntity npc = (LOTRNPCEntity) entity;
            if (npc.hiredNPCInfo.getTask().displayXpLevel) {
                displayLevel = npc.hiredNPCInfo.xpLevel;
            }
            String sq = npc.hiredNPCInfo.getSquadron();
            if (sq != null && !sq.isEmpty()) {
                squadron = sq;
            }
            ItemStack held = living.getMainHandItem();
            if (!held.isEmpty()) {
                icon = new ItemStackRenderState();
                mc.getItemModelResolver().updateForNonLiving(icon, held, ItemDisplayContext.GUI, entity);
            }
            // The level shows in place of the icon while the viewer sneaks.
            if (!viewer.isShiftKeyDown()) {
                displayLevel = -1;
            }
        }
        boolean healthBar = LOTRConfig.hiredUnitHealthBars;
        float health = Math.max(0.0f, living.getHealth() / living.getMaxHealth());
        float mountHealth = entity.getVehicle() instanceof LivingEntity mount
                ? Math.max(0.0f, mount.getHealth() / mount.getMaxHealth()) : -1.0f;
        return new Overhead(entity.getBbHeight(), offset, icon, displayLevel, squadron, health, mountHealth, healthBar, null);
    }

    /** LOTRRenderPlayer.postRender's alignment. */
    private static @Nullable Overhead extractPlayer(Player other, Player viewer) {
        if (!LOTRConfig.displayAlignmentAboveHead || other == viewer || other.isShiftKeyDown() || other.isInvisibleTo(viewer)
                || LOTRPlayerAlignments.getHideAlignment(other)) {
            return null;
        }
        float alignment = LOTRPlayerAlignments.getAlignment(other, LOTRViewingFaction.getViewingFaction(viewer));
        float yOffset = other.isSleeping() ? -1.5f : 0.0f;
        return new Overhead(other.getBbHeight(), yOffset, null, -1, null, 0.0f, -1.0f, false,
                LOTRAlignmentValues.formatAlignForDisplay(alignment));
    }

    /** calcSpeechDisplacement: above what the NPC is saying. */
    private static float calcSpeechDisplacement(LOTRNPCEntity npc, Font fr) {
        LOTRSpeechClient.TimedSpeech speech = LOTRSpeechClient.getSpeechFor(npc);
        if (speech == null) {
            return 0.0f;
        }
        int numLines = fr.split(Component.literal(speech.getSpeech()), 150).size();
        return fr.lineHeight * (3 + numLines) * 0.015f;
    }

    public static void submit(Overhead o, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Font fr = Minecraft.getInstance().font;
        if (o.alignment() != null) {
            submitAlignment(o, fr, poseStack, collector, camera);
            return;
        }
        if (o.icon() != null || o.squadron() != null || o.displayLevel() >= 0) {
            submitHiredIcon(o, fr, poseStack, collector, camera);
        }
        if (o.healthBar()) {
            submitHealthBar(o, poseStack, collector, camera);
        }
    }

    private static void submitAlignment(Overhead o, Font fr, PoseStack poseStack, SubmitNodeCollector collector,
                                        CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, o.height() + 0.6f + o.yOffset(), 0.0f);
        poseStack.mulPose(camera.orientation);
        float scale = 0.025f;
        poseStack.scale(scale, -scale, scale);
        Component sAlign = Component.literal(o.alignment());
        int x = -Mth.floor((fr.width(sAlign) + 18) / 2.0);
        icon(poseStack, collector, x, -19, 0, 36);
        borderedText(poseStack, collector, sAlign.getVisualOrderText(), x + 18, -12, 16772620);
        poseStack.popPose();
    }

    private static void submitHiredIcon(Overhead o, Font fr, PoseStack poseStack, SubmitNodeCollector collector,
                                        CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, o.yOffset() + o.height(), 0.0f);
        poseStack.mulPose(camera.orientation);
        if (o.squadron() != null) {
            poseStack.translate(0.0f, 0.3f, 0.0f);
            poseStack.pushPose();
            int halfWidth = fr.width(o.squadron()) / 2;
            float boxScale = 0.015f;
            poseStack.scale(boxScale, -boxScale, boxScale);
            quad(poseStack, collector, -halfWidth - 1, -9, halfWidth + 1, 0, 0x40000000);
            FormattedCharSequence sq = Component.literal(o.squadron()).getVisualOrderText();
            collector.submitText(poseStack, -halfWidth, -8, sq, false, Font.DisplayMode.SEE_THROUGH, FULL_BRIGHT,
                    553648127, 0, 0);
            collector.submitText(poseStack, -halfWidth, -8, sq, false, Font.DisplayMode.NORMAL, FULL_BRIGHT, -1, 0, 0);
            poseStack.popPose();
        }
        poseStack.translate(0.0f, 0.5f, 0.0f);
        if (o.displayLevel() >= 0) {
            float textScale = 0.03f;
            poseStack.scale(textScale, -textScale, textScale);
            String s = String.valueOf(o.displayLevel());
            borderedText(poseStack, collector, Component.literal(s).getVisualOrderText(), -fr.width(s) / 2, 0, 16733440);
        } else if (o.icon() != null) {
            float itemScale = 0.03f * 16.0f;
            poseStack.scale(itemScale, itemScale, 0.001f);
            o.icon().submit(poseStack, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }

    private static void submitHealthBar(Overhead o, PoseStack poseStack, SubmitNodeCollector collector,
                                        CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, o.yOffset() + o.height() + 0.7f, 0.0f);
        poseStack.mulPose(camera.orientation);
        float f2 = 0.016666666f * 1.6f;
        poseStack.scale(f2, -f2, f2);
        bar(poseStack, collector, 18.5f, o.health(), HIRED_COLOURS);
        if (o.mountHealth() >= 0.0f) {
            bar(poseStack, collector, 23.5f, o.mountHealth(), HIRED_MOUNT_COLOURS);
        }
        poseStack.popPose();
    }

    private static void bar(PoseStack poseStack, SubmitNodeCollector collector, float top, float remaining, int[] colours) {
        quad(poseStack, collector, -19.5f, top, 19.5f, top + 2.5f, 0xFF000000);
        quad(poseStack, collector, -19.0f, top + 0.5f, 19.0f, top + 2.0f, 0xFF000000 | colours[1]);
        quad(poseStack, collector, -19.0f, top + 0.5f, -19.0f + 38.0f * remaining, top + 2.0f, 0xFF000000 | colours[0]);
    }

    private static void quad(PoseStack poseStack, SubmitNodeCollector collector, float x0, float y0, float x1, float y1,
                             int colour) {
        collector.submitCustomGeometry(poseStack, RenderTypes.textBackgroundSeeThrough(), (pose, vc) -> {
            vc.addVertex(pose, x0, y1, 0.0f).setColor(colour).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x1, y1, 0.0f).setColor(colour).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x1, y0, 0.0f).setColor(colour).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x0, y0, 0.0f).setColor(colour).setLight(FULL_BRIGHT);
        });
    }

    private static void icon(PoseStack poseStack, SubmitNodeCollector collector, int x, int y, int u, int v) {
        collector.submitCustomGeometry(poseStack, RenderTypes.textSeeThrough(LOTRAlignmentBarRenderer.ALIGNMENT), (pose, vc) -> {
            float u0 = u / 256.0f;
            float v0 = v / 256.0f;
            float u1 = (u + 16) / 256.0f;
            float v1 = (v + 16) / 256.0f;
            vc.addVertex(pose, x, y + 16, 0.0f).setColor(-1).setUv(u0, v1).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x + 16, y + 16, 0.0f).setColor(-1).setUv(u1, v1).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x + 16, y, 0.0f).setColor(-1).setUv(u1, v0).setLight(FULL_BRIGHT);
            vc.addVertex(pose, x, y, 0.0f).setColor(-1).setUv(u0, v0).setLight(FULL_BRIGHT);
        });
    }

    /** drawBorderedText, in the world. */
    private static void borderedText(PoseStack poseStack, SubmitNodeCollector collector, FormattedCharSequence s, int x,
                                     int y, int colour) {
        int[][] offsets = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
        for (int[] off : offsets) {
            collector.submitText(poseStack, x + off[0], y + off[1], s, false, Font.DisplayMode.SEE_THROUGH, FULL_BRIGHT,
                    0xFF000000, 0, 0);
        }
        collector.submitText(poseStack, x, y, s, false, Font.DisplayMode.SEE_THROUGH, FULL_BRIGHT, 0xFF000000 | colour, 0, 0);
    }
}
