package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.util.StringRepresentable;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemBanner.BannerType: the forty-two faction banners.
 *
 * <p>The original packed these into one item's damage value and hung the
 * faction off each constant. Damage values are gone, so each is its own pair of
 * blocks here. The enum order is the original's bannerID order.
 */
public enum LOTRBannerType implements StringRepresentable {
    GONDOR("gondor", LOTRFaction.GONDOR),
    ROHAN("rohan", LOTRFaction.ROHAN),
    MORDOR("mordor", LOTRFaction.MORDOR),
    LOTHLORIEN("lothlorien", LOTRFaction.LOTHLORIEN),
    MIRKWOOD("mirkwood", LOTRFaction.WOOD_ELF),
    DUNLAND("dunland", LOTRFaction.DUNLAND),
    ISENGARD("isengard", LOTRFaction.ISENGARD),
    DURIN("durin", LOTRFaction.DURINS_FOLK),
    ANGMAR("angmar", LOTRFaction.ANGMAR),
    NEAR_HARAD("near_harad", LOTRFaction.NEAR_HARAD),
    HIGH_ELF("high_elf", LOTRFaction.HIGH_ELF),
    BLUE_MOUNTAINS("blue_mountains", LOTRFaction.BLUE_MOUNTAINS),
    RANGER("ranger", LOTRFaction.RANGER_NORTH),
    DOL_GULDUR("dol_guldur", LOTRFaction.DOL_GULDUR),
    GUNDABAD("gundabad", LOTRFaction.GUNDABAD),
    HALF_TROLL("half_troll", LOTRFaction.HALF_TROLL),
    DOL_AMROTH("dol_amroth", LOTRFaction.GONDOR),
    MOREDAIN("moredain", LOTRFaction.MORWAITH),
    TAUREDAIN("tauredain", LOTRFaction.TAURETHRIM),
    DALE("dale", LOTRFaction.DALE),
    DORWINION("dorwinion", LOTRFaction.DORWINION),
    HOBBIT("hobbit", LOTRFaction.HOBBIT),
    ANORIEN("anorien", LOTRFaction.GONDOR),
    ITHILIEN("ithilien", LOTRFaction.GONDOR),
    LOSSARNACH("lossarnach", LOTRFaction.GONDOR),
    LEBENNIN("lebennin", LOTRFaction.GONDOR),
    PELARGIR("pelargir", LOTRFaction.GONDOR),
    BLACKROOT_VALE("blackroot_vale", LOTRFaction.GONDOR),
    PINNATH_GELIN("pinnath_gelin", LOTRFaction.GONDOR),
    MINAS_MORGUL("minas_morgul", LOTRFaction.MORDOR),
    BLACK_URUK("black_uruk", LOTRFaction.MORDOR),
    GONDOR_STEWARD("gondor_steward", LOTRFaction.GONDOR),
    NAN_UNGOL("nan_ungol", LOTRFaction.MORDOR),
    RHUDAUR("rhudaur", LOTRFaction.ANGMAR),
    LAMEDON("lamedon", LOTRFaction.GONDOR),
    RHUN("rhun", LOTRFaction.RHUDEL),
    RIVENDELL("rivendell", LOTRFaction.HIGH_ELF),
    ESGAROTH("esgaroth", LOTRFaction.DALE),
    UMBAR("umbar", LOTRFaction.NEAR_HARAD),
    HARAD_NOMAD("harad_nomad", LOTRFaction.NEAR_HARAD),
    HARAD_GULF("harad_gulf", LOTRFaction.NEAR_HARAD),
    BREE("bree", LOTRFaction.BREE);

    public static final Codec<LOTRBannerType> CODEC = StringRepresentable.fromEnum(LOTRBannerType::values);

    private final String name;
    /** The faction whose banner this is: whose alignment it asks of those in its territory. */
    public final LOTRFaction faction;

    LOTRBannerType(String name, LOTRFaction faction) {
        this.name = name;
        this.faction = faction;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    /** The original's constant names, in bannerID order -- the names the structures pass. */
    private static final String[] LEGACY_NAMES = {
            "GONDOR", "ROHAN", "MORDOR", "GALADHRIM", "WOOD_ELF", "DUNLAND", "ISENGARD", "DWARF", "ANGMAR",
            "NEAR_HARAD", "HIGH_ELF", "BLUE_MOUNTAINS", "RANGER_NORTH", "DOL_GULDUR", "GUNDABAD",
            "HALF_TROLL", "DOL_AMROTH", "MOREDAIN", "TAUREDAIN", "DALE", "DORWINION", "HOBBIT", "ANORIEN",
            "ITHILIEN", "LOSSARNACH", "LEBENNIN", "PELARGIR", "BLACKROOT_VALE", "PINNATH_GELIN",
            "MINAS_MORGUL", "BLACK_URUK", "GONDOR_STEWARD", "NAN_UNGOL", "RHUDAUR", "LAMEDON", "RHUN",
            "RIVENDELL", "ESGAROTH", "UMBAR", "HARAD_NOMAD", "HARAD_GULF", "BREE"
    };

    /** LOTRItemBanner.BannerType.valueOf: this banner by the original's name for it, or null. */
    public static @Nullable LOTRBannerType forLegacyName(String name) {
        for (int i = 0; i < LEGACY_NAMES.length; ++i) {
            if (LEGACY_NAMES[i].equals(name)) {
                return values()[i];
            }
        }
        return null;
    }

    /** lotr:textures/entity/banner/<name>.png -- was lotr:item/banner/banner_<name>.png. */
    public String textureName() {
        return this.name;
    }
}
