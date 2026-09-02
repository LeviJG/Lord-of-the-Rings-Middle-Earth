package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.util.StringRepresentable;

/**
 * LOTRItemBossTrophy.TrophyType: what a boss left behind.
 *
 * <p>The original's two, in its own trophyID order. Its short names -- "mtc"
 * and "mallornEnt" -- are spelled out here, since a registry name is read by
 * people rather than packed into an item's damage value.
 */
public enum LOTRTrophyType implements StringRepresentable {
    /** trophyID 0, "mtc": the Hill-troll Chieftain's twin skulls. */
    MOUNTAIN_TROLL_CHIEFTAIN("mountain_troll_chieftain"),
    /** trophyID 1, "mallornEnt": the Mallorn Ent's trunk. */
    MALLORN_ENT("mallorn_ent");

    private final String name;

    LOTRTrophyType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    /** The item id, and the item icon: <name>_trophy. */
    public String itemName() {
        return this.name + "_trophy";
    }

    public static LOTRTrophyType byIndex(int index) {
        LOTRTrophyType[] values = values();
        return index < 0 || index >= values.length ? MOUNTAIN_TROLL_CHIEFTAIN : values[index];
    }
}
