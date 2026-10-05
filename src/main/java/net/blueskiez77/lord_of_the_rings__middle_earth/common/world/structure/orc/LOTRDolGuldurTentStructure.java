package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRTentBaseStructure;

import net.minecraft.util.RandomSource;

public class LOTRDolGuldurTentStructure extends LOTRTentBaseStructure {
    public LOTRDolGuldurTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        int randomWool = random.nextInt(3);
        switch (randomWool) {
            case 0:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 15;
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
        fenceBlock = LOTRLegacyBlocks.mod("fence");
        fenceMeta = 3;
        tableBlock = LOTRLegacyBlocks.mod("dolGuldurTable");
        chestContents = LOTRChestContents.DOL_GULDUR_TENT;
        hasOrcTorches = true;
    }
}
