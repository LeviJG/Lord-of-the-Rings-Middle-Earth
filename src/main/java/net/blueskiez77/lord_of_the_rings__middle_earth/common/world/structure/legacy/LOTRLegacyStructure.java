package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomeDecorator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRGrukHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRMarshHutStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun.LOTRTicketBoothStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGen;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import org.jspecify.annotations.Nullable;

/**
 * The original's structures, placed as today's are: in the STRUCTURE_STARTS step each chunk of
 * Middle-earth looks for what the original would have built there -- the biome decorator's random
 * structures with their chunk chances, the marsh hut, Gruk's house and the ticket booth, and the
 * buildings of any village whose plan puts one in this chunk -- and each is built once by its own
 * code into a blueprint (LOTRBlueprint), a piece of this chunk's structure. The FEATURES step of
 * every chunk a piece stands in then places its part.
 *
 * <p>Where the structures stand is chosen with the original's own chances and rules, from a random
 * of the chunk's own; what they look like is the original's code. A structure that fails while
 * being built is left out, and logged, rather than stopping the world.
 */
public class LOTRLegacyStructure extends Structure {

    public static final MapCodec<LOTRLegacyStructure> CODEC = simpleCodec(LOTRLegacyStructure::new);
    public static final StructureType<LOTRLegacyStructure> TYPE = () -> CODEC;
    public static final StructurePieceType.ContextlessType PIECE_TYPE = LOTRBlueprintPiece::new;

    /** The structures keep placement state of their own between calls: one is planned at a time. */
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static volatile @Nullable MinecraftServer server;

    public LOTRLegacyStructure(StructureSettings settings) {
        super(settings);
    }

