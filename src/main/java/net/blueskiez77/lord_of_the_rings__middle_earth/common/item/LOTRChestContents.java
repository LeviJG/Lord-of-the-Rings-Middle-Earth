package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The LOTRChestContents pools that items and entities draw from outside of
 * structures, and fillInventory's single pick. Structure chests are not
 * ported yet; when they are, their pools belong here too.
 */
public final class LOTRChestContents {

    public record Entry(Supplier<ItemStack> item, int min, int max, int weight) {
    }

    /**
     * A pool: its entries, plus the two extras a pool may enable. Pouches are
     * deliberately not ported, so a pool with them keeps only the pouch roll's
     * other branch, the 1 in 50 smith's scroll.
     */
    public record Pool(List<Entry> entries, boolean pouches) {
    }

    /**
     * MIRKWOOD_LOOT, entry for entry. It also carried Woodland Realm and Dol
     * Guldur lore at 1 in 20; lore books are not ported.
     */
    public static final Pool MIRKWOOD_LOOT = new Pool(List.of(
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

    /** ANCIENT_SWORD: orc scimitar, and the elven, Gondor, Arnor and Dwarven swords. */
    public static final Pool ANCIENT_SWORD = new Pool(List.of(
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
    public static final Pool ANCIENT_DAGGER = new Pool(List.of(
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
    public static final Pool ANCIENT_HELMET = new Pool(List.of(
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
    public static final Pool ANCIENT_BODY = new Pool(List.of(
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
    public static final Pool ANCIENT_LEGS = new Pool(List.of(
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
    public static final Pool ANCIENT_BOOTS = new Pool(List.of(
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

    /**
     * One of fillInventory's picks: a weighted entry and a count in its range;
     * for a pool with pouches, a pouch (not ported) or else a smith's scroll,
     * each 1 in 50; a damageable item worn up to three quarters; the stack cut
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
        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }
        LOTRModifiers.applyRandom(stack, random, !isNPCDrop && random.nextInt(5) == 0);
        return stack;
    }
}
