package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

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
 *   assets/lotr/textures/block/ctm/&lt;textureBaseName&gt;_&lt;suffix&gt;.png
 * where suffix is "base" or one of {@link LOTRConnectedBorder.Piece}'s suffixes.
 */
public record LOTRConnectedBorderType(String textureBaseName, Matcher matcher) {

    /** Where all connected-border textures live, relative to the assets root. */
    public static final String TEXTURE_FOLDER = "block/ctm/";

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

    /**
     * Full texture id for the inventory icon: the base with its full frame
     * composited on, i.e. how the block looks with no neighbours.
     *
     * This is one of the 47 sprites the sprite source composites, so there is no
     * shipped file to keep in sync -- exactly the combination the original's
     * inventory icon used.
     */
    public Identifier itemTexture() {
        return LOTRConnectedBorderSpriteSource.spriteFor(root(),
                LOTRConnectedBorder.piecesFor(false, false, false, false, false, false, false, false));
    }

    /**
     * The family root: folder + base name, with NO piece suffix.
     *
     * This is the id declared in atlases/blocks.json and the one the sprite
     * source names its composited sprites from, so every lookup of a composited
     * sprite must start here -- not from baseTexture(), which already carries
     * the "_base" suffix.
     */
    public Identifier root() {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, TEXTURE_FOLDER + textureBaseName);
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