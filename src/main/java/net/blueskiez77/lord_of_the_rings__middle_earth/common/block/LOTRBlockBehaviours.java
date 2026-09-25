package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.List;

import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Direction;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// Block interactions the port had none of: burning, hoeing, shovelling, fuel,
// composting.
//
// The 1.7.10 mod set the fire values in LOTRMod.load() by walking the block
// registry and calling Blocks.fire.setFireInfo per class. The numbers below are
// that method's, verbatim; vanilla's (encouragement, flammability) and Fabric's
// add(block, burn, spread) take them in the same positional order.
public final class LOTRBlockBehaviours {

    private LOTRBlockBehaviours() {
    }

    public static void init() {
        LOTRMod.LOGGER.info("LOTR block behaviours: wiring {} logs, {} leaves, {} planks; hoe turns soil into {}",
                LOTRBlocks.ALL_LOGS.size(), LOTRBlocks.ALL_LEAVES.size(), LOTRBlocks.ALL_PLANKS.size(),
                LOTRBlocks.MUD_FARMLAND);

        FlammableBlockRegistry fire = FlammableBlockRegistry.getDefaultInstance();

        // setFireInfo(block, encouragement, flammability)
        LOTRBlocks.ALL_LOGS.forEach(b -> fire.add(b, 5, 5));
        LOTRBlocks.ALL_LEAVES.forEach(b -> fire.add(b, 30, 60));
        LOTRBlocks.ALL_BUSHES.forEach(b -> fire.add(b, 30, 60));
        LOTRBlocks.ALL_PLANKS.forEach(b -> fire.add(b, 5, 20));
        LOTRBlocks.ALL_BEAMS.forEach(b -> fire.add(b, 5, 20));
        LOTRBlocks.ALL_FENCES.forEach(b -> fire.add(b, 5, 20));
        LOTRBlocks.ALL_FENCE_GATES.forEach(b -> fire.add(b, 5, 20));
        // ALL_FLOWERS also holds plants the original did not class as flowers
        // or grass (LOTRBlockReed, LOTRBlockCorn, LOTRBlockGrapevine,
        // LOTRBlockFangornRiverweed); load() gave those no fire info at all.
        List<Block> notFlowers = List.of(LOTRBlocks.REEDS, LOTRBlocks.DRIED_REEDS,
                LOTRBlocks.CORN_STALK, LOTRBlocks.GRAPEVINE, LOTRBlocks.FANGORN_RIVERWEED);
        LOTRBlocks.ALL_FLOWERS.stream().filter(b -> !notFlowers.contains(b))
                .forEach(b -> fire.add(b, 60, 100));
        LOTRBlocks.ALL_DOUBLE_FLOWERS.forEach(b -> fire.add(b, 60, 100));
        LOTRBlocks.ALL_SAPLINGS.forEach(b -> fire.add(b, 60, 100));
        LOTRBlocks.ALL_VINES.forEach(b -> fire.add(b, 15, 100));

        // LOTRBlockWoodBars.
        List.of(LOTRBlocks.GALADHRIM_WOOD_BARS, LOTRBlocks.HIGH_ELF_WOOD_BARS, LOTRBlocks.WOOD_ELF_WOOD_BARS)
                .forEach(b -> fire.add(b, 5, 20));

        // LOTRBlockMordorMoss, alongside the grasses (Mordor grass is in ALL_FLOWERS).
        fire.add(LOTRBlocks.MORDOR_MOSS, 60, 100);

        // LOTRBlockDaub.
        fire.add(LOTRBlocks.DAUB, 40, 40);

        // Wooden slabs and stairs only -- the originals tested for Material.wood.
        LOTRBlocks.SLAB_BASE.forEach((slab, base) -> {
            if (isWood(base)) {
                fire.add(slab, 5, 20);
            }
        });
        LOTRBlocks.STAIRS_BASE.forEach((stairs, base) -> {
            if (isWood(base)) {
                fire.add(stairs, 5, 20);
            }
        });

        // Thatch burns readily but spreads poorly -- 60/20, not 60/100.
        // Also LOTRBlockReedBars, and the slabs and stairs of Material.grass,
        // which are the thatch ones.
        List.of(LOTRBlocks.THATCH_THATCH, LOTRBlocks.THATCH_REED, LOTRBlocks.THATCH_FLOOR,
                        LOTRBlocks.REED_BARS,
                        LOTRBlocks.THATCH_THATCH_SLAB, LOTRBlocks.THATCH_REED_SLAB,
                        LOTRBlocks.THATCH_THATCH_STAIRS, LOTRBlocks.THATCH_REED_STAIRS)
                .forEach(b -> fire.add(b, 60, 20));

        // Hoe: soil -> farmland. Without this there is no way to make farmland
        // at all, so nothing can be planted. Like vanilla, only with nothing on
        // top -- except that a grapevine does not count: LOTREventHandler's
        // onUseHoe set LOTRBlockGrapevine.hoeing so the vine above reported
        // itself as air, letting you till the soil a vineyard stands in.
        TillableBlockRegistry.register(LOTRBlocks.MUD, LOTRBlockBehaviours::airOrGrapevineAbove,
                LOTRBlocks.MUD_FARMLAND.defaultBlockState());
        TillableBlockRegistry.register(LOTRBlocks.BARREN_JUNGLE_MUD, LOTRBlockBehaviours::airOrGrapevineAbove,
                LOTRBlocks.MUD_FARMLAND.defaultBlockState());
        TillableBlockRegistry.register(LOTRBlocks.MUD_GRASS, LOTRBlockBehaviours::airOrGrapevineAbove,
                LOTRBlocks.MUD_FARMLAND.defaultBlockState());

        // Paths hoe back into farmland, as vanilla dirt path does.
        TillableBlockRegistry.register(LOTRBlocks.DIRT_PATH_MUD, LOTRBlockBehaviours::airOrGrapevineAbove,
                LOTRBlocks.MUD_FARMLAND.defaultBlockState());

        // The same grapevine allowance for vanilla's own tillable soils, which
        // otherwise keep vanilla's rule (HoeItem.onlyIfAirAbove).
        for (Block soil : List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT_PATH)) {
            TillableBlockRegistry.register(soil, LOTRBlockBehaviours::airOrGrapevineAbove,
                    Blocks.FARMLAND.defaultBlockState());
        }

