package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.world.item.DyeColor;

/**
 * LOTRBlockBed. Eight beds that differ from a vanilla one only in what they are
 * upholstered with.
 *
 * <p>The original is a bare {@code BlockBed} subclass: it overrides the icons,
 * the dropped item and {@code isBed}, and nothing else. Sleeping, the two-block
 * placement, the respawn point and the nether explosion all come from vanilla,
 * and they still do here.
 *
 * <p>1.7.10 drew beds as ordinary block models rather than through a renderer,
 * with six textures per bed -- top, side and end, for each of the head and the
 * foot -- plus a plank texture on the underside taken from
 * {@code bedBottomBlock}. Modern beds are model-rendered again, so the port
 * keeps that arrangement instead of repacking the art into the entity-sheet
 * layout vanilla's own beds now use. See lotr:block/template_lotr_bed_head.
 *
 * <p>{@link DyeColor} has no counterpart in the original -- these beds are not
 * dyed, they are woven or furred -- but {@code BedBlock} requires one for
 * {@code getColor}, so every bed is registered as WHITE. Nothing reads it: the
 * colour drives only vanilla's own bed textures and item model, both of which
 * are replaced here.
 */
public class LOTRBedBlock extends net.minecraft.world.level.block.BedBlock {

    public LOTRBedBlock(Properties properties) {
        super(DyeColor.WHITE, properties);
    }
}
