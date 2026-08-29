package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBeaconScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCraftingScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRForgeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHobbitOvenScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderPlugin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBeaconBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

public class LOTRModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");

        LOTRConnectedBorderPlugin.init();

        // could not verify. Fabric also exposes BlockEntityRendererFactories;
        // if this does not resolve, that is the alternative. The renderer
        // itself does not change either way.

        MenuScreens.register(LOTRMenus.FORGE, LOTRForgeScreen::new);
        MenuScreens.register(LOTRMenus.HOBBIT_OVEN, LOTRHobbitOvenScreen::new);

        for (LOTRCraftingTable table : LOTRCraftingTable.values()) {
            MenuScreens.register(LOTRMenus.forTable(table), LOTRCraftingScreen::new);
        }

        // The beacon screen has no menu -- it is a naming dialog, like a sign,
        // not a container -- so it cannot go through MenuScreens. Opening it
        // client-side keeps every client class out of the common source set;
        // the name change travels back to the server as LOTRBeaconEditPayload.
        // LOTRBlockBeacon.onBlockActivated: the lighting and quenching cases
        // return first, and EVERYTHING else falls through to openGui(50). My
        // first version only opened the screen for an empty hand, so holding a
        // pickaxe did nothing at all. The two special cases are re-tested here
        // so the screen does not steal a click that should light or quench.
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (!level.isClientSide() || player.isSecondaryUseActive()) {
                return InteractionResult.PASS;
            }
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof LOTRBeaconBlock beacon)) {
                return InteractionResult.PASS;
            }

            ItemStack stack = player.getItemInHand(hand);
            boolean lit = state.getValue(LOTRBeaconBlock.LIT);
            boolean waterAbove = level.getFluidState(pos.above()).is(Fluids.WATER)
                    || level.getFluidState(pos.above()).is(Fluids.FLOWING_WATER);

            // Lighting it wins.
            if (beacon.canItemLightBeacon(stack) && !lit && !waterAbove) {
                return InteractionResult.PASS;
            }
            // Quenching it wins.
            if (stack.is(Items.WATER_BUCKET) && lit) {
                return InteractionResult.PASS;
            }

            if (level.getBlockEntity(pos) instanceof LOTRBeaconBlockEntity blockEntity) {
                Minecraft.getInstance().setScreenAndShow(new LOTRBeaconScreen(
                        pos, blockEntity.getDisplayName(),
                        blockEntity.getBeaconName(), blockEntity.getFellowshipName()));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}