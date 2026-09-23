package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRSignColors;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCarvedSignBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRSignEditPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiEditSign: "Edit sign message", the carved block's face drawn large
 * with the lines laid over it, and a Done button.
 *
 * <p>Keys as the original read them: up and down move between the eight lines
 * (wrapping round), enter moves down, backspace deletes, and typing adds up to
 * fifteen allowed characters. The line being edited blinks between arrows.
 * Closing the screen by any means sends the lines to the server.
 */
public class LOTRCarvedSignEditScreen extends Screen {
    /** The face preview: the original's 93.75 scale on a half-block icon. */
    private static final int FACE_SIZE = 94;

    private final LOTRCarvedSignBlockEntity sign;
    private final String[] lines;
    private final int textColor;
    private final TextureAtlasSprite faceSprite;
    private int editLine;
    private int updateCounter;

    public LOTRCarvedSignEditScreen(LOTRCarvedSignBlockEntity sign) {
        super(Component.translatable("sign.edit"));
        this.sign = sign;
        this.lines = sign.copyText();
        BlockState onBlock = Blocks.STONE.defaultBlockState();
        if (sign.getLevel() != null && sign.getBlockState().hasProperty(LOTRCarvedSignBlock.FACING)) {
            BlockPos behind = sign.getBlockPos().relative(sign.getBlockState().getValue(LOTRCarvedSignBlock.FACING).getOpposite());
            onBlock = sign.getLevel().getBlockState(behind);
        }
        this.textColor = sign.isIthildin() ? 0xFFFFFFFF : LOTRSignColors.contrastColor(onBlock);
        this.faceSprite = net.minecraft.client.Minecraft.getInstance().getModelManager().getBlockStateModelSet()
                .getParticleMaterial(onBlock.isAir() ? Blocks.STONE.defaultBlockState() : onBlock).sprite();
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(width / 2 - 100, height / 4 + 120, 200, 20)
                .build());
    }

    @Override
    public void tick() {
        super.tick();
        ++updateCounter;
        if (sign.isRemoved()) {
            onClose();
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        switch (event.key()) {
            case GLFW.GLFW_KEY_UP -> {
                editLine = (editLine - 1) & (LOTRCarvedSignBlockEntity.NUM_LINES - 1);
                return true;
            }
            case GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                editLine = (editLine + 1) & (LOTRCarvedSignBlockEntity.NUM_LINES - 1);
                return true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!lines[editLine].isEmpty()) {
                    lines[editLine] = lines[editLine].substring(0, lines[editLine].length() - 1);
                }
                return true;
            }
            default -> {
                return super.keyPressed(event);
            }
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (event.isAllowedChatCharacter()
                && lines[editLine].length() < LOTRCarvedSignBlockEntity.MAX_LINE_LENGTH) {
            lines[editLine] = lines[editLine] + event.codepointAsString();
            return true;
        }
        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.centeredText(font, title, width / 2, 40, 0xFFFFFFFF);
        int faceX = width / 2 - FACE_SIZE / 2;
        int faceY = 56;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, faceSprite, faceX, faceY, FACE_SIZE, FACE_SIZE);
        int lineHeight = font.lineHeight + 1;
        int lineBase = faceY + FACE_SIZE / 2 - (LOTRCarvedSignBlockEntity.NUM_LINES * lineHeight) / 2;
        for (int l = 0; l < LOTRCarvedSignBlockEntity.NUM_LINES; ++l) {
            String s = lines[l];
            if (l == editLine && updateCounter / 6 % 2 == 0) {
                s = "> " + s + " <";
            }
            graphics.text(font, s, width / 2 - font.width(s) / 2, lineBase + l * lineHeight, textColor, false);
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    /** onGuiClosed: the lines go to the server. */
    @Override
    public void onClose() {
        ClientPlayNetworking.send(new LOTRSignEditPayload(sign.getBlockPos(), List.of(lines)));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
