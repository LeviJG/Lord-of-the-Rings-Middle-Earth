package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAIGollumRemainStill extends EntityAIBase {
	public LOTREntityGollum theGollum;

	public LOTREntityAIGollumRemainStill(LOTREntityGollum entity) {
		theGollum = entity;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (theGollum.getGollumOwner() == null || theGollum.isInWater() || !theGollum.onGround) {
			return false;
		}
		return theGollum.isGollumSitting();
	}

	@Override
	public void startExecuting() {
		theGollum.getNavigator().clearPathEntity();
	}
}
