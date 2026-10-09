package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship.LOTRClientFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowshipPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowshipPayloads.View;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiFellowships: the fellowships the player leads and those they are in, each opened to show its
 * players (their titles, who leads it and who are its admins, who is online) and what its owner and
 * admins may do -- invite, disband, rename, set its icon from the item in hand, and set whether its
 * members may hurt one another, one another's hired units, and are shown on the map; remove, make
 * admin or hand the fellowship to a player -- each asked once more before it is done; and the
 * invitations the player has, to accept or turn down.
 */
public class LOTRFellowshipsScreen extends LOTRMenuBaseScreen {

    public static final Identifier ICONS = Identifier.fromNamespaceAndPath("lotr", "gui/fellowships.png");

    private enum Page {
        LIST, CREATE, FELLOWSHIP, INVITE, DISBAND, LEAVE, REMOVE, OP, DEOP, TRANSFER, RENAME, INVITATIONS,
        ACCEPT_INVITE_RESULT
    }

    private Page page = Page.LIST;
    private final List<View> allFellowshipsLeading = new ArrayList<>();
    private final List<View> allFellowshipsOther = new ArrayList<>();
    private final List<View> allFellowshipInvites = new ArrayList<>();
    private @Nullable View mouseOverFellowship;
    private @Nullable View viewingFellowship;
    private @Nullable UUID mouseOverPlayer;
    private boolean mouseOverPlayerRemove;
    private boolean mouseOverPlayerOp;
    private boolean mouseOverPlayerDeop;
    private boolean mouseOverPlayerTransfer;
    private @Nullable UUID removingPlayer;
    private @Nullable UUID oppingPlayer;
    private @Nullable UUID deoppingPlayer;
    private @Nullable UUID transferringPlayer;
    private boolean mouseOverInviteAccept;
    private boolean mouseOverInviteReject;
    private LOTRFellowshipPayloads.@Nullable AcceptInviteResult acceptInviteResult;
    private @Nullable String acceptInviteResultFellowshipName;
    private Button buttonCreate;
    private Button buttonCreateThis;
    private OptionButton buttonInvitePlayer;
    private Button buttonInviteThis;
    private OptionButton buttonDisband;
    private Button buttonDisbandThis;
    private Button buttonLeave;
    private Button buttonLeaveThis;
    private OptionButton buttonSetIcon;
    private Button buttonRemove;
    private Button buttonTransfer;
    private OptionButton buttonRename;
    private Button buttonRenameThis;
    private Button buttonBack;
    private InvitesButton buttonInvites;
    private OptionButton buttonPVP;
    private OptionButton buttonHiredFF;
    private OptionButton buttonMapShow;
    private Button buttonOp;
    private Button buttonDeop;
    private final List<OptionButton> orderedFsOptionButtons = new ArrayList<>();
    private EditBox textFieldName;
    private EditBox textFieldPlayer;
    private EditBox textFieldRename;
    private final int scrollBarX;
    private final LOTRScrollPane scrollPaneLeading = new LOTRScrollPane(9, 8);
    private final LOTRScrollPane scrollPaneOther = new LOTRScrollPane(9, 8);
    private final LOTRScrollPane scrollPaneMembers = new LOTRScrollPane(9, 8);
    private final LOTRScrollPane scrollPaneInvites = new LOTRScrollPane(9, 8);
    private int displayedFellowshipsLeading;
    private int displayedFellowshipsOther;
    private int displayedMembers;
    private int displayedInvites;
    private int tickCounter;

    public LOTRFellowshipsScreen() {
        super(Component.translatable("lotr.gui.fellowships.title"));
        this.xSize = 256;
        this.scrollBarX = this.xSize + 2 + 1;
    }

    private boolean isPlayerOnline(UUID player, String username) {
        return this.minecraft.getConnection() != null
                && (this.minecraft.getConnection().getPlayerInfo(player) != null
                || this.minecraft.getConnection().getPlayerInfoIgnoreCase(username) != null);
    }

