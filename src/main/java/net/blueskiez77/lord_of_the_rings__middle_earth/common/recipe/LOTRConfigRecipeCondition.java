package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.Map;
import java.util.function.BooleanSupplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRecipes.modifyStandardRecipes' config switches: the vanilla recipes the original took out by
 * config ("Remove Golden Apple recipes", "Remove diamond armour recipes") are the mod's copies of
 * them, each loading only while its option is off ({@code fabric:not} round this condition, which
 * holds while the named option is on). The data is read again on /reload.
 */
public record LOTRConfigRecipeCondition(String option) implements ResourceCondition {

    private static final Map<String, BooleanSupplier> OPTIONS = Map.of(
            "removeGoldenAppleRecipes", () -> LOTRConfig.removeGoldenAppleRecipes,
            "removeDiamondArmorRecipes", () -> LOTRConfig.removeDiamondArmorRecipes);

    public static final ResourceConditionType<LOTRConfigRecipeCondition> TYPE = ResourceConditionType.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "config"),
            Codec.STRING.validate(option -> OPTIONS.containsKey(option)
                            ? com.mojang.serialization.DataResult.success(option)
                            : com.mojang.serialization.DataResult.error(() -> "Unknown LOTR config option: " + option))
                    .fieldOf("option").xmap(LOTRConfigRecipeCondition::new, LOTRConfigRecipeCondition::option));

    public static void init() {
        ResourceConditions.register(TYPE);
    }

    @Override
    public ResourceConditionType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(RegistryOps.@Nullable RegistryInfoLookup registryInfo) {
        return OPTIONS.get(this.option).getAsBoolean();
    }
}
