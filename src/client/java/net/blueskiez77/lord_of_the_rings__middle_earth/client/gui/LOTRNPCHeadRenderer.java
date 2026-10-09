package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTROverheadHolder;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRBipedRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiMiniquestOffer.renderNPC: the NPC's head alone -- with its hair or hood and its helmet --
 * turned a little towards the pointer, nodding and shaking as it talks.
 */
public class LOTRNPCHeadRenderer extends PictureInPictureRenderer<LOTRNPCHeadRenderer.State> {

    /** Where the neck is in the model, above the feet: the biped's 1.501, scaled as the renderer scales it. */
    private static final float NECK_HEIGHT = 1.501f * LOTRBipedRenderer.PLAYER_SCALE;

    /**
     * The head; its neck {@code neckBelowTop} GUI pixels down from the top of the area it is drawn in,
     * tilted by {@code lookPitch} (degrees) as a whole, {@code scale} GUI pixels to the block.
     */
    public record State(EntityRenderState renderState, float lookPitch, float neckBelowTop,
                        int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea,
                        @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
    }

    /**
     * The render state for the portrait: facing the viewer, turned {@code lookYaw} (degrees) as a
     * whole -- as vanilla's inventory figure turns its body -- its head at {@code headYaw} and
     * {@code headPitch}, with no body armour, held items, cape, shield, speech, name or overhead marks.
     */
    public static @Nullable EntityRenderState extract(LOTRNPCEntity npc, float lookYaw, float headYaw, float headPitch,
                                                      float partialTick) {
        EntityRenderState state = Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(npc, partialTick);
        if (!(state instanceof LOTRNPCRenderState npcState)) {
            return null;
        }
        npcState.bodyRot = 180.0f + lookYaw;
        npcState.yRot = headYaw;
        npcState.xRot = headPitch;
        npcState.chestEquipment = ItemStack.EMPTY;
        npcState.legsEquipment = ItemStack.EMPTY;
        npcState.feetEquipment = ItemStack.EMPTY;
        npcState.rightHandItemState.clear();
        npcState.leftHandItemState.clear();
        npcState.cape = null;
        npcState.speech = null;
        npcState.questMarks = null;
        npcState.nameTag = null;
        npcState.shadowPieces.clear();
        npcState.lightCoords = 15728880;
        if (npcState instanceof LOTROverheadHolder holder) {
            holder.lotr$setOverhead(null);
            holder.lotr$setEntityId(-1);
        }
        return npcState;
    }

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    /** The body, arms and legs hidden while the head is drawn. */
    @Override
    public void prepare(State state, GuiRenderState guiRenderState, FeatureRenderDispatcher dispatcher, int guiScale) {
        EntityRenderer<?, ?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(state.renderState());
        ModelPart[] hidden = renderer instanceof LivingEntityRenderer<?, ?, ?> living
                && living.getModel() instanceof HumanoidModel<?> model
                ? new ModelPart[]{model.body, model.rightArm, model.leftArm, model.rightLeg, model.leftLeg}
                : new ModelPart[0];
        for (ModelPart part : hidden) {
            part.visible = false;
        }
        try {
            super.prepare(state, guiRenderState, dispatcher, guiScale);
        } finally {
            for (ModelPart part : hidden) {
                part.visible = true;
            }
        }
    }

    @Override
    protected void renderToTexture(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        // From the area's middle down to the neck; turned upright and towards the pointer about the
        // neck, as the original turned the head about its pivot; then down to the feet.
        poseStack.translate(0.0f, (state.neckBelowTop() - (state.y1() - state.y0()) / 2.0f) / state.scale(), 0.0f);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.PI).rotateX(state.lookPitch() * Mth.DEG_TO_RAD));
        poseStack.translate(0.0f, -NECK_HEIGHT, 0.0f);
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.submit(state.renderState(), new CameraRenderState(), 0.0, 0.0, 0.0, poseStack, collector);
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0f;
    }

    @Override
    protected String getTextureLabel() {
        return "lotr npc head";
    }
}
