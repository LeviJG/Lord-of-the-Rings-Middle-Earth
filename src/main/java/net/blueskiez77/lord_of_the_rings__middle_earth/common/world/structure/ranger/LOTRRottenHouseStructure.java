package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

public class LOTRRottenHouseStructure extends LOTRRuinedHouseStructure {
    public LOTRRottenHouseStructure(boolean flag) {
        super(flag);
        woodBlock = LOTRLegacyBlocks.mod("rottenLog");
        woodMeta = 0;
        plankBlock = LOTRLegacyBlocks.mod("planksRotten");
        plankMeta = 0;
        fenceBlock = LOTRLegacyBlocks.mod("fenceRotten");
        fenceMeta = 0;
        stairBlock = LOTRLegacyBlocks.mod("stairsRotten");
    }
}
