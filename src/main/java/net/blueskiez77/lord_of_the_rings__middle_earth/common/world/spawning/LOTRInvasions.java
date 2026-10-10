package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRWarhornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRInvasions: who turns up when a warhorn is blown.
 *
 * <p>A faction can field more than one kind of warband -- Gondor alone has eight,
 * one for each of its fiefs, and the high elves have Lindon and Rivendell -- so
 * an invasion is a FACTION plus an optional subfaction, and it is the invasion
 * rather than the faction that a warhorn carries.
 *
 * <p>Each warband is made of its weighted NPCs ({@link #invasionMobs}) and shown by its
 * icon, an item of its people's.
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
    GONDOR_DOL_AMROTH(LOTRFaction.GONDOR, "dolAmroth"),
    GONDOR_LOSSARNACH(LOTRFaction.GONDOR, "lossarnach"),
    GONDOR_PELARGIR(LOTRFaction.GONDOR, "pelargir"),
    GONDOR_PINNATH_GELIN(LOTRFaction.GONDOR, "pinnathGelin"),
    GONDOR_BLACKROOT(LOTRFaction.GONDOR, "blackroot"),
    GONDOR_LEBENNIN(LOTRFaction.GONDOR, "lebennin"),
    GONDOR_LAMEDON(LOTRFaction.GONDOR, "lamedon"),
    MORDOR(LOTRFaction.MORDOR),
    MORDOR_BLACK_URUK(LOTRFaction.MORDOR, "blackUruk"),
    MORDOR_NAN_UNGOL(LOTRFaction.MORDOR, "nanUngol"),
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

    /** The NPCs it is made of, each by weight. */
    public final List<InvasionSpawnEntry> invasionMobs = new ArrayList<>();
    /** Its icon on the invasion bar; an iron sword if none. */
    public @Nullable Supplier<Item> invasionIcon;

    LOTRInvasions(LOTRFaction faction) {
        this(faction, null);
    }

    /** createMobLists: each warband's NPCs and icon, once the items are registered. */
    public static void createMobLists() {
        HOBBIT.invasionIcon = () -> LOTRLegacyItems.mod("hobbitPipe", 0);
        HOBBIT.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HOBBIT_BOUNDER, 15));
        BREE.invasionIcon = () -> LOTRLegacyItems.mod("pikeIron", 0);
        BREE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BREE_GUARD, 15));
        BREE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BREE_BANNER_BEARER, 2));
        RANGER_NORTH.invasionIcon = () -> LOTRLegacyItems.mod("rangerBow", 0);
        RANGER_NORTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RANGER_NORTH, 15));
        RANGER_NORTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RANGER_NORTH_BANNER_BEARER, 2));
        BLUE_MOUNTAINS.invasionIcon = () -> LOTRLegacyItems.mod("hammerBlueDwarven", 0);
        BLUE_MOUNTAINS.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLUE_DWARF_WARRIOR, 10));
        BLUE_MOUNTAINS.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLUE_DWARF_AXE_THROWER, 5));
        BLUE_MOUNTAINS.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLUE_DWARF_BANNER_BEARER, 2));
        HIGH_ELF_LINDON.invasionIcon = () -> LOTRLegacyItems.mod("swordHighElven", 0);
        HIGH_ELF_LINDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HIGH_ELF_WARRIOR, 15));
        HIGH_ELF_LINDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HIGH_ELF_BANNER_BEARER, 2));
        HIGH_ELF_RIVENDELL.invasionIcon = () -> LOTRLegacyItems.mod("swordRivendell", 0);
        HIGH_ELF_RIVENDELL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RIVENDELL_WARRIOR, 15));
        HIGH_ELF_RIVENDELL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RIVENDELL_BANNER_BEARER, 2));
        GUNDABAD.invasionIcon = () -> LOTRLegacyItems.mod("swordGundabadUruk", 0);
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_ORC, 20));
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_ORC_ARCHER, 10));
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_WARG, 20));
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_BANNER_BEARER, 5));
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_URUK, 5));
        GUNDABAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_URUK_ARCHER, 2));
        GUNDABAD_WARG.invasionIcon = () -> LOTRLegacyItems.mod("wargBone", 0);
        GUNDABAD_WARG.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GUNDABAD_WARG, 10));
        ANGMAR.invasionIcon = () -> LOTRLegacyItems.mod("swordAngmar", 0);
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_ORC, 10));
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_ORC_ARCHER, 5));
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_ORC_BOMBARDIER, 3));
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_BANNER_BEARER, 2));
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_WARG, 10));
        ANGMAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_WARG_BOMBARDIER, 1));
        ANGMAR_HILLMEN.invasionIcon = () -> LOTRLegacyItems.mod("swordBronze", 0);
        ANGMAR_HILLMEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_HILLMAN, 10));
        ANGMAR_HILLMEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_HILLMAN_WARRIOR, 5));
        ANGMAR_HILLMEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_HILLMAN_AXE_THROWER, 5));
        ANGMAR_HILLMEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_HILLMAN_BANNER_BEARER, 2));
        ANGMAR_WARG.invasionIcon = () -> LOTRLegacyItems.mod("wargBone", 0);
        ANGMAR_WARG.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ANGMAR_WARG, 10));
        WOOD_ELF.invasionIcon = () -> LOTRLegacyItems.mod("swordWoodElven", 0);
        WOOD_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.WOOD_ELF_WARRIOR, 10));
        WOOD_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.WOOD_ELF_SCOUT, 5));
        WOOD_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.WOOD_ELF_BANNER_BEARER, 2));
        DOL_GULDUR.invasionIcon = () -> LOTRLegacyItems.mod("swordDolGuldur", 0);
        DOL_GULDUR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MIRKWOOD_SPIDER, 15));
        DOL_GULDUR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_GULDUR_ORC, 10));
        DOL_GULDUR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_GULDUR_ORC_ARCHER, 5));
        DOL_GULDUR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_GULDUR_BANNER_BEARER, 2));
        DOL_GULDUR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MIRK_TROLL, 3));
        DALE.invasionIcon = () -> LOTRLegacyItems.mod("swordDale", 0);
        DALE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DALE_LEVYMAN, 5));
        DALE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DALE_SOLDIER, 10));
        DALE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DALE_ARCHER, 5));
        DALE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DALE_BANNER_BEARER, 1));
        DALE.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ESGAROTH_BANNER_BEARER, 1));
        DWARF.invasionIcon = () -> LOTRLegacyItems.mod("hammerDwarven", 0);
        DWARF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DWARF_WARRIOR, 10));
        DWARF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DWARF_AXE_THROWER, 5));
        DWARF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DWARF_BANNER_BEARER, 2));
        GALADHRIM.invasionIcon = () -> LOTRLegacyItems.mod("swordElven", 0);
        GALADHRIM.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GALADHRIM_WARRIOR, 15));
        GALADHRIM.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GALADHRIM_BANNER_BEARER, 2));
        DUNLAND.invasionIcon = () -> LOTRLegacyItems.mod("dunlendingClub", 0);
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING, 10));
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING_WARRIOR, 5));
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING_ARCHER, 3));
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING_AXE_THROWER, 3));
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING_BERSERKER, 2));
        DUNLAND.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DUNLENDING_BANNER_BEARER, 2));
        URUK_HAI.invasionIcon = () -> LOTRLegacyItems.mod("scimitarUruk", 0);
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ISENGARD_SNAGA, 5));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ISENGARD_SNAGA_ARCHER, 5));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_HAI, 10));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_HAI_CROSSBOWER, 5));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_HAI_BERSERKER, 5));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_HAI_SAPPER, 3));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_HAI_BANNER_BEARER, 2));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_WARG, 10));
        URUK_HAI.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.URUK_WARG_BOMBARDIER, 1));
        FANGORN.invasionIcon = () -> LOTRLegacyItems.vanilla("stick", 0);
        FANGORN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ENT, 10));
        FANGORN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HUORN, 20));
        ROHAN.invasionIcon = () -> LOTRLegacyItems.mod("swordRohan", 0);
        ROHAN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ROHIRRIM_WARRIOR, 10));
        ROHAN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ROHIRRIM_ARCHER, 5));
        ROHAN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.ROHAN_BANNER_BEARER, 2));
        GONDOR.invasionIcon = () -> LOTRLegacyItems.mod("swordGondor", 0);
        GONDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 5));
        GONDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_SOLDIER, 10));
        GONDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_ARCHER, 5));
        GONDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_BANNER_BEARER, 2));
        GONDOR_ITHILIEN.invasionIcon = () -> LOTRLegacyItems.mod("gondorBow", 0);
        GONDOR_ITHILIEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RANGER_ITHILIEN, 15));
        GONDOR_ITHILIEN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.RANGER_ITHILIEN_BANNER_BEARER, 2));
        GONDOR_DOL_AMROTH.invasionIcon = () -> LOTRLegacyItems.mod("swordDolAmroth", 0);
        GONDOR_DOL_AMROTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_AMROTH_SOLDIER, 10));
        GONDOR_DOL_AMROTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_AMROTH_ARCHER, 5));
        GONDOR_DOL_AMROTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.SWAN_KNIGHT, 5));
        GONDOR_DOL_AMROTH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DOL_AMROTH_BANNER_BEARER, 2));
        GONDOR_LOSSARNACH.invasionIcon = () -> LOTRLegacyItems.mod("battleaxeLossarnach", 0);
        GONDOR_LOSSARNACH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 5));
        GONDOR_LOSSARNACH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LOSSARNACH_AXEMAN, 15));
        GONDOR_LOSSARNACH.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LOSSARNACH_BANNER_BEARER, 2));
        GONDOR_PELARGIR.invasionIcon = () -> LOTRLegacyItems.mod("tridentPelargir", 0);
        GONDOR_PELARGIR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LEBENNIN_LEVYMAN, 5));
        GONDOR_PELARGIR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.PELARGIR_MARINE, 15));
        GONDOR_PELARGIR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.PELARGIR_BANNER_BEARER, 2));
        GONDOR_PINNATH_GELIN.invasionIcon = () -> LOTRLegacyItems.mod("swordGondor", 0);
        GONDOR_PINNATH_GELIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 5));
        GONDOR_PINNATH_GELIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.PINNATH_GELIN_SOLDIER, 15));
        GONDOR_PINNATH_GELIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.PINNATH_GELIN_BANNER_BEARER, 2));
        GONDOR_BLACKROOT.invasionIcon = () -> LOTRLegacyItems.mod("blackrootBow", 0);
        GONDOR_BLACKROOT.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 5));
        GONDOR_BLACKROOT.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACKROOT_SOLDIER, 10));
        GONDOR_BLACKROOT.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACKROOT_ARCHER, 5));
        GONDOR_BLACKROOT.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACKROOT_BANNER_BEARER, 2));
        GONDOR_LEBENNIN.invasionIcon = () -> LOTRLegacyItems.mod("swordGondor", 0);
        GONDOR_LEBENNIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LEBENNIN_LEVYMAN, 10));
        GONDOR_LEBENNIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_SOLDIER, 10));
        GONDOR_LEBENNIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_ARCHER, 5));
        GONDOR_LEBENNIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LEBENNIN_BANNER_BEARER, 2));
        GONDOR_LAMEDON.invasionIcon = () -> LOTRLegacyItems.mod("hammerGondor", 0);
        GONDOR_LAMEDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LAMEDON_HILLMAN, 5));
        GONDOR_LAMEDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LAMEDON_SOLDIER, 10));
        GONDOR_LAMEDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LAMEDON_ARCHER, 5));
        GONDOR_LAMEDON.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.LAMEDON_BANNER_BEARER, 2));
        MORDOR.invasionIcon = () -> LOTRLegacyItems.mod("scimitarOrc", 0);
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_ORC, 10));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_ORC_ARCHER, 5));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_ORC_BOMBARDIER, 2));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_BANNER_BEARER, 2));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MINAS_MORGUL_BANNER_BEARER, 1));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_WARG, 10));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_WARG_BOMBARDIER, 1));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK, 2));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK_ARCHER, 1));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK_BANNER_BEARER, 1));
        MORDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.OLOG_HAI, 3));
        MORDOR_BLACK_URUK.invasionIcon = () -> LOTRLegacyItems.mod("scimitarBlackUruk", 0);
        MORDOR_BLACK_URUK.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK, 10));
        MORDOR_BLACK_URUK.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK_ARCHER, 5));
        MORDOR_BLACK_URUK.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.BLACK_URUK_BANNER_BEARER, 2));
        MORDOR_NAN_UNGOL.invasionIcon = () -> LOTRLegacyItems.mod("scimitarOrc", 0);
        MORDOR_NAN_UNGOL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_ORC, 20));
        MORDOR_NAN_UNGOL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_ORC_ARCHER, 10));
        MORDOR_NAN_UNGOL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NAN_UNGOL_BANNER_BEARER, 5));
        MORDOR_NAN_UNGOL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_SPIDER, 30));
        MORDOR_WARG.invasionIcon = () -> LOTRLegacyItems.mod("wargBone", 0);
        MORDOR_WARG.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MORDOR_WARG, 10));
        DORWINION.invasionIcon = () -> LOTRLegacyItems.mod("mugRedWine", 0);
        DORWINION.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_GUARD, 10));
        DORWINION.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_CROSSBOWER, 5));
        DORWINION.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_BANNER_BEARER, 2));
        DORWINION_ELF.invasionIcon = () -> LOTRLegacyItems.mod("spearBladorthin", 0);
        DORWINION_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_ELF_WARRIOR, 10));
        DORWINION_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_ELF_ARCHER, 5));
        DORWINION_ELF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.DORWINION_ELF_BANNER_BEARER, 2));
        RHUN.invasionIcon = () -> LOTRLegacyItems.mod("swordRhun", 0);
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_LEVYMAN, 5));
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_WARRIOR, 10));
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_ARCHER, 5));
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_GOLD_WARRIOR, 5));
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_BANNER_BEARER, 5));
        RHUN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.EASTERLING_FIRE_THROWER, 3));
        NEAR_HARAD_HARNEDOR.invasionIcon = () -> LOTRLegacyItems.mod("swordHarad", 0);
        NEAR_HARAD_HARNEDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HARNEDOR_WARRIOR, 10));
        NEAR_HARAD_HARNEDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HARNEDOR_ARCHER, 5));
        NEAR_HARAD_HARNEDOR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HARNEDOR_BANNER_BEARER, 2));
        NEAR_HARAD_COAST.invasionIcon = () -> LOTRLegacyItems.mod("scimitarNearHarad", 0);
        NEAR_HARAD_COAST.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NEAR_HARADRIM_WARRIOR, 8));
        NEAR_HARAD_COAST.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NEAR_HARADRIM_ARCHER, 5));
        NEAR_HARAD_COAST.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NEAR_HARAD_BANNER_BEARER, 2));
        NEAR_HARAD_COAST.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.SOUTHRON_CHAMPION, 2));
        NEAR_HARAD_COAST.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MOREDAIN_MERCENARY, 5));
        NEAR_HARAD_UMBAR.invasionIcon = () -> LOTRLegacyItems.mod("scimitarNearHarad", 0);
        NEAR_HARAD_UMBAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.UMBAR_WARRIOR, 100));
        NEAR_HARAD_UMBAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.UMBAR_ARCHER, 50));
        NEAR_HARAD_UMBAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.UMBAR_BANNER_BEARER, 20));
        NEAR_HARAD_UMBAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MOREDAIN_MERCENARY, 30));
        NEAR_HARAD_UMBAR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GONDOR_RENEGADE, 4));
        NEAR_HARAD_CORSAIR.invasionIcon = () -> LOTRLegacyItems.mod("swordCorsair", 0);
        NEAR_HARAD_CORSAIR.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.CORSAIR, 10));
        NEAR_HARAD_NOMAD.invasionIcon = () -> LOTRLegacyItems.mod("swordHarad", 0);
        NEAR_HARAD_NOMAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NOMAD_WARRIOR, 10));
        NEAR_HARAD_NOMAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NOMAD_ARCHER, 5));
        NEAR_HARAD_NOMAD.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.NOMAD_BANNER_BEARER, 2));
        NEAR_HARAD_GULF.invasionIcon = () -> LOTRLegacyItems.mod("swordGulfHarad", 0);
        NEAR_HARAD_GULF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GULF_WARRIOR, 10));
        NEAR_HARAD_GULF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GULF_ARCHER, 5));
        NEAR_HARAD_GULF.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.GULF_BANNER_BEARER, 2));
        MOREDAIN.invasionIcon = () -> LOTRLegacyItems.mod("spearMoredain", 0);
        MOREDAIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MOREDAIN_WARRIOR, 15));
        MOREDAIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.MOREDAIN_BANNER_BEARER, 2));
        TAUREDAIN.invasionIcon = () -> LOTRLegacyItems.mod("swordTauredain", 0);
        TAUREDAIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.TAUREDAIN_WARRIOR, 10));
        TAUREDAIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.TAUREDAIN_BLOWGUNNER, 5));
        TAUREDAIN.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.TAUREDAIN_BANNER_BEARER, 2));
        HALF_TROLL.invasionIcon = () -> LOTRLegacyItems.mod("scimitarHalfTroll", 0);
        HALF_TROLL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HALF_TROLL, 10));
        HALF_TROLL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HALF_TROLL_WARRIOR, 10));
        HALF_TROLL.invasionMobs.add(new InvasionSpawnEntry(LOTREntities.HALF_TROLL_BANNER_BEARER, 2));
    }

    public ItemStack getInvasionIcon() {
        Item sword = this.invasionIcon == null ? null : this.invasionIcon.get();
        return new ItemStack(sword == null || sword == Items.AIR ? Items.IRON_SWORD : sword);
    }

    public String codeNameHorn() {
        return "lotr.invasion." + codeName() + ".horn";
    }

    /** An NPC kind in a warband, by weight. */
    public record InvasionSpawnEntry(EntityType<? extends LOTRNPCEntity> entityClass, int itemWeight)
            implements LOTRWeightedRandom.Item {
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

    public ItemStack createConquestHorn() {
        return LOTRWarhornItem.createHorn(this);
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
