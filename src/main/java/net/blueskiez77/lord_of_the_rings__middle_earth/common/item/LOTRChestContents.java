package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLore;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

import org.jspecify.annotations.Nullable;

/**
 * LOTRChestContents: the structures' chest pools, and those items and
 * entities draw from, with fillChest and fillInventory's single pick. Not
 * here yet: the Ettenmoors troll hoard and the Gondor ruins' bones and
 * treasure, which only biome decoration places (D10).
 */
public final class LOTRChestContents {

    public record Entry(Supplier<ItemStack> item, int min, int max, int weight) {
    }

    /**
     * A pool: how many stacks a chest gets (minItems to maxItems), its entries, whether it enabled pouches, the vessels its
     * drinks come in (setDrinkVessels), if any, and its lore books (setLore), if any. Pouches are deliberately not
     * ported, so a pool with them keeps only the pouch roll's other branch,
     * the 1 in 50 smith's scroll.
     */
    public record Pool(int minItems, int maxItems, List<Entry> entries, boolean pouches,
                       LOTRVessel @Nullable [] vessels, int loreChance, List<LOTRLore.LoreCategory> loreCategories) {

        public Pool(int minItems, int maxItems, List<Entry> entries, boolean pouches, LOTRVessel @Nullable [] vessels) {
            this(minItems, maxItems, entries, pouches, vessels, 0, List.of());
        }

        /** setLore: one roll in {@code chance} is a lore book of these categories instead. */
        public Pool withLore(int chance, LOTRLore.LoreCategory... categories) {
            return new Pool(minItems, maxItems, entries, pouches, vessels, chance, List.of(categories));
        }

        public Pool(int minItems, int maxItems, List<Entry> entries, boolean pouches) {
            this(minItems, maxItems, entries, pouches, null);
        }
    }

