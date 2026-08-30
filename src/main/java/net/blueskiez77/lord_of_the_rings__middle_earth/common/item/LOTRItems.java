package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

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