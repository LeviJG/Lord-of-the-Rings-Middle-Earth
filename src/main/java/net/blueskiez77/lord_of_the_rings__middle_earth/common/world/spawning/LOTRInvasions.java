package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * LOTRInvasions: who turns up when a warhorn is blown.
 *
 * <p>A faction can field more than one kind of warband -- Gondor alone has six,
 * one for each of its fiefs, and the high elves have Lindon and Rivendell -- so
 * an invasion is a FACTION plus an optional subfaction, and it is the invasion
 * rather than the faction that a warhorn carries.
 *
 * <p>ONLY the identity half is ported. The original also held invasionMobs, a
 * weighted table of the NPCs each warband is made of, and invasionIcon. Both
 * want the NPCs, which the port does not have; the names and the factions do
 * not, and they are what {@link LOTRWarhornItem} needs. The mob table slots in
 * here when the NPCs land.
 */
public enum LOTRInvasions {

    HOBBIT(LOTRFaction.HOBBIT),
    BREE(LOTRFaction.BREE),
    RANGER_NORTH(LOTRFaction.RANGER_NORTH),
    BLUE_MOUNTAINS(LOTRFaction.BLUE_MOUNTAINS),
    HIGH_ELF_LINDON(LOTRFaction.HIGH_ELF, "lindon"),
    HIGH_ELF_RIVENDELL(LOTRFaction.HIGH_ELF, "rivendell"),
    GUNDABAD(LOTRFaction.GUNDABAD),
    GUNDABAD_WARG(LOTRFaction.GUNDABAD, "warg"),
    ANGMAR(LOTRFaction.ANGMAR),
    ANGMAR_HILLMEN(LOTRFaction.ANGMAR, "hillmen"),
    ANGMAR_WARG(LOTRFaction.ANGMAR, "warg"),
    WOOD_ELF(LOTRFaction.WOOD_ELF),
    DOL_GULDUR(LOTRFaction.DOL_GULDUR),
    DALE(LOTRFaction.DALE),
    DWARF(LOTRFaction.DURINS_FOLK),
    GALADHRIM(LOTRFaction.LOTHLORIEN),
    DUNLAND(LOTRFaction.DUNLAND),
    URUK_HAI(LOTRFaction.ISENGARD),
    FANGORN(LOTRFaction.FANGORN),
    ROHAN(LOTRFaction.ROHAN),
    GONDOR(LOTRFaction.GONDOR),
    GONDOR_ITHILIEN(LOTRFaction.GONDOR, "ithilien"),
    GONDOR_LOSSARNACH(LOTRFaction.GONDOR, "lossarnach"),
    GONDOR_PELARGIR(LOTRFaction.GONDOR, "pelargir"),
    GONDOR_BLACKROOT(LOTRFaction.GONDOR, "blackroot"),
    GONDOR_LEBENNIN(LOTRFaction.GONDOR, "lebennin"),
    GONDOR_LAMEDON(LOTRFaction.GONDOR, "lamedon"),
    MORDOR(LOTRFaction.MORDOR),
    MORDOR_WARG(LOTRFaction.MORDOR, "warg"),
    DORWINION(LOTRFaction.DORWINION),
    DORWINION_ELF(LOTRFaction.DORWINION, "elf"),
    RHUN(LOTRFaction.RHUDEL),
    NEAR_HARAD_HARNEDOR(LOTRFaction.NEAR_HARAD, "harnedor"),
    NEAR_HARAD_COAST(LOTRFaction.NEAR_HARAD, "coast"),
    NEAR_HARAD_UMBAR(LOTRFaction.NEAR_HARAD, "umbar"),
    NEAR_HARAD_CORSAIR(LOTRFaction.NEAR_HARAD, "corsair"),
    NEAR_HARAD_NOMAD(LOTRFaction.NEAR_HARAD, "nomad"),
    NEAR_HARAD_GULF(LOTRFaction.NEAR_HARAD, "gulf"),
    MOREDAIN(LOTRFaction.MORWAITH),
    TAUREDAIN(LOTRFaction.TAURETHRIM),
    HALF_TROLL(LOTRFaction.HALF_TROLL);

    public final LOTRFaction invasionFaction;

    /** The fief, tribe or branch, where a faction fields more than one. */
    @Nullable
    public final String subfaction;

    LOTRInvasions(LOTRFaction faction) {
        this(faction, null);
    }

    LOTRInvasions(LOTRFaction faction, @Nullable String subfaction) {
        this.invasionFaction = faction;
        this.subfaction = subfaction;
    }

    /** codeName: the faction's, with the subfaction on the end where there is one. */
    public String codeName() {
        return this.subfaction == null
                ? this.invasionFaction.codeName()
                : this.invasionFaction.codeName() + "_" + this.subfaction;
    }

    /**
     * invasionName: a warband with no subfaction is just called after its
     * faction; one with a subfaction has a name of its own.
     */
    public Component invasionName() {
        return this.subfaction == null
                ? this.invasionFaction.factionName()
                : Component.translatable("lotr.invasion." + codeName());
    }

    @Nullable
    public static LOTRInvasions forName(String name) {
        for (LOTRInvasions invasion : values()) {
            if (invasion.codeName().equals(name)) {
                return invasion;
            }
        }
        return null;
    }
}
