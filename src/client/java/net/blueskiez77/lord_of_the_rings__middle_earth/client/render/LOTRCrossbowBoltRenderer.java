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
 * <p>The original's sheet held two bolts, the poisoned one ten pixels under the
 * plain one, and yOffset picked between them. ArrowModel's UVs are fixed, so the
 * poisoned bolt has that lower half on a sheet of its own.
 */
public class LOTRCrossbowBoltRenderer
        extends ArrowRenderer<LOTRCrossbowBoltEntity, LOTRCrossbowBoltRenderer.BoltRenderState> {

    /** Was lotr:item/crossbowBolt.png, which is not a legal texture path now. */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/projectiles/crossbow_bolt.png");

    private static final Identifier POISONED_TEXTURE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/projectiles/crossbow_bolt_poisoned.png");

    public static class BoltRenderState extends ArrowRenderState {
        public boolean poisoned;
    }

    public LOTRCrossbowBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public BoltRenderState createRenderState() {
        return new BoltRenderState();
    }

    @Override
    public void extractRenderState(LOTRCrossbowBoltEntity bolt, BoltRenderState state, float partialTick) {
        super.extractRenderState(bolt, state, partialTick);
        state.poisoned = bolt.isPoisoned();
    }

    @Override
    protected Identifier getTextureLocation(BoltRenderState state) {
        return state.poisoned ? POISONED_TEXTURE : TEXTURE;
    }
}
