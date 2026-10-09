package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.quest.LOTRClientMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiRedBook: the red book's one page, the mini-quests. On the left, the date and how many quests
 * are in hand and done, or -- a quest picked -- its diary: when and where it was given, what was said,
 * what was asked, how it goes, and, done, what came of it. On the right, the quests in hand (by
 * faction and giver) or those done (newest first), four at a time with a scroll bar; each may be
 * followed, or abandoned (or its record removed) after asking.
 *
 * <p>NOT ported yet: the biome a quest was given in (with the biomes, D10).
 */
public class LOTRRedBookScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/quest/red_book.png");
    private static final Identifier TEXTURE_MINIQUESTS = Identifier.fromNamespaceAndPath("lotr", "gui/quest/red_book_miniquests.png");
    private static final int TEXT_COLOUR = 8019267;
    private static final int TEXT_COLOUR_RED = 16711680;
    private static final int X_SIZE = 420;
    private static final int Y_SIZE = 256;
    private static final int PAGE_WIDTH = 186;
    private static final int PAGE_TOP = 18;
    private static final int PAGE_BORDER = 10;
    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_HEIGHT = 216;
    private static final int SCROLL_BAR_X = X_SIZE / 2 + PAGE_WIDTH;
    private static final int SCROLL_BAR_Y = 18;
    private static final int SCROLL_BAR_ACTIVE_HEIGHT = 203;
    private static final int SCROLL_BAR_ACTIVE_Y_OFFSET = 3;
    private static final int SCROLL_BAR_ACTIVE_X_BORDER = 1;
    private static final int SCROLL_WIDGET_WIDTH = 10;
    private static final int SCROLL_WIDGET_HEIGHT = 17;
    private static final int MAX_DISPLAYED_MINIQUESTS = 4;
    private static final int Q_PANEL_WIDTH = 170;
    private static final int Q_PANEL_HEIGHT = 45;
    private static final int Q_PANEL_BORDER = 4;
    private static final int Q_DEL_X = 158;
    private static final int Q_DEL_Y = 4;
    private static final int Q_TRACK_X = 148;
    private static final int Q_TRACK_Y = 4;
    private static final int Q_WIDGET_SIZE = 8;
    private static final int DIARY_WIDTH = 170;
    private static final int DIARY_HEIGHT = 218;
    private static final int DIARY_X = X_SIZE / 2 - PAGE_BORDER - PAGE_WIDTH / 2 - DIARY_WIDTH / 2;
    private static final int DIARY_Y = Y_SIZE / 2 - DIARY_HEIGHT / 2 - 1;
    private static final int DIARY_BORDER = 6;

    private static boolean viewCompleted;

    private int guiLeft;
    private int guiTop;
    private boolean wasMouseDown;
    private int lastMouseY;
    private boolean isScrolling;
    private float currentScroll;
    private final Map<LOTRMiniQuest, int[]> displayedMiniQuests = new LinkedHashMap<>();
    private boolean mouseInDiary;
    private boolean isDiaryScrolling;
    private float diaryScroll;
    private @Nullable LOTRMiniQuest selectedMiniquest;
    private @Nullable LOTRMiniQuest deletingMiniquest;
    private int trackTicks;
    private LOTRRedBookButton buttonViewActive;
    private LOTRRedBookButton buttonViewCompleted;
    private LOTRRedBookButton buttonQuestDelete;
    private LOTRRedBookButton buttonQuestDeleteCancel;

    public LOTRRedBookScreen() {
        super(Component.translatable("lotr.gui.redBook.page.miniquests"));
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        int buttonX = this.guiLeft + X_SIZE / 2 - PAGE_BORDER - PAGE_WIDTH / 2;
        int buttonY = this.guiTop + 80;
        this.buttonViewActive = addRenderableWidget(new LOTRRedBookButton(buttonX - 10 - 60, buttonY, 60, 20,
                Component.translatable("lotr.gui.redBook.mq.viewActive"), () -> viewCompleted = false));
        this.buttonViewCompleted = addRenderableWidget(new LOTRRedBookButton(buttonX + 10, buttonY, 60, 20,
                Component.translatable("lotr.gui.redBook.mq.viewComplete"), () -> viewCompleted = true));
        buttonX = this.guiLeft + X_SIZE / 2 + PAGE_BORDER + PAGE_WIDTH / 2;
        buttonY = this.guiTop + Y_SIZE - 60;
        this.buttonQuestDelete = addRenderableWidget(new LOTRRedBookButton(buttonX - 10 - 60, buttonY, 60, 20,
                Component.empty(), () -> {
            if (this.deletingMiniquest != null) {
                LOTRClientMiniQuests.deleteMiniQuest(this.deletingMiniquest);
                this.deletingMiniquest = null;
                this.selectedMiniquest = null;
                this.diaryScroll = 0.0f;
            }
        }));
        this.buttonQuestDeleteCancel = addRenderableWidget(new LOTRRedBookButton(buttonX + 10, buttonY, 60, 20,
                Component.empty(), () -> this.deletingMiniquest = null));
        updateButtons();
    }

    private List<LOTRMiniQuest> getMiniQuests() {
        return viewCompleted ? LOTRClientMiniQuests.getMiniQuestsCompleted() : LOTRClientMiniQuests.getMiniQuests();
    }

    private boolean hasScrollBar() {
        return this.deletingMiniquest == null;
    }

    private boolean canScroll() {
        return hasScrollBar() && getMiniQuests().size() > MAX_DISPLAYED_MINIQUESTS;
    }

    private void updateButtons() {
        boolean hasQuestViewButtons = this.selectedMiniquest == null;
        this.buttonViewActive.active = this.buttonViewActive.visible = hasQuestViewButtons;
        this.buttonViewCompleted.active = this.buttonViewCompleted.visible = hasQuestViewButtons;
        boolean hasQuestDeleteButtons = this.deletingMiniquest != null;
        this.buttonQuestDelete.active = this.buttonQuestDelete.visible = hasQuestDeleteButtons;
        this.buttonQuestDeleteCancel.active = this.buttonQuestDeleteCancel.visible = hasQuestDeleteButtons;
        this.buttonQuestDelete.setMessage(Component.translatable(viewCompleted ? "lotr.gui.redBook.mq.deleteCmpYes"
                : "lotr.gui.redBook.mq.deleteYes"));
        this.buttonQuestDeleteCancel.setMessage(Component.translatable(viewCompleted ? "lotr.gui.redBook.mq.deleteCmpNo"
                : "lotr.gui.redBook.mq.deleteNo"));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F, X_SIZE, Y_SIZE, 512, 512);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.displayedMiniQuests.clear();
        setupScrollBar(mouseX, mouseY);
        updateButtons();
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int x = this.guiLeft + X_SIZE / 2 - PAGE_BORDER - PAGE_WIDTH / 2;
        if (this.selectedMiniquest == null) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(2.0f, 2.0f);
            graphics.centeredText(this.font, this.title, (int) (x / 2.0f), (int) ((this.guiTop + 30) / 2.0f), 0xFF000000 | TEXT_COLOUR);
            graphics.pose().popMatrix();
            graphics.centeredText(this.font, Component.translatable(viewCompleted ? "lotr.gui.redBook.mq.viewComplete"
                    : "lotr.gui.redBook.mq.viewActive"), x, this.guiTop + 50, 0xFF000000 | TEXT_COLOUR);
            graphics.centeredText(this.font, LOTRDate.ShireReckoning.getShireDate().getDateName(false), x,
                    this.guiTop + Y_SIZE - 30, 0xFF000000 | TEXT_COLOUR);
            graphics.centeredText(this.font, Component.translatable("lotr.gui.redBook.mq.numActive",
                    LOTRClientMiniQuests.getActiveMiniQuests().size()), x, this.guiTop + 120, 0xFF000000 | TEXT_COLOUR);
            graphics.centeredText(this.font, Component.translatable("lotr.gui.redBook.mq.numComplete",
                    LOTRClientMiniQuests.getCompletedMiniQuestsTotal()), x, this.guiTop + 140, 0xFF000000 | TEXT_COLOUR);
        } else {
            extractDiary(graphics, this.selectedMiniquest);
        }
        if (this.deletingMiniquest == null) {
            List<LOTRMiniQuest> miniquests = new ArrayList<>(getMiniQuests());
            if (!miniquests.isEmpty()) {
                if (viewCompleted) {
                    miniquests = miniquests.reversed();
                } else {
                    miniquests.sort(LOTRMiniQuest.SORTER_ALPHABETICAL);
                }
                int size = miniquests.size();
                int min = Math.max(Math.round(this.currentScroll * (size - MAX_DISPLAYED_MINIQUESTS)), 0);
                int max = Math.min(MAX_DISPLAYED_MINIQUESTS - 1 + Math.round(this.currentScroll * (size - MAX_DISPLAYED_MINIQUESTS)), size - 1);
                for (int index = min; index <= max; ++index) {
                    LOTRMiniQuest quest = miniquests.get(index);
                    int questX = this.guiLeft + X_SIZE / 2 + PAGE_BORDER;
                    int questY = this.guiTop + PAGE_TOP + (index - min) * (4 + Q_PANEL_HEIGHT);
                    extractMiniQuestPanel(graphics, quest, questX, questY, mouseX, mouseY);
                    this.displayedMiniQuests.put(quest, new int[]{questX, questY});
                }
            }
        } else {
            Component deleteText = Component.translatable(viewCompleted ? "lotr.gui.redBook.mq.deleteCmp" : "lotr.gui.redBook.mq.delete");
            int lineX = this.guiLeft + X_SIZE / 2 + PAGE_BORDER + PAGE_WIDTH / 2;
            int lineY = this.guiTop + 50;
            for (FormattedCharSequence line : this.font.split(deleteText, PAGE_WIDTH)) {
                graphics.centeredText(this.font, line, lineX, lineY, 0xFF000000 | TEXT_COLOUR);
                lineY += this.font.lineHeight;
            }
            int questX = this.guiLeft + X_SIZE / 2 + PAGE_BORDER + PAGE_WIDTH / 2 - Q_PANEL_WIDTH / 2;
            int questY = this.guiTop + PAGE_TOP + 80;
            extractMiniQuestPanel(graphics, this.deletingMiniquest, questX, questY, mouseX, mouseY);
        }
        if (hasScrollBar()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, this.guiLeft + SCROLL_BAR_X,
                    this.guiTop + SCROLL_BAR_Y, 244.0F, 0.0F, SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT, 256, 256);
            int widgetX = this.guiLeft + SCROLL_BAR_X + SCROLL_BAR_ACTIVE_X_BORDER;
            int widgetY = this.guiTop + SCROLL_BAR_Y + SCROLL_BAR_ACTIVE_Y_OFFSET;
            if (canScroll()) {
                int scroll = (int) (this.currentScroll * (SCROLL_BAR_ACTIVE_HEIGHT - SCROLL_WIDGET_HEIGHT));
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, widgetX, widgetY + scroll, 224.0F, 0.0F,
                        SCROLL_WIDGET_WIDTH, SCROLL_WIDGET_HEIGHT, 256, 256);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, widgetX, widgetY, 234.0F, 0.0F,
                        SCROLL_WIDGET_WIDTH, SCROLL_WIDGET_HEIGHT, 256, 256);
            }
        }
    }

    private static MutableComponent quote(LOTRMiniQuest quest, String speech, net.minecraft.world.entity.player.Player player) {
        return Component.translatable("lotr.gui.redBook.mq.diary.quote",
                LOTRSpeech.formatSpeech(speech, player, null, quest.getObjectiveInSpeech()));
    }

    /** The quest's diary, on its colour's page. */
    private void extractDiary(GuiGraphicsExtractor graphics, LOTRMiniQuest quest) {
        int x = this.guiLeft + DIARY_X;
        int y = this.guiTop + DIARY_Y;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 256.0F, DIARY_WIDTH, DIARY_HEIGHT, 512, 512,
                0xFF000000 | quest.getQuestColor());
        int textBottom = y + DIARY_HEIGHT - DIARY_BORDER;
        x += DIARY_BORDER;
        y += DIARY_BORDER;
        boolean completed = quest.isCompleted();
        boolean failed = !completed && quest.isFailed();
        String entityName = quest.entityName;
        LOTRTextBody pageText = new LOTRTextBody(TEXT_COLOUR);
        pageText.setTextWidth(DIARY_WIDTH - DIARY_BORDER * 2);
        String[] dayYear = LOTRDate.ShireReckoning.getShireDate(quest.dateGiven).getDayAndYearNames(false);
        pageText.add(dayYear[0]);
        pageText.add(dayYear[1]);
        pageText.add("");
        pageText.add(quote(quest, quest.quoteStart, this.minecraft.player));
        pageText.add("");
        for (String s : quest.quotesStages) {
            pageText.add(quote(quest, s, this.minecraft.player));
            pageText.add("");
        }
        pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.asked", entityName, quest.getQuestObjective()));
        pageText.add("");
        pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.progress", quest.getQuestProgress()));
        if (quest.willHire) {
            pageText.add("");
            pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.willHire", entityName));
        }
        if (failed) {
            for (int l = 0; l < pageText.size(); ++l) {
                pageText.set(l, pageText.getText(l).copy().withStyle(ChatFormatting.STRIKETHROUGH));
            }
            pageText.add(quest.getQuestFailure(), TEXT_COLOUR_RED);
        }
        if (completed) {
            pageText.add("");
            pageText.addLinebreak();
            pageText.add("");
            dayYear = LOTRDate.ShireReckoning.getShireDate(quest.dateCompleted).getDayAndYearNames(false);
            pageText.add(dayYear[0]);
            pageText.add(dayYear[1]);
            pageText.add("");
            pageText.add(quote(quest, quest.quoteComplete, this.minecraft.player));
            pageText.add("");
            pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.complete"));
            if (quest.anyRewardsGiven()) {
                pageText.add("");
                pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.reward", entityName));
                if (quest.alignmentRewarded != 0.0f) {
                    pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.reward.align",
                            LOTRAlignmentValues.formatAlignForDisplay(quest.alignmentRewarded),
                            quest.getAlignmentRewardFaction().factionName()));
                }
                if (quest.coinsRewarded != 0) {
                    pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.reward.coins", quest.coinsRewarded));
                }
                for (ItemStack item : quest.itemsRewarded) {
                    pageText.add(item.has(DataComponents.WRITTEN_BOOK_CONTENT)
                            ? Component.translatable("lotr.gui.redBook.mq.diary.reward.book", item.getHoverName())
                            : Component.translatable("lotr.gui.redBook.mq.diary.reward.item", item.getHoverName(), item.getCount()));
                }
            }
            if (quest.wasHired) {
                pageText.add("");
                pageText.add(Component.translatable("lotr.gui.redBook.mq.diary.reward.hired", entityName));
            }
        }
        this.diaryScroll = pageText.renderAndReturnScroll(graphics, this.font, x, y, textBottom, this.diaryScroll);
    }

    private void extractMiniQuestPanel(GuiGraphicsExtractor graphics, LOTRMiniQuest quest, int questX, int questY,
                                       int mouseX, int mouseY) {
        boolean mouseInPanel = mouseX >= questX && mouseX < questX + Q_PANEL_WIDTH && mouseY >= questY
                && mouseY < questY + Q_PANEL_HEIGHT;
        boolean mouseInDelete = inWidget(mouseX, mouseY, questX + Q_DEL_X, questY + Q_DEL_Y);
        boolean mouseInTrack = inWidget(mouseX, mouseY, questX + Q_TRACK_X, questY + Q_TRACK_Y);
        boolean isTracking = quest == LOTRClientMiniQuests.getTrackingMiniQuest();
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, questX, questY, 0.0F,
                mouseInPanel || quest == this.selectedMiniquest ? Q_PANEL_HEIGHT : 0.0F, Q_PANEL_WIDTH, Q_PANEL_HEIGHT, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, questX, questY, 0.0F, Q_PANEL_HEIGHT * 2,
                Q_PANEL_WIDTH, Q_PANEL_HEIGHT, 256, 256, 0xFF000000 | quest.getQuestColor());
        MutableComponent questName = Component.literal(quest.entityName);
        MutableComponent factionName = quest.getFactionSubtitle().copy();
        if (quest.isFailed()) {
            questName.withStyle(ChatFormatting.STRIKETHROUGH);
            factionName.withStyle(ChatFormatting.STRIKETHROUGH);
        }
        int colour = 0xFF000000 | TEXT_COLOUR;
        graphics.text(this.font, questName, questX + Q_PANEL_BORDER, questY + Q_PANEL_BORDER, colour, false);
        graphics.text(this.font, factionName, questX + Q_PANEL_BORDER, questY + Q_PANEL_BORDER + this.font.lineHeight, colour, false);
        if (quest.isFailed()) {
            graphics.text(this.font, quest.getQuestFailureShorthand(), questX + Q_PANEL_BORDER, questY + 25,
                    0xFF000000 | TEXT_COLOUR_RED, false);
        } else if (isTracking && this.trackTicks > 0) {
            graphics.text(this.font, Component.translatable("lotr.gui.redBook.mq.tracking"), questX + Q_PANEL_BORDER,
                    questY + 25, colour, false);
        } else {
            String objective = quest.getQuestObjective().getString();
            int maxObjLength = Q_PANEL_WIDTH - Q_PANEL_BORDER * 2 - 18;
            if (this.font.width(objective) >= maxObjLength) {
                String ellipsis = "...";
                while (this.font.width(objective + ellipsis) >= maxObjLength) {
                    objective = objective.substring(0, objective.length() - 1);
                    while (Character.isWhitespace(objective.charAt(objective.length() - 1))) {
                        objective = objective.substring(0, objective.length() - 1);
                    }
                }
                objective = objective + ellipsis;
            }
            graphics.text(this.font, objective, questX + Q_PANEL_BORDER, questY + 25, colour, false);
            Component progress = quest.isCompleted() ? Component.translatable("lotr.gui.redBook.mq.complete")
                    : quest.getQuestProgress();
            graphics.text(this.font, progress, questX + Q_PANEL_BORDER, questY + 25 + this.font.lineHeight, colour, false);
        }
        if (this.deletingMiniquest == null) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, questX + Q_DEL_X, questY + Q_DEL_Y,
                    Q_PANEL_WIDTH, mouseInDelete ? Q_WIDGET_SIZE : 0, Q_WIDGET_SIZE, Q_WIDGET_SIZE, 256, 256);
            if (!viewCompleted) {
                int trackU = Q_PANEL_WIDTH + Q_WIDGET_SIZE + (isTracking ? Q_WIDGET_SIZE : 0);
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MINIQUESTS, questX + Q_TRACK_X, questY + Q_TRACK_Y,
                        trackU, mouseInTrack ? Q_WIDGET_SIZE : 0, Q_WIDGET_SIZE, Q_WIDGET_SIZE, 256, 256);
            }
        }
        graphics.item(quest.getQuestIcon(), questX + 149, questY + 24);
    }

    private static boolean inWidget(double mouseX, double mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + Q_WIDGET_SIZE && mouseY >= y && mouseY < y + Q_WIDGET_SIZE;
    }

    private void setupScrollBar(int mouseX, int mouseY) {
        boolean isMouseDown = this.minecraft.mouseHandler.isLeftPressed();
        int i1 = mouseX - this.guiLeft;
        int j1 = mouseY - this.guiTop;
        this.mouseInDiary = this.selectedMiniquest != null && i1 >= DIARY_X && i1 < DIARY_X + DIARY_WIDTH
                && j1 >= DIARY_Y && j1 < DIARY_Y + DIARY_HEIGHT;
        boolean mouseInScrollBar = i1 >= SCROLL_BAR_X + SCROLL_BAR_ACTIVE_X_BORDER
                && i1 < SCROLL_BAR_X + SCROLL_BAR_WIDTH - SCROLL_BAR_ACTIVE_X_BORDER * 2
                && j1 >= SCROLL_BAR_Y + SCROLL_BAR_ACTIVE_Y_OFFSET
                && j1 < SCROLL_BAR_Y + SCROLL_BAR_ACTIVE_Y_OFFSET + SCROLL_BAR_ACTIVE_HEIGHT;
        if (!this.wasMouseDown && isMouseDown) {
            if (mouseInScrollBar) {
                this.isScrolling = canScroll();
            } else if (this.mouseInDiary) {
                this.isDiaryScrolling = true;
            }
        }
        if (!isMouseDown) {
            this.isScrolling = false;
            this.isDiaryScrolling = false;
        }
        this.wasMouseDown = isMouseDown;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - (this.guiTop + SCROLL_BAR_Y + SCROLL_BAR_ACTIVE_Y_OFFSET) - SCROLL_WIDGET_HEIGHT / 2.0f)
                    / ((float) SCROLL_BAR_ACTIVE_HEIGHT - SCROLL_WIDGET_HEIGHT);
            this.currentScroll = Math.clamp(this.currentScroll, 0.0f, 1.0f);
        } else if (this.isDiaryScrolling) {
            this.diaryScroll -= (float) (this.lastMouseY - mouseY) / this.font.lineHeight;
        }
        this.lastMouseY = mouseY;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0.0 && (canScroll() || this.mouseInDiary)) {
            int i = scrollY > 0.0 ? 1 : -1;
            if (this.mouseInDiary) {
                this.diaryScroll += i;
            } else {
                int j = getMiniQuests().size() - MAX_DISPLAYED_MINIQUESTS;
                this.currentScroll = Math.clamp(this.currentScroll - (float) i / j, 0.0f, 1.0f);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && this.deletingMiniquest == null) {
            double i = event.x();
            double j = event.y();
            for (Map.Entry<LOTRMiniQuest, int[]> entry : this.displayedMiniQuests.entrySet()) {
                LOTRMiniQuest quest = entry.getKey();
                int questX = entry.getValue()[0];
                int questY = entry.getValue()[1];
                if (inWidget(i, j, questX + Q_DEL_X, questY + Q_DEL_Y)) {
                    this.selectedMiniquest = this.deletingMiniquest = quest;
                    this.diaryScroll = 0.0f;
                    return true;
                }
                if (!viewCompleted && inWidget(i, j, questX + Q_TRACK_X, questY + Q_TRACK_Y)) {
                    trackOrUntrack(quest);
                    return true;
                }
            }
            for (Map.Entry<LOTRMiniQuest, int[]> entry : this.displayedMiniQuests.entrySet()) {
                int questX = entry.getValue()[0];
                int questY = entry.getValue()[1];
                if (i >= questX && j >= questY && i < questX + Q_PANEL_WIDTH && j < questY + Q_PANEL_HEIGHT) {
                    this.selectedMiniquest = entry.getKey();
                    this.diaryScroll = 0.0f;
                    return true;
                }
            }
            if (super.mouseClicked(event, doubleClick)) {
                return true;
            }
            if (!this.mouseInDiary && !this.isScrolling) {
                this.selectedMiniquest = null;
                this.diaryScroll = 0.0f;
            }
            return false;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void trackOrUntrack(LOTRMiniQuest quest) {
        LOTRClientMiniQuests.setTrackingMiniQuest(quest == LOTRClientMiniQuests.getTrackingMiniQuest() ? null : quest);
        this.trackTicks = 40;
    }

    /** keyTyped: Escape or the inventory key steps back from asking, then from the diary, then closes. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        boolean back = event.key() == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.matches(event);
        if (back) {
            if (this.deletingMiniquest != null) {
                this.deletingMiniquest = null;
                return true;
            }
            if (this.selectedMiniquest != null) {
                this.selectedMiniquest = null;
                return true;
            }
            if (event.key() != GLFW.GLFW_KEY_ESCAPE) {
                onClose();
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.trackTicks > 0) {
            --this.trackTicks;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
