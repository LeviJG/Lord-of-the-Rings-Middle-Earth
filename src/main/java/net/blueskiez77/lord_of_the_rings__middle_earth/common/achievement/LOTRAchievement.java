package net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRWickedDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestPickpocket;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRAchievement: the mod's own achievements, by category -- the general ones, and one category for
 * each land of Middle-earth and for Utumno -- each with its icon, its name and description, perhaps a
 * title it unlocks, and perhaps a requirement to be an enemy, or a friend, of some faction to earn it
 * (one who no longer qualifies keeps it, but it no longer counts). Saved by category and number, as
 * the original saved them. Earning one is told to every player in chat.
 *
 * <p>Listed but not yet earnable, until what they are earned by is ported: the biome achievements
 * (one for each land entered), climbMistyMountains, growBaobab, rideGiraffeShire and
 * rideBarrelMirkwood (with the biomes, D10); fishRing (fishing, D3/D10);
 * factionConquest (conquest, D14); and Utumno's and its monsters' (D15). attackRabbit,
 * useSpearFromFar, hitByOrcSpear and useDunlendingTrident are earned by new means
 * (LOTRAchievementEvents), the port keeping vanilla's rabbit, spear and trident.
 */
public class LOTRAchievement {

    /** The categories, each in its land's colour (the biome's map colour, or the faction's). */
    public enum Category {
        GENERAL(15916746), SHIRE(LOTRFaction.HOBBIT), BLUE_MOUNTAINS(LOTRFaction.BLUE_MOUNTAINS), LINDON(7646533),
        ERIADOR(7054916), BREE_LAND(6861625), ANGMAR(5523247), EREGION(6656072), ENEDWAITH(8038479),
        DUNLAND(LOTRFaction.DUNLAND), MISTY_MOUNTAINS(15263713), FORODWAITH(14211282), RHOVANION(9612368),
        MIRKWOOD(4089126), DALE(LOTRFaction.DALE), IRON_HILLS(LOTRFaction.DURINS_FOLK), LOTHLORIEN(LOTRFaction.LOTHLORIEN),
        FANGORN(LOTRFaction.FANGORN), ROHAN(LOTRFaction.ROHAN), GONDOR(LOTRFaction.GONDOR), NINDALF(7111750),
        MORDOR(LOTRFaction.MORDOR), DORWINION(LOTRFaction.DORWINION), RHUN(10465880), OROCARNI(9662796),
        NEAR_HARAD(14205815), FAR_HARAD_SAVANNAH(9740353), FAR_HARAD_JUNGLE(4944931), PERDOROGWAITH(8879706),
        OCEAN(153997), UTUMNO(LOTRDimension.UTUMNO, LOTRFaction.UTUMNO.getFactionColor());

        public final int color;
        public final LOTRDimension dimension;
        public final List<LOTRAchievement> list = new ArrayList<>();
        private int nextRankAchID = 1000;

        Category(int color) {
            this(LOTRDimension.MIDDLE_EARTH, color);
        }

        Category(LOTRFaction faction) {
            this(faction.getFactionColor());
        }

        Category(LOTRDimension dimension, int color) {
            this.color = color;
            this.dimension = dimension;
        }

        public String codeName() {
            return name();
        }

        public Component getDisplayName() {
            return Component.translatable("lotr.achievement.category." + codeName());
        }

        public int getNextRankAchID() {
            return ++this.nextRankAchID;
        }

        public static @Nullable Category forName(String name) {
            for (Category category : values()) {
                if (category.name().equals(name)) {
                    return category;
                }
            }
            return null;
        }
    }

    /** LOTRFaction.achieveCategory: where each faction's rank achievements are listed. */
    private static final Map<LOTRFaction, Category> FACTION_CATEGORIES = new EnumMap<>(LOTRFaction.class);

    static {
        FACTION_CATEGORIES.put(LOTRFaction.HOBBIT, Category.SHIRE);
        FACTION_CATEGORIES.put(LOTRFaction.BREE, Category.BREE_LAND);
        FACTION_CATEGORIES.put(LOTRFaction.RANGER_NORTH, Category.ERIADOR);
        FACTION_CATEGORIES.put(LOTRFaction.BLUE_MOUNTAINS, Category.BLUE_MOUNTAINS);
        FACTION_CATEGORIES.put(LOTRFaction.HIGH_ELF, Category.LINDON);
        FACTION_CATEGORIES.put(LOTRFaction.GUNDABAD, Category.ERIADOR);
        FACTION_CATEGORIES.put(LOTRFaction.ANGMAR, Category.ANGMAR);
        FACTION_CATEGORIES.put(LOTRFaction.WOOD_ELF, Category.MIRKWOOD);
        FACTION_CATEGORIES.put(LOTRFaction.DOL_GULDUR, Category.MIRKWOOD);
        FACTION_CATEGORIES.put(LOTRFaction.DALE, Category.DALE);
        FACTION_CATEGORIES.put(LOTRFaction.DURINS_FOLK, Category.IRON_HILLS);
        FACTION_CATEGORIES.put(LOTRFaction.LOTHLORIEN, Category.LOTHLORIEN);
        FACTION_CATEGORIES.put(LOTRFaction.DUNLAND, Category.DUNLAND);
        FACTION_CATEGORIES.put(LOTRFaction.ISENGARD, Category.ROHAN);
        FACTION_CATEGORIES.put(LOTRFaction.FANGORN, Category.FANGORN);
        FACTION_CATEGORIES.put(LOTRFaction.ROHAN, Category.ROHAN);
        FACTION_CATEGORIES.put(LOTRFaction.GONDOR, Category.GONDOR);
        FACTION_CATEGORIES.put(LOTRFaction.MORDOR, Category.MORDOR);
        FACTION_CATEGORIES.put(LOTRFaction.DORWINION, Category.DORWINION);
        FACTION_CATEGORIES.put(LOTRFaction.RHUDEL, Category.RHUN);
        FACTION_CATEGORIES.put(LOTRFaction.NEAR_HARAD, Category.NEAR_HARAD);
        FACTION_CATEGORIES.put(LOTRFaction.MORWAITH, Category.FAR_HARAD_SAVANNAH);
        FACTION_CATEGORIES.put(LOTRFaction.TAURETHRIM, Category.FAR_HARAD_JUNGLE);
        FACTION_CATEGORIES.put(LOTRFaction.HALF_TROLL, Category.PERDOROGWAITH);
    }

    public static @Nullable Category categoryForFaction(LOTRFaction faction) {
        return FACTION_CATEGORIES.get(faction);
    }

