package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRAbstractWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

/**
 * LOTRGuiFastTravel: the journey. Under four vignettes the sepia map runs from where the player set out
 * to the waypoint, speeding up as it goes and zooming out on the way, then zooming back in on arrival;
 * a bar above says where to ("Fast travelling to ...", its dots counting), and one below gives a line of
 * the fastTravel speech bank. Once the waypoint's chunk has reached the client, the inventory key or
 * Escape skips the rest, and when the map has both arrived and zoomed in the screen closes itself.
 */
public class LOTRFastTravelScreen extends Screen {

    private final LOTRMapRenderer mapRenderer = new LOTRMapRenderer();
    private final LOTRAbstractWaypoint theWaypoint;
    private final String message;
    private int tickCounter;
    private boolean chunkLoaded;
    private boolean playedSound;
    private final float zoomBase;
    private final double mapScaleFactor;
    private float currentZoom;
    private float prevZoom;
    private boolean finishedZoomIn;
    private double mapSpeed;
    private double mapVelX;
    private double mapVelY;
    private boolean reachedWP;
    private double prevMapX;
    private double mapX;
    private double prevMapY;
    private double mapY;

    public LOTRFastTravelScreen(LOTRAbstractWaypoint waypoint, int x, int z) {
        super(Component.translatable("lotr.fastTravel.travel", waypoint.getDisplayName()));
        this.theWaypoint = waypoint;
        this.message = LOTRSpeech.getSpeechBank("fastTravel").getRandomSpeech();
        this.prevMapX = this.mapX = LOTRMapCoords.worldToMapX(x);
        this.prevMapY = this.mapY = LOTRMapCoords.worldToMapZ(z);
        double dx = this.theWaypoint.getX() - this.mapX;
        double dy = this.theWaypoint.getY() - this.mapY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        this.mapScaleFactor = dist / 100.0;
        this.zoomBase = -((float) (Math.log(this.mapScaleFactor * 0.3f) / Math.log(2.0)));
        this.currentZoom = this.prevZoom = this.zoomBase + 0.5f;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        float f = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        this.mapRenderer.setView(this.prevMapX + (this.mapX - this.prevMapX) * f, this.prevMapY + (this.mapY - this.prevMapY) * f,
                this.prevZoom + (this.currentZoom - this.prevZoom) * f, (float) Math.pow(2.0, this.zoomBase));
        this.mapRenderer.renderMap(graphics, this.font, this.minecraft.player, true, 0, 0, this.width, this.height);
        LOTRMapRenderer.drawVignettes(graphics, 4, 0, 0, this.width, this.height, this.width, this.height);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        float f = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        Component title = getTitle();
        String titleExtra = new String[]{"", ".", "..", "..."}[this.tickCounter / 10 % 4];
        List<FormattedCharSequence> messageLines = this.font.split(Component.literal(this.message), this.width - 100);
        Component skipText = Component.translatable("lotr.fastTravel.skip", this.minecraft.options.keyInventory.getTranslatedKeyMessage());
        int boxColor = (int) (0.5f * 255.0f) << 24;
        int fh = this.font.lineHeight;
        int border = fh * 2;
        if (this.chunkLoaded) {
            graphics.fill(0, 0, this.width, border + fh * 3 + border, boxColor);
        } else {
            graphics.fill(0, 0, this.width, border + fh + border, boxColor);
        }
        int messageY = this.height - border - messageLines.size() * fh;
        graphics.fill(0, messageY - border, this.width, this.height, boxColor);
        graphics.text(this.font, Component.empty().append(title).append(titleExtra), this.width / 2 - this.font.width(title) / 2, border,
                0xFFFFFFFF, true);
        for (FormattedCharSequence line : messageLines) {
            graphics.centeredText(this.font, line, this.width / 2, messageY, 0xFFFFFFFF);
            messageY += fh;
        }
        if (this.chunkLoaded) {
            float skipAlpha = triangleWave(this.tickCounter + f, 0.3f, 1.0f, 80.0f);
            int skipColor = 0xFFFFFF | Mth.clamp((int) (skipAlpha * 255.0f), 4, 255) << 24;
            graphics.text(this.font, skipText, this.width / 2 - this.font.width(skipText) / 2, border + fh * 2, skipColor, false);
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    /** LOTRFunctions.triangleWave. */
    private static float triangleWave(float t, float min, float max, float period) {
        return min + (max - min) * (Math.abs(t % period / period - 0.5f) * 2.0f);
    }

    /** keyTyped: no key does anything until the waypoint's chunk is here; then Escape or the inventory key skips. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.chunkLoaded && (event.isEscape() || this.minecraft.options.keyInventory.matches(event))) {
            onClose();
        }
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.chunkLoaded && this.minecraft.level != null
                && this.minecraft.level.getChunkSource().hasChunk(this.theWaypoint.getXCoord() >> 4, this.theWaypoint.getZCoord() >> 4)) {
            this.chunkLoaded = true;
        }
        if (!this.playedSound) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(LOTRSounds.EVENT_FAST_TRAVEL, 1.0f, 1.0f));
            this.playedSound = true;
        }
        this.prevMapX = this.mapX;
        this.prevMapY = this.mapY;
        ++this.tickCounter;
        this.prevZoom = this.currentZoom;
        if (this.reachedWP) {
            this.currentZoom += 0.008333334f;
            this.currentZoom = Math.min(this.currentZoom, this.zoomBase + 0.5f);
            if (this.currentZoom >= this.zoomBase + 0.5f) {
                this.finishedZoomIn = true;
            }
        } else {
            double dx = this.theWaypoint.getX() - this.mapX;
            double dy = this.theWaypoint.getY() - this.mapY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= this.mapScaleFactor) {
                this.reachedWP = true;
                this.mapSpeed = 0.0;
                this.mapVelX = 0.0;
                this.mapVelY = 0.0;
            } else {
                this.mapSpeed += 0.01f;
                this.mapSpeed = Math.min(this.mapSpeed, 2.0);
                double vXNew = dx / dist * this.mapSpeed;
                double vYNew = dy / dist * this.mapSpeed;
                double a = 0.2f;
                this.mapVelX += (vXNew - this.mapVelX) * a;
                this.mapVelY += (vYNew - this.mapVelY) * a;
            }
            this.mapX += this.mapVelX * this.mapScaleFactor;
            this.mapY += this.mapVelY * this.mapScaleFactor;
            this.currentZoom -= 0.008333334f;
            this.currentZoom = Math.max(this.currentZoom, this.zoomBase);
        }
        if (this.chunkLoaded && this.reachedWP && this.finishedZoomIn) {
            onClose();
        }
    }
}
