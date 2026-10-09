package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfigFile;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiConfig: the mod's options, as Forge's config screen listed them --
 * a button for each category of {@code lotr.cfg}, and in each its options, a
 * true/false toggle or a number, with the option's note as its tooltip. Done
 * writes them and reloads the config (onConfigChanged's load()). Mod Menu
 * opens it, as Forge's mod list did.
 */
public class LOTRConfigScreen extends Screen {

    private final @Nullable Screen parent;

    public LOTRConfigScreen(@Nullable Screen parent) {
        super(Component.translatable("lotr.gui.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        LOTRConfigFile file = LOTRConfig.file();
        List<String> categories = file == null ? List.of() : file.categoryNames();
        int top = this.height / 6;
        for (int i = 0; i < categories.size(); ++i) {
            String category = categories.get(i);
            addRenderableWidget(Button.builder(categoryName(category),
                            button -> this.minecraft.gui.setScreen(new Category(this, category)))
                    .bounds(this.width / 2 - 100, top + i * 24, 200, 20).build());
        }
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    private static Component categoryName(String category) {
        return Component.translatableWithFallback("lotr.config." + category, category);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, 15, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    /** One category's options. */
    private static final class Category extends Screen {

        private final Screen parent;
        private final String category;
        private final List<Option> options = new ArrayList<>();

        Category(Screen parent, String category) {
            super(categoryName(category));
            this.parent = parent;
            this.category = category;
        }

        @Override
        protected void init() {
            this.options.clear();
            OptionList list = new OptionList(this.minecraft, this.width, this.height - 64, 32);
            LOTRConfigFile file = LOTRConfig.file();
            if (file != null) {
                for (LOTRConfigFile.OptionView view : file.options(this.category)) {
                    Option option = new Option(view, this.font);
                    this.options.add(option);
                    list.add(option);
                }
            }
            addRenderableWidget(list);
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                    .bounds(this.width / 2 - 100, this.height - 28, 200, 20).build());
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            super.extractRenderState(graphics, mouseX, mouseY, partialTick);
            graphics.centeredText(this.font, this.title, this.width / 2, 15, 0xFFFFFFFF);
        }

        /** Done: the new values written, and the config reloaded from them. */
        @Override
        public void onClose() {
            LOTRConfigFile file = LOTRConfig.file();
            if (file != null) {
                boolean changed = false;
                for (Option option : this.options) {
                    changed |= option.apply(file);
                }
                if (changed) {
                    boolean naturalBlocks = LOTRConfig.naturalBlocks;
                    boolean snowyStone = LOTRConfig.snowyStone;
                    file.save();
                    LOTRConfig.load();
                    // The block models read these as the chunks are drawn: draw them again.
                    if ((naturalBlocks != LOTRConfig.naturalBlocks || snowyStone != LOTRConfig.snowyStone)
                            && this.minecraft.level != null) {
                        this.minecraft.levelExtractor.allChanged();
                    }
                }
            }
            this.minecraft.gui.setScreen(this.parent);
        }
    }

    private static final class OptionList extends ContainerObjectSelectionList<Option> {
        OptionList(Minecraft minecraft, int width, int height, int y) {
            super(minecraft, width, height, y, 24);
        }

        void add(Option option) {
            addEntry(option);
        }

        @Override
        public int getRowWidth() {
            return 310;
        }
    }

    /** An option's row: its name, and a toggle or a number box. */
    private static final class Option extends ContainerObjectSelectionList.Entry<Option> {

        private final LOTRConfigFile.OptionView view;
        private final net.minecraft.client.gui.Font font;
        private final AbstractWidget control;
        private boolean bool;

        Option(LOTRConfigFile.OptionView view, net.minecraft.client.gui.Font font) {
            this.view = view;
            this.font = font;
            if (view.type() == 'B') {
                this.bool = Boolean.parseBoolean(view.value());
                this.control = Button.builder(boolText(this.bool), button -> {
                    this.bool = !this.bool;
                    button.setMessage(boolText(this.bool));
                }).bounds(0, 0, 90, 20).build();
            } else {
                EditBox box = new EditBox(font, 0, 0, 88, 18, Component.literal(view.name()));
                box.setValue(view.value());
                this.control = box;
            }
            if (view.comment() != null) {
                this.control.setTooltip(Tooltip.create(Component.literal(view.comment())));
            }
        }

        /** Forge drew a boolean as green "true" or red "false". */
        private static MutableComponent boolText(boolean value) {
            return Component.literal(Boolean.toString(value))
                    .withStyle(value ? ChatFormatting.GREEN : ChatFormatting.RED);
        }

        /** The value set into the file; whether it differs from what was there. */
        boolean apply(LOTRConfigFile file) {
            if (this.view.type() == 'B') {
                if (this.bool == Boolean.parseBoolean(this.view.value())) {
                    return false;
                }
                file.set(this.view.category(), this.view.name(), this.bool);
                return true;
            }
            String text = ((EditBox) this.control).getValue();
            try {
                int value = Integer.parseInt(text);
                if (Integer.toString(value).equals(this.view.value())) {
                    return false;
                }
                file.set(this.view.category(), this.view.name(), value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            int y = getY() + (getHeight() - 8) / 2;
            graphics.text(this.font, Component.literal(this.view.name()), getX(), y, 0xFFFFFFFF);
            this.control.setX(getX() + getWidth() - 92);
            this.control.setY(getY() + (getHeight() - this.control.getHeight()) / 2);
            this.control.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(this.control);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(this.control);
        }
    }
}
