package lotr.common.world.structure2;

import lotr.common.LOTRMod;

public class LOTRWorldGenTowerHillsTower extends LOTRWorldGenHighElvenTower {
	public LOTRWorldGenTowerHillsTower(boolean flag) {
		super(flag);
		brickBlock = LOTRMod.brick4;
		brickMeta = 15;
		brickSlabBlock = LOTRMod.slabSingle9;
		brickSlabMeta = 0;
		brickStairBlock = LOTRMod.stairsChalkBrick;
		brickWallBlock = LOTRMod.wall3;
		brickWallMeta = 6;
		pillarBlock = LOTRMod.pillar2;
		pillarMeta = 1;
		floorBlock = LOTRMod.slabDouble8;
		floorMeta = 7;
		roofBlock = brickBlock;
		roofMeta = brickMeta;
		roofSlabBlock = brickSlabBlock;
		roofSlabMeta = brickSlabMeta;
		roofStairBlock = brickStairBlock;
	}
}
