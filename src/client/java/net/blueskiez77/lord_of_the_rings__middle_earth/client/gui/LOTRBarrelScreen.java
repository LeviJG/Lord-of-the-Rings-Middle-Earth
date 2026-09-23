package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRMugRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRBarrelMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBrewingButtonPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiBarrel: 210x221, with the brewing button at (25, 97) and a 48x96 tank
 * on the right at (148, 34).
 *
 * <p>The tank fills with the brew's own liquid colour -- the middle pixel of its
 * liquid texture stretched out, darkening towards the bottom -- to how full the
 * barrel is, or while brewing, to how far through the current strength it is.
 * While brewing, bubbles rise through it on a 32-tick loop.
 */
public class LOTRBarrelScreen extends AbstractContainerScreen<LOTRBarrelMenu> {
    private static final Identifier GUI = Identifier.fromNamespaceAndPath("lotr", "gui/barrel.png");
    private static final Identifier BREWING = Identifier.fromNamespaceAndPath("lotr", "gui/barrel_brewing.png");
    private static final int LABEL_COLOUR = 0xFF404040;
    /** fullColor, 2167561, opaque. */
    private static final int FULL_COLOUR = 0xFF000000 | 2167561;

    private Button brewingButton;
    private int brewingAnim;
    private int brewingAnimPrev;

    public LOTRBarrelScreen(LOTRBarrelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 210, 221);
        this.inventoryLabelX = 25;
        this.inventoryLabelY = 127;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
        brewingButton = addRenderableWidget(Button.builder(
                        Component.translatable("container.lotr.barrel.startBrewing"),
                        button -> ClientPlayNetworking.send(LOTRBrewingButtonPayload.INSTANCE))
                .bounds(leftPos + 25, topPos + 97, 100, 20)
                .build());
        updateButton();
    }

    /** drawScreen's button update. */
    private void updateButton() {
        int mode = menu.barrelMode();
        ItemStack brew = menu.brewingItem();
        if (mode == LOTRBarrelBlockEntity.BREWING) {
            brewingButton.active = !brew.isEmpty() && brew.getOrDefault(LOTRDataComponents.DRINK_STRENGTH, 0) > 0;
            brewingButton.setMessage(Component.translatable("container.lotr.barrel.stopBrewing"));
        } else {
            brewingButton.active = mode == LOTRBarrelBlockEntity.EMPTY && !brew.isEmpty();
            brewingButton.setMessage(Component.translatable("container.lotr.barrel.startBrewing"));
        }
    }

    /** The client half of the tile entity's updateEntity: the bubble loop. */
    @Override
    protected void containerTick() {
        super.containerTick();
        brewingAnimPrev = brewingAnim++;
        if (menu.barrelMode() != LOTRBarrelBlockEntity.BREWING || brewingAnim >= LOTRBarrelBlockEntity.BREW_ANIM_TIME) {
            brewingAnimPrev = brewingAnim = 0;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        updateButton();
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = leftPos;
        int y = topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
        int mode = menu.barrelMode();
        if (mode != LOTRBarrelBlockEntity.BREWING && mode != LOTRBarrelBlockEntity.FULL) {
            return;
        }
        ItemStack brew = menu.brewingItem();
        int fullAmount = mode == LOTRBarrelBlockEntity.BREWING
                ? menu.brewingTime() * 96 / LOTRBarrelBlockEntity.BREW_TIME
                : brew.getCount() * 96 / LOTRBrewingRecipes.BARREL_CAPACITY;
        int x0 = x + 148;
        int x1 = x + 196;
        int y0 = y + 34;
        int y1 = y + 130;
        int yFull = y1 - fullAmount;
        if (brew.getItem() instanceof LOTRDrinkItem && fullAmount > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMugRenderer.liquidTexture(brew),
                    x0, yFull, 7.0F, 7.0F, x1 - x0, fullAmount, 1, 1, 16, 16);
            graphics.fillGradient(x0, yFull, x1, y1, 0, FULL_COLOUR);
        }
        if (mode == LOTRBarrelBlockEntity.BREWING) {
            float anim = Mth.lerp(partialTick, brewingAnimPrev, brewingAnim);
            int yAnim = Math.round(y1 - anim * 97.0f / LOTRBarrelBlockEntity.BREW_ANIM_TIME);
            if (yAnim < y1) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, BREWING, x0, yAnim, 51.0F, 0.0F, x1 - x0, y1 - yAnim,
                        256, 256, ARGB.white(anim / LOTRBarrelBlockEntity.BREW_ANIM_TIME));
            }
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, BREWING, x0, y0, 1.0F, 0.0F, x1 - x0, y1 - y0, 256, 256);
    }

    /** drawGuiContainerForegroundLayer: the name, the subtitle under it, and "Inventory". */
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        Component subtitle = LOTRBarrelBlockEntity.subtitle(menu.barrelMode(), menu.brewingItem());
        graphics.text(font, subtitle, imageWidth / 2 - font.width(subtitle) / 2, 17, LABEL_COLOUR, false);
    }
}
