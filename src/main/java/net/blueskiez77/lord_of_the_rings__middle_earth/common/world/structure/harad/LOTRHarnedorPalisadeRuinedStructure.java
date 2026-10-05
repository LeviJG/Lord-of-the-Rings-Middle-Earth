package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRHarnedorPalisadeRuinedStructure extends LOTRHarnedorPalisadeStructure {
    public LOTRHarnedorPalisadeRuinedStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean isRuined() {
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        if (random.nextBoolean()) {
            woodBlock = LOTRLegacyBlocks.mod("wood");
            woodMeta = 3;
        }
    }
}
