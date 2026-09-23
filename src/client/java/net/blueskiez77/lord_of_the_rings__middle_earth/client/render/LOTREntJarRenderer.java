package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTREntJarBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.data.AtlasIds;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderEntJar. One horizontal quad -- the surface of whatever the jar
 * holds -- floating at a height set by how full it is.
 *
 * <p>The original drew it 3/8 of a block across, from 1/16 up at the very
 * bottom to 13/16 at the brim, and tinted it: biome water colour for water, or
 * a single pixel sampled out of the middle of an Ent-draught's item icon for a
 * draught.
 */
public class LOTREntJarRenderer
        implements BlockEntityRenderer<LOTREntJarBlockEntity, LOTREntJarRenderState> {

    /** {@code d3 = 0.1875}: the quad's half-width about the block centre. */
    private static final float HALF_WIDTH = 0.1875f;

    /** {@code -0.0625 - 0.75 * amount / MAX}, un-negated out of the original's flipped matrix. */
    private static final float SURFACE_BASE = 0.0625f;
    private static final float SURFACE_RANGE = 0.75f;

    /** {@code transparency = 0.5f}. */
    private static final int ALPHA = 128;

    private static final Identifier WATER_STILL =
            Identifier.withDefaultNamespace("block/water_still");

    /** The original's {@code icon.getInterpolatedU(0..6)} on the water sprite. */
    private static final float WATER_UV_SPAN = 6.0f;

    /** The draught icons, in LOTRItemEntDraught's order. */
    private static final String[] DRAUGHT_COLORS = {"green", "brown", "gold", "yellow", "red", "silver", "blue"};

    private final SpriteGetter sprites;

    public LOTREntJarRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
    }

    @Override
    public LOTREntJarRenderState createRenderState() {
        return new LOTREntJarRenderState();
    }

    @Override
    public void extractRenderState(LOTREntJarBlockEntity jar, LOTREntJarRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(jar, state, crumbling);

        // Level is not a BlockAndTintGetter -- biome tinting is a client-side
        // interface that only ClientLevel implements -- so the water colour is
        // only available once this narrows.
        if (jar.isEmpty() || !(jar.getLevel() instanceof BlockAndTintGetter tinted)) {
            state.surfaceY = -1.0f;
            return;
        }

        state.surfaceY = SURFACE_BASE
                + SURFACE_RANGE * jar.getDrinkAmount() / LOTREntJarBlockEntity.MAX_CAPACITY;

        if (jar.holdsWater()) {
            state.atlas = AtlasIds.BLOCKS;
            state.sprite = sprites.get(new SpriteId(AtlasIds.BLOCKS, WATER_STILL));
            state.tint = ARGB.opaque(BiomeColors.getAverageWaterColor(tinted, jar.getBlockPos()));
            state.uvFrom = 0.0f;
            state.uvTo = WATER_UV_SPAN;
        } else {
            // The single texel at (7, 7) of the draught's own icon, untinted.
            int draught = Math.clamp(jar.getDrinkMeta(), 0, DRAUGHT_COLORS.length - 1);
            state.atlas = AtlasIds.ITEMS;
            state.sprite = sprites.get(new SpriteId(AtlasIds.ITEMS,
                    Identifier.fromNamespaceAndPath("lotr", "item/ent_draught_" + DRAUGHT_COLORS[draught])));
            state.tint = 0xFFFFFFFF;
            state.uvFrom = 7.0f;
            state.uvTo = 8.0f;
        }
    }

    @Override
    public void submit(LOTREntJarRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        TextureAtlasSprite sprite = state.sprite;

        if (sprite == null || state.surfaceY < 0.0f) {
            return;
        }

        float y = state.surfaceY;
        int light = state.lightCoords;
        int tint = state.tint;
        float uvFrom = state.uvFrom;
        float uvTo = state.uvTo;

        collector.submitCustomGeometry(poseStack,
                RenderTypes.entityTranslucent(state.atlas),
                (pose, consumer) -> surface(pose, consumer, sprite, y, tint, light, uvFrom, uvTo));
    }

    /**
     * The surface quad, facing up, wound counter-clockwise seen from above.
     *
     * <p>The original disabled lighting outright, so the liquid drew at full
     * brightness. This uses the block's own light instead -- a jar in deep
     * forest shade reading as brightly lit is a 1.7.10 immediate-mode artefact
     * rather than something the block was trying to do.
     */
    private static void surface(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                float y, int tint, int light, float uvFrom, float uvTo) {
        float min = 0.5f - HALF_WIDTH;
        float max = 0.5f + HALF_WIDTH;

        float u0 = sprite.getU(uvFrom / 16.0f);
        float u1 = sprite.getU(uvTo / 16.0f);
        float v0 = sprite.getV(uvFrom / 16.0f);
        float v1 = sprite.getV(uvTo / 16.0f);

        int r = ARGB.red(tint);
        int g = ARGB.green(tint);
        int b = ARGB.blue(tint);

        vertex(pose, consumer, min, y, max, u0, v1, r, g, b, light);
        vertex(pose, consumer, max, y, max, u1, v1, r, g, b, light);
        vertex(pose, consumer, max, y, min, u1, v0, r, g, b, light);
        vertex(pose, consumer, min, y, min, u0, v0, r, g, b, light);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer,
                               float x, float y, float z, float u, float v,
                               int r, int g, int b, int light) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, ALPHA)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
