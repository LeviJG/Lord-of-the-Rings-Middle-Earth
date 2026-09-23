package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPoisonedDrinks. A full drink can have a bottle of poison stirred in --
 * in the crafting grid, into a placed mug, or into a barrel -- and whoever
 * drinks it takes the killing poison for fifteen seconds. Only the poisoner
 * (and creative players) can see from the tooltip that anything is wrong.
 *
 * <p>The original applied the poison from a PlayerUseItemEvent.Finish hook.
 * Here the effect rides on the drink's own CONSUMABLE component instead, so it
 * lands however the drink is finished: from the hand, from a placed mug, or as
 * a vanilla water bottle.
 */
public final class LOTRPoisonedDrinks {
    /** addPoisonEffect: duration 300. */
    public static final int DURATION = 300;

    private LOTRPoisonedDrinks() {
    }

    /** canPoison: any full drink, vanilla's water bottle included. */
    public static boolean canPoison(ItemStack stack) {
        return !stack.isEmpty() && LOTRVessel.isFullDrink(stack);
    }

    public static boolean isPoisoned(ItemStack stack) {
        return stack.getOrDefault(LOTRDataComponents.POISON_DRINK, false);
    }

    /** setDrinkPoisoned(stack, true) and setPoisonerPlayer. */
    public static void poison(ItemStack stack, @Nullable Player poisoner) {
        stack.set(LOTRDataComponents.POISON_DRINK, true);
        if (poisoner != null) {
            stack.set(LOTRDataComponents.POISONER, poisoner.getUUID());
        }
        addEffectToConsumable(stack);
    }

    /**
     * A drink moved into a different item -- water poured from a bottle into a
     * mug, or back -- keeps its poison.
     */
    public static ItemStack copyPoison(ItemStack from, ItemStack to) {
        if (isPoisoned(from)) {
            to.set(LOTRDataComponents.POISON_DRINK, true);
            UUID poisoner = from.get(LOTRDataComponents.POISONER);
            if (poisoner != null) {
                to.set(LOTRDataComponents.POISONER, poisoner);
            }
            addEffectToConsumable(to);
        }
        return to;
    }

    /** canPlayerSeePoisoned: nobody named means everybody can tell. */
    public static boolean canPlayerSeePoisoned(ItemStack stack, Player player) {
        UUID poisoner = stack.get(LOTRDataComponents.POISONER);
        return poisoner == null || poisoner.equals(player.getUUID()) || player.hasInfiniteMaterials();
    }

    private static void addEffectToConsumable(ItemStack stack) {
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable == null) {
            return;
        }
        ConsumeEffect poison = new ApplyStatusEffectsConsumeEffect(
                new MobEffectInstance(LOTREffects.DRINK_POISON, DURATION), 1.0F);
        if (consumable.onConsumeEffects().contains(poison)) {
            return;
        }
        List<ConsumeEffect> effects = new ArrayList<>(consumable.onConsumeEffects());
        effects.add(poison);
        stack.set(DataComponents.CONSUMABLE, new Consumable(consumable.consumeSeconds(), consumable.animation(),
                consumable.sound(), consumable.hasConsumeParticles(), effects));
    }
}
