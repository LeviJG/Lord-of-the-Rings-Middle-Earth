package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.resources.Identifier;

/**
 * LOTRCapes: the capes NPCs wear on their backs ({@code cape/<name>.png}),
 * set as an NPC's {@link LOTRNPCEntity#npcCape}.
 */
public final class LOTRCapes {

    public static final Identifier GONDOR = forName("gondor");
    public static final Identifier TOWER_GUARD = forName("gondor_tower_guard");
    public static final Identifier RANGER = forName("ranger");
    public static final Identifier RANGER_ITHILIEN = forName("ranger_ithilien");
    public static final Identifier LOSSARNACH = forName("lossarnach");
    public static final Identifier PELARGIR = forName("pelargir");
    public static final Identifier BLACKROOT = forName("blackroot");
    public static final Identifier PINNATH_GELIN = forName("pinnath_gelin");
    public static final Identifier LAMEDON = forName("lamedon");
    public static final Identifier ROHAN = forName("rohan");
    public static final Identifier DALE = forName("dale");
    public static final Identifier DUNLENDING_BERSERKER = forName("dunlending_berserker");
    public static final Identifier GALADHRIM = forName("galadhrim");
    public static final Identifier GALADHRIM_TRADER = forName("galadhrim_trader");
    public static final Identifier WOOD_ELF = forName("wood_elf");
    public static final Identifier HIGH_ELF = forName("high_elf");
    public static final Identifier RIVENDELL = forName("rivendell");
    public static final Identifier RIVENDELL_TRADER = forName("rivendell_trader");
    public static final Identifier NEAR_HARAD = forName("near_harad");
    public static final Identifier SOUTHRON_CHAMPION = forName("harad_champion");
    public static final Identifier GULF_HARAD = forName("gulf");
    public static final Identifier TAURETHRIM = forName("taurethrim");
    public static final Identifier DORWINION_CAPTAIN = forName("dorwinion_captain");
    public static final Identifier DORWINION_ELF_CAPTAIN = forName("dorwinion_elf_captain");
    public static final Identifier GANDALF = forName("gandalf");
    public static final Identifier GANDALF_SANTA = forName("santa");

    private LOTRCapes() {
    }

    public static Identifier forName(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/cape/" + name + ".png");
    }
}
