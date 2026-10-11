package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.legacy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.ticks.BlackholeTickAccess;

import org.jspecify.annotations.Nullable;

/**
 * One of the original's structures, built once by its own code into a level of its own -- the
 * terrain as the generator makes it, nothing else -- and kept as what it built: its blocks, the
 * contents of its chests and signs, and its people. Today's structures are planned this way before
 * any chunk they stand in is decorated, and each chunk then places its own part (LOTRBlueprintPiece).
 */
public final class LOTRBlueprint {

    /** The terrain the structure is built on: a block of the generated world, before decoration. */
    public interface Terrain {
        BlockState block(int x, int y, int z);
    }

    final Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
    final Map<BlockPos, CompoundTag> blockEntities = new LinkedHashMap<>();
    final List<CompoundTag> entities = new ArrayList<>();
    @Nullable BoundingBox box;

    LOTRBlueprint() {
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty() && this.entities.isEmpty();
    }

    // ------------------------------------------------------------------ recording

    /** The level a structure is built into; build with {@link #level()}, then {@link #finish()}. */
    public static final class Recorder implements InvocationHandler {
        private final LOTRBlueprint blueprint = new LOTRBlueprint();
        private final Map<BlockPos, BlockEntity> liveBlockEntities = new LinkedHashMap<>();
        private final List<Entity> liveEntities = new ArrayList<>();
        private final Terrain terrain;
        private final ServerLevel server;
        private final RegistryAccess registries;
        private final BiomeSource biomeSource;
        private final Climate.Sampler sampler;
        private final long seed;
        private final int minY;
        private final int height;
        private final int seaLevel;
        private final WorldGenLevel level;

        public Recorder(Terrain terrain, ServerLevel server, BiomeSource biomeSource, Climate.Sampler sampler, long seed,
                        int minY, int height, int seaLevel) {
            this.terrain = terrain;
            this.server = server;
            this.registries = server.registryAccess();
            this.biomeSource = biomeSource;
            this.sampler = sampler;
            this.seed = seed;
            this.minY = minY;
            this.height = height;
            this.seaLevel = seaLevel;
            this.level = (WorldGenLevel) Proxy.newProxyInstance(LOTRBlueprint.class.getClassLoader(), new Class<?>[]{WorldGenLevel.class}, this);
        }

        public WorldGenLevel level() {
            return this.level;
        }

        private BlockState getBlockState(BlockPos pos) {
            BlockState written = this.blueprint.blocks.get(pos);
            return written != null ? written : this.terrain.block(pos.getX(), pos.getY(), pos.getZ());
        }

        private boolean setBlock(BlockPos pos, BlockState state) {
            if (pos.getY() < this.minY || pos.getY() >= this.minY + this.height) {
                return false;
            }
            BlockPos at = pos.immutable();
            this.blueprint.blocks.put(at, state);
            this.liveBlockEntities.remove(at);
            if (state.getBlock() instanceof EntityBlock entityBlock) {
                BlockEntity blockEntity = entityBlock.newBlockEntity(at, state);
                if (blockEntity != null) {
                    this.liveBlockEntities.put(at, blockEntity);
                }
            }
            return true;
        }

        private int getHeight(Heightmap.Types type, int x, int z) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
            for (int y = this.minY + this.height - 1; y >= this.minY; --y) {
                if (type.isOpaque().test(getBlockState(pos.setY(y)))) {
                    return y + 1;
                }
            }
            return this.minY;
        }

