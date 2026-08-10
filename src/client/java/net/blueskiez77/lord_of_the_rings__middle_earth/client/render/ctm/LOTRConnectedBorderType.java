package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public record LOTRConnectedBorderType(String textureBaseName, Matcher matcher) {
    public static final String TEXTURE_FOLDER = "block/ctm/";

    @FunctionalInterface
    public interface Matcher {
        boolean connects(BlockAndTintGetter level,
                         BlockPos selfPos, BlockState selfState,
                         BlockPos otherPos, BlockState otherState);
    }

    public static LOTRConnectedBorderType sameBlock(String textureBaseName) {
        return new LOTRConnectedBorderType(textureBaseName,
                (level, selfPos, selfState, otherPos, otherState) ->
                        otherState.is(selfState.getBlock()));
    }

    public Identifier itemTexture() {
        return LOTRConnectedBorderSpriteSource.spriteFor(root(),
                LOTRConnectedBorder.piecesFor(false, false, false, false, false, false, false, false));
    }

    // The family root: folder + base name, with NO piece suffix. This is the id declared in atlases/blocks.json and the one the sprite source names its composited sprites from, so every lookup of a composited sprite must start here -- not from baseTexture(), which already carries the "_base" suffix.
    public Identifier root() {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, TEXTURE_FOLDER + textureBaseName);
    }

    public Identifier baseTexture() {
        return texture(LOTRConnectedBorder.BASE_SUFFIX);
    }

    public Identifier pieceTexture(LOTRConnectedBorder.Piece piece) {
        return texture(piece.textureSuffix());
    }

    private Identifier texture(String suffix) {
        return Identifier.fromNamespaceAndPath(
                LOTRMod.NAMESPACE, TEXTURE_FOLDER + textureBaseName + "_" + suffix);
    }
}