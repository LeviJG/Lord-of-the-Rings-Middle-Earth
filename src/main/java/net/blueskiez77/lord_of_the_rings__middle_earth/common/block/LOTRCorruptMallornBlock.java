package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCorruptMallornBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRMallornEntEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockCorruptMallorn: a mallorn sapling corrupted by gulduril, which the
 * Ents come to heal (LOTREntityAIEntHealSapling), and which keeps count, in
 * its metadata, of the Ents slain by players beside it.
 *
 * <p>NOT ported yet: its tile entity's spawning of Ents in Fangorn, with the
 * biomes (D10).
 */
public class LOTRCorruptMallornBlock extends LOTRPlantBlock implements EntityBlock {

    public static final MapCodec<LOTRCorruptMallornBlock> CODEC = simpleCodec(LOTRCorruptMallornBlock::new);

    /** ENT_KILLS: the kills that summon the Mallorn Ent. */
    public static final int ENT_KILLS = 3;
    /** The metadata: Ents slain beside it so far. */
    public static final IntegerProperty KILLS = IntegerProperty.create("ent_kills", 0, ENT_KILLS - 1);

    public LOTRCorruptMallornBlock(Properties properties) {
        super(Shape.GRASS, Ground.SOIL, properties);
        registerDefaultState(defaultBlockState().setValue(KILLS, 0));
    }

    /** summonEntBoss: the sapling gone, the Mallorn Ent rises in its place and speaks. */
    public static void summonEntBoss(ServerLevel level, BlockPos pos) {
        level.removeBlock(pos, false);
        LOTRMallornEntEntity ent = LOTREntities.MALLORN_ENT.create(level, EntitySpawnReason.TRIGGERED);
        if (ent == null) {
            return;
        }
        ent.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.getRandom().nextFloat() * 360.0f, 0.0f);
        ent.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.TRIGGERED, null);
        level.addFreshEntity(ent);
        ent.sendEntBossSpeech("summon");
    }

    @Override
    public MapCodec<LOTRCorruptMallornBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(KILLS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRCorruptMallornBlockEntity(pos, state);
    }
}
