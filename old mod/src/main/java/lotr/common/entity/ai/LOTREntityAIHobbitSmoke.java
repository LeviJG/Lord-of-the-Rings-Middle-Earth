package lotr.common.entity.ai;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import lotr.common.item.LOTRItemHobbitPipe;
import net.minecraft.item.ItemStack;

public class LOTREntityAIHobbitSmoke extends LOTREntityAIConsumeBase {
	public LOTREntityAIHobbitSmoke(LOTREntityNPC entity, int chance) {
		super(entity, null, chance);
	}

	@Override
	public void consume() {
		LOTREntitySmokeRing smoke = new LOTREntitySmokeRing(theEntity.worldObj, theEntity);
		int color = 0;
		ItemStack itemstack = theEntity.getHeldItem();
		if (itemstack != null && itemstack.getItem() instanceof LOTRItemHobbitPipe) {
			color = LOTRItemHobbitPipe.getSmokeColor(itemstack);
		}
		smoke.setSmokeColour(color);
		theEntity.worldObj.spawnEntityInWorld(smoke);
		theEntity.playSound("lotr:item.puff", 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
		theEntity.heal(2.0f);
	}

	@Override
	public ItemStack createConsumable() {
		return new ItemStack(LOTRMod.hobbitPipe);
	}

	@Override
	public void updateConsumeTick(int tick) {
	}
}
