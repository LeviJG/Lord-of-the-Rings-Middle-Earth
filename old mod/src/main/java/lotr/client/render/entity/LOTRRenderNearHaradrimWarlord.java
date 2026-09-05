package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderNearHaradrimWarlord extends LOTRRenderNearHaradrim {
	public static ResourceLocation skin = new ResourceLocation("lotr:mob/nearHarad/warlord.png");

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return skin;
	}
}
