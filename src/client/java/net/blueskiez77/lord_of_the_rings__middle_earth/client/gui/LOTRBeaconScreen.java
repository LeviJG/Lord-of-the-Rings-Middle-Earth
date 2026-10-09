package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBeaconEditPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

// LOTRGuiBeacon. Both text fields, because beacon.png has a slot for each.
//
// Geometry is the original's: 200x160, lotr:gui/beacon.png, title centred at
// y+11, fellowship field 160x20 at y+45, beacon name field 160x20 at y+100,
// Done button 80x20 at y+130. Labels sit one and two font-heights above their
// fields, as in the original's drawScreen.
//
// The fellowship is typed by name, shown by the name of the one it is given
// to, and sent as the id of the player's fellowship of that name (or none).
public class LOTRBeaconScreen extends Screen {

    private static final Identifier BEACON_GUI =
            Identifier.fromNamespaceAndPath("lotr", "gui/beacon.png");

    // 1.7.10 passed 4210752 (0x404040) to drawString, which had no alpha
    // channel. Modern colours are ARGB, so a bare 0x404040 is fully
    // transparent -- this is the same value vanilla's own extractLabels uses,
    // written as -12566464 there.
    private static final int LABEL_COLOUR = 0xFF404040;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 160;

    private final BlockPos beaconPos;
    private final String initialName;
    private final String initialFellowship;

    private int guiLeft;
    private int guiTop;
    private EditBox fellowshipNameField;
    private EditBox beaconNameField;

    public LOTRBeaconScreen(BlockPos pos, Component title, String initialName, java.util.@org.jspecify.annotations.Nullable UUID fellowshipID) {
        super(title);
        this.beaconPos = pos;
        this.initialName = initialName == null ? "" : initialName;
        var fellowship = net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship.LOTRClientFellowships.getByID(fellowshipID);
        this.initialFellowship = fellowship == null ? "" : fellowship.name();
    }

    @Override
    protected void init() {
        guiLeft = (width - WIDTH) / 2;
        guiTop = (height - HEIGHT) / 2;

        fellowshipNameField = new EditBox(font, guiLeft + WIDTH / 2 - 80, guiTop + 45, 160, 20,
                Component.translatable("container.lotr.beacon.nameFellowship"));
        fellowshipNameField.setMaxLength(LOTRBeaconEditPayload.MAX_NAME_LENGTH);
        fellowshipNameField.setValue(initialFellowship);
        addRenderableWidget(fellowshipNameField);

        beaconNameField = new EditBox(font, guiLeft + WIDTH / 2 - 80, guiTop + 100, 160, 20,
                Component.translatable("container.lotr.beacon.nameBeacon"));
        beaconNameField.setMaxLength(LOTRBeaconEditPayload.MAX_NAME_LENGTH);
        beaconNameField.setValue(initialName);
        addRenderableWidget(beaconNameField);

        setInitialFocus(fellowshipNameField);

        addRenderableWidget(Button.builder(
                        Component.translatable("container.lotr.beacon.done"),
                        button -> onClose())
                .bounds(guiLeft + WIDTH / 2 - 40, guiTop + 130, 80, 20)
                .build());
    }

    // Screen splits this in two: extractBackground draws the dimmed/blurred
    // backdrop, then extractRenderState draws the widgets. The panel and its
    // labels belong under the widgets, so they go at the top of this one --
    // there is no renderBackground any more.
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BEACON_GUI,
                guiLeft, guiTop, 0.0F, 0.0F, WIDTH, HEIGHT, 256, 256);

        // The original drew the block's display name centred at guiTop + 11.
        graphics.text(font, title, guiLeft + WIDTH / 2 - font.width(title) / 2,
                guiTop + 11, LABEL_COLOUR, false);

        // "Assign fellowship:" one line above the upper field.
        graphics.text(font, Component.translatable("container.lotr.beacon.nameFellowship"),
                fellowshipNameField.getX() + 4,
                fellowshipNameField.getY() - 4 - font.lineHeight, LABEL_COLOUR, false);

        // "Name beacon:" two lines above the lower field, "The beacon of" one.
        graphics.text(font, Component.translatable("container.lotr.beacon.nameBeacon"),
                beaconNameField.getX() + 4,
                beaconNameField.getY() - 4 - font.lineHeight * 2, LABEL_COLOUR, false);
        graphics.text(font, Component.translatable("container.lotr.beacon.namePrefix"),
                beaconNameField.getX() + 4,
                beaconNameField.getY() - 4 - font.lineHeight, LABEL_COLOUR, false);

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    // LOTRGuiBeacon.updateScreen: close if the player walks away or the block
    // entity stops existing.
    @Override
    public void tick() {
        super.tick();
        if (minecraft == null || minecraft.player == null || minecraft.level == null) {
            return;
        }
        double distSq = minecraft.player.distanceToSqr(
                beaconPos.getX() + 0.5, beaconPos.getY() + 0.5, beaconPos.getZ() + 0.5);
        if (distSq > 64.0
                || !(minecraft.level.getBlockEntity(beaconPos) instanceof LOTRBeaconBlockEntity)) {
            onClose();
        }
    }

    // onGuiClosed -> sendBeaconEditPacket(true)
    @Override
    public void onClose() {
        String fsName = fellowshipNameField.getValue();
        var fellowship = fsName.isBlank() ? null
                : net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship.LOTRClientFellowships.getByName(fsName);
        ClientPlayNetworking.send(new LOTRBeaconEditPayload(beaconPos,
                java.util.Optional.ofNullable(fellowship == null ? null : fellowship.fellowshipID()), beaconNameField.getValue()));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}