package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTradeEntries: a trader's pool of buy or sell trades, from which each
 * trader draws its own few at random prices. The pools arrive with their
 * traders.
 */
public final class LOTRTradeEntries {

    public enum TradeType {
        BUY, SELL
    }

    public static final LOTRTradeEntries HOBBIT_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7), t(Items.COOKED_CHICKEN, 6),
            t(Items.BREAD, 5), t("pipeweed", 4, 4), t("smoking_pipe", 15), t("mug", 2), t("ceramic_mug", 2),
            t(Items.BAKED_POTATO, 2, 7), t(Items.COOKED_COD, 6), t("chocolate", 4), t("apple_juice", 6),
            t("orange_juice", 6), t("gammon", 7), t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6),
            t(Items.RABBIT_STEW, 10), t(Items.MUSHROOM_STEW, 10), t("mushroom_pie", 10), t("leek_soup", 10),
            t("fine_plate", 4), t("stoneware_plate", 2), t("hobbit_marriage_ring", 14), t("hobbit_oven", 40),
            t("hobbit_crafting_table", 100), brew("ale", 8), brew("cider", 8), brew("perry", 8), t("hobbit_pancake", 10))
            .setVessels(LOTRFoods.HOBBIT_DRINK);

    public static final LOTRTradeEntries HOBBIT_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t("clay_mug", 1), t("clay_plate", 1), t("pipeweed_leaf", 1), t(Items.POTATO, 2, 1),
            t("leek", 2, 1), t(Items.COD, 2), t("salt", 10), t("four_leaf_clover", 40), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    /** Without the branding iron (16), not ported yet. */
    public static final LOTRTradeEntries HOBBIT_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("leek", 3), t("turnip", 3), t("pipeweed_leaf", 4), t("pipeweed_seeds", 3), t("corn", 2),
            t("corn_stalk", 1), t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8),
            t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.HOBBIT_DRINK);

    public static final LOTRTradeEntries HOBBIT_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries HOBBIT_ORCHARDER_BUY = buy(
            t(Items.APPLE, 3), t("green_apple", 3), t("pear", 3), t("cherries", 2), t("plum", 3),
            t("apple_log", 4, 5), t("pear_log", 4, 5), t("cherry_log", 4, 5), t("plum_log", 4, 5),
            t("apple_sapling", 4, 8), t("pear_sapling", 4, 8), t("cherry_sapling", 4, 12), t("plum_sapling", 4, 8),
            brew("cider", 10), t("apple_juice", 8), brew("perry", 10), brew("cherry_liqueur", 12))
            .setVessels(LOTRFoods.HOBBIT_DRINK);

    public static final LOTRTradeEntries HOBBIT_ORCHARDER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t("mug", 1), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries BREE_BLACKSMITH_BUY = buy(
            t("bree_crafting_table", 100), t(Items.IRON_SWORD, 12), t("iron_dagger", 8), t("iron_spear", 15),
            t("iron_battleaxe", 15), t("iron_pike", 16), t("bronze_sword", 10), t("bronze_dagger", 7),
            t("bronze_spear", 13), t("bronze_battleaxe", 13), t(Items.IRON_HELMET, 16), t(Items.IRON_CHESTPLATE, 22),
            t(Items.IRON_LEGGINGS, 18), t(Items.IRON_BOOTS, 14), t(Items.CHAINMAIL_HELMET, 12),
            t(Items.CHAINMAIL_CHESTPLATE, 16), t(Items.CHAINMAIL_LEGGINGS, 14), t(Items.CHAINMAIL_BOOTS, 10),
            t("blacksmith_hammer", 16), t(Items.IRON_BARS, 8, 20), t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3),
            t("iron_crossbow", 15), t("bronze_crossbow", 12))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), t(Items.IRON_NUGGET, 9, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL),
            t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3),
            t(Items.STRING, 3, 1), t("emerald", 15), t("amber", 10), t("topaz", 8), t(Items.LEATHER, 2));

    public static final LOTRTradeEntries BREE_INNKEEPER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7), t(Items.COOKED_CHICKEN, 6),
            t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t(Items.RABBIT_STEW, 10), t(Items.BREAD, 5), t("mug", 2),
            t("ceramic_mug", 2), t("copper_goblet", 8), t("wooden_cup", 2), t(Items.BAKED_POTATO, 2, 7),
            t(Items.COOKED_COD, 6), t("gammon", 7), t(Items.MUSHROOM_STEW, 10), t("mushroom_pie", 10),
            t("leek_soup", 10), t("fine_plate", 4), t("stoneware_plate", 2), brew("ale", 8), brew("cider", 8),
            brew("perry", 8), brew("mead", 8), t("apple_juice", 6), t("pipeweed", 4, 8), t("smoking_pipe", 25))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_INNKEEPER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1), t("leek", 2, 1),
            t(Items.COD, 2), t(Items.WHEAT, 2, 1), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t("salt", 10), t("pipeweed_leaf", 2));

    public static final LOTRTradeEntries BREE_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("corn_bread", 5), t("mushroom_pie", 8), t(Items.CAKE, 12), t("apple_crumble", 12),
            t("berry_pie", 12), t("cherry_pie", 12), t("lemon_cake", 20), t("hobbit_pancake", 10),
            t("hobbit_pancake_with_maple_syrup", 12), t("fine_plate", 4), t("stoneware_plate", 2), t("wooden_plate", 2))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.RED_MUSHROOM, 2), t(Items.BROWN_MUSHROOM, 2), t(Items.SUGAR, 2, 1),
            t(Items.EGG, 2, 1), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4), t(Items.APPLE, 1),
            t("green_apple", 1), t("blueberries", 1), t("blackberries", 1), t("raspberries", 1), t("cranberries", 1),
            t("elderberries", 1), t("cherries", 2), t("lemon", 2), t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries BREE_BUTCHER_BUY = buy(
            t(Items.BEEF, 5), t(Items.PORKCHOP, 5), t("gammon", 6), t("raw_mutton", 5), t(Items.CHICKEN, 4),
            t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_BUTCHER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t(Items.IRON_INGOT, 3), t(Items.LEAD, 4), t(Items.WHEAT, 3, 1),
            t("salt", 10));

    public static final LOTRTradeEntries BREE_BREWER_BUY = buy(
            brew("ale", 10), barrel("ale", 2, 120), barrel("ale", 3, 170),
            brew("cider", 10), barrel("cider", 2, 120), barrel("cider", 3, 170),
            brew("perry", 10), barrel("perry", 2, 120), barrel("perry", 3, 170),
            brew("maple_beer", 10), barrel("maple_beer", 2, 120), barrel("maple_beer", 3, 170),
            brew("plum_kvass", 10), barrel("plum_kvass", 2, 120), barrel("plum_kvass", 3, 170),
            brew("mead", 12), barrel("mead", 2, 140), barrel("mead", 3, 180),
            brew("cherry_liqueur", 12), barrel("cherry_liqueur", 2, 140), barrel("cherry_liqueur", 3, 180),
            t("apple_juice", 6), t("blueberry_juice", 5), t("blackberry_juice", 5), t("raspberry_juice", 5),
            t("cranberry_juice", 5), t("elderberry_juice", 5))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_BREWER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.WHEAT, 2, 1), t(Items.APPLE, 1), t("green_apple", 1),
            t("pear", 1), t("maple_syrup", 1), t("plum", 1), t(Items.SUGAR, 2, 1), t("cherries", 1));

    public static final LOTRTradeEntries BREE_MASON_BUY = buy(
            t("bree_crafting_table", 100), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1), t(Items.STONE_BRICKS, 8, 2),
            t(Items.BRICKS, 8, 5), t("drystone", 8, 3), t("stone_pillar", 4, 2), t("brick_pillar", 4, 5))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_MASON_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6), t(Items.IRON_SHOVEL, 8),
            t(Items.STONE_SHOVEL, 1), t("bronze_shovel", 6), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL),
            t(Items.TORCH, 16, 2), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.BREAD, 2),
            t(Items.COOKED_BEEF, 3), t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3), t("pipeweed", 2, 1));

    public static final LOTRTradeEntries BREE_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4),
            t(Items.SPRUCE_LOG, 4), t(Items.SPRUCE_PLANKS, 4, 4), t("spruce_beam", 3, 4), t(Items.SPRUCE_LEAVES, 4),
            t(Items.SPRUCE_SAPLING, 8),
            t(Items.BIRCH_LOG, 3), t(Items.BIRCH_PLANKS, 4, 3), t("birch_beam", 3, 3), t(Items.BIRCH_LEAVES, 3),
            t(Items.BIRCH_SAPLING, 6),
            t(Items.DARK_OAK_LOG, 6), t(Items.DARK_OAK_PLANKS, 4, 6), t("dark_oak_beam", 3, 6),
            t(Items.DARK_OAK_LEAVES, 6), t(Items.DARK_OAK_SAPLING, 12),
            t("shire_pine_log", 6), t("shire_pine_planks", 4, 6), t("shire_pine_beam", 3, 6), t("shire_pine_leaves", 6),
            t("shire_pine_sapling", 12),
            t("apple_log", 5), t("apple_planks", 4, 5), t("apple_beam", 3, 5), t("apple_leaves", 5),
            t("apple_sapling", 10),
            t("pear_log", 5), t("pear_planks", 4, 5), t("pear_beam", 3, 5), t("pear_leaves", 5),
            t("pear_sapling", 10),
            t("beech_log", 3), t("beech_planks", 4, 3), t("beech_beam", 3, 3), t("beech_leaves", 3),
            t("beech_sapling", 6),
            t("holly_log", 6), t("holly_planks", 4, 6), t("holly_beam", 3, 6), t("holly_leaves", 6),
            t("holly_sapling", 12),
            t("maple_log", 4), t("maple_planks", 4, 4), t("maple_beam", 3, 4), t("maple_leaves", 4),
            t("maple_sapling", 8),
            t("larch_log", 4), t("larch_planks", 4, 4), t("larch_beam", 3, 4), t("larch_leaves", 4),
            t("larch_sapling", 8),
            t("chestnut_log", 3), t("chestnut_planks", 4, 3), t("chestnut_beam", 3, 3), t("chestnut_leaves", 3),
            t("chestnut_sapling", 6),
            t("fir_log", 4), t("fir_planks", 4, 4), t("fir_beam", 3, 4), t("fir_leaves", 4),
            t("fir_sapling", 8),
            t("pine_log", 4), t("pine_planks", 4, 4), t("pine_beam", 3, 4), t("pine_leaves", 4),
            t("pine_sapling", 8),
            t("willow_log", 4), t("willow_planks", 4, 4), t("willow_beam", 3, 4), t("willow_leaves", 4),
            t("willow_sapling", 8),
            t("aspen_log", 3), t("aspen_planks", 4, 3), t("aspen_beam", 3, 3), t("aspen_leaves", 3),
            t("aspen_sapling", 6),
            t("plum_log", 5), t("plum_planks", 4, 5), t("plum_beam", 3, 5), t("plum_leaves", 5),
            t("plum_sapling", 10),
            t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    /**
     * The original's tall flower 0 is the black iris (25) and 1 the yellow
     * iris (10); its "dandelion" was the yellow flower block at damage 8,
     * which was only ever a dandelion.
     */
    public static final LOTRTradeEntries BREE_FLORIST_BUY = buy(
            t(Items.POPPY, 3), t(Items.BLUE_ORCHID, 8), t(Items.ALLIUM, 6), t(Items.AZURE_BLUET, 3), t(Items.RED_TULIP, 3),
            t(Items.ORANGE_TULIP, 3), t(Items.WHITE_TULIP, 3), t(Items.PINK_TULIP, 3), t(Items.OXEYE_DAISY, 3),
            t(Items.DANDELION, 3), t("bluebell", 3), t("marigold", 3), t("lavender", 3), t(Items.SUNFLOWER, 10),
            t(Items.LILAC, 8), t(Items.ROSE_BUSH, 8), t(Items.PEONY, 8), t("yellow_iris", 10), t("black_iris", 25),
            t("shire_heather", 6))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_FLORIST_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    /** Without the branding iron (16), not ported yet. */
    public static final LOTRTradeEntries BREE_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("leek", 3), t("turnip", 3), t("pipeweed_leaf", 8), t("pipeweed_seeds", 6), t("corn", 2),
            t("corn_stalk", 1), t(Items.APPLE, 3), t("green_apple", 3), t("pear", 3), t("cherries", 2), t("plum", 3),
            t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8), t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.BREE_DRINK);

    public static final LOTRTradeEntries BREE_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries ROHAN_BLACKSMITH_BUY = buy(
            t("rohirric_crafting_table", 100), t("rohirric_sword", 15), t("rohirric_battleaxe", 18),
            t("rohirric_dagger", 9), t("rohirric_spear", 16), t("rohirric_coif", 18), t("rohirric_hauberk", 28),
            t("rohirric_leggings", 24), t("rohirric_boots", 16), t("rohirric_marshal_helmet", 45),
            t("rohirric_marshal_chestplate", 60), t("rohirric_marshal_leggings", 55), t("rohirric_marshal_boots", 40),
            t("blacksmith_hammer", 18), t(Items.IRON_BARS, 8, 20), t("bronze_bars", 8, 20), t(Items.SADDLE, 15),
            t("crossbow_bolt", 4, 3), t("iron_crossbow", 15), t("rohirric_horse_armor", 25))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("emerald", 15),
            t("amber", 10), t("topaz", 8), t(Items.LEATHER, 2));

    public static final LOTRTradeEntries ROHAN_MEADHOST_BUY = buy(
            t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7), t(Items.COOKED_CHICKEN, 6),
            t("cooked_venison", 6), t(Items.BREAD, 5), t(Items.APPLE, 2, 6), t("wooden_plate", 2),
            t("stoneware_plate", 2), t("mug", 2), t("ceramic_mug", 2), t("ale_horn", 5), t("golden_ale_horn", 10),
            t("water", 2), brew("mead", 8), t("milk", 3))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_MEADHOST_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t("raw_venison", 3),
            t("clay_mug", 1), t("clay_plate", 1), t(Items.SUGAR, 2, 1), t(Items.WHEAT, 2, 1), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4), t("salt", 10));

    public static final LOTRTradeEntries ROHAN_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("leek", 3), t("turnip", 3), t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8),
            t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries ROHAN_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4),
            t(Items.SPRUCE_LOG, 4), t(Items.SPRUCE_PLANKS, 4, 4), t("spruce_beam", 3, 4), t(Items.SPRUCE_LEAVES, 4),
            t(Items.SPRUCE_SAPLING, 8),
            t(Items.BIRCH_LOG, 3), t(Items.BIRCH_PLANKS, 4, 3), t("birch_beam", 3, 3), t(Items.BIRCH_LEAVES, 3),
            t(Items.BIRCH_SAPLING, 6),
            t("apple_log", 5), t("apple_planks", 4, 5), t("apple_beam", 3, 5), t("apple_leaves", 5),
            t("apple_sapling", 10),
            t("pear_log", 5), t("pear_planks", 4, 5), t("pear_beam", 3, 5), t("pear_leaves", 5),
            t("pear_sapling", 10),
            t("beech_log", 3), t("beech_planks", 4, 3), t("beech_beam", 3, 3), t("beech_leaves", 3),
            t("beech_sapling", 6),
            t("holly_log", 6), t("holly_planks", 4, 6), t("holly_beam", 3, 6), t("holly_leaves", 6),
            t("holly_sapling", 12),
            t("maple_log", 4), t("maple_planks", 4, 4), t("maple_beam", 3, 4), t("maple_leaves", 4),
            t("maple_sapling", 8),
            t("larch_log", 4), t("larch_planks", 4, 4), t("larch_beam", 3, 4), t("larch_leaves", 4),
            t("larch_sapling", 8),
            t("chestnut_log", 3), t("chestnut_planks", 4, 3), t("chestnut_beam", 3, 3), t("chestnut_leaves", 3),
            t("chestnut_sapling", 6),
            t("fir_log", 4), t("fir_planks", 4, 4), t("fir_beam", 3, 4), t("fir_leaves", 4),
            t("fir_sapling", 8),
            t("pine_log", 4), t("pine_planks", 4, 4), t("pine_beam", 3, 4), t("pine_leaves", 4),
            t("pine_sapling", 8),
            t("willow_log", 4), t("willow_planks", 4, 4), t("willow_beam", 3, 4), t("willow_leaves", 4),
            t("willow_sapling", 8),
            t("aspen_log", 3), t("aspen_planks", 4, 3), t("aspen_beam", 3, 3), t("aspen_leaves", 3),
            t("aspen_sapling", 6),
            t("plum_log", 5), t("plum_planks", 4, 5), t("plum_beam", 3, 5), t("plum_leaves", 5),
            t("plum_sapling", 10),
            t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    public static final LOTRTradeEntries ROHAN_BUILDER_BUY = buy(
            t("rohirric_crafting_table", 100), t(Items.DIRT, 8, 1), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1),
            t(Items.STONE_BRICKS, 8, 2), t("rohan_rock", 8, 3), t("rohan_brick", 8, 3), t("carved_rohan_brick", 2),
            t(Items.TERRACOTTA, 8, 4), t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2),
            t(Items.SPRUCE_LOG, 2), t(Items.SPRUCE_PLANKS, 4, 2), t("spruce_beam", 3, 2), t("rohan_beam", 3, 4),
            t("rohan_gold_beam", 3, 8), t("thatch_thatch", 4, 2), t("thatch_reed", 4, 4), t("thatch_floor", 16, 2))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_BUILDER_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6), t(Items.IRON_AXE, 8),
            t(Items.STONE_AXE, 1), t("bronze_axe", 6), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.TORCH, 16, 2),
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.BREAD, 2), t(Items.COOKED_BEEF, 3),
            t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3));

    public static final LOTRTradeEntries ROHAN_BREWER_BUY = buy(
            brew("ale", 10), barrel("ale", 2, 120), barrel("ale", 3, 170),
            brew("mead", 10), barrel("mead", 2, 120), barrel("mead", 3, 170),
            brew("cider", 10), barrel("cider", 2, 120), barrel("cider", 3, 170),
            brew("perry", 10), barrel("perry", 2, 120), barrel("perry", 3, 170),
            brew("plum_kvass", 10), barrel("plum_kvass", 2, 120), barrel("plum_kvass", 3, 170),
            t("apple_juice", 6), t("orange_juice", 6), t("blueberry_juice", 5), t("blackberry_juice", 5),
            t("raspberry_juice", 5), t("cranberry_juice", 5), t("elderberry_juice", 5))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_BREWER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.APPLE, 1),
            t("green_apple", 1), t("pear", 1), t("plum", 1));

    public static final LOTRTradeEntries ROHAN_BUTCHER_BUY = buy(
            t(Items.BEEF, 5), t(Items.PORKCHOP, 5), t("gammon", 6), t("raw_mutton", 5), t(Items.CHICKEN, 4),
            t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_BUTCHER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t(Items.IRON_INGOT, 3), t(Items.LEAD, 4), t(Items.WHEAT, 3, 1),
            t("salt", 10));

    public static final LOTRTradeEntries ROHAN_FISHMONGER_BUY = buy(
            t(Items.COD, 4), t(Items.SALMON, 6), t(Items.TROPICAL_FISH, 8), t(Items.PUFFERFISH, 12),
            t(Items.FISHING_ROD, 8), t(Items.OAK_BOAT, 5), t("pearl", 70), t(Items.LEATHER_BOOTS, 5))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_FISHMONGER_SELL = sell(
            t(Items.STICK, 8, 1), t(Items.OAK_PLANKS, 4, 1), t(Items.STRING, 3, 1), t(Items.BUCKET, 3),
            t(Items.IRON_INGOT, 3), t("iron_dagger", 3), t("bronze_dagger", 3), t("salt", 10));

    public static final LOTRTradeEntries ROHAN_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("corn_bread", 5), t("olive_bread", 8), t(Items.CAKE, 12), t("apple_crumble", 12),
            t("berry_pie", 12), t("cherry_pie", 12), t("stoneware_plate", 2), t("wooden_plate", 2))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.EGG, 2, 1), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            t(Items.MILK_BUCKET, 4), t(Items.APPLE, 1), t("green_apple", 1), t("cherries", 2), t("blueberries", 1),
            t("blackberries", 1), t("raspberries", 1), t("cranberries", 1), t("elderberries", 1), t("olives", 2),
            t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries ROHAN_ORCHARDER_BUY = buy(
            t(Items.APPLE, 3), t("green_apple", 3), t("pear", 3), t("plum", 3), t("apple_log", 4, 5),
            t("pear_log", 4, 5), t("plum_log", 4, 5), t("apple_sapling", 4, 8), t("pear_sapling", 4, 8),
            t("plum_sapling", 4, 8), brew("cider", 10), t("apple_juice", 8), brew("perry", 10))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_ORCHARDER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t("mug", 1), t("ceramic_mug", 1), t("wooden_cup", 1),
            t("copper_goblet", 3), t("waterskin", 2), t("ale_horn", 3));

    public static final LOTRTradeEntries ROHAN_STABLEMASTER_BUY = buy(
            t(Items.SADDLE, 10), t(Items.LEAD, 8), t(Items.STRING, 2))
            .setVessels(LOTRFoods.ROHAN_DRINK);

    public static final LOTRTradeEntries ROHAN_STABLEMASTER_SELL = sell(
            t(Items.WHEAT, 3, 1), t(Items.HAY_BLOCK, 3), t(Items.APPLE, 2), t("green_apple", 2), t("plum", 2),
            t(Items.CARROT, 1), t("turnip", 2, 1), t("lettuce", 2, 1), t(Items.SUGAR, 2, 1), t(Items.WATER_BUCKET, 4));

    public static final LOTRTradeEntries GONDOR_BLACKSMITH_BUY = buy(
            t("gondorian_crafting_table", 100), t("gondor_sword", 15), t("gondor_dagger", 9), t("gondor_spear", 16),
            t("gondor_helmet", 20), t("gondor_chestplate", 32), t("gondor_leggings", 26), t("gondor_boots", 17),
            t("blacksmith_hammer", 18), t(Items.IRON_BARS, 8, 20), t("bronze_bars", 8, 20), t("gondor_warhammer", 18),
            t("crossbow_bolt", 4, 3), t("iron_crossbow", 15), t("gondor_horse_armor", 25))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("diamond", 25),
            t("sapphire", 12), t("pearl", 25), t(Items.LEATHER, 2));

    /** Without the branding iron (16), not ported yet. */
    public static final LOTRTradeEntries GONDOR_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("leek", 3), t("turnip", 3), t("pipeweed_plant", 10), t("corn", 2, 1), t("corn_stalk", 1),
            t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8), t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries GONDOR_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7), t(Items.COOKED_CHICKEN, 6),
            t(Items.BREAD, 5), t("mug", 2), t("ceramic_mug", 2), t("ale_horn", 5), t("golden_ale_horn", 8),
            t(Items.BAKED_POTATO, 2, 7), t(Items.COOKED_COD, 6), t("chocolate", 4), t("apple_juice", 6),
            t("orange_juice", 6), t("gammon", 7), t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6),
            t(Items.RABBIT_STEW, 10), t("fine_plate", 4), t("stoneware_plate", 2), brew("ale", 8), brew("cider", 8),
            brew("perry", 8), brew("mead", 8), brew("red_wine", 12), brew("white_wine", 12))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1), t(Items.COD, 2),
            t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    public static final LOTRTradeEntries GONDOR_GREENGROCER_BUY = buy(
            t(Items.APPLE, 2), t("green_apple", 2), t("pear", 2), t("cherries", 2), t("lemon", 5), t("orange", 5),
            t("lime", 5), t("plum", 3), t("olives", 2), t("almond", 2), t("date", 4), t("pomegranate", 5),
            t(Items.CARROT, 3), t(Items.POTATO, 3), t("lettuce", 3), t("leek", 3), t("turnip", 3), t("corn", 2),
            t("red_grapes", 4), t("green_grapes", 4), t("raisins", 3), t("blueberries", 2), t("blackberries", 2),
            t("cranberries", 2), t("raspberries", 2), t("elderberries", 2), t(Items.MELON, 25))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_GREENGROCER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries GONDOR_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4),
            t(Items.SPRUCE_LOG, 4), t(Items.SPRUCE_PLANKS, 4, 4), t("spruce_beam", 3, 4), t(Items.SPRUCE_LEAVES, 4),
            t(Items.SPRUCE_SAPLING, 8),
            t(Items.BIRCH_LOG, 3), t(Items.BIRCH_PLANKS, 4, 3), t("birch_beam", 3, 3), t(Items.BIRCH_LEAVES, 3),
            t(Items.BIRCH_SAPLING, 6),
            t("apple_log", 5), t("apple_planks", 4, 5), t("apple_beam", 3, 5), t("apple_leaves", 5),
            t("apple_sapling", 10),
            t("pear_log", 5), t("pear_planks", 4, 5), t("pear_beam", 3, 5), t("pear_leaves", 5),
            t("pear_sapling", 10),
            t("cherry_log", 8), t("cherry_planks", 4, 8), t("cherry_beam", 3, 8), t("cherry_leaves", 8),
            t("cherry_sapling", 16),
            t("lebethron_log", 6), t("lebethron_planks", 4, 6), t("lebethron_beam", 3, 6), t("lebethron_leaves", 6),
            t("lebethron_sapling", 12),
            t("beech_log", 3), t("beech_planks", 4, 3), t("beech_beam", 3, 3), t("beech_leaves", 3),
            t("beech_sapling", 6),
            t("holly_log", 6), t("holly_planks", 4, 6), t("holly_beam", 3, 6), t("holly_leaves", 6),
            t("holly_sapling", 12),
            t("maple_log", 4), t("maple_planks", 4, 4), t("maple_beam", 3, 4), t("maple_leaves", 4),
            t("maple_sapling", 8),
            t("larch_log", 4), t("larch_planks", 4, 4), t("larch_beam", 3, 4), t("larch_leaves", 4),
            t("larch_sapling", 8),
            t("date_palm_log", 8), t("date_palm_planks", 4, 8), t("date_palm_beam", 3, 8), t("date_palm_leaves", 8),
            t("date_palm_sapling", 16),
            t("chestnut_log", 3), t("chestnut_planks", 4, 3), t("chestnut_beam", 3, 3), t("chestnut_leaves", 3),
            t("chestnut_sapling", 6),
            t("cedar_log", 4), t("cedar_planks", 4, 4), t("cedar_beam", 3, 4), t("cedar_leaves", 4),
            t("cedar_sapling", 8),
            t("fir_log", 4), t("fir_planks", 4, 4), t("fir_beam", 3, 4), t("fir_leaves", 4),
            t("fir_sapling", 8),
            t("pine_log", 4), t("pine_planks", 4, 4), t("pine_beam", 3, 4), t("pine_leaves", 4),
            t("pine_sapling", 8),
            t("lemon_log", 7), t("lemon_planks", 4, 7), t("lemon_beam", 3, 7), t("lemon_leaves", 7),
            t("lemon_sapling", 14),
            t("orange_log", 7), t("orange_planks", 4, 7), t("orange_beam", 3, 7), t("orange_leaves", 7),
            t("orange_sapling", 14),
            t("lime_log", 7), t("lime_planks", 4, 7), t("lime_beam", 3, 7), t("lime_leaves", 7),
            t("lime_sapling", 14),
            t("mahogany_log", 10), t("mahogany_planks", 4, 10), t("mahogany_beam", 3, 10), t("mahogany_leaves", 10),
            t("mahogany_sapling", 20),
            t("willow_log", 4), t("willow_planks", 4, 4), t("willow_beam", 3, 4), t("willow_leaves", 4),
            t("willow_sapling", 8),
            t("cypress_log", 5), t("cypress_planks", 4, 5), t("cypress_beam", 3, 5), t("cypress_leaves", 5),
            t("cypress_sapling", 10),
            t("olive_log", 6), t("olive_planks", 4, 6), t("olive_beam", 3, 6), t("olive_leaves", 6),
            t("olive_sapling", 12),
            t("aspen_log", 3), t("aspen_planks", 4, 3), t("aspen_beam", 3, 3), t("aspen_leaves", 3),
            t("aspen_sapling", 6),
            t("almond_log", 5), t("almond_planks", 4, 5), t("almond_beam", 3, 5), t("almond_leaves", 5),
            t("almond_sapling", 10),
            t("plum_log", 5), t("plum_planks", 4, 5), t("plum_beam", 3, 5), t("plum_leaves", 5),
            t("plum_sapling", 10),
            t("palm_log", 5), t("palm_planks", 4, 5), t("palm_beam", 3, 5), t("palm_leaves", 5),
            t("palm_sapling", 10),
            t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    public static final LOTRTradeEntries GONDOR_MASON_BUY = buy(
            t("gondorian_crafting_table", 100), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1),
            t(Items.STONE_BRICKS, 8, 2), t("gondor_rock", 8, 3), t("gondor_brick", 8, 3), t("carved_gondor_brick", 2),
            t("gondor_cobblebrick", 8, 3), t("gondor_pillar", 4, 3), t("mordor_rock", 8, 16), t("numenorean_brick", 8, 12),
            t("carved_black_gondor_brick", 12), t(Items.TERRACOTTA, 8, 4), t(Items.SANDSTONE, 8, 4),
            t(Items.RED_SANDSTONE, 8, 12), t("white_sandstone", 8, 6))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_MASON_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.TORCH, 16, 2), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            t(Items.BREAD, 2), t("olive_bread", 3), t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3),
            t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3), t("cooked_venison", 3));

    public static final LOTRTradeEntries GONDOR_BREWER_BUY = buy(
            brew("ale", 10), barrel("ale", 2, 120), barrel("ale", 3, 170),
            brew("mead", 10), barrel("mead", 2, 120), barrel("mead", 3, 170),
            brew("cider", 10), barrel("cider", 2, 120), barrel("cider", 3, 170),
            brew("perry", 10), barrel("perry", 2, 120), barrel("perry", 3, 170),
            brew("plum_kvass", 10), barrel("plum_kvass", 2, 120), barrel("plum_kvass", 3, 170),
            brew("cherry_liqueur", 10), barrel("cherry_liqueur", 2, 120), barrel("cherry_liqueur", 3, 170),
            brew("carrot_wine", 12), barrel("carrot_wine", 2, 140), barrel("carrot_wine", 3, 180),
            brew("lemon_liqueur", 15), brew("lime_liqueur", 15), brew("pomegranate_wine", 15), t("apple_juice", 6),
            t("orange_juice", 6), t("chocolate", 6), t("blueberry_juice", 5), t("blackberry_juice", 5),
            t("raspberry_juice", 5), t("cranberry_juice", 5), t("elderberry_juice", 5), t("pomegranate_juice", 10))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_BREWER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.APPLE, 1),
            t("green_apple", 1), t("pear", 1), t("plum", 1), t("cherries", 1), t(Items.CARROT, 1), t("lemon", 2),
            t("lime", 2), t("pomegranate", 2));

    /**
     * The original's "dandelion" was the yellow flower block at damage 8,
     * which was only ever a dandelion; its tall flowers 0-3 are the black and
     * yellow irises, hibiscus and flame of Harad.
     */
    public static final LOTRTradeEntries GONDOR_FLORIST_BUY = buy(
            t(Items.POPPY, 3), t(Items.BLUE_ORCHID, 8), t(Items.ALLIUM, 6), t(Items.AZURE_BLUET, 3), t(Items.RED_TULIP, 3),
            t(Items.ORANGE_TULIP, 3), t(Items.WHITE_TULIP, 3), t(Items.PINK_TULIP, 3), t(Items.OXEYE_DAISY, 3),
            t(Items.DANDELION, 3), t("bluebell", 3), t("marigold", 3), t("lavender", 3), t(Items.SUNFLOWER, 15),
            t(Items.LILAC, 8), t(Items.ROSE_BUSH, 8), t(Items.PEONY, 8), t("yellow_iris", 10),
            t("rhun_flower_chrys_blue", 20), t("rhun_flower_chrys_orange", 20), t("rhun_flower_chrys_pink", 20),
            t("rhun_flower_chrys_yellow", 20), t("rhun_flower_chrys_white", 20), t("black_iris", 25),
            t("red_harad_flower", 30), t("yellow_harad_flower", 30), t("harad_flower_daisy", 30),
            t("pink_harad_flower", 30), t("hibiscus", 45), t("flame_of_harad", 45))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_FLORIST_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries GONDOR_BUTCHER_BUY = buy(
            t(Items.BEEF, 5), t(Items.PORKCHOP, 5), t("gammon", 6), t("raw_mutton", 5), t(Items.CHICKEN, 4),
            t(Items.RABBIT, 4), t("raw_venison", 4), t("raw_camel", 8), t(Items.LEATHER, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_BUTCHER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t(Items.IRON_INGOT, 3), t(Items.LEAD, 4), t(Items.WHEAT, 3, 1),
            t("salt", 10));

    public static final LOTRTradeEntries GONDOR_FISHMONGER_BUY = buy(
            t(Items.COD, 4), t(Items.SALMON, 6), t(Items.TROPICAL_FISH, 8), t(Items.PUFFERFISH, 12),
            t(Items.FISHING_ROD, 8), t(Items.OAK_BOAT, 5), t(Items.INK_SAC, 4), t(Items.SPONGE, 15), t("coral", 9),
            t("pearl", 50), t(Items.LEATHER_BOOTS, 5))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_FISHMONGER_SELL = sell(
            t(Items.STICK, 8, 1), t(Items.OAK_PLANKS, 4, 1), t(Items.STRING, 3, 1), t(Items.BUCKET, 3),
            t(Items.IRON_INGOT, 3), t("iron_dagger", 3), t("bronze_dagger", 3), t("salt", 10));

    public static final LOTRTradeEntries GONDOR_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("corn_bread", 5), t("olive_bread", 6), t(Items.CAKE, 12), t("lemon_cake", 15),
            t("apple_crumble", 12), t("berry_pie", 12), t("cherry_pie", 12), t("marchpane", 6),
            t("chocolate_marchpane", 10), t("marchpane_block", 20), t(Items.COOKIE, 4), t("fine_plate", 4),
            t("stoneware_plate", 2))
            .setVessels(LOTRFoods.GONDOR_DRINK);

    public static final LOTRTradeEntries GONDOR_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.EGG, 2, 1), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            t(Items.MILK_BUCKET, 4), t(Items.APPLE, 1), t("green_apple", 1), t("cherries", 2), t("lemon", 2),
            t("olives", 2), t("almond", 2), t(Items.COCOA_BEANS, 2), t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries GALADHRIM_TRADER_BUY = buy(
            t("galadhrim_sword", 21), t("galadhrim_dagger", 13), t("galadhrim_spear", 21), t("galadhrim_battlestaff", 21),
            t("galadhrim_longspear", 21), t("galadhrim_axe", 18), t("galadhrim_bow", 21), t("mallorn_sword", 18),
            t("mallorn_bow", 17), t("galadhrim_helmet", 25), t("galadhrim_chestplate", 36), t("galadhrim_leggings", 30),
            t("galadhrim_boots", 22), t("mallorn_planks", 4, 4), t("elanor", 2), t("niphredil", 2),
            t("mallorn_torch", 4, 8), t("blue_mallorn_torch", 4, 8), t("mallorn_gold_torch", 4, 8),
            t("green_mallorn_torch", 4, 8), t("lembas", 16), brew("miruvor", 8), t(Items.ARROW, 4, 3), t("hithlain", 4),
            t("mallorn_box", 20))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries GALADHRIM_TRADER_SELL = sell(
            t(Items.IRON_INGOT, 3), t(Items.WHEAT, 3, 1), t(Items.STRING, 3, 1), t(Items.FEATHER, 2), t("mug", 1),
            t("orc_bone", 1), t("warg_bone", 1), t("troll_bone", 2), t("mithril_nugget", 40), t("diamond", 25),
            t("emerald", 15), t("opal", 10), t("pearl", 25), t("coral", 6));

    public static final LOTRTradeEntries GALADHRIM_SMITH_BUY = buy(
            t("elven_crafting_table", 100), t("galadhrim_sword", 18), t("galadhrim_dagger", 14), t("galadhrim_spear", 18),
            t("galadhrim_battlestaff", 18), t("galadhrim_longspear", 18), t("galadhrim_axe", 15),
            t("galadhrim_pickaxe", 15), t("galadhrim_shovel", 12), t("galadhrim_helmet", 18),
            t("galadhrim_chestplate", 25), t("galadhrim_leggings", 22), t("galadhrim_boots", 16),
            t("galadhrim_horse_armor", 25), t("galadhrim_bow", 20), t(Items.ARROW, 4, 3))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries GALADHRIM_SMITH_SELL = sell(
            t(Items.IRON_ORE, 5), t(Items.GOLD_ORE, 15), t(Items.COPPER_ORE, 5), t("tin_ore", 5), t(Items.IRON_INGOT, 3),
            t("elven_steel_ingot", 3), t("bronze_ingot", 3), t(Items.GOLD_INGOT, 15), t("galvorn_ingot", 4),
            t("diamond", 25), t("emerald", 15), t("opal", 10), t("amber", 10), t("amethyst", 8), t("pearl", 25),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.LAVA_BUCKET, 16), t("mallorn_stick", 8, 1),
            t(Items.STRING, 3, 1), t("mithril_nugget", 40));

    public static final LOTRTradeEntries HIGH_ELF_SMITH_BUY = buy(
            t("high_elven_crafting_table", 100), t("lindon_sword", 20), t("lindon_battlestaff", 20), t("lindon_longspear", 20),
            t("lindon_dagger", 14), t("lindon_spear", 18), t("lindon_axe", 15), t("lindon_pickaxe", 15),
            t("lindon_shovel", 12), t("lindon_helmet", 18), t("lindon_chestplate", 25), t("lindon_leggings", 22),
            t("lindon_boots", 16), t("lindon_horse_armor", 25), t("lindon_bow", 20), t(Items.ARROW, 4, 3))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries HIGH_ELF_SMITH_SELL = sell(
            t(Items.IRON_ORE, 5), t(Items.GOLD_ORE, 15), t(Items.COPPER_ORE, 5), t("tin_ore", 5), t(Items.IRON_INGOT, 3),
            t("elven_steel_ingot", 3), t("bronze_ingot", 3), t(Items.GOLD_INGOT, 15), t("galvorn_ingot", 4),
            t("diamond", 25), t("sapphire", 12), t("opal", 10), t("pearl", 25), t("coral", 6),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.LAVA_BUCKET, 16), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("mithril_nugget", 40));

    public static final LOTRTradeEntries RIVENDELL_SMITH_BUY = buy(
            t("rivendell_crafting_table", 100), t("rivendell_sword", 20), t("rivendell_battlestaff", 20), t("rivendell_longspear", 20),
            t("rivendell_dagger", 14), t("rivendell_spear", 18), t("rivendell_axe", 15), t("rivendell_pickaxe", 15),
            t("rivendell_shovel", 12), t("rivendell_helmet", 18), t("rivendell_chestplate", 25), t("rivendell_leggings", 22),
            t("rivendell_boots", 16), t("rivendell_horse_armor", 25), t("rivendell_bow", 20), t(Items.ARROW, 4, 3))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries RIVENDELL_SMITH_SELL = sell(
            t(Items.IRON_ORE, 5), t(Items.GOLD_ORE, 15), t(Items.COPPER_ORE, 5), t("tin_ore", 5), t(Items.IRON_INGOT, 3),
            t("elven_steel_ingot", 3), t("bronze_ingot", 3), t(Items.GOLD_INGOT, 15), t("galvorn_ingot", 4),
            t("emerald", 15), t("sapphire", 12), t("opal", 10), t("amethyst", 8), t("pearl", 25),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.LAVA_BUCKET, 16), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("mithril_nugget", 40));

    /** The original's planks at damage 9, which are beech. */
    public static final LOTRTradeEntries RIVENDELL_TRADER_BUY = buy(
            t("rivendell_sword", 21), t("rivendell_dagger", 13), t("rivendell_spear", 21), t("rivendell_battlestaff", 21),
            t("rivendell_longspear", 21), t("rivendell_bow", 21), t("rivendell_helmet", 25),
            t("rivendell_chestplate", 36), t("rivendell_leggings", 30), t("rivendell_boots", 22),
            t("beech_planks", 4, 4), t("high_elven_torch", 4, 8), t("lembas", 16), brew("miruvor", 8),
            t(Items.ARROW, 4, 3))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries RIVENDELL_TRADER_SELL = sell(
            t(Items.IRON_INGOT, 3), t(Items.WHEAT, 3, 1), t(Items.STRING, 3, 1), t(Items.FEATHER, 2), t("mug", 1),
            t("orc_bone", 1), t("warg_bone", 1), t("troll_bone", 2), t("mithril_nugget", 40), t("emerald", 15),
            t("opal", 10), t("pearl", 25), t("coral", 6));

    public static final LOTRTradeEntries WOOD_ELF_SMITH_BUY = buy(
            t("wood_elven_crafting_table", 100), t("wood_elven_sword", 18), t("wood_elven_dagger", 14),
            t("wood_elven_spear", 18), t("wood_elven_battlestaff", 18), t("wood_elven_longspear", 18),
            t("wood_elven_axe", 15), t("wood_elven_pickaxe", 15), t("wood_elven_shovel", 12), t("wood_elven_helmet", 18),
            t("wood_elven_chestplate", 25), t("wood_elven_leggings", 22), t("wood_elven_boots", 16),
            t("wood_elven_elk_armor", 25), t("mirkwood_bow", 15), t(Items.ARROW, 4, 3))
            .setVessels(LOTRFoods.ELF_DRINK);

    public static final LOTRTradeEntries WOOD_ELF_SMITH_SELL = sell(
            t(Items.IRON_ORE, 5), t(Items.GOLD_ORE, 15), t(Items.COPPER_ORE, 5), t("tin_ore", 5), t(Items.IRON_INGOT, 3),
            t("elven_steel_ingot", 3), t("bronze_ingot", 3), t(Items.GOLD_INGOT, 15), t("diamond", 25),
            t("emerald", 15), t("ruby", 12), t("opal", 10), t("amber", 10), t("topaz", 8),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.LAVA_BUCKET, 16), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("mithril_nugget", 40));

    public static final LOTRTradeEntries DORWINION_VINTNER_BUY = buy(
            t("dorwinion_crafting_table", 100), strength("red_wine", 0, 5), strength("red_wine", 1, 7),
            strength("red_wine", 2, 10), strength("red_wine", 3, 15), strength("red_wine", 4, 25),
            strength("white_wine", 0, 5), strength("white_wine", 1, 7), strength("white_wine", 2, 10),
            strength("white_wine", 3, 15), strength("white_wine", 4, 25), t("red_grape_juice", 6),
            t("green_grape_juice", 6), t("mug", 2))
            .setVessels(LOTRFoods.DORWINION_DRINK);

    public static final LOTRTradeEntries DORWINION_VINTNER_SELL = sell(
            t("red_grapes", 3, 1), t("green_grapes", 3, 1), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            t(Items.SUGAR, 2, 1), t("clay_mug", 1));

    public static final LOTRTradeEntries DORWINION_VINEKEEPER_BUY = buy(
            t("red_grapes", 3), t("green_grapes", 3), t("grapevine", 4))
            .setVessels(LOTRFoods.DORWINION_DRINK);

    public static final LOTRTradeEntries DORWINION_VINEKEEPER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries DORWINION_MERCHANT_BUY = buy(
            t("dorwinion_helmet", 25), t("dorwinion_chestplate", 35), t("dorwinion_leggings", 30),
            t("dorwinion_boots", 20), t("red_grapes", 4), t("green_grapes", 4), t("raisins", 3), brew("red_wine", 15),
            brew("white_wine", 15), barrel("red_wine", 2, 160), barrel("red_wine", 3, 220),
            barrel("white_wine", 2, 160), barrel("white_wine", 3, 220), t("red_grape_juice", 8),
            t("green_grape_juice", 8), t("dorwinion_banner", 20), t("dorwinion_brick", 4),
            t("rhun_flower_chrys_blue", 8), t("rhun_flower_chrys_orange", 5), t("rhun_flower_chrys_pink", 10),
            t("rhun_flower_chrys_yellow", 5), t("rhun_flower_chrys_white", 5), t("black_iris", 12))
            .setVessels(LOTRFoods.DORWINION_DRINK);

    public static final LOTRTradeEntries DORWINION_MERCHANT_SELL = sell(
            t("mug", 2), t("silver_goblet", 10), t("golden_goblet", 15), t("wine_glass", 4), t("barrel", 10),
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.BONE_MEAL, 6, 1), t(Items.APPLE, 2),
            t("green_apple", 2), t("pear", 2), t("banana", 2), t("mango", 2), t(Items.MELON_SLICE, 2),
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t("elven_steel_ingot", 5),
            t("emerald", 15), t("sapphire", 12), t("ruby", 12), t("opal", 10), t("amethyst", 8), t("pearl", 25),
            t("pine_log", 2), t("fir_log", 2), t("mirk_oak_log", 2), t("green_oak_log", 2));

    // --- Dwarves ----------------------------------------------------------------------

    public static final LOTRTradeEntries DWARF_MINER_BUY = buy(
            t(Items.COAL, 2, 4), t(Items.IRON_ORE, 8), t(Items.COPPER_ORE, 7), t("tin_ore", 7), t("silver_ore", 12),
            t(Items.GOLD_ORE, 22), t(Items.GLOWSTONE_DUST, 4, 3), t(Items.COBBLESTONE, 8, 1), t(Items.STONE, 8, 1),
            t(Items.LAVA_BUCKET, 20),
            t(Items.FLINT, 2), t("sulfur", 6), t("niter", 6), t("diamond", 40), t("emerald", 25), t("sapphire", 20),
            t("ruby", 20), t("opal", 15), t("amber", 15), t("amethyst", 12), t("topaz", 12))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries DWARF_MINER_SELL = sell(
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3),
            t("gammon", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3), t("cooked_venison", 3), t(Items.BREAD, 2),
            t("cram", 10), t("ale", 8), t("mead", 8), t("cider", 8), t("perry", 8), t("dwarven_ale", 12),
            t("dwarven_pickaxe", 10));

    public static final LOTRTradeEntries BLUE_DWARF_MINER_BUY = buy(
            t(Items.COAL, 2, 4), t(Items.IRON_ORE, 8), t(Items.COPPER_ORE, 7), t("tin_ore", 7), t("silver_ore", 12),
            t(Items.GOLD_ORE, 22), t(Items.GLOWSTONE_DUST, 4, 3), t(Items.COBBLESTONE, 8, 1), t(Items.LAVA_BUCKET, 20),
            t(Items.FLINT, 2), t("sulfur", 6), t("niter", 6), t("diamond", 40), t("emerald", 25), t("sapphire", 20),
            t("ruby", 20), t("opal", 15), t("amber", 15), t("amethyst", 12), t("topaz", 12))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries BLUE_DWARF_MINER_SELL = sell(
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3),
            t("gammon", 3), t("cooked_venison", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3), t(Items.BREAD, 2),
            t("ale", 8), t("mead", 8), t("cider", 8), t("perry", 8), t("dwarven_ale", 12), t("blue_dwarven_pickaxe", 10));

    public static final LOTRTradeEntries BLUE_DWARF_MERCHANT_BUY = buy(
            t("dwarven_forge", 40), t("dwarven_marriage_ring", 16), t("blue_dwarven_sword", 16), t("blue_dwarven_spear", 18), t("blue_dwarven_battleaxe", 18),
            t("blue_dwarven_warhammer", 18), t("blue_dwarven_pike", 18), t("blue_dwarven_dagger", 13), t("blue_dwarven_axe", 15), t("blue_dwarven_pickaxe", 14),
            t("blue_dwarven_shovel", 12), t("blue_dwarven_mattock", 18), t("blue_dwarven_throwing_axe", 15), t("blue_dwarven_helmet", 25),
            t("blue_dwarven_chestplate", 36), t("blue_dwarven_leggings", 30), t("blue_dwarven_boots", 22),
            brew("dwarven_ale", 9), brew("dwarven_tonic", 14), barrel("dwarven_ale", 2, 120), barrel("dwarven_ale", 3, 160),
            t("dwarf_herb", 10), t(Items.GLOWSTONE_DUST, 4, 2), t("glowing_dwarven_brick", 4), t("blue_rock", 8, 2),
            t("emerald", 25), t("ruby", 20), t("amethyst", 12), t("topaz", 12))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries BLUE_DWARF_MERCHANT_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t("diamond", 25), t("sapphire", 12), t("opal", 10), t("pearl", 25),
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3),
            t(Items.COOKED_CHICKEN, 3), t("gammon", 3), t("cooked_venison", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3),
            t(Items.BREAD, 2));

    public static final LOTRTradeEntries IRON_HILLS_MERCHANT_BUY = buy(
            t("dwarven_forge", 40), t("dwarven_marriage_ring", 16), t("dwarven_sword", 16), t("dwarven_spear", 18), t("dwarven_battleaxe", 18),
            t("dwarven_warhammer", 18), t("dwarven_pike", 18), t("dwarven_dagger", 13), t("dwarven_axe", 15), t("dwarven_pickaxe", 14),
            t("dwarven_shovel", 12), t("dwarven_mattock", 18), t("dwarven_throwing_axe", 15), t("dwarven_helmet", 25),
            t("dwarven_chestplate", 36), t("dwarven_leggings", 30), t("dwarven_boots", 22),
            t("silver_trimmed_dwarven_helmet", 50), t("silver_trimmed_dwarven_chestplate", 60),
            t("silver_trimmed_dwarven_leggings", 55), t("silver_trimmed_dwarven_boots", 50),
            brew("dwarven_ale", 9), brew("dwarven_tonic", 14), barrel("dwarven_ale", 2, 120), barrel("dwarven_ale", 3, 160),
            t("dwarf_herb", 10), t(Items.GLOWSTONE_DUST, 4, 2), t("glowing_dwarven_brick", 4), t("cram", 12),
            t("dalish_pastry", 16), t(Items.COBBLESTONE, 8, 2), t(Items.STONE, 8, 2), t("amber", 15), t("amethyst", 12),
            t("topaz", 12))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries IRON_HILLS_MERCHANT_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t("diamond", 25), t("emerald", 15), t("sapphire", 12), t("ruby", 12),
            t("opal", 10), t("pearl", 25), t("amethyst", 8), t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3),
            t(Items.COOKED_CHICKEN, 3), t("gammon", 3),
            t("cooked_venison", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3), t(Items.BREAD, 2));

    public static final LOTRTradeEntries DWARF_SMITH_BUY = buy(
            t("dwarven_crafting_table", 100), t("blacksmith_hammer", 18), t("dwarven_marriage_ring", 20),
            t("dwarven_sword", 16), t("dwarven_spear", 18), t("dwarven_battleaxe", 18),
            t("dwarven_warhammer", 18), t("dwarven_pike", 18), t("dwarven_dagger", 13), t("dwarven_axe", 15), t("dwarven_pickaxe", 14),
            t("dwarven_shovel", 12), t("dwarven_mattock", 18), t("dwarven_throwing_axe", 15), t("dwarven_helmet", 25),
            t("dwarven_chestplate", 36), t("dwarven_leggings", 30), t("dwarven_boots", 22),
            t("silver_trimmed_dwarven_helmet", 50), t("silver_trimmed_dwarven_chestplate", 60),
            t("silver_trimmed_dwarven_leggings", 55), t("silver_trimmed_dwarven_boots", 50),
            t("gold_trimmed_dwarven_helmet", 70), t("gold_trimmed_dwarven_chestplate", 80),
            t("gold_trimmed_dwarven_leggings", 75), t("gold_trimmed_dwarven_boots", 70),
            t("dwarven_boar_armor", 25), t("dwarf_bars", 8, 20))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries DWARF_SMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), t("dwarven_steel_ingot", 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("diamond", 25), t("emerald", 15), t("sapphire", 12),
            t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8), t("topaz", 8), t("pearl", 25),
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3),
            t(Items.COOKED_CHICKEN, 3), t("gammon", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3), t(Items.BREAD, 2), t(Items.LAVA_BUCKET, 16));

    public static final LOTRTradeEntries BLUE_DWARF_SMITH_BUY = buy(
            t("blue_dwarven_crafting_table", 100), t("blacksmith_hammer", 18), t("dwarven_marriage_ring", 20),
            t("blue_dwarven_sword", 16), t("blue_dwarven_spear", 18), t("blue_dwarven_battleaxe", 18),
            t("blue_dwarven_warhammer", 18), t("blue_dwarven_pike", 18), t("blue_dwarven_dagger", 13), t("blue_dwarven_axe", 15), t("blue_dwarven_pickaxe", 14),
            t("blue_dwarven_shovel", 12), t("blue_dwarven_mattock", 18), t("blue_dwarven_throwing_axe", 15), t("blue_dwarven_helmet", 25),
            t("blue_dwarven_chestplate", 36), t("blue_dwarven_leggings", 30), t("blue_dwarven_boots", 22),
            t("blue_dwarven_boar_armor", 25), t("blue_dwarf_bars", 8, 20))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries BLUE_DWARF_SMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), t("blue_dwarven_steel_ingot", 3), t("blue_rock", 8, 1), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL),
            t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("diamond", 25), t("emerald", 15), t("sapphire", 12),
            t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8), t("topaz", 8), t("pearl", 25),
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3),
            t(Items.COOKED_CHICKEN, 3), t("gammon", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3), t(Items.BREAD, 2), t(Items.LAVA_BUCKET, 16));

    /** Sold to anyone at +100 with Mordor, Angmar or Rhûn. */
    public static final LOTRTradeEntries WICKED_DWARF_BUY = buy(
            t("blacksmith_hammer", 18), t("dwarven_sword", 25), t("dwarven_battleaxe", 25), t("dwarven_warhammer", 25),
            t("dwarven_dagger", 18), t("dwarven_pickaxe", 20), t("dwarven_mattock", 25), t("dwarven_throwing_axe", 18),
            t("dwarf_bars", 8, 30), t("dwarven_forge", 100), brew("dwarven_ale", 10), brew("dwarven_tonic", 30),
            t("dwarven_brick", 8, 5), t("dwarven_brick_slab", 16, 5), t("dwarven_brick_stairs", 5, 5),
            t("dwarven_brick_wall", 8, 5), t("carved_dwarven_brick", 5), t("dwarven_pillar", 4, 5),
            t("dwarven_pillar_slab", 8, 5), t("cracked_dwarven_brick", 8, 2), t("cracked_dwarven_brick_slab", 16, 2),
            t("cracked_dwarven_brick_stairs", 5, 2), t("cracked_dwarven_brick_wall", 8, 2),
            t("cracked_dwarven_pillar", 4, 2), t("cracked_dwarven_pillar_slab", 8, 2), t("dwarven_silver_brick", 4),
            t("dwarven_gold_brick", 8), t("glowing_dwarven_brick", 4), t("obsidian_dwarven_brick", 8, 8),
            t("obsidian_dwarven_brick_slab", 16, 8), t("obsidian_dwarven_brick_stairs", 5, 8),
            t("obsidian_dwarven_brick_wall", 8, 8), t("dwarven_gate", 12))
            .setVessels(LOTRFoods.DWARF_DRINK);

    public static final LOTRTradeEntries WICKED_DWARF_SELL = sell(
            t(Items.IRON_INGOT, 3), t("dwarven_steel_ingot", 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("diamond", 25), t("emerald", 15), t("sapphire", 12),
            t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8), t("topaz", 8), t("pearl", 25),
            t(Items.COOKED_BEEF, 3), t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3),
            t(Items.COOKED_CHICKEN, 3), t("gammon", 3), t(Items.COOKED_COD, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3), t(Items.BREAD, 2), t(Items.LAVA_BUCKET, 16),
            t(Items.COBBLESTONE, 8, 1), t(Items.STONE, 8, 1), t(Items.GLOWSTONE_DUST, 3, 1), t("obsidian_shard", 2));

    // --- Mordor -----------------------------------------------------------------------

    public static final LOTRTradeEntries MORDOR_TRADER_BUY = buy(
            t("morgul_crafting_table", 100), t("mordor_helmet", 20), t("mordor_chestplate", 30),
            t("mordor_leggings", 26), t("mordor_boots", 18), t("mordor_scimitar", 16), t("mordor_warscythe", 18),
            t("mordor_spear", 16), t("mordor_dagger", 10), t("poisoned_mordor_dagger", 15), t("mordor_battleaxe", 18),
            t("orc_bomb", 30), brew("orc_draught", 8), t("fur", 4), t("orc_bow", 16), t(Items.ARROW, 4, 3),
            t("mordor_pickaxe", 14), t("mordor_axe", 14), t("mordor_warhammer", 16), t("maggoty_bread", 4),
            t("fur_hat", 15), t("fur_tunic", 20), t("fur_leggings", 16), t("fur_boots", 12), t("mordor_warg_armor", 25))
            .setVessels(LOTRFoods.ORC_DRINK);

    public static final LOTRTradeEntries MORDOR_TRADER_SELL = sell(
            t("orc_steel_ingot", 3), t("black_uruk_steel_ingot", 3), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3),
            t("durnor", 2), t("diamond", 25), t("ruby", 12), t("amber", 10), t(Items.STRING, 3, 1), t(Items.FEATHER, 2),
            t("mug", 1), t("waterskin", 2), t("morgul_shroom", 2), t("elf_bone", 1), t("dwarf_bone", 1),
            t("hobbit_bone", 1), t(Items.BONE, 1), t("man_flesh", 3));

    // --- Angmar and Gundabad ----------------------------------------------------------

    public static final LOTRTradeEntries ANGMAR_TRADER_BUY = buy(
            t("angmar_crafting_table", 100), t("angmar_helmet", 20), t("angmar_chestplate", 30),
            t("angmar_leggings", 26), t("angmar_boots", 18), t("angmar_sword", 14), t("angmar_spear", 15),
            t("angmar_dagger", 8), t("poisoned_angmar_dagger", 10), t("angmar_battleaxe", 18), t("angmar_poleaxe", 16),
            t("orc_bomb", 30), brew("orc_draught", 8), t("fur", 4), t("orc_bow", 16), t(Items.ARROW, 4, 3),
            t("angmar_pickaxe", 14), t("angmar_axe", 14), t("angmar_warhammer", 16), t("maggoty_bread", 4),
            t("fur_hat", 15), t("fur_tunic", 20), t("fur_leggings", 16), t("fur_boots", 12), t("angmar_warg_armor", 25))
            .setVessels(LOTRFoods.ORC_DRINK);

    public static final LOTRTradeEntries ANGMAR_TRADER_SELL = sell(
            t("orc_steel_ingot", 3), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3), t("gulduril", 8), t("emerald", 15),
            t("ruby", 12), t(Items.STRING, 3, 1), t(Items.FEATHER, 2), t("mug", 1), t("waterskin", 2),
            t(Items.WHEAT, 2, 1), t("elf_bone", 1), t("dwarf_bone", 1), t("hobbit_bone", 1), t(Items.BONE, 1),
            t("man_flesh", 3));

    public static final LOTRTradeEntries GUNDABAD_TRADER_BUY = buy(
            t("gundabad_crafting_table", 100), t("bronze_helmet", 20), t("bronze_chestplate", 30),
            t("bronze_leggings", 26), t("bronze_boots", 18), t("bronze_sword", 14), t("bronze_spear", 15),
            t("bronze_dagger", 8), t("poisoned_bronze_dagger", 10), t("bronze_battleaxe", 18), t(Items.IRON_SWORD, 14),
            t("iron_spear", 15), t("iron_dagger", 8), t("poisoned_iron_dagger", 10), t("iron_battleaxe", 18),
            brew("orc_draught", 8), t("fur", 4), t("orc_bow", 16), t(Items.ARROW, 4, 3), t("bronze_pickaxe", 14),
            t("bronze_axe", 14), t(Items.IRON_PICKAXE, 14), t(Items.IRON_AXE, 14), t("maggoty_bread", 4),
            t("fur_hat", 15), t("fur_tunic", 20), t("fur_leggings", 16), t("fur_boots", 12))
            .setVessels(LOTRFoods.ORC_DRINK);

    public static final LOTRTradeEntries GUNDABAD_TRADER_SELL = sell(
            t("orc_steel_ingot", 3), t("uruk_steel_ingot", 3), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3),
            t("diamond", 25), t("emerald", 15), t("sapphire", 12), t("ruby", 12), t("opal", 10), t("pearl", 25),
            t(Items.STRING, 3, 1), t(Items.FEATHER, 2), t("mug", 1), t("waterskin", 2), t(Items.WHEAT, 2, 1),
            t("elf_bone", 1), t("dwarf_bone", 1), t("hobbit_bone", 1), t(Items.BONE, 1), t("man_flesh", 3));

    // --- Dol Guldur -------------------------------------------------------------------

    public static final LOTRTradeEntries DOL_GULDUR_TRADER_BUY = buy(
            t("dol_guldur_crafting_table", 100), t("dol_guldur_helmet", 20), t("dol_guldur_chestplate", 30),
            t("dol_guldur_leggings", 26), t("dol_guldur_boots", 18), t("dol_guldur_sword", 14), t("dol_guldur_spear", 15),
            t("dol_guldur_dagger", 8), t("poisoned_dol_guldur_dagger", 10), t("dol_guldur_battleaxe", 18),
            t("dol_guldur_spike", 18), t("orc_bomb", 30), brew("orc_draught", 8), t("orc_bow", 16), t(Items.ARROW, 4, 3),
            t("dol_guldur_pickaxe", 14), t("dol_guldur_axe", 14), t("dol_guldur_warhammer", 16), t("maggoty_bread", 4),
            t(Items.STRING, 2))
            .setVessels(LOTRFoods.ORC_DRINK);

    public static final LOTRTradeEntries DOL_GULDUR_TRADER_SELL = sell(
            t("orc_steel_ingot", 3), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3), t("gulduril", 8), t("emerald", 15),
            t("ruby", 12), t("amethyst", 8), t(Items.FEATHER, 2), t("mug", 1), t("waterskin", 2), t(Items.WHEAT, 2, 1),
            t("elf_bone", 1), t("dwarf_bone", 1), t("hobbit_bone", 1), t(Items.BONE, 1), t("man_flesh", 3));

    // --- Isengard ---------------------------------------------------------------------

    public static final LOTRTradeEntries URUK_HAI_TRADER_BUY = buy(
            t("uruk_crafting_table", 100), t("uruk_helmet", 24), t("uruk_chestplate", 36), t("uruk_leggings", 30),
            t("uruk_boots", 22), t("uruk_cleaver", 20), t("uruk_spear", 18), t("uruk_dagger", 9),
            t("poisoned_uruk_dagger", 11), t("uruk_battleaxe", 20), t("uruk_warhammer", 18), t("uruk_pike", 18),
            t("orc_bomb", 30), brew("orc_draught", 8), t("fur", 4), t("uruk_crossbow", 18), t("crossbow_bolt", 4, 3),
            t("uruk_pickaxe", 16), t("uruk_axe", 16), t("maggoty_bread", 4), t("fur_hat", 15), t("fur_tunic", 20),
            t("fur_leggings", 16), t("fur_boots", 12), t("isengard_warg_armor", 25))
            .setVessels(LOTRFoods.ORC_DRINK);

    public static final LOTRTradeEntries URUK_HAI_TRADER_SELL = sell(
            t("uruk_steel_ingot", 3), t("orc_steel_ingot", 3), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3),
            t("diamond", 25), t("ruby", 12), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.LEATHER, 2),
            t(Items.STRING, 3, 1), t(Items.FEATHER, 2), t("elf_bone", 1), t("dwarf_bone", 1), t("hobbit_bone", 1),
            t(Items.BONE, 1), t("pipeweed", 3, 2), t("man_flesh", 3));

    // --- Dunland ----------------------------------------------------------------------

    public static final LOTRTradeEntries DUNLENDING_BARTENDER_BUY = buy(
            t("dunlending_crafting_table", 100), t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7),
            t(Items.COOKED_CHICKEN, 6), t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t(Items.RABBIT_STEW, 10),
            t(Items.BREAD, 5), t("mug", 2), t("ceramic_mug", 2), t("waterskin", 2), t(Items.BAKED_POTATO, 2, 7),
            t(Items.COOKED_COD, 6), t("gammon", 7), t("wooden_plate", 2), brew("ale", 8), brew("mead", 8),
            brew("cider", 8), brew("rum", 12), t("apple_juice", 6))
            .setVessels(LOTRFoods.DUNLENDING_DRINK);

    public static final LOTRTradeEntries DUNLENDING_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t("clay_mug", 1), t(Items.LEATHER, 2), t(Items.POTATO, 2, 1), t(Items.COD, 2),
            t(Items.WHEAT, 2, 1), t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    // --- Near Harad: the Southron coast (the HARAD_ pools serve every Harad market) --

    public static final LOTRTradeEntries NEAR_HARAD_BLACKSMITH_BUY = buy(
            t("near_harad_crafting_table", 100), t("umbaric_scimitar", 15), t("umbaric_dagger", 9),
            t("poisoned_umbaric_dagger", 15), t("umbaric_spear", 16), t("umbaric_pike", 16), t("umbaric_poleaxe", 16),
            t("umbaric_mace", 16), t("haradric_sword", 14), t("haradric_dagger", 8),
            t("poisoned_haradric_dagger", 14), t("haradric_spear", 15), t("haradric_pike", 15),
            t("coast_southron_helmet", 20), t("coast_southron_chestplate", 32), t("coast_southron_leggings", 26),
            t("coast_southron_boots", 17), t("blacksmith_hammer", 18), t(Items.IRON_BARS, 8, 20),
            t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3), t("iron_crossbow", 15), t("bronze_crossbow", 12),
            t("coast_southron_horse_armor", 25))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries NEAR_HARAD_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t("diamond", 25), t("emerald", 15),
            t("ruby", 12), t("amber", 10), t("pearl", 25), t(Items.STRING, 3, 1), t(Items.LEATHER, 2),
            t("bottle_of_poison", 10));

    public static final LOTRTradeEntries NEAR_HARAD_MERCHANT_BUY = buy(
            t("umbaric_scimitar", 20), t("umbaric_spear", 20), t("umbaric_poleaxe", 20), t("umbaric_mace", 20),
            t("umbaric_pike", 20), t("umbaric_dagger", 13), t("poisoned_umbaric_dagger", 16), t("haradric_sword", 18),
            t("haradric_spear", 18), t("haradric_pike", 18), t("haradric_dagger", 12),
            t("poisoned_haradric_dagger", 15), t("gulfen_khopesh", 25), t("coast_southron_helmet", 25),
            t("coast_southron_chestplate", 36), t("coast_southron_leggings", 30), t("coast_southron_boots", 22),
            t("harnennor_helmet", 22), t("harnennor_chestplate", 34), t("harnennor_leggings", 28),
            t("harnennor_boots", 20), t("umbaric_helmet", 28), t("umbaric_chestplate", 40), t("umbaric_leggings", 32),
            t("umbaric_boots", 25), t("gulfen_helmet", 25), t("gulfen_chestplate", 36), t("gulfen_leggings", 30),
            t("gulfen_boots", 22), t("coast_southron_horse_armor", 40), t("umbaric_horse_armor", 40),
            t("harad_bow", 20), t(Items.ARROW, 4, 3), t("morwaith_sword", 18), brew("arak", 10),
            barrel("arak", 2, 120), barrel("arak", 3, 160), t("date", 4, 8), t("date_palm_planks", 4, 8),
            t("date_palm_sapling", 50), t("lemon", 5), t("orange", 5), t("lime", 5), t("plum", 5), t("olives", 5),
            t("almond", 5), t("marchpane", 10), t("chocolate_marchpane", 15), t("marchpane_block", 25),
            t(Items.COCOA_BEANS, 6), t("lemon_cake", 16), t("orange_juice", 10), t("lemonade", 10),
            brew("lemon_liqueur", 10), barrel("lemon_liqueur", 2, 120), barrel("lemon_liqueur", 3, 160),
            brew("lime_liqueur", 10), barrel("lime_liqueur", 2, 120), barrel("lime_liqueur", 3, 160),
            t("lemon_planks", 4, 8), t("orange_planks", 4, 8), t("lime_planks", 4, 8), t("red_harad_flower", 5),
            t("yellow_harad_flower", 5), t("harad_flower_daisy", 8), t("pink_harad_flower", 8),
            t("flame_of_harad", 12), t("hibiscus", 8), t("banana", 5), brew("banana_beer", 15), t("mango", 5),
            t("mango_juice", 10), t("lion_fur", 5), t("cooked_lion", 8), t("cooked_rhino", 8), t("cooked_camel", 8),
            t("rhino_horn", 6), t("gemsbok_hide", 4), t(Items.LAPIS_LAZULI, 1), t(Items.LAPIS_ORE, 8),
            t("diamond", 40), t("emerald", 25), t("sapphire", 20), t("opal", 15), t("amber", 15), t("amethyst", 12),
            t("topaz", 12), t("pearl", 50), t("kebab", 10), t("shish_kebab", 10), t("kebab_stand", 40),
            t("kebab_stand_sand", 60))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries NEAR_HARAD_MERCHANT_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t("ruby", 12), t(Items.BREAD, 2),
            t(Items.WHEAT, 2, 2), t("mug", 1), t(Items.WATER_BUCKET, 4), t(Items.SUGAR, 2, 1));

    public static final LOTRTradeEntries SOUTHRON_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t("cooked_mutton", 7), t(Items.COOKED_PORKCHOP, 7), t(Items.COOKED_CHICKEN, 6),
            t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t(Items.RABBIT_STEW, 10), t(Items.COOKED_COD, 6),
            t("cooked_camel", 6), t("kebab", 8), t("shish_kebab", 8), t(Items.BREAD, 5), t("olive_bread", 6),
            t(Items.BAKED_POTATO, 2, 7), t("orange", 3), t("plum", 3), t("lemon_cake", 12), t("orange_juice", 6),
            t("stoneware_plate", 2), t("wooden_plate", 2), t("mug", 2), t("ceramic_mug", 2), t("copper_goblet", 8),
            t("wooden_cup", 2), t("waterskin", 2), brew("ale", 8), brew("cider", 8), brew("arak", 10),
            brew("lemon_liqueur", 12), brew("lime_liqueur", 12), brew("cactus_liqueur", 12))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries SOUTHRON_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t(Items.COD, 2), t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1),
            t(Items.WHEAT, 2, 1), t(Items.APPLE, 1), t("green_apple", 1), t("date", 2), t("orange", 2), t("lemon", 2),
            t("lime", 2), t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    /** The branding iron (16) comes with the branding iron item. */
    public static final LOTRTradeEntries HARAD_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("turnip", 3), t("corn", 2), t("corn_stalk", 1), t("orange", 3), t("lemon", 3), t("lime", 3),
            t("almond", 2), t("plum", 3), t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8),
            t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries HARAD_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("olive_bread", 5), t(Items.CAKE, 12), t("lemon_cake", 12), t("apple_crumble", 12),
            t("berry_pie", 12), t("marchpane", 5), t("chocolate_marchpane", 8), t("marchpane_block", 16),
            t("stoneware_plate", 2), t("wooden_plate", 2))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.EGG, 2, 1), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4), t("lemon", 1), t(Items.APPLE, 1), t("green_apple", 1),
            t("blueberries", 1), t("blackberries", 1), t("raspberries", 1), t("cranberries", 1), t("elderberries", 1),
            t("almond", 1), t("olives", 1), t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries HARAD_BREWER_BUY = buy(
            brew("ale", 10), barrel("ale", 2, 120), barrel("ale", 3, 170), brew("cider", 10), barrel("cider", 2, 120),
            barrel("cider", 3, 170), brew("arak", 10), barrel("arak", 2, 120), barrel("arak", 3, 170),
            brew("cactus_liqueur", 10), barrel("cactus_liqueur", 2, 120), barrel("cactus_liqueur", 3, 170),
            brew("lemon_liqueur", 12), barrel("lemon_liqueur", 2, 140), barrel("lemon_liqueur", 3, 180),
            brew("lime_liqueur", 12), barrel("lime_liqueur", 2, 140), barrel("lime_liqueur", 3, 180),
            brew("mead", 15), brew("pomegranate_wine", 15), brew("cherry_liqueur", 15), brew("melon_liqueur", 15),
            brew("banana_beer", 15), t("apple_juice", 6), t("orange_juice", 6), t("chocolate", 6),
            t("blueberry_juice", 5), t("blackberry_juice", 5), t("raspberry_juice", 5), t("cranberry_juice", 5),
            t("elderberry_juice", 5), t("pomegranate_juice", 10), t("mango_juice", 8))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_BREWER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1),
            t(Items.APPLE, 1), t("green_apple", 1), t("cherries", 2), t("date", 1), t(Items.CACTUS, 1),
            t("orange", 1), t("lemon", 1), t("lime", 1), t("pomegranate", 2), t("banana", 2),
            t(Items.MELON_SLICE, 2, 1), t("mango", 2));

    public static final LOTRTradeEntries HARAD_BUTCHER_BUY = buy(
            t(Items.BEEF, 5), t(Items.PORKCHOP, 5), t("raw_mutton", 5), t(Items.CHICKEN, 4), t(Items.RABBIT, 4),
            t("raw_venison", 4), t("raw_camel", 4), t(Items.LEATHER, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_BUTCHER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t(Items.IRON_INGOT, 3), t(Items.LEAD, 4),
            t(Items.WHEAT, 3, 1), t("salt", 10));

    public static final LOTRTradeEntries HARAD_FISHMONGER_BUY = buy(
            t(Items.COD, 4), t(Items.SALMON, 6), t(Items.TROPICAL_FISH, 8), t(Items.PUFFERFISH, 12),
            t(Items.FISHING_ROD, 8), t(Items.OAK_BOAT, 5), t(Items.INK_SAC, 4), t(Items.SPONGE, 15), t("coral", 9),
            t("pearl", 50), t(Items.LEATHER_BOOTS, 5))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_FISHMONGER_SELL = sell(
            t(Items.STICK, 8, 1), t(Items.OAK_PLANKS, 4, 1), t(Items.STRING, 3, 1), t(Items.BUCKET, 3),
            t(Items.IRON_INGOT, 3), t("iron_dagger", 3), t("bronze_dagger", 3), t("salt", 10));

    public static final LOTRTradeEntries HARAD_FLORIST_BUY = buy(
            t("red_harad_flower", 3), t("yellow_harad_flower", 3), t("harad_flower_daisy", 3),
            t("pink_harad_flower", 3), t("hibiscus", 8), t("flame_of_harad", 10), t("rhun_flower_chrys_blue", 20),
            t("rhun_flower_chrys_orange", 20), t("rhun_flower_chrys_pink", 20), t("rhun_flower_chrys_yellow", 20),
            t("rhun_flower_chrys_white", 20), t("black_iris", 25), t(Items.POPPY, 6), t(Items.RED_TULIP, 10),
            t(Items.ORANGE_TULIP, 10), t(Items.WHITE_TULIP, 10), t(Items.PINK_TULIP, 10), t(Items.OXEYE_DAISY, 6),
            t(Items.DANDELION, 6), t("bluebell", 10), t("marigold", 10), t("lavender", 10), t(Items.SUNFLOWER, 10),
            t(Items.LILAC, 15), t(Items.ROSE_BUSH, 15), t(Items.PEONY, 15), t("yellow_iris", 15),
            t("morgul_shroom", 20))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_FLORIST_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1), t("emerald", 15), t("ruby", 12), t("amber", 10));

    public static final LOTRTradeEntries HARAD_GOLDSMITH_BUY = buy(
            t("gold_ring", 15), t("silver_ring", 12), t("golden_goblet", 27), t("silver_goblet", 18),
            t("copper_goblet", 8), t("gold_chandelier", 15), t("silver_chandelier", 12), t("gold_bird_cage", 20),
            t("silver_bird_cage", 15), t("gold_bars", 8), t("silver_bars", 6))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_GOLDSMITH_SELL = sell(
            t(Items.GOLD_INGOT, 15), t(Items.GOLD_NUGGET, 2), t("diamond", 25), t("emerald", 15), t("sapphire", 12),
            t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8), t("topaz", 8), t("pearl", 25),
            t("coral", 6));

    public static final LOTRTradeEntries HARAD_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4), t(Items.BIRCH_LOG, 8), t(Items.BIRCH_PLANKS, 4, 8), t("birch_beam", 3, 8),
            t(Items.BIRCH_LEAVES, 8), t(Items.BIRCH_SAPLING, 16), t(Items.BIRCH_LOG, 4), t(Items.BIRCH_PLANKS, 4, 4),
            t("birch_beam", 3, 4), t(Items.BIRCH_LEAVES, 4), t(Items.BIRCH_SAPLING, 8), t(Items.ACACIA_LOG, 6),
            t(Items.ACACIA_PLANKS, 4, 6), t("acacia_beam", 3, 6), t(Items.ACACIA_LEAVES, 6),
            t(Items.ACACIA_SAPLING, 12), t("apple_log", 5), t("apple_planks", 4, 5), t("apple_beam", 3, 5),
            t("apple_leaves", 5), t("apple_sapling", 10), t("pear_log", 5), t("pear_planks", 4, 5),
            t("pear_beam", 3, 5), t("pear_leaves", 5), t("pear_sapling", 10), t("beech_log", 4),
            t("beech_planks", 4, 4), t("beech_beam", 3, 4), t("beech_leaves", 4), t("beech_sapling", 8),
            t("date_palm_log", 5), t("date_palm_planks", 4, 5), t("date_palm_beam", 3, 5), t("date_palm_leaves", 5),
            t("date_palm_sapling", 10), t("cedar_log", 3), t("cedar_planks", 4, 3), t("cedar_beam", 3, 3),
            t("cedar_leaves", 3), t("cedar_sapling", 6), t("pine_log", 8), t("pine_planks", 4, 8),
            t("pine_beam", 3, 8), t("pine_leaves", 8), t("pine_sapling", 16), t("lemon_log", 5),
            t("lemon_planks", 4, 5), t("lemon_beam", 3, 5), t("lemon_leaves", 5), t("lemon_sapling", 10),
            t("orange_log", 5), t("orange_planks", 4, 5), t("orange_beam", 3, 5), t("orange_leaves", 5),
            t("orange_sapling", 10), t("lime_log", 5), t("lime_planks", 4, 5), t("lime_beam", 3, 5),
            t("lime_leaves", 5), t("lime_sapling", 10), t("willow_log", 4), t("willow_planks", 4, 4),
            t("willow_beam", 3, 4), t("willow_leaves", 4), t("willow_sapling", 8), t("cypress_log", 3),
            t("cypress_planks", 4, 3), t("cypress_beam", 3, 3), t("cypress_leaves", 3), t("cypress_sapling", 6),
            t("olive_log", 5), t("olive_planks", 4, 5), t("olive_beam", 3, 5), t("olive_leaves", 5),
            t("olive_sapling", 10), t("almond_log", 5), t("almond_planks", 4, 5), t("almond_beam", 3, 5),
            t("almond_leaves", 5), t("almond_sapling", 10), t("plum_log", 5), t("plum_planks", 4, 5),
            t("plum_beam", 3, 5), t("plum_leaves", 5), t("plum_sapling", 10), t("palm_log", 5),
            t("palm_planks", 4, 5), t("palm_beam", 3, 5), t("palm_leaves", 5), t("palm_sapling", 10),
            t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    public static final LOTRTradeEntries HARAD_MASON_BUY = buy(
            t("near_harad_crafting_table", 100), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1),
            t(Items.STONE_BRICKS, 8, 2), t(Items.TERRACOTTA, 8, 4), t(Items.SANDSTONE, 8, 2),
            t(Items.RED_SANDSTONE, 8, 4), t("white_sandstone", 8, 6), t("near_harad_brick", 8, 3),
            t("near_harad_carved_brick", 2), t("near_harad_pillar", 4, 3), t("near_harad_red_brick", 8, 6),
            t("near_harad_red_carved_brick", 4), t("near_harad_red_pillar", 4, 6))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_MASON_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.TORCH, 16, 2), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.BREAD, 2), t("olive_bread", 3), t(Items.COOKED_BEEF, 3),
            t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3), t("cooked_venison", 3),
            t("cooked_camel", 3), t("kebab", 3));

    public static final LOTRTradeEntries HARAD_MINER_BUY = buy(
            t(Items.COAL, 2, 4), t(Items.IRON_ORE, 8), t(Items.COPPER_ORE, 7), t("tin_ore", 7), t("silver_ore", 12),
            t(Items.GOLD_ORE, 22), t(Items.LAPIS_LAZULI, 1), t(Items.GLOWSTONE_DUST, 4, 3), t(Items.LAVA_BUCKET, 20),
            t(Items.FLINT, 2), t("sulfur", 6), t("niter", 6), t("salt", 16), t("diamond", 40), t("emerald", 25),
            t("sapphire", 20), t("ruby", 20), t("opal", 15), t("amber", 15), t("amethyst", 12), t("topaz", 12))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_MINER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.IRON_PICKAXE, 8), t("bronze_pickaxe", 6), t(Items.STONE_PICKAXE, 1),
            t(Items.TORCH, 16, 2));

    // --- Umbar -------------------------------------------------------------------------

    public static final LOTRTradeEntries UMBAR_BLACKSMITH_BUY = buy(
            t("umbar_crafting_table", 100), t("umbaric_scimitar", 15), t("umbaric_dagger", 9),
            t("poisoned_umbaric_dagger", 15), t("umbaric_spear", 16), t("umbaric_pike", 16), t("umbaric_poleaxe", 16),
            t("umbaric_mace", 16), t("umbaric_helmet", 22), t("umbaric_chestplate", 35), t("umbaric_leggings", 29),
            t("umbaric_boots", 19), t("corsair_eket", 15), t("corsair_dagger", 9), t("poisoned_corsair_dagger", 15),
            t("corsair_harpoon", 16), t("corsair_battleaxe", 16), t("corsair_helmet", 20),
            t("corsair_chestplate", 32), t("corsair_leggings", 26), t("corsair_boots", 17),
            t("blacksmith_hammer", 18), t(Items.IRON_BARS, 8, 20), t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3),
            t("iron_crossbow", 15), t("bronze_crossbow", 12), t("umbaric_horse_armor", 25))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries UMBAR_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t("diamond", 25), t("emerald", 15),
            t("ruby", 12), t("sapphire", 10), t("pearl", 25), t(Items.LEATHER, 2), t("bottle_of_poison", 10),
            t(Items.STRING, 3, 1));

    public static final LOTRTradeEntries UMBAR_MASON_BUY = buy(
            t("umbar_crafting_table", 100), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1),
            t(Items.STONE_BRICKS, 8, 2), t(Items.TERRACOTTA, 8, 4), t(Items.SANDSTONE, 8, 2),
            t(Items.RED_SANDSTONE, 8, 4), t("white_sandstone", 8, 6), t("near_harad_brick", 8, 3),
            t("near_harad_carved_brick", 2), t("near_harad_pillar", 4, 3), t("near_harad_red_brick", 8, 6),
            t("near_harad_red_carved_brick", 4), t("near_harad_red_pillar", 4, 6), t("umbar_brick", 8, 3),
            t("carved_umbar_brick", 2), t("umbar_pillar", 4, 3), t("mordor_rock", 8, 8), t("numenorean_brick", 8, 8),
            t("carved_black_umbar_brick", 8), t("numenorean_pillar", 4, 8))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries UMBAR_MASON_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.TORCH, 16, 2), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.BREAD, 2), t("olive_bread", 3), t(Items.COOKED_BEEF, 3),
            t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3), t("cooked_venison", 3),
            t("cooked_camel", 3), t("kebab", 3));

    // --- Harnedor ----------------------------------------------------------------------

    public static final LOTRTradeEntries HARNEDOR_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t("cooked_mutton", 7), t(Items.COOKED_PORKCHOP, 7), t(Items.COOKED_CHICKEN, 6),
            t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t(Items.RABBIT_STEW, 10), t(Items.COOKED_COD, 6),
            t("cooked_camel", 6), t("kebab", 8), t("shish_kebab", 8), t(Items.BREAD, 5), t("olive_bread", 6),
            t(Items.BAKED_POTATO, 2, 7), t("orange", 3), t("plum", 3), t("orange_juice", 6), t("stoneware_plate", 2),
            t("wooden_plate", 2), t("mug", 2), t("ceramic_mug", 2), t("copper_goblet", 8), t("wooden_cup", 2),
            t("waterskin", 2), brew("ale", 8), brew("cider", 8), brew("arak", 10), brew("lemon_liqueur", 15),
            brew("lime_liqueur", 15), brew("cactus_liqueur", 15))
            .setVessels(LOTRFoods.HARNEDOR_DRINK);

    public static final LOTRTradeEntries HARNEDOR_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t(Items.COD, 2), t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1),
            t(Items.WHEAT, 2, 1), t(Items.APPLE, 1), t("green_apple", 1), t("date", 2), t("orange", 2), t("lemon", 3),
            t("lime", 3), t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    public static final LOTRTradeEntries HARNEDOR_BLACKSMITH_BUY = buy(
            t("near_harad_crafting_table", 100), t("haradric_sword", 14), t("haradric_dagger", 8),
            t("poisoned_haradric_dagger", 14), t("haradric_spear", 15), t("haradric_pike", 15),
            t("harnennor_helmet", 20), t("harnennor_chestplate", 32), t("harnennor_leggings", 26),
            t("harnennor_boots", 17), t("blacksmith_hammer", 18), t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3),
            t("bronze_crossbow", 12))
            .setVessels(LOTRFoods.HARNEDOR_DRINK);

    public static final LOTRTradeEntries HARNEDOR_BLACKSMITH_SELL = sell(
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t("emerald", 15), t("ruby", 12), t("amber", 10), t("topaz", 12),
            t(Items.STRING, 3, 1), t(Items.LEATHER, 2), t("bottle_of_poison", 10));

    public static final LOTRTradeEntries NOMAD_ARMOURER_BUY = buy(
            t("near_harad_crafting_table", 100), t("haradric_sword", 14), t("haradric_dagger", 8),
            t("poisoned_haradric_dagger", 14), t("haradric_spear", 15), t("haradric_pike", 15), t("nomad_cap", 20),
            t("nomad_tunic", 30), t("nomad_leggings", 25), t("nomad_shoes", 16), t("harad_turban", 18),
            t("harad_robe", 25), t("harad_robe_leggings", 22), t("harad_robe_shoes", 15), t("blacksmith_hammer", 18),
            t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3), t("bronze_crossbow", 12))
            .setVessels(LOTRFoods.NOMAD_DRINK);

    public static final LOTRTradeEntries NOMAD_ARMOURER_SELL = sell(
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t(Items.STRING, 3, 1), t("dried_reeds", 1),
            t(Items.WOOL.white(), 2), t("emerald", 15), t("ruby", 12), t("amber", 10), t("topaz", 12),
            t(Items.LEATHER, 2), t("bottle_of_poison", 10));

    public static final LOTRTradeEntries NOMAD_MERCHANT_BUY = buy(
            t("umbaric_scimitar", 16), t("umbaric_spear", 16), t("umbaric_poleaxe", 17), t("umbaric_mace", 17),
            t("umbaric_pike", 17), t("umbaric_dagger", 12), t("poisoned_umbaric_dagger", 15), t("haradric_sword", 15),
            t("haradric_spear", 15), t("haradric_pike", 16), t("haradric_dagger", 11),
            t("poisoned_haradric_dagger", 14), t("gulfen_khopesh", 20), t("coast_southron_helmet", 25),
            t("coast_southron_chestplate", 36), t("coast_southron_leggings", 30), t("coast_southron_boots", 22),
            t("harnennor_helmet", 22), t("harnennor_chestplate", 34), t("harnennor_leggings", 28),
            t("harnennor_boots", 20), t("umbaric_helmet", 28), t("umbaric_chestplate", 40), t("umbaric_leggings", 32),
            t("umbaric_boots", 25), t("gulfen_helmet", 25), t("gulfen_chestplate", 36), t("gulfen_leggings", 30),
            t("gulfen_boots", 22), t("coast_southron_horse_armor", 40), t("umbaric_horse_armor", 40),
            t("harad_bow", 20), t(Items.ARROW, 4, 3), t("lion_fur", 4), t("topaz", 12), t("bottle_of_poison", 10),
            t(Items.SANDSTONE, 4, 1), t(Items.RED_SANDSTONE, 4, 5))
            .setVessels(LOTRFoods.NOMAD_DRINK);

    public static final LOTRTradeEntries NOMAD_MERCHANT_SELL = sell(
            t(Items.GOLD_INGOT, 15), t(Items.IRON_INGOT, 3), t("bronze_ingot", 3), t("diamond", 25), t("emerald", 15),
            t("sapphire", 12), t("ruby", 12), t("opal", 10), t("amber", 10), t("pearl", 25), t(Items.BREAD, 2),
            t("date", 2), t("orange", 3), t("lemon", 3), t("lime", 3), t("kebab", 4), t(Items.WATER_BUCKET, 4),
            t("arak", 4), t(Items.SUGAR, 1), t("salt", 10));

    public static final LOTRTradeEntries GULF_BLACKSMITH_BUY = buy(
            t("gulf_crafting_table", 100), t("gulfen_khopesh", 15), t("haradric_dagger", 8),
            t("poisoned_haradric_dagger", 14), t("haradric_spear", 15), t("haradric_pike", 15),
            t("gulfen_helmet", 20), t("gulfen_chestplate", 32), t("gulfen_leggings", 26), t("gulfen_boots", 17),
            t("blacksmith_hammer", 18), t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3), t("bronze_crossbow", 12))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries GULF_BLACKSMITH_SELL = sell(
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3),
            t("tin_ingot", 3), t("bronze_ingot", 3), t("dried_reeds", 1), t("emerald", 15), t("ruby", 12),
            t("amber", 10), t("topaz", 12), t(Items.LEATHER, 2), t(Items.STRING, 3, 1), t("bottle_of_poison", 10));

    public static final LOTRTradeEntries GULF_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t("cooked_mutton", 7), t(Items.COOKED_PORKCHOP, 7), t(Items.COOKED_CHICKEN, 6),
            t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t(Items.RABBIT_STEW, 10), t(Items.COOKED_COD, 6),
            t("cooked_camel", 6), t("kebab", 8), t("shish_kebab", 8), t("cooked_lion", 12), t("cooked_zebra", 10),
            t("cooked_rhino", 12), t(Items.BREAD, 5), t("olive_bread", 6), t(Items.BAKED_POTATO, 2, 7),
            t("orange", 3), t("plum", 3), t("lemon_cake", 12), t("orange_juice", 6), t("stoneware_plate", 2),
            t("wooden_plate", 2), t("mug", 2), t("ceramic_mug", 2), t("copper_goblet", 8), t("wooden_cup", 2),
            t("waterskin", 2), t("skull_cup", 6), brew("ale", 8), brew("cider", 8), brew("arak", 10),
            brew("lemon_liqueur", 12), brew("lime_liqueur", 12), brew("cactus_liqueur", 12), brew("banana_beer", 12),
            t("mango_juice", 10))
            .setVessels(LOTRFoods.GULF_HARAD_DRINK);

    public static final LOTRTradeEntries GULF_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t(Items.COD, 2), t("raw_lion", 3), t("raw_zebra", 3), t("raw_rhino", 3),
            t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1), t(Items.WHEAT, 2, 1), t(Items.APPLE, 1),
            t("green_apple", 1), t("date", 2), t("orange", 2), t("lemon", 2), t("lime", 2), t("banana", 3),
            t("mango", 3), t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    public static final LOTRTradeEntries GULF_HUNTER_BUY = buy(
            t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t("fur", 3), t(Items.FEATHER, 3),
            t("horn", 4), t("lion_fur", 6), t("gemsbok_hide", 4), t("gemsbok_horn", 12), t("rhino_horn", 15),
            t("raw_lion", 8), t("raw_zebra", 6), t("raw_rhino", 8), t(Items.ROTTEN_FLESH, 3))
            .setVessels(LOTRFoods.GULF_HARAD_DRINK);

    public static final LOTRTradeEntries GULF_HUNTER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t("haradric_dagger", 3), t("iron_spear", 5),
            t("bronze_spear", 5), t("haradric_spear", 5), t(Items.ARROW, 4, 2), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("bottle_of_poison", 10));

    public static final LOTRTradeEntries GULF_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("olive_bread", 5), t(Items.CAKE, 12), t("lemon_cake", 12), t("banana_cake", 16),
            t("berry_pie", 12), t("marchpane", 5), t("chocolate_marchpane", 8), t("marchpane_block", 16),
            t("stoneware_plate", 2), t("wooden_plate", 2))
            .setVessels(LOTRFoods.GULF_HARAD_DRINK);

    public static final LOTRTradeEntries GULF_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.EGG, 2, 1), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4), t("lemon", 1), t("banana", 2), t("blueberries", 1),
            t("blackberries", 1), t("raspberries", 1), t("cranberries", 1), t("elderberries", 1), t("almond", 1),
            t("olives", 1), t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries GULF_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4), t(Items.BIRCH_LOG, 4), t(Items.BIRCH_PLANKS, 4, 4), t("birch_beam", 3, 4),
            t(Items.BIRCH_LEAVES, 4), t(Items.BIRCH_SAPLING, 8), t(Items.JUNGLE_LOG, 6), t(Items.JUNGLE_PLANKS, 4, 6),
            t("jungle_beam", 3, 6), t(Items.JUNGLE_LEAVES, 6), t(Items.JUNGLE_SAPLING, 12), t(Items.ACACIA_LOG, 4),
            t(Items.ACACIA_PLANKS, 4, 4), t("acacia_beam", 3, 4), t(Items.ACACIA_LEAVES, 4),
            t(Items.ACACIA_SAPLING, 8), t("apple_log", 5), t("apple_planks", 4, 5), t("apple_beam", 3, 5),
            t("apple_leaves", 5), t("apple_sapling", 10), t("pear_log", 5), t("pear_planks", 4, 5),
            t("pear_beam", 3, 5), t("pear_leaves", 5), t("pear_sapling", 10), t("mango_log", 6),
            t("mango_planks", 4, 6), t("mango_beam", 3, 6), t("mango_leaves", 6), t("mango_sapling", 12),
            t("beech_log", 4), t("beech_planks", 4, 4), t("beech_beam", 3, 4), t("beech_leaves", 4),
            t("beech_sapling", 8), t("banana_log", 6), t("banana_planks", 4, 6), t("banana_beam", 3, 6),
            t("banana_leaves", 6), t("banana_sapling", 12), t("date_palm_log", 5), t("date_palm_planks", 4, 5),
            t("date_palm_beam", 3, 5), t("date_palm_leaves", 5), t("date_palm_sapling", 10), t("cedar_log", 4),
            t("cedar_planks", 4, 4), t("cedar_beam", 3, 4), t("cedar_leaves", 4), t("cedar_sapling", 8),
            t("lemon_log", 5), t("lemon_planks", 4, 5), t("lemon_beam", 3, 5), t("lemon_leaves", 5),
            t("lemon_sapling", 10), t("orange_log", 5), t("orange_planks", 4, 5), t("orange_beam", 3, 5),
            t("orange_leaves", 5), t("orange_sapling", 10), t("lime_log", 5), t("lime_planks", 4, 5),
            t("lime_beam", 3, 5), t("lime_leaves", 5), t("lime_sapling", 10), t("mahogany_log", 8),
            t("mahogany_planks", 4, 8), t("mahogany_beam", 3, 8), t("mahogany_leaves", 8), t("mahogany_sapling", 16),
            t("willow_log", 4), t("willow_planks", 4, 4), t("willow_beam", 3, 4), t("willow_leaves", 4),
            t("willow_sapling", 8), t("cypress_log", 3), t("cypress_planks", 4, 3), t("cypress_beam", 3, 3),
            t("cypress_leaves", 3), t("cypress_sapling", 6), t("olive_log", 5), t("olive_planks", 4, 5),
            t("olive_beam", 3, 5), t("olive_leaves", 5), t("olive_sapling", 10), t("almond_log", 5),
            t("almond_planks", 4, 5), t("almond_beam", 3, 5), t("almond_leaves", 5), t("almond_sapling", 10),
            t("plum_log", 5), t("plum_planks", 4, 5), t("plum_beam", 3, 5), t("plum_leaves", 5),
            t("plum_sapling", 10), t("palm_log", 3), t("palm_planks", 4, 3), t("palm_beam", 3, 3),
            t("palm_leaves", 3), t("palm_sapling", 6), t("dragon_log", 6), t("dragon_planks", 4, 6),
            t("dragon_beam", 3, 6), t("dragon_leaves", 6), t("dragon_sapling", 12), t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.GULF_HARAD_DRINK);

    public static final LOTRTradeEntries GULF_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    public static final LOTRTradeEntries RHUN_BLACKSMITH_BUY = buy(
            t("rhun_crafting_table", 100), t("rhunic_sword", 15), t("rhunic_dagger", 9), t("rhunic_spear", 16),
            t("rhunic_pike", 16), t("rhunic_bardiche", 16), t("rhunic_battleaxe", 18), t("rhunic_helmet", 20),
            t("rhunic_chestplate", 32), t("rhunic_leggings", 26), t("rhunic_boots", 17),
            t("golden_rhunic_helmet", 30), t("golden_rhunic_chestplate", 45), t("golden_rhunic_leggings", 35),
            t("golden_rhunic_boots", 25), t("blacksmith_hammer", 18), t(Items.IRON_BARS, 8, 20),
            t("bronze_bars", 8, 20), t("crossbow_bolt", 4, 3), t("iron_crossbow", 15), t("bronze_crossbow", 12),
            t("rhunic_horse_armor", 25))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_BLACKSMITH_SELL = sell(
            t(Items.IRON_INGOT, 3), t("gilded_iron_ingot", 4), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL),
            t(Items.GOLD_INGOT, 15), t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3),
            t(Items.STRING, 3, 1), t("diamond", 25), t("ruby", 12), t("amber", 10), t("topaz", 8), t("pearl", 25),
            t(Items.LEATHER, 2));

    public static final LOTRTradeEntries RHUN_LUMBERMAN_BUY = buy(
            t(Items.OAK_LOG, 2), t(Items.OAK_PLANKS, 4, 2), t("oak_beam", 3, 2), t(Items.OAK_LEAVES, 2),
            t(Items.OAK_SAPLING, 4), t(Items.SPRUCE_LOG, 4), t(Items.SPRUCE_PLANKS, 4, 4), t("spruce_beam", 3, 4),
            t(Items.SPRUCE_LEAVES, 4), t(Items.SPRUCE_SAPLING, 8), t(Items.BIRCH_LOG, 3), t(Items.BIRCH_PLANKS, 4, 3),
            t("birch_beam", 3, 3), t(Items.BIRCH_LEAVES, 3), t(Items.BIRCH_SAPLING, 6), t("apple_log", 5),
            t("apple_planks", 4, 5), t("apple_beam", 3, 5), t("apple_leaves", 5), t("apple_sapling", 10),
            t("pear_log", 5), t("pear_planks", 4, 5), t("pear_beam", 3, 5), t("pear_leaves", 5),
            t("pear_sapling", 10), t("cherry_log", 8), t("cherry_planks", 4, 8), t("cherry_beam", 3, 8),
            t("cherry_leaves", 8), t("cherry_sapling", 16), t("beech_log", 3), t("beech_planks", 4, 3),
            t("beech_beam", 3, 3), t("beech_leaves", 3), t("beech_sapling", 6), t("maple_log", 4),
            t("maple_planks", 4, 4), t("maple_beam", 3, 4), t("maple_leaves", 4), t("maple_sapling", 8),
            t("larch_log", 4), t("larch_planks", 4, 4), t("larch_beam", 3, 4), t("larch_leaves", 4),
            t("larch_sapling", 8), t("date_palm_log", 8), t("date_palm_planks", 4, 8), t("date_palm_beam", 3, 8),
            t("date_palm_leaves", 8), t("date_palm_sapling", 16), t("cedar_log", 4), t("cedar_planks", 4, 4),
            t("cedar_beam", 3, 4), t("cedar_leaves", 4), t("cedar_sapling", 8), t("fir_log", 4),
            t("fir_planks", 4, 4), t("fir_beam", 3, 4), t("fir_leaves", 4), t("fir_sapling", 8), t("pine_log", 4),
            t("pine_planks", 4, 4), t("pine_beam", 3, 4), t("pine_leaves", 4), t("pine_sapling", 8),
            t("lemon_log", 7), t("lemon_planks", 4, 7), t("lemon_beam", 3, 7), t("lemon_leaves", 7),
            t("lemon_sapling", 14), t("orange_log", 7), t("orange_planks", 4, 7), t("orange_beam", 3, 7),
            t("orange_leaves", 7), t("orange_sapling", 14), t("lime_log", 7), t("lime_planks", 4, 7),
            t("lime_beam", 3, 7), t("lime_leaves", 7), t("lime_sapling", 14), t("willow_log", 4),
            t("willow_planks", 4, 4), t("willow_beam", 3, 4), t("willow_leaves", 4), t("willow_sapling", 8),
            t("cypress_log", 5), t("cypress_planks", 4, 5), t("cypress_beam", 3, 5), t("cypress_leaves", 5),
            t("cypress_sapling", 10), t("olive_log", 6), t("olive_planks", 4, 6), t("olive_beam", 3, 6),
            t("olive_leaves", 6), t("olive_sapling", 12), t("almond_log", 5), t("almond_planks", 4, 5),
            t("almond_beam", 3, 5), t("almond_leaves", 5), t("almond_sapling", 10), t("plum_log", 5),
            t("plum_planks", 4, 5), t("plum_beam", 3, 5), t("plum_leaves", 5), t("plum_sapling", 10),
            t("redwood_log", 5), t("redwood_planks", 4, 5), t("redwood_beam", 3, 5), t("redwood_leaves", 5),
            t("redwood_sapling", 10), t("pomegranate_log", 5), t("pomegranate_planks", 4, 5),
            t("pomegranate_beam", 3, 5), t("pomegranate_leaves", 5), t("pomegranate_sapling", 10),
            t(Items.STICK, 4, 1))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_LUMBERMAN_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_AXE, 8), t(Items.STONE_AXE, 1),
            t("bronze_axe", 6), t(Items.BONE_MEAL, 6, 1), t(Items.SHEARS, 5));

    public static final LOTRTradeEntries RHUN_MASON_BUY = buy(
            t("rhun_crafting_table", 100), t(Items.STONE, 8, 2), t(Items.COBBLESTONE, 8, 1),
            t(Items.STONE_BRICKS, 8, 2), t("rhun_brick", 8, 3), t("rhun_carved_brick", 2), t("rhun_flowers_brick", 2),
            t("rhun_gold_brick", 2), t("rhun_pillar", 4, 3), t("red_rock", 8, 8), t("rhun_red_brick", 8, 8),
            t("rhun_red_carved_brick", 8), t("rhun_red_pillar", 4, 8), t(Items.TERRACOTTA, 8, 4),
            t(Items.SANDSTONE, 8, 4), t(Items.RED_SANDSTONE, 8, 12), t("white_sandstone", 8, 6))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_MASON_SELL = sell(
            t(Items.IRON_PICKAXE, 8), t(Items.STONE_PICKAXE, 1), t("bronze_pickaxe", 6),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.TORCH, 16, 2), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.BREAD, 2), t("olive_bread", 3), t(Items.COOKED_BEEF, 3),
            t(Items.COOKED_PORKCHOP, 3), t("cooked_mutton", 3), t(Items.COOKED_CHICKEN, 3), t(Items.COOKED_RABBIT, 3),
            t("cooked_venison", 3));

    public static final LOTRTradeEntries RHUN_BUTCHER_BUY = buy(
            t(Items.BEEF, 5), t(Items.PORKCHOP, 5), t("gammon", 6), t("raw_mutton", 5), t(Items.CHICKEN, 4),
            t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_BUTCHER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t(Items.IRON_INGOT, 3), t(Items.LEAD, 4),
            t(Items.WHEAT, 3, 1), t("salt", 10));

    public static final LOTRTradeEntries RHUN_BREWER_BUY = buy(
            brew("ale", 10), barrel("ale", 2, 120), barrel("ale", 3, 170), brew("mead", 10), barrel("mead", 2, 120),
            barrel("mead", 3, 170), brew("cider", 10), barrel("cider", 2, 120), barrel("cider", 3, 170),
            brew("arak", 10), barrel("arak", 2, 120), barrel("arak", 3, 170), brew("soured_milk", 10),
            barrel("soured_milk", 2, 120), barrel("soured_milk", 3, 170), brew("plum_kvass", 10),
            barrel("plum_kvass", 2, 120), barrel("plum_kvass", 3, 170), brew("vodka", 10), barrel("vodka", 2, 120),
            barrel("vodka", 3, 170), brew("red_wine", 15), barrel("red_wine", 2, 160), barrel("red_wine", 3, 200),
            brew("white_wine", 15), barrel("white_wine", 2, 160), barrel("white_wine", 3, 200),
            brew("pomegranate_wine", 10), barrel("pomegranate_wine", 2, 120), barrel("pomegranate_wine", 3, 170),
            t("apple_juice", 6), t("blueberry_juice", 5), t("blackberry_juice", 5), t("raspberry_juice", 5),
            t("cranberry_juice", 5), t("elderberry_juice", 5), t("red_grape_juice", 8), t("green_grape_juice", 8),
            t("pomegranate_juice", 5))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_BREWER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1),
            t(Items.APPLE, 1), t("green_apple", 1), t(Items.MILK_BUCKET, 4), t("date", 1), t("plum", 1),
            t(Items.POTATO, 1), t("red_grapes", 1), t("green_grapes", 1), t("pomegranate", 1));

    public static final LOTRTradeEntries RHUN_FISHMONGER_BUY = buy(
            t(Items.COD, 4), t(Items.SALMON, 6), t(Items.TROPICAL_FISH, 8), t(Items.PUFFERFISH, 12),
            t(Items.FISHING_ROD, 8), t(Items.OAK_BOAT, 5), t(Items.INK_SAC, 4), t(Items.SPONGE, 15), t("coral", 9),
            t("pearl", 50), t(Items.LEATHER_BOOTS, 5))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_FISHMONGER_SELL = sell(
            t(Items.STICK, 8, 1), t(Items.OAK_PLANKS, 4, 1), t(Items.STRING, 3, 1), t(Items.BUCKET, 3),
            t(Items.IRON_INGOT, 3), t("iron_dagger", 3), t("bronze_dagger", 3), t("salt", 10));

    public static final LOTRTradeEntries RHUN_BAKER_BUY = buy(
            t(Items.BREAD, 5), t("corn_bread", 5), t("olive_bread", 6), t(Items.CAKE, 12), t("apple_crumble", 12),
            t("berry_pie", 12), t("cherry_pie", 12), t("stoneware_plate", 2), t("wooden_plate", 2))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_BAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t(Items.SUGAR, 2, 1), t(Items.EGG, 2, 1), t(Items.BUCKET, 3),
            t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4), t(Items.APPLE, 1), t("green_apple", 1),
            t("cherries", 2), t("blueberries", 1), t("blackberries", 1), t("raspberries", 1), t("cranberries", 1),
            t("elderberries", 1), t("olives", 2), t("salt", 10), t("clay_plate", 1));

    public static final LOTRTradeEntries RHUN_HUNTER_BUY = buy(
            t(Items.BEEF, 5), t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t("fur", 3),
            t(Items.FEATHER, 3), t("horn", 4), t("kine_of_araw_horn", 50), t(Items.ROTTEN_FLESH, 3))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_HUNTER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t("rhunic_dagger", 3), t("iron_spear", 5),
            t("bronze_spear", 5), t("rhunic_spear", 5), t(Items.ARROW, 4, 2), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("bottle_of_poison", 10));

    /** Without the branding iron (16), not ported yet. */
    public static final LOTRTradeEntries RHUN_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("leek", 3), t("turnip", 3), t("corn", 2), t("corn_stalk", 1), t("red_grapes", 4), t("green_grapes", 4),
            t("pomegranate", 3), t(Items.WOOL.white(), 2), t(Items.MILK_BUCKET, 8), t(Items.LEAD, 8),
            t(Items.HAY_BLOCK, 12))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries RHUN_GOLDSMITH_BUY = buy(
            t("gold_ring", 15), t("silver_ring", 12), t("golden_goblet", 27), t("silver_goblet", 18),
            t("copper_goblet", 8), t("gold_chandelier", 15), t("silver_chandelier", 12), t("gold_bird_cage", 20),
            t("silver_bird_cage", 15), t("gold_bars", 8), t("silver_bars", 6), t("rhun_gold_brick", 3),
            t("golden_rhunic_helmet", 30), t("golden_rhunic_chestplate", 45), t("golden_rhunic_leggings", 35),
            t("golden_rhunic_boots", 25))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_GOLDSMITH_SELL = sell(
            t(Items.GOLD_INGOT, 15), t(Items.GOLD_NUGGET, 2), t("diamond", 25), t("emerald", 15), t("sapphire", 12),
            t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8), t("topaz", 8), t("pearl", 25),
            t("coral", 6));

    public static final LOTRTradeEntries RHUN_BARTENDER_BUY = buy(
            t(Items.COOKED_BEEF, 7), t(Items.COOKED_PORKCHOP, 7), t("cooked_mutton", 7), t(Items.COOKED_CHICKEN, 6),
            t(Items.BREAD, 5), t("olive_bread", 6), t("mug", 2), t("ceramic_mug", 2), t("copper_goblet", 8),
            t("wooden_cup", 2), t(Items.BAKED_POTATO, 2, 7), t(Items.COOKED_COD, 6), t("pomegranate_juice", 6),
            t("cooked_venison", 7), t(Items.COOKED_RABBIT, 6), t("stoneware_plate", 2), t("wooden_plate", 2),
            brew("ale", 8), brew("red_wine", 12), brew("white_wine", 12), brew("pomegranate_wine", 12),
            brew("arak", 10), brew("soured_milk", 8))
            .setVessels(LOTRFoods.RHUN_DRINK);

    public static final LOTRTradeEntries RHUN_BARTENDER_SELL = sell(
            t(Items.BEEF, 3), t(Items.PORKCHOP, 3), t("raw_mutton", 3), t(Items.CHICKEN, 3), t(Items.RABBIT, 3),
            t("raw_venison", 3), t("clay_mug", 1), t("clay_plate", 1), t(Items.POTATO, 2, 1), t(Items.COD, 2),
            t("salt", 10), t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.MILK_BUCKET, 4),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL));

    public static final LOTRTradeEntries MOREDAIN_HUNTSMAN_BUY = buy(
            t("lion_fur", 5), t("gemsbok_hide", 4), t("gemsbok_horn", 5), t("rhino_horn", 6), t("raw_lion", 6),
            t("raw_zebra", 4), t("raw_rhino", 6), t(Items.ROTTEN_FLESH, 3), t(Items.FEATHER, 3))
            .setVessels(LOTRFoods.MOREDAIN_DRINK);

    public static final LOTRTradeEntries MOREDAIN_HUNTSMAN_SELL = sell(
            t(Items.BREAD, 2), t("roast_yam", 2), t(Items.STICK, 8, 1), t(Items.STRING, 3, 1),
            anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t("mango", 2), t("banana", 2), t("morwaith_spear", 10),
            t("bottle_of_poison", 10), t(Items.GOLD_NUGGET, 2));

    public static final LOTRTradeEntries MOREDAIN_HUTMAKER_BUY = buy(
            t("moredain_crafting_table", 100), t("morwaith_brick", 4, 1), t(Items.TERRACOTTA, 4, 2),
            t(Items.DYED_TERRACOTTA.orange(), 4, 3), t("thatch_thatch", 4, 2), t(Items.ACACIA_PLANKS, 4, 3))
            .setVessels(LOTRFoods.MOREDAIN_DRINK);

    public static final LOTRTradeEntries MOREDAIN_HUTMAKER_SELL = sell(
            t(Items.WHEAT, 2, 1), t("red_clay_ball", 16, 1), t("red_harad_flower", 4, 1),
            t("yellow_harad_flower", 4, 1), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_NUGGET, 2),
            t("diamond", 25), t("ruby", 12));

    public static final LOTRTradeEntries TAUREDAIN_SHAMAN_BUY = buy(
            t("taurethrim_dagger", 12), t("poisoned_taurethrim_dagger", 20), t("taurethrim_blowgun", 25),
            t("taurethrim_dart", 4, 5), t("poisoned_taurethrim_dart", 4, 10), t("bottle_of_poison", 10),
            t(Items.JUNGLE_SAPLING, 6), t("mahogany_sapling", 8), t("chocolate", 4), brew("taurethrim_cocoa", 12),
            t("jungle_remedy", 20))
            .setVessels(LOTRFoods.TAUREDAIN_DRINK);

    public static final LOTRTradeEntries TAUREDAIN_SHAMAN_SELL = sell(
            t("obsidian_shard", 2), t(Items.GOLD_NUGGET, 2), t("emerald", 15), t("amber", 10),
            t(Items.GLASS_BOTTLE, 2), t("mug", 1), t(Items.BONE, 1), t("rhino_horn", 5), t("gemsbok_horn", 4),
            t("hibiscus", 2, 1), t("flame_of_harad", 2, 1), t(Items.RED_MUSHROOM, 2), t(Items.BROWN_MUSHROOM, 2),
            t("mango", 1), t("banana", 1), t("corn", 2, 1));

    public static final LOTRTradeEntries TAUREDAIN_SMITH_BUY = buy(
            t("tauredain_crafting_table", 100), t("blacksmith_hammer", 18), t("taurethrim_sword", 16),
            t("taurethrim_dagger", 12), t("taurethrim_spear", 15), t("taurethrim_battleaxe", 16),
            t("taurethrim_bludgeon", 16), t("taurethrim_pike", 18), t("taurethrim_helmet", 20),
            t("taurethrim_chestplate", 32), t("taurethrim_leggings", 25), t("taurethrim_boots", 16),
            t("taurethrim_chieftain_helmet", 40), t("bronze_bars", 8, 20))
            .setVessels(LOTRFoods.TAUREDAIN_DRINK);

    public static final LOTRTradeEntries TAUREDAIN_SMITH_SELL = sell(
            t("obsidian_shard", 2), anyOf(t(Items.COAL, 2, 1), Items.CHARCOAL), t(Items.GOLD_INGOT, 15),
            t(Items.COPPER_INGOT, 3), t("tin_ingot", 3), t("bronze_ingot", 3), t("emerald", 15), t("ruby", 12),
            t("amber", 10), t("topaz", 8), t("flame_of_harad", 2, 1));

    public static final LOTRTradeEntries TAUREDAIN_FARMER_BUY = buy(
            t(Items.WHEAT, 2), t(Items.WHEAT_SEEDS, 1), t(Items.CARROT, 3), t(Items.POTATO, 2), t("lettuce", 3),
            t("mango", 3), t("banana", 3), t("reeds", 2), t("dried_reeds", 2), t(Items.COCOA_BEANS, 8), t("corn", 3))
            .setVessels(LOTRFoods.TAUREDAIN_DRINK);

    public static final LOTRTradeEntries TAUREDAIN_FARMER_SELL = sell(
            t(Items.BUCKET, 3), t(Items.WATER_BUCKET, 4), t(Items.IRON_HOE, 8), t(Items.STONE_HOE, 1),
            t("bronze_hoe", 6), t("taurethrim_hoe", 10), t(Items.BONE_MEAL, 6, 1));

    public static final LOTRTradeEntries HALF_TROLL_SCAVENGER_BUY = buy(
            t("half_troll_crafting_table", 100), t(Items.GOLD_INGOT, 22), t(Items.GOLD_NUGGET, 5),
            t("silver_ingot", 18), t("silver_nugget", 4), t("bronze_ingot", 12), brew("torog_draught", 7),
            t("umbaric_scimitar", 16), t("umbaric_spear", 16), t("haradric_sword", 14), t("haradric_spear", 14),
            t("coast_southron_helmet", 15), t("mordor_scimitar", 15), t("mordor_battleaxe", 15),
            t("mordor_helmet", 14), t("half_troll_scimitar", 16), t("half_troll_mace", 16), t("half_troll_pike", 16),
            t("half_troll_dagger", 12), t("poisoned_half_troll_dagger", 15), t("half_troll_warhammer", 16),
            t("gondor_sword", 20), t("gondor_warhammer", 20), t("morwaith_spear", 15), t("morwaith_battleaxe", 15),
            t("morwaith_dagger", 12), t("morwaith_club", 14), t("morwaith_sword", 15), t(Items.ARROW, 4, 3),
            t(Items.BONE, 2), t("orc_bone", 3), t("troll_bone", 6), t("man_flesh", 8), t("diamond", 40),
            t("emerald", 25), t("sapphire", 20), t("ruby", 20), t("opal", 15), t("amber", 15), t("amethyst", 12),
            t("topaz", 12), t("pearl", 50))
            .setVessels(LOTRFoods.HALF_TROLL_DRINK);

    public static final LOTRTradeEntries HALF_TROLL_SCAVENGER_SELL = sell(
            t("gemsbok_horn", 3), t("gemsbok_hide", 2), t("raw_zebra", 3), t("raw_lion", 3), t("raw_rhino", 3),
            t(Items.ROTTEN_FLESH, 2), t(Items.SUGAR_CANE, 4, 1), t(Items.COBBLESTONE, 8, 1), t("diamond", 25),
            t("emerald", 15), t("sapphire", 12), t("ruby", 12), t("opal", 10), t("amber", 10), t("amethyst", 8),
            t("topaz", 8), t("pearl", 25));

    public static final LOTRTradeEntries HARAD_HUNTER_BUY = buy(
            t(Items.RABBIT, 4), t("raw_venison", 4), t(Items.LEATHER, 3), t("fur", 3), t(Items.FEATHER, 3),
            t("horn", 4), t("lion_fur", 8), t("gemsbok_hide", 6), t("gemsbok_horn", 20), t("rhino_horn", 30),
            t("raw_lion", 10), t("raw_zebra", 8), t("raw_rhino", 10), t(Items.ROTTEN_FLESH, 3))
            .setVessels(LOTRFoods.SOUTHRON_DRINK);

    public static final LOTRTradeEntries HARAD_HUNTER_SELL = sell(
            t("iron_dagger", 3), t("bronze_dagger", 3), t("haradric_dagger", 3), t("iron_spear", 5),
            t("bronze_spear", 5), t("haradric_spear", 5), t(Items.ARROW, 4, 2), t(Items.STICK, 8, 1),
            t(Items.STRING, 3, 1), t("bottle_of_poison", 10));

    public final TradeType tradeType;
    private final List<Entry> tradeEntries;
    private LOTRVessel @Nullable [] drinkVessels;

    /**
     * A pool entry, resolved to its item when first drawn from. A barrel
     * entry (barrelStrength 0 or more) is a barrel full of the drink at that
     * strength (LOTRTradeEntryBarrel).
     */
    private record Entry(Object item, int count, int cost, boolean randomStrength, Set<Item> alsoMatches,
                         int barrelStrength, int strength) {

        LOTRTradeEntry create() {
            Item resolved = this.item instanceof Item i ? i : lotr((String) this.item);
            ItemStack stack = this.barrelStrength >= 0 ? barrelOf(resolved, this.barrelStrength)
                    : this.strength >= 0 ? LOTRDrinkItem.stack(resolved, this.strength)
                    : new ItemStack(resolved, this.count);
            LOTRTradeEntry trade = new LOTRTradeEntry(stack, this.cost);
            trade.randomStrength = this.randomStrength;
            trade.alsoMatches = this.alsoMatches;
            return trade;
        }
    }

    private LOTRTradeEntries(TradeType type, Entry... entries) {
        this.tradeType = type;
        this.tradeEntries = List.of(entries);
    }

    private static LOTRTradeEntries buy(Entry... entries) {
        return new LOTRTradeEntries(TradeType.BUY, entries);
    }

    private static LOTRTradeEntries sell(Entry... entries) {
        return new LOTRTradeEntries(TradeType.SELL, entries);
    }

    private static Entry t(Object item, int cost) {
        return t(item, 1, cost);
    }

    private static Entry t(Object item, int count, int cost) {
        return new Entry(item, count, cost, false, Set.of(), -1, -1);
    }

    private static Entry brew(String item, int cost) {
        return new Entry(item, 1, cost, true, Set.of(), -1, -1);
    }

    /** A drink at one given strength (the original's damage 0-4), not a random one. */
    private static Entry strength(String item, int strength, int cost) {
        return new Entry(item, 1, cost, false, Set.of(), -1, strength);
    }

    /** LOTRTradeEntryBarrel: a barrel of the drink, brewed to the given strength. */
    private static Entry barrel(String item, int strength, int cost) {
        return new Entry(item, 1, cost, false, Set.of(), strength, -1);
    }

    /**
     * LOTRTradeEntryBarrel.createTradeItem: a barrel whose drink slot holds a
     * full barrel's worth of the drink and whose brewing is done. Items and
     * their components are static registries, so the barrel's contents are
     * written without a level.
     */
    private static ItemStack barrelOf(Item drink, int strength) {
        LOTRBarrelBlockEntity barrel = new LOTRBarrelBlockEntity(BlockPos.ZERO, LOTRFoodBlocks.BARREL.defaultBlockState());
        ItemStack brew = LOTRDrinkItem.stack(drink, strength);
        brew.setCount(LOTRBrewingRecipes.BARREL_CAPACITY);
        barrel.setItem(LOTRBarrelBlockEntity.BARREL_SLOT, brew);
        barrel.setBarrelMode(LOTRBarrelBlockEntity.FULL);
        ItemStack stack = new ItemStack(LOTRFoodBlocks.BARREL);
        stack.set(LOTRDataComponents.BARREL_DATA,
                CustomData.of(barrel.toTag(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY))));
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
        return stack;
    }

    private static Entry anyOf(Entry entry, Item... also) {
        return new Entry(entry.item, entry.count, entry.cost, entry.randomStrength, Set.of(also), entry.barrelStrength, entry.strength);
    }

    private static Item lotr(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, path);
        return BuiltInRegistries.ITEM.getOptional(id)
                .orElseThrow(() -> new IllegalStateException("LOTR trade item " + id + " is not registered"));
    }

    /** setVessels: the vessels a bought drink is served in; buy pools only. */
    private LOTRTradeEntries setVessels(LOTRFoods foods) {
        if (this.tradeType != TradeType.BUY) {
            throw new IllegalArgumentException("Cannot set the vessel types for a sell list");
        }
        this.drinkVessels = foods.getDrinkVessels();
        return this;
    }

    /** getItemSellResult: the first of the trader's sell trades that takes this stack. */
    public static @Nullable LOTRTradeSellResult getItemSellResult(ItemStack stack, LOTRNPCEntity trader) {
        LOTRTradeEntry[] sellTrades = trader.traderNPCInfo == null ? null : trader.traderNPCInfo.getSellTrades();
        if (sellTrades != null) {
            for (int index = 0; index < sellTrades.length; ++index) {
                LOTRTradeEntry trade = sellTrades[index];
                if (trade != null && trade.matches(stack)) {
                    return new LOTRTradeSellResult(index, trade, stack);
                }
            }
        }
        return null;
    }

    /**
     * getRandomTrades: three to nine of the pool, shuffled. A brewed drink
     * gets a strength from light to strong and a price by it, a drink its
     * vessel and that vessel's extra price, and a bought item its modifiers
     * (skilful one time in three) and their worth; then the price varies by a
     * quarter either way.
     */
    public LOTRTradeEntry[] getRandomTrades(RandomSource random) {
        int numTrades = Math.min(3 + random.nextInt(3) + random.nextInt(3) + random.nextInt(3), this.tradeEntries.size());
        List<Entry> temp = new ArrayList<>(this.tradeEntries);
        net.minecraft.util.Util.shuffle(temp, random);
        LOTRTradeEntry[] trades = new LOTRTradeEntry[numTrades];
        for (int i = 0; i < numTrades; ++i) {
            LOTRTradeEntry template = temp.get(i).create();
            ItemStack tradeItem = template.createTradeItem();
            float tradeCost = template.getCost();
            if (template.randomStrength && tradeItem.getItem() instanceof LOTRDrinkItem drink && drink.isBrewable()) {
                tradeItem.set(LOTRDataComponents.DRINK_STRENGTH, 1 + random.nextInt(3));
                tradeCost *= drink.foodStrength(tradeItem);
            }
            if (this.drinkVessels != null && tradeItem.getItem() instanceof LOTRDrinkItem) {
                LOTRVessel vessel = this.drinkVessels[random.nextInt(this.drinkVessels.length)];
                tradeItem.set(LOTRDataComponents.VESSEL, vessel);
                tradeCost += vessel.extraPrice();
            }
            // LOTRConfig.enchantingLOTR, on by default.
            if (this.tradeType == TradeType.BUY) {
                LOTRModifiers.applyRandom(tradeItem, random, random.nextInt(3) == 0);
                tradeCost *= LOTRModifiers.tradeValueFactor(tradeItem);
            }
            tradeCost *= Mth.randomBetween(random, 0.75f, 1.25f);
            tradeCost = Math.max(tradeCost, 1.0f);
            LOTRTradeEntry trade = new LOTRTradeEntry(tradeItem, Math.max(Math.round(tradeCost), 1));
            trade.alsoMatches = template.alsoMatches;
            trades[i] = trade;
        }
        return trades;
    }
}
