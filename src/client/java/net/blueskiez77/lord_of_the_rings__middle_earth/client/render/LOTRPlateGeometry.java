package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * LOTRRenderBlocks.renderEntityPlate: a plate, drawn outside the chunk mesh --
 * thrown, or worn on a head. The same two boxes as the block model, the top
 * face wearing {@code <plate>_top} and every other face {@code <plate>_base}.
 * Coordinates are a block's 0..1 from the pose's origin.
 */
public final class LOTRPlateGeometry {
    /** The foot, then the dish on it, in sixteenths. */
    private static final float[][] BOXES = {{3, 0, 3, 13, 1, 13}, {2, 1, 2, 14, 2, 14}};

    private LOTRPlateGeometry() {
    }

    private static Identifier texture(Block plate, String suffix) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(plate);
        return Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + suffix + ".png");
    }

    public static void submit(PoseStack poseStack, SubmitNodeCollector collector, Block plate, int light) {
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture(plate, "_top")),
                (pose, consumer) -> draw(pose, consumer, true, light));
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture(plate, "_base")),
                (pose, consumer) -> draw(pose, consumer, false, light));
    }

    private static void draw(PoseStack.Pose pose, VertexConsumer consumer, boolean top, int light) {
        for (float[] box : BOXES) {
            float x0 = box[0] / 16.0f;
            float y0 = box[1] / 16.0f;
            float z0 = box[2] / 16.0f;
            float x1 = box[3] / 16.0f;
            float y1 = box[4] / 16.0f;
            float z1 = box[5] / 16.0f;
            if (top) {
                vertex(pose, consumer, x0, y1, z0, x0, z0, 0, 1, 0, light);
                vertex(pose, consumer, x0, y1, z1, x0, z1, 0, 1, 0, light);
                vertex(pose, consumer, x1, y1, z1, x1, z1, 0, 1, 0, light);
                vertex(pose, consumer, x1, y1, z0, x1, z0, 0, 1, 0, light);
                continue;
            }
            vertex(pose, consumer, x0, y0, z1, x0, z1, 0, -1, 0, light);
            vertex(pose, consumer, x0, y0, z0, x0, z0, 0, -1, 0, light);
            vertex(pose, consumer, x1, y0, z0, x1, z0, 0, -1, 0, light);
            vertex(pose, consumer, x1, y0, z1, x1, z1, 0, -1, 0, light);

            vertex(pose, consumer, x1, y1, z0, x1, 1 - y1, 0, 0, -1, light);
            vertex(pose, consumer, x1, y0, z0, x1, 1 - y0, 0, 0, -1, light);
            vertex(pose, consumer, x0, y0, z0, x0, 1 - y0, 0, 0, -1, light);
            vertex(pose, consumer, x0, y1, z0, x0, 1 - y1, 0, 0, -1, light);

            vertex(pose, consumer, x0, y1, z1, x0, 1 - y1, 0, 0, 1, light);
            vertex(pose, consumer, x0, y0, z1, x0, 1 - y0, 0, 0, 1, light);
            vertex(pose, consumer, x1, y0, z1, x1, 1 - y0, 0, 0, 1, light);
            vertex(pose, consumer, x1, y1, z1, x1, 1 - y1, 0, 0, 1, light);

            vertex(pose, consumer, x0, y1, z0, z0, 1 - y1, -1, 0, 0, light);
            vertex(pose, consumer, x0, y0, z0, z0, 1 - y0, -1, 0, 0, light);
            vertex(pose, consumer, x0, y0, z1, z1, 1 - y0, -1, 0, 0, light);
            vertex(pose, consumer, x0, y1, z1, z1, 1 - y1, -1, 0, 0, light);

            vertex(pose, consumer, x1, y1, z1, z1, 1 - y1, 1, 0, 0, light);
            vertex(pose, consumer, x1, y0, z1, z1, 1 - y0, 1, 0, 0, light);
            vertex(pose, consumer, x1, y0, z0, z0, 1 - y0, 1, 0, 0, light);
            vertex(pose, consumer, x1, y1, z0, z0, 1 - y1, 1, 0, 0, light);
        }
    }

    static void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v,
            float nx, float ny, float nz, int light) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}
