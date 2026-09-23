package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRDaleCrackerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/** LOTRGuiDaleCracker: the gift slots, and a Seal button that wakes once something is in them. */
public class LOTRDaleCrackerScreen extends AbstractContainerScreen<LOTRDaleCrackerMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/dale_cracker.png");

    private Button sealButton;

    public LOTRDaleCrackerScreen(LOTRDaleCrackerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
        sealButton = addRenderableWidget(Button.builder(
                        Component.translatable("lotr.gui.daleCracker.seal"),
                        button -> {
                            if (!this.menu.isContentsEmpty()) {
                                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
                                        LOTRDaleCrackerMenu.SEAL_BUTTON);
                                this.onClose();
                            }
                        })
                .bounds(this.leftPos + this.imageWidth / 2 - 40, this.topPos + 48, 80, 20)
                .build());
        sealButton.active = false;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        sealButton.active = !this.menu.isContentsEmpty();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
