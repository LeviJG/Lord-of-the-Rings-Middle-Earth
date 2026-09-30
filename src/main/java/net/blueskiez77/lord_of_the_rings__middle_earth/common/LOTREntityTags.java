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
     * {@code entity instanceof LOTREntityBird}: the tag holds the bird and its
     * three kinds (crebain, gorcrow, seagull), and a datapack can add to it.
     */
    public static final TagKey<EntityType<?>> BIRD_CAGE_CATCHABLE = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bird_cage_catchable"));

    /**
     * What a butterfly jar will catch. LOTRBlockButterflyJar.canCapture asked
     * {@code entity instanceof LOTREntityButterfly}: the tag holds
     * {@code lotr:butterfly}, and a datapack can add to it.
     */
    public static final TagKey<EntityType<?>> BUTTERFLY_JAR_CATCHABLE = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "butterfly_jar_catchable"));

    /** Every orc: what LOTREnchantmentBane("baneOrc") asked of {@code entity instanceof LOTREntityOrc}. */
    public static final TagKey<EntityType<?>> ORCS = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "orcs"));

    /**
     * Every dwarf, the wicked ones too: what LOTREnchantmentBane("baneDwarf")
     * asked of {@code entity instanceof LOTREntityDwarf}.
     */
    public static final TagKey<EntityType<?>> DWARVES = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dwarves"));

    /** Every elf: what LOTREnchantmentBane("baneElf") asked of {@code entity instanceof LOTREntityElf}. */
    public static final TagKey<EntityType<?>> ELVES = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "elves"));

    /** Every warg: what LOTREnchantmentBane("baneWarg") asked of {@code entity instanceof LOTREntityWarg}. */
    public static final TagKey<EntityType<?>> WARGS = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "wargs"));

    private LOTREntityTags() {
    }

    /** Every troll and half-troll: what LOTREnchantmentBane("baneTroll") asked of LOTREntityTroll and LOTREntityHalfTroll. */
    public static final TagKey<EntityType<?>> TROLLS = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "trolls"));

    /** The marsh wraiths: what LOTREnchantmentBane("baneWraith") asked of LOTREntityMarshWraith. */
    public static final TagKey<EntityType<?>> MARSH_WRAITHS = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "marsh_wraiths"));
}
