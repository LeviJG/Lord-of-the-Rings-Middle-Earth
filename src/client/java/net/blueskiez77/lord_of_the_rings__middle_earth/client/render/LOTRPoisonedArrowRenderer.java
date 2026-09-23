package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPoisonedArrowEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

/** LOTRRenderArrowPoisoned: vanilla's arrow renderer on the poisoned arrow's own sheet. */
public class LOTRPoisonedArrowRenderer extends ArrowRenderer<LOTRPoisonedArrowEntity, ArrowRenderState> {

    /** Was lotr:item/arrowPoisoned.png. */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/projectiles/arrow_poisoned.png");

    public LOTRPoisonedArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }
}
