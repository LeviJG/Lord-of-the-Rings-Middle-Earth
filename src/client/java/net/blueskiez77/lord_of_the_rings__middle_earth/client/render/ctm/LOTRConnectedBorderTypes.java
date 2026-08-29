package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.world.level.block.Block;

// The single list of blocks that render with a connected border. Two unrelated systems need to agree on this: the render plugin (LOTRConnectedBorderPlugin), which installs the world model, and datagen (LOTRModelProvider), which has to give these blocks an item model pointing at the same base texture instead of the default block/&lt;name&gt; one. Keeping the registry here, populated in a static initialiser, means neither can silently disagree with the other about which blocks are involved or where their textures live. TO ADD A FAMILY: 1. Drop thirteen textures into assets/lotr/textures/block/ctm/ -- &lt;base&gt;_base plus the twelve piece suffixes in LOTRConnectedBorder.Piece. 2. Add one line to the static block below. Nothing else: no per-family classes, no model JSON, no atlas edit (the atlases/blocks.json directory source stitches the whole folder), and no duplicated texture for the item icon. Still to come from the 1.7.10 mod, once these blocks exist in the port: dwarven gold/silver/mithril bricks, cobblebrick, ceramic/stone/wood plates, the gate family and the birdcages. Gates carry a facing, so they will likely want a Matcher comparing orientation rather than sameBlock.
public final class LOTRConnectedBorderTypes {
    private static final Map<Block, LOTRConnectedBorderType> TYPES = new LinkedHashMap<>();

    static {
        register(LOTRBlocks.MITHRIL_BLOCK, LOTRConnectedBorderType.sameBlock("mithril_block"));

        register(LOTRBlocks.DAUB, LOTRConnectedBorderType.sameBlock("daub"));
        // LOTRBlockCobblebrick connected unconditionally -- it had one subtype.
        register(LOTRBlocks.DRYSTONE, LOTRConnectedBorderType.sameBlock("drystone"));
        // brick metas 8/9/10 -- the trimmed dwarven set. LOTRBlockBrick.getIcon
        // guards on those three metas and no other brick, which is why the plain
        // dwarven brick stays flat.
        register(LOTRBlocks.DWARVEN_SILVER_BRICK, LOTRConnectedBorderType.sameBlock("dwarven_silver_brick"));
        register(LOTRBlocks.DWARVEN_GOLD_BRICK, LOTRConnectedBorderType.sameBlock("dwarven_gold_brick"));
        register(LOTRBlocks.DWARVEN_MITHRIL_BRICK, LOTRConnectedBorderType.sameBlock("dwarven_mithril_brick"));
    }

    private LOTRConnectedBorderTypes() {
    }

    private static void register(Block block, LOTRConnectedBorderType type) {
        TYPES.put(block, type);
    }

    public static Map<Block, LOTRConnectedBorderType> all() {
        return Collections.unmodifiableMap(TYPES);
    }

    public static LOTRConnectedBorderType get(Block block) {
        return TYPES.get(block);
    }

    public static boolean has(Block block) {
        return TYPES.containsKey(block);
    }
}