        private Holder<Biome> biome(int qx, int qy, int qz) {
            return this.biomeSource.getNoiseBiome(qx, qy, qz, this.sampler);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            int n = args == null ? 0 : args.length;
            switch (method.getName()) {
                case "getBlockState":
                    return getBlockState((BlockPos) args[0]);
                case "getFluidState":
                    return getBlockState((BlockPos) args[0]).getFluidState();
                case "isStateAtPosition":
                    @SuppressWarnings("unchecked") Predicate<BlockState> sp = (Predicate<BlockState>) args[1];
                    return sp.test(getBlockState((BlockPos) args[0]));
                case "isFluidAtPosition":
                    @SuppressWarnings("unchecked") Predicate<FluidState> fp = (Predicate<FluidState>) args[1];
                    return fp.test(getBlockState((BlockPos) args[0]).getFluidState());
                case "setBlock":
                    if (n == 4) {
                        return setBlock((BlockPos) args[0], (BlockState) args[1]);
                    }
                    break;
                case "removeBlock":
                case "destroyBlock":
                    BlockPos removed = (BlockPos) args[0];
                    return setBlock(removed, getBlockState(removed).getFluidState().createLegacyBlock());
                case "getBlockEntity":
                    BlockEntity found = this.liveBlockEntities.get((BlockPos) args[0]);
                    if (n == 2) {
                        return found != null && found.getType() == args[1] ? Optional.of(found) : Optional.empty();
                    }
                    return found;
                case "addFreshEntity":
                    this.liveEntities.add((Entity) args[0]);
                    return true;
                case "getHeight":
                    if (n == 3) {
                        return getHeight((Heightmap.Types) args[0], (int) args[1], (int) args[2]);
                    }
                    return this.height;
                case "getHeightmapPos":
                    BlockPos at = (BlockPos) args[1];
                    return new BlockPos(at.getX(), getHeight((Heightmap.Types) args[0], at.getX(), at.getZ()), at.getZ());
                case "getMinY":
                    return this.minY;
                case "getSeaLevel":
                    return this.seaLevel;
                case "getBiome":
                    BlockPos bp = (BlockPos) args[0];
                    return biome(bp.getX() >> 2, bp.getY() >> 2, bp.getZ() >> 2);
                case "getNoiseBiome":
                case "getUncachedNoiseBiome":
                    return biome((int) args[0], (int) args[1], (int) args[2]);
                case "getLevel":
                    return this.server;
                case "getServer":
                    return this.server.getServer();
                case "getSeed":
                    return this.seed;
                case "getRandom":
                    return RandomSource.create(this.seed);
                case "registryAccess":
                    return this.registries;
                case "dimensionType":
                    return this.server.dimensionType();
                case "getLevelData":
                    return this.server.getLevelData();
                case "getCurrentDifficultyAt":
                    return new DifficultyInstance(Difficulty.NORMAL, 0L, 0L, 0.0f);
                case "getBrightness":
                    return args[0] == LightLayer.SKY ? 15 : 0;
                case "getRawBrightness":
                case "getMaxLocalRawBrightness":
                    return 15;
                case "canSeeSky":
                    BlockPos sky = (BlockPos) args[0];
                    return sky.getY() >= getHeight(Heightmap.Types.MOTION_BLOCKING, sky.getX(), sky.getZ());
                case "isClientSide":
                    return false;
                case "getBlockTicks":
                case "getFluidTicks":
                    return BlackholeTickAccess.emptyLevelList();
                case "ensureCanWrite":
                    return true;
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];
                case "toString":
                    return "LOTRBlueprint.Recorder";
                default:
                    break;
            }
            if (method.isDefault()) {
                return InvocationHandler.invokeDefault(proxy, method, args);
            }
            if (method.getReturnType() == void.class) {
                return null;
            }
            throw new UnsupportedOperationException("A structure being planned asked its level for " + method.getName());
        }

        /** What was built: its block entities and people saved as data, its bounds found. */
        public LOTRBlueprint finish() {
            LOTRBlueprint result = this.blueprint;
            for (Map.Entry<BlockPos, BlockEntity> e : this.liveBlockEntities.entrySet()) {
                result.blockEntities.put(e.getKey(), e.getValue().saveWithFullMetadata(this.registries));
            }
            for (Entity entity : this.liveEntities) {
                TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.registries);
                if (entity.save(output)) {
                    result.entities.add(output.buildResult());
                }
            }
            List<BlockPos> all = new ArrayList<>(result.blocks.keySet());
            for (Entity entity : this.liveEntities) {
                all.add(entity.blockPosition());
            }
            result.box = all.isEmpty() ? null : BoundingBox.encapsulatingPositions(all).orElse(null);
            return result;
        }
    }

    // ------------------------------------------------------------------ placing

    /** Places the blueprint's blocks, block entities and people that fall inside {@code area}. */
    public void place(WorldGenLevel level, BoundingBox area) {
        for (Map.Entry<BlockPos, BlockState> e : this.blocks.entrySet()) {
            BlockPos pos = e.getKey();
            if (!area.isInside(pos)) {
                continue;
            }
            level.setBlock(pos, e.getValue(), Block.UPDATE_CLIENTS);
            CompoundTag saved = this.blockEntities.get(pos);
            BlockEntity blockEntity = saved == null ? null : level.getBlockEntity(pos);
            if (blockEntity != null) {
                // As StructureTemplate fills its block entities: the one the level made, loaded with what was built.
                blockEntity.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), saved));
            }
        }
        ServerLevel server = level.getLevel();
        for (CompoundTag tag : this.entities) {
            ListTag posTag = tag.getListOrEmpty("Pos");
            BlockPos pos = BlockPos.containing(posTag.getDoubleOr(0, 0.0), posTag.getDoubleOr(1, 0.0), posTag.getDoubleOr(2, 0.0));
            if (!area.isInside(pos)) {
                continue;
            }
            Entity entity = EntityType.loadEntityRecursive(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), tag),
                    server, EntitySpawnReason.STRUCTURE, e -> e);
            if (entity != null) {
                level.addFreshEntityWithPassengers(entity);
            }
        }
    }

    // ------------------------------------------------------------------ saving

    public void save(CompoundTag tag) {
        Map<BlockState, Integer> palette = new LinkedHashMap<>();
        long[] positions = new long[this.blocks.size()];
        int[] states = new int[this.blocks.size()];
        int i = 0;
        for (Map.Entry<BlockPos, BlockState> e : this.blocks.entrySet()) {
            positions[i] = e.getKey().asLong();
            states[i] = palette.computeIfAbsent(e.getValue(), s -> palette.size());
            ++i;
        }
        ListTag paletteTag = new ListTag();
        for (BlockState state : palette.keySet()) {
            paletteTag.add(NbtUtils.writeBlockState(state));
        }
        tag.put("Palette", paletteTag);
        tag.put("Positions", new LongArrayTag(positions));
        tag.put("States", new IntArrayTag(states));
        ListTag blockEntityTag = new ListTag();
        blockEntityTag.addAll(this.blockEntities.values());
        tag.put("BlockEntities", blockEntityTag);
        ListTag entityTag = new ListTag();
        entityTag.addAll(this.entities);
        tag.put("Entities", entityTag);
    }

    public static LOTRBlueprint load(CompoundTag tag) {
        LOTRBlueprint blueprint = new LOTRBlueprint();
        HolderLookup<Block> blocks = BuiltInRegistries.BLOCK;
        ListTag paletteTag = tag.getListOrEmpty("Palette");
        BlockState[] palette = new BlockState[paletteTag.size()];
        for (int i = 0; i < palette.length; ++i) {
            palette[i] = NbtUtils.readBlockState(blocks, paletteTag.getCompoundOrEmpty(i));
        }
        long[] positions = tag.getLongArray("Positions").orElse(new long[0]);
        int[] states = tag.getIntArray("States").orElse(new int[0]);
        for (int i = 0; i < positions.length && i < states.length; ++i) {
            blueprint.blocks.put(BlockPos.of(positions[i]), palette[states[i]]);
        }
        for (Tag t : tag.getListOrEmpty("BlockEntities")) {
            if (t instanceof CompoundTag c) {
                blueprint.blockEntities.put(new BlockPos(c.getIntOr("x", 0), c.getIntOr("y", 0), c.getIntOr("z", 0)), c);
            }
        }
        for (Tag t : tag.getListOrEmpty("Entities")) {
            if (t instanceof CompoundTag c) {
                blueprint.entities.add(c);
            }
        }
        return blueprint;
    }
}
