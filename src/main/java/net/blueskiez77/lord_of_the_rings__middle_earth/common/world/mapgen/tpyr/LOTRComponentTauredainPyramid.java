package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.tpyr;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureBoundingBox;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureComponent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainPyramidStructure;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRComponentTauredainPyramid extends LOTRStructureComponent {
    public static final String ID = "LOTR.TPyr.Pyramid";

    public static final LOTRTauredainPyramidStructure pyramidGen = new LOTRTauredainPyramidStructure(false);
    public static final RandomSource pyramidRand = new LegacyRandomSource(0L);

    static {
        pyramidGen.restrictions = false;
    }

    public int posX;
    public int posY = -1;
    public int posZ;
    public int direction;

    public long pyramidSeed = -1L;

    public LOTRComponentTauredainPyramid() {
    }

    public LOTRComponentTauredainPyramid(int l, RandomSource random, int i, int k) {
        super(l);
        int r = LOTRTauredainPyramidStructure.RADIUS + 5;
        boundingBox = new LOTRStructureBoundingBox(i - r, 0, k - r, i + r, 255, k + r);
        posX = i;
        posZ = k;
        direction = random.nextInt(4);
    }

    @Override
    public boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox structureBoundingBox) {
        if (posY == -1) {
            posY = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, structureBoundingBox.getCenterX(), structureBoundingBox.getCenterZ());
        }
        if (pyramidSeed == -1L) {
            pyramidSeed = random.nextLong();
        }
        LOTRStructureBoundingBox bb = structureBoundingBox;
        pyramidGen.setStructureBB(new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ));
        pyramidRand.setSeed(pyramidSeed);
        pyramidGen.generateAndFinish(world, pyramidRand, posX, posY, posZ, direction);
        return true;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void readExtra(CompoundTag nbt) {
        posX = nbt.getIntOr("PyrX", 0);
        posY = nbt.getIntOr("PyrY", 0);
        posZ = nbt.getIntOr("PyrZ", 0);
        direction = nbt.getIntOr("Direction", 0);
        pyramidSeed = nbt.getLongOr("Seed", 0L);
    }

    @Override
    protected void writeExtra(CompoundTag nbt) {
        nbt.putInt("PyrX", posX);
        nbt.putInt("PyrY", posY);
        nbt.putInt("PyrZ", posZ);
        nbt.putInt("Direction", direction);
        nbt.putLong("Seed", pyramidSeed);
    }
}
