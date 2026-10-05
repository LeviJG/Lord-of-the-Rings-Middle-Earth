package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRGundabadForgeTentStructure extends LOTRGundabadTentStructure {
    public LOTRGundabadForgeTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        tentMeta = 0;
        fenceBlock = LOTRLegacyBlocks.vanilla("cobblestone_wall");
        fenceMeta = 0;
        hasOrcForge = true;
    }
}
