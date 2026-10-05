package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRRespawnerPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * LOTRGuiNPCRespawner: a creative player's settings for a respawner -- its
 * two kinds (by entity id, as "lotr:gondor_soldier"), the box it counts them
 * in and its cap, the enemy-blocking range, the box it spawns them in, their
 * home range and mounts, the interval in minutes and seconds, and how near a
 * player stops it -- sent back as it closes; and Destroy.
 */
public class LOTRNPCRespawnerScreen extends Screen {

    private static final int X_SIZE = 256;
    private static final int Y_SIZE = 280;

    private final LOTRNPCRespawnerEntity theSpawner;
    private int guiLeft;
    private int guiTop;
    private EditBox textSpawnClass1;
    private EditBox textSpawnClass2;
    private LOTRIntSlider sliderCheckVerticalMin;
    private LOTRIntSlider sliderCheckVerticalMax;
    private LOTRIntSlider sliderBlockEnemy;
    private LOTRIntSlider sliderSpawnVerticalMin;
    private LOTRIntSlider sliderSpawnVerticalMax;
    private LOTRIntSlider sliderHomeRange;
    private LOTRIntSlider sliderSpawnIntervalM;
    private LOTRIntSlider sliderSpawnIntervalS;
    private Button buttonMounts;
    private boolean destroySpawner;

    public LOTRNPCRespawnerScreen(LOTRNPCRespawnerEntity spawner) {
        super(Component.translatable("lotr.gui.npcRespawner.title"));
        this.theSpawner = spawner;
    }

    private static Component t(String key) {
        return Component.translatable("lotr.gui.npcRespawner." + key);
    }

    @Override
    protected void init() {
        LOTRNPCRespawnerEntity s = this.theSpawner;
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        int mid = this.guiLeft + X_SIZE / 2;
        this.textSpawnClass1 = addRenderableWidget(new EditBox(this.font, mid - 190, this.guiTop + 35, 180, 20, t("spawnClass1")));
        this.textSpawnClass1.setMaxLength(100);
        if (s.spawnClass1 != null) {
            this.textSpawnClass1.setValue(BuiltInRegistries.ENTITY_TYPE.getKey(s.spawnClass1).toString());
        }
        this.textSpawnClass2 = addRenderableWidget(new EditBox(this.font, mid + 10, this.guiTop + 35, 180, 20, t("spawnClass2")));
        this.textSpawnClass2.setMaxLength(100);
        if (s.spawnClass2 != null) {
            this.textSpawnClass2.setValue(BuiltInRegistries.ENTITY_TYPE.getKey(s.spawnClass2).toString());
        }
        addRenderableWidget(new LOTRIntSlider(mid - 180, this.guiTop + 70, 160, 20, t("checkHorizontal"), 0, 64,
                s.checkHorizontalRange, v -> s.checkHorizontalRange = v));
        this.sliderCheckVerticalMin = addRenderableWidget(new LOTRIntSlider(mid - 180, this.guiTop + 95, 160, 20,
                t("checkVerticalMin"), -64, 64, s.checkVerticalMin, v -> {
                    s.checkVerticalMin = v;
                    if (s.checkVerticalMax < v) {
                        s.checkVerticalMax = v;
                        this.sliderCheckVerticalMax.setSliderValue(v);
                    }
                }));
        this.sliderCheckVerticalMax = addRenderableWidget(new LOTRIntSlider(mid - 180, this.guiTop + 120, 160, 20,
                t("checkVerticalMax"), -64, 64, s.checkVerticalMax, v -> {
                    s.checkVerticalMax = v;
                    if (s.checkVerticalMin > v) {
                        s.checkVerticalMin = v;
                        this.sliderCheckVerticalMin.setSliderValue(v);
                    }
                }));
        addRenderableWidget(new LOTRIntSlider(mid - 180, this.guiTop + 145, 160, 20, t("spawnCap"), 0, 64, s.spawnCap,
                v -> s.spawnCap = v));
        this.sliderBlockEnemy = addRenderableWidget(new LOTRIntSlider(mid - 180, this.guiTop + 170, 160, 20, t("blockEnemy"),
                0, 64, s.blockEnemySpawns, v -> s.blockEnemySpawns = v));
        addRenderableWidget(new LOTRIntSlider(mid + 20, this.guiTop + 70, 160, 20, t("spawnHorizontal"), 0, 64,
                s.spawnHorizontalRange, v -> s.spawnHorizontalRange = v));
        this.sliderSpawnVerticalMin = addRenderableWidget(new LOTRIntSlider(mid + 20, this.guiTop + 95, 160, 20,
                t("spawnVerticalMin"), -64, 64, s.spawnVerticalMin, v -> {
                    s.spawnVerticalMin = v;
                    if (s.spawnVerticalMax < v) {
                        s.spawnVerticalMax = v;
                        this.sliderSpawnVerticalMax.setSliderValue(v);
                    }
                }));
        this.sliderSpawnVerticalMax = addRenderableWidget(new LOTRIntSlider(mid + 20, this.guiTop + 120, 160, 20,
                t("spawnVerticalMax"), -64, 64, s.spawnVerticalMax, v -> {
                    s.spawnVerticalMax = v;
                    if (s.spawnVerticalMin > v) {
                        s.spawnVerticalMin = v;
                        this.sliderSpawnVerticalMin.setSliderValue(v);
                    }
                }));
        this.sliderHomeRange = addRenderableWidget(new LOTRIntSlider(mid + 20, this.guiTop + 145, 160, 20, t("homeRange"),
                -1, 64, s.homeRange, v -> s.homeRange = v));
        this.buttonMounts = addRenderableWidget(Button.builder(t("mounts"), b -> s.toggleMountSetting())
                .bounds(mid + 20, this.guiTop + 170, 160, 20).build());
        this.sliderSpawnIntervalM = addRenderableWidget(new LOTRIntSlider(mid - 100 - 5, this.guiTop + 195, 100, 20,
                t("spawnIntervalM"), 0, 60, s.spawnInterval / 20 / 60, v -> updateInterval()).setValueOnly());
        this.sliderSpawnIntervalS = addRenderableWidget(new LOTRIntSlider(mid + 5, this.guiTop + 195, 100, 20,
                t("spawnIntervalS"), 0, 59, s.spawnInterval / 20 % 60, v -> updateInterval()).setValueOnly().setNumberDigits(2));
        addRenderableWidget(new LOTRIntSlider(mid - 80, this.guiTop + 220, 160, 20, t("noPlayerRange"), 0, 64,
                s.noPlayerRange, v -> s.noPlayerRange = v));
        addRenderableWidget(Button.builder(t("destroy"), b -> {
            this.destroySpawner = true;
            onClose();
        }).bounds(mid - 50, this.guiTop + 255, 100, 20).build());
        updateStates();
    }

