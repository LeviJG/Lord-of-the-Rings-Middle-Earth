package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * LOTRItemSauronMace: hold it down for two seconds and everything around you
 * is struck at once -- six damage times a factor that falls off with distance,
 * and a shove to match -- while a ring of smoke goes out from your feet.
 *
 * <p>The original spared Mordor's own: NPCs friendly to Mordor, and players
 * with positive Mordor alignment (or any player at all with PvP off). The port
 * has no NPCs, so only the player half of that test survives.
 */
public class LOTRSauronMaceItem extends LOTRModifiableItem {
    /** getMaxItemUseDuration */
    public static final int USE_DURATION = 40;

    private static final double RANGE_XZ = 12.0;
    private static final double RANGE_Y = 8.0;

    public LOTRSauronMaceItem(Properties properties) {
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

    /** onEaten: two points of wear, then the blow. */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        InteractionHand hand = user.getUsedItemHand();
        stack.hurtAndBreak(2, user, hand);
        user.swing(hand);
        level.playSound(null, user.getX(), user.getY(), user.getZ(), LOTRSounds.ITEM_MACE_SAURON,
                SoundSource.PLAYERS, 2.0f,
                (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (level instanceof ServerLevel server) {
            strike(server, user);
        }
        return stack;
    }

    private static void strike(ServerLevel server, LivingEntity user) {
        DamageSource source = user instanceof Player player
                ? server.damageSources().playerAttack(player)
                : server.damageSources().mobAttack(user);
        for (LivingEntity entity : server.getEntitiesOfClass(LivingEntity.class,
                user.getBoundingBox().inflate(RANGE_XZ, RANGE_Y, RANGE_XZ))) {
            if (entity == user) {
                continue;
            }
            if (entity instanceof Player target
                    && (!server.getGameRules().get(GameRules.PVP)
                            || LOTRPlayerAlignments.getAlignment(target, LOTRFaction.MORDOR) > 0.0f)) {
                continue;
            }
            float strength = Math.max(1.0f, 6.0f - user.distanceTo(entity) * 0.75f);
            entity.hurtServer(server, source, 6.0f * strength);
            float knockback = Math.min(strength, 4.0f);
            entity.push(-Mth.sin(user.getYRot() * Mth.DEG_TO_RAD) * 0.7f * knockback,
                    0.2 + 0.12 * knockback,
                    Mth.cos(user.getYRot() * Mth.DEG_TO_RAD) * 0.7f * knockback);
            entity.hurtMarked = true;
        }
        // LOTRPacketWeaponFX.MACE_SAURON: smoke all the way round, at a radius
        // of one and a half blocks.
        for (int degrees = 0; degrees < 360; degrees += 2) {
            double angle = Math.toRadians(degrees);
            double dx = 1.5 * Math.sin(angle);
            double dz = 1.5 * Math.cos(angle);
            server.sendParticles(ParticleTypes.SMOKE, user.getX() + dx, user.getY() + 0.1, user.getZ() + dz,
                    0, dx * 0.2, 0.0, dz * 0.2, 1.0);
        }
    }
}
