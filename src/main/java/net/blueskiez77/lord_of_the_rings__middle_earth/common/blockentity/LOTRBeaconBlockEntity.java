package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBeaconBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

// Beacon of Gondor. Light one and, a hundred ticks later, it lights every
// beacon within eighty-eight blocks, which lights their neighbours in turn --
// the chain from Amon Din to Rohan. Quenching propagates the same way.
//
// Ported from LOTRTileEntityBeacon, with two deliberate omissions:
//
//  * NO FELLOWSHIP. The original could be assigned to a fellowship and would
//    chat "The beacon of X is lit!" to its members. The port has no fellowship
//    system at all, so sendFellowshipMessage and the beaconFellowshipID field
//    are dropped rather than stubbed. The lang keys container.lotr.beacon.lit /
//    .unlit stay unused until fellowships land.
//  * NO GUI. LOTRGuiBeacon was purely the fellowship assignment and the beacon
//    name -- both fellowship features -- so GUI 50 has nothing left to show.
//    beaconName is still saved and loaded so that naming can be added later
//    without a data migration.
//
// The lit state lives on the BLOCK (LOTRBeaconBlock.LIT) rather than only
// in the block entity, because light emission is computed from the blockstate
// in modern versions and cannot consult a block entity. litCounter/unlitCounter
// stay here.
public class LOTRBeaconBlockEntity extends BlockEntity {

    /** Ticks from ignition to full blaze, and from quenching to fully dark. */
    public static final int WARMUP = 100;

    /** LOTRTileEntityBeacon: range 88, compared as distance squared 6400. */
    public static final int SPREAD_RANGE = 88;
    private static final double SPREAD_RANGE_SQR = 6400.0;

    /** Beacons only look for neighbours every ten ticks. */
    private static final int SPREAD_INTERVAL = 10;

    // The original walked the chunk map looking for beacon tile entities. That
    // reached into chunkTileEntityMap, which no longer exists; instead every
    // loaded beacon registers itself here and drops out when it unloads, which
    // gives the same "loaded chunks only" behaviour the original had.
    private static final Map<ResourceKey<Level>, Set<BlockPos>> LOADED =
            new ConcurrentHashMap<>();

    private int ticksExisted;
    private int litCounter;
    private int unlitCounter;
    private long stateChangeTime = -1L;
    private String beaconName;
    /**
     * The fellowship NAME as typed. The original stored a resolved UUID
     * (beaconFellowshipID); with no fellowship system to resolve against, the
     * raw text is kept so nothing the player types is lost.
     */
    private String fellowshipName;

    /** editingPlayers: who has the naming dialog open. Not saved, as in the original. */
    private final java.util.Set<java.util.UUID> editingPlayers = new java.util.HashSet<>();

