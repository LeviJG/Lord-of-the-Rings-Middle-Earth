package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** 1.7.10's WorldGenerator: something placed into the world at a position -- a tree, an ore vein, a structure. */
@FunctionalInterface
public interface LOTRWorldGenerator {

    boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k);
}
