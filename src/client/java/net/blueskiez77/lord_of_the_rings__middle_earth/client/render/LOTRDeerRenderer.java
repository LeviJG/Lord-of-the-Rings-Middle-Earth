package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRDeerModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDeerEntity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/** LOTRRenderDeer: shadow 0.5, one of the random deer skins. */
@SuppressWarnings("deprecation")
public class LOTRDeerRenderer extends AgeableMobRenderer<LOTRDeerEntity, LOTRDeerRenderState, LOTRDeerModel> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/deer", "deer");

    public LOTRDeerRenderer(EntityRendererProvider.Context context) {
        super(context,
                new LOTRDeerModel(LOTRDeerModel.createBodyLayer().bakeRoot()),
                new LOTRDeerModel(LOTRDeerModel.createBabyLayer().bakeRoot()),
                0.5f);
    }

    @Override
    public LOTRDeerRenderState createRenderState() {
        return new LOTRDeerRenderState();
    }

    @Override
    public void extractRenderState(LOTRDeerEntity deer, LOTRDeerRenderState state, float partialTick) {
        super.extractRenderState(deer, state, partialTick);
        state.male = deer.isMale();
        state.skin = SKINS.getRandomSkin(deer.getUUID());
    }

    @Override
    public Identifier getTextureLocation(LOTRDeerRenderState state) {
        return state.skin;
    }
}
