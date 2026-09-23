package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Block tags the mod defines for itself. */
public final class LOTRBlockTags {
    /**
     * What LOTRBiomeGenMordor.isSurfaceMordorBlock accepted: Mordor rock,
     * Mordor dirt and Mordor gravel. The Mordor plants grow on these and on
     * nothing else. A tag rather than a hard-coded list so a datapack can widen
     * it, and so the check does not have to reach back into LOTRBlocks.
     */
    public static final TagKey<Block> MORDOR_SURFACE = TagKey.create(
            Registries.BLOCK, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mordor_surface"));

    /**
     * The bed a reed will root in: the floor under the shallow water it stands
     * in. The 1.7.10 block asked only for a water source above and did not care
     * what was under it, so this tag has no counterpart in the original.
     */
    public static final TagKey<Block> REEDS_PLANTABLE_ON = TagKey.create(
            Registries.BLOCK, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "reeds_plantable_on"));

    /**
     * What a mattock digs well: LOTRItemMattock was a pickaxe that also took
     * setHarvestLevel("axe") and cut wood, plants and vines at full speed.
     */
    public static final TagKey<Block> MATTOCK_MINEABLE = TagKey.create(
            Registries.BLOCK, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mineable/mattock"));

    private LOTRBlockTags() {
    }
}
