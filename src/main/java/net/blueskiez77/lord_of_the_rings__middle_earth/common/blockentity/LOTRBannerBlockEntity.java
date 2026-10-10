package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowship;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerWhitelistEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWallBannerBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerDataPayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * A faction banner's block entity: which banner it is (off the block), and
 * all of LOTREntityBanner's territory protection -- whether it protects by
 * faction alignment or by a whitelist of players (and fellowships), whether a
 * structure placed it, a range of its own, whether it protects itself, the
 * alignment it asks, the whitelist with each entry's permissions, and the
 * permissions everyone has.
 *
 * <p>Only a standing banner protects, and only while it stands on a block of
 * bronze, silver or gold (or has a range of its own); a wall banner, as the
 * original's LOTREntityBannerWall, only keeps the data so that the item it
 * drops still carries it. The protection saves as the original's
 * {@code ProtectData} compound, and the item carries it as {@code
 * LOTRBannerData} in its custom data.
 *
 * <p>The loaded banners of each level are kept here, for
 * {@link LOTRBannerProtection#isProtected} to search. Clients are sent the
 * protection settings with the block (for the debug-screen protection box),
 * and the whitelist and their own standing only when they open the banner or
 * it changes ({@link LOTRBannerDataPayload}).
 *
 * <p>A whitelisted fellowship counts only while it stands and the placer is
 * in it; until then its line matches no one and is not shown.
 */
public class LOTRBannerBlockEntity extends BlockEntity {

    public static final float ALIGNMENT_PROTECTION_MIN = 1.0f;
    public static final float ALIGNMENT_PROTECTION_MAX = 10000.0f;
    public static final int WHITELIST_DEFAULT = 16;
    public static final int WHITELIST_MIN = 1;
    public static final int WHITELIST_MAX = 4000;

    /** The key the banner item carries its protection under, as LOTRItemBanner's. */
    public static final String ITEM_DATA_KEY = "LOTRBannerData";

    private static final Map<Level, Set<LOTRBannerBlockEntity>> LOADED = new WeakHashMap<>();

    private @Nullable CompoundTag protectData;
    private boolean wasEverProtecting;
    private boolean playerSpecificProtection;
    private boolean structureProtection;
    private int customRange;
    private boolean selfProtection = true;
    private float alignmentProtection = ALIGNMENT_PROTECTION_MIN;
    private @Nullable LOTRBannerWhitelistEntry[] allowedPlayers = new LOTRBannerWhitelistEntry[WHITELIST_DEFAULT];
    private final Set<LOTRBannerProtection.Permission> defaultPermissions = EnumSet.noneOf(LOTRBannerProtection.Permission.class);
    private boolean clientsidePlayerHasPermission;

    public LOTRBannerBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.BANNER, pos, state);
    }

    // ------------------------------------------------------------ the loaded banners

    /** Every banner block entity loaded in this level. */
    public static Collection<LOTRBannerBlockEntity> loadedIn(Level level) {
        synchronized (LOADED) {
            Set<LOTRBannerBlockEntity> set = LOADED.get(level);
            return set == null ? List.of() : List.copyOf(set);
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        track();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        track();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null) {
            synchronized (LOADED) {
                Set<LOTRBannerBlockEntity> set = LOADED.get(this.level);
                if (set != null) {
                    set.remove(this);
                }
            }
        }
    }

    private void track() {
        if (this.level != null && !isRemoved()) {
            synchronized (LOADED) {
                LOADED.computeIfAbsent(this.level, l -> Collections.newSetFromMap(new WeakHashMap<>())).add(this);
            }
        }
    }

    // ------------------------------------------------------------ the banner

    /** Which banner to draw and whose it is, read back off the block. */
    public LOTRBannerType getBannerType() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof LOTRBannerBlock standing) {
            return standing.getBannerType();
        }
        if (state.getBlock() instanceof LOTRWallBannerBlock wall) {
            return wall.getBannerType();
        }
        return LOTRBannerType.GONDOR;
    }

    private boolean isStanding() {
        return getBlockState().getBlock() instanceof LOTRBannerBlock;
    }

    /** getProtectionRange: its own range, or else that of the block it stands on; 0 on a wall. */
    public int getProtectionRange() {
        if (!isStanding() || this.level == null) {
            return 0;
        }
        if (!this.structureProtection && !LOTRConfig.allowBannerProtection) {
            return 0;
        }
        if (this.customRange > 0) {
            return this.customRange;
        }
        return LOTRBannerProtection.getProtectionRange(this.level.getBlockState(this.worldPosition.below()).getBlock());
    }

    public boolean isProtectingTerritory() {
        return getProtectionRange() > 0;
    }

    public AABB createProtectionCube() {
        return new AABB(this.worldPosition).inflate(getProtectionRange());
    }

    /** onUpdate's bookkeeping, called by the block's ticker on the server. */
    public static void serverTick(Level level, BlockPos pos, BlockState state, LOTRBannerBlockEntity banner) {
        if (banner.isProtectingTerritory() && !banner.wasEverProtecting) {
            banner.wasEverProtecting = true;
            banner.setChanged();
        }
        if (banner.getPlacingPlayer() == null && banner.playerSpecificProtection) {
            banner.playerSpecificProtection = false;
            banner.setChanged();
        }
    }

    // ------------------------------------------------------------ who may do what

    public boolean canPlayerEditBanner(Player player) {
        LOTRBannerWhitelistEntry owner = getPlacingPlayer();
        if (owner != null && owner.playerID != null && player.getUUID().equals(owner.playerID)) {
            return true;
        }
        return !this.structureProtection && player instanceof ServerPlayer serverPlayer
                && serverPlayer.level().getServer().getPlayerList().isOp(serverPlayer.nameAndId())
                && player.isCreative();
    }

    public boolean isPlayerAllowedByFaction(Player player, LOTRBannerProtection.Permission perm) {
        if (!this.playerSpecificProtection) {
            if (hasDefaultPermission(perm)) {
                return true;
            }
            return LOTRPlayerAlignments.getAlignment(player, getBannerType().faction) >= this.alignmentProtection;
        }
        return false;
    }

    public boolean isPlayerWhitelisted(Player player, LOTRBannerProtection.Permission perm) {
        if (this.playerSpecificProtection) {
            if (hasDefaultPermission(perm)) {
                return true;
            }
            UUID playerID = player.getUUID();
            for (LOTRBannerWhitelistEntry entry : this.allowedPlayers) {
                if (entry == null) {
                    continue;
                }
                boolean playerMatch;
                if (entry.isFellowship()) {
                    LOTRFellowship fs = whitelistedFellowship(entry);
                    playerMatch = fs != null && fs.containsPlayer(playerID);
                } else {
                    playerMatch = playerID.equals(entry.playerID);
                }
                if (playerMatch && entry.allowsPermission(perm)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A whitelisted fellowship, while it stands and the banner's placer is still in it (isValidFellowship);
     * otherwise none, and the line matches no one. Only the server knows fellowships' members.
     */
    private @Nullable LOTRFellowship whitelistedFellowship(LOTRBannerWhitelistEntry entry) {
        if (entry.fellowshipID == null) {
            return null;
        }
        LOTRFellowship fs = LOTRFellowships.getActiveFellowship(entry.fellowshipID);
        return isValidFellowship(fs) ? fs : null;
    }

    /** isValidFellowship: one not disbanded, with the banner's placer in it. */
    public boolean isValidFellowship(@Nullable LOTRFellowship fs) {
        LOTRBannerWhitelistEntry owner = getPlacingPlayer();
        return fs != null && !fs.isDisbanded() && owner != null && owner.playerID != null && fs.containsPlayer(owner.playerID);
    }

    /** getPlacersFellowshipByName: the placer's fellowship of that name. */
    public @Nullable LOTRFellowship getPlacersFellowshipByName(String fsName) {
        LOTRBannerWhitelistEntry owner = getPlacingPlayer();
        return owner == null || owner.playerID == null ? null : LOTRFellowships.getFellowshipByName(owner.playerID, fsName);
    }

    /** whitelistFellowship: a fellowship of the placer's, by its id. */
    public void whitelistFellowship(int index, LOTRFellowship fs, Iterable<LOTRBannerProtection.Permission> perms) {
        if (isValidFellowship(fs)) {
            whitelistPlayer(index, LOTRBannerWhitelistEntry.fellowship(fs.getFellowshipID(), fs.getName()), perms);
        }
    }

    /** isPlayerPermittedInSurvival: whether the banner would let this player do anything, creative or not. */
    public boolean isPlayerPermittedInSurvival(Player player) {
        return new LOTRBannerProtection.FilterForPlayer(player, LOTRBannerProtection.Permission.FULL)
                .ignoreCreativeMode().protects(this) == LOTRBannerProtection.ProtectType.NONE;
    }

    public boolean clientsidePlayerHasPermissionInSurvival() {
        return this.clientsidePlayerHasPermission;
    }

    public void setClientsidePlayerHasPermissionInSurvival(boolean flag) {
        this.clientsidePlayerHasPermission = flag;
    }

    // ------------------------------------------------------------ settings

    public boolean isPlayerSpecificProtection() {
        return this.playerSpecificProtection;
    }

    public void setPlayerSpecificProtection(boolean flag) {
        this.playerSpecificProtection = flag;
        changed();
    }

    public boolean isStructureProtection() {
        return this.structureProtection;
    }

    public void setStructureProtection(boolean flag) {
        this.structureProtection = flag;
        changed();
    }

    /** isSelfProtection: as set, unless the config forbids self-protecting banners. */
    public boolean isSelfProtection() {
        return LOTRConfig.allowSelfProtectingBanners && this.selfProtection;
    }

    /** The setting itself, as attackEntityFrom read it -- whatever the config. */
    public boolean selfProtectionSetting() {
        return this.selfProtection;
    }

    public void setSelfProtection(boolean flag) {
        this.selfProtection = flag;
        changed();
    }

    public int getCustomRange() {
        return this.customRange;
    }

    public void setCustomRange(int range) {
        this.customRange = Mth.clamp(range, 0, LOTRBannerProtection.MAX_RANGE);
        changed();
    }

    public float getAlignmentProtection() {
        return this.alignmentProtection;
    }

    public void setAlignmentProtection(float f) {
        this.alignmentProtection = Mth.clamp(f, ALIGNMENT_PROTECTION_MIN, ALIGNMENT_PROTECTION_MAX);
        changed();
    }

    public boolean hasDefaultPermission(LOTRBannerProtection.Permission p) {
        return this.defaultPermissions.contains(p);
    }

    public int getDefaultPermBitFlags() {
        return LOTRBannerWhitelistEntry.encodePermBitFlags(this.defaultPermissions);
    }

    public void addDefaultPermission(LOTRBannerProtection.Permission p) {
        if (p == LOTRBannerProtection.Permission.FULL) {
            return;
        }
        this.defaultPermissions.add(p);
        changed();
    }

    public void removeDefaultPermission(LOTRBannerProtection.Permission p) {
        this.defaultPermissions.remove(p);
        changed();
    }

    public void setDefaultPermissions(Iterable<LOTRBannerProtection.Permission> perms) {
        this.defaultPermissions.clear();
        for (LOTRBannerProtection.Permission p : perms) {
            if (p != LOTRBannerProtection.Permission.FULL) {
                this.defaultPermissions.add(p);
            }
        }
        changed();
    }

    // ------------------------------------------------------------ the whitelist

    public @Nullable LOTRBannerWhitelistEntry getPlacingPlayer() {
        return this.allowedPlayers[0];
    }

    /** The owner's name, looked up from the server's profile cache if the banner has only the id. */
    public @Nullable String getPlacingPlayerName() {
        LOTRBannerWhitelistEntry owner = getPlacingPlayer();
        if (owner == null) {
            return null;
        }
        if (owner.playerName != null && !owner.playerName.isBlank()) {
            return owner.playerName;
        }
        if (owner.playerID != null && this.level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().services().nameToIdCache().get(owner.playerID).map(NameAndId::name).orElse(null);
        }
        return null;
    }

    public void setPlacingPlayer(Player player) {
        whitelistPlayer(0, LOTRBannerWhitelistEntry.player(player.getUUID(), player.getGameProfile().name()));
    }

    public @Nullable LOTRBannerWhitelistEntry getWhitelistEntry(int index) {
        return this.allowedPlayers[index];
    }

    public int getWhitelistLength() {
        return this.allowedPlayers.length;
    }

    /** whitelistPlayer(index, profile): the entry, with full permissions. */
    public void whitelistPlayer(int index, @Nullable LOTRBannerWhitelistEntry entry) {
        whitelistPlayer(index, entry, List.of(LOTRBannerProtection.Permission.FULL));
    }

    public void whitelistPlayer(int index, @Nullable LOTRBannerWhitelistEntry entry, Iterable<LOTRBannerProtection.Permission> perms) {
        if (index < 0 || index >= this.allowedPlayers.length) {
            return;
        }
        if (entry != null) {
            entry.setPermissions(perms);
        }
        this.allowedPlayers[index] = entry;
        changed();
    }

    public void resizeWhitelist(int length) {
        length = Mth.clamp(length, WHITELIST_MIN, WHITELIST_MAX);
        if (length == this.allowedPlayers.length) {
            return;
        }
        LOTRBannerWhitelistEntry[] resized = new LOTRBannerWhitelistEntry[length];
        System.arraycopy(this.allowedPlayers, 0, resized, 0, Math.min(length, this.allowedPlayers.length));
        this.allowedPlayers = resized;
        changed();
    }

    /** A setting changed: saved, and on the server every player watching told (updateForAllWatchers). */
    private void changed() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        setChanged();
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        for (ServerPlayer player : PlayerLookup.tracking(this)) {
            sendBannerData(player, false, false);
        }
    }

    /**
     * sendBannerData: the banner's settings to one player -- with the whole
     * whitelist when it is for the owner's screen, else only the owner's line
     * -- and whether that player may do as they like here.
     */
    public void sendBannerData(ServerPlayer player, boolean sendWhitelist, boolean openGui) {
        int maxSendIndex = sendWhitelist ? this.allowedPlayers.length : 1;
        List<LOTRBannerDataPayload.Slot> slots = new ArrayList<>();
        for (int index = 0; index < maxSendIndex; ++index) {
            LOTRBannerWhitelistEntry entry = this.allowedPlayers[index];
            if (entry == null) {
                continue;
            }
            if (entry.isFellowship()) {
                // A fellowship is sent only while it is valid.
                LOTRFellowship fs = whitelistedFellowship(entry);
                if (fs != null) {
                    slots.add(new LOTRBannerDataPayload.Slot(index, LOTRBannerWhitelistEntry.addFellowshipCode(fs.getName()),
                            entry.encodePermBitFlags()));
                }
                continue;
            }
            String username = index == 0 ? getPlacingPlayerName() : entryName(entry, player.level().getServer());
            if (username == null || username.isBlank()) {
                if (index == 0) {
                    LOTRMod.LOGGER.info("LOTR: Banner needs to be replaced at {} {} {} {}", this.worldPosition.getX(),
                            this.worldPosition.getY(), this.worldPosition.getZ(), player.level().dimension().identifier());
                }
                continue;
            }
            slots.add(new LOTRBannerDataPayload.Slot(index, username, entry.encodePermBitFlags()));
        }
        ServerPlayNetworking.send(player, new LOTRBannerDataPayload(this.worldPosition, openGui,
                this.playerSpecificProtection, this.selfProtection, this.structureProtection, this.customRange,
                this.alignmentProtection, this.allowedPlayers.length, slots, getDefaultPermBitFlags(),
                isPlayerPermittedInSurvival(player)));
    }

    private static @Nullable String entryName(LOTRBannerWhitelistEntry entry, MinecraftServer server) {
        if (entry.playerName != null && !entry.playerName.isBlank()) {
            return entry.playerName;
        }
        if (entry.playerID == null) {
            return null;
        }
        return server.services().nameToIdCache().get(entry.playerID).map(NameAndId::name).orElse(null);
    }

    /** LOTRPacketBannerData's handler: the server's word on this banner, on the client. */
    public void applyClientData(LOTRBannerDataPayload data) {
        this.playerSpecificProtection = data.playerSpecificProtection();
        this.selfProtection = data.selfProtection();
        this.structureProtection = data.structureProtection();
        this.customRange = data.customRange();
        this.alignmentProtection = data.alignmentProtection();
        resizeWhitelist(data.whitelistLength());
        int sentUpTo = data.slots().stream().mapToInt(LOTRBannerDataPayload.Slot::index).max().orElse(0);
        for (int index = 0; index <= Math.min(sentUpTo, this.allowedPlayers.length - 1); ++index) {
            this.allowedPlayers[index] = null;
        }
        for (LOTRBannerDataPayload.Slot slot : data.slots()) {
            if (slot.index() < 0 || slot.index() >= this.allowedPlayers.length) {
                continue;
            }
            LOTRBannerWhitelistEntry entry = LOTRBannerWhitelistEntry.hasFellowshipCode(slot.name())
                    ? LOTRBannerWhitelistEntry.fellowship(null, LOTRBannerWhitelistEntry.stripFellowshipCode(slot.name()))
                    : LOTRBannerWhitelistEntry.player(null, slot.name());
            entry.setPermissions(LOTRBannerWhitelistEntry.decodePermBitFlags(slot.perms()));
            this.allowedPlayers[slot.index()] = entry;
        }
        this.defaultPermissions.clear();
        for (LOTRBannerProtection.Permission p : LOTRBannerWhitelistEntry.decodePermBitFlags(data.defaultPerms())) {
            if (p != LOTRBannerProtection.Permission.FULL) {
                this.defaultPermissions.add(p);
            }
        }
        this.clientsidePlayerHasPermission = data.thisPlayerHasPermission();
    }

    // ------------------------------------------------------------ saving

    /**
     * readProtectionFromNBT: the protection from a saved or carried
     * compound, keys as the original's; {@code WhitelistLength} absent means
     * the default sixteen, and entries saved before permissions existed get
     * full permissions.
     */
    public void readProtectionFromNBT(CompoundTag nbt) {
        this.protectData = nbt.copy();
        this.playerSpecificProtection = nbt.getBooleanOr("PlayerProtection", false);
        this.structureProtection = nbt.getBooleanOr("StructureProtection", false);
        this.customRange = Mth.clamp(nbt.getShortOr("CustomRange", (short) 0), 0, LOTRBannerProtection.MAX_RANGE);
        this.selfProtection = nbt.getBooleanOr("SelfProtection", true);
        if (nbt.contains("AlignmentProtection")) {
            this.alignmentProtection = Mth.clamp(nbt.getIntOr("AlignmentProtection", 0), ALIGNMENT_PROTECTION_MIN, ALIGNMENT_PROTECTION_MAX);
        } else {
            this.alignmentProtection = Mth.clamp(nbt.getFloatOr("AlignProtectF", 0.0f), ALIGNMENT_PROTECTION_MIN, ALIGNMENT_PROTECTION_MAX);
        }
        int wlength = nbt.getIntOr("WhitelistLength", WHITELIST_DEFAULT);
        this.allowedPlayers = new LOTRBannerWhitelistEntry[Math.max(wlength, 0)];
        ListTag allowedTags = nbt.getListOrEmpty("AllowedPlayers");
        for (int i = 0; i < allowedTags.size(); ++i) {
            CompoundTag playerData = allowedTags.getCompoundOrEmpty(i);
            int index = playerData.getIntOr("Index", -1);
            if (index < 0 || index >= wlength) {
                continue;
            }
            LOTRBannerWhitelistEntry entry = null;
            if (playerData.getBooleanOr("Fellowship", false)) {
                Optional<UUID> fsID = playerData.getString("FellowshipID").flatMap(LOTRBannerBlockEntity::parseUUID);
                if (fsID.isPresent()) {
                    entry = LOTRBannerWhitelistEntry.fellowship(fsID.get(), null);
                }
            } else if (playerData.contains("Profile")) {
                CompoundTag profile = playerData.getCompoundOrEmpty("Profile");
                UUID id = profile.getString("Id").flatMap(LOTRBannerBlockEntity::parseUUID).orElse(null);
                String name = profile.getString("Name").orElse(null);
                if (id != null || name != null) {
                    entry = LOTRBannerWhitelistEntry.player(id, name);
                }
            }
            if (entry == null) {
                continue;
            }
            // By its saved slot. The original wrote allowedPlayers[i], by list
            // position, so a gap in the whitelist shifted later names up on
            // every reload; fixed here (user).
            this.allowedPlayers[index] = entry;
            if (playerData.getBooleanOr("PermsSaved", false)) {
                for (Tag t : playerData.getListOrEmpty("Perms")) {
                    t.asString().map(LOTRBannerProtection.Permission::forName).ifPresent(entry::addPermission);
                }
            } else {
                entry.setFullPerms();
            }
        }
        this.defaultPermissions.clear();
        for (Tag t : nbt.getListOrEmpty("DefaultPerms")) {
            t.asString().map(LOTRBannerProtection.Permission::forName).ifPresent(this.defaultPermissions::add);
        }
    }

    private static Optional<UUID> parseUUID(String s) {
        try {
            return Optional.of(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public void writeProtectionToNBT(CompoundTag nbt) {
        nbt.putBoolean("PlayerProtection", this.playerSpecificProtection);
        nbt.putBoolean("StructureProtection", this.structureProtection);
        nbt.putShort("CustomRange", (short) this.customRange);
        nbt.putBoolean("SelfProtection", this.selfProtection);
        nbt.putFloat("AlignProtectF", this.alignmentProtection);
        nbt.putInt("WhitelistLength", this.allowedPlayers.length);
        ListTag allowedTags = new ListTag();
        for (int i = 0; i < this.allowedPlayers.length; ++i) {
            LOTRBannerWhitelistEntry entry = this.allowedPlayers[i];
            if (entry == null) {
                continue;
            }
            CompoundTag playerData = new CompoundTag();
            playerData.putInt("Index", i);
            playerData.putBoolean("Fellowship", entry.isFellowship());
            if (entry.isFellowship()) {
                if (entry.fellowshipID != null) {
                    playerData.putString("FellowshipID", entry.fellowshipID.toString());
                }
            } else {
                CompoundTag profile = new CompoundTag();
                if (entry.playerName != null) {
                    profile.putString("Name", entry.playerName);
                }
                if (entry.playerID != null) {
                    profile.putString("Id", entry.playerID.toString());
                }
                playerData.put("Profile", profile);
            }
            ListTag permTags = new ListTag();
            for (LOTRBannerProtection.Permission p : entry.listPermissions()) {
                permTags.add(StringTag.valueOf(p.codeName));
            }
            playerData.put("Perms", permTags);
            playerData.putBoolean("PermsSaved", true);
            allowedTags.add(playerData);
        }
        nbt.put("AllowedPlayers", allowedTags);
        if (!this.defaultPermissions.isEmpty()) {
            ListTag permTags = new ListTag();
            for (LOTRBannerProtection.Permission p : this.defaultPermissions) {
                permTags.add(StringTag.valueOf(p.codeName));
            }
            nbt.put("DefaultPerms", permTags);
        }
    }

    /** The protection as the banner item should carry it, or null: none for a structure's banner. */
    public @Nullable CompoundTag itemProtectionData() {
        if (this.wasEverProtecting && this.protectData == null) {
            this.protectData = new CompoundTag();
        }
        if (this.protectData == null) {
            return null;
        }
        writeProtectionToNBT(this.protectData);
        return this.structureProtection ? null : this.protectData.copy();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("ProtectData", CompoundTag.CODEC).ifPresent(this::readProtectionFromNBT);
        // The client's copy, from getUpdateTag: the settings, not the whitelist.
        input.read("ClientProtect", CompoundTag.CODEC).ifPresent(tag -> {
            this.playerSpecificProtection = tag.getBooleanOr("PlayerProtection", false);
            this.structureProtection = tag.getBooleanOr("StructureProtection", false);
            this.customRange = tag.getShortOr("CustomRange", (short) 0);
            this.selfProtection = tag.getBooleanOr("SelfProtection", true);
            this.alignmentProtection = tag.getFloatOr("AlignProtectF", ALIGNMENT_PROTECTION_MIN);
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (this.protectData == null && this.wasEverProtecting) {
            this.protectData = new CompoundTag();
        }
        if (this.protectData != null) {
            writeProtectionToNBT(this.protectData);
            output.store("ProtectData", CompoundTag.CODEC, this.protectData);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag settings = new CompoundTag();
        settings.putBoolean("PlayerProtection", this.playerSpecificProtection);
        settings.putBoolean("StructureProtection", this.structureProtection);
        settings.putShort("CustomRange", (short) this.customRange);
        settings.putBoolean("SelfProtection", this.selfProtection);
        settings.putFloat("AlignProtectF", this.alignmentProtection);
        CompoundTag tag = new CompoundTag();
        tag.put("ClientProtect", settings);
        return tag;
    }

    // ------------------------------------------------------------ the item

    /** An item placed carrying protection brings it (LOTRItemBanner.onItemUse's readProtectionFromNBT). */
    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        CustomData data = components.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            data.copyTag().getCompound(ITEM_DATA_KEY).ifPresent(this::readProtectionFromNBT);
        }
    }

    /** getBannerItem: the dropped (or picked) item carries the protection, unless a structure's. */
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        CompoundTag data = itemProtectionData();
        if (data != null) {
            CompoundTag custom = new CompoundTag();
            custom.put(ITEM_DATA_KEY, data);
            components.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
        }
    }
}