    private void send(net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    protected void init() {
        super.init();
        refreshFellowshipList();
        int midX = this.guiLeft + this.xSize / 2;
        this.buttonCreate = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.create"),
                b -> this.page = Page.CREATE).bounds(midX - 100, this.guiTop + 230, 200, 20).build());
        this.buttonCreateThis = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.createThis"), b -> {
            String name = this.textFieldName.getValue();
            if (checkValidFellowshipName(name) == null) {
                send(new LOTRFellowshipPayloads.Create(StringUtils.trim(name)));
            }
            this.page = Page.LIST;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonInvitePlayer = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 0, 48,
                Component.translatable("lotr.gui.fellowships.invite"), () -> this.page = Page.INVITE));
        this.buttonInviteThis = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.inviteThis"), b -> {
            String name = this.textFieldPlayer.getValue();
            if (checkValidPlayerName(name) == null) {
                send(new LOTRFellowshipPayloads.InvitePlayer(this.viewingFellowship.fellowshipID(), StringUtils.trim(name)));
            }
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonDisband = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 16, 48,
                Component.translatable("lotr.gui.fellowships.disband"), () -> this.page = Page.DISBAND));
        this.buttonDisbandThis = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.disbandThis"), b -> {
            send(new LOTRFellowshipPayloads.Do(this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.DISBAND));
            this.page = Page.LIST;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonLeave = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.leave"),
                b -> this.page = Page.LEAVE).bounds(midX - 60, this.guiTop + 230, 120, 20).build());
        this.buttonLeaveThis = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.leaveThis"), b -> {
            send(new LOTRFellowshipPayloads.Do(this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.LEAVE));
            this.page = Page.LIST;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonSetIcon = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 48, 48,
                Component.translatable("lotr.gui.fellowships.setIcon"), () -> send(new LOTRFellowshipPayloads.Do(
                this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.SET_ICON))));
        this.buttonRemove = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.remove"), b -> {
            send(new LOTRFellowshipPayloads.DoPlayer(this.viewingFellowship.fellowshipID(), this.removingPlayer,
                    LOTRFellowshipPayloads.PlayerFunction.REMOVE));
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonTransfer = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.transfer"), b -> {
            send(new LOTRFellowshipPayloads.DoPlayer(this.viewingFellowship.fellowshipID(), this.transferringPlayer,
                    LOTRFellowshipPayloads.PlayerFunction.TRANSFER));
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonRename = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 32, 48,
                Component.translatable("lotr.gui.fellowships.rename"), () -> this.page = Page.RENAME));
        this.buttonRenameThis = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.renameThis"), b -> {
            String name = this.textFieldRename.getValue();
            if (checkValidFellowshipName(name) == null) {
                send(new LOTRFellowshipPayloads.Rename(this.viewingFellowship.fellowshipID(), StringUtils.trim(name)));
            }
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonBack = addRenderableWidget(Button.builder(Component.literal("<"), b -> goBack())
                .bounds(this.guiLeft - 10, this.guiTop, 20, 20).build());
        this.buttonInvites = addRenderableWidget(new InvitesButton(this.guiLeft + this.xSize - 16, this.guiTop,
                () -> this.page = Page.INVITATIONS));
        this.buttonPVP = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 64, 48,
                Component.translatable("lotr.gui.fellowships.togglePVP"), () -> send(new LOTRFellowshipPayloads.Do(
                this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.TOGGLE_PVP))));
        this.buttonHiredFF = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 80, 48,
                Component.translatable("lotr.gui.fellowships.toggleHiredFF"), () -> send(new LOTRFellowshipPayloads.Do(
                this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.TOGGLE_HIRED_FF))));
        this.buttonMapShow = addRenderableWidget(new OptionButton(midX, this.guiTop + 232, 96, 48,
                Component.translatable("lotr.gui.fellowships.toggleMapShow"), () -> send(new LOTRFellowshipPayloads.Do(
                this.viewingFellowship.fellowshipID(), LOTRFellowshipPayloads.Action.TOGGLE_MAP_SHOW))));
        this.buttonOp = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.op"), b -> {
            send(new LOTRFellowshipPayloads.DoPlayer(this.viewingFellowship.fellowshipID(), this.oppingPlayer,
                    LOTRFellowshipPayloads.PlayerFunction.OP));
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.buttonDeop = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.fellowships.deop"), b -> {
            send(new LOTRFellowshipPayloads.DoPlayer(this.viewingFellowship.fellowshipID(), this.deoppingPlayer,
                    LOTRFellowshipPayloads.PlayerFunction.DEOP));
            this.page = Page.FELLOWSHIP;
        }).bounds(midX - 100, this.guiTop + 170, 200, 20).build());
        this.orderedFsOptionButtons.clear();
        this.orderedFsOptionButtons.addAll(List.of(this.buttonInvitePlayer, this.buttonDisband, this.buttonRename,
                this.buttonSetIcon, this.buttonMapShow, this.buttonPVP, this.buttonHiredFF));
        this.textFieldName = addRenderableWidget(new EditBox(this.font, midX - 80, this.guiTop + 40, 160, 20,
                Component.translatable("lotr.gui.fellowships.createName")));
        this.textFieldName.setMaxLength(40);
        this.textFieldPlayer = addRenderableWidget(new EditBox(this.font, midX - 80, this.guiTop + 40, 160, 20,
                Component.translatable("lotr.gui.fellowships.inviteName")));
        this.textFieldRename = addRenderableWidget(new EditBox(this.font, midX - 80, this.guiTop + 40, 160, 20,
                Component.translatable("lotr.gui.fellowships.renameName")));
        this.textFieldRename.setMaxLength(40);
    }

    /** The Back button and Escape on a page below the list: up a page. */
    private void goBack() {
        switch (this.page) {
            case INVITE, DISBAND, LEAVE, REMOVE, OP, DEOP, TRANSFER, RENAME -> this.page = Page.FELLOWSHIP;
            case ACCEPT_INVITE_RESULT -> {
                if (this.acceptInviteResult != null) {
                    this.page = Page.INVITATIONS;
                }
            }
            default -> this.page = Page.LIST;
        }
    }

    private void alignOptionButtons() {
        List<AbstractWidget> active = new ArrayList<>();
        for (OptionButton button : this.orderedFsOptionButtons) {
            if (button.visible) {
                active.add(button);
            }
        }
        if (this.buttonLeave.visible) {
            active.add(this.buttonLeave);
        }
        if (!active.isEmpty()) {
            int gap = 8;
            int allWidth = 0;
            for (AbstractWidget button : active) {
                if (allWidth > 0) {
                    allWidth += gap;
                }
                allWidth += button.getWidth();
            }
            int x = this.guiLeft + this.xSize / 2 - allWidth / 2;
            for (AbstractWidget button : active) {
                button.setX(x);
                x += button.getWidth() + gap;
            }
        }
    }

    private @Nullable Component checkValidFellowshipName(String name) {
        if (!StringUtils.isWhitespace(name)) {
            return LOTRClientFellowships.anyMatchingFellowshipNames(name)
                    ? Component.translatable("lotr.gui.fellowships.nameExists") : null;
        }
        return Component.empty();
    }

    private @Nullable Component checkValidPlayerName(String name) {
        if (!StringUtils.isWhitespace(name)) {
            return this.viewingFellowship.containsPlayerUsername(name)
                    ? Component.translatable("lotr.gui.fellowships.playerExists", name) : null;
        }
        return Component.empty();
    }

    private int countOnlineMembers(View fs) {
        int i = 0;
        for (UUID player : fs.getAllPlayerUuids()) {
            if (isPlayerOnline(player, fs.getUsernameFor(player))) {
                ++i;
            }
        }
        return i;
    }

    /** displayAcceptInvitationResult: joined, the fellowship is shown; otherwise why not. */
    public void displayAcceptInvitationResult(UUID fellowshipID, String name, LOTRFellowshipPayloads.AcceptInviteResult result) {
        if (this.page == Page.ACCEPT_INVITE_RESULT) {
            if (result == LOTRFellowshipPayloads.AcceptInviteResult.JOINED) {
                this.page = Page.FELLOWSHIP;
                this.viewingFellowship = LOTRClientFellowships.getByID(fellowshipID);
                if (this.viewingFellowship == null) {
                    this.page = Page.LIST;
                }
            } else {
                this.acceptInviteResult = result;
                this.acceptInviteResultFellowshipName = name;
            }
        }
    }

    private boolean isFellowshipMaxSize(@Nullable View fs) {
        if (fs != null) {
            int limit = LOTRConfig.getFellowshipMaxSize(this.minecraft.level);
            return limit >= 0 && fs.getPlayerCount() >= limit;
        }
        return false;
    }

    private void refreshFellowshipList() {
        this.allFellowshipsLeading.clear();
        this.allFellowshipsOther.clear();
        for (View fs : LOTRClientFellowships.getFellowships()) {
            (fs.owned() ? this.allFellowshipsLeading : this.allFellowshipsOther).add(fs);
        }
        this.allFellowshipInvites.clear();
        this.allFellowshipInvites.addAll(LOTRClientFellowships.getInvites());
        // The fellowship shown, as it now is (or the list, if the player is out of it).
        if (this.viewingFellowship != null) {
            View current = LOTRClientFellowships.getByID(this.viewingFellowship.fellowshipID());
            if (current != null) {
                this.viewingFellowship = current;
            } else if (this.page != Page.LIST && this.page != Page.INVITATIONS && this.page != Page.ACCEPT_INVITE_RESULT
                    && this.page != Page.CREATE) {
                this.page = Page.LIST;
                this.viewingFellowship = null;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        ++this.tickCounter;
        refreshFellowshipList();
        if (this.page != Page.CREATE) {
            this.textFieldName.setValue("");
        }
        if (this.page != Page.INVITE || isFellowshipMaxSize(this.viewingFellowship)) {
            this.textFieldPlayer.setValue("");
        }
        if (this.page != Page.RENAME) {
            this.textFieldRename.setValue("");
        }
    }

    private void updateButtons() {
        boolean viewingOwned = this.viewingFellowship != null && this.viewingFellowship.owned();
        boolean viewingAdminned = this.viewingFellowship != null && this.viewingFellowship.adminned();
        boolean creationEnabled = LOTRConfig.isFellowshipCreationEnabled(this.minecraft.level);
        this.buttonCreate.visible = this.page == Page.LIST;
        this.buttonCreate.active = this.buttonCreate.visible && creationEnabled && LOTRClientFellowships.canCreateFellowships();
        this.buttonCreateThis.visible = this.page == Page.CREATE;
        this.buttonCreateThis.active = this.buttonCreateThis.visible && checkValidFellowshipName(this.textFieldName.getValue()) == null;
        this.buttonInvitePlayer.visible = this.buttonInvitePlayer.active = this.page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
        boolean canInvite = this.page == Page.INVITE && !isFellowshipMaxSize(this.viewingFellowship);
        this.buttonInviteThis.visible = canInvite;
        this.buttonInviteThis.active = canInvite && checkValidPlayerName(this.textFieldPlayer.getValue()) == null;
        this.buttonDisband.visible = this.buttonDisband.active = this.page == Page.FELLOWSHIP && viewingOwned;
        this.buttonDisbandThis.visible = this.buttonDisbandThis.active = this.page == Page.DISBAND;
        this.buttonLeave.visible = this.buttonLeave.active = this.page == Page.FELLOWSHIP && !viewingOwned;
        this.buttonLeaveThis.visible = this.buttonLeaveThis.active = this.page == Page.LEAVE;
        this.buttonSetIcon.visible = this.buttonSetIcon.active = this.page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
        this.buttonRemove.visible = this.buttonRemove.active = this.page == Page.REMOVE;
        this.buttonTransfer.visible = this.buttonTransfer.active = this.page == Page.TRANSFER;
        this.buttonRename.visible = this.buttonRename.active = this.page == Page.FELLOWSHIP && viewingOwned;
        this.buttonRenameThis.visible = this.page == Page.RENAME;
        this.buttonRenameThis.active = this.buttonRenameThis.visible && checkValidFellowshipName(this.textFieldRename.getValue()) == null;
        this.buttonBack.visible = this.buttonBack.active = this.page != Page.LIST;
        this.buttonInvites.visible = this.buttonInvites.active = this.page == Page.LIST;
        this.buttonPVP.visible = this.buttonPVP.active = this.page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
        if (this.buttonPVP.active) {
            this.buttonPVP.setIconUV(64, this.viewingFellowship.preventPVP() ? 80 : 48);
        }
        this.buttonHiredFF.visible = this.buttonHiredFF.active = this.page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
        if (this.buttonHiredFF.active) {
            this.buttonHiredFF.setIconUV(80, this.viewingFellowship.preventHiredFF() ? 80 : 48);
        }
        this.buttonMapShow.visible = this.buttonMapShow.active = this.page == Page.FELLOWSHIP && viewingOwned;
        if (this.buttonMapShow.active) {
            this.buttonMapShow.setIconUV(96, this.viewingFellowship.showMapLocations() ? 48 : 80);
        }
        this.buttonOp.visible = this.buttonOp.active = this.page == Page.OP;
        this.buttonDeop.visible = this.buttonDeop.active = this.page == Page.DEOP;
        this.textFieldName.visible = this.page == Page.CREATE;
        this.textFieldPlayer.visible = this.page == Page.INVITE && !isFellowshipMaxSize(this.viewingFellowship);
        this.textFieldRename.visible = this.page == Page.RENAME;
        for (EditBox field : List.of(this.textFieldName, this.textFieldPlayer, this.textFieldRename)) {
            if (!field.visible && getFocused() == field) {
                setFocused(null);
            } else if (field.visible && getFocused() != field) {
                setFocused(field);
            }
        }
        alignOptionButtons();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseOverFellowship = null;
        this.mouseOverPlayer = null;
        this.mouseOverPlayerRemove = false;
        this.mouseOverPlayerOp = false;
        this.mouseOverPlayerDeop = false;
        this.mouseOverPlayerTransfer = false;
        if (this.page != Page.REMOVE) {
            this.removingPlayer = null;
        }
        if (this.page != Page.OP) {
            this.oppingPlayer = null;
        }
        if (this.page != Page.DEOP) {
            this.deoppingPlayer = null;
        }
        if (this.page != Page.TRANSFER) {
            this.transferringPlayer = null;
        }
        this.mouseOverInviteAccept = false;
        this.mouseOverInviteReject = false;
        if (this.page != Page.ACCEPT_INVITE_RESULT) {
            this.acceptInviteResult = null;
            this.acceptInviteResultFellowshipName = null;
        }
        updateButtons();
        setupScrollBars(mouseX, mouseY);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int midX = this.guiLeft + this.xSize / 2;
        graphics.centeredText(this.font, this.title, midX, this.guiTop - 30, 0xFFFFFFFF);
        switch (this.page) {
            case LIST -> drawList(graphics, mouseX, mouseY);
            case CREATE -> drawTextFieldPage(graphics, Component.translatable("lotr.gui.fellowships.createName"),
                    this.textFieldName, checkValidFellowshipName(this.textFieldName.getValue()));
            case FELLOWSHIP -> drawFellowship(graphics, mouseX, mouseY);
            case INVITE -> {
                if (isFellowshipMaxSize(this.viewingFellowship)) {
                    drawLines(graphics, Component.translatable("lotr.gui.fellowships.invite.maxSize",
                            this.viewingFellowship.name(), LOTRConfig.getFellowshipMaxSize(this.minecraft.level)), this.guiTop + 30);
                } else {
                    drawTextFieldPage(graphics, Component.translatable("lotr.gui.fellowships.inviteName",
                            this.viewingFellowship.name()), this.textFieldPlayer, checkValidPlayerName(this.textFieldPlayer.getValue()));
                }
            }
            case DISBAND -> {
                int y = this.guiTop + 30;
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.disbandCheck1",
                        this.viewingFellowship.name()), midX, y, 0xFFFFFFFF);
                y += this.font.lineHeight;
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.disbandCheck2"), midX, y, 0xFFFFFFFF);
                y += this.font.lineHeight * 2;
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.disbandCheck3"), midX, y, 0xFFFFFFFF);
            }
            case LEAVE -> {
                int y = this.guiTop + 30;
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.leaveCheck1",
                        this.viewingFellowship.name()), midX, y, 0xFFFFFFFF);
                y += this.font.lineHeight;
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.leaveCheck2"), midX, y, 0xFFFFFFFF);
            }
            case REMOVE -> drawLines(graphics, Component.translatable("lotr.gui.fellowships.removeCheck",
                    this.viewingFellowship.name(), this.viewingFellowship.getUsernameFor(this.removingPlayer)), this.guiTop + 30);
            case OP -> {
                int y = drawLines(graphics, Component.translatable("lotr.gui.fellowships.opCheck1",
                        this.viewingFellowship.name(), this.viewingFellowship.getUsernameFor(this.oppingPlayer)), this.guiTop + 30);
                drawLines(graphics, Component.translatable("lotr.gui.fellowships.opCheck2",
                        this.viewingFellowship.name(), this.viewingFellowship.getUsernameFor(this.oppingPlayer)),
                        y + this.font.lineHeight);
            }
            case DEOP -> drawLines(graphics, Component.translatable("lotr.gui.fellowships.deopCheck",
                    this.viewingFellowship.name(), this.viewingFellowship.getUsernameFor(this.deoppingPlayer)), this.guiTop + 30);
            case TRANSFER -> {
                int y = drawLines(graphics, Component.translatable("lotr.gui.fellowships.transferCheck1",
                        this.viewingFellowship.name(), this.viewingFellowship.getUsernameFor(this.transferringPlayer)),
                        this.guiTop + 30);
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.transferCheck2"), midX,
                        y + this.font.lineHeight, 0xFFFFFFFF);
            }
            case RENAME -> drawTextFieldPage(graphics, Component.translatable("lotr.gui.fellowships.renameName",
                    this.viewingFellowship.name()), this.textFieldRename, checkValidFellowshipName(this.textFieldRename.getValue()));
            case INVITATIONS -> drawInvitations(graphics, mouseX, mouseY);
            case ACCEPT_INVITE_RESULT -> drawAcceptResult(graphics);
            default -> {
            }
        }
    }

    private int drawLines(GuiGraphicsExtractor graphics, Component text, int y) {
        for (FormattedCharSequence line : this.font.split(text, this.xSize)) {
            graphics.centeredText(this.font, line, this.guiLeft + this.xSize / 2, y, 0xFFFFFFFF);
            y += this.font.lineHeight;
        }
        return y;
    }

    private void drawTextFieldPage(GuiGraphicsExtractor graphics, Component label, EditBox field, @Nullable Component error) {
        int midX = this.guiLeft + this.xSize / 2;
        graphics.centeredText(this.font, label, midX, field.getY() - 4 - this.font.lineHeight, 0xFFFFFFFF);
        if (error != null) {
            graphics.centeredText(this.font, error, midX, field.getY() + field.getHeight() + this.font.lineHeight, 0xFFFF0000);
        }
    }

    private void drawList(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int midX = this.guiLeft + this.xSize / 2;
        int x = this.guiLeft;
        int y = this.scrollPaneLeading.paneY0;
        graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.leading"), midX, y, 0xFFFFFFFF);
        y += this.font.lineHeight + 10;
        List<View> sortedLeading = sortFellowshipsForDisplay(this.allFellowshipsLeading);
        int[] leadingMinMax = this.scrollPaneLeading.getMinMaxIndices(sortedLeading, this.displayedFellowshipsLeading);
        for (int index = leadingMinMax[0]; index <= leadingMinMax[1]; ++index) {
            drawFellowshipEntry(graphics, sortedLeading.get(index), x, y, mouseX, mouseY, false);
            y += this.font.lineHeight + 5;
        }
        y = this.scrollPaneOther.paneY0;
        graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.member"), midX, y, 0xFFFFFFFF);
        y += this.font.lineHeight + 10;
        List<View> sortedOther = sortFellowshipsForDisplay(this.allFellowshipsOther);
        int[] otherMinMax = this.scrollPaneOther.getMinMaxIndices(sortedOther, this.displayedFellowshipsOther);
        for (int index = otherMinMax[0]; index <= otherMinMax[1]; ++index) {
            drawFellowshipEntry(graphics, sortedOther.get(index), x, y, mouseX, mouseY, false);
            y += this.font.lineHeight + 5;
        }
        String invites = String.valueOf(LOTRClientFellowships.getInvites().size());
        graphics.text(this.font, invites, this.buttonInvites.getX() - 2 - this.font.width(invites),
                this.buttonInvites.getY() + this.buttonInvites.getHeight() / 2 - this.font.lineHeight / 2, 0xFFFFFFFF, false);
        if (this.buttonInvites.isHovered()) {
            renderIconTooltip(graphics, mouseX, mouseY, Component.translatable("lotr.gui.fellowships.invitesTooltip"));
        }
        if (this.buttonCreate.isHovered()) {
            if (!LOTRConfig.isFellowshipCreationEnabled(this.minecraft.level)) {
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.creationDisabled"), midX,
                        this.buttonCreate.getY() + this.buttonCreate.getHeight() + 4, 0xFFFFFFFF);
            } else if (!LOTRClientFellowships.canCreateFellowships()) {
                graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.createLimit"), midX,
                        this.buttonCreate.getY() + this.buttonCreate.getHeight() + 4, 0xFFFFFFFF);
            }
        }
        if (this.scrollPaneLeading.hasScrollBar) {
            this.scrollPaneLeading.drawScrollBar(graphics);
        }
        if (this.scrollPaneOther.hasScrollBar) {
            this.scrollPaneOther.drawScrollBar(graphics);
        }
    }

    private void drawFellowship(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        View fs = this.viewingFellowship;
        int midX = this.guiLeft + this.xSize / 2;
        int x = this.guiLeft;
        int y = this.guiTop + 10;
        graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.nameAndPlayers", fs.name(),
                fs.getPlayerCount()), midX, y, 0xFFFFFFFF);
        y += this.font.lineHeight + 5;
        if (!fs.icon().isEmpty()) {
            drawFellowshipIcon(graphics, fs, midX - 8, y, 1.0f);
        }
        int iconPVPX = this.guiLeft + this.xSize - 36;
        int iconHFFX = this.guiLeft + this.xSize - 16;
        int iconMapX = this.guiLeft + this.xSize - 56;
        int iconSize = 16;
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconPVPX, y, 64.0f, fs.preventPVP() ? 80 : 48, iconSize, iconSize, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconHFFX, y, 80.0f, fs.preventHiredFF() ? 80 : 48, iconSize, iconSize, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconMapX, y, 96.0f, fs.showMapLocations() ? 48 : 80, iconSize, iconSize, 256, 256);
        if (mouseX >= iconPVPX && mouseX < iconPVPX + iconSize && mouseY >= y && mouseY < y + iconSize) {
            renderIconTooltip(graphics, mouseX, mouseY, Component.translatable(fs.preventPVP()
                    ? "lotr.gui.fellowships.pvp.prevent" : "lotr.gui.fellowships.pvp.allow"));
        }
        if (mouseX >= iconHFFX && mouseX < iconHFFX + iconSize && mouseY >= y && mouseY < y + iconSize) {
            renderIconTooltip(graphics, mouseX, mouseY, Component.translatable(fs.preventHiredFF()
                    ? "lotr.gui.fellowships.hiredFF.prevent" : "lotr.gui.fellowships.hiredFF.allow"));
        }
        if (mouseX >= iconMapX && mouseX < iconMapX + iconSize && mouseY >= y && mouseY < y + iconSize) {
            renderIconTooltip(graphics, mouseX, mouseY, Component.translatable(fs.showMapLocations()
                    ? "lotr.gui.fellowships.mapShow.on" : "lotr.gui.fellowships.mapShow.off"));
        }
        y += iconSize + 10;
        int titleOffset = 0;
        for (UUID player : fs.getAllPlayerUuids()) {
            LOTRTitle.PlayerTitle title = fs.getTitleFor(player);
            if (title != null) {
                titleOffset = Math.max(titleOffset, this.font.width(title.getFullTitleComponent(this.minecraft.player)));
            }
        }
        drawPlayerEntry(graphics, fs.owner(), x, y, titleOffset, mouseX, mouseY);
        y += this.font.lineHeight + 10;
        List<UUID> membersSorted = sortMembersForDisplay(fs);
        int[] membersMinMax = this.scrollPaneMembers.getMinMaxIndices(membersSorted, this.displayedMembers);
        for (int index = membersMinMax[0]; index <= membersMinMax[1]; ++index) {
            drawPlayerEntry(graphics, membersSorted.get(index), x, y, titleOffset, mouseX, mouseY);
            y += this.font.lineHeight + 5;
        }
        for (OptionButton button : this.orderedFsOptionButtons) {
            if (button.visible && button.isHovered()) {
                graphics.centeredText(this.font, button.getMessage(), midX, button.getY() + button.getHeight() + 4, 0xFFFFFFFF);
            }
        }
        if (this.scrollPaneMembers.hasScrollBar) {
            this.scrollPaneMembers.drawScrollBar(graphics);
        }
    }

    private void drawInvitations(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int midX = this.guiLeft + this.xSize / 2;
        int y = this.guiTop + 10;
        graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.invites"), midX, y, 0xFFFFFFFF);
        y += this.font.lineHeight + 10;
        if (this.allFellowshipInvites.isEmpty()) {
            y += this.font.lineHeight;
            graphics.centeredText(this.font, Component.translatable("lotr.gui.fellowships.invitesNone"), midX, y, 0xFFFFFFFF);
        } else {
            int[] minMax = this.scrollPaneInvites.getMinMaxIndices(this.allFellowshipInvites, this.displayedInvites);
            for (int index = minMax[0]; index <= minMax[1]; ++index) {
                drawFellowshipEntry(graphics, this.allFellowshipInvites.get(index), this.guiLeft, y, mouseX, mouseY, true);
                y += this.font.lineHeight + 5;
            }
        }
        if (this.scrollPaneInvites.hasScrollBar) {
            this.scrollPaneInvites.drawScrollBar(graphics);
        }
    }

    private void drawAcceptResult(GuiGraphicsExtractor graphics) {
        int y = this.guiTop + 30;
        if (this.acceptInviteResult == null) {
            graphics.centeredText(this.font, ".".repeat(Math.floorMod(this.tickCounter / 10, 3)), this.guiLeft + this.xSize / 2, y, 0xFFFFFFFF);
            return;
        }
        Component text = switch (this.acceptInviteResult) {
            case DISBANDED -> Component.translatable("lotr.gui.fellowships.invited.disbanded", this.acceptInviteResultFellowshipName);
            case TOO_LARGE -> Component.translatable("lotr.gui.fellowships.invited.maxSize", this.acceptInviteResultFellowshipName,
                    LOTRConfig.getFellowshipMaxSize(this.minecraft.level));
            case NONEXISTENT -> Component.translatable("lotr.gui.fellowships.invited.notFound");
            default -> Component.empty();
        };
        drawLines(graphics, text, y);
    }

    private void drawFellowshipEntry(GuiGraphicsExtractor graphics, View fs, int x, int y, int mouseX, int mouseY, boolean isInvite) {
        int selectX0 = x - 2;
        int selectX1 = x + this.xSize + 2;
        int selectY0 = y - 2;
        int selectY1 = y + this.font.lineHeight + 2;
        if (mouseX >= selectX0 && mouseX <= selectX1 && mouseY >= selectY0 && mouseY <= selectY1) {
            graphics.fill(selectX0, selectY0, selectX1, selectY1, 1442840575);
            this.mouseOverFellowship = fs;
        }
        boolean isMouseOver = this.mouseOverFellowship == fs;
        drawFellowshipIcon(graphics, fs, x, y, 0.5f);
        String fsName = fs.name();
        int maxLength = 110;
        if (this.font.width(fsName) > maxLength) {
            while (this.font.width(fsName + "...") > maxLength) {
                fsName = fsName.substring(0, fsName.length() - 1);
            }
            fsName = fsName + "...";
        }
        String ownerName = fs.getUsernameFor(fs.owner());
        boolean ownerOnline = isPlayerOnline(fs.owner(), ownerName);
        graphics.text(this.font, fsName, x + 15, y, 0xFFFFFFFF, false);
        graphics.text(this.font, ownerName, x + 130, y, 0xFF000000 | (ownerOnline ? 16777215 : isMouseOver ? 12303291 : 7829367), false);
        if (isInvite) {
            int iconWidth = 8;
            int iconAcceptX = x + this.xSize - 18;
            int iconRejectX = x + this.xSize - 8;
            boolean accept = false;
            boolean reject = false;
            if (isMouseOver) {
                accept = this.mouseOverInviteAccept = mouseX >= iconAcceptX && mouseX <= iconAcceptX + iconWidth
                        && mouseY >= y && mouseY <= y + iconWidth;
                reject = this.mouseOverInviteReject = mouseX >= iconRejectX && mouseX <= iconRejectX + iconWidth
                        && mouseY >= y && mouseY <= y + iconWidth;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconAcceptX, y, 16.0f, 16 + (accept ? 0 : iconWidth), iconWidth, iconWidth, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconRejectX, y, 8.0f, 16 + (reject ? 0 : iconWidth), iconWidth, iconWidth, 256, 256);
        } else {
            String memberCount = String.valueOf(fs.getPlayerCount());
            String onlineMemberCount = countOnlineMembers(fs) + " | ";
            graphics.text(this.font, memberCount, x + this.xSize - this.font.width(memberCount), y,
                    0xFF000000 | (isMouseOver ? 12303291 : 7829367), false);
            graphics.text(this.font, onlineMemberCount, x + this.xSize - this.font.width(memberCount) - this.font.width(onlineMemberCount),
                    y, 0xFFFFFFFF, false);
        }
    }

    private void drawFellowshipIcon(GuiGraphicsExtractor graphics, View fs, int x, int y, float scale) {
        ItemStack icon = fs.icon();
        if (!icon.isEmpty()) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(scale, scale);
            graphics.item(icon, Math.round(x / scale), Math.round(y / scale));
            graphics.pose().popMatrix();
        }
    }

    private void drawPlayerEntry(GuiGraphicsExtractor graphics, UUID player, int x, int y, int titleOffset, int mouseX, int mouseY) {
        View fs = this.viewingFellowship;
        String username = fs.getUsernameFor(player);
        int selectX0 = x - 2;
        int selectX1 = x + this.xSize + 2;
        int selectY0 = y - 2;
        int selectY1 = y + this.font.lineHeight + 2;
        if (mouseX >= selectX0 && mouseX <= selectX1 && mouseY >= selectY0 && mouseY <= selectY1) {
            graphics.fill(selectX0, selectY0, selectX1, selectY1, 1442840575);
            this.mouseOverPlayer = player;
        }
        boolean isMouseOver = player.equals(this.mouseOverPlayer);
        LOTRTitle.PlayerTitle title = fs.getTitleFor(player);
        if (title != null) {
            graphics.text(this.font, title.getFullTitleComponent(this.minecraft.player), x, y, 0xFFFFFFFF, false);
        }
        graphics.text(this.font, username, x + titleOffset, y,
                0xFF000000 | (isPlayerOnline(player, username) ? 16777215 : isMouseOver ? 12303291 : 7829367), false);
        boolean isOwner = fs.owner().equals(player);
        boolean isAdmin = fs.isAdmin(player);
        int markX = x + titleOffset + this.font.width(username + " ");
        if (isOwner) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, markX, y, 0.0f, 0.0f, 8, 8, 256, 256);
        } else if (isAdmin) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, markX, y, 8.0f, 0.0f, 8, 8, 256, 256);
        }
        boolean owned = fs.owned();
        boolean adminned = fs.adminned();
        if (!isOwner && (owned || adminned)) {
            int iconWidth = 8;
            int iconRemoveX = x + this.xSize - 28;
            int iconOpDeopX = x + this.xSize - 18;
            int iconTransferX = x + this.xSize - 8;
            if (adminned) {
                iconRemoveX = x + this.xSize - 8;
            }
            boolean remove = false;
            boolean opDeop = false;
            boolean transfer = false;
            if (isMouseOver) {
                remove = this.mouseOverPlayerRemove = within(mouseX, mouseY, iconRemoveX, y, iconWidth);
                if (owned) {
                    if (isAdmin) {
                        opDeop = this.mouseOverPlayerDeop = within(mouseX, mouseY, iconOpDeopX, y, iconWidth);
                    } else {
                        opDeop = this.mouseOverPlayerOp = within(mouseX, mouseY, iconOpDeopX, y, iconWidth);
                    }
                    transfer = this.mouseOverPlayerTransfer = within(mouseX, mouseY, iconTransferX, y, iconWidth);
                }
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconRemoveX, y, 8.0f, 16 + (remove ? 0 : iconWidth), iconWidth, iconWidth, 256, 256);
            if (owned) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconOpDeopX, y, isAdmin ? 32.0f : 24.0f,
                        16 + (opDeop ? 0 : iconWidth), iconWidth, iconWidth, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, iconTransferX, y, 0.0f, 16 + (transfer ? 0 : iconWidth),
                        iconWidth, iconWidth, 256, 256);
            }
        }
    }

    private static boolean within(int mouseX, int mouseY, int x, int y, int size) {
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }

    private void renderIconTooltip(GuiGraphicsExtractor graphics, int x, int y, Component text) {
        graphics.setTooltipForNextFrame(this.font, this.font.split(text, 200), x, y);
    }

    private void setupScrollBars(int mouseX, int mouseY) {
        boolean isMouseDown = this.minecraft.mouseHandler.isLeftPressed();
        if (this.page == Page.LIST) {
            this.displayedFellowshipsLeading = this.allFellowshipsLeading.size();
            this.displayedFellowshipsOther = this.allFellowshipsOther.size();
            this.scrollPaneLeading.hasScrollBar = false;
            this.scrollPaneOther.hasScrollBar = false;
            while (this.displayedFellowshipsLeading + this.displayedFellowshipsOther > 12) {
                if (this.displayedFellowshipsOther >= this.displayedFellowshipsLeading) {
                    --this.displayedFellowshipsOther;
                    this.scrollPaneOther.hasScrollBar = true;
                } else {
                    --this.displayedFellowshipsLeading;
                    this.scrollPaneLeading.hasScrollBar = true;
                }
            }
            this.scrollPaneLeading.paneX0 = this.guiLeft;
            this.scrollPaneLeading.scrollBarX0 = this.guiLeft + this.scrollBarX;
            this.scrollPaneLeading.paneY0 = this.guiTop + 10;
            this.scrollPaneLeading.paneY1 = this.scrollPaneLeading.paneY0 + this.font.lineHeight + 10
                    + (this.font.lineHeight + 5) * this.displayedFellowshipsLeading;
            this.scrollPaneLeading.mouseDragScroll(mouseX, mouseY, isMouseDown);
            this.scrollPaneOther.paneX0 = this.guiLeft;
            this.scrollPaneOther.scrollBarX0 = this.guiLeft + this.scrollBarX;
            this.scrollPaneOther.paneY0 = this.scrollPaneLeading.paneY1 + 5;
            this.scrollPaneOther.paneY1 = this.scrollPaneOther.paneY0 + this.font.lineHeight + 10
                    + (this.font.lineHeight + 5) * this.displayedFellowshipsOther;
            this.scrollPaneOther.mouseDragScroll(mouseX, mouseY, isMouseDown);
        }
        if (this.page == Page.FELLOWSHIP && this.viewingFellowship != null) {
            this.displayedMembers = this.viewingFellowship.members().size();
            this.scrollPaneMembers.hasScrollBar = false;
            if (this.displayedMembers > 11) {
                this.displayedMembers = 11;
                this.scrollPaneMembers.hasScrollBar = true;
            }
            this.scrollPaneMembers.paneX0 = this.guiLeft;
            this.scrollPaneMembers.scrollBarX0 = this.guiLeft + this.scrollBarX;
            this.scrollPaneMembers.paneY0 = this.guiTop + 10 + this.font.lineHeight + 5 + 16 + 10 + this.font.lineHeight + 10;
            this.scrollPaneMembers.paneY1 = this.scrollPaneMembers.paneY0 + (this.font.lineHeight + 5) * this.displayedMembers;
        } else {
            this.scrollPaneMembers.hasScrollBar = false;
        }
        this.scrollPaneMembers.mouseDragScroll(mouseX, mouseY, isMouseDown);
        if (this.page == Page.INVITATIONS) {
            this.displayedInvites = this.allFellowshipInvites.size();
            this.scrollPaneInvites.hasScrollBar = false;
            if (this.displayedInvites > 15) {
                this.displayedInvites = 15;
                this.scrollPaneInvites.hasScrollBar = true;
            }
            this.scrollPaneInvites.paneX0 = this.guiLeft;
            this.scrollPaneInvites.scrollBarX0 = this.guiLeft + this.scrollBarX;
            this.scrollPaneInvites.paneY0 = this.guiTop + 10 + this.font.lineHeight + 10;
            this.scrollPaneInvites.paneY1 = this.scrollPaneInvites.paneY0 + (this.font.lineHeight + 5) * this.displayedInvites;
            this.scrollPaneInvites.mouseDragScroll(mouseX, mouseY, isMouseDown);
        }
    }

    /** sortFellowshipsForDisplay: the largest first, then by name. */
    private static List<View> sortFellowshipsForDisplay(List<View> list) {
        List<View> sorted = new ArrayList<>(list);
        sorted.sort((fs1, fs2) -> {
            int count1 = fs1.getPlayerCount();
            int count2 = fs2.getPlayerCount();
            if (count1 == count2) {
                return fs1.name().toLowerCase(Locale.ROOT).compareTo(fs2.name().toLowerCase(Locale.ROOT));
            }
            return -Integer.compare(count1, count2);
        });
        return sorted;
    }

    /** sortMembersForDisplay: as the original's comparator ordered them, then by name. */
    private List<UUID> sortMembersForDisplay(View fs) {
        List<UUID> members = new ArrayList<>(fs.members());
        members.sort(Comparator.<UUID, Boolean>comparing(p -> isPlayerOnline(p, fs.getUsernameFor(p))).reversed()
                .thenComparing(fs::isAdmin).reversed()
                .thenComparing(p -> fs.getUsernameFor(p).toLowerCase(Locale.ROOT)));
        return members;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int k = (int) Math.signum(scrollY);
        if (k != 0) {
            if (this.page == Page.LIST) {
                if (this.scrollPaneLeading.hasScrollBar && this.scrollPaneLeading.mouseOver) {
                    this.scrollPaneLeading.mouseWheelScroll(k, this.allFellowshipsLeading.size() - this.displayedFellowshipsLeading);
                }
                if (this.scrollPaneOther.hasScrollBar && this.scrollPaneOther.mouseOver) {
                    this.scrollPaneOther.mouseWheelScroll(k, this.allFellowshipsOther.size() - this.displayedFellowshipsOther);
                }
            }
            if (this.page == Page.FELLOWSHIP && this.scrollPaneMembers.hasScrollBar && this.scrollPaneMembers.mouseOver) {
                this.scrollPaneMembers.mouseWheelScroll(k, this.viewingFellowship.members().size() - this.displayedMembers);
            }
            if (this.page == Page.INVITATIONS && this.scrollPaneInvites.hasScrollBar && this.scrollPaneInvites.mouseOver) {
                this.scrollPaneInvites.mouseWheelScroll(k, this.allFellowshipInvites.size() - this.displayedInvites);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean handled = super.mouseClicked(event, doubleClick);
        if (event.button() != 0) {
            return handled;
        }
        if (this.page == Page.LIST && this.mouseOverFellowship != null) {
            this.buttonBack.playDownSound(this.minecraft.getSoundManager());
            this.page = Page.FELLOWSHIP;
            this.viewingFellowship = this.mouseOverFellowship;
            return true;
        }
        if (this.page == Page.FELLOWSHIP && this.mouseOverPlayer != null) {
            Page next = this.mouseOverPlayerRemove ? Page.REMOVE : this.mouseOverPlayerOp ? Page.OP
                    : this.mouseOverPlayerDeop ? Page.DEOP : this.mouseOverPlayerTransfer ? Page.TRANSFER : null;
            if (next != null) {
                this.buttonBack.playDownSound(this.minecraft.getSoundManager());
                switch (next) {
                    case REMOVE -> this.removingPlayer = this.mouseOverPlayer;
                    case OP -> this.oppingPlayer = this.mouseOverPlayer;
                    case DEOP -> this.deoppingPlayer = this.mouseOverPlayer;
                    default -> this.transferringPlayer = this.mouseOverPlayer;
                }
                this.page = next;
                return true;
            }
        }
        if (this.page == Page.INVITATIONS && this.mouseOverFellowship != null) {
            if (this.mouseOverInviteAccept) {
                this.buttonBack.playDownSound(this.minecraft.getSoundManager());
                send(new LOTRFellowshipPayloads.RespondInvite(this.mouseOverFellowship.fellowshipID(), true));
                this.mouseOverFellowship = null;
                this.page = Page.ACCEPT_INVITE_RESULT;
                return true;
            }
            if (this.mouseOverInviteReject) {
                this.buttonBack.playDownSound(this.minecraft.getSoundManager());
                send(new LOTRFellowshipPayloads.RespondInvite(this.mouseOverFellowship.fellowshipID(), false));
                this.mouseOverFellowship = null;
                return true;
            }
        }
        return handled;
    }

    /** keyTyped: Escape or the inventory key goes back a page below the list; text fields keep their keys. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.page != Page.LIST && (event.key() == GLFW.GLFW_KEY_ESCAPE
                || this.minecraft.options.keyInventory.matches(event) && !(getFocused() instanceof EditBox))) {
            goBack();
            return true;
        }
        if (getFocused() instanceof EditBox box && box.visible && box.keyPressed(event)) {
            return true;
        }
        return super.keyPressed(event);
    }

    /** LOTRGuiButtonFsOption: a 16-pixel icon off the fellowships sheet, its name shown under it when hovered. */
    private static final class OptionButton extends AbstractButton {

        private int iconU;
        private int iconV;
        private final Runnable onPress;

        OptionButton(int x, int y, int u, int v, Component label, Runnable onPress) {
            super(x, y, 16, 16, label);
            this.iconU = u;
            this.iconV = v;
            this.onPress = onPress;
        }

        void setIconUV(int u, int v) {
            this.iconU = u;
            this.iconV = v;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.onPress.run();
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, getX(), getY(), this.iconU,
                    this.iconV + (isHovered() ? this.height : 0), this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    /** LOTRGuiButtonFsInvites: the invitations icon. */
    private static final class InvitesButton extends AbstractButton {

        private final Runnable onPress;

        InvitesButton(int x, int y, Runnable onPress) {
            super(x, y, 16, 16, Component.translatable("lotr.gui.fellowships.invitesTooltip"));
            this.onPress = onPress;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.onPress.run();
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, getX(), getY(), 80.0f, isHovered() ? this.height : 0,
                    this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
