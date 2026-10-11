package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** RenderType.create, package-private in 26.2: the mod's own render types (the portals' surfaces). */
@Mixin(RenderType.class)
public interface LOTRRenderTypeInvoker {
    @Invoker("create")
    static RenderType lotr$create(String name, RenderSetup setup) {
        throw new AssertionError();
    }
}
