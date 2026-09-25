package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTrollTotemBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTrollTotemBlock.Part;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityTrollTotem. Carried by all three parts because the renderer
 * needs one on each, but only the head does any thinking.
 *
 * <p>The head works out whether the totem is ready to summon and yawns its jaw
 * open when it is. "Ready" means: it is night, the head can see the sky, and a
 * correctly-oriented body and base stand directly beneath it. Whether the
 * player may then USE it is a separate question the block asks -- the original
 * also required a bone in hand and negative Angmar alignment.
 *
 * <p>The readiness flag is computed on the server and pushed to clients, which
 * is what {@code getDescriptionPacket} did with its lone "CanSummon" boolean.
 * The jaw angle itself is never sent: each client animates its own copy towards
 * the flag, so the sync is one boolean rather than sixty steps of an angle.
 */
public class LOTRTrollTotemBlockEntity extends BlockEntity {

    /** Ticks the jaw takes to open fully, and the divisor for its angle. */
    public static final int JAW_OPEN_TICKS = 60;

    /** getJawRotation: the fully open jaw hangs 35 degrees down. */
    public static final float JAW_MAX_DEGREES = -35.0f;

    /**
     * What a completed totem calls up. PLACEHOLDER -- this is
     * LOTREntityMountainTrollChieftain in the original, and a chicken here only
     * because the port has no entities of its own yet. One line to change.
     */
    private static final EntityType<? extends Mob> SUMMONED = EntityTypes.CHICKEN;

    private int jawTick;
    private int prevJawTick;

    /** Server: last computed readiness, to spot changes. Client: the synced one. */
    private boolean canSummon;

    public LOTRTrollTotemBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.TROLL_TOTEM, pos, state);
    }

    // ------------------------------------------------------------- readiness

    /**
     * LOTRTileEntityTrollTotem.canSummon. On the client this is whatever the
     * server last said; on the server it is recomputed from the world.
     */
    public boolean canSummon() {
        if (level == null) {
            return false;
        }
        if (level.isClientSide()) {
            return canSummon;
        }
        return computeCanSummon(level, worldPosition, getBlockState());
    }

    /**
     * Night, open sky, and a matching body and base stacked below. The original
     * compared full metadata values, which folded the part AND the rotation
     * into one test -- hence the facing comparison here.
     */
    public static boolean computeCanSummon(Level level, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof LOTRTrollTotemBlock totem) || totem.part() != Part.HEAD) {
            return false;
        }
        // worldObj.isDaytime() is spelled isBrightOutside() now.
        if (level.isBrightOutside()) {
            return false;
        }
        // Sky is checked ONE ABOVE the head, not at it. canSeeSky is
        // getBrightness(SKY, pos) >= 15, and the head is a full-collision
        // block, so the sky light measured inside it is dimmed and the totem
        // could never read as open to the sky. 1.7.10 had no such problem:
        // canBlockSeeTheSky was a heightmap test, and the block was invisible
        // to light anyway because isOpaqueCube() returned false, which set its
        // lightOpacity to 0. Sampling the air above is the same question
        // without depending on how this block dampens light.
        if (!level.canSeeSky(pos.above())) {
            return false;
        }
        Direction facing = state.getValue(LOTRTrollTotemBlock.FACING);
        return isPart(level, pos.below(), Part.BODY, facing)
                && isPart(level, pos.below(2), Part.BASE, facing);
    }

    private static boolean isPart(Level level, BlockPos pos, Part expected, Direction facing) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof LOTRTrollTotemBlock other
                && other.part() == expected
                && state.getValue(LOTRTrollTotemBlock.FACING) == facing;
    }

    /**
     * LOTRTileEntityTrollTotem.summon. The three blocks are consumed and a
     * Mountain Troll Chieftain rises in their place, at the BASE position --
     * two below the head -- facing a random direction.
     *
     * <p>PLACEHOLDER: it spawns a CHICKEN. {@code
     * LOTREntityMountainTrollChieftain} does not exist because the port has no
     * entities at all yet, so the summon is wired end to end against a stand-in
     * that proves the plumbing -- the blocks vanish, something appears at the
     * right spot with a random yaw. Swap {@link #SUMMONED} for the chieftain's
     * type when entities land (PORT_PLAN Track D); nothing else here needs
     * to change.
     */
    public void summon() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        BlockPos basePos = worldPosition.below(2);

        serverLevel.removeBlock(worldPosition, false);
        serverLevel.removeBlock(worldPosition.below(), false);
        serverLevel.removeBlock(basePos, false);

        Mob summoned = SUMMONED.create(serverLevel, EntitySpawnReason.TRIGGERED);
        if (summoned == null) {
            return;
        }
        summoned.snapTo(basePos.getX() + 0.5, basePos.getY(), basePos.getZ() + 0.5,
                serverLevel.getRandom().nextFloat() * 360.0f, 0.0f);
        // onSpawnWithEgg(null) in the original -- the hook that rolls a mob's
        // random starting equipment and variant.
        summoned.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(basePos),
                EntitySpawnReason.TRIGGERED, null);
        serverLevel.addFreshEntity(summoned);
    }

    // ---------------------------------------------------------------- ticking

    public static void tick(Level level, BlockPos pos, BlockState state,
                            LOTRTrollTotemBlockEntity totem) {
        if (level.isClientSide()) {
            totem.prevJawTick = totem.jawTick;
            if (totem.canSummon && totem.jawTick < JAW_OPEN_TICKS) {
                ++totem.jawTick;
            } else if (!totem.canSummon && totem.jawTick > 0) {
                --totem.jawTick;
            }
            return;
        }

        boolean ready = computeCanSummon(level, pos, state);
        if (ready != totem.canSummon) {
            totem.canSummon = ready;
            // setChanged marks it for saving; sendBlockUpdated is what actually
            // pushes the block entity data out, and is what makes the jaw move
            // on every client rather than only on the one that placed it.
            totem.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
        }
    }

    /** getJawRotation: interpolated, scaled to the open angle. */
    public float getJawRotation(float partialTick) {
        float tick = prevJawTick + (jawTick - prevJawTick) * partialTick;
        return tick / JAW_OPEN_TICKS * JAW_MAX_DEGREES;
    }

    // ------------------------------------------------------------------ sync

    /**
     * The description packet carried nothing but this one boolean, and neither
     * does this. It is deliberately NOT saved: readiness is derived from the
     * time of day and the blocks around it, so a loaded totem recomputes it on
     * its first server tick.
     */
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        canSummon = input.getBooleanOr("CanSummon", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("CanSummon", canSummon);
    }
}
