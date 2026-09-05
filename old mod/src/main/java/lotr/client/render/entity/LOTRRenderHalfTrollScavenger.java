package lotr.client.render.entity;

import lotr.client.model.LOTRModelHalfTroll;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderHalfTrollScavenger extends LOTRRenderHalfTroll {
	public static ResourceLocation outfitTexture = new ResourceLocation("lotr:mob/halfTroll/scavenger.png");
	public ModelBiped outfitModel = new LOTRModelHalfTroll(0.5f);

	public LOTRRenderHalfTrollScavenger() {
		setRenderPassModel(outfitModel);
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		if (pass == 0) {
			setRenderPassModel(outfitModel);
			bindTexture(outfitTexture);
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
	}
}
