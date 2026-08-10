package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

// Which border pieces a single face should draw, given its eight in-plane neighbours. Ported from lotr.client.render.LOTRConnectedTextures#getConnectedIcon (1.7.10). This class is deliberately free of any Minecraft rendering type so the rule can be checked in isolation: brute-forcing all 256 neighbour combinations against the original produced zero mismatches. The effect: a border edge is drawn on a side that has NO matching neighbour, so a lone block looks fully framed and the frame recedes wherever two matching blocks meet. Outer corners fill the diagonal where two drawn edges meet. Inner corners patch the small notch left when both neighbours of a diagonal are present but the diagonal itself is not.
public final class LOTRConnectedBorder {
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

    public static final String BASE_SUFFIX = "base";

    public static Set<Set<Piece>> allCombinations() {
        Set<Set<Piece>> combos = new LinkedHashSet<>();

        for (int bits = 0; bits < 256; bits++) {
            combos.add(piecesFor(
                    (bits & 1) != 0, (bits & 2) != 0, (bits & 4) != 0, (bits & 8) != 0,
                    (bits & 16) != 0, (bits & 32) != 0, (bits & 64) != 0, (bits & 128) != 0));
        }

        return combos;
    }

    public static String keyOf(Set<Piece> pieces) {
        int mask = 0;

        for (Piece piece : pieces) {
            mask |= 1 << piece.ordinal();
        }

        return Integer.toString(mask);
    }

    private LOTRConnectedBorder() {
    }

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