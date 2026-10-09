package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTitleShieldNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRPlayerShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiShields: the shields one by one -- alignment, achievable and exclusive, a category at a
 * time, those hidden from the player left out -- shown on a figure of the player that may be dragged
 * round, with the shield's name and how it is won; "Select Shield" bears it, "Remove Shield" takes
 * off the one borne.
 */
public class LOTRShieldsScreen extends LOTRMenuBaseScreen {

    private static final Identifier WIDGETS = Identifier.fromNamespaceAndPath("lotr", "gui/widgets.png");
    private static final float MODEL_SCALE = 55.0f;
    private static int currentShieldTypeID;
    private static int currentShieldID;

    private int modelX;
    private int modelY;
    private float modelRotation = -140.0f;
    private float modelRotationPrev = -140.0f;
    private int isMouseDown;
    private int mouseX;
    private int mouseY;
    private int prevMouseX;
    private LOTRShields.ShieldType currentShieldType;
    private LOTRShields currentShield;
    private Model.Simple wideModel;
    private Model.Simple slimModel;
    private ArrowButton shieldLeft;
    private Button shieldSelect;
    private ArrowButton shieldRight;
    private Button shieldRemove;
    private Button changeCategory;

    public LOTRShieldsScreen() {
        super(Component.translatable("lotr.gui.shields.title"));
    }

