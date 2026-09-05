package lotr.client.render.entity;

import lotr.common.entity.npc.LOTREntityRohanMan;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderRohanTrader extends LOTRRenderRohirrim {
	public ResourceLocation traderOutfit;

	public LOTRRenderRohanTrader(String s) {
		traderOutfit = new ResourceLocation("lotr:mob/rohan/" + s + ".png");
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		LOTREntityRohanMan rohirrim = (LOTREntityRohanMan) entity;
		if (pass == 1 && rohirrim.getEquipmentInSlot(3) == null) {
			setRenderPassModel(outfitModel);
			bindTexture(traderOutfit);
			return 1;
		}
		return super.shouldRenderPass(rohirrim, pass, f);
	}
}
