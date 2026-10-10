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

public class LOTRStructureDwarvenMinePieces {
    public static void getNextComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int direction, int iteration, boolean ruined) {
        getNextMineComponent(component, list, random, i, j, k, direction, iteration, ruined);
    }

    public static void getNextMineComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int direction, int iteration, boolean ruined) {
        if (iteration > 12) {
            return;
        }
        if (Math.abs(i - component.getBoundingBox().minX) <= 80 && Math.abs(k - component.getBoundingBox().minZ) <= 80) {
            LOTRStructureComponent structurecomponent1 = getRandomComponent(list, random, i, j, k, direction, iteration + 1, ruined);
            if (structurecomponent1 != null) {
                list.add(structurecomponent1);
                structurecomponent1.buildComponent(component, list, random);
            }
        }
    }

    public static LOTRStructureComponent getRandomComponent(List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int direction, int iteration, boolean ruined) {
        int l = random.nextInt(100);
        if (l >= 80) {
            LOTRStructureBoundingBox structureboundingbox = LOTRComponentDwarvenMineCrossing.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null) {
                return new LOTRComponentDwarvenMineCrossing(iteration, random, structureboundingbox, direction, ruined);
            }
        } else if (l >= 70) {
            LOTRStructureBoundingBox structureboundingbox = LOTRComponentDwarvenMineStairs.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null) {
                return new LOTRComponentDwarvenMineStairs(iteration, random, structureboundingbox, direction, ruined);
            }
        } else {
            LOTRStructureBoundingBox structureboundingbox = LOTRComponentDwarvenMineCorridor.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null) {
                return new LOTRComponentDwarvenMineCorridor(iteration, random, structureboundingbox, direction, ruined);
            }
        }
        return null;
    }
}
