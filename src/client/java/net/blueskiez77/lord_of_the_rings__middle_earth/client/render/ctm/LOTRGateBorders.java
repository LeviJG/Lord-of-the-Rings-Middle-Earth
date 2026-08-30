package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * Where a gate's art lives. Unlike {@link LOTRConnectedBorderTypes}, which
 * needs a hand-written line per family because the blocks it covers are
 * unrelated to each other, every gate follows the same rule -- so the list is
 * just LOTRBlocks.ALL_GATES and the texture names come off the block id.
 *
 * <p>Each gate therefore owns thirteen files under
 * {@code assets/lotr/textures/block/ctm/}: {@code <name>_base} and the twelve
 * piece suffixes in {@link LOTRConnectedBorder.Piece}. For the three gates
 * built with {@code hasConnectedTextures = false}, those thirteen are the
 * 3/16 border slices that LOTRConnectedTextures.registerNonConnectedGateIcons
 * used to cut out of the flat sprite at load time, over a transparent base.
 */
public final class LOTRGateBorders {
    private LOTRGateBorders() {
    }

    public static List<LOTRGateBlock> all() {
        List<LOTRGateBlock> gates = new ArrayList<>();

        for (Block block : LOTRBlocks.ALL_GATES) {
            if (block instanceof LOTRGateBlock gate) {
                gates.add(gate);
            }
        }

        return gates;
    }

    public static String name(Block gate) {
        return BuiltInRegistries.BLOCK.getKey(gate).getPath();
    }

    /** Folder + base name with NO piece suffix: what atlases/blocks.json declares. */
    public static Identifier root(Block gate) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                LOTRConnectedBorderType.TEXTURE_FOLDER + name(gate));
    }

    /**
     * Gates whose flat sprite is not {@code lotr:block/<name>}. The two dwarven
     * doors wear vanilla stone so that a closed one is invisible in a stone
     * wall: LOTRBlockGateDwarven.getIcon returned
     * {@code Blocks.stone.getIcon(side, 0)} while closed, and registered its
     * border pieces from that same texture's name.
     */
    private static final Map<String, Identifier> FLAT_TEXTURE_OVERRIDES = Map.of(
            "dwarven_door", Identifier.withDefaultNamespace("block/stone"),
            "ithildin_dwarven_door", Identifier.withDefaultNamespace("block/stone"));

    /**
     * The plain, unframed sprite. Only the non-connected gates draw it -- it is
     * what LOTRBlockGate.getIcon returned as {@code blockIcon} for a closed
     * gate with no connected art.
     */
    public static Identifier flatTexture(Block gate) {
        String name = name(gate);
        Identifier override = FLAT_TEXTURE_OVERRIDES.get(name);
        if (override != null) {
            return override;
        }
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "block/" + name);
    }

    /**
     * The inventory icon. LOTRConnectedTextures.getConnectedIconItem asked for
     * the combination with no neighbours at all, so the item shows a fully
     * framed panel; a gate with no connected art shows its flat sprite.
     */
    public static Identifier itemTexture(LOTRGateBlock gate) {
        if (!gate.hasConnectedTextures()) {
            return flatTexture(gate);
        }
        return LOTRConnectedBorderSpriteSource.spriteFor(root(gate),
                LOTRConnectedBorder.piecesFor(false, false, false, false, false, false, false, false), true);
    }
}