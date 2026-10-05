package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.world.level.WorldGenLevel;

public class LOTRBlueMountainsSmithyStructure extends LOTRDwarfSmithyStructure {
    public LOTRBlueMountainsSmithyStructure(boolean flag) {
        super(flag);
        baseBrickBlock = LOTRLegacyBlocks.mod("brick");
        baseBrickMeta = 14;
        carvedBrickBlock = LOTRLegacyBlocks.mod("brick3");
        carvedBrickMeta = 0;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 3;
        tableBlock = LOTRLegacyBlocks.mod("blueDwarvenTable");
        barsBlock = LOTRLegacyBlocks.mod("blueDwarfBars");
    }

    @Override
    public LOTRDwarfEntity createSmith(WorldGenLevel world) {
        return create(LOTREntities.BLUE_DWARF_SMITH, world);
    }

    @Override
    public LOTRChestContents.Pool getChestContents() {
        return LOTRChestContents.BLUE_MOUNTAINS_SMITHY;
    }
}
