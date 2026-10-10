package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemMorgulShroom: the shroom, planted on a block or eaten from the hand (while hungry, over 32
 * ticks). It feeds one who stands well with Mordor (4 hunger, 0.4 saturation) and poisons anyone
 * else for four seconds; either way it earns eatMorgulShroom.
 */
public class LOTRMorgulShroomItem extends BlockItem {

    public LOTRMorgulShroomItem(Block block, Properties properties) {
        super(block, properties.component(DataComponents.CONSUMABLE, Consumables.defaultFood().build()));
    }

    /** onItemRightClick: eaten only while the player can eat. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR) > 0.0f) {
                player.getFoodData().eat(4, 0.4f);
            } else if (!level.isClientSide()) {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 80));
            }
            if (!level.isClientSide()) {
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.EAT_MORGUL_SHROOM);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP,
                    SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.1f + 0.9f);
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
