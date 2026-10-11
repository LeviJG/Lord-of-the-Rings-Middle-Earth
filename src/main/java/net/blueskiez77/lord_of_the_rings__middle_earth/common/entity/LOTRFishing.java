package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * LOTRFishing: what is fished up in Middle-earth (LOTREntityFishHook swapped in for the game's hook
 * there) -- one time in ten some junk (bones, rotten wood, an old leather hat, tools worn nearly
 * through), one in five some treasure (pearls, coins, pouches, ingots and nuggets, rings, a worn bow or
 * dagger), else a fish; Luck of the Sea making junk rarer and treasure likelier, Lure making both
 * rarer.
 */
public final class LOTRFishing {

    private static final List<FishingItem> FISH = new ArrayList<>();
    private static final List<FishingItem> JUNK = new ArrayList<>();
    private static final List<FishingItem> TREASURE = new ArrayList<>();

    static {
        FISH.add(new FishingItem(() -> Items.COD, 60));
        FISH.add(new FishingItem(() -> Items.SALMON, 25));
        FISH.add(new FishingItem(() -> Items.TROPICAL_FISH, 2));
        FISH.add(new FishingItem(() -> Items.PUFFERFISH, 13));
        JUNK.add(new FishingItem(() -> Items.FISHING_ROD, 5).setMaxDurability(0.1f));
        JUNK.add(new FishingItem(() -> Items.WOODEN_SWORD, 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.WOODEN_AXE, 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.WOODEN_PICKAXE, 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.WOODEN_SHOVEL, 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.WOODEN_HOE, 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(mod("leather_hat"), 10));
        JUNK.add(new FishingItem(() -> Items.LEATHER_HELMET, 5).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.LEATHER_BOOTS, 5).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(mod("bone_helmet"), 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(mod("bone_boots"), 2).setMaxDurability(0.5f));
        JUNK.add(new FishingItem(() -> Items.SKELETON_SKULL, 5));
        JUNK.add(new FishingItem(() -> Items.BONE, 20));
        JUNK.add(new FishingItem(mod("orc_bone"), 10));
        JUNK.add(new FishingItem(mod("elf_bone"), 2));
        JUNK.add(new FishingItem(mod("dwarf_bone"), 2));
        JUNK.add(new FishingItem(mod("hobbit_bone"), 1));
        JUNK.add(new FishingItem(mod("rotten_log"), 10));
        JUNK.add(new FishingItem(() -> Items.LEATHER, 10));
        JUNK.add(new FishingItem(() -> Items.STRING, 10));
        JUNK.add(new FishingItem(() -> Items.BOWL, 10));
        JUNK.add(new FishingItem(mod("mug"), 10));
        JUNK.add(new FishingItem(() -> Items.BOOK, 5));
        JUNK.add(new FishingItem(() -> Items.STICK, 10));
        JUNK.add(new FishingItem(() -> Items.FEATHER, 10));
        JUNK.add(new FishingItem(() -> Items.INK_SAC, 5));
        JUNK.add(new FishingItem(() -> Items.ROTTEN_FLESH, 5));
        JUNK.add(new FishingItem(mod("suspicious_meat"), 5));
        JUNK.add(new FishingItem(mod("maggoty_bread"), 5));
        JUNK.add(new FishingItem(mod("man_flesh"), 5));
        JUNK.add(new FishingItem(() -> Items.LILY_PAD, 15));
        TREASURE.add(new FishingItem(mod("pearl"), 200));
        TREASURE.add(new FishingItem(() -> Items.BOW, 20).setMaxDurability(0.75f));
        TREASURE.add(new FishingItem(() -> Items.FISHING_ROD, 20).setMaxDurability(0.75f));
        TREASURE.add(new FishingItem(mod("iron_dagger"), 20).setMaxDurability(0.75f));
        TREASURE.add(new FishingItem(mod("bronze_dagger"), 20).setMaxDurability(0.75f));
        TREASURE.add(new FishingItem(mod("silver_coin"), 100));
        TREASURE.add(new FishingItem(mod("silver_coin_stack"), 10));
        TREASURE.add(new FishingItem(mod("silver_coin_pile"), 1));
        TREASURE.add(new FishingItem(mod("small_pouch"), 20));
        TREASURE.add(new FishingItem(mod("medium_pouch"), 10));
        TREASURE.add(new FishingItem(mod("large_pouch"), 5));
        TREASURE.add(new FishingItem(() -> Items.IRON_INGOT, 20));
        TREASURE.add(new FishingItem(() -> Items.IRON_NUGGET, 10));
        TREASURE.add(new FishingItem(mod("bronze_ingot"), 10));
        TREASURE.add(new FishingItem(() -> Items.COPPER_INGOT, 10));
        TREASURE.add(new FishingItem(mod("tin_ingot"), 10));
        TREASURE.add(new FishingItem(() -> Items.GOLD_NUGGET, 50));
        TREASURE.add(new FishingItem(() -> Items.GOLD_INGOT, 5));
        TREASURE.add(new FishingItem(mod("silver_nugget"), 50));
        TREASURE.add(new FishingItem(mod("silver_ingot"), 5));
        TREASURE.add(new FishingItem(mod("mithril_nugget"), 5));
        TREASURE.add(new FishingItem(mod("silver_ring"), 10));
        TREASURE.add(new FishingItem(mod("gold_ring"), 5));
        TREASURE.add(new FishingItem(mod("mithril_ring"), 1));
    }

    private LOTRFishing() {
    }

    private static Supplier<Item> mod(String name) {
        return () -> BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
    }

    /** getFishResult: by the chance drawn, junk, then treasure, else a fish -- or only a fish if junk and treasure are not allowed. */
    public static ItemStack getFishResult(RandomSource rand, float chance, int luck, int speed, boolean allowJunkTreasure) {
        float junkChance = Mth.clamp(0.1f - luck * 0.025f - speed * 0.01f, 0.0f, 1.0f);
        float treasureChance = Mth.clamp(0.2f + luck * 0.01f - speed * 0.01f, 0.0f, 1.0f);
        if (allowJunkTreasure) {
            if (chance < junkChance) {
                return LOTRWeightedRandom.getRandomItem(rand, JUNK).getRandomResult(rand);
            }
            chance -= junkChance;
            if (chance < treasureChance) {
                return LOTRWeightedRandom.getRandomItem(rand, TREASURE).getRandomResult(rand);
            }
        }
        return LOTRWeightedRandom.getRandomItem(rand, FISH).getRandomResult(rand);
    }

    private static final class FishingItem implements LOTRWeightedRandom.Item {
        private final Supplier<Item> item;
        private final int weight;
        private float maxDurability;

        FishingItem(Supplier<Item> item, int weight) {
            this.item = item;
            this.weight = weight;
        }

        FishingItem setMaxDurability(float f) {
            this.maxDurability = f;
            return this;
        }

        @Override
        public int itemWeight() {
            return this.weight;
        }

        /** Worn by up to the fraction given, but never quite to breaking. */
        ItemStack getRandomResult(RandomSource rand) {
            ItemStack result = new ItemStack(this.item.get());
            if (this.maxDurability > 0.0f && result.isDamageableItem()) {
                float damageF = 1.0f - rand.nextFloat() * this.maxDurability;
                int damage = (int) (damageF * result.getMaxDamage());
                damage = Math.max(Math.min(damage, result.getMaxDamage()), 1);
                result.setDamageValue(damage);
            }
            return result;
        }
    }
}
