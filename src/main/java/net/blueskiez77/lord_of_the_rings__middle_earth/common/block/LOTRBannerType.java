package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

/**
 * LOTRItemBanner.BannerType: the forty-two faction banners.
 *
 * <p>The original packed these into one item's damage value and hung the
 * faction off each constant. Damage values are gone, so each is its own pair of
 * blocks here, and the faction link is left out until LOTRFaction's banner
 * side -- territory protection, alignment gates -- is ported; the enum order is
 * the original's bannerID order so that mapping is a one-liner when it lands.
 */
public enum LOTRBannerType implements StringRepresentable {
    GONDOR("gondor"),
    ROHAN("rohan"),
    MORDOR("mordor"),
    LOTHLORIEN("lothlorien"),
    MIRKWOOD("mirkwood"),
    DUNLAND("dunland"),
    ISENGARD("isengard"),
    DURIN("durin"),
    ANGMAR("angmar"),
    NEAR_HARAD("near_harad"),
    HIGH_ELF("high_elf"),
    BLUE_MOUNTAINS("blue_mountains"),
    RANGER("ranger"),
    DOL_GULDUR("dol_guldur"),
    GUNDABAD("gundabad"),
    HALF_TROLL("half_troll"),
    DOL_AMROTH("dol_amroth"),
    MOREDAIN("moredain"),
    TAUREDAIN("tauredain"),
    DALE("dale"),
    DORWINION("dorwinion"),
    HOBBIT("hobbit"),
    ANORIEN("anorien"),
    ITHILIEN("ithilien"),
    LOSSARNACH("lossarnach"),
    LEBENNIN("lebennin"),
    PELARGIR("pelargir"),
    BLACKROOT_VALE("blackroot_vale"),
    PINNATH_GELIN("pinnath_gelin"),
    MINAS_MORGUL("minas_morgul"),
    BLACK_URUK("black_uruk"),
    GONDOR_STEWARD("gondor_steward"),
    NAN_UNGOL("nan_ungol"),
    RHUDAUR("rhudaur"),
    LAMEDON("lamedon"),
    RHUN("rhun"),
    RIVENDELL("rivendell"),
    ESGAROTH("esgaroth"),
    UMBAR("umbar"),
    HARAD_NOMAD("harad_nomad"),
    HARAD_GULF("harad_gulf"),
    BREE("bree");

    public static final Codec<LOTRBannerType> CODEC = StringRepresentable.fromEnum(LOTRBannerType::values);

    private final String name;

    LOTRBannerType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    /** lotr:textures/entity/banner/<name>.png -- was lotr:item/banner/banner_<name>.png. */
    public String textureName() {
        return this.name;
    }
}
