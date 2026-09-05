package lotr.client.render.entity;

import lotr.common.entity.npc.LOTREntityDunedain;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDunedainTrader extends LOTRRenderDunedain {
	public ResourceLocation traderOutfit;

	public LOTRRenderDunedainTrader(String s) {
		traderOutfit = new ResourceLocation("lotr:mob/ranger/" + s + ".png");
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		LOTREntityDunedain man = (LOTREntityDunedain) entity;
		if (pass == 1 && man.getEquipmentInSlot(3) == null) {
			setRenderPassModel(outfitModel);
			bindTexture(traderOutfit);
			return 1;
		}
		return super.shouldRenderPass(man, pass, f);
	}
}
