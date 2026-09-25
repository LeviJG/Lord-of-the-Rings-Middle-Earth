package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.UnaryOperator;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

/**
 * Item data that 1.7.10 kept in the item damage value.
 *
 * <p>A drink's damage was {@code vessel * 100 + strength}, and an ent-draught's
 * was simply which draught it was. Each is its own component now, saved with the
 * stack and synced to clients so item models can select on them.
 */
public final class LOTRDataComponents {

    /** Which cup a drink is in. See LOTRVessel. */
    public static final DataComponentType<LOTRVessel> VESSEL = register("vessel",
            b -> b.persistent(LOTRVessel.CODEC).networkSynchronized(LOTRVessel.STREAM_CODEC));

    /** A brewed drink's strength, 0 (weak) to 4 (potent). */
    public static final DataComponentType<Integer> DRINK_STRENGTH = register("drink_strength",
            b -> b.persistent(ExtraCodecs.intRange(0, 4)).networkSynchronized(ByteBufCodecs.VAR_INT));

    /** Which of the seven ent-draughts this is. */
    public static final DataComponentType<Integer> ENT_DRAUGHT = register("ent_draught",
            b -> b.persistent(ExtraCodecs.intRange(0, 6)).networkSynchronized(ByteBufCodecs.VAR_INT));

    /**
     * LOTRItemHobbitPipe's "SmokeColour": 0-15 are the dye colours, and 16 is
     * the mithril-touched magic smoke.
     */
    public static final DataComponentType<Integer> SMOKE_COLOR = register("smoke_color",
            b -> b.persistent(ExtraCodecs.intRange(0, 16)).networkSynchronized(ByteBufCodecs.VAR_INT));

    /** LOTRItemModifierTemplate's "ScrollModifier": the modifier a scroll teaches. */
    public static final DataComponentType<net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier> SCROLL_MODIFIER =
            register("scroll_modifier", b -> b
                    .persistent(net.minecraft.util.StringRepresentable.fromEnum(
                            net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier::values))
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8.map(
                            net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier::byName,
                            net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier::getSerializedName)));

    /** LOTRItemBarrel's "LOTRBarrelData": a barrel's contents, carried as an item. */
    public static final DataComponentType<net.minecraft.world.item.component.CustomData> BARREL_DATA = register("barrel_data",
            b -> b.persistent(net.minecraft.world.item.component.CustomData.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG.map(
                            net.minecraft.world.item.component.CustomData::of,
                            net.minecraft.world.item.component.CustomData::copyTag)));

    /** LOTRItemKebabStand's "LOTRKebabData": the meat on a stand, carried as an item. */
    public static final DataComponentType<net.minecraft.world.item.component.CustomData> KEBAB_DATA = register("kebab_data",
            b -> b.persistent(net.minecraft.world.item.component.CustomData.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG.map(
                            net.minecraft.world.item.component.CustomData::of,
                            net.minecraft.world.item.component.CustomData::copyTag)));

    /** LOTRPoisonedDrinks' "PoisonDrink": the drink has had poison stirred in. */
    public static final DataComponentType<Boolean> POISON_DRINK = register("poison_drink",
            b -> b.persistent(com.mojang.serialization.Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    /** LOTRPoisonedDrinks' "PoisonerUUID": who poisoned it, the one player who can tell. */
    public static final DataComponentType<java.util.UUID> POISONER = register("poisoner",
            b -> b.persistent(net.minecraft.core.UUIDUtil.CODEC).networkSynchronized(net.minecraft.core.UUIDUtil.STREAM_CODEC));

    /** LOTRItemDaleCracker's emptyMeta bit: an unsealed cracker, ready to be filled. */
    public static final DataComponentType<Boolean> CRACKER_EMPTY = register("cracker_empty",
            b -> b.persistent(com.mojang.serialization.Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    /** LOTRItemDaleCracker's "SealingPlayer": who filled and sealed it. */
    public static final DataComponentType<String> CRACKER_SEALER = register("cracker_sealer",
            b -> b.persistent(com.mojang.serialization.Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    /**
     * LOTRItemDaleCracker's "CustomCracker": what a player sealed inside. Its own
     * component rather than vanilla's CONTAINER, whose tooltip would give the
     * surprise away.
     */
    public static final DataComponentType<net.minecraft.world.item.component.ItemContainerContents> CRACKER_CONTENTS =
            register("cracker_contents", b -> b
                    .persistent(net.minecraft.world.item.component.ItemContainerContents.CODEC)
                    .networkSynchronized(net.minecraft.world.item.component.ItemContainerContents.STREAM_CODEC));

    private LOTRDataComponents() {
    }

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name), builder.apply(DataComponentType.builder()).build());
    }

    /** Touching the class registers the components; call before LOTRItems. */
    public static void init() {
    }
}
