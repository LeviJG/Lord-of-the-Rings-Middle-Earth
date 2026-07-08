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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * Central block registry. Mirrors the ~694 static Block fields the original
 * LOTRMod.java declared.
 *
 * 26.1 registration: build ResourceKey<Block>, setId() on properties, register
 * into BuiltInRegistries.BLOCK; optionally create+register a BlockItem.
 *
 * Batch 1: metal/mineral ores. Original values (from LOTRBlockOre): base
 * hardness 3.0, resistance 5.0. Harvest levels: copper/tin = stone (needs
 * stone pickaxe), silver/mithril = iron. Mithril is tougher (4.0/10.0). The
 * "requires correct tool" flag + the mineable/needs_*_tool tags (datagen)
 * reproduce the original harvest-level gating.
 */
public final class LOTRBlocks {

    // --- Batch 1: ores ---
    // Stone-tier ores (copper, tin) — mineable with a stone pickaxe.
    public static final Block COPPER_ORE = registerOre("copper_ore", 3.0f, 5.0f);
    public static final Block TIN_ORE = registerOre("tin_ore", 3.0f, 5.0f);
    // Iron-tier ores (silver, mithril).
    public static final Block SILVER_ORE = registerOre("silver_ore", 3.0f, 5.0f);
    public static final Block MITHRIL_ORE = registerOre("mithril_ore", 4.0f, 10.0f);
    // Soft mineral ores (salt, saltpeter, sulfur) — still need a pickaxe.
    public static final Block SALT_ORE = registerOre("salt_ore", 3.0f, 5.0f);
    public static final Block SALTPETER_ORE = registerOre("saltpeter_ore", 3.0f, 5.0f);
    public static final Block SULFUR_ORE = registerOre("sulfur_ore", 3.0f, 5.0f);

    private LOTRBlocks() {
    }

    /** Standard stone-like ore: requires the correct tool, stone sound. */
    private static Block registerOre(String name, float hardness, float resistance) {
        return register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true);
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