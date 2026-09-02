package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * The mod's entity types. The stone troll is the first one the port has, so
 * this is deliberately small; more will follow the same shape.
 */
public final class LOTREntities {

    /**
     * LOTREntityStoneTroll, setSize(1.6f, 3.2f). MobCategory.MISC because it is
     * furniture and scenery -- it must never be counted against a mob cap or
     * swept up by natural despawning, which the entity handles itself.
     */
    public static final EntityType<LOTRStoneTrollEntity> STONE_TROLL = register("stone_troll",
            EntityType.Builder.of(LOTRStoneTrollEntity::new, MobCategory.MISC)
                    .sized(1.6f, 3.2f)
                    // Ten chunks, to match the 128-block range at which a
                    // worldgen statue removes itself. The update interval is
                    // the ordinary one: a statue is still subject to gravity
                    // and will slide and settle when the ground gives way.
                    .clientTrackingRange(10)
                    .updateInterval(3));

    /** LOTREntityBossTrophy, setSize(1.0f, 1.0f). */
    public static final EntityType<LOTRBossTrophyEntity> BOSS_TROPHY = register("boss_trophy",
            EntityType.Builder.of(LOTRBossTrophyEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f)
                    .clientTrackingRange(10)
                    .updateInterval(3));

    private LOTREntities() {
    }

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(
            String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    /** Force class-load so the static fields register. Called from mod init. */
    public static void init() {
    }
}
