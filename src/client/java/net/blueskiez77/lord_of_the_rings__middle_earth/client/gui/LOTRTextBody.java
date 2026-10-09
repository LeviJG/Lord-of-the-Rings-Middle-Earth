package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

/**
 * LOTRTextBody: a body of text, part by part each in its colour, wrapped to a width and drawn up from
 * the bottom of its area, scrolled back by whole lines; a line break part is a row of dashes.
 *
 * <p>The original counted only its last part's lines (getTotalLines set rather than added), which
 * kept it from scrolling at all and pushed long text off the top; here every part's lines count.
 */
public class LOTRTextBody {

    private final List<Part> parts = new ArrayList<>();
    private final int defaultColor;
    private int textWidth = 100;

    private record Part(Component text, int color, boolean linebreak) {
    }

    public LOTRTextBody(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void add(Component s) {
        add(s, this.defaultColor);
    }

    public void add(String s) {
        add(Component.literal(s));
    }

    public void add(Component s, int color) {
        this.parts.add(new Part(s, color, false));
    }

    public void addLinebreak() {
        this.parts.add(new Part(Component.empty(), this.defaultColor, true));
    }

    public Component getText(int i) {
        return this.parts.get(i).text();
    }

    public void set(int i, Component s) {
        Part part = this.parts.get(i);
        this.parts.set(i, new Part(s, part.color(), part.linebreak()));
    }

    public void setTextWidth(int w) {
        this.textWidth = w;
    }

    public int size() {
        return this.parts.size();
    }

    private List<FormattedCharSequence> lines(Font font, Part part) {
        if (part.linebreak()) {
            StringBuilder line = new StringBuilder();
            while (font.width(line.toString() + '-') < this.textWidth) {
                line.append('-');
            }
            return List.of(Component.literal(line.toString()).getVisualOrderText());
        }
        if (part.text().getString().isEmpty()) {
            return List.of(FormattedCharSequence.EMPTY);
        }
        return font.split(part.text(), this.textWidth);
    }

    public int getTotalLines(Font font) {
        int lines = 0;
        for (Part part : this.parts) {
            lines += lines(font, part).size();
        }
        return lines;
    }

    public float renderAndReturnScroll(GuiGraphicsExtractor graphics, Font font, int x, int yTop, int yBottom, float scroll) {
        int ySize = yBottom - yTop;
        int numLines = getTotalLines(font);
        int lineHeight = font.lineHeight;
        scroll = Math.max(scroll, 0.0f);
        scroll = Math.min(scroll, numLines - Mth.floor((float) ySize / lineHeight));
        int d1 = Math.round(scroll);
        int y = yTop + ySize / lineHeight * lineHeight - lineHeight;
        int maxLines = ySize / lineHeight;
        if (numLines < maxLines) {
            y -= (maxLines - numLines) * lineHeight;
        }
        for (int i = this.parts.size() - 1; i >= 0; --i) {
            Part part = this.parts.get(i);
            List<FormattedCharSequence> lineList = lines(font, part);
            for (int l = lineList.size() - 1; l >= 0; --l) {
                if (d1 > 0) {
                    --d1;
                    continue;
                }
                if (y < yTop) {
                    return scroll;
                }
                graphics.text(font, lineList.get(l), x, y, 0xFF000000 | part.color(), false);
                y -= lineHeight;
            }
        }
        return scroll;
    }
}
