package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRFlamingoModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRFlamingoEntity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/** LOTRRenderFlamingo: shadow 0.5, the chick's own texture, and the chicken's wing flap. */
@SuppressWarnings("deprecation")
public class LOTRFlamingoRenderer extends AgeableMobRenderer<LOTRFlamingoEntity, LOTRFlamingoRenderState, LOTRFlamingoModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/flamingo/flamingo.png");
    private static final Identifier TEXTURE_CHICK =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/flamingo/chick.png");

    public LOTRFlamingoRenderer(EntityRendererProvider.Context context) {
        super(context,
                new LOTRFlamingoModel(LOTRFlamingoModel.createBodyLayer().bakeRoot()),
                new LOTRFlamingoModel(LOTRFlamingoModel.createBabyLayer().bakeRoot()),
                0.5f);
    }

    @Override
    public LOTRFlamingoRenderState createRenderState() {
        return new LOTRFlamingoRenderState();
    }

    @Override
    public void extractRenderState(LOTRFlamingoEntity flamingo, LOTRFlamingoRenderState state, float partialTick) {
        super.extractRenderState(flamingo, state, partialTick);
        float flap = Mth.lerp(partialTick, flamingo.oFlap, flamingo.flap);
        float flapSpeed = Mth.lerp(partialTick, flamingo.oFlapSpeed, flamingo.flapSpeed);
        state.flap = (Mth.sin(flap) + 1.0f) * flapSpeed;
        int cur = flamingo.getFishingTickCur();
        int pre = flamingo.getFishingTickPre();
        state.fishingCur = cur;
        state.fishing = pre + (cur - pre) * partialTick;
    }

    @Override
    public Identifier getTextureLocation(LOTRFlamingoRenderState state) {
        return state.isBaby ? TEXTURE_CHICK : TEXTURE;
    }
}
