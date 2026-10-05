package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiHiredFarmer: its status, farming mode and range, the company it
 * belongs to, and its inventory.
 */
public class LOTRHiredFarmerScreen extends LOTRHiredNPCScreen {

    private Button buttonGuardMode;
    private GuardRangeSlider sliderGuardRange;
    private EditBox squadronNameField;
    private boolean sendSquadronUpdate;

    public LOTRHiredFarmerScreen(LOTRNPCEntity npc) {
        super(npc);
    }

    @Override
    protected void init() {
        super.init();
        int midX = this.guiLeft + this.xSize / 2;
        this.buttonGuardMode = addRenderableWidget(optionButton("lotr.gui.farmer.mode", midX - 80, this.guiTop + 60,
                b -> sendActionPacket(0)));
        this.sliderGuardRange = addRenderableWidget(new GuardRangeSlider("lotr.gui.farmer.range", 1,
                midX - 80, this.guiTop + 84));
        this.squadronNameField = new EditBox(this.font, midX - 80, this.guiTop + 120, 160, 20,
                Component.translatable("lotr.gui.farmer.squadron"));
        this.squadronNameField.setMaxLength(LOTRCommandHornItem.SQUADRON_LENGTH_MAX);
        String squadron = this.theNPC.hiredNPCInfo.getSquadron();
        if (squadron != null && !squadron.isEmpty()) {
            this.squadronNameField.setValue(squadron);
        }
        this.squadronNameField.setResponder(text -> {
            this.theNPC.hiredNPCInfo.setSquadron(text);
            this.sendSquadronUpdate = true;
        });
        addRenderableWidget(this.squadronNameField);
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.farmer.openInv"), b -> sendActionPacket(2))
                .bounds(midX - 80, this.guiTop + 144, 160, 20).build());
        updateOptions();
    }

    @Override
    public void tick() {
        super.tick();
        updateOptions();
    }

    private void updateOptions() {
        setState(this.buttonGuardMode, "lotr.gui.farmer.mode", this.theNPC.hiredNPCInfo.isGuardMode());
        this.sliderGuardRange.visible = this.theNPC.hiredNPCInfo.isGuardMode();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        centred(graphics, this.theNPC.hiredNPCInfo.getStatusString(), this.guiTop + 48, TEXT_COLOUR);
        graphics.text(this.font, Component.translatable("lotr.gui.farmer.squadron"), this.squadronNameField.getX(),
                this.squadronNameField.getY() - this.font.lineHeight - 3, TEXT_COLOUR, false);
    }

    @Override
    public void removed() {
        super.removed();
        if (this.sendSquadronUpdate) {
            String squadron = this.theNPC.hiredNPCInfo.getSquadron();
            ClientPlayNetworking.send(new LOTRHiredPayloads.Squadron(this.theNPC.getId(), squadron == null ? "" : squadron));
        }
    }
}
