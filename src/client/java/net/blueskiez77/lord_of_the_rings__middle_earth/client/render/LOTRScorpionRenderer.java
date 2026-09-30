package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRScorpionModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDesertScorpionEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRScorpionEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderScorpion: shadow 1, the jungle or desert texture, scaled by the
 * scorpion's size, and rolling over fully (180 degrees) as it dies.
 */
public class LOTRScorpionRenderer extends MobRenderer<LOTRScorpionEntity, LOTRStrikeRenderState, LOTRScorpionModel> {

    private static final Identifier JUNGLE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/scorpion/jungle.png");
    private static final Identifier DESERT = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/scorpion/desert.png");

    public LOTRScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRScorpionModel(LOTRScorpionModel.createBodyLayer().bakeRoot()), 1.0f);
    }

    @Override
    public LOTRStrikeRenderState createRenderState() {
        return new LOTRStrikeRenderState();
    }

    @Override
    public void extractRenderState(LOTRScorpionEntity scorpion, LOTRStrikeRenderState state, float partialTick) {
        super.extractRenderState(scorpion, state, partialTick);
        float strike = scorpion.getStrikeTime();
        if (strike > 0.0f) {
            strike -= partialTick;
        }
        state.strike = strike / 20.0f;
        state.scorpionScale = scorpion.getScorpionScaleAmount();
        state.desert = scorpion instanceof LOTRDesertScorpionEntity;
    }

    @Override
    protected void scale(LOTRStrikeRenderState state, PoseStack poseStack) {
        poseStack.scale(state.scorpionScale, state.scorpionScale, state.scorpionScale);
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0f;
    }

    @Override
    public Identifier getTextureLocation(LOTRStrikeRenderState state) {
        return state.desert ? DESERT : JUNGLE;
    }
}
