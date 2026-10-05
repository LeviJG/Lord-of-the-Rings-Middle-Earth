package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMainMenuScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRAnvilScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBarrelScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBeaconScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRBrandingIronScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCarvedSignEditScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCoinExchangeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRCraftingScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRDaleCrackerScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRFactionsScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRForgeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRGollumScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHiredFarmerInventoryScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHiredFarmerScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHiredInteractScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHiredWarriorInventoryScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHiredWarriorScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHobbitOvenScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRHornSelectScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMessageScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMillstoneScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRNPCMountInventoryScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRNPCRespawnerScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRSquadronItemScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRTradeInteractScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRTradeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRUnitTradeInteractScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRUnitTradeScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRUnsmelteryScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentHud;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentTicker;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRCompassRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTREnvironmentOverlayHud;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRSpiderClimbHud;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.particle.LOTRParticleProviders;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAlignmentBonusRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAnimalJarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAnimalJarSpecialRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRArmorRenderers;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRAurochsRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBannerRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBarrelBoatRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBearRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBirdRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRBossTrophyRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRButterflyRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRCarvedSignRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRChestRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRCrocodileRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRCrossbowBoltRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRDartRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRDeerRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRDikDikRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRElvenBladeItemModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTREntJarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRFallenLeavesPlugin;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRFlamingoRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRGandalfFireballRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRGemsbokRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRHatFeatherTint;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRItemModelProperties;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRIthildinDoorRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRKebabStandRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRLionRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMarshWraithBallRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMidgesRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMountRenderers;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMugRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRNPCRespawnerRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateEntityRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateFallingInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateHeadRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPlateRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRPoisonedArrowRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRugRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRScorpionRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSmokeRingRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSnowyStoneModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSpiderRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRStoneTrollRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSwanRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSwordCommandMarkerRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRTermiteRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRThrowingAxeRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRThrownRockRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRThrownTridentRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRTraderRespawnRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRTrollTotemRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRUnsmelteryRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRWargRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRWeaponRackRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm.LOTRConnectedBorderPlugin;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRAngmarHillmanRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBanditRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBarrowWightRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBreeManRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRDaleManRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRDorwinionManRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRDunedainRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRDunlendingRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRDwarfRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTREasterlingRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRElfRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTREntRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRGandalfRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRGollumRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRGondorManRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHalfTrollRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHaradSlaveRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHobbitRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHuornRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRMallornEntRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRMarshWraithRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRMoredainRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNearHaradrimRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNurnSlaveRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTROrcRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRRohirrimRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRSarumanRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRScrapTraderRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRSkeletalWraithRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRSpeechClient;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRTauredainRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRTrollRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGuiMessageTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBeaconBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPottedPlants;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMenus;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRTradeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBrandingIronItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemOwnership;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPoisonedDrinks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTooltipItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTROpenSignEditorPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRRespawnerPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTradePayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class LOTRModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");

        // The main menu's buttons are laid out after other mods' (Mod Menu's) changes to the title screen.
        LOTRMainMenuScreen.init(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "after_other_mods"));

        LOTRConnectedBorderPlugin.init();
        LOTRFallenLeavesPlugin.init();
        LOTRHatFeatherTint.init();
        LOTRParticleProviders.init();
        LOTRMallornEntClient.init();

        // lotr:sneaking and lotr:swinging, which the pikes' item models pose by.
        LOTRItemModelProperties.init();
        LOTRElvenBladeItemModel.init();

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
            // LOTRItemOwnership, engraved at the LOTR anvil: the owner, then up
            // to three previous owners in italics.
            String owner = LOTRItemOwnership.getCurrentOwner(stack);
            if (owner != null) {
                lines.add(Component.empty());
                lines.add(Component.translatable("item.lotr.generic.currentOwner", owner));
            }
            List<String> previous = LOTRItemOwnership.getPreviousOwners(stack);
            if (!previous.isEmpty()) {
                lines.add(Component.empty());
                if (previous.size() == 1) {
                    lines.add(Component.translatable("item.lotr.generic.previousOwner", previous.get(0))
                            .withStyle(ChatFormatting.ITALIC));
                } else {
                    lines.add(Component.translatable("item.lotr.generic.previousOwnerList").withStyle(ChatFormatting.ITALIC));
                    for (String name : previous) {
                        lines.add(Component.literal("  " + name).withStyle(ChatFormatting.ITALIC));
                    }
                }
            }
        });

        // The tall grasses are greyscale sprites tinted by biome, the way
        // LOTRBlockTallGrass.colorMultiplier returned getBiomeGrassColor. Their
        // block models put tintindex 0 on the grass cross only, so the flower,
        // wheat and thistle overlays stay their own colour.
        BlockColorRegistry.register(List.of(BlockTintSources.grass()),
                LOTRBlocks.GRASS_TINTED.toArray(new Block[0]));
        // A potted clover is tinted as the clover is (LOTRRenderBlocks.renderFlowerPot took its colour).
        BlockColorRegistry.register(List.of(BlockTintSources.grass()),
                LOTRPottedPlants.POTTED.get(LOTRDecorationBlocks.CLOVER),
                LOTRPottedPlants.POTTED.get(LOTRDecorationBlocks.FOUR_LEAF_CLOVER));

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
        EntityRenderers.register(LOTREntities.SMOKE_RING,
                LOTRSmokeRingRenderer::new);

        // And a bolt is drawn exactly as an arrow is; see LOTRCrossbowBoltRenderer.
        EntityRenderers.register(LOTREntities.CROSSBOW_BOLT, LOTRCrossbowBoltRenderer::new);

        // A pebble is drawn as a snowball is -- its own item sprite, billboarded
        // at the camera -- so vanilla's thrown-item renderer serves as it is.
        EntityRenderers.register(LOTREntities.PEBBLE, ThrownItemRenderer::new);

        // A thrown LOTR trident, which needs a renderer of its own so it is not
        // drawn as a vanilla one. See LOTRThrownTridentRenderer.
        EntityRenderers.register(LOTREntities.THROWN_TRIDENT, LOTRThrownTridentRenderer::new);
        EntityRenderers.register(LOTREntities.MALLORN_LEAF_BOMB, NoopRenderer::new);

        // A blowgun dart, drawn as its own sprite; see LOTRDartRenderer.
        EntityRenderers.register(LOTREntities.DART, LOTRDartRenderer::new);

        // The poisoned arrow on its own sheet, and the fire-pot drawn as its item, as a snowball is.
        EntityRenderers.register(LOTREntities.POISONED_ARROW, LOTRPoisonedArrowRenderer::new);
        EntityRenderers.register(LOTREntities.FIRE_POT, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.THROWN_ROCK, LOTRThrownRockRenderer::new);
        EntityRenderers.register(LOTREntities.MARSH_WRAITH_BALL, LOTRMarshWraithBallRenderer::new);
        EntityRenderers.register(LOTREntities.TROLL_SNOWBALL, ThrownItemRenderer::new);
        // Drawn full-bright and a block across, as LOTRRenderGandalfFireball drew it.
        EntityRenderers.register(LOTREntities.CONKER, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.EXPLODING_TERMITE, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.MYSTERY_WEB, ThrownItemRenderer::new);
        EntityRenderers.register(LOTREntities.BARREL, LOTRBarrelBoatRenderer::new);
        EntityRenderers.register(LOTREntities.BUTTERFLY, LOTRButterflyRenderer::new);
        EntityRenderers.register(LOTREntities.MIDGES, LOTRMidgesRenderer::new);
        EntityRenderers.register(LOTREntities.SWAN, LOTRSwanRenderer::new);
        EntityRenderers.register(LOTREntities.BIRD, LOTRBirdRenderer::new);
        EntityRenderers.register(LOTREntities.CREBAIN, LOTRBirdRenderer::new);
        EntityRenderers.register(LOTREntities.GORCROW, LOTRBirdRenderer::new);
        EntityRenderers.register(LOTREntities.SEAGULL, LOTRBirdRenderer::new);
        EntityRenderers.register(LOTREntities.HORSE, LOTRMountRenderers::horse);
        EntityRenderers.register(LOTREntities.SHIRE_PONY, LOTRMountRenderers::shirePony);
        EntityRenderers.register(LOTREntities.WILD_BOAR, LOTRMountRenderers::wildBoar);
        EntityRenderers.register(LOTREntities.GIRAFFE, LOTRMountRenderers::giraffe);
        EntityRenderers.register(LOTREntities.ZEBRA, LOTRMountRenderers::zebra);
        EntityRenderers.register(LOTREntities.RHINO, LOTRMountRenderers::rhino);
        EntityRenderers.register(LOTREntities.CAMEL, LOTRMountRenderers::camel);
        EntityRenderers.register(LOTREntities.ELK, LOTRMountRenderers::elk);
        EntityRenderers.register(LOTREntities.GIRAFFE_RUG, LOTRRugRenderer::giraffe);
        EntityRenderers.register(LOTREntities.LION, LOTRLionRenderer::new);
        EntityRenderers.register(LOTREntities.LIONESS, LOTRLionRenderer::new);
        EntityRenderers.register(LOTREntities.GEMSBOK, LOTRGemsbokRenderer::gemsbok);
        EntityRenderers.register(LOTREntities.FLAMINGO, LOTRFlamingoRenderer::new);
        EntityRenderers.register(LOTREntities.DIK_DIK, LOTRDikDikRenderer::new);
        EntityRenderers.register(LOTREntities.DEER, LOTRDeerRenderer::new);
        EntityRenderers.register(LOTREntities.TRADER_RESPAWN, LOTRTraderRespawnRenderer::new);
        EntityRenderers.register(LOTREntities.NPC_RESPAWNER, LOTRNPCRespawnerRenderer::new);
        LOTRBrandingIronItem.openScreen = () -> Minecraft.getInstance().setScreenAndShow(new LOTRBrandingIronScreen());
        LOTRNPCRespawnerEntity.clientCreative = () -> Minecraft.getInstance().player != null
                && Minecraft.getInstance().player.isCreative();
        EntityRenderers.register(LOTREntities.HOBBIT, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.HOBBIT_BARTENDER, LOTRHobbitRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.HOBBIT_BOUNDER, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.HOBBIT_SHIRRIFF, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.HOBBIT_ORCHARDER, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.HOBBIT_FARMER, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.HOBBIT_FARMHAND, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_MAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_SOLDIER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_ARCHER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDORIAN_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_TOWER_GUARD, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_LEVYMAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_FARMER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_FARMHAND, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_LUMBERMAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_FISHMONGER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_BLACKSMITH, LOTRGondorManRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.GONDOR_BARTENDER, LOTRGondorManRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.GONDOR_GREENGROCER, LOTRGondorManRenderer.trader("outfit_greengrocer"));
        EntityRenderers.register(LOTREntities.GONDOR_MASON, LOTRGondorManRenderer.trader("outfit_mason"));
        EntityRenderers.register(LOTREntities.GONDOR_BREWER, LOTRGondorManRenderer.trader("outfit_brewer"));
        EntityRenderers.register(LOTREntities.GONDOR_FLORIST, LOTRGondorManRenderer.trader("outfit_florist"));
        EntityRenderers.register(LOTREntities.GONDOR_BUTCHER, LOTRGondorManRenderer.trader("outfit_butcher"));
        EntityRenderers.register(LOTREntities.GONDOR_BAKER, LOTRGondorManRenderer.trader("outfit_baker"));
        EntityRenderers.register(LOTREntities.GONDOR_RENEGADE, LOTRGondorManRenderer::renegade);
        EntityRenderers.register(LOTREntities.SWAN_KNIGHT, LOTRGondorManRenderer::swanKnight);
        EntityRenderers.register(LOTREntities.DOL_AMROTH_CAPTAIN, LOTRGondorManRenderer::swanKnight);
        EntityRenderers.register(LOTREntities.DOL_AMROTH_BANNER_BEARER, LOTRGondorManRenderer::swanKnight);
        EntityRenderers.register(LOTREntities.LOSSARNACH_AXEMAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LOSSARNACH_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PELARGIR_MARINE, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PELARGIR_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PINNATH_GELIN_SOLDIER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PINNATH_GELIN_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.BLACKROOT_SOLDIER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.BLACKROOT_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.BLACKROOT_ARCHER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_AMROTH_SOLDIER, LOTRGondorManRenderer::swanKnight);
        EntityRenderers.register(LOTREntities.DOL_AMROTH_ARCHER, LOTRGondorManRenderer::swanKnight);
        EntityRenderers.register(LOTREntities.LEBENNIN_LEVYMAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LEBENNIN_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LOSSARNACH_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PELARGIR_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.PINNATH_GELIN_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.BLACKROOT_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LEBENNIN_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LAMEDON_SOLDIER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LAMEDON_ARCHER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LAMEDON_BANNER_BEARER, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LAMEDON_CAPTAIN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.LAMEDON_HILLMAN, LOTRGondorManRenderer::new);
        EntityRenderers.register(LOTREntities.GALADHRIM_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.GALADHRIM_WARRIOR, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.GALADHRIM_TRADER, LOTRElfRenderer.cloaked("galadhrim_trader_cloak"));
        EntityRenderers.register(LOTREntities.GALADHRIM_LORD, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.GALADHRIM_BANNER_BEARER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.GALADHRIM_SMITH, LOTRElfRenderer.cloaked("galadhrim_smith_cloak", "galadhrim_smith_cape"));
        EntityRenderers.register(LOTREntities.GALADHRIM_WARDEN, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.HIGH_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.HIGH_ELF_WARRIOR, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.HIGH_ELF_LORD, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.HIGH_ELF_BANNER_BEARER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.HIGH_ELF_SMITH, LOTRElfRenderer.cloaked("high_elf_smith_cloak", "high_elf_smith_cape"));
        EntityRenderers.register(LOTREntities.RIVENDELL_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.RIVENDELL_WARRIOR, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.RIVENDELL_LORD, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.RIVENDELL_BANNER_BEARER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.RIVENDELL_SMITH, LOTRElfRenderer.cloaked("rivendell_smith_cloak", "rivendell_smith_cape"));
        EntityRenderers.register(LOTREntities.RIVENDELL_TRADER, LOTRElfRenderer.cloaked("rivendell_trader_cloak"));
        EntityRenderers.register(LOTREntities.WOOD_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.WOOD_ELF_SCOUT, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.WOOD_ELF_WARRIOR, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.WOOD_ELF_CAPTAIN, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.WOOD_ELF_BANNER_BEARER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.WOOD_ELF_SMITH, LOTRElfRenderer.cloaked("wood_elf_smith_cloak", "wood_elf_smith_cape"));
        EntityRenderers.register(LOTREntities.DORWINION_MAN, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_GUARD, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_CAPTAIN, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_BANNER_BEARER, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF_WARRIOR, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF_BANNER_BEARER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF_CAPTAIN, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF_VINTNER, LOTRElfRenderer.cloaked("dorwinion_vintner_cloak", "dorwinion_vintner_cape"));
        EntityRenderers.register(LOTREntities.DORWINION_VINEHAND, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_VINEKEEPER, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_MERCHANT_ELF, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_CROSSBOWER, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_MERCHANT_MAN, LOTRDorwinionManRenderer::new);
        EntityRenderers.register(LOTREntities.DORWINION_ELF_ARCHER, LOTRElfRenderer::new);
        EntityRenderers.register(LOTREntities.DWARF, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.DWARF_WARRIOR, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.DWARF_MINER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.DWARF_COMMANDER, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.COMMANDER));
        EntityRenderers.register(LOTREntities.DWARF_AXE_THROWER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.DWARF_BANNER_BEARER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF_WARRIOR, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF_AXE_THROWER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF_BANNER_BEARER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF_COMMANDER, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.COMMANDER));
        EntityRenderers.register(LOTREntities.BLUE_DWARF_MINER, LOTRDwarfRenderer::new);
        EntityRenderers.register(LOTREntities.BLUE_DWARF_MERCHANT, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.COMMANDER));
        EntityRenderers.register(LOTREntities.IRON_HILLS_MERCHANT, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.COMMANDER));
        EntityRenderers.register(LOTREntities.SCRAP_TRADER, LOTRScrapTraderRenderer::new);
        EntityRenderers.register(LOTREntities.BANDIT, LOTRBanditRenderer.of("bandit"));
        EntityRenderers.register(LOTREntities.BANDIT_HARAD, LOTRBanditRenderer.of("harad"));
        EntityRenderers.register(LOTREntities.DWARF_SMITH, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.SMITH));
        EntityRenderers.register(LOTREntities.BLUE_DWARF_SMITH, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.SMITH));
        EntityRenderers.register(LOTREntities.WICKED_DWARF, LOTRDwarfRenderer.of(LOTRDwarfRenderer.Kind.WICKED));
        EntityRenderers.register(LOTREntities.MORDOR_ORC, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_BOMBARDIER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_TRADER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_MERCENARY_CAPTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.BLACK_URUK, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.BLACK_URUK_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.BLACK_URUK_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.NAN_UNGOL_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MINAS_MORGUL_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.BLACK_URUK_CAPTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_ORC, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_ORC_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_ORC_TRADER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_ORC_MERCENARY_CAPTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_URUK, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_URUK_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_ORC, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_ORC_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_ORC_BOMBARDIER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_ORC_TRADER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_ORC_MERCENARY_CAPTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ISENGARD_SNAGA, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.ISENGARD_SNAGA_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_CROSSBOWER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_SAPPER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_BERSERKER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_TRADER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_MERCENARY_CAPTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_HAI_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_GULDUR_ORC, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_GULDUR_ORC_ARCHER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_GULDUR_ORC_CHIEFTAIN, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_GULDUR_ORC_TRADER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.DOL_GULDUR_BANNER_BEARER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_SPIDER_KEEPER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.NURN_SLAVE, LOTRNurnSlaveRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_ORC_SLAVER, LOTROrcRenderer::new);
        EntityRenderers.register(LOTREntities.MIRKWOOD_SPIDER, LOTRSpiderRenderer::mirkwood);
        EntityRenderers.register(LOTREntities.MORDOR_SPIDER, LOTRSpiderRenderer::mordor);
        EntityRenderers.register(LOTREntities.HARNEDHRIM, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_WARRIOR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_ARCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_WARLORD, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_BANNER_BEARER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_BARTENDER, LOTRNearHaradrimRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.HARNEDOR_BLACKSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_FARMER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_FARMHAND, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_BAKER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_BREWER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_BUTCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_FISHMONGER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_HUNTER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_LUMBERMAN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_MASON, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARNEDOR_MINER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_WARRIOR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_ARCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_BANNER_BEARER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_CHIEFTAIN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_MERCHANT, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_MASON, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_BREWER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_MINER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NOMAD_ARMOURER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_HARADRIM, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_WARRIOR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_ARCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BANNER_BEARER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_WARLORD, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BLACKSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_MASON, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BUTCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BREWER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_FISHMONGER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BAKER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_MINER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_GOLDSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_LUMBERMAN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_HUNTER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.GULF_BARTENDER, LOTRNearHaradrimRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.GULF_FARMER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_WARRIOR, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BANNER_BEARER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_ARCHER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BLACKSMITH, LOTREasterlingRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.EASTERLING_WARLORD, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_FIRE_THROWER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_LEVYMAN, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_GOLD_WARRIOR, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_LUMBERMAN, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_MASON, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BUTCHER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BREWER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_FISHMONGER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BAKER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_HUNTER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_FARMER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_GOLDSMITH, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_BARTENDER, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.EASTERLING_FARMHAND, LOTREasterlingRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_WARRIOR, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_BANNER_BEARER, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_CHIEFTAIN, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_HUNTSMAN, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_HUTMAKER, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.MOREDAIN_MERCENARY, LOTRMoredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_WARRIOR, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_BANNER_BEARER, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_CHIEFTAIN, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_BLOWGUNNER, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_SHAMAN, LOTRTauredainRenderer::shaman);
        EntityRenderers.register(LOTREntities.TAUREDAIN_FARMER, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_FARMHAND, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_SMITH, LOTRTauredainRenderer::new);
        EntityRenderers.register(LOTREntities.HALF_TROLL, LOTRHalfTrollRenderer::new);
        EntityRenderers.register(LOTREntities.HALF_TROLL_WARRIOR, LOTRHalfTrollRenderer::new);
        EntityRenderers.register(LOTREntities.HALF_TROLL_BANNER_BEARER, LOTRHalfTrollRenderer::new);
        EntityRenderers.register(LOTREntities.HALF_TROLL_WARLORD, LOTRHalfTrollRenderer::new);
        EntityRenderers.register(LOTREntities.HALF_TROLL_SCAVENGER, LOTRHalfTrollRenderer::scavenger);
        EntityRenderers.register(LOTREntities.TROLL, LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.TROLL));
        EntityRenderers.register(LOTREntities.OLOG_HAI, LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.OLOG_HAI));
        EntityRenderers.register(LOTREntities.MIRK_TROLL, LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.MIRK_TROLL));
        EntityRenderers.register(LOTREntities.MOUNTAIN_TROLL, LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.MOUNTAIN_TROLL));
        EntityRenderers.register(LOTREntities.MOUNTAIN_TROLL_CHIEFTAIN,
                LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.MOUNTAIN_TROLL_CHIEFTAIN));
        EntityRenderers.register(LOTREntities.SNOW_TROLL, LOTRTrollRenderer.of(LOTRTrollRenderer.Kind.SNOW_TROLL));
        EntityRenderers.register(LOTREntities.ENT, LOTREntRenderer::new);
        EntityRenderers.register(LOTREntities.MALLORN_ENT, LOTRMallornEntRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_MAN, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_LEVYMAN, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_SOLDIER, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_ARCHER, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_BANNER_BEARER, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_CAPTAIN, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.DALE_BLACKSMITH, LOTRDaleManRenderer.trader("blacksmith_apron"));
        EntityRenderers.register(LOTREntities.DALE_BAKER, LOTRDaleManRenderer.trader("baker_apron"));
        EntityRenderers.register(LOTREntities.DALE_MERCHANT, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.ESGAROTH_BANNER_BEARER, LOTRDaleManRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_NORTH, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_ITHILIEN, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.DUNEDAIN, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_NORTH_CAPTAIN, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_NORTH_BANNER_BEARER, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_ITHILIEN_CAPTAIN, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.RANGER_ITHILIEN_BANNER_BEARER, LOTRDunedainRenderer::new);
        EntityRenderers.register(LOTREntities.DUNEDAIN_BLACKSMITH, LOTRDunedainRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.GOLLUM, LOTRGollumRenderer::new);
        EntityRenderers.register(LOTREntities.SARUMAN, LOTRSarumanRenderer::new);
        EntityRenderers.register(LOTREntities.GANDALF, LOTRGandalfRenderer::new);
        EntityRenderers.register(LOTREntities.HUORN, LOTRHuornRenderer::new);
        EntityRenderers.register(LOTREntities.DARK_HUORN, LOTRHuornRenderer::new);
        EntityRenderers.register(LOTREntities.GONDOR_RUINS_WRAITH, LOTRSkeletalWraithRenderer::new);
        EntityRenderers.register(LOTREntities.MARSH_WRAITH, LOTRMarshWraithRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_BARROW_WRAITH, LOTRSkeletalWraithRenderer::new);
        EntityRenderers.register(LOTREntities.HARAD_PYRAMID_WRAITH, LOTRSkeletalWraithRenderer::new);
        EntityRenderers.register(LOTREntities.BARROW_WIGHT, LOTRBarrowWightRenderer::new);
        EntityRenderers.register(LOTREntities.TAUREDAIN_PYRAMID_WRAITH, LOTRSkeletalWraithRenderer::new);
        EntityRenderers.register(LOTREntities.UMBARIAN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_WARRIOR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_ARCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_CAPTAIN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_BANNER_BEARER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_BARTENDER, LOTRNearHaradrimRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.UMBAR_BLACKSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_FARMER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_BAKER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_BREWER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_BUTCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_FISHMONGER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_FLORIST, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_GOLDSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_LUMBERMAN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_MASON, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.UMBAR_MINER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.CORSAIR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.CORSAIR_CAPTAIN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.CORSAIR_SLAVER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.HARAD_SLAVE, LOTRHaradSlaveRenderer::new);
        EntityRenderers.register(LOTREntities.NEAR_HARADRIM, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NEAR_HARADRIM_WARRIOR, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NEAR_HARADRIM_ARCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NEAR_HARADRIM_WARLORD, LOTRNearHaradrimRenderer::warlord);
        EntityRenderers.register(LOTREntities.NEAR_HARAD_BANNER_BEARER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_CHAMPION, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.NEAR_HARAD_BLACKSMITH, LOTRNearHaradrimRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.NEAR_HARAD_MERCHANT, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_BARTENDER, LOTRNearHaradrimRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.SOUTHRON_FARMER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_BAKER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_BREWER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_BUTCHER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_FISHMONGER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_FLORIST, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_GOLDSMITH, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_LUMBERMAN, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_MASON, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.SOUTHRON_MINER, LOTRNearHaradrimRenderer::new);
        EntityRenderers.register(LOTREntities.DUNLENDING, LOTRDunlendingRenderer::dunlending);
        EntityRenderers.register(LOTREntities.DUNLENDING_WARRIOR, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.DUNLENDING_ARCHER, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.DUNLENDING_AXE_THROWER, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.DUNLENDING_BERSERKER, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.DUNLENDING_WARLORD, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.DUNLENDING_BARTENDER, LOTRDunlendingRenderer::dunlending);
        EntityRenderers.register(LOTREntities.DUNLENDING_BANNER_BEARER, LOTRDunlendingRenderer::warrior);
        EntityRenderers.register(LOTREntities.ANGMAR_HILLMAN, LOTRAngmarHillmanRenderer::hillman);
        EntityRenderers.register(LOTREntities.ANGMAR_HILLMAN_WARRIOR, LOTRAngmarHillmanRenderer::warrior);
        EntityRenderers.register(LOTREntities.ANGMAR_HILLMAN_CHIEFTAIN, LOTRAngmarHillmanRenderer::warrior);
        EntityRenderers.register(LOTREntities.ANGMAR_HILLMAN_BANNER_BEARER, LOTRAngmarHillmanRenderer::warrior);
        EntityRenderers.register(LOTREntities.ANGMAR_HILLMAN_AXE_THROWER, LOTRAngmarHillmanRenderer::warrior);
        EntityRenderers.register(LOTREntities.ROHAN_MAN, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHIRRIM_ARCHER, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHIRRIM_MARSHAL, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_SHIELDMAIDEN, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_BANNER_BEARER, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_FARMHAND, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_FARMER, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_LUMBERMAN, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_FISHMONGER, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_STABLEMASTER, LOTRRohirrimRenderer::new);
        EntityRenderers.register(LOTREntities.ROHAN_BLACKSMITH, LOTRRohirrimRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.ROHAN_MEADHOST, LOTRRohirrimRenderer.trader("outfit_meadhost"));
        EntityRenderers.register(LOTREntities.ROHAN_BUILDER, LOTRRohirrimRenderer.trader("outfit_builder"));
        EntityRenderers.register(LOTREntities.ROHAN_BREWER, LOTRRohirrimRenderer.trader("outfit_brewer"));
        EntityRenderers.register(LOTREntities.ROHAN_BUTCHER, LOTRRohirrimRenderer.trader("outfit_butcher"));
        EntityRenderers.register(LOTREntities.ROHAN_BAKER, LOTRRohirrimRenderer.trader("outfit_baker"));
        EntityRenderers.register(LOTREntities.ROHAN_ORCHARDER, LOTRRohirrimRenderer.trader("outfit_orcharder"));
        EntityRenderers.register(LOTREntities.BREE_MAN, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_GUARD, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_BANNER_BEARER, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_CAPTAIN, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_BLACKSMITH, LOTRBreeManRenderer.trader("outfit_blacksmith"));
        EntityRenderers.register(LOTREntities.BREE_INNKEEPER, LOTRBreeManRenderer.trader("outfit_innkeeper"));
        EntityRenderers.register(LOTREntities.BREE_HOBBIT, LOTRHobbitRenderer::new);
        EntityRenderers.register(LOTREntities.RUFFIAN_SPY, LOTRBreeManRenderer::ruffian);
        EntityRenderers.register(LOTREntities.RUFFIAN_BRUTE, LOTRBreeManRenderer::ruffian);
        EntityRenderers.register(LOTREntities.BREE_HOBBIT_INNKEEPER, LOTRHobbitRenderer.trader("outfit_bartender"));
        EntityRenderers.register(LOTREntities.BREE_BAKER, LOTRBreeManRenderer.trader("outfit_baker"));
        EntityRenderers.register(LOTREntities.BREE_BUTCHER, LOTRBreeManRenderer.trader("outfit_butcher"));
        EntityRenderers.register(LOTREntities.BREE_BREWER, LOTRBreeManRenderer.trader("outfit_brewer"));
        EntityRenderers.register(LOTREntities.BREE_MASON, LOTRBreeManRenderer.trader("outfit_mason"));
        EntityRenderers.register(LOTREntities.BREE_LUMBERMAN, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_FLORIST, LOTRBreeManRenderer.trader("outfit_florist"));
        EntityRenderers.register(LOTREntities.BREE_FARMER, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_FARMHAND, LOTRBreeManRenderer::new);
        EntityRenderers.register(LOTREntities.BREE_HOBBIT_BAKER, LOTRHobbitRenderer.trader("outfit_baker"));
        EntityRenderers.register(LOTREntities.BREE_HOBBIT_BUTCHER, LOTRHobbitRenderer.trader("outfit_butcher"));
        EntityRenderers.register(LOTREntities.BREE_HOBBIT_BREWER, LOTRHobbitRenderer.trader("outfit_brewer"));
        EntityRenderers.register(LOTREntities.BREE_HOBBIT_FLORIST, LOTRHobbitRenderer.trader("outfit_florist"));
        EntityRenderers.register(LOTREntities.AUROCHS, LOTRAurochsRenderer::aurochs);
        EntityRenderers.register(LOTREntities.KINE_OF_ARAW, LOTRAurochsRenderer::kineAraw);
        EntityRenderers.register(LOTREntities.BEAR, LOTRBearRenderer::new);
        EntityRenderers.register(LOTREntities.WHITE_ORYX, LOTRGemsbokRenderer::whiteOryx);
        EntityRenderers.register(LOTREntities.CROCODILE, LOTRCrocodileRenderer::new);
        EntityRenderers.register(LOTREntities.JUNGLE_SCORPION, LOTRScorpionRenderer::new);
        EntityRenderers.register(LOTREntities.DESERT_SCORPION, LOTRScorpionRenderer::new);
        EntityRenderers.register(LOTREntities.TERMITE, LOTRTermiteRenderer::new);
        EntityRenderers.register(LOTREntities.LION_RUG, LOTRRugRenderer::lion);
        EntityRenderers.register(LOTREntities.BEAR_RUG, LOTRRugRenderer::bear);
        EntityRenderers.register(LOTREntities.WARGSKIN_RUG, LOTRRugRenderer::warg);
        EntityRenderers.register(LOTREntities.MORDOR_WARG, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.MORDOR_WARG_BOMBARDIER, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.GUNDABAD_WARG, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_WARG, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.ANGMAR_WARG_BOMBARDIER, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_WARG, LOTRWargRenderer::new);
        EntityRenderers.register(LOTREntities.URUK_WARG_BOMBARDIER, LOTRWargRenderer::new);
        LOTRRandomSkins.init();
        LOTRSpeechClient.init();
        EntityRenderers.register(LOTREntities.FALLING_TREASURE,
                FallingBlockRenderer::new);
        EntityRenderers.register(LOTREntities.GANDALF_FIREBALL,
                LOTRGandalfFireballRenderer::new);

        // A plain Horn of Command opens its selection screen instead of being
        // blown. Opened client-side, the way the beacon's naming dialog is, so
        // no client class leaks into the common source set; the choice travels
        // back as LOTRHornModePayload.
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack held = player.getItemInHand(hand);
            // Main hand only: the choice is applied to the main-hand item.
            if (level.isClientSide() && hand == net.minecraft.world.InteractionHand.MAIN_HAND
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
                LOTRMugRenderer::new);
        BlockEntityRenderers.register(LOTRBlockEntities.PLATE,
                LOTRPlateRenderer::new);
        // A thrown plate, drawn as the plate itself, spinning.
        EntityRenderers.register(LOTREntities.PLATE,
                LOTRPlateEntityRenderer::new);
        // A plate worn in the helmet slot.
        LOTRPlateHeadRenderer.init();
        // LOTRTickHandlerClient: nausea drags the view about.
        LOTRDrunkCamera.init();
        LOTRAnimalJarSpecialRenderer.init();
        LOTRSnowyStoneModel.init();
        LOTRScrapTraderMisbehaviour.init();
        LOTRClientFactionState.init();

        // Carved signs: the lettering in the world, and the screen a chisel opens.
        BlockEntityRenderers.register(LOTRBlockEntities.CARVED_SIGN,
                LOTRCarvedSignRenderer::new);
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
                LOTROpenSignEditorPayload.TYPE, (payload, context) ->
                        context.client().execute(() -> {
                            Minecraft client = context.client();
                            if (client.level != null && client.level.getBlockEntity(payload.pos())
                                    instanceof LOTRCarvedSignBlockEntity sign) {
                                client.setScreenAndShow(new LOTRCarvedSignEditScreen(sign));
                            }
                        }));

        MenuScreens.register(LOTRMenus.BARREL,
                LOTRBarrelScreen::new);
        MenuScreens.register(LOTRMenus.FORGE, LOTRForgeScreen::new);
        MenuScreens.register(LOTRMenus.HOBBIT_OVEN, LOTRHobbitOvenScreen::new);
        MenuScreens.register(LOTRMenus.UNSMELTERY, LOTRUnsmelteryScreen::new);
        MenuScreens.register(LOTRMenus.MILLSTONE, LOTRMillstoneScreen::new);
        MenuScreens.register(LOTRMenus.ANVIL,
                LOTRAnvilScreen::new);
        MenuScreens.register(LOTRMenus.DALE_CRACKER,
                LOTRDaleCrackerScreen::new);
        MenuScreens.register(LOTRMenus.TRADE, LOTRTradeScreen::new);
        MenuScreens.register(LOTRMenus.COIN_EXCHANGE, LOTRCoinExchangeScreen::new);
        MenuScreens.register(LOTRMenus.SMITH, LOTRAnvilScreen::new);
        MenuScreens.register(LOTRMenus.UNIT_TRADE, LOTRUnitTradeScreen::new);
        MenuScreens.register(LOTRMenus.HIRED_WARRIOR_INVENTORY, LOTRHiredWarriorInventoryScreen::new);
        MenuScreens.register(LOTRMenus.HIRED_FARMER_INVENTORY, LOTRHiredFarmerInventoryScreen::new);
        MenuScreens.register(LOTRMenus.NPC_MOUNT_INVENTORY, LOTRNPCMountInventoryScreen::new);
        MenuScreens.register(LOTRMenus.GOLLUM, LOTRGollumScreen::new);

        // The LOTR menu: its key, and the one-time messages (LOTRPacketMessage).
        LOTRKeyBindings.register();
        LOTRAlignmentTicker.init();
        LOTRAlignmentHud.init();
        LOTRPlateFallingInfo.init();
        ClientPlayNetworking.registerGlobalReceiver(LOTRRespawnerPayloads.Open.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (context.client().level != null && context.client().level.getEntity(payload.entityId())
                            instanceof LOTRNPCRespawnerEntity spawner) {
                        spawner.readSpawnerData(payload.data());
                        context.client().setScreenAndShow(new LOTRNPCRespawnerScreen(spawner));
                    }
                }));
        LOTREnvironmentOverlayHud.init();
        LOTRSpiderClimbHud.init();
        PictureInPictureRendererRegistry.register(context -> new LOTRCompassRenderer());
        EntityRenderers.register(LOTREntities.ALIGNMENT_BONUS, LOTRAlignmentBonusRenderer::new);
        EntityRenderers.register(LOTREntities.SWORD_COMMAND_MARKER, LOTRSwordCommandMarkerRenderer::new);
        // LOTRPacketLocationFX SWORD_COMMAND: the marker six blocks above the spot, to fall onto it.
        ClientPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.SwordCommandFX.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    var level = context.client().level;
                    if (level != null) {
                        var marker = LOTREntities.SWORD_COMMAND_MARKER.create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
                        if (marker != null) {
                            marker.setPos(payload.x(), payload.y() + 6.0, payload.z());
                            level.addEntity(marker);
                        }
                    }
                }));
        ClientPlayNetworking.registerGlobalReceiver(LOTRMenuPayloads.AlignmentSee.TYPE, (payload, context) ->
                context.client().execute(() -> context.client().setScreenAndShow(
                        new LOTRFactionsScreen().setOtherPlayer(payload.username(), payload.alignments()))));
        ClientPlayNetworking.registerGlobalReceiver(LOTRMenuPayloads.Message.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    LOTRGuiMessageTypes[] types = LOTRGuiMessageTypes.values();
                    if (payload.message() >= 0 && payload.message() < types.length) {
                        context.client().setScreenAndShow(new LOTRMessageScreen(types[payload.message()]));
                    }
                }));

        ClientPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.OpenSquadronItem.TYPE, (payload, context) ->
                context.client().execute(() -> context.client().setScreenAndShow(new LOTRSquadronItemScreen())));

        // LOTRPacketHiredGui: the unit's state, and openHiredNPCGui by its task.
        ClientPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.HiredGui.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (context.player().level().getEntity(payload.entityId()) instanceof LOTRNPCEntity npc
                            && npc.hiredNPCInfo.getHiringPlayer() == context.player()) {
                        npc.hiredNPCInfo.receiveClientPacket(payload);
                        if (payload.openGui()) {
                            if (npc.hiredNPCInfo.getTask() == LOTRHiredTask.WARRIOR) {
                                context.client().setScreenAndShow(new LOTRHiredWarriorScreen(npc));
                            } else if (npc.hiredNPCInfo.getTask() == LOTRHiredTask.FARMER) {
                                context.client().setScreenAndShow(new LOTRHiredFarmerScreen(npc));
                            }
                        }
                    }
                }));

        // LOTRPacketTraderInfo: only into the trade screen open on that trader.
        ClientPlayNetworking.registerGlobalReceiver(LOTRTradePayloads.TraderInfo.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (context.player().containerMenu instanceof LOTRTradeMenu menu && menu.theTraderNPC != null
                            && menu.theTraderNPC.getId() == payload.entityId() && menu.theTraderNPC.traderNPCInfo != null) {
                        menu.theTraderNPC.traderNPCInfo.receiveClientPacket(payload.data());
                    }
                }));
        ClientPlayNetworking.registerGlobalReceiver(LOTRTradePayloads.OpenInteract.TYPE, (payload, context) ->
                context.client().execute(() -> {
                    if (context.player().level().getEntity(payload.entityId()) instanceof LOTRNPCEntity npc) {
                        context.client().setScreenAndShow(switch (payload.kind()) {
                            case LOTRTradePayloads.OpenInteract.TRADE_UNIT_TRADE -> new LOTRTradeInteractScreen(npc, true);
                            case LOTRTradePayloads.OpenInteract.UNIT_TRADE, LOTRTradePayloads.OpenInteract.MERCENARY ->
                                    new LOTRUnitTradeInteractScreen(npc);
                            case LOTRTradePayloads.OpenInteract.HIRED -> new LOTRHiredInteractScreen(npc);
                            default -> new LOTRTradeInteractScreen(npc, false);
                        });
                    }
                }));

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