package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.effect.LOTRDrinkPoisonMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

/** The mod's status effects. */
public final class LOTREffects {
    /** LOTRPoisonedDrinks.killingPoison, "Poisoned Drink". */
    public static final Holder<MobEffect> DRINK_POISON = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "drink_poison"), new LOTRDrinkPoisonMobEffect());

    private LOTREffects() {
    }

    /** Loads the class, and with it the registrations above. */
    public static void init() {
    }
}
