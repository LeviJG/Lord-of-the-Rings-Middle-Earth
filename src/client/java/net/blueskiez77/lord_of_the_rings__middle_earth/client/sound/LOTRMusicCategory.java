package net.blueskiez77.lord_of_the_rings__middle_earth.client.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/** LOTRMusicCategory: when a track may play in a region -- by day, by night, or underground. */
public enum LOTRMusicCategory {
    DAY("day"), NIGHT("night"), CAVE("cave");

    public final String categoryName;

    LOTRMusicCategory(String s) {
        this.categoryName = s;
    }

    public static @Nullable LOTRMusicCategory forName(String s) {
        for (LOTRMusicCategory cat : values()) {
            if (s.equalsIgnoreCase(cat.categoryName)) {
                return cat;
            }
        }
        return null;
    }

    /** isCave: below 50 and out of sight of the sky. */
    public static boolean isCave(Level level, BlockPos pos) {
        return pos.getY() < 50 && !level.canSeeSky(pos);
    }

    /** isDay: calculateSkylightSubtracted under 5. */
    public static boolean isDay(Level level) {
        return level.getSkyDarken() < 5;
    }
}
