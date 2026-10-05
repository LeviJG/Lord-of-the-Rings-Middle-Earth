package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeGuardEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBreeMarketStructure extends LOTRBreeStructure {
    public LOTRBreeMarketStallStructure[] presetStalls;
    public boolean frontStepsOnly;

    public LOTRBreeMarketStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int step;
        int i1;
        int j2;
        int k1;
        setOriginAndRotation(world, i, j, k, rotation, 13);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -12; i1 <= 12; ++i1) {
                for (k1 = -12; k1 <= 12; ++k1) {
                    int j12 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j12, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -12; i1 <= 12; ++i1) {
            for (k1 = -12; k1 <= 12; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                boolean marketBounds = false;
                if (i2 <= 1 && k2 <= 12 || k2 <= 1 && i2 <= 12) {
                    marketBounds = true;
                } else if (i2 <= 4 && k2 <= 11 || k2 <= 4 && i2 <= 11) {
                    marketBounds = true;
                } else if (i2 <= 6 && k2 <= 10 || k2 <= 6 && i2 <= 10) {
                    marketBounds = true;
                } else if (i2 <= 7 && k2 <= 9 || k2 <= 7 && i2 <= 9) {
                    marketBounds = true;
                } else if (i2 <= 8 && k2 == 8) {
                    marketBounds = true;
                }
                if (!marketBounds) {
                    continue;
                }
                for (int j13 = 1; j13 <= 8; ++j13) {
                    setAir(world, i1, j13, k1);
                }
            }
        }
        loadStrScan("bree_market");
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", LOTRLegacyBlocks.vanilla("stone_slab"), 5);
        associateBlockMetaAlias("COBBLE", LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        associateBlockMetaAlias("COBBLE_SLAB", LOTRLegacyBlocks.vanilla("stone_slab"), 3);
        associateBlockAlias("COBBLE_STAIR", LOTRLegacyBlocks.vanilla("stone_stairs"));
        associateBlockMetaAlias("COBBLE_WALL", LOTRLegacyBlocks.vanilla("cobblestone_wall"), 0);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        addBlockMetaAliasOption("GROUND", 1, LOTRLegacyBlocks.vanilla("gravel"), 0);
        addBlockMetaAliasOption("GROUND", 1, LOTRLegacyBlocks.vanilla("grass"), 0);
        addBlockMetaAliasOption("GROUND", 1, LOTRLegacyBlocks.vanilla("dirt"), 1);
        addBlockMetaAliasOption("GROUND", 1, LOTRLegacyBlocks.mod("dirtPath"), 0);
        addBlockMetaAliasOption("THATCH_FLOOR", 1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
        setBlockAliasChance("THATCH_FLOOR", 0.15f);
        associateBlockMetaAlias("LEAF", LOTRLegacyBlocks.vanilla("leaves"), 4);
        associateBlockMetaAlias("LEAF_FLOOR", LOTRLegacyBlocks.mod("fallenLeaves"), 0);
        setBlockAliasChance("LEAF_FLOOR", 0.5f);
        generateStrScan(world, random, 0, 0, 0);
        for (int i12 = -1; i12 <= 1; ++i12) {
            int k12;
            for (step = 0; step < 12 && !isOpaque(world, i12, j1 = -1 - step, k12 = -13 - step); ++step) {
                placeRandomFloor(world, random, i12, j1, k12);
                setGrassToDirt(world, i12, j1 - 1, k12);
                j2 = j1 - 1;
                while (!isOpaque(world, i12, j2, k12) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i12, j2, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j2 - 1, k12);
                    --j2;
                }
            }
            if (frontStepsOnly) {
                continue;
            }
            for (step = 0; step < 12 && !isOpaque(world, i12, j1 = -1 - step, k12 = 13 + step); ++step) {
                placeRandomFloor(world, random, i12, j1, k12);
                setGrassToDirt(world, i12, j1 - 1, k12);
                j2 = j1 - 1;
                while (!isOpaque(world, i12, j2, k12) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i12, j2, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j2 - 1, k12);
                    --j2;
                }
            }
        }
        if (!frontStepsOnly) {
            for (k1 = -1; k1 <= 1; ++k1) {
                int i13;
                for (step = 0; step < 12 && !isOpaque(world, i13 = -13 - step, j1 = -1 - step, k1); ++step) {
                    placeRandomFloor(world, random, i13, j1, k1);
                    setGrassToDirt(world, i13, j1 - 1, k1);
                    j2 = j1 - 1;
                    while (!isOpaque(world, i13, j2, k1) && getY(j2) >= world.getMinY()) {
                        setBlockAndMetadata(world, i13, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                        setGrassToDirt(world, i13, j2 - 1, k1);
                        --j2;
                    }
                }
                for (step = 0; step < 12 && !isOpaque(world, i13 = 13 + step, j1 = -1 - step, k1); ++step) {
                    placeRandomFloor(world, random, i13, j1, k1);
                    setGrassToDirt(world, i13, j1 - 1, k1);
                    j2 = j1 - 1;
                    while (!isOpaque(world, i13, j2, k1) && getY(j2) >= world.getMinY()) {
                        setBlockAndMetadata(world, i13, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                        setGrassToDirt(world, i13, j2 - 1, k1);
                        --j2;
                    }
                }
            }
        }
        placeWallBanner(world, 0, 4, 0, "BREE", 2);
        placeAnimalJar(world, -3, 1, 4, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, 3, 2, -8, LOTRLegacyBlocks.mod("birdCage"), 1, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -1, 4, 0, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, 0, 2, 1, LOTRLegacyBlocks.mod("butterflyJar"), 0, create(LOTREntities.BUTTERFLY, world));
        LOTRBreeGuardEntity armorGuard = create(LOTREntities.BREE_GUARD, world);
        armorGuard.finalizeSpawn(world, world.getCurrentDifficultyAt(armorGuard.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        placeArmorStand(world, 2, 1, 0, 3, new ItemStack[]{armorGuard.getItemBySlot(EquipmentSlot.HEAD), armorGuard.getItemBySlot(EquipmentSlot.CHEST), null, null});
        LOTRBreeMarketStallStructure[] stalls = presetStalls;
        if (stalls == null) {
            stalls = LOTRBreeMarketStallStructure.getRandomStalls(random, notifyChanges, 4);
        }
        generateSubstructureWithRestrictionFlag(stalls[0], world, random, 6, 1, 3, 0, false);
        generateSubstructureWithRestrictionFlag(stalls[1], world, random, 3, 1, -6, 1, false);
        generateSubstructureWithRestrictionFlag(stalls[2], world, random, -6, 1, -3, 2, false);
        generateSubstructureWithRestrictionFlag(stalls[3], world, random, -3, 1, 6, 3, false);
        return true;
    }

    public LOTRBreeMarketStructure setFrontStepsOnly(boolean flag) {
        frontStepsOnly = flag;
        return this;
    }

    public LOTRBreeMarketStructure setStalls(LOTRBreeMarketStallStructure... stalls) {
        if (stalls.length != 4) {
            throw new IllegalArgumentException("Error: Market must have 4 stalls, but " + stalls.length + " supplied");
        }
        presetStalls = stalls;
        return this;
    }
}
