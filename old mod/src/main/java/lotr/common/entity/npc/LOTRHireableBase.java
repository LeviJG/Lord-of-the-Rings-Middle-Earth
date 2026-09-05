package lotr.common.entity.npc;

import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.player.EntityPlayer;

public interface LOTRHireableBase {
	boolean canTradeWith(EntityPlayer var1);

	LOTRFaction getFaction();

	String getNPCName();

	void onUnitTrade(EntityPlayer var1);
}
