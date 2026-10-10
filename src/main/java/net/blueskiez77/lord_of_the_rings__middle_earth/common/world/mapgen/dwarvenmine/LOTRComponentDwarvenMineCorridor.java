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

public class LOTRComponentDwarvenMineCorridor extends LOTRStructureComponent {
    public static final String ID = "LOTR.DwarvenMine.Corridor";

    public int sectionCount;
    public boolean ruined;

    public LOTRComponentDwarvenMineCorridor() {
    }

    public LOTRComponentDwarvenMineCorridor(int i, RandomSource random, LOTRStructureBoundingBox structureBoundingBox, int j, boolean r) {
        super(i);
        coordBaseMode = j;
        boundingBox = structureBoundingBox;
        sectionCount = coordBaseMode != 2 && coordBaseMode != 0 ? boundingBox.getXSize() / 4 : boundingBox.getZSize() / 4;
        ruined = r;
    }

    public static LOTRStructureBoundingBox findValidPlacement(List<LOTRStructureComponent> list, RandomSource random, int i, int j, int k, int l) {
        int i1;
        LOTRStructureBoundingBox structureboundingbox = new LOTRStructureBoundingBox(i, j, k, i, j + 3, k);
        for (i1 = random.nextInt(3) + 2; i1 > 0; --i1) {
            int j1 = i1 * 4;
            switch (l) {
                case 0: {
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.maxZ = k + j1 - 1;
                    break;
                }
                case 1: {
                    structureboundingbox.minX = i - (j1 - 1);
                    structureboundingbox.maxZ = k + 2;
                    break;
                }
                case 2: {
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.minZ = k - (j1 - 1);
                    break;
                }
                case 3: {
                    structureboundingbox.maxX = i + j1 - 1;
                    structureboundingbox.maxZ = k + 2;
                }
            }
            if (LOTRStructureComponent.findIntersecting(list, structureboundingbox) == null) {
                break;
            }
        }
        return i1 > 0 ? structureboundingbox : null;
    }

