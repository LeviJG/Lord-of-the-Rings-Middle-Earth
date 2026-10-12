package net.blueskiez77.lord_of_the_rings__middle_earth.client.sound;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMusicRegion;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMusicTrack: one track of a music pack -- its file, title, authors, and the regions it plays in
 * (each with its subregions, categories and weight) -- and, as a sound, the track played in full
 * at the listener, streamed, on the music volume. Its sound is not registered: it resolves to
 * itself, under an id of its own whose stream LOTRMusic opens from the pack.
 */
public class LOTRMusicTrack extends AbstractSoundInstance {

    public final String filename;
    private @Nullable String title;
    private final Map<LOTRMusicRegion, RegionInfo> regions = new EnumMap<>(LOTRMusicRegion.class);
    private final List<String> authors = new ArrayList<>();

    public LOTRMusicTrack(Identifier id, String filename) {
        super(id, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.filename = filename;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.looping = false;
        this.delay = 0;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.relative = true;
    }

    @Override
    public WeighedSoundEvents resolve(SoundManager manager) {
        this.sound = new Sound(this.identifier, new ConstantFloat(1.0f), new ConstantFloat(1.0f), 1, Sound.Type.FILE, true, false, 16);
        WeighedSoundEvents events = new WeighedSoundEvents(this.identifier, null);
        events.addSound(this.sound);
        return events;
    }

    public void addAuthor(String s) {
        this.authors.add(s);
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    public RegionInfo createRegionInfo(LOTRMusicRegion reg) {
        return this.regions.computeIfAbsent(reg, RegionInfo::new);
    }

    public Set<LOTRMusicRegion> getAllRegions() {
        return this.regions.keySet();
    }

    public @Nullable RegionInfo getRegionInfo(LOTRMusicRegion reg) {
        return this.regions.get(reg);
    }

    public String getTitle() {
        return this.title != null ? this.title : this.filename;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    /** LOTRTrackRegionInfo: where in a region the track plays, when, and how often. */
    public static final class RegionInfo {
        public final LOTRMusicRegion region;
        private final List<String> subregions = new ArrayList<>();
        private double weight = 1.0;
        private final List<LOTRMusicCategory> categories = new ArrayList<>();

        RegionInfo(LOTRMusicRegion r) {
            this.region = r;
        }

        public void addAllCategories() {
            for (LOTRMusicCategory cat : LOTRMusicCategory.values()) {
                addCategory(cat);
            }
        }

        public void addAllSubregions() {
            for (String sub : this.region.getAllSubregions()) {
                addSubregion(sub);
            }
        }

        public void addCategory(LOTRMusicCategory cat) {
            if (!this.categories.contains(cat)) {
                this.categories.add(cat);
            }
        }

        public void addSubregion(String sub) {
            if (!this.subregions.contains(sub)) {
                this.subregions.add(sub);
            }
        }

        public List<LOTRMusicCategory> getCategories() {
            return this.categories;
        }

        public List<String> getSubregions() {
            return this.subregions;
        }

        public double getWeight() {
            return this.weight;
        }

        public void setWeight(double d) {
            this.weight = d;
        }
    }
}
