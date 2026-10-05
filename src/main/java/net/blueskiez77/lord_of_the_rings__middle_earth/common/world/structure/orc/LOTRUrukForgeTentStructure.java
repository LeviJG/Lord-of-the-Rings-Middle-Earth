package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRUrukForgeTentStructure extends LOTRUrukTentStructure {
    public LOTRUrukForgeTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.mod("brick2");
        tentMeta = 7;
        fenceBlock = LOTRLegacyBlocks.mod("wall2");
        fenceMeta = 7;
        hasOrcForge = true;
    }
}
