package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRChestWithPouchMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRPouchMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRSpawnerChests;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * The pouch's packets (LOTRPacketRenamePouch, LOTRPacketRestockPouches) and its use on a chest
 * (onItemUseFirst, before the chest itself opens) or a chest minecart (LOTREventHandler's
 * onMinecartInteract).
 */
public final class LOTRPouchNetworking {

    private LOTRPouchNetworking() {
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> payloadType(String name) {
        return new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
    }

    /** LOTRPacketRenamePouch. */
    public record RenamePayload(String name) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RenamePayload> TYPE = payloadType("rename_pouch");
        public static final StreamCodec<RegistryFriendlyByteBuf, RenamePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.stringUtf8(50), RenamePayload::name, RenamePayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketRestockPouches. */
    public record RestockPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RestockPayload> TYPE = payloadType("restock_pouches");
        public static final StreamCodec<RegistryFriendlyByteBuf, RestockPayload> STREAM_CODEC =
                StreamCodec.unit(new RestockPayload());

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(RenamePayload.TYPE, RenamePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(RestockPayload.TYPE, RestockPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RenamePayload.TYPE, (payload, context) -> {
            if (context.player().containerMenu instanceof LOTRPouchMenu menu) {
                menu.renamePouch(payload.name());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(RestockPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (LOTRPouchItem.restockPouches(player)) {
                player.containerMenu.broadcastChanges();
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.HORSE_SADDLE.value(),
                        SoundSource.PLAYERS, 0.5f, 1.0f);
            }
        });
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if (player.isSpectator() || !LOTRPouchItem.isPouch(player.getItemInHand(hand))) {
                return InteractionResult.PASS;
            }
            BlockPos pos = hit.getBlockPos();
            Container chest = getChestInvAt(player, level, pos);
            if (chest == null) {
                return InteractionResult.PASS;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                openChestWithPouch(serverPlayer, slotOf(player, hand), chest);
            }
            return InteractionResult.SUCCESS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if (!(entity instanceof MinecartChest minecart) || player.isSpectator()
                    || !LOTRPouchItem.isPouch(player.getItemInHand(hand))) {
                return InteractionResult.PASS;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                openChestWithPouch(serverPlayer, slotOf(player, hand), minecart);
            }
            return InteractionResult.SUCCESS;
        });
    }

    private static int slotOf(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : Inventory.SLOT_OFFHAND;
    }

    /**
     * getChestInvAt: a chest (a double one whole, unless something sits on it), the mod's chests, or
     * the player's ender chest if nothing solid is on it; never a spawner chest still holding its
     * creature.
     */
    public static @Nullable Container getChestInvAt(Player player, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null && be.hasAttached(LOTRSpawnerChests.SPAWNER_MOB)) {
            return null;
        }
        if (state.getBlock() instanceof ChestBlock chestBlock) {
            return ChestBlock.getContainer(chestBlock, state, level, pos, false);
        }
        if (state.getBlock() instanceof EnderChestBlock && be instanceof EnderChestBlockEntity enderChest
                && !level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above())) {
            PlayerEnderChestContainer enderInv = player.getEnderChestInventory();
            if (!level.isClientSide()) {
                enderInv.setActiveChest(enderChest);
            }
            return enderInv;
        }
        return null;
    }

    private static void openChestWithPouch(ServerPlayer player, int pouchSlot, Container chest) {
        Component title = chest instanceof net.minecraft.world.Nameable nameable ? nameable.getDisplayName()
                : Component.translatable("container.chest");
        player.openMenu(new ExtendedMenuProvider<LOTRChestWithPouchMenu.OpeningData>() {
            @Override
            public LOTRChestWithPouchMenu.OpeningData getScreenOpeningData(ServerPlayer p) {
                return new LOTRChestWithPouchMenu.OpeningData(pouchSlot, chest.getContainerSize() / 9);
            }

            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player p) {
                return new LOTRChestWithPouchMenu(containerId, inv, pouchSlot, chest);
            }
        });
    }
}
