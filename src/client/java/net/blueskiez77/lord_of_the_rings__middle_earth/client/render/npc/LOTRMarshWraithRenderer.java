package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMarshWraithModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRMarshWraithEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderMarshWraith: a marsh wraith at a player's fifteen sixteenths,
 * fading in as it rises and out as it goes, not toppling when it dies.
 */
public class LOTRMarshWraithRenderer extends MobRenderer<LOTRMarshWraithEntity, LOTRMarshWraithRenderState, LOTRMarshWraithModel> {

    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
            "textures/entity/wraith/marsh_wraith.png");

    public LOTRMarshWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRMarshWraithModel(LOTRMarshWraithModel.createBodyLayer().bakeRoot()), 0.5f);
    }

    @Override
    public LOTRMarshWraithRenderState createRenderState() {
        return new LOTRMarshWraithRenderState();
    }

    @Override
    public void extractRenderState(LOTRMarshWraithEntity wraith, LOTRMarshWraithRenderState state, float partialTick) {
        super.extractRenderState(wraith, state, partialTick);
        state.spawnFadeTime = wraith.getSpawnFadeTime();
        state.deathFadeTime = wraith.getDeathFadeTime();
    }

    @Override
    public Identifier getTextureLocation(LOTRMarshWraithRenderState state) {
        return SKIN;
    }

    @Override
    protected float getFlipDegrees() {
        return 0.0f;
    }

    @Override
    protected void scale(LOTRMarshWraithRenderState state, PoseStack poseStack) {
        poseStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    /** The fade: in over its first 30 ticks, out over its last. */
    private static float alpha(LOTRMarshWraithRenderState state) {
        if (state.spawnFadeTime < 30) {
            return state.spawnFadeTime / 30.0f;
        }
        if (state.deathFadeTime > 0) {
            return state.deathFadeTime / 30.0f;
        }
        return 1.0f;
    }

    @Override
    protected int getModelTint(LOTRMarshWraithRenderState state) {
        return ARGB.white(alpha(state));
    }

    @Override
    protected @Nullable RenderType getRenderType(LOTRMarshWraithRenderState state, boolean visible, boolean translucent,
                                                 boolean glowing) {
        if (alpha(state) < 1.0f && visible) {
            return RenderTypes.entityTranslucent(SKIN);
        }
        return super.getRenderType(state, visible, translucent, glowing);
    }
}
