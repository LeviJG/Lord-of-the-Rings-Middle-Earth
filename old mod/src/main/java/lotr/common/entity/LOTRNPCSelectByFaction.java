package lotr.common.entity;

import lotr.common.LOTRMod;
import lotr.common.fac.LOTRFaction;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

public class LOTRNPCSelectByFaction implements IEntitySelector {
	public LOTRFaction faction;

	public LOTRNPCSelectByFaction(LOTRFaction f) {
		faction = f;
	}

	@Override
	public boolean isEntityApplicable(Entity entity) {
		return entity.isEntityAlive() && matchFaction(entity);
	}

	public boolean matchFaction(Entity entity) {
		return LOTRMod.getNPCFaction(entity) == faction;
	}
}
