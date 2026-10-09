package net.blueskiez77.lord_of_the_rings__middle_earth.common.banner;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.server.players.NameAndId;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBannerWhitelistEntry: one line of a banner's whitelist -- a player, or
 * a fellowship (LOTRFellowshipProfile, by its id) -- with the permissions it
 * grants.
 *
 * <p>A player's name may be known without the id (on the client, and in the
 * owner's half-typed edits) or the id without the name (an old save); the
 * server fills in either from its profile cache when it needs to. A
 * fellowship's members are known only to the server.
 */
public final class LOTRBannerWhitelistEntry {

    /** The fellowship code a whitelist name starts with, as LOTRFellowshipProfile.fellowshipPrefix. */
    public static final String FELLOWSHIP_PREFIX = "f/";

    public final @Nullable UUID playerID;
    public final @Nullable String playerName;
    public final @Nullable UUID fellowshipID;
    public final @Nullable String fellowshipName;
    private final Set<LOTRBannerProtection.Permission> perms = EnumSet.noneOf(LOTRBannerProtection.Permission.class);

    private LOTRBannerWhitelistEntry(@Nullable UUID playerID, @Nullable String playerName,
                                     @Nullable UUID fellowshipID, @Nullable String fellowshipName) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.fellowshipID = fellowshipID;
        this.fellowshipName = fellowshipName;
    }

    public static LOTRBannerWhitelistEntry player(NameAndId profile) {
        return new LOTRBannerWhitelistEntry(profile.id(), profile.name(), null, null);
    }

    public static LOTRBannerWhitelistEntry player(@Nullable UUID id, @Nullable String name) {
        return new LOTRBannerWhitelistEntry(id, name, null, null);
    }

    public static LOTRBannerWhitelistEntry fellowship(@Nullable UUID id, @Nullable String name) {
        return new LOTRBannerWhitelistEntry(null, null, id, name);
    }

    public boolean isFellowship() {
        return this.fellowshipID != null || this.fellowshipName != null;
    }

    /** The name the banner screen shows: the player's, or the fellowship's with its code. */
    public @Nullable String displayName() {
        if (isFellowship()) {
            return this.fellowshipName == null ? null : addFellowshipCode(this.fellowshipName);
        }
        return this.playerName;
    }

    public static String addFellowshipCode(String s) {
        return FELLOWSHIP_PREFIX + s;
    }

    public static boolean hasFellowshipCode(String s) {
        return s.toLowerCase(java.util.Locale.ROOT).startsWith(FELLOWSHIP_PREFIX);
    }

    public static String stripFellowshipCode(String s) {
        return s.substring(FELLOWSHIP_PREFIX.length());
    }

    // ------------------------------------------------------------ permissions

    public static List<LOTRBannerProtection.Permission> decodePermBitFlags(int i) {
        List<LOTRBannerProtection.Permission> decoded = new ArrayList<>();
        for (LOTRBannerProtection.Permission p : LOTRBannerProtection.Permission.values()) {
            if ((i & p.bitFlag) != 0) {
                decoded.add(p);
            }
        }
        return decoded;
    }

    public static int encodePermBitFlags(Iterable<LOTRBannerProtection.Permission> permList) {
        int i = 0;
        for (LOTRBannerProtection.Permission p : permList) {
            i |= p.bitFlag;
        }
        return i;
    }

    public void addPermission(LOTRBannerProtection.Permission p) {
        this.perms.add(p);
    }

    public boolean allowsPermission(LOTRBannerProtection.Permission p) {
        return isPermissionEnabled(LOTRBannerProtection.Permission.FULL) || isPermissionEnabled(p);
    }

    public void clearPermissions() {
        this.perms.clear();
    }

    public int encodePermBitFlags() {
        return encodePermBitFlags(this.perms);
    }

    public boolean isPermissionEnabled(LOTRBannerProtection.Permission p) {
        return this.perms.contains(p);
    }

    public Set<LOTRBannerProtection.Permission> listPermissions() {
        return this.perms;
    }

    public void removePermission(LOTRBannerProtection.Permission p) {
        this.perms.remove(p);
    }

    public LOTRBannerWhitelistEntry setFullPerms() {
        clearPermissions();
        addPermission(LOTRBannerProtection.Permission.FULL);
        return this;
    }

    public void setPermissions(Iterable<LOTRBannerProtection.Permission> permissions) {
        clearPermissions();
        for (LOTRBannerProtection.Permission p : permissions) {
            addPermission(p);
        }
    }
}
