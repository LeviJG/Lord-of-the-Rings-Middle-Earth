package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;

public class LOTRRangerTentStructure extends LOTRTentBaseStructure {
    public LOTRRangerTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        int randomWool = random.nextInt(3);
        switch (randomWool) {
            case 0:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 13;
                break;
            case 1:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 12;
                break;
            case 2:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 7;
                break;
            default:
                break;
        }
        fenceBlock = LOTRLegacyBlocks.vanilla("fence");
        fenceMeta = 0;
        tableBlock = LOTRLegacyBlocks.mod("rangerTable");
        chestContents = LOTRChestContents.RANGER_TENT;
    }
}