    public static LOTRAchievement ENTER_MIDDLE_EARTH;
    public static LOTRAchievement DO_GREY_QUEST;
    public static LOTRAchievement KILL_ORC;
    public static LOTRAchievement MINE_MITHRIL;
    public static LOTRAchievement RIDE_WARG;
    public static LOTRAchievement KILL_WARG;
    public static LOTRAchievement USE_SPEAR_FROM_FAR;
    public static LOTRAchievement WEAR_FULL_MITHRIL;
    public static LOTRAchievement GAIN_HIGH_ALCOHOL_TOLERANCE;
    public static LOTRAchievement CRAFT_SADDLE;
    public static LOTRAchievement CRAFT_BRONZE;
    public static LOTRAchievement DRINK_ORC_DRAUGHT;
    public static LOTRAchievement GET_POUCH;
    public static LOTRAchievement KILL_USING_ONLY_PLATES;
    public static LOTRAchievement WEAR_FULL_FUR;
    public static LOTRAchievement BREW_DRINK_IN_BARREL;
    public static LOTRAchievement FIND_ATHELAS;
    public static LOTRAchievement DRINK_ATHELAS_BREW;
    public static LOTRAchievement KILL_LARGE_MOB_WITH_SLINGSHOT;
    public static LOTRAchievement EAT_MAGGOTY_BREAD;
    public static LOTRAchievement KILL_WHILE_DRUNK;
    public static LOTRAchievement COLLECT_CRAFTING_TABLES;
    public static LOTRAchievement HIT_BY_ORC_SPEAR;
    public static LOTRAchievement KILL_BOMBARDIER;
    public static LOTRAchievement EARN_MANY_COINS;
    public static LOTRAchievement CRAFT_APPLE_CRUMBLE;
    public static LOTRAchievement KILL_BUTTERFLY;
    public static LOTRAchievement FISH_RING;
    public static LOTRAchievement USE_CROSSBOW;
    public static LOTRAchievement COLLECT_CROSSBOW_BOLTS;
    public static LOTRAchievement TRAVEL10;
    public static LOTRAchievement TRAVEL20;
    public static LOTRAchievement TRAVEL30;
    public static LOTRAchievement ATTACK_RABBIT;
    public static LOTRAchievement CRAFT_RABBIT_STEW;
    public static LOTRAchievement DRINK_SKULL;
    public static LOTRAchievement TRAVEL40;
    public static LOTRAchievement TRAVEL50;
    public static LOTRAchievement KILL_THIEVING_BANDIT;
    public static LOTRAchievement HUNDREDS;
    public static LOTRAchievement ALLOY_BRONZE;
    public static LOTRAchievement BANNER_PROTECT;
    public static LOTRAchievement CATCH_BUTTERFLY;
    public static LOTRAchievement UNSMELT;
    public static LOTRAchievement TRADE_SCRAP_TRADER;
    public static LOTRAchievement COOK_DEER;
    public static LOTRAchievement EAT_MAN_FLESH;
    public static LOTRAchievement CRAFT_SALTED_FLESH;
    public static LOTRAchievement ENCHANT_BANE_ELF;
    public static LOTRAchievement ENCHANT_BANE_ORC;
    public static LOTRAchievement ENCHANT_BANE_DWARF;
    public static LOTRAchievement ENCHANT_BANE_WARG;
    public static LOTRAchievement ENCHANT_BANE_TROLL;
    public static LOTRAchievement ENCHANT_BANE_SPIDER;
    public static LOTRAchievement ENCHANT_BANE_WIGHT;
    public static LOTRAchievement GET_DRUNK;
    public static LOTRAchievement REFORGE;
    public static LOTRAchievement DO_MINIQUEST_HUNTER;
    public static LOTRAchievement DO_MINIQUEST_HUNTER5;
    public static LOTRAchievement KILL_HUNTING_PLAYER;
    public static LOTRAchievement PLEDGE_SERVICE;
    public static LOTRAchievement FACTION_CONQUEST;
    public static LOTRAchievement DEFEAT_INVASION;
    public static LOTRAchievement PICKPOCKET;
    public static LOTRAchievement COMBINE_SMITH_SCROLLS;
    public static LOTRAchievement ENGRAVE_OWNERSHIP;
    public static LOTRAchievement KILL_HOBBIT;
    public static LOTRAchievement SELL_PIPEWEED_LEAF;
    public static LOTRAchievement MARRY_HOBBIT;
    public static LOTRAchievement FIND_FOUR_LEAF_CLOVER;
    public static LOTRAchievement USE_MAGIC_PIPE;
    public static LOTRAchievement RIDE_SHIRE_PONY;
    public static LOTRAchievement TRADE_BARTENDER;
    public static LOTRAchievement SPEAK_TO_DRUNKARD;
    public static LOTRAchievement TRADE_HOBBIT_SHIRRIFF;
    public static LOTRAchievement KILL_DARK_HUORN;
    public static LOTRAchievement ENTER_OLD_FOREST;
    public static LOTRAchievement BUY_ORCHARDER_FOOD;
    public static LOTRAchievement RIDE_GIRAFFE_SHIRE;
    public static LOTRAchievement BUY_POTATO_HOBBIT_FARMER;
    public static LOTRAchievement DO_MINIQUEST_HOBBIT;
    public static LOTRAchievement ENTER_WHITE_DOWNS;
    public static LOTRAchievement USE_HOBBIT_TABLE;
    public static LOTRAchievement HIRE_HOBBIT_FARMER;
    public static LOTRAchievement ENTER_BLUE_MOUNTAINS;
    public static LOTRAchievement SMELT_BLUE_DWARF_STEEL;
    public static LOTRAchievement KILL_BLUE_DWARF;
    public static LOTRAchievement WEAR_FULL_BLUE_DWARVEN;
    public static LOTRAchievement USE_BLUE_DWARVEN_TABLE;
    public static LOTRAchievement TRADE_BLUE_DWARF_MINER;
    public static LOTRAchievement TRADE_BLUE_DWARF_COMMANDER;
    public static LOTRAchievement TRADE_BLUE_DWARF_MERCHANT;
    public static LOTRAchievement MARRY_BLUE_DWARF;
    public static LOTRAchievement DO_MINIQUEST_BLUE_MOUNTAINS;
    public static LOTRAchievement TRADE_BLUE_DWARF_SMITH;
    public static LOTRAchievement ENTER_LINDON;
    public static LOTRAchievement DO_MINIQUEST_HIGH_ELF;
    public static LOTRAchievement KILL_HIGH_ELF;
    public static LOTRAchievement TRADE_HIGH_ELF_LORD;
    public static LOTRAchievement USE_HIGH_ELVEN_TABLE;
    public static LOTRAchievement WEAR_FULL_HIGH_ELVEN;
    public static LOTRAchievement SMELT_ELF_STEEL;
    public static LOTRAchievement TRADE_HIGH_ELF_SMITH;
    public static LOTRAchievement WEAR_FULL_GONDOLIN;
    public static LOTRAchievement ENTER_TOWER_HILLS;
    public static LOTRAchievement WEAR_FULL_GALVORN;
    public static LOTRAchievement ENTER_BREELAND;
    public static LOTRAchievement ENTER_CHETWOOD;
    public static LOTRAchievement KILL_BREELANDER;
    public static LOTRAchievement DO_MINIQUEST_BREE;
    public static LOTRAchievement TRADE_BREE_CAPTAIN;
    public static LOTRAchievement USE_BREE_TABLE;
    public static LOTRAchievement TRADE_BREE_BLACKSMITH;
    public static LOTRAchievement TRADE_BREE_INNKEEPER;
    public static LOTRAchievement KILL_BREE_HOBBIT;
    public static LOTRAchievement KILL_RUFFIAN_SPY;
    public static LOTRAchievement KILL_RUFFIAN_BRUTE;
    public static LOTRAchievement DO_MINIQUEST_RUFFIAN_SPY;
    public static LOTRAchievement DO_MINIQUEST_RUFFIAN_BRUTE;
    public static LOTRAchievement TRADE_BREE_MARKET_TRADER;
    public static LOTRAchievement BUY_APPLE_BREE_FARMER;
    public static LOTRAchievement HIRE_BREE_FARMER;
    public static LOTRAchievement KILL_RANGER_NORTH;
    public static LOTRAchievement WEAR_FULL_RANGER;
    public static LOTRAchievement KILL_TROLL;
    public static LOTRAchievement GET_TROLL_STATUE;
    public static LOTRAchievement MAKE_TROLL_SNEEZE;
    public static LOTRAchievement KILL_MOUNTAIN_TROLL;
    public static LOTRAchievement KILL_TROLL_FLEEING_SUN;
    public static LOTRAchievement KILL_MOUNTAIN_TROLL_CHIEFTAIN;
    public static LOTRAchievement SHOOT_DOWN_MIDGES;
    public static LOTRAchievement ENTER_TROLLSHAWS;
    public static LOTRAchievement ENTER_MIDGEWATER;
    public static LOTRAchievement ENTER_LONE_LANDS;
    public static LOTRAchievement ENTER_ETTENMOORS;
    public static LOTRAchievement ENTER_ERIADOR;
    public static LOTRAchievement ENTER_COLDFELLS;
    public static LOTRAchievement ENTER_SWANFLEET;
    public static LOTRAchievement ENTER_MINHIRIATH;
    public static LOTRAchievement TRADE_GUNDABAD_CAPTAIN;
    public static LOTRAchievement TRADE_RANGER_NORTH_CAPTAIN;
    public static LOTRAchievement ENTER_BARROW_DOWNS;
    public static LOTRAchievement USE_RANGER_TABLE;
    public static LOTRAchievement USE_GUNDABAD_TABLE;
    public static LOTRAchievement DO_MINIQUEST_RANGER;
    public static LOTRAchievement DO_MINIQUEST_GUNDABAD;
    public static LOTRAchievement KILL_BARROW_WIGHT;
    public static LOTRAchievement KILL_GUNDABAD_ORC;
    public static LOTRAchievement KILL_GUNDABAD_URUK;
    public static LOTRAchievement WEAR_FULL_GUNDABAD_URUK;
    public static LOTRAchievement KILL_DUNEDAIN;
    public static LOTRAchievement ENTER_ANGLE;
    public static LOTRAchievement WEAR_FULL_ARNOR;
    public static LOTRAchievement TRADE_DUNEDAIN_BLACKSMITH;
    public static LOTRAchievement ENTER_RIVENDELL;
    public static LOTRAchievement USE_RIVENDELL_TABLE;
    public static LOTRAchievement WEAR_FULL_RIVENDELL;
    public static LOTRAchievement TRADE_RIVENDELL_SMITH;
    public static LOTRAchievement TRADE_RIVENDELL_LORD;
    public static LOTRAchievement TRADE_RIVENDELL_TRADER;
    public static LOTRAchievement DO_MINIQUEST_RIVENDELL;
    public static LOTRAchievement KILL_RIVENDELL_ELF;
    public static LOTRAchievement TRADE_GUNDABAD_TRADER;
    public static LOTRAchievement TRADE_ANGMAR_CAPTAIN;
    public static LOTRAchievement KILL_ANGMAR_ORC;
    public static LOTRAchievement ENTER_ANGMAR;
    public static LOTRAchievement USE_ANGMAR_TABLE;
    public static LOTRAchievement WEAR_FULL_ANGMAR;
    public static LOTRAchievement DO_MINIQUEST_ANGMAR;
    public static LOTRAchievement TRADE_ANGMAR_TRADER;
    public static LOTRAchievement KILL_ANGMAR_HILLMAN;
    public static LOTRAchievement TRADE_ANGMAR_HILLMAN_CHIEFTAIN;
    public static LOTRAchievement KILL_SNOW_TROLL;
    public static LOTRAchievement ENTER_EREGION;
    public static LOTRAchievement ENTER_ENEDWAITH;
    public static LOTRAchievement ENTER_NAN_CURUNIR;
    public static LOTRAchievement ENTER_PUKEL;
    public static LOTRAchievement KILL_DUNLENDING;
    public static LOTRAchievement WEAR_FULL_DUNLENDING;
    public static LOTRAchievement USE_DUNLENDING_TABLE;
    public static LOTRAchievement TRADE_DUNLENDING_WARLORD;
    public static LOTRAchievement USE_DUNLENDING_TRIDENT;
    public static LOTRAchievement TRADE_DUNLENDING_BARTENDER;
    public static LOTRAchievement ENTER_DUNLAND;
    public static LOTRAchievement DO_MINIQUEST_DUNLAND;
    public static LOTRAchievement CLIMB_MISTY_MOUNTAINS;
    public static LOTRAchievement ENTER_MISTY_MOUNTAINS;
    public static LOTRAchievement TAME_GOLLUM;
    public static LOTRAchievement ENTER_FORODWAITH;
    public static LOTRAchievement ENTER_VALES_OF_ANDUIN;
    public static LOTRAchievement ENTER_GREY_MOUNTAINS;
    public static LOTRAchievement ENTER_GLADDEN_FIELDS;
    public static LOTRAchievement ENTER_EMYN_MUIL;
    public static LOTRAchievement ENTER_BROWN_LANDS;
    public static LOTRAchievement ENTER_WILDERLAND;
    public static LOTRAchievement ENTER_DAGORLAD;
    public static LOTRAchievement ENTER_CELEBRANT;
    public static LOTRAchievement ENTER_LONG_MARSHES;
    public static LOTRAchievement ENTER_EAST_BIGHT;
    public static LOTRAchievement KILL_MIRKWOOD_SPIDER;
    public static LOTRAchievement KILL_WOOD_ELF;
    public static LOTRAchievement USE_WOOD_ELVEN_TABLE;
    public static LOTRAchievement WEAR_FULL_WOOD_ELVEN_SCOUT;
    public static LOTRAchievement TRADE_WOOD_ELF_CAPTAIN;
    public static LOTRAchievement RIDE_BARREL_MIRKWOOD;
    public static LOTRAchievement ENTER_MIRKWOOD;
    public static LOTRAchievement ENTER_WOODLAND_REALM;
    public static LOTRAchievement WEAR_FULL_WOOD_ELVEN;
    public static LOTRAchievement ENTER_DOL_GULDUR;
    public static LOTRAchievement KILL_DOL_GULDUR_ORC;
    public static LOTRAchievement USE_DOL_GULDUR_TABLE;
    public static LOTRAchievement TRADE_DOL_GULDUR_CAPTAIN;
    public static LOTRAchievement KILL_MIRK_TROLL;
    public static LOTRAchievement WEAR_FULL_DOL_GULDUR;
    public static LOTRAchievement DO_MINIQUEST_WOOD_ELF;
    public static LOTRAchievement DO_MINIQUEST_DOL_GULDUR;
    public static LOTRAchievement TRADE_DOL_GULDUR_TRADER;
    public static LOTRAchievement TRADE_WOOD_ELF_SMITH;
    public static LOTRAchievement ENTER_DALE;
    public static LOTRAchievement DO_MINIQUEST_DALE;
    public static LOTRAchievement USE_DALE_TABLE;
    public static LOTRAchievement WEAR_FULL_DALE;
    public static LOTRAchievement KILL_DALISH;
    public static LOTRAchievement TRADE_DALE_CAPTAIN;
    public static LOTRAchievement TRADE_DALE_BLACKSMITH;
    public static LOTRAchievement TRADE_DALE_BAKER;
    public static LOTRAchievement TRADE_DALE_MERCHANT;
    public static LOTRAchievement ENTER_EREBOR;
    public static LOTRAchievement OPEN_DALE_CRACKER;
    public static LOTRAchievement KILL_DWARF;
    public static LOTRAchievement WEAR_FULL_DWARVEN;
    public static LOTRAchievement USE_DWARVEN_THROWING_AXE;
    public static LOTRAchievement USE_DWARVEN_TABLE;
    public static LOTRAchievement TRADE_DWARF_MINER;
    public static LOTRAchievement TRADE_DWARF_COMMANDER;
    public static LOTRAchievement MINE_GLOWSTONE;
    public static LOTRAchievement SMELT_DWARF_STEEL;
    public static LOTRAchievement DRINK_DWARVEN_TONIC;
    public static LOTRAchievement CRAFT_MITHRIL_DWARVEN_BRICK;
    public static LOTRAchievement TALK_DWARF_WOMAN;
    public static LOTRAchievement ENTER_IRON_HILLS;
    public static LOTRAchievement USE_DWARVEN_DOOR;
    public static LOTRAchievement MARRY_DWARF;
    public static LOTRAchievement DO_MINIQUEST_DWARF;
    public static LOTRAchievement TRADE_IRON_HILLS_MERCHANT;
    public static LOTRAchievement TRADE_DWARF_SMITH;
    public static LOTRAchievement KILL_ELF;
    public static LOTRAchievement USE_ELVEN_PORTAL;
    public static LOTRAchievement WEAR_FULL_ELVEN;
    public static LOTRAchievement USE_ELVEN_TABLE;
    public static LOTRAchievement TRADE_ELF_LORD;
    public static LOTRAchievement MINE_QUENDITE;
    public static LOTRAchievement TAKE_MALLORN_WOOD;
    public static LOTRAchievement ENTER_LOTHLORIEN;
    public static LOTRAchievement TRADE_ELVEN_TRADER;
    public static LOTRAchievement DO_MINIQUEST_GALADHRIM;
    public static LOTRAchievement TRADE_GALADHRIM_SMITH;
    public static LOTRAchievement WEAR_FULL_HITHLAIN;
    public static LOTRAchievement KILL_ENT;
    public static LOTRAchievement DRINK_ENT_DRAUGHT;
    public static LOTRAchievement KILL_HUORN;
    public static LOTRAchievement TALK_ENT;
    public static LOTRAchievement ENTER_FANGORN;
    public static LOTRAchievement SUMMON_HUORN;
    public static LOTRAchievement KILL_MALLORN_ENT;
    public static LOTRAchievement RAID_URUK_CAMP;
    public static LOTRAchievement USE_URUK_TABLE;
    public static LOTRAchievement TRADE_URUK_TRADER;
    public static LOTRAchievement TRADE_URUK_CAPTAIN;
    public static LOTRAchievement USE_ROHIRRIC_TABLE;
    public static LOTRAchievement SMELT_URUK_STEEL;
    public static LOTRAchievement WEAR_FULL_URUK;
    public static LOTRAchievement HIRE_WARG_BOMBARDIER;
    public static LOTRAchievement KILL_ROHIRRIM;
    public static LOTRAchievement TRADE_ROHIRRIM_MARSHAL;
    public static LOTRAchievement WEAR_FULL_ROHIRRIC;
    public static LOTRAchievement TRADE_ROHAN_BLACKSMITH;
    public static LOTRAchievement BUY_ROHAN_MEAD;
    public static LOTRAchievement ENTER_ROHAN;
    public static LOTRAchievement ENTER_ROHAN_URUK_HIGHLANDS;
    public static LOTRAchievement DO_MINIQUEST_ROHAN;
    public static LOTRAchievement DO_MINIQUEST_ISENGARD;
    public static LOTRAchievement KILL_URUK_HAI;
    public static LOTRAchievement ENTER_ENTWASH_MOUTH;
    public static LOTRAchievement WEAR_FULL_ROHIRRIC_MARSHAL;
    public static LOTRAchievement KILL_ISENGARD_SNAGA;
    public static LOTRAchievement DO_MINIQUEST_ROHAN_SHIELDMAIDEN;
    public static LOTRAchievement TRADE_ROHAN_FARMER;
    public static LOTRAchievement HIRE_ROHAN_FARMER;
    public static LOTRAchievement TRADE_ROHAN_MARKET_TRADER;
    public static LOTRAchievement TRADE_ROHAN_STABLEMASTER;
    public static LOTRAchievement ENTER_ADORNLAND;
    public static LOTRAchievement KILL_GONDORIAN;
    public static LOTRAchievement LIGHT_GONDOR_BEACON;
    public static LOTRAchievement USE_GONDORIAN_TABLE;
    public static LOTRAchievement TRADE_GONDOR_BLACKSMITH;
    public static LOTRAchievement TRADE_GONDORIAN_CAPTAIN;
    public static LOTRAchievement WEAR_FULL_GONDORIAN;
    public static LOTRAchievement KILL_RANGER_ITHILIEN;
    public static LOTRAchievement ENTER_GONDOR;
    public static LOTRAchievement ENTER_ITHILIEN;
    public static LOTRAchievement ENTER_WHITE_MOUNTAINS;
    public static LOTRAchievement ENTER_TOLFALAS;
    public static LOTRAchievement ENTER_LEBENNIN;
    public static LOTRAchievement DO_MINIQUEST_GONDOR;
    public static LOTRAchievement TRADE_RANGER_ITHILIEN_CAPTAIN;
    public static LOTRAchievement USE_DOL_AMROTH_TABLE;
    public static LOTRAchievement WEAR_FULL_DOL_AMROTH;
    public static LOTRAchievement KILL_SWAN_KNIGHT;
    public static LOTRAchievement ENTER_DOR_EN_ERNIL;
    public static LOTRAchievement TRADE_DOL_AMROTH_CAPTAIN;
    public static LOTRAchievement ENTER_ANDUIN_MOUTH;
    public static LOTRAchievement ENTER_PELENNOR;
    public static LOTRAchievement WEAR_FULL_RANGER_ITHILIEN;
    public static LOTRAchievement ENTER_LOSSARNACH;
    public static LOTRAchievement ENTER_IMLOTH_MELUI;
    public static LOTRAchievement WEAR_FULL_LOSSARNACH;
    public static LOTRAchievement WEAR_FULL_PELARGIR;
    public static LOTRAchievement WEAR_FULL_PINNATH_GELIN;
    public static LOTRAchievement WEAR_FULL_BLACKROOT;
    public static LOTRAchievement HIRE_GONDOR_FARMER;
    public static LOTRAchievement BUY_PIPEWEED_GONDOR_FARMER;
    public static LOTRAchievement TRADE_GONDOR_BARTENDER;
    public static LOTRAchievement TRADE_GONDOR_MARKET_TRADER;
    public static LOTRAchievement TRADE_LOSSARNACH_CAPTAIN;
    public static LOTRAchievement TRADE_PELARGIR_CAPTAIN;
    public static LOTRAchievement TRADE_PINNATH_GELIN_CAPTAIN;
    public static LOTRAchievement TRADE_BLACKROOT_CAPTAIN;
    public static LOTRAchievement TRADE_LEBENNIN_CAPTAIN;
    public static LOTRAchievement ENTER_PELARGIR;
    public static LOTRAchievement WEAR_FULL_LAMEDON;
    public static LOTRAchievement TRADE_LAMEDON_CAPTAIN;
    public static LOTRAchievement ENTER_LAMEDON;
    public static LOTRAchievement ENTER_BLACKROOT_VALE;
    public static LOTRAchievement ENTER_PINNATH_GELIN;
    public static LOTRAchievement ENTER_ANDRAST;
    public static LOTRAchievement DO_MINIQUEST_GONDOR_KILL_RENEGADE;
    public static LOTRAchievement MINE_REMAINS;
    public static LOTRAchievement CRAFT_ANCIENT_ITEM;
    public static LOTRAchievement ENTER_DEAD_MARSHES;
    public static LOTRAchievement ENTER_NINDALF;
    public static LOTRAchievement KILL_MARSH_WRAITH;
    public static LOTRAchievement KILL_OLOG_HAI;
    public static LOTRAchievement USE_MORGUL_TABLE;
    public static LOTRAchievement SMELT_ORC_STEEL;
    public static LOTRAchievement WEAR_FULL_ORC;
    public static LOTRAchievement TRADE_ORC_TRADER;
    public static LOTRAchievement TRADE_ORC_CAPTAIN;
    public static LOTRAchievement MINE_NAURITE;
    public static LOTRAchievement EAT_MORGUL_SHROOM;
    public static LOTRAchievement CRAFT_ORC_BOMB;
    public static LOTRAchievement HIRE_OLOG_HAI;
    public static LOTRAchievement MINE_GULDURIL;
    public static LOTRAchievement USE_MORGUL_PORTAL;
    public static LOTRAchievement WEAR_FULL_MORGUL;
    public static LOTRAchievement ENTER_MORDOR;
    public static LOTRAchievement ENTER_NURN;
    public static LOTRAchievement ENTER_NAN_UNGOL;
    public static LOTRAchievement KILL_MORDOR_SPIDER;
    public static LOTRAchievement TRADE_ORC_SPIDER_KEEPER;
    public static LOTRAchievement KILL_MORDOR_ORC;
    public static LOTRAchievement DO_MINIQUEST_MORDOR;
    public static LOTRAchievement SMELT_BLACK_URUK_STEEL;
    public static LOTRAchievement WEAR_FULL_BLACK_URUK;
    public static LOTRAchievement KILL_BLACK_URUK;
    public static LOTRAchievement HIRE_NURN_SLAVE;
    public static LOTRAchievement ENTER_MORGUL_VALE;
    public static LOTRAchievement TRADE_BLACK_URUK_CAPTAIN;
    public static LOTRAchievement KILL_WICKED_DWARF;
    public static LOTRAchievement TRADE_WICKED_DWARF;
    public static LOTRAchievement ENTER_DORWINION;
    public static LOTRAchievement DO_MINIQUEST_DORWINION;
    public static LOTRAchievement USE_DORWINION_TABLE;
    public static LOTRAchievement WEAR_FULL_DORWINION;
    public static LOTRAchievement WEAR_FULL_DORWINION_ELF;
    public static LOTRAchievement KILL_DORWINION;
    public static LOTRAchievement TRADE_DORWINION_CAPTAIN;
    public static LOTRAchievement KILL_DORWINION_ELF;
    public static LOTRAchievement TRADE_DORWINION_ELF_CAPTAIN;
    public static LOTRAchievement DRINK_WINE;
    public static LOTRAchievement HARVEST_GRAPES;
    public static LOTRAchievement BUY_WINE_VINTNER;
    public static LOTRAchievement HIRE_DORWINION_VINEKEEPER;
    public static LOTRAchievement STEAL_DORWINION_GRAPES;
    public static LOTRAchievement TRADE_DORWINION_MERCHANT;
    public static LOTRAchievement ENTER_DORWINION_HILLS;
    public static LOTRAchievement ENTER_RHUN;
    public static LOTRAchievement USE_RHUN_TABLE;
    public static LOTRAchievement KILL_EASTERLING;
    public static LOTRAchievement DO_MINIQUEST_RHUN;
    public static LOTRAchievement WEAR_FULL_RHUN;
    public static LOTRAchievement TRADE_RHUN_BLACKSMITH;
    public static LOTRAchievement TRADE_RHUN_CAPTAIN;
    public static LOTRAchievement HIT_BIRD_FIRE_POT;
    public static LOTRAchievement GET_KINE_ARAW_HORN;
    public static LOTRAchievement WEAR_FULL_RHUN_GOLD;
    public static LOTRAchievement ENTER_LAST_DESERT;
    public static LOTRAchievement ENTER_MOUNTAINS_WIND;
    public static LOTRAchievement TRADE_RHUN_MARKET_TRADER;
    public static LOTRAchievement TRADE_RHUN_BARTENDER;
    public static LOTRAchievement HIRE_RHUN_FARMER;
    public static LOTRAchievement ENTER_RHUN_LAND;
    public static LOTRAchievement ENTER_RHUN_REDWOOD;
    public static LOTRAchievement ENTER_RHUN_ISLAND;
    public static LOTRAchievement ENTER_RED_MOUNTAINS;
    public static LOTRAchievement ENTER_HARONDOR;
    public static LOTRAchievement ENTER_NEAR_HARAD;
    public static LOTRAchievement KILL_NEAR_HARADRIM;
    public static LOTRAchievement USE_NEAR_HARAD_TABLE;
    public static LOTRAchievement WEAR_FULL_NEAR_HARAD;
    public static LOTRAchievement TRADE_NEAR_HARAD_WARLORD;
    public static LOTRAchievement RIDE_CAMEL;
    public static LOTRAchievement TRADE_BAZAAR_TRADER;
    public static LOTRAchievement TRADE_NEAR_HARAD_MERCHANT;
    public static LOTRAchievement DO_MINIQUEST_NEAR_HARAD;
    public static LOTRAchievement COOK_KEBAB;
    public static LOTRAchievement ENTER_NEAR_HARAD_OASIS;
    public static LOTRAchievement TRADE_NEAR_HARAD_BLACKSMITH;
    public static LOTRAchievement ENTER_HARNEDOR;
    public static LOTRAchievement ENTER_SOUTHRON_COASTS;
    public static LOTRAchievement ENTER_UMBAR;
    public static LOTRAchievement ENTER_LOSTLADEN;
    public static LOTRAchievement ENTER_GULF_HARAD;
    public static LOTRAchievement USE_UMBAR_TABLE;
    public static LOTRAchievement USE_GULF_TABLE;
    public static LOTRAchievement WEAR_FULL_GULF_HARAD;
    public static LOTRAchievement WEAR_FULL_CORSAIR;
    public static LOTRAchievement WEAR_FULL_UMBAR;
    public static LOTRAchievement WEAR_FULL_HARNEDOR;
    public static LOTRAchievement WEAR_FULL_NOMAD;
    public static LOTRAchievement TRADE_HARNEDOR_WARLORD;
    public static LOTRAchievement TRADE_UMBAR_CAPTAIN;
    public static LOTRAchievement TRADE_CORSAIR_CAPTAIN;
    public static LOTRAchievement TRADE_NOMAD_WARLORD;
    public static LOTRAchievement TRADE_GULF_WARLORD;
    public static LOTRAchievement HIRE_HARAD_SLAVE;
    public static LOTRAchievement HIRE_MOREDAIN_MERCENARY;
    public static LOTRAchievement TRADE_HARNEDOR_BLACKSMITH;
    public static LOTRAchievement TRADE_UMBAR_BLACKSMITH;
    public static LOTRAchievement TRADE_GULF_BLACKSMITH;
    public static LOTRAchievement DO_MINIQUEST_GONDOR_RENEGADE;
    public static LOTRAchievement TRADE_NOMAD_MERCHANT;
    public static LOTRAchievement TRADE_HARAD_BARTENDER;
    public static LOTRAchievement TRADE_NOMAD_ARMOURER;
    public static LOTRAchievement HIRE_HARNEDOR_FARMER;
    public static LOTRAchievement TRADE_HARAD_FARMER;
    public static LOTRAchievement WEAR_FULL_BLACK_NUMENOREAN;
    public static LOTRAchievement ENTER_FAR_HARAD_SAVANNAH;
    public static LOTRAchievement PICK_BANANA;
    public static LOTRAchievement GROW_BAOBAB;
    public static LOTRAchievement ENTER_FAR_HARAD_VOLCANO;
    public static LOTRAchievement KILL_MOREDAIN;
    public static LOTRAchievement USE_MOREDAIN_TABLE;
    public static LOTRAchievement WEAR_FULL_MOREDAIN;
    public static LOTRAchievement TRADE_MOREDAIN_CHIEFTAIN;
    public static LOTRAchievement DO_MINIQUEST_MOREDAIN;
    public static LOTRAchievement TRADE_MOREDAIN_VILLAGER;
    public static LOTRAchievement ENTER_CORSAIR_COASTS;
    public static LOTRAchievement ENTER_FAR_HARAD_JUNGLE;
    public static LOTRAchievement DRINK_MANGO_JUICE;
    public static LOTRAchievement DO_MINIQUEST_TAUREDAIN;
    public static LOTRAchievement USE_TAUREDAIN_TABLE;
    public static LOTRAchievement WEAR_FULL_TAUREDAIN;
    public static LOTRAchievement KILL_TAUREDAIN;
    public static LOTRAchievement TRADE_TAUREDAIN_CHIEFTAIN;
    public static LOTRAchievement TRADE_TAUREDAIN_SHAMAN;
    public static LOTRAchievement SMELT_OBSIDIAN_SHARD;
    public static LOTRAchievement TRADE_TAUREDAIN_FARMER;
    public static LOTRAchievement HIRE_TAUREDAIN_FARMER;
    public static LOTRAchievement TRADE_TAUREDAIN_SMITH;
    public static LOTRAchievement WEAR_FULL_TAURETHRIM_GOLD;
    public static LOTRAchievement ENTER_PERTOROGWAITH;
    public static LOTRAchievement KILL_HALF_TROLL;
    public static LOTRAchievement WEAR_FULL_HALF_TROLL;
    public static LOTRAchievement TRADE_HALF_TROLL_WARLORD;
    public static LOTRAchievement USE_HALF_TROLL_TABLE;
    public static LOTRAchievement DO_MINIQUEST_HALF_TROLL;
    public static LOTRAchievement TRADE_HALF_TROLL_SCAVENGER;
    public static LOTRAchievement ENTER_HALF_TROLL_FOREST;
    public static LOTRAchievement ENTER_OCEAN;
    public static LOTRAchievement ENTER_MENELTARMA;
    public static LOTRAchievement ENTER_UTUMNO_ICE;
    public static LOTRAchievement ENTER_UTUMNO_OBSIDIAN;
    public static LOTRAchievement ENTER_UTUMNO_FIRE;
    public static LOTRAchievement WEAR_FULL_UTUMNO;
    public static LOTRAchievement KILL_UTUMNO_ORC;
    public static LOTRAchievement KILL_UTUMNO_WARG;
    public static LOTRAchievement KILL_BALROG;
    public static LOTRAchievement KILL_TORMENTED_ELF;
    public static LOTRAchievement KILL_UTUMNO_TROLL;
    public static LOTRAchievement CRAFT_UTUMNO_KEY;
    public static LOTRAchievement LEAVE_UTUMNO;

