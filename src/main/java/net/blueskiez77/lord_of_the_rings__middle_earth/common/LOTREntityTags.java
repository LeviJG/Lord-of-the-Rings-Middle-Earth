package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/** Entity tags the mod defines for itself. */
public final class LOTREntityTags {
    /**
     * What a bird cage will catch. LOTRBlockBirdCage.canCapture asked
     * {@code entity instanceof LOTREntityBird}; LOTREntityBird is not ported, so
     * the answer lives in a tag until it is -- and a datapack can change it
     * meanwhile. It currently holds vanilla's parrot and chicken as the nearest
     * things to a Middle-earth songbird.
     */
    public static final TagKey<EntityType<?>> BIRD_CAGE_CATCHABLE = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bird_cage_catchable"));

    /**
     * What a butterfly jar will catch. LOTRBlockButterflyJar.canCapture asked
     * {@code entity instanceof LOTREntityButterfly}. Vanilla has no butterfly,
     * so the tag holds the bee -- the nearest thing to one -- until LOTR's own
     * butterflies are ported.
     */
    public static final TagKey<EntityType<?>> BUTTERFLY_JAR_CATCHABLE = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "butterfly_jar_catchable"));

    private LOTREntityTags() {
    }
}
