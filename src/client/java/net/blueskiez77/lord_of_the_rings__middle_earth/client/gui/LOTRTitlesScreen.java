package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTitleShieldNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiTitles: every title the player may bear, then (below a "---") those they cannot yet, each
 * telling how it is won when the mouse is over it; a title clicked is chosen, its colour picked from
 * the sixteen boxes beneath, and borne with "Select Title".
 */
public class LOTRTitlesScreen extends LOTRMenuBaseScreen {

    private static final int ROWS = 12;
    private static final int ROW_HEIGHT = 12;
    private static final int COLOR_BOX_WIDTH = 8;
    private static final int COLOR_BOX_GAP = 4;
    private static final int SCROLL_BAR_WIDTH = 11;
    private static final int SCROLL_BAR_HEIGHT = 144;
    private static final int SCROLL_BAR_X = 197 - (SCROLL_BAR_WIDTH - 1) / 2;
    private static final int SCROLL_BAR_Y = 30;
    private static final int SCROLL_WIDGET_WIDTH = 11;
    private static final int SCROLL_WIDGET_HEIGHT = 8;

    private LOTRTitle.@Nullable PlayerTitle currentTitle;
    private final List<@Nullable LOTRTitle> displayedTitles = new ArrayList<>();
    private final Map<LOTRTitle, Boolean> displayedTitleInfo = new LinkedHashMap<>();
    private final Map<TextColor, int[]> displayedColorBoxes = new LinkedHashMap<>();
    private @Nullable LOTRTitle selectedTitle;
    private TextColor selectedColor = TextColor.WHITE;
    private Button selectButton;
    private Button removeButton;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasMouseDown;

    public LOTRTitlesScreen() {
        super(Component.translatable("lotr.gui.titles.title"));
    }

    private Player player() {
        return this.minecraft.player;
    }

