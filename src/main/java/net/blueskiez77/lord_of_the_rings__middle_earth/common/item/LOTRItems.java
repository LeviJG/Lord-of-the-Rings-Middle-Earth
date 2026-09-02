package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class LOTRItems {
    // Item under the same id is a duplicate-key crash on load.

    public static final Item MITHRIL = register("mithril",
            Item::new, new Item.Properties());
    public static final Item PIPEWEED = register("pipeweed",
            Item::new, new Item.Properties());

    // LOTRItemKebab: new LOTRItemKebab(8, 0.8f, true) -- heal 8, saturation
    // 0.8, and wolves will eat it. It is what the kebab stand turns raw meat
    // into, so the stand needs it registered.
    //
    // NOT ported: onEaten had a one-in-a-hundred chance of telling the player
    // "That was a good kebab. You feel a lot better." A consumption hook like
    // that wants an item subclass; the joke is noted here so it is not lost.
    public static final Item KEBAB = register("kebab",
            Item::new, new Item.Properties()
                    .food(new FoodProperties(8, 0.8f, false)));

    // LOTRItemTrollStatue: places a stone troll. Stack size 1, as it was --
    // outfit and head count live in the stack, so two statues are rarely alike.
    public static final Item TROLL_STATUE = register("troll_statue",
            LOTRTrollStatueItem::new, new Item.Properties().stacksTo(1));

    // LOTRItemBossTrophy's two subtypes, one item each. Stack size 1, as it was.
    public static final Item MOUNTAIN_TROLL_CHIEFTAIN_TROPHY = register(
            LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN.itemName(),
            props -> new LOTRBossTrophyItem(LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN, props),
            new Item.Properties().stacksTo(1));
    public static final Item MALLORN_ENT_TROPHY = register(
            LOTRTrophyType.MALLORN_ENT.itemName(),
            props -> new LOTRBossTrophyItem(LOTRTrophyType.MALLORN_ENT, props),
            new Item.Properties().stacksTo(1));

    /** The item a given trophy entity gives back when it is knocked down. */
    public static Item trophyItem(LOTRTrophyType type) {
        return type == LOTRTrophyType.MALLORN_ENT
                ? MALLORN_ENT_TROPHY
                : MOUNTAIN_TROLL_CHIEFTAIN_TROPHY;
    }

    /**
     * One of the two forms a treasure pile is carried in -- the two-pixel carpet
     * at one layer, or the full block at eight. Called from LOTRBlocks while the
     * piles are being built; see registerTreasurePile for why the order of the
     * two calls matters.
     */
    public static Item registerTreasurePileItem(Block pile, String name, int layers) {
        return register(name,
                props -> new LOTRTreasurePileItem(pile, layers, props),
                new Item.Properties());
    }

    private LOTRItems() {
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        T item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    // Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}