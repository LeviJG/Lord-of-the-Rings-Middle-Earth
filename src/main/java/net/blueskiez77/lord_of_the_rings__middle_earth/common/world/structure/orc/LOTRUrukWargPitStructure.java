package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRUrukWargPitStructure extends LOTRWargPitBaseStructure {
    public LOTRUrukWargPitStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRNPCEntity getOrc(WorldGenLevel world) {
        return create(LOTREntities.ISENGARD_SNAGA, world);
    }

    @Override
    public LOTRNPCEntity getWarg(WorldGenLevel world) {
        return create(LOTREntities.URUK_WARG, world);
    }

    @Override
    public void setOrcSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.ISENGARD_SNAGA);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("brick2");
        brickMeta = 7;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
        brickSlabMeta = 4;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsUrukBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall2");
        brickWallMeta = 7;
        pillarBlock = beamBlock;
        pillarMeta = beamMeta;
        woolBlock = LOTRLegacyBlocks.vanilla("wool");
        woolMeta = 12;
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 12;
        barsBlock = LOTRLegacyBlocks.mod("urukBars");
        gateOrcBlock = LOTRLegacyBlocks.mod("gateUruk");
        tableBlock = LOTRLegacyBlocks.mod("urukTable");
        banner = "ISENGARD";
        chestContents = LOTRChestContents.URUK_TENT;
    }

    @Override
    public void setWargSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.URUK_WARG);
    }
}
