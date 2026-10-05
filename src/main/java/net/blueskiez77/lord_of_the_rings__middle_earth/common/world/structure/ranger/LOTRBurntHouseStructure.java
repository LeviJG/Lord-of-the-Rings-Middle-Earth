package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

public class LOTRBurntHouseStructure extends LOTRRuinedHouseStructure {
    public LOTRBurntHouseStructure(boolean flag) {
        super(flag);
        woodBlock = LOTRLegacyBlocks.mod("wood");
        woodMeta = 3;
        plankBlock = LOTRLegacyBlocks.mod("planks");
        plankMeta = 3;
        fenceBlock = LOTRLegacyBlocks.mod("fence");
        fenceMeta = 3;
        stairBlock = LOTRLegacyBlocks.mod("stairsCharred");
        stoneBlock = LOTRLegacyBlocks.mod("scorchedStone");
        stoneMeta = 0;
        stoneVariantBlock = LOTRLegacyBlocks.mod("scorchedStone");
        stoneVariantMeta = 0;
    }
}
