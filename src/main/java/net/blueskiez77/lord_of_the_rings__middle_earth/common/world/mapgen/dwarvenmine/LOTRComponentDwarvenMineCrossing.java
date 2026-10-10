package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.dwarvenmine;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureBoundingBox;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureComponent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRComponentDwarvenMineCrossing extends LOTRStructureComponent {
    public static final String ID = "LOTR.DwarvenMine.Crossing";

    public int corridorDirection;
    public boolean isMultipleFloors;
    public boolean ruined;

    public LOTRComponentDwarvenMineCrossing() {
    }

    public LOTRComponentDwarvenMineCrossing(int i, RandomSource random, LOTRStructureBoundingBox structureBoundingBox, int j, boolean r) {
        super(i);
        corridorDirection = j;
        boundingBox = structureBoundingBox;
        isMultipleFloors = boundingBox.getYSize() > 3;
        ruined = r;
    }

    public static LOTRStructureBoundingBox findValidPlacement(List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int l) {
        LOTRStructureBoundingBox structureboundingbox = new LOTRStructureBoundingBox(i, j, k, i, j + 2, k);
        if (random.nextInt(4) == 0) {
            structureboundingbox.maxY += 4;
        }
        switch (l) {
            case 0: {
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.maxZ = k + 4;
                break;
            }
            case 1: {
                structureboundingbox.minX = i - 4;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
                break;
            }
            case 2: {
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.minZ = k - 4;
                break;
            }
            case 3: {
                structureboundingbox.maxX = i + 4;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
            }
        }
        return LOTRStructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
    }

    @Override
    public boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox structureBoundingBox) {
        if (isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
            return false;
        }
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.minZ + 1, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.maxZ - 1, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.minZ + 1, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ - 1, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        for (int i = boundingBox.minX; i <= boundingBox.maxX; ++i) {
            for (int j = boundingBox.minZ; j <= boundingBox.maxZ; ++j) {
                BlockState block = getBlockAtCurrentPosition(world, i, boundingBox.minY - 1, j, structureBoundingBox);
                if (isReplaceableOrSand(block)) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, i, boundingBox.minY - 1, j, structureBoundingBox);
                }
                if (!isReplaceableOrSand(block = getBlockAtCurrentPosition(world, i, boundingBox.maxY + 1, j, structureBoundingBox))) {
                    continue;
                }
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, i, boundingBox.maxY + 1, j, structureBoundingBox);
            }
        }
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.minZ - 1, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.maxZ + 1, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, boundingBox.minX - 1, boundingBox.minY - 1, boundingBox.minZ + 2, boundingBox.maxX + 1, boundingBox.minY - 1, boundingBox.minZ + 2, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
        if (!ruined) {
            placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.mod("brick3"), 12, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.minZ + 2, structureBoundingBox);
        }
        return true;
    }

    @Override
    public void buildComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random) {
        int i = getComponentType();
        switch (corridorDirection) {
            case 0: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i, ruined);
                break;
            }
            case 1: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i, ruined);
                break;
            }
            case 2: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i, ruined);
                break;
            }
            case 3: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i, ruined);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i, ruined);
            }
        }
        if (isMultipleFloors) {
            if (random.nextBoolean()) {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ - 1, 2, i, ruined);
            }
            if (random.nextBoolean()) {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 1, i, ruined);
            }
            if (random.nextBoolean()) {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 3, i, ruined);
            }
            if (random.nextBoolean()) {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.maxZ + 1, 0, i, ruined);
            }
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void readExtra(CompoundTag nbt) {
        corridorDirection = nbt.getIntOr("Direction", 0);
        isMultipleFloors = nbt.getBooleanOr("Multiple", false);
        ruined = nbt.getBooleanOr("Ruined", false);
    }

    @Override
    protected void writeExtra(CompoundTag nbt) {
        nbt.putInt("Direction", corridorDirection);
        nbt.putBoolean("Multiple", isMultipleFloors);
        nbt.putBoolean("Ruined", ruined);
    }
}
