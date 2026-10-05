package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRForgeBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRDwarfSmithyStructure extends LOTRStructureBase2 {
    public LegacyBlock baseBrickBlock = LOTRLegacyBlocks.vanilla("stonebrick");
    public int baseBrickMeta;
    public LegacyBlock brickBlock = LOTRLegacyBlocks.mod("brick");
    public int brickMeta = 6;
    public LegacyBlock brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
    public int brickSlabMeta = 7;
    public LegacyBlock brickStairBlock = LOTRLegacyBlocks.mod("stairsDwarvenBrick");
    public LegacyBlock carvedBrickBlock = LOTRLegacyBlocks.mod("brick2");
    public int carvedBrickMeta = 12;
    public LegacyBlock pillarBlock = LOTRLegacyBlocks.mod("pillar");
    public int pillarMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock gateBlock;
    public LegacyBlock tableBlock = LOTRLegacyBlocks.mod("dwarvenTable");
    public LegacyBlock barsBlock = LOTRLegacyBlocks.mod("dwarfBars");

    public LOTRDwarfSmithyStructure(boolean flag) {
        super(flag);
    }

    public LOTRDwarfEntity createSmith(WorldGenLevel world) {
        return create(LOTREntities.DWARF_SMITH, world);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k2;
        int k1;
        int i1;
        int j1;
        int k12;
        int i2;
        setOriginAndRotation(world, i, j, k, rotation, 5);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i12 = -4; i12 <= 4; ++i12) {
                for (int k13 = -4; k13 <= 4; ++k13) {
                    j1 = getTopBlock(world, i12, k13);
                    BlockState block = getBlockState(world, i12, j1 - 1, k13);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 5) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            for (k1 = -4; k1 <= 4; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 + k2 > 6) {
                    continue;
                }
                layFoundation(world, i1, k1);
                for (j1 = 1; j1 <= 5; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 4 || k2 == 4) {
                    setBlockAndMetadata(world, i1, 1, k1, baseBrickBlock, baseBrickMeta);
                    setBlockAndMetadata(world, i1, 2, k1, plankBlock, plankMeta);
                    setBlockAndMetadata(world, i1, 3, k1, brickBlock, brickMeta);
                }
                if (i2 != 3 || k2 != 3) {
                    continue;
                }
                for (j1 = 1; j1 <= 3; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, pillarBlock, pillarMeta);
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            setBlockAndMetadata(world, i1, 3, -3, brickStairBlock, 7);
            setBlockAndMetadata(world, i1, 3, 3, brickStairBlock, 6);
        }
        for (k12 = -2; k12 <= 2; ++k12) {
            setBlockAndMetadata(world, -3, 3, k12, brickStairBlock, 4);
            setBlockAndMetadata(world, 3, 3, k12, brickStairBlock, 5);
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                setBlockAndMetadata(world, i1, 4, k1, brickBlock, brickMeta);
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            setBlockAndMetadata(world, i1, 4, -4, brickStairBlock, 2);
            setBlockAndMetadata(world, i1, 4, 4, brickStairBlock, 3);
        }
        for (k12 = -2; k12 <= 2; ++k12) {
            setBlockAndMetadata(world, -4, 4, k12, brickStairBlock, 1);
            setBlockAndMetadata(world, 4, 4, k12, brickStairBlock, 0);
        }
        setBlockAndMetadata(world, -4, 4, 2, brickStairBlock, 3);
        setBlockAndMetadata(world, -3, 4, 2, brickStairBlock, 1);
        setBlockAndMetadata(world, -3, 4, 3, brickStairBlock, 3);
        setBlockAndMetadata(world, -2, 4, 3, brickStairBlock, 1);
        setBlockAndMetadata(world, 4, 4, 2, brickStairBlock, 3);
        setBlockAndMetadata(world, 3, 4, 2, brickStairBlock, 0);
        setBlockAndMetadata(world, 3, 4, 3, brickStairBlock, 3);
        setBlockAndMetadata(world, 2, 4, 3, brickStairBlock, 0);
        setBlockAndMetadata(world, -4, 4, -2, brickStairBlock, 2);
        setBlockAndMetadata(world, -3, 4, -2, brickStairBlock, 1);
        setBlockAndMetadata(world, -3, 4, -3, brickStairBlock, 2);
        setBlockAndMetadata(world, -2, 4, -3, brickStairBlock, 1);
        setBlockAndMetadata(world, 4, 4, -2, brickStairBlock, 2);
        setBlockAndMetadata(world, 3, 4, -2, brickStairBlock, 0);
        setBlockAndMetadata(world, 3, 4, -3, brickStairBlock, 2);
        setBlockAndMetadata(world, 2, 4, -3, brickStairBlock, 0);
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = 2; k1 <= 4; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1 - 3);
                if (i2 == 1 && k2 == 1) {
                    setBlockAndMetadata(world, i1, 5, k1, brickSlabBlock, brickSlabMeta);
                    continue;
                }
                if (i2 == 1 || k2 == 1) {
                    setBlockAndMetadata(world, i1, 5, k1, brickBlock, brickMeta);
                    continue;
                }
                if (i2 != 0 || k2 != 0) {
                    continue;
                }
                setAir(world, i1, 3, k1);
                setAir(world, i1, 4, k1);
            }
            setBlockAndMetadata(world, i1, 4, 4, brickBlock, brickMeta);
            for (int j12 = 1; j12 <= 2; ++j12) {
                setBlockAndMetadata(world, i1, j12, 4, brickBlock, brickMeta);
            }
        }
        setBlockAndMetadata(world, 0, 6, 2, brickStairBlock, 2);
        setBlockAndMetadata(world, -1, 6, 3, brickStairBlock, 1);
        setBlockAndMetadata(world, 1, 6, 3, brickStairBlock, 0);
        setBlockAndMetadata(world, 0, 6, 4, brickStairBlock, 3);
        setBlockAndMetadata(world, 0, 1, -4, gateBlock, 0);
        setAir(world, 0, 2, -4);
        setBlockAndMetadata(world, -2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, 0, 1, -1, LOTRLegacyBlocks.vanilla("anvil"), 1);
        for (int i13 : new int[]{-3, 3}) {
            setBlockAndMetadata(world, i13, 1, -1, LOTRLegacyBlocks.vanilla("anvil"), 0);
            setBlockAndMetadata(world, i13, 1, 0, tableBlock, 0);
            setBlockAndMetadata(world, i13, 1, 2, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        }
        setBlockAndMetadata(world, -3, 1, -2, LOTRLegacyBlocks.mod("unsmeltery"), 4);
        setBlockAndMetadata(world, 3, 1, -2, LOTRLegacyBlocks.mod("unsmeltery"), 5);
        placeChest(world, random, -3, 1, 1, 4, getChestContents());
        placeChest(world, random, 3, 1, 1, 5, getChestContents());
        placeDwarfForge(world, random, 0, 1, 2, 2);
        placeDwarfForge(world, random, -1, 1, 3, 5);
        placeDwarfForge(world, random, 1, 1, 3, 4);
        for (int i13 : new int[]{-1, 1}) {
            setBlockAndMetadata(world, i13, 1, 2, brickBlock, brickMeta);
            setBlockAndMetadata(world, i13, 2, 2, carvedBrickBlock, carvedBrickMeta);
            setBlockAndMetadata(world, i13, 3, 2, brickStairBlock, 2);
            setBlockAndMetadata(world, i13, 2, 3, barsBlock, 0);
            setBlockAndMetadata(world, i13, 3, 3, brickBlock, brickMeta);
        }
        setBlockAndMetadata(world, 0, 2, 2, barsBlock, 0);
        setBlockAndMetadata(world, 0, 3, 2, brickStairBlock, 2);
        setBlockAndMetadata(world, 0, 1, 3, LOTRLegacyBlocks.vanilla("lava"), 0);
        LOTRDwarfEntity smith = createSmith(world);
        spawnNPCAndSetHome(smith, world, 0, 1, 0, 8);
        return true;
    }

    public LOTRChestContents.Pool getChestContents() {
        return LOTRChestContents.DWARF_SMITHY;
    }

    public void layFoundation(WorldGenLevel world, int i, int k) {
        for (int j = 0; (j == 0 || !isOpaque(world, i, j, k)) && getY(j) >= 0; --j) {
            setBlockAndMetadata(world, i, j, k, baseBrickBlock, baseBrickMeta);
            setGrassToDirt(world, i, j - 1, k);
        }
    }

    public void placeDwarfForge(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("dwarvenForge"), meta);
        BlockEntity tileentity = getTileEntity(world, i, j, k);
        if (tileentity instanceof LOTRForgeBlockEntity) {
            LOTRForgeBlockEntity forge = (LOTRForgeBlockEntity) tileentity;
            int fuelAmount = Mth.randomBetweenInclusive(random, 0, 4);
            if (fuelAmount > 0) {
                ItemStack fuel = LOTRLegacyItems.vanillaStack("coal", fuelAmount, 0);
                forge.setItem(LOTRForgeBlockEntity.FUEL_SLOT, fuel);
            }
        }
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 1;
                gateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 13;
                gateBlock = LOTRLegacyBlocks.mod("fenceGateLarch");
                break;
            case 2:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 4;
                gateBlock = LOTRLegacyBlocks.mod("fenceGatePine");
                break;
            case 3:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 3;
                gateBlock = LOTRLegacyBlocks.mod("fenceGateFir");
                break;
            default:
                break;
        }
    }
}
