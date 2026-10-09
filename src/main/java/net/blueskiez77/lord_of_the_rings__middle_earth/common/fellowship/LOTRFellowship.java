package net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRFellowship: a band of players under an owner, with members (some of them its admins), a name,
 * an icon, and whether its members may not hurt one another or one another's hired units.
 * Saved by the original's keys.
 *
 * <p>NOT ported yet: the map's side -- members' places shown on the map (ShowMap, kept and
 * toggled but shown nowhere) and custom waypoints shared to it (D13).
 */
public class LOTRFellowship {

    private record Member(UUID member, boolean admin) {
        static final Codec<Member> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("Member").forGetter(Member::member),
                Codec.BOOL.optionalFieldOf("Admin", false).forGetter(Member::admin)).apply(i, Member::new));
    }

    public static final Codec<LOTRFellowship> CODEC = RecordCodecBuilder.create(i -> i.group(
            UUIDUtil.STRING_CODEC.fieldOf("FellowshipID").forGetter(fs -> fs.fellowshipUUID),
            Codec.BOOL.optionalFieldOf("Disbanded", false).forGetter(fs -> fs.disbanded),
            UUIDUtil.STRING_CODEC.optionalFieldOf("Owner").forGetter(fs -> Optional.ofNullable(fs.ownerUUID)),
            Member.CODEC.listOf().optionalFieldOf("Members", List.of()).forGetter(fs -> fs.memberUUIDs.stream()
                    .map(m -> new Member(m, fs.adminUUIDs.contains(m))).toList()),
            Codec.STRING.optionalFieldOf("Name").forGetter(fs -> Optional.ofNullable(fs.fellowshipName)),
            ItemStack.CODEC.optionalFieldOf("Icon").forGetter(fs -> Optional.ofNullable(fs.fellowshipIcon)),
            Codec.BOOL.optionalFieldOf("PreventPVP", true).forGetter(fs -> fs.preventPVP),
            Codec.BOOL.optionalFieldOf("PreventHiredFF", true).forGetter(fs -> fs.preventHiredFF),
            Codec.BOOL.optionalFieldOf("ShowMap", true).forGetter(fs -> fs.showMapLocations)
    ).apply(i, (id, disbanded, owner, members, name, icon, pvp, hiredFF, showMap) -> {
        LOTRFellowship fs = new LOTRFellowship(id);
        fs.disbanded = disbanded;
        fs.ownerUUID = owner.orElseGet(UUID::randomUUID);
        for (Member m : members) {
            fs.memberUUIDs.add(m.member());
            if (m.admin()) {
                fs.adminUUIDs.add(m.member());
            }
        }
        fs.fellowshipName = name.orElse(null);
        fs.fellowshipIcon = icon.orElse(null);
        fs.preventPVP = pvp;
        fs.preventHiredFF = hiredFF;
        fs.showMapLocations = showMap;
        return fs;
    }));

    private final UUID fellowshipUUID;
    private @Nullable String fellowshipName;
    private boolean disbanded;
    private @Nullable ItemStack fellowshipIcon;
    private UUID ownerUUID;
    private final List<UUID> memberUUIDs = new ArrayList<>();
    private final Set<UUID> adminUUIDs = new HashSet<>();
    private boolean preventPVP = true;
    private boolean preventHiredFF = true;
    private boolean showMapLocations = true;

    private LOTRFellowship(UUID id) {
        this.fellowshipUUID = id;
        this.ownerUUID = UUID.randomUUID();
    }

    public LOTRFellowship(UUID owner, String name) {
        this(UUID.randomUUID());
        this.ownerUUID = owner;
        this.fellowshipName = name;
    }

    public UUID getFellowshipID() {
        return this.fellowshipUUID;
    }

    public String getName() {
        return this.fellowshipName == null ? "" : this.fellowshipName;
    }

    void setName(String name) {
        this.fellowshipName = name;
    }

    public @Nullable ItemStack getIcon() {
        return this.fellowshipIcon;
    }

    void setIcon(@Nullable ItemStack icon) {
        this.fellowshipIcon = icon;
    }

    public UUID getOwner() {
        return this.ownerUUID;
    }

    /** setOwner: the old owner stays on as the first member; the new one is no longer a member or admin. */
    void setOwner(UUID owner) {
        UUID prevOwner = this.ownerUUID;
        if (prevOwner != null && !this.memberUUIDs.contains(prevOwner)) {
            this.memberUUIDs.add(0, prevOwner);
        }
        this.ownerUUID = owner;
        this.memberUUIDs.remove(owner);
        this.adminUUIDs.remove(owner);
    }

    public List<UUID> getMemberUUIDs() {
        return this.memberUUIDs;
    }

    public List<UUID> getAllPlayerUUIDs() {
        List<UUID> list = new ArrayList<>();
        list.add(this.ownerUUID);
        list.addAll(this.memberUUIDs);
        return list;
    }

    public int getPlayerCount() {
        return this.memberUUIDs.size() + 1;
    }

    public boolean containsPlayer(UUID player) {
        return isOwner(player) || hasMember(player);
    }

    public boolean hasMember(UUID player) {
        return this.memberUUIDs.contains(player);
    }

    public boolean isOwner(UUID player) {
        return this.ownerUUID.equals(player);
    }

    public boolean isAdmin(UUID player) {
        return hasMember(player) && this.adminUUIDs.contains(player);
    }

    public Set<UUID> getAdminUUIDs() {
        return this.adminUUIDs;
    }

    void addMember(UUID player) {
        if (!isOwner(player) && !this.memberUUIDs.contains(player)) {
            this.memberUUIDs.add(player);
        }
    }

    void removeMember(UUID player) {
        this.memberUUIDs.remove(player);
        this.adminUUIDs.remove(player);
    }

    void setAdmin(UUID player, boolean flag) {
        if (this.memberUUIDs.contains(player)) {
            if (flag) {
                this.adminUUIDs.add(player);
            } else {
                this.adminUUIDs.remove(player);
            }
        }
    }

    public boolean isDisbanded() {
        return this.disbanded;
    }

    void setDisbanded() {
        this.disbanded = true;
    }

    public boolean getPreventPVP() {
        return this.preventPVP;
    }

    void setPreventPVP(boolean flag) {
        this.preventPVP = flag;
    }

    public boolean getPreventHiredFriendlyFire() {
        return this.preventHiredFF;
    }

    void setPreventHiredFriendlyFire(boolean flag) {
        this.preventHiredFF = flag;
    }

    public boolean getShowMapLocations() {
        return this.showMapLocations;
    }

    void setShowMapLocations(boolean flag) {
        this.showMapLocations = flag;
    }
}
