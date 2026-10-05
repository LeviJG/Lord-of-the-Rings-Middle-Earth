package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRTableOfCommandBlockEntity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
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
 * <p>NOT ported yet: the conquest map screen it opens and the map drawn on its
 * surface, with conquest (D14).
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
     * LOTRBlockCommandTable.onBlockActivated, with a squadron item in hand:
     * the screen to name the company it speaks to (gui 33), with the table's
     * clack.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSecondaryUseActive() || hand != InteractionHand.MAIN_HAND || !LOTRHiredNetworking.isSquadronItem(stack)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new LOTRHiredPayloads.OpenSquadronItem());
            level.playSound(null, pos, getSoundType(state).getBreakSound(), SoundSource.BLOCKS,
                    (getSoundType(state).getVolume() + 1.0f) / 2.0f, getSoundType(state).getPitch() * 0.5f);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRBlockCommandTable.onBlockActivated: SNEAK-right-click cycles the
     * map's zoom.
     *
     * <p>NOT ported yet: otherwise, with conquest on, the conquest map (gui
     * 60), with conquest (D14).
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
