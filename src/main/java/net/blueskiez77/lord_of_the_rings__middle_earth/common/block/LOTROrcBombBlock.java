package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTROrcBombEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockOrcBomb: a keg of orc powder that goes off when something lights it.
 *
 * <p>The original packed the strength into metadata -- the low three bits, 0
 * through 2 -- and read it back when priming. Metadata is gone, so each strength
 * is its own block, and {@link #strengthLevel} is that number. The explosion is
 * {@code (strength + 1) * 4}, so 4, 8 and 12; the fuse is
 * {@code 40 + strength * 20} ticks. Both live on the entity.
 *
 * <p>What lights it is the point of the thing, and the original was strict:
 * onBlockActivated primed it ONLY when the held item was the orc torch. Flint
 * and steel, fire charges and burning arrows do nothing at all -- there is no
 * onBlockDestroyedByExplosion path for them and no ignite hook. That is kept
 * exactly. The other two ways in are redstone power and another explosion
 * setting it off, both of which the original had.
 *
 * <p>The fire variant is metadata bit 8, {@link #isFireBomb}: the same keg in
 * different colours whose blast leaves fires behind. Crossed with the three
 * strengths that is six blocks, which is exactly the six entries
 * LOTRBlockOrcBomb.getSubBlocks offered.
 */
public class LOTROrcBombBlock extends Block {
    public static final MapCodec<LOTROrcBombBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("strength_level").forGetter(LOTROrcBombBlock::getStrengthLevel),
                    Codec.BOOL.fieldOf("fire").forGetter(LOTROrcBombBlock::isFireBomb),
                    propertiesCodec()
            ).apply(instance, LOTROrcBombBlock::new));

    /** LOTRBlockOrcBomb.getBombStrengthLevel: 0 plain, 1 double, 2 triple. */
    private final int strengthLevel;

    /** LOTRBlockOrcBomb.isFireBomb: metadata bit 8, which made the blast set fires. */
    private final boolean fire;

    public LOTROrcBombBlock(int strengthLevel, boolean fire, Properties properties) {
        super(properties);
        this.strengthLevel = strengthLevel;
        this.fire = fire;
    }

    public int getStrengthLevel() {
        return this.strengthLevel;
    }

    public boolean isFireBomb() {
        return this.fire;
    }

    @Override
    protected MapCodec<LOTROrcBombBlock> codec() {
        return CODEC;
    }

    /** canDropFromExplosion: it never drops as an item, it goes off instead. */
    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    /**
     * onBlockActivated: an orc torch and nothing else. Held in either hand --
     * 1.7.10 had only the one, so this is the only reading that makes sense now.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (!held.is(LOTRBlocks.ORC_TORCH.asItem())) {
            return InteractionResult.PASS;
        }
        prime(level, pos, player);
        return InteractionResult.SUCCESS;
    }

    /** onBlockAdded: placing one into a live redstone signal lights it at once. */
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.hasNeighborSignal(pos)) {
            prime(level, pos, null);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbourBlock,
            Orientation orientation, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            prime(level, pos, null);
        }
    }

    /**
     * onBlockExploded: a bomb caught in someone else's blast goes off too, on a
     * shortened fuse so a stack of them chains rather than detonating as one.
     */
    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion,
            java.util.function.BiConsumer<ItemStack, BlockPos> dropConsumer) {
        LOTROrcBombEntity bomb = new LOTROrcBombEntity(LOTREntities.ORC_BOMB, level,
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                explosion.getIndirectSourceEntity(), state);
        bomb.setFuseFromExplosion();
        level.addFreshEntity(bomb);
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    /**
     * onBlockDestroyedByPlayer with meta -1: swap the block for a primed bomb
     * and let the fuse run.
     */
    private void prime(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
        BlockState state = level.getBlockState(pos);
        level.removeBlock(pos, false);
        if (level.isClientSide()) {
            return;
        }
        // pos.getY(), not pos.getY() + 0.5. The original's j + 0.5f was 1.7.10's
        // centre-origin convention -- posY was the middle of the entity, and
        // yOffset put its feet back on the block floor. A modern entity's
        // position IS its feet, so the half block was a real half block: the
        // bomb appeared hanging and then dropped. Vanilla's TntBlock spawns at
        // pos.getY() for the same reason, and so does onExplosionHit below.
        LOTROrcBombEntity bomb = new LOTROrcBombEntity(LOTREntities.ORC_BOMB, level,
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter, state);
        level.addFreshEntity(bomb);
        level.playSound(null, bomb.getX(), bomb.getY(), bomb.getZ(),
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
}
