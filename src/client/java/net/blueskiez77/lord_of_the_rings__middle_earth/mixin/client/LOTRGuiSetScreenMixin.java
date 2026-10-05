package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMainMenuScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** LOTRGuiHandler.onGuiOpen: with "Custom main menu", the title screen is the mod's own. */
@Mixin(Gui.class)
abstract class LOTRGuiSetScreenMixin {

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private @Nullable Screen lotr$mainMenu(@Nullable Screen screen) {
        if (LOTRConfig.customMainMenu && screen != null && screen.getClass() == TitleScreen.class) {
            return new LOTRMainMenuScreen();
        }
        return screen;
    }
}
