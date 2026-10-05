package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHireableBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitPledgeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRUnitTradeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTradePayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiHireBase (LOTRGuiUnitTrade, LOTRGuiMercenaryHire): one unit at a
 * time, turned with the arrows -- the unit itself, on its mount if it has
 * one, turning to follow the mouse; its name, price, the alignment it asks
 * and any pledge (the pledge's terms on hovering); a line of extra
 * information for some; a box naming the company to put it in; and Hire.
 */
public class LOTRUnitTradeScreen extends AbstractContainerScreen<LOTRUnitTradeMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/unit_trade.png");
    private static final Identifier BUTTONS = Identifier.fromNamespaceAndPath("lotr", "gui/npc/unit_trade_buttons.png");
    /** LOTRClientProxy.alignmentTexture. */
    private static final Identifier ALIGNMENT = Identifier.fromNamespaceAndPath("lotr", "gui/alignment.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    private static final int REQ_X = 64;
    private static final int REQ_X_TEXT = REQ_X + 19;
    private static final int REQ_Y = 65;
    private static final int REQ_Y_TEXT_BELOW = 4;
    private static final int REQ_GAP = 18;

    private int currentTradeEntryIndex;
    private int displayedIndex = -1;
    private @Nullable LivingEntity currentDisplayedMob;
    private @Nullable LivingEntity currentDisplayedMount;

    private UnitButton buttonLeftUnit;
    private UnitButton buttonHire;
    private UnitButton buttonRightUnit;
    private EditBox squadronNameField;

    public LOTRUnitTradeScreen(LOTRUnitTradeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 220, 256);
    }

    private @Nullable LOTRUnitTradeEntry currentTrade() {
        return this.menu.trades.isEmpty() ? null : this.menu.trades.get(this.currentTradeEntryIndex);
    }

    @Override
    protected void init() {
        super.init();
        buttonLeftUnit = addRenderableWidget(new UnitButton(0, this.leftPos + 90, this.topPos + 144, 12));
        buttonHire = addRenderableWidget(new UnitButton(1, this.leftPos + 102, this.topPos + 144, 16));
        buttonRightUnit = addRenderableWidget(new UnitButton(2, this.leftPos + 118, this.topPos + 144, 12));
        squadronNameField = new EditBox(this.font, this.leftPos + this.imageWidth / 2 - 80, this.topPos + 120, 160, 20,
                Component.translatable("container.lotr.unitTrade.squadronBox"));
        squadronNameField.setMaxLength(LOTRCommandHornItem.SQUADRON_LENGTH_MAX);
        squadronNameField.setHint(Component.translatable("container.lotr.unitTrade.squadronBox"));
        addRenderableWidget(squadronNameField);
        updateButtons();
    }

    private void onButton(int id) {
        if (id == 0) {
            if (this.currentTradeEntryIndex > 0) {
                --this.currentTradeEntryIndex;
            }
        } else if (id == 1) {
            ClientPlayNetworking.send(new LOTRTradePayloads.BuyUnit(this.currentTradeEntryIndex,
                    this.squadronNameField.getValue()));
        } else if (id == 2 && this.currentTradeEntryIndex < this.menu.trades.size() - 1) {
            ++this.currentTradeEntryIndex;
        }
        updateButtons();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        LOTRUnitTradeEntry trade = currentTrade();
        LOTRHireableBase trader = this.menu.unitTrader();
        buttonLeftUnit.active = this.currentTradeEntryIndex > 0;
        buttonHire.active = trade != null && trader != null
                && trade.hasRequiredCostAndAlignment(this.minecraft.player, trader);
        buttonRightUnit.active = this.currentTradeEntryIndex < this.menu.trades.size() - 1;
    }

    /** keyTyped: the company box takes what it can before the screen does (the inventory key included). */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            return super.keyPressed(event);
        }
        if (squadronNameField.keyPressed(event) || squadronNameField.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        drawMobOnGui(graphics, mouseX, mouseY);
    }

    private void centred(GuiGraphicsExtractor graphics, Component s, int x, int y) {
        graphics.text(this.font, s, x - this.font.width(s) / 2, y, TEXT_COLOUR, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        LOTRUnitTradeEntry curTrade = currentTrade();
        LOTRHireableBase trader = this.menu.unitTrader();
        if (this.menu.theLivingTrader != null) {
            centred(graphics, Component.literal(this.menu.theLivingTrader.getNPCName()), 110, 11);
        }
        graphics.text(this.font, Component.translatable("container.inventory"), 30, 162, TEXT_COLOUR, false);
        if (curTrade == null || trader == null) {
            return;
        }
        centred(graphics, curTrade.getUnitTradeName(), 138, 50);
        int reqY = REQ_Y;
        graphics.item(new ItemStack(LOTRMiscItems.SILVER_COIN), REQ_X, reqY);
        int cost = curTrade.getCost(this.minecraft.player, trader);
        graphics.text(this.font, String.valueOf(cost), REQ_X_TEXT, reqY + REQ_Y_TEXT_BELOW, TEXT_COLOUR, false);
        reqY += REQ_GAP;
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, REQ_X, reqY, 0.0F, 36.0F, 16, 16, 256, 256);
        graphics.text(this.font, LOTRAlignmentValues.formatAlignForDisplay(curTrade.alignmentRequired),
                REQ_X_TEXT, reqY + REQ_Y_TEXT_BELOW, TEXT_COLOUR, false);
        if (curTrade.getPledgeType() != LOTRUnitPledgeType.NONE) {
            reqY += REQ_GAP;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, REQ_X, reqY, 0.0F, 212.0F, 16, 16, 256, 256);
            graphics.text(this.font, Component.translatable("container.lotr.unitTrade.pledge"),
                    REQ_X_TEXT, reqY + REQ_Y_TEXT_BELOW, TEXT_COLOUR, false);
        }
        if (curTrade.hasExtraInfo()) {
            boolean mouseover = isOverExtraInfo(mouseX, mouseY);
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 49, 106, 220.0F, 38 + (mouseover ? 7 : 0), 9, 7,
                    256, 256);
        }
    }

    private boolean isOverExtraInfo(int mouseX, int mouseY) {
        return mouseX >= this.leftPos + 49 && mouseX < this.leftPos + 49 + 9
                && mouseY >= this.topPos + 106 && mouseY < this.topPos + 106 + 7;
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        LOTRUnitTradeEntry curTrade = currentTrade();
        LOTRHireableBase trader = this.menu.unitTrader();
        if (curTrade == null || trader == null) {
            return;
        }
        if (curTrade.getPledgeType() != LOTRUnitPledgeType.NONE) {
            int i2 = mouseX - this.leftPos - REQ_X;
            int j2 = mouseY - this.topPos - (REQ_Y + 2 * REQ_GAP);
            if (i2 >= 0 && i2 < 16 && j2 >= 0 && j2 < 16) {
                graphics.setTooltipForNextFrame(this.font, Component.translatable(
                        "lotr.hiredNPC.commandReq.pledge." + curTrade.getPledgeType().name(),
                        trader.getFaction().factionName()), mouseX, mouseY);
            }
        }
        if (curTrade.hasExtraInfo() && isOverExtraInfo(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(this.font, this.font.split(curTrade.getFormattedExtraInfo(), 200),
                    mouseX, mouseY);
        }
    }

    // ------------------------------------------------------------ the figure

    /**
     * drawMobOnGui: the unit, and its mount, as the server made them for
     * show (a mercenary as himself), sized to fit, following the mouse.
     */
    private void drawMobOnGui(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.displayedIndex != this.currentTradeEntryIndex) {
            this.displayedIndex = this.currentTradeEntryIndex;
            this.currentDisplayedMob = createDisplay(false);
            this.currentDisplayedMount = createDisplay(true);
        }
        LivingEntity mob = this.currentDisplayedMob;
        if (mob == null) {
            return;
        }
        LivingEntity mount = this.currentDisplayedMount;
        float size = mob.getBbWidth() * mob.getBbHeight() * mob.getBbWidth();
        if (mount != null) {
            size += mount.getBbWidth() * mount.getBbHeight() * mount.getBbWidth() * 0.5f;
        }
        int scale = Math.round(Mth.sqrt(Mth.sqrt(1.0f / size)) * 30.0f);
        // The figure stood at (32, 109); each is drawn centred in this box.
        int x0 = this.leftPos + 7;
        int y0 = this.topPos + 29;
        int x1 = this.leftPos + 57;
        int y1 = this.topPos + 109;
        if (mount != null) {
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x0, y0, x1, y1, scale, 0.0f,
                    mouseX, mouseY, mount);
            // The rider's centre where it sits: its feet on the mount's saddle.
            float saddle = (float) mob.getVehicleAttachmentPoint(mount).y;
            float yOffset = mount.getBbHeight() / 2.0f - saddle - mob.getBbHeight() / 2.0f;
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x0, y0, x1, y1, scale, yOffset,
                    mouseX, mouseY, mob);
        } else {
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x0, y0, x1, y1, scale, 0.0f,
                    mouseX, mouseY, mob);
        }
    }

    private @Nullable LivingEntity createDisplay(boolean mount) {
        LOTRUnitTradeMenu.OpeningData data = this.menu.displayData;
        LOTRUnitTradeEntry trade = currentTrade();
        if (data == null || trade == null) {
            return null;
        }
        int i = this.currentTradeEntryIndex;
        Optional<CompoundTag> tag = (mount ? data.mounts() : data.units()).get(i);
        if (tag.isEmpty()) {
            // A mercenary is shown as himself; he has no mount to show.
            return mount ? null : this.menu.theLivingTrader;
        }
        EntityType<?> type = mount ? EntityType.by(TagValueInput.create(ProblemReporter.DISCARDING,
                this.minecraft.level.registryAccess(), tag.get())).orElse(null) : trade.getEntityType();
        if (type == null) {
            return null;
        }
        if (!(type.create(this.minecraft.level, EntitySpawnReason.LOAD) instanceof LivingEntity entity)) {
            return null;
        }
        entity.load(TagValueInput.create(ProblemReporter.DISCARDING, this.minecraft.level.registryAccess(), tag.get()));
        return entity;
    }

    /** LOTRGuiUnitTradeButton: a strip of the button sheet per id -- plain, hovered, disabled. */
    private final class UnitButton extends AbstractButton {
        private final int id;

        UnitButton(int id, int x, int y, int width) {
            super(x, y, width, 19, Component.empty());
            this.id = id;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            onButton(this.id);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int u = !this.active ? this.width * 2 : isHovered() ? this.width : 0;
            graphics.blit(RenderPipelines.GUI_TEXTURED, BUTTONS, getX(), getY(), u, this.id * 19,
                    this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