    public final Category category;
    public final int ID;
    private final Supplier<ItemStack> icon;
    private @Nullable ItemStack iconStack;
    public final String name;
    public boolean isBiomeAchievement;
    public boolean isSpecial;
    public @Nullable LOTRTitle achievementTitle;
    public final List<LOTRFaction> enemyFactions = new ArrayList<>();
    public final List<LOTRFaction> allyFactions = new ArrayList<>();

    public LOTRAchievement(Category category, int id, Supplier<ItemStack> icon, String name) {
        this.category = category;
        this.ID = id;
        this.icon = icon;
        this.name = name;
        for (LOTRAchievement achievement : category.list) {
            if (achievement.ID == id) {
                throw new IllegalArgumentException("Duplicate ID " + id + " for LOTR achievement category " + category.name());
            }
        }
        category.list.add(this);
    }

    private static Supplier<ItemStack> item(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name);
        return () -> new ItemStack(BuiltInRegistries.ITEM.getValue(id));
    }

    public static void createAchievements() {
        ENTER_MIDDLE_EARTH = new LOTRAchievement(Category.GENERAL, 1, item("red_book"), "enterMiddleEarth").setSpecial();
        DO_GREY_QUEST = new LOTRAchievement(Category.GENERAL, 2, item("gandalf_staff_grey"), "doGreyQuest");
        KILL_ORC = new LOTRAchievement(Category.GENERAL, 14, item("orc_bone"), "killOrc").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        MINE_MITHRIL = new LOTRAchievement(Category.GENERAL, 15, item("mithril_ore"), "mineMithril").createTitle();
        RIDE_WARG = new LOTRAchievement(Category.GENERAL, 16, () -> new ItemStack(Items.SADDLE), "rideWarg").setRequiresAnyAlly(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        KILL_WARG = new LOTRAchievement(Category.GENERAL, 17, item("warg_bone"), "killWarg").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        USE_SPEAR_FROM_FAR = new LOTRAchievement(Category.GENERAL, 18, item("iron_spear"), "useSpearFromFar");
        WEAR_FULL_MITHRIL = new LOTRAchievement(Category.GENERAL, 19, item("mithril_chestplate"), "wearFullMithril");
        GAIN_HIGH_ALCOHOL_TOLERANCE = new LOTRAchievement(Category.GENERAL, 20, item("rum"), "gainHighAlcoholTolerance").createTitle("alcoholic");
        CRAFT_SADDLE = new LOTRAchievement(Category.GENERAL, 21, () -> new ItemStack(Items.SADDLE), "craftSaddle");
        CRAFT_BRONZE = new LOTRAchievement(Category.GENERAL, 22, item("bronze_ingot"), "craftBronze");
        DRINK_ORC_DRAUGHT = new LOTRAchievement(Category.GENERAL, 23, item("orc_draught"), "drinkOrcDraught");
        GET_POUCH = new LOTRAchievement(Category.GENERAL, 24, item("small_pouch"), "getPouch");
        KILL_USING_ONLY_PLATES = new LOTRAchievement(Category.GENERAL, 25, item("fine_plate"), "killUsingOnlyPlates").createTitle();
        WEAR_FULL_FUR = new LOTRAchievement(Category.GENERAL, 26, item("fur_tunic"), "wearFullFur");
        BREW_DRINK_IN_BARREL = new LOTRAchievement(Category.GENERAL, 27, item("barrel"), "brewDrinkInBarrel").createTitle("brewDrink");
        FIND_ATHELAS = new LOTRAchievement(Category.GENERAL, 28, item("athelas"), "findAthelas");
        DRINK_ATHELAS_BREW = new LOTRAchievement(Category.GENERAL, 29, item("athelas_brew"), "drinkAthelasBrew");
        KILL_LARGE_MOB_WITH_SLINGSHOT = new LOTRAchievement(Category.GENERAL, 30, item("sling"), "killLargeMobWithSlingshot");
        EAT_MAGGOTY_BREAD = new LOTRAchievement(Category.GENERAL, 31, item("maggoty_bread"), "eatMaggotyBread");
        KILL_WHILE_DRUNK = new LOTRAchievement(Category.GENERAL, 32, item("ale"), "killWhileDrunk").createTitle();
        COLLECT_CRAFTING_TABLES = new LOTRAchievement(Category.GENERAL, 33, () -> new ItemStack(Items.CRAFTING_TABLE), "collectCraftingTables").createTitle();
        HIT_BY_ORC_SPEAR = new LOTRAchievement(Category.GENERAL, 34, item("mordor_spear"), "hitByOrcSpear").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC));
        KILL_BOMBARDIER = new LOTRAchievement(Category.GENERAL, 35, item("orc_bomb"), "killBombardier").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC));
        EARN_MANY_COINS = new LOTRAchievement(Category.GENERAL, 36, item("silver_coin"), "earnManyCoins").createTitle();
        CRAFT_APPLE_CRUMBLE = new LOTRAchievement(Category.GENERAL, 37, item("apple_crumble"), "craftAppleCrumble");
        KILL_BUTTERFLY = new LOTRAchievement(Category.GENERAL, 38, () -> new ItemStack(Items.IRON_SWORD), "killButterfly").createTitle();
        FISH_RING = new LOTRAchievement(Category.GENERAL, 39, () -> new ItemStack(Items.FISHING_ROD), "fishRing").createTitle("fishRing");
        USE_CROSSBOW = new LOTRAchievement(Category.GENERAL, 40, item("iron_crossbow"), "useCrossbow");
        COLLECT_CROSSBOW_BOLTS = new LOTRAchievement(Category.GENERAL, 41, item("crossbow_bolt"), "collectCrossbowBolts");
        TRAVEL10 = new LOTRAchievement(Category.GENERAL, 42, () -> new ItemStack(Items.LEATHER_BOOTS), "travel10").setSpecial();
        TRAVEL20 = new LOTRAchievement(Category.GENERAL, 43, () -> new ItemStack(Items.COMPASS), "travel20").setSpecial();
        TRAVEL30 = new LOTRAchievement(Category.GENERAL, 44, () -> new ItemStack(Items.FILLED_MAP), "travel30").setSpecial();
        ATTACK_RABBIT = new LOTRAchievement(Category.GENERAL, 45, () -> new ItemStack(Items.WHEAT_SEEDS), "attackRabbit");
        CRAFT_RABBIT_STEW = new LOTRAchievement(Category.GENERAL, 46, item("rabbit_stew"), "craftRabbitStew");
        DRINK_SKULL = new LOTRAchievement(Category.GENERAL, 47, item("skull_cup"), "drinkSkull").createTitle("drinkSkull");
        TRAVEL40 = new LOTRAchievement(Category.GENERAL, 48, () -> new ItemStack(Items.FILLED_MAP), "travel40").setSpecial();
        TRAVEL50 = new LOTRAchievement(Category.GENERAL, 49, () -> new ItemStack(Items.FILLED_MAP), "travel50").setSpecial().createTitle("explore50Biomes");
        KILL_THIEVING_BANDIT = new LOTRAchievement(Category.GENERAL, 50, item("leather_hat"), "killThievingBandit");
        HUNDREDS = new LOTRAchievement(Category.GENERAL, 51, () -> new ItemStack(Items.IRON_SWORD), "hundreds");
        ALLOY_BRONZE = new LOTRAchievement(Category.GENERAL, 52, item("alloy_forge"), "alloyBronze");
        BANNER_PROTECT = new LOTRAchievement(Category.GENERAL, 53, item("gondor_banner"), "bannerProtect").createTitle();
        CATCH_BUTTERFLY = new LOTRAchievement(Category.GENERAL, 54, item("butterfly_jar"), "catchButterfly");
        UNSMELT = new LOTRAchievement(Category.GENERAL, 55, item("unsmeltery"), "unsmelt");
        TRADE_SCRAP_TRADER = new LOTRAchievement(Category.GENERAL, 56, item("silver_coin"), "tradeScrapTrader");
        COOK_DEER = new LOTRAchievement(Category.GENERAL, 57, item("cooked_venison"), "cookDeer");
        EAT_MAN_FLESH = new LOTRAchievement(Category.GENERAL, 58, item("man_flesh"), "eatManFlesh").setRequiresAnyAlly(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC, LOTRFaction.FactionType.TYPE_TROLL)).createTitle("manFlesh");
        CRAFT_SALTED_FLESH = new LOTRAchievement(Category.GENERAL, 59, item("suspicious_meat"), "craftSaltedFlesh").createTitle("saltedFlesh");
        ENCHANT_BANE_ELF = new LOTRAchievement(Category.GENERAL, 60, item("mordor_scimitar"), "enchantBaneElf").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ELF)).createTitle();
        ENCHANT_BANE_ORC = new LOTRAchievement(Category.GENERAL, 61, item("lindon_sword"), "enchantBaneOrc").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        ENCHANT_BANE_DWARF = new LOTRAchievement(Category.GENERAL, 62, item("gundabad_uruk_waraxe"), "enchantBaneDwarf").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_DWARF)).createTitle();
        ENCHANT_BANE_WARG = new LOTRAchievement(Category.GENERAL, 63, item("rohirric_sword"), "enchantBaneWarg").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        ENCHANT_BANE_TROLL = new LOTRAchievement(Category.GENERAL, 64, item("arnor_sword"), "enchantBaneTroll").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_TROLL)).createTitle();
        ENCHANT_BANE_SPIDER = new LOTRAchievement(Category.GENERAL, 65, item("sting"), "enchantBaneSpider").setRequiresAnyEnemy(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC)).createTitle();
        ENCHANT_BANE_WIGHT = new LOTRAchievement(Category.GENERAL, 66, item("barrow_blade"), "enchantBaneWight").createTitle();
        GET_DRUNK = new LOTRAchievement(Category.GENERAL, 67, item("ale"), "getDrunk").createTitle();
        REFORGE = new LOTRAchievement(Category.GENERAL, 68, () -> new ItemStack(Items.ANVIL), "reforge").createTitle();
        DO_MINIQUEST_HUNTER = new LOTRAchievement(Category.GENERAL, 69, item("red_book"), "doMiniquestHunter").createTitle();
        DO_MINIQUEST_HUNTER5 = new LOTRAchievement(Category.GENERAL, 70, item("headhunters_trophy"), "doMiniquestHunter5").createTitle();
        KILL_HUNTING_PLAYER = new LOTRAchievement(Category.GENERAL, 71, () -> new ItemStack(Items.IRON_SWORD), "killHuntingPlayer").createTitle();
        PLEDGE_SERVICE = new LOTRAchievement(Category.GENERAL, 72, item("gold_ring"), "pledgeService");
        FACTION_CONQUEST = new LOTRAchievement(Category.GENERAL, 73, item("table_of_command"), "factionConquest");
        DEFEAT_INVASION = new LOTRAchievement(Category.GENERAL, 74, () -> new ItemStack(Items.IRON_SWORD), "defeatInvasion");
        PICKPOCKET = new LOTRAchievement(Category.GENERAL, 75, LOTRMiniQuestPickpocket::createPickpocketIcon, "pickpocket").createTitle("pickpocket");
        COMBINE_SMITH_SCROLLS = new LOTRAchievement(Category.GENERAL, 76, item("smiths_scroll"), "combineSmithScrolls");
        ENGRAVE_OWNERSHIP = new LOTRAchievement(Category.GENERAL, 77, item("gondolin_sword"), "engraveOwnership");
        KILL_HOBBIT = new LOTRAchievement(Category.SHIRE, 0, item("hobbit_bone"), "killHobbit").setRequiresEnemy(LOTRFaction.HOBBIT).createTitle();
        SELL_PIPEWEED_LEAF = new LOTRAchievement(Category.SHIRE, 1, item("pipeweed_leaf"), "sellPipeweedLeaf").setRequiresAlly(LOTRFaction.HOBBIT);
        MARRY_HOBBIT = new LOTRAchievement(Category.SHIRE, 2, item("hobbit_marriage_ring"), "marryHobbit").setRequiresAlly(LOTRFaction.HOBBIT);
        FIND_FOUR_LEAF_CLOVER = new LOTRAchievement(Category.SHIRE, 3, item("four_leaf_clover"), "findFourLeafClover").createTitle("fourLeafClover");
        USE_MAGIC_PIPE = new LOTRAchievement(Category.SHIRE, 4, item("smoking_pipe"), "useMagicPipe").createTitle();
        RIDE_SHIRE_PONY = new LOTRAchievement(Category.SHIRE, 5, () -> new ItemStack(Items.SADDLE), "rideShirePony");
        TRADE_BARTENDER = new LOTRAchievement(Category.SHIRE, 6, item("silver_coin"), "tradeBartender").setRequiresAlly(LOTRFaction.HOBBIT);
        SPEAK_TO_DRUNKARD = new LOTRAchievement(Category.SHIRE, 7, item("ale"), "speakToDrunkard");
        TRADE_HOBBIT_SHIRRIFF = new LOTRAchievement(Category.SHIRE, 8, item("silver_coin"), "tradeHobbitShirriffChief").setRequiresAlly(LOTRFaction.HOBBIT);
        KILL_DARK_HUORN = new LOTRAchievement(Category.SHIRE, 9, () -> new ItemStack(Items.OAK_LOG), "killDarkHuorn");
        ENTER_OLD_FOREST = new LOTRAchievement(Category.SHIRE, 10, () -> new ItemStack(Items.OAK_LOG), "enterOldForest").setBiomeAchievement();
        BUY_ORCHARDER_FOOD = new LOTRAchievement(Category.SHIRE, 11, () -> new ItemStack(Items.APPLE), "buyOrcharderFood").setRequiresAlly(LOTRFaction.HOBBIT);
        RIDE_GIRAFFE_SHIRE = new LOTRAchievement(Category.SHIRE, 15, () -> new ItemStack(Items.SADDLE), "rideGiraffeShire").createTitle("zookeeper");
        BUY_POTATO_HOBBIT_FARMER = new LOTRAchievement(Category.SHIRE, 16, () -> new ItemStack(Items.POTATO), "buyPotatoHobbitFarmer").setRequiresAlly(LOTRFaction.HOBBIT);
        DO_MINIQUEST_HOBBIT = new LOTRAchievement(Category.SHIRE, 17, item("red_book"), "doMiniquestHobbit").setRequiresAlly(LOTRFaction.HOBBIT);
        ENTER_WHITE_DOWNS = new LOTRAchievement(Category.SHIRE, 18, item("chalk"), "enterWhiteDowns").setBiomeAchievement();
        USE_HOBBIT_TABLE = new LOTRAchievement(Category.SHIRE, 19, item("hobbit_crafting_table"), "useHobbitTable").setRequiresAlly(LOTRFaction.HOBBIT);
        HIRE_HOBBIT_FARMER = new LOTRAchievement(Category.SHIRE, 20, () -> new ItemStack(Items.IRON_HOE), "hireHobbitFarmer").setRequiresAlly(LOTRFaction.HOBBIT);
        ENTER_BLUE_MOUNTAINS = new LOTRAchievement(Category.BLUE_MOUNTAINS, 0, item("blue_rock"), "enterBlueMountains").setBiomeAchievement();
        SMELT_BLUE_DWARF_STEEL = new LOTRAchievement(Category.BLUE_MOUNTAINS, 4, item("blue_dwarven_steel_ingot"), "smeltBlueDwarfSteel");
        KILL_BLUE_DWARF = new LOTRAchievement(Category.BLUE_MOUNTAINS, 5, item("dwarf_bone"), "killBlueDwarf").setRequiresEnemy(LOTRFaction.BLUE_MOUNTAINS).createTitle();
        WEAR_FULL_BLUE_DWARVEN = new LOTRAchievement(Category.BLUE_MOUNTAINS, 6, item("blue_dwarven_chestplate"), "wearFullBlueDwarven");
        USE_BLUE_DWARVEN_TABLE = new LOTRAchievement(Category.BLUE_MOUNTAINS, 7, item("blue_dwarven_crafting_table"), "useBlueDwarvenTable").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        TRADE_BLUE_DWARF_MINER = new LOTRAchievement(Category.BLUE_MOUNTAINS, 8, item("silver_coin"), "tradeBlueDwarfMiner").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        TRADE_BLUE_DWARF_COMMANDER = new LOTRAchievement(Category.BLUE_MOUNTAINS, 9, item("silver_coin"), "tradeBlueDwarfCommander").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        TRADE_BLUE_DWARF_MERCHANT = new LOTRAchievement(Category.BLUE_MOUNTAINS, 10, item("silver_coin"), "tradeBlueDwarfMerchant").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        MARRY_BLUE_DWARF = new LOTRAchievement(Category.BLUE_MOUNTAINS, 11, item("dwarven_marriage_ring"), "marryBlueDwarf").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        DO_MINIQUEST_BLUE_MOUNTAINS = new LOTRAchievement(Category.BLUE_MOUNTAINS, 12, item("red_book"), "doMiniquestBlueMountains").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        TRADE_BLUE_DWARF_SMITH = new LOTRAchievement(Category.BLUE_MOUNTAINS, 13, item("silver_coin"), "tradeBlueDwarfSmith").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
        ENTER_LINDON = new LOTRAchievement(Category.LINDON, 0, item("high_elven_brick"), "enterLindon").setBiomeAchievement();
        DO_MINIQUEST_HIGH_ELF = new LOTRAchievement(Category.LINDON, 1, item("red_book"), "doMiniquestHighElf").setRequiresAlly(LOTRFaction.HIGH_ELF);
        KILL_HIGH_ELF = new LOTRAchievement(Category.LINDON, 5, item("elf_bone"), "killHighElf").setRequiresEnemy(LOTRFaction.HIGH_ELF).createTitle();
        TRADE_HIGH_ELF_LORD = new LOTRAchievement(Category.LINDON, 6, item("silver_coin"), "tradeHighElfLord").setRequiresAlly(LOTRFaction.HIGH_ELF);
        USE_HIGH_ELVEN_TABLE = new LOTRAchievement(Category.LINDON, 7, item("high_elven_crafting_table"), "useHighElvenTable").setRequiresAlly(LOTRFaction.HIGH_ELF);
        WEAR_FULL_HIGH_ELVEN = new LOTRAchievement(Category.LINDON, 8, item("lindon_chestplate"), "wearFullHighElven");
        SMELT_ELF_STEEL = new LOTRAchievement(Category.LINDON, 9, item("elven_steel_ingot"), "smeltElfSteel");
        TRADE_HIGH_ELF_SMITH = new LOTRAchievement(Category.LINDON, 10, item("silver_coin"), "tradeHighElfSmith").setRequiresAlly(LOTRFaction.HIGH_ELF);
        WEAR_FULL_GONDOLIN = new LOTRAchievement(Category.LINDON, 11, item("gondolin_chestplate"), "wearFullGondolin").createTitle("wearFullGondolin");
        ENTER_TOWER_HILLS = new LOTRAchievement(Category.LINDON, 12, item("chalk_brick"), "enterTowerHills").setBiomeAchievement();
        WEAR_FULL_GALVORN = new LOTRAchievement(Category.LINDON, 13, item("galvorn_chestplate"), "wearFullGalvorn");
        ENTER_BREELAND = new LOTRAchievement(Category.BREE_LAND, 0, item("drystone"), "enterBreeland").setBiomeAchievement();
        ENTER_CHETWOOD = new LOTRAchievement(Category.BREE_LAND, 1, () -> new ItemStack(Items.OAK_SAPLING), "enterChetwood").setBiomeAchievement();
        KILL_BREELANDER = new LOTRAchievement(Category.BREE_LAND, 2, () -> new ItemStack(Items.BONE), "killBreelander").setRequiresEnemy(LOTRFaction.BREE).createTitle();
        DO_MINIQUEST_BREE = new LOTRAchievement(Category.BREE_LAND, 3, item("red_book"), "doMiniquestBree").setRequiresAlly(LOTRFaction.BREE);
        TRADE_BREE_CAPTAIN = new LOTRAchievement(Category.BREE_LAND, 4, item("silver_coin"), "tradeBreeCaptain").setRequiresAlly(LOTRFaction.BREE);
        USE_BREE_TABLE = new LOTRAchievement(Category.BREE_LAND, 5, item("bree_crafting_table"), "useBreeTable").setRequiresAlly(LOTRFaction.BREE);
        TRADE_BREE_BLACKSMITH = new LOTRAchievement(Category.BREE_LAND, 6, item("silver_coin"), "tradeBreeBlacksmith").setRequiresAlly(LOTRFaction.BREE);
        TRADE_BREE_INNKEEPER = new LOTRAchievement(Category.BREE_LAND, 7, item("silver_coin"), "tradeBreeInnkeeper").setRequiresAlly(LOTRFaction.BREE);
        KILL_BREE_HOBBIT = new LOTRAchievement(Category.BREE_LAND, 8, item("hobbit_bone"), "killBreeHobbit").setRequiresEnemy(LOTRFaction.BREE).createTitle();
        KILL_RUFFIAN_SPY = new LOTRAchievement(Category.BREE_LAND, 9, item("leather_hat"), "killRuffianSpy").createTitle();
        KILL_RUFFIAN_BRUTE = new LOTRAchievement(Category.BREE_LAND, 10, item("iron_battleaxe"), "killRuffianBrute");
        DO_MINIQUEST_RUFFIAN_SPY = new LOTRAchievement(Category.BREE_LAND, 11, item("red_book"), "doMiniquestRuffianSpy");
        DO_MINIQUEST_RUFFIAN_BRUTE = new LOTRAchievement(Category.BREE_LAND, 12, item("red_book"), "doMiniquestRuffianBrute");
        TRADE_BREE_MARKET_TRADER = new LOTRAchievement(Category.BREE_LAND, 13, item("silver_coin"), "tradeBreeMarketTrader").setRequiresAlly(LOTRFaction.BREE);
        BUY_APPLE_BREE_FARMER = new LOTRAchievement(Category.BREE_LAND, 14, item("green_apple"), "buyAppleBreeFarmer").setRequiresAlly(LOTRFaction.BREE);
        HIRE_BREE_FARMER = new LOTRAchievement(Category.BREE_LAND, 15, () -> new ItemStack(Items.IRON_HOE), "hireBreeFarmer").setRequiresAlly(LOTRFaction.BREE);
        KILL_RANGER_NORTH = new LOTRAchievement(Category.ERIADOR, 0, item("ranger_bow"), "killRangerNorth").setRequiresEnemy(LOTRFaction.RANGER_NORTH).createTitle();
        WEAR_FULL_RANGER = new LOTRAchievement(Category.ERIADOR, 1, item("ranger_tunic"), "wearFullRanger");
        KILL_TROLL = new LOTRAchievement(Category.ERIADOR, 2, item("troll_bone"), "killTroll").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        GET_TROLL_STATUE = new LOTRAchievement(Category.ERIADOR, 3, item("troll_statue"), "getTrollStatue").createTitle();
        MAKE_TROLL_SNEEZE = new LOTRAchievement(Category.ERIADOR, 4, () -> new ItemStack(Items.SLIME_BALL), "makeTrollSneeze").setRequiresAlly(LOTRFaction.ANGMAR);
        KILL_MOUNTAIN_TROLL = new LOTRAchievement(Category.ERIADOR, 5, item("troll_bone"), "killMountainTroll").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        KILL_TROLL_FLEEING_SUN = new LOTRAchievement(Category.ERIADOR, 6, () -> new ItemStack(Items.FEATHER), "killTrollFleeingSun").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        KILL_MOUNTAIN_TROLL_CHIEFTAIN = new LOTRAchievement(Category.ERIADOR, 7, item("mountain_troll_chieftain_trophy"), "killMountainTrollChieftain").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle("trollSlayer");
        SHOOT_DOWN_MIDGES = new LOTRAchievement(Category.ERIADOR, 8, () -> new ItemStack(Items.ARROW), "shootDownMidges");
        ENTER_TROLLSHAWS = new LOTRAchievement(Category.ERIADOR, 9, item("cooked_mutton"), "enterTrollshaws").setBiomeAchievement();
        ENTER_MIDGEWATER = new LOTRAchievement(Category.ERIADOR, 10, item("quagmire"), "enterMidgewater").setBiomeAchievement();
        ENTER_LONE_LANDS = new LOTRAchievement(Category.ERIADOR, 11, () -> new ItemStack(Items.CRACKED_STONE_BRICKS), "enterLoneLands").setBiomeAchievement();
        ENTER_ETTENMOORS = new LOTRAchievement(Category.ERIADOR, 12, () -> new ItemStack(Items.SPRUCE_SAPLING), "enterEttenmoors").setBiomeAchievement();
        ENTER_ERIADOR = new LOTRAchievement(Category.ERIADOR, 13, () -> new ItemStack(Items.GRASS_BLOCK), "enterEriador").setBiomeAchievement();
        ENTER_COLDFELLS = new LOTRAchievement(Category.ERIADOR, 14, () -> new ItemStack(Items.OAK_SAPLING), "enterColdfells").setBiomeAchievement();
        ENTER_SWANFLEET = new LOTRAchievement(Category.ERIADOR, 15, () -> new ItemStack(Items.LILY_PAD), "enterSwanfleet").setBiomeAchievement();
        ENTER_MINHIRIATH = new LOTRAchievement(Category.ERIADOR, 16, () -> new ItemStack(Items.GRASS_BLOCK), "enterMinhiriath").setBiomeAchievement();
        TRADE_GUNDABAD_CAPTAIN = new LOTRAchievement(Category.ERIADOR, 17, item("silver_coin"), "tradeGundabadCaptain").setRequiresAlly(LOTRFaction.GUNDABAD);
        TRADE_RANGER_NORTH_CAPTAIN = new LOTRAchievement(Category.ERIADOR, 18, item("silver_coin"), "tradeRangerNorthCaptain").setRequiresAlly(LOTRFaction.RANGER_NORTH);
        ENTER_BARROW_DOWNS = new LOTRAchievement(Category.ERIADOR, 25, () -> new ItemStack(Items.BONE), "enterBarrowDowns").setBiomeAchievement();
        USE_RANGER_TABLE = new LOTRAchievement(Category.ERIADOR, 26, item("ranger_crafting_table"), "useRangerTable").setRequiresAlly(LOTRFaction.RANGER_NORTH);
        USE_GUNDABAD_TABLE = new LOTRAchievement(Category.ERIADOR, 27, item("gundabad_crafting_table"), "useGundabadTable").setRequiresAlly(LOTRFaction.GUNDABAD);
        DO_MINIQUEST_RANGER = new LOTRAchievement(Category.ERIADOR, 28, item("red_book"), "doMiniquestRanger").setRequiresAlly(LOTRFaction.RANGER_NORTH);
        DO_MINIQUEST_GUNDABAD = new LOTRAchievement(Category.ERIADOR, 29, item("red_book"), "doMiniquestGundabad").setRequiresAlly(LOTRFaction.GUNDABAD);
        KILL_BARROW_WIGHT = new LOTRAchievement(Category.ERIADOR, 30, () -> new ItemStack(Items.BONE), "killBarrowWight").createTitle("killBarrowWight");
        KILL_GUNDABAD_ORC = new LOTRAchievement(Category.ERIADOR, 31, item("orc_bone"), "killGundabadOrc").setRequiresEnemy(LOTRFaction.GUNDABAD).createTitle();
        KILL_GUNDABAD_URUK = new LOTRAchievement(Category.ERIADOR, 32, item("gundabad_uruk_helmet"), "killGundabadUruk").setRequiresEnemy(LOTRFaction.GUNDABAD);
        WEAR_FULL_GUNDABAD_URUK = new LOTRAchievement(Category.ERIADOR, 33, item("gundabad_uruk_chestplate"), "wearFullGundabadUruk");
        KILL_DUNEDAIN = new LOTRAchievement(Category.ERIADOR, 34, () -> new ItemStack(Items.BONE), "killDunedain").setRequiresEnemy(LOTRFaction.RANGER_NORTH).createTitle();
        ENTER_ANGLE = new LOTRAchievement(Category.ERIADOR, 35, () -> new ItemStack(Items.WOOL.pick(DyeColor.GREEN)), "enterAngle").setBiomeAchievement();
        WEAR_FULL_ARNOR = new LOTRAchievement(Category.ERIADOR, 36, item("arnor_chestplate"), "wearFullArnor");
        TRADE_DUNEDAIN_BLACKSMITH = new LOTRAchievement(Category.ERIADOR, 37, item("silver_coin"), "tradeDunedainBlacksmith").setRequiresAlly(LOTRFaction.RANGER_NORTH);
        ENTER_RIVENDELL = new LOTRAchievement(Category.ERIADOR, 38, item("high_elven_brick"), "enterRivendell").setBiomeAchievement();
        USE_RIVENDELL_TABLE = new LOTRAchievement(Category.ERIADOR, 39, item("rivendell_crafting_table"), "useRivendellTable").setRequiresAlly(LOTRFaction.HIGH_ELF);
        WEAR_FULL_RIVENDELL = new LOTRAchievement(Category.ERIADOR, 40, item("rivendell_chestplate"), "wearFullRivendell");
        TRADE_RIVENDELL_SMITH = new LOTRAchievement(Category.ERIADOR, 41, item("silver_coin"), "tradeRivendellSmith").setRequiresAlly(LOTRFaction.HIGH_ELF);
        TRADE_RIVENDELL_LORD = new LOTRAchievement(Category.ERIADOR, 42, item("silver_coin"), "tradeRivendellLord").setRequiresAlly(LOTRFaction.HIGH_ELF);
        TRADE_RIVENDELL_TRADER = new LOTRAchievement(Category.ERIADOR, 43, item("silver_coin"), "tradeRivendellTrader").setRequiresAlly(LOTRFaction.HIGH_ELF);
        DO_MINIQUEST_RIVENDELL = new LOTRAchievement(Category.ERIADOR, 44, item("red_book"), "doMiniquestRivendell").setRequiresAlly(LOTRFaction.HIGH_ELF);
        KILL_RIVENDELL_ELF = new LOTRAchievement(Category.ERIADOR, 45, item("elf_bone"), "killRivendellElf").setRequiresEnemy(LOTRFaction.HIGH_ELF);
        TRADE_GUNDABAD_TRADER = new LOTRAchievement(Category.ERIADOR, 46, item("silver_coin"), "tradeGundabadTrader").setRequiresAlly(LOTRFaction.GUNDABAD);
        TRADE_ANGMAR_CAPTAIN = new LOTRAchievement(Category.ANGMAR, 0, item("silver_coin"), "tradeAngmarCaptain").setRequiresAlly(LOTRFaction.ANGMAR);
        KILL_ANGMAR_ORC = new LOTRAchievement(Category.ANGMAR, 1, item("orc_bone"), "killAngmarOrc").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        ENTER_ANGMAR = new LOTRAchievement(Category.ANGMAR, 2, item("angmar_brick"), "enterAngmar").setBiomeAchievement();
        USE_ANGMAR_TABLE = new LOTRAchievement(Category.ANGMAR, 3, item("angmar_crafting_table"), "useAngmarTable").setRequiresAlly(LOTRFaction.ANGMAR);
        WEAR_FULL_ANGMAR = new LOTRAchievement(Category.ANGMAR, 4, item("angmar_chestplate"), "wearFullAngmar");
        DO_MINIQUEST_ANGMAR = new LOTRAchievement(Category.ANGMAR, 8, item("red_book"), "doMiniquestAngmar").setRequiresAlly(LOTRFaction.ANGMAR);
        TRADE_ANGMAR_TRADER = new LOTRAchievement(Category.ANGMAR, 9, item("silver_coin"), "tradeAngmarTrader").setRequiresAlly(LOTRFaction.ANGMAR);
        KILL_ANGMAR_HILLMAN = new LOTRAchievement(Category.ANGMAR, 10, () -> new ItemStack(Items.BONE), "killAngmarHillman").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        TRADE_ANGMAR_HILLMAN_CHIEFTAIN = new LOTRAchievement(Category.ANGMAR, 11, item("silver_coin"), "tradeAngmarHillmanChieftain").setRequiresAlly(LOTRFaction.ANGMAR);
        KILL_SNOW_TROLL = new LOTRAchievement(Category.ANGMAR, 12, item("troll_bone"), "killSnowTroll").setRequiresEnemy(LOTRFaction.ANGMAR).createTitle();
        ENTER_EREGION = new LOTRAchievement(Category.EREGION, 0, item("holly_sapling"), "enterEregion").setBiomeAchievement();
        ENTER_ENEDWAITH = new LOTRAchievement(Category.ENEDWAITH, 0, () -> new ItemStack(Items.GRASS_BLOCK), "enterEnedwaith").setBiomeAchievement();
        ENTER_NAN_CURUNIR = new LOTRAchievement(Category.ENEDWAITH, 1, item("uruk_cleaver"), "enterNanCurunir").setBiomeAchievement();
        ENTER_PUKEL = new LOTRAchievement(Category.ENEDWAITH, 2, () -> new ItemStack(Items.DARK_OAK_SAPLING), "enterPukel").setBiomeAchievement();
        KILL_DUNLENDING = new LOTRAchievement(Category.DUNLAND, 0, item("dunlending_club"), "killDunlending").setRequiresEnemy(LOTRFaction.DUNLAND).createTitle();
        WEAR_FULL_DUNLENDING = new LOTRAchievement(Category.DUNLAND, 1, item("dunlending_chestplate"), "wearFullDunlending");
        USE_DUNLENDING_TABLE = new LOTRAchievement(Category.DUNLAND, 2, item("dunlending_crafting_table"), "useDunlendingTable").setRequiresAlly(LOTRFaction.DUNLAND);
        TRADE_DUNLENDING_WARLORD = new LOTRAchievement(Category.DUNLAND, 3, item("silver_coin"), "tradeDunlendingWarlord").setRequiresAlly(LOTRFaction.DUNLAND);
        USE_DUNLENDING_TRIDENT = new LOTRAchievement(Category.DUNLAND, 4, item("dunlending_trident"), "useDunlendingTrident");
        TRADE_DUNLENDING_BARTENDER = new LOTRAchievement(Category.DUNLAND, 5, item("silver_coin"), "tradeDunlendingBartender").setRequiresAlly(LOTRFaction.DUNLAND);
        ENTER_DUNLAND = new LOTRAchievement(Category.DUNLAND, 6, () -> new ItemStack(Items.STONE_SWORD), "enterDunland").setBiomeAchievement();
        DO_MINIQUEST_DUNLAND = new LOTRAchievement(Category.DUNLAND, 10, item("red_book"), "doMiniquestDunland").setRequiresAlly(LOTRFaction.DUNLAND);
        CLIMB_MISTY_MOUNTAINS = new LOTRAchievement(Category.MISTY_MOUNTAINS, 0, () -> new ItemStack(Items.SNOW), "climbMistyMountains");
        ENTER_MISTY_MOUNTAINS = new LOTRAchievement(Category.MISTY_MOUNTAINS, 1, () -> new ItemStack(Items.STONE), "enterMistyMountains").setBiomeAchievement();
        TAME_GOLLUM = new LOTRAchievement(Category.MISTY_MOUNTAINS, 2, () -> new ItemStack(Items.COD), "tameGollum");
        ENTER_FORODWAITH = new LOTRAchievement(Category.FORODWAITH, 0, () -> new ItemStack(Items.ICE), "enterForodwaith").setBiomeAchievement();
        ENTER_VALES_OF_ANDUIN = new LOTRAchievement(Category.RHOVANION, 0, () -> new ItemStack(Items.OAK_SAPLING), "enterValesOfAnduin").setBiomeAchievement();
        ENTER_GREY_MOUNTAINS = new LOTRAchievement(Category.RHOVANION, 1, () -> new ItemStack(Items.STONE), "enterGreyMountains").setBiomeAchievement();
        ENTER_GLADDEN_FIELDS = new LOTRAchievement(Category.RHOVANION, 2, item("yellow_iris"), "enterGladdenFields").setBiomeAchievement();
        ENTER_EMYN_MUIL = new LOTRAchievement(Category.RHOVANION, 3, () -> new ItemStack(Items.STONE), "enterEmynMuil").setBiomeAchievement();
        ENTER_BROWN_LANDS = new LOTRAchievement(Category.RHOVANION, 4, () -> new ItemStack(Items.DIRT), "enterBrownLands").setBiomeAchievement();
        ENTER_WILDERLAND = new LOTRAchievement(Category.RHOVANION, 5, () -> new ItemStack(Items.GRASS_BLOCK), "enterWilderland").setBiomeAchievement();
        ENTER_DAGORLAD = new LOTRAchievement(Category.RHOVANION, 6, item("mordor_gravel"), "enterDagorlad").setBiomeAchievement();
        ENTER_CELEBRANT = new LOTRAchievement(Category.RHOVANION, 8, () -> new ItemStack(Items.AZURE_BLUET), "enterCelebrant").setBiomeAchievement();
        ENTER_LONG_MARSHES = new LOTRAchievement(Category.RHOVANION, 9, item("reeds"), "enterLongMarshes").setBiomeAchievement();
        ENTER_EAST_BIGHT = new LOTRAchievement(Category.RHOVANION, 10, () -> new ItemStack(Items.OAK_LOG), "enterEastBight").setBiomeAchievement();
        KILL_MIRKWOOD_SPIDER = new LOTRAchievement(Category.MIRKWOOD, 0, () -> new ItemStack(Items.STRING), "killMirkwoodSpider").setRequiresEnemy(LOTRFaction.DOL_GULDUR).createTitle();
        KILL_WOOD_ELF = new LOTRAchievement(Category.MIRKWOOD, 1, item("elf_bone"), "killWoodElf").setRequiresEnemy(LOTRFaction.WOOD_ELF).createTitle();
        USE_WOOD_ELVEN_TABLE = new LOTRAchievement(Category.MIRKWOOD, 2, item("wood_elven_crafting_table"), "useWoodElvenTable").setRequiresAlly(LOTRFaction.WOOD_ELF);
        WEAR_FULL_WOOD_ELVEN_SCOUT = new LOTRAchievement(Category.MIRKWOOD, 4, item("wood_elven_scout_tunic"), "wearFullWoodElvenScout");
        TRADE_WOOD_ELF_CAPTAIN = new LOTRAchievement(Category.MIRKWOOD, 5, item("silver_coin"), "tradeWoodElfCaptain").setRequiresAlly(LOTRFaction.WOOD_ELF);
        RIDE_BARREL_MIRKWOOD = new LOTRAchievement(Category.MIRKWOOD, 6, item("barrel"), "rideBarrelMirkwood").createTitle("rideBarrel");
        ENTER_MIRKWOOD = new LOTRAchievement(Category.MIRKWOOD, 7, item("web_ungoliant"), "enterMirkwood").setBiomeAchievement();
        ENTER_WOODLAND_REALM = new LOTRAchievement(Category.MIRKWOOD, 8, item("mirkwood_bow"), "enterWoodlandRealm").setBiomeAchievement();
        WEAR_FULL_WOOD_ELVEN = new LOTRAchievement(Category.MIRKWOOD, 9, item("wood_elven_chestplate"), "wearFullWoodElven");
        ENTER_DOL_GULDUR = new LOTRAchievement(Category.MIRKWOOD, 17, item("dol_guldur_brick"), "enterDolGuldur").setBiomeAchievement();
        KILL_DOL_GULDUR_ORC = new LOTRAchievement(Category.MIRKWOOD, 18, item("orc_bone"), "killDolGuldurOrc").setRequiresEnemy(LOTRFaction.DOL_GULDUR).createTitle();
        USE_DOL_GULDUR_TABLE = new LOTRAchievement(Category.MIRKWOOD, 19, item("dol_guldur_crafting_table"), "useDolGuldurTable").setRequiresAlly(LOTRFaction.DOL_GULDUR);
        TRADE_DOL_GULDUR_CAPTAIN = new LOTRAchievement(Category.MIRKWOOD, 20, item("silver_coin"), "tradeDolGuldurCaptain").setRequiresAlly(LOTRFaction.DOL_GULDUR);
        KILL_MIRK_TROLL = new LOTRAchievement(Category.MIRKWOOD, 21, item("troll_bone"), "killMirkTroll").setRequiresEnemy(LOTRFaction.DOL_GULDUR).createTitle();
        WEAR_FULL_DOL_GULDUR = new LOTRAchievement(Category.MIRKWOOD, 22, item("dol_guldur_chestplate"), "wearFullDolGuldur");
        DO_MINIQUEST_WOOD_ELF = new LOTRAchievement(Category.MIRKWOOD, 23, item("red_book"), "doMiniquestWoodElf").setRequiresAlly(LOTRFaction.WOOD_ELF);
        DO_MINIQUEST_DOL_GULDUR = new LOTRAchievement(Category.MIRKWOOD, 24, item("red_book"), "doMiniquestDolGuldur").setRequiresAlly(LOTRFaction.DOL_GULDUR);
        TRADE_DOL_GULDUR_TRADER = new LOTRAchievement(Category.MIRKWOOD, 25, item("silver_coin"), "tradeDolGuldurTrader").setRequiresAlly(LOTRFaction.DOL_GULDUR);
        TRADE_WOOD_ELF_SMITH = new LOTRAchievement(Category.MIRKWOOD, 26, item("silver_coin"), "tradeWoodElfSmith").setRequiresAlly(LOTRFaction.WOOD_ELF);
        ENTER_DALE = new LOTRAchievement(Category.DALE, 0, item("dalish_pastry"), "enterDale").setBiomeAchievement();
        DO_MINIQUEST_DALE = new LOTRAchievement(Category.DALE, 4, item("red_book"), "doMiniquestDale").setRequiresAlly(LOTRFaction.DALE);
        USE_DALE_TABLE = new LOTRAchievement(Category.DALE, 5, item("dale_crafting_table"), "useDaleTable").setRequiresAlly(LOTRFaction.DALE);
        WEAR_FULL_DALE = new LOTRAchievement(Category.DALE, 6, item("dale_chestplate"), "wearFullDale");
        KILL_DALISH = new LOTRAchievement(Category.DALE, 7, () -> new ItemStack(Items.BONE), "killDalish").setRequiresEnemy(LOTRFaction.DALE).createTitle();
        TRADE_DALE_CAPTAIN = new LOTRAchievement(Category.DALE, 8, item("silver_coin"), "tradeDaleCaptain").setRequiresAlly(LOTRFaction.DALE);
        TRADE_DALE_BLACKSMITH = new LOTRAchievement(Category.DALE, 9, item("silver_coin"), "tradeDaleBlacksmith").setRequiresAlly(LOTRFaction.DALE);
        TRADE_DALE_BAKER = new LOTRAchievement(Category.DALE, 10, item("silver_coin"), "tradeDaleBaker").setRequiresAlly(LOTRFaction.DALE);
        TRADE_DALE_MERCHANT = new LOTRAchievement(Category.DALE, 11, item("silver_coin"), "tradeDaleMerchant").setRequiresAlly(LOTRFaction.DALE);
        ENTER_EREBOR = new LOTRAchievement(Category.DALE, 12, item("dwarven_brick"), "enterErebor").setBiomeAchievement();
        OPEN_DALE_CRACKER = new LOTRAchievement(Category.DALE, 13, item("red_dalish_cracker"), "openDaleCracker");
        KILL_DWARF = new LOTRAchievement(Category.IRON_HILLS, 0, item("dwarf_bone"), "killDwarf").setRequiresEnemy(LOTRFaction.DURINS_FOLK).createTitle();
        WEAR_FULL_DWARVEN = new LOTRAchievement(Category.IRON_HILLS, 1, item("dwarven_chestplate"), "wearFullDwarven");
        USE_DWARVEN_THROWING_AXE = new LOTRAchievement(Category.IRON_HILLS, 2, item("dwarven_throwing_axe"), "useDwarvenThrowingAxe");
        USE_DWARVEN_TABLE = new LOTRAchievement(Category.IRON_HILLS, 3, item("dwarven_crafting_table"), "useDwarvenTable").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        TRADE_DWARF_MINER = new LOTRAchievement(Category.IRON_HILLS, 4, item("silver_coin"), "tradeDwarfMiner").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        TRADE_DWARF_COMMANDER = new LOTRAchievement(Category.IRON_HILLS, 5, item("silver_coin"), "tradeDwarfCommander").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        MINE_GLOWSTONE = new LOTRAchievement(Category.IRON_HILLS, 6, item("glowstone_ore"), "mineGlowstone");
        SMELT_DWARF_STEEL = new LOTRAchievement(Category.IRON_HILLS, 7, item("dwarven_steel_ingot"), "smeltDwarfSteel");
        DRINK_DWARVEN_TONIC = new LOTRAchievement(Category.IRON_HILLS, 8, item("dwarven_tonic"), "drinkDwarvenTonic");
        CRAFT_MITHRIL_DWARVEN_BRICK = new LOTRAchievement(Category.IRON_HILLS, 9, item("dwarven_mithril_brick"), "craftMithrilDwarvenBrick");
        TALK_DWARF_WOMAN = new LOTRAchievement(Category.IRON_HILLS, 10, item("dwarven_ale"), "talkDwarfWoman").setRequiresAnyAlly(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_DWARF));
        ENTER_IRON_HILLS = new LOTRAchievement(Category.IRON_HILLS, 11, item("dwarven_pickaxe"), "enterIronHills").setBiomeAchievement();
        USE_DWARVEN_DOOR = new LOTRAchievement(Category.IRON_HILLS, 12, item("dwarven_door"), "useDwarvenDoor");
        MARRY_DWARF = new LOTRAchievement(Category.IRON_HILLS, 16, item("dwarven_marriage_ring"), "marryDwarf").setRequiresAnyAlly(LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_DWARF));
        DO_MINIQUEST_DWARF = new LOTRAchievement(Category.IRON_HILLS, 17, item("red_book"), "doMiniquestDwarf").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        TRADE_IRON_HILLS_MERCHANT = new LOTRAchievement(Category.IRON_HILLS, 18, item("silver_coin"), "tradeIronHillsMerchant").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        TRADE_DWARF_SMITH = new LOTRAchievement(Category.IRON_HILLS, 19, item("silver_coin"), "tradeDwarfSmith").setRequiresAlly(LOTRFaction.DURINS_FOLK);
        KILL_ELF = new LOTRAchievement(Category.LOTHLORIEN, 0, item("elf_bone"), "killElf").setRequiresEnemy(LOTRFaction.LOTHLORIEN).createTitle();
        USE_ELVEN_PORTAL = new LOTRAchievement(Category.LOTHLORIEN, 1, item("quendite_grass"), "useElvenPortal").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        WEAR_FULL_ELVEN = new LOTRAchievement(Category.LOTHLORIEN, 2, item("galadhrim_chestplate"), "wearFullElven");
        USE_ELVEN_TABLE = new LOTRAchievement(Category.LOTHLORIEN, 3, item("elven_crafting_table"), "useElvenTable").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        TRADE_ELF_LORD = new LOTRAchievement(Category.LOTHLORIEN, 4, item("silver_coin"), "tradeElfLord").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        MINE_QUENDITE = new LOTRAchievement(Category.LOTHLORIEN, 5, item("quendite_ore"), "mineQuendite");
        TAKE_MALLORN_WOOD = new LOTRAchievement(Category.LOTHLORIEN, 6, item("mallorn_log"), "takeMallornWood").setRequiresEnemy(LOTRFaction.LOTHLORIEN);
        ENTER_LOTHLORIEN = new LOTRAchievement(Category.LOTHLORIEN, 8, item("mallorn_sapling"), "enterLothlorien").setBiomeAchievement();
        TRADE_ELVEN_TRADER = new LOTRAchievement(Category.LOTHLORIEN, 12, item("silver_coin"), "tradeElvenTrader").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        DO_MINIQUEST_GALADHRIM = new LOTRAchievement(Category.LOTHLORIEN, 13, item("red_book"), "doMiniquestGaladhrim").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        TRADE_GALADHRIM_SMITH = new LOTRAchievement(Category.LOTHLORIEN, 14, item("silver_coin"), "tradeGaladhrimSmith").setRequiresAlly(LOTRFaction.LOTHLORIEN);
        WEAR_FULL_HITHLAIN = new LOTRAchievement(Category.LOTHLORIEN, 15, item("galadhrim_cloak_tunic"), "wearFullHithlain");
        KILL_ENT = new LOTRAchievement(Category.FANGORN, 0, () -> new ItemStack(Items.OAK_LOG), "killEnt").setRequiresEnemy(LOTRFaction.FANGORN).createTitle("killEnt");
        DRINK_ENT_DRAUGHT = new LOTRAchievement(Category.FANGORN, 1, item("ent_draught"), "drinkEntDraught").setRequiresAlly(LOTRFaction.FANGORN);
        KILL_HUORN = new LOTRAchievement(Category.FANGORN, 2, () -> new ItemStack(Items.OAK_LOG), "killHuorn").setRequiresEnemy(LOTRFaction.FANGORN).createTitle();
        TALK_ENT = new LOTRAchievement(Category.FANGORN, 3, () -> new ItemStack(Items.OAK_LOG), "talkEnt");
        ENTER_FANGORN = new LOTRAchievement(Category.FANGORN, 4, () -> new ItemStack(Items.OAK_LEAVES), "enterFangorn").setBiomeAchievement();
        SUMMON_HUORN = new LOTRAchievement(Category.FANGORN, 8, item("ent_draught"), "summonHuorn").setRequiresAlly(LOTRFaction.FANGORN);
        KILL_MALLORN_ENT = new LOTRAchievement(Category.FANGORN, 9, item("mallorn_ent_trophy"), "killMallornEnt").setRequiresEnemy(LOTRFaction.FANGORN).createTitle("entSlayer");
        RAID_URUK_CAMP = new LOTRAchievement(Category.ROHAN, 0, () -> new ItemStack(Items.SKELETON_SKULL), "raidUrukCamp").setRequiresEnemy(LOTRFaction.ISENGARD);
        USE_URUK_TABLE = new LOTRAchievement(Category.ROHAN, 1, item("uruk_crafting_table"), "useUrukTable").setRequiresAlly(LOTRFaction.ISENGARD);
        TRADE_URUK_TRADER = new LOTRAchievement(Category.ROHAN, 2, item("silver_coin"), "tradeUrukTrader").setRequiresAlly(LOTRFaction.ISENGARD);
        TRADE_URUK_CAPTAIN = new LOTRAchievement(Category.ROHAN, 3, item("silver_coin"), "tradeUrukCaptain").setRequiresAlly(LOTRFaction.ISENGARD);
        USE_ROHIRRIC_TABLE = new LOTRAchievement(Category.ROHAN, 4, item("rohirric_crafting_table"), "useRohirricTable").setRequiresAlly(LOTRFaction.ROHAN);
        SMELT_URUK_STEEL = new LOTRAchievement(Category.ROHAN, 5, item("uruk_steel_ingot"), "smeltUrukSteel");
        WEAR_FULL_URUK = new LOTRAchievement(Category.ROHAN, 6, item("uruk_chestplate"), "wearFullUruk");
        HIRE_WARG_BOMBARDIER = new LOTRAchievement(Category.ROHAN, 7, item("fur"), "hireWargBombardier").setRequiresAlly(LOTRFaction.ISENGARD);
        KILL_ROHIRRIM = new LOTRAchievement(Category.ROHAN, 8, item("rohirric_sword"), "killRohirrim").setRequiresEnemy(LOTRFaction.ROHAN).createTitle();
        TRADE_ROHIRRIM_MARSHAL = new LOTRAchievement(Category.ROHAN, 9, item("silver_coin"), "tradeRohirrimMarshal").setRequiresAlly(LOTRFaction.ROHAN);
        WEAR_FULL_ROHIRRIC = new LOTRAchievement(Category.ROHAN, 10, item("rohirric_hauberk"), "wearFullRohirric");
        TRADE_ROHAN_BLACKSMITH = new LOTRAchievement(Category.ROHAN, 11, item("silver_coin"), "tradeRohanBlacksmith").setRequiresAlly(LOTRFaction.ROHAN);
        BUY_ROHAN_MEAD = new LOTRAchievement(Category.ROHAN, 12, item("mead"), "buyRohanMead").setRequiresAlly(LOTRFaction.ROHAN);
        ENTER_ROHAN = new LOTRAchievement(Category.ROHAN, 13, item("rohirric_spear"), "enterRohan").setBiomeAchievement();
        ENTER_ROHAN_URUK_HIGHLANDS = new LOTRAchievement(Category.ROHAN, 14, item("uruk_helmet"), "enterRohanUrukHighlands").setBiomeAchievement();
        DO_MINIQUEST_ROHAN = new LOTRAchievement(Category.ROHAN, 21, item("red_book"), "doMiniquestRohan").setRequiresAlly(LOTRFaction.ROHAN);
        DO_MINIQUEST_ISENGARD = new LOTRAchievement(Category.ROHAN, 22, item("red_book"), "doMiniquestIsengard").setRequiresAlly(LOTRFaction.ISENGARD);
        KILL_URUK_HAI = new LOTRAchievement(Category.ROHAN, 23, item("orc_bone"), "killUrukHai").setRequiresEnemy(LOTRFaction.ISENGARD).createTitle();
        ENTER_ENTWASH_MOUTH = new LOTRAchievement(Category.ROHAN, 24, () -> new ItemStack(Items.FERN), "enterEntwashMouth").setBiomeAchievement();
        WEAR_FULL_ROHIRRIC_MARSHAL = new LOTRAchievement(Category.ROHAN, 25, item("rohirric_marshal_chestplate"), "wearFullRohirricMarshal");
        KILL_ISENGARD_SNAGA = new LOTRAchievement(Category.ROHAN, 26, item("orc_bone"), "killIsengardSnaga").setRequiresEnemy(LOTRFaction.ISENGARD).createTitle();
        DO_MINIQUEST_ROHAN_SHIELDMAIDEN = new LOTRAchievement(Category.ROHAN, 27, item("red_book"), "doMiniquestRohanShieldmaiden").setRequiresAlly(LOTRFaction.ROHAN);
        TRADE_ROHAN_FARMER = new LOTRAchievement(Category.ROHAN, 28, item("silver_coin"), "tradeRohanFarmer").setRequiresAlly(LOTRFaction.ROHAN);
        HIRE_ROHAN_FARMER = new LOTRAchievement(Category.ROHAN, 29, () -> new ItemStack(Items.IRON_HOE), "hireRohanFarmer").setRequiresAlly(LOTRFaction.ROHAN);
        TRADE_ROHAN_MARKET_TRADER = new LOTRAchievement(Category.ROHAN, 30, item("silver_coin"), "tradeRohanMarketTrader").setRequiresAlly(LOTRFaction.ROHAN);
        TRADE_ROHAN_STABLEMASTER = new LOTRAchievement(Category.ROHAN, 31, item("silver_coin"), "tradeRohanStablemaster").setRequiresAlly(LOTRFaction.ROHAN);
        ENTER_ADORNLAND = new LOTRAchievement(Category.ROHAN, 32, () -> new ItemStack(Items.SKELETON_SKULL), "enterAdornland").setBiomeAchievement();
        KILL_GONDORIAN = new LOTRAchievement(Category.GONDOR, 0, item("gondor_sword"), "killGondorian").setRequiresEnemy(LOTRFaction.GONDOR).createTitle();
        LIGHT_GONDOR_BEACON = new LOTRAchievement(Category.GONDOR, 1, item("beacon_of_gondor"), "lightGondorBeacon");
        USE_GONDORIAN_TABLE = new LOTRAchievement(Category.GONDOR, 2, item("gondorian_crafting_table"), "useGondorianTable").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_GONDOR_BLACKSMITH = new LOTRAchievement(Category.GONDOR, 3, item("silver_coin"), "tradeGondorBlacksmith").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_GONDORIAN_CAPTAIN = new LOTRAchievement(Category.GONDOR, 4, item("silver_coin"), "tradeGondorianCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        WEAR_FULL_GONDORIAN = new LOTRAchievement(Category.GONDOR, 5, item("gondor_chestplate"), "wearFullGondorian");
        KILL_RANGER_ITHILIEN = new LOTRAchievement(Category.GONDOR, 6, item("gondor_bow"), "killRangerIthilien").setRequiresEnemy(LOTRFaction.GONDOR).createTitle();
        ENTER_GONDOR = new LOTRAchievement(Category.GONDOR, 7, item("gondor_spear"), "enterGondor").setBiomeAchievement();
        ENTER_ITHILIEN = new LOTRAchievement(Category.GONDOR, 8, item("gondor_bow"), "enterIthilien").setBiomeAchievement();
        ENTER_WHITE_MOUNTAINS = new LOTRAchievement(Category.GONDOR, 9, item("gondor_rock"), "enterWhiteMountains").setBiomeAchievement();
        ENTER_TOLFALAS = new LOTRAchievement(Category.GONDOR, 13, () -> new ItemStack(Items.STONE), "enterTolfalas").setBiomeAchievement();
        ENTER_LEBENNIN = new LOTRAchievement(Category.GONDOR, 14, () -> new ItemStack(Items.BIRCH_SAPLING), "enterLebennin").setBiomeAchievement();
        DO_MINIQUEST_GONDOR = new LOTRAchievement(Category.GONDOR, 15, item("red_book"), "doMiniquestGondor").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_RANGER_ITHILIEN_CAPTAIN = new LOTRAchievement(Category.GONDOR, 16, item("silver_coin"), "tradeRangerIthilienCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        USE_DOL_AMROTH_TABLE = new LOTRAchievement(Category.GONDOR, 17, item("dol_amroth_crafting_table"), "useDolAmrothTable").setRequiresAlly(LOTRFaction.GONDOR);
        WEAR_FULL_DOL_AMROTH = new LOTRAchievement(Category.GONDOR, 18, item("dol_amroth_chestplate"), "wearFullDolAmroth");
        KILL_SWAN_KNIGHT = new LOTRAchievement(Category.GONDOR, 19, item("dol_amroth_sword"), "killSwanKnight").setRequiresEnemy(LOTRFaction.GONDOR).createTitle();
        ENTER_DOR_EN_ERNIL = new LOTRAchievement(Category.GONDOR, 20, () -> new ItemStack(Items.FEATHER), "enterDorEnErnil").setBiomeAchievement();
        TRADE_DOL_AMROTH_CAPTAIN = new LOTRAchievement(Category.GONDOR, 21, item("silver_coin"), "tradeDolAmrothCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        ENTER_ANDUIN_MOUTH = new LOTRAchievement(Category.GONDOR, 22, () -> new ItemStack(Items.LILY_PAD), "enterAnduinMouth").setBiomeAchievement();
        ENTER_PELENNOR = new LOTRAchievement(Category.GONDOR, 23, item("gondor_brick"), "enterPelennor").setBiomeAchievement();
        WEAR_FULL_RANGER_ITHILIEN = new LOTRAchievement(Category.GONDOR, 24, item("ithilien_ranger_tunic"), "wearFullRangerIthilien");
        ENTER_LOSSARNACH = new LOTRAchievement(Category.GONDOR, 25, item("apple_sapling"), "enterLossarnach").setBiomeAchievement();
        ENTER_IMLOTH_MELUI = new LOTRAchievement(Category.GONDOR, 26, () -> new ItemStack(Items.ROSE_BUSH), "enterImlothMelui").setBiomeAchievement();
        WEAR_FULL_LOSSARNACH = new LOTRAchievement(Category.GONDOR, 27, item("lossarnach_chestplate"), "wearFullLossarnach");
        WEAR_FULL_PELARGIR = new LOTRAchievement(Category.GONDOR, 28, item("pelargir_chestplate"), "wearFullPelargir");
        WEAR_FULL_PINNATH_GELIN = new LOTRAchievement(Category.GONDOR, 29, item("pinnath_gelin_chestplate"), "wearFullPinnathGelin");
        WEAR_FULL_BLACKROOT = new LOTRAchievement(Category.GONDOR, 30, item("blackroot_vale_chestplate"), "wearFullBlackroot");
        HIRE_GONDOR_FARMER = new LOTRAchievement(Category.GONDOR, 31, () -> new ItemStack(Items.IRON_HOE), "hireGondorFarmer").setRequiresAlly(LOTRFaction.GONDOR);
        BUY_PIPEWEED_GONDOR_FARMER = new LOTRAchievement(Category.GONDOR, 32, item("pipeweed_plant"), "buyPipeweedGondorFarmer").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_GONDOR_BARTENDER = new LOTRAchievement(Category.GONDOR, 33, item("silver_coin"), "tradeGondorBartender").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_GONDOR_MARKET_TRADER = new LOTRAchievement(Category.GONDOR, 34, item("silver_coin"), "tradeGondorMarketTrader").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_LOSSARNACH_CAPTAIN = new LOTRAchievement(Category.GONDOR, 35, item("silver_coin"), "tradeLossarnachCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_PELARGIR_CAPTAIN = new LOTRAchievement(Category.GONDOR, 36, item("silver_coin"), "tradePelargirCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_PINNATH_GELIN_CAPTAIN = new LOTRAchievement(Category.GONDOR, 37, item("silver_coin"), "tradePinnathGelinCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_BLACKROOT_CAPTAIN = new LOTRAchievement(Category.GONDOR, 38, item("silver_coin"), "tradeBlackrootCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        TRADE_LEBENNIN_CAPTAIN = new LOTRAchievement(Category.GONDOR, 39, item("silver_coin"), "tradeLebenninCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        ENTER_PELARGIR = new LOTRAchievement(Category.GONDOR, 40, item("pelargir_trident"), "enterPelargir").setBiomeAchievement();
        WEAR_FULL_LAMEDON = new LOTRAchievement(Category.GONDOR, 41, item("lamedon_chestplate"), "wearFullLamedon");
        TRADE_LAMEDON_CAPTAIN = new LOTRAchievement(Category.GONDOR, 42, item("silver_coin"), "tradeLamedonCaptain").setRequiresAlly(LOTRFaction.GONDOR);
        ENTER_LAMEDON = new LOTRAchievement(Category.GONDOR, 43, item("bronze_axe"), "enterLamedon").setBiomeAchievement();
        ENTER_BLACKROOT_VALE = new LOTRAchievement(Category.GONDOR, 44, item("blackroot"), "enterBlackrootVale").setBiomeAchievement();
        ENTER_PINNATH_GELIN = new LOTRAchievement(Category.GONDOR, 45, () -> new ItemStack(Items.GRASS_BLOCK), "enterPinnathGelin").setBiomeAchievement();
        ENTER_ANDRAST = new LOTRAchievement(Category.GONDOR, 46, () -> new ItemStack(Items.STONE), "enterAndrast").setBiomeAchievement();
        DO_MINIQUEST_GONDOR_KILL_RENEGADE = new LOTRAchievement(Category.GONDOR, 47, item("red_book"), "doMiniquestGondorKillRenegade").setRequiresAlly(LOTRFaction.GONDOR).createTitle("killGondorRenegade");
        MINE_REMAINS = new LOTRAchievement(Category.NINDALF, 0, item("remains"), "mineRemains");
        CRAFT_ANCIENT_ITEM = new LOTRAchievement(Category.NINDALF, 1, item("ancient_sword"), "craftAncientItem");
        ENTER_DEAD_MARSHES = new LOTRAchievement(Category.NINDALF, 2, item("dead_marsh_plant"), "enterDeadMarshes").setBiomeAchievement();
        ENTER_NINDALF = new LOTRAchievement(Category.NINDALF, 3, () -> new ItemStack(Items.FERN), "enterNindalf").setBiomeAchievement();
        KILL_MARSH_WRAITH = new LOTRAchievement(Category.NINDALF, 4, () -> new ItemStack(Items.SKELETON_SKULL), "killMarshWraith").createTitle();
        KILL_OLOG_HAI = new LOTRAchievement(Category.MORDOR, 0, item("troll_bone"), "killOlogHai").setRequiresEnemy(LOTRFaction.MORDOR).createTitle();
        USE_MORGUL_TABLE = new LOTRAchievement(Category.MORDOR, 1, item("morgul_crafting_table"), "useMorgulTable").setRequiresAlly(LOTRFaction.MORDOR);
        SMELT_ORC_STEEL = new LOTRAchievement(Category.MORDOR, 2, item("orc_steel_ingot"), "smeltOrcSteel");
        WEAR_FULL_ORC = new LOTRAchievement(Category.MORDOR, 3, item("mordor_chestplate"), "wearFullOrc");
        TRADE_ORC_TRADER = new LOTRAchievement(Category.MORDOR, 4, item("silver_coin"), "tradeOrcTrader").setRequiresAlly(LOTRFaction.MORDOR);
        TRADE_ORC_CAPTAIN = new LOTRAchievement(Category.MORDOR, 5, item("silver_coin"), "tradeOrcCaptain").setRequiresAlly(LOTRFaction.MORDOR);
        MINE_NAURITE = new LOTRAchievement(Category.MORDOR, 6, item("naurite_ore"), "mineNaurite");
        EAT_MORGUL_SHROOM = new LOTRAchievement(Category.MORDOR, 7, item("morgul_shroom"), "eatMorgulShroom");
        CRAFT_ORC_BOMB = new LOTRAchievement(Category.MORDOR, 8, item("orc_bomb"), "craftOrcBomb").setRequiresAlly(LOTRFaction.MORDOR);
        HIRE_OLOG_HAI = new LOTRAchievement(Category.MORDOR, 9, item("mordor_warhammer"), "hireOlogHai").setRequiresAlly(LOTRFaction.MORDOR);
        MINE_GULDURIL = new LOTRAchievement(Category.MORDOR, 10, item("gulduril_ore"), "mineGulduril");
        USE_MORGUL_PORTAL = new LOTRAchievement(Category.MORDOR, 11, item("gulduril"), "useMorgulPortal").setRequiresAlly(LOTRFaction.MORDOR);
        WEAR_FULL_MORGUL = new LOTRAchievement(Category.MORDOR, 12, item("morgul_chestplate"), "wearFullMorgul");
        ENTER_MORDOR = new LOTRAchievement(Category.MORDOR, 13, item("mordor_rock"), "enterMordor").setBiomeAchievement();
        ENTER_NURN = new LOTRAchievement(Category.MORDOR, 14, item("mordor_hoe"), "enterNurn").setBiomeAchievement();
        ENTER_NAN_UNGOL = new LOTRAchievement(Category.MORDOR, 15, item("web_ungoliant"), "enterNanUngol").setBiomeAchievement();
        KILL_MORDOR_SPIDER = new LOTRAchievement(Category.MORDOR, 16, () -> new ItemStack(Items.STRING), "killMordorSpider").setRequiresEnemy(LOTRFaction.MORDOR).createTitle();
        TRADE_ORC_SPIDER_KEEPER = new LOTRAchievement(Category.MORDOR, 17, item("silver_coin"), "tradeOrcSpiderKeeper").setRequiresAlly(LOTRFaction.MORDOR);
        KILL_MORDOR_ORC = new LOTRAchievement(Category.MORDOR, 18, item("orc_bone"), "killMordorOrc").setRequiresEnemy(LOTRFaction.MORDOR).createTitle();
        DO_MINIQUEST_MORDOR = new LOTRAchievement(Category.MORDOR, 22, item("red_book"), "doMiniquestMordor").setRequiresAlly(LOTRFaction.MORDOR);
        SMELT_BLACK_URUK_STEEL = new LOTRAchievement(Category.MORDOR, 23, item("black_uruk_steel_ingot"), "smeltBlackUrukSteel");
        WEAR_FULL_BLACK_URUK = new LOTRAchievement(Category.MORDOR, 24, item("black_uruk_chestplate"), "wearFullBlackUruk");
        KILL_BLACK_URUK = new LOTRAchievement(Category.MORDOR, 25, item("black_uruk_helmet"), "killBlackUruk").setRequiresEnemy(LOTRFaction.MORDOR).createTitle();
        HIRE_NURN_SLAVE = new LOTRAchievement(Category.MORDOR, 26, item("branding_iron"), "hireNurnSlave").setRequiresAlly(LOTRFaction.MORDOR);
        ENTER_MORGUL_VALE = new LOTRAchievement(Category.MORDOR, 27, () -> new ItemStack(Items.SKELETON_SKULL), "enterMorgulVale").setBiomeAchievement();
        TRADE_BLACK_URUK_CAPTAIN = new LOTRAchievement(Category.MORDOR, 28, item("silver_coin"), "tradeBlackUrukCaptain").setRequiresAlly(LOTRFaction.MORDOR);
        KILL_WICKED_DWARF = new LOTRAchievement(Category.MORDOR, 29, item("dwarven_pickaxe"), "killWickedDwarf").setRequiresEnemy(LOTRFaction.MORDOR).createTitle();
        TRADE_WICKED_DWARF = new LOTRAchievement(Category.MORDOR, 30, item("silver_coin"), "tradeWickedDwarf").setRequiresAlly(LOTRWickedDwarfEntity.getTradeFactions());
        ENTER_DORWINION = new LOTRAchievement(Category.DORWINION, 0, item("white_wine"), "enterDorwinion").setBiomeAchievement();
        DO_MINIQUEST_DORWINION = new LOTRAchievement(Category.DORWINION, 4, item("red_book"), "doMiniquestDorwinion").setRequiresAlly(LOTRFaction.DORWINION);
        USE_DORWINION_TABLE = new LOTRAchievement(Category.DORWINION, 5, item("dorwinion_crafting_table"), "useDorwinionTable").setRequiresAlly(LOTRFaction.DORWINION);
        WEAR_FULL_DORWINION = new LOTRAchievement(Category.DORWINION, 6, item("dorwinion_chestplate"), "wearFullDorwinion");
        WEAR_FULL_DORWINION_ELF = new LOTRAchievement(Category.DORWINION, 7, item("dorwinion_elven_chestplate"), "wearFullDorwinionElf");
        KILL_DORWINION = new LOTRAchievement(Category.DORWINION, 8, () -> new ItemStack(Items.BONE), "killDorwinion").setRequiresEnemy(LOTRFaction.DORWINION).createTitle();
        TRADE_DORWINION_CAPTAIN = new LOTRAchievement(Category.DORWINION, 9, item("silver_coin"), "tradeDorwinionCaptain").setRequiresAlly(LOTRFaction.DORWINION);
        KILL_DORWINION_ELF = new LOTRAchievement(Category.DORWINION, 10, item("elf_bone"), "killDorwinionElf").setRequiresEnemy(LOTRFaction.DORWINION).createTitle();
        TRADE_DORWINION_ELF_CAPTAIN = new LOTRAchievement(Category.DORWINION, 11, item("silver_coin"), "tradeDorwinionElfCaptain").setRequiresAlly(LOTRFaction.DORWINION);
        DRINK_WINE = new LOTRAchievement(Category.DORWINION, 12, item("red_wine"), "drinkWine");
        HARVEST_GRAPES = new LOTRAchievement(Category.DORWINION, 13, item("green_grapes"), "harvestGrapes");
        BUY_WINE_VINTNER = new LOTRAchievement(Category.DORWINION, 14, item("silver_coin"), "buyWineVintner").setRequiresAlly(LOTRFaction.DORWINION);
        HIRE_DORWINION_VINEKEEPER = new LOTRAchievement(Category.DORWINION, 15, item("red_grape_seeds"), "hireDorwinionVinekeeper").setRequiresAlly(LOTRFaction.DORWINION);
        STEAL_DORWINION_GRAPES = new LOTRAchievement(Category.DORWINION, 16, item("red_grapes"), "stealDorwinionGrapes").createTitle("stealGrapes");
        TRADE_DORWINION_MERCHANT = new LOTRAchievement(Category.DORWINION, 17, item("silver_coin"), "tradeDorwinionMerchant").setRequiresAlly(LOTRFaction.DORWINION);
        ENTER_DORWINION_HILLS = new LOTRAchievement(Category.DORWINION, 18, item("chalk"), "enterDorwinionHills").setBiomeAchievement();
        ENTER_RHUN = new LOTRAchievement(Category.RHUN, 0, () -> new ItemStack(Items.GRASS_BLOCK), "enterRhun").setBiomeAchievement();
        USE_RHUN_TABLE = new LOTRAchievement(Category.RHUN, 4, item("rhun_crafting_table"), "useRhunTable").setRequiresAlly(LOTRFaction.RHUDEL);
        KILL_EASTERLING = new LOTRAchievement(Category.RHUN, 5, () -> new ItemStack(Items.BONE), "killEasterling").setRequiresEnemy(LOTRFaction.RHUDEL).createTitle();
        DO_MINIQUEST_RHUN = new LOTRAchievement(Category.RHUN, 6, item("red_book"), "doMiniquestRhun").setRequiresAlly(LOTRFaction.RHUDEL);
        WEAR_FULL_RHUN = new LOTRAchievement(Category.RHUN, 7, item("rhunic_chestplate"), "wearFullRhun");
        TRADE_RHUN_BLACKSMITH = new LOTRAchievement(Category.RHUN, 8, item("silver_coin"), "tradeRhunBlacksmith").setRequiresAlly(LOTRFaction.RHUDEL);
        TRADE_RHUN_CAPTAIN = new LOTRAchievement(Category.RHUN, 9, item("silver_coin"), "tradeRhunCaptain").setRequiresAlly(LOTRFaction.RHUDEL);
        HIT_BIRD_FIRE_POT = new LOTRAchievement(Category.RHUN, 10, item("rhunic_fire_pot"), "hitBirdFirePot");
        GET_KINE_ARAW_HORN = new LOTRAchievement(Category.RHUN, 11, item("kine_of_araw_horn"), "getKineArawHorn");
        WEAR_FULL_RHUN_GOLD = new LOTRAchievement(Category.RHUN, 12, item("golden_rhunic_chestplate"), "wearFullRhunGold");
        ENTER_LAST_DESERT = new LOTRAchievement(Category.RHUN, 13, () -> new ItemStack(Items.SAND), "enterLastDesert").setBiomeAchievement();
        ENTER_MOUNTAINS_WIND = new LOTRAchievement(Category.RHUN, 14, () -> new ItemStack(Items.STONE), "enterMountainsWind").setBiomeAchievement();
        TRADE_RHUN_MARKET_TRADER = new LOTRAchievement(Category.RHUN, 15, item("silver_coin"), "tradeRhunMarketTrader").setRequiresAlly(LOTRFaction.RHUDEL);
        TRADE_RHUN_BARTENDER = new LOTRAchievement(Category.RHUN, 16, item("silver_coin"), "tradeRhunBartender").setRequiresAlly(LOTRFaction.RHUDEL);
        HIRE_RHUN_FARMER = new LOTRAchievement(Category.RHUN, 17, item("bronze_hoe"), "hireRhunFarmer").setRequiresAlly(LOTRFaction.RHUDEL);
        ENTER_RHUN_LAND = new LOTRAchievement(Category.RHUN, 18, item("golden_rhunic_helmet"), "enterRhunLand").setBiomeAchievement();
        ENTER_RHUN_REDWOOD = new LOTRAchievement(Category.RHUN, 19, item("redwood_sapling"), "enterRhunRedwood").setBiomeAchievement();
        ENTER_RHUN_ISLAND = new LOTRAchievement(Category.RHUN, 20, () -> new ItemStack(Items.OAK_BOAT), "enterRhunIsland").setBiomeAchievement();
        ENTER_RED_MOUNTAINS = new LOTRAchievement(Category.OROCARNI, 0, item("red_rock"), "enterRedMountains").setBiomeAchievement();
        ENTER_HARONDOR = new LOTRAchievement(Category.NEAR_HARAD, 0, () -> new ItemStack(Items.DIRT), "enterHarondor").setBiomeAchievement();
        ENTER_NEAR_HARAD = new LOTRAchievement(Category.NEAR_HARAD, 1, () -> new ItemStack(Items.SAND), "enterNearHarad").setBiomeAchievement();
        KILL_NEAR_HARADRIM = new LOTRAchievement(Category.NEAR_HARAD, 2, () -> new ItemStack(Items.BONE), "killNearHaradrim").setRequiresEnemy(LOTRFaction.NEAR_HARAD).createTitle();
        USE_NEAR_HARAD_TABLE = new LOTRAchievement(Category.NEAR_HARAD, 6, item("near_harad_crafting_table"), "useNearHaradTable").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        WEAR_FULL_NEAR_HARAD = new LOTRAchievement(Category.NEAR_HARAD, 7, item("coast_southron_chestplate"), "wearFullNearHarad");
        TRADE_NEAR_HARAD_WARLORD = new LOTRAchievement(Category.NEAR_HARAD, 8, item("silver_coin"), "tradeNearHaradWarlord").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        RIDE_CAMEL = new LOTRAchievement(Category.NEAR_HARAD, 9, () -> new ItemStack(Items.SADDLE), "rideCamel");
        TRADE_BAZAAR_TRADER = new LOTRAchievement(Category.NEAR_HARAD, 10, item("silver_coin"), "tradeBazaarTrader").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_NEAR_HARAD_MERCHANT = new LOTRAchievement(Category.NEAR_HARAD, 11, item("silver_coin"), "tradeNearHaradMerchant").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        DO_MINIQUEST_NEAR_HARAD = new LOTRAchievement(Category.NEAR_HARAD, 12, item("red_book"), "doMiniquestNearHarad").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        COOK_KEBAB = new LOTRAchievement(Category.NEAR_HARAD, 13, item("kebab"), "cookKebab").createTitle("cookKebab");
        ENTER_NEAR_HARAD_OASIS = new LOTRAchievement(Category.NEAR_HARAD, 14, item("date_palm_sapling"), "enterNearHaradOasis").setBiomeAchievement();
        TRADE_NEAR_HARAD_BLACKSMITH = new LOTRAchievement(Category.NEAR_HARAD, 15, item("silver_coin"), "tradeNearHaradBlacksmith").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        ENTER_HARNEDOR = new LOTRAchievement(Category.NEAR_HARAD, 16, item("haradric_sword"), "enterHarnedor").setBiomeAchievement();
        ENTER_SOUTHRON_COASTS = new LOTRAchievement(Category.NEAR_HARAD, 17, item("date_palm_sapling"), "enterSouthronCoasts").setBiomeAchievement();
        ENTER_UMBAR = new LOTRAchievement(Category.NEAR_HARAD, 18, item("umbaric_scimitar"), "enterUmbar").setBiomeAchievement();
        ENTER_LOSTLADEN = new LOTRAchievement(Category.NEAR_HARAD, 19, () -> new ItemStack(Items.STONE), "enterLostladen").setBiomeAchievement();
        ENTER_GULF_HARAD = new LOTRAchievement(Category.NEAR_HARAD, 20, item("flame_of_harad"), "enterGulfHarad").setBiomeAchievement();
        USE_UMBAR_TABLE = new LOTRAchievement(Category.NEAR_HARAD, 21, item("umbar_crafting_table"), "useUmbarTable").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        USE_GULF_TABLE = new LOTRAchievement(Category.NEAR_HARAD, 22, item("gulf_crafting_table"), "useGulfTable").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        WEAR_FULL_GULF_HARAD = new LOTRAchievement(Category.NEAR_HARAD, 23, item("gulfen_chestplate"), "wearFullGulfHarad");
        WEAR_FULL_CORSAIR = new LOTRAchievement(Category.NEAR_HARAD, 24, item("corsair_chestplate"), "wearFullCorsair");
        WEAR_FULL_UMBAR = new LOTRAchievement(Category.NEAR_HARAD, 25, item("umbaric_chestplate"), "wearFullUmbar");
        WEAR_FULL_HARNEDOR = new LOTRAchievement(Category.NEAR_HARAD, 26, item("harnennor_chestplate"), "wearFullHarnedor");
        WEAR_FULL_NOMAD = new LOTRAchievement(Category.NEAR_HARAD, 27, item("nomad_tunic"), "wearFullNomad");
        TRADE_HARNEDOR_WARLORD = new LOTRAchievement(Category.NEAR_HARAD, 28, item("silver_coin"), "tradeHarnedorWarlord").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_UMBAR_CAPTAIN = new LOTRAchievement(Category.NEAR_HARAD, 29, item("silver_coin"), "tradeUmbarCaptain").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_CORSAIR_CAPTAIN = new LOTRAchievement(Category.NEAR_HARAD, 30, item("silver_coin"), "tradeCorsairCaptain").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_NOMAD_WARLORD = new LOTRAchievement(Category.NEAR_HARAD, 31, item("silver_coin"), "tradeNomadWarlord").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_GULF_WARLORD = new LOTRAchievement(Category.NEAR_HARAD, 32, item("silver_coin"), "tradeGulfWarlord").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        HIRE_HARAD_SLAVE = new LOTRAchievement(Category.NEAR_HARAD, 33, item("branding_iron"), "hireHaradSlave").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        HIRE_MOREDAIN_MERCENARY = new LOTRAchievement(Category.NEAR_HARAD, 34, item("silver_coin"), "hireMoredainMercenary").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_HARNEDOR_BLACKSMITH = new LOTRAchievement(Category.NEAR_HARAD, 35, item("silver_coin"), "tradeHarnedorBlacksmith").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_UMBAR_BLACKSMITH = new LOTRAchievement(Category.NEAR_HARAD, 36, item("silver_coin"), "tradeUmbarBlacksmith").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_GULF_BLACKSMITH = new LOTRAchievement(Category.NEAR_HARAD, 37, item("silver_coin"), "tradeGulfBlacksmith").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        DO_MINIQUEST_GONDOR_RENEGADE = new LOTRAchievement(Category.NEAR_HARAD, 38, item("red_book"), "doMiniquestGondorRenegade").setRequiresAlly(LOTRFaction.NEAR_HARAD).createTitle("gondorRenegade");
        TRADE_NOMAD_MERCHANT = new LOTRAchievement(Category.NEAR_HARAD, 39, item("silver_coin"), "tradeNomadMerchant").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_HARAD_BARTENDER = new LOTRAchievement(Category.NEAR_HARAD, 40, item("silver_coin"), "tradeHaradBartender").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_NOMAD_ARMOURER = new LOTRAchievement(Category.NEAR_HARAD, 41, item("silver_coin"), "tradeNomadArmourer").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        HIRE_HARNEDOR_FARMER = new LOTRAchievement(Category.NEAR_HARAD, 42, item("bronze_hoe"), "hireHarnedorFarmer").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        TRADE_HARAD_FARMER = new LOTRAchievement(Category.NEAR_HARAD, 43, item("silver_coin"), "tradeHaradFarmer").setRequiresAlly(LOTRFaction.NEAR_HARAD);
        WEAR_FULL_BLACK_NUMENOREAN = new LOTRAchievement(Category.NEAR_HARAD, 44, item("black_numenorean_chestplate"), "wearFullBlackNumenorean");
        ENTER_FAR_HARAD_SAVANNAH = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 0, item("lion_fur"), "enterFarHaradSavannah").setBiomeAchievement();
        PICK_BANANA = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 1, item("banana"), "pickBanana");
        GROW_BAOBAB = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 5, item("baobab_sapling"), "growBaobab");
        ENTER_FAR_HARAD_VOLCANO = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 6, () -> new ItemStack(Items.LAVA_BUCKET), "enterFarHaradVolcano").setBiomeAchievement();
        KILL_MOREDAIN = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 7, () -> new ItemStack(Items.BONE), "killMoredain").setRequiresEnemy(LOTRFaction.MORWAITH).createTitle();
        USE_MOREDAIN_TABLE = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 8, item("moredain_crafting_table"), "useMoredainTable").setRequiresAlly(LOTRFaction.MORWAITH);
        WEAR_FULL_MOREDAIN = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 9, item("morwaith_chestplate"), "wearFullMoredain");
        TRADE_MOREDAIN_CHIEFTAIN = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 10, item("silver_coin"), "tradeMoredainChieftain").setRequiresAlly(LOTRFaction.MORWAITH);
        DO_MINIQUEST_MOREDAIN = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 11, item("red_book"), "doMiniquestMoredain").setRequiresAlly(LOTRFaction.MORWAITH);
        TRADE_MOREDAIN_VILLAGER = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 12, item("silver_coin"), "tradeMoredainVillager").setRequiresAlly(LOTRFaction.MORWAITH);
        ENTER_CORSAIR_COASTS = new LOTRAchievement(Category.FAR_HARAD_SAVANNAH, 13, item("corsair_eket"), "enterCorsairCoasts").setBiomeAchievement();
        ENTER_FAR_HARAD_JUNGLE = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 0, () -> new ItemStack(Items.JUNGLE_SAPLING), "enterFarHaradJungle").setBiomeAchievement();
        DRINK_MANGO_JUICE = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 1, item("mango_juice"), "drinkMangoJuice");
        DO_MINIQUEST_TAUREDAIN = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 5, item("red_book"), "doMiniquestTauredain").setRequiresAlly(LOTRFaction.TAURETHRIM);
        USE_TAUREDAIN_TABLE = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 6, item("tauredain_crafting_table"), "useTauredainTable").setRequiresAlly(LOTRFaction.TAURETHRIM);
        WEAR_FULL_TAUREDAIN = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 7, item("taurethrim_chestplate"), "wearFullTauredain");
        KILL_TAUREDAIN = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 8, () -> new ItemStack(Items.BONE), "killTauredain").setRequiresEnemy(LOTRFaction.TAURETHRIM).createTitle();
        TRADE_TAUREDAIN_CHIEFTAIN = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 9, item("silver_coin"), "tradeTauredainChieftain").setRequiresAlly(LOTRFaction.TAURETHRIM);
        TRADE_TAUREDAIN_SHAMAN = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 10, item("silver_coin"), "tradeTauredainShaman").setRequiresAlly(LOTRFaction.TAURETHRIM);
        SMELT_OBSIDIAN_SHARD = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 11, item("obsidian_shard"), "smeltObsidianShard");
        TRADE_TAUREDAIN_FARMER = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 12, item("silver_coin"), "tradeTauredainFarmer").setRequiresAlly(LOTRFaction.TAURETHRIM);
        HIRE_TAUREDAIN_FARMER = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 13, item("taurethrim_hoe"), "hireTauredainFarmer").setRequiresAlly(LOTRFaction.TAURETHRIM);
        TRADE_TAUREDAIN_SMITH = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 14, item("silver_coin"), "tradeTauredainSmith").setRequiresAlly(LOTRFaction.TAURETHRIM);
        WEAR_FULL_TAURETHRIM_GOLD = new LOTRAchievement(Category.FAR_HARAD_JUNGLE, 15, item("golden_taurethrim_chestplate"), "wearFullTaurethrimGold");
        ENTER_PERTOROGWAITH = new LOTRAchievement(Category.PERDOROGWAITH, 0, () -> new ItemStack(Items.STONE), "enterPertorogwaith").setBiomeAchievement();
        KILL_HALF_TROLL = new LOTRAchievement(Category.PERDOROGWAITH, 4, item("troll_bone"), "killHalfTroll").setRequiresEnemy(LOTRFaction.HALF_TROLL).createTitle();
        WEAR_FULL_HALF_TROLL = new LOTRAchievement(Category.PERDOROGWAITH, 5, item("half_troll_chestplate"), "wearFullHalfTroll");
        TRADE_HALF_TROLL_WARLORD = new LOTRAchievement(Category.PERDOROGWAITH, 6, item("silver_coin"), "tradeHalfTrollWarlord").setRequiresAlly(LOTRFaction.HALF_TROLL);
        USE_HALF_TROLL_TABLE = new LOTRAchievement(Category.PERDOROGWAITH, 7, item("half_troll_crafting_table"), "useHalfTrollTable").setRequiresAlly(LOTRFaction.HALF_TROLL);
        DO_MINIQUEST_HALF_TROLL = new LOTRAchievement(Category.PERDOROGWAITH, 8, item("red_book"), "doMiniquestHalfTroll").setRequiresAlly(LOTRFaction.HALF_TROLL);
        TRADE_HALF_TROLL_SCAVENGER = new LOTRAchievement(Category.PERDOROGWAITH, 9, item("silver_coin"), "tradeHalfTrollScavenger").setRequiresAlly(LOTRFaction.HALF_TROLL);
        ENTER_HALF_TROLL_FOREST = new LOTRAchievement(Category.PERDOROGWAITH, 10, item("tauredain_cracked_brick"), "enterHalfTrollForest").setBiomeAchievement();
        ENTER_OCEAN = new LOTRAchievement(Category.OCEAN, 0, () -> new ItemStack(Items.OAK_BOAT), "enterOcean").setBiomeAchievement().createTitle("visitOcean");
        ENTER_MENELTARMA = new LOTRAchievement(Category.OCEAN, 1, item("athelas"), "enterMeneltarma").setBiomeAchievement().createTitle("enterMeneltarma");
        ENTER_UTUMNO_ICE = new LOTRAchievement(Category.UTUMNO, 0, item("glowing_ice_utumno_brick"), "enterUtumnoIce").setSpecial().createTitle("enterUtumno");
        ENTER_UTUMNO_OBSIDIAN = new LOTRAchievement(Category.UTUMNO, 1, item("utumno_obsidian_fire_brick"), "enterUtumnoObsidian").setSpecial();
        ENTER_UTUMNO_FIRE = new LOTRAchievement(Category.UTUMNO, 2, item("burning_utumno_brick"), "enterUtumnoFire").setSpecial().createTitle("enterUtumnoFire");
        WEAR_FULL_UTUMNO = new LOTRAchievement(Category.UTUMNO, 3, item("utumno_chestplate"), "wearFullUtumno");
        KILL_UTUMNO_ORC = new LOTRAchievement(Category.UTUMNO, 4, item("orc_bone"), "killUtumnoOrc");
        KILL_UTUMNO_WARG = new LOTRAchievement(Category.UTUMNO, 5, item("warg_bone"), "killUtumnoWarg");
        KILL_BALROG = new LOTRAchievement(Category.UTUMNO, 6, item("balrog_whip"), "killBalrog").createTitle();
        KILL_TORMENTED_ELF = new LOTRAchievement(Category.UTUMNO, 7, item("elf_bone"), "killTormentedElf");
        KILL_UTUMNO_TROLL = new LOTRAchievement(Category.UTUMNO, 8, item("troll_bone"), "killUtumnoTroll");
        CRAFT_UTUMNO_KEY = new LOTRAchievement(Category.UTUMNO, 9, item("key_of_ice"), "craftUtumnoKey");
        LEAVE_UTUMNO = new LOTRAchievement(Category.UTUMNO, 10, item("utumno_sword"), "leaveUtumno").createTitle();
    }

    public static @Nullable LOTRAchievement achievementForCategoryAndID(@Nullable Category category, int id) {
        if (category == null) {
            return null;
        }
        for (LOTRAchievement achievement : category.list) {
            if (achievement.ID == id) {
                return achievement;
            }
        }
        return null;
    }

    public static @Nullable LOTRAchievement findByName(String name) {
        for (Category category : Category.values()) {
            for (LOTRAchievement achievement : category.list) {
                if (achievement.name.equalsIgnoreCase(name)) {
                    return achievement;
                }
            }
        }
        return null;
    }

    public static List<LOTRAchievement> getAllAchievements() {
        List<LOTRAchievement> list = new ArrayList<>();
        for (Category category : Category.values()) {
            list.addAll(category.list);
        }
        return list;
    }

    /** sortForDisplay: the special first (by number), then the lands entered, then the rest, by title. */
    public static Comparator<LOTRAchievement> sortForDisplay(Player player) {
        return (ach1, ach2) -> {
            if (ach1.isSpecial) {
                return ach2.isSpecial ? Integer.compare(ach1.ID, ach2.ID) : -1;
            }
            if (ach2.isSpecial) {
                return 1;
            }
            if (ach1.isBiomeAchievement != ach2.isBiomeAchievement) {
                return ach1.isBiomeAchievement ? -1 : 1;
            }
            return ach1.getTitle(player).getString().compareTo(ach2.getTitle(player).getString());
        };
    }

    public ItemStack getIcon() {
        if (this.iconStack == null) {
            this.iconStack = this.icon.get();
        }
        return this.iconStack;
    }

    public LOTRDimension getDimension() {
        return this.category.dimension;
    }

    public String getCodeName() {
        return this.name;
    }

    /**
     * canPlayerEarn: an enemy (alignment not above zero) of at least one of its enemy factions, and a
     * friend (not below zero) of at least one of its ally factions.
     */
    public boolean canPlayerEarn(Player player) {
        if (!this.enemyFactions.isEmpty()) {
            boolean anyEnemies = false;
            for (LOTRFaction faction : this.enemyFactions) {
                if (LOTRPlayerAlignments.getAlignment(player, faction) <= 0.0f) {
                    anyEnemies = true;
                }
            }
            if (!anyEnemies) {
                return false;
            }
        }
        if (!this.allyFactions.isEmpty()) {
            for (LOTRFaction faction : this.allyFactions) {
                if (LOTRPlayerAlignments.getAlignment(player, faction) >= 0.0f) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public String getUntranslatedTitle(Player player) {
        return "lotr.achievement." + this.name + ".title";
    }

    public Component getTitle(Player player) {
        return Component.translatable(getUntranslatedTitle(player));
    }

    public Component getDescription(Player player) {
        return Component.translatable("lotr.achievement." + this.name + ".desc");
    }

    /** getAchievementChatComponent: its title in yellow, its details on hover (LOTRChatEvents.SHOW_LOTR_ACHIEVEMENT). */
    public MutableComponent getAchievementChatComponent(Player player) {
        MutableComponent hover = Component.translatable("lotr.gui.achievements.hover.name",
                        getTitle(player).copy().withStyle(ChatFormatting.YELLOW))
                .append("\n")
                .append(Component.translatable("lotr.gui.achievements.hover.subtitle", getDimension().getDimensionName(),
                        this.category.getDisplayName()).withStyle(ChatFormatting.ITALIC))
                .append("\n")
                .append(getDescription(player));
        return getTitle(player).copy().withStyle(style -> style.withColor(ChatFormatting.YELLOW)
                .withHoverEvent(new HoverEvent.ShowText(hover)));
    }

    /** getChatComponentForEarn: "[Title]". */
    public MutableComponent getChatComponentForEarn(Player player) {
        MutableComponent base = getAchievementChatComponent(player);
        return Component.literal("[").append(base).append("]").withStyle(base.getStyle());
    }

    /** broadcastEarning: "X has just earned the Middle-earth achievement [Title]" -- but never Hobbit Slayer, if protected. */
    public void broadcastEarning(Player player, MinecraftServer server) {
        if (LOTRConfig.protectHobbitKillers && this == KILL_HOBBIT) {
            return;
        }
        Component msg = Component.translatable("chat.lotr.achievement", player.getDisplayName(),
                getDimension().getDimensionName(), getChatComponentForEarn(player));
        server.getPlayerList().broadcastSystemMessage(msg, false);
    }

    public @Nullable LOTRTitle getAchievementTitle() {
        return this.achievementTitle;
    }

    public LOTRAchievement createTitle() {
        return createTitle(null);
    }

    public LOTRAchievement createTitle(@Nullable String s) {
        if (this.achievementTitle != null) {
            throw new IllegalArgumentException("LOTR achievement " + this.name + " already has an associated title!");
        }
        this.achievementTitle = new LOTRTitle(s, this);
        return this;
    }

    public LOTRAchievement setBiomeAchievement() {
        this.isBiomeAchievement = true;
        return this;
    }

    public LOTRAchievement setRequiresAlly(LOTRFaction... factions) {
        this.allyFactions.addAll(Arrays.asList(factions));
        return this;
    }

    public LOTRAchievement setRequiresAnyAlly(Collection<LOTRFaction> factions) {
        this.allyFactions.addAll(factions);
        return this;
    }

    public LOTRAchievement setRequiresAnyEnemy(Collection<LOTRFaction> factions) {
        this.enemyFactions.addAll(factions);
        return this;
    }

    public LOTRAchievement setRequiresEnemy(LOTRFaction... factions) {
        this.enemyFactions.addAll(Arrays.asList(factions));
        return this;
    }

    public LOTRAchievement setSpecial() {
        this.isSpecial = true;
        return this;
    }
}
