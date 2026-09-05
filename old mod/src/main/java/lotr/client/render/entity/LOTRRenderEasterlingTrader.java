package lotr.client.render.entity;

import lotr.common.entity.npc.LOTREntityEasterling;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderEasterlingTrader extends LOTRRenderEasterling {
	public ResourceLocation traderOutfit;

	public LOTRRenderEasterlingTrader(String s) {
		traderOutfit = new ResourceLocation("lotr:mob/rhun/" + s + ".png");
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		LOTREntityEasterling easterling = (LOTREntityEasterling) entity;
		if (pass == 1 && easterling.getEquipmentInSlot(3) == null) {
			setRenderPassModel(outfitModel);
			bindTexture(traderOutfit);
			return 1;
		}
		return super.shouldRenderPass(easterling, pass, f);
	}
}
