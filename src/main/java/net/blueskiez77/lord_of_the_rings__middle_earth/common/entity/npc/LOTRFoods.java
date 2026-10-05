package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * LOTRFoods: what each people eats and drinks -- LOTREntityAIEat and
 * LOTREntityAIDrink pick from these. A drink is served in one of the
 * people's vessels. Each list arrives with its NPCs.
 */
public final class LOTRFoods {

    /** Saruman's: logs. */
    public static final LOTRFoods SARUMAN = new LOTRFoods(List.of(() -> Items.OAK_LOG));

    public static final LOTRFoods HOBBIT = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.GAMMON, () -> Items.BAKED_POTATO, () -> Items.APPLE,
            () -> LOTRFoodItems.GREEN_APPLE, () -> Items.BREAD, () -> LOTRFoodItems.CORN_BREAD, () -> Items.CARROT,
            () -> LOTRFoodItems.LETTUCE, () -> LOTRFoodItems.LEEK, () -> LOTRFoodItems.LEEK_SOUP, () -> Items.MUSHROOM_STEW,
            () -> LOTRFoodItems.TURNIP, () -> LOTRFoodItems.ROAST_TURNIP, () -> Items.PUMPKIN_PIE,
            () -> LOTRFoodItems.MUSHROOM_PIE, () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.CHERRIES,
            () -> LOTRFoodItems.PLUM, () -> Items.COOKIE, () -> LOTRFoodItems.HOBBIT_PANCAKE, () -> Items.COOKED_RABBIT,
            () -> LOTRFoodItems.BLUEBERRIES, () -> LOTRFoodItems.BLACKBERRIES, () -> LOTRFoodItems.RASPBERRIES,
            () -> LOTRFoodItems.CRANBERRIES, () -> LOTRFoodItems.ELDERBERRIES, () -> LOTRFoodItems.ROAST_CHESTNUT,
            () -> LOTRFoodItems.COOKED_CORN, () -> LOTRFoodItems.MARCHPANE, () -> LOTRFoodItems.CHOCOLATE_MARCHPANE));

    public static final LOTRFoods HOBBIT_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY,
            () -> LOTRFoodItems.CHERRY_LIQUEUR, () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE);

    public static final LOTRFoods BREE = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> Items.COOKED_COD,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> Items.BAKED_POTATO, () -> Items.APPLE,
            () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.PLUM, () -> Items.BREAD,
            () -> Items.COOKED_RABBIT, () -> LOTRFoodItems.RABBIT_STEW, () -> LOTRFoodItems.COOKED_VENISON,
            () -> LOTRFoodItems.TURNIP, () -> LOTRFoodItems.ROAST_TURNIP, () -> Items.CARROT,
            () -> LOTRFoodItems.LETTUCE, () -> LOTRFoodItems.LEEK, () -> LOTRFoodItems.LEEK_SOUP,
            () -> Items.MUSHROOM_STEW, () -> LOTRFoodItems.MUSHROOM_PIE));

    public static final LOTRFoods BREE_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER,
            () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY, () -> LOTRFoodItems.MEAD,
            () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD);

    public static final LOTRFoods DALE = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> Items.BAKED_POTATO, () -> Items.APPLE,
            () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.PLUM, () -> Items.BREAD,
            () -> Items.COOKED_RABBIT, () -> LOTRFoodItems.COOKED_VENISON, () -> LOTRFoodItems.CRAM,
            () -> LOTRFoodItems.CRAM, () -> LOTRFoodItems.CRAM, () -> LOTRFoodItems.CRAM, () -> LOTRFoodItems.MARCHPANE,
            () -> LOTRFoodItems.CHOCOLATE_MARCHPANE));

    public static final LOTRFoods DALE_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY,
            () -> LOTRFoodItems.VODKA, () -> LOTRFoodItems.PLUM_KVASS, () -> LOTRFoodItems.DWARVEN_ALE,
            () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.WHITE_WINE, () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER,
                    LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE, LOTRVessel.HORN);

    public static final LOTRFoods RANGER = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> Items.COOKED_COD,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.COOKED_VENISON, () -> Items.BAKED_POTATO,
            () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> Items.BREAD,
            () -> LOTRFoodItems.BLUEBERRIES, () -> LOTRFoodItems.BLACKBERRIES, () -> LOTRFoodItems.CRANBERRIES,
            () -> LOTRFoodItems.RASPBERRIES, () -> LOTRFoodItems.ELDERBERRIES));

    public static final LOTRFoods RANGER_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY,
            () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE, LOTRVessel.SKIN, LOTRVessel.HORN);

    public static final LOTRFoods ROHAN = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> Items.BAKED_POTATO, () -> Items.APPLE,
            () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> Items.BREAD, () -> Items.COOKED_RABBIT,
            () -> LOTRFoodItems.BLUEBERRIES, () -> LOTRFoodItems.BLACKBERRIES));

    public static final LOTRFoods ROHAN_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.ALE,
            () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY, () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER,
                    LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD, LOTRVessel.HORN, LOTRVessel.HORN_GOLD);

    public static final LOTRFoods GONDOR = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> Items.COOKED_COD,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.COOKED_VENISON, () -> Items.BAKED_POTATO,
            () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.OLIVES,
            () -> LOTRFoodItems.PLUM, () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> LOTRFoodItems.BLUEBERRIES,
            () -> LOTRFoodItems.BLACKBERRIES, () -> LOTRFoodItems.CRANBERRIES));

    public static final LOTRFoods GONDOR_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.MEAD,
            () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.PERRY, () -> LOTRFoodItems.APPLE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER,
                    LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE, LOTRVessel.HORN);

    public static final LOTRFoods ELF = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR,
            () -> LOTRFoodItems.PLUM, () -> LOTRFoodItems.LETTUCE, () -> Items.CARROT, () -> LOTRFoodItems.LEMBAS,
            () -> LOTRFoodItems.LEMBAS, () -> LOTRFoodItems.LEMBAS, () -> LOTRFoodItems.ROAST_CHESTNUT));

    public static final LOTRFoods ELF_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.MIRUVOR, () -> LOTRFoodItems.MIRUVOR, () -> LOTRFoodItems.MIRUVOR,
            () -> LOTRFoodItems.APPLE_JUICE, () -> LOTRFoodItems.BLUEBERRY_JUICE, () -> LOTRFoodItems.BLACKBERRY_JUICE,
            () -> LOTRFoodItems.ELDERBERRY_JUICE, () -> LOTRFoodItems.RASPBERRY_JUICE,
            () -> LOTRFoodItems.CRANBERRY_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER,
                    LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD, LOTRVessel.GLASS, LOTRVessel.BOTTLE);

    public static final LOTRFoods WOOD_ELF_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.RED_WINE,
            () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.WHITE_WINE, () -> LOTRFoodItems.WHITE_WINE,
            () -> LOTRFoodItems.APPLE_JUICE, () -> LOTRFoodItems.BLUEBERRY_JUICE, () -> LOTRFoodItems.BLACKBERRY_JUICE,
            () -> LOTRFoodItems.ELDERBERRY_JUICE, () -> LOTRFoodItems.RASPBERRY_JUICE,
            () -> LOTRFoodItems.CRANBERRY_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE);

    public static final LOTRFoods DORWINION = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> Items.CARROT, () -> LOTRFoodItems.LETTUCE,
            () -> LOTRFoodItems.TURNIP, () -> LOTRFoodItems.ROAST_TURNIP, () -> Items.COOKED_RABBIT,
            () -> LOTRFoodItems.COOKED_VENISON, () -> LOTRFoodItems.OLIVES, () -> LOTRFoodItems.PLUM,
            () -> LOTRFoodItems.ALMOND, () -> LOTRFoodItems.MARCHPANE, () -> LOTRFoodItems.CHOCOLATE_MARCHPANE,
            () -> Items.COOKED_COD, () -> LOTRFoodItems.RED_GRAPES, () -> LOTRFoodItems.GREEN_GRAPES,
            () -> LOTRFoodItems.RAISINS));

    public static final LOTRFoods DORWINION_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.WHITE_WINE, () -> LOTRFoodItems.RED_GRAPE_JUICE,
            () -> LOTRFoodItems.GREEN_GRAPE_JUICE))
            .setDrinkVessels(LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER, LOTRVessel.GLASS, LOTRVessel.BOTTLE);

    public static final LOTRFoods DWARF = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.GAMMON, () -> Items.BREAD, () -> LOTRFoodItems.CRAM,
            () -> LOTRFoodItems.CRAM, () -> LOTRFoodItems.CRAM));

    public static final LOTRFoods BLUE_DWARF = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.GAMMON, () -> Items.BREAD));

    public static final LOTRFoods DWARF_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.DWARVEN_ALE, () -> LOTRFoodItems.DWARVEN_ALE, () -> LOTRFoodItems.DWARVEN_ALE,
            () -> LOTRFoodItems.DWARVEN_ALE, () -> LOTRFoodItems.DWARVEN_TONIC))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_WOOD, LOTRVessel.HORN);

    public static final LOTRFoods ORC = new LOTRFoods(List.of(() -> LOTRFoodItems.MAGGOTY_BREAD));

    public static final LOTRFoods ORC_DRINK = new LOTRFoods(List.of(() -> LOTRFoodItems.ORC_DRAUGHT))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.SKULL, LOTRVessel.SKIN);

    public static final LOTRFoods RHUDAUR = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> Items.BREAD, () -> Items.COOKED_RABBIT, () -> Items.ROTTEN_FLESH));

    public static final LOTRFoods RHUDAUR_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.ORC_DRAUGHT))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.GOBLET_WOOD, LOTRVessel.SKULL, LOTRVessel.SKIN, LOTRVessel.HORN);

    public static final LOTRFoods DUNLENDING = new LOTRFoods(List.of(
            () -> Items.COOKED_PORKCHOP, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRFoodItems.GAMMON, () -> Items.BAKED_POTATO, () -> Items.APPLE,
            () -> LOTRFoodItems.GREEN_APPLE, () -> LOTRFoodItems.PEAR, () -> Items.BREAD, () -> Items.COOKED_RABBIT,
            () -> LOTRFoodItems.ROAST_CHESTNUT));

    public static final LOTRFoods DUNLENDING_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.MEAD, () -> LOTRFoodItems.CIDER,
            () -> LOTRFoodItems.RUM))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.GOBLET_WOOD, LOTRVessel.SKULL, LOTRVessel.SKIN, LOTRVessel.HORN);

    public static final LOTRFoods RHUN = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> Items.CARROT, () -> LOTRFoodItems.LETTUCE,
            () -> LOTRFoodItems.TURNIP, () -> LOTRFoodItems.ROAST_TURNIP, () -> Items.COOKED_RABBIT,
            () -> LOTRFoodItems.COOKED_VENISON, () -> LOTRFoodItems.OLIVES, () -> LOTRFoodItems.PLUM,
            () -> LOTRFoodItems.ALMOND, () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF,
            () -> Items.COOKED_PORKCHOP, () -> LOTRFoodItems.RED_GRAPES, () -> LOTRFoodItems.GREEN_GRAPES,
            () -> LOTRFoodItems.RAISINS, () -> LOTRFoodItems.DATE, () -> LOTRFoodItems.POMEGRANATE));

    public static final LOTRFoods RHUN_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.SOURED_MILK, () -> LOTRFoodItems.SOURED_MILK, () -> LOTRFoodItems.SOURED_MILK,
            () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ALE,
            () -> LOTRFoodItems.RED_WINE, () -> LOTRFoodItems.WHITE_WINE, () -> LOTRFoodItems.RED_GRAPE_JUICE,
            () -> LOTRFoodItems.GREEN_GRAPE_JUICE, () -> LOTRFoodItems.POMEGRANATE_WINE,
            () -> LOTRFoodItems.POMEGRANATE_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_SILVER,
                    LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD, LOTRVessel.GLASS, LOTRVessel.BOTTLE, LOTRVessel.SKIN);

    public static final LOTRFoods SOUTHRON = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE,
            () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.DATE, () -> LOTRFoodItems.LEMON, () -> LOTRFoodItems.ORANGE,
            () -> LOTRFoodItems.LIME, () -> LOTRFoodItems.OLIVES, () -> LOTRFoodItems.ALMOND, () -> LOTRFoodItems.PLUM,
            () -> Items.CARROT, () -> Items.BAKED_POTATO, () -> LOTRFoodItems.LETTUCE, () -> Items.COOKED_PORKCHOP,
            () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> LOTRFoodItems.COOKED_MUTTON,
            () -> LOTRItems.KEBAB, () -> LOTRFoodItems.SHISH_KEBAB, () -> LOTRFoodItems.COOKED_CAMEL));

    public static final LOTRFoods SOUTHRON_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK,
            () -> LOTRFoodItems.CACTUS_LIQUEUR, () -> LOTRFoodItems.ORANGE_JUICE, () -> LOTRFoodItems.LEMON_LIQUEUR,
            () -> LOTRFoodItems.LEMONADE, () -> LOTRFoodItems.LIME_LIQUEUR, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_GOLD,
                    LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE, LOTRVessel.SKIN);

    public static final LOTRFoods CORSAIR = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> LOTRFoodItems.DATE, () -> Items.COOKED_COD,
            () -> Items.COOKED_COD, () -> LOTRItems.KEBAB, () -> LOTRFoodItems.SHISH_KEBAB));

    public static final LOTRFoods CORSAIR_DRINK = new LOTRFoods(List.of(() -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.RUM))
            .setDrinkVessels(LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE,
                    LOTRVessel.SKIN, LOTRVessel.SKULL);

    public static final LOTRFoods NOMAD = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> LOTRFoodItems.DATE, () -> Items.COOKED_BEEF,
            () -> LOTRFoodItems.COOKED_MUTTON, () -> LOTRItems.KEBAB, () -> LOTRFoodItems.SHISH_KEBAB,
            () -> LOTRFoodItems.COOKED_CAMEL, () -> LOTRFoodItems.COOKED_CAMEL, () -> LOTRFoodItems.COOKED_CAMEL,
            () -> LOTRFoodItems.COOKED_CAMEL, () -> LOTRFoodItems.SUSPICIOUS_MEAT));

    public static final LOTRFoods NOMAD_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.ARAK,
            () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.CACTUS_LIQUEUR, () -> LOTRFoodItems.ALE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD,
                    LOTRVessel.SKIN);

    public static final LOTRFoods GULF_HARAD = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE,
            () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.DATE, () -> LOTRFoodItems.OLIVES, () -> LOTRFoodItems.PLUM,
            () -> Items.CARROT, () -> Items.BAKED_POTATO, () -> LOTRFoodItems.ORANGE, () -> Items.COOKED_PORKCHOP,
            () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> LOTRFoodItems.COOKED_MUTTON,
            () -> LOTRItems.KEBAB, () -> LOTRFoodItems.SHISH_KEBAB, () -> LOTRFoodItems.COOKED_CAMEL,
            () -> LOTRFoodItems.SUSPICIOUS_MEAT, () -> LOTRFoodItems.SUSPICIOUS_MEAT, () -> LOTRFoodItems.SUSPICIOUS_MEAT));

    public static final LOTRFoods GULF_HARAD_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK,
            () -> LOTRFoodItems.CACTUS_LIQUEUR, () -> LOTRFoodItems.ORANGE_JUICE, () -> LOTRFoodItems.ALE,
            () -> LOTRFoodItems.CIDER, () -> LOTRFoodItems.BANANA_BEER, () -> LOTRFoodItems.MANGO_JUICE))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_GOLD,
                    LOTRVessel.GOBLET_WOOD, LOTRVessel.SKIN, LOTRVessel.SKULL, LOTRVessel.SKULL, LOTRVessel.SKULL);

    public static final LOTRFoods MOREDAIN = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.BANANA_BREAD, () -> LOTRFoodItems.BANANA, () -> LOTRFoodItems.MANGO,
            () -> Items.MELON_SLICE, () -> LOTRFoodItems.COOKED_LION, () -> LOTRFoodItems.COOKED_ZEBRA,
            () -> LOTRFoodItems.COOKED_RHINO, () -> LOTRFoodItems.ROAST_YAM));

    public static final LOTRFoods MOREDAIN_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.MANGO_JUICE, () -> LOTRFoodItems.BANANA_BEER, () -> LOTRFoodItems.MELON_LIQUEUR))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_WOOD, LOTRVessel.GOBLET_GOLD,
                    LOTRVessel.SKIN);

    public static final LOTRFoods TAUREDAIN = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.BANANA_BREAD, () -> LOTRFoodItems.CORN_BREAD, () -> LOTRFoodItems.CORN,
            () -> LOTRFoodItems.COOKED_CORN, () -> Items.BAKED_POTATO, () -> LOTRFoodItems.BANANA, () -> LOTRFoodItems.MANGO,
            () -> Items.MELON_SLICE, () -> LOTRFoodItems.MELON_SOUP, () -> Items.COOKED_COD));

    public static final LOTRFoods TAUREDAIN_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.CHOCOLATE, () -> LOTRFoodItems.MANGO_JUICE, () -> LOTRFoodItems.BANANA_BEER,
            () -> LOTRFoodItems.MELON_LIQUEUR, () -> LOTRFoodItems.CORN_LIQUOR))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.GOBLET_GOLD, LOTRVessel.GOBLET_WOOD);

    public static final LOTRFoods HALF_TROLL = new LOTRFoods(List.of(
            () -> LOTRFoodItems.MAGGOTY_BREAD, () -> Items.ROTTEN_FLESH, () -> LOTRFoodItems.RAW_LION,
            () -> LOTRFoodItems.RAW_ZEBRA, () -> LOTRFoodItems.RAW_RHINO, () -> LOTRFoodItems.TOROG_STEW));

    public static final LOTRFoods HALF_TROLL_DRINK = new LOTRFoods(List.of(() -> LOTRFoodItems.TOROG_DRAUGHT))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.SKULL, LOTRVessel.SKIN);

    public static final LOTRFoods NURN_SLAVE = new LOTRFoods(List.of(() -> LOTRFoodItems.MAGGOTY_BREAD));

    public static final LOTRFoods NURN_SLAVE_DRINK = new LOTRFoods(List.of(() -> LOTRFoodItems.WATER, () -> LOTRFoodItems.ORC_DRAUGHT))
            .setDrinkVessels(LOTRVessel.SKIN);

    public static final LOTRFoods HARAD_SLAVE = new LOTRFoods(List.of(() -> Items.BREAD, () -> LOTRFoodItems.DATE,
            () -> LOTRItems.KEBAB));

    public static final LOTRFoods HARAD_SLAVE_DRINK = new LOTRFoods(List.of(() -> LOTRFoodItems.WATER))
            .setDrinkVessels(LOTRVessel.SKIN);

    public static final LOTRFoods HARNEDOR = new LOTRFoods(List.of(
            () -> Items.BREAD, () -> LOTRFoodItems.OLIVE_BREAD, () -> Items.APPLE, () -> LOTRFoodItems.GREEN_APPLE,
            () -> LOTRFoodItems.PEAR, () -> LOTRFoodItems.DATE, () -> LOTRFoodItems.OLIVES, () -> LOTRFoodItems.PLUM,
            () -> Items.CARROT, () -> Items.BAKED_POTATO, () -> LOTRFoodItems.LETTUCE, () -> Items.COOKED_PORKCHOP,
            () -> Items.COOKED_COD, () -> Items.COOKED_CHICKEN, () -> Items.COOKED_BEEF, () -> LOTRFoodItems.COOKED_MUTTON,
            () -> LOTRItems.KEBAB, () -> LOTRFoodItems.SHISH_KEBAB, () -> LOTRFoodItems.COOKED_CAMEL));

    public static final LOTRFoods HARNEDOR_DRINK = new LOTRFoods(List.of(
            () -> LOTRFoodItems.WATER, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK, () -> LOTRFoodItems.ARAK,
            () -> LOTRFoodItems.CACTUS_LIQUEUR, () -> LOTRFoodItems.ORANGE_JUICE, () -> LOTRFoodItems.ALE, () -> LOTRFoodItems.CIDER))
            .setDrinkVessels(LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_COPPER, LOTRVessel.GOBLET_WOOD,
                    LOTRVessel.SKIN);

    private final List<Supplier<Item>> foodList;
    private LOTRVessel[] drinkVessels = {LOTRVessel.MUG};

    private LOTRFoods(List<Supplier<Item>> foods) {
        this.foodList = foods;
    }

    private LOTRFoods setDrinkVessels(LOTRVessel... vessels) {
        this.drinkVessels = vessels;
        return this;
    }

    public LOTRVessel[] getDrinkVessels() {
        return this.drinkVessels;
    }

    public LOTRVessel getRandomVessel(RandomSource random) {
        return this.drinkVessels[random.nextInt(this.drinkVessels.length)];
    }

    /** getPlaceableDrinkVessels: the vessels that can be set down, or a mug if none can. */
    public LOTRVessel[] getPlaceableDrinkVessels() {
        LOTRVessel[] placeable = java.util.Arrays.stream(this.drinkVessels).filter(LOTRVessel::canPlace)
                .toArray(LOTRVessel[]::new);
        return placeable.length == 0 ? new LOTRVessel[]{LOTRVessel.MUG} : placeable;
    }

    /** getRandomFoodForPlate: one of the list that leaves nothing behind (no bowl), for a plate. */
    public ItemStack getRandomFoodForPlate(RandomSource random) {
        List<Item> foods = new java.util.ArrayList<>();
        for (Supplier<Item> food : this.foodList) {
            if (food.get().getCraftingRemainder() == null) {
                foods.add(food.get());
            }
        }
        return new ItemStack(foods.get(random.nextInt(foods.size())));
    }

    /** getRandomPlaceableDrink: one of the list, a drink in a vessel that can be set down. */
    public ItemStack getRandomPlaceableDrink(RandomSource random) {
        ItemStack food = new ItemStack(this.foodList.get(random.nextInt(this.foodList.size())).get());
        if (food.getItem() instanceof LOTRDrinkItem) {
            LOTRVessel[] vessels = getPlaceableDrinkVessels();
            food.set(LOTRDataComponents.VESSEL, vessels[random.nextInt(vessels.length)]);
        }
        return food;
    }

    /** getRandomBrewableDrink: one of the list's brewable drinks, in one of the vessels. */
    public ItemStack getRandomBrewableDrink(RandomSource random) {
        List<Item> alcohols = new java.util.ArrayList<>();
        for (Supplier<Item> food : this.foodList) {
            if (food.get() instanceof LOTRDrinkItem drink && drink.isBrewable()) {
                alcohols.add(food.get());
            }
        }
        ItemStack drink = new ItemStack(alcohols.get(random.nextInt(alcohols.size())));
        drink.set(LOTRDataComponents.VESSEL, this.drinkVessels[random.nextInt(this.drinkVessels.length)]);
        return drink;
    }

    /** getRandomFood: one of the list, a drink in one of the vessels. */
    public ItemStack getRandomFood(RandomSource random) {
        ItemStack food = new ItemStack(this.foodList.get(random.nextInt(this.foodList.size())).get());
        if (food.getItem() instanceof LOTRDrinkItem) {
            food.set(LOTRDataComponents.VESSEL, this.drinkVessels[random.nextInt(this.drinkVessels.length)]);
        }
        return food;
    }
}