    @Override
    protected void init() {
        super.init();
        this.wideModel = new Model.Simple(this.minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER), RenderTypes::entityTranslucent);
        this.slimModel = new Model.Simple(this.minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), RenderTypes::entityTranslucent);
        this.modelX = this.guiLeft + this.xSize / 2;
        this.modelY = this.guiTop + 40;
        int midX = this.guiLeft + this.xSize / 2;
        this.shieldLeft = addRenderableWidget(new ArrowButton(true, midX - 64, this.guiTop + 207, () -> updateCurrentShield(-1, 0)));
        this.shieldSelect = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.shields.select"), b -> {
            updateCurrentShield(0, 0);
            ClientPlayNetworking.send(new LOTRTitleShieldNetworking.SelectShield(Optional.of(this.currentShield.name())));
        }).bounds(midX - 40, this.guiTop + 195, 80, 20).build());
        this.shieldRight = addRenderableWidget(new ArrowButton(false, midX + 44, this.guiTop + 207, () -> updateCurrentShield(1, 0)));
        this.shieldRemove = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.shields.remove"), b -> {
            updateCurrentShield(0, 0);
            ClientPlayNetworking.send(new LOTRTitleShieldNetworking.SelectShield(Optional.empty()));
        }).bounds(midX - 40, this.guiTop + 219, 80, 20).build());
        this.changeCategory = addRenderableWidget(Button.builder(Component.empty(), b -> updateCurrentShield(0, 1))
                .bounds(midX - 80, this.guiTop + 250, 160, 20).build());
        LOTRShields equipped = getPlayerEquippedShield();
        if (equipped != null) {
            currentShieldTypeID = equipped.shieldType.ordinal();
            currentShieldID = equipped.shieldID;
        }
        updateCurrentShield(0, 0);
    }

    private @Nullable LOTRShields getPlayerEquippedShield() {
        return LOTRPlayerShields.getShield(this.minecraft.player);
    }

    private boolean canGoLeft() {
        for (int i = 0; i <= currentShieldID - 1; ++i) {
            if (this.currentShieldType.list.get(i).canDisplay(this.minecraft.player)) {
                return true;
            }
        }
        return false;
    }

    private boolean canGoRight() {
        for (int i = currentShieldID + 1; i <= this.currentShieldType.list.size() - 1; ++i) {
            if (this.currentShieldType.list.get(i).canDisplay(this.minecraft.player)) {
                return true;
            }
        }
        return false;
    }

    private void updateCurrentShield(int shield, int type) {
        if (shield != 0) {
            currentShieldID = Mth.clamp(currentShieldID + shield, 0, this.currentShieldType.list.size() - 1);
        }
        if (type != 0) {
            currentShieldTypeID += type;
            if (currentShieldTypeID > LOTRShields.ShieldType.values().length - 1) {
                currentShieldTypeID = 0;
            }
            if (currentShieldTypeID < 0) {
                currentShieldTypeID = LOTRShields.ShieldType.values().length - 1;
            }
            currentShieldID = 0;
        }
        this.currentShieldType = LOTRShields.ShieldType.values()[currentShieldTypeID];
        this.currentShield = this.currentShieldType.list.get(currentShieldID);
        while (!this.currentShield.canDisplay(this.minecraft.player)) {
            if ((shield < 0 || type != 0) && canGoLeft()) {
                updateCurrentShield(-1, 0);
                continue;
            }
            if ((shield > 0 || type != 0) && canGoRight()) {
                updateCurrentShield(1, 0);
                continue;
            }
            updateCurrentShield(0, 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.modelRotationPrev = Mth.wrapDegrees(this.modelRotation);
        this.modelRotation = Mth.wrapDegrees(this.modelRotation);
        boolean mouseWithinModel = Math.abs(this.mouseX - this.modelX) <= 60 && Math.abs(this.mouseY - this.modelY) <= 80;
        if (this.minecraft.mouseHandler.isLeftPressed()) {
            if (this.isMouseDown == 0 || this.isMouseDown == 1) {
                if (this.isMouseDown == 0) {
                    if (mouseWithinModel) {
                        this.isMouseDown = 1;
                    }
                } else if (this.mouseX != this.prevMouseX) {
                    this.modelRotation += -(this.mouseX - this.prevMouseX) * 1.0f;
                }
                this.prevMouseX = this.mouseX;
            }
        } else {
            this.isMouseDown = 0;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        LOTRShields equipped = getPlayerEquippedShield();
        this.shieldLeft.active = canGoLeft();
        this.shieldSelect.active = this.currentShield.canPlayerWear(this.minecraft.player);
        this.shieldSelect.setMessage(Component.translatable(equipped == this.currentShield
                ? "lotr.gui.shields.selected" : "lotr.gui.shields.select"));
        this.shieldRight.active = canGoRight();
        this.shieldRemove.active = equipped != null && equipped == this.currentShield;
        this.changeCategory.setMessage(this.currentShieldType.getDisplayName());
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int midX = this.guiLeft + this.xSize / 2;
        graphics.centeredText(this.font, this.title, midX, this.guiTop - 30, 0xFFFFFFFF);
        PlayerSkin skin = this.minecraft.player.getSkin();
        Model.Simple model = skin.model() == PlayerModelType.SLIM ? this.slimModel : this.wideModel;
        float rotation = this.modelRotationPrev + Mth.wrapDegrees(this.modelRotation - this.modelRotationPrev)
                * this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        int x0 = this.modelX - 80;
        int x1 = this.modelX + 80;
        int y0 = this.modelY - 60;
        int y1 = this.modelY + 100;
        graphics.guiRenderState.addPicturesInPictureState(new LOTRShieldPreviewRenderer.State(model,
                skin.body().texturePath(), this.currentShield, rotation, this.modelY - y0, x0, y0, x1, y1, MODEL_SCALE,
                graphics.scissorStack.peek(), net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState
                        .getBounds(x0, y0, x1, y1, graphics.scissorStack.peek())));
        int y = this.guiTop + 145;
        graphics.centeredText(this.font, this.currentShield.getShieldName(), midX, y, 0xFFFFFFFF);
        y += this.font.lineHeight * 2;
        List<FormattedCharSequence> desc = this.font.split(this.currentShield.getShieldDesc(), 220);
        for (FormattedCharSequence line : desc) {
            graphics.centeredText(this.font, line, midX, y, 0xFFFFFFFF);
            y += this.font.lineHeight;
        }
    }

    /** LOTRGuiButtonShieldsArrows: the widgets sheet's left or right arrow. */
    private static final class ArrowButton extends AbstractButton {

        private final boolean left;
        private final Runnable onPress;

        ArrowButton(boolean left, int x, int y, Runnable onPress) {
            super(x, y, 20, 20, Component.empty());
            this.left = left;
            this.onPress = onPress;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.onPress.run();
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int k = !this.active ? 0 : isHovered() ? 2 : 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, getX(), getY(), this.left ? 0.0f : 20.0f, 60 + k * 20,
                    this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
