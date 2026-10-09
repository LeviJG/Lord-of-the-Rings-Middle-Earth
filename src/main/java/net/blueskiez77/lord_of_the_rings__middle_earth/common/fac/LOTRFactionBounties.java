package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.jspecify.annotations.Nullable;

/**
 * LOTRFactionBounties: for each faction, the players who have lately killed its people in its own
 * land -- each kill remembered for 3,456,000 ticks -- of whom those with 25 such kills may be made
 * the target of a bounty; and those on whom a bounty was lately claimed (864,000 ticks), who are safe
 * from more meanwhile. The original kept a file per faction; here one file for the world holds them all.
 */
public final class LOTRFactionBounties {

    public static final int KILL_RECORD_TIME = 3456000;
    public static final int BOUNTY_KILLED_TIME = 864000;

    private static final Map<LOTRFaction, LOTRFactionBounties> FACTION_BOUNTY_MAP = new EnumMap<>(LOTRFaction.class);
    private static @Nullable MinecraftServer server;
    private static @Nullable Storage storage;

    public final LOTRFaction theFaction;
    private final Map<UUID, PlayerData> playerList = new HashMap<>();

    private LOTRFactionBounties(LOTRFaction faction) {
        this.theFaction = faction;
    }

    public static LOTRFactionBounties forFaction(LOTRFaction faction) {
        return FACTION_BOUNTY_MAP.computeIfAbsent(faction, LOTRFactionBounties::new);
    }

    private static void markDirty() {
        if (storage != null) {
            storage.setDirty();
        }
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            FACTION_BOUNTY_MAP.clear();
            storage = s.getDataStorage().computeIfAbsent(Storage.TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            storage = null;
            FACTION_BOUNTY_MAP.clear();
        });
        // updateAll, each server tick; the saved copy is refreshed now and then (minor changes, every 600 ticks).
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            boolean changed = false;
            for (LOTRFactionBounties fb : FACTION_BOUNTY_MAP.values()) {
                changed |= fb.update();
            }
            if (changed && s.getTickCount() % 600 == 0) {
                markDirty();
            }
        });
    }

    /** findBountyTargets: those with at least so many kills, not lately bountied. */
    public List<PlayerData> findBountyTargets(int killAmount) {
        List<PlayerData> players = new ArrayList<>();
        for (PlayerData pd : this.playerList.values()) {
            if (!pd.recentlyBountyKilled() && pd.getNumKills() >= killAmount) {
                players.add(pd);
            }
        }
        return players;
    }

    public PlayerData forPlayer(Player player) {
        return forPlayer(player.getUUID());
    }

    public PlayerData forPlayer(UUID id) {
        return this.playerList.computeIfAbsent(id, PlayerData::new);
    }

    private boolean update() {
        boolean changed = false;
        for (PlayerData pd : this.playerList.values()) {
            changed |= pd.update();
        }
        return changed;
    }

    public static final class PlayerData {
        public final UUID playerID;
        private @Nullable String username;
        private final List<int[]> killRecords = new ArrayList<>();
        private int recentBountyKilled;

        PlayerData(UUID id) {
            this.playerID = id;
        }

        /** findUsername: from the server's record of names. */
        public String findUsername() {
            if (this.username == null && server != null) {
                this.username = server.services().nameToIdCache().get(this.playerID).map(NameAndId::name).orElse("");
            }
            return this.username == null ? "" : this.username;
        }

        public int getNumKills() {
            return this.killRecords.size();
        }

        public boolean recentlyBountyKilled() {
            return this.recentBountyKilled > 0;
        }

        public void recordBountyKilled() {
            this.recentBountyKilled = BOUNTY_KILLED_TIME;
            markDirty();
        }

        public void recordNewKill() {
            this.killRecords.add(new int[]{KILL_RECORD_TIME});
            markDirty();
        }

        boolean shouldSave() {
            return !this.killRecords.isEmpty() || this.recentBountyKilled > 0;
        }

        boolean update() {
            boolean minorChanges = false;
            if (this.recentBountyKilled > 0) {
                --this.recentBountyKilled;
                minorChanges = true;
            }
            for (int[] kr : this.killRecords) {
                --kr[0];
                minorChanges = true;
            }
            this.killRecords.removeIf(kr -> kr[0] <= 0);
            return minorChanges;
        }
    }

    /** The saved form: each faction's PlayerList, by the original's keys. */
    private static final class Storage extends SavedData {

        private record SavedPlayer(UUID id, List<Integer> killTimes, int recentBountyKilled) {
            static final Codec<SavedPlayer> CODEC = RecordCodecBuilder.create(i -> i.group(
                    UUIDUtil.STRING_CODEC.fieldOf("UUID").forGetter(SavedPlayer::id),
                    Codec.INT.fieldOf("Time").codec().listOf().optionalFieldOf("KillRecords", List.of())
                            .forGetter(SavedPlayer::killTimes),
                    Codec.INT.optionalFieldOf("RecentBountyKilled", 0).forGetter(SavedPlayer::recentBountyKilled)
            ).apply(i, SavedPlayer::new));
        }

        private static final Codec<Storage> CODEC = Codec.unboundedMap(Codec.STRING, SavedPlayer.CODEC.listOf()
                .fieldOf("PlayerList").codec()).xmap(map -> {
            for (Map.Entry<String, List<SavedPlayer>> e : map.entrySet()) {
                LOTRFaction faction = LOTRFaction.forName(e.getKey());
                if (faction == null) {
                    continue;
                }
                LOTRFactionBounties fb = forFaction(faction);
                for (SavedPlayer sp : e.getValue()) {
                    PlayerData pd = fb.forPlayer(sp.id());
                    sp.killTimes().forEach(t -> pd.killRecords.add(new int[]{t}));
                    pd.recentBountyKilled = sp.recentBountyKilled();
                }
            }
            return new Storage();
        }, storage -> {
            Map<String, List<SavedPlayer>> map = new HashMap<>();
            for (LOTRFactionBounties fb : FACTION_BOUNTY_MAP.values()) {
                List<SavedPlayer> players = new ArrayList<>();
                for (PlayerData pd : fb.playerList.values()) {
                    if (pd.shouldSave()) {
                        players.add(new SavedPlayer(pd.playerID, pd.killRecords.stream().map(kr -> kr[0]).toList(),
                                pd.recentBountyKilled));
                    }
                }
                if (!players.isEmpty()) {
                    map.put(fb.theFaction.codeName(), players);
                }
            }
            return map;
        });

        static final SavedDataType<Storage> TYPE = new SavedDataType<>(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "faction_bounties"), Storage::new, CODEC, null);
    }
}
