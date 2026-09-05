package lotr.client.render.entity;

import lotr.common.entity.npc.LOTREntityNearHaradrimBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderHaradrimTrader extends LOTRRenderNearHaradrim {
	public ResourceLocation traderOutfit;

	public LOTRRenderHaradrimTrader(String s) {
		traderOutfit = new ResourceLocation("lotr:mob/nearHarad/" + s + ".png");
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		LOTREntityNearHaradrimBase haradrim = (LOTREntityNearHaradrimBase) entity;
		if (pass == 1 && haradrim.getEquipmentInSlot(3) == null) {
			setRenderPassModel(outfitModel);
			bindTexture(traderOutfit);
			return 1;
		}
		return super.shouldRenderPass(haradrim, pass, f);
	}
}
