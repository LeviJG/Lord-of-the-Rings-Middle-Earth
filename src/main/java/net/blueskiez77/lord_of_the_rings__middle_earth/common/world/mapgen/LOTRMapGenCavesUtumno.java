package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumnoLevel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;

import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRMapGenCavesUtumno: caves three times as common and as high as Utumno goes, cutting anything --
 * but never into the solid brick between the levels.
 */
public class LOTRMapGenCavesUtumno extends LOTRMapGenCaves {

    @Override
    protected int caveRarity() {
        return 3;
    }

    @Override
    protected int getCaveGenerationHeight() {
        return this.rand.nextInt(this.rand.nextInt(240) + 8);
    }

    @Override
    protected void digBlock(BlockState[] blockArray, int index, int xzIndex, int j, LOTRBiome biome, boolean cutSurface) {
        if (j < LOTRUtumnoLevel.forY(0).getLowestCorridorFloor() || j > LOTRUtumnoLevel.forY(255).getHighestCorridorRoof()) {
            return;
        }
        for (int l = 0; l < LOTRUtumnoLevel.values().length - 1; ++l) {
            LOTRUtumnoLevel levelUpper = LOTRUtumnoLevel.values()[l];
            LOTRUtumnoLevel levelLower = LOTRUtumnoLevel.values()[l + 1];
            if (j > levelLower.getHighestCorridorRoof() && j < levelUpper.getLowestCorridorFloor()) {
                return;
            }
        }
        blockArray[index] = LOTRChunkTerrain.AIR;
    }
}
