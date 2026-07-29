package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.connected;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Describes one connected-border family: where its textures live and what
 * counts as a matching neighbour.
 *
 * The 1.7.10 mod put both of these on the block itself
 * (LOTRConnectedBlock#getConnectedName and #areBlocksConnected). Splitting them
 * into a value object keeps the render code family-agnostic, so adding the
 * dwarven bricks, cobblebrick, daub, plates and gates later is one registration
 * line each rather than a new class each.
 *
 * Textures are looked up as:
 *   assets/lotr/textures/block/connected/&lt;textureBaseName&gt;_&lt;suffix&gt;.png
 * where suffix is "base" or one of {@link LOTRConnectedBorder.Piece}'s suffixes.
 */
public record LOTRConnectedBorderType(String textureBaseName, Matcher matcher) {

    /** Where all connected-border textures live, relative to the assets root. */
    public static final String TEXTURE_FOLDER = "block/connected/";

    /**
     * The modern equivalent of LOTRConnectedBlock#areBlocksConnected: decides
     * whether the block at {@code otherPos} should be treated as continuous
     * with the block at {@code selfPos}, hiding the border between them.
     */
    @FunctionalInterface
    public interface Matcher {
        boolean connects(BlockAndTintGetter level,
                         BlockPos selfPos, BlockState selfState,
                         BlockPos otherPos, BlockState otherState);
    }

    /**
     * The common rule, and the one the ore-storage blocks used: a neighbour
     * connects when it is the very same block.
     */
    public static LOTRConnectedBorderType sameBlock(String textureBaseName) {
        return new LOTRConnectedBorderType(textureBaseName,
                (level, selfPos, selfState, otherPos, otherState) ->
                        otherState.is(selfState.getBlock()));
    }

    /** Full texture id for the always-drawn background tile. */
    public Identifier baseTexture() {
        return texture(LOTRConnectedBorder.BASE_SUFFIX);
    }

    /** Full texture id for one overlay piece. */
    public Identifier pieceTexture(LOTRConnectedBorder.Piece piece) {
        return texture(piece.textureSuffix());
    }

    private Identifier texture(String suffix) {
        return Identifier.fromNamespaceAndPath(
                LOTRMod.NAMESPACE, TEXTURE_FOLDER + textureBaseName + "_" + suffix);
    }
}