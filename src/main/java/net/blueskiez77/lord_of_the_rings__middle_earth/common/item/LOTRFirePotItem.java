package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRFirePotEntity;

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
 * LOTRItemFirePot: a clay pot of Khamul's fire, thrown by hand.
 *
 * <p>Stacks to four. onItemRightClick threw a LOTREntityFirePot with the bow's
 * twang at half volume and a low pitch, and a dispenser could throw one too
 * (LOTRDispenseFirePot) -- which ProjectileItem gives a dispenser for free.
 */
public class LOTRFirePotItem extends Item implements ProjectileItem {

    /** LOTREntityFirePot.func_70182_d: it leaves the hand at 1.2. */
    public static final float THROW_VELOCITY = 1.2f;

    public LOTRFirePotItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        if (level instanceof ServerLevel server) {
            Projectile.spawnProjectileFromRotation(
                    (lvl, thrower, pot) -> new LOTRFirePotEntity(LOTREntities.FIRE_POT, thrower, lvl, pot),
                    server, stack, player, 0.0f, THROW_VELOCITY, 1.0f);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new LOTRFirePotEntity(LOTREntities.FIRE_POT, level, pos.x(), pos.y(), pos.z(), stack);
    }
}
