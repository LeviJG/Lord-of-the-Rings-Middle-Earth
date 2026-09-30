package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSwanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRSwanEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/** LOTRRenderSwan: shadow 0.5. */
public class LOTRSwanRenderer extends MobRenderer<LOTRSwanEntity, LOTRSwanRenderState, LOTRSwanModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/swan.png");

    public LOTRSwanRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRSwanModel(LOTRSwanModel.createBodyLayer().bakeRoot()), 0.5f);
    }

    @Override
    public LOTRSwanRenderState createRenderState() {
        return new LOTRSwanRenderState();
    }

    @Override
    public void extractRenderState(LOTRSwanEntity swan, LOTRSwanRenderState state, float partialTick) {
        super.extractRenderState(swan, state, partialTick);
        state.flapPhase = Mth.lerp(partialTick, swan.prevFlapPhase, swan.flapPhase);
        state.flapPower = Mth.lerp(partialTick, swan.prevFlapPower, swan.flapPower);
        state.peckAngle = swan.getPeckAngle(partialTick);
    }

    @Override
    public Identifier getTextureLocation(LOTRSwanRenderState state) {
        return TEXTURE;
    }
}
