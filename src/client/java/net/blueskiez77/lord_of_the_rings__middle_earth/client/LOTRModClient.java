package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTooltipItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.core.component.DataComponents;
import java.util.ArrayList;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPoisonedDrinks;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBeaconScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCraftingScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHornSelectScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRForgeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHobbitOvenScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMillstoneScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRUnsmelteryScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAnimalJarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRArmorRenderers;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBannerRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBossTrophyRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRChestRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRCrossbowBoltRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRDartRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTREntJarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRItemModelProperties;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRKebabStandRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPoisonedArrowRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRStoneTrollRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRThrowingAxeRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRThrownTridentRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRIthildinDoorRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRTrollTotemRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRUnsmelteryRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRWeaponRackRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderPlugin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBeaconBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class LOTRModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");

        LOTRConnectedBorderPlugin.init();

        // lotr:sneaking and lotr:swinging, which the pikes' item models pose by.
        LOTRItemModelProperties.init();

        // The mod's items' own tooltip lines (their addInformation), put
        // straight under the name where appendHoverText would have -- that
        // method is deprecated in 26.2. A hidden tooltip stays hidden.
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            if (stack.getItem() instanceof LOTRTooltipItem item && !lines.isEmpty()
                    && !stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).hideTooltip()) {
                List<Component> extra = new ArrayList<>();
                item.addTooltip(stack, context, extra::add, flag);
                lines.addAll(1, extra);
            }
        });

        // LOTRTickHandlerClient's tooltip line: a poisoned drink says so, but
        // only to the poisoner and to creative players.
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            Player player = Minecraft.getInstance().player;
            if (player != null && LOTRPoisonedDrinks.isPoisoned(stack)
                    && LOTRPoisonedDrinks.canPlayerSeePoisoned(stack, player)) {
                lines.add(Component.translatable("item.lotr.drink.poison").withStyle(ChatFormatting.DARK_GREEN));
            }
        });

        // The tall grasses are greyscale sprites tinted by biome, the way
        // LOTRBlockTallGrass.colorMultiplier returned getBiomeGrassColor. Their
        // block models put tintindex 0 on the grass cross only, so the flower,
        // wheat and thistle overlays stay their own colour.
        BlockColorRegistry.register(List.of(BlockTintSources.grass()),
                LOTRBlocks.GRASS_TINTED.toArray(new Block[0]));

        // Block entity renderers, for the two things that cannot be baked into
        // a static model. The ithildin engraving's brightness depends on the
        // player's distance and the time of day; LOTRClientProxy bound it the
        // same way.
        BlockEntityRenderers.register(
                LOTRBlockEntities.DWARVEN_DOOR, LOTRIthildinDoorRenderer::new);

        // The Ent Jar's liquid level: a surface quad whose height and tint come
        // from the block entity, so it cannot live in a static model either.
        BlockEntityRenderers.register(
                LOTRBlockEntities.ENT_JAR, LOTREntJarRenderer::new);

        // The troll totem draws nothing in the chunk mesh at all -- its blocks
        // are RenderShape.INVISIBLE and the whole idol is this model.
        BlockEntityRenderers.register(
                LOTRBlockEntities.TROLL_TOTEM, LOTRTrollTotemRenderer::new);

        // The unsmeltery's cauldron, which sways while it works.
        BlockEntityRenderers.register(
                LOTRBlockEntities.UNSMELTERY, LOTRUnsmelteryRenderer::new);

        // The kebab stand, and the meat turning on its spit.
        BlockEntityRenderers.register(
                LOTRBlockEntities.KEBAB_STAND, LOTRKebabStandRenderer::new);

        // Whatever a bird cage is holding. The cage itself is a static model;
        // only its occupant needs drawing from a block entity.
        BlockEntityRenderers.register(
                LOTRBlockEntities.ANIMAL_JAR, LOTRAnimalJarRenderer::new);

        // The weapon rack draws nothing in the chunk mesh -- the stand and the
        // weapon on it are both this renderer's work.
        BlockEntityRenderers.register(
                LOTRBlockEntities.WEAPON_RACK, LOTRWeaponRackRenderer::new);

        // The mod's chests: vanilla's chest model wearing their own textures.
        BlockEntityRenderers.register(
                LOTRBlockEntities.CHEST, LOTRChestRenderer::new);

        // Helmets whose crests and wings are geometry the vanilla armour model
        // does not have. See LOTRArmorRenderers.
        LOTRArmorRenderers.init();

        // Faction banners. Vanilla's banner renderer draws a base colour and a
        // pattern list; these are one texture apiece, so they need their own.
        BlockEntityRenderers.register(
                LOTRBlockEntities.BANNER, LOTRBannerRenderer::new);

        // Boss trophies: the chieftain's skulls and the mallorn ent's trunk.
        EntityRenderers.register(LOTREntities.BOSS_TROPHY, LOTRBossTrophyRenderer::new);

        // The orc bomb rides on vanilla's TNT renderer: it draws the block
        // state the entity carries, which is the bomb block, so the swelling
        // and the white flash come free and correct.
        EntityRenderers.register(LOTREntities.ORC_BOMB, TntRenderer::new);

        // The stone troll, which draws nothing without one.
        EntityRenderers.register(LOTREntities.STONE_TROLL, LOTRStoneTrollRenderer::new);

        // A thrown axe draws its own item sprite, tumbling.
        EntityRenderers.register(LOTREntities.THROWING_AXE, LOTRThrowingAxeRenderer::new);

        // And a bolt is drawn exactly as an arrow is; see LOTRCrossbowBoltRenderer.
        EntityRenderers.register(LOTREntities.CROSSBOW_BOLT, LOTRCrossbowBoltRenderer::new);

        // A pebble is drawn as a snowball is -- its own item sprite, billboarded
        // at the camera -- so vanilla's thrown-item renderer serves as it is.
        EntityRenderers.register(LOTREntities.PEBBLE, ThrownItemRenderer::new);

        // A thrown LOTR trident, which needs a renderer of its own so it is not
        // drawn as a vanilla one. See LOTRThrownTridentRenderer.
        EntityRenderers.register(LOTREntities.THROWN_TRIDENT, LOTRThrownTridentRenderer::new);

        // A blowgun dart, drawn as its own sprite; see LOTRDartRenderer.
        EntityRenderers.register(LOTREntities.DART, LOTRDartRenderer::new);

        // The poisoned arrow on its own sheet, and the fire-pot drawn as its item, as a snowball is.
        EntityRenderers.register(LOTREntities.POISONED_ARROW, LOTRPoisonedArrowRenderer::new);
        EntityRenderers.register(LOTREntities.FIRE_POT, ThrownItemRenderer::new);
        // Drawn full-bright and a block across, as LOTRRenderGandalfFireball drew it.
        EntityRenderers.register(LOTREntities.CONKER, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.EXPLODING_TERMITE, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.MYSTERY_WEB, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.GANDALF_FIREBALL,
                context -> new ThrownItemRenderer<>(context, 2.0F, true));

        // A plain Horn of Command opens its selection screen instead of being
        // blown. Opened client-side, the way the beacon's naming dialog is, so
        // no client class leaks into the common source set; the choice travels
        // back as LOTRHornModePayload.
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack held = player.getItemInHand(hand);
            if (level.isClientSide()
                    && held.getItem() instanceof LOTRCommandHornItem
                    && LOTRCommandHornItem.getMode(held) == LOTRCommandHornItem.Mode.SELECT) {
                Minecraft.getInstance().setScreenAndShow(new LOTRHornSelectScreen(held));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        // tabFood's blocks that draw more than a model can: a drink set down in
        // its vessel, and the food piled on a plate.
        BlockEntityRenderers.register(LOTRBlockEntities.MUG,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMugRenderer::new);
        BlockEntityRenderers.register(LOTRBlockEntities.PLATE,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateRenderer::new);
        // A thrown plate, drawn as the plate itself, spinning.
        EntityRenderers.register(LOTREntities.PLATE,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateEntityRenderer::new);
        // A plate worn in the helmet slot.
        net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateHeadRenderer.init();
        // LOTRTickHandlerClient: nausea drags the view about.
        LOTRDrunkCamera.init();

        // Carved signs: the lettering in the world, and the screen a chisel opens.
        BlockEntityRenderers.register(LOTRBlockEntities.CARVED_SIGN,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRCarvedSignRenderer::new);
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
                net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTROpenSignEditorPayload.TYPE, (payload, context) ->
                        context.client().execute(() -> {
                            Minecraft client = context.client();
                            if (client.level != null && client.level.getBlockEntity(payload.pos())
                                    instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity sign) {
                                client.setScreenAndShow(new net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCarvedSignEditScreen(sign));
                            }
                        }));

        MenuScreens.register(LOTRMenus.BARREL,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBarrelScreen::new);
        MenuScreens.register(LOTRMenus.FORGE, LOTRForgeScreen::new);
        MenuScreens.register(LOTRMenus.HOBBIT_OVEN, LOTRHobbitOvenScreen::new);
        MenuScreens.register(LOTRMenus.UNSMELTERY, LOTRUnsmelteryScreen::new);
        MenuScreens.register(LOTRMenus.MILLSTONE, LOTRMillstoneScreen::new);
        MenuScreens.register(LOTRMenus.DALE_CRACKER,
                net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRDaleCrackerScreen::new);

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