package lotr.client.render.entity;

import lotr.common.entity.npc.LOTREntityGondorMan;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderGondorTrader extends LOTRRenderGondorMan {
	public ResourceLocation traderOutfit;

	public LOTRRenderGondorTrader(String s) {
		traderOutfit = new ResourceLocation("lotr:mob/gondor/" + s + ".png");
	}

	@Override
	public int shouldRenderPass(EntityLiving entity, int pass, float f) {
		LOTREntityGondorMan gondorian = (LOTREntityGondorMan) entity;
		if (pass == 1 && gondorian.getEquipmentInSlot(3) == null) {
			setRenderPassModel(outfitModel);
			bindTexture(traderOutfit);
			return 1;
		}
		return super.shouldRenderPass(gondorian, pass, f);
	}
}
