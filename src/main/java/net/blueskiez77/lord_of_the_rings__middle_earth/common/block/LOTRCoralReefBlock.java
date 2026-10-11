package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockCoralReef: rock, with one of five corals growing from its top (the
 * block models, picked by position as getRandomPlantIcon picked by a hash of
 * it). It gives up one or two pieces of coral (more with Fortune; itself with
 * Silk Touch) and nought to two experience, and it scrapes anything that walks
 * on it but sea creatures, half a heart as a cactus does.
 *
 * <p>It lets light through: its coral is drawn in the water above it, lit by
 * the block itself, which a solid cube would leave dark.
 */
public class LOTRCoralReefBlock extends DropExperienceBlock {

    public static final MapCodec<LOTRCoralReefBlock> CODEC = simpleCodec(LOTRCoralReefBlock::new);

    public LOTRCoralReefBlock(Properties properties) {
        super(UniformInt.of(0, 2), properties);
    }

    @Override
    public MapCodec<LOTRCoralReefBlock> codec() {
        return CODEC;
    }

    /** onEntityWalking. */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity && !(entity instanceof WaterAnimal)) {
            entity.hurtServer(serverLevel, level.damageSources().cactus(), 0.5f);
        }
        super.stepOn(level, pos, state, entity);
    }
}
