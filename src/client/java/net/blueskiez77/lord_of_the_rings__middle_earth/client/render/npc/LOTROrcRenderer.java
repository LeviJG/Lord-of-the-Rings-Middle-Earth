package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTROrcModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRGlowingEyes;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard.LOTRUrukHaiBerserkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard.LOTRUrukHaiEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRBlackUrukEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderOrc: an orc in one of the skins for its kind (orcs, Uruk-hai,
 * Black Uruks),
 * a weak orc at 0.85 of the size, with eyes that glow -- the two 2x1 eye
 * patches of the skin, at (9, 11) and (13, 11), drawn again at full
 * brightness over the head.
 * An Uruk-hai wears the Uruk skins, and a berserker is drawn at its
 * {@link LOTRUrukHaiBerserkerEntity#BERSERKER_SCALE}.
 */
public class LOTROrcRenderer
        extends LOTRBipedRenderer<LOTROrcEntity, LOTROrcRenderer.State, LOTROrcModel<LOTROrcRenderer.State>> {

    private static final LOTRRandomSkins ORC_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/orc/orc", "orc/orc");
    private static final LOTRRandomSkins BLACK_URUK_SKINS =
            LOTRRandomSkins.loadSkinsList("lotr:mob/orc/blackUruk", "orc/black_uruk");
    private static final LOTRRandomSkins URUK_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/orc/urukHai", "orc/uruk_hai");
    private static final int[][] EYES = {{9, 11}, {13, 11}};

    public static class State extends LOTRNPCRenderState {
        public boolean weakOrc;
        public boolean berserker;
    }

    public LOTROrcRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTROrcModel<>(LOTROrcModel.createBodyLayer().bakeRoot()));
    }

    private LOTROrcRenderer(EntityRendererProvider.Context context, LOTROrcModel<State> model) {
        super(context, model, model, 0.5f);
        addLayer(new HumanoidArmorLayer<State, LOTROrcModel<State>, HumanoidModel<State>>(this,
                LOTROrcModel.createArmorLayers().map(layer -> (HumanoidModel<State>) new LOTROrcModel<State>(layer.bakeRoot())),
                context.getEquipmentRenderer()));
        LOTROrcModel<State> eyesModel = new LOTROrcModel<>(LOTROrcModel.createEyesLayer().bakeRoot()) {
            @Override
            public void setupAnim(State state) {
                super.setupAnim(state);
                showOnlyHead();
            }
        };
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (!state.isInvisible) {
                    Identifier eyes = LOTRGlowingEyes.getEyesTexture(state.skin, EYES, 2, 1);
                    collector.order(3).submitModel(eyesModel, state, poseStack, RenderTypes.eyes(eyes),
                            0xF000F0, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTROrcEntity orc, State state, float partialTick) {
        super.extractRenderState(orc, state, partialTick);
        LOTRRandomSkins skins = orc instanceof LOTRUrukHaiEntity ? URUK_SKINS
                : orc instanceof LOTRBlackUrukEntity ? BLACK_URUK_SKINS : ORC_SKINS;
        state.skin = skins.getRandomSkin(orc.getUUID());
        state.berserker = orc instanceof LOTRUrukHaiBerserkerEntity;
        state.weakOrc = orc.isWeakOrc;
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        super.scale(state, poseStack);
        if (state.weakOrc) {
            poseStack.scale(0.85f, 0.85f, 0.85f);
        } else if (state.berserker) {
            float scale = LOTRUrukHaiBerserkerEntity.BERSERKER_SCALE;
            poseStack.scale(scale, scale, scale);
        }
    }
}