        // Shovel: grass-like -> path, matching what vanilla does to grass.
        FlattenableBlockRegistry.register(LOTRBlocks.MUD_GRASS, LOTRBlocks.DIRT_PATH_MUD.defaultBlockState());
        FlattenableBlockRegistry.register(LOTRBlocks.MUD, LOTRBlocks.DIRT_PATH_MUD.defaultBlockState());

        composting();
        fuel();

        LOTRMod.LOGGER.info("LOTR block behaviours: done");
    }

    // Vanilla compost chances: leaves and saplings 0.3, vines and tall grass
    // 0.5, flowers 0.65, hay 0.85. Thatch is hay.
    private static void composting() {
        LOTRBlocks.ALL_LEAVES.forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.3F));
        LOTRBlocks.ALL_SAPLINGS.forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.3F));
        LOTRBlocks.ALL_BUSHES.forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.3F));
        LOTRBlocks.ALL_VINES.forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.5F));
        LOTRBlocks.ALL_FLOWERS.forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.65F));
        List.of(LOTRBlocks.THATCH_THATCH, LOTRBlocks.THATCH_REED, LOTRBlocks.THATCH_FLOOR)
                .forEach(b -> CompostableRegistry.INSTANCE.add(b, 0.85F));
    }

    // Vanilla burn times, in ticks: anything wooden 300, wooden slabs 150,
    // wooden doors 200, buttons and saplings 100.
    private static void fuel() {
        FuelValueEvents.BUILD.register((builder, context) -> {
            LOTRBlocks.ALL_PLANKS.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_LOGS.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_BEAMS.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_FENCES.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_FENCE_GATES.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_TRAPDOORS.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_CRAFTING_TABLES.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_LADDERS.forEach(b -> builder.add(b, 300));
            LOTRBlocks.ALL_DOORS.forEach(b -> builder.add(b, 200));
            LOTRBlocks.ALL_SAPLINGS.forEach(b -> builder.add(b, 100));

            // Only the wooden cut shapes burn; stone slabs and stairs do not.
            LOTRBlocks.SLAB_BASE.forEach((slab, base) -> {
                if (isWood(base)) {
                    builder.add(slab, 150);
                }
            });
            LOTRBlocks.STAIRS_BASE.forEach((stairs, base) -> {
                if (isWood(base)) {
                    builder.add(stairs, 300);
                }
            });
            LOTRBlocks.BUTTON_BASE.forEach((button, base) -> {
                if (isWood(base)) {
                    builder.add(button, 100);
                }
            });
            LOTRBlocks.PRESSURE_PLATE_BASE.forEach((plate, base) -> {
                if (isWood(base)) {
                    builder.add(plate, 300);
                }
            });
        });
    }

    private static boolean airOrGrapevineAbove(UseOnContext context) {
        if (HoeItem.onlyIfAirAbove(context)) {
            return true;
        }
        BlockState above = context.getLevel().getBlockState(context.getClickedPos().above());
        return context.getClickedFace() != Direction.DOWN
                && (above.is(LOTRBlocks.GRAPEVINE) || above.getBlock() instanceof LOTRGrapevineBlock);
    }

    private static boolean isWood(Block base) {
        return LOTRBlocks.ALL_PLANKS.contains(base)
                || LOTRBlocks.ALL_LOGS.contains(base)
                || LOTRBlocks.ALL_BEAMS.contains(base);
    }
}