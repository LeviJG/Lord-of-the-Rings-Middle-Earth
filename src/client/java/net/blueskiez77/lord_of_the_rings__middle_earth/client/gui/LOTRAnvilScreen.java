package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRAnvilMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAnvilRenamePayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiAnvil: the item on the left, what it is combined with and the
 * material to pay with in the middle, the result on the right; a name box
 * along the top, which takes the item's colour; and, for anything that can
 * carry modifiers, a reforge button and an engrave-ownership button, with the
 * cost of whatever the mouse is over written underneath.
 */
public class LOTRAnvilScreen extends AbstractContainerScreen<LOTRAnvilMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "textures/gui/anvil.png");

    /** The cost line: green when it can be paid, red when it cannot. */
    private static final int COST_OK = 8453920;
    private static final int COST_SHORT = 16736352;

    private EditBox name;
    private SheetButton reforge;
    private SheetButton engrave;
    private ItemStack previousInput = ItemStack.EMPTY;
    private int seenReforges;
    private int reforgeFlash;

    public LOTRAnvilScreen(LOTRAnvilMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 198);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 60;
        this.titleLabelY = 6;
        this.inventoryLabelY = this.imageHeight + 100;
        reforge = addRenderableWidget(new SheetButton(this.leftPos + 25, this.topPos + 78, 176, 39,
                LOTRAnvilMenu.BUTTON_REFORGE));
        engrave = addRenderableWidget(new SheetButton(this.leftPos + 5, this.topPos + 78, 176, 59,
                LOTRAnvilMenu.BUTTON_ENGRAVE));
        name = new EditBox(this.font, this.leftPos + 62, this.topPos + 24, 103, 12,
                Component.translatable("container.repair"));
        name.setCanLoseFocus(false);
        name.setTextColor(-1);
        name.setTextColorUneditable(-1);
        name.setBordered(false);
        name.setMaxLength(LOTRAnvilRenamePayload.MAX_LENGTH);
        name.setResponder(this::onNameChanged);
        name.setValue("");
        name.setEditable(false);
        addRenderableWidget(name);
        seenReforges = this.menu.reforges();
    }

    @Override
    protected void setInitialFocus() {
        setInitialFocus(name);
    }

    /** renameItem: an unchanged name is sent as no rename at all. */
    private void onNameChanged(String typed) {
        ItemStack stack = this.menu.inputItem();
        if (stack.isEmpty()) {
            return;
        }
        String rename = typed;
        if (!stack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)
                && rename.equals(stack.getHoverName().getString())) {
            rename = "";
        }
        this.menu.setItemName(rename);
        ClientPlayNetworking.send(new LOTRAnvilRenamePayload(rename));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (reforgeFlash > 0) {
            --reforgeFlash;
        }
        if (this.menu.reforges() != seenReforges) {
            seenReforges = this.menu.reforges();
            reforgeFlash = LOTRAnvilMenu.REFORGE_FLASH;
        }
        ItemStack input = this.menu.inputItem();
        if (!ItemStack.isSameItemSameComponents(input, previousInput)) {
            previousInput = input.copy();
            name.setValue(input.isEmpty() ? "" : input.getHoverName().getString());
            name.setEditable(!input.isEmpty());
        }
        ChatFormatting colour = LOTRAnvilMenu.nameColourOf(this.menu.resultItem().isEmpty()
                ? input : this.menu.resultItem());
        name.setTextColor(colour != null ? 0xFF000000 | net.minecraft.network.chat.TextColor.fromLegacyFormat(colour).getValue() : -1);

        boolean reforgeable = !input.isEmpty() && LOTRModifiers.isReforgeable(input);
        reforge.visible = reforge.active = reforgeable && this.menu.reforgeCost() > 0;
        engrave.visible = engrave.active = reforgeable && this.menu.engraveOwnerCost() > 0
                && LOTRAnvilMenu.canEngraveNewOwner(input, this.minecraft.player);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            this.minecraft.player.closeContainer();
            return true;
        }
        if (name.keyPressed(event) || name.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        // The name box: lit with an item in, dim without.
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 59, y + 20, 0.0F,
                this.imageHeight + (this.menu.inputItem().isEmpty() ? 16 : 0), 110, 16, 256, 256);
        // The cross over the arrow when there is something in but nothing out.
        if (this.menu.resultItem().isEmpty()) {
            boolean any = false;
            for (int i = 0; i < LOTRAnvilMenu.RESULT; i++) {
                any |= this.menu.getSlot(i).hasItem();
            }
            if (any) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 99, y + 56,
                        this.imageWidth, 0.0F, 28, 21, 256, 256);
            }
        }
        if (reforge.visible && engrave.visible) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 5, y + 78, 176.0F, 99.0F, 40, 20, 256, 256);
        } else if (reforge.visible) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 25, y + 78, 176.0F, 79.0F, 20, 20, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);

        Component cost = null;
        boolean affordable = true;
        if (!this.menu.inputItem().isEmpty()) {
            if (reforge.visible && reforge.isHovered()) {
                cost = Component.translatable("container.lotr.anvil.reforgeCost", this.menu.reforgeCost());
                affordable = this.menu.hasMaterialAmount(this.menu.reforgeCost());
            } else if (engrave.visible && engrave.isHovered()) {
                cost = Component.translatable("container.lotr.anvil.engraveOwnerCost", this.menu.engraveOwnerCost());
                affordable = this.menu.hasMaterialAmount(this.menu.engraveOwnerCost());
            } else if (this.menu.materialCost() > 0 && !this.menu.resultItem().isEmpty()) {
                cost = Component.translatable("container.lotr.anvil.cost", this.menu.materialCost());
                affordable = this.menu.getSlot(LOTRAnvilMenu.RESULT).mayPickup(this.minecraft.player);
            }
        }
        if (cost != null) {
            int colour = affordable ? COST_OK : COST_SHORT;
            int shadow = 0xFF000000 | (colour & 0xFCFCFC) >> 2;
            int cx = this.imageWidth - 8 - this.font.width(cost);
            int cy = 94;
            graphics.text(this.font, cost, cx, cy + 1, shadow, false);
            graphics.text(this.font, cost, cx + 1, cy, shadow, false);
            graphics.text(this.font, cost, cx + 1, cy + 1, shadow, false);
            graphics.text(this.font, cost, cx, cy, 0xFF000000 | colour, false);
        }

        // The white flash over the item as it is reforged.
        if (reforgeFlash > 0) {
            int alpha = Mth.clamp((int) (reforgeFlash / (float) LOTRAnvilMenu.REFORGE_FLASH * 255.0f), 0, 255);
            Slot slot = this.menu.getSlot(LOTRAnvilMenu.INPUT);
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, alpha << 24 | 0xFFFFFF);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        if (reforge.visible && reforge.isHovered()) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("container.lotr.anvil.reforge"),
                    mouseX, mouseY);
        } else if (engrave.visible && engrave.isHovered()) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("container.lotr.anvil.engraveOwner"),
                    mouseX, mouseY);
        }
    }

    /** LOTRGuiButtonReforge: a 20x20 icon off the anvil sheet, the hovered frame beside it. */
    private final class SheetButton extends AbstractButton {
        private final int u;
        private final int v;
        private final int buttonId;

        SheetButton(int x, int y, int u, int v, int buttonId) {
            super(x, y, 20, 20, Component.empty());
            this.u = u;
            this.v = v;
            this.buttonId = buttonId;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            LOTRAnvilScreen.this.minecraft.gameMode.handleInventoryButtonClick(
                    LOTRAnvilScreen.this.menu.containerId, buttonId);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(),
                    this.u + (isHovered() ? 20 : 0), this.v, 20, 20, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
