package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFastTravel;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Input;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTREventHandler.onServerChat: a player's title goes before their name in what they say.
 * LOTRNetHandlerPlayServer: moving -- pressing to walk or jump, or arriving somewhere new on foot --
 * stops a fast travel's countdown.
 */
@Mixin(ServerGamePacketListenerImpl.class)
abstract class LOTRServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @WrapOperation(method = "broadcastChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/ChatType;bind(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/ChatType$Bound;"))
    private ChatType.Bound lotr$titledName(ResourceKey<ChatType> chatType, Entity entity, Operation<ChatType.Bound> original) {
        return LOTRPlayerTitles.withTitle(original.call(chatType, entity), this.player);
    }

    @Inject(method = "handlePlayerInput", at = @At("TAIL"))
    private void lotr$inputCancelsFastTravel(ServerboundPlayerInputPacket packet, CallbackInfo ci) {
        Input input = packet.input();
        if (input.forward() || input.backward() || input.left() || input.right() || input.jump()) {
            LOTRFastTravel.cancelFastTravel(this.player);
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void lotr$moveCancelsFastTravel(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!this.player.isPassenger() && (packet.getX(this.player.getX()) != this.player.getX()
                || packet.getY(this.player.getY()) != this.player.getY() || packet.getZ(this.player.getZ()) != this.player.getZ())) {
            LOTRFastTravel.cancelFastTravel(this.player);
        }
    }
}
