package lotr.common.world.structure2;

import lotr.common.LOTRMod;

public class LOTRWorldGenBurntHouse extends LOTRWorldGenRuinedHouse {
	public LOTRWorldGenBurntHouse(boolean flag) {
		super(flag);
		woodBlock = LOTRMod.wood;
		woodMeta = 3;
		plankBlock = LOTRMod.planks;
		plankMeta = 3;
		fenceBlock = LOTRMod.fence;
		fenceMeta = 3;
		stairBlock = LOTRMod.stairsCharred;
		stoneBlock = LOTRMod.scorchedStone;
		stoneMeta = 0;
		stoneVariantBlock = LOTRMod.scorchedStone;
		stoneVariantMeta = 0;
	}
}
