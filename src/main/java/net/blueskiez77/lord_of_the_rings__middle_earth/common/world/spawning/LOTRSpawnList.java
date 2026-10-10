package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRSpawnList: the named NPC lists the biomes draw from -- one faction's people or soldiers each,
 * weighted, with their group sizes.
 *
 * <p>NOT ported yet: Utumno's lists (UTUMNO_ICE, UTUMNO_OBSIDIAN, UTUMNO_FIRE), with Utumno and its creatures (D15).
 */
public class LOTRSpawnList {
    public static final LOTRSpawnList HOBBITS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HOBBIT, 40, 1, 4),
            new LOTRSpawnEntry(LOTREntities.HOBBIT_BOUNDER, 1, 1, 3));
    public static final LOTRSpawnList HOBBITS_ORCHARD = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HOBBIT_ORCHARDER, 10, 1, 1));
    public static final LOTRSpawnList DARK_HUORNS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DARK_HUORN, 10, 4, 4));
    public static final LOTRSpawnList BARROW_WIGHTS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.BARROW_WIGHT, 10, 1, 1));
    public static final LOTRSpawnList BREE_MEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.BREE_MAN, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.BREE_HOBBIT, 3, 4, 6));
    public static final LOTRSpawnList BREE_GUARDS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.BREE_GUARD, 10, 2, 4));
    public static final LOTRSpawnList RUFFIANS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.RUFFIAN_SPY, 10, 1, 4),
            new LOTRSpawnEntry(LOTREntities.RUFFIAN_BRUTE, 5, 1, 4)).factionOverride(LOTRFaction.ISENGARD);
    public static final LOTRSpawnList BLUE_DWARVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.BLUE_DWARF, 100, 4, 4),
            new LOTRSpawnEntry(LOTREntities.BLUE_DWARF_MINER, 15, 1, 3),
            new LOTRSpawnEntry(LOTREntities.BLUE_DWARF_WARRIOR, 20, 4, 4),
            new LOTRSpawnEntry(LOTREntities.BLUE_DWARF_AXE_THROWER, 10, 4, 4));
    public static final LOTRSpawnList DUNEDAIN_NORTH = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DUNEDAIN, 10, 2, 4));
    public static final LOTRSpawnList RANGERS_NORTH = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.RANGER_NORTH, 10, 1, 4));
    public static final LOTRSpawnList LINDON_ELVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HIGH_ELF, 10, 4, 6));
    public static final LOTRSpawnList LINDON_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HIGH_ELF_WARRIOR, 10, 4, 4));
    public static final LOTRSpawnList RIVENDELL_ELVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.RIVENDELL_ELF, 10, 4, 6));
    public static final LOTRSpawnList RIVENDELL_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.RIVENDELL_WARRIOR, 10, 4, 4));
    public static final LOTRSpawnList GUNDABAD_ORCS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GUNDABAD_ORC, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.GUNDABAD_ORC_ARCHER, 10, 4, 6));
    public static final LOTRSpawnList GUNDABAD_URUKS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GUNDABAD_URUK, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.GUNDABAD_URUK_ARCHER, 10, 2, 4));
    public static final LOTRSpawnList GUNDABAD_WARGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GUNDABAD_WARG, 10, 4, 4));
    public static final LOTRSpawnList ANGMAR_ORCS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ANGMAR_ORC, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.ANGMAR_ORC_ARCHER, 10, 4, 6));
    public static final LOTRSpawnList ANGMAR_BOMBARDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ANGMAR_ORC_BOMBARDIER, 10, 1, 2));
    public static final LOTRSpawnList ANGMAR_WARGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ANGMAR_WARG, 10, 4, 4));
    public static final LOTRSpawnList ANGMAR_HILLMEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ANGMAR_HILLMAN, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.ANGMAR_HILLMAN_WARRIOR, 5, 4, 6),
            new LOTRSpawnEntry(LOTREntities.ANGMAR_HILLMAN_AXE_THROWER, 5, 4, 6));
    public static final LOTRSpawnList TROLLS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.TROLL, 10, 1, 3));
    public static final LOTRSpawnList HILL_TROLLS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MOUNTAIN_TROLL, 10, 1, 3));
    public static final LOTRSpawnList SNOW_TROLLS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.SNOW_TROLL, 10, 1, 3));
    public static final LOTRSpawnList WOOD_ELVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.WOOD_ELF, 10, 4, 6));
    public static final LOTRSpawnList WOOD_ELF_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.WOOD_ELF_SCOUT, 10, 4, 4),
            new LOTRSpawnEntry(LOTREntities.WOOD_ELF_WARRIOR, 5, 4, 4));
    public static final LOTRSpawnList MIRKWOOD_SPIDERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MIRKWOOD_SPIDER, 10, 4, 6));
    public static final LOTRSpawnList DOL_GULDUR_ORCS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DOL_GULDUR_ORC, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.DOL_GULDUR_ORC_ARCHER, 10, 4, 6));
    public static final LOTRSpawnList MIRK_TROLLS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MIRK_TROLL, 10, 1, 3));
    public static final LOTRSpawnList DALE_MEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DALE_MAN, 10, 2, 4));
    public static final LOTRSpawnList DALE_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DALE_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.DALE_SOLDIER, 10, 2, 8),
            new LOTRSpawnEntry(LOTREntities.DALE_ARCHER, 5, 2, 8));
    public static final LOTRSpawnList DWARVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DWARF, 100, 4, 4),
            new LOTRSpawnEntry(LOTREntities.DWARF_MINER, 15, 1, 3),
            new LOTRSpawnEntry(LOTREntities.DWARF_WARRIOR, 20, 4, 4),
            new LOTRSpawnEntry(LOTREntities.DWARF_AXE_THROWER, 10, 4, 4));
    public static final LOTRSpawnList GALADHRIM = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GALADHRIM_ELF, 10, 4, 6));
    public static final LOTRSpawnList GALADHRIM_WARDENS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GALADHRIM_WARDEN, 10, 4, 4));
    public static final LOTRSpawnList GALADHRIM_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GALADHRIM_WARRIOR, 10, 4, 4));
    public static final LOTRSpawnList DUNLENDINGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DUNLENDING, 30, 4, 6));
    public static final LOTRSpawnList DUNLENDING_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DUNLENDING_WARRIOR, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.DUNLENDING_ARCHER, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.DUNLENDING_AXE_THROWER, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.DUNLENDING_BERSERKER, 5, 1, 2));
    public static final LOTRSpawnList ENTS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ENT, 10, 4, 4));
    public static final LOTRSpawnList HUORNS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HUORN, 10, 4, 4));
    public static final LOTRSpawnList ISENGARD_SNAGA = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ISENGARD_SNAGA, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.ISENGARD_SNAGA_ARCHER, 5, 4, 6));
    public static final LOTRSpawnList URUK_HAI = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.URUK_HAI, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.URUK_HAI_CROSSBOWER, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.URUK_HAI_SAPPER, 3, 1, 2),
            new LOTRSpawnEntry(LOTREntities.URUK_HAI_BERSERKER, 5, 4, 6));
    public static final LOTRSpawnList URUK_WARGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.URUK_WARG, 10, 4, 4));
    public static final LOTRSpawnList ROHIRRIM = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ROHAN_MAN, 10, 4, 6));
    public static final LOTRSpawnList ROHIRRIM_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.ROHIRRIM_WARRIOR, 20, 3, 8),
            new LOTRSpawnEntry(LOTREntities.ROHIRRIM_ARCHER, 10, 3, 8));
    public static final LOTRSpawnList GONDOR_MEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_MAN, 10, 4, 6));
    public static final LOTRSpawnList GONDOR_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.GONDOR_SOLDIER, 10, 2, 8),
            new LOTRSpawnEntry(LOTREntities.GONDOR_ARCHER, 5, 2, 8));
    public static final LOTRSpawnList DOL_AMROTH_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DOL_AMROTH_SOLDIER, 20, 2, 6),
            new LOTRSpawnEntry(LOTREntities.DOL_AMROTH_ARCHER, 10, 2, 6),
            new LOTRSpawnEntry(LOTREntities.SWAN_KNIGHT, 5, 2, 4));
    public static final LOTRSpawnList LOSSARNACH_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.LOSSARNACH_AXEMAN, 20, 2, 6));
    public static final LOTRSpawnList LEBENNIN_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.LEBENNIN_LEVYMAN, 40, 2, 4),
            new LOTRSpawnEntry(LOTREntities.GONDOR_SOLDIER, 10, 2, 8),
            new LOTRSpawnEntry(LOTREntities.GONDOR_ARCHER, 5, 2, 8));
    public static final LOTRSpawnList PELARGIR_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.LEBENNIN_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.PELARGIR_MARINE, 15, 2, 8));
    public static final LOTRSpawnList LAMEDON_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 10, 2, 4),
            new LOTRSpawnEntry(LOTREntities.LAMEDON_SOLDIER, 10, 2, 6),
            new LOTRSpawnEntry(LOTREntities.LAMEDON_ARCHER, 5, 2, 6));
    public static final LOTRSpawnList LAMEDON_HILLMEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.LAMEDON_HILLMAN, 20, 4, 6));
    public static final LOTRSpawnList BLACKROOT_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.BLACKROOT_SOLDIER, 5, 2, 8),
            new LOTRSpawnEntry(LOTREntities.BLACKROOT_ARCHER, 5, 15, 8));
    public static final LOTRSpawnList PINNATH_GELIN_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.PINNATH_GELIN_SOLDIER, 15, 2, 6));
    public static final LOTRSpawnList RANGERS_ITHILIEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.RANGER_ITHILIEN, 10, 1, 4));
    public static final LOTRSpawnList MORDOR_ORCS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MORDOR_ORC, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.MORDOR_ORC_ARCHER, 10, 4, 6));
    public static final LOTRSpawnList MORDOR_BOMBARDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MORDOR_ORC_BOMBARDIER, 10, 1, 2));
    public static final LOTRSpawnList BLACK_URUKS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.BLACK_URUK, 20, 4, 6),
            new LOTRSpawnEntry(LOTREntities.BLACK_URUK_ARCHER, 10, 4, 6));
    public static final LOTRSpawnList MORDOR_WARGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MORDOR_WARG, 10, 4, 4));
    public static final LOTRSpawnList MORDOR_SPIDERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MORDOR_SPIDER, 10, 4, 4));
    public static final LOTRSpawnList OLOG_HAI = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.OLOG_HAI, 10, 1, 3));
    public static final LOTRSpawnList WICKED_DWARVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.WICKED_DWARF, 10, 1, 3));
    public static final LOTRSpawnList DORWINION_MEN = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DORWINION_MAN, 10, 4, 6));
    public static final LOTRSpawnList DORWINION_GUARDS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DORWINION_GUARD, 20, 1, 3),
            new LOTRSpawnEntry(LOTREntities.DORWINION_CROSSBOWER, 10, 1, 3));
    public static final LOTRSpawnList DORWINION_ELVES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DORWINION_ELF, 10, 4, 6));
    public static final LOTRSpawnList DORWINION_ELF_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DORWINION_ELF_WARRIOR, 20, 1, 3),
            new LOTRSpawnEntry(LOTREntities.DORWINION_ELF_ARCHER, 10, 1, 3));
    public static final LOTRSpawnList DORWINION_VINEYARDS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.DORWINION_VINEHAND, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.DORWINION_VINEKEEPER, 2, 1, 1));
    public static final LOTRSpawnList EASTERLINGS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.EASTERLING, 20, 2, 4));
    public static final LOTRSpawnList EASTERLING_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.EASTERLING_LEVYMAN, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.EASTERLING_WARRIOR, 10, 3, 6),
            new LOTRSpawnEntry(LOTREntities.EASTERLING_ARCHER, 5, 3, 6),
            new LOTRSpawnEntry(LOTREntities.EASTERLING_FIRE_THROWER, 2, 1, 3));
    public static final LOTRSpawnList EASTERLING_GOLD_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.EASTERLING_GOLD_WARRIOR, 10, 2, 4));
    public static final LOTRSpawnList HARNEDHRIM = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HARNEDHRIM, 10, 2, 4));
    public static final LOTRSpawnList HARNEDOR_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HARNEDOR_WARRIOR, 20, 3, 6),
            new LOTRSpawnEntry(LOTREntities.HARNEDOR_ARCHER, 10, 3, 6));
    public static final LOTRSpawnList COAST_SOUTHRONS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.NEAR_HARADRIM, 10, 2, 4));
    public static final LOTRSpawnList SOUTHRON_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.NEAR_HARADRIM_WARRIOR, 20, 3, 6),
            new LOTRSpawnEntry(LOTREntities.NEAR_HARADRIM_ARCHER, 10, 3, 6),
            new LOTRSpawnEntry(LOTREntities.SOUTHRON_CHAMPION, 4, 1, 2));
    public static final LOTRSpawnList UMBARIANS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.UMBARIAN, 10, 2, 4));
    public static final LOTRSpawnList UMBAR_SOLDIERS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.UMBAR_WARRIOR, 10, 3, 6),
            new LOTRSpawnEntry(LOTREntities.UMBAR_ARCHER, 5, 3, 6));
    public static final LOTRSpawnList CORSAIRS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.CORSAIR, 10, 2, 6));
    public static final LOTRSpawnList GONDOR_RENEGADES = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GONDOR_RENEGADE, 10, 1, 4));
    public static final LOTRSpawnList NOMADS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.NOMAD, 10, 4, 6));
    public static final LOTRSpawnList NOMAD_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.NOMAD_WARRIOR, 20, 2, 4),
            new LOTRSpawnEntry(LOTREntities.NOMAD_ARCHER, 10, 2, 4));
    public static final LOTRSpawnList GULF_HARADRIM = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GULF_HARADRIM, 10, 2, 4));
    public static final LOTRSpawnList GULF_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.GULF_WARRIOR, 20, 3, 6),
            new LOTRSpawnEntry(LOTREntities.GULF_ARCHER, 10, 3, 6));
    public static final LOTRSpawnList MORWAITH = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MOREDAIN, 10, 4, 6));
    public static final LOTRSpawnList MORWAITH_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.MOREDAIN_WARRIOR, 10, 4, 6));
    public static final LOTRSpawnList TAURETHRIM = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.TAUREDAIN, 10, 4, 6));
    public static final LOTRSpawnList TAURETHRIM_WARRIORS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.TAUREDAIN_WARRIOR, 10, 4, 6),
            new LOTRSpawnEntry(LOTREntities.TAUREDAIN_BLOWGUNNER, 20, 4, 6));
    public static final LOTRSpawnList HALF_TROLLS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.HALF_TROLL, 60, 2, 4),
            new LOTRSpawnEntry(LOTREntities.HALF_TROLL_WARRIOR, 35, 2, 4),
            new LOTRSpawnEntry(LOTREntities.HALF_TROLL_SCAVENGER, 5, 1, 1));
    public static final LOTRSpawnList UTUMNO_GUESTS = new LOTRSpawnList(
            new LOTRSpawnEntry(LOTREntities.SCRAP_TRADER, 10, 1, 1));

    public final List<LOTRSpawnEntry> spawnList;
    public @Nullable LOTRFaction discoveredFaction;

    public LOTRSpawnList(LOTRSpawnEntry... entries) {
        this.spawnList = List.of(entries);
    }

    public LOTRSpawnList factionOverride(LOTRFaction fac) {
        this.discoveredFaction = fac;
        return this;
    }

    /** getListCommonFaction: the one faction all the list's NPCs belong to, found by making one of each. */
    public LOTRFaction getListCommonFaction(Level level) {
        if (this.discoveredFaction != null) {
            return this.discoveredFaction;
        }
        LOTRFaction commonFaction = null;
        for (LOTRSpawnEntry entry : this.spawnList) {
            Entity entity = entry.type().create(level, EntitySpawnReason.NATURAL);
            if (!(entity instanceof LOTRNPCEntity npc)) {
                throw new IllegalArgumentException("Spawn list must contain only NPCs - invalid " + entry.type());
            }
            LOTRFaction fac = npc.getFaction();
            npc.discard();
            if (commonFaction == null) {
                commonFaction = fac;
            } else if (commonFaction != fac) {
                throw new IllegalArgumentException("Spawn lists must contain only one faction! Mismatched entity: " + entry.type());
            }
        }
        if (commonFaction == null) {
            throw new IllegalArgumentException("Failed to discover faction for spawn list");
        }
        this.discoveredFaction = commonFaction;
        return commonFaction;
    }

    public LOTRSpawnEntry getRandomSpawnEntry(RandomSource rand) {
        return LOTRWeightedRandom.getRandomItem(rand, this.spawnList);
    }

    public List<LOTRSpawnEntry> getReadOnlyList() {
        return new ArrayList<>(this.spawnList);
    }
}
