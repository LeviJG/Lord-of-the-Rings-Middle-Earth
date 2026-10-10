package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMusicRegion: the regions (and their named subregions) a biome's music belongs to; music packs
 * sort their tracks by them.
 *
 * <p>NOT ported yet: the music itself -- the music packs, their tracks and the ticker that plays
 * them by region (LOTRMusic, client).
 */
public enum LOTRMusicRegion {
    MENU("menu"), SEA("sea"), SHIRE("shire"), OLD_FOREST("oldForest"), LINDON("lindon"), BARROW_DOWNS("barrowDowns"),
    BREE("bree"), ERIADOR("eriador"), RIVENDELL("rivendell"), ANGMAR("angmar"), EREGION("eregion"), ENEDWAITH("enedwaith"),
    DUNLAND("dunland"), PUKEL("pukel"), MISTY_MOUNTAINS("mistyMountains"), FORODWAITH("forodwaith"),
    GREY_MOUNTAINS("greyMountains"), RHOVANION("rhovanion"), MIRKWOOD("mirkwood"), WOODLAND_REALM("woodlandRealm"),
    DALE("dale"), DWARVEN("dwarven"), LOTHLORIEN("lothlorien"), FANGORN("fangorn"), ROHAN("rohan"), ISENGARD("isengard"),
    GONDOR("gondor"), BROWN_LANDS("brownLands"), DEAD_MARSHES("deadMarshes"), MORDOR("mordor"), DORWINION("dorwinion"),
    RHUN("rhun"), NEAR_HARAD("nearHarad"), FAR_HARAD("farHarad"), FAR_HARAD_JUNGLE("farHaradJungle"),
    PERDOROGWAITH("pertorogwaith"), UTUMNO("utumno");

    public static final String ALL_REGION_CODE = "all";
    public final String regionName;
    private final List<String> subregions = new CopyOnWriteArrayList<>();

    LOTRMusicRegion(String s) {
        this.regionName = s;
    }

    public static @Nullable LOTRMusicRegion forName(String s) {
        for (LOTRMusicRegion r : values()) {
            if (s.equalsIgnoreCase(r.regionName)) {
                return r;
            }
        }
        return null;
    }

    public List<String> getAllSubregions() {
        return this.subregions;
    }

    public Sub getSubregion(@Nullable String s) {
        if (s != null && !this.subregions.contains(s)) {
            this.subregions.add(s);
        }
        return new Sub(this, s);
    }

    public Sub getWithoutSub() {
        return new Sub(this, null);
    }

    public boolean hasNoSubregions() {
        return this.subregions.isEmpty();
    }

    public boolean hasSubregion(String s) {
        return this.subregions.contains(s);
    }

    /** A region and, where it has them, one of its subregions. */
    public record Sub(LOTRMusicRegion region, @Nullable String subregion) {
    }
}
