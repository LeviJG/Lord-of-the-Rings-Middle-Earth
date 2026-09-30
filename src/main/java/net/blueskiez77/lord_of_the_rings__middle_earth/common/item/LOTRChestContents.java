package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
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
 * The LOTRChestContents pools that items and entities draw from outside of
 * structures, and fillInventory's single pick. Structure chests are not
 * ported yet; when they are, their pools belong here too.
 */
public final class LOTRChestContents {

    public record Entry(Supplier<ItemStack> item, int min, int max, int weight) {
    }

    /**
     * A pool: how many stacks a chest gets (minItems to maxItems), its entries, whether it enabled pouches, and the vessels its
     * drinks come in (setDrinkVessels), if any. Pouches are deliberately not
     * ported, so a pool with them keeps only the pouch roll's other branch,
     * the 1 in 50 smith's scroll.
     */
    public record Pool(int minItems, int maxItems, List<Entry> entries, boolean pouches,
                       LOTRVessel @Nullable [] vessels) {

        public Pool(int minItems, int maxItems, List<Entry> entries, boolean pouches) {
            this(minItems, maxItems, entries, pouches, null);
        }
    }

    /** RARE_DROPS: what a slain NPC may rarely drop -- silver or gold nuggets. */
    public static final Pool RARE_DROPS = new Pool(1, 1, List.of(
            new Entry(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 6, 20),
            new Entry(() -> new ItemStack(Items.GOLD_NUGGET), 1, 4, 10)), false);

