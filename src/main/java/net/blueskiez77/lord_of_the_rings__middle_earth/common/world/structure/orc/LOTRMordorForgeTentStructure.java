package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRMordorForgeTentStructure extends LOTRMordorTentStructure {
    public LOTRMordorForgeTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.mod("brick");
        tentMeta = 0;
        fenceBlock = LOTRLegacyBlocks.mod("wall");
        fenceMeta = 1;
        hasOrcForge = true;
    }
}
