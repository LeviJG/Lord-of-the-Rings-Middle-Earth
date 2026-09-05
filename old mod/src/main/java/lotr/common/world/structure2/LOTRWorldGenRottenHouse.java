package lotr.common.world.structure2;

import lotr.common.LOTRMod;

public class LOTRWorldGenRottenHouse extends LOTRWorldGenRuinedHouse {
	public LOTRWorldGenRottenHouse(boolean flag) {
		super(flag);
		woodBlock = LOTRMod.rottenLog;
		woodMeta = 0;
		plankBlock = LOTRMod.planksRotten;
		plankMeta = 0;
		fenceBlock = LOTRMod.fenceRotten;
		fenceMeta = 0;
		stairBlock = LOTRMod.stairsRotten;
	}
}
