package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockGateDwarven -- the "Dwarven Door".
 *
 * <p>A gate that is a full cube rather than a quarter-block panel, built out of
 * plain stone: closed, it is indistinguishable from the wall around it, and
 * only reveals itself when opened. That is the whole trick of the block, and it
 * is why {@link #hasConnectedTextures()} is false. LOTRBlockGateDwarven.getIcon
 * returned {@code Blocks.stone.getIcon(side, 0)} while closed and the
 * connected-border set only once open, with the border pieces sliced out of the
 * stone texture by registerNonConnectedGateIcons -- exactly the arrangement the
 * three portcullis gates use, so it needs no code here beyond the two flags.
 *
 * <p>The original also granted LOTRAchievement.useDwarvenDoor on a successful
 * activation. The port has no achievement system yet, so that is dropped rather
 * than stubbed; the block is otherwise complete.
 */
public class LOTRDwarvenDoorBlock extends LOTRGateBlock {

    public static final MapCodec<LOTRDwarvenDoorBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertiesCodec()
            ).apply(instance, LOTRDwarvenDoorBlock::new));

    public LOTRDwarvenDoorBlock(Properties properties) {
        // ct off: the closed door wears plain stone, not a bordered sprite.
        super(false, properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /** LOTRBlockGateDwarven's constructor called setFullBlock(). */
    @Override
    public boolean isFullBlock() {
        return true;
    }

    /**
     * Placing a door against another door of the same kind adopts that door's
     * facing instead of deriving a fresh one from the clicked face.
     *
     * <p>DEVIATION from 1.7.10, and a deliberate one. LOTRItemGate derived the
     * facing from the clicked face for a wall click but from the PLAYER'S YAW
     * for a click on a top or bottom face -- so stacking a second door on top
     * of the first gave the two different facings unless the player happened to
     * be looking exactly along the wall. For an ordinary gate that is harmless,
     * because LOTRBlockGate.directionsMatch treats the two horizontals of an
     * axis as equal. For the ithildin door it is fatal: its directionsMatch is
     * strict equality, so mismatched doors never join into one design, never
     * hide the seam between them, and never open together.
     *
     * <p>The original got away with it because multi-block ithildin doors were
     * placed by world generation (LOTRWorldGenStructureBase2.placeIthildinDoor
     * passes the direction and the DoorSize explicitly), not by hand. Building
     * one by hand needs this.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }
        // The block whose face was clicked, i.e. the one we are placing against.
        BlockPos againstPos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
        BlockState against = context.getLevel().getBlockState(againstPos);

        if (against.is(this)) {
            return state.setValue(FACING, against.getValue(FACING));
        }
        return state;
    }
}
