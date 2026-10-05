package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBrandingIronItem;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.StringUtils;

/**
 * LOTRGuiBrandingIron: naming an unnamed branding iron. The name is sent as
 * the screen closes, if there is one; it closes by itself if the iron leaves
 * the player's hand.
 */
public class LOTRBrandingIronScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/branding_iron.png");
    private static final int X_SIZE = 200;
    private static final int Y_SIZE = 132;
    private static final int TEXT_COLOUR = 0xFF404040;

    private int guiLeft;
    private int guiTop;
    private EditBox brandNameField;
    private Button buttonDone;
    private ItemStack theItem = ItemStack.EMPTY;

    public LOTRBrandingIronScreen() {
        super(Component.translatable("lotr.gui.brandingIron.title"));
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        this.brandNameField = addRenderableWidget(new EditBox(this.font, this.guiLeft + X_SIZE / 2 - 80,
                this.guiTop + 50, 160, 20, Component.translatable("lotr.gui.brandingIron.naming")));
        this.brandNameField.setMaxLength(LOTRBrandingIronItem.MAX_NAME_LENGTH);
        setInitialFocus(this.brandNameField);
        this.buttonDone = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.brandingIron.done"),
                button -> onClose()).bounds(this.guiLeft + X_SIZE / 2 - 40, this.guiTop + 97, 80, 20).build());
        updateItem();
    }

    /** The iron in hand, or the screen closes. */
    private boolean updateItem() {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack stack = minecraft.player == null ? ItemStack.EMPTY : minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof LOTRBrandingIronItem)) {
            minecraft.setScreenAndShow(null);
            return false;
        }
        this.theItem = stack;
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        updateItem();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F, X_SIZE, Y_SIZE, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.buttonDone.active = !StringUtils.isBlank(this.brandNameField.getValue());
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.text(this.font, this.title, this.guiLeft + X_SIZE / 2 - this.font.width(this.title) / 2,
                this.guiTop + 11, TEXT_COLOUR, false);
        Component naming = Component.translatable("lotr.gui.brandingIron.naming");
        graphics.text(this.font, naming, this.brandNameField.getX(),
                this.brandNameField.getY() - this.font.lineHeight - 3, TEXT_COLOUR, false);
        Component hint = Component.translatable("lotr.gui.brandingIron.unnameHint");
        graphics.text(this.font, hint, this.brandNameField.getX(),
                this.brandNameField.getY() + this.brandNameField.getHeight() + 3, TEXT_COLOUR, false);
        if (!this.theItem.isEmpty()) {
            graphics.item(this.theItem, this.guiLeft + 8, this.guiTop + 8);
        }
    }

    /** onGuiClosed: the name goes to the server. */
    @Override
    public void removed() {
        super.removed();
        String name = this.brandNameField.getValue();
        if (!StringUtils.isBlank(name)) {
            ClientPlayNetworking.send(new LOTRBrandingIronItem.NamePayload(name));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
