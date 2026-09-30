package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRWargModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargBombardierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargType;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * LOTRRenderWarg: the warg in its coat, its barding laid over the coat
 * (getLayeredMountTexture), its saddle, and eyes that glow -- two pixels
 * each at (100, 12) and (108, 12) of its skin. A bombardier carries its orc
 * bomb on its back, a little smaller than a placed one, flashing as it burns.
 *
 * <p>The bomb's flash is 26.2's TNT flash; the original faded a white
 * overlay in as the fuse burned.
 */
public class LOTRWargRenderer extends MobRenderer<LOTRWargEntity, LOTRWargRenderState, LOTRWargModel> {

    private static final Identifier SADDLE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
            "textures/entity/warg/saddle.png");
    private static final int[][] EYES = {{100, 12}, {108, 12}};

    private final BlockModelResolver blockModelResolver;

    public LOTRWargRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRWargModel(LOTRWargModel.createBodyLayer(0.0f).bakeRoot()), 0.5f);
        this.blockModelResolver = context.getBlockModelResolver();
        LOTRWargModel saddleModel = new LOTRWargModel(LOTRWargModel.createBodyLayer(0.5f).bakeRoot());
        LOTRWargModel eyesModel = new LOTRWargModel(LOTRWargModel.createBodyLayer(0.05f).bakeRoot());
        eyesModel.showOnlyHead();
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTRWargRenderState state,
                               float yRot, float xRot) {
                if (state.armor != null) {
                    coloredCutoutModelCopyLayerRender(getParentModel(), state.armor, poseStack, collector, light, state, -1, 1);
                }
                if (state.saddled) {
                    coloredCutoutModelCopyLayerRender(saddleModel, SADDLE, poseStack, collector, light, state, -1, 2);
                }
                if (!state.isInvisible) {
                    Identifier eyes = LOTRGlowingEyes.getEyesTexture(state.skin, EYES, 2, 1);
                    collector.order(3).submitModel(eyesModel, state, poseStack, RenderTypes.eyes(eyes),
                            0xF000F0, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                }
            }
        });
    }

    public static Identifier getWargSkin(LOTRWargType type) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/warg/" + type.textureName() + ".png");
    }

    @Override
    public LOTRWargRenderState createRenderState() {
        return new LOTRWargRenderState();
    }

    @Override
    public void extractRenderState(LOTRWargEntity warg, LOTRWargRenderState state, float partialTick) {
        super.extractRenderState(warg, state, partialTick);
        state.skin = getWargSkin(warg.getWargType());
        state.tailRotation = warg.getTailRotation();
        state.saddled = warg.isMountSaddled();
        state.armor = LOTRMountArmorTextures.textureFor(warg.getWargArmor());
        state.hasBomb = warg instanceof LOTRWargBombardierEntity;
        if (warg instanceof LOTRWargBombardierEntity bombardier) {
            state.bombFuse = bombardier.getBombFuse() - partialTick + 1.0f;
            Block bomb = switch (bombardier.getBombStrengthLevel()) {
                case 0 -> LOTRCombatBlocks.ORC_BOMB;
                case 1 -> LOTRCombatBlocks.DOUBLE_STRENGTH_ORC_BOMB;
                default -> LOTRCombatBlocks.TRIPLE_STRENGTH_ORC_BOMB;
            };
            this.blockModelResolver.update(state.bomb, bomb.defaultBlockState(), TntRenderer.BLOCK_DISPLAY_CONTEXT);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRWargRenderState state) {
        return state.skin;
    }

    /** doRender: the bomb, 1.7 above its feet, turned with its body and scaled to 3/4. */
    @Override
    public void submit(LOTRWargRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.hasBomb && !state.bomb.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0f, 1.7f, 0.0f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-state.bodyRot));
            poseStack.scale(0.75f, 0.75f, 0.75f);
            if (state.bombFuse < 10.0f) {
                float scale = 1.0f + TntRenderer.getSwellAmount(state.bombFuse);
                poseStack.scale(scale, scale, scale);
            }
            poseStack.translate(-0.5f, 0.0f, -0.5f);
            TntMinecartRenderer.submitWhiteSolidBlock(state.bomb, poseStack, collector, state.lightCoords,
                    TntRenderer.isLit(state.bombFuse), state.outlineColor);
            poseStack.popPose();
        }
        super.submit(state, poseStack, collector, camera);
    }
}
