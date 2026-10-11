package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTRWorldTypeMiddleEarth.showWorldInfoNotice: the Middle-earth and M.E. Classic
 * world types' notes ("Spawn directly into Middle-earth") on the World Type button, as
 * vanilla gives amplified its own -- set after vanilla's listener, which
 * clears the tooltip for any other type.
 */
@Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$WorldTab")
public abstract class LOTRCreateWorldTabMixin extends GridLayoutTab {


    private LOTRCreateWorldTabMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lotr$middleEarthInfo(CreateWorldScreen screen, CallbackInfo ci) {
        screen.getUiState().addListener(state -> visitChildren(widget -> {
            if (widget instanceof CycleButton<?> button && button.getValue() instanceof WorldCreationUiState.WorldTypeEntry entry) {
                Identifier preset = entry.preset() == null ? null : entry.preset().unwrapKey().map(key -> key.identifier()).orElse(null);
                if (preset != null && "lotr".equals(preset.getNamespace())) {
                    button.setTooltip(Tooltip.create(Component.translatable("generator.lotr." + preset.getPath() + ".info")));
                }
            }
        }));
    }
}
