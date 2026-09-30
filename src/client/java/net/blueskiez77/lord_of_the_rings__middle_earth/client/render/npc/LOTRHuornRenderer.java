package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHuornModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRHuornBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRTreeEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import it.unimi.dsi.fastutil.ints.IntList;

import org.joml.Vector3f;

/**
 * LOTRRenderHuorn: a huorn as a tree of its kind's logs and leaves -- the
 * oak's leaves in the colour of the forest it stands in -- with one of the
 * huorn faces while it is awake. Asleep, it stands square on its block, facing
 * nowhere in particular, and shows no hurt. It is drawn whenever any of its
 * crown is in view, not only its trunk.
 *
 * <p>NOT ported yet: the hired unit's icon and health bar (with the hire
 * screens).
 */
public class LOTRHuornRenderer extends MobRenderer<LOTRHuornBaseEntity, LOTRHuornRenderState, LOTRHuornModel> {

    private static final LOTRRandomSkins FACES = LOTRRandomSkins.loadSkinsList("lotr:mob/huorn/face", "huorn/face");

    private final BlockModelResolver blockModelResolver;

    public LOTRHuornRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRHuornModel(LOTRHuornModel.createBodyLayer().bakeRoot()), 0.0f);
        this.blockModelResolver = context.getBlockModelResolver();
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTRHuornRenderState state,
                               float yRot, float xRot) {
                submitBlocks(state.wood, LOTRHuornModel.WOOD_BLOCKS, poseStack, collector, light, state);
                submitBlocks(state.leaves, LOTRHuornModel.LEAF_BLOCKS, poseStack, collector, light, state);
            }
        });
    }

    /** Each block centred at its place in the model, turned back upright from the model's flipped space. */
    private static void submitBlocks(BlockModelRenderState block, java.util.List<Vector3f> places, PoseStack poseStack,
                                     SubmitNodeCollector collector, int light, LOTRHuornRenderState state) {
        if (block.isEmpty()) {
            return;
        }
        for (Vector3f place : places) {
            poseStack.pushPose();
            poseStack.translate(place.x() / 16.0f, place.y() / 16.0f, place.z() / 16.0f);
            poseStack.scale(-1.0f, -1.0f, 1.0f);
            poseStack.translate(-0.5f, -0.5f, -0.5f);
            block.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }

    @Override
    public LOTRHuornRenderState createRenderState() {
        return new LOTRHuornRenderState();
    }

    @Override
    public void extractRenderState(LOTRHuornBaseEntity huorn, LOTRHuornRenderState state, float partialTick) {
        super.extractRenderState(huorn, state, partialTick);
        state.face = FACES.getRandomSkin(huorn.getUUID());
        state.active = huorn.isHuornActive();
        // renderLivingAt set hurtTime to 0: a huorn never flushes red.
        state.hasRedOverlay = false;
        int treeType = huorn.getTreeType();
        this.blockModelResolver.update(state.wood, LOTRTreeEntity.woodBlock(treeType).defaultBlockState(),
                TntRenderer.BLOCK_DISPLAY_CONTEXT);
        BlockState leaves = LOTRTreeEntity.leafBlock(treeType).defaultBlockState();
        this.blockModelResolver.update(state.leaves, leaves, TntRenderer.BLOCK_DISPLAY_CONTEXT);
        // bindLeafTexture: the leaves' own colour, or the biome's foliage for oak.
        IntList tints = state.leaves.tintLayers();
        for (int i = 0; i < tints.size(); ++i) {
            BlockTintSource source = Minecraft.getInstance().getBlockColors().getTintSource(leaves, i);
            if (source == null) {
                continue;
            }
            int colour = leaves.is(Blocks.OAK_LEAVES)
                    ? source.colorInWorld(leaves, Minecraft.getInstance().level,
                    BlockPos.containing(huorn.getX(), huorn.getBoundingBox().minY, huorn.getZ()))
                    : source.color(leaves);
            tints.set(i, colour);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRHuornRenderState state) {
        return state.face;
    }

    /** renderLivingAt: asleep, at the middle of its block. */
    @Override
    public Vec3 getRenderOffset(LOTRHuornRenderState state) {
        double dx = 0.0;
        double dy = -0.0078125;
        double dz = 0.0;
        if (!state.active) {
            dx = Mth.floor(state.x) + 0.5 - state.x;
            dy += Mth.floor(state.y) - state.y;
            dz = Mth.floor(state.z) + 0.5 - state.z;
        }
        return new Vec3(dx, dy, dz);
    }

    /** rotateCorpse: asleep, it faces due south. */
    @Override
    protected void setupRotations(LOTRHuornRenderState state, PoseStack poseStack, float bodyRot, float scale) {
        super.setupRotations(state, poseStack, state.active ? bodyRot : 0.0f, scale);
    }

    /** isInRangeToRender3d: in view if its crown is. */
    @Override
    public boolean shouldRender(LOTRHuornBaseEntity huorn, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(huorn, frustum, x, y, z) || frustum.isVisible(huorn.getBoundingBox().inflate(2.0, 3.0, 2.0));
    }
}
