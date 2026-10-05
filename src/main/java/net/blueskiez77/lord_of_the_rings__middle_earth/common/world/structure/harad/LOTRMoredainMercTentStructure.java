package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMoredainMercTentStructure extends LOTRStructureBase2 {
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock tentBlock;
    public int tentMeta;
    public LegacyBlock tent2Block;
    public int tent2Meta;
    public LegacyBlock tableBlock;
    public LOTRChestContents.Pool chestContents;
    public String bannerType;

    public LOTRMoredainMercTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 4);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -2; i1 <= 2; ++i1) {
                for (k1 = -3; k1 <= 3; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    int randomGround = random.nextInt(3);
                    switch (randomGround) {
                        case 0:
                            if (j1 == 0) {
                                setBiomeTop(world, i1, 0, k1);
                            } else {
                                setBiomeFiller(world, i1, j1, k1);
                            }
                            break;
                        case 1:
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("gravel"), 0);
                            break;
                        case 2:
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                            break;
                        default:
                            break;
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 3; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (int k12 = -3; k12 <= 3; ++k12) {
            boolean tent2 = Math.floorMod(k12, 2) == 0;
            LegacyBlock block = tent2 ? tent2Block : tentBlock;
            int meta = tent2 ? tent2Meta : tentMeta;
            for (int i12 : new int[]{-2, 2}) {
                for (int j12 = 1; j12 <= 2; ++j12) {
                    setBlockAndMetadata(world, i12, j12, k12, block, meta);
                }
                setGrassToDirt(world, i12, 0, k12);
            }
            setBlockAndMetadata(world, -1, 3, k12, block, meta);
            setBlockAndMetadata(world, 1, 3, k12, block, meta);
            setBlockAndMetadata(world, 0, 4, k12, block, meta);
            if (Math.abs(k12) != 3) {
                continue;
            }
            setBlockAndMetadata(world, 0, 5, k12, block, meta);
        }
        for (int j13 = 1; j13 <= 3; ++j13) {
            setBlockAndMetadata(world, 0, j13, -3, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 0, j13, 3, fenceBlock, fenceMeta);
        }
        setBlockAndMetadata(world, -1, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 1, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, -1, 2, 3, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 1, 2, 3, LOTRLegacyBlocks.vanilla("torch"), 1);
        if (random.nextBoolean()) {
            placeChest(world, random, -1, 1, 0, 4, chestContents);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
            setGrassToDirt(world, -1, 0, -1);
            setBlockAndMetadata(world, -1, 1, 1, tableBlock, 0);
            setGrassToDirt(world, -1, 0, 1);
        } else {
            placeChest(world, random, 1, 1, 0, 5, chestContents);
            setBlockAndMetadata(world, 1, 1, -1, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
            setGrassToDirt(world, 1, 0, -1);
            setBlockAndMetadata(world, 1, 1, 1, tableBlock, 0);
            setGrassToDirt(world, 1, 0, 1);
        }
        placeWallBanner(world, 0, 5, -3, bannerType, 2);
        placeWallBanner(world, 0, 5, 3, bannerType, 0);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        fenceBlock = LOTRLegacyBlocks.mod("fence2");
        fenceMeta = 2;
        int randomWool = random.nextInt(3);
        switch (randomWool) {
            case 0:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 14;
                break;
            case 1:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 12;
                break;
            case 2:
                tentBlock = LOTRLegacyBlocks.vanilla("wool");
                tentMeta = 1;
                break;
            default:
                break;
        }
        tent2Block = LOTRLegacyBlocks.vanilla("wool");
        tent2Meta = 15;
        chestContents = LOTRChestContents.MOREDAIN_MERC_TENT;
        if (random.nextBoolean()) {
            tableBlock = LOTRLegacyBlocks.mod("nearHaradTable");
            bannerType = "NEAR_HARAD";
        } else {
            tableBlock = LOTRLegacyBlocks.mod("moredainTable");
            bannerType = "MOREDAIN";
        }
    }
}
