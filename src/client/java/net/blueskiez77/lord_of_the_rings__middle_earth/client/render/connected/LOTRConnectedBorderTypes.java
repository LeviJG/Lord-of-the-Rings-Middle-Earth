package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.connected;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.world.level.block.Block;

/**
 * The single list of blocks that render with a connected border.
 *
 * Two unrelated systems need to agree on this: the render plugin
 * (LOTRConnectedBorderPlugin), which installs the world model, and datagen
 * (LOTRModelProvider), which has to give these blocks an item model pointing at
 * the same base texture instead of the default block/&lt;name&gt; one. Keeping the
 * registry here, populated in a static initialiser, means neither can silently
 * disagree with the other about which blocks are involved or where their
 * textures live.
 *
 * TO ADD A FAMILY:
 *   1. Drop fourteen textures into assets/lotr/textures/block/connected/ --
 *      &lt;base&gt;_base plus the twelve piece suffixes in
 *      LOTRConnectedBorder.Piece.
 *   2. Add one line to the static block below.
 * Nothing else: no per-family classes, no model JSON, no atlas edit (the
 * atlases/blocks.json directory source stitches the whole folder), and no
 * duplicated texture for the item icon.
 *
 * Still to come from the 1.7.10 mod, once these blocks exist in the port:
 * dwarven gold/silver/mithril bricks, cobblebrick, daub, ceramic/stone/wood
 * plates, the gate family and the birdcages. Gates carry a facing, so they will
 * likely want a Matcher comparing orientation rather than sameBlock.
 */
public final class LOTRConnectedBorderTypes {

    private static final Map<Block, LOTRConnectedBorderType> TYPES = new LinkedHashMap<>();

    static {
        // The only ore-storage block the original gave a connected border.
        register(LOTRBlocks.MITHRIL_BLOCK, LOTRConnectedBorderType.sameBlock("mithril_block"));
    }

    private LOTRConnectedBorderTypes() {
    }

    private static void register(Block block, LOTRConnectedBorderType type) {
        TYPES.put(block, type);
    }

    /** Every registered family, in declaration order. */
    public static Map<Block, LOTRConnectedBorderType> all() {
        return Collections.unmodifiableMap(TYPES);
    }

    /** The family a block belongs to, or null if it has no connected border. */
    public static LOTRConnectedBorderType get(Block block) {
        return TYPES.get(block);
    }

    /** Whether a block renders with a connected border. */
    public static boolean has(Block block) {
        return TYPES.containsKey(block);
    }
}
