package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.dwarvenmine;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureBoundingBox;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureComponent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDwarvenMineEntranceStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRComponentDwarvenMineEntrance extends LOTRStructureComponent {
    public static final String ID = "LOTR.DwarvenMine.Entrance";

    public static final LOTRDwarvenMineEntranceStructure entranceGen = new LOTRDwarvenMineEntranceStructure(false);

    static {
        entranceGen.restrictions = false;
    }

    public int posX;
    public int posY = -1;
    public int posZ;
    public int direction;

    public boolean ruined;

    public LOTRComponentDwarvenMineEntrance() {
    }

    public LOTRComponentDwarvenMineEntrance(int l, RandomSource random, int i, int k, boolean r) {
        super(l);
        boundingBox = new LOTRStructureBoundingBox(i - 4, 40, k - 4, i + 4, 256, k + 4);
        posX = i;
        posZ = k;
        ruined = r;
    }

    @Override
    public boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox structureBoundingBox) {
        if (posY == -1) {
            posY = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, posX, posZ);
        }
        if (!world.getBlockState(new BlockPos(posX, posY - 1, posZ)).is(Blocks.GRASS_BLOCK)) {
            return false;
        }
        entranceGen.isRuined = ruined;
        entranceGen.generateAndFinish(world, random, posX, posY, posZ, direction);
        return true;
    }

    @Override
    public void buildComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random) {
        LOTRStructureBoundingBox structureBoundingBox = null;
        direction = random.nextInt(4);
        switch (direction) {
            case 0: {
                structureBoundingBox = new LOTRStructureBoundingBox(posX - 1, boundingBox.minY + 1, posZ + 4, posX + 1, boundingBox.minY + 4, posZ + 15);
                break;
            }
            case 1: {
                structureBoundingBox = new LOTRStructureBoundingBox(posX - 15, boundingBox.minY + 1, posZ - 1, posX - 4, boundingBox.minY + 4, posZ + 1);
                break;
            }
            case 2: {
                structureBoundingBox = new LOTRStructureBoundingBox(posX - 1, boundingBox.minY + 1, posZ - 15, posX + 1, boundingBox.minY + 4, posZ - 4);
                break;
            }
            case 3: {
                structureBoundingBox = new LOTRStructureBoundingBox(posX + 4, boundingBox.minY + 1, posZ - 1, posX + 15, boundingBox.minY + 4, posZ + 1);
            }
        }
        LOTRComponentDwarvenMineCorridor corridor = new LOTRComponentDwarvenMineCorridor(0, random, structureBoundingBox, direction, ruined);
        list.add(corridor);
        corridor.buildComponent(component, list, random);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void readExtra(CompoundTag nbt) {
        posX = nbt.getIntOr("EntranceX", 0);
        posY = nbt.getIntOr("EntranceY", 0);
        posZ = nbt.getIntOr("EntranceZ", 0);
        direction = nbt.getIntOr("Direction", 0);
        ruined = nbt.getBooleanOr("Ruined", false);
    }

    @Override
    protected void writeExtra(CompoundTag nbt) {
        nbt.putInt("EntranceX", posX);
        nbt.putInt("EntranceY", posY);
        nbt.putInt("EntranceZ", posZ);
        nbt.putInt("Direction", direction);
        nbt.putBoolean("Ruined", ruined);
    }
}
