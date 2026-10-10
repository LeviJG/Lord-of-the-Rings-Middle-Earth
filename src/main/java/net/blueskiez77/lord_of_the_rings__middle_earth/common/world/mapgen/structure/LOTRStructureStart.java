package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** 1.7.10's StructureStart: one structure's pieces, laid out from the chunk it started in. */
public class LOTRStructureStart {
    public final List<LOTRStructureComponent> components = new ArrayList<>();
    public LOTRStructureBoundingBox boundingBox;
    public int chunkX;
    public int chunkZ;

    public LOTRStructureStart(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /** updateBoundingBox: the box about all its pieces. */
    public void updateBoundingBox() {
        this.boundingBox = LOTRStructureBoundingBox.getNewBoundingBox();
        for (LOTRStructureComponent component : this.components) {
            this.boundingBox.expandTo(component.getBoundingBox());
        }
    }

    /** generateStructure: each piece in the box built; those that will not build are dropped. */
    public void generateStructure(WorldGenLevel world, RandomSource random, LOTRStructureBoundingBox sbb) {
        Iterator<LOTRStructureComponent> iterator = this.components.iterator();
        while (iterator.hasNext()) {
            LOTRStructureComponent component = iterator.next();
            if (component.getBoundingBox().intersectsWith(sbb) && !component.addComponentParts(world, random, sbb)) {
                iterator.remove();
            }
        }
    }

    /** isSizeableStructure: whether it has anything to build. */
    public boolean isSizeableStructure() {
        return true;
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ChunkX", this.chunkX);
        nbt.putInt("ChunkZ", this.chunkZ);
        if (this.boundingBox != null) {
            nbt.putIntArray("BB", this.boundingBox.toArray());
        }
        ListTag children = new ListTag();
        for (LOTRStructureComponent component : this.components) {
            children.add(component.save());
        }
        nbt.put("Children", children);
        return nbt;
    }

    public static LOTRStructureStart load(CompoundTag nbt, Map<String, Supplier<? extends LOTRStructureComponent>> componentTypes) {
        LOTRStructureStart start = new LOTRStructureStart(nbt.getIntOr("ChunkX", 0), nbt.getIntOr("ChunkZ", 0));
        nbt.getIntArray("BB").ifPresent(bb -> start.boundingBox = new LOTRStructureBoundingBox(bb));
        for (Tag tag : nbt.getListOrEmpty("Children")) {
            if (tag instanceof CompoundTag child) {
                Supplier<? extends LOTRStructureComponent> type = componentTypes.get(child.getStringOr("id", ""));
                if (type != null) {
                    LOTRStructureComponent component = type.get();
                    component.load(child);
                    start.components.add(component);
                }
            }
        }
        if (start.boundingBox == null) {
            start.updateBoundingBox();
        }
        return start;
    }
}