    @Override
    protected void init() {
        this.xSize = 256;
        super.init();
        this.selectButton = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.titles.select"), b -> {
            if (this.selectedTitle != null && (this.currentTitle == null || this.selectedTitle != this.currentTitle.title()
                    || this.selectedColor != this.currentTitle.color())) {
                ClientPlayNetworking.send(new LOTRTitleShieldNetworking.SelectTitle(
                        Optional.of(this.selectedTitle.getTitleName()),
                        LOTRTitle.PlayerTitle.COLORS.indexOf(this.selectedColor)));
            }
        }).bounds(this.guiLeft + this.xSize / 2 - 10 - 80, this.guiTop + 220, 80, 20).build());
        this.removeButton = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.titles.remove"), b ->
                ClientPlayNetworking.send(new LOTRTitleShieldNetworking.SelectTitle(Optional.empty(), 0)))
                .bounds(this.guiLeft + this.xSize / 2 + 10, this.guiTop + 220, 80, 20).build());
        updateTitles();
    }

    @Override
    public void tick() {
        super.tick();
        updateTitles();
    }

    /** updateScreen: the titles the player may bear, sorted, then a gap, then those shown that they may not. */
    private void updateTitles() {
        this.currentTitle = LOTRPlayerTitles.getPlayerTitle(player());
        this.displayedTitles.clear();
        List<LOTRTitle> available = new ArrayList<>();
        List<LOTRTitle> unavailable = new ArrayList<>();
        for (LOTRTitle title : LOTRTitle.ALL_TITLES) {
            if (title.canPlayerUse(player())) {
                available.add(title);
            } else if (title.canDisplay(player())) {
                unavailable.add(title);
            }
        }
        Comparator<LOTRTitle> sorter = LOTRTitle.createTitleSorter(player());
        available.sort(sorter);
        unavailable.sort(sorter);
        this.displayedTitles.addAll(available);
        this.displayedTitles.add(null);
        this.displayedTitles.addAll(unavailable);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        setupScrollBar(mouseX, mouseY);
        this.selectButton.active = this.selectedTitle != null;
        this.removeButton.active = this.currentTitle != null;
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int midX = this.guiLeft + this.xSize / 2;
        graphics.centeredText(this.font, this.title, midX, this.guiTop - 30, 0xFFFFFFFF);
        Component titleName = this.currentTitle == null ? Component.translatable("lotr.gui.titles.currentTitle.none")
                : this.currentTitle.title().getDisplayName(player()).copy()
                        .withStyle(style -> style.withColor(this.currentTitle.color()));
        graphics.centeredText(this.font, Component.translatable("lotr.gui.titles.currentTitle", titleName), midX,
                this.guiTop, 0xFFFFFFFF);
        this.displayedTitleInfo.clear();
        int titleY = this.guiTop + 30;
        graphics.fill(midX - 70, titleY - 1, midX - 69, titleY + ROW_HEIGHT * ROWS, -1711276033);
        graphics.fill(midX + 70 - 1, titleY - 1, midX + 70, titleY + ROW_HEIGHT * ROWS, -1711276033);
        int size = this.displayedTitles.size();
        int min = Math.max(Math.round(this.currentScroll * (size - ROWS)), 0);
        int max = Math.min(ROWS - 1 + Math.round(this.currentScroll * (size - ROWS)), size - 1);
        for (int index = min; index <= max; ++index) {
            LOTRTitle title = this.displayedTitles.get(index);
            boolean isCurrentTitle = this.currentTitle != null && this.currentTitle.title() == title;
            MutableComponent name;
            if (title != null) {
                name = title.getDisplayName(player()).copy();
                if (isCurrentTitle) {
                    name = Component.literal("[").append(name).append("]")
                            .withStyle(style -> style.withColor(this.currentTitle.color()));
                }
            } else {
                name = Component.literal("---");
            }
            int nameWidth = this.font.width(name);
            boolean mouseOver = mouseX >= midX - nameWidth / 2 && mouseX < midX + nameWidth / 2
                    && mouseY >= titleY && mouseY < titleY + this.font.lineHeight;
            if (title != null) {
                this.displayedTitleInfo.put(title, mouseOver);
            }
            int textColor = title == null ? 7829367 : title.canPlayerUse(player())
                    ? mouseOver ? 16777120 : 16777215 : mouseOver ? 12303291 : 7829367;
            graphics.centeredText(this.font, name, midX, titleY, 0xFF000000 | textColor);
            titleY += ROW_HEIGHT;
        }
        this.displayedColorBoxes.clear();
        if (this.selectedTitle != null) {
            graphics.centeredText(this.font, this.selectedTitle.getDisplayName(player()).copy()
                    .withStyle(style -> style.withColor(this.selectedColor)), midX, this.guiTop + 185, 0xFFFFFFFF);
            List<TextColor> colors = LOTRTitle.PlayerTitle.COLORS;
            int colorX = midX - (COLOR_BOX_WIDTH * colors.size() + COLOR_BOX_GAP * (colors.size() - 1)) / 2;
            int colorY = this.guiTop + 200;
            for (TextColor color : colors) {
                boolean mouseOver = mouseX >= colorX && mouseX < colorX + COLOR_BOX_WIDTH && mouseY >= colorY
                        && mouseY < colorY + COLOR_BOX_WIDTH;
                int y = colorY + (mouseOver ? -1 : 0);
                graphics.fill(colorX, y, colorX + COLOR_BOX_WIDTH, y + COLOR_BOX_WIDTH, 0xFF000000 | color.getValue());
                this.displayedColorBoxes.put(color, new int[]{colorX, colorY});
                colorX += COLOR_BOX_WIDTH + COLOR_BOX_GAP;
            }
        }
        if (this.displayedTitles.size() > ROWS) {
            int scroll = (int) (this.currentScroll * (SCROLL_BAR_HEIGHT - SCROLL_WIDGET_HEIGHT));
            int x1 = this.guiLeft + SCROLL_BAR_X;
            int y1 = this.guiTop + SCROLL_BAR_Y + scroll;
            graphics.fill(x1, y1, x1 + SCROLL_WIDGET_WIDTH, y1 + SCROLL_WIDGET_HEIGHT, -1426063361);
        }
        for (Map.Entry<LOTRTitle, Boolean> entry : this.displayedTitleInfo.entrySet()) {
            if (entry.getValue()) {
                graphics.setTooltipForNextFrame(this.font, this.font.split(entry.getKey().getDescription(player()), 200),
                        mouseX + 10, mouseY + 10);
            }
        }
    }

    private void setupScrollBar(int mouseX, int mouseY) {
        boolean isMouseDown = this.minecraft.mouseHandler.isLeftPressed();
        int i1 = this.guiLeft + SCROLL_BAR_X;
        int j1 = this.guiTop + SCROLL_BAR_Y;
        int i2 = i1 + SCROLL_BAR_WIDTH;
        int j2 = j1 + SCROLL_BAR_HEIGHT;
        if (!this.wasMouseDown && isMouseDown && mouseX >= i1 && mouseY >= j1 && mouseX < i2 && mouseY < j2) {
            this.isScrolling = true;
        }
        if (!isMouseDown) {
            this.isScrolling = false;
        }
        this.wasMouseDown = isMouseDown;
        if (this.isScrolling) {
            this.currentScroll = Mth.clamp((mouseY - j1 - SCROLL_WIDGET_HEIGHT / 2.0f)
                    / ((float) (j2 - j1) - SCROLL_WIDGET_HEIGHT), 0.0f, 1.0f);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int i = (int) Math.signum(scrollY);
        if (i != 0 && this.displayedTitles.size() > ROWS) {
            int j = this.displayedTitles.size() - ROWS;
            this.currentScroll = Mth.clamp(this.currentScroll - (float) i / j, 0.0f, 1.0f);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            for (Map.Entry<LOTRTitle, Boolean> entry : this.displayedTitleInfo.entrySet()) {
                if (entry.getValue() && entry.getKey().canPlayerUse(player())) {
                    this.selectedTitle = entry.getKey();
                    this.selectedColor = TextColor.WHITE;
                    return true;
                }
            }
            for (Map.Entry<TextColor, int[]> entry : this.displayedColorBoxes.entrySet()) {
                int colorX = entry.getValue()[0];
                int colorY = entry.getValue()[1];
                if (event.x() >= colorX && event.x() < colorX + COLOR_BOX_WIDTH && event.y() >= colorY
                        && event.y() < colorY + COLOR_BOX_WIDTH) {
                    this.selectedColor = entry.getKey();
                    break;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
}
