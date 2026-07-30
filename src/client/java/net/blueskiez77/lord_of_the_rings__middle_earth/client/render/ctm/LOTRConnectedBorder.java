package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Which border pieces a single face should draw, given its eight in-plane
 * neighbours.
 *
 * Ported from lotr.client.render.LOTRConnectedTextures#getConnectedIcon
 * (1.7.10). This class is deliberately free of any Minecraft rendering type so
 * the rule can be checked in isolation: brute-forcing all 256 neighbour
 * combinations against the original produced zero mismatches.
 *
 * The effect: a border edge is drawn on a side that has NO matching neighbour,
 * so a lone block looks fully framed and the frame recedes wherever two
 * matching blocks meet. Outer corners fill the diagonal where two drawn edges
 * meet. Inner corners patch the small notch left when both neighbours of a
 * diagonal are present but the diagonal itself is not.
 */
public final class LOTRConnectedBorder {

    /**
     * The twelve overlay pieces layered over the base tile.
     *
     * {@link #textureSuffix} is appended to a type's texture base name, so a
     * block using base name "mithril_block" looks for
     * assets/lotr/textures/block/connected/mithril_block_edge_top.png and so on.
     */
    public enum Piece {
        EDGE_TOP("edge_top"),
        EDGE_BOTTOM("edge_bottom"),
        EDGE_LEFT("edge_left"),
        EDGE_RIGHT("edge_right"),
        CORNER_TOP_LEFT("corner_top_left"),
        CORNER_TOP_RIGHT("corner_top_right"),
        CORNER_BOTTOM_LEFT("corner_bottom_left"),
        CORNER_BOTTOM_RIGHT("corner_bottom_right"),
        INNER_TOP_LEFT("inner_top_left"),
        INNER_TOP_RIGHT("inner_top_right"),
        INNER_BOTTOM_LEFT("inner_bottom_left"),
        INNER_BOTTOM_RIGHT("inner_bottom_right");

        private final String textureSuffix;

        Piece(String textureSuffix) {
            this.textureSuffix = textureSuffix;
        }

        public String textureSuffix() {
            return textureSuffix;
        }
    }

    /** Suffix of the always-drawn background tile. */
    public static final String BASE_SUFFIX = "base";

    /**
     * Every piece set the rules can actually produce.
     *
     * There are 256 neighbour configurations but only 47 distinct outcomes --
     * the same 47 that the connected-texture format enumerates, and the same set
     * the original built in IconElement.allCombos. One composited sprite is
     * generated per entry.
     */
    public static Set<Set<Piece>> allCombinations() {
        Set<Set<Piece>> combos = new LinkedHashSet<>();

        for (int bits = 0; bits < 256; bits++) {
            combos.add(piecesFor(
                    (bits & 1) != 0, (bits & 2) != 0, (bits & 4) != 0, (bits & 8) != 0,
                    (bits & 16) != 0, (bits & 32) != 0, (bits & 64) != 0, (bits & 128) != 0));
        }

        return combos;
    }

    /**
     * Stable name for a piece set, used as the composited sprite's id suffix.
     *
     * A bitmask over Piece ordinals, so it is deterministic, independent of
     * iteration order, and unique by construction. Joining ordinals with
     * separators instead is not safe: the empty set and the set containing only
     * ordinal 0 both render as "0".
     */
    public static String keyOf(Set<Piece> pieces) {
        int mask = 0;

        for (Piece piece : pieces) {
            mask |= 1 << piece.ordinal();
        }

        return Integer.toString(mask);
    }

    private LOTRConnectedBorder() {
    }

    /**
     * @param topLeft     top-left neighbour matches
     * @param top         top neighbour matches
     * @param topRight    top-right neighbour matches
     * @param left        left neighbour matches
     * @param right       right neighbour matches
     * @param bottomLeft  bottom-left neighbour matches
     * @param bottom      bottom neighbour matches
     * @param bottomRight bottom-right neighbour matches
     * @return the overlay pieces to draw over the base tile
     */
    public static Set<Piece> piecesFor(boolean topLeft, boolean top, boolean topRight,
                                       boolean left, boolean right,
                                       boolean bottomLeft, boolean bottom, boolean bottomRight) {
        Set<Piece> pieces = EnumSet.noneOf(Piece.class);

        if (!left) pieces.add(Piece.EDGE_LEFT);
        if (!right) pieces.add(Piece.EDGE_RIGHT);
        if (!top) pieces.add(Piece.EDGE_TOP);
        if (!bottom) pieces.add(Piece.EDGE_BOTTOM);

        if (!left && !top) pieces.add(Piece.CORNER_TOP_LEFT);
        if (!right && !top) pieces.add(Piece.CORNER_TOP_RIGHT);
        if (!left && !bottom) pieces.add(Piece.CORNER_BOTTOM_LEFT);
        if (!right && !bottom) pieces.add(Piece.CORNER_BOTTOM_RIGHT);

        if (left && top && !topLeft) pieces.add(Piece.INNER_TOP_LEFT);
        if (right && top && !topRight) pieces.add(Piece.INNER_TOP_RIGHT);
        if (left && bottom && !bottomLeft) pieces.add(Piece.INNER_BOTTOM_LEFT);
        if (right && bottom && !bottomRight) pieces.add(Piece.INNER_BOTTOM_RIGHT);

        return pieces;
    }
}