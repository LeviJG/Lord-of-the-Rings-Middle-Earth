package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRCrocodileModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCrocodileEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

/** LOTRRenderCrocodile: shadow 0.75, and the snap time for the jaw. */
public class LOTRCrocodileRenderer extends MobRenderer<LOTRCrocodileEntity, LOTRStrikeRenderState, LOTRCrocodileModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/crocodile.png");

    public LOTRCrocodileRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRCrocodileModel(LOTRCrocodileModel.createBodyLayer().bakeRoot()), 0.75f);
    }

    @Override
    public LOTRStrikeRenderState createRenderState() {
        return new LOTRStrikeRenderState();
    }

    @Override
    public void extractRenderState(LOTRCrocodileEntity crocodile, LOTRStrikeRenderState state, float partialTick) {
        super.extractRenderState(crocodile, state, partialTick);
        float snap = crocodile.getSnapTime();
        if (snap > 0.0f) {
            snap -= partialTick;
        }
        state.strike = snap / 20.0f;
    }

    @Override
    public Identifier getTextureLocation(LOTRStrikeRenderState state) {
        return TEXTURE;
    }
}