    /** RARE_DROPS: what a slain NPC may rarely drop -- silver or gold nuggets. */
    public static final Pool RARE_DROPS = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 4, 10)), false);

    /**
     * MIRKWOOD_LOOT, entry for entry. It also carries Woodland Realm and Dol
     * Guldur lore at 1 in 20.
     */
    public static final Pool MIRKWOOD_LOOT = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 10, 100),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 2, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LEATHER_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LEATHER_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LEATHER_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LEATHER_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 5, 25),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELF_BONE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DWARF_BONE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 50)),
            true).withLore(20, LOTRLore.LoreCategory.WOODLAND_REALM, LOTRLore.LoreCategory.DOL_GULDUR);

    /** HOBBIT_HOLE_STUDY. It also carries Shire lore at 1 in 12. */
    public static final Pool HOBBIT_HOLE_STUDY = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.PAPER), 2, 8, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.INK_SAC), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 2, 4, 50),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.STRING), 1, 5, 50),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRToolItems.SULFUR_MATCH), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.CONKER), 1, 6, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SMOKING_PIPE), 1, 1, 10)),
            true).withLore(12, LOTRLore.LoreCategory.SHIRE);

    /** HOBBIT_HOLE_LARDER, its drinks served in the hobbits' vessels. */
    public static final Pool HOBBIT_HOLE_LARDER = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.CORN_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRItems.PIPEWEED), 3, 8, 100),
            new Entry(() -> new ItemStack(Items.BOWL), 2, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.SUGAR), 2, 5, 100),
            new Entry(() -> new ItemStack(Items.EGG), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.PUMPKIN_PIE), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUSHROOM_PIE), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.MUSHROOM_STEW), 1, 1, 100),
            new Entry(() -> new ItemStack(Blocks.RED_MUSHROOM), 1, 4, 25),
            new Entry(() -> new ItemStack(Blocks.BROWN_MUSHROOM), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.MILK_BUCKET), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.CARROT), 2, 4, 100),
            new Entry(() -> new ItemStack(Items.COOKIE), 2, 5, 75),
            new Entry(() -> new ItemStack(Items.COCOA_BEANS), 2, 6, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHERRIES), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.FINE_PLATE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.HOBBIT_PANCAKE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHERRY_LIQUEUR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.APPLE_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.BLUEBERRIES), 2, 5, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.BLACKBERRIES), 2, 5, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RASPBERRIES), 2, 5, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRANBERRIES), 2, 5, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ELDERBERRIES), 2, 5, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_CHESTNUT), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CORN), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CORN), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK_SOUP), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 2, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MARCHPANE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHOCOLATE_MARCHPANE), 1, 3, 10)),
            false, new LOTRVessel[]{LOTRVessel.MUG, LOTRVessel.MUG_CLAY, LOTRVessel.GOBLET_WOOD, LOTRVessel.BOTTLE});

    /**
     * BREE_HOUSE, its drinks served in Bree-land's vessels. It also carries
     * Bree lore at 1 in 20.
     */
    public static final Pool BREE_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.FLINT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.FISHING_ROD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.WHEAT_SEEDS), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Blocks.HAY_BLOCK), 1, 6, 50),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK_SOUP), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUSHROOM_PIE), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.MUSHROOM_STEW), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.IRON_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.IRON_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.IRON_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.IRON_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.CHAINMAIL_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.CHAINMAIL_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.APPLE_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100)),
            true, LOTRFoods.BREE_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.BREE);

    /**
     * ROHAN_HOUSE, its drinks served in Rohan's vessels. It also carries
     * Rohan lore at 1 in 30.
     */
    public static final Pool ROHAN_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.FLINT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.FISHING_ROD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.HORN), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.WHEAT_SEEDS), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Blocks.HAY_BLOCK), 1, 6, 50),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_COIF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_HAUBERK), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100)),
            true, LOTRFoods.ROHAN_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.ROHAN);

    /**
     * GONDOR_HOUSE, its drinks served in Gondor's vessels. It also carries
     * Gondor lore at 1 in 20.
     */
    public static final Pool GONDOR_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.FLINT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.FISHING_ROD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.PAPER), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.WHEAT_SEEDS), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVES), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALMOND), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MARCHPANE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHOCOLATE_MARCHPANE), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_WARHAMMER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100)),
            true, LOTRFoods.GONDOR_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.GONDOR);

    /**
     * DORWINION_HOUSE, its drinks served in Dorwinion's vessels. It also
     * carries Dorwinion lore at 1 in 20. The
     * original's "reeds" here were vanilla's, sugar cane.
     */
    public static final Pool DORWINION_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVES), 1, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_GRAPES), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_GRAPES), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAISINS), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.SUGAR_CANE), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_WINE), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.WHITE_WINE), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_GRAPE_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_GRAPE_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 50)),
            true, LOTRFoods.DORWINION_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DORWINION);

    /**
     * ELF_HOUSE (Lothlórien's), its drinks served in the elves' vessels. It
     * also carries Lothlórien lore at 1 in 16.
     */
    public static final Pool ELF_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRMaterialItems.MALLORN_STICK), 3, 8, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.MALLORN_SWORD), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.MALLORN_BOW), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.ELANOR), 1, 3, 75),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.NIPHREDIL), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMBAS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MALLORN_NUT), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MIRUVOR), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.HITHLAIN), 1, 4, 100)),
            true, LOTRFoods.ELF_DRINK.getDrinkVessels()).withLore(16, LOTRLore.LoreCategory.LOTHLORIEN);

    /**
     * HIGH_ELVEN_HALL (Lindon's), its drinks served in the elves' vessels. It
     * also carries Lindon lore at 1 in 16.
     */
    public static final Pool HIGH_ELVEN_HALL = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MIRUVOR), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 6, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMBAS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 10, 10)),
            true, LOTRFoods.ELF_DRINK.getDrinkVessels()).withLore(16, LOTRLore.LoreCategory.LINDON);

    /**
     * WOOD_ELF_HOUSE, its drinks served in the Wood-elves' vessels. It also
     * carries Woodland Realm lore at 1 in 20.
     */
    public static final Pool WOOD_ELF_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MIRKWOOD_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.WOOD_ELVEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 6, 100),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.GREEN_OAK_SAPLING), 1, 3, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_WINE), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMBAS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100)),
            true, LOTRFoods.WOOD_ELF_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.WOODLAND_REALM);

    /**
     * RIVENDELL_HALL, its drinks served in the elves' vessels. It also
     * carries Rivendell lore at 1 in 16.
     */
    public static final Pool RIVENDELL_HALL = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MIRUVOR), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 6, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMBAS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 10, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 10)),
            true, LOTRFoods.ELF_DRINK.getDrinkVessels()).withLore(16, LOTRLore.LoreCategory.RIVENDELL);

    /**
     * DWARF_HOUSE_LARDER, its drinks served in the dwarves' vessels.
     */
    public static final Pool DWARF_HOUSE_LARDER = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 4, 150),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_ALE_HORN), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 2, 50)),
            false, LOTRFoods.DWARF_DRINK.getDrinkVessels());

    /**
     * BLUE_DWARF_HOUSE_LARDER, its drinks served in the dwarves' vessels.
     */
    public static final Pool BLUE_DWARF_HOUSE_LARDER = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 2, 50)),
            false, LOTRFoods.DWARF_DRINK.getDrinkVessels());

    /**
     * DWARVEN_TOWER, its drinks served in the dwarves' vessels. It also carries
     * Durin's Folk lore at 1 in 20.
     */
    public static final Pool DWARVEN_TOWER = new Pool(3, 4, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DWARVEN_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DWARVEN_MATTOCK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_THROWING_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_BOAR_ARMOR), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Blocks.TORCH), 2, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DWARVEN_STEEL_INGOT), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.DWARVEN_MARRIAGE_RING), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 15)),
            true, LOTRFoods.DWARF_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DURIN);

    /**
     * BLUE_MOUNTAINS_STRONGHOLD, its drinks served in the dwarves' vessels. It
     * also carries Blue Mountains lore at 1 in 20.
     */
    public static final Pool BLUE_MOUNTAINS_STRONGHOLD = new Pool(3, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.BLUE_DWARVEN_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.BLUE_DWARVEN_MATTOCK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_THROWING_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLUE_DWARVEN_BOAR_ARMOR), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Blocks.TORCH), 2, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.DWARVEN_MARRIAGE_RING), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 15)),
            true, LOTRFoods.DWARF_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.BLUE_MOUNTAINS);

    /**
     * DWARVEN_MINE_CORRIDOR. It also carries Durin's Folk lore at 1 in 20.
     */
    public static final Pool DWARVEN_MINE_CORRIDOR = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRItems.MITHRIL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COAL), 3, 8, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.GLOWSTONE_DUST), 2, 8, 75),
            new Entry(() -> new ItemStack(Blocks.TORCH), 2, 6, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 20)),
            false).withLore(20, LOTRLore.LoreCategory.DURIN);

    /**
     * ORC_TENT (Mordor's), its drinks served in the orcs' vessels. It also
     * carries Mordor lore at 1 in 20.
     */
    public static final Pool ORC_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DURNOR), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_MORDOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_SCIMITAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_WARSCYTHE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.MORDOR);

    /**
     * BLACK_URUK_FORT, its drinks served in the orcs' vessels. It also carries
     * Mordor lore at 1 in 30.
     */
    public static final Pool BLACK_URUK_FORT = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELF_BONE), 1, 2, 5),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DWARF_BONE), 1, 2, 5),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.FUR), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DURNOR), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BLACK_URUK_STEEL_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 1, 2),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_BLACK_URUK_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_CLEAVER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_URUK_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.BOTTLE_OF_POISON), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.MORDOR);

    /**
     * GUNDABAD_TENT, its drinks served in the orcs' vessels. It also carries
     * Eriador or Gundabad lore at 1 in 10.
     */
    public static final Pool GUNDABAD_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.URUK_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_MORDOR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_SCIMITAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_WARSCYTHE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_PICKAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.MORDOR_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_ANGMAR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_PICKAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_PICKAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 15)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(10, LOTRLore.LoreCategory.ERIADOR, LOTRLore.LoreCategory.GUNDABAD);

    /**
     * ANGMAR_TENT, its drinks served in the orcs' vessels. It also carries
     * Angmar lore at 1 in 20.
     */
    public static final Pool ANGMAR_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GULDURIL), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_ANGMAR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_POLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.ANGMAR);

    /**
     * DOL_GULDUR_TENT, its drinks served in the orcs' vessels. It also carries
     * Dol Guldur lore at 1 in 20.
     */
    public static final Pool DOL_GULDUR_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GULDURIL), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_DOL_GULDUR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DOL_GULDUR_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DOL_GULDUR_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DOL_GULDUR);

    /**
     * ANGMAR_HILLMAN_HOUSE, its drinks served in Rhudaur's vessels. It also
     * carries Eriador or Angmar lore at 1 in 40.
     */
    public static final Pool ANGMAR_HILLMAN_HOUSE = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.WARG_BONE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.FUR), 1, 5, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_VENISON), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_BATTLEAXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_AXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRToolItems.ANGMAR_PICKAXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_WARHAMMER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_POLEAXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STONE_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.WOODEN_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.STONE_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_THROWING_AXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_THROWING_AXE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 75)),
            true, LOTRFoods.RHUDAUR_DRINK.getDrinkVessels()).withLore(40, LOTRLore.LoreCategory.ERIADOR, LOTRLore.LoreCategory.ANGMAR);

    /**
     * URUK_TENT, its drinks served in the orcs' vessels. It also carries
     * Isengard lore at 1 in 20.
     */
    public static final Pool URUK_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.URUK_STEEL_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.CROSSBOW_BOLT), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_CROSSBOW), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_URUK_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_CLEAVER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.URUK_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 200)),
            true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.ISENGARD);

    /**
     * DUNLENDING_HOUSE, its drinks served in Dunland's vessels. It also carries
     * Dunland lore at 1 in 40.
     */
    public static final Pool DUNLENDING_HOUSE = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.FUR), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 2, 100),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_VENISON), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.STONE_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.WOODEN_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.STONE_SPEAR), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DUNLENDING_HELMET), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DUNLENDING_CHESTPLATE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DUNLENDING_LEGGINGS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DUNLENDING_BOOTS), 1, 1, 20),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 75)),
            true, LOTRFoods.DUNLENDING_DRINK.getDrinkVessels()).withLore(40, LOTRLore.LoreCategory.DUNLAND);

    /**
     * NEAR_HARAD_HOUSE, its drinks served in the Southrons' vessels. It also
     * carries Southron or Umbar lore at 1 in 20.
     */
    public static final Pool NEAR_HARAD_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 75),
            new Entry(() -> new ItemStack(Items.PAPER), 2, 8, 50),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMON), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.LEMON_CAKE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORANGE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LIME), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVES), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALMOND), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MARCHPANE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHOCOLATE_MARCHPANE), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.BAKED_POTATO), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CAMEL), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SHISH_KEBAB), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.CACTUS_LIQUEUR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORANGE_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMONADE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMON_LIQUEUR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LIME_LIQUEUR), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 25)),
            true, LOTRFoods.SOUTHRON_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.SOUTHRON, LOTRLore.LoreCategory.UMBAR);

    /**
     * CORSAIR, its drinks served in the Corsairs' vessels. It also carries
     * Umbar lore at 1 in 40.
     */
    public static final Pool CORSAIR = new Pool(3, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.CORSAIR_EKET), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.CORSAIR_HARPOON), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.CORSAIR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.CORSAIR_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SWORD), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SPEAR), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 75),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 2, 10),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 10, 200),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 30, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 5, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 1),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SHISH_KEBAB), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.RUM), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 25),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRANDING_IRON), 1, 1, 25)),
            true, LOTRFoods.CORSAIR_DRINK.getDrinkVessels()).withLore(40, LOTRLore.LoreCategory.UMBAR);

    /**
     * NOMAD_TENT, its drinks served in the nomads' vessels. It also carries
     * nomad lore at 1 in 50, and pouches; pouches are not ported.
     */
    public static final Pool NOMAD_TENT = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SWORD), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_PIKE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 12, 50),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CAMEL), 1, 2, 200),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SHISH_KEBAB), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.CACTUS_LIQUEUR), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 25)),
            true, LOTRFoods.NOMAD_DRINK.getDrinkVessels()).withLore(50, LOTRLore.LoreCategory.NOMAD);

    /**
     * GULF_HOUSE, its drinks served in the Gulf's vessels. It also carries
     * Gulf lore at 1 in 30, and pouches; pouches are not ported.
     */
    public static final Pool GULF_HOUSE = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_KHOPESH), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_PIKE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.WATER_BUCKET), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CAMEL), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_LION), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_ZEBRA), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SHISH_KEBAB), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SUSPICIOUS_MEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 200),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 25)),
            true, LOTRFoods.GULF_HARAD_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.GULF);

    /**
     * EASTERLING_HOUSE, its drinks served in Rhûn's vessels. It also carries
     * Rhûn lore at 1 in 20, and pouches; pouches are not ported.
     */
    public static final Pool EASTERLING_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.PAPER), 2, 8, 50),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GILDED_IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVES), 1, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAISINS), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.POMEGRANATE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.SUGAR_CANE), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.SOURED_MILK), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.MILK_BUCKET), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.POMEGRANATE_WINE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.POMEGRANATE_JUICE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_WINE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WHITE_WINE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_GRAPE_JUICE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_GRAPE_JUICE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 50)),
            true, LOTRFoods.RHUN_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.RHUN);

    /**
     * MOREDAIN_HUT, its drinks served in the Moredain's vessels. It also
     * carries Far Harad lore at 1 in 30, and pouches; pouches are not ported.
     */
    public static final Pool MOREDAIN_HUT = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_MORWAITH_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_CLUB), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORWAITH_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_MACE), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 5, 15),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 15),
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 10, 50),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 6, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.YAM), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_YAM), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GEMSBOK_HIDE), 1, 5, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.RHINO_HORN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GEMSBOK_HORN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.LION_FUR), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_RHINO), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_ZEBRA), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_LION), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_RHINO), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_ZEBRA), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_LION), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MANGO_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.BANANA_BEER), 1, 1, 50)),
            true, LOTRFoods.MOREDAIN_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.FAR_HARAD);

    /**
     * TAUREDAIN_HOUSE, its drinks served in the Taurethrim's vessels. It also
     * carries Far Harad jungle lore at 1 in 20, and pouches; pouches are not ported.
     */
    public static final Pool TAUREDAIN_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRMaterialItems.OBSIDIAN_SHARD), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.PAPER), 2, 8, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 6, 50),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 5, 25),
            new Entry(() -> new ItemStack(Items.SUGAR_CANE), 1, 5, 25),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.REEDS), 1, 5, 25),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.DRIED_REEDS), 2, 6, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MANGO), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.BANANA), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.MELON_SLICE), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MELON_SOUP), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.BANANA_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CORN_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CORN), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CORN), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.CORN_STALK), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 5, 100),
            new Entry(() -> new ItemStack(Items.BAKED_POTATO), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MELON_LIQUEUR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.BANANA_BEER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CORN_LIQUOR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CHOCOLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TAURETHRIM_COCOA), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MANGO_JUICE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.COCOA_BEANS), 1, 8, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_TAURETHRIM_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.TAURETHRIM_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_BLOWGUN), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_DART), 2, 8, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_TAURETHRIM_DART), 1, 4, 25)),
            true, LOTRFoods.TAUREDAIN_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.FAR_HARAD_JUNGLE);

    /** BARROW_DOWNS: a barrow's grave-goods. It also carries Eriador lore at 1 in 30. */
    public static final Pool BARROW_DOWNS = new Pool(4, 8, List.of(
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 20, 200),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 8, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 5),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 5, 100),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 5, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 3, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.BARROW_BLADE), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_DAGGER), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_SWORD), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_BATTLEAXE), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_WARHAMMER), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_HELMET), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_CHESTPLATE), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_LEGGINGS), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_BOOTS), 1, 1, 1),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 20),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 2, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 20)), true).withLore(30, LOTRLore.LoreCategory.ERIADOR);

    /** MARSH_REMAINS: what the Dead Marshes' drowned leave behind. */
    public static final Pool MARSH_REMAINS = new Pool(1, 4, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELF_BONE), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 2, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DWARF_BONE), 1, 2, 20),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 16, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 5, 25),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 5, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 1, 2),
            new Entry(() -> new ItemStack(LOTRMiscItems.ANCIENT_SWORD_TIP), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.ANCIENT_SWORD_BLADE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.ANCIENT_SWORD_HILT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.ANCIENT_ARMOR_PLATE), 1, 1, 120)), false);

    /** TROLL_HOARD: a troll's hoard. It also carries ruins and Eriador lore at 1 in 12. */
    public static final Pool TROLL_HOARD = new Pool(1, 4, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 30, 75),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 2, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_SCIMITAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_WARSCYTHE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true).withLore(12, LOTRLore.LoreCategory.RUINS, LOTRLore.LoreCategory.ERIADOR);

    /**
     * HARNENNOR_HOUSE, its drinks served in Harnedor's vessels. It also carries
     * Harnennor lore at 1 in 30.
     */
    public static final Pool HARNENNOR_HOUSE = new Pool(2, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SWORD), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_HARADRIC_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_PIKE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.WATER_BUCKET), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_CAMEL), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.SHISH_KEBAB), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 75),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.WOODEN_PLATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 200),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 25)),
            true, LOTRFoods.HARNEDOR_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.HARNENNOR);

    /** ANCIENT_SWORD: orc scimitar, and the elven, Gondor, Arnor and Dwarven swords. */
    public static final Pool ANCIENT_SWORD = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_SCIMITAR), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_SWORD), 1, 1, 50)),
            false);

    /** ANCIENT_DAGGER. */
    public static final Pool ANCIENT_DAGGER = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_DAGGER), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_DAGGER), 1, 1, 50)),
            false);

    /** ANCIENT_HELMET. */
    public static final Pool ANCIENT_HELMET = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_HELMET), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_HELMET), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_HELMET), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_HELMET), 1, 1, 50)),
            false);

    /** ANCIENT_BODY. */
    public static final Pool ANCIENT_BODY = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_CHESTPLATE), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_CHESTPLATE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_CHESTPLATE), 1, 1, 50)),
            false);

    /** ANCIENT_LEGS. */
    public static final Pool ANCIENT_LEGS = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_LEGGINGS), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_LEGGINGS), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_LEGGINGS), 1, 1, 50)),
            false);

    /** ANCIENT_BOOTS. */
    public static final Pool ANCIENT_BOOTS = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_BOOTS), 1, 1, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RIVENDELL_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GALADHRIM_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.WOOD_ELVEN_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOOTS), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_BOOTS), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_BOOTS), 1, 1, 50)),
            false);

    private LOTRChestContents() {
    }

    /** HOBBIT_HOLE_TREASURE: a hobbit hole's hidden hoard, entry for entry. */
    public static final Pool HOBBIT_HOLE_TREASURE = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 25, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 10, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 3, 25)), false);

    /** BREE_TREASURE: Bree-land's valuables, entry for entry. */
    public static final Pool BREE_TREASURE = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 25, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 10, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 3, 10)), false);

    /** BREE_OFFICE_ITEMS: the office's papers and stationery, entry for entry. */
    public static final Pool BREE_OFFICE_ITEMS = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(Items.BOOK), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.PAPER), 2, 8, 50),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 2, 10),
            new Entry(() -> new ItemStack(Items.INK_SAC), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 2, 4, 10),
            new Entry(() -> new ItemStack(Items.STRING), 1, 5, 10),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 10, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 5),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 2, 50)), true);

    /** BREE_RUFFIAN_PIPEWEED: the ruffians' pipeweed hoard, entry for entry. */
    public static final Pool BREE_RUFFIAN_PIPEWEED = new Pool(3, 6, List.of(
            new Entry(() -> new ItemStack(LOTRItems.PIPEWEED), 1, 8, 100)), false);

    /** RANGER_HOUSE: a Ranger's house and what a slain Dúnadan may drop, entry for entry. It also carries lore at 1 in 20. */
    public static final Pool RANGER_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.FLINT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.FISHING_ROD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.WHEAT_SEEDS), 1, 6, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.HAY_BLOCK), 1, 6, 50),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LETTUCE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_HOOD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_TUNIC), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100)), true, LOTRFoods.RANGER_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.ERIADOR);

    /** RANGER_SMITHY: a Ranger smithy's stock, entry for entry. */
    public static final Pool RANGER_SMITHY = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(Items.STICK), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 2, 50)), true, LOTRFoods.RANGER_DRINK.getDrinkVessels());

    /** RANGER_TENT: a Ranger's tent and watchtower, entry for entry. It also carries lore at 1 in 20. */
    public static final Pool RANGER_TENT = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.STICK), 8, 16, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_HOOD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_TUNIC), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RANGER_BOW), 1, 1, 25)), true, LOTRFoods.RANGER_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.ERIADOR);

    /** DUNEDAIN_TOWER: the hoard atop a ruined Dúnedain tower, entry for entry. It also carries lore at 1 in 16. */
    public static final Pool DUNEDAIN_TOWER = new Pool(5, 7, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 2, 5, 100),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 2, 3, 100),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 2, 9, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 2, 9, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 2, 20, 100),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ARNOR_SPEAR), 1, 1, 25)), true).withLore(16, LOTRLore.LoreCategory.ERIADOR);

    /** RUINED_HOUSE: what is left in a ruined house, entry for entry. Its drinks come in any vessel. It also carries lore at 1 in 12. */
    public static final Pool RUINED_HOUSE = new Pool(2, 7, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.WARG_BONE), 1, 2, 10),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 2, 4, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 2, 12, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 2, 4, 50),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 2, 3, 25),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 2, 9, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 2, 9, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 50, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 5),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 4, 5),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.STONE_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 5, 50),
            new Entry(() -> new ItemStack(Items.STRING), 1, 5, 50),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.BOOK), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.PEBBLE), 1, 8, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 7, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.CROSSBOW_BOLT), 1, 7, 50),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_CROSSBOW), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_CROSSBOW), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.BOTTLE_OF_POISON), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.FINE_PLATE), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodBlocks.STONEWARE_PLATE), 1, 3, 15),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 10)), true, LOTRVessel.values()).withLore(12, LOTRLore.LoreCategory.RUINS);

    /** DORWINION_CAMP: a Dorwinion camp's stores, entry for entry. It also carries lore at 1 in 20. */
    public static final Pool DORWINION_CAMP = new Pool(1, 2, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_PIKE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_SPEAR), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_WINE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WHITE_WINE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_GRAPE_JUICE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_GRAPE_JUICE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DORWINION_ELVEN_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_CROSSBOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.CROSSBOW_BOLT), 1, 6, 50)), true, LOTRFoods.DORWINION_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DORWINION);

    /** ELVEN_FORGE: an elven forge's chests, entry for entry. */
    public static final Pool ELVEN_FORGE = new Pool(2, 4, List.of(
            new Entry(() -> new ItemStack(Items.BOOK), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COAL), 2, 8, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 2, 8, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELVEN_STEEL_INGOT), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.IRON_ORE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 4, 10),
            new Entry(() -> new ItemStack(Items.COPPER_ORE), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRBuildingBlocks.TIN_ORE), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 4, 10),
            new Entry(() -> new ItemStack(Items.GOLD_ORE), 1, 4, 5),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 5),
            new Entry(() -> new ItemStack(Items.STICK), 3, 8, 25)), true);

    /** UNDERWATER_ELVEN_RUIN: what lies in a drowned elven ruin, entry for entry. It also carries lore at 1 in 30. */
    public static final Pool UNDERWATER_ELVEN_RUIN = new Pool(6, 10, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELF_BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_BONE), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 2, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ELVEN_STEEL_INGOT), 2, 4, 50),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 2, 3, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 2, 9, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 2, 9, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 2, 30, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.IRON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_BATTLESTAFF), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_LONGSPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.LINDON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_SWORD), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.MITHRIL_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 5, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.MITHRIL_RING), 1, 1, 5)), true).withLore(30, LOTRLore.LoreCategory.LINDON);

    /** DALE_HOUSE: a Dale house, and what a slain Dalishman may drop, entry for entry. It also carries lore at 1 in 20. */
    public static final Pool DALE_HOUSE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.COAL), 1, 3, 75),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.FEATHER), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.WHEAT), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.POTATO), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.CARROT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEEK), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ROAST_TURNIP), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 3, 10),
            new Entry(() -> new ItemStack(Items.IRON_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_HOE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.IRON_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.BRONZE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.STONE_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.WOODEN_SHOVEL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.IRON_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.BRONZE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CERAMIC_MUG), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_ALE_HORN), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PERRY), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.VODKA), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM_KVASS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RED_WINE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.WHITE_WINE), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100)), true, LOTRFoods.DALE_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DALE);

    /** BLUE_MOUNTAINS_SMITHY: a Blue Mountains smithy's stock, entry for entry. */
    public static final Pool BLUE_MOUNTAINS_SMITHY = new Pool(3, 4, List.of(
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRBuildingBlocks.BLUE_ROCK), 1, 8, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 15)), true);

    /** DALE_WATCHTOWER: a Dale watchtower's stores, entry for entry. It also carries lore at 1 in 20. */
    public static final Pool DALE_WATCHTOWER = new Pool(3, 5, List.of(
            new Entry(() -> new ItemStack(Items.ARROW), 2, 6, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_PITCHFORK), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.VODKA), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM_KVASS), 1, 1, 20),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true, LOTRFoods.DALE_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DALE);

    /** DWARF_SMITHY: a dwarven smithy's stock, entry for entry. */
    public static final Pool DWARF_SMITHY = new Pool(3, 4, List.of(
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DWARVEN_STEEL_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COOKED_CHICKEN), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 4, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 4, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER), 1, 1, 15)), true);

    /** DALE_HOUSE_TREASURE: a Dale house's hidden valuables, entry for entry. */
    public static final Pool DALE_HOUSE_TREASURE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 25, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 10, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_SWORD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_DAGGER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_SWORD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_DAGGER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DALE_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DWARVEN_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.SILVER_TRIMMED_DWARVEN_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLD_TRIMMED_DWARVEN_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 3, 10)), true);

    /** GONDOR_HOUSE_TREASURE: a Gondor house's hidden valuables, entry for entry. */
    public static final Pool GONDOR_HOUSE_TREASURE = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 50),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 25, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 10, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SWORD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_DAGGER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_WARHAMMER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_WINGED_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 4, 10)), true, LOTRFoods.GONDOR_DRINK.getDrinkVessels());

    /** ROHAN_BARROWS: a Rohan barrow's grave goods, entry for entry. */
    public static final Pool ROHAN_BARROWS = new Pool(6, 8, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 2, 5, 75),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 3, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 2, 3, 75),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 2, 9, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 2, 9, 75),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 2, 10, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 75),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 1, 50)), true);

    /** ROHAN_WATCHTOWER: a Rohan watchtower's stores, entry for entry. It also carries lore at 1 in 40. */
    public static final Pool ROHAN_WATCHTOWER = new Pool(3, 5, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.ROHIRRIC_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 6, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE_HORN), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 75),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true, LOTRFoods.ROHAN_DRINK.getDrinkVessels()).withLore(40, LOTRLore.LoreCategory.ROHAN);

    /** GONDOR_FORTRESS_SUPPLIES: the stores of Gondor's fortresses, watchforts and towers It also carries lore at 1 in 20. */
    public static final Pool GONDOR_FORTRESS_SUPPLIES = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_LANCE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOW), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 4, 10, 100),
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 2, 5, 75),
            new Entry(() -> new ItemStack(Items.APPLE), 2, 5, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 2, 5, 100),
            new Entry(() -> new ItemStack(Items.BREAD), 2, 4, 100),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true, LOTRFoods.GONDOR_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.GONDOR);

    /** GONDOR_FORTRESS_DRINKS: the drink barrels and chests of Gondor's forts */
    public static final Pool GONDOR_FORTRESS_DRINKS = new Pool(2, 4, List.of(
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 50)), false, LOTRFoods.GONDOR_DRINK.getDrinkVessels());

    /** GONDOR_SMITHY: a Gondorian smithy's chest */
    public static final Pool GONDOR_SMITHY = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(Items.STICK), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 2, 50)), true, LOTRFoods.GONDOR_DRINK.getDrinkVessels());

    /** DOL_AMROTH_STABLES: the Dol Amroth stables' chest It also carries lore at 1 in 20. */
    public static final Pool DOL_AMROTH_STABLES = new Pool(2, 4, List.of(
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GOLDEN_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SILVER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.WINE_GLASS), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.GLASS_BOTTLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ALE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MEAD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.CIDER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RUM), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_LANCE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_AMROTH_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GONDOR_BOW), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 4, 10, 100),
            new Entry(() -> new ItemStack(Items.STICK), 4, 12, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 2, 5, 75),
            new Entry(() -> new ItemStack(Items.APPLE), 2, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 2, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PEAR), 2, 5, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 2, 5, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 2, 4, 100),
            new Entry(() -> new ItemStack(Items.WHEAT), 2, 6, 100),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SWAN_FEATHER), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COAL), 1, 5, 100),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 150),
            new Entry(() -> new ItemStack(Items.LEAD), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true, LOTRFoods.GONDOR_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.GONDOR);

    /** ORC_DUNGEON: an orc dungeon's chest */
    public static final Pool ORC_DUNGEON = new Pool(6, 8, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 8, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.URUK_STEEL_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 2, 75),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GULDURIL), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRCombatBlocks.ORC_BOMB), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRDecorationBlocks.ORC_TORCH), 1, 4, 75),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_HELMET), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_CHESTPLATE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_LEGGINGS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_BOOTS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_SCIMITAR), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_WARSCYTHE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_DAGGER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_MORDOR_DAGGER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_BATTLEAXE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.MORDOR_WARHAMMER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_HELMET), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_CHESTPLATE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_LEGGINGS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_BOOTS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_SWORD), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_DAGGER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_ANGMAR_DAGGER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_BATTLEAXE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_WARHAMMER), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ANGMAR_POLEAXE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 75),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 7, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 200)), true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /** HALF_TROLL_HOUSE: a half-troll house's chest It also carries lore at 1 in 30. */
    public static final Pool HALF_TROLL_HOUSE = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_SCIMITAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_HALF_TROLL_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_MACE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HALF_TROLL_WARHAMMER), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 4, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 8, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 8, 10),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 8, 10),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.TOROG_STEW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.COAL), 1, 8, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GEMSBOK_HIDE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.STRING), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COBBLESTONE), 4, 10, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_RHINO), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_ZEBRA), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_LION), 1, 3, 10)), true, LOTRFoods.HALF_TROLL_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.HALF_TROLL);

    /** DOL_GULDUR_TOWER: the chests of Dol Guldur's towers It also carries lore at 1 in 20. */
    public static final Pool DOL_GULDUR_TOWER = new Pool(6, 8, List.of(
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 50),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GULDURIL), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SKULL_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORC_DRAUGHT), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 2, 8, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.ORC_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_DOL_GULDUR_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_SWORD), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DOL_GULDUR_PICKAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRToolItems.DOL_GULDUR_AXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_WARHAMMER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_SPIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.DOL_GULDUR_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MAGGOTY_BREAD), 1, 4, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.BOTTLE_OF_POISON), 1, 1, 25)), true, LOTRFoods.ORC_DRINK.getDrinkVessels()).withLore(20, LOTRLore.LoreCategory.DOL_GULDUR);

    /** DUNLENDING_CAMPFIRE: the chest by a Dunlending campfire */
    public static final Pool DUNLENDING_CAMPFIRE = new Pool(1, 4, List.of(
            new Entry(() -> new ItemStack(Items.FLINT_AND_STEEL), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.STICK), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.PORKCHOP), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.GAMMON), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.BEEF), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_BEEF), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_MUTTON), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_MUTTON), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.PORKCHOP), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_PORKCHOP), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COD), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_COD), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.RABBIT), 1, 2, 50),
            new Entry(() -> new ItemStack(Items.COOKED_RABBIT), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.RAW_VENISON), 1, 2, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.COOKED_VENISON), 1, 2, 50)), false, LOTRFoods.DUNLENDING_DRINK.getDrinkVessels());

    /** NEAR_HARAD_TOWER: the chests of Near Harad's towers, forts and barracks It also carries lore at 1 in 30. */
    public static final Pool NEAR_HARAD_TOWER = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_MACE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 75),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.GREEN_APPLE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LEMON), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ORANGE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.LIME), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.PLUM), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_HORSE_ARMOR), 1, 1, 25)), true, LOTRFoods.SOUTHRON_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.SOUTHRON, LOTRLore.LoreCategory.UMBAR);

    /** NEAR_HARAD_PYRAMID: the treasure of a Haradric pyramid */
    public static final Pool NEAR_HARAD_PYRAMID = new Pool(8, 10, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SPEAR), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_MACE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_PIKE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_HELMET), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_CHESTPLATE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_LEGGINGS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_BOOTS), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_KHOPESH), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SWORD), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SPEAR), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_PIKE), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 40, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 15, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 2, 10),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 8, 100),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 8, 100),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 8, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DIAMOND), 1, 2, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.EMERALD), 1, 3, 15),
            new Entry(() -> new ItemStack(LOTRMaterialItems.RUBY), 1, 3, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SAPPHIRE), 1, 3, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TOPAZ), 1, 4, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.AMBER), 1, 4, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 2, 25),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 2, 25),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.ROTTEN_FLESH), 1, 3, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.OLD_HARADRIC_SACRIFICIAL_DAGGER), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_HELMET), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_CHESTPLATE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_LEGGINGS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_BOOTS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_SWORD), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_DAGGER), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_SPEAR), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.BLACK_NUMENOREAN_MACE), 1, 1, 20)), false, LOTRFoods.SOUTHRON_DRINK.getDrinkVessels());

    /** MOREDAIN_MERC_TENT: a Moredain mercenary tent's chest It also carries lore at 1 in 30. */
    public static final Pool MOREDAIN_MERC_TENT = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.COAST_SOUTHRON_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_HELMET), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_CHESTPLATE), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_LEGGINGS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_BOOTS), 1, 1, 15),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARNENNOR_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_HELMET), 1, 1, 8),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_CHESTPLATE), 1, 1, 8),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_LEGGINGS), 1, 1, 8),
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_BOOTS), 1, 1, 8),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SCIMITAR), 1, 1, 150),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_UMBARIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_MACE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.UMBARIC_SPEAR), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_HARADRIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_PIKE), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARAD_BOW), 1, 1, 100),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 150),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.LEATHER), 1, 4, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRItems.KEBAB), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true, LOTRFoods.SOUTHRON_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.HARNENNOR, LOTRLore.LoreCategory.SOUTHRON, LOTRLore.LoreCategory.UMBAR, LOTRLore.LoreCategory.GULF);

    /** GULF_PYRAMID: the treasure of a Gulf pyramid */
    public static final Pool GULF_PYRAMID = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.GULFEN_KHOPESH), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.HARADRIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.BONE), 1, 3, 100),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 5),
            new Entry(() -> new ItemStack(LOTRMaterialItems.OBSIDIAN_SHARD), 1, 3, 5)), true, LOTRFoods.GULF_HARAD_DRINK.getDrinkVessels());

    /** EASTERLING_SMITHY: an Easterling smithy's chest */
    public static final Pool EASTERLING_SMITHY = new Pool(1, 3, List.of(
            new Entry(() -> new ItemStack(Items.STICK), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.COAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.CHARCOAL), 1, 4, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COPPER_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.TIN_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GILDED_IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.BUCKET), 1, 2, 50)), true, LOTRFoods.RHUN_DRINK.getDrinkVessels());

    /** EASTERLING_TOWER: the chests of Easterling towers and fortresses It also carries lore at 1 in 30. */
    public static final Pool EASTERLING_TOWER = new Pool(4, 6, List.of(
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_HELMET), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_CHESTPLATE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_LEGGINGS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BOOTS), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_HELMET), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_CHESTPLATE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_LEGGINGS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_BOOTS), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_SWORD), 1, 1, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BATTLEAXE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BARDICHE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_DAGGER), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_SPEAR), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_PIKE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_BOW), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.ARROW), 1, 6, 100),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.IRON_NUGGET), 1, 6, 10),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRMaterialItems.GILDED_IRON_INGOT), 1, 3, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.MUG), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WATERSKIN), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.WOODEN_CUP), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SOURED_MILK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SOURED_MILK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.SOURED_MILK), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRFoodItems.ARAK), 1, 1, 10),
            new Entry(() -> new ItemStack(Items.BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.OLIVE_BREAD), 1, 3, 50),
            new Entry(() -> new ItemStack(LOTRFoodItems.DATE), 1, 3, 25),
            new Entry(() -> new ItemStack(LOTRFoodItems.POMEGRANATE), 1, 3, 25),
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Entry(() -> new ItemStack(Items.SADDLE), 1, 1, 25),
            new Entry(() -> new ItemStack(LOTRCombatItems.RHUNIC_HORSE_ARMOR), 1, 1, 25)), true, LOTRFoods.RHUN_DRINK.getDrinkVessels()).withLore(30, LOTRLore.LoreCategory.RHUN);

    /** getRandomItemAmount: how many stacks a chest of this pool holds. */
    public static int getRandomItemAmount(Pool pool, RandomSource random) {
        return Mth.randomBetweenInclusive(random, pool.minItems(), pool.maxItems());
    }

    /** TAUREDAIN_PYRAMID: the treasure of a Tauredain pyramid It also carries lore at 1 in 30. */
    public static final Pool TAUREDAIN_PYRAMID = new Pool(8, 10, List.of(
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN), 1, 40, 100),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_STACK), 1, 20, 50),
            new Entry(() -> new ItemStack(LOTRMiscItems.SILVER_COIN_PILE), 1, 5, 20),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 16, 50),
            new Entry(() -> new ItemStack(Items.GOLD_INGOT), 1, 20, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 16, 50),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_INGOT), 1, 20, 50),
            new Entry(() -> new ItemStack(Items.IRON_INGOT), 1, 15, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.BRONZE_INGOT), 1, 15, 20),
            new Entry(() -> new ItemStack(Items.LAPIS_LAZULI), 1, 50, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.DIAMOND), 1, 5, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.EMERALD), 1, 5, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.SAPPHIRE), 1, 5, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.RUBY), 1, 5, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.AMBER), 1, 5, 20),
            new Entry(() -> new ItemStack(LOTRMaterialItems.OBSIDIAN_SHARD), 1, 20, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_SWORD), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_TAURETHRIM_DAGGER), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_SPEAR), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_BLUDGEON), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_BATTLEAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_PIKE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.TAURETHRIM_PICKAXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRToolItems.TAURETHRIM_AXE), 1, 1, 10),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_BLOWGUN), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_DART), 1, 50, 100),
            new Entry(() -> new ItemStack(LOTRCombatItems.POISONED_TAURETHRIM_DART), 1, 50, 50),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_HELMET), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_CHESTPLATE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_LEGGINGS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.TAURETHRIM_BOOTS), 1, 1, 20),
            new Entry(() -> new ItemStack(Items.SKELETON_SKULL), 1, 1, 50),
            new Entry(() -> new ItemStack(Items.BONE), 1, 4, 200),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_TAURETHRIM_HELMET), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_TAURETHRIM_CHESTPLATE), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_TAURETHRIM_LEGGINGS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRCombatItems.GOLDEN_TAURETHRIM_BOOTS), 1, 1, 20),
            new Entry(() -> new ItemStack(LOTRMiscItems.TAURETHRIM_AMULET), 1, 2, 40)), true).withLore(30, LOTRLore.LoreCategory.FAR_HARAD_JUNGLE);

    /**
     * fillChest: the container at this spot filled from the pool -- the given
     * number of stacks, or with -1 the pool's own count -- each into a random
     * slot, as a chest in a structure is.
     */
    public static void fillChest(LevelAccessor level, RandomSource random, BlockPos pos, Pool pool, int amount) {
        if (!(level.getBlockEntity(pos) instanceof Container container)) {
            LOTRMod.LOGGER.warn("LOTRChestContents attempted to fill a chest at {} which does not exist", pos);
            return;
        }
        fillInventory(container, random, pool, amount, false);
    }

    public static void fillInventory(Container inventory, RandomSource random, Pool pool, int amount) {
        fillInventory(inventory, random, pool, amount, false);
    }

    public static void fillInventory(Container inventory, RandomSource random, Pool pool, int amount, boolean isNPCDrop) {
        if (amount == -1) {
            amount = getRandomItemAmount(pool, random);
        } else if (amount <= 0) {
            throw new IllegalArgumentException("LOTRChestContents tried to fill a chest with " + amount + " items");
        }
        for (int i = 0; i < amount; ++i) {
            // ChestGenHooks.generateStacks: more than fits in one stack comes as that many single items, each in its own slot.
            Entry entry = chooseEntry(pool, random);
            ItemStack rolled = entry.item().get();
            int count = Mth.nextInt(random, entry.min(), entry.max());
            if (count > rolled.getMaxStackSize()) {
                for (int l = 0; l < count; ++l) {
                    inventory.setItem(random.nextInt(inventory.getContainerSize()),
                            finish(rolled.copyWithCount(1), pool, random, isNPCDrop));
                }
            } else {
                inventory.setItem(random.nextInt(inventory.getContainerSize()),
                        finish(rolled.copyWithCount(count), pool, random, isNPCDrop));
            }
        }
    }

    /**
     * One of fillInventory's picks: a weighted entry and a count in its range;
     * for a pool with pouches, a pouch (not ported) or else a smith's scroll,
     * each 1 in 50; for a pool with lore, perhaps a lore book; a damageable item worn up to three quarters; a brewed drink light to strong,
     * in one of the pool's vessels if it has them; the stack cut
     * to its maximum; and the mod's modifiers rolled on, skilful 1 in 5 unless
     * an NPC dropped it.
     */
    public static ItemStack pick(Pool pool, RandomSource random, boolean isNPCDrop) {
        Entry entry = chooseEntry(pool, random);
        ItemStack stack = entry.item().get();
        stack.setCount(Mth.nextInt(random, entry.min(), entry.max()));
        return finish(stack, pool, random, isNPCDrop);
    }

    private static Entry chooseEntry(Pool pool, RandomSource random) {
        int totalWeight = pool.entries().stream().mapToInt(Entry::weight).sum();
        int roll = random.nextInt(totalWeight);
        for (Entry candidate : pool.entries()) {
            roll -= candidate.weight();
            if (roll < 0) {
                return candidate;
            }
        }
        return pool.entries().getFirst();
    }

    private static ItemStack finish(ItemStack stack, Pool pool, RandomSource random, boolean isNPCDrop) {
        if (!isNPCDrop && pool.pouches() && random.nextInt(50) != 0 && random.nextInt(50) == 0) {
            stack = LOTRModifiers.randomTemplate(random);
        }
        if (!pool.loreCategories().isEmpty()) {
            // An NPC's drop gives a lore book three quarters as rarely again, but no rarer than 1 in 8.
            int loreChance = pool.loreChance();
            int minDropLoreChance = 8;
            if (isNPCDrop && loreChance > minDropLoreChance) {
                loreChance = Math.max((int) (loreChance * 0.75f), minDropLoreChance);
            }
            LOTRLore lore;
            if (random.nextInt(Math.max(loreChance, 1)) == 0
                    && (lore = LOTRLore.getMultiRandomLore(pool.loreCategories(), random, false)) != null) {
                stack = lore.createLoreBook(random);
            }
        }
        if (stack.isDamageableItem()) {
            stack.setDamageValue(Mth.floor(stack.getMaxDamage() * Mth.randomBetween(random, 0.0f, 0.75f)));
        }
        if (stack.getItem() instanceof LOTRDrinkItem drink) {
            if (drink.isBrewable()) {
                stack.set(LOTRDataComponents.DRINK_STRENGTH, 1 + random.nextInt(3));
            }
            if (pool.vessels() != null) {
                stack.set(LOTRDataComponents.VESSEL, pool.vessels()[random.nextInt(pool.vessels().length)]);
            }
        }
        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }
        if (LOTRConfig.enchantingLOTR) {
            LOTRModifiers.applyRandom(stack, random, !isNPCDrop && random.nextInt(5) == 0);
        }
        return stack;
    }
}
