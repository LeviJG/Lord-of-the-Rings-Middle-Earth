package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRTableOfCommandBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockCommandTable -- the Table of Command.
 *
 * <p>A war table: an iron block carrying a plank surface that overhangs it on
 * every side, with a map of Middle-earth projected onto the top. Commanders use
 * it to review conquest and to direct squadrons of hired troops.
 *
 * <p>PARTIAL PORT -- the block, its shape and its zoom control are here; the
 * two screens it opens and the map drawn on its surface are not (PORT_PLAN
 * Track D).
 */
public class LOTRTableOfCommandBlock extends Block implements EntityBlock {

    public static final MapCodec<LOTRTableOfCommandBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertiesCodec()
            ).apply(instance, LOTRTableOfCommandBlock::new));

    public LOTRTableOfCommandBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRTableOfCommandBlockEntity(pos, state);
    }

    /**
     * LOTRBlockCommandTable.onBlockActivated, minus the two GUI branches.
     *
     * <p>What survives: SNEAK-right-click cycles the map's zoom, which is the
     * one interaction that needs no interface. The original then had two more
     * branches, both of which opened a screen -- holding a squadron banner
     * opened the squadron orders GUI (id 33), and otherwise, if conquest was
     * enabled for the world, it opened the conquest map (id 60).
     *
     * <p>Neither is ported: there are no squadrons and no conquest grid yet.
     * The zoom is still stored and synced, so it will already be right when the
     * map lands.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (!(level.getBlockEntity(pos) instanceof LOTRTableOfCommandBlockEntity table)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            table.toggleZoom();
            // The original announced the change with the block's own break
            // sound at half pitch -- a metallic clack as the map redraws.
            level.playSound(null, pos, getSoundType(state).getBreakSound(), SoundSource.BLOCKS,
                    (getSoundType(state).getVolume() + 1.0f) / 2.0f,
                    getSoundType(state).getPitch() * 0.5f);
        }
        return InteractionResult.SUCCESS;
    }
}
