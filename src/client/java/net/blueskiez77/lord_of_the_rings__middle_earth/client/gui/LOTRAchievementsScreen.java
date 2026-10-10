package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

/**
 * LOTRGuiAchievements: the achievements of the land of one category at a time -- those earned first,
 * then the rest, four at a time with a scroll bar -- a strip of the categories' colours across the top
 * (dragged, or the arrows either side, to move between them), and how many of all the player may
 * earn they have. Only those the player may earn, with their alignments, are listed.
 */
public class LOTRAchievementsScreen extends LOTRMenuBaseScreen {

    public static final Identifier PAGE = Identifier.fromNamespaceAndPath("lotr", "gui/achievements/page.png");
    public static final Identifier ICONS = Identifier.fromNamespaceAndPath("lotr", "gui/achievements/icons.png");
    private static final int TEXT_COLOUR = 8019267;

    private static LOTRAchievement.Category currentCategory = LOTRAchievement.Category.GENERAL;

    private final List<LOTRAchievement> taken = new ArrayList<>();
    private final List<LOTRAchievement> untaken = new ArrayList<>();
    private int totalTakenCount;
    private int totalAvailableCount;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasMouseDown;
    private int catScrollAreaX0;
    private int catScrollAreaX1;
    private int catScrollAreaY0;
    private int catScrollAreaY1;
    private boolean wasInCategoryScrollBar;
    private int prevMouseX;
    private int lastMouseX;
    private int lastMouseY;

    public LOTRAchievementsScreen() {
        super(Component.translatable("lotr.gui.achievements"));
    }

    private static List<LOTRAchievement.Category> categories() {
        List<LOTRAchievement.Category> list = new ArrayList<>();
        for (LOTRAchievement.Category category : LOTRAchievement.Category.values()) {
            if (category.dimension == LOTRDimension.MIDDLE_EARTH) {
                list.add(category);
            }
        }
        return list;
    }

    private static LOTRAchievement.Category categoryAt(int relative) {
        List<LOTRAchievement.Category> categories = categories();
        int index = Math.floorMod(categories.indexOf(currentCategory) + relative, categories.size());
        return categories.get(index);
    }

    @Override
    protected void init() {
        this.xSize = 220;
        super.init();
        if (currentCategory.dimension != LOTRDimension.MIDDLE_EARTH) {
            currentCategory = LOTRAchievement.Category.GENERAL;
        }
        addRenderableWidget(new CategoryButton(true, this.guiLeft + 14, this.guiTop + 13));
        addRenderableWidget(new CategoryButton(false, this.guiLeft + 191, this.guiTop + 13));
        updateAchievementLists();
    }

    private void moveCategory(int by) {
        currentCategory = categoryAt(by);
        this.currentScroll = 0.0f;
        updateAchievementLists();
    }

    private void updateAchievementLists() {
        var player = this.minecraft.player;
        this.taken.clear();
        this.untaken.clear();
        for (LOTRAchievement achievement : currentCategory.list) {
            if (!achievement.canPlayerEarn(player)) {
                continue;
            }
            (LOTRPlayerAchievements.hasAchievement(player, achievement) ? this.taken : this.untaken).add(achievement);
        }
        this.totalTakenCount = LOTRPlayerAchievements.getEarnedAchievements(player, LOTRDimension.MIDDLE_EARTH).size();
        this.totalAvailableCount = 0;
        for (LOTRAchievement.Category category : categories()) {
            for (LOTRAchievement achievement : category.list) {
                if (achievement.canPlayerEarn(player)) {
                    ++this.totalAvailableCount;
                }
            }
        }
        Comparator<LOTRAchievement> sorter = LOTRAchievement.sortForDisplay(player);
        this.taken.sort(sorter);
        this.untaken.sort(sorter);
    }

