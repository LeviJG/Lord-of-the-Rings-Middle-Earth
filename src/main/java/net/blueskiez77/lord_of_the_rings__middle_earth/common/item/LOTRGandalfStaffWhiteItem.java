package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRGandalfFireballEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

/**
 * LOTRItemGandalfStaffWhite: held down for two seconds, it throws the fireball
 * -- a ghast's shot to the ear, a ring of pale flame underfoot, and two points
 * of wear.
 */
public class LOTRGandalfStaffWhiteItem extends LOTRModifiableItem {
    public static final int USE_DURATION = 40;

    /** EntityThrowable's func_70182_d and its one-degree spread. */
    private static final float SHOT_POWER = 1.5f;
    private static final float SHOT_SPREAD = 1.0f;

    public LOTRGandalfStaffWhiteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        InteractionHand hand = user.getUsedItemHand();
        stack.hurtAndBreak(2, user, hand);
        user.swing(hand);
        level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.GHAST_SHOOT,
                SoundSource.PLAYERS, 2.0f,
                (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (level instanceof ServerLevel server) {
            LOTRGandalfFireballEntity fireball = new LOTRGandalfFireballEntity(
                    LOTREntities.GANDALF_FIREBALL, user, server, new ItemStack(LOTRItems.GANDALF_FIREBALL));
            fireball.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, SHOT_POWER, SHOT_SPREAD);
            server.addFreshEntity(fireball);
            // LOTRPacketWeaponFX.STAFF_GANDALF_WHITE: a ring of blue flame.
            for (int degrees = 0; degrees < 360; degrees += 2) {
                double angle = Math.toRadians(degrees);
                double dx = 1.5 * Math.sin(angle);
                double dz = 1.5 * Math.cos(angle);
                server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, user.getX() + dx, user.getY() + 0.1,
                        user.getZ() + dz, 0, dx * 0.2, 0.0, dz * 0.2, 1.0);
            }
        }
        return stack;
    }
}