    /**
     * MIRKWOOD_LOOT, entry for entry. It also carried Woodland Realm and Dol
     * Guldur lore at 1 in 20; lore books are not ported.
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
            true);

    /** HOBBIT_HOLE_STUDY. It also carried Shire lore at 1 in 12; lore books are not ported. */
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
            true);

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
     * BREE_HOUSE, its drinks served in Bree-land's vessels. It also carried
     * Bree lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.BREE_DRINK.getDrinkVessels());

    /**
     * ROHAN_HOUSE, its drinks served in Rohan's vessels. It also carried
     * Rohan lore at 1 in 30; lore books are not ported.
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
            true, LOTRFoods.ROHAN_DRINK.getDrinkVessels());

    /**
     * GONDOR_HOUSE, its drinks served in Gondor's vessels. It also carried
     * Gondor lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.GONDOR_DRINK.getDrinkVessels());

    /**
     * DORWINION_HOUSE, its drinks served in Dorwinion's vessels. It also
     * carried Dorwinion lore at 1 in 20; lore books are not ported. The
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
            true, LOTRFoods.DORWINION_DRINK.getDrinkVessels());

    /**
     * ELF_HOUSE (Lothlórien's), its drinks served in the elves' vessels. It
     * also carried Lothlórien lore at 1 in 16; lore books are not ported.
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
            true, LOTRFoods.ELF_DRINK.getDrinkVessels());

    /**
     * HIGH_ELVEN_HALL (Lindon's), its drinks served in the elves' vessels. It
     * also carried Lindon lore at 1 in 16; lore books are not ported.
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
            true, LOTRFoods.ELF_DRINK.getDrinkVessels());

    /**
     * WOOD_ELF_HOUSE, its drinks served in the Wood-elves' vessels. It also
     * carried Woodland Realm lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.WOOD_ELF_DRINK.getDrinkVessels());

    /**
     * RIVENDELL_HALL, its drinks served in the elves' vessels. It also
     * carried Rivendell lore at 1 in 16; lore books are not ported.
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
            true, LOTRFoods.ELF_DRINK.getDrinkVessels());

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
     * DWARVEN_TOWER, its drinks served in the dwarves' vessels. It also carried
     * Durin's Folk lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.DWARF_DRINK.getDrinkVessels());

    /**
     * BLUE_MOUNTAINS_STRONGHOLD, its drinks served in the dwarves' vessels. It
     * also carried Blue Mountains lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.DWARF_DRINK.getDrinkVessels());

    /**
     * DWARVEN_MINE_CORRIDOR. It also carried Durin's Folk lore at 1 in 20;
     * lore books are not ported.
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
            false);

    /**
     * ORC_TENT (Mordor's), its drinks served in the orcs' vessels. It also
     * carried Mordor lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * BLACK_URUK_FORT, its drinks served in the orcs' vessels. It also carried
     * Mordor lore at 1 in 30; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * GUNDABAD_TENT, its drinks served in the orcs' vessels. It also carried
     * Eriador or Gundabad lore at 1 in 10; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * ANGMAR_TENT, its drinks served in the orcs' vessels. It also carried
     * Angmar lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * DOL_GULDUR_TENT, its drinks served in the orcs' vessels. It also carried
     * Dol Guldur lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * ANGMAR_HILLMAN_HOUSE, its drinks served in Rhudaur's vessels. It also
     * carried Eriador or Angmar lore at 1 in 40; lore books are not ported.
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
            true, LOTRFoods.RHUDAUR_DRINK.getDrinkVessels());

    /**
     * URUK_TENT, its drinks served in the orcs' vessels. It also carried
     * Isengard lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.ORC_DRINK.getDrinkVessels());

    /**
     * DUNLENDING_HOUSE, its drinks served in Dunland's vessels. It also carried
     * Dunland lore at 1 in 40; lore books are not ported.
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
            true, LOTRFoods.DUNLENDING_DRINK.getDrinkVessels());

    /**
     * NEAR_HARAD_HOUSE, its drinks served in the Southrons' vessels. It also
     * carried Southron or Umbar lore at 1 in 20; lore books are not ported.
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
            true, LOTRFoods.SOUTHRON_DRINK.getDrinkVessels());

    /**
     * CORSAIR, its drinks served in the Corsairs' vessels. It also carried
     * Umbar lore at 1 in 40 (lore books are not ported) and a branding iron
     * (weight 25, with the branding iron item).
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
            new Entry(() -> new ItemStack(Items.LEAD), 1, 2, 10)),
            true, LOTRFoods.CORSAIR_DRINK.getDrinkVessels());

    /**
     * NOMAD_TENT, its drinks served in the nomads' vessels. It also carried
     * nomad lore at 1 in 50, and pouches; lore books and pouches are not ported.
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
            true, LOTRFoods.NOMAD_DRINK.getDrinkVessels());

    /**
     * GULF_HOUSE, its drinks served in the Gulf's vessels. It also carried
     * Gulf lore at 1 in 30, and pouches; lore books and pouches are not ported.
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
            true, LOTRFoods.GULF_HARAD_DRINK.getDrinkVessels());

    /**
     * EASTERLING_HOUSE, its drinks served in Rhûn's vessels. It also carried
     * Rhûn lore at 1 in 20, and pouches; lore books and pouches are not ported.
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
            true, LOTRFoods.RHUN_DRINK.getDrinkVessels());

    /**
     * MOREDAIN_HUT, its drinks served in the Moredain's vessels. It also
     * carried Far Harad lore at 1 in 30, and pouches; lore books and pouches
     * are not ported.
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
            true, LOTRFoods.MOREDAIN_DRINK.getDrinkVessels());

    /**
     * TAUREDAIN_HOUSE, its drinks served in the Taurethrim's vessels. It also
     * carried Far Harad jungle lore at 1 in 20, and pouches; lore books and
     * pouches are not ported.
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
            true, LOTRFoods.TAUREDAIN_DRINK.getDrinkVessels());

    /** BARROW_DOWNS: a barrow's grave-goods. It also carried Eriador lore at 1 in 30; lore books are not ported. */
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
            new Entry(() -> new ItemStack(LOTRFoodItems.COPPER_GOBLET), 1, 3, 20)), true);

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

    /** TROLL_HOARD: a troll's hoard. It also carried ruins and Eriador lore at 1 in 12; lore books are not ported. */
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
            new Entry(() -> new ItemStack(Items.COMPASS), 1, 1, 25)), true);

    /**
     * HARNENNOR_HOUSE, its drinks served in Harnedor's vessels. It also carried
     * Harnennor lore at 1 in 30; lore books are not ported.
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
            true, LOTRFoods.HARNEDOR_DRINK.getDrinkVessels());

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

    public static void fillInventory(Container inventory, RandomSource random, Pool pool, int amount, boolean isNPCDrop) {
        if (amount == -1) {
            amount = Mth.randomBetweenInclusive(random, pool.minItems(), pool.maxItems());
        } else if (amount <= 0) {
            throw new IllegalArgumentException("LOTRChestContents tried to fill a chest with " + amount + " items");
        }
        for (int i = 0; i < amount; ++i) {
            inventory.setItem(random.nextInt(inventory.getContainerSize()), pick(pool, random, isNPCDrop));
        }
    }

    /**
     * One of fillInventory's picks: a weighted entry and a count in its range;
     * for a pool with pouches, a pouch (not ported) or else a smith's scroll,
     * each 1 in 50; a damageable item worn up to three quarters; a brewed drink light to strong,
     * in one of the pool's vessels if it has them; the stack cut
     * to its maximum; and the mod's modifiers rolled on, skilful 1 in 5 unless
     * an NPC dropped it.
     */
    public static ItemStack pick(Pool pool, RandomSource random, boolean isNPCDrop) {
        int totalWeight = pool.entries().stream().mapToInt(Entry::weight).sum();
        int roll = random.nextInt(totalWeight);
        Entry entry = pool.entries().get(0);
        for (Entry candidate : pool.entries()) {
            roll -= candidate.weight();
            if (roll < 0) {
                entry = candidate;
                break;
            }
        }
        ItemStack stack = entry.item().get();
        stack.setCount(Mth.nextInt(random, entry.min(), entry.max()));
        if (!isNPCDrop && pool.pouches() && random.nextInt(50) != 0 && random.nextInt(50) == 0) {
            stack = LOTRModifiers.randomTemplate(random);
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
        LOTRModifiers.applyRandom(stack, random, !isNPCDrop && random.nextInt(5) == 0);
        return stack;
    }
}
