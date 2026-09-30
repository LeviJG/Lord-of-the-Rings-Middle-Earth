package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import java.util.Locale;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.world.item.Item;

/** LOTREntityWarg.WargType: a warg's coat, and the rug its skin makes. */
public enum LOTRWargType {
    BROWN(0, () -> LOTRItems.BROWN_WARGSKIN_RUG),
    GREY(1, () -> LOTRItems.GREY_WARGSKIN_RUG),
    BLACK(2, () -> LOTRItems.BLACK_WARGSKIN_RUG),
    WHITE(3, () -> LOTRItems.WHITE_WARGSKIN_RUG),
    ICE(4, () -> LOTRItems.ICE_WARGSKIN_RUG),
    OBSIDIAN(5, () -> LOTRItems.OBSIDIAN_WARGSKIN_RUG),
    FIRE(6, () -> LOTRItems.FIRE_WARGSKIN_RUG);

    public final int wargID;
    private final Supplier<Item> rug;

    LOTRWargType(int id, Supplier<Item> rug) {
        this.wargID = id;
        this.rug = rug;
    }

    public static LOTRWargType forID(int id) {
        for (LOTRWargType type : values()) {
            if (type.wargID == id) {
                return type;
            }
        }
        return BROWN;
    }

    public String textureName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Item rug() {
        return this.rug.get();
    }
}
