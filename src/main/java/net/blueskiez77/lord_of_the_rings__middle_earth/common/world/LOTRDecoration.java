package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

import org.jspecify.annotations.Nullable;

/**
 * Middle-earth's decoration -- the original's populate without its structures, which are today's
 * (LOTRLegacyStructure) -- run in the FEATURES step, chunk by chunk as vanilla decorates, each chunk
 * decorating its own square.
 *
 * <p>Today a chunk being decorated may only touch the chunks next to it, so the decorators work
 * through a {@link WorldGenLevel} of their own: next to the chunk it is the generation region
 * itself; beyond that, what they read is the terrain as the generator makes it, and what they would
 * build there is left out (counted, and logged now and then).
 *
 * <p>The decorators keep state of their own between calls, so one chunk is decorated at a time.
 */
public final class LOTRDecoration {

    /** What a generator decorates with: its populate, and its terrain as generated. */
    public interface Decorating {
        /** populate for chunk (chunkX, chunkZ), over that chunk's own square. */
        void decorate(WorldGenLevel level, ServerLevel server, int chunkX, int chunkZ);

        /** The terrain before decoration: the block at (x, y, z). */
        BlockState terrainBlock(TerrainCache cache, int x, int y, int z);
    }

    /** A few chunks' generated terrain, for reading where the region does not reach. */
    public static final class TerrainCache {
        private final LinkedHashMap<Long, LOTRChunkTerrain> chunks = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, LOTRChunkTerrain> eldest) {
                return size() > 48;
            }
        };

        public LOTRChunkTerrain get(int chunkX, int chunkZ, java.util.function.BiFunction<Integer, Integer, LOTRChunkTerrain> make) {
            return this.chunks.computeIfAbsent(ChunkPos.pack(chunkX, chunkZ), key -> make.apply(chunkX, chunkZ));
        }
    }

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final TerrainCache TERRAIN = new TerrainCache();
    private static final AtomicLong DROPPED = new AtomicLong();

    private LOTRDecoration() {
    }

    /** FEATURES: the chunk's decoration. */
    public static void decorate(WorldGenLevel regionLevel, ChunkPos chunk, Decorating generator) {
        if (!(regionLevel instanceof WorldGenRegion region)) {
            return;
        }
        ServerLevel server = regionLevel.getLevel();
        LOCK.lock();
        try {
            View view = new View(region, generator, chunk);
            WorldGenLevel level = (WorldGenLevel) Proxy.newProxyInstance(LOTRDecoration.class.getClassLoader(),
                    new Class<?>[]{WorldGenLevel.class}, view);
            generator.decorate(level, server, chunk.x(), chunk.z());
        } finally {
            LOCK.unlock();
        }
    }

    private static void dropped(BlockPos pos) {
        long n = DROPPED.incrementAndGet();
        if (n == 1 || n % 1000 == 0) {
            LOTRMod.LOGGER.debug("Decoration reaching past the chunks about it left out ({} so far, e.g. at {})", n, pos);
        }
    }

    /** The decorators' level for one chunk's decoration. */
    private static final class View implements InvocationHandler {
        private final WorldGenRegion region;
        private final Decorating generator;
        private final ChunkPos centre;

        View(WorldGenRegion region, Decorating generator, ChunkPos centre) {
            this.region = region;
            this.generator = generator;
            this.centre = centre;
        }

        /** The chunks next to the one decorated, which the region itself reads and writes. */
        private boolean inRegion(BlockPos pos) {
            return Math.abs((pos.getX() >> 4) - this.centre.x()) <= 1 && Math.abs((pos.getZ() >> 4) - this.centre.z()) <= 1;
        }

        private BlockState getBlockState(BlockPos pos) {
            if (inRegion(pos)) {
                return this.region.getBlockState(pos);
            }
            return this.generator.terrainBlock(TERRAIN, pos.getX(), pos.getY(), pos.getZ());
        }

        private boolean setBlock(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
            if (inRegion(pos)) {
                return this.region.setBlock(pos, state, flags, maxUpdateDepth);
            }
            dropped(pos);
            return false;
        }

        private @Nullable BlockEntity getBlockEntity(BlockPos pos) {
            if (inRegion(pos)) {
                return this.region.getBlockEntity(pos);
            }
            return null;
        }

        private boolean addFreshEntity(Entity entity) {
            BlockPos pos = entity.blockPosition();
            if (inRegion(pos)) {
                return this.region.addFreshEntity(entity);
            }
            dropped(pos);
            return false;
        }

        /**
         * The biome at quart (x, y, z), from the generator's own biome source: the region only holds
         * the biomes of the chunks about the one decorated, and the original's map gives them all.
         */
        private net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> noiseBiome(int x, int y, int z) {
            ServerLevel server = ((WorldGenLevel) this.region).getLevel();
            return server.getChunkSource().getGenerator().getBiomeSource()
                    .getNoiseBiome(x, y, z, server.getChunkSource().randomState().sampler());
        }

        /** Heightmap.Types at (x, z): one above the highest block of that kind, as the original's world reads it. */
        private int getHeight(Heightmap.Types type, int x, int z) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
            int top = this.region.getMaxY();
            for (int y = top; y >= this.region.getMinY(); --y) {
                if (type.isOpaque().test(getBlockState(pos.setY(y)))) {
                    return y + 1;
                }
            }
            return this.region.getMinY();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            int n = args == null ? 0 : args.length;
            switch (name) {
                case "getBlockState":
                    if (n == 1 && args[0] instanceof BlockPos pos) {
                        return getBlockState(pos);
                    }
                    break;
                case "getFluidState":
                    if (n == 1 && args[0] instanceof BlockPos pos) {
                        return inRegion(pos) ? this.region.getFluidState(pos) : getBlockState(pos).getFluidState();
                    }
                    break;
                case "isStateAtPosition":
                    @SuppressWarnings("unchecked") Predicate<BlockState> statePredicate = (Predicate<BlockState>) args[1];
                    return statePredicate.test(getBlockState((BlockPos) args[0]));
                case "isFluidAtPosition":
                    @SuppressWarnings("unchecked") Predicate<FluidState> fluidPredicate = (Predicate<FluidState>) args[1];
                    return fluidPredicate.test(getBlockState((BlockPos) args[0]).getFluidState());
                case "setBlock":
                    if (n == 4) {
                        return setBlock((BlockPos) args[0], (BlockState) args[1], (int) args[2], (int) args[3]);
                    }
                    break;
                case "removeBlock":
                    BlockPos removed = (BlockPos) args[0];
                    return setBlock(removed, getBlockState(removed).getFluidState().createLegacyBlock(), Block.UPDATE_ALL, 512);
                case "destroyBlock":
                    BlockPos destroyed = (BlockPos) args[0];
                    return setBlock(destroyed, getBlockState(destroyed).getFluidState().createLegacyBlock(), Block.UPDATE_ALL, 512);
                case "getBlockEntity":
                    if (n == 1) {
                        return getBlockEntity((BlockPos) args[0]);
                    }
                    if (n == 2 && args[1] instanceof BlockEntityType<?> type) {
                        BlockEntity found = getBlockEntity((BlockPos) args[0]);
                        return found != null && found.getType() == type ? Optional.of(found) : Optional.empty();
                    }
                    break;
                case "addFreshEntity":
                    return addFreshEntity((Entity) args[0]);
                case "getHeight":
                    if (n == 3) {
                        return getHeight((Heightmap.Types) args[0], (int) args[1], (int) args[2]);
                    }
                    break;
                case "getBiome":
                    if (n == 1 && args[0] instanceof BlockPos pos) {
                        return noiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);
                    }
                    break;
                case "getNoiseBiome":
                case "getUncachedNoiseBiome":
                    if (n >= 3) {
                        return noiseBiome((int) args[0], (int) args[1], (int) args[2]);
                    }
                    break;
                case "getHeightmapPos":
                    BlockPos at = (BlockPos) args[1];
                    return new BlockPos(at.getX(), getHeight((Heightmap.Types) args[0], at.getX(), at.getZ()), at.getZ());
                case "ensureCanWrite":
                    return true;
                case "scheduleTick":
                    if (!inRegion((BlockPos) args[0])) {
                        return null;
                    }
                    return invokeOnRegion(method, args);
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];
                case "toString":
                    return "LOTRDecoration[" + this.centre + "]";
                default:
                    break;
            }
            if (method.isDefault()) {
                return InvocationHandler.invokeDefault(proxy, method, args);
            }
            return invokeOnRegion(method, args);
        }

        private Object invokeOnRegion(Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(this.region, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    /** Blocks below and above the original's world, for the predicted terrain. */
    public static BlockState outsideOriginalWorld(int y) {
        return y < 0 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }
}
