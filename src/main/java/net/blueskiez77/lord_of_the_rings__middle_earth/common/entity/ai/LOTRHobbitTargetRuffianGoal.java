package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeRuffianEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRShireBiome;

import net.minecraft.world.entity.PathfinderMob;

/**
 * LOTREntityAIHobbitTargetRuffian: a bounder goes after Bree ruffians, but
 * only while it is in the Shire.
 */
public class LOTRHobbitTargetRuffianGoal extends LOTRNearestAttackableTargetGoal {

    public LOTRHobbitTargetRuffianGoal(PathfinderMob mob, int chance, boolean checkSight) {
        super(mob, LOTRBreeRuffianEntity.class, chance, checkSight,
                target -> LOTRBiomes.of(mob.level().getBiome(mob.blockPosition())) instanceof LOTRShireBiome);
    }
}
