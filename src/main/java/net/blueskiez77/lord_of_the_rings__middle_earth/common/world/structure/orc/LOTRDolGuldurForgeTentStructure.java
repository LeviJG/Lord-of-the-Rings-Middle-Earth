package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRDolGuldurForgeTentStructure extends LOTRDolGuldurTentStructure {
    public LOTRDolGuldurForgeTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.mod("brick2");
        tentMeta = 8;
        fenceBlock = LOTRLegacyBlocks.mod("wall2");
        fenceMeta = 8;
        hasOrcForge = true;
    }
}
