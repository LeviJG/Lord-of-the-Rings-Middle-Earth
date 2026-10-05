package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNPCInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitPledgeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

/**
 * LOTRGuiHiredNPC: a hired unit's own screen -- its name and kind, and on the
 * first page what the player must keep to command it. Closing it tells the
 * server (command -1), so the unit is free to move again.
 */
public abstract class LOTRHiredNPCScreen extends Screen {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/hired.png");
    protected static final int TITLE_COLOUR = 0xFF373737;
    protected static final int TEXT_COLOUR = 0xFF404040;

    protected final int xSize = 200;
    protected final int ySize = 220;
    protected int guiLeft;
    protected int guiTop;
    protected final LOTRNPCEntity theNPC;
    protected int page;

    protected LOTRHiredNPCScreen(LOTRNPCEntity npc) {
        super(npc.getDisplayName());
        this.theNPC = npc;
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F,
                this.xSize, this.ySize, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        centred(graphics, Component.literal(this.theNPC.getNPCName()), this.guiTop + 11, TITLE_COLOUR);
        centred(graphics, this.theNPC.getEntityClassName(), this.guiTop + 26, TITLE_COLOUR);
        LOTRHiredNPCInfo info = this.theNPC.hiredNPCInfo;
        if (this.page == 0 && info.hasHiringRequirements()) {
            int x = this.guiLeft + 6;
            int y = this.guiTop + 170;
            graphics.text(this.font, Component.translatable("lotr.hiredNPC.commandReq"), x, y, TITLE_COLOUR, false);
            y += this.font.lineHeight;
            x += 4;
            int maxWidth = this.xSize - 12 - 4;
            LOTRFaction fac = this.theNPC.getHiringFaction();
            String alignS = LOTRAlignmentValues.formatAlignForDisplay(info.alignmentRequiredToCommand);
            List<FormattedCharSequence> lines = new ArrayList<>(this.font.split(
                    Component.translatable("lotr.hiredNPC.commandReq.align", alignS, fac.factionName()), maxWidth));
            if (info.pledgeType != LOTRUnitPledgeType.NONE) {
                lines.addAll(this.font.split(Component.translatable(
                        "lotr.hiredNPC.commandReq.pledge." + info.pledgeType.name(), fac.factionName()), maxWidth));
            }
            for (FormattedCharSequence line : lines) {
                graphics.text(this.font, line, x, y, TITLE_COLOUR, false);
                y += this.font.lineHeight;
            }
        }
    }

    protected void centred(GuiGraphicsExtractor graphics, Component s, int y, int colour) {
        graphics.text(this.font, s, this.guiLeft + this.xSize / 2 - this.font.width(s) / 2, y, colour, false);
    }

    @Override
    public void removed() {
        super.removed();
        sendActionPacket(-1);
    }

    protected void sendActionPacket(int action) {
        sendActionPacket(action, 0);
    }

    protected void sendActionPacket(int action, int value) {
        ClientPlayNetworking.send(new LOTRHiredPayloads.Command(this.theNPC.getId(), this.page, action, value));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.theNPC.isAlive() || this.theNPC.hiredNPCInfo.getHiringPlayer() != this.minecraft.player
                || this.theNPC.distanceToSqr(this.minecraft.player) > 64.0) {
            onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /** LOTRGuiButtonOptions: "Name: ON" or "OFF". */
    protected static Button optionButton(String key, int x, int y, Button.OnPress onPress) {
        return Button.builder(Component.translatable(key), onPress).bounds(x, y, 160, 20).build();
    }

    protected static void setState(Button button, String key, boolean flag) {
        button.setMessage(Component.translatable(key).append(": ")
                .append(Component.translatable(flag ? "lotr.gui.button.on" : "lotr.gui.button.off")));
    }

    /** LOTRGuiSlider for the guard range: "Name: n", the new range sent as it moves. */
    protected final class GuardRangeSlider extends AbstractSliderButton {
        private final String key;
        private final int action;

        GuardRangeSlider(String key, int action, int x, int y) {
            super(x, y, 160, 20, CommonComponents.EMPTY, toSlider(LOTRHiredNPCScreen.this.theNPC.hiredNPCInfo.getGuardRange()));
            this.key = key;
            this.action = action;
            updateMessage();
        }

        int getSliderValue() {
            return LOTRHiredNPCInfo.GUARD_RANGE_MIN
                    + (int) Math.round(this.value * (LOTRHiredNPCInfo.GUARD_RANGE_MAX - LOTRHiredNPCInfo.GUARD_RANGE_MIN));
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.translatable(this.key).append(": " + getSliderValue()));
        }

        @Override
        protected void applyValue() {
            int i = getSliderValue();
            LOTRHiredNPCScreen.this.theNPC.hiredNPCInfo.setGuardRange(i);
            sendActionPacket(this.action, i);
        }
    }

    private static double toSlider(int range) {
        int value = Mth.clamp(range, LOTRHiredNPCInfo.GUARD_RANGE_MIN, LOTRHiredNPCInfo.GUARD_RANGE_MAX);
        return (double) (value - LOTRHiredNPCInfo.GUARD_RANGE_MIN)
                / (LOTRHiredNPCInfo.GUARD_RANGE_MAX - LOTRHiredNPCInfo.GUARD_RANGE_MIN);
    }
}
