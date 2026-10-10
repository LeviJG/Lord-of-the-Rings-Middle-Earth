package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** LOTRWorldGenDorwinionCamp: a captain's tent with three tents down either side. */
public class LOTRDorwinionCampStructure extends LOTRDorwinionTentStructure {
    public LOTRDorwinionCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        LOTRDorwinionCaptainTentStructure captainTent = new LOTRDorwinionCaptainTentStructure(notifyChanges);
        captainTent.restrictions = true;
        int i1 = 0;
        int k1 = -7;
        int j1 = getTopBlock(world, i1, k1);
        int r = 0;
        if (!generateChild(captainTent, world, random, getX(i1, k1), getY(j1), getZ(i1, k1), (getRotationMode() + r) % 4)) {
            return false;
        }
        int xMin = 8;
        int xMax = 12;
        int zMin = -5;
        int zMax = 5;
        for (int k2 : new int[]{-9, 0, 9}) {
            tryGenerateTent(world, random, new int[]{-xMax, -xMin}, new int[]{k2 + zMin, k2 + zMax}, 3);
            tryGenerateTent(world, random, new int[]{xMin, xMax}, new int[]{k2 + zMin, k2 + zMax}, 1);
        }
        return true;
    }

    public void tryGenerateTent(WorldGenLevel world, RandomSource random, int[] i, int[] k, int r) {
        LOTRDorwinionTentStructure tent = new LOTRDorwinionTentStructure(notifyChanges);
        tent.restrictions = true;
        int attempts = 1;
        for (int l = 0; l < attempts; ++l) {
            int i1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, i[0], i[1]);
            int k1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, k[0], k[1]);
            int j1 = getTopBlock(world, i1, k1);
            if (generateChild(tent, world, random, getX(i1, k1), getY(j1), getZ(i1, k1), (getRotationMode() + r) % 4)) {
                return;
            }
        }
    }
}
