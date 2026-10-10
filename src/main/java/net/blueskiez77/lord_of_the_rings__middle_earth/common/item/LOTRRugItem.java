package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRRugBaseEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRItemRugBase: puts the rug down where it was clicked, facing the player,
 * on anything with a solid top -- as long as nothing is in the way and it is
 * not in a liquid. One item per rug now; the original told them apart by
 * damage value.
 */
public class LOTRRugItem extends Item {

    private final Function<Level, LOTRRugBaseEntity> factory;

    public LOTRRugItem(Properties properties, Function<Level, LOTRRugBaseEntity> factory) {
        super(properties);
        this.factory = factory;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockState state = level.getBlockState(clicked);
        BlockPos pos = clicked;
        if (!state.is(Blocks.SNOW) && !state.canBeReplaced()) {
            pos = clicked.relative(face);
        }
        if (player == null || !player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
            return InteractionResult.FAIL;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        // The click's position within the block it was on, carried to the new spot.
        Vec3 hit = context.getClickLocation();
        LOTRRugBaseEntity rug = this.factory.apply(level);
        rug.placeAt(pos.getX() + (hit.x - clicked.getX()), pos.getY(), pos.getZ() + (hit.z - clicked.getZ()), player);
        AABB box = rug.getBoundingBox();
        if (level.getEntities(rug, box, e -> e.blocksBuilding).isEmpty() && level.noCollision(rug, box)
                && !level.containsAnyLiquid(box)) {
            level.addFreshEntity(rug);
            SoundType wool = SoundType.WOOL;
            rug.playSound(wool.getPlaceSound(), (wool.getVolume() + 1.0f) / 2.0f, wool.getPitch() * 0.8f);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        rug.discard();
        return InteractionResult.FAIL;
    }
}
