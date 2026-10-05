package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRPlayerNPCOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRGuiOptions: the player's own Middle-earth options, each a button that
 * shows its state and flips it on the server; hovering one explains both
 * settings.
 *
 * <p>NOT ported yet: Show Map Location (the map, D13) and Conquest Kills
 * (conquest, D14). Their rows are left where they stood.
 */
public class LOTROptionsScreen extends LOTRMenuBaseScreen {

    private final List<OptionButton> options = new ArrayList<>();

    public LOTROptionsScreen() {
        super(Component.translatable("lotr.gui.options.title"));
    }

    private record OptionButton(Button button, String key) {
    }

    @Override
    protected void init() {
        super.init();
        this.options.clear();
        int buttonX = this.guiLeft + this.xSize / 2 - 100;
        int buttonY = this.guiTop + 40;
        option(LOTRMenuNetworking.FRIENDLY_FIRE, buttonX, buttonY, "lotr.gui.options.friendlyFire");
        option(LOTRMenuNetworking.HIRED_DEATH_MESSAGES, buttonX, buttonY + 24, "lotr.gui.options.hiredDeathMessages");
        option(LOTRMenuNetworking.SHOW_ALIGNMENT, buttonX, buttonY + 48, "lotr.gui.options.showAlignment");
        option(LOTRMenuNetworking.FEM_RANK, buttonX, buttonY + 120, "lotr.gui.options.femRank");
        updateStates();
    }

    private void option(int id, int x, int y, String key) {
        Button button = addRenderableWidget(Button.builder(Component.translatable(key),
                        b -> ClientPlayNetworking.send(new LOTRMenuPayloads.SetOption(id)))
                .bounds(x, y, 200, 20).build());
        this.options.add(new OptionButton(button, key));
    }

    private void updateStates() {
        Player player = this.minecraft.player;
        setState(0, LOTRPlayerNPCOptions.getFriendlyFire(player));
        setState(1, LOTRPlayerNPCOptions.getEnableHiredDeathMessages(player));
        setState(2, !LOTRPlayerAlignments.getHideAlignment(player));
        setState(3, LOTRRankOptions.getFemRankOverride(player));
    }

    private void setState(int i, boolean flag) {
        OptionButton option = this.options.get(i);
        option.button().setMessage(Component.translatable(option.key()).append(": ")
                .append(Component.translatable(flag ? "lotr.gui.button.on" : "lotr.gui.button.off")));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        updateStates();
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        Component s = Component.translatable("lotr.gui.options.title");
        graphics.text(this.font, s, this.guiLeft + 100 - this.font.width(s) / 2, this.guiTop - 30, 0xFFFFFFFF, false);
        s = Component.translatable("lotr.gui.options.worldSettings");
        graphics.text(this.font, s, this.guiLeft + 100 - this.font.width(s) / 2, this.guiTop + 10, 0xFFFFFFFF, false);
        for (OptionButton option : this.options) {
            if (option.button().active && option.button().isHovered()) {
                drawTooltip(graphics, option.key(), mouseX, mouseY);
            }
        }
    }

    /** LOTRGuiButtonOptions.drawTooltip: both settings explained, in a dark box by the mouse. */
    private void drawTooltip(GuiGraphicsExtractor graphics, String key, int i, int j) {
        int border = 3;
        int stringWidth = 200;
        List<FormattedCharSequence> lines = new ArrayList<>(this.font.split(Component.translatable(key + ".desc.on"), stringWidth));
        lines.add(FormattedCharSequence.EMPTY);
        lines.addAll(this.font.split(Component.translatable(key + ".desc.off"), stringWidth));
        int stringHeight = lines.size() * this.font.lineHeight;
        int offset = 10;
        i += offset;
        j += offset;
        graphics.fill(i, j, i + stringWidth + border * 2, j + stringHeight + border * 2, -1073741824);
        int y = j + border;
        for (FormattedCharSequence line : lines) {
            graphics.text(this.font, line, i + border, y, 0xFFFFFFFF, false);
            y += this.font.lineHeight;
        }
    }
}
