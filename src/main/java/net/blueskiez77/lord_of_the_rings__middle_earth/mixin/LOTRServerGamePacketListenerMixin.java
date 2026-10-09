package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;

import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** LOTREventHandler.onServerChat: a player's title goes before their name in what they say. */
@Mixin(ServerGamePacketListenerImpl.class)
abstract class LOTRServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @WrapOperation(method = "broadcastChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/ChatType;bind(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/ChatType$Bound;"))
    private ChatType.Bound lotr$titledName(ResourceKey<ChatType> chatType, Entity entity, Operation<ChatType.Bound> original) {
        return LOTRPlayerTitles.withTitle(original.call(chatType, entity), this.player);
    }
}
