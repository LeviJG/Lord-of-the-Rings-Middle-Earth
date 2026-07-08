package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Central block registry for the mod. Mirrors the ~694 static Block fields the
 * original LOTRMod.java declared.
 *
 * 26.1 registration pattern (verified against Fabric docs):
 *  - Build a ResourceKey<Block> from Registries.BLOCK + the mod Identifier,
 *    and set it on the BlockBehaviour.Properties via setId(key).
 *  - Register the block into BuiltInRegistries.BLOCK.
 *  - For the item form, build a ResourceKey<Item> (same path), set it on the
 *    Item.Properties via setId, construct a BlockItem, register into ITEM.
 *
 * Simple blocks are registered here in bulk; models/blockstates/loot/recipes
 * come from datagen. This starts with one representative block to prove the
 * pipeline end-to-end.
 */
public final class LOTRBlocks {

    // First representative block: a decorative mineral block.
    public static final Block MITHRIL_ORE = register("mithril_ore",
            Block::new, BlockBehaviour.Properties.of().strength(3.0f, 5.0f), true);

    private LOTRBlocks() {
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                 BlockBehaviour.Properties properties, boolean withItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        Block block = factory.apply(properties.setId(blockKey));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        if (withItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
            BlockItem blockItem = new BlockItem(block,
                    new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        return block;
    }

    /** Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}