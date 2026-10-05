package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.effect.LOTRDrinkPoisonMobEffect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** The mod's status effects. */
public final class LOTREffects {
    /** LOTRPoisonedDrinks.killingPoison, "Poisoned Drink". */
    public static final Holder<MobEffect> DRINK_POISON = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "drink_poison"), new LOTRDrinkPoisonMobEffect());

    private LOTREffects() {
    }

    /** Loads the class, and with it the registrations above. */
    public static void init() {
        // LOTRReplacedMethods.Potions.getStrengthModifier: Strength is half
        // again the attack damage a level (1.7.10's vanilla had 130%). The
        // original kept vanilla's Weakness, which stays modern vanilla's.
        MobEffects.STRENGTH.value().addAttributeModifier(Attributes.ATTACK_DAMAGE,
                Identifier.withDefaultNamespace("effect.strength"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
