package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCarvedSignBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTROpenSignEditorPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRItemChisel: carves a sign into the side of a block, and opens the
 * lettering screen for whoever held it. A hundred uses.
 *
 * <p>onItemUse, transcribed: only on a side face (never a top or bottom), only
 * into an opaque block of stone, wood or metal, and only where the space in
 * front is free. The original's Material test is the pickaxe and axe tags here
 * -- stone and metal are mined with one, wood with the other.
 */
public class LOTRChiselItem extends Item {
    private final Supplier<Block> signBlock;

    public LOTRChiselItem(Supplier<Block> signBlock, Properties properties) {
        super(properties);
        this.signBlock = signBlock;
    }

    private static boolean isCarvable(BlockState state) {
        return state.isSolidRender()
                && (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction side = context.getClickedFace();
        if (side.getAxis() == Direction.Axis.Y) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        if (!isCarvable(level.getBlockState(context.getClickedPos()))) {
            return InteractionResult.PASS;
        }
        BlockPos target = context.getClickedPos().relative(side);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player == null || !player.mayUseItemAt(target, side, stack) || !level.getBlockState(target).canBeReplaced()) {
            return InteractionResult.PASS;
        }
        BlockState sign = signBlock.get().defaultBlockState().setValue(LOTRCarvedSignBlock.FACING, side);
        if (!sign.canSurvive(level, target)) {
            return InteractionResult.PASS;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            level.setBlock(target, sign, Block.UPDATE_ALL);
            stack.hurtAndBreak(1, player, context.getHand());
            if (level.getBlockEntity(target) instanceof LOTRCarvedSignBlockEntity carved) {
                carved.setEditingPlayer(player.getUUID());
                // Block changes reach the client at the end of the tick, but a
                // payload goes at once -- so without this the client would get
                // "open the editor" before the sign exists there, find no block
                // entity, and open nothing. Vanilla's openTextEdit does the same.
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, target));
                ServerPlayNetworking.send(serverPlayer, new LOTROpenSignEditorPayload(target));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
