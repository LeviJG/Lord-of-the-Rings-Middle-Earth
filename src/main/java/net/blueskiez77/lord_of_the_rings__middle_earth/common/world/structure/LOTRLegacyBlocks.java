package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.serialization.Dynamic;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWeaponRackBlock;

import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import org.jspecify.annotations.Nullable;

/**
 * The original structures' blocks, as a block and its metadata, in the
 * port's block states.
 *
 * <p>1.7.10's own blocks go through the game's data fixers: their 1.12
 * numeric id and metadata give the 1.13 flattened state (BlockStateData),
 * which the fixers then carry up to today's.
 *
 * <p>The mod's blocks come from {@code lotr/legacy_blocks.tsv}, generated
 * from the parity table: each old field and metadata to the port block that
 * replaced it. The metadata that picked the kind is masked off by the kinds
 * listed; the orientation the rest of it carried is read as the vanilla
 * block of the same shape read it (stairs as oak stairs, slabs as stone
 * slabs, logs and beams as logs, doors, trapdoors, gates, torches, chests,
 * furnace-like blocks, ladders, beds, crops, leaves, buttons, levers), and
 * whichever of those properties the port block has are copied across. The
 * mod's own gate, mugs and weapon racks read their metadata themselves: the
 * gate its direction below 8 and open above; a mug the way it faces; a rack
 * the way it faces, and whether it hangs on a wall (4).
 */
public final class LOTRLegacyBlocks {

    /** The 1.13 data version BlockStateData's flattened states are written at. */
    private static final int FLATTENING_VERSION = 1451;

    private static final Map<String, List<Row>> MOD_ROWS = new HashMap<>();
    private static final Map<String, String> FIELD_BY_UNLOC = new HashMap<>();
    private static final Map<Long, BlockState> VANILLA_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, LegacyBlock> BLOCKS = new ConcurrentHashMap<>();

    private record Row(int meta, Identifier id, Map<String, String> forced) {
    }

    /** One of the original's blocks: give it a metadata for the state it placed. */
    public interface LegacyBlock {
        BlockState state(int meta);

        default BlockState state() {
            return state(0);
        }

        /** {@code world.getBlock(..) == block}: whether this state is this block, at any metadata. */
        default boolean matches(BlockState state) {
            for (int meta = 0; meta < 16; ++meta) {
                if (state.is(state(meta).getBlock())) {
                    return true;
                }
            }
            return false;
        }

        /** {@code new ItemStack(block, count, meta)}. */
        default net.minecraft.world.item.ItemStack stack(int count, int meta) {
            return new net.minecraft.world.item.ItemStack(state(meta).getBlock(), count);
        }
    }

    private LOTRLegacyBlocks() {
    }

