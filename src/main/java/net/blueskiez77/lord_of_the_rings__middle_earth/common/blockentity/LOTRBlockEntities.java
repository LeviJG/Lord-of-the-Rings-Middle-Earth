package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class LOTRBlockEntities {

    public static BlockEntityType<LOTRForgeBlockEntity> FORGE;
    public static BlockEntityType<LOTRDartTrapBlockEntity> DART_TRAP;
    public static BlockEntityType<LOTRBeaconBlockEntity> BEACON;
    public static BlockEntityType<LOTRHobbitOvenBlockEntity> HOBBIT_OVEN;
    public static BlockEntityType<LOTRDwarvenDoorBlockEntity> DWARVEN_DOOR;
    public static BlockEntityType<LOTREntJarBlockEntity> ENT_JAR;
    public static BlockEntityType<LOTRTrollTotemBlockEntity> TROLL_TOTEM;
    public static BlockEntityType<LOTRTableOfCommandBlockEntity> TABLE_OF_COMMAND;
    public static BlockEntityType<LOTRUnsmelteryBlockEntity> UNSMELTERY;
    public static BlockEntityType<LOTRKebabStandBlockEntity> KEBAB_STAND;
    public static BlockEntityType<LOTRChestBlockEntity> CHEST;
    public static BlockEntityType<LOTRMillstoneBlockEntity> MILLSTONE;
    public static BlockEntityType<LOTRAnimalJarBlockEntity> ANIMAL_JAR;
    public static BlockEntityType<LOTRWeaponRackBlockEntity> WEAPON_RACK;
    public static BlockEntityType<LOTRBannerBlockEntity> BANNER;
    public static BlockEntityType<LOTRBarrelBlockEntity> BARREL;
    public static BlockEntityType<LOTRPlateBlockEntity> PLATE;
    public static BlockEntityType<LOTRMugBlockEntity> MUG;
    public static BlockEntityType<LOTRCarvedSignBlockEntity> CARVED_SIGN;

    private LOTRBlockEntities() {
    }

    public static void init() {
        CARVED_SIGN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "carved_sign"),
                new BlockEntityType<>(LOTRCarvedSignBlockEntity::new, Set.copyOf(LOTRUtilityBlocks.ALL_CARVED_SIGNS)));

        BARREL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "barrel"),
                new BlockEntityType<>(LOTRBarrelBlockEntity::new, Set.of(LOTRFoodBlocks.BARREL)));

        PLATE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "plate"),
                new BlockEntityType<>(LOTRPlateBlockEntity::new, Set.copyOf(LOTRBlocks.ALL_PLATES)));

        MUG = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mug"),
                new BlockEntityType<>(LOTRMugBlockEntity::new, Set.copyOf(LOTRBlocks.ALL_MUG_BLOCKS)));

        FORGE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "forge"),
                new BlockEntityType<>(LOTRForgeBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_FORGES)));

        // Both banner forms, all forty-two of each: the block entity is only
        // there to get them a renderer.
        BANNER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner"),
                new BlockEntityType<>(LOTRBannerBlockEntity::new,
                        java.util.stream.Stream.concat(
                                LOTRBlocks.ALL_BANNERS.stream(),
                                LOTRBlocks.ALL_WALL_BANNERS.stream())
                                .collect(java.util.stream.Collectors.toUnmodifiableSet())));

        WEAPON_RACK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "weapon_rack"),
                new BlockEntityType<>(LOTRWeaponRackBlockEntity::new,
                        Set.of(LOTRDecorationBlocks.WEAPON_RACK)));

        ANIMAL_JAR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "animal_jar"),
                new BlockEntityType<>(LOTRAnimalJarBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_ANIMAL_JARS)));

        DART_TRAP = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dart_trap"),
                new BlockEntityType<>(LOTRDartTrapBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_DART_TRAPS)));

        BEACON = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "beacon"),
                new BlockEntityType<>(LOTRBeaconBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.BEACON_OF_GONDOR)));

        HOBBIT_OVEN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hobbit_oven"),
                new BlockEntityType<>(LOTRHobbitOvenBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.HOBBIT_OVEN)));

        // Only the ithildin door carries one. The plain dwarven door has no
        // design to remember and no glow to drive, exactly as in 1.7.10 where
        // hasTileEntity was overridden on the ithildin subclass alone.
        DWARVEN_DOOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dwarven_door"),
                new BlockEntityType<>(LOTRDwarvenDoorBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.ITHILDIN_DWARVEN_DOOR)));

        ENT_JAR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ent_jar"),
                new BlockEntityType<>(LOTREntJarBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.ENT_JAR)));

        // All three parts carry one: the renderer needs a block entity on each
        // to draw from, even though only the head thinks.
        TROLL_TOTEM = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "troll_totem"),
                new BlockEntityType<>(LOTRTrollTotemBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_TROLL_TOTEMS)));

        TABLE_OF_COMMAND = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "table_of_command"),
                new BlockEntityType<>(LOTRTableOfCommandBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.TABLE_OF_COMMAND)));

        UNSMELTERY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "unsmeltery"),
                new BlockEntityType<>(LOTRUnsmelteryBlockEntity::new,
                        Set.of(LOTRUtilityBlocks.UNSMELTERY)));

        KEBAB_STAND = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "kebab_stand"),
                new BlockEntityType<>(LOTRKebabStandBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_KEBAB_STANDS)));

        CHEST = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "chest"),
                new BlockEntityType<>(LOTRChestBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_CHESTS)));

        MILLSTONE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "millstone"),
                new BlockEntityType<>(LOTRMillstoneBlockEntity::new, Set.of(LOTRUtilityBlocks.MILLSTONE)));
    }
}