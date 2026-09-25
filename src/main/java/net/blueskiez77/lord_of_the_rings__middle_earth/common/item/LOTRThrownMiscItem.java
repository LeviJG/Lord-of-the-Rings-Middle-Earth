package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.BiFunction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * The three throwables of tabMisc -- the conker, the exploding termite and the
 * mystery web. All three did the same thing in 1.7.10: bow twang, one off the
 * stack, and an EntityThrowable on its way.
 */
public class LOTRThrownMiscItem extends Item implements net.minecraft.world.item.ProjectileItem {
    private final BiFunction<Level, LivingEntity, ThrowableItemProjectile> factory;
    private final float power;
    private final java.util.function.Supplier<? extends net.minecraft.world.entity.EntityType<? extends ThrowableItemProjectile>> type;

    public LOTRThrownMiscItem(BiFunction<Level, LivingEntity, ThrowableItemProjectile> factory, float power,
            java.util.function.Supplier<? extends net.minecraft.world.entity.EntityType<? extends ThrowableItemProjectile>> type,
            Properties properties) {
        super(properties);
        this.factory = factory;
        this.power = power;
        this.type = type;
    }

    /**
     * LOTRDispenseConker, LOTRDispenseMysteryWeb, LOTRDispenseTermite: plain
     * BehaviorProjectileDispense, whose 1.1 power and 6.0 spread are vanilla's
     * dispenser defaults still.
     */
    @Override
    public net.minecraft.world.entity.projectile.Projectile asProjectile(Level level, net.minecraft.core.Position pos,
            ItemStack stack, net.minecraft.core.Direction direction) {
        ThrowableItemProjectile thrown = type.get().create(level, net.minecraft.world.entity.EntitySpawnReason.DISPENSER);
        thrown.setPos(pos.x(), pos.y(), pos.z());
        thrown.setItem(stack.copyWithCount(1));
        return thrown;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        if (level instanceof ServerLevel server) {
            ThrowableItemProjectile thrown = factory.apply(level, player);
            // EntityThrowable's own spread of one degree.
            thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power, 1.0f);
            server.addFreshEntity(thrown);
        }
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