    public static void init() {
        try (InputStream in = LOTRLegacyBlocks.class.getResourceAsStream("/lotr/legacy_blocks.tsv")) {
            if (in == null) {
                throw new IllegalStateException("lotr/legacy_blocks.tsv is missing");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] cols = line.split("\t", -1);
                Map<String, String> forced = new HashMap<>();
                if (cols.length > 3 && !cols[3].isEmpty()) {
                    for (String kv : cols[3].split(";")) {
                        String[] pair = kv.split("=");
                        forced.put(pair[0], pair[1]);
                    }
                }
                int meta = cols[1].equals("*") ? -1 : Integer.parseInt(cols[1]);
                MOD_ROWS.computeIfAbsent(cols[0], k -> new ArrayList<>())
                        .add(new Row(meta, Identifier.parse(cols[2]), forced));
                if (cols.length > 4 && !cols[4].isEmpty()) {
                    FIELD_BY_UNLOC.putIfAbsent(cols[4], cols[0]);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read lotr/legacy_blocks.tsv", e);
        }
    }

    /** {@code LOTRMod.<field>}. */
    public static LegacyBlock mod(String field) {
        return BLOCKS.computeIfAbsent("lotr:" + field, k -> {
            List<Row> rows = MOD_ROWS.get(field);
            if (rows == null) {
                LOTRMod.LOGGER.warn("No port block for the original's LOTRMod.{}", field);
                return meta -> Blocks.AIR.defaultBlockState();
            }
            return new ModBlock(rows);
        });
    }

    /**
     * A block by the name a structure scan gives it: 1.7.10's registry name
     * for its own ("dirt"), "lotr:tile." and the unlocalized name for the
     * mod's.
     */
    public static LegacyBlock byScanName(String name) {
        String prefix = "lotr:tile.";
        if (name.startsWith(prefix)) {
            String unloc = name.substring(prefix.length());
            return mod(FIELD_BY_UNLOC.getOrDefault(unloc, unloc));
        }
        return vanilla(name.startsWith("minecraft:") ? name.substring("minecraft:".length()) : name);
    }

    /** {@code Blocks.<name>}: 1.7.10's field names are its registry names. */
    public static LegacyBlock vanilla(String name) {
        return BLOCKS.computeIfAbsent("minecraft:" + name, k -> {
            int base = BlockStateData.ID_BY_OLD_NAME.getInt("minecraft:" + name);
            if (base < 0) {
                LOTRMod.LOGGER.warn("Unknown 1.7.10 block minecraft:{}", name);
                return meta -> Blocks.AIR.defaultBlockState();
            }
            int blockId = base >> 4;
            return meta -> vanillaState(blockId, meta);
        });
    }

    /** A 1.12 numeric block id and metadata, as today's state. */
    public static BlockState vanillaState(int blockId, int meta) {
        return VANILLA_CACHE.computeIfAbsent(((long) blockId << 4) | (meta & 15), key -> {
            Dynamic<?> flattened = BlockStateData.getTag((blockId << 4) | (meta & 15));
            Dynamic<Tag> tag = flattened.convert(NbtOps.INSTANCE);
            Dynamic<Tag> fixed = DataFixers.getDataFixer().update(References.BLOCK_STATE, tag, FLATTENING_VERSION,
                    SharedConstants.getCurrentVersion().dataVersion().version());
            return BlockState.CODEC.parse(fixed).result().orElseGet(Blocks.AIR::defaultBlockState);
        });
    }

    private static final class ModBlock implements LegacyBlock {

        private final List<Row> rows;
        private final int mask;
        private final Map<Integer, BlockState> cache = new ConcurrentHashMap<>();

        ModBlock(List<Row> rows) {
            this.rows = rows;
            int max = 0;
            boolean any = false;
            for (Row row : rows) {
                if (row.meta < 0) {
                    any = true;
                }
                max = Math.max(max, row.meta);
            }
            this.mask = any ? 0 : Integer.highestOneBit(Math.max(max, 1)) * 2 - 1;
        }

        @Override
        public BlockState state(int meta) {
            return this.cache.computeIfAbsent(meta & 15, this::resolve);
        }

        private BlockState resolve(int meta) {
            Row row = null;
            for (Row r : this.rows) {
                if (r.meta < 0 || r.meta == (meta & this.mask)) {
                    row = r;
                    break;
                }
            }
            if (row == null) {
                row = this.rows.getFirst();
            }
            Block block = BuiltInRegistries.BLOCK.getValue(row.id);
            BlockState state = orient(block, meta);
            for (Map.Entry<String, String> e : row.forced.entrySet()) {
                state = withNamed(state, e.getKey(), e.getValue());
            }
            return state;
        }
    }

    /** The orientation the metadata carried, as the vanilla block of the same shape read it. */
    private static BlockState orient(Block block, int meta) {
        BlockState state = block.defaultBlockState();
        if (block instanceof LOTRGateBlock) {
            return state.setValue(LOTRGateBlock.FACING, Direction.from3DDataValue(meta & 7))
                    .setValue(LOTRGateBlock.OPEN, (meta & 8) != 0);
        }
        if (block instanceof LOTRMugBlock) {
            return state.setValue(LOTRMugBlock.FACING, Direction.from2DDataValue(meta & 3));
        }
        if (block instanceof LOTRWeaponRackBlock) {
            return state.setValue(LOTRWeaponRackBlock.FACING, Direction.from2DDataValue(meta & 3))
                    .setValue(LOTRWeaponRackBlock.ON_WALL, (meta & 4) != 0);
        }
        String analogue = analogue(block);
        if (analogue == null) {
            return state;
        }
        BlockState vanilla = vanilla(analogue).state(meta);
        if (block instanceof TorchBlock && vanilla.is(Blocks.WALL_TORCH)) {
            Block wall = block.asItem() instanceof StandingAndWallBlockItem item ? wallOf(item) : null;
            if (wall != null) {
                state = wall.defaultBlockState();
            }
        }
        for (Property<?> property : vanilla.getProperties()) {
            state = copy(vanilla, state, property);
        }
        return state;
    }

    private static @Nullable Block wallOf(StandingAndWallBlockItem item) {
        return item.wallBlock;
    }

    private static @Nullable String analogue(Block block) {
        if (block instanceof StairBlock) {
            return "oak_stairs";
        }
        if (block instanceof SlabBlock) {
            return "stone_slab";
        }
        if (block instanceof RotatedPillarBlock) {
            return "log";
        }
        if (block instanceof DoorBlock) {
            return "wooden_door";
        }
        if (block instanceof TrapDoorBlock) {
            return "trapdoor";
        }
        if (block instanceof FenceGateBlock) {
            return "fence_gate";
        }
        if (block instanceof LeavesBlock) {
            return "leaves";
        }
        if (block instanceof LadderBlock) {
            return "ladder";
        }
        if (block instanceof ChestBlock) {
            return "chest";
        }
        if (block instanceof TorchBlock) {
            return "torch";
        }
        if (block instanceof BedBlock) {
            return "bed";
        }
        if (block instanceof CropBlock) {
            return "wheat";
        }
        if (block instanceof ButtonBlock) {
            return "stone_button";
        }
        if (block instanceof LeverBlock) {
            return "lever";
        }
        if (block instanceof HorizontalDirectionalBlock
                || block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return "furnace";
        }
        return null;
    }

    /** One property, by name, from the vanilla state onto the port's, where it has one that takes the value. */
    private static <T extends Comparable<T>> BlockState copy(BlockState from, BlockState to, Property<T> property) {
        String value = property.getName(from.getValue(property));
        return withNamed(to, property.getName(), value);
    }

    private static BlockState withNamed(BlockState state, String name, String value) {
        Property<?> target = state.getBlock().getStateDefinition().getProperty(name);
        if (target == null) {
            return state;
        }
        return withValue(state, target, value);
    }

    private static <T extends Comparable<T>> BlockState withValue(BlockState state, Property<T> property, String value) {
        if (property instanceof IntegerProperty ints) {
            try {
                int v = Integer.parseInt(value);
                int max = ints.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElse(v);
                value = Integer.toString(Math.min(v, max));
            } catch (NumberFormatException ignored) {
            }
        }
        return property.getValue(value).map(v -> state.setValue(property, v)).orElse(state);
    }
}
