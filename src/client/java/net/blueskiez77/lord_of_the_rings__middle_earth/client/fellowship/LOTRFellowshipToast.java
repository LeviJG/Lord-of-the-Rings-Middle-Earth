package net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

/**
 * LOTRGuiNotificationDisplay.NotificationFellowship: a fellowship's news in the corner of the
 * screen for six seconds, on the achievement notice's frame, with the fellowships icon.
 */
public class LOTRFellowshipToast implements Toast {

    private static final Identifier FRAME = Identifier.fromNamespaceAndPath("lotr", "gui/achievements/icons.png");
    private static final Identifier ICONS = Identifier.fromNamespaceAndPath("lotr", "gui/fellowships.png");
    private static final long DURATION_MS = 6000L;

    private final Component message;
    private Visibility wantedVisibility = Visibility.SHOW;

    public LOTRFellowshipToast(Component message) {
        this.message = message;
    }

    @Override
    public int width() {
        return 190;
    }

    @Override
    public int height() {
        return 32;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        this.wantedVisibility = fullyVisibleForMs >= DURATION_MS ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, FRAME, 0, 0, 0.0f, 200.0f, 190, 32, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, 8, 8, 80.0f, 0.0f, 16, 16, 256, 256);
        int y = 7;
        for (FormattedCharSequence line : font.split(this.message.copy().withStyle(s -> s.withColor(0x7A5D43)), 152)) {
            graphics.text(font, line, 30, y, 0xFF000000 | 8019267, false);
            y += font.lineHeight;
        }
    }
}
