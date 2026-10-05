package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRAngmarForgeTentStructure extends LOTRAngmarTentStructure {
    public LOTRAngmarForgeTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.mod("brick2");
        tentMeta = 0;
        fenceBlock = LOTRLegacyBlocks.mod("wall2");
        fenceMeta = 0;
        hasOrcForge = true;
    }
}
