package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMainMenuScreen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** On the mod's main menu, the Realms notification icons follow the Realms button to where it now stands. */
@Mixin(TitleScreen.class)
abstract class LOTRTitleScreenMixin {

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE",
            target = "Lcom/mojang/realmsclient/gui/screens/RealmsNotificationsScreen;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"))
    private void lotr$notificationsAtRealmsButton(RealmsNotificationsScreen notifications, GuiGraphicsExtractor graphics,
                                                  int mouseX, int mouseY, float partialTick, Operation<Void> original) {
        if ((Object) this instanceof LOTRMainMenuScreen menu) {
            int[] offset = menu.realmsNotificationOffset(notifications);
            graphics.pose().pushMatrix();
            graphics.pose().translate(offset[0], offset[1]);
            original.call(notifications, graphics, mouseX - offset[0], mouseY - offset[1], partialTick);
            graphics.pose().popMatrix();
        } else {
            original.call(notifications, graphics, mouseX, mouseY, partialTick);
        }
    }
}
