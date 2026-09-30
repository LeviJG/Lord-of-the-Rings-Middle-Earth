package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRItemNPCRespawner: in creative only, sets an NPC respawner against the
 * face used, or in the air at the end of the player's reach -- where there is
 * room and none is already.
 */
public class LOTRNPCRespawnerItem extends Item {

    public LOTRNPCRespawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getAbilities().instabuild && !level.isClientSide()) {
            double range = player.blockInteractionRange();
            Vec3 target = player.getEyePosition().add(player.getLookAngle().scale(range));
            placeSpawnerAt(level, BlockPos.containing(target));
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            return InteractionResult.FAIL;
        }
        if (!context.getLevel().isClientSide()) {
            placeSpawnerAt(context.getLevel(), context.getClickedPos().relative(context.getClickedFace()));
        }
        return InteractionResult.SUCCESS;
    }

    public static void placeSpawnerAt(Level level, BlockPos pos) {
        double f = 0.1;
        AABB inside = new AABB(pos.getX() + f, pos.getY() + f, pos.getZ() + f,
                pos.getX() + 1 - f, pos.getY() + 1 - f, pos.getZ() + 1 - f);
        if (!level.getEntitiesOfClass(LOTRNPCRespawnerEntity.class, inside).isEmpty()) {
            return;
        }
        LOTRNPCRespawnerEntity spawner = new LOTRNPCRespawnerEntity(level);
        spawner.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
        if (level.noCollision(spawner, spawner.getBoundingBox().deflate(0.01))) {
            level.addFreshEntity(spawner);
        }
    }
}
