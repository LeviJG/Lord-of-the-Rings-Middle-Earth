package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.level.ChunkPos;

import org.jspecify.annotations.Nullable;

/** LOTRVillagePositionCache: whether a village has its centre in each chunk asked about, remembered (20,000 at most). */
public class LOTRVillagePositionCache {
    private final Map<Long, LocationInfo> cacheMap = new ConcurrentHashMap<>();

    public void clearCache() {
        this.cacheMap.clear();
    }

    public @Nullable LocationInfo getLocationAt(int chunkX, int chunkZ) {
        return this.cacheMap.get(ChunkPos.pack(chunkX, chunkZ));
    }

    public LocationInfo markResult(int chunkX, int chunkZ, LocationInfo result) {
        if (this.cacheMap.size() >= 20000) {
            clearCache();
        }
        this.cacheMap.put(ChunkPos.pack(chunkX, chunkZ), result);
        return result;
    }
}
