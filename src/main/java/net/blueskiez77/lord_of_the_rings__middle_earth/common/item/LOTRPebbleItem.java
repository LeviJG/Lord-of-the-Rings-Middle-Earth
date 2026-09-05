package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPebbleEntity;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

/**
 * LOTRItemPebble: a stone you can throw, and the sling's ammunition.
 *
 * <p>Thrown by hand it does one point of damage and lands where it fell, ready
 * to be picked up. It is also what {@link LOTRSlingItem} spends, which is the
 * only reason to carry a stack of them.
 *
 * <p>ProjectileItem so that a dispenser can throw one, which is what
 * LOTRDispensePebble did; DispenserBlock is told about it in LOTRMod.
 */
public class LOTRPebbleItem extends Item implements ProjectileItem {

    public LOTRPebbleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // "random.bow" at half volume and a low pitch -- a stone leaving a
        // hand, not a bowstring.
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5f,
                0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

        if (level instanceof ServerLevel server) {
            // spawnProjectileFromRotation, NOT spawnProjectileUsingShoot: the
            // latter's three doubles are a direction VECTOR, and feeding it the
            // player's pitch and yaw sent the stone off along whatever line
            // those two numbers happened to describe. This overload takes the
            // shooter and reads the look direction itself.
            Projectile.spawnProjectileFromRotation(
                    (lvl, shooter, ammo) -> new LOTRPebbleEntity(
                            LOTREntities.PEBBLE, shooter, lvl, ammo.copyWithCount(1)),
                    server, stack, player, 0.0f, LOTRPebbleEntity.THROW_VELOCITY, 1.0f);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new LOTRPebbleEntity(LOTREntities.PEBBLE, level,
                pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
    }
}
