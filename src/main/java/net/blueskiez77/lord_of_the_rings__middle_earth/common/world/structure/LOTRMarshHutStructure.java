package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.Locale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenFangornTrees;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariants;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.SignBlockEntity;

/**
 * LOTRWorldGenMarshHut: the hut in the swamp at map image 1419, 1134 -- a round willow hut sunk in a
 * turfed mound with an old oak growing out of the roof, two trolls who will not fight or burn, a
 * donkey and a cat, and two signs.
 *
 * <p>Its donkey is vanilla's (the original's own horse of the donkey kind); it can be ridden, where
 * the original's could not. The animals' health is as large as today's attribute allows.
 */
public class LOTRMarshHutStructure extends LOTRStructureBase {

    public LOTRMarshHutStructure() {
        super(false);
    }

    public static boolean generatesAt(int i, int k) {
        return LOTRFixedStructures.generatesAtMapImageCoords(i, k, 1419, 1134);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        --j;
        int radius = 8;
        int radiusPlusOne = radius + 1;
        int wallThresholdMin = radius * radius;
        int wallThresholdMax = radiusPlusOne * radiusPlusOne;
        LegacyBlock plankBlock = LOTRLegacyBlocks.mod("planks2");
        int plankMeta = 9;
        LegacyBlock doorBlock = LOTRLegacyBlocks.mod("doorWillow");
        for (int i1 = i - radiusPlusOne; i1 <= i + radiusPlusOne; ++i1) {
            for (int k1 = k - radiusPlusOne; k1 <= k + radiusPlusOne; ++k1) {
                int j1;
                int i2 = i1 - i;
                int k2 = k1 - k;
                int distSq = i2 * i2 + k2 * k2;
                if (distSq >= wallThresholdMax) {
                    continue;
                }
                for (j1 = j; (j1 == j || !isOpaqueAt(world, i1, j1, k1)) && j1 >= world.getMinY(); --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = j + 1; j1 <= j + 6; ++j1) {
                    if (distSq >= wallThresholdMin) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
                        continue;
                    }
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (int i1 = i - radiusPlusOne - 2; i1 <= i + radiusPlusOne + 2; ++i1) {
            for (int k1 = k - radiusPlusOne - 2; k1 <= k + radiusPlusOne + 2; ++k1) {
                for (int j1 = j + 6; j1 <= j + 10; ++j1) {
                    int i2 = i1 - i;
                    int k2 = k1 - k;
                    int j2 = j1 - (j + 4);
                    int distSq = i2 * i2 + k2 * k2 + j2 * j2;
                    if (distSq + j2 * j2 >= wallThresholdMax) {
                        continue;
                    }
                    boolean grass = !isOpaqueAt(world, i1, j1 + 1, k1);
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla(grass ? "grass" : "dirt"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        LegacyBlock torch = LOTRLegacyBlocks.vanilla("torch");
        setBlockAndNotifyAdequately(world, i - (radius - 1), j + 3, k, torch, 1);
        setBlockAndNotifyAdequately(world, i + radius - 1, j + 3, k, torch, 2);
        setBlockAndNotifyAdequately(world, i, j + 3, k - (radius - 1), torch, 3);
        setBlockAndNotifyAdequately(world, i, j + 3, k + radius - 1, torch, 4);
        setBlockAndNotifyAdequately(world, i, j + 1, k - radius, doorBlock, 1);
        setBlockAndNotifyAdequately(world, i, j + 2, k - radius, doorBlock, 8);
        new LOTRWorldGenFangornTrees(false, LOTRLegacyBlocks.vanilla("log"), 0, LOTRLegacyBlocks.vanilla("leaves"), 0)
                .disableRestrictions().generate(world, random, i, j + 11, k);
        BlockPos pos = new BlockPos(i, j + 1, k);
        LOTRTrollEntity troll = spawnTroll(world, pos, "" + 'S' + 'h' + 'r' + 'e' + 'k');
        LOTRTrollEntity troll2 = spawnTroll(world, pos, "" + 'D' + 'r' + 'e' + 'k');
        Donkey horse = create(EntityTypes.DONKEY, world);
        horse.snapTo(i + 0.5, j + 1, k + 0.5, 0.0f, 0.0f);
        horse.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
        spawnUndying(world, horse);
        Cat cat = create(EntityTypes.CAT, world);
        cat.snapTo(i + 0.5, j + 1, k + 0.5, 0.0f, 0.0f);
        cat.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
        cat.setTame(true, false);
        cat.setComponent(DataComponents.CAT_VARIANT,
                world.registryAccess().lookupOrThrow(Registries.CAT_VARIANT).getOrThrow(CatVariants.RED));
        spawnUndying(world, cat);
        LegacyBlock wallSign = LOTRLegacyBlocks.vanilla("wall_sign");
        setBlockAndNotifyAdequately(world, i, j + 2, k + radius - 1, wallSign, 2);
        if (world.getBlockEntity(new BlockPos(i, j + 2, k + radius - 1)) instanceof SignBlockEntity sign) {
            LOTRStructureBase2.setSignLine(sign, 0, "Check yourself");
            LOTRStructureBase2.setSignLine(sign, 1, "before you");
            LOTRStructureBase2.setSignLine(sign, 2, troll.familyInfo.getName() + " yourself");
        }
        setBlockAndNotifyAdequately(world, i, j + 1, k + radius - 1, wallSign, 2);
        if (world.getBlockEntity(new BlockPos(i, j + 1, k + radius - 1)) instanceof SignBlockEntity sign) {
            LOTRStructureBase2.setSignLine(sign, 0, troll.familyInfo.getName().toUpperCase(Locale.ROOT));
            LOTRStructureBase2.setSignLine(sign, 1, "IS");
            LOTRStructureBase2.setSignLine(sign, 2, troll2.familyInfo.getName().toUpperCase(Locale.ROOT));
        }
        return true;
    }

    private static LOTRTrollEntity spawnTroll(WorldGenLevel world, BlockPos pos, String name) {
        LOTRTrollEntity troll = create(LOTREntities.TROLL, world);
        troll.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
        troll.isNPCPersistent = true;
        troll.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
        troll.trollImmuneToSun = true;
        troll.isPassive = true;
        troll.familyInfo.setName(name);
        spawnUndying(world, troll);
        return troll;
    }

    /** At the original's 1.0E8 health -- as much as today's attribute holds -- and full. */
    public static void spawnUndying(WorldGenLevel world, LivingEntity entity) {
        entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0E8);
        entity.setHealth(entity.getMaxHealth());
        world.addFreshEntityWithPassengers(entity);
    }
}
