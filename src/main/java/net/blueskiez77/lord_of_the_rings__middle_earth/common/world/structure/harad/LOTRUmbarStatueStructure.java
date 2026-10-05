package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.minecraft.util.RandomSource;

public class LOTRUmbarStatueStructure extends LOTRSouthronStatueStructure {
    public LOTRUmbarStatueStructure(boolean flag) {
        super(flag);
    }

    @Override
    public String getRandomStatueStrscan(RandomSource random) {
        String[] statues = {"pillar", "snake", "pharazon"};
        return "umbar_statue_" + statues[random.nextInt(statues.length)];
    }

    @Override
    public boolean isUmbar() {
        return true;
    }
}
