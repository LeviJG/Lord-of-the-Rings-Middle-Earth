package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRTableOfCommandBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderCommandTable: the map of Middle-earth on the Table of Command's top, centred on the
 * table (kept within the map), four hundred map pixels across at the table's zoom, with its faint
 * overlay, the compass in the corner, and a red and a black edge beneath. Meneltarma stays sunk in
 * the sea until the viewer has stood on it.
 *
 * The table's item shows the same map, centred on the player (LOTRTableOfCommandItemRenderer).
 */
public class LOTRTableOfCommandRenderer implements BlockEntityRenderer<LOTRTableOfCommandBlockEntity, LOTRTableOfCommandRenderer.State> {

    private static final double RADIUS = 0.9;

    public static class State extends BlockEntityRenderState {
        double tableX;
        double tableZ;
        int zoomExp;
        boolean meneltarma;
    }

    public LOTRTableOfCommandRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRTableOfCommandBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(table, state, crumbling);
        state.tableX = table.getBlockPos().getX() + 0.5;
        state.tableZ = table.getBlockPos().getZ() + 0.5;
        state.zoomExp = table.getZoomExp();
        Player player = Minecraft.getInstance().player;
        state.meneltarma = player != null && LOTRPlayerAchievements.hasAchievement(player, LOTRAchievement.ENTER_MENELTARMA);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        submitMap(poseStack, collector, state.tableX, state.tableZ, state.zoomExp, state.meneltarma, state.lightCoords);
    }

    /** renderTableAt: the map above the block at the origin, centred on (viewerX, viewerZ) in the world. */
    public static void submitMap(PoseStack poseStack, SubmitNodeCollector collector, double viewerX, double viewerZ, int zoomExp,
                                 boolean meneltarma, int light) {
        int imageWidth = LOTRMapRenderer.MAP_IMAGE_WIDTH;
        int imageHeight = LOTRMapRenderer.MAP_IMAGE_HEIGHT;
        float posX = Math.round(viewerX / LOTRMapCoords.SCALE) + 810L;
        float posY = Math.round(viewerZ / LOTRMapCoords.SCALE) + 730L;
        int viewportWidth = (int) Math.round(400 * Math.pow(2.0, zoomExp));
        if (posX - (float) viewportWidth / 2 < 0.0f) {
            posX = (float) viewportWidth / 2;
        }
        if (posX + (float) viewportWidth / 2 >= imageWidth) {
            posX = imageWidth - (float) viewportWidth / 2;
        }
        if (posY - (float) viewportWidth / 2 < 0.0f) {
            posY = (float) viewportWidth / 2;
        }
        if (posY + (float) viewportWidth / 2 >= imageHeight) {
            posY = imageHeight - (float) viewportWidth / 2;
        }
        float minU = (posX - viewportWidth / 2) / imageWidth;
        float maxU = (posX + viewportWidth / 2) / imageWidth;
        float minV = (posY - viewportWidth / 2) / imageHeight;
        float maxV = (posY + viewportWidth / 2) / imageHeight;
        float r = (float) RADIUS;
        poseStack.pushPose();
        // The original drew in a plane turned flat onto the table: its x is east and its y south.
        poseStack.translate(0.5f, 1.1f, 0.5f);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(LOTRMapRenderer.MAP), (pose, consumer) ->
                quad(consumer, pose, -r, r, -r, r, 0.0f, minU, maxU, minV, maxV, 0xFFFFFFFF, light));
        if (!meneltarma) {
            submitMeneltarmaSea(poseStack, collector, minU, maxU, minV, maxV, imageWidth, imageHeight, light);
        }
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(LOTRMapRenderer.MAP_OVERLAY), (pose, consumer) ->
                quad(consumer, pose, -r, r, -r, r, 0.0005f, 0.0f, 1.0f, 0.0f, 1.0f, 0x33FFFFFF, light));
        // drawMapCompassBottomLeft, a little above the map.
        float inset = r * 0.05f;
        float size = 32.0f * 0.15f * r * 0.0625f;
        float cx0 = -r + inset;
        float cz1 = r - inset;
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(LOTRMapRenderer.MAP_ICONS), (pose, consumer) ->
                quad(consumer, pose, cx0, cx0 + size, cz1 - size, cz1, 0.01f, 224.0f / 256.0f, 256.0f / 256.0f, 200.0f / 256.0f,
                        232.0f / 256.0f, 0xFFFFFFFF, light));
        // The red edge, then the black one wider still, each a little below.
        float rRed = r + 0.015f;
        float rBlack = rRed + 0.015f;
        collector.submitCustomGeometry(poseStack, RenderTypes.textBackground(), (pose, consumer) -> {
            colourQuad(consumer, pose, rRed, -0.01f, -6156032, light);
            colourQuad(consumer, pose, rBlack, -0.02f, -16777216, light);
        });
        poseStack.popPose();
    }

    /** drawMap's Meneltarma: the sea over the isle, forty map pixels square. */
    private static void submitMeneltarmaSea(PoseStack poseStack, SubmitNodeCollector collector, float minU, float maxU,
                                            float minV, float maxV, int imageWidth, int imageHeight, int light) {
        double mtX = LOTRWaypoint.MENELTARMA_SUMMIT.getX();
        double mtY = LOTRWaypoint.MENELTARMA_SUMMIT.getY();
        int mtW = 20;
        double mtMinU = (mtX - mtW) / imageWidth;
        double mtMaxU = (mtX + mtW) / imageWidth;
        double mtMinV = (mtY - mtW) / imageHeight;
        double mtMaxV = (mtY + mtW) / imageHeight;
        if (minU > mtMaxU || maxU < mtMinU || minV > mtMaxV || maxV < mtMinV) {
            return;
        }
        mtMinU = Math.max(mtMinU, minU);
        mtMaxU = Math.min(mtMaxU, maxU);
        mtMinV = Math.max(mtMinV, minV);
        mtMaxV = Math.min(mtMaxV, maxV);
        float r = (float) RADIUS;
        double ratioX = 2.0 * r / (maxU - minU);
        double ratioY = 2.0 * r / (maxV - minV);
        float x0 = (float) (-r + (mtMinU - minU) * ratioX);
        float x1 = (float) (-r + (mtMaxU - minU) * ratioX);
        float z0 = (float) (-r + (mtMinV - minV) * ratioY);
        float z1 = (float) (-r + (mtMaxV - minV) * ratioY);
        int ocean = LOTRMapRenderer.getMapOceanColor(true);
        collector.submitCustomGeometry(poseStack, RenderTypes.textBackground(), (pose, consumer) -> {
            consumer.addVertex(pose, x0, 0.0002f, z1).setColor(ocean).setLight(light);
            consumer.addVertex(pose, x1, 0.0002f, z1).setColor(ocean).setLight(light);
            consumer.addVertex(pose, x1, 0.0002f, z0).setColor(ocean).setLight(light);
            consumer.addVertex(pose, x0, 0.0002f, z0).setColor(ocean).setLight(light);
        });
    }

    private static void quad(VertexConsumer consumer, PoseStack.Pose pose, float x0, float x1, float z0, float z1, float y,
                             float minU, float maxU, float minV, float maxV, int colour, int light) {
        vertex(consumer, pose, x0, y, z1, minU, maxV, colour, light);
        vertex(consumer, pose, x1, y, z1, maxU, maxV, colour, light);
        vertex(consumer, pose, x1, y, z0, maxU, minV, colour, light);
        vertex(consumer, pose, x0, y, z0, minU, minV, colour, light);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int colour, int light) {
        consumer.addVertex(pose, x, y, z).setColor(colour).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }

    private static void colourQuad(VertexConsumer consumer, PoseStack.Pose pose, float r, float y, int colour, int light) {
        consumer.addVertex(pose, -r, y, r).setColor(colour).setLight(light);
        consumer.addVertex(pose, r, y, r).setColor(colour).setLight(light);
        consumer.addVertex(pose, r, y, -r).setColor(colour).setLight(light);
        consumer.addVertex(pose, -r, y, -r).setColor(colour).setLight(light);
    }
}
