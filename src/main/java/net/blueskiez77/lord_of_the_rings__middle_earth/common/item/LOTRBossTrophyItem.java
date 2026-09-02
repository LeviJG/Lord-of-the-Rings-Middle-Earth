package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBossTrophyEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * LOTRItemBossTrophy: sets a boss trophy down, or hangs it on a wall.
 *
 * <p>The original was one item with a damage value per trophy; each is its own
 * item here, and {@link #type} is what that damage value was.
 *
 * <p>onItemUse had two paths, and both are kept. Clicking a floor puts the
 * trophy in the middle of the block above, facing whoever placed it. Clicking a
 * side hangs it on that wall, in the block the face points into, turned to
 * match. Clicking the underside of a block does nothing at all.
 */
public class LOTRBossTrophyItem extends Item {

    private final LOTRTrophyType type;

    public LOTRBossTrophyItem(LOTRTrophyType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public LOTRTrophyType getTrophyType() {
        return this.type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        BlockState clickedState = level.getBlockState(clicked);
        BlockPos pos = clicked;
        if (clickedState.canBeReplaced()) {
            // Standing in snow or tall grass: the trophy takes its place.
            face = Direction.UP;
        } else if (face == Direction.UP) {
            pos = clicked.above();
        }

        // "if (side == 0) return false": nothing hangs from a ceiling.
        if (face == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        if (player == null || !player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }

        boolean hanging = face != Direction.UP;
        if (hanging) {
            pos = clicked.relative(face);
        } else {
            BlockPos below = pos.below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                return InteractionResult.FAIL;
            }
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        LOTRBossTrophyEntity trophy =
                new LOTRBossTrophyEntity(LOTREntities.BOSS_TROPHY, level);
        trophy.setTrophyHanging(hanging);
        if (hanging) {
            trophy.setTrophyFacing(face);
            trophy.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    face.toYRot(), 0.0f);
        } else {
            trophy.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    180.0f - player.getYRot() % 360.0f, 0.0f);
        }

        AABB box = trophy.getBoundingBox();
        boolean clear = level.noCollision(trophy, box) && !level.containsAnyLiquid(box);
        if (!clear || (hanging && !trophy.hangingOnValidSurface())) {
            trophy.discard();
            return InteractionResult.FAIL;
        }

        trophy.setTrophyType(this.type);
        level.addFreshEntity(trophy);
        level.playSound(null, trophy.getX(), trophy.getY(), trophy.getZ(),
                Blocks.STONE.defaultBlockState().getSoundType().getBreakSound(),
                SoundSource.BLOCKS,
                (Blocks.STONE.defaultBlockState().getSoundType().getVolume() + 1.0f) / 2.0f,
                Blocks.STONE.defaultBlockState().getSoundType().getPitch() * 0.8f);
        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