    @Override
    public boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox structureBoundingBox) {
        if (isLiquidInStructureBoundingBox(world, structureBoundingBox)) {
            return false;
        }
        int length = sectionCount * 4 - 1;
        fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 2, 2, length, LOTRLegacyBlocks.vanilla("air"), LOTRLegacyBlocks.vanilla("air"), false);
        for (int l = 0; l < sectionCount; ++l) {
            int k = 2 + l * 4;
            for (int i : new int[]{0, 2}) {
                int wallHeight = ruined ? random.nextInt(3) : 2;
                for (int j = 0; j <= wallHeight; ++j) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.mod("wall"), 7, i, j, k, structureBoundingBox);
                }
            }
            fillWithBlocks(world, structureBoundingBox, -1, 0, k, -1, 2, k, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
            fillWithBlocks(world, structureBoundingBox, 3, 0, k, 3, 2, k, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
            fillWithBlocks(world, structureBoundingBox, 1, -1, k - 2, 1, -1, k + 2, LOTRLegacyBlocks.mod("pillar"), LOTRLegacyBlocks.vanilla("air"), false);
            if (!getBlockAtCurrentPosition(world, 1, -1, k - 3, structureBoundingBox).isAir()) {
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.mod("pillar"), 0, 1, -1, k - 3, structureBoundingBox);
            }
            if (!getBlockAtCurrentPosition(world, 1, -1, k + 3, structureBoundingBox).isAir()) {
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.mod("pillar"), 0, 1, -1, k + 3, structureBoundingBox);
            }
            if (!ruined) {
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.mod("brick3"), 12, 1, -1, k, structureBoundingBox);
                if (random.nextInt(80) == 0) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("crafting_table"), 0, 2, 0, k - 1, structureBoundingBox);
                }
                if (random.nextInt(80) == 0) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("crafting_table"), 0, 0, 0, k + 1, structureBoundingBox);
                }
            }
            if (random.nextInt(120) == 0) {
                generateStructureChestContents(world, structureBoundingBox, random, 2, 0, k - 1, LOTRChestContents.DWARVEN_MINE_CORRIDOR, LOTRChestContents.getRandomItemAmount(LOTRChestContents.DWARVEN_MINE_CORRIDOR, random));
            }
            if (random.nextInt(120) != 0) {
                continue;
            }
            generateStructureChestContents(world, structureBoundingBox, random, 0, 0, k + 1, LOTRChestContents.DWARVEN_MINE_CORRIDOR, LOTRChestContents.getRandomItemAmount(LOTRChestContents.DWARVEN_MINE_CORRIDOR, random));
        }
        for (int k = 0; k <= length; ++k) {
            BlockState block;
            for (int i = -1; i <= 3; ++i) {
                int j;
                block = getBlockAtCurrentPosition(world, i, -1, k, structureBoundingBox);
                if (isReplaceableOrSand(block)) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, i, -1, k, structureBoundingBox);
                }
                if (!isReplaceableOrSand(block = getBlockAtCurrentPosition(world, i, j = 3, k, structureBoundingBox))) {
                    continue;
                }
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, i, j, k, structureBoundingBox);
            }
            for (int j = 0; j <= 2; ++j) {
                block = getBlockAtCurrentPosition(world, -1, j, k, structureBoundingBox);
                if (isReplaceableOrSand(block)) {
                    placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, -1, j, k, structureBoundingBox);
                }
                if (!isReplaceableOrSand(block = getBlockAtCurrentPosition(world, 3, j, k, structureBoundingBox))) {
                    continue;
                }
                placeBlockAtCurrentPosition(world, LOTRLegacyBlocks.vanilla("stone"), 0, 3, j, k, structureBoundingBox);
            }
        }
        return true;
    }

    @Override
    public void buildComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random) {
        block24:
        {
            int i = getComponentType();
            int j = random.nextInt(4);
            switch (coordBaseMode) {
                case 0: {
                    if (j <= 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, coordBaseMode, i, ruined);
                        break;
                    }
                    if (j == 2) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ - 3, 1, i, ruined);
                        break;
                    }
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ - 3, 3, i, ruined);
                    break;
                }
                case 1: {
                    if (j <= 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, coordBaseMode, i, ruined);
                        break;
                    }
                    if (j == 2) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, 2, i, ruined);
                        break;
                    }
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, 0, i, ruined);
                    break;
                }
                case 2: {
                    if (j <= 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, coordBaseMode, i, ruined);
                        break;
                    }
                    if (j == 2) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, 1, i, ruined);
                        break;
                    }
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, 3, i, ruined);
                    break;
                }
                case 3: {
                    if (j <= 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, coordBaseMode, i, ruined);
                        break;
                    }
                    if (j == 2) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX - 3, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, 2, i, ruined);
                        break;
                    }
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX - 3, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, 0, i, ruined);
                }
            }
            if (i >= 12) {
                break block24;
            }
            if (coordBaseMode != 2 && coordBaseMode != 0) {
                int k = boundingBox.minX + 3;
                while (k + 3 <= boundingBox.maxX) {
                    int l = random.nextInt(5);
                    if (l == 0) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, k, boundingBox.minY, boundingBox.minZ - 1, 2, i + 1, ruined);
                    } else if (l == 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, k, boundingBox.minY, boundingBox.maxZ + 1, 0, i + 1, ruined);
                    }
                    k += 4;
                }
            } else {
                int k = boundingBox.minZ + 3;
                while (k + 3 <= boundingBox.maxZ) {
                    int l = random.nextInt(5);
                    if (l == 0) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, k, 1, i + 1, ruined);
                    } else if (l == 1) {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, k, 3, i + 1, ruined);
                    }
                    k += 4;
                }
            }
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void readExtra(CompoundTag nbt) {
        sectionCount = nbt.getIntOr("Sections", 0);
        ruined = nbt.getBooleanOr("Ruined", false);
    }

    @Override
    protected void writeExtra(CompoundTag nbt) {
        nbt.putInt("Sections", sectionCount);
        nbt.putBoolean("Ruined", ruined);
    }
}
