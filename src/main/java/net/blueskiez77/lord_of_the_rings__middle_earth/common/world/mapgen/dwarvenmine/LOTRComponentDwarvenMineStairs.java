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

public class LOTRComponentDwarvenMineStairs extends LOTRStructureComponent {
    public static final String ID = "LOTR.DwarvenMine.Stairs";

    public boolean ruined;

    public LOTRComponentDwarvenMineStairs() {
    }

    public LOTRComponentDwarvenMineStairs(int i, RandomSource random, LOTRStructureBoundingBox structureBoundingBox, int j, boolean r) {
        super(i);
        coordBaseMode = j;
        boundingBox = structureBoundingBox;
        ruined = r;
    }

    public static LOTRStructureBoundingBox findValidPlacement(List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int l) {
        LOTRStructureBoundingBox structureboundingbox = new LOTRStructureBoundingBox(i, j - 5, k, i, j + 2, k);
        switch (l) {
            case 0: {
                structureboundingbox.maxX = i + 2;
                structureboundingbox.maxZ = k + 8;
                break;
            }
            case 1: {
                structureboundingbox.minX = i - 8;
                structureboundingbox.maxZ = k + 2;
                break;
            }
            case 2: {
                structureboundingbox.maxX = i + 2;
                structureboundingbox.minZ = k - 8;
                break;
            }
            case 3: {
                structureboundingbox.maxX = i + 8;
                structureboundingbox.maxZ = k + 2;
            }
        }
        return LOTRStructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
    }

    @Override
    public boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox structureBoundingBox) {
        if (isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
            return false;
        }
        fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 2, 7, 1, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        fillWithBlocks(world, structureBoundingBox, 0, 0, 7, 2, 2, 8, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        for (int i = 0; i < 5; ++i) {
            fillWithBlocks(world, structureBoundingBox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        }
        return true;
    }

    @Override
    public void buildComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random) {
        int i = getComponentType();
        switch (coordBaseMode) {
            case 0: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY, boundingBox.maxZ + 1, 0, i, ruined);
                break;
            }
            case 1: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ, 1, i, ruined);
                break;
            }
            case 2: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY, boundingBox.minZ - 1, 2, i, ruined);
                break;
            }
            case 3: {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ, 3, i, ruined);
            }
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void readExtra(CompoundTag nbt) {
        ruined = nbt.getBooleanOr("Ruined", false);
    }

    @Override
    protected void writeExtra(CompoundTag nbt) {
        nbt.putBoolean("Ruined", ruined);
    }
}
