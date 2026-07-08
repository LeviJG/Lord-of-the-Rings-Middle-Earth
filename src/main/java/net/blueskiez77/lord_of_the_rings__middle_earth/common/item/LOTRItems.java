package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Central item registry for the mod. Mirrors the role of the item declarations
 * in the original LOTRMod.java (which held ~822 static Item fields).
 *
 * 26.1 registration pattern (verified against Fabric docs):
 *  - Build a ResourceKey<Item> from Registries.ITEM + the mod Identifier.
 *  - Set that key on the Item.Properties via setId(key) BEFORE constructing.
 *  - Register into BuiltInRegistries.ITEM.
 *
 * Simple content items (materials, foods, trinkets) are registered here in
 * bulk; their models/recipes/tags come from datagen. Behavioural items
 * (weapons, tools, special interactions) get their own subclasses later.
 *
 * This starts with a couple of representative items to prove the pipeline;
 * the ~822-item bulk is ported in batches driven by the inventory JSON.
 */
public final class LOTRItems {

    // First representative simple items (from the original mod's roster).
    public static final Item MITHRIL = register("mithril",
            Item::new, new Item.Properties());
    public static final Item ATHELAS = register("athelas",
            Item::new, new Item.Properties());
    public static final Item PIPEWEED = register("pipeweed",
            Item::new, new Item.Properties());

    private LOTRItems() {
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        T item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    /** Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}