package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAINPCFollowSpouse extends EntityAIBase {
	public LOTREntityNPC theNPC;
	public LOTREntityNPC theSpouse;
	public double moveSpeed;
	public int followTick;

	public LOTREntityAINPCFollowSpouse(LOTREntityNPC npc, double d) {
		theNPC = npc;
		moveSpeed = d;
	}

	@Override
	public boolean continueExecuting() {
		if (!theSpouse.isEntityAlive()) {
			return false;
		}
		double d = theNPC.getDistanceSqToEntity(theSpouse);
		return d >= 36.0 && d <= 256.0;
	}

	@Override
	public void resetTask() {
		theSpouse = null;
	}

	@Override
	public boolean shouldExecute() {
		LOTREntityNPC spouse = theNPC.familyInfo.getSpouse();
		if (spouse == null || !spouse.isEntityAlive() || theNPC.getDistanceSqToEntity(spouse) < 36.0 || theNPC.getDistanceSqToEntity(spouse) >= 256.0) {
			return false;
		}
		theSpouse = spouse;
		return true;
	}

	@Override
	public void startExecuting() {
		followTick = 200;
	}

	@Override
	public void updateTask() {
		--followTick;
		if (theNPC.getDistanceSqToEntity(theSpouse) > 144.0 || followTick <= 0) {
			followTick = 200;
			theNPC.getNavigator().tryMoveToEntityLiving(theSpouse, moveSpeed);
		}
	}
}
