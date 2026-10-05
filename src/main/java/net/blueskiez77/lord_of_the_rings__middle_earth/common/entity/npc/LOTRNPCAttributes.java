package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/**
 * LOTREntityNPC's own attributes: its unarmed, extra and drunken damage, its
 * aim, and its horse's charge speed; and a mountain troll's thrown rocks
 * (LOTREntityMountainTroll.thrownRockDamage, 5 by default, at most 100).
 */
public final class LOTRNPCAttributes {

    public static final Holder<Attribute> NPC_ATTACK_DAMAGE = register("npc_attack_damage", 2.0);
    public static final Holder<Attribute> NPC_ATTACK_DAMAGE_EXTRA = register("npc_attack_damage_extra", 0.0);
    public static final Holder<Attribute> NPC_ATTACK_DAMAGE_DRUNK = register("npc_attack_damage_drunk", 4.0);
    public static final Holder<Attribute> NPC_RANGED_ACCURACY = register("npc_ranged_accuracy", 1.0);
    /**
     * lotr.horseAttackSpeed. Registered and set per NPC as in the original, but
     * nothing reads it: a mounted NPC charges at its mount's own speed (user).
     */
    public static final Holder<Attribute> HORSE_ATTACK_SPEED = register("horse_attack_speed", 1.7);
    public static final Holder<Attribute> THROWN_ROCK_DAMAGE = register("thrown_rock_damage", 5.0, 100.0);

    private LOTRNPCAttributes() {
    }

    private static Holder<Attribute> register(String name, double base) {
        return register(name, base, Double.MAX_VALUE);
    }

    private static Holder<Attribute> register(String name, double base, double max) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name),
                new RangedAttribute("attribute.name.lotr." + name, base, 0.0, max).setSyncable(true));
    }

    public static void init() {
    }
}
