package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRRivendellLordEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRivendellHallStructure extends LOTRHighElvenHallStructure {
    public LOTRRivendellHallStructure(boolean flag) {
        super(flag);
        tableBlock = LOTRLegacyBlocks.mod("rivendellTable");
        bannerType = "RIVENDELL";
        chestContents = LOTRChestContents.RIVENDELL_HALL;
    }

    @Override
    public LOTRElfEntity createElf(WorldGenLevel world) {
        return create(LOTREntities.RIVENDELL_ELF, world);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (super.generate(world, random, i, j, k)) {
            LOTRRivendellLordEntity elfLord = create(LOTREntities.RIVENDELL_LORD, world);
            elfLord.snapTo(i + 6, j + 6, k + 6, 0.0f, 0.0f);
            elfLord.spawnRidingHorse = false;
            elfLord.finalizeSpawn(world, world.getCurrentDifficultyAt(elfLord.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            elfLord.isNPCPersistent = true;
            world.addFreshEntity(elfLord);
            elfLord.setHomeTo(new BlockPos(i + 7, j + 3, k + 7), 16);
        }
        return false;
    }
}