    public static void init() {
        Registry.register(BuiltInRegistries.STRUCTURE_TYPE, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "legacy"), TYPE);
        Registry.register(BuiltInRegistries.STRUCTURE_PIECE, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "blueprint"), PIECE_TYPE);
        ServerLifecycleEvents.SERVER_STARTING.register(s -> server = s);
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> server = null);
    }

    @Override
    public StructureType<?> type() {
        return TYPE;
    }

    private record Planned(String name, LOTRBlueprint blueprint) {
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!(context.chunkGenerator() instanceof LOTRChunkGenerator generator)) {
            return Optional.empty();
        }
        MinecraftServer s = server;
        ServerLevel level = s == null ? null : s.getLevel(LOTRWorldGen.MIDDLE_EARTH);
        if (level == null) {
            return Optional.empty();
        }
        ChunkPos chunk = context.chunkPos();
        List<Planned> planned;
        LOCK.lock();
        try {
            planned = plan(generator, level, context, chunk);
        } catch (RuntimeException e) {
            LOTRMod.LOGGER.error("Could not plan the structures of chunk {}", chunk, e);
            return Optional.empty();
        } finally {
            LOCK.unlock();
        }
        if (planned.isEmpty()) {
            return Optional.empty();
        }
        BlockPos at = new BlockPos(chunk.getMiddleBlockX(), generator.getSeaLevel(), chunk.getMiddleBlockZ());
        return Optional.of(new GenerationStub(at, builder -> addPieces(builder, planned)));
    }

    private static void addPieces(StructurePiecesBuilder builder, List<Planned> planned) {
        for (Planned p : planned) {
            if (p.blueprint.box != null) {
                builder.addPiece(new LOTRBlueprintPiece(p.name, p.blueprint, p.blueprint.box));
            }
        }
    }

    // ---------------------------------------------------------------- planning

    private static List<Planned> plan(LOTRChunkGenerator generator, ServerLevel level, GenerationContext context, ChunkPos chunk) {
        List<Planned> planned = new ArrayList<>();
        LOTRChunkGenerator.World w = generator.world();
        LOTRWorldChunkManager chunkManager = w.chunkManager;
        // The decorated square of this chunk: its own columns (the original's populate square, moved
        // back eight blocks onto the chunk, as LOTRChunkGenerator.decorate is).
        int chunkX = chunk.getMinBlockX() - 8;
        int chunkZ = chunk.getMinBlockZ() - 8;
        LOTRBiome biome = chunkManager.getBiomeGenAt(chunkX + 16, chunkZ + 16);
        LOTRBiomeVariant variant = chunkManager.getBiomeVariantAt(chunkX + 16, chunkZ + 16);
        LOTRBiomeDecorator decorator = biome.decorator;
        RandomSource rand = new LegacyRandomSource(w.seed ^ chunk.pack() * 341873128712L ^ 0x4C4F5452L);

        if (!variant.disableStructures && Math.abs(chunkX) > 32 && Math.abs(chunkZ) > 32) {
            // LOTRBiomeDecorator.decorate's random structures: each by its chunk chance, off the roads.
            long seed = chunkX * 1879267L ^ chunkZ * 67209689L;
            seed = seed * seed * 5829687L + seed * 2876L;
            RandomSource structureRand = new LegacyRandomSource(seed);
            if (LOTRRoads.isRoadNear(chunkX + 8, chunkZ + 8, 16) < 0.0f) {
                for (LOTRBiomeDecorator.RandomStructure random : decorator.randomStructures) {
                    if (structureRand.nextInt(random.chunkChance()) != 0) {
                        continue;
                    }
                    int i = chunkX + rand.nextInt(16) + 8;
                    int k = chunkZ + rand.nextInt(16) + 8;
                    record(planned, generator, level, context, random.structureGen(), (world, r) ->
                            build(random.structureGen(), world, r, i, LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k), k), rand);
                }
            }
            // The buildings of the villages whose plans put one in this chunk.
            for (LOTRVillageGen village : decorator.villages) {
                planVillageBuildings(planned, generator, level, context, village, chunkManager, w.seed, chunk);
            }
        }
        // The structures the biome's own decorate built.
        biome.planStructures((structure, build) -> record(planned, generator, level, context, structure, build, rand),
                rand, chunkX, chunkZ);
        if (LOTRMarshHutStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRMarshHutStructure house = new LOTRMarshHutStructure();
            house.restrictions = false;
            record(planned, generator, level, context, house, (world, r) ->
                    house.generateAndFinish(world, r, i, LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k), k), rand);
        }
        if (LOTRGrukHouseStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRGrukHouseStructure house = new LOTRGrukHouseStructure(false);
            house.restrictions = false;
            record(planned, generator, level, context, house, (world, r) ->
                    house.generateAndFinish(world, r, i, LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k), k, 2), rand);
        }
        if (LOTRTicketBoothStructure.generatesAt(chunkX, chunkZ)) {
            int i = chunkX + 8;
            int k = chunkZ + 8;
            LOTRTicketBoothStructure booth = new LOTRTicketBoothStructure(false);
            booth.restrictions = false;
            record(planned, generator, level, context, booth, (world, r) ->
                    booth.generateAndFinish(world, r, i, LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k), k, 3), rand);
        }
        return planned;
    }

    /** LOTRVillageGen.generateInstanceInChunk's buildings: each whose place on the plan falls in this chunk. */
    private static void planVillageBuildings(List<Planned> planned, LOTRChunkGenerator generator, ServerLevel level, GenerationContext context,
                                             LOTRVillageGen village, LOTRWorldChunkManager chunkManager, long seed, ChunkPos chunk) {
        for (LOTRVillageGen.AbstractInstance<?> instance : village.getNearbyVillages(seed, chunkManager, chunk.x(), chunk.z())) {
            instance.setupVillageStructures();
            for (LOTRVillageGen.StructureInfo struct : instance.structures) {
                int[] coords = instance.getWorldCoords(struct.posX(), struct.posZ());
                int i1 = coords[0];
                int k1 = coords[1];
                if (i1 >> 4 != chunk.x() || k1 >> 4 != chunk.z()) {
                    continue;
                }
                record(planned, generator, level, context, struct.structure(), (world, r) -> {
                    instance.setupWorldPositionSeed(i1, k1);
                    int j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                    // The original's floor: 62, or just under a flatter world's own minimum.
                    if (j1 <= Math.min(62, world.getSeaLevel() - 1)) {
                        return false;
                    }
                    return struct.structure().generateAndFinish(world, instance.instanceRand, i1, j1, k1,
                            instance.getStructureRotation(struct.rotation()));
                }, null);
            }
        }
    }

    /** Builds one structure into the level it is given. */
    public interface Build {
        boolean build(WorldGenLevel world, RandomSource random);
    }

    /** What a biome's planStructures plans with: each structure built by its Build into a blueprint. */
    public interface Planner {
        void plan(Class<?> structure, Build build);
    }

    /** LOTRBiomeDecorator.place: a structure of the older kind with its shape pass. */
    private static boolean build(LOTRWorldGenerator gen, WorldGenLevel world, RandomSource rand, int i, int j, int k) {
        if (gen instanceof LOTRStructureBase structure) {
            return structure.generateAndFinish(world, rand, i, j, k);
        }
        return gen.generate(world, rand, i, j, k);
    }

    /** Builds one structure into a blueprint, and keeps it if it built anything. */
    private static void record(List<Planned> planned, LOTRChunkGenerator generator, ServerLevel level, GenerationContext context,
                               Object structure, Build build, @Nullable RandomSource rand) {
        LOTRBlueprint.Recorder recorder = new LOTRBlueprint.Recorder((x, y, z) -> generator.terrainBlock(x, y, z),
                level, context.biomeSource(), context.randomState().sampler(), context.seed(),
                context.heightAccessor().getMinY(), context.heightAccessor().getHeight(), generator.getSeaLevel());
        String name = (structure instanceof Class<?> c ? c : structure.getClass()).getSimpleName();
        try {
            if (!build.build(recorder.level(), rand != null ? rand : RandomSource.create(context.seed() ^ context.chunkPos().pack()))) {
                return;
            }
            LOTRBlueprint blueprint = recorder.finish();
            if (!blueprint.isEmpty()) {
                planned.add(new Planned(name, blueprint));
            }
        } catch (RuntimeException e) {
            LOTRMod.LOGGER.error("Could not plan {} in chunk {}", name, context.chunkPos(), e);
        }
    }
}
