package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntConsumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerWhitelistEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerDataPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerEditPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBannerNamePayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiBanner: the owner's screen for a protecting banner. At the top, the
 * mode -- by faction (with the alignment asked) or by whitelist -- and at the
 * foot, self-protection and the default permissions. In whitelist mode, the
 * slots (the owner's first, fixed), six at a time with a scroll bar, each name
 * checked with the server when its box loses focus and shown green if valid;
 * beside each valid one, an icon opening its permissions. Everything goes to
 * the server when the screen closes; the mode, alignment and default
 * permissions also as they change.
 *
 * <p>Geometry, texture regions and colours are the original's, on
 * lotr:gui/banner_edit.png.
 */
public class LOTRBannerScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/banner_edit.png");
    private static final int LABEL_COLOUR = 0xFF404040;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GREEN = 0xFF00FF00;
    private static final int RED = 0xFFFF0000;

    private static final int X_SIZE = 200;
    private static final int Y_SIZE = 250;
    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_HEIGHT = 132;
    private static final int SCROLL_BAR_X = 181;
    private static final int SCROLL_BAR_Y = 68;
    private static final int SCROLL_BAR_BORDER = 1;
    private static final int SCROLL_WIDGET_WIDTH = 10;
    private static final int SCROLL_WIDGET_HEIGHT = 17;
    private static final int PERM_ICON_X = 3;
    private static final int PERM_ICON_Y = 0;
    private static final int PERM_ICON_WIDTH = 10;
    private static final int PERM_WINDOW_BORDER = 4;
    private static final int PERM_WINDOW_WIDTH = 150;
    private static final int PERM_WINDOW_HEIGHT = 70;
    private static final int SLOTS_SHOWN = 6;

    private final LOTRBannerBlockEntity theBanner;
    private int guiLeft;
    private int guiTop;
    private Button buttonMode;
    private IconButton buttonSelfProtection;
    private SlotButton buttonAddSlot;
    private SlotButton buttonRemoveSlot;
    private IconButton buttonDefaultPermissions;
    private EditBox alignmentField;
    private EditBox[] allowedPlayers = {};
    private boolean[] invalidUsernames = {};
    private boolean[] validatedUsernames = {};
    private boolean[] checkUsernames = {};
    private float currentScroll;
    private boolean isScrolling;
    private int permissionsMouseoverIndex = -1;
    private int permissionsMouseoverY = -1;
    private int permissionsOpenIndex = -1;
    private int permissionsOpenY = -1;
    private LOTRBannerProtection.@Nullable Permission mouseOverPermission;
    private boolean defaultPermissionsOpen;
    /** Set while the screen fills its own boxes, so that is not taken for typing. */
    private boolean settingUp;

    public LOTRBannerScreen(LOTRBannerBlockEntity banner) {
        super(Component.translatable("lotr.gui.bannerEdit.title"));
        this.theBanner = banner;
    }

    /** LOTRPacketBannerData's handler: the server's word, into the banner and, if asked, this screen. */
    public static void handleData(LOTRBannerDataPayload data) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null && mc.level.getBlockEntity(data.pos()) instanceof LOTRBannerBlockEntity banner) {
            banner.applyClientData(data);
            if (data.openGui()) {
                mc.gui.setScreen(new LOTRBannerScreen(banner));
            }
        }
    }

    /** LOTRPacketBannerValidate's handler, via validateBannerUsername. */
    public static void handleValidate(LOTRBannerNamePayloads.Validate data) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.gui.screen() instanceof LOTRBannerScreen screen && screen.theBanner.getBlockPos().equals(data.pos())) {
            screen.validateUsername(data.slot(), data.prevText(), data.valid());
        }
    }

    // ------------------------------------------------------------ setup

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        // Rebuilt from the banner each time: a resize clears the widgets.
        this.allowedPlayers = new EditBox[0];
        this.invalidUsernames = new boolean[0];
        this.validatedUsernames = new boolean[0];
        this.checkUsernames = new boolean[0];
        this.buttonMode = addRenderableWidget(Button.builder(Component.empty(), b -> {
            this.theBanner.setPlayerSpecificProtection(!this.theBanner.isPlayerSpecificProtection());
        }).bounds(this.guiLeft + X_SIZE / 2 - 80, this.guiTop + 20, 160, 20).build());
        this.buttonSelfProtection = addRenderableWidget(new IconButton(this.guiLeft + X_SIZE / 2 - 24, this.guiTop + 224, 212, 100,
                () -> this.theBanner.setSelfProtection(!this.theBanner.isSelfProtection())));
        this.buttonAddSlot = addRenderableWidget(new SlotButton(0, this.guiLeft + 179, this.guiTop + 202, id -> {
            this.theBanner.resizeWhitelist(this.theBanner.getWhitelistLength() + 1);
            refreshWhitelist();
        }));
        this.buttonRemoveSlot = addRenderableWidget(new SlotButton(1, this.guiLeft + 187, this.guiTop + 202, id -> {
            this.theBanner.resizeWhitelist(this.theBanner.getWhitelistLength() - 1);
            refreshWhitelist();
        }));
        this.buttonDefaultPermissions = addRenderableWidget(new IconButton(this.guiLeft + X_SIZE / 2 + 8, this.guiTop + 224, 200, 134,
                () -> this.defaultPermissionsOpen = true));
        this.buttonDefaultPermissions.activated = true;
        this.settingUp = true;
        this.alignmentField = addRenderableWidget(new EditBox(this.font, this.guiLeft + X_SIZE / 2 - 70, this.guiTop + 100, 130, 18,
                Component.empty()));
        this.alignmentField.setValue(String.valueOf(this.theBanner.getAlignmentProtection()));
        this.alignmentField.setEditable(false);
        refreshWhitelist();
        for (int i = 0; i < this.allowedPlayers.length; ++i) {
            EditBox textBox = this.allowedPlayers[i];
            textBox.setTextColor(WHITE);
            LOTRBannerWhitelistEntry entry = this.theBanner.getWhitelistEntry(i);
            String name = entry == null ? null : entry.displayName();
            if (name != null && !name.isBlank()) {
                textBox.setValue(name);
                textBox.setTextColor(GREEN);
                this.validatedUsernames[i] = true;
            }
        }
        this.allowedPlayers[0].setEditable(false);
        Arrays.fill(this.checkUsernames, false);
        this.settingUp = false;
        updateScreen();
    }

    private void refreshWhitelist() {
        int length = this.theBanner.getWhitelistLength();
        EditBox[] boxesNew = new EditBox[length];
        boolean[] invalidNew = new boolean[length];
        boolean[] validatedNew = new boolean[length];
        boolean[] checkNew = new boolean[length];
        for (int i = 0; i < length; ++i) {
            if (i < this.allowedPlayers.length) {
                boxesNew[i] = this.allowedPlayers[i];
                invalidNew[i] = this.invalidUsernames[i];
                validatedNew[i] = this.validatedUsernames[i];
                checkNew[i] = this.checkUsernames[i];
            } else {
                boxesNew[i] = newWhitelistBox(i);
            }
        }
        for (int i = length; i < this.allowedPlayers.length; ++i) {
            removeWidget(this.allowedPlayers[i]);
        }
        this.allowedPlayers = boxesNew;
        this.invalidUsernames = invalidNew;
        this.validatedUsernames = validatedNew;
        this.checkUsernames = checkNew;
    }

    private EditBox newWhitelistBox(int index) {
        EditBox box = new EditBox(this.font, 0, 0, 130, 18, Component.empty());
        box.setTextColor(WHITE);
        box.setResponder(text -> {
            // keyTyped: a changed name must be checked again.
            if (this.settingUp || index == 0) {
                return;
            }
            this.validatedUsernames[index] = false;
            this.checkUsernames[index] = true;
            box.setTextColor(WHITE);
            updateWhitelistedPlayer(index, null);
        });
        return addRenderableWidget(box);
    }

    private static String invalidText() {
        return Component.translatable("lotr.gui.bannerEdit.invalidUsername").getString();
    }

    // ------------------------------------------------------------ updates

    /** updateScreen, run every tick. */
    private void updateScreen() {
        this.buttonSelfProtection.activated = this.theBanner.isSelfProtection();
        boolean specific = this.theBanner.isPlayerSpecificProtection();
        this.buttonAddSlot.visible = this.buttonRemoveSlot.visible = specific;
        this.buttonAddSlot.active = this.theBanner.getWhitelistLength() < LOTRBannerBlockEntity.WHITELIST_MAX;
        this.buttonRemoveSlot.active = this.theBanner.getWhitelistLength() > LOTRBannerBlockEntity.WHITELIST_MIN;
        this.alignmentField.setVisible(!specific);
        this.alignmentField.setEditable(!specific);
        if (this.alignmentField.visible && !this.alignmentField.isFocused()) {
            float prevAlignment = this.theBanner.getAlignmentProtection();
            float alignment = LOTRAlignmentValues.parseDisplayedAlign(this.alignmentField.getValue());
            alignment = Mth.clamp(alignment, LOTRBannerBlockEntity.ALIGNMENT_PROTECTION_MIN, LOTRBannerBlockEntity.ALIGNMENT_PROTECTION_MAX);
            this.theBanner.setAlignmentProtection(alignment);
            String formatted = LOTRAlignmentValues.formatAlignForDisplay(alignment);
            if (!this.alignmentField.getValue().equals(formatted)) {
                this.alignmentField.setValue(formatted);
            }
            if (alignment != prevAlignment) {
                sendBannerData(false);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        updateScreen();
    }

    private void updateWhitelistedPlayer(int index, @Nullable String username) {
        LOTRBannerWhitelistEntry prevEntry = this.theBanner.getWhitelistEntry(index);
        int prevPerms = prevEntry == null ? -1 : prevEntry.encodePermBitFlags();
        if (username == null || username.isBlank()) {
            this.theBanner.whitelistPlayer(index, null);
            return;
        }
        if (LOTRBannerWhitelistEntry.hasFellowshipCode(username)) {
            String fsName = LOTRBannerWhitelistEntry.stripFellowshipCode(username);
            this.theBanner.whitelistPlayer(index, fsName.isBlank() ? null : LOTRBannerWhitelistEntry.fellowship(null, fsName));
        } else {
            this.theBanner.whitelistPlayer(index, LOTRBannerWhitelistEntry.player(null, username));
        }
        LOTRBannerWhitelistEntry entry = this.theBanner.getWhitelistEntry(index);
        if (prevPerms >= 0 && entry != null) {
            entry.setPermissions(LOTRBannerWhitelistEntry.decodePermBitFlags(prevPerms));
        }
    }

    private void checkUsernameValid(int index) {
        String username = this.allowedPlayers[index].getValue();
        if (!username.isBlank() && !this.invalidUsernames[index]) {
            ClientPlayNetworking.send(new LOTRBannerNamePayloads.Request(this.theBanner.getBlockPos(), index, username));
        }
    }

    private void validateUsername(int index, String prevText, boolean valid) {
        if (index < 0 || index >= this.allowedPlayers.length) {
            return;
        }
        EditBox textBox = this.allowedPlayers[index];
        if (!textBox.getValue().equals(prevText)) {
            return;
        }
        if (valid) {
            this.validatedUsernames[index] = true;
            this.invalidUsernames[index] = false;
            textBox.setTextColor(GREEN);
            updateWhitelistedPlayer(index, prevText);
        } else {
            this.invalidUsernames[index] = true;
            this.validatedUsernames[index] = false;
            textBox.setTextColor(RED);
            this.settingUp = true;
            textBox.setValue(invalidText());
            this.settingUp = false;
            updateWhitelistedPlayer(index, null);
        }
    }

    private void sendBannerData(boolean sendWhitelist) {
        Optional<List<LOTRBannerDataPayload.Slot>> whitelist = Optional.empty();
        if (sendWhitelist) {
            List<LOTRBannerDataPayload.Slot> slots = new ArrayList<>();
            for (int index = 1; index < this.allowedPlayers.length; ++index) {
                String text = this.invalidUsernames[index] ? "" : this.allowedPlayers[index].getValue();
                updateWhitelistedPlayer(index, text);
                LOTRBannerWhitelistEntry entry = this.theBanner.getWhitelistEntry(index);
                String name = entry == null ? null : entry.displayName();
                if (name == null || name.isBlank()) {
                    slots.add(new LOTRBannerDataPayload.Slot(index, "", 0));
                } else {
                    slots.add(new LOTRBannerDataPayload.Slot(index, name, entry.encodePermBitFlags()));
                }
            }
            whitelist = Optional.of(slots);
        }
        ClientPlayNetworking.send(new LOTRBannerEditPayload(this.theBanner.getBlockPos(),
                this.theBanner.isPlayerSpecificProtection(), this.theBanner.isSelfProtection(),
                this.theBanner.getAlignmentProtection(), this.theBanner.getWhitelistLength(), whitelist,
                this.theBanner.getDefaultPermBitFlags()));
    }

    @Override
    public void removed() {
        super.removed();
        sendBannerData(true);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ------------------------------------------------------------ drawing

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.permissionsMouseoverIndex = -1;
        this.permissionsMouseoverY = -1;
        this.mouseOverPermission = null;
        for (EditBox box : this.allowedPlayers) {
            box.setVisible(false);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0f, 0.0f, X_SIZE, Y_SIZE, 256, 256);
        graphics.text(this.font, this.title, this.guiLeft + X_SIZE / 2 - this.font.width(this.title) / 2, this.guiTop + 6,
                LABEL_COLOUR, false);
        if (this.theBanner.isPlayerSpecificProtection()) {
            this.buttonMode.setMessage(Component.translatable("lotr.gui.bannerEdit.protectionMode.playerSpecific"));
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.protectionMode.playerSpecific.desc.1"), this.guiTop + 46);
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.protectionMode.playerSpecific.desc.2"),
                    this.guiTop + 46 + this.font.lineHeight);
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.fellowshipHint",
                    LOTRBannerWhitelistEntry.FELLOWSHIP_PREFIX), this.guiTop + 206);
            int start = Math.round(this.currentScroll * (this.allowedPlayers.length - SLOTS_SHOWN));
            int end = start + SLOTS_SHOWN - 1;
            start = Math.max(start, 0);
            end = Math.min(end, this.allowedPlayers.length - 1);
            for (int index = start; index <= end; ++index) {
                int displayIndex = index - start;
                EditBox textBox = this.allowedPlayers[index];
                textBox.setVisible(true);
                textBox.setEditable(index != 0);
                textBox.setX(this.guiLeft + X_SIZE / 2 - 70);
                textBox.setY(this.guiTop + 70 + displayIndex * (textBox.getHeight() + 4));
                String number = (index + 1) + ".";
                graphics.text(this.font, number, this.guiLeft + 24 - this.font.width(number), textBox.getY() + 6, LABEL_COLOUR, false);
                if (index == 0 || !this.validatedUsernames[index]) {
                    continue;
                }
                int permX = textBox.getX() + textBox.getWidth() + PERM_ICON_X;
                int permY = textBox.getY() + PERM_ICON_Y;
                boolean mouseOver = mouseX >= permX && mouseX < permX + PERM_ICON_WIDTH && mouseY >= permY && mouseY < permY + PERM_ICON_WIDTH;
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, permX, permY, 200 + (mouseOver ? PERM_ICON_WIDTH : 0), 150,
                        PERM_ICON_WIDTH, PERM_ICON_WIDTH, 256, 256);
                if (mouseOver) {
                    this.permissionsMouseoverIndex = index;
                    this.permissionsMouseoverY = textBox.getY();
                }
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft + SCROLL_BAR_X, this.guiTop + SCROLL_BAR_Y, 200, 0,
                    SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT, 256, 256);
            int scroll = (int) (this.currentScroll * (SCROLL_BAR_HEIGHT - SCROLL_BAR_BORDER * 2 - SCROLL_WIDGET_HEIGHT));
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft + SCROLL_BAR_X + SCROLL_BAR_BORDER,
                    this.guiTop + SCROLL_BAR_Y + SCROLL_BAR_BORDER + scroll, 212, 0, SCROLL_WIDGET_WIDTH, SCROLL_WIDGET_HEIGHT, 256, 256);
        } else {
            this.permissionsOpenY = -1;
            this.permissionsOpenIndex = -1;
            this.buttonMode.setMessage(Component.translatable("lotr.gui.bannerEdit.protectionMode.faction"));
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.protectionMode.faction.desc.1"), this.guiTop + 46);
            String alignment = LOTRAlignmentValues.formatAlignForDisplay(this.theBanner.getAlignmentProtection());
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.protectionMode.faction.desc.2",
                    alignment.startsWith("+") ? alignment.substring(1) : alignment,
                    this.theBanner.getBannerType().faction.factionName()), this.guiTop + 46 + this.font.lineHeight);
            centred(graphics, Component.translatable("lotr.gui.bannerEdit.protectionMode.faction.desc.3"),
                    this.guiTop + 46 + this.font.lineHeight * 2);
            graphics.text(this.font, Component.translatable("lotr.gui.bannerEdit.protectionMode.faction.alignment"),
                    this.alignmentField.getX(), this.alignmentField.getY() - this.font.lineHeight - 3, LABEL_COLOUR, false);
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (this.permissionsOpenIndex >= 0 && this.permissionsOpenIndex < this.allowedPlayers.length) {
            String username = this.allowedPlayers[this.permissionsOpenIndex].getValue();
            boolean isFellowship = LOTRBannerWhitelistEntry.hasFellowshipCode(username);
            if (isFellowship) {
                username = LOTRBannerWhitelistEntry.stripFellowshipCode(username);
            }
            LOTRBannerWhitelistEntry entry = this.theBanner.getWhitelistEntry(this.permissionsOpenIndex);
            drawPermissionsWindow(graphics, mouseX, mouseY, this.guiLeft + X_SIZE + PERM_WINDOW_BORDER, this.permissionsOpenY,
                    Component.translatable(isFellowship ? "lotr.gui.bannerEdit.perms.fellowship" : "lotr.gui.bannerEdit.perms.player"),
                    Component.translatable("lotr.gui.bannerEdit.perms.name", username),
                    p -> entry != null && entry.isPermissionEnabled(p), true);
        }
        if (this.defaultPermissionsOpen) {
            drawPermissionsWindow(graphics, mouseX, mouseY, this.guiLeft + X_SIZE + PERM_WINDOW_BORDER,
                    this.guiTop + Y_SIZE - PERM_WINDOW_HEIGHT, Component.translatable("lotr.gui.bannerEdit.perms.default"),
                    Component.translatable("lotr.gui.bannerEdit.perms.allPlayers"), this.theBanner::hasDefaultPermission, false);
        }
        if (this.buttonSelfProtection.isHovered()) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("lotr.gui.bannerEdit.selfProtection."
                    + (this.buttonSelfProtection.activated ? "on" : "off")), mouseX, mouseY);
        }
        if (this.buttonDefaultPermissions.isHovered()) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("lotr.gui.bannerEdit.perms.default"), mouseX, mouseY);
        }
        if (this.permissionsMouseoverIndex >= 0) {
            boolean isFellowship = LOTRBannerWhitelistEntry.hasFellowshipCode(this.allowedPlayers[this.permissionsMouseoverIndex].getValue());
            graphics.setTooltipForNextFrame(this.font, Component.translatable(isFellowship
                    ? "lotr.gui.bannerEdit.perms.fellowship" : "lotr.gui.bannerEdit.perms.player"), mouseX, mouseY);
        }
    }

    private void centred(GuiGraphicsExtractor graphics, Component text, int y) {
        graphics.text(this.font, text, this.guiLeft + X_SIZE / 2 - this.font.width(text) / 2, y, LABEL_COLOUR, false);
    }

    private void drawPermissionsWindow(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int windowX, int windowY,
                                       Component boxTitle, Component boxSubtitle,
                                       Function<LOTRBannerProtection.Permission, Boolean> getEnabled, boolean includeFull) {
        graphics.fill(windowX, windowY, windowX + PERM_WINDOW_WIDTH, windowY + PERM_WINDOW_HEIGHT, 0xAA000000);
        graphics.text(this.font, boxTitle, windowX + 4, windowY + 4, WHITE, false);
        graphics.text(this.font, boxSubtitle, windowX + 4, windowY + 14, 0xFFAAAAAA, false);
        int x = windowX + 4;
        int y = windowY + 32;
        this.mouseOverPermission = null;
        for (LOTRBannerProtection.Permission p : LOTRBannerProtection.Permission.values()) {
            if (!includeFull && p == LOTRBannerProtection.Permission.FULL) {
                continue;
            }
            if (mouseX >= x && mouseX < x + 10 && mouseY >= y && mouseY < y + 10) {
                this.mouseOverPermission = p;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y,
                    200 + (getEnabled.apply(p) ? 0 : 20) + (this.mouseOverPermission == p ? 10 : 0), 160 + p.ordinal() * 10,
                    10, 10, 256, 256);
            x += 14;
            if (p == LOTRBannerProtection.Permission.FULL) {
                x += 4;
            }
        }
        if (this.mouseOverPermission != null) {
            graphics.textWithWordWrap(this.font, Component.translatable("lotr.gui.bannerEdit.perm." + this.mouseOverPermission.codeName),
                    windowX + 4, windowY + 47, PERM_WINDOW_WIDTH - PERM_WINDOW_BORDER * 2, WHITE);
        }
    }

    // ------------------------------------------------------------ input

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double i = event.x();
        double j = event.y();
        int barX = this.guiLeft + SCROLL_BAR_X;
        int barY = this.guiTop + SCROLL_BAR_Y;
        if (this.theBanner.isPlayerSpecificProtection() && i >= barX && j >= barY && i < barX + SCROLL_BAR_WIDTH
                && j < barY + SCROLL_BAR_HEIGHT) {
            this.isScrolling = true;
            scrollTo(j);
            return true;
        }
        boolean handled = super.mouseClicked(event, doubleClick);
        for (int l = 1; l < this.allowedPlayers.length; ++l) {
            EditBox textBox = this.allowedPlayers[l];
            if (!textBox.visible) {
                continue;
            }
            if (!textBox.isFocused() && this.checkUsernames[l]) {
                checkUsernameValid(l);
                this.checkUsernames[l] = false;
            }
            if (textBox.isFocused() && this.invalidUsernames[l]) {
                this.invalidUsernames[l] = false;
                textBox.setTextColor(WHITE);
                textBox.setValue("");
            }
        }
        if (this.permissionsMouseoverIndex >= 0) {
            this.permissionsOpenIndex = this.permissionsMouseoverIndex;
            this.permissionsOpenY = this.permissionsMouseoverY;
            this.permissionsMouseoverIndex = -1;
            this.permissionsMouseoverY = -1;
            this.defaultPermissionsOpen = false;
            return true;
        }
        if (this.permissionsOpenIndex >= 0) {
            double dx = i - (this.guiLeft + X_SIZE + PERM_WINDOW_BORDER);
            double dy = j - this.permissionsOpenY;
            if (dx < 0 || dx >= PERM_WINDOW_WIDTH || dy < 0 || dy >= PERM_WINDOW_HEIGHT) {
                this.permissionsOpenY = -1;
                this.permissionsOpenIndex = -1;
                return handled;
            }
            LOTRBannerWhitelistEntry entry = this.theBanner.getWhitelistEntry(this.permissionsOpenIndex);
            if (this.mouseOverPermission != null && entry != null) {
                LOTRBannerProtection.Permission p = this.mouseOverPermission;
                if (p == LOTRBannerProtection.Permission.FULL) {
                    boolean had = entry.isPermissionEnabled(p);
                    entry.clearPermissions();
                    if (!had) {
                        entry.addPermission(p);
                    }
                } else if (entry.isPermissionEnabled(p)) {
                    entry.removePermission(p);
                } else {
                    entry.removePermission(LOTRBannerProtection.Permission.FULL);
                    entry.addPermission(p);
                }
                return true;
            }
        }
        if (this.defaultPermissionsOpen) {
            double dx = i - (this.guiLeft + X_SIZE + PERM_WINDOW_BORDER);
            double dy = j - (this.guiTop + Y_SIZE - PERM_WINDOW_HEIGHT);
            if ((dx < 0 || dx >= PERM_WINDOW_WIDTH || dy < 0 || dy >= PERM_WINDOW_HEIGHT)
                    && !this.buttonDefaultPermissions.isMouseOver(i, j)) {
                this.defaultPermissionsOpen = false;
                return handled;
            }
            if (this.mouseOverPermission != null) {
                if (this.theBanner.hasDefaultPermission(this.mouseOverPermission)) {
                    this.theBanner.removeDefaultPermission(this.mouseOverPermission);
                } else {
                    this.theBanner.addDefaultPermission(this.mouseOverPermission);
                }
                sendBannerData(false);
                return true;
            }
        }
        return handled;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (this.isScrolling) {
            scrollTo(event.y());
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.isScrolling = false;
        return super.mouseReleased(event);
    }

    private void scrollTo(double mouseY) {
        int top = this.guiTop + SCROLL_BAR_Y;
        this.currentScroll = Mth.clamp((float) ((mouseY - top - SCROLL_WIDGET_HEIGHT / 2.0) / (SCROLL_BAR_HEIGHT - SCROLL_WIDGET_HEIGHT)),
                0.0f, 1.0f);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        int extra = this.allowedPlayers.length - SLOTS_SHOWN;
        if (scrollY != 0 && extra > 0) {
            this.currentScroll = Mth.clamp(this.currentScroll - (float) Math.signum(scrollY) / extra, 0.0f, 1.0f);
            return true;
        }
        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        boolean closeKey = event.key() == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
                || this.minecraft != null && this.minecraft.options.keyInventory.matches(event);
        if (closeKey && this.permissionsOpenIndex >= 0 && getFocused() == null) {
            this.permissionsOpenY = -1;
            this.permissionsOpenIndex = -1;
            return true;
        }
        if (closeKey && this.defaultPermissionsOpen && getFocused() == null) {
            this.defaultPermissionsOpen = false;
            return true;
        }
        return super.keyPressed(event);
    }

    // ------------------------------------------------------------ the buttons

    /** LOTRGuiButtonBanner: a 16x16 icon, lit when activated, brighter under the mouse. */
    private static final class IconButton extends AbstractButton {
        private final int iconU;
        private final int iconV;
        private final Runnable action;
        boolean activated;

        IconButton(int x, int y, int u, int v, Runnable action) {
            super(x, y, 16, 16, Component.empty());
            this.iconU = u;
            this.iconV = v;
            this.action = action;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.action.run();
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            int state = (this.activated ? 0 : 2) + (isHovered() ? 1 : 0);
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), this.iconU + state % 2 * this.width,
                    this.iconV + state / 2 * this.height, this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    /** LOTRGuiButtonBannerWhitelistSlots: the 7x7 add and remove buttons. */
    private static final class SlotButton extends AbstractButton {
        private final int id;
        private final IntConsumer action;

        SlotButton(int id, int x, int y, IntConsumer action) {
            super(x, y, 7, 7, Component.empty());
            this.id = id;
            this.action = action;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.action.accept(this.id);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            int hoverState = !this.active ? 0 : isHovered() ? 2 : 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), 226 + this.id * this.width,
                    hoverState * this.height, this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
