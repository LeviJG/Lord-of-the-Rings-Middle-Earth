package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNPCInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiHiredWarrior: two pages, turned with the arrows either side. The
 * overview -- health, status, combat level with its experience bar, kills,
 * and the unit's inventory; the options -- guard mode and its range,
 * teleporting to the player, and the company it belongs to.
 */
public class LOTRHiredWarriorScreen extends LOTRHiredNPCScreen {

    private static final String[] PAGE_TITLES = {"overview", "options"};

    private @Nullable Button buttonTeleport;
    private @Nullable Button buttonGuardMode;
    private @Nullable GuardRangeSlider sliderGuardRange;
    private @Nullable EditBox squadronNameField;
    private boolean sendSquadronUpdate;

    public LOTRHiredWarriorScreen(LOTRNPCEntity npc) {
        super(npc);
    }

    @Override
    protected void init() {
        super.init();
        int midX = this.guiLeft + this.xSize / 2;
        this.buttonTeleport = null;
        this.buttonGuardMode = null;
        this.sliderGuardRange = null;
        this.squadronNameField = null;
        if (this.page == 0) {
            addRenderableWidget(Button.builder(Component.translatable("lotr.gui.warrior.openInv"), b -> sendActionPacket(0))
                    .bounds(midX - 80, this.guiTop + 142, 160, 20).build());
        } else if (this.page == 1) {
            this.buttonTeleport = addRenderableWidget(optionButton("lotr.gui.warrior.teleport", midX - 80, this.guiTop + 180,
                    b -> sendActionPacket(0)));
            this.buttonGuardMode = addRenderableWidget(optionButton("lotr.gui.warrior.guardMode", midX - 80, this.guiTop + 50,
                    b -> sendActionPacket(1)));
            this.sliderGuardRange = addRenderableWidget(new GuardRangeSlider("lotr.gui.warrior.guardRange", 2,
                    midX - 80, this.guiTop + 74));
            this.squadronNameField = new EditBox(this.font, midX - 80, this.guiTop + 130, 160, 20,
                    Component.translatable("lotr.gui.warrior.squadron"));
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
            updateOptions();
        }
        int left = this.page == 0 ? PAGE_TITLES.length - 1 : this.page - 1;
        int right = this.page == PAGE_TITLES.length - 1 ? 0 : this.page + 1;
        addRenderableWidget(new LOTRLeftRightButton(true, this.guiLeft - 160, this.guiTop + 50,
                Component.translatable("lotr.gui.warrior." + PAGE_TITLES[left]), () -> turnTo(left)));
        addRenderableWidget(new LOTRLeftRightButton(false, this.guiLeft + this.xSize + 40, this.guiTop + 50,
                Component.translatable("lotr.gui.warrior." + PAGE_TITLES[right]), () -> turnTo(right)));
    }

    @Override
    public void tick() {
        super.tick();
        updateOptions();
    }

    private void updateOptions() {
        LOTRHiredNPCInfo info = this.theNPC.hiredNPCInfo;
        if (this.buttonTeleport != null) {
            setState(this.buttonTeleport, "lotr.gui.warrior.teleport", info.teleportAutomatically);
            this.buttonTeleport.active = !info.isGuardMode();
            setState(this.buttonGuardMode, "lotr.gui.warrior.guardMode", info.isGuardMode());
            this.sliderGuardRange.visible = info.isGuardMode();
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        LOTRHiredNPCInfo info = this.theNPC.hiredNPCInfo;
        if (this.page == 0) {
            int midX = this.guiLeft + this.xSize / 2;
            centred(graphics, Component.translatable("lotr.gui.warrior.health", Math.round(this.theNPC.getHealth()),
                    Math.round(this.theNPC.getMaxHealth())), this.guiTop + 50, TEXT_COLOUR);
            centred(graphics, info.getStatusString(), this.guiTop + 62, TEXT_COLOUR);
            centred(graphics, Component.translatable("lotr.gui.warrior.level", info.xpLevel), this.guiTop + 80, TEXT_COLOUR);
            float lvlProgress = info.getProgressToNextLevel();
            Component curLevel = Component.literal(String.valueOf(info.xpLevel)).withStyle(ChatFormatting.BOLD);
            Component nextLevel = Component.literal(String.valueOf(info.xpLevel + 1)).withStyle(ChatFormatting.BOLD);
            String xpCurLevel = String.valueOf(LOTRHiredNPCInfo.totalXPForLevel(info.xpLevel));
            String xpNextLevel = String.valueOf(LOTRHiredNPCInfo.totalXPForLevel(info.xpLevel + 1));
            graphics.fill(midX - 36, this.guiTop + 96, midX + 36, this.guiTop + 102, -16777216);
            graphics.fill(midX - 35, this.guiTop + 97, midX + 35, this.guiTop + 101, -10658467);
            graphics.fill(midX - 35, this.guiTop + 97, midX - 35 + (int) (lvlProgress * 70.0f), this.guiTop + 101, -43776);
            float scale = 0.67f;
            graphics.pose().pushMatrix();
            graphics.pose().scale(scale, scale);
            graphics.text(this.font, curLevel, Math.round((midX - 38 - this.font.width(curLevel) * scale) / scale),
                    (int) ((this.guiTop + 94) / scale), TEXT_COLOUR, false);
            graphics.text(this.font, nextLevel, Math.round((midX + 38) / scale), (int) ((this.guiTop + 94) / scale),
                    TEXT_COLOUR, false);
            graphics.text(this.font, xpCurLevel, Math.round((midX - 38 - this.font.width(xpCurLevel) * scale) / scale),
                    (int) ((this.guiTop + 101) / scale), TEXT_COLOUR, false);
            graphics.text(this.font, xpNextLevel, Math.round((midX + 38) / scale), (int) ((this.guiTop + 101) / scale),
                    TEXT_COLOUR, false);
            graphics.pose().popMatrix();
            centred(graphics, Component.translatable("lotr.gui.warrior.xp", info.xp), this.guiTop + 110, TEXT_COLOUR);
            centred(graphics, Component.translatable("lotr.gui.warrior.kills", info.mobKills), this.guiTop + 122, TEXT_COLOUR);
        }
        if (this.page == 1 && this.squadronNameField != null) {
            graphics.text(this.font, Component.translatable("lotr.gui.warrior.squadron"), this.squadronNameField.getX(),
                    this.squadronNameField.getY() - this.font.lineHeight - 3, TEXT_COLOUR, false);
        }
    }

    @Override
    public void removed() {
        super.removed();
        if (this.sendSquadronUpdate) {
            String squadron = this.theNPC.hiredNPCInfo.getSquadron();
            ClientPlayNetworking.send(new LOTRHiredPayloads.Squadron(this.theNPC.getId(), squadron == null ? "" : squadron));
        }
    }

    private void turnTo(int page) {
        this.page = page;
        rebuildWidgets();
    }
}
