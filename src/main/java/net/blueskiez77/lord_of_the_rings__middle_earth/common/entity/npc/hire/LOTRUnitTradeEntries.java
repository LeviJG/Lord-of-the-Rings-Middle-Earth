package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

/**
 * LOTRUnitTradeEntries: a hirer's list of units, each asking the list's base
 * alignment on top of its own. The lists arrive with their hirers.
 */
public final class LOTRUnitTradeEntries {

    public static final LOTRUnitTradeEntries HOBBIT_SHIRRIFF = new LOTRUnitTradeEntries(50.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HOBBIT_BOUNDER, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HOBBIT_BOUNDER, () -> LOTREntities.SHIRE_PONY,
                    "HobbitBounder_Pony", 40, 50.0f));

    public static final LOTRUnitTradeEntries HOBBIT_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HOBBIT_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries BREE_CAPTAIN = new LOTRUnitTradeEntries(100.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.BREE_GUARD, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BREE_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries BREE_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.BREE_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries ROHIRRIM_MARSHAL = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHIRRIM_WARRIOR, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHIRRIM_ARCHER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHIRRIM_WARRIOR, () -> LOTREntities.HORSE, "Rohirrim_Horse", 50, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.ROHIRRIC_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHIRRIM_ARCHER, () -> LOTREntities.HORSE, "RohirrimArcher_Horse", 70, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.ROHIRRIC_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHAN_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries ROHAN_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.ROHAN_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries GONDORIAN_CAPTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_SOLDIER, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_SOLDIER, () -> LOTREntities.HORSE, "GondorSoldier_Horse", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.GONDOR_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_TOWER_GUARD, 50, 250.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries DOL_AMROTH_CAPTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_AMROTH_SOLDIER, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_AMROTH_ARCHER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.SWAN_KNIGHT, 50, 100.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_AMROTH_SOLDIER, () -> LOTREntities.HORSE, "DolAmrothSoldier_Horse", 50, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.DOL_AMROTH_HORSE_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.SWAN_KNIGHT, () -> LOTREntities.HORSE, "SwanKnight_Horse", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.DOL_AMROTH_HORSE_ARMOR, 1.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_AMROTH_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries LOSSARNACH_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LOSSARNACH_AXEMAN, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LOSSARNACH_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries PELARGIR_CAPTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.LEBENNIN_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.PELARGIR_MARINE, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.PELARGIR_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries PINNATH_GELIN_CAPTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.PINNATH_GELIN_SOLDIER, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.PINNATH_GELIN_SOLDIER, () -> LOTREntities.HORSE, "PinnathGelinSoldier_Horse", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.GONDOR_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.PINNATH_GELIN_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries BLACKROOT_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACKROOT_SOLDIER, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACKROOT_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACKROOT_SOLDIER, () -> LOTREntities.HORSE, "BlackrootSoldier_Horse", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.GONDOR_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACKROOT_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries LEBENNIN_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.LEBENNIN_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_SOLDIER, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LEBENNIN_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries LAMEDON_CAPTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.LAMEDON_HILLMAN, 15, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LAMEDON_SOLDIER, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LAMEDON_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LAMEDON_SOLDIER, () -> LOTREntities.HORSE, "LamedonSoldier_Horse", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.LAMEDON_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.LAMEDON_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries ELF_LORD = new LOTRUnitTradeEntries(300.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GALADHRIM_ELF, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GALADHRIM_WARDEN, 40, 50.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.GALADHRIM_WARRIOR, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.GALADHRIM_WARRIOR, () -> LOTREntities.HORSE, "GaladhrimWarrior_Horse", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.GALADHRIM_HORSE_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.GALADHRIM_BANNER_BEARER, 70, 250.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_ELF));

    public static final LOTRUnitTradeEntries HIGH_ELF_LORD = new LOTRUnitTradeEntries(300.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HIGH_ELF, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HIGH_ELF_WARRIOR, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.HIGH_ELF_WARRIOR, () -> LOTREntities.HORSE, "HighElfWarrior_Horse", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.LINDON_HORSE_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.HIGH_ELF_BANNER_BEARER, 70, 250.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_ELF));

    public static final LOTRUnitTradeEntries RIVENDELL_LORD = new LOTRUnitTradeEntries(300.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.RIVENDELL_ELF, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.RIVENDELL_WARRIOR, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.RIVENDELL_WARRIOR, () -> LOTREntities.HORSE, "RivendellWarrior_Horse", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.RIVENDELL_HORSE_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.RIVENDELL_BANNER_BEARER, 70, 250.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_ELF));

    public static final LOTRUnitTradeEntries DWARF_COMMANDER = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF_WARRIOR, 30, 50.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF_AXE_THROWER, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF_WARRIOR, () -> LOTREntities.WILD_BOAR, "DwarfWarrior_Boar", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.DWARVEN_BOAR_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF_AXE_THROWER, () -> LOTREntities.WILD_BOAR, "DwarfAxeThrower_Boar", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.DWARVEN_BOAR_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.DWARF_BANNER_BEARER, 50, 200.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_DWARF));

    public static final LOTRUnitTradeEntries BLUE_DWARF_COMMANDER = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF_WARRIOR, 30, 50.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF_AXE_THROWER, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF_WARRIOR, () -> LOTREntities.WILD_BOAR, "BlueDwarfWarrior_Boar", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.BLUE_DWARVEN_BOAR_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF_AXE_THROWER, () -> LOTREntities.WILD_BOAR, "BlueDwarfAxeThrower_Boar", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.BLUE_DWARVEN_BOAR_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_DWARF),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLUE_DWARF_BANNER_BEARER, 50, 200.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_DWARF));

    public static final LOTRUnitTradeEntries ANGMAR_HILLMAN_CHIEFTAIN = new LOTRUnitTradeEntries(100.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN, 15, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN_WARRIOR, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN_AXE_THROWER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN, () -> LOTREntities.ANGMAR_WARG, "AngmarHillman_Warg", 35, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN_WARRIOR, () -> LOTREntities.ANGMAR_WARG, "AngmarHillmanWarrior_Warg", 50, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.ANGMAR_WARG_ARMOR, 0.3f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN_AXE_THROWER, () -> LOTREntities.ANGMAR_WARG, "AngmarHillmanAxeThrower_Warg", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.ANGMAR_WARG_ARMOR, 0.3f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_HILLMAN_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries ANGMAR_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_ORC, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_ORC_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_ORC_BOMBARDIER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_WARG, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_ORC, () -> LOTREntities.ANGMAR_WARG, "AngmarOrc_Warg", 40, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.ANGMAR_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_ORC_ARCHER, () -> LOTREntities.ANGMAR_WARG, "AngmarOrcArcher_Warg", 60, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.ANGMAR_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_WARG_BOMBARDIER, 50, 250.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.TROLL, 100, 250.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MOUNTAIN_TROLL, 120, 350.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.ANGMAR_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries GUNDABAD_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntries(100.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_ORC, 15, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_ORC_ARCHER, 35, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_WARG, 20, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_ORC, () -> LOTREntities.GUNDABAD_WARG, "GundabadOrc_Warg", 35, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_ORC_ARCHER, () -> LOTREntities.GUNDABAD_WARG, "GundabadOrcArcher_Warg", 55, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_URUK, 40, 250.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_URUK_ARCHER, 60, 300.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.GUNDABAD_BANNER_BEARER, 35, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries DOL_GULDUR_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_GULDUR_ORC, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_GULDUR_ORC_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MIRKWOOD_SPIDER, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_GULDUR_ORC, () -> LOTREntities.MIRKWOOD_SPIDER, "DolGuldurOrc_Spider", 40, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_GULDUR_ORC_ARCHER, () -> LOTREntities.MIRKWOOD_SPIDER, "DolGuldurOrcArcher_Spider", 60, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MIRK_TROLL, 100, 350.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.DOL_GULDUR_BANNER_BEARER, 40, 0.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries MORDOR_ORC_SPIDER_KEEPER = new LOTRUnitTradeEntries(250.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_SPIDER, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC_ARCHER, 40, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC, () -> LOTREntities.MORDOR_SPIDER, "MordorOrc_Spider", 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC_ARCHER, () -> LOTREntities.MORDOR_SPIDER, "MordorOrcArcher_Spider", 70, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NAN_UNGOL_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries UMBAR_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.UMBAR_WARRIOR, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.UMBAR_ARCHER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.UMBAR_WARRIOR, () -> LOTREntities.HORSE, "UmbarWarrior_Horse", 50, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.UMBARIC_HORSE_ARMOR, 1.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.UMBAR_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries CORSAIR_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.CORSAIR, 20, 0.0f).setExtraInfo("Corsair"));

    public static final LOTRUnitTradeEntries CORSAIR_SLAVER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HARAD_SLAVE, 40, 0.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries HARNEDOR_WARLORD = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_WARRIOR, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_WARRIOR, () -> LOTREntities.HORSE, "HarnedorWarrior_Horse", 40, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_ARCHER, () -> LOTREntities.HORSE, "HarnedorArcher_Horse", 60, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries NOMAD_WARLORD = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.NOMAD_WARRIOR, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NOMAD_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NOMAD_WARRIOR, () -> LOTREntities.CAMEL, "NomadWarrior_Camel", 40, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NOMAD_ARCHER, () -> LOTREntities.CAMEL, "NomadArcher_Camel", 60, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NOMAD_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries GULF_WARLORD = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GULF_WARRIOR, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GULF_ARCHER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GULF_WARRIOR, () -> LOTREntities.HORSE, "GulfWarrior_Horse", 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GULF_ARCHER, () -> LOTREntities.HORSE, "GulfArcher_Horse", 70, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.GULF_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries EASTERLING_WARLORD = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_LEVYMAN, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_WARRIOR, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_GOLD_WARRIOR, 50, 200.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_WARRIOR, () -> LOTREntities.HORSE, "EasterlingWarrior_Horse", 50, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_ARCHER, () -> LOTREntities.HORSE, "EasterlingArcher_Horse", 70, 200.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_GOLD_WARRIOR, () -> LOTREntities.HORSE, "EasterlingGoldWarrior_Horse", 70, 300.0f)
                    .setMountArmor(() -> LOTRCombatItems.RHUNIC_HORSE_ARMOR, 1.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_FIRE_THROWER, 60, 150.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries EASTERLING_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.EASTERLING_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries MOREDAIN_CHIEFTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.MOREDAIN_WARRIOR, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MOREDAIN_WARRIOR, () -> LOTREntities.ZEBRA, "MoredainWarrior_Zebra", 40, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MOREDAIN_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries TAUREDAIN_CHIEFTAIN = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.TAUREDAIN_WARRIOR, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.TAUREDAIN_BLOWGUNNER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.TAUREDAIN_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries TAUREDAIN_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.TAUREDAIN_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries HALF_TROLL_WARLORD = new LOTRUnitTradeEntries(200.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HALF_TROLL, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HALF_TROLL_WARRIOR, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HALF_TROLL_WARRIOR, () -> LOTREntities.RHINO, "HalfTrollWarrior_Rhino", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.HALF_TROLL_RHINO_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.HALF_TROLL_BANNER_BEARER, 70, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries HARNEDOR_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.HARNEDOR_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries NEAR_HARADRIM_WARLORD = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.NEAR_HARADRIM_WARRIOR, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.NEAR_HARADRIM_ARCHER, 50, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.SOUTHRON_CHAMPION, () -> LOTREntities.HORSE, "SouthronChampion_Horse", 60, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.COAST_SOUTHRON_HORSE_ARMOR, 1.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.NEAR_HARAD_BANNER_BEARER, 50, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries DUNLENDING_WARLORD = new LOTRUnitTradeEntries(100.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING, 15, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING_WARRIOR, 30, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING_ARCHER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING_AXE_THROWER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING_BERSERKER, 50, 200.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.DUNLENDING_BANNER_BEARER, 50, 200.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries URUK_HAI_MERCENARY_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.ISENGARD_SNAGA, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ISENGARD_SNAGA_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_HAI, 40, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_HAI_CROSSBOWER, 60, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_HAI_SAPPER, 70, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_HAI_BERSERKER, 60, 150.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_WARG, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ISENGARD_SNAGA, () -> LOTREntities.URUK_WARG, "IsengardSnaga_Warg", 40, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.ISENGARD_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.ISENGARD_SNAGA_ARCHER, () -> LOTREntities.URUK_WARG, "IsengardSnagaArcher_Warg", 60, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.ISENGARD_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_WARG_BOMBARDIER, 50, 250.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.URUK_HAI_BANNER_BEARER, 60, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries MORDOR_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC_ARCHER, 40, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC_BOMBARDIER, 50, 100.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_WARG, 20, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC, () -> LOTREntities.MORDOR_WARG, "MordorOrc_Warg", 40, 100.0f)
                    .setMountArmor(() -> LOTRCombatItems.MORDOR_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_ORC_ARCHER, () -> LOTREntities.MORDOR_WARG, "MordorOrcArcher_Warg", 60, 150.0f)
                    .setMountArmor(() -> LOTRCombatItems.MORDOR_WARG_ARMOR, 0.5f),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_WARG_BOMBARDIER, 50, 250.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.OLOG_HAI, 120, 350.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.MORDOR_BANNER_BEARER, 40, 150.0f).setBannerBearer(),
            new LOTRUnitTradeEntry(() -> LOTREntities.MINAS_MORGUL_BANNER_BEARER, 40, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries BLACK_URUK_CAPTAIN = new LOTRUnitTradeEntries(400.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACK_URUK, 60, 250.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACK_URUK_ARCHER, 80, 300.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.OLOG_HAI, 120, 350.0f).setPledgeExclusive(),
            new LOTRUnitTradeEntry(() -> LOTREntities.BLACK_URUK_BANNER_BEARER, 80, 400.0f).setBannerBearer()
                    .setPledgeExclusive());

    public static final LOTRUnitTradeEntries WOOD_ELF_CAPTAIN = new LOTRUnitTradeEntries(250.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.WOOD_ELF, 30, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.WOOD_ELF_SCOUT, 40, 50.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.WOOD_ELF_WARRIOR, 50, 100.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.WOOD_ELF_WARRIOR, () -> LOTREntities.ELK, "WoodElfWarrior_Elk", 70, 200.0f)
                    .setMountArmor(() -> LOTRCombatItems.WOOD_ELVEN_ELK_ARMOR, 1.0f).setPledgeType(LOTRUnitPledgeType.ANY_ELF),
            new LOTRUnitTradeEntry(() -> LOTREntities.WOOD_ELF_BANNER_BEARER, 70, 250.0f).setBannerBearer()
                    .setPledgeType(LOTRUnitPledgeType.ANY_ELF));

    public static final LOTRUnitTradeEntries DORWINION_CAPTAIN = new LOTRUnitTradeEntries(150.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_GUARD, 40, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_CROSSBOWER, 60, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_BANNER_BEARER, 60, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries DORWINION_ELF_CAPTAIN = new LOTRUnitTradeEntries(250.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_ELF_WARRIOR, 50, 0.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_ELF_ARCHER, 70, 50.0f),
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_ELF_BANNER_BEARER, 70, 150.0f).setBannerBearer());

    public static final LOTRUnitTradeEntries DORWINION_VINEKEEPER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.DORWINION_VINEHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public static final LOTRUnitTradeEntries GONDOR_FARMER = new LOTRUnitTradeEntries(0.0f,
            new LOTRUnitTradeEntry(() -> LOTREntities.GONDOR_FARMHAND, 40, 50.0f).setTask(LOTRHiredTask.FARMER));

    public final List<LOTRUnitTradeEntry> tradeEntries;

    private LOTRUnitTradeEntries(float baseAlignment, LOTRUnitTradeEntry... trades) {
        for (LOTRUnitTradeEntry trade : trades) {
            trade.alignmentRequired += baseAlignment;
            if (trade.alignmentRequired < 0.0f) {
                throw new IllegalArgumentException("Units cannot require negative alignment!");
            }
        }
        this.tradeEntries = List.of(trades);
    }
}
