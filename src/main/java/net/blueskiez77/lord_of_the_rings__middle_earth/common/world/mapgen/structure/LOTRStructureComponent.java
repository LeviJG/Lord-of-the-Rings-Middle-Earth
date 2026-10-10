package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * 1.7.10's StructureComponent: one piece of a structure laid out in pieces (a mine corridor, a
 * pyramid), with its box, its facing and its depth in the layout, placing its blocks only within
 * the part of the world being built at the time.
 */
public abstract class LOTRStructureComponent {
    public LOTRStructureBoundingBox boundingBox;
    public int coordBaseMode;
    public int componentType;

    protected LOTRStructureComponent() {
    }

    protected LOTRStructureComponent(int type) {
        this.componentType = type;
        this.coordBaseMode = -1;
    }

    /** The name it is saved under. */
    public abstract String getId();

    public abstract boolean addComponentParts(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox sbb);

    public void buildComponent(LOTRStructureComponent component, List<LOTRStructureComponent> list, RandomSource random) {
    }

    /** func_143012_a: its own fields, saved. */
    protected abstract void writeExtra(CompoundTag nbt);

    /** func_143011_b: its own fields, loaded. */
    protected abstract void readExtra(CompoundTag nbt);

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", getId());
        nbt.putIntArray("BB", this.boundingBox.toArray());
        nbt.putInt("O", this.coordBaseMode);
        nbt.putInt("GD", this.componentType);
        writeExtra(nbt);
        return nbt;
    }

    public void load(CompoundTag nbt) {
        this.boundingBox = new LOTRStructureBoundingBox(nbt.getIntArray("BB").orElseThrow());
        this.coordBaseMode = nbt.getIntOr("O", -1);
        this.componentType = nbt.getIntOr("GD", 0);
        readExtra(nbt);
    }

    public LOTRStructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public int getComponentType() {
        return this.componentType;
    }

    public static @Nullable LOTRStructureComponent findIntersecting(List<LOTRStructureComponent> list, LOTRStructureBoundingBox bb) {
        for (LOTRStructureComponent component : list) {
            if (component.getBoundingBox() != null && component.getBoundingBox().intersectsWith(bb)) {
                return component;
            }
        }
        return null;
    }

    private static boolean isLiquid(WorldGenLevel world, int i, int j, int k) {
        return !world.getFluidState(new BlockPos(i, j, k)).isEmpty();
    }

    /** isLiquidInStructureBoundingBox: liquid anywhere on the faces of the box just outside it. */
    protected boolean isLiquidInStructureBoundingBox(WorldGenLevel world, LOTRStructureBoundingBox bb) {
        int i = Math.max(this.boundingBox.minX - 1, bb.minX);
        int j = Math.max(this.boundingBox.minY - 1, bb.minY);
        int k = Math.max(this.boundingBox.minZ - 1, bb.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, bb.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, bb.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, bb.maxZ);
        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = k; l1 <= j1; ++l1) {
                if (isLiquid(world, k1, j, l1) || isLiquid(world, k1, i1, l1)) {
                    return true;
                }
            }
        }
        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                if (isLiquid(world, k1, l1, k) || isLiquid(world, k1, l1, j1)) {
                    return true;
                }
            }
        }
        for (int k1 = k; k1 <= j1; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                if (isLiquid(world, i, l1, k1) || isLiquid(world, l, l1, k1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** {@code block.getMaterial().isReplaceable() || block.getMaterial() == Material.sand}: air, plants, liquids, sand and gravel. */
    protected static boolean isReplaceableOrSand(BlockState block) {
        return block.canBeReplaced() || block.getBlock() instanceof net.minecraft.world.level.block.FallingBlock
                || block.is(Blocks.SOUL_SAND);
    }

    protected int getXWithOffset(int x, int z) {
        return switch (this.coordBaseMode) {
            case 0, 2 -> this.boundingBox.minX + x;
            case 1 -> this.boundingBox.maxX - z;
            case 3 -> this.boundingBox.minX + z;
            default -> x;
        };
    }

    protected int getYWithOffset(int y) {
        return this.coordBaseMode == -1 ? y : y + this.boundingBox.minY;
    }

    protected int getZWithOffset(int x, int z) {
        return switch (this.coordBaseMode) {
            case 0 -> this.boundingBox.minZ + z;
            case 1, 3 -> this.boundingBox.minZ + x;
            case 2 -> this.boundingBox.maxZ - z;
            default -> z;
        };
    }

    protected void placeBlockAtCurrentPosition(WorldGenLevel world, LegacyBlock block, int meta, int x, int y, int z, LOTRStructureBoundingBox sbb) {
        int i1 = getXWithOffset(x, z);
        int j1 = getYWithOffset(y);
        int k1 = getZWithOffset(x, z);
        if (sbb.isVecInside(i1, j1, k1)) {
            world.setBlock(new BlockPos(i1, j1, k1), block.state(meta), Block.UPDATE_CLIENTS);
        }
    }

    protected BlockState getBlockAtCurrentPosition(WorldGenLevel world, int x, int y, int z, LOTRStructureBoundingBox sbb) {
        int i1 = getXWithOffset(x, z);
        int j1 = getYWithOffset(y);
        int k1 = getZWithOffset(x, z);
        return !sbb.isVecInside(i1, j1, k1) ? Blocks.AIR.defaultBlockState() : world.getBlockState(new BlockPos(i1, j1, k1));
    }

    /** fillWithBlocks: the box's shell of one block and its inside of another, only over non-air if asked. */
    protected void fillWithBlocks(WorldGenLevel world, LOTRStructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY,
                                  int maxZ, LegacyBlock placeBlock, LegacyBlock replaceBlock, boolean alwaysReplace) {
        fillWithMetadataBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, placeBlock, 0, replaceBlock, 0, alwaysReplace);
    }

    protected void fillWithMetadataBlocks(WorldGenLevel world, LOTRStructureBoundingBox sbb, int minX, int minY, int minZ, int maxX,
                                          int maxY, int maxZ, LegacyBlock placeBlock, int placeMeta, LegacyBlock replaceBlock,
                                          int replaceMeta, boolean alwaysReplace) {
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (alwaysReplace && getBlockAtCurrentPosition(world, x, y, z, sbb).isAir()) {
                        continue;
                    }
                    if (y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ) {
                        placeBlockAtCurrentPosition(world, replaceBlock, replaceMeta, x, y, z, sbb);
                    } else {
                        placeBlockAtCurrentPosition(world, placeBlock, placeMeta, x, y, z, sbb);
                    }
                }
            }
        }
    }

    /**
     * generateStructureChestContents: a chest, filled as 1.7.10's WeightedRandomChestContent filled one
     * -- the pool's entries alone, without the mod's pouches, lore or modifiers.
     */
    protected boolean generateStructureChestContents(WorldGenLevel world, LOTRStructureBoundingBox sbb, RandomSource random,
                                                     int x, int y, int z, LOTRChestContents.Pool items, int count) {
        int i1 = getXWithOffset(x, z);
        int j1 = getYWithOffset(y);
        int k1 = getZWithOffset(x, z);
        BlockPos pos = new BlockPos(i1, j1, k1);
        if (sbb.isVecInside(i1, j1, k1) && !world.getBlockState(pos).is(Blocks.CHEST)) {
            world.setBlock(pos, LOTRLegacyBlocks.vanilla("chest").state(0), Block.UPDATE_CLIENTS);
            if (world.getBlockEntity(pos) instanceof Container chest) {
                LOTRChestContents.fillInventoryPlain(chest, random, items, count);
            }
            return true;
        }
        return false;
    }
}
