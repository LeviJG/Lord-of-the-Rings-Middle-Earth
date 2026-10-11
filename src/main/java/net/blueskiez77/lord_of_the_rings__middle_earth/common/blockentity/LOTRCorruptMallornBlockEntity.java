package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTREntEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFangornBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;

/**
 * LOTRTileEntityCorruptMallorn: marks a corrupt mallorn for the Ents that come looking for one to
 * heal -- and in Fangorn calls them: one tick in forty, if no Ent is within 24 blocks, one is made
 * somewhere within 20 where it may stand and spawn, with room, if mobs may spawn at all.
 */
public class LOTRCorruptMallornBlockEntity extends BlockEntity {

    public LOTRCorruptMallornBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.CORRUPT_MALLORN, pos, state);
    }

    /** updateEntity. */
    public static void serverTick(ServerLevel level, BlockPos pos) {
        RandomSource rand = level.getRandom();
        if (!level.getGameRules().get(GameRules.SPAWN_MOBS) || rand.nextInt(40) != 0
                || !(LOTRBiomes.of(level.getBiome(pos)) instanceof LOTRFangornBiome)) {
            return;
        }
        int checkRange = 24;
        int spawnRange = 20;
        if (!level.getEntitiesOfClass(LOTREntEntity.class, new AABB(pos).inflate(checkRange)).isEmpty()) {
            return;
        }
        LOTREntEntity ent = LOTREntities.ENT.create(level, EntitySpawnReason.NATURAL);
        if (ent == null) {
            return;
        }
        for (int l = 0; l < 16; ++l) {
            int i = pos.getX() + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange, spawnRange);
            int j = pos.getY() + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange, spawnRange);
            int k = pos.getZ() + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange, spawnRange);
            BlockPos at = new BlockPos(i, j, k);
            if (!level.getBlockState(at.below()).isRedstoneConductor(level, at.below())
                    || level.getBlockState(at).isRedstoneConductor(level, at)
                    || level.getBlockState(at.above()).isRedstoneConductor(level, at.above())) {
                continue;
            }
            ent.snapTo(i + 0.5, j, k + 0.5, rand.nextFloat() * 360.0f, 0.0f);
            ent.liftSpawnRestrictions = false;
            if (!ent.checkSpawnRules(level, EntitySpawnReason.NATURAL) || !ent.checkSpawnObstruction(level)) {
                continue;
            }
            ent.finalizeSpawn(level, level.getCurrentDifficultyAt(at), EntitySpawnReason.NATURAL, null);
            ent.isNPCPersistent = false;
            level.addFreshEntity(ent);
            break;
        }
    }
}
