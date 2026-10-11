package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * One of the original's structures as a piece of today's: its blueprint, placed a chunk at a time
 * as each chunk it stands in is decorated.
 */
public class LOTRBlueprintPiece extends StructurePiece {

    private final String name;
    private final LOTRBlueprint blueprint;

    public LOTRBlueprintPiece(String name, LOTRBlueprint blueprint, BoundingBox box) {
        super(LOTRLegacyStructure.PIECE_TYPE, 0, box);
        this.name = name;
        this.blueprint = blueprint;
    }

    public LOTRBlueprintPiece(CompoundTag tag) {
        super(LOTRLegacyStructure.PIECE_TYPE, tag);
        this.name = tag.getStringOr("Name", "");
        this.blueprint = LOTRBlueprint.load(tag);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("Name", this.name);
        this.blueprint.save(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random,
                            BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        this.blueprint.place(level, chunkBox);
    }
}
