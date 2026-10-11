package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRScrapTraderMisbehaviour;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRScrapTraderEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderScrapTrader: the oddment collector in one of his skins
 * ({@code mob/scrapTrader}). He is not drawn while the screenshot key is
 * held. While he misbehaves (LOTRScrapTraderMisbehaviour) he stands six
 * times his height, wherever the camera looks, ringed about the viewer by
 * copies of himself three times as tall, six blocks apart. Fading away in
 * Utumno, he is drawn ever fainter.
 */
public class LOTRScrapTraderRenderer
        extends LOTRBipedRenderer<LOTRScrapTraderEntity, LOTRScrapTraderRenderer.State, LOTRHumanModel<LOTRScrapTraderRenderer.State>> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/scrapTrader", "scrap_trader");

    public static class State extends LOTRNPCRenderState {
        public float fadeout;
    }

    public LOTRScrapTraderRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRScrapTraderRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model) {
        super(context, model, model, 0.5f);
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRScrapTraderEntity trader, State state, float partialTick) {
        super.extractRenderState(trader, state, partialTick);
        state.skin = SKINS.getRandomSkin(trader.getUUID());
        state.fadeout = trader.getFadeoutProgress(partialTick);
    }

    /** preRenderCallback: fading, he is drawn see-through, the less of him the further gone. */
    @Override
    protected net.minecraft.client.renderer.rendertype.RenderType getRenderType(State state, boolean bodyVisible, boolean translucent,
                                                                                 boolean glowing) {
        if (state.fadeout > 0.0f) {
            return net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(getTextureLocation(state));
        }
        return super.getRenderType(state, bodyVisible, translucent, glowing);
    }

    @Override
    protected int getModelTint(State state) {
        if (state.fadeout > 0.0f) {
            return net.minecraft.util.ARGB.white(net.minecraft.util.Mth.clamp(1.0f - state.fadeout, 0.0f, 1.0f));
        }
        return super.getModelTint(state);
    }

    @Override
    public boolean shouldRender(LOTRScrapTraderEntity trader, Frustum culler, double camX, double camY, double camZ) {
        return LOTRScrapTraderMisbehaviour.isMisbehaving(trader) || super.shouldRender(trader, culler, camX, camY, camZ);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        if (Minecraft.getInstance().options.keyScreenshot.isDown()) {
            return;
        }
        if (!LOTRScrapTraderMisbehaviour.isMisbehaving()) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        int r = 3;
        double spacing = 6.0;
        for (int i = -r; i <= r; ++i) {
            for (int k = -r; k <= r; ++k) {
                if (Math.abs(i) + Math.abs(k) <= 2) {
                    continue;
                }
                poseStack.pushPose();
                poseStack.translate(camera.pos.x - state.x + i * spacing, camera.pos.y - state.y,
                        camera.pos.z - state.z + k * spacing);
                poseStack.scale(1.0f, 3.0f, 1.0f);
                super.submit(state, poseStack, collector, camera);
                poseStack.popPose();
            }
        }
        poseStack.pushPose();
        poseStack.scale(1.0f, 6.0f, 1.0f);
        super.submit(state, poseStack, collector, camera);
        poseStack.popPose();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
