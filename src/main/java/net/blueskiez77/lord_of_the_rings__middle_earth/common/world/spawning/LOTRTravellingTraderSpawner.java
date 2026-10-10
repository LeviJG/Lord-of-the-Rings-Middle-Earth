package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRTravellingTraderSpawner: one kind of travelling trader -- after a wait of 0.8 to 10 hours,
 * it comes, one tick in a thousand, to a player not its faction's enemy, 48 to 80 blocks off in a
 * biome it travels in, on that biome's own ground. The wait is kept with the world.
 */
public class LOTRTravellingTraderSpawner {
    private static final RandomSource RAND = RandomSource.create();

    public final EntityType<?> theEntityClass;
    public final String entityClassName;
    public int timeUntilTrader;

    public LOTRTravellingTraderSpawner(EntityType<?> entityClass) {
        this.theEntityClass = entityClass;
        this.entityClassName = BuiltInRegistries.ENTITY_TYPE.getKey(entityClass).toString();
        this.timeUntilTrader = LOTRLevelData.getTravellingTraderTime(this.entityClassName, getRandomTraderTime());
    }

    public static int getRandomTraderTime() {
        float minHours = 0.8f;
        float maxHours = 10.0f;
        return LOTRWorldGenUtil.getRandomIntegerInRange(RAND, (int) (minHours * 3600.0f) * 20, (int) (maxHours * 3600.0f) * 20);
    }

    private void setTime(int time) {
        this.timeUntilTrader = time;
        LOTRLevelData.setTravellingTraderTime(this.entityClassName, time);
    }

    public void performSpawning(ServerLevel world) {
        if (this.timeUntilTrader > 0) {
            --this.timeUntilTrader;
            if (this.timeUntilTrader % 1200 == 0) {
                setTime(this.timeUntilTrader);
            }
            return;
        }
        RandomSource rand = world.getRandom();
        if (rand.nextInt(1000) != 0) {
            return;
        }
        Entity created = this.theEntityClass.create(world, EntitySpawnReason.EVENT);
        if (!(created instanceof LOTRNPCEntity entityTrader) || entityTrader.travellingTraderInfo == null) {
            return;
        }
        boolean spawned = false;
        playerLoop:
        for (ServerPlayer entityplayer : world.players()) {
            if (LOTRPlayerAlignments.getAlignment(entityplayer, entityTrader.getFaction()) < 0.0f) {
                continue;
            }
            for (int attempts = 0; attempts < 16; ++attempts) {
                // The original's angle was in degrees, given to sin and cos as radians; kept.
                float angle = rand.nextFloat() * 360.0f;
                int i = Mth.floor(entityplayer.getX()) + Mth.floor(Mth.sin(angle) * (48 + rand.nextInt(33)));
                int k = Mth.floor(entityplayer.getZ()) + Mth.floor(Mth.cos(angle) * (48 + rand.nextInt(33)));
                LOTRBiome biome = LOTRBiomes.of(world.getBiome(new BlockPos(i, 64, k)));
                if (biome == null || !biome.spawnableTraders.contains(this.theEntityClass)) {
                    continue;
                }
                int j = LOTRWorldGenUtil.getHeightValue(world, i, k);
                BlockState block = world.getBlockState(new BlockPos(i, j - 1, k));
                BlockPos pos = new BlockPos(i, j, k);
                if (j <= 62 || !block.is(biome.topBlock.getBlock()) && !block.is(biome.fillerBlock.getBlock())
                        || world.getBlockState(pos).isRedstoneConductor(world, pos)
                        || world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above())) {
                    continue;
                }
                entityTrader.snapTo(i + 0.5, j, k + 0.5, rand.nextFloat() * 360.0f, 0.0f);
                entityTrader.liftSpawnRestrictions = true;
                if (!entityTrader.checkSpawnRules(world, EntitySpawnReason.EVENT) || !entityTrader.checkSpawnObstruction(world)) {
                    continue;
                }
                entityTrader.liftSpawnRestrictions = false;
                entityTrader.spawnRidingHorse = false;
                entityTrader.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.EVENT, null);
                world.addFreshEntityWithPassengers(entityTrader);
                entityTrader.isNPCPersistent = true;
                entityTrader.setShouldTraderRespawn(false);
                entityTrader.travellingTraderInfo.startVisiting(entityplayer);
                spawned = true;
                setTime(getRandomTraderTime());
                break playerLoop;
            }
        }
        if (!spawned) {
            setTime(200 + rand.nextInt(400));
        }
    }
}
