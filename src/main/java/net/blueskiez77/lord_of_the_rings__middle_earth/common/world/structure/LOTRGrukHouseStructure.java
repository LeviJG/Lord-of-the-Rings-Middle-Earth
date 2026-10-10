package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariants;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRWorldGenGrukHouse: the long reed-thatched hall at map image 989, 528 -- benches of goblets and
 * horns, barrels of plum kvass and vodka, two fire pits before it, a path of the tundra's kind about
 * it, signs, a rolling pin and a book on the gable, and a cat and a wolf, both its maker's.
 *
 * <p>The door sign reads "Kvas chlebový"; the original's text had lost its accent to a wrong
 * encoding. The animals' health is as large as today's attribute allows.
 */
public class LOTRGrukHouseStructure extends LOTRStructureBase2 {

    private static final UUID OWNER = UUID.fromString("6c94c61a-aebb-4b77-9699-4d5236d0e78a");

    public LOTRGrukHouseStructure(boolean flag) {
        super(flag);
    }

    public static boolean generatesAt(int i, int k) {
        return LOTRFixedStructures.generatesAtMapImageCoords(i, k, 989, 528);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        int k2;
        int k12;
        int j1;
        int k13;
        setOriginAndRotation(world, i, j, k, rotation, 9);
        if (this.restrictions) {
            for (i1 = -5; i1 <= 5; ++i1) {
                for (k12 = -8; k12 <= 8; ++k12) {
                    j1 = getTopBlock(world, i1, k12);
                    if (getBlock(world, i1, j1 - 1, k12) == Blocks.GRASS_BLOCK) {
                        continue;
                    }
                    return false;
                }
            }
        }
        LegacyBlock cobblestone = LOTRLegacyBlocks.vanilla("cobblestone");
        LegacyBlock beam = LOTRLegacyBlocks.mod("woodBeamV1");
        LegacyBlock planks = LOTRLegacyBlocks.vanilla("planks");
        LegacyBlock stairsReed = LOTRLegacyBlocks.mod("stairsReed");
        LegacyBlock fence = LOTRLegacyBlocks.vanilla("fence");
        LegacyBlock reedBars = LOTRLegacyBlocks.mod("reedBars");
        LegacyBlock torch = LOTRLegacyBlocks.vanilla("torch");
        LegacyBlock wallSign = LOTRLegacyBlocks.vanilla("wall_sign");
        for (i1 = -5; i1 <= 5; ++i1) {
            for (k12 = -8; k12 <= 8; ++k12) {
                int j12;
                int i2 = Math.abs(i1);
                k2 = Math.abs(k12);
                for (j12 = 0; (j12 == 0 || !isOpaque(world, i1, j12, k12)) && getY(j12) >= world.getMinY(); --j12) {
                    setBlockAndMetadata(world, i1, j12, k12, cobblestone, 0);
                    setGrassToDirt(world, i1, j12 - 1, k12);
                }
                if (i2 == 5 && k2 == 8) {
                    for (j12 = 1; j12 <= 5; ++j12) {
                        setBlockAndMetadata(world, i1, j12, k12, beam, 1);
                    }
                    continue;
                }
                if (i2 == 5 || k2 == 8) {
                    for (j12 = 1; j12 <= 5; ++j12) {
                        setBlockAndMetadata(world, i1, j12, k12, planks, 1);
                    }
                    continue;
                }
                for (j12 = 1; j12 <= 10; ++j12) {
                    setAir(world, i1, j12, k12);
                }
            }
        }
        for (k13 = -9; k13 <= 9; ++k13) {
            for (int l = 0; l <= 5; ++l) {
                setBlockAndMetadata(world, -6 + l, 5 + l, k13, stairsReed, 1);
                setBlockAndMetadata(world, 6 - l, 5 + l, k13, stairsReed, 0);
                setBlockAndMetadata(world, -6 + l, 4 + l, k13, stairsReed, 4);
                setBlockAndMetadata(world, 6 - l, 4 + l, k13, stairsReed, 5);
            }
            setBlockAndMetadata(world, 0, 10, k13, LOTRLegacyBlocks.mod("thatch"), 1);
            setBlockAndMetadata(world, 0, 11, k13, LOTRLegacyBlocks.mod("slabSingleThatch"), 1);
        }
        for (int l = 0; l <= 5; ++l) {
            for (int i12 = -5 + l; i12 <= 5 - l; ++i12) {
                setBlockAndMetadata(world, i12, 5 + l, -8, planks, 1);
                setBlockAndMetadata(world, i12, 5 + l, 8, planks, 1);
            }
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            setBlockAndMetadata(world, i1, 5, -8, beam, 5);
            setBlockAndMetadata(world, i1, 5, 8, beam, 5);
            setBlockAndMetadata(world, i1, 5, -7, fence, 0);
            setBlockAndMetadata(world, i1, 5, 7, fence, 0);
        }
        for (k13 = -7; k13 <= 7; ++k13) {
            setBlockAndMetadata(world, -5, 5, k13, beam, 9);
            setBlockAndMetadata(world, 5, 5, k13, beam, 9);
            setBlockAndMetadata(world, -4, 5, k13, fence, 0);
            setBlockAndMetadata(world, 4, 5, k13, fence, 0);
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            int i2 = Math.abs(i1);
            if (i2 != 2 && i2 != 3) {
                continue;
            }
            setBlockAndMetadata(world, i1, 2, -8, reedBars, 0);
            setBlockAndMetadata(world, i1, 3, -8, reedBars, 0);
        }
        for (k13 = -7; k13 <= 7; ++k13) {
            int k22 = Math.abs(k13);
            if (k22 == 0 || k22 == 1 || k22 == 5 || k22 == 6) {
                setBlockAndMetadata(world, -5, 2, k13, reedBars, 0);
                setBlockAndMetadata(world, -5, 3, k13, reedBars, 0);
                setBlockAndMetadata(world, 5, 2, k13, reedBars, 0);
                setBlockAndMetadata(world, 5, 3, k13, reedBars, 0);
                continue;
            }
            if (k22 != 3) {
                continue;
            }
            for (j1 = 0; j1 <= 4; ++j1) {
                setBlockAndMetadata(world, -5, j1, k13, beam, 1);
                setBlockAndMetadata(world, 5, j1, k13, beam, 1);
            }
            setBlockAndMetadata(world, -3, 1, k13, fence, 1);
            setBlockAndMetadata(world, -3, 2, k13, torch, 5);
            setBlockAndMetadata(world, 3, 1, k13, fence, 1);
            setBlockAndMetadata(world, 3, 2, k13, torch, 5);
        }
        setBlockAndMetadata(world, 0, 0, -8, cobblestone, 0);
        setBlockAndMetadata(world, 0, 1, -8, LOTRLegacyBlocks.mod("doorPine"), 1);
        setBlockAndMetadata(world, 0, 2, -8, LOTRLegacyBlocks.mod("doorPine"), 8);
        setBlockAndMetadata(world, 0, 4, -9, torch, 4);
        setBlockAndMetadata(world, 0, 3, -7, torch, 3);
        setBlockAndMetadata(world, 0, 1, 7, fence, 1);
        setBlockAndMetadata(world, 0, 2, 7, torch, 5);
        for (k13 = -7; k13 <= 7; ++k13) {
            for (int i13 : new int[]{-4, 4}) {
                setBlockAndMetadata(world, i13, 1, k13, LOTRLegacyBlocks.mod("planks2"), 4);
                if (!random.nextBoolean()) {
                    continue;
                }
                placeMug(world, random, i13, 2, k13, random.nextInt(4), getRandomDrink(random),
                        new LOTRVessel[]{LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER, LOTRVessel.HORN, LOTRVessel.HORN_GOLD});
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            if (i1 == 0) {
                continue;
            }
            placeBarrel(world, random, i1, 1, 7, 2, getRandomDrink(random));
            placeBarrel(world, random, i1, 2, 7, 2, getRandomDrink(random));
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            setBlockAndMetadata(world, i1, 4, 7, LOTRLegacyBlocks.vanilla("wool"), 14);
            setBlockAndMetadata(world, i1, 5, 7, LOTRLegacyBlocks.vanilla("wool"), 0);
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -6; k1 <= -3; ++k1) {
                setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.vanilla("carpet"), 14);
            }
            for (k1 = -2; k1 <= 1; ++k1) {
                setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.vanilla("carpet"), 0);
            }
        }
        for (int i15 : new int[]{-8, 8}) {
            for (int i2 = i15 - 2; i2 <= i15 + 2; ++i2) {
                for (int k14 = -20; k14 <= -16; ++k14) {
                    int j13;
                    for (j13 = 4; (j13 >= 0 || !isOpaque(world, i2, j13, k14)) && getY(j13) >= world.getMinY(); --j13) {
                        setBlockAndMetadata(world, i2, j13, k14, cobblestone, 0);
                        setGrassToDirt(world, i2, j13 - 1, k14);
                    }
                    for (j13 = 5; j13 <= 10; ++j13) {
                        setAir(world, i2, j13, k14);
                    }
                    if (Math.abs(i2 - i15) > 1 || Math.abs(k14 + 18) > 1) {
                        continue;
                    }
                    setBlockAndMetadata(world, i2, 4, k14, LOTRLegacyBlocks.mod("hearth"), 0);
                    setBlockAndMetadata(world, i2, 5, k14, LOTRLegacyBlocks.vanilla("fire"), 0);
                }
            }
        }
        for (int i16 = -12; i16 <= 12; ++i16) {
            for (k1 = -20; k1 <= 0; ++k1) {
                int dz = k1 + 8;
                int dSq = i16 * i16 + dz * dz;
                if (dSq > 144 || random.nextInt(6) == 0) {
                    continue;
                }
                int j14 = getTopBlock(world, i16, k1) - 1;
                BlockPos pos = worldPos(i16, j14, k1);
                LOTRBiome biome = pos == null ? null : LOTRBiomes.of(world.getBiome(pos));
                BlockState below = getBlockState(world, i16, j14, k1);
                if (biome == null || below != biome.topBlock && below != biome.fillerBlock) {
                    continue;
                }
                LOTRRoadType.RoadBlock roadblock = LOTRRoadType.PATH.getBlock(random, true, false);
                setBlockAndMetadata(world, i16, j14, k1, roadblock.block(), roadblock.meta());
            }
        }
        placeSign(world, 0, 3, -9, wallSign, 2, new String[]{"", "Kvas", "chlebový", ""});
        placeSign(world, 0, 3, 7, wallSign, 2, new String[]{"", ":^)", "", ""});
        placeSign(world, 0, 8, -7, wallSign, 3, new String[]{"", "Textures?", "", ""});
        spawnItemFrame(world, -1, 7, -8, 0, new ItemStack(LOTRCombatItems.ROLLING_PIN));
        spawnItemFrame(world, 1, 7, -8, 0, new ItemStack(Items.BOOK));
        Cat bazyl = create(EntityTypes.CAT, world);
        tameUndying(bazyl, "Bazyl");
        spawnNPCAndSetHome(bazyl, world, -1, 1, 0, 16);
        bazyl.setComponent(DataComponents.CAT_VARIANT,
                world.registryAccess().lookupOrThrow(Registries.CAT_VARIANT).getOrThrow(CatVariants.BLACK));
        Wolf wiktor = create(EntityTypes.WOLF, world);
        tameUndying(wiktor, "Wiktor");
        spawnNPCAndSetHome(wiktor, world, 1, 1, 0, 16);
        return true;
    }

    private static void tameUndying(TamableAnimal animal, String name) {
        animal.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0E8);
        animal.setHealth(animal.getMaxHealth());
        animal.setTame(true, false);
        animal.setOwnerReference(EntityReference.of(OWNER));
        animal.setCustomName(Component.literal(name));
    }

    public ItemStack getRandomDrink(RandomSource random) {
        if (random.nextBoolean()) {
            return new ItemStack(LOTRFoodItems.PLUM_KVASS);
        }
        return new ItemStack(LOTRFoodItems.VODKA);
    }
}
