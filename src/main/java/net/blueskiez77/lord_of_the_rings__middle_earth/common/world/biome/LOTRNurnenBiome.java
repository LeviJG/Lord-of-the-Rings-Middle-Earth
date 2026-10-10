package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;



/**
 * LOTRBiomeGenNurnen.
 */
public class LOTRNurnenBiome extends LOTRNurnBiome {

    public LOTRNurnenBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
