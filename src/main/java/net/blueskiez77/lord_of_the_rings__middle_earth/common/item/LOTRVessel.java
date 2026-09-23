package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import io.netty.buffer.ByteBuf;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemMug.Vessel: what a drink is held in.
 *
 * <p>1.7.10 packed the vessel into the item damage ({@code vesselMeta} = 100);
 * here it is the {@code lotr:vessel} component, so a drink keeps its cup when it
 * moves around and the item model can pick the right picture from it. The
 * {@code icon} names are the original's drink_*.png suffixes.
 *
 * <p>Vanilla's glass bottle is the BOTTLE vessel empty, and its water bottle is
 * water in a BOTTLE: {@link #fill} and {@link #equivalentDrink} convert between
 * the two, as setVessel and getEquivalentDrink did.
 */
public enum LOTRVessel implements StringRepresentable {
    MUG("mug", "mug"),
    MUG_CLAY("ceramic_mug", "clay"),
    GOBLET_GOLD("golden_goblet", "gobletGold"),
    GOBLET_SILVER("silver_goblet", "gobletSilver"),
    GOBLET_COPPER("copper_goblet", "gobletCopper"),
    GOBLET_WOOD("wooden_cup", "gobletWood"),
    SKULL("skull_cup", "skull"),
    GLASS("wine_glass", "glass"),
    BOTTLE("bottle", "bottle"),
    SKIN("waterskin", "skin"),
    HORN("ale_horn", "horn"),
    HORN_GOLD("golden_ale_horn", "hornGold");

    public static final Codec<LOTRVessel> CODEC = StringRepresentable.fromEnum(LOTRVessel::values);
    public static final StreamCodec<ByteBuf, LOTRVessel> STREAM_CODEC = ByteBufCodecs.idMapper(
            ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);

    private final String name;
    private final String icon;

    LOTRVessel(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String icon() {
        return this.icon;
    }

    /** Vessel.getEmptyVesselItem: the cup handed back when the drink is gone. */
    public Item emptyItem() {
        return switch (this) {
            case MUG -> LOTRItems.MUG;
            case MUG_CLAY -> LOTRItems.CERAMIC_MUG;
            case GOBLET_GOLD -> LOTRItems.GOLDEN_GOBLET;
            case GOBLET_SILVER -> LOTRItems.SILVER_GOBLET;
            case GOBLET_COPPER -> LOTRItems.COPPER_GOBLET;
            case GOBLET_WOOD -> LOTRItems.WOODEN_CUP;
            case SKULL -> LOTRItems.SKULL_CUP;
            case GLASS -> LOTRItems.WINE_GLASS;
            case BOTTLE -> Items.GLASS_BOTTLE;
            case SKIN -> LOTRItems.WATERSKIN;
            case HORN -> LOTRItems.ALE_HORN;
            case HORN_GOLD -> LOTRItems.GOLDEN_ALE_HORN;
        };
    }

    public ItemStack emptyStack() {
        return new ItemStack(emptyItem());
    }

    /** Vessel.canPlace: all but the waterskin can be set down. */
    public boolean canPlace() {
        return this != SKIN;
    }

    /** Vessel.getBlock: what a vessel set down becomes. */
    public Block block() {
        return switch (this) {
            case MUG -> LOTRBlocks.MUG_BLOCK;
            case MUG_CLAY -> LOTRBlocks.CERAMIC_MUG_BLOCK;
            case GOBLET_GOLD -> LOTRBlocks.GOLDEN_GOBLET_BLOCK;
            case GOBLET_SILVER -> LOTRBlocks.SILVER_GOBLET_BLOCK;
            case GOBLET_COPPER -> LOTRBlocks.COPPER_GOBLET_BLOCK;
            case GOBLET_WOOD -> LOTRBlocks.WOODEN_CUP_BLOCK;
            case SKULL -> LOTRBlocks.SKULL_CUP_BLOCK;
            case GLASS -> LOTRBlocks.WINE_GLASS_BLOCK;
            case BOTTLE -> LOTRBlocks.BOTTLE_BLOCK;
            case SKIN -> null;
            case HORN -> LOTRBlocks.ALE_HORN_BLOCK;
            case HORN_GOLD -> LOTRBlocks.GOLDEN_ALE_HORN_BLOCK;
        };
    }

    /** getVessel(ItemStack): what a stack is held in, or null if it is not a vessel at all. */
    public static LOTRVessel of(ItemStack stack) {
        if (stack.getItem() instanceof LOTRDrinkItem) {
            return LOTRDrinkItem.vessel(stack);
        }
        if (stack.getItem() instanceof LOTRVesselItem vesselItem) {
            return vesselItem.vessel();
        }
        if (stack.is(Items.GLASS_BOTTLE) || isWaterBottle(stack)) {
            return BOTTLE;
        }
        return null;
    }

    public static boolean isWaterBottle(ItemStack stack) {
        return stack.is(Items.POTION)
                && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
    }

    /** isItemEmptyDrink. */
    public static boolean isEmptyDrink(ItemStack stack) {
        return stack.getItem() instanceof LOTRVesselItem || stack.is(Items.GLASS_BOTTLE);
    }

    /** isItemFullDrink. */
    public static boolean isFullDrink(ItemStack stack) {
        return stack.getItem() instanceof LOTRDrinkItem || isWaterBottle(stack);
    }

    /** getEquivalentDrink: vanilla's water bottle counts as water in a bottle. */
    public static ItemStack equivalentDrink(ItemStack stack) {
        if (isWaterBottle(stack)) {
            ItemStack water = new ItemStack(LOTRItems.WATER, stack.getCount());
            water.set(LOTRDataComponents.VESSEL, BOTTLE);
            return LOTRPoisonedDrinks.copyPoison(stack, water);
        }
        return stack;
    }

    /**
     * setVessel(drink, this, true): a copy of the drink moved into this vessel.
     * Water going into a bottle becomes vanilla's water bottle.
     */
    public ItemStack fill(ItemStack drink) {
        ItemStack result = equivalentDrink(drink).copy();
        if (this == BOTTLE && result.is(LOTRItems.WATER)) {
            return LOTRPoisonedDrinks.copyPoison(result,
                    PotionContents.createItemStack(Items.POTION, Potions.WATER).copyWithCount(result.getCount()));
        }
        result.set(LOTRDataComponents.VESSEL, this);
        return result;
    }
}
