package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTREntModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRGlowingEyes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTREntEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRTreeEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderEnt: an Ent in the bark of its tree -- oak, beech or birch --
 * with its eyes glowing (the skin's eye pixels drawn again at full light), and
 * anything it is saying over its head.
 */
public class LOTREntRenderer extends MobRenderer<LOTREntEntity, LOTREntRenderState, LOTREntModel> {

    private static final int[][] EYES = {{15, 23}, {22, 23}};
    private static final Identifier[] SKINS = new Identifier[LOTRTreeEntity.TYPES.length];

    static {
        for (int i = 0; i < SKINS.length; ++i) {
            SKINS[i] = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                    "textures/entity/ent/" + LOTRTreeEntity.TYPES[i] + ".png");
        }
    }

    public LOTREntRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTREntModel(LOTREntModel.createLayer(0.0f).bakeRoot(), LOTREntModel.Piece.ALL), 0.5f);
        LOTREntModel eyesModel = new LOTREntModel(LOTREntModel.createLayer(0.05f).bakeRoot(), LOTREntModel.Piece.EYES);
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTREntRenderState state,
                               float yRot, float xRot) {
                if (!state.isInvisible && state.skin != null) {
                    Identifier eyes = LOTRGlowingEyes.getEyesTexture(state.skin, EYES, 3, 2);
                    collector.order(1).submitModel(eyesModel, state, poseStack, RenderTypes.eyes(eyes),
                            0xF000F0, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                }
            }
        });
    }

    @Override
    public LOTREntRenderState createRenderState() {
        return new LOTREntRenderState();
    }

    @Override
    public void extractRenderState(LOTREntEntity ent, LOTREntRenderState state, float partialTick) {
        super.extractRenderState(ent, state, partialTick);
        state.skin = SKINS[ent.getTreeType()];
        state.healing = ent.isHealingSapling();
        state.eyesClosed = ent.eyesClosed > 0;
        state.hurt = ent.hurtTime > 0;
        state.extraBranches = ent.getExtraHeadBranches();
        state.attackTime = ent.getAttackAnim(partialTick);
        state.speech = LOTRNPCSpeechRendering.extract(ent, getFont());
    }

    @Override
    public Identifier getTextureLocation(LOTREntRenderState state) {
        return state.skin;
    }

    @Override
    public void submit(LOTREntRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.speech != null && state.distanceToCameraSq <= LOTRNPCSpeechRendering.NAME_TAG_RANGE * LOTRNPCSpeechRendering.NAME_TAG_RANGE) {
            LOTRNPCSpeechRendering.submit(getFont(), state.boundingBoxHeight, state.speech, poseStack, collector, camera);
        }
    }
}
