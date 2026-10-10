package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;

import net.minecraft.util.Mth;

/**
 * LOTRWorldProvider: the rules of the mod's dimensions -- here, the moon's phases by the Shire
 * Reckoning's day, and the lunar eclipse every fourth new moon.
 */
public final class LOTRWorldProvider {
    public static final int MOON_PHASES = 8;

    private LOTRWorldProvider() {
    }

    public static int getLOTRMoonPhase() {
        return Mth.positiveModulo(LOTRDate.ShireReckoning.currentDay, MOON_PHASES);
    }

    public static boolean isLunarEclipse() {
        int day = LOTRDate.ShireReckoning.currentDay;
        return getLOTRMoonPhase() == 0 && Mth.positiveModulo(day / MOON_PHASES, 4) == 3;
    }
}
