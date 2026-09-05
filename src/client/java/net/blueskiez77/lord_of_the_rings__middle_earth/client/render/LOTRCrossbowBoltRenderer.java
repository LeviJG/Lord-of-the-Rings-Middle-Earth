package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRCrossbowBoltEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderCrossbowBolt, which turns out to be vanilla's arrow renderer.
 *
 * <p>The original drew the bolt by hand -- a cross of quads with the UVs
 * {@code (0, 0)-(16, 5)} for the shaft and {@code (0, 5)-(5, 10)} for the head,
 * off a 32x32 sheet -- which is exactly the layout and exactly the geometry
 * ArrowModel already has. So there is nothing to transcribe: point ArrowRenderer
 * at the same texture and it draws the same bolt.
 *
 * <p>The sheet holds two: the plain bolt on the top half, the poisoned one
 * underneath, which is what the original's yOffset picked between. Only the top
 * half is used until crossbowBoltPoisoned is ported.
 */
public class LOTRCrossbowBoltRenderer
        extends ArrowRenderer<LOTRCrossbowBoltEntity, ArrowRenderState> {

    /** Was lotr:item/crossbowBolt.png, which is not a legal texture path now. */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/projectiles/crossbow_bolt.png");

    public LOTRCrossbowBoltRenderer(EntityRendererProvider.Context context) {
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