    private boolean hasScrollBar() {
        return this.taken.size() + this.untaken.size() > 4;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, PAGE, this.guiLeft, this.guiTop, 0.0F, 0.0F, this.xSize, this.ySize, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        if (this.wasInCategoryScrollBar) {
            int diff = mouseX - this.prevMouseX;
            if (diff >= 4) {
                moveCategory(-1);
                this.wasInCategoryScrollBar = false;
            } else if (diff <= -4) {
                moveCategory(1);
                this.wasInCategoryScrollBar = false;
            }
        }
        boolean isMouseDown = this.minecraft.mouseHandler.isLeftPressed();
        int scrollBarX0 = this.guiLeft + 201;
        int scrollBarX1 = scrollBarX0 + 12;
        int scrollBarY0 = this.guiTop + 48;
        int scrollBarY1 = scrollBarY0 + 200;
        if (!this.wasMouseDown && isMouseDown && mouseX >= scrollBarX0 && mouseX < scrollBarX1 && mouseY >= scrollBarY0
                && mouseY < scrollBarY1) {
            this.isScrolling = hasScrollBar();
        }
        if (!isMouseDown) {
            this.isScrolling = false;
        }
        this.wasMouseDown = isMouseDown;
        if (this.isScrolling) {
            this.currentScroll = Math.clamp((mouseY - scrollBarY0 - 8.5f) / (scrollBarY1 - scrollBarY0 - 17.0f), 0.0f, 1.0f);
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, Component.translatable("lotr.gui.achievements.title",
                LOTRDimension.MIDDLE_EARTH.getDimensionName(), this.totalTakenCount, this.totalAvailableCount),
                this.guiLeft + this.xSize / 2, this.guiTop - 30, 0xFFFFFFFF);
        graphics.centeredText(this.font, Component.translatable("lotr.gui.achievements.category", currentCategory.getDisplayName(),
                this.taken.size(), this.taken.size() + this.untaken.size()), this.guiLeft + this.xSize / 2, this.guiTop + 28,
                0xFF000000 | TEXT_COLOUR);
        extractCategoryStrip(graphics);
        if (hasScrollBar()) {
            int offset = (int) (this.currentScroll * 181.0f);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, scrollBarX0, scrollBarY0 + offset, 190.0F, 0.0F, 10, 17, 256, 256);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, scrollBarX0, scrollBarY0, 200.0F, 0.0F, 10, 17, 256, 256);
        }
        extractAchievements(graphics);
    }

    private void extractCategoryStrip(GuiGraphicsExtractor graphics) {
        int catScrollCentre = this.guiLeft + this.xSize / 2;
        int catScrollX = catScrollCentre - 76;
        int catScrollY = this.guiTop + 13;
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, catScrollX, catScrollY, 0.0F, 100.0F, 152, 10, 256, 256);
        this.catScrollAreaX0 = catScrollX;
        this.catScrollAreaX1 = catScrollX + 152;
        this.catScrollAreaY0 = catScrollY;
        this.catScrollAreaY1 = catScrollY + 10;
        int catWidth = 16;
        int catCentreWidth = 50;
        int catsEitherSide = (this.catScrollAreaX1 - this.catScrollAreaX0) / catWidth + 1;
        for (int l = -catsEitherSide; l <= catsEitherSide; ++l) {
            int thisCatWidth = l == 0 ? catCentreWidth : catWidth;
            int catX = catScrollCentre;
            if (l != 0) {
                int signum = Integer.signum(l);
                catX += (catCentreWidth + catWidth) / 2 * signum;
                catX += (Math.abs(l) - 1) * signum * catWidth;
            }
            int catX0 = Math.max(catX - thisCatWidth / 2, this.catScrollAreaX0);
            int catX1 = Math.min(catX + thisCatWidth, this.catScrollAreaX1);
            if (catX1 <= catX0) {
                continue;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, catX0, this.catScrollAreaY0, catX0 - this.catScrollAreaX0, 100.0F,
                    catX1 - catX0, this.catScrollAreaY1 - this.catScrollAreaY0, 256, 256, 0xFF000000 | categoryAt(l).color);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, catScrollX, catScrollY, 0.0F, 110.0F, 152, 10, 256, 256);
    }

    private void extractAchievements(GuiGraphicsExtractor graphics) {
        var player = this.minecraft.player;
        int size = this.taken.size() + this.untaken.size();
        int min = Math.round(this.currentScroll * (size - 4));
        int max = Math.min(3 + Math.round(this.currentScroll * (size - 4)), size - 1);
        for (int i = Math.max(min, 0); i <= max; ++i) {
            boolean hasAchievement = i < this.taken.size();
            LOTRAchievement achievement = hasAchievement ? this.taken.get(i) : this.untaken.get(i - this.taken.size());
            int offset = 47 + 50 * (i - min);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, this.guiLeft + 9, this.guiTop + offset, 0.0F,
                    hasAchievement ? 0.0F : 50.0F, 190, 50, 256, 256);
            int iconLeft = this.guiLeft + 12;
            int iconTop = this.guiTop + offset + 3;
            graphics.item(achievement.getIcon(), iconLeft, iconTop);
            if (!hasAchievement) {
                graphics.fill(iconLeft, iconTop, iconLeft + 16, iconTop + 16, -2013265920);
            }
            int textColour = 0xFF000000 | (hasAchievement ? TEXT_COLOUR : 5652783);
            graphics.text(this.font, achievement.getTitle(player), this.guiLeft + 33, this.guiTop + offset + 5, textColour, false);
            int y = this.guiTop + offset + 24;
            for (FormattedCharSequence line : this.font.split(achievement.getDescription(player), 184)) {
                graphics.text(this.font, line, this.guiLeft + 12, y, textColour, false);
                y += this.font.lineHeight;
            }
            if (hasAchievement) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, this.guiLeft + 179, this.guiTop + offset + 2, 190.0F, 17.0F,
                        16, 16, 256, 256);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0.0 && hasScrollBar()) {
            int j = this.taken.size() + this.untaken.size() - 4;
            this.currentScroll = Math.clamp(this.currentScroll - (float) Math.signum(scrollY) / j, 0.0f, 1.0f);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void tick() {
        super.tick();
        updateAchievementLists();
        this.prevMouseX = this.lastMouseX;
        this.wasInCategoryScrollBar = this.minecraft.mouseHandler.isLeftPressed() && this.lastMouseX >= this.catScrollAreaX0
                && this.lastMouseX < this.catScrollAreaX1 && this.lastMouseY >= this.catScrollAreaY0
                && this.lastMouseY < this.catScrollAreaY1;
    }

    /** LOTRGuiButtonAchievements: an arrow in the colour of the category it moves to. */
    private final class CategoryButton extends AbstractButton {
        private final boolean left;

        CategoryButton(boolean left, int x, int y) {
            super(x, y, 15, 21, Component.empty());
            this.left = left;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            moveCategory(this.left ? -1 : 1);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int texU = this.left ? 0 : this.width * 3;
            if (!this.active) {
                texU += this.width * 2;
            } else if (isHovered()) {
                texU += this.width;
            }
            int colour = 0xFF000000 | categoryAt(this.left ? -1 : 1).color;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, getX(), getY(), texU, 124.0F, this.width, this.height, 256, 256, colour);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, getX(), getY(), texU, 124.0F + this.height, this.width, this.height,
                    256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
