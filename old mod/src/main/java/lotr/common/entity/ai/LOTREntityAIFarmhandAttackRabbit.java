package lotr.common.entity.ai;

import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.npc.LOTREntityNPC;

public class LOTREntityAIFarmhandAttackRabbit extends LOTREntityAINearestAttackableTargetBasic {
	public LOTREntityNPC theNPC;

	public LOTREntityAIFarmhandAttackRabbit(LOTREntityNPC npc) {
		super(npc, LOTREntityRabbit.class, 0, false);
		theNPC = npc;
	}

	@Override
	public double getTargetDistance() {
		return 8.0;
	}

	@Override
	public boolean shouldExecute() {
		if (theNPC.hiredNPCInfo.isActive && !theNPC.hiredNPCInfo.isGuardMode()) {
			return false;
		}
		return super.shouldExecute();
	}
}
