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

    /**
     * LOTREntityOrcBomb. Sized like vanilla's primed TNT, which it extends; the
     * fuse and blast come from the bomb block it carries.
     */
    public static final EntityType<LOTROrcBombEntity> ORC_BOMB = register("orc_bomb",
            EntityType.Builder.<LOTROrcBombEntity>of(LOTROrcBombEntity::new, MobCategory.MISC)
                    .sized(0.98f, 0.98f)
                    .clientTrackingRange(10)
                    .updateInterval(10));

    /**
     * LOTREntityThrowingAxe, setSize(0.5f, 0.5f) -- an arrow's footprint, which
     * is what it is built on. Tracked like an arrow too: close range, and a
     * slow update interval, since its flight is worked out the same way on both
     * sides.
     */
    public static final EntityType<LOTRThrowingAxeEntity> THROWING_AXE = register("throwing_axe",
            EntityType.Builder.<LOTRThrowingAxeEntity>of(LOTRThrowingAxeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    /**
     * LOTREntityCrossbowBolt, setSize(0.5f, 0.5f) -- an arrow's, which it is
     * built on and tracked like.
     */
    public static final EntityType<LOTRCrossbowBoltEntity> CROSSBOW_BOLT = register("crossbow_bolt",
            EntityType.Builder.<LOTRCrossbowBoltEntity>of(LOTRCrossbowBoltEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    /**
     * LOTREntityPebble, an EntityThrowable and so setSize(0.25f, 0.25f) -- the
     * size vanilla gives a snowball, which is what it is built on.
     */
    public static final EntityType<LOTRPebbleEntity> PEBBLE = register("pebble",
            EntityType.Builder.<LOTRPebbleEntity>of(LOTRPebbleEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    /**
     * A thrown LOTR trident. Sized and tracked as vanilla's is; it exists so
     * the projectile gets a renderer of its own rather than vanilla's fixed
     * trident model. See LOTRThrownTridentEntity.
     */
    public static final EntityType<LOTRThrownTridentEntity> THROWN_TRIDENT = register("thrown_trident",
            EntityType.Builder.<LOTRThrownTridentEntity>of(LOTRThrownTridentEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    /** LOTREntityDart, sized and tracked as an arrow, which it is built on. */
    public static final EntityType<LOTRDartEntity> DART = register("dart",
            EntityType.Builder.<LOTRDartEntity>of(LOTRDartEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20));

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
