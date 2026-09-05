package lotr.client.render.entity;

import lotr.client.model.LOTRModelElf;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderElvenTrader extends LOTRRenderElf {
	public ResourceLocation outfitTexture;
	public ModelBiped outfitModel = new LOTRModelElf(0.5f);

	public LOTRRenderElvenTrader(String s) {
		setRenderPassModel(outfitModel);
		outfitTexture = new ResourceLocation("lotr:mob/elf/" + s + ".png");
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
