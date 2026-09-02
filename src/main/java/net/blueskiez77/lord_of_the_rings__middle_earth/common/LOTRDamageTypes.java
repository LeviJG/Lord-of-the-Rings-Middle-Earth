package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

/** Damage the mod's own blocks deal. */
public final class LOTRDamageTypes {
    /**
     * LOTRDamage.plantHurt: what the thistle, the nettles and the stabbing thorn
     * do to anything that walks into them. It bypassed armour in 1.7.10, which
     * is now expressed by putting it in #minecraft:bypasses_armor rather than by
     * a flag on the type itself. Defined as a datapack file under
     * data/lotr/damage_type/plant_hurt.json.
     */
    public static final ResourceKey<DamageType> PLANT_HURT = ResourceKey.create(
            Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "plant_hurt"));

    public static DamageSource plantHurt(Level level) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(PLANT_HURT));
    }

    private LOTRDamageTypes() {
    }
}