    public LOTRBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.BEACON, pos, state);
    }

    // ------------------------------------------------------------ lit state

    public boolean isLit() {
        return getBlockState().getValue(LOTRBeaconBlock.LIT);
    }

    public boolean isFullyLit() {
        return getBlockState().getValue(LOTRBeaconBlock.FULLY_LIT);
    }

    /** The block's own name, for the naming screen's title. */
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    public String getBeaconName() {
        return beaconName;
    }

    public String getFellowshipName() {
        return fellowshipName;
    }

    public void addEditingPlayer(net.minecraft.world.entity.player.Player player) {
        this.editingPlayers.add(player.getUUID());
    }

    /** isPlayerEditing: on the list, and (the tick's pruning) still alive. */
    public boolean isPlayerEditing(net.minecraft.world.entity.player.Player player) {
        return player.isAlive() && this.editingPlayers.contains(player.getUUID());
    }

    public void releaseEditingPlayer(net.minecraft.world.entity.player.Player player) {
        this.editingPlayers.remove(player.getUUID());
    }

    public void setFellowshipName(String name) {
        this.fellowshipName = name;
        syncToClients();
    }

    public void setBeaconName(String name) {
        this.beaconName = name;
        syncToClients();
    }

    /**
     * setBeaconName/setFellowship: markDirty and markBlockForUpdate, so the
     * names reach the client's copy of the block entity.
     */
    private void syncToClients() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    // The names are block entity data, not blockstate, so they need an
    // explicit sync. LIT and FULLY_LIT ride along on the blockstate already.
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    /**
     * LOTRTileEntityBeacon.setLit. Flips the blockstate, which is what drives
     * the light level, and resets the counter for the direction of travel.
     */
    public void setLit(Level level, boolean lit) {
        if (isLit() == lit) {
            return;
        }
        if (lit) {
            unlitCounter = 0;
        } else {
            litCounter = 0;
        }
        stateChangeTime = level.getGameTime();
        // Quenching drops FULLY_LIT immediately -- isFullyLit() was
        // "isLit && litCounter == 100", so it goes false the moment isLit does.
        // Lighting sets it only after the warmup, down in serverTick.
        level.setBlock(worldPosition, getBlockState()
                .setValue(LOTRBeaconBlock.LIT, lit)
                .setValue(LOTRBeaconBlock.FULLY_LIT, false), 3);
        // updateLight() in the original: recalc light, markBlockForUpdate,
        // markDirty. setBlock covers the first two; setChanged is the third,
        // and without it litCounter and stateChangeTime could be lost on
        // chunk unload.
        setChanged();
    }

    // ------------------------------------------------------------ client tick

    /**
     * The reason a campfire's smoke is a steady column and animateTick's is
     * not: animateTick only fires for randomly chosen blocks near the camera,
     * so it is bursty by nature. CampfireBlockEntity spawns its smoke from a
     * client-side ticker instead, which runs every tick without fail. This is
     * that ticker.
     *
     * addAlwaysVisibleParticle rather than addParticle, again as the campfire
     * does -- ordinary particles are culled past 32 blocks, and a beacon that
     * stops smoking when you back away from it defeats the point.
     */
    public static void clientTick(Level level, BlockPos pos, BlockState state, LOTRBeaconBlockEntity beacon) {
        if (!state.getValue(LOTRBeaconBlock.FULLY_LIT)) {
            return;
        }
        RandomSource random = level.getRandom();

        // CampfireBlockEntity.particleTick exactly: an 11% chance per tick of
        // two or three puffs. That is all a campfire does -- the steadiness
        // comes from the particle's long life and slow rise, not from volume.
        if (random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                // CampfireBlock.makeParticles, cosy variant.
                level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                        pos.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        pos.getY() + 0.85,
                        pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }
    }

    // ---------------------------------------------------------------- ticking

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, LOTRBeaconBlockEntity beacon) {
        ++beacon.ticksExisted;
        loadedIn(level).add(pos.immutable());

        boolean lit = state.getValue(LOTRBeaconBlock.LIT);

        if (lit && beacon.litCounter < WARMUP) {
            ++beacon.litCounter;
            if (beacon.litCounter == WARMUP) {
                // Full blaze: this is the flag the light level reads.
                level.setBlock(pos, state.setValue(LOTRBeaconBlock.FULLY_LIT, true), 3);
                beacon.setChanged();
            }
        }
        if (!lit && beacon.unlitCounter < WARMUP) {
            ++beacon.unlitCounter;
            if (beacon.unlitCounter == WARMUP) {
                // updateLight() again in the original. FULLY_LIT already went
                // false the moment it was quenched, so there is no state to
                // push -- only the save to force.
                beacon.setChanged();
            }
        }

        if (beacon.ticksExisted % SPREAD_INTERVAL != 0) {
            return;
        }

        boolean spreadLit = lit && beacon.litCounter >= WARMUP;
        boolean spreadUnlit = !lit && beacon.unlitCounter >= WARMUP;
        if (!spreadLit && !spreadUnlit) {
            return;
        }

        Set<BlockPos> nearby = loadedIn(level);
        for (BlockPos otherPos : nearby) {
            if (otherPos.equals(pos) || otherPos.distSqr(pos) > SPREAD_RANGE_SQR) {
                continue;
            }
            if (!(level.getBlockEntity(otherPos) instanceof LOTRBeaconBlockEntity other)) {
                continue;
            }
            // Only push onto beacons whose state is older than ours, which is
            // what stops two lit beacons relighting each other forever.
            if (other.stateChangeTime >= beacon.stateChangeTime) {
                continue;
            }
            if (spreadLit && !other.isLit()) {
                other.setLit(level, true);
            } else if (spreadUnlit && other.isLit()) {
                other.setLit(level, false);
            }
        }
    }

    private static Set<BlockPos> loadedIn(Level level) {
        return LOADED.computeIfAbsent(level.dimension(), key -> ConcurrentHashMap.newKeySet());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null) {
            Set<BlockPos> set = LOADED.get(level.dimension());
            if (set != null) {
                set.remove(worldPosition);
            }
        }
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        litCounter = input.getShortOr("LitCounter", (short) 0);
        unlitCounter = input.getShortOr("UnlitCounter", (short) 0);
        stateChangeTime = input.getLongOr("StateChangeTime", -1L);
        beaconName = input.getStringOr("BeaconName", null);
        fellowshipName = input.getStringOr("BeaconFellowshipName", null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("LitCounter", (short) litCounter);
        output.putShort("UnlitCounter", (short) unlitCounter);
        output.putLong("StateChangeTime", stateChangeTime);
        if (beaconName != null) {
            output.putString("BeaconName", beaconName);
        }
        if (fellowshipName != null) {
            output.putString("BeaconFellowshipName", fellowshipName);
        }
    }

    /** Kept so the spread set does not leak across a server restart in tests. */
    public static void clearLoaded() {
        LOADED.clear();
    }
}