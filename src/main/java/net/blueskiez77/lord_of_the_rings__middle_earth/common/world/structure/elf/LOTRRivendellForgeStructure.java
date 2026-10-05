package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.world.level.WorldGenLevel;

public class LOTRRivendellForgeStructure extends LOTRHighElvenForgeStructure {
    public LOTRRivendellForgeStructure(boolean flag) {
        super(flag);
        roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        roofMeta = 9;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedCyan");
        tableBlock = LOTRLegacyBlocks.mod("rivendellTable");
    }

    @Override
    public LOTRElfEntity getElf(WorldGenLevel world) {
        return create(LOTREntities.RIVENDELL_SMITH, world);
    }
}
