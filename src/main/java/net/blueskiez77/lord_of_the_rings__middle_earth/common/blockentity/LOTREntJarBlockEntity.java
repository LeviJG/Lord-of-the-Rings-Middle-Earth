package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityEntJar. Six units of one liquid, and which liquid it is.
 *
 * <p>{@code drinkMeta} is the Ent-draught subtype the jar holds, or -1 for
 * plain water -- the original packed the draught's item damage value straight
 * in, and the port keeps the same field and sentinel.
 */
public class LOTREntJarBlockEntity extends BlockEntity {

    /** LOTRTileEntityEntJar.MAX_CAPACITY. */
    public static final int MAX_CAPACITY = 6;

    /** Water, as opposed to any Ent-draught subtype. */
    public static final int WATER = -1;

    /**
     * One in this many ticks, a rained-on jar gains a unit of water. At 2500
     * that is a little over two minutes of rain per unit, so filling a jar from
     * empty takes most of a storm.
     */
    private static final int RAIN_FILL_CHANCE = 2500;

    private int drinkMeta = WATER;
    private int drinkAmount;

    public LOTREntJarBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.ENT_JAR, pos, state);
    }

    // ----------------------------------------------------------------- state

    public int getDrinkAmount() {
        return drinkAmount;
    }

    public int getDrinkMeta() {
        return drinkMeta;
    }

    /** True while the jar holds water rather than a draught. */
    public boolean holdsWater() {
        return drinkMeta == WATER;
    }

    public boolean isEmpty() {
        return drinkAmount <= 0;
    }

    /** LOTRTileEntityEntJar.consume: one unit out, and empty reverts to water. */
    public void consume() {
        --drinkAmount;
        if (drinkAmount <= 0) {
            drinkMeta = WATER;
        }
        drinkAmount = Math.max(drinkAmount, 0);
        sync();
    }

    /** LOTRTileEntityEntJar.fillWithWater: only ever adds to a water jar. */
    public void fillWithWater() {
        if (drinkMeta == WATER && drinkAmount < MAX_CAPACITY) {
            ++drinkAmount;
        }
        drinkAmount = Math.min(drinkAmount, MAX_CAPACITY);
        sync();
    }

    /**
     * LOTRTileEntityEntJar.fillFromBowl. Pours one bowl of a draught in: it
     * either starts an empty jar off on that draught or tops up a jar already
     * holding the same one. Mixing two draughts is refused.
     */
    public boolean fillFromBowl(int draughtMeta) {
        if (drinkMeta == WATER && drinkAmount == 0) {
            drinkMeta = draughtMeta;
            ++drinkAmount;
            sync();
            return true;
        }
        if (drinkMeta == draughtMeta && drinkAmount < MAX_CAPACITY) {
            ++drinkAmount;
            sync();
            return true;
        }
        return false;
    }

    /**
     * The brewing step: a jar with water in it turns wholly into the given
     * draught, keeping its level. LOTRBlockEntJar did this inline, gated on the
     * player standing in Fangorn and on the held item matching an
     * LOTREntJarRecipes entry.
     */
    public boolean brew(int draughtMeta) {
        if (drinkMeta != WATER || drinkAmount <= 0) {
            return false;
        }
        drinkMeta = draughtMeta;
        sync();
        return true;
    }

    // ---------------------------------------------------------------- ticking

    /**
     * LOTRTileEntityEntJar.updateEntity. The original tested
     * canLightningStrikeAt at the jar AND one block above it -- the jar itself
     * is not a full block, so the rain check can land on either -- and only
     * rain, never a bucket, could overfill nothing: fillWithWater caps itself.
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                  LOTREntJarBlockEntity jar) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (serverLevel.getRandom().nextInt(RAIN_FILL_CHANCE) != 0) {
            return;
        }
        if (serverLevel.isRainingAt(pos) || serverLevel.isRainingAt(pos.above())) {
            jar.fillWithWater();
        }
    }

    // ------------------------------------------------------------------ sync

    /**
     * markBlockForUpdate + markDirty. The level is what the renderer draws, so
     * every change has to reach the client; without the sendBlockUpdated the
     * jar would keep whatever level it had when the chunk loaded.
     */
    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
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

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        drinkMeta = input.getIntOr("DrinkMeta", WATER);
        drinkAmount = input.getIntOr("DrinkAmount", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("DrinkMeta", drinkMeta);
        output.putInt("DrinkAmount", drinkAmount);
    }
}
