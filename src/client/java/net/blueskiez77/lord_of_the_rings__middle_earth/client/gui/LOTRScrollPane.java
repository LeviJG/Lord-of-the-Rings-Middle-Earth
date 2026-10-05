package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.Collection;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

/** LOTRGuiScrollPane: a thin scroll bar beside a list, dragged or wheeled. */
public class LOTRScrollPane {

    public final int scrollWidgetWidth;
    public final int scrollWidgetHeight;
    public int barColor = -1711276033;
    public int widgetColor = -1426063361;
    public int scrollBarX0;
    public int paneX0;
    public int paneY0;
    public int paneY1;
    public boolean hasScrollBar;
    public float currentScroll;
    public boolean isScrolling;
    public boolean mouseOver;
    private boolean wasMouseDown;

    public LOTRScrollPane(int ww, int wh) {
        this.scrollWidgetWidth = ww;
        this.scrollWidgetHeight = wh;
    }

    public void drawScrollBar(GuiGraphicsExtractor graphics) {
        int x0 = this.scrollBarX0 + this.scrollWidgetWidth / 2;
        int y0 = this.paneY0;
        int y1 = this.paneY1;
        graphics.fill(x0, y0, x0 + 1, y1, this.barColor);
        int scroll = (int) (this.currentScroll * (y1 - y0 - this.scrollWidgetHeight));
        x0 = this.scrollBarX0;
        y0 += scroll;
        graphics.fill(x0, y0, x0 + this.scrollWidgetWidth, y0 + this.scrollWidgetHeight, this.widgetColor);
    }

    public int[] getMinMaxIndices(Collection<?> list, int displayed) {
        int size = list.size();
        int min = Math.max(Math.round(this.currentScroll * (size - displayed)), 0);
        int max = Math.min(displayed - 1 + Math.round(this.currentScroll * (size - displayed)), size - 1);
        return new int[]{min, max};
    }

    public void mouseDragScroll(int i, int j, boolean isMouseDown) {
        if (this.hasScrollBar) {
            int x0 = this.paneX0;
            int x1 = this.scrollBarX0 + this.scrollWidgetWidth;
            int y0 = this.paneY0;
            int y1 = this.paneY1;
            this.mouseOver = i >= x0 && j >= y0 && i < x1 && j < y1;
            x0 = this.scrollBarX0;
            boolean mouseOverScroll = i >= x0 && j >= y0 && i < x1 && j < y1;
            if (!this.wasMouseDown && isMouseDown && mouseOverScroll) {
                this.isScrolling = true;
            }
            if (!isMouseDown) {
                this.isScrolling = false;
            }
            if (this.isScrolling) {
                this.currentScroll = Mth.clamp((j - y0 - this.scrollWidgetHeight / 2.0f)
                        / ((float) (y1 - y0) - this.scrollWidgetHeight), 0.0f, 1.0f);
            }
        } else {
            resetScroll();
        }
        this.wasMouseDown = isMouseDown;
    }

    public void mouseWheelScroll(int delta, int size) {
        this.currentScroll = Mth.clamp(this.currentScroll - (float) delta / size, 0.0f, 1.0f);
    }

    public void resetScroll() {
        this.currentScroll = 0.0f;
        this.isScrolling = false;
    }

    public LOTRScrollPane setColors(int c1, int c2) {
        int alphaMask = -16777216;
        this.barColor = (c1 & alphaMask) == 0 ? c1 | alphaMask : c1;
        this.widgetColor = (c2 & alphaMask) == 0 ? c2 | alphaMask : c2;
        return this;
    }
}
