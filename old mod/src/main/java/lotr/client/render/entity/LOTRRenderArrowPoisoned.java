package lotr.client.render.entity;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderArrowPoisoned extends RenderArrow {
	public static ResourceLocation arrowPoisonTexture = new ResourceLocation("lotr:item/arrowPoisoned.png");

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return arrowPoisonTexture;
	}
}
