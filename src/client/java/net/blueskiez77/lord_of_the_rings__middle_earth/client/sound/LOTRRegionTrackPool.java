package net.blueskiez77.lord_of_the_rings__middle_earth.client.sound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMusicRegion;

import net.minecraft.util.RandomSource;

import org.jspecify.annotations.Nullable;

/** LOTRRegionTrackPool (and LOTRTrackSorter): the tracks of a region or subregion, one picked by weight. */
public class LOTRRegionTrackPool {

    private final LOTRMusicRegion region;
    private final Collection<LOTRMusicTrack> trackList = new ArrayList<>();

    public LOTRRegionTrackPool(LOTRMusicRegion r) {
        this.region = r;
    }

    public void addTrack(LOTRMusicTrack track) {
        this.trackList.add(track);
    }

    /** LOTRTrackSorter.forRegionAndCategory: the tracks that play in the region at that time. */
    public static Predicate<LOTRMusicTrack> forRegionAndCategory(LOTRMusicRegion reg, LOTRMusicCategory cat) {
        return track -> track.getRegionInfo(reg).getCategories().contains(cat);
    }

    public @Nullable LOTRMusicTrack getRandomTrack(RandomSource rand, Predicate<LOTRMusicTrack> filter) {
        List<LOTRMusicTrack> sortedTracks = new ArrayList<>();
        for (LOTRMusicTrack track : this.trackList) {
            if (filter.test(track)) {
                sortedTracks.add(track);
            }
        }
        double totalWeight = 0.0;
        for (LOTRMusicTrack track : sortedTracks) {
            totalWeight += track.getRegionInfo(this.region).getWeight();
        }
        double randWeight = rand.nextDouble() * totalWeight;
        LOTRMusicTrack track = null;
        for (LOTRMusicTrack t : sortedTracks) {
            track = t;
            if ((randWeight -= t.getRegionInfo(this.region).getWeight()) < 0.0) {
                break;
            }
        }
        return track;
    }

    public boolean isEmpty() {
        return this.trackList.isEmpty();
    }
}
