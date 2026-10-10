package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTROldForestBiome;
import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDarkHuorn: an oak of the Old Forest, black of heart, answering
 * to no one.
 */
public class LOTRDarkHuornEntity extends LOTRHuornBaseEntity {

    public LOTRDarkHuornEntity(EntityType<? extends LOTRDarkHuornEntity> type, Level level) {
        super(type, level);
        setTreeType(0);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DARK_HUORN;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_DARK_HUORN;
    }

    @Override
    public boolean isTreeHomeBiome(@Nullable LOTRBiome biome) {
        return biome instanceof LOTROldForestBiome;
    }
}
