package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPlateEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemPlate: placed on a block it is a plate; thrown it is a frisbee.
 *
 * <p>onItemRightClick throws it at 1.5 with the bow sound; a dispenser throws
 * it too (LOTRDispensePlate). isValidArmor let it go in the helmet slot, which
 * is the head-slot equippable here -- not swapped on by right-clicking, since
 * right-clicking throws it.
 */
public class LOTRPlateItem extends BlockItem implements ProjectileItem {
    public LOTRPlateItem(Block block, Properties properties) {
        super(block, properties.component(DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).setDispensable(false).build()));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
                SoundSource.NEUTRAL, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + 0.25f);
        if (level instanceof ServerLevel server) {
            Projectile.spawnProjectileFromRotation(
                    (serverLevel, owner, thrown) -> new LOTRPlateEntity(LOTREntities.PLATE, owner, serverLevel, thrown),
                    server, stack, player, 0.0f, LOTRPlateEntity.SPEED, 1.0f);
        }
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new LOTRPlateEntity(LOTREntities.PLATE, level, position.x(), position.y(), position.z(),
                stack.copyWithCount(1));
    }
}
