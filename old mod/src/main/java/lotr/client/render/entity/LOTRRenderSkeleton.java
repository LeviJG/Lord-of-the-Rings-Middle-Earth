package lotr.client.render.entity;

import lotr.client.model.LOTRModelSkeleton;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderSkeleton extends RenderBiped {
	public static ResourceLocation skin = new ResourceLocation("textures/entity/skeleton/skeleton.png");

	public LOTRRenderSkeleton() {
		super(new LOTRModelSkeleton(), 0.5f);
	}

	@Override
	public void func_82421_b() {
		field_82423_g = new LOTRModelSkeleton(1.0f);
		field_82425_h = new LOTRModelSkeleton(0.5f);
	}

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return skin;
	}
}
