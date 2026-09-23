package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTRItemManFlesh: food only to those an orc or troll faction counts a friend.
 * Anyone else chokes it down for nothing and is left hungry for thirty seconds.
 */
public class LOTRManFleshItem extends Item {

    public LOTRManFleshItem(Properties properties) {
        super(properties);
    }

    private static boolean orcAligned(Player player) {
        for (LOTRFaction faction : LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC, LOTRFaction.FactionType.TYPE_TROLL)) {
            if (LOTRPlayerAlignments.getAlignment(player, faction) > 0.0f) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && !orcAligned(player)) {
            if (!level.isClientSide()) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP,
                    SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.1f + 0.9f);
            stack.consume(1, player);
            return stack;
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
