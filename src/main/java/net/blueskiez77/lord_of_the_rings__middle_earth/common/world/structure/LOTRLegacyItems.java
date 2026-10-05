package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.serialization.Dynamic;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.ItemStackTheFlatteningFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The original structures' items -- {@code new ItemStack(item, count, meta)} --
 * as port stacks. The mod's come from {@code lotr/legacy_items.tsv}
 * (generated from the parity table, field and metadata to port item); 1.7.10's
 * own go through the game's data fixers, the flattening turning a name and a
 * damage value into today's item ("dye" 4 to lapis, "fish" to cod) and the
 * later fixes carrying the name on.
 */
public final class LOTRLegacyItems {

    private static final int FLATTENING_VERSION = 1451;

    private static final Map<String, Map<Integer, Identifier>> MOD_ITEMS = new HashMap<>();
    private static final Map<String, Item> VANILLA_CACHE = new ConcurrentHashMap<>();

    private LOTRLegacyItems() {
    }

    public static void init() {
        try (InputStream in = LOTRLegacyItems.class.getResourceAsStream("/lotr/legacy_items.tsv")) {
            if (in == null) {
                throw new IllegalStateException("lotr/legacy_items.tsv is missing");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] cols = line.split("\t", -1);
                if (!cols[1].equals("*") && !cols[1].matches("\\d+")) {
                    LOTRMod.LOGGER.warn("Skipping legacy table row with non-numeric meta: {}", line);
                    continue;
                }
                int meta = cols[1].equals("*") ? -1 : Integer.parseInt(cols[1]);
                MOD_ITEMS.computeIfAbsent(cols[0], k -> new HashMap<>()).put(meta, Identifier.parse(cols[2]));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read lotr/legacy_items.tsv", e);
        }
    }

    /** {@code LOTRMod.<field>} with this metadata, or the block of that name's item. */
    public static Item mod(String field, int meta) {
        Map<Integer, Identifier> byMeta = MOD_ITEMS.get(field);
        if (byMeta == null) {
            return LOTRLegacyBlocks.mod(field).state(meta).getBlock().asItem();
        }
        Identifier id = byMeta.getOrDefault(meta, byMeta.getOrDefault(-1, byMeta.values().iterator().next()));
        return BuiltInRegistries.ITEM.getValue(id);
    }

    public static ItemStack modStack(String field) {
        return new ItemStack(mod(field, 0));
    }

    public static ItemStack modStack(String field, int count, int meta) {
        return new ItemStack(mod(field, meta), count);
    }

    /** {@code Items.<name>}, or a vanilla block's item, with this damage value. */
    public static Item vanilla(String name, int meta) {
        return VANILLA_CACHE.computeIfAbsent(name + "@" + meta, key -> {
            String old = "minecraft:" + name;
            String flattened = ItemStackTheFlatteningFix.updateItem(old, meta);
            if (flattened == null) {
                flattened = old;
            }
            Dynamic<Tag> fixed = DataFixers.getDataFixer().update(References.ITEM_NAME,
                    new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(flattened)), FLATTENING_VERSION,
                    SharedConstants.getCurrentVersion().dataVersion().version());
            Identifier id = Identifier.tryParse(fixed.asString(flattened));
            Item item = id == null ? Items.AIR : BuiltInRegistries.ITEM.getValue(id);
            if (item == Items.AIR) {
                item = LOTRLegacyBlocks.vanilla(name).state(meta).getBlock().asItem();
            }
            if (item == Items.AIR) {
                LOTRMod.LOGGER.warn("Unknown 1.7.10 item minecraft:{}:{}", name, meta);
            }
            return item;
        });
    }

    public static ItemStack vanillaStack(String name) {
        return new ItemStack(vanilla(name, 0));
    }

    public static ItemStack vanillaStack(String name, int count, int meta) {
        return new ItemStack(vanilla(name, meta), count);
    }
}
