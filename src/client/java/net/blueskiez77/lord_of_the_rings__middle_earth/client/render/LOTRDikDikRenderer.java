package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRDikDikModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDikDikEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

/** LOTRRenderDikDik: shadow 0.8, one of the random dik-dik skins. */
public class LOTRDikDikRenderer extends MobRenderer<LOTRDikDikEntity, LOTRSkinnedRenderState, LOTRDikDikModel> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/dikdik", "dikdik");

    public LOTRDikDikRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRDikDikModel(LOTRDikDikModel.createBodyLayer().bakeRoot()), 0.8f);
    }

    @Override
    public LOTRSkinnedRenderState createRenderState() {
        return new LOTRSkinnedRenderState();
    }

    @Override
    public void extractRenderState(LOTRDikDikEntity entity, LOTRSkinnedRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.skin = SKINS.getRandomSkin(entity.getUUID());
    }

    @Override
    public Identifier getTextureLocation(LOTRSkinnedRenderState state) {
        return state.skin;
    }
}
