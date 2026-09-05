package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;

public class LOTREntityAIHiredRemainStill extends EntityAIBase {
	public LOTREntityNPC theNPC;

	public LOTREntityAIHiredRemainStill(LOTREntityNPC entity) {
		theNPC = entity;
		setMutexBits(6);
	}

	@Override
	public boolean shouldExecute() {
		if (!theNPC.hiredNPCInfo.isActive || theNPC.isInWater() || !theNPC.onGround) {
			return false;
		}
		return theNPC.hiredNPCInfo.isHalted() && (theNPC.getAttackTarget() == null || !theNPC.getAttackTarget().isEntityAlive());
	}

	@Override
	public void startExecuting() {
		theNPC.getNavigator().clearPathEntity();
	}

	@Override
	public void updateTask() {
		theNPC.getNavigator().clearPathEntity();
		Vec3 pos = Vec3.createVectorHelper(theNPC.posX, theNPC.posY + theNPC.getEyeHeight(), theNPC.posZ);
		Vec3 look = theNPC.getLookVec().normalize();
		Vec3 lookUp = pos.addVector(look.xCoord * 3.0, pos.yCoord + 0.25, look.zCoord * 3.0);
		theNPC.getLookHelper().setLookPosition(lookUp.xCoord, lookUp.yCoord, lookUp.zCoord, 20.0f, 20.0f);
	}
}
