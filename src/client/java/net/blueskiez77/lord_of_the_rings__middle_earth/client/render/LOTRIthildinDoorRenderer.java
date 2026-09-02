package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRIthildinDwarvenDoorBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRIthildinDwarvenDoorBlock.DoorSize;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRDwarvenDoorBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderDwarvenDoor + LOTRRenderBlocks.renderDwarvenDoorGlow.
 *
 * <p>Draws one fullbright quad of ithildin over the door's outward face,
 * faded by {@link LOTRDwarvenDoorBlockEntity#getGlowBrightness}. The original
 * asked getGlowIcon for all six sides and drew whichever came back non-null,
 * which was only ever the one face opposite the gate's FACING; that single
 * face is all this draws.
 *
 * <p>The quad sits {@value #INSET} out from the block so it never z-fights with
 * the stone underneath -- the {@code d = 0.01} of the original.
 */
public class LOTRIthildinDoorRenderer
        implements BlockEntityRenderer<LOTRDwarvenDoorBlockEntity, LOTRIthildinDoorRenderState> {

    /** LOTRRenderBlocks.renderDwarvenDoorGlow's {@code d}. */
    public static final float INSET = 0.01f;

    /**
     * LOTRTileEntityDwarvenDoor.getMaxRenderDistanceSquared was
     * {@code (GLOW_RANGE + 20)^2}. The engraving cannot be lit unless a player
     * is within twelve blocks of it anyway, so there is nothing to draw further
     * out than this and the renderer is simply skipped.
     */
    private static final int VIEW_DISTANCE = LOTRDwarvenDoorBlockEntity.GLOW_RANGE + 20;

    private final SpriteGetter sprites;

    public LOTRIthildinDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
    }

    @Override
    public LOTRIthildinDoorRenderState createRenderState() {
        return new LOTRIthildinDoorRenderState();
    }

    @Override
    public int getViewDistance() {
        return VIEW_DISTANCE;
    }

    @Override
    public void extractRenderState(LOTRDwarvenDoorBlockEntity door, LOTRIthildinDoorRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(door, state, crumbling);

        BlockState blockState = door.getBlockState();
        if (!(blockState.getBlock() instanceof LOTRIthildinDwarvenDoorBlock)) {
            state.glow = 0.0f;
            return;
        }

        Direction facing = blockState.getValue(LOTRGateBlock.FACING);

        // A door lying flat carries no design, and an open one is a hole: the
        // original's getGlowIcon returned null for both.
        if (facing.getAxis() == Direction.Axis.Y || blockState.getValue(LOTRGateBlock.OPEN)) {
            state.glow = 0.0f;
            return;
        }

        state.glow = door.getGlowBrightness(partialTick);
        state.glowFace = facing.getOpposite();
        state.across = LOTRIthildinDwarvenDoorBlock.acrossFrom(facing);
        state.sprite = spriteFor(door.getDoorSize(), door.getDoorPosX(), door.getDoorPosY());
    }

    /**
     * {@code ithildin_dwarven_door_glow_<size>_<x>_<y>}. Twenty-five sprites,
     * one per cell of the five designs, which tile into a single engraving
     * because each block draws only its own cell.
     */
    private TextureAtlasSprite spriteFor(DoorSize size, int x, int y) {
        Identifier texture = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "block/ithildin_dwarven_door_glow_" + size.doorName() + "_" + x + "_" + y);
        return sprites.get(new SpriteId(AtlasIds.BLOCKS, texture));
    }

    @Override
    public void submit(LOTRIthildinDoorRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        TextureAtlasSprite sprite = state.sprite;

        // Daylight, a roofed-in door, or nobody nearby: nothing is submitted at
        // all, so a door that is not currently glowing costs one float compare.
        if (sprite == null || state.glow <= 0.0f) {
            return;
        }

        int alpha = Math.clamp((int) (state.glow * 255.0f), 0, 255);
        if (alpha == 0) {
            return;
        }

        Direction face = state.glowFace;
        Direction right = state.across;

        collector.submitCustomGeometry(poseStack,
                RenderTypes.entityTranslucentEmissive(AtlasIds.BLOCKS),
                (customPose, consumer) -> quad(customPose, consumer, face, right, sprite, alpha));
    }

    /**
     * One unit square on {@code face}, pushed {@link #INSET} clear of the
     * block, with the sprite's "up" along world up and its "right" along the
     * design's own across axis so a multi-block engraving lines up.
     *
     * <p>If a design ever renders mirrored left-to-right, this is the place:
     * swap {@code right} for its opposite. The 1.7.10 original got the same
     * decision for free from RenderBlocks' per-face UV conventions, which have
     * no direct equivalent here.
     */
    private static void quad(PoseStack.Pose pose, VertexConsumer consumer,
                             Direction face, Direction right, TextureAtlasSprite sprite, int alpha) {
        // The face's outward normal, and the block-local centre of that face.
        float nx = face.getStepX();
        float ny = face.getStepY();
        float nz = face.getStepZ();

        float cx = 0.5f + nx * (0.5f + INSET);
        float cy = 0.5f + ny * (0.5f + INSET);
        float cz = 0.5f + nz * (0.5f + INSET);

        // Half-extents along the face's right and up axes. Up is world up:
        // every door that carries a design stands vertically.
        float rx = right.getStepX() * 0.5f;
        float ry = right.getStepY() * 0.5f;
        float rz = right.getStepZ() * 0.5f;
        float ux = 0.0f;
        float uy = 0.5f;
        float uz = 0.0f;

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        // Wound counter-clockwise seen from outside, starting top-left, so the
        // texture's v0 edge is the top of the engraving.
        vertex(pose, consumer, cx - rx + ux, cy - ry + uy, cz - rz + uz, u0, v0, alpha, nx, ny, nz);
        vertex(pose, consumer, cx - rx - ux, cy - ry - uy, cz - rz - uz, u0, v1, alpha, nx, ny, nz);
        vertex(pose, consumer, cx + rx - ux, cy + ry - uy, cz + rz - uz, u1, v1, alpha, nx, ny, nz);
        vertex(pose, consumer, cx + rx + ux, cy + ry + uy, cz + rz + uz, u1, v0, alpha, nx, ny, nz);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer,
                               float x, float y, float z, float u, float v, int alpha,
                               float nx, float ny, float nz) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, alpha)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                // Emissive: the engraving is its own light source, so it is
                // drawn fullbright rather than at the block's light level.
                .setLight(0x00F000F0)
                .setNormal(pose, nx, ny, nz);
    }
}
