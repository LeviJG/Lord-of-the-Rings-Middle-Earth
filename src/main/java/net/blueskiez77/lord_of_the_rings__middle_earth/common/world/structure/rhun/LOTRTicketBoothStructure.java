package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.chat.Component;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRTicketBoothStructure extends LOTREasterlingStructureTownStructure {
    public LOTRTicketBoothStructure(boolean flag) {
        super(flag);
    }

    public static boolean generatesAt(int i, int k) {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures.generatesAtMapImageCoords(i, k, 1583, 2527);
    }


    public void generateSupports(WorldGenLevel world, int i, int j, int k, LegacyBlock stairBlock, int stairMeta, LegacyBlock woodBlock, int woodMeta) {
        setBlockAndMetadata(world, i, j, k, stairBlock, stairMeta);
        int j1 = -1;
        while (!isOpaque(world, i, j + j1, k) && getY(j + j1) >= world.getMinY()) {
            LegacyBlock block = LOTRLegacyBlocks.vanilla("fence");
            int meta = 0;
            BlockState below = world.getBlockState(new BlockPos(i, j + j1, k));
            if (LOTRLegacyWorld.isLiquid(below)) {
                block = LOTRLegacyBlocks.vanilla("planks");
                meta = woodMeta;
            }
            setBlockAndMetadata(world, i, j + j1, k, block, meta);
            --j1;
        }
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int j12;
        int j13;
        int i1;
        int k1;
        int i12;
        setOriginAndRotation(world, i, j, k, rotation, 3, 3);
        setupRandomBlocks(random);
        int woolType = 14;
        LegacyBlock woodBlock = LOTRLegacyBlocks.vanilla("planks");
        int woodMeta = 0;
        LegacyBlock stairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
        LegacyBlock seatBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
        for (k1 = -2; k1 <= 15; ++k1) {
            for (i12 = -2; i12 <= 9; ++i12) {
                if (i12 >= 5 && k1 <= 1) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                j13 = -1;
                while (!isOpaque(world, i12, j13, k1) && getY(j13) >= world.getMinY()) {
                    setBiomeFiller(world, i12, j13, k1);
                    setGrassToDirt(world, i12, j13 - 1, k1);
                    --j13;
                }
                for (j13 = 1; j13 <= 3; ++j13) {
                    setBlockAndMetadata(world, i12, j13, k1, woodBlock, woodMeta);
                }
                if (k1 > 2) {
                    for (j13 = 4; j13 <= 5; ++j13) {
                        setBlockAndMetadata(world, i12, j13, k1, woodBlock, woodMeta);
                    }
                }
                setBlockAndMetadata(world, i12, 2, k1, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
            }
        }
        for (k1 = 3; k1 <= 14; ++k1) {
            for (i12 = -1; i12 <= 8; ++i12) {
                for (j13 = 1; j13 <= 4; ++j13) {
                    setAir(world, i12, j13, k1);
                }
                if (k1 > 9 || Math.floorMod(k1, 2) != 1) {
                    setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("wool"), woolType);
                }
                if (k1 <= 9 && Math.floorMod(k1, 2) == 1 && i12 != 3 && i12 != 4) {
                    setBlockAndMetadata(world, i12, 1, k1, seatBlock, 3);
                }
                if (i12 != 3 && i12 != 4) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
            }
        }
        for (j12 = 0; j12 <= 4; ++j12) {
            for (i12 = 2; i12 <= 5; ++i12) {
                if (j12 >= 1 && j12 <= 3 && i12 >= 3 && i12 <= 4) {
                    setBlockAndMetadata(world, i12, j12, 14, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 15);
                    continue;
                }
                setBlockAndMetadata(world, i12, j12, 14, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
            }
        }
        for (k1 = -2; k1 <= 2; ++k1) {
            for (j1 = 1; j1 <= 2; ++j1) {
                setAir(world, 3, j1, k1);
            }
        }
        for (k1 = -1; k1 <= 0; ++k1) {
            for (j1 = 1; j1 <= 2; ++j1) {
                for (int i13 = -1; i13 <= 1; ++i13) {
                    setAir(world, i13, j1, k1);
                    if (k1 != -1 || j1 != 2) {
                        continue;
                    }
                    setAir(world, i13, j1, k1 - 1);
                }
            }
        }
        setBlockAndMetadata(world, -1, 2, 0, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 1, 2, 0, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, 0, 1, -2, LOTRLegacyBlocks.vanilla("fence"), 0);
        setBlockAndMetadata(world, -1, 2, -2, LOTRLegacyBlocks.vanilla("glass_pane"), 0);
        setBlockAndMetadata(world, 1, 2, -2, LOTRLegacyBlocks.vanilla("glass_pane"), 0);
        for (k1 = 4; k1 <= 14; ++k1) {
            setBlockAndMetadata(world, -1, 4, k1, stairBlock, 4);
            setBlockAndMetadata(world, 8, 4, k1, stairBlock, 5);
        }
        for (i1 = 0; i1 <= 7; ++i1) {
            setBlockAndMetadata(world, i1, 4, 3, stairBlock, 7);
            if (i1 > 1 && i1 < 6) {
                continue;
            }
            setBlockAndMetadata(world, i1, 4, 14, stairBlock, 6);
        }
        for (j12 = 0; j12 <= 4; ++j12) {
            LegacyBlock block = woodBlock;
            int meta = woodMeta;
            if (j12 == 2) {
                block = LOTRLegacyBlocks.vanilla("glowstone");
                meta = 0;
            }
            setBlockAndMetadata(world, -1, j12, 3, block, meta);
            setBlockAndMetadata(world, -1, j12, 14, block, meta);
            setBlockAndMetadata(world, 8, j12, 3, block, meta);
            setBlockAndMetadata(world, 8, j12, 14, block, meta);
        }
        for (i1 = -2; i1 <= 4; ++i1) {
            if (i1 == 3) {
                setBlockAndMetadata(world, 3, 3, -3, woodBlock, woodMeta);
                continue;
            }
            setBlockAndMetadata(world, i1, 3, -3, stairBlock, 2);
        }
        for (k1 = -2; k1 <= 3; ++k1) {
            setBlockAndMetadata(world, -3, 3, k1, stairBlock, 1);
        }
        for (k1 = -2; k1 <= 0; ++k1) {
            setBlockAndMetadata(world, 5, 3, k1, stairBlock, 0);
        }
        generateSupports(world, 5, 3, 1, stairBlock, 2, woodBlock, woodMeta);
        for (i1 = 6; i1 <= 9; ++i1) {
            setBlockAndMetadata(world, i1, 3, 1, stairBlock, 2);
        }
        for (i1 = -2; i1 <= 9; ++i1) {
            setBlockAndMetadata(world, i1, 5, 2, stairBlock, 2);
            setBlockAndMetadata(world, i1, 5, 16, stairBlock, 3);
        }
        for (k1 = 3; k1 <= 15; ++k1) {
            setBlockAndMetadata(world, -3, 5, k1, stairBlock, 1);
            setBlockAndMetadata(world, 10, 5, k1, stairBlock, 0);
        }
        setBlockAndMetadata(world, 10, 3, 2, stairBlock, 0);
        setBlockAndMetadata(world, 10, 3, 3, stairBlock, 0);
        generateSupports(world, -3, 3, -3, stairBlock, 2, woodBlock, woodMeta);
        generateSupports(world, 5, 3, -3, stairBlock, 2, woodBlock, woodMeta);
        generateSupports(world, 10, 3, 1, stairBlock, 2, woodBlock, woodMeta);
        generateSupports(world, 10, 3, 4, stairBlock, 3, woodBlock, woodMeta);
        generateSupports(world, -3, 3, 4, stairBlock, 3, woodBlock, woodMeta);
        setBlockAndMetadata(world, -3, 5, 2, stairBlock, 2);
        setBlockAndMetadata(world, 10, 5, 2, stairBlock, 2);
        generateSupports(world, -3, 5, 16, stairBlock, 3, woodBlock, woodMeta);
        generateSupports(world, 10, 5, 16, stairBlock, 3, woodBlock, woodMeta);
        setBlockAndMetadata(world, 3, 1, -2, LOTRLegacyBlocks.vanilla("wooden_door"), 1);
        setBlockAndMetadata(world, 3, 2, -2, LOTRLegacyBlocks.vanilla("wooden_door"), 8);
        for (k1 = 5; k1 <= 12; ++k1) {
            if (Math.floorMod(k1, 3) == 1) {
                continue;
            }
            setBlockAndMetadata(world, -1, 2, k1, LOTRLegacyBlocks.vanilla("torch"), 2);
            setBlockAndMetadata(world, 8, 2, k1, LOTRLegacyBlocks.vanilla("torch"), 1);
        }
        for (i1 = 1; i1 <= 6; ++i1) {
            if (i1 <= 1 || i1 == 6) {
                setBlockAndMetadata(world, i1, 2, 14, LOTRLegacyBlocks.vanilla("torch"), 4);
            }
            if (i1 > 2 && i1 < 5) {
                continue;
            }
            setBlockAndMetadata(world, i1, 2, 3, LOTRLegacyBlocks.vanilla("torch"), 3);
        }
        setBlockAndMetadata(world, -2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 4, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 4);
        placeSign(world, 3, 3, -4, LOTRLegacyBlocks.vanilla("wall_sign"), 2, new String[]{"---------------", "Now showing:", "The Lion King", "---------------"});
        LOTRLionEntity lion = create(LOTREntities.LION, world);
        lion.setCustomName(Component.literal("Ticket Lion"));
        lion.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0E8);
        lion.setHealth(lion.getMaxHealth());
        spawnNPCAndSetHome(lion, world, 0, 1, -1, 4);
        setBlockAndMetadata(world, 0, 1, 2, LOTRLegacyBlocks.vanilla("chest"), 3);
        Container chest = (Container) getTileEntity(world, 0, 1, 2);
        if (chest != null) {
            int lootAmount = 2 + random.nextInt(4);
            for (int l = 0; l < lootAmount; ++l) {
                chest.setItem(random.nextInt(chest.getContainerSize()), getBasicLoot(random));
            }
        }
        setBlockAndMetadata(world, 0, 2, 2, LOTRLegacyBlocks.vanilla("trapdoor"), 1);
        placeSign(world, 3, 2, 13, LOTRLegacyBlocks.vanilla("wall_sign"), 2, new String[]{"", "Showings", "postponed", ""});
        placeSign(world, 4, 2, 13, LOTRLegacyBlocks.vanilla("wall_sign"), 2, new String[]{"", "until further", "notice.", ""});
        return true;
    }

    public ItemStack getBasicLoot(RandomSource random) {
        int i = random.nextInt(11);
        switch (i) {
            case 1:
                return LOTRLegacyItems.vanillaStack("paper", 1 + random.nextInt(3), 0);
            case 2:
                return LOTRLegacyItems.vanillaStack("book", 1 + random.nextInt(2), 0);
            case 3:
                return LOTRLegacyItems.vanillaStack("bread", 3 + random.nextInt(2), 0);
            case 4:
                return LOTRLegacyItems.vanillaStack("compass", 1, 0);
            case 5:
                return LOTRLegacyItems.vanillaStack("gold_nugget", 2 + random.nextInt(6), 0);
            case 6:
                return LOTRLegacyItems.vanillaStack("apple", 1 + random.nextInt(3), 0);
            case 7:
                return LOTRLegacyItems.vanillaStack("string", 2 + random.nextInt(2), 0);
            case 8:
                return LOTRLegacyItems.vanillaStack("bowl", 1 + random.nextInt(4), 0);
            case 9:
                return LOTRLegacyItems.vanillaStack("cookie", 1 + random.nextInt(3), 0);
            default:
                return LOTRLegacyItems.vanillaStack("stick", 2 + random.nextInt(4), 0);
        }
    }
}
