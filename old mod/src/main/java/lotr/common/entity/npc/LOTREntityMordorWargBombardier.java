package lotr.common.entity.npc;

import lotr.common.fac.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityMordorWargBombardier extends LOTREntityWargBombardier {
	public LOTREntityMordorWargBombardier(World world) {
		super(world);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		setWargType(LOTREntityWarg.WargType.BLACK);
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.MORDOR;
	}
}
