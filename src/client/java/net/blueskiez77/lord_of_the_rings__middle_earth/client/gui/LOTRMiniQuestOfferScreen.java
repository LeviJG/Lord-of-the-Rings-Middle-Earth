package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBipedRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCQuestInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiMiniquestOffer: an NPC's offer of a mini-quest -- its name, its head talking, its words, and
 * the task with the quest's icon either side -- to accept or decline. Closing it any other way
 * declines; it closes itself if the NPC dies or the player goes more than eight blocks off.
 */
public class LOTRMiniQuestOfferScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/quest/miniquest.png");
    private static final int X_SIZE = 256;
    private static final int Y_SIZE = 200;
    private static final int DESCRIPTION_X = 85;
    private static final int DESCRIPTION_Y = 30;
    private static final int DESCRIPTION_WIDTH = 160;
    private static final int NPC_X = 46;
    private static final int NPC_Y = 90;
    private static final float NPC_SCALE = 70.0f / LOTRBipedRenderer.PLAYER_SCALE;
    private static final int TEXT_COLOUR = 0xFF7A5D43;

    private final LOTRMiniQuest theMiniQuest;
    private final LOTRNPCEntity theNPC;
    private final RandomSource rand;
    private @Nullable String description;
    private int openTick;
    private int guiLeft;
    private int guiTop;
    private boolean sentClosePacket;
    private @Nullable NPCAction npcAction;
    private int actionTick;
    private int actionTime;
    private float actionSlow;
    private float headYaw;
    private float prevHeadYaw;
    private float headPitch;
    private float prevHeadPitch;

    public LOTRMiniQuestOfferScreen(LOTRMiniQuest quest, LOTRNPCEntity npc) {
        super(Component.literal(npc.getNPCName()));
        this.theMiniQuest = quest;
        this.theNPC = npc;
        this.rand = npc.getRandom();
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        addRenderableWidget(new LOTRRedBookButton(this.guiLeft + X_SIZE / 2 - 20 - 80, this.guiTop + Y_SIZE - 30, 80, 20,
                Component.translatable("lotr.gui.miniquestOffer.accept"), () -> respond(true)));
        addRenderableWidget(new LOTRRedBookButton(this.guiLeft + X_SIZE / 2 + 20, this.guiTop + Y_SIZE - 30, 80, 20,
                Component.translatable("lotr.gui.miniquestOffer.decline"), () -> respond(false)));
    }

    private void respond(boolean accept) {
        sendClose(accept);
        onClose();
    }

    private void sendClose(boolean accept) {
        if (!this.sentClosePacket) {
            ClientPlayNetworking.send(new LOTRNPCQuestInfo.OfferResponsePayload(this.theNPC.getId(), accept));
            this.sentClosePacket = true;
        }
    }

    /** onGuiClosed: declined, unless already answered. */
    @Override
    public void removed() {
        super.removed();
        sendClose(false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F, X_SIZE, Y_SIZE, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (this.description == null) {
            this.description = LOTRSpeech.formatSpeech(this.theMiniQuest.quoteStart, this.minecraft.player, null,
                    this.theMiniQuest.getObjectiveInSpeech());
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.guiLeft + X_SIZE / 2, this.guiTop + 8, TEXT_COLOUR);
        extractNPC(graphics, this.guiLeft + NPC_X, this.guiTop + NPC_Y, mouseX, mouseY, partialTick);
        int y = this.guiTop + DESCRIPTION_Y;
        for (FormattedCharSequence line : this.font.split(Component.literal(this.description), DESCRIPTION_WIDTH)) {
            graphics.text(this.font, line, this.guiLeft + DESCRIPTION_X, y, TEXT_COLOUR, false);
            y += this.font.lineHeight;
        }
        List<FormattedCharSequence> objectiveLines = this.font.split(this.theMiniQuest.getQuestObjective(), X_SIZE - 64);
        int objFirstLineY = this.guiTop + Y_SIZE - 50;
        int objY = objFirstLineY;
        for (FormattedCharSequence line : objectiveLines) {
            graphics.centeredText(this.font, line, this.guiLeft + X_SIZE / 2, objY, TEXT_COLOUR);
            objY += this.font.lineHeight;
        }
        if (!objectiveLines.isEmpty()) {
            int objFirstLineWidth = this.font.width(objectiveLines.getFirst());
            int iconW = 16;
            int iconB = 6;
            int iconY = objFirstLineY + this.font.lineHeight / 2 - iconW / 2;
            ItemStack icon = this.theMiniQuest.getQuestIcon();
            graphics.item(icon, this.guiLeft + X_SIZE / 2 - objFirstLineWidth / 2 - iconW - iconB, iconY);
            graphics.item(icon, this.guiLeft + X_SIZE / 2 + objFirstLineWidth / 2 + iconB, iconY);
        }
    }

    /** renderNPC: the head, turned towards the pointer by the arctangent of its distance over 40, twenty times over. */
    private void extractNPC(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        float yaw = Mth.lerp(partialTick, this.prevHeadYaw, this.headYaw) * Mth.RAD_TO_DEG;
        float pitch = Mth.lerp(partialTick, this.prevHeadPitch, this.headPitch) * Mth.RAD_TO_DEG;
        float lookYaw = (float) Math.atan((x - mouseX) / 40.0f) * 20.0f;
        float lookPitch = (float) Math.atan((y - mouseY) / 40.0f) * 20.0f;
        EntityRenderState state = LOTRNPCHeadRenderer.extract(this.theNPC, lookYaw, yaw, pitch, partialTick);
        if (state == null) {
            return;
        }
        int x0 = x - 50;
        int x1 = x + 50;
        int y0 = y - 70;
        int y1 = y + 30;
        graphics.guiRenderState.addPicturesInPictureState(new LOTRNPCHeadRenderer.State(state, lookPitch, y - y0,
                x0, y0, x1, y1, NPC_SCALE, graphics.scissorStack.peek(),
                PictureInPictureRenderState.getBounds(x0, y0, x1, y1, graphics.scissorStack.peek())));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.theNPC.isAlive() || this.minecraft.player.distanceTo(this.theNPC) > 8.0f) {
            onClose();
            return;
        }
        this.prevHeadYaw = this.headYaw;
        this.prevHeadPitch = this.headPitch;
        if (this.npcAction == null) {
            if (this.openTick < 100) {
                this.npcAction = NPCAction.TALKING;
                this.actionTime = 100;
                this.actionSlow = 1.0f;
            } else if (this.rand.nextInt(200) == 0) {
                this.npcAction = NPCAction.getRandomAction(this.rand);
                this.actionTime = switch (this.npcAction) {
                    case TALKING -> 40 + this.rand.nextInt(60);
                    case LOOKING -> 60 + this.rand.nextInt(60);
                    case SHAKING -> 100 + this.rand.nextInt(60);
                    case LOOKING_UP -> 30 + this.rand.nextInt(50);
                };
                this.actionSlow = 1.0f;
            }
        } else {
            ++this.actionTick;
        }
        if (this.npcAction != null) {
            if (this.actionTick >= this.actionTime) {
                this.npcAction = null;
                this.actionTick = 0;
                this.actionTime = 0;
            } else if (this.npcAction == NPCAction.TALKING) {
                if (this.actionTick % 20 == 0) {
                    this.actionSlow = 0.7f + this.rand.nextFloat() * 1.5f;
                }
                float slow = this.actionSlow * 2.0f;
                this.headYaw = Mth.sin(this.actionTick / slow) * 0.17453292519943295f;
                this.headPitch = (Mth.sin(this.actionTick / slow * 2.0f) + 1.0f) / 2.0f * -0.3490658503988659f;
            } else if (this.npcAction == NPCAction.SHAKING) {
                this.actionSlow += 0.01f;
                this.headYaw = Mth.sin(this.actionTick / this.actionSlow) * 0.5235987755982988f;
                this.headPitch += 0.006981317007977318f;
            } else if (this.npcAction == NPCAction.LOOKING) {
                float slow = this.actionSlow * 16.0f;
                this.headYaw = Mth.sin(this.actionTick / slow) * 1.0471975511965976f;
                this.headPitch = (Mth.sin(this.actionTick / slow * 2.0f) + 1.0f) / 2.0f * -0.2617993877991494f;
            } else {
                this.headYaw = 0.0f;
                this.headPitch = -0.3490658503988659f;
            }
        } else {
            this.headYaw = 0.0f;
            this.headPitch = Mth.sin(this.openTick * 0.07f) * 0.08726646259971647f;
        }
        ++this.openTick;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private enum NPCAction {
        TALKING(1.0f), SHAKING(0.1f), LOOKING(0.3f), LOOKING_UP(0.4f);

        private static final float TOTAL_WEIGHT = 1.8f;

        private final float weight;

        NPCAction(float f) {
            this.weight = f;
        }

        static NPCAction getRandomAction(RandomSource rand) {
            float f = rand.nextFloat() * TOTAL_WEIGHT;
            for (NPCAction action : values()) {
                f -= action.weight;
                if (f <= 0.0f) {
                    return action;
                }
            }
            return LOOKING_UP;
        }
    }
}
