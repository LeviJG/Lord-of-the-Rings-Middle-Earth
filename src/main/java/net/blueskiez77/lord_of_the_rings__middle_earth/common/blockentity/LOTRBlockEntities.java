package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class LOTRBlockEntities {

    public static BlockEntityType<LOTRForgeBlockEntity> FORGE;
    public static BlockEntityType<LOTRDartTrapBlockEntity> DART_TRAP;
    public static BlockEntityType<LOTRBeaconBlockEntity> BEACON;
    public static BlockEntityType<LOTRHobbitOvenBlockEntity> HOBBIT_OVEN;

    private LOTRBlockEntities() {
    }

    public static void init() {
        FORGE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "forge"),
                new BlockEntityType<>(LOTRForgeBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_FORGES)));

        DART_TRAP = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "dart_trap"),
                new BlockEntityType<>(LOTRDartTrapBlockEntity::new,
                        Set.copyOf(LOTRBlocks.ALL_DART_TRAPS)));

        BEACON = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "beacon"),
                new BlockEntityType<>(LOTRBeaconBlockEntity::new,
                        Set.of(LOTRBlocks.BEACON_OF_GONDOR)));

        HOBBIT_OVEN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hobbit_oven"),
                new BlockEntityType<>(LOTRHobbitOvenBlockEntity::new,
                        Set.of(LOTRBlocks.HOBBIT_OVEN)));
    }
}