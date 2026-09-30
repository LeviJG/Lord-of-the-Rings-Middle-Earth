package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * LOTREntityZebra: a horse in stripes, with its own calls, that gives milk to
 * a bucket (but not in creative, as the original checked). Drops are
 * {@code lotr:entities/zebra}: zero to one leather and one to two zebra meat,
 * each plus looting.
 */
public class LOTRZebraEntity extends LOTRHorseEntity {

    public LOTRZebraEntity(EntityType<? extends LOTRZebraEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.BUCKET) && !player.isCreative()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.MILK_BUCKET)));
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.ZEBRA_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.ZEBRA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.ZEBRA_DEATH;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return LOTRSounds.ZEBRA_HURT;
    }
}
