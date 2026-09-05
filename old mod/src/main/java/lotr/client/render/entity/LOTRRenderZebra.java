package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderZebra extends LOTRRenderHorse {
	public static ResourceLocation zebraTexture = new ResourceLocation("lotr:mob/zebra.png");

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return zebraTexture;
	}
}