    /** At least a second between spawns. */
    private void updateInterval() {
        if (this.sliderSpawnIntervalM.getSliderValue() == 0 && this.sliderSpawnIntervalS.getSliderValue() < 1) {
            this.sliderSpawnIntervalS.setSliderValue(1);
        }
        this.theSpawner.spawnInterval = (this.sliderSpawnIntervalM.getSliderValue() * 60
                + this.sliderSpawnIntervalS.getSliderValue()) * 20;
    }

    private void updateStates() {
        LOTRNPCRespawnerEntity s = this.theSpawner;
        int mount = Math.min(s.mountSetting, 2);
        this.buttonMounts.setMessage(t("mounts").copy().append(": ").append(t("mounts." + mount)));
        this.sliderBlockEnemy.setOverrideStateString(s.blockEnemySpawns() ? null : t("blockEnemy.off"));
        this.sliderHomeRange.setOverrideStateString(s.hasHomeRange() ? null : t("homeRange.off"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        updateStates();
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.text(this.font, this.title, this.guiLeft + X_SIZE / 2 - this.font.width(this.title) / 2, this.guiTop,
                0xFFFFFFFF, false);
        graphics.text(this.font, t("spawnClass1"), this.textSpawnClass1.getX() + 3,
                this.textSpawnClass1.getY() - this.font.lineHeight - 3, 0xFFCCCCCC, false);
        graphics.text(this.font, t("spawnClass2"), this.textSpawnClass2.getX() + 3,
                this.textSpawnClass2.getY() - this.font.lineHeight - 3, 0xFFCCCCCC, false);
        Component timepre = t("spawnInterval");
        int timeY = this.sliderSpawnIntervalM.getY() + this.sliderSpawnIntervalM.getHeight() / 2 - this.font.lineHeight / 2;
        graphics.text(this.font, timepre, this.sliderSpawnIntervalM.getX() - 5 - this.font.width(timepre), timeY, 0xFFFFFFFF, false);
        int timesplitX = (this.sliderSpawnIntervalM.getX() + this.sliderSpawnIntervalM.getWidth()
                + this.sliderSpawnIntervalS.getX()) / 2 - this.font.width(":") / 2;
        graphics.text(this.font, ":", timesplitX, timeY, 0xFFFFFFFF, false);
        if (this.sliderBlockEnemy.active && this.sliderBlockEnemy.isHovered()) {
            Component tooltip = t("blockEnemy.tooltip");
            int border = 3;
            int i = mouseX + 10;
            int j = mouseY + 10;
            graphics.fill(i, j, i + this.font.width(tooltip) + border * 2, j + this.font.lineHeight + border * 2, -1073741824);
            graphics.text(this.font, tooltip, i + border, j + border, 0xFFFFFFFF, false);
        }
    }

    /** keyTyped: the inventory key closes it too, as LOTRGuiScreenBase's did. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.textSpawnClass1.isFocused() || this.textSpawnClass2.isFocused()) {
            return super.keyPressed(event);
        }
        return LOTRMenuBaseScreen.closeOnInventoryKey(this, event) || super.keyPressed(event);
    }

    /** onGuiClosed: sendSpawnerData. */
    @Override
    public void removed() {
        super.removed();
        LOTRNPCRespawnerEntity s = this.theSpawner;
        var type1 = typeFrom(this.textSpawnClass1.getValue());
        var type2 = typeFrom(this.textSpawnClass2.getValue());
        if (type1 != null) {
            s.spawnClass1 = type1;
        }
        if (type2 != null) {
            s.spawnClass2 = type2;
        }
        ClientPlayNetworking.send(new LOTRRespawnerPayloads.Edit(s.getId(), s.writeSpawnerData(), this.destroySpawner));
    }

    private static net.minecraft.world.entity.@org.jspecify.annotations.Nullable EntityType<?> typeFrom(String text) {
        if (text.isEmpty()) {
            return null;
        }
        Identifier id = Identifier.tryParse(text.contains(":") ? text : "lotr:" + text);
        return id == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
