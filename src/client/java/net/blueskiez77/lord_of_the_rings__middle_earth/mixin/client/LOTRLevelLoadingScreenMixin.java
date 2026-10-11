package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTRGuiDownloadTerrain: loading into Middle-earth, in place of the terrain's progress, the sepia map
 * where the player stands, between bars of the menu's background, under a vignette, and "Entering
 * Middle-earth"; loading into Utumno, a black screen and "Entering the pits of Utumno...", fading to
 * black over six seconds.
 */
@Mixin(LevelLoadingScreen.class)
abstract class LOTRLevelLoadingScreenMixin extends Screen {

    @Unique
    private final LOTRMapRenderer lotr$mapRenderer = new LOTRMapRenderer();

    private LOTRLevelLoadingScreenMixin(Component title) {
        super(title);
    }

    /** tickCounter. */
    @Unique
    private int lotr$tickCounter;

    @Inject(method = "tick", at = @At("HEAD"))
    private void lotr$tick(CallbackInfo ci) {
        ++this.lotr$tickCounter;
    }

    @Unique
    private boolean lotr$inUtumno() {
        LocalPlayer player = this.minecraft.player;
        return player != null && player.level().dimension() == LOTRWorldGen.UTUMNO;
    }

    @Unique
    private @Nullable LocalPlayer lotr$playerInMiddleEarth() {
        LocalPlayer player = this.minecraft.player;
        return player != null && player.level().dimension() == LOTRWorldGen.MIDDLE_EARTH ? player : null;
    }

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void lotr$middleEarthMap(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (lotr$inUtumno()) {
            graphics.fill(0, 0, this.width, this.height, 0xFF000000);
            ci.cancel();
            return;
        }
        LocalPlayer player = lotr$playerInMiddleEarth();
        if (player == null) {
            return;
        }
        extractMenuBackground(graphics);
        int x0 = 0;
        int x1 = this.width;
        int y0 = 40;
        int y1 = this.height - 40;
        this.lotr$mapRenderer.setView(LOTRMapCoords.worldToMapX(player.getX()), LOTRMapCoords.worldToMapZ(player.getZ()),
                -0.3f, (float) Math.pow(2.0, -0.3f));
        this.lotr$mapRenderer.renderMap(graphics, this.font, player, true, x0, y0, x1, y1);
        LOTRMapRenderer.drawVignettes(graphics, 1, x0, y0, x1, y1, this.width, this.height);
        ci.cancel();
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void lotr$enteringMiddleEarth(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (lotr$inUtumno()) {
            float brightness = 1.0f - this.lotr$tickCounter / 120.0f;
            int brightnessI = net.minecraft.util.Mth.clamp((int) (brightness * 255.0f), 0, 255);
            graphics.centeredText(this.font, Component.translatable("lotr.loading.enterUtumno"), this.width / 2, this.height / 2 - 50,
                    0xFF000000 | brightnessI << 16 | brightnessI << 8 | brightnessI);
            ci.cancel();
            return;
        }
        if (lotr$playerInMiddleEarth() == null) {
            return;
        }
        graphics.centeredText(this.font, Component.translatable("lotr.loading.enterME"), this.width / 2, this.height / 2 - 50, 0xFFFFFFFF);
        ci.cancel();
    }
}
