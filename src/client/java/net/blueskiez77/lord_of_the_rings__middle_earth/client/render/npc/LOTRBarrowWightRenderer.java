package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBarrowWightModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRBarrowWightEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderBarrowWight: a wight in one of the wight skins, casting no
 * shadow, bobbing gently in the air; dying, it does not topple but swells to
 * twice its size and fades away over a second.
 *
 * <p>NOT ported yet: marking that the viewer is the wight's quarry, for the
 * darkening of their screen (LOTRTickHandlerClient.anyWightsViewed, with the
 * HUD).
 */
public class LOTRBarrowWightRenderer extends LOTRBipedRenderer<LOTRBarrowWightEntity, LOTRNPCRenderState,
        LOTRBarrowWightModel<LOTRNPCRenderState>> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/barrowWight/wight", "barrow_wight/wight");

    public LOTRBarrowWightRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRBarrowWightModel<>(LOTRBarrowWightModel.createBodyLayer().bakeRoot()));
    }

    private LOTRBarrowWightRenderer(EntityRendererProvider.Context context, LOTRBarrowWightModel<LOTRNPCRenderState> model) {
        super(context, model, model, 0.0f);
    }

    @Override
    public LOTRNPCRenderState createRenderState() {
        return new LOTRNPCRenderState();
    }

    @Override
    public void extractRenderState(LOTRBarrowWightEntity wight, LOTRNPCRenderState state, float partialTick) {
        super.extractRenderState(wight, state, partialTick);
        state.skin = SKINS.getRandomSkin(wight.getUUID());
        // setRotationAngles: f1 = 0, so it never walks.
        state.walkAnimationSpeed = 0.0f;
    }

    @Override
    public Identifier getTextureLocation(LOTRNPCRenderState state) {
        return state.skin;
    }

    /** getDeathMaxRotation: 0. */
    @Override
    protected float getFlipDegrees() {
        return 0.0f;
    }

    private static float death(LOTRNPCRenderState state) {
        return state.deathTime > 0.0f ? Mth.clamp((state.deathTime - 1.0f) / 20.0f, 0.0f, 1.0f) : 0.0f;
    }

    /** preRenderCallback: the bob, and the swelling as it dies. */
    @Override
    protected void scale(LOTRNPCRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        float hover = Mth.sin(state.ageInTicks * 0.05f) * 0.2f;
        poseStack.translate(0.0f, hover, 0.0f);
        if (state.deathTime > 0.0f) {
            float scale = 1.0f + death(state);
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    protected int getModelTint(LOTRNPCRenderState state) {
        return state.deathTime > 0.0f ? ARGB.white(1.0f - death(state)) : -1;
    }

    @Override
    protected @Nullable RenderType getRenderType(LOTRNPCRenderState state, boolean visible, boolean translucent,
                                                 boolean glowing) {
        if (state.deathTime > 0.0f && visible) {
            return RenderTypes.entityTranslucent(getTextureLocation(state));
        }
        return super.getRenderType(state, visible, translucent, glowing);
    }
}
