package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.core.UUIDUtil;

/**
 * LOTRGreyWandererTracker: the Grey Wanderers (Gandalf) abroad in the world,
 * each with the ticks it has left -- three minutes from its arrival, renewed
 * whenever it offers a player its welcome. A wanderer whose time is up
 * departs as soon as it is not fighting. Kept with the world in
 * LOTRLevelData, as "GreyWanderers" and "GWSpawnTick".
 *
 * <p>NOT ported yet: performSpawning -- a wanderer arriving, every two
 * minutes while none is abroad, 4 to 16 blocks from a player with no Grey
 * Wanderer quest -- which ran from LOTREventSpawner in the Middle-earth
 * dimension (D12). {@link #spawnCooldown} is kept and saved meanwhile.
 */
public final class LOTRGreyWandererTracker {

    public static final int WANDERER_TIME = 3600;

    public static final Map<UUID, Integer> activeGreyWanderers = new HashMap<>();
    public static int spawnCooldown = 2400;

    record Entry(UUID id, int cd) {
        static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("ID").forGetter(Entry::id),
                Codec.INT.fieldOf("CD").forGetter(Entry::cd)).apply(i, Entry::new));
    }

    private LOTRGreyWandererTracker() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> updateCooldowns());
    }

    public static void addNewWanderer(UUID id) {
        activeGreyWanderers.put(id, WANDERER_TIME);
        LOTRLevelData.markDirty();
    }

    public static boolean isWandererActive(UUID id) {
        return activeGreyWanderers.getOrDefault(id, 0) > 0;
    }

    public static void setWandererActive(UUID id) {
        if (activeGreyWanderers.containsKey(id)) {
            activeGreyWanderers.put(id, WANDERER_TIME);
            LOTRLevelData.markDirty();
        }
    }

    public static void updateCooldowns() {
        boolean removed = false;
        Iterator<Map.Entry<UUID, Integer>> it = activeGreyWanderers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Integer> entry = it.next();
            int cd = entry.getValue() - 1;
            entry.setValue(cd);
            if (cd <= 0) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            LOTRLevelData.markDirty();
        }
    }

    static List<Entry> save() {
        return activeGreyWanderers.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList();
    }

    static void load(List<Entry> entries, int cooldown) {
        activeGreyWanderers.clear();
        for (Entry e : entries) {
            activeGreyWanderers.put(e.id(), e.cd());
        }
        spawnCooldown = cooldown;
    }

    static void reset() {
        activeGreyWanderers.clear();
        spawnCooldown = 2400;
    }
}
