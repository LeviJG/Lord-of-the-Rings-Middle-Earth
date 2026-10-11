package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowship;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * LOTRCustomWaypoint: a waypoint a player made where they stood, named as they chose, which they may
 * share with their fellowships. A fellow sees it, shared, once they have come within a thousand
 * blocks of it, and may hide it from their map.
 */
public class LOTRCustomWaypoint implements LOTRAbstractWaypoint {

    public String customName;
    public double mapX;
    public double mapY;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public int ID;
    public List<UUID> sharedFellowshipIDs = new ArrayList<>();
    public @Nullable UUID sharingPlayer;
    public @Nullable String sharingPlayerName;
    public boolean sharedUnlocked;
    public boolean sharedHidden;
    /** The player's journeys to it, as the server last sent them to the client. */
    public int useCount;

    public LOTRCustomWaypoint(String name, double i, double j, int posX, int posY, int posZ, int id) {
        this.customName = name;
        this.mapX = i;
        this.mapY = j;
        this.xCoord = posX;
        this.yCoord = posY;
        this.zCoord = posZ;
        this.ID = id;
    }

    /** writeToNBT/readFromNBT: "Name", "XMap", "YMap", "XCoord", "YCoord", "ZCoord", "ID", "SharedFellowships". */
    public static final Codec<LOTRCustomWaypoint> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("Name").forGetter(w -> w.customName),
            Codec.DOUBLE.optionalFieldOf("XMap", 0.0).forGetter(w -> w.mapX),
            Codec.DOUBLE.optionalFieldOf("YMap", 0.0).forGetter(w -> w.mapY),
            Codec.INT.optionalFieldOf("XCoord", 0).forGetter(w -> w.xCoord),
            Codec.INT.optionalFieldOf("YCoord", -1).forGetter(w -> w.yCoord),
            Codec.INT.optionalFieldOf("ZCoord", 0).forGetter(w -> w.zCoord),
            Codec.INT.fieldOf("ID").forGetter(w -> w.ID),
            Codec.STRING.xmap(UUID::fromString, UUID::toString).listOf().optionalFieldOf("SharedFellowships", List.of())
                    .forGetter(w -> List.copyOf(w.sharedFellowshipIDs))
    ).apply(i, (name, x, y, xc, yc, zc, id, shared) -> {
        LOTRCustomWaypoint cwp = new LOTRCustomWaypoint(name, x, y, xc, yc, zc, id);
        cwp.sharedFellowshipIDs = new ArrayList<>(shared);
        return cwp;
    }));

    /** LOTRPacketCreateCWPClient: the waypoint, and for a shared one who shares it and how it stands. */
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRCustomWaypoint> STREAM_CODEC = StreamCodec.of(
            (buf, w) -> {
                buf.writeUtf(w.customName);
                buf.writeDouble(w.mapX);
                buf.writeDouble(w.mapY);
                buf.writeInt(w.xCoord);
                buf.writeInt(w.yCoord);
                buf.writeInt(w.zCoord);
                buf.writeVarInt(w.ID);
                buf.writeCollection(w.sharedFellowshipIDs, (b, id) -> b.writeUUID(id));
                buf.writeOptional(Optional.ofNullable(w.sharingPlayer), (b, id) -> b.writeUUID(id));
                buf.writeUtf(w.sharingPlayerName == null ? "" : w.sharingPlayerName);
                buf.writeBoolean(w.sharedUnlocked);
                buf.writeBoolean(w.sharedHidden);
                buf.writeVarInt(w.useCount);
            },
            buf -> {
                LOTRCustomWaypoint w = new LOTRCustomWaypoint(buf.readUtf(), buf.readDouble(), buf.readDouble(), buf.readInt(),
                        buf.readInt(), buf.readInt(), buf.readVarInt());
                w.sharedFellowshipIDs = buf.<UUID, ArrayList<UUID>>readCollection(ArrayList::new, b -> b.readUUID());
                w.sharingPlayer = buf.<UUID>readOptional(b -> b.readUUID()).orElse(null);
                String name = buf.readUtf();
                w.sharingPlayerName = name.isEmpty() ? null : name;
                w.sharedUnlocked = buf.readBoolean();
                w.sharedHidden = buf.readBoolean();
                w.useCount = buf.readVarInt();
                return w;
            });

    /** validateCustomName: trimmed, and not blank. */
    public static @Nullable String validateCustomName(String name) {
        name = StringUtils.trim(name);
        return StringUtils.isBlank(name) ? null : name;
    }

    /** createForPlayer: the waypoint, where the player stands. */
    public static LOTRCustomWaypoint createAt(Player player, String name, int id) {
        int i = Mth.floor(player.getX());
        int j = Mth.floor(player.getBoundingBox().minY);
        int k = Mth.floor(player.getZ());
        return new LOTRCustomWaypoint(name, LOTRMapCoords.worldToMapX(i), LOTRMapCoords.worldToMapZ(k), i, j, k, id);
    }

    public void addSharedFellowship(UUID fsID) {
        if (!this.sharedFellowshipIDs.contains(fsID)) {
            this.sharedFellowshipIDs.add(fsID);
        }
    }

    public void removeSharedFellowship(UUID fsID) {
        this.sharedFellowshipIDs.remove(fsID);
    }

    public boolean hasSharedFellowship(UUID fsID) {
        return this.sharedFellowshipIDs.contains(fsID);
    }

    /** canUnlockShared: a fellow within a thousand blocks of it. */
    public boolean canUnlockShared(Player player) {
        return this.yCoord >= 0 && player.distanceToSqr(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 1000000.0;
    }

    /** createCopyOfShared: the waypoint as a fellow sees it, shared by its owner. */
    public LOTRCustomWaypoint createCopyOfShared(UUID sharer, String sharerName) {
        LOTRCustomWaypoint copy = new LOTRCustomWaypoint(this.customName, this.mapX, this.mapY, this.xCoord, this.yCoord,
                this.zCoord, this.ID);
        copy.sharingPlayer = sharer;
        copy.sharingPlayerName = sharerName;
        copy.sharedFellowshipIDs = new ArrayList<>(this.sharedFellowshipIDs);
        return copy;
    }

    /** validateFellowshipIDs: only fellowships still standing and with its owner in them. */
    public void validateFellowshipIDs(UUID owner) {
        this.sharedFellowshipIDs.removeIf(fsID -> {
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(fsID);
            return fs == null || !fs.containsPlayer(owner);
        });
    }

    public boolean isShared() {
        return this.sharingPlayer != null;
    }

    public @Nullable UUID getSharingPlayerID() {
        return this.sharingPlayer;
    }

    public @Nullable String getSharingPlayerName() {
        return this.sharingPlayerName;
    }

    public List<UUID> getSharedFellowshipIDs() {
        return this.sharedFellowshipIDs;
    }

    public boolean isSharedUnlocked() {
        return this.sharedUnlocked;
    }

    public boolean isSharedHidden() {
        return this.sharedHidden;
    }

    public void rename(String newName) {
        this.customName = newName;
    }

    @Override
    public String getCodeName() {
        return this.customName;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(isShared() ? "lotr.waypoint.shared" : "lotr.waypoint.custom", this.customName);
    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public WaypointLockState getLockState(Player player) {
        boolean unlocked = hasPlayerUnlocked(player);
        if (isShared()) {
            return unlocked ? WaypointLockState.SHARED_CUSTOM_UNLOCKED : WaypointLockState.SHARED_CUSTOM_LOCKED;
        }
        return unlocked ? WaypointLockState.CUSTOM_UNLOCKED : WaypointLockState.CUSTOM_LOCKED;
    }

    /**
     * getLoreText: for one shared, the fellowships it is shared with (three named, and how many more)
     * and, seen by a fellow, whose it is; else nothing. The names come from the player's own
     * fellowships as they know them.
     */
    public @Nullable Component getLoreText(Player player, Function<UUID, @Nullable String> fellowshipName) {
        boolean ownShared = !isShared() && !this.sharedFellowshipIDs.isEmpty();
        boolean shared = isShared() && this.sharingPlayerName != null;
        if (!ownShared && !shared) {
            return null;
        }
        int numShared = this.sharedFellowshipIDs.size();
        int numShown = 0;
        StringBuilder sharedFsNames = new StringBuilder();
        for (int i = 0; i < 3 && i < this.sharedFellowshipIDs.size(); ++i) {
            String name = fellowshipName.apply(this.sharedFellowshipIDs.get(i));
            if (name != null) {
                sharedFsNames.append("\n").append(name);
                ++numShown;
            }
        }
        Component names = Component.literal(sharedFsNames.toString());
        if (numShared > numShown) {
            names = Component.empty().append(names).append("\n").append(
                    Component.translatable("lotr.waypoint.custom.andMore", numShared - numShown));
        }
        return ownShared ? Component.translatable("lotr.waypoint.custom.info", names)
                : Component.translatable("lotr.waypoint.shared.info", this.sharingPlayerName, names);
    }

    @Override
    public Component getLoreText(Player player) {
        Component lore = getLoreText(player, fsID -> null);
        return lore == null ? Component.empty() : lore;
    }

    @Override
    public double getX() {
        return this.mapX;
    }

    @Override
    public double getY() {
        return this.mapY;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
    }

    /**
     * getYCoord: where it was made, if one can still stand there; else the first safe place below if
     * the ground has gone, or above if the spot is filled; else the top.
     */
    @Override
    public int getYCoord(ServerLevel level, int i, int k) {
        int j = this.yCoord;
        if (j < 0) {
            this.yCoord = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(level, i, k);
        } else if (!isSafeBlock(level, i, j, k)) {
            BlockState below = level.getBlockState(new BlockPos(i, j - 1, k));
            BlockPos at = new BlockPos(i, j, k);
            BlockPos above = at.above();
            boolean belowSafe = blocksMovement(level, below, at.below());
            boolean blockSafe = !level.getBlockState(at).isRedstoneConductor(level, at);
            boolean aboveSafe = !level.getBlockState(above).isRedstoneConductor(level, above);
            boolean foundSafe = false;
            if (!belowSafe) {
                for (int j1 = j - 1; j1 >= level.getMinY() + 1; --j1) {
                    if (isSafeBlock(level, i, j1, k)) {
                        this.yCoord = j1;
                        foundSafe = true;
                        break;
                    }
                }
            }
            if (!foundSafe && (!blockSafe || !aboveSafe)) {
                for (int j1 = aboveSafe ? j + 1 : j + 2; j1 <= level.getMaxY(); ++j1) {
                    if (isSafeBlock(level, i, j1, k)) {
                        this.yCoord = j1;
                        foundSafe = true;
                        break;
                    }
                }
            }
            if (!foundSafe) {
                this.yCoord = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(level, i, k);
            }
        }
        return this.yCoord;
    }

    private static boolean blocksMovement(BlockGetter level, BlockState state, BlockPos pos) {
        return !state.getCollisionShape(level, pos).isEmpty();
    }

    /** isSafeBlock: solid below, room for the head, and no liquid or fire. */
    public boolean isSafeBlock(BlockGetter level, int i, int j, int k) {
        BlockPos at = new BlockPos(i, j, k);
        BlockState below = level.getBlockState(at.below());
        BlockState block = level.getBlockState(at);
        BlockState above = level.getBlockState(at.above());
        if (blocksMovement(level, below, at.below()) && !block.isRedstoneConductor(level, at)
                && !above.isRedstoneConductor(level, at.above())) {
            if (!block.getFluidState().isEmpty() || block.is(BlockTags.FIRE)) {
                return false;
            }
            return above.getFluidState().isEmpty() && !above.is(BlockTags.FIRE);
        }
        return false;
    }

    @Override
    public int getYCoordSaved() {
        return this.yCoord;
    }

    /** One's own always; a shared one once unlocked. */
    @Override
    public boolean hasPlayerUnlocked(Player player) {
        return !isShared() || this.sharedUnlocked;
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
