package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntitySignCarved (and its ithildin subclass): eight lines of up to
 * fifteen characters, saved as Text1 to Text8.
 *
 * <p>Only the player whose chisel carved it may write on it, and only once: a
 * sign loaded from disk has no editing player at all, which is the original's
 * readFromNBT setting editable to false.
 */
public class LOTRCarvedSignBlockEntity extends BlockEntity {
    /** getNumLines. */
    public static final int NUM_LINES = 8;
    /** MAX_LINE_LENGTH. */
    public static final int MAX_LINE_LENGTH = 15;

    private final String[] signText = new String[NUM_LINES];
    private @Nullable UUID editingPlayer;
    /** new LOTRDwarvenGlowLogic().setPlayerRange(8). */
    private final LOTRDwarvenGlowLogic glowLogic = new LOTRDwarvenGlowLogic().setPlayerRange(8);

    public LOTRCarvedSignBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.CARVED_SIGN, pos, state);
        Arrays.fill(signText, "");
    }

    public boolean isIthildin() {
        return getBlockState().is(LOTRUtilityBlocks.CARVED_ITHILDIN_SIGN);
    }

    public String[] copyText() {
        return signText.clone();
    }

    public void setEditingPlayer(@Nullable UUID player) {
        editingPlayer = player;
    }

    public boolean canBeEditedBy(Player player) {
        return editingPlayer != null && editingPlayer.equals(player.getUUID());
    }

    /** LOTRPacketEditSign's handler, once the lines have been checked. */
    public void applyEdit(List<String> lines) {
        for (int i = 0; i < NUM_LINES; ++i) {
            signText[i] = i < lines.size() ? lines.get(i) : "";
        }
        editingPlayer = null;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public float getGlowBrightness(float partialTick) {
        return level == null ? 0.0f : glowLogic.getGlowBrightness(level, worldPosition, partialTick);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, LOTRCarvedSignBlockEntity sign) {
        sign.glowLogic.update(level, pos);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (int i = 0; i < NUM_LINES; ++i) {
            String line = input.getStringOr("Text" + (i + 1), "");
            signText[i] = line.length() > MAX_LINE_LENGTH ? line.substring(0, MAX_LINE_LENGTH) : line;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        for (int i = 0; i < NUM_LINES; ++i) {
            output.putString("Text" + (i + 1), signText[i]);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
