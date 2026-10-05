package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGulfVillageSignStructure extends LOTRGulfStructure {
    public String[] signText = LOTRNames.getHaradVillageName(RandomSource.create());

    public LOTRGulfVillageSignStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions && !isSurface(world, i1 = 0, getTopBlock(world, i1, k1 = 0) - 1, k1)) {
            return false;
        }
        for (int j12 = 0; (j12 >= 0 || !isOpaque(world, 0, j12, 0)) && getY(j12) >= world.getMinY(); --j12) {
            setBlockAndMetadata(world, 0, j12, 0, boneBlock, boneMeta);
            setGrassToDirt(world, 0, j12 - 1, 0);
        }
        setBlockAndMetadata(world, 0, 1, 0, boneBlock, boneMeta);
        setBlockAndMetadata(world, 0, 2, 0, boneBlock, boneMeta);
        setBlockAndMetadata(world, 0, 3, 0, boneBlock, boneMeta);
        setBlockAndMetadata(world, 0, 4, 0, boneBlock, boneMeta);
        setBlockAndMetadata(world, -1, 4, 0, boneWallBlock, boneWallMeta);
        setBlockAndMetadata(world, 1, 4, 0, boneWallBlock, boneWallMeta);
        setBlockAndMetadata(world, 0, 4, -1, boneWallBlock, boneWallMeta);
        setBlockAndMetadata(world, 0, 4, 1, boneWallBlock, boneWallMeta);
        placeSkull(world, random, -1, 5, 0);
        placeSkull(world, random, 1, 5, 0);
        placeSkull(world, random, 0, 5, -1);
        placeSkull(world, random, 0, 5, 1);
        setBlockAndMetadata(world, -1, 3, 0, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, 1, 3, 0, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 0, 3, -1, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 3, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        if (signText != null) {
            placeSign(world, -1, 2, 0, LOTRLegacyBlocks.vanilla("wall_sign"), 5, signText);
            placeSign(world, 1, 2, 0, LOTRLegacyBlocks.vanilla("wall_sign"), 4, signText);
            placeSign(world, 0, 2, -1, LOTRLegacyBlocks.vanilla("wall_sign"), 2, signText);
            placeSign(world, 0, 2, 1, LOTRLegacyBlocks.vanilla("wall_sign"), 3, signText);
        }
        return true;
    }

    public LOTRGulfVillageSignStructure setSignText(String[] s) {
        signText = s;
        return this;
    }
}
