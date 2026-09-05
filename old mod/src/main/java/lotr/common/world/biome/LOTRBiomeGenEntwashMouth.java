package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.spawning.LOTREventSpawner;
import lotr.common.world.structure2.LOTRWorldGenRottenHouse;

public class LOTRBiomeGenEntwashMouth extends LOTRBiomeGenGondor {
	public LOTRBiomeGenEntwashMouth(int i, boolean major) {
		super(i, major);
		npcSpawnList.clear();
		clearBiomeVariants();
		variantChance = 1.0f;
		addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
		decorator.sandPerChunk = 0;
		decorator.quagmirePerChunk = 2;
		decorator.treesPerChunk = 0;
		decorator.willowPerChunk = 1;
		decorator.logsPerChunk = 2;
		decorator.flowersPerChunk = 3;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 10;
		decorator.enableFern = true;
		decorator.waterlilyPerChunk = 2;
		decorator.canePerChunk = 10;
		decorator.reedPerChunk = 4;
		decorator.clearTrees();
		decorator.addTree(LOTRTreeType.OAK_TALL, 100);
		decorator.addTree(LOTRTreeType.OAK_SWAMP, 600);
		decorator.addTree(LOTRTreeType.OAK_LARGE, 400);
		registerSwampFlowers();
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenRottenHouse(false), 500);
		decorator.clearVillages();
		setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
	}

	@Override
	public LOTRAchievement getBiomeAchievement() {
		return LOTRAchievement.enterEntwashMouth;
	}

	@Override
	public LOTRWaypoint.Region getBiomeWaypoints() {
		return null;
	}
}
