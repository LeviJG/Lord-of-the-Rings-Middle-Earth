package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRPouchNetworking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiButtonRestockPouch and LOTRGuiHandler.addPouchRestockButton: on any container screen that
 * shows the player's inventory (the pouch screens aside), a small button just above its top right
 * corner -- top left on the anvils', beside it on the barrel's -- that moves into the player's
 * pouches whatever they already hold the like of. It shows only while the player carries a pouch,
 * and in the creative screen only on its inventory tab.
 */
public class LOTRPouchRestockButton extends AbstractButton {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/widgets.png");

    private enum Position { TOP_RIGHT, LEFT, SIDE }

    private final AbstractContainerScreen<?> parent;
    private final Position position;

    private LOTRPouchRestockButton(AbstractContainerScreen<?> parent, Position position) {
        super(0, 0, 10, 10, Component.empty());
        this.parent = parent;
        this.position = position;
    }

    public static void init() {
        ScreenEvents.AFTER_INIT.register((minecraft, screen, width, height) -> {
            if (!(screen instanceof AbstractContainerScreen<?> containerScreen) || screen instanceof LOTRPouchScreen
                    || screen instanceof LOTRChestWithPouchScreen || minecraft.player == null) {
                return;
            }
            Position position = screen instanceof LOTRAnvilScreen || screen instanceof AnvilScreen ? Position.LEFT
                    : screen instanceof LOTRBarrelScreen ? Position.SIDE : Position.TOP_RIGHT;
            LOTRPouchRestockButton button = new LOTRPouchRestockButton(containerScreen, position);
            if (!button.place()) {
                return;
            }
            Screens.getWidgets(screen).add(button);
            ScreenEvents.beforeExtract(screen).register((s, graphics, mouseX, mouseY, partialTick) -> button.place());
        });
    }

    /** The player's inventory slot at the top right (or top left) of the screen, if it shows one. */
    private @Nullable Slot cornerSlot(boolean left) {
        Inventory inv = Minecraft.getInstance().player.getInventory();
        boolean creative = this.parent instanceof CreativeModeInventoryScreen;
        Slot corner = null;
        for (Slot slot : this.parent.getMenu().slots) {
            int index = slot.getContainerSlot();
            boolean acceptable = creative ? index >= 9 && index < 36 : index < Inventory.INVENTORY_SIZE;
            if (slot.container != inv || !acceptable) {
                continue;
            }
            if (corner == null || slot.y < corner.y || slot.y == corner.y && (left ? slot.x < corner.x : slot.x > corner.x)) {
                corner = slot;
            }
        }
        return corner;
    }

    /** checkPouchRestockEnabled and the button's place; false if the screen shows no inventory. */
    private boolean place() {
        Minecraft mc = Minecraft.getInstance();
        Slot corner = cornerSlot(this.position == Position.LEFT);
        if (corner == null || mc.player == null) {
            this.visible = this.active = false;
            return corner != null;
        }
        int x = switch (this.position) {
            case LEFT -> corner.x - 1;
            case SIDE -> corner.x + 21;
            case TOP_RIGHT -> corner.x + 7;
        };
        int y = this.position == Position.SIDE ? corner.y - 1 : corner.y - 14;
        setX(this.parent.leftPos + x);
        setY(this.parent.topPos + y);
        boolean hasPouch = false;
        for (net.minecraft.world.item.ItemStack stack : mc.player.getInventory().getNonEquipmentItems()) {
            if (LOTRPouchItem.isPouch(stack)) {
                hasPouch = true;
                break;
            }
        }
        if (this.parent instanceof CreativeModeInventoryScreen creative && !creative.isInventoryOpen()) {
            hasPouch = false;
        }
        this.visible = this.active = hasPouch;
        return true;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        ClientPlayNetworking.send(new LOTRPouchNetworking.RestockPayload());
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int k = !this.active ? 0 : isHovered() ? 2 : 1;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), 0.0F, 128 + k * 10, this.width, this.height,
                256, 256);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